package com.yangcong.datacrawl.thread;

import com.yangcong.datacrawl.crawl.ProductDetlQuery;
import com.yangcong.datacrawl.entity.ProductInfo;
import com.yangcong.datacrawl.entity.ProductUrlInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;

@Component
public class ExecutorThreadRunnable implements Callable<List<ProductInfo>> {
   private List<ProductUrlInfo> productUrlInfos;

    @Autowired
    private ProductDetlQuery productDetlQuery;
    public ExecutorThreadRunnable(List<ProductUrlInfo> productUrlInfos) {
        this.productUrlInfos = productUrlInfos;
    }

    @Override
    public List<ProductInfo> call() throws Exception {
        return null;
    }
}
