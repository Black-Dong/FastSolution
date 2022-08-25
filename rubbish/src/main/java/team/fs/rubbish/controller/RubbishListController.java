package team.fs.rubbish.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.fs.common.annotation.Anonymous;
import team.fs.common.annotation.Log;
import team.fs.common.constant.UserConstants;
import team.fs.common.core.controller.BaseController;
import team.fs.common.core.domain.AjaxResult;
import team.fs.common.core.page.TableDataInfo;
import team.fs.common.enums.BusinessType;
import team.fs.common.utils.PageUtils;
import team.fs.common.utils.StringUtils;
import team.fs.common.utils.poi.ExcelUtil;
import team.fs.rubbish.domain.RubbishList;
import team.fs.rubbish.mapper.RubbishListMapper;
import team.fs.rubbish.service.IRubbishListService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 垃圾管理Controller
 *
 * @author Dong
 * @date 2022-08-23
 */
@RestController
@RequestMapping("/rubbish/list")
public class RubbishListController extends BaseController {
    @Autowired
    private IRubbishListService rubbishListService;

    @Anonymous
    @GetMapping("/selectByRubbishName/{rubbishName}")
    public AjaxResult selectByRubbishName(@PathVariable("rubbishName")String rubbishName) {
        RubbishList rubbish = rubbishListService.selectByRubbishName(rubbishName);
        return AjaxResult.success(rubbish);
    }

    /**
     * 查询垃圾管理列表
     */
    @PreAuthorize("@ss.hasPermi('rubbish:list:list')")
    @GetMapping("/list")
    public TableDataInfo list(RubbishList rubbishList) {
        startPage();
        List<RubbishList> list = rubbishListService.selectRubbishListList(rubbishList);
        return getDataTable(list);
    }

    /**
     * 导出垃圾管理列表
     */
    @PreAuthorize("@ss.hasPermi('rubbish:list:export')")
    @Log(title = "垃圾管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RubbishList rubbishList) {
        List<RubbishList> list = rubbishListService.selectRubbishListList(rubbishList);
        ExcelUtil<RubbishList> util = new ExcelUtil<RubbishList>(RubbishList.class);
        util.exportExcel(response, list, "垃圾管理数据");
    }

    /**
     * 获取垃圾管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('rubbish:list:query')")
    @GetMapping(value = "/{listId}")
    public AjaxResult getInfo(@PathVariable("listId") String listId) {
        return AjaxResult.success(rubbishListService.selectRubbishListByListId(listId));
    }

    /**
     * 新增垃圾管理
     */
    @PreAuthorize("@ss.hasPermi('rubbish:list:add')")
    @Log(title = "垃圾管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RubbishList rubbishList) {
        if (UserConstants.NOT_UNIQUE.equals(rubbishListService.checkRubbishNameUnique(rubbishList))) {
            return AjaxResult.error("新增垃圾'" + rubbishList.getRubbishName() + "'失败，名称已存在");
        }
        return toAjax(rubbishListService.insertRubbishList(rubbishList));
    }

    /**
     * 修改垃圾管理
     */
    @PreAuthorize("@ss.hasPermi('rubbish:list:edit')")
    @Log(title = "垃圾管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RubbishList rubbishList) {
        if (UserConstants.NOT_UNIQUE.equals(rubbishListService.checkRubbishNameUnique(rubbishList))) {
            return AjaxResult.error("新增垃圾'" + rubbishList.getRubbishName() + "'失败，名称已存在");
        }
        return toAjax(rubbishListService.updateRubbishList(rubbishList));
    }

    /**
     * 删除垃圾管理
     */
    @PreAuthorize("@ss.hasPermi('rubbish:list:remove')")
    @Log(title = "垃圾管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{listIds}")
    public AjaxResult remove(@PathVariable String[] listIds) {
        return toAjax(rubbishListService.deleteRubbishListByListIds(listIds));
    }
}
