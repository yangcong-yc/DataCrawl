package com.yangcong.datacrawl.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yangcong.datacrawl.common.EasyBaseMapper;
import com.yangcong.datacrawl.entity.ProductInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author admin
* @description 针对表【product_info(产品信息表)】的数据库操作Mapper
* @createDate 2024-04-24 13:20:30
* @Entity generator.domain.ProductInfo
*/
@Mapper
public interface ProductInfoMapper extends EasyBaseMapper<ProductInfo> {

    List<ProductInfo> selectAll();

    void deleteAllProductUrlInfo();

    void deleteAllProductInfo();

    List<ProductInfo> selectDataByProductName(@Param("productName") String productName);

    Integer insertBatch(@Param("allProductInfos") List<ProductInfo> allProductInfos);

    List<ProductInfo> getNoBrandProduct();

    List<ProductInfo> getBrandProduct();
}




