package team.fs.rubbish.service.impl;

import java.util.List;

import team.fs.common.core.domain.entity.SysDept;
import team.fs.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.fs.rubbish.mapper.RubbishCategoryMapper;
import team.fs.rubbish.domain.RubbishCategory;
import team.fs.rubbish.service.IRubbishCategoryService;

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
    public RubbishCategory selectRubbishCategoryByCategoryId(String categoryId) {
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
        rubbishCategory.setUpdateTime(DateUtils.getNowDate());
        return rubbishCategoryMapper.updateRubbishCategory(rubbishCategory);
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
    public int deleteRubbishCategoryByCategoryId(String categoryId) {
        return rubbishCategoryMapper.deleteRubbishCategoryByCategoryId(categoryId);
    }
}
