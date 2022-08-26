package team.fs.rubbish.service.impl;

import java.util.List;

import team.fs.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.fs.rubbish.mapper.RubbishBucketMapper;
import team.fs.rubbish.domain.RubbishBucket;
import team.fs.rubbish.service.IRubbishBucketService;

/**
 * 垃圾站管理Service业务层处理
 *
 * @author Dong
 * @date 2022-08-25
 */
@Service
public class RubbishBucketServiceImpl implements IRubbishBucketService {
    @Autowired
    private RubbishBucketMapper rubbishBucketMapper;

    /**
     * 查询垃圾站管理
     *
     * @param id 垃圾站管理主键
     * @return 垃圾站管理
     */
    @Override
    public RubbishBucket selectRubbishBucketById(String id) {
        return rubbishBucketMapper.selectRubbishBucketById(id);
    }

    /**
     * 查询垃圾站管理列表
     *
     * @param rubbishBucket 垃圾站管理
     * @return 垃圾站管理
     */
    @Override
    public List<RubbishBucket> selectRubbishBucketList(RubbishBucket rubbishBucket) {
        return rubbishBucketMapper.selectRubbishBucketList(rubbishBucket);
    }

    /**
     * 新增垃圾站管理
     *
     * @param rubbishBucket 垃圾站管理
     * @return 结果
     */
    @Override
    public int insertRubbishBucket(RubbishBucket rubbishBucket) {
        rubbishBucket.setCreateTime(DateUtils.getNowDate());
        return rubbishBucketMapper.insertRubbishBucket(rubbishBucket);
    }

    /**
     * 修改垃圾站管理
     *
     * @param rubbishBucket 垃圾站管理
     * @return 结果
     */
    @Override
    public int updateRubbishBucket(RubbishBucket rubbishBucket) {
        rubbishBucket.setUpdateTime(DateUtils.getNowDate());
        return rubbishBucketMapper.updateRubbishBucket(rubbishBucket);
    }

    /**
     * 批量删除垃圾站管理
     *
     * @param ids 需要删除的垃圾站管理主键
     * @return 结果
     */
    @Override
    public int deleteRubbishBucketByIds(String[] ids) {
        return rubbishBucketMapper.deleteRubbishBucketByIds(ids);
    }

    /**
     * 删除垃圾站管理信息
     *
     * @param id 垃圾站管理主键
     * @return 结果
     */
    @Override
    public int deleteRubbishBucketById(String id) {
        return rubbishBucketMapper.deleteRubbishBucketById(id);
    }
}
