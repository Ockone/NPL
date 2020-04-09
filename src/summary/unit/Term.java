package summary.unit;

/**
 * 一个单词，用户可以直接访问此单词的全部属性
 * @author Ockone
 */
public class Term
{
    /**
     * 词语
     */
    public String word;

    /**
     * 词性
     */
    public String nature;

    /**
     * 在文本中的起始位置（需开启分词器的offset选项）
     */
    public int offset;

    /**
     * 构造一个单词
     * @param word 词语
     * @param nature 词性
     */
    public Term(String word, String nature)
    {
        this.word = word;
        this.nature = nature;
    }

    /**
     * 判断Term是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Term)
        {
            Term term = (Term)obj;
            if (this.nature == term.nature && this.word.equals(term.word))
            {
                return true;
            }
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Term{" +
                "word='" + word + '\'' +
                ", nature='" + nature +
                '}';
    }
}
