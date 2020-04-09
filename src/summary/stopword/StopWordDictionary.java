package summary.stopword;

import summary.unit.Term;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StopWordDictionary {

    /*
     * 读取停用词表
     */
    public static Set<String> stopwordDict;
    // 加载停用词表
    static {load("src/summary/stopword/stopword.dic");}
    public static void load(String dictPath){
        stopwordDict = new HashSet<String>();
        try{
            BufferedReader bf = new BufferedReader(new FileReader(dictPath));
            String temp = null;
            while((temp = bf.readLine()) != null){ // 读取停用词表每一行，兵填入stopwordDict
                stopwordDict.add(temp.trim());
            }
            bf.close();
        }catch(IOException e){
            throw new RuntimeException("载入停用词词典" + dictPath + "失败");
        }

    }
    /**
     * 停用词典的过滤器，词性属于名词、动词、副词、形容词，并且不在停用词表中才不会被过滤
     */
    public static boolean shouldInclude(Term term) {
        // 除掉停用词
        String nature = term.nature != null ? term.nature.toString() : "空";
        char firstChar = nature.charAt(0);
        switch (firstChar)
        {
            case 'm': // 数词
            case 'b': // 区别词
            case 'c': // 连词
            case 'e': // 叹词
            case 'o': // 拟声词
            case 'p': // 介词
            case 'q': // 量词
            case 'u': // 助词
            case 'y': // 语气词
            case 'z': // 状态词
            case 'r': // 代词
            case 'w': // 标点符号
            {
                return false;
            }
            default:
            {
                if (!stopwordDict.contains(term.word))
                {
                    return true;
                }
            }
            break;
        }
        return false;
    }

    private static boolean contains(String word) {
        return true;
    }
}
