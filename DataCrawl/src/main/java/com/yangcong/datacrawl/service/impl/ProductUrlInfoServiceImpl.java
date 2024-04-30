package com.yangcong.datacrawl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yangcong.datacrawl.entity.ProductUrlInfo;
import com.yangcong.datacrawl.dao.ProductUrlInfoMapper;
import com.yangcong.datacrawl.service.ProductUrlInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author admin
 * @description 针对表【product_url_info(产品URl访问表)】的数据库操作Service实现
 * @createDate 2024-04-25 09:58:18
 */
@Service
public class ProductUrlInfoServiceImpl extends ServiceImpl<ProductUrlInfoMapper, ProductUrlInfo>
        implements ProductUrlInfoService {

    @Autowired
    private ProductUrlInfoMapper productUrlInfoMapper;


    @Override
    public void insertProductUrlInfo(List<ProductUrlInfo> categoryUrls) {
        this.saveBatch(categoryUrls);
    }

    @Override
    public void deleteAllProductUrlInfo() {
        productUrlInfoMapper.deleteAll();
    }

    @Override
    public List<ProductUrlInfo>  selectAllProductUrlInfo() {
        return productUrlInfoMapper.selectAll();

    }
}
