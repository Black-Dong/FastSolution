package team.fs.activiti.controller;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.fs.activiti.domain.BizExampleDemo;
import team.fs.activiti.service.IBizExampleDemoService;
import team.fs.activiti.service.IProcessService;
import team.fs.common.annotation.Log;
import team.fs.common.core.controller.BaseController;
import team.fs.common.core.domain.AjaxResult;
import team.fs.common.core.page.TableDataInfo;
import team.fs.common.enums.BusinessType;
import team.fs.common.utils.poi.ExcelUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 示例DemoController
 *
 * @author 一只闲鹿
 * @date 2020-11-25
 */
@RestController
@RequestMapping("/example/demo")
@AllArgsConstructor
public class BizExampleDemoController extends BaseController {
    private IBizExampleDemoService bizExampleDemoService;
    private IProcessService processService;

    /**
     * 查询示例Demo列表
     */
    @PreAuthorize("@ss.hasPermi('example:demo:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizExampleDemo bizExampleDemo) {
        startPage();
        List<BizExampleDemo> list = bizExampleDemoService.selectBizExampleDemoList(bizExampleDemo);
        return getDataTable(list);
    }

    /**
     * 导出示例Demo列表
     */
    @PreAuthorize("@ss.hasPermi('example:demo:export')")
    @Log(title = "示例Demo", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BizExampleDemo bizExampleDemo) {
        List<BizExampleDemo> list = bizExampleDemoService.selectBizExampleDemoList(bizExampleDemo);
        ExcelUtil<BizExampleDemo> util = new ExcelUtil<BizExampleDemo>(BizExampleDemo.class);
        return util.exportExcel(list, "demo");
    }

    /**
     * 获取示例Demo详细信息
     */
    @PreAuthorize("@ss.hasPermi('example:demo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(bizExampleDemoService.selectBizExampleDemoById(id));
    }

    /**
     * 新增示例Demo
     */
    @PreAuthorize("@ss.hasPermi('example:demo:add')")
    @Log(title = "示例Demo", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizExampleDemo bizExampleDemo) {
        return toAjax(bizExampleDemoService.insertBizExampleDemo(bizExampleDemo));
    }

    /**
     * 修改示例Demo
     */
    @PreAuthorize("@ss.hasPermi('example:demo:edit')")
    @Log(title = "示例Demo", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizExampleDemo bizExampleDemo) {
        return toAjax(bizExampleDemoService.updateBizExampleDemo(bizExampleDemo));
    }

    /**
     * 删除示例Demo
     */
    @PreAuthorize("@ss.hasPermi('example:demo:remove')")
    @Log(title = "示例Demo", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizExampleDemoService.deleteBizExampleDemoByIds(ids));
    }

    /**
     * 提交申请
     */
    @Log(title = "示例Demo", businessType = BusinessType.UPDATE)
    @PostMapping("/submitApply/{id}")
    @ResponseBody
    public AjaxResult submitApply(@PathVariable Long id, String variablesStr) {
        try {
            System.out.println("variables: " + variablesStr);
            Map<String, Object> vars = (Map<String, Object>) JSON.parse(variablesStr);
            String[] users = vars.get("users").toString().split(",");
            BizExampleDemo bizExampleDemo = bizExampleDemoService.selectBizExampleDemoById(id);
            Map<String, Object> variables = new HashMap<>();
            if (users.length > 0) {
                Object value = Arrays.asList(users);
                variables.put("users", value);
            }
            Boolean gytd = "1".equals(vars.get("gytd").toString());
            variables.put("gytd", gytd);
            processService.submitApply(bizExampleDemo, "exampleDemo", variables);
            bizExampleDemoService.updateBizExampleDemo(bizExampleDemo);
        } catch (Exception e) {
            e.printStackTrace();
            return error("提交申请出错：" + e.getMessage());
        }
        return success();
    }
}
