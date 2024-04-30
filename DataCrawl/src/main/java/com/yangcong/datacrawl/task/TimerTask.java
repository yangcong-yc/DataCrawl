package com.yangcong.datacrawl.task;

import com.yangcong.datacrawl.crawl.MainURLHandler;
import com.yangcong.datacrawl.crawl.ProductDetlQuery;
import com.yangcong.datacrawl.entity.ProductInfo;
import com.yangcong.datacrawl.entity.ProductUrlInfo;
import com.yangcong.datacrawl.service.ProductInfoService;
import com.yangcong.datacrawl.service.ProductUrlInfoService;
import com.yangcong.datacrawl.thread.ExecutorThreadRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class TimerTask {


    @Autowired
    private ProductUrlInfoService productUrlInfoService;

    @Autowired
    private ProductDetlQuery productDetlQuery;

    @Scheduled(cron = "${time.cron}")
    public void  flush1() throws  Exception{
        System.out.println("Execute1开始");
        List<ProductUrlInfo> productUrlInfos = productUrlInfoService.selectAllProductUrlInfo();
        Map<String, List<ProductUrlInfo>> subUrlMap = productUrlInfos.stream().collect(Collectors.groupingBy(ProductUrlInfo::getFirstDirectory));
        Set<String> subMenus = subUrlMap.keySet();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.AbortPolicy());
        List<ProductInfo> productInfos = new ArrayList<>();
        List<Future<List<ProductInfo>>> futures = new ArrayList<>();
        CountDownLatch cdl = new CountDownLatch(subMenus.size());
        try {
            for (String subMenu : subMenus) {
//                if (subMenu.equals("ATTACHMENTS")) {
//                    continue;
//                }
                Future<List<ProductInfo>> submit = threadPoolExecutor.submit(new ExecutorThreadRunnable(subUrlMap.get(subMenu)));
//                productInfos = productDetlQuery.getProductDetlByUrls(subUrlMap.get(subMenu));
                futures.add(submit);
            }
            cdl.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        futures.forEach(future -> {
            try {
                productInfos.addAll(future.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        System.err.println(productInfos);
    }


}
