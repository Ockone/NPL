package summary;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * 提取关键词的基类
 *
 * @author hankcs
 */
public abstract class KeywordExtractor
{
    /**
     * 默认分词器
     */
    protected Segment defaultSegment;

    public KeywordExtractor(Segment defaultSegment)
    {
        this.defaultSegment = defaultSegment;
    }

    public KeywordExtractor()
    {
        this(StandardTokenizer.SEGMENT);
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
     * 设置关键词提取器使用的分词器
     *
     * @param segment 任何开启了词性标注的分词器
     * @return 自己
     */
    public KeywordExtractor setSegment(Segment segment)
    {
        defaultSegment = segment;
        return this;
    }

    public Segment getSegment()
    {
        return defaultSegment;
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
        return getKeywords(defaultSegment.seg(document), size);
    }

    /**
     * 提取关键词（top 10）
     *
     * @param document 文章
     * @return
     */
    public List<String> getKeywords(String document)
    {
        return getKeywords(defaultSegment.seg(document), 10);
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
