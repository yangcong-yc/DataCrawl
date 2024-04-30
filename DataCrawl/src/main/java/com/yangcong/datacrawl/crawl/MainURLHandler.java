package com.yangcong.datacrawl.crawl;

import com.yangcong.datacrawl.entity.ProductUrlInfo;
import org.apache.catalina.startup.UserConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品类别查询URL获取
 */
@Component
public class MainURLHandler {

    private static final String MAIN_URL = "https://www.fpe-store.com";


    @Value(value = "${network.cookie_value}")
    private String COOKIE_VALUE ;
    @Value(value = "${network.cookie_name}")
    private String COOKIE_NAME ;



    private static String TOP_MENU = "";
    private static String SECOND_MENU = "";
    private static String THREE_MENU = "";
    private static String FOUR_MENU = "";
    private static String INIT_MENU = null;


    private static Integer COUNT_NUM = 0;

//    public  void main(String[] args) {
//        this.getCategoryUrl();
//        System.err.println("总计条数：" + COUNT_NUM);
//    }

    public List<ProductUrlInfo> getCategoryUrl() {
        List<ProductUrlInfo> urlInfos= new ArrayList<>();
        try {
            Document document = Jsoup.connect(MAIN_URL).cookie(COOKIE_NAME, COOKIE_VALUE).timeout(Integer.MAX_VALUE).get();
            Element mainMenu = document.getElementsByClass("row home-grid").first();
            Elements topOneMenu = mainMenu.getElementsByClass("col-xs-3 col-md-2 col-lg-aux text-center");
            for (Element oneMenu : topOneMenu) {
                String url = oneMenu.select("a").first().attr("href");
                String TOP_MENU = oneMenu.select("a img").attr("alt");
                ProductUrlInfo productUrlInfo = new ProductUrlInfo();
                productUrlInfo.setFirstDirectory(TOP_MENU);
                productUrlInfo.setDirectoryUrl(MAIN_URL+url);
                urlInfos.add(productUrlInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!urlInfos.isEmpty()){
            return urlInfos;
        }
        return null;
    }

//    /**
//     * 递归获取所有URL
//     *
//     * @param elements
//     * @param index
//     * @return
//     */
//    public static String recursionURL(Elements elements, int index) {
//        for (Element menu : elements) {
//            COUNT_NUM++;
//            String content = menu.select(" > a").text();
//            if (content.contains("All")) {
//                continue;
//            }
//            String url = menu.select("a").first().attr("href");
//            if (!url.startsWith(MAIN_URL)) {
////                System.err.println("错误URL：" + url);
//                url = MAIN_URL + url;
//            }
//            transMenuGrade(index,content);
////            System.err.println("fourContent:" + content);
////            System.err.println("fourUrl:" + url);
//            Elements nextMenus = menu.select(".vnav--level" + (index + 1) + " > .vnav__item");
//            if (!nextMenus.isEmpty()) {
//                recursionURL(nextMenus, index + 1);
//            }else {
//                //没有下级目录就插入数据库
//                ProductUrlInfo productUrlInfo = new ProductUrlInfo();
//                productUrlInfo.setFirstDirectory(TOP_MENU);
//                productUrlInfo.setSecondDirectory(SECOND_MENU);
//                productUrlInfo.setThreeDirectory(THREE_MENU);
//                productUrlInfo.setFourDirectory(FOUR_MENU);
//                productUrlInfo.setDirectoryUrl(url);
//                urlInfos.add(productUrlInfo);
//            }
//        }
//        return null;
//    }

    /**
     *
     * 根据层级序号转换菜单等级
     * @param index
     * @return
     */
    public static void transMenuGrade(int index,String content) {
        switch (index) {
            case 1:
                TOP_MENU = content;
                break;
            case 2:
                SECOND_MENU = content;
                THREE_MENU = INIT_MENU;
                FOUR_MENU = INIT_MENU;
                break;
            case 3:
                THREE_MENU = content;
                FOUR_MENU = INIT_MENU;
                break;
            case 4:
                FOUR_MENU = content;
                break;
            default:
                break;
    }
    }

}
