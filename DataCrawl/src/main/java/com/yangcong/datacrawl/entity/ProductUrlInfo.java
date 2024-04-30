package com.yangcong.datacrawl.entity;


import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 产品URl访问表
 * @TableName product_url_info
 */
@Data
public class ProductUrlInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 一级目录
     */
    private String firstDirectory;

    /**
     * 二级目录
     */
    private String secondDirectory;

    /**
     * 三级目录
     */
    private String threeDirectory;

    /**
     * 四级目录
     */
    private String fourDirectory;

    /**
     * 目录url地址
     */
    private String directoryUrl;
    /**
     * 产品所有品牌
     */
    private String brand;

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
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ProductUrlInfo other = (ProductUrlInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFirstDirectory() == null ? other.getFirstDirectory() == null : this.getFirstDirectory().equals(other.getFirstDirectory()))
            && (this.getSecondDirectory() == null ? other.getSecondDirectory() == null : this.getSecondDirectory().equals(other.getSecondDirectory()))
            && (this.getThreeDirectory() == null ? other.getThreeDirectory() == null : this.getThreeDirectory().equals(other.getThreeDirectory()))
            && (this.getFourDirectory() == null ? other.getFourDirectory() == null : this.getFourDirectory().equals(other.getFourDirectory()))
            && (this.getDirectoryUrl() == null ? other.getDirectoryUrl() == null : this.getDirectoryUrl().equals(other.getDirectoryUrl()))
            && (this.getBrand() == null ? other.getBrand() == null : this.getBrand().equals(other.getBrand()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFirstDirectory() == null) ? 0 : getFirstDirectory().hashCode());
        result = prime * result + ((getSecondDirectory() == null) ? 0 : getSecondDirectory().hashCode());
        result = prime * result + ((getThreeDirectory() == null) ? 0 : getThreeDirectory().hashCode());
        result = prime * result + ((getFourDirectory() == null) ? 0 : getFourDirectory().hashCode());
        result = prime * result + ((getDirectoryUrl() == null) ? 0 : getDirectoryUrl().hashCode());
        result = prime * result + ((getBrand() == null) ? 0 : getBrand().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", firstDirectory=").append(firstDirectory);
        sb.append(", secondDirectory=").append(secondDirectory);
        sb.append(", threeDirectory=").append(threeDirectory);
        sb.append(", fourDirectory=").append(fourDirectory);
        sb.append(", directoryUrl=").append(directoryUrl);
        sb.append(", brand=").append(brand);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}