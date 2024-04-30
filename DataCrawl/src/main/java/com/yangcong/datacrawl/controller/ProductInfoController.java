package com.yangcong.datacrawl.controller;

import com.yangcong.datacrawl.common.ExcelUtil;
import com.yangcong.datacrawl.crawl.MainURLHandler;
import com.yangcong.datacrawl.crawl.ProductDetlQuery;
import com.yangcong.datacrawl.entity.ErrInfo;
import com.yangcong.datacrawl.entity.ProductInfo;
import com.yangcong.datacrawl.entity.ProductUrlInfo;
import com.yangcong.datacrawl.service.ErrInfoService;
import com.yangcong.datacrawl.service.ProductInfoService;
import com.yangcong.datacrawl.service.ProductUrlInfoService;
import com.yangcong.datacrawl.thread.CallableTask;
import org.apache.ibatis.annotations.Param;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductInfoController {


    @Value(value = "${network.cookie_value}")
    private String COOKIE_VALUE;
    @Value(value = "${network.cookie_name}")
    private String COOKIE_NAME;
    @Value(value = "${file.path}")
    private String FILE_PATH;


    private final static Logger logger = LoggerFactory.getLogger(ProductDetlQuery.class);

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private ErrInfoService errInfoService;
    @Autowired
    private ProductUrlInfoService productUrlInfoService;

    @Autowired
    private MainURLHandler mainURLHandler;

    @Autowired
    private ProductDetlQuery productDetlQuery;

    private List<String> SKIP_MENU = Arrays.asList("Attachments", "Brakes", "Cooling","Chassis","Filters");


    @RequestMapping("/getAllProductInfo")
    @ResponseBody
    public List<ProductInfo> getProductList() {
//        System.err.println("COOKIE_VALUE:" + COOKIE_VALUE);
//        System.err.println("COOKIE_NAME:" + COOKIE_NAME);
        return productInfoService.getAllProductInfo();
    }

    @RequestMapping("/insertProductInfo")
    public void insertProductInfo(ProductInfo productInfo) {
        productInfoService.inserytProductInfo(productInfo);
    }

    @RequestMapping("/updateProductInfo")
    public void updateProductInfo(ProductInfo productInfo) {
        productInfoService.updateProductInfo(productInfo);
    }


    @RequestMapping("/getCategoryUrls")
    public void getCategoryUrls() {
        logger.info("getCategoryUrls开始");
        productUrlInfoService.deleteAllProductUrlInfo();
        List<ProductUrlInfo> categoryUrls = mainURLHandler.getCategoryUrl();
        productUrlInfoService.insertProductUrlInfo(categoryUrls);
        logger.info("getCategoryUrls结束总计[{}]条", categoryUrls.size());
    }

    @RequestMapping("/queryProductUrls")
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
    public String queryProductUrls() throws ExecutionException, InterruptedException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProductUrlInfo> productUrlInfos = productUrlInfoService.selectAllProductUrlInfo();
//        List<ProductUrlInfo> productUrlInfos = mainURLHandler.getCategoryUrl();
        productUrlInfos = productUrlInfos.stream().filter(productUrlInfo -> !SKIP_MENU.contains(productUrlInfo.getFirstDirectory())).collect(Collectors.toList());
//        productUrlInfos = productUrlInfos.stream().filter(e->"Tires & Wheels".equals(e.getFirstDirectory())).collect(Collectors.toList());
        Map<String, List<ProductUrlInfo>> subUrlMap = productUrlInfos.stream().collect(Collectors.groupingBy(e -> e.getFirstDirectory()));
        TreeSet<String> subMenus = new TreeSet(subUrlMap.keySet());
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
        List<ProductInfo> allProductInfos = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(subMenus.size());
        for (String subMenu : subMenus) {
//            if (!"Chassis".equals(subMenu)){
//                continue;
//            }
            List<ProductUrlInfo> productUrlInfos1 = subUrlMap.get(subMenu);
//            productDetlQuery.getProductDetailedWithBrandByUrls(productUrlInfos1);
            Future<List<ProductInfo>> future = threadPoolExecutor.submit(new Callable<List<ProductInfo>>() {
                @Override
                public List<ProductInfo> call() throws Exception {
                    List<ProductInfo> productInfos = new ArrayList<>();
//                    try {
                    logger.info("对应目录：" + subMenu + "开始爬取数据,开始时间:" + sdf.format(new Date()));
                    productDetlQuery.getProductDetailedWithBrandByUrls(productUrlInfos1);
                    logger.info("对应目录：" + subMenu + "爬取数据结束,结束时间:" + sdf.format(new Date()));
//                    } catch (Exception e) {
//                        logger.error("数据运行出错",e.getMessage());
//                    }finally {
                    countDownLatch.countDown();
//                    }
                    return productInfos;
                }
            });
        }
        countDownLatch.await();
        logger.info("最终结果：" + allProductInfos);
        return "数据提取完成";
    }


    @RequestMapping("/queryAllProductUrls")
    public void queryAllProductUrls() throws ExecutionException, InterruptedException {
        List<ErrInfo> errInfos = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProductUrlInfo> productUrlInfos = productUrlInfoService.selectAllProductUrlInfo();
//        productUrlInfos = productUrlInfos.stream().filter(productUrlInfo -> !SKIP_MENU.contains(productUrlInfo.getFirstDirectory())).collect(Collectors.toList());
        Map<String, List<ProductUrlInfo>> subUrlMap = productUrlInfos.stream().filter(e->"Fuel".equals(e.getFirstDirectory())).collect(Collectors.groupingBy(e -> e.getFirstDirectory()));
//        Map<String, List<ProductUrlInfo>> subUrlMap = productUrlInfos.stream().collect(Collectors.groupingBy(e -> e.getFirstDirectory()));
        TreeSet<String> subMenus = new TreeSet(subUrlMap.keySet());
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
        CountDownLatch countDownLatch = new CountDownLatch(subMenus.size());
        for (String subMenu : subMenus) {
            List<ProductUrlInfo> productUrlInfos1 = subUrlMap.get(subMenu);
//            productDetlQuery.getProductDetailedWithBrandByUrls(productUrlInfos1);
            Future<List<ProductInfo>> future = threadPoolExecutor.submit(new Callable<List<ProductInfo>>() {
                @Override
                public List<ProductInfo> call() throws Exception {
                    List<ProductInfo> productInfos = new ArrayList<>();
//                    try {
                    logger.info("对应目录：" + subMenu + "开始爬取数据,开始时间:" + sdf.format(new Date()));
                    productDetlQuery.getProductDetailedWithNoBrandByUrls(productUrlInfos1);
                    logger.info("对应目录：" + subMenu + "爬取数据结束,结束时间:" + sdf.format(new Date()));
//                    } catch (Exception e) {
//                        logger.error("数据运行出错",e.getMessage());
//                    }finally {
                    countDownLatch.countDown();
//                    }
                    return productInfos;
                }
            });
        }
        countDownLatch.await();
        if (errInfos.size() > 0) {
            errInfoService.insertBatch(errInfos);
        }
        logger.info("----------------------------数据爬取完毕---------------------------");
    }

    @RequestMapping("deleteDistinctData")
    public void deleteDistinctData(@Param("productName") String productName) {
        List<ProductInfo> productInfos = productInfoService.getBrandProduct();
        logger.info("总数据[{}]条",productInfos.size());
        List<ProductInfo>   productInfos1 = productInfos.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getModel() + ";" + o.getBrand() + ";" + o.getProductParts() + ";"
                                + o.getProductSubParts() + ";" + o.getProductName() + ";" + o.getProductCode() + ";" + o.getProductDescription() + ";" + o.getPhotoUrl()))), ArrayList::new));
        logger.info("去重后数据[{}]条",productInfos1.size());
        productInfos.removeAll(productInfos1);
        logger.info("需要删除重复数据[{}]条",productInfos.size());
        productInfoService.deleteDistinctData(productInfos);

    }


    @RequestMapping("/createXlsFile")
    public void createXlsFile() {

        List<ProductInfo> allProductInfo = productInfoService.getAllProductInfo();
        //数据进行excel导出
        List<String> headList = Arrays.asList("品牌", "车型", "部件", "子部件", "零件名称", "零件号", "价格", "重量", "描述", "图片");
        ExcelUtil.exportToExcel("fpe数据", headList, allProductInfo, FILE_PATH);


    }


    @RequestMapping("/testUrl")
    public void testUrl() throws IOException {
        List<ErrInfo> errInfos = new ArrayList<>();
        ProductUrlInfo productUrlInfo = new ProductUrlInfo();
        productUrlInfo.setDirectoryUrl("https://www.fpe-store.com/category-s/201.htm");
        Document document = null;
        try {
            document = Jsoup.connect("https://www.fpe-store.com/category-s/201.htm?searching=Y&sort=2&show=30&page=12&brand=CHAMP+/+LUBERFINER").cookie(COOKIE_NAME, COOKIE_VALUE).timeout(Integer.MAX_VALUE).get();

        } catch (Exception e) {
            ErrInfo errInfo = new ErrInfo();
            if (e.getMessage().length() > 1024) {
                errInfo.setErrMessage(e.getMessage().substring(0, 1024));
            } else {
                errInfo.setErrMessage(e.getMessage());
            }
            errInfos.add(errInfo);
            System.err.println("发生异常，异常原因:" + e.getMessage());
        }


    }


}
