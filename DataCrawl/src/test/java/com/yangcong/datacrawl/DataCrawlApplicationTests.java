package com.yangcong.datacrawl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class DataCrawlApplicationTests {

    @Test
    void contextLoads() {
//        'vsettings=; ASPSESSIONIDSAQTSBSQ=DHPGNIBDGNKEAMAJIDJCNKCE; dtCookie=v_4_srv_10_sn_7CCB15DA0834236FD0525297A2F26529_perc_100000_ol_0_mul_1_app-3A110b643356060ee6_1; TS01e1b643=014f69ac9bdfb4e167de1e69edb52468877b78b01ac7721ac5ae094f4c6c97da7ba7f3c808ae8daccccab6f10b845a18594ef93e062feb40fe2b6172d7f683d83ee707342c; rxVisitor=17138570089295QRBDUN2O1R3AJKB0RFJIVGUNJ37SD4T; _gid=GA1.2.200524208.1713857013; _fbp=fb.1.1713857014096.1077597032; _hjSessionUser_1966167=eyJpZCI6IjE2ODIyZjU0LWVjZTgtNTIxYi1hZjUxLWExMjZlNTQ0OTk4NCIsImNyZWF0ZWQiOjE3MTM4NTcwMTQ0MTUsImV4aXN0aW5nIjp0cnVlfQ==; _hjSession_1966167=eyJpZCI6IjVkYjZkMDhiLTk2NmQtNGNjOS1iODYyLTE0NDEyNzc3MTJjZSIsImMiOjE3MTM4NTcwMTQ0MTgsInMiOjEsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjoxLCJzcCI6MH0=; __stripe_mid=41a7211d-9ae4-4c3a-9210-b2695febd05981ba22; __stripe_sid=010c9183-6e2d-499d-a62b-26b5b33c525534b3ed; TS014fe2d9_30=01cab3fd34d8349a9b54340d1ce1d2e06f970c5db309f995c67cede7df50336d5aef369ab578264373793e8198cb2942063cdd1b81; _gcl_au=1.1.1225147573.1713857011.976196486.1713857037.1713857162; dtSa=-; TS014fe2d9=014f69ac9ba6c8d35ed1f0bddce084969043e3764239a22a7f6106f66e13ba008743cdba7e267e94d321083d49ea1958e237397dd621c1ab59a3b127838bd4d5d02ec78fe6921636fe1f30e2de8c83cf4114a69bd06f68f3846df0841856ca99b642e37d26f6e81faa664df7b7f188b24b7bd4ee5b6328a88f97d0a96c2abae24806572051; _ga=GA1.1.2042424344.1713857013; _ga_CKNRPPNHJK=GS1.1.1713857013.1.1.1713859138.56.0.0; rxvt=1713860939927|1713857008941; dtPC=10$259134785_763h-vARSCRKDKWACCCLWKFADLAIWMTWKHHPLL-0e0'
        try {
            String url = "https://www.fpe-store.com/category-s/164.htm?searching=Y&sort=2&cat=164&show=30&page=2";
            Map<String, String> data = new HashMap<>();
            data.put("email", "1772748735@qq.com");
            data.put("password", "yangcong98.");
            Map<String, String> cookieMap = new HashMap<>();
            cookieMap.put("Cookie", "vsettings=; rxVisitor=17138570089295QRBDUN2O1R3AJKB0RFJIVGUNJ37SD4T; _gid=GA1.2.377897362.1713862312; _fbp=fb.1.1713862312617.818477389; _hjSessionUser_1966167=eyJpZCI6IjU2OWEzMWE3LTg0NmItNTBkOC04ZmE4LTZhMGFkNWRjMTI0ZSIsImNyZWF0ZWQiOjE3MTM4NjIzMTMzMjYsImV4aXN0aW5nIjp0cnVlfQ==; __stripe_mid=b25e1d96-5754-4a6d-8baa-807f4c9e45ec16c573; volses=9d99cb00-4088-3e62-a5b8-15abbb1e0f7e; ASPSESSIONIDSARRSBSR=AJANDOGAKJNGCBNAFFFPNCCJ; dtCookie=v_4_srv_3_sn_641E3B711EE7CCB2E24AEBE88AD63767_perc_100000_ol_0_mul_1_app-3A110b643356060ee6_1; TS01e1b643=014f69ac9ba93fc02261437c9715d583b6ba95f5e83dff9b361c6f5ef07e3a90e4da7cc22045f461c7f71cb242f807905e057695bcd4fb1d4d4b3286798dc455b8174029c9; __stripe_sid=c5fd034b-c44b-422a-aaa5-20de18f638ed847bba; _hjSession_1966167=eyJpZCI6IjA0YTM2ODc2LTdjODYtNGRkMC1iYTk4LTE0MGFjMzE2NThiNCIsImMiOjE3MTQwMDYxMTE5NjAsInMiOjEsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MH0=; returnAddress=http%3A%2F%2Fwww%2Efpe%2Dstore%2Ecom%2FPart%2DFinder%2Ds%2F768%2Ehtm; _gcl_au=1.1.1958749165.1713862312.1859850096.1714006099.1714007105; slt=C938BFE4-9FFE-44BB-BC3E-E327B7C17FC4; CustomerID=DDDC7D70323722A1DEC1175B0839F8EC1415EB08A9ED767089B46C25F4C5229D; UserConfig=Config%5FOrderDetailID=2214422&LastName=yang&FirstName=cong&CustomerID=16606&Email=1772748735%40qq%2Ecom; TS014fe2d9_30=01cab3fd34c2ec88b47500268f86b32047a3dc43f6b95697e7f8ae6260ca79fa7e9c6421246c480fb87242069822949e20dca3e34c; dtSa=-; TS014fe2d9=014f69ac9bf1cfe6912dfee3a0588fc62e000936074f33062cf78bd49fc188a32d474bb15382e05e40fd116b1b5a6d3980770cc3e5183baa28a2a2eb47271a6f0b380d636642112a699f04717861dbd129d838195aac840e8574368b945bb6cdc0a07c493ae78ac8503aab999831ffb5fd44f7ce2ff7a1f965346db26b5ff88a1e7102cb88fe3e0e9038ea6c65976e0ae5b64052c92c4835e014a204b7d93dbd5795909a92677aa2fa24f12ba395210bb8c903f43080559acdbe8e896bbd19e1fafb0e369a; _ga_CKNRPPNHJK=GS1.1.1714006091.8.1.1714008967.60.0.0; dtPC=3$408967778_835h1vFOHFDDVOPCEVVBFRCAMVOCHPLWSMWHOH-0e0; _ga=GA1.2.114318622.1713862312; _gat_gtag_UA_1399167_37=1; rxvt=1714010769674|1714006084709");
//            cookieMap.put("CustomerID", "DDDC7D70323722A1DEC1175B0839F8EC1415EB08A9ED767089B46C25F4C5229D");
//            cookieMap.put("UserConfig", "Config%5FOrderDetailID=2214422&CustomerID=16606&Email=1772748735%40qq%2Ecom&LastName=yang&FirstName=cong");
//            cookieMap.put("slt", "C938BFE4-9FFE-44BB-BC3E-E327B7C17FC4");
//            cookieMap.put("_ga_CKNRPPNHJK", "GS1.1.1714006091.8.1.1714007115.50.0.0");
//            cookieMap.put("_gcl_au", "1.1.1958749165.1713862312.1859850096.1714006099.1714007105");
//            cookieMap.put("TS014fe2d9_30", "01cab3fd34c2ec88b47500268f86b32047a3dc43f6b95697e7f8ae6260ca79fa7e9c6421246c480fb87242069822949e20dca3e34c");
//            cookieMap.put("useremail", "1772748735@qq.com");
            Document document = Jsoup.connect(url).cookies(cookieMap).get();
            Elements elementsByClass = document.getElementsByClass("v-product");
//            for (Element element : elementsByClass) {

            Elements links = elementsByClass.get(0).getElementsByClass("v-product__title productnamecolor colors_productname");
//                for (Element link : links) {
            String productUrl = links.get(0).attr("href");
//            System.err.println("URL" + productUrl);
            document = Jsoup.connect(productUrl).cookies(cookieMap).get();
            Element prductDetl = document.getElementById("vCSS_mainform");
            //获取部件 子部件
            Elements vCSSBreadcrumbTd1 = prductDetl.getElementsByClass("vCSS_breadcrumb_td");
            for (Element vCSSBreadcrumbTd : vCSSBreadcrumbTd1) {
                Elements fontText = vCSSBreadcrumbTd.select("a");
                String parts = "";
                for (Element element : fontText) {
                    if (element.text().equals("Home")) {
                        continue;
                    }
                    parts += element.text() + ";";
                }
//                System.err.println("部件名称:  " + parts);
//                //零件名称
//                Elements productName = prductDetl.getElementsByClass("vp-product-title");
//                System.err.println("零件名称:  " + productName.get(0).text());
//                //零件信息
                Element prodtctInfo = prductDetl.getElementsByClass("colors_pricebox").get(0);
//                System.err.println(prodtctInfo);
//                Elements textInfos = prodtctInfo.select("b");
//                String applicaton = "";
//                for (Element textInfo : textInfos) {
//                    if (textInfo.text().contains("Applications:")) {
//                        applicaton= textInfo.nextSibling().toString();
//                    }
//
//                }
//                System.err.println("车型号:  "+applicaton);


                //零件号
//                Elements elementsByClass1 = prodtctInfo.getElementsByClass("product_code");
//                String productCode = elementsByClass1.get(0).text();
//                System.err.println("零件号:  " + productCode);
                //零件价格  需要解决登录问题
                String priceElement = prodtctInfo.getElementsByClass("pricecolor colors_productprice").select("div b span").get(0).text();
//                System.err.println("零件价格:" + priceElement);
//                //零件描述
//                String productDescription = prductDetl.getElementById("product_description").text();
//                System.err.println("零件描述:  " + productDescription);
//                //零件图片URL
//                String productPhotoUrl = prductDetl.getElementById("product_photo_zoom_url").attr("href");
//                System.err.println("零件图片URL:  " + productPhotoUrl);
            }


//                System.err.println(links.get(0) + "-" + document);
//                }


//            System.err.println("---------------------------");
//
//            System.err.println("elementsByClass:" + elementsByClass);

//            // 获取网页标题
//            String title = document.title();
//            System.out.println("网页标题：" + title);
//
//            // 获取所有的链接
//            Elements links = document.select("a[href]");
//            System.out.println("链接数量：" + links.size());
//
//            // 打印每个链接的文本和URL
//            for (Element link : links) {
//                String linkText = link.text();
//                String linkUrl = link.attr("href");
//                System.out.println("链接文本：" + linkText);
//                System.out.println("链接URL：" + linkUrl);
//            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }

}
