package team.fs.rubbish.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.fs.common.constant.UserConstants;
import team.fs.common.utils.DateUtils;
import team.fs.common.utils.StringUtils;
import team.fs.rubbish.domain.RubbishList;
import team.fs.rubbish.mapper.RubbishListMapper;
import team.fs.rubbish.service.IRubbishListService;

import java.util.List;

/**
 * 垃圾管理Service业务层处理
 *
 * @author Dong
 * @date 2022-08-23
 */
@Service
public class RubbishListServiceImpl implements IRubbishListService {
    @Autowired
    private RubbishListMapper rubbishListMapper;

    /**
     * 查询垃圾管理
     *
     * @param listId 垃圾管理主键
     * @return 垃圾管理
     */
    @Override
    public RubbishList selectRubbishListByListId(String listId) {
        return rubbishListMapper.selectRubbishListByListId(listId);
    }

    /**
     * 查询垃圾管理列表
     *
     * @param rubbishList 垃圾管理
     * @return 垃圾管理
     */
    @Override
    public List<RubbishList> selectRubbishListList(RubbishList rubbishList) {
        return rubbishListMapper.selectRubbishListList(rubbishList);
    }

    /**
     * 新增垃圾管理
     *
     * @param rubbishList 垃圾管理
     * @return 结果
     */
    @Override
    public int insertRubbishList(RubbishList rubbishList) {
        rubbishList.setCreateTime(DateUtils.getNowDate());
        return rubbishListMapper.insertRubbishList(rubbishList);
    }

    /**
     * 修改垃圾管理
     *
     * @param rubbishList 垃圾管理
     * @return 结果
     */
    @Override
    public int updateRubbishList(RubbishList rubbishList) {
        rubbishList.setUpdateTime(DateUtils.getNowDate());
        return rubbishListMapper.updateRubbishList(rubbishList);
    }

    /**
     * 批量删除垃圾管理
     *
     * @param listIds 需要删除的垃圾管理主键
     * @return 结果
     */
    @Override
    public int deleteRubbishListByListIds(String[] listIds) {
        return rubbishListMapper.deleteRubbishListByListIds(listIds);
    }

    /**
     * 删除垃圾管理信息
     *
     * @param listId 垃圾管理主键
     * @return 结果
     */
    @Override
    public int deleteRubbishListByListId(String listId) {
        return rubbishListMapper.deleteRubbishListByListId(listId);
    }

    @Override
    public RubbishList selectByRubbishName(String rubbishName) {
        return rubbishListMapper.selectByRubbishName(rubbishName);
    }

    @Override
    public String checkRubbishNameUnique(RubbishList rubbishList) {
        Long rubbishId = StringUtils.isNull(rubbishList.getListId())
                ? -1L : rubbishList.getListId();
        RubbishList info = rubbishListMapper.selectByRubbishName(rubbishList.getRubbishName());
        if (StringUtils.isNotNull(info) && info.getListId().longValue() != rubbishId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
