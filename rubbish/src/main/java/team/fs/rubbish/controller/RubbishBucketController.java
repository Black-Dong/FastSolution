package team.fs.rubbish.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.fs.common.annotation.Log;
import team.fs.common.core.controller.BaseController;
import team.fs.common.core.domain.AjaxResult;
import team.fs.common.enums.BusinessType;
import team.fs.rubbish.domain.RubbishBucket;
import team.fs.rubbish.service.IRubbishBucketService;
import team.fs.common.utils.poi.ExcelUtil;
import team.fs.common.core.page.TableDataInfo;

/**
 * 垃圾站管理Controller
 *
 * @author Dong
 * @date 2022-08-25
 */
@RestController
@RequestMapping("/rubbish/bucket")
public class RubbishBucketController extends BaseController {
    @Autowired
    private IRubbishBucketService rubbishBucketService;

    /**
     * 查询垃圾站管理列表
     */
    @PreAuthorize("@ss.hasPermi('rubbish:bucket:list')")
    @GetMapping("/list")
    public TableDataInfo list(RubbishBucket rubbishBucket) {
        // startPage();
        List<RubbishBucket> list = rubbishBucketService.selectRubbishBucketList(rubbishBucket);
        return getDataTable(list);
    }

    /**
     * 导出垃圾站管理列表
     */
    @PreAuthorize("@ss.hasPermi('rubbish:bucket:export')")
    @Log(title = "垃圾站管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RubbishBucket rubbishBucket) {
        List<RubbishBucket> list = rubbishBucketService.selectRubbishBucketList(rubbishBucket);
        ExcelUtil<RubbishBucket> util = new ExcelUtil<RubbishBucket>(RubbishBucket.class);
        util.exportExcel(response, list, "垃圾站管理数据");
    }

    /**
     * 获取垃圾站管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('rubbish:bucket:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(rubbishBucketService.selectRubbishBucketById(id));
    }

    /**
     * 新增垃圾站管理
     */
    @PreAuthorize("@ss.hasPermi('rubbish:bucket:add')")
    @Log(title = "垃圾站管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RubbishBucket rubbishBucket) {
        return toAjax(rubbishBucketService.insertRubbishBucket(rubbishBucket));
    }

    /**
     * 修改垃圾站管理
     */
    @PreAuthorize("@ss.hasPermi('rubbish:bucket:edit')")
    @Log(title = "垃圾站管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RubbishBucket rubbishBucket) {
        return toAjax(rubbishBucketService.updateRubbishBucket(rubbishBucket));
    }

    /**
     * 删除垃圾站管理
     */
    @PreAuthorize("@ss.hasPermi('rubbish:bucket:remove')")
    @Log(title = "垃圾站管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(rubbishBucketService.deleteRubbishBucketByIds(ids));
    }
}
