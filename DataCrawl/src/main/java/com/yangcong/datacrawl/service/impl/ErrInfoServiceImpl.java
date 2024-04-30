package com.yangcong.datacrawl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yangcong.datacrawl.dao.ErrInfoMapper;
import com.yangcong.datacrawl.dao.ProductInfoMapper;
import com.yangcong.datacrawl.entity.ErrInfo;
import com.yangcong.datacrawl.entity.ProductInfo;
import com.yangcong.datacrawl.service.ErrInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ErrInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ErrInfoService {

    @Autowired
    private ErrInfoMapper errInfoMapper;



    @Override
    public void insertBatch(List<ErrInfo> errInfos) {
        errInfoMapper.insertBatchSomeColumn(errInfos);
    }
}
