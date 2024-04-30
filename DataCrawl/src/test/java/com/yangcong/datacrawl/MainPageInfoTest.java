package com.yangcong.datacrawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class MainPageInfoTest {


    @Test
    public void testMainPageInfo() {

        String mainUrl = "https://www.fpe-store.com";

        try {
            Document document = Jsoup.connect(mainUrl).get();
            Element mainMenu = document.getElementById("display_menu_2");
            Elements firstMenus = mainMenu.select(".vnav--level1 > .vnav__item ");
            int index = 0;
            int innerIndex = 0;
            for (Element menu : firstMenus) {
                index++;
//                System.err.println("处理第" + index + "大类数据");
                Element liElement = menu.select("a").first();
                String subMenuContent = liElement.select(" > a").text();
                String firstUrl = liElement.attr("href");
                if (!firstUrl.startsWith(mainUrl)){
//                    System.err.println("错误URL："+firstUrl);
                    firstUrl = mainUrl + firstUrl;
                }
//                System.err.println("subMenuContent:"+subMenuContent);
//                System.err.println("firstUrl:"+firstUrl);
                Elements secendMenus = menu.select(".vnav--level2 > .vnav__item");
                innerIndex = 0;
                for (Element secendMenu : secendMenus) {
                    innerIndex++;
//                    System.err.println("处理第" + index + "大类第"+innerIndex+"小类数据");
                    String secondContent = secendMenu.select(" > a").text();
                    if (secondContent.contains("BRAKE SHOES & PADS")){
//                        System.err.println(secondContent);
                    }
                    if (secondContent.contains("All")){
                        continue;
                    }
                    String secondUrl = secendMenu.select("a").first().attr("href");
                    if (!secondUrl.startsWith(mainUrl)){
//                        System.err.println("错误URL："+secondUrl);
                        secondUrl = mainUrl + secondUrl;
                    }
//                    System.err.println("secondContent:"+secondContent);
//                    System.err.println("secondUrl:"+secondUrl);

                    Elements threeMenus = secendMenu.select(".vnav--level3 > .vnav__item");
                    int threeIndex = 0;
                    for (Element threeMenu : threeMenus) {
                        threeIndex++;
//                        System.err.println("处理第" + index + "大类第"+innerIndex+"小类数据");
                        String threeContent = threeMenu.select(" > a").text();
                        if (threeContent.contains("All")){
                            continue;
                        }
                        String threeUrl = threeMenu.select("a").first().attr("href");
                        if (!threeUrl.startsWith(mainUrl)){
//                            System.err.println("错误URL："+threeUrl);
                            threeUrl = mainUrl + threeUrl;
                        }
//                        System.err.println("threeContent:"+threeContent);
//                        System.err.println("threeUrl:"+threeUrl);

                        Elements fourMenus = secendMenu.select(".vnav--level4 > .vnav__item");
                        for (Element fourMenu : fourMenus) {
                            threeIndex++;
//                            System.err.println("处理第" + index + "大类第"+innerIndex+"小类数据");
                            String fourContent = fourMenu.select(" > a").text();
                            if (fourContent.contains("All")){
                                continue;
                            }
                            String fourUrl = fourMenu.select("a").first().attr("href");
                            if (!fourUrl.startsWith(mainUrl)){
//                                System.err.println("错误URL："+fourUrl);
                                fourUrl = mainUrl + fourUrl;
                            }
//                            System.err.println("fourContent:"+fourContent);
//                            System.err.println("fourUrl:"+fourUrl);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
