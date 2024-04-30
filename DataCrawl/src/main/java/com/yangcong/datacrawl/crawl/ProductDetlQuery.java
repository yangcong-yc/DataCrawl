package com.yangcong.datacrawl.crawl;

import com.yangcong.datacrawl.entity.ErrInfo;
import com.yangcong.datacrawl.entity.ProductInfo;
import com.yangcong.datacrawl.entity.ProductUrlInfo;
import com.yangcong.datacrawl.service.ErrInfoService;
import com.yangcong.datacrawl.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDetlQuery {

    @Value(value = "${network.cookie_value}")
    private String COOKIE_VALUE;
    @Value(value = "${network.cookie_name}")
    private String COOKIE_NAME;

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private ErrInfoService errInfoService;

    private static Map<String, String> SKIP_BRAND = new HashMap<>();
    static {
//        SKIP_BRAND.put("Chassis", "TOYOTA");
        SKIP_BRAND.put("Electrical", "INTELLA");
        SKIP_BRAND.put("Engine", "KOMATSU");
        SKIP_BRAND.put("Hydraulic", "CLARK");
        SKIP_BRAND.put("Fuel", "NISSAN");
    }

    private final static Logger logger = LoggerFactory.getLogger(ProductDetlQuery.class);
    private static final String NO_BRAND = "NoBrand";
    private static final String SPLICING_URL = "&searching=Y&sort=2&show=30&page=";

    public void getProductDetailedWithBrandByUrls(List<ProductUrlInfo> urlInfos) {
        logger.info(Thread.currentThread().getName() + "开始爬取");
        try {
            for (ProductUrlInfo urlInfo : urlInfos) {
                //获得去重后的产品详细页面的Document对象
                getProductDetailedDocumentWithBrand(urlInfo);
                logger.info(urlInfo.getFirstDirectory() + "数据爬取完毕");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 数据组装
     * @param brandName
     * @param productInfos
     */
    private static void accembleData(String brandName, Document document, List<ProductInfo> productInfos) {
        Element prductDetl = document.getElementById("vCSS_mainform");
        //获取部件 子部件
        Element vCSSBreadcrumbTd = prductDetl.getElementsByClass("vCSS_breadcrumb_td").first();
        ProductInfo productInfo = new ProductInfo();
        Elements fontText = vCSSBreadcrumbTd.select("a");
        String parts = "";
        for (Element element : fontText) {
            if (element.text().equals("Home")) {
                continue;
            }
            parts += element.text() + ";";
        }
        productInfo.setBrand(brandName);
        String[] partsNames = parts.split(";");
        productInfo.setProductParts(partsNames[0]);
        if (partsNames.length>1){
            productInfo.setProductSubParts(partsNames[1]);
        }
        //零件名称
        Element productName = prductDetl.getElementsByClass("vp-product-title").first();
        productInfo.setProductName(productName.text());
        //零件信息
        Element prodtctInfo = prductDetl.getElementsByClass("colors_pricebox").first();
        Elements textInfos = prodtctInfo.select("b");
        String applicaton = "";
        String weight = "";
        for (Element textInfo : textInfos) {
            if (textInfo.text().contains("Applications:")) {
                applicaton = textInfo.nextSibling().toString();
            }
            if (textInfo.text().contains("Weight:")) {
                weight = textInfo.nextSibling().toString();
            }
        }
        productInfo.setModel(applicaton);
        productInfo.setWeight(weight);
        productInfo.setModel(applicaton);
        //零件号
        Elements elementsByClass1 = prodtctInfo.getElementsByClass("product_code");
        String productCode = elementsByClass1.get(0).text();
        productInfo.setProductCode(productCode);
        //零件价格  需要解决登录问题
        String priceElement = prodtctInfo.getElementsByClass("pricecolor colors_productprice").select("div b span").get(0).text();
        productInfo.setProductPrice(priceElement);
        //零件描述
        String productDescription = prductDetl.getElementById("product_description").text();
        productInfo.setProductDescription(productDescription);
        //零件图片URL
        Element productPhotoEle = prductDetl.getElementById("product_photo_zoom_url");
        String productPhotoUrl = "";
        if (productPhotoEle != null){
            productPhotoUrl = productPhotoEle.attr("href");
        }
        productInfo.setPhotoUrl(productPhotoUrl);
        productInfos.add(productInfo);
    }

    public void getProductDetailedWithNoBrandByUrls(List<ProductUrlInfo> urlInfos) throws Exception {
        System.err.println(Thread.currentThread().getName() + "开始执行");
        try {
            for (ProductUrlInfo urlInfo : urlInfos) {
                //获得去重后的产品详细页面的Document对象
                getProductDetailedDocumentWithNoBrand(urlInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据产品目录URL获取产品详细页面的Document对象 不带品牌
     *
     * @param urlInfo
     * @return
     * @throws Exception
     */
    public void getProductDetailedDocumentWithNoBrand(ProductUrlInfo urlInfo) throws Exception {
        String directoryUrl = urlInfo.getDirectoryUrl();
        List<ErrInfo> errInfos = new ArrayList<>();
        String catText = directoryUrl.substring(directoryUrl.lastIndexOf("/") + 1, directoryUrl.lastIndexOf("."));
        int firstPage = 30;
        directoryUrl = directoryUrl + "?" + "cat=" + catText + SPLICING_URL;
        Document allDocument = null;
        //第一次查询所有产品
        allDocument = Jsoup.connect(directoryUrl + firstPage).cookie(COOKIE_NAME, COOKIE_VALUE).timeout(Integer.MAX_VALUE).get();
        Element mainForm = allDocument.getElementById("MainForm");
        Element inputPage = mainForm.select("table td[align=right] b b").last();
        String text = inputPage.text();
        String totPage = text.substring(text.indexOf("f") + 1).trim();
        Elements elementsByClass = allDocument.getElementsByClass("v-product");
        productDetlAnalys(elementsByClass, null,errInfos);
        logger.info("目录:" + urlInfo.getFirstDirectory() + "第 ["+firstPage+"] 页爬取完成，共计:" + elementsByClass.size() + "条");
        while (firstPage < Integer.valueOf(totPage)) {
            firstPage++;
            try {
                allDocument = Jsoup.connect(directoryUrl + firstPage).cookie(COOKIE_NAME, COOKIE_VALUE).timeout(Integer.MAX_VALUE).get();
            } catch (Exception e) {
                ErrInfo errInfo = new ErrInfo();
                errInfo.setErrDetlUrl(directoryUrl + firstPage);
                errInfo.setErrMessage(e.getMessage());
                errInfos.add(errInfo);
                System.err.println("getProductDetailedDocumentWithBrand--2--发生异常，异常原因:" + e.getMessage());
                continue;
            }
            elementsByClass = allDocument.getElementsByClass("v-product");
            productDetlAnalys(elementsByClass, null,errInfos);
            logger.info("目录:" + urlInfo.getFirstDirectory() + "第 [" + firstPage + "] 爬取完成，共计:" + elementsByClass.size() + "条");
        }
        if (errInfos.size()>0){
            logger.error("目录[{}]记录错误信息[{}]条",urlInfo.getFirstDirectory(),errInfos.size());
            errInfoService.insertBatch(errInfos);
        }


    }

    /**
     * 根据产品目录URL获取产品详细页面的Document对象 带品牌
     *
     * @param urlInfo
     * @return
     * @throws Exception
     */
    public void getProductDetailedDocumentWithBrand(ProductUrlInfo urlInfo) throws Exception {
        String topMenu = urlInfo.getFirstDirectory();
        String skipBrand = SKIP_BRAND.get(topMenu);
        List<ErrInfo> errInfos = new ArrayList<>();
        String directoryUrl = urlInfo.getDirectoryUrl();
        String threadName = Thread.currentThread().getName();
        String catText = directoryUrl.substring(directoryUrl.lastIndexOf("/") + 1, directoryUrl.lastIndexOf("."));
        int firstPage = 1;
        directoryUrl = directoryUrl + "?" + "cat=" + catText + SPLICING_URL;
        Document allDocument = null;
        try {
//            firstDocument = Jsoup.connect(directoryUrl + firstPage).cookie(COOKIE_NAME, COOKIE_VALUE).timeout(Integer.MAX_VALUE).get();
            allDocument = Jsoup.connect(directoryUrl + firstPage).cookie(COOKIE_NAME, COOKIE_VALUE).timeout(Integer.MAX_VALUE).get();
        } catch (Exception e) {
            ErrInfo errInfo = new ErrInfo();
            errInfo.setErrDetlUrl(directoryUrl + firstPage);
            errInfo.setErrMessage(e.getMessage());
            errInfos.add(errInfo);
            logger.info("getProductDetailedDocumentWithBrand----发生异常，异常原因:" + e.getMessage());
        }

        Element mainForm = allDocument.getElementById("MainForm");
        if (mainForm == null) {
            ErrInfo errInfo = new ErrInfo();
            errInfo.setErrDetlUrl(directoryUrl + firstPage);
            errInfo.setErrMessage("链接无数据：" + directoryUrl);
            errInfos.add(errInfo);
            logger.info("链接无数据：" + directoryUrl);
            return;
        }
        StringBuilder brands = new StringBuilder("");
        Elements elementsByClass = null;
        Elements brandOptions = allDocument.getElementsByClass("refinement_brand_option");
//        StringBuilder beannenmas = new StringBuilder("");
        //获取品牌
        for (Element brandOption : brandOptions) {
            String brandName = brandOption.text().substring(0, brandOption.text().indexOf("(")).trim();
            if (brandName.length() > 0) {
                brands.append(brandName + ";");
            }
//            beannenmas.append(brandOption.text()+";");
        }
//        System.err.println("目录的品牌有："+urlInfo.getFirstDirectory()+"-["+beannenmas+"]");
//        有品牌  重新开始查询
        if (brands.length() > 0) {
            Element inputPage = null;
            String text = null;
            String totPage = null;
            int numCount = 0;
            if (skipBrand != null && !"".equals(skipBrand)) {
                int skipIndex = brands.indexOf(skipBrand);
                brands = new StringBuilder(brands.substring(skipIndex));
            }
            logger.info("需要抓取品牌：[{}]",brands);
            for (String brand : brands.toString().split(";")) {
//                if (!"KOMATSU".equals(brand)){
//                    continue;
//                }
                if (brand.contains("&")){
                    brand = brand.replaceAll("&","%26");
                }
                firstPage = 1;
                numCount++;
                long startTime = System.currentTimeMillis();
                logger.info(threadName + "开始品牌：" + brand + "数据抓取");
                Document document = null;
                try {
                    document = Jsoup.connect(directoryUrl + firstPage + "&brand=" + brand + "&cat=" + catText).cookie(COOKIE_NAME, COOKIE_VALUE).timeout(Integer.MAX_VALUE).get();
                } catch (Exception e) {
                    ErrInfo errInfo = new ErrInfo();
                    errInfo.setErrDetlUrl(directoryUrl + firstPage + "&brand=" + brand + "&cat=" + catText);
                    errInfo.setErrMessage(e.getMessage());
                    errInfos.add(errInfo);
                    logger.info("getProductDetailedDocumentWithBrand--1--发生异常，异常原因:" + e.getMessage());
                    numCount--;
                    continue;
                }
                elementsByClass = document.getElementsByClass("v-product");
                productDetlAnalys(elementsByClass, brand,errInfos);
                logger.info("目录:" + urlInfo.getFirstDirectory() + "-品牌:" + brand + "第 [1] 页爬取完成，共计:" + elementsByClass.size() + "条");
                mainForm = document.getElementById("MainForm");
                inputPage = mainForm.select("table td[align=right] b b").last();
                text = inputPage.text();
                totPage = text.substring(text.indexOf("f") + 1).trim();
                while (firstPage < Integer.valueOf(totPage)) {
                    firstPage++;
                    try {
                        document = Jsoup.connect(directoryUrl + firstPage + "&brand=" + brand + "&cat=" + catText).cookie(COOKIE_NAME, COOKIE_VALUE).timeout(Integer.MAX_VALUE).get();
                    } catch (Exception e) {
                        ErrInfo errInfo = new ErrInfo();
                        errInfo.setErrMessage(e.getMessage());
                        errInfo.setErrDetlUrl(directoryUrl + firstPage + "&brand=" + brand + "&cat=" + catText);
                        errInfos.add(errInfo);
                        logger.info("目录:" + urlInfo.getFirstDirectory() + "-品牌:" + brand + "第 [" + firstPage + "] 页爬取完成，共计:" + elementsByClass.size() + "条");
                        numCount--;
                        continue;
                    }
                    elementsByClass = document.getElementsByClass("v-product");
                    productDetlAnalys(elementsByClass, brand,errInfos);
                    logger.info("目录:" + urlInfo.getFirstDirectory() + "-品牌:" + brand + "第 [" + firstPage + "] 页爬取完成，共计:" + elementsByClass.size() + "条");
                }
                long endTime = System.currentTimeMillis();
                logger.info(threadName + "第" + numCount + "品牌-" + brand + "抓取完毕,耗时[" + ((endTime - startTime) / (1000)) + "s]");
            }
            if (errInfos.size() > 0) {
                logger.error("目录[{}]记录错误信息[{}]条",urlInfo.getFirstDirectory(),errInfos.size());
                errInfoService.insertBatch(errInfos);
            }
        }

    }

    /**
     * 详情页面数据抓取
     *
     * @param elementsByClass
     * @throws Exception
     */
    private void productDetlAnalys(List<Element> elementsByClass, String brandName,List<ErrInfo> errInfos) throws Exception {
        Document document;
        List<ProductInfo> productInfos = new ArrayList<>();
        for (Element element : elementsByClass) {
            Element link = element.getElementsByClass("v-product__title productnamecolor colors_productname").first();
            String productUrl = link.attr("href");
//            logger.info(threadName + "开始产品：" + productUrl + "数据抓取");
            try {
//                productUrl = "https://www.fpe-store.com/product-p/23540-u3330-71.htm";
                document = Jsoup.connect(productUrl).cookie(COOKIE_NAME, COOKIE_VALUE).timeout(Integer.MAX_VALUE).get();
                accembleData(brandName, document, productInfos);
            } catch (Exception e) {
                if (!e.getMessage().contains("NullPointerException")) {
                    try {
                        document = Jsoup.connect(productUrl).cookie(COOKIE_NAME, COOKIE_VALUE).timeout(Integer.MAX_VALUE).get();
                        accembleData(brandName, document, productInfos);
                    } catch (Exception ex) {
                        ErrInfo errInfo = new ErrInfo();
                        errInfo.setErrMessage(e.getMessage());
                        errInfo.setErrDetlUrl(productUrl);
                        errInfos.add(errInfo);
                        logger.error("重试后productDetlAnalys----发生异常，异常原因:" + ex.getMessage());
                    }
                }
                logger.error("productDetlAnalys----发生异常，异常原因:" + e.getMessage());
            }
        }
//        for (int i = 0; i < productInfos.size(); i++) {
//            ProductInfo productInfo = productInfos.get(i);
//            if ("3EB-01-27140".equals(productInfo.getProductCode())){
//                productInfoService.inserytProductInfo(productInfo);
//            }
//        }


        if (productInfos.size() > 0) {
            Integer count = productInfoService.insertBatch(productInfos);
            ProductInfo productInfo = productInfos.get(0);
            logger.info(productInfo.getProductParts() + "-" + brandName + "插入数据" + count + "条");
        }

    }


}
