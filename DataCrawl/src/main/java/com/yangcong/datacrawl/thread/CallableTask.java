package com.yangcong.datacrawl.thread;

import com.yangcong.datacrawl.crawl.ProductDetlQuery;
import com.yangcong.datacrawl.entity.ProductInfo;
import com.yangcong.datacrawl.entity.ProductUrlInfo;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;


public class CallableTask implements Callable<List<ProductInfo>> {


    String subMenu;
    SimpleDateFormat sdf;
    ProductDetlQuery productDetlQuery;
    List<ProductUrlInfo> productUrlInfos1;
    CountDownLatch countDownLatch;

    @Override
    public List<ProductInfo> call() throws Exception {
        List<ProductInfo> productInfos = new ArrayList<>();
        try {
            System.err.println("对应目录：" + subMenu + "开始爬取数据,开始时间:" + sdf.format(new Date()));
            productDetlQuery.getProductDetailedWithBrandByUrls(productUrlInfos1);
            System.err.println("对应目录：" + subMenu + "爬取数据结束,结束时间:" + sdf.format(new Date()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            countDownLatch.countDown();
        }
        return productInfos;
    }

    public CallableTask(String subMenu, SimpleDateFormat sdf, ProductDetlQuery productDetlQuery, List<ProductUrlInfo> productUrlInfos1, CountDownLatch countDownLatch) {
        this.subMenu = subMenu;
        this.sdf = sdf;
        this.productDetlQuery = productDetlQuery;
        this.productUrlInfos1 = productUrlInfos1;
        this.countDownLatch = countDownLatch;
    }
}
