package team.fs.rubbish.service;

import java.util.List;
import team.fs.rubbish.domain.RubbishCategory;

/**
 * 分类管理Service接口
 *
 * @author Dong
 * @date 2022-08-22
 */
public interface IRubbishCategoryService
{
    /**
     * 查询分类管理
     *
     * @param categoryId 分类管理主键
     * @return 分类管理
     */
    public RubbishCategory selectRubbishCategoryByCategoryId(Long categoryId);

    /**
     * 查询分类管理列表
     *
     * @param rubbishCategory 分类管理
     * @return 分类管理集合
     */
    public List<RubbishCategory> selectRubbishCategoryList(RubbishCategory rubbishCategory);

    /**
     * 新增分类管理
     *
     * @param rubbishCategory 分类管理
     * @return 结果
     */
    public int insertRubbishCategory(RubbishCategory rubbishCategory);

    /**
     * 修改分类管理
     *
     * @param rubbishCategory 分类管理
     * @return 结果
     */
    public int updateRubbishCategory(RubbishCategory rubbishCategory);

    /**
     * 批量删除分类管理
     *
     * @param categoryIds 需要删除的分类管理主键集合
     * @return 结果
     */
    public int deleteRubbishCategoryByCategoryIds(String[] categoryIds);

    /**
     * 删除分类管理信息
     *
     * @param categoryId 分类管理主键
     * @return 结果
     */
    public int deleteRubbishCategoryByCategoryId(Long categoryId);

    String checkCategoryNameUnique(RubbishCategory rubbishCategory);

    boolean hasChildByCategoryId(Long categoryId);

    boolean checkCategoryExistRubbish(Long categoryId);
}
