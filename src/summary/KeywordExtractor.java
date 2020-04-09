package summary;

import ictclas.NlpirMethod;
import summary.stopword.StopWordDictionary;
import summary.unit.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * 提取关键词的基类
 *
 * @author Ockone
 */
public abstract class KeywordExtractor
{
    /**
     * 默认分词器NLPIR-ICTCLAS
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
     * 是否应当将这个term纳入计算，词性属于名词、动词、副词、形容词
     *
     * @param term
     * @return 是否应当
     */
    protected boolean shouldInclude(Term term)
    {
        // 除掉停用词
        return StopWordDictionary.shouldInclude(term);
    }

    /**
     * 提取关键词
     *
     * @param document 关键词
     * @param size     需要几个关键词
     * @return
     */
    public List<String> getKeywords(String document, int size)
    {
        return getKeywords(segment(document), size);
    }

    /**
     * 提取关键词（top 10）
     *
     * @param document 文章
     * @return
     */
    public List<String> getKeywords(String document)
    {
        return getKeywords(segment(document), 10);
    }

    protected void filter(List<Term> termList)
    {
        ListIterator<Term> listIterator = termList.listIterator();
        while (listIterator.hasNext())
        {
            if (!shouldInclude(listIterator.next()))
                listIterator.remove();
        }
    }

    abstract public List<String> getKeywords(List<Term> termList, int size);
}
