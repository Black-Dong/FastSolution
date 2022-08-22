package team.fs.rubbish.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.fs.common.constant.UserConstants;
import team.fs.common.core.domain.entity.SysDept;
import team.fs.common.utils.DateUtils;
import team.fs.common.utils.StringUtils;
import team.fs.rubbish.domain.RubbishCategory;
import team.fs.rubbish.mapper.RubbishCategoryMapper;
import team.fs.rubbish.service.IRubbishCategoryService;

import java.util.List;

/**
 * 分类管理Service业务层处理
 *
 * @author Dong
 * @date 2022-08-22
 */
@Service
public class RubbishCategoryServiceImpl implements IRubbishCategoryService {
    @Autowired
    private RubbishCategoryMapper rubbishCategoryMapper;

    /**
     * 查询分类管理
     *
     * @param categoryId 分类管理主键
     * @return 分类管理
     */
    @Override
    public RubbishCategory selectRubbishCategoryByCategoryId(Long categoryId) {
        return rubbishCategoryMapper.selectRubbishCategoryByCategoryId(categoryId);
    }

    /**
     * 查询分类管理列表
     *
     * @param rubbishCategory 分类管理
     * @return 分类管理
     */
    @Override
    public List<RubbishCategory> selectRubbishCategoryList(RubbishCategory rubbishCategory) {
        return rubbishCategoryMapper.selectRubbishCategoryList(rubbishCategory);
    }

    /**
     * 新增分类管理
     *
     * @param rubbishCategory 分类管理
     * @return 结果
     */
    @Override
    public int insertRubbishCategory(RubbishCategory rubbishCategory) {
        RubbishCategory info = rubbishCategoryMapper.selectRubbishCategoryByCategoryId(rubbishCategory.getParentId());

        rubbishCategory.setCreateTime(DateUtils.getNowDate());
        rubbishCategory.setAncestors(info.getAncestors() + "," + rubbishCategory.getParentId());
        return rubbishCategoryMapper.insertRubbishCategory(rubbishCategory);
    }

    /**
     * 修改分类管理
     *
     * @param rubbishCategory 分类管理
     * @return 结果
     */
    @Override
    public int updateRubbishCategory(RubbishCategory rubbishCategory) {
        RubbishCategory newParentRubbishCategory = rubbishCategoryMapper.selectRubbishCategoryByCategoryId(rubbishCategory.getParentId());
        RubbishCategory oldRubbishCategory = rubbishCategoryMapper.selectRubbishCategoryByCategoryId(rubbishCategory.getCategoryId());
        if (StringUtils.isNotNull(newParentRubbishCategory) && StringUtils.isNotNull(oldRubbishCategory)) {
            String newAncestors = newParentRubbishCategory.getAncestors() + "," + newParentRubbishCategory.getCategoryId();
            String oldAncestors = oldRubbishCategory.getAncestors();
            rubbishCategory.setAncestors(newAncestors);
            updateCategoryChildren(rubbishCategory.getCategoryId(), newAncestors, oldAncestors);
        }
        rubbishCategory.setUpdateTime(DateUtils.getNowDate());
        int result = rubbishCategoryMapper.updateRubbishCategory(rubbishCategory);
        return result;
    }

    /**
     * 修改子元素关系
     *
     * @param categoryId   被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateCategoryChildren(Long categoryId, String newAncestors, String oldAncestors) {
        List<RubbishCategory> children = rubbishCategoryMapper.selectChildrenCategoryById(categoryId);
        for (RubbishCategory child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            rubbishCategoryMapper.updateCategoryChildren(children);
        }
    }

    /**
     * 批量删除分类管理
     *
     * @param categoryIds 需要删除的分类管理主键
     * @return 结果
     */
    @Override
    public int deleteRubbishCategoryByCategoryIds(String[] categoryIds) {
        return rubbishCategoryMapper.deleteRubbishCategoryByCategoryIds(categoryIds);
    }

    /**
     * 删除分类管理信息
     *
     * @param categoryId 分类管理主键
     * @return 结果
     */
    @Override
    public int deleteRubbishCategoryByCategoryId(Long categoryId) {
        return rubbishCategoryMapper.deleteRubbishCategoryByCategoryId(categoryId);
    }

    @Override
    public String checkCategoryNameUnique(RubbishCategory rubbishCategory) {
        Long categoryId = StringUtils.isNull(rubbishCategory.getCategoryId())
                ? -1L : rubbishCategory.getCategoryId();
        RubbishCategory info = rubbishCategoryMapper.checkCategoryNameUnique(rubbishCategory.getCategoryName(), rubbishCategory.getParentId());
        if (StringUtils.isNotNull(info) && info.getCategoryId().longValue() != categoryId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean hasChildByCategoryId(Long categoryId) {

        int result = rubbishCategoryMapper.hasChildByDeptId(categoryId);
        return result > 0;
    }

    @Override
    public boolean checkCategoryExistRubbish(Long categoryId) {
        return false;
    }
}
