package team.fs.rubbish.service;

import java.util.List;
import team.fs.rubbish.domain.RubbishList;

/**
 * 垃圾管理Service接口
 * 
 * @author Dong
 * @date 2022-08-23
 */
public interface IRubbishListService 
{
    /**
     * 查询垃圾管理
     * 
     * @param listId 垃圾管理主键
     * @return 垃圾管理
     */
    public RubbishList selectRubbishListByListId(String listId);

    /**
     * 查询垃圾管理列表
     * 
     * @param rubbishList 垃圾管理
     * @return 垃圾管理集合
     */
    public List<RubbishList> selectRubbishListList(RubbishList rubbishList);

    /**
     * 新增垃圾管理
     * 
     * @param rubbishList 垃圾管理
     * @return 结果
     */
    public int insertRubbishList(RubbishList rubbishList);

    /**
     * 修改垃圾管理
     * 
     * @param rubbishList 垃圾管理
     * @return 结果
     */
    public int updateRubbishList(RubbishList rubbishList);

    /**
     * 批量删除垃圾管理
     * 
     * @param listIds 需要删除的垃圾管理主键集合
     * @return 结果
     */
    public int deleteRubbishListByListIds(String[] listIds);

    /**
     * 删除垃圾管理信息
     * 
     * @param listId 垃圾管理主键
     * @return 结果
     */
    public int deleteRubbishListByListId(String listId);
}
