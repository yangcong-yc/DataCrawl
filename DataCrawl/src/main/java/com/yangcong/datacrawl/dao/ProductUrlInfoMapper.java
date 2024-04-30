package com.yangcong.datacrawl.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yangcong.datacrawl.common.EasyBaseMapper;
import com.yangcong.datacrawl.entity.ProductUrlInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author admin
* @description 针对表【product_url_info(产品URl访问表)】的数据库操作Mapper
* @createDate 2024-04-25 09:58:18
* @Entity generator.com/yangcong/datacrawl/entity.ProductUrlInfo
*/
@Mapper
public interface ProductUrlInfoMapper extends EasyBaseMapper<ProductUrlInfo> {


    void deleteAll();

    List<ProductUrlInfo> selectAll();
}
