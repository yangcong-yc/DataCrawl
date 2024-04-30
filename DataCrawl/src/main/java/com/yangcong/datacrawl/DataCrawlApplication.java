package com.yangcong.datacrawl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataCrawlApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataCrawlApplication.class, args);
    }

}
