package com.yangcong.datacrawl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yangcong.datacrawl.service.ProductInfoService;
import com.yangcong.datacrawl.dao.ProductInfoMapper;
import com.yangcong.datacrawl.entity.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author admin
* @description 针对表【product_info(产品信息表)】的数据库操作Service实现
* @createDate 2024-04-24 13:20:31
*/
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo>
    implements ProductInfoService{

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> getAllProductInfo() {
        return productInfoMapper.selectAll();
    }

    @Override
    public void inserytProductInfo(ProductInfo productInfo) {
        productInfoMapper.insert(productInfo);
    }

    @Override
    public void updateProductInfo(ProductInfo productInfo) {
        productInfoMapper.updateById(productInfo);
    }

    @Override
    public void deleteAllProductInfo() {
        productInfoMapper.deleteAllProductInfo();
    }

    @Override
    public void deleteAllProductUrlInfo() {
        productInfoMapper.deleteAllProductUrlInfo();
    }

    @Override
    public Integer insertBatch(List<ProductInfo> allProductInfos) {
       return productInfoMapper.insertBatch(allProductInfos);
    }

    @Override
    public List<ProductInfo> selectDataByProductName(String productName) {
        return productInfoMapper.selectDataByProductName(productName);
    }

    @Override
    public void deleteDistinctData(List<ProductInfo> productInfos) {
        productInfoMapper.deleteBatchIds(productInfos.stream().map(ProductInfo::getId).collect(Collectors.toList()));
    }

    @Override
    public List<ProductInfo> getNoBrandProduct() {
        return productInfoMapper.getNoBrandProduct();
    }

    @Override
    public List<ProductInfo> getBrandProduct() {
        return productInfoMapper.getBrandProduct();
    }
}




