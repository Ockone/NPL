package summary;

import org.junit.Test;

import java.util.List;

public class TextRankTest {
    @Test
    public void testGetSummary(){
        String content = "据俄罗斯卫星网8月11日发布美国《国家利益》杂志刊登的文章称，中国购买俄制苏-27第四代战机，为本国空军翻开了现代史的页章。" +
                "从那时起，中国空军日益强大。中国空军长期以来落后于像美国这样的世界大国，从2008年起中国开始研制堪舆美国F-22猛禽战机和F-35闪电-II相媲美的第五代战机J-20和J-31，" +
                "不仅用它们装备本国空军，而且还在国际市场销售。它让中国具有了远程打击的能力，能达到西太平洋的任何地点。J-31可能成为J-20的有力补充，是理想的战机，能在西太平洋切断重要地区。" +
                "J-31升空后，完全能应对美国的F-35。这些战机能从根本上改变中国同美国以及同台湾地区冲突的走向。如果中国大陆通过台湾海峡进攻台湾(解放军每年都要进行这方面的演练)，" +
                "因为拥有最先进的战机而具有的空中优势是解放军进攻取胜的关键因素。这无疑应当引起美国的不安，无论是从战略上，还是从战术上以及从机动性上，美国《国家利益》警告。";
//        String content = "";
        System.out.println(content);
        String summary = TextRankSentence.getSummary(content,150); // 这里max_length是指摘要字数
        System.out.println("形成摘要：\n" + summary);

    }

    @Test
    public void testGetKeyword(){
        String content = "据俄罗斯卫星网8月11日发布美国《国家利益》杂志刊登的文章称，中国购买俄制苏-27第四代战机，为本国空军翻开了现代史的页章。" +
                "从那时起，中国空军日益强大。中国空军长期以来落后于像美国这样的世界大国，从2008年起中国开始研制堪舆美国F-22猛禽战机和F-35闪电-II相媲美的第五代战机J-20和J-31，" +
                "不仅用它们装备本国空军，而且还在国际市场销售。它让中国具有了远程打击的能力，能达到西太平洋的任何地点。J-31可能成为J-20的有力补充，是理想的战机，能在西太平洋切断重要地区。" +
                "J-31升空后，完全能应对美国的F-35。这些战机能从根本上改变中国同美国以及同台湾地区冲突的走向。如果中国大陆通过台湾海峡进攻台湾(解放军每年都要进行这方面的演练)，" +
                "因为拥有最先进的战机而具有的空中优势是解放军进攻取胜的关键因素。这无疑应当引起美国的不安，无论是从战略上，还是从战术上以及从机动性上，美国《国家利益》警告。";
        List<String> keywords = TextRankKeyword.getKeywordList(content,20);
        System.out.println("\n关键词：\n" + TextRankKeyword.count + keywords.toString());
    }
}
