package team.fs.rubbish.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import team.fs.common.annotation.Excel;
import team.fs.common.core.domain.BaseEntity;

/**
 * 垃圾站管理对象 rubbish_bucket
 *
 * @author Dong
 * @date 2022-08-25
 */
public class RubbishBucket extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String bucketId;

    /** 百度地图:lng */
    @Excel(name = "百度地图:lng")
    private String lng;

    /** 百度地图:lat */
    @Excel(name = "百度地图:lat")
    private String lat;

    /** 垃圾站名称 */
    @Excel(name = "垃圾站名称")
    private String bucketName;

    public void setBucketId(String bucketId)
    {
        this.bucketId = bucketId;
    }

    public String getBucketId()
    {
        return bucketId;
    }
    public void setLng(String lng)
    {
        this.lng = lng;
    }

    public String getLng()
    {
        return lng;
    }
    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public String getLat()
    {
        return lat;
    }
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }

    public String getBucketName()
    {
        return bucketName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("bucketId", getBucketId())
                .append("lng", getLng())
                .append("lat", getLat())
                .append("bucketName", getBucketName())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
