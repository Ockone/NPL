package summary;

import java.util.*;
import ictclas.NlpirMethod;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

/**
 * TextRank 自动摘要
 *
 * @author hankcs
 */
public class TextRankSentence
{
    /**
     * 阻尼系数（ＤａｍｐｉｎｇＦａｃｔｏｒ），一般取值为0.85
     */
    final static double d = 0.85;
    /**
     * 最大迭代次数
     */
    final static int max_iter = 200;
    final static double min_diff = 0.001;

    final static String default_sentence_separator = "[，,。:：“”？?！!；;]";
    /**
     * 文档句子的个数
     */
    int D;
    /**
     * 拆分为[句子[单词]]形式的文档
     */
    List<List<String>> docs;
    /**
     * 排序后的最终结果 score <-> index
     */
    TreeMap<Double, Integer> top;

    /**
     * 句子和其他句子的相关程度
     */
    double[][] weight;
    /**
     * 该句子和其他句子相关程度之和
     */
    double[] weight_sum;
    /**
     * 迭代之后收敛的权重
     */
    double[] vertex;

    /**
     * BM25相似度
     */
    BM25 bm25;

    public TextRankSentence(List<List<String>> docs)
    {
        this.docs = docs;
        bm25 = new BM25(docs);
        D = docs.size();
        weight = new double[D][D];
        weight_sum = new double[D];
        vertex = new double[D];
        top = new TreeMap<Double, Integer>(Collections.reverseOrder());
        solve();
    }

    private void solve()
    {
        int cnt = 0;
        for (List<String> sentence : docs)
        {
            double[] scores = bm25.simAll(sentence);
//            System.out.println(Arrays.toString(scores));
            weight[cnt] = scores;
            weight_sum[cnt] = sum(scores) - scores[cnt]; // 减掉自己，自己跟自己肯定最相似
            vertex[cnt] = 1.0;
            ++cnt;
        }
        for (int buffer = 0; buffer < max_iter; ++buffer)
        {
            double[] m = new double[D];
            double max_diff = 0;
            for (int i = 0; i < D; ++i)
            {
                m[i] = 1 - d;
                for (int j = 0; j < D; ++j)
                {
                    if (j == i || weight_sum[j] == 0) continue;
                    m[i] += (d * weight[j][i] / weight_sum[j] * vertex[j]);
                }
                double diff = Math.abs(m[i] - vertex[i]);
                if (diff > max_diff)
                {
                    max_diff = diff;
                }
            }
            vertex = m;
            if (max_diff <= min_diff) break;
        }
        // 我们来排个序吧
        for (int i = 0; i < D; ++i)
        {
            top.put(vertex[i], i); // TreeMap<Double, Integer>(Collections.reverseOrder())该数据类型自动降序
        }
    }

    /**
     * 获取前几个关键句子
     *
     * @param size 要几个
     * @return 关键句子的下标
     */
    public int[] getTopSentence(int size)
    {
        Collection<Integer> values = top.values();
        size = Math.min(size, values.size());
        int[] indexArray = new int[size];
        Iterator<Integer> it = values.iterator();
        for (int i = 0; i < size; ++i)
        {
            indexArray[i] = it.next();
        }
        return indexArray;
    }

    /**
     * 简单的求和
     *
     * @param array
     * @return
     */
    private static double sum(double[] array)
    {
        double total = 0;
        for (double v : array)
        {
            total += v;
        }
        return total;
    }

    /**
     * 将文章分割为句子
     * 默认句子分隔符为：[，,。:：“”？?！!；;]
     *
     * @param document
     * @return
     */
    static List<String> splitSentence(String document)
    {
        return splitSentence(document, default_sentence_separator);
    }

    /**
     * 将文章分割为句子
     *
     * @param document 待分割的文档
     * @param sentence_separator 句子分隔符，正则表达式，如：   [。:？?！!；;]
     * @return
     */
    static List<String> splitSentence(String document, String sentence_separator)
    {
        List<String> sentences = new ArrayList<String>();
        for (String line : document.split("[\r\n]"))
        {
            line = line.trim(); //去除字符串两端多余空格
            if (line.length() == 0) continue;
            for (String sent : line.split(sentence_separator))		// [，,。:：“”？?！!；;]
            {
                sent = sent.trim();
                if (sent.length() == 0) continue;
                sentences.add(sent);
            }
        }

        return sentences;
    }

    /**
     * 将句子列表转化为文档
     *
     * @param sentenceList
     * @return
     */
    private static List<List<String>> convertSentenceListToDocument(List<String> sentenceList)
    {
        List<List<String>> docs = new ArrayList<List<String>>(sentenceList.size());
        for (String sentence : sentenceList)
        {
            List<Term> termList = segment(sentence); // 分词步骤
            List<String> wordList = new LinkedList<String>();
            for (Term term : termList)
            {
                if (StopWordDictionary.shouldInclude(term)) // 去停用词
                {
                    wordList.add(term.word);
                }
            }
            docs.add(wordList);
        }
        return docs;
    }

