package team.fs.rubbish.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import team.fs.common.annotation.Excel;
import team.fs.common.core.domain.BaseEntity;

/**
 * 垃圾管理对象 rubbish_list
 * 
 * @author Dong
 * @date 2022-08-23
 */
public class RubbishList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 垃圾列表id */
    private String listId;

    /** 垃圾图片 */
    @Excel(name = "垃圾图片")
    private String rubbishUrl;

    /** 垃圾名称 */
    @Excel(name = "垃圾名称")
    private String rubbishName;

    /** 垃圾分类id */
    @Excel(name = "垃圾分类id")
    private Long categoryId;

    /** 垃圾分类名称 */
    @Excel(name = "垃圾分类名称")
    private String categoryName;

    /** 处置方式 */
    @Excel(name = "处置方式")
    private String disposalWay;

    public void setListId(String listId) 
    {
        this.listId = listId;
    }

    public String getListId() 
    {
        return listId;
    }
    public void setRubbishUrl(String rubbishUrl) 
    {
        this.rubbishUrl = rubbishUrl;
    }

    public String getRubbishUrl() 
    {
        return rubbishUrl;
    }
    public void setRubbishName(String rubbishName) 
    {
        this.rubbishName = rubbishName;
    }

    public String getRubbishName() 
    {
        return rubbishName;
    }
    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }
    public void setCategoryName(String categoryName) 
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName() 
    {
        return categoryName;
    }
    public void setDisposalWay(String disposalWay) 
    {
        this.disposalWay = disposalWay;
    }

    public String getDisposalWay() 
    {
        return disposalWay;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("listId", getListId())
            .append("rubbishUrl", getRubbishUrl())
            .append("rubbishName", getRubbishName())
            .append("categoryId", getCategoryId())
            .append("categoryName", getCategoryName())
            .append("disposalWay", getDisposalWay())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
