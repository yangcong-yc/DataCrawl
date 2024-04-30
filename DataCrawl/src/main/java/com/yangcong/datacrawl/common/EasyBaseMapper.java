package com.yangcong.datacrawl.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yangcong.datacrawl.entity.ProductInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface EasyBaseMapper<T> extends BaseMapper<T> {


    /**
     * 批量插入 仅适用于mysql
     *
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<T> entityList);

    void deleteDistinctData(List<ProductInfo> productInfos);
}
