package team.fs.rubbish.controller;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.fs.common.annotation.Log;
import team.fs.common.constant.UserConstants;
import team.fs.common.core.controller.BaseController;
import team.fs.common.core.domain.AjaxResult;
import team.fs.common.core.domain.entity.SysDept;
import team.fs.common.core.page.TableDataInfo;
import team.fs.common.enums.BusinessType;
import team.fs.common.utils.StringUtils;
import team.fs.common.utils.poi.ExcelUtil;
import team.fs.rubbish.domain.RubbishCategory;
import team.fs.rubbish.service.IRubbishCategoryService;

import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
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
    public AjaxResult list(RubbishCategory rubbishCategory) {
        List<RubbishCategory> list = rubbishCategoryService.selectRubbishCategoryList(rubbishCategory);
        return AjaxResult.success(list);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('rubbish:category:list')")
    @GetMapping("/list/exclude/{categoryId}")
    public AjaxResult excludeChild(@PathVariable(value = "categoryId", required = false) Long categoryId) {
        List<RubbishCategory> categoryList = rubbishCategoryService.selectRubbishCategoryList(new RubbishCategory());
        Iterator<RubbishCategory> it = categoryList.iterator();
        while (it.hasNext()) {
            RubbishCategory d = (RubbishCategory) it.next();
            if (d.getCategoryId().intValue() == categoryId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), categoryId + "")) {
                it.remove();
            }
        }
        return AjaxResult.success(categoryList);
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
    public AjaxResult getInfo(@PathVariable("categoryId") Long categoryId) {
        return AjaxResult.success(rubbishCategoryService.selectRubbishCategoryByCategoryId(categoryId));
    }

    /**
     * 新增分类管理
     */
    @PreAuthorize("@ss.hasPermi('rubbish:category:add')")
    @Log(title = "分类管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RubbishCategory rubbishCategory) {
        if (UserConstants.NOT_UNIQUE.equals(rubbishCategoryService.checkCategoryNameUnique(rubbishCategory))) {
            return AjaxResult.error("新增垃圾分类'" + rubbishCategory.getCategoryName() + "'失败，分类名称已存在");
        }
        rubbishCategory.setCreateBy(getUsername());
        return toAjax(rubbishCategoryService.insertRubbishCategory(rubbishCategory));
    }

    /**
     * 修改分类管理
     */
    @PreAuthorize("@ss.hasPermi('rubbish:category:edit')")
    @Log(title = "分类管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RubbishCategory rubbishCategory) {
        Long categoryId = rubbishCategory.getCategoryId();
        // rubbishCategoryService.checkDeptDataScope(categoryId);
        if (UserConstants.NOT_UNIQUE.equals(rubbishCategoryService.checkCategoryNameUnique(rubbishCategory))) {
            return AjaxResult.error("修改垃圾分类'" + rubbishCategory.getCategoryName() + "'失败，分类名称已存在");
        } else if (rubbishCategory.getParentId().equals(categoryId)) {
            return AjaxResult.error("修改垃圾分类'" + rubbishCategory.getCategoryName() + "'失败，上级部门不能是自己");
        } /* else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0) {
            return AjaxResult.error("该部门包含未停用的子部门！");
        } */
        rubbishCategory.setUpdateBy(getUsername());
        return toAjax(rubbishCategoryService.updateRubbishCategory(rubbishCategory));
    }

    /**
     * 删除分类管理
     */
    @PreAuthorize("@ss.hasPermi('rubbish:category:remove')")
    @Log(title = "分类管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryId}")
    public AjaxResult remove(@PathVariable Long categoryId) {
        if (rubbishCategoryService.hasChildByCategoryId(categoryId)) {
            return AjaxResult.error("存在下级分类,不允许删除");
        }
        if (rubbishCategoryService.checkCategoryExistRubbish(categoryId)) {
            return AjaxResult.error("分类下存在垃圾信息,不允许删除");
        }
        return toAjax(rubbishCategoryService.deleteRubbishCategoryByCategoryId(categoryId));
    }
}
