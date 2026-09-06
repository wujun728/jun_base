package com.ruoyi.ai.controller;

import java.util.List;

import com.ruoyi.ai.utils.FileParseUtil;
import com.ruoyi.ai.utils.RuoyiDocumentSplitter;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.K;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.ai.domain.KnowledgeBaseLine;
import com.ruoyi.ai.service.IKnowledgeBaseLineService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 知识库单栏Controller
 * 
 * @author 欧阳日峰
 * @date 2025-04-10
 */
@RestController
@RequestMapping("/ai/knowledgeBaseLine")
public class KnowledgeBaseLineController extends BaseController
{
    @Autowired
    private IKnowledgeBaseLineService knowledgeBaseLineService;

    @Autowired(required = false)
    @org.springframework.beans.factory.annotation.Qualifier("inMemoryEmbeddingStore")
    private EmbeddingStore embeddingStore;
    /**
     * 查询知识库单栏列表
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseLine:list')")
    @GetMapping("/list")
    public TableDataInfo list(KnowledgeBaseLine knowledgeBaseLine)
    {
        startPage();
        List<KnowledgeBaseLine> list = knowledgeBaseLineService.selectKnowledgeBaseLineList(knowledgeBaseLine);
        return getDataTable(list);
    }


    /**
     * 知识库单栏向量化
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseLine:edit')")
    @GetMapping("/withDocumentStorageByPg/{id}")
    public AjaxResult withDocumentStorageByPg(@PathVariable("id") Long id)
    {
        KnowledgeBaseLine knowledgeBaseLine = knowledgeBaseLineService.selectKnowledgeBaseLineById(id);
        String context = knowledgeBaseLine.getContext();
        if (embeddingStore == null) {
            return AjaxResult.error("未配置向量数据库(PgVector)，无法向量化");
        }
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        Document document = new Document(context);
        DocumentSplitter documentSplitter = new RuoyiDocumentSplitter();
        EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build()
                .ingest(document);
        knowledgeBaseLine.setStatus("1");
        int i = knowledgeBaseLineService.updateKnowledgeBaseLine(knowledgeBaseLine);
        if (i>0){
            return AjaxResult.success("向量化成功");
        }else {
            return AjaxResult.error("向量化失败");
        }
    }

    /**
     * 导出知识库单栏列表
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseLine:export')")
    @Log(title = "知识库单栏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KnowledgeBaseLine knowledgeBaseLine)
    {
        List<KnowledgeBaseLine> list = knowledgeBaseLineService.selectKnowledgeBaseLineList(knowledgeBaseLine);
        ExcelUtil<KnowledgeBaseLine> util = new ExcelUtil<KnowledgeBaseLine>(KnowledgeBaseLine.class);
        util.exportExcel(response, list, "知识库单栏数据");
    }

    /**
     * 获取知识库单栏详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseLine:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(knowledgeBaseLineService.selectKnowledgeBaseLineById(id));
    }

    /**
     * 新增知识库单栏
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseLine:add')")
    @Log(title = "知识库单栏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KnowledgeBaseLine knowledgeBaseLine)
    {
        return toAjax(knowledgeBaseLineService.insertKnowledgeBaseLine(knowledgeBaseLine));
    }

    /**
     * 修改知识库单栏
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseLine:edit')")
    @Log(title = "知识库单栏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KnowledgeBaseLine knowledgeBaseLine)
    {
        knowledgeBaseLine.setUpdateBy(getUsername());
        return toAjax(knowledgeBaseLineService.updateKnowledgeBaseLine(knowledgeBaseLine));
    }

    /**
     * 删除知识库单栏
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseLine:remove')")
    @Log(title = "知识库单栏", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(knowledgeBaseLineService.deleteKnowledgeBaseLineByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseLine:add')")
    @PostMapping("/upload")
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile file,
                                 @RequestParam("knowledgeBaseId") Long knowledgeBaseId,
                                 @RequestParam("name") String name) {
        try {
            String content = FileParseUtil.parseFile(file);
            KnowledgeBaseLine knowledgeBaseLine = new KnowledgeBaseLine();
            knowledgeBaseLine.setKnowledgeBaseId(knowledgeBaseId);
            knowledgeBaseLine.setContext(content);
            knowledgeBaseLine.setName(name);
            knowledgeBaseLine.setCreateBy(getUsername());
            knowledgeBaseLineService.insertKnowledgeBaseLine(knowledgeBaseLine);
            return AjaxResult.success(content);
        } catch (Exception e) {
            return AjaxResult.error("解析失败: " + e.getMessage());
        }
    }
}
