package com.yangcong.datacrawl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yangcong.datacrawl.entity.ProductInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author admin
* @description 针对表【product_info(产品信息表)】的数据库操作Service
* @createDate 2024-04-24 13:20:31
*/
public interface ProductInfoService extends IService<ProductInfo> {

    List<ProductInfo> getAllProductInfo();

    void inserytProductInfo(ProductInfo productInfo);

    void updateProductInfo(ProductInfo productInfo);

    void deleteAllProductInfo();

    void deleteAllProductUrlInfo();

    @Transactional
    Integer insertBatch(List<ProductInfo> allProductInfos);

    List<ProductInfo> selectDataByProductName(String productName);

    void deleteDistinctData(List<ProductInfo> productInfos);

    List<ProductInfo> getNoBrandProduct();
    List<ProductInfo> getBrandProduct();
}
