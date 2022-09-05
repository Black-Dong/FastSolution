package team.fs.rubbish.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import team.fs.common.annotation.Anonymous;
import team.fs.common.annotation.DataScope;
import team.fs.common.annotation.Log;
import team.fs.common.constant.UserConstants;
import team.fs.common.core.controller.BaseController;
import team.fs.common.core.domain.AjaxResult;
import team.fs.common.core.page.TableDataInfo;
import team.fs.common.enums.BusinessType;
import team.fs.common.utils.poi.ExcelUtil;
import team.fs.framework.interceptor.LoggingInterceptor;
import team.fs.rubbish.domain.RubbishList;
import team.fs.rubbish.service.IRubbishListService;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.rubbish.appId}")
    private String appId;

    @Value("${api.rubbish.appSecret}")
    private String appSecret;

    @Anonymous
    @GetMapping("/selectByRubbishName/{rubbishName}")
    public AjaxResult selectByRubbishName(@PathVariable("rubbishName") String rubbishName) {
        RubbishList rubbish = rubbishListService.selectByRubbishName(rubbishName);
        if (Objects.isNull(rubbish)) {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.set("Content-Type", "application/json; charset=utf-8");
            requestHeaders.set("APP_ID", appId);
            requestHeaders.set("APP_SECRET", appSecret);
            HttpEntity requestEntity = new HttpEntity(requestHeaders);
            restTemplate.setInterceptors(Collections.singletonList(new LoggingInterceptor()));
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://www.mxnzp.com/api/rubbish/type?name=" + rubbishName,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );
            JSONObject obj = JSON.parseObject(response.getBody());
            JSONObject data = obj.getJSONObject("data");
            if (Objects.nonNull(data)) {
                rubbish = new RubbishList();
                JSONObject aim = data.getJSONObject("aim");
                rubbish.setRubbishName(aim.getString("goodsName"));
                rubbish.setCategoryName(aim.getString("goodsType"));
            }
        }
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
