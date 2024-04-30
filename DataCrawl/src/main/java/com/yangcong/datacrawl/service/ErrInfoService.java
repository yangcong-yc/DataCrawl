package com.yangcong.datacrawl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yangcong.datacrawl.entity.ErrInfo;
import com.yangcong.datacrawl.entity.ProductInfo;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ErrInfoService extends IService<ProductInfo> {
    void insertBatch(List<ErrInfo> errInfos);
}
