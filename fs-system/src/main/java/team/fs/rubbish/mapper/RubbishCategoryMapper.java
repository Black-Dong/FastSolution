package team.fs.rubbish.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import team.fs.rubbish.domain.RubbishCategory;

/**
 * 分类管理Mapper接口
 *
 * @author Dong
 * @date 2022-08-22
 */
public interface RubbishCategoryMapper
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
     * 删除分类管理
     *
     * @param categoryId 分类管理主键
     * @return 结果
     */
    public int deleteRubbishCategoryByCategoryId(String categoryId);

    /**
     * 批量删除分类管理
     *
     * @param categoryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRubbishCategoryByCategoryIds(String[] categoryIds);

    List<RubbishCategory> selectChildrenCategoryById(Long categoryId);

    int updateCategoryChildren(@Param("categorys") List<RubbishCategory> children);

    RubbishCategory checkCategoryNameUnique(@Param("categoryName") String categoryName,@Param("parentId") Long parentId);
}
