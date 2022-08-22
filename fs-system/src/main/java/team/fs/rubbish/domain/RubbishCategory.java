package team.fs.rubbish.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import team.fs.common.annotation.Excel;
import team.fs.common.core.domain.BaseEntity;

/**
 * 分类管理对象 rubbish_category
 *
 * @author Dong
 * @date 2022-08-22
 */
public class RubbishCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 垃圾分类id */
    private String categoryId;

    /** 父id */
    @Excel(name = "父id")
    private String parentId;

    /** 祖级id */
    private String ancestors;

    /** 垃圾分类名称 */
    @Excel(name = "垃圾分类名称")
    private String categoryName;

    /** 处置方式 */
    @Excel(name = "处置方式")
    private String disposalWay;

    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getCategoryId()
    {
        return categoryId;
    }
    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public String getParentId()
    {
        return parentId;
    }
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getAncestors() {
        return ancestors;
    }
    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
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
            .append("categoryId", getCategoryId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
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
