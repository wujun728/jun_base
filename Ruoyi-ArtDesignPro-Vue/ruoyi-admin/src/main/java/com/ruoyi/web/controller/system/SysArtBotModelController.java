package com.ruoyi.web.controller.system;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.SysArtBotModel;
import com.ruoyi.system.service.ISysArtBotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "AI 模型管理")
@RestController
@RequestMapping("/system/artbot/model")
public class SysArtBotModelController extends BaseController
{
    @Autowired
    private ISysArtBotService artBotService;

    @PreAuthorize("@ss.hasPermi('system:artbot:list')")
    @Operation(summary = "查询AI 模型管理列表")
    @GetMapping("/list")
    public TableDataInfo list(SysArtBotModel model)
    {
        startPage();
        return getDataTable(artBotService.selectModelList(model));
    }

    @PreAuthorize("@ss.hasPermi('system:artbot:query')")
    @Operation(summary = "获取AI 模型管理详情")
    @GetMapping("/{modelId}")
    @io.swagger.v3.oas.annotations.Parameter(description = "AI 模型ID")
    public AjaxResult getInfo(@PathVariable Long modelId)
    {
        return success(artBotService.selectModelById(modelId));
    }

    @PreAuthorize("@ss.hasPermi('system:artbot:add')")
    @Log(title = "AI模型", businessType = BusinessType.INSERT)
    @Operation(summary = "新增AI 模型管理")
    @PostMapping
    public AjaxResult add(@RequestBody SysArtBotModel model)
    {
        model.setCreateBy(getUsername());
        return toAjax(artBotService.insertModel(model));
    }

    @PreAuthorize("@ss.hasPermi('system:artbot:edit')")
    @Log(title = "AI模型", businessType = BusinessType.UPDATE)
    @Operation(summary = "修改AI 模型管理")
    @PutMapping
    public AjaxResult edit(@RequestBody SysArtBotModel model)
    {
        model.setUpdateBy(getUsername());
        return toAjax(artBotService.updateModel(model));
    }

    @PreAuthorize("@ss.hasPermi('system:artbot:edit')")
    @Log(title = "AI模型", businessType = BusinessType.UPDATE)
    @Operation(summary = "设置默认 AI 模型")
    @PutMapping("/{modelId}/default")
    @io.swagger.v3.oas.annotations.Parameter(description = "AI 模型ID")
    public AjaxResult setDefault(@PathVariable Long modelId)
    {
        return toAjax(artBotService.setDefaultModel(modelId, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('system:artbot:remove')")
    @Log(title = "AI模型", businessType = BusinessType.DELETE)
    @Operation(summary = "删除AI 模型管理")
    @DeleteMapping("/{modelId}")
    @io.swagger.v3.oas.annotations.Parameter(description = "AI 模型ID")
    public AjaxResult remove(@PathVariable Long modelId)
    {
        return toAjax(artBotService.deleteModel(modelId));
    }

    @PreAuthorize("@ss.hasPermi('system:artbot:test')")
    @Log(title = "AI模型连接测试", businessType = BusinessType.OTHER, isSaveResponseData = false)
    @Operation(summary = "测试 AI 模型连接")
    @PostMapping("/{modelId}/test")
    @io.swagger.v3.oas.annotations.Parameter(description = "AI 模型ID")
    public AjaxResult test(@PathVariable Long modelId)
    {
        SysArtBotModel model = artBotService.selectModelSecretById(modelId);
        if (model == null)
        {
            return error("模型不存在");
        }
        try
        {
            JSONObject body = new JSONObject();
            body.put("model", model.getModelCode());
            body.put("stream", false);
            body.put("max_tokens", 8);
            JSONArray messages = new JSONArray();
            messages.add(JSONObject.of("role", "user", "content", "ping"));
            body.put("messages", messages);
            HttpResponse<String> response = client().send(request(model, body), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300)
            {
                return error("连接失败：" + upstreamError(response));
            }
            return success("连接成功");
        }
        catch (Exception e)
        {
            throw new ServiceException("连接失败：" + e.getMessage());
        }
    }

    private static HttpClient client()
    {
        return HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    }

    private static HttpRequest request(SysArtBotModel model, JSONObject body)
    {
        return HttpRequest.newBuilder(URI.create(endpoint(model.getBaseUrl())))
            .timeout(Duration.ofSeconds(60))
            .header("Authorization", "Bearer " + model.getApiKey())
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body.toJSONString()))
            .build();
    }

    public static String endpoint(String baseUrl)
    {
        String normalized = baseUrl.replaceAll("/+$", "");
        return normalized.endsWith("/chat/completions") ? normalized : normalized + "/chat/completions";
    }

    public static String upstreamError(HttpResponse<String> response)
    {
        String body = response.body();
        return "HTTP " + response.statusCode() + (body == null || body.isBlank() ? "" : " - " + body);
    }
}
