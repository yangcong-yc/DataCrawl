package com.yangcong.datacrawl.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 错误信息表
 * @TableName err_info
 */
@Data
public class ErrInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 错误产品详情url
     */
    private String errDetlUrl;
    /**
     * 错误信息
     */
    private String errMessage;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrInfo)) return false;
        ErrInfo errInfo = (ErrInfo) o;
        return Objects.equals(id, errInfo.id) && Objects.equals(errMessage, errInfo.errMessage) && Objects.equals(createdAt, errInfo.createdAt) && Objects.equals(updatedAt, errInfo.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, errMessage, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "ErrInfo{" +
                "id=" + id +
                ", errMessage='" + errMessage + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}