    /**
     * 分词
     * @param text 文本
     * @return 分词结果s
     */
    public static List<Term> segment(String text)
    {
        String result = NlpirMethod.NLPIR_ParagraphProcess(text, 1);
        String[] temp = result.split("\\s+"); // 以一个或多个空格分割字符串
        List<Term> termList = new ArrayList<Term>();
        for(String s : temp){
            String[] p = s.split("/(?!/)"); // 我吐了：正则表达式里1个反斜杠要用4个反斜杠表示！！（正向否定断言）
            if(p.length != 2) continue;
            Term term = new Term(p[0],p[1]);
            termList.add(term);
        }
        return termList;
    }

    /**
     * 一句话调用接口
     *
     * @param document 目标文档
     * @param size     需要的关键句的个数
     * @return 关键句列表
     */
    public static List<String> getTopSentenceList(String document, int size)
    {
        return getTopSentenceList(document, size, default_sentence_separator);
    }

    /**
     * 一句话调用接口
     *
     * @param document 目标文档
     * @param size     需要的关键句的个数
     * @param sentence_separator 句子分隔符，正则格式， 如：[。？?！!；;]
     * @return 关键句列表
     */
    public static List<String> getTopSentenceList(String document, int size, String sentence_separator)
    {
        List<String> sentenceList = splitSentence(document, sentence_separator);
        List<List<String>> docs = convertSentenceListToDocument(sentenceList);
        TextRankSentence textRank = new TextRankSentence(docs);
        int[] topSentence = textRank.getTopSentence(size);
        List<String> resultList = new LinkedList<String>();
        for (int i : topSentence)
        {
            resultList.add(sentenceList.get(i));
        }
        return resultList;
    }

    /**
     * 一句话调用接口
     *
     * @param document   目标文档
     * @param max_length 需要摘要的长度
     * @return 摘要文本
     */
    public static String getSummary(String document, int max_length)
    {
        return getSummary(document, max_length, default_sentence_separator);
    }

    /**
     * 一句话调用接口
     *
     * @param document   目标文档
     * @param max_length 需要摘要的长度
     * @param sentence_separator 句子分隔符，正则格式， 如：[。？?！!；;]
     * @return 摘要文本
     */
    public static String getSummary(String document, int max_length, String sentence_separator)
    {
        List<String> sentenceList = splitSentence(document, sentence_separator);
//        for(String s : sentenceList){
//            System.out.println(s);
//        }
        int sentence_count = sentenceList.size();
        int document_length = document.length();
        int sentence_length_avg = document_length / sentence_count;
        int size = max_length / sentence_length_avg + 1;
        List<List<String>> docs = convertSentenceListToDocument(sentenceList);
        TextRankSentence textRank = new TextRankSentence(docs);
        int[] topSentence = textRank.getTopSentence(size);

        List<String> resultList = new LinkedList<String>();
        for (int i : topSentence)
        {
            resultList.add(sentenceList.get(i));
        }

        resultList = permutation(resultList, sentenceList); // 将摘要句子按原文顺序排列
        resultList = pick_sentences(resultList, max_length); // 从排好序的句子里留下符合摘要长度的句子
        return text_join("。", resultList);
    }

    /**
     * 文本合并
     *
     * @param delimiter   合并分隔符
     * @param stringCollection 待合并文本集合
     * @return 合并后摘要文本
     */
    public static String text_join(String delimiter, Collection<String> stringCollection)
    {
        StringBuilder sb = new StringBuilder(stringCollection.size() * (16 + delimiter.length()));
        for (String str : stringCollection)
        {
            sb.append(str).append(delimiter);
        }

        return sb.toString();
    }

    /*
     * 将摘要句子按原文顺序排列
     */
    private static List<String> permutation(List<String> resultList, final List<String> sentenceList)
    {
        Collections.sort(resultList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Integer num1 = sentenceList.indexOf(o1);
                Integer num2 = sentenceList.indexOf(o2);
                return num1.compareTo(num2);
            }
        });
        return resultList;
    }

    /*
     * 从排好序的句子里留下符合摘要长度的句子
     */
    private static List<String> pick_sentences(List<String> resultList, int max_length)
    {
        List<String> summary = new ArrayList<String>();
        int count = 0;
        for (String result : resultList) {
            if (count + result.length() <= max_length) {
                summary.add(result);
                count += result.length();
            }
        }
        return summary;
    }

}
