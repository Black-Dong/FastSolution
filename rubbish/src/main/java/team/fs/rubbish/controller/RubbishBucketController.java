package team.fs.rubbish.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.fs.activiti.service.IProcessService;
import team.fs.common.annotation.Anonymous;
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

    @Autowired
    private IProcessService processService;

    /**
     * 查询垃圾站管理列表
     */
    // @PreAuthorize("@ss.hasAnyPermi('rubbish:bucket:list,rubbish:bucket:manage')")
    @GetMapping("/list")
    @Anonymous
    public TableDataInfo list(RubbishBucket rubbishBucket) {
        // startPage();
        List<RubbishBucket> list = rubbishBucketService.selectRubbishBucketList(rubbishBucket);
        return getDataTable(list);
    }

    /**
     * 导出垃圾站管理列表
     */
    @PreAuthorize("@ss.hasAnyPermi('rubbish:bucket:export,rubbish:bucket:manage')")
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
    @PreAuthorize("@ss.hasAnyPermi('rubbish:bucket:query,rubbish:bucket:manage')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(rubbishBucketService.selectRubbishBucketById(id));
    }

    /**
     * 新增垃圾站管理
     */
    @PreAuthorize("@ss.hasAnyPermi('rubbish:bucket:add,rubbish:bucket:manage')")
    @Log(title = "垃圾站管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RubbishBucket rubbishBucket) {
        return toAjax(rubbishBucketService.insertRubbishBucket(rubbishBucket));
    }

    /**
     * 修改垃圾站管理
     */
    @PreAuthorize("@ss.hasAnyPermi('rubbish:bucket:edit,rubbish:bucket:manage')")
    @Log(title = "垃圾站管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RubbishBucket rubbishBucket) {
        return toAjax(rubbishBucketService.updateRubbishBucket(rubbishBucket));
    }

    /**
     * 删除垃圾站管理
     */
    @PreAuthorize("@ss.hasAnyPermi('rubbish:bucket:remove,rubbish:bucket:manage')")
    @Log(title = "垃圾站管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(rubbishBucketService.deleteRubbishBucketByIds(ids));
    }


    /**
     * 提交申请
     */
    @Log(title = "请假业务", businessType = BusinessType.UPDATE)
    @PostMapping( "/submitApply/{id}")
    @ResponseBody
    public AjaxResult submitApply(@PathVariable String id) {
        try {
            RubbishBucket bucket = rubbishBucketService.selectRubbishBucketById(id);
            processService.submitApply(bucket, "test", "getBucketId");
            rubbishBucketService.updateRubbishBucket(bucket);
        } catch (Exception e) {
            e.printStackTrace();
            return error("提交申请出错：" + e.getMessage());
        }
        return success();
    }
}
