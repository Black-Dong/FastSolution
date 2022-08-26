package team.fs.rubbish.service;

import java.util.List;
import team.fs.rubbish.domain.RubbishBucket;

/**
 * 垃圾站管理Service接口
 *
 * @author Dong
 * @date 2022-08-25
 */
public interface IRubbishBucketService
{
    /**
     * 查询垃圾站管理
     *
     * @param id 垃圾站管理主键
     * @return 垃圾站管理
     */
    public RubbishBucket selectRubbishBucketById(String id);

    /**
     * 查询垃圾站管理列表
     *
     * @param rubbishBucket 垃圾站管理
     * @return 垃圾站管理集合
     */
    public List<RubbishBucket> selectRubbishBucketList(RubbishBucket rubbishBucket);

    /**
     * 新增垃圾站管理
     *
     * @param rubbishBucket 垃圾站管理
     * @return 结果
     */
    public int insertRubbishBucket(RubbishBucket rubbishBucket);

    /**
     * 修改垃圾站管理
     *
     * @param rubbishBucket 垃圾站管理
     * @return 结果
     */
    public int updateRubbishBucket(RubbishBucket rubbishBucket);

    /**
     * 批量删除垃圾站管理
     *
     * @param ids 需要删除的垃圾站管理主键集合
     * @return 结果
     */
    public int deleteRubbishBucketByIds(String[] ids);

    /**
     * 删除垃圾站管理信息
     *
     * @param id 垃圾站管理主键
     * @return 结果
     */
    public int deleteRubbishBucketById(String id);
}
