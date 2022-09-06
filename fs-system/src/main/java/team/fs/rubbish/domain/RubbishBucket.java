package team.fs.rubbish.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import team.fs.common.annotation.Excel;
import team.fs.common.core.domain.BaseEntity;

import java.util.Date;

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

    private String applyUserId;
    private String applyUserName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;
    private String instanceId;
    private String processKey;


    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

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
                .append("applyUserId", getApplyUserId())
                .append("applyUserName", getApplyUserName())
                .append("applyTime", getApplyTime())
                .append("instanceId", getInstanceId())
                .append("processKey", getProcessKey())
                .toString();
    }
}
