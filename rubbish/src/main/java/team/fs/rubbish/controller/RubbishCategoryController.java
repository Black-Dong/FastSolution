package team.fs.rubbish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.fs.common.annotation.Log;
import team.fs.common.core.controller.BaseController;
import team.fs.common.core.domain.AjaxResult;
import team.fs.common.core.page.TableDataInfo;
import team.fs.common.enums.BusinessType;
import team.fs.common.utils.poi.ExcelUtil;
import team.fs.rubbish.domain.RubbishCategory;
import team.fs.rubbish.service.IRubbishCategoryService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分类管理Controller
 *
 * @author Dong
 * @date 2022-08-22
 */
@RestController
@RequestMapping("/rubbish/category")
public class RubbishCategoryController extends BaseController {
    @Autowired
    private IRubbishCategoryService rubbishCategoryService;

    /**
     * 查询分类管理列表
     */
    @PreAuthorize("@ss.hasPermi('rubbish:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(RubbishCategory rubbishCategory) {
        startPage();
        List<RubbishCategory> list = rubbishCategoryService.selectRubbishCategoryList(rubbishCategory);
        return getDataTable(list);
    }

    /**
     * 导出分类管理列表
     */
    @PreAuthorize("@ss.hasPermi('rubbish:category:export')")
    @Log(title = "分类管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RubbishCategory rubbishCategory) {
        List<RubbishCategory> list = rubbishCategoryService.selectRubbishCategoryList(rubbishCategory);
        ExcelUtil<RubbishCategory> util = new ExcelUtil<RubbishCategory>(RubbishCategory.class);
        util.exportExcel(response, list, "分类管理数据");
    }

    /**
     * 获取分类管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('rubbish:category:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") String categoryId) {
        return AjaxResult.success(rubbishCategoryService.selectRubbishCategoryByCategoryId(categoryId));
    }

    /**
     * 新增分类管理
     */
    @PreAuthorize("@ss.hasPermi('rubbish:category:add')")
    @Log(title = "分类管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RubbishCategory rubbishCategory) {
        return toAjax(rubbishCategoryService.insertRubbishCategory(rubbishCategory));
    }

    /**
     * 修改分类管理
     */
    @PreAuthorize("@ss.hasPermi('rubbish:category:edit')")
    @Log(title = "分类管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RubbishCategory rubbishCategory) {
        return toAjax(rubbishCategoryService.updateRubbishCategory(rubbishCategory));
    }

    /**
     * 删除分类管理
     */
    @PreAuthorize("@ss.hasPermi('rubbish:category:remove')")
    @Log(title = "分类管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable String[] categoryIds) {
        return toAjax(rubbishCategoryService.deleteRubbishCategoryByCategoryIds(categoryIds));
    }
}
