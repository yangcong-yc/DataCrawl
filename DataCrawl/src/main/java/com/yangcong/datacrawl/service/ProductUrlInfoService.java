package com.yangcong.datacrawl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yangcong.datacrawl.entity.ProductUrlInfo;

import java.util.List;

/**
* @author admin
* @description 针对表【product_url_info(产品URl访问表)】的数据库操作Service
* @createDate 2024-04-25 09:58:18
*/
public interface ProductUrlInfoService extends IService<ProductUrlInfo> {

    void insertProductUrlInfo(List<ProductUrlInfo> categoryUrls);

    void deleteAllProductUrlInfo();

    List<ProductUrlInfo> selectAllProductUrlInfo();

}
