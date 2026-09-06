package com.ruoyi.common.utils.ai.RAGFlow.dto;

import java.util.List;
import java.util.Map;

/**
 * 数据集相关DTO
 */
public class DatasetDTO {

    /**
     * 数据集信息
     */
    public static class Dataset {
        private String id;
        private String name;
        private String avatar;
        private String description;
        private String embeddingModel;
        private String permission;
        private String chunkMethod;
        private String language;
        private String status;
        private Integer pagerank;
        private Integer chunkCount;
        private Integer documentCount;
        private Integer tokenNum;
        private Double similarityThreshold;
        private Double vectorSimilarityWeight;
        private String tenantId;
        private String createdBy;
        private Long createTime;
        private String createDate;
        private Long updateTime;
        private String updateDate;
        private Map<String, Object> parserConfig;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getEmbeddingModel() { return embeddingModel; }
        public void setEmbeddingModel(String embeddingModel) { this.embeddingModel = embeddingModel; }
        public String getPermission() { return permission; }
        public void setPermission(String permission) { this.permission = permission; }
        public String getChunkMethod() { return chunkMethod; }
        public void setChunkMethod(String chunkMethod) { this.chunkMethod = chunkMethod; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Integer getPagerank() { return pagerank; }
        public void setPagerank(Integer pagerank) { this.pagerank = pagerank; }
        public Integer getChunkCount() { return chunkCount; }
        public void setChunkCount(Integer chunkCount) { this.chunkCount = chunkCount; }
        public Integer getDocumentCount() { return documentCount; }
        public void setDocumentCount(Integer documentCount) { this.documentCount = documentCount; }
        public Integer getTokenNum() { return tokenNum; }
        public void setTokenNum(Integer tokenNum) { this.tokenNum = tokenNum; }
        public Double getSimilarityThreshold() { return similarityThreshold; }
        public void setSimilarityThreshold(Double similarityThreshold) { this.similarityThreshold = similarityThreshold; }
        public Double getVectorSimilarityWeight() { return vectorSimilarityWeight; }
        public void setVectorSimilarityWeight(Double vectorSimilarityWeight) { this.vectorSimilarityWeight = vectorSimilarityWeight; }
        public String getTenantId() { return tenantId; }
        public void setTenantId(String tenantId) { this.tenantId = tenantId; }
        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
        public Long getCreateTime() { return createTime; }
        public void setCreateTime(Long createTime) { this.createTime = createTime; }
        public String getCreateDate() { return createDate; }
        public void setCreateDate(String createDate) { this.createDate = createDate; }
        public Long getUpdateTime() { return updateTime; }
        public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
        public String getUpdateDate() { return updateDate; }
        public void setUpdateDate(String updateDate) { this.updateDate = updateDate; }
        public Map<String, Object> getParserConfig() { return parserConfig; }
        public void setParserConfig(Map<String, Object> parserConfig) { this.parserConfig = parserConfig; }
    }

    /**
     * 数据集列表响应
     */
    public static class DatasetListResponse {
        private List<Dataset> data;
        private Integer total;

        public List<Dataset> getData() { return data; }
        public void setData(List<Dataset> data) { this.data = data; }
        public Integer getTotal() { return total; }
        public void setTotal(Integer total) { this.total = total; }
    }

    /**
     * 创建数据集请求
     */
    public static class CreateDatasetRequest {
        private String name;
        private String avatar;
        private String description;
        private String embeddingModel;
        private String permission;
        private String chunkMethod;
        private Map<String, Object> parserConfig;
        private Integer parseType;
        private String pipelineId;

        public CreateDatasetRequest(String name) {
            this.name = name;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getEmbeddingModel() { return embeddingModel; }
        public void setEmbeddingModel(String embeddingModel) { this.embeddingModel = embeddingModel; }
        public String getPermission() { return permission; }
        public void setPermission(String permission) { this.permission = permission; }
        public String getChunkMethod() { return chunkMethod; }
        public void setChunkMethod(String chunkMethod) { this.chunkMethod = chunkMethod; }
        public Map<String, Object> getParserConfig() { return parserConfig; }
        public void setParserConfig(Map<String, Object> parserConfig) { this.parserConfig = parserConfig; }
        public Integer getParseType() { return parseType; }
        public void setParseType(Integer parseType) { this.parseType = parseType; }
        public String getPipelineId() { return pipelineId; }
        public void setPipelineId(String pipelineId) { this.pipelineId = pipelineId; }
    }

    /**
     * 更新数据集请求
     */
    public static class UpdateDatasetRequest {
        private String name;
        private String avatar;
        private String description;
        private String embeddingModel;
        private String permission;
        private String chunkMethod;
        private Integer pagerank;
        private Map<String, Object> parserConfig;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getEmbeddingModel() { return embeddingModel; }
        public void setEmbeddingModel(String embeddingModel) { this.embeddingModel = embeddingModel; }
        public String getPermission() { return permission; }
        public void setPermission(String permission) { this.permission = permission; }
        public String getChunkMethod() { return chunkMethod; }
        public void setChunkMethod(String chunkMethod) { this.chunkMethod = chunkMethod; }
        public Integer getPagerank() { return pagerank; }
        public void setPagerank(Integer pagerank) { this.pagerank = pagerank; }
        public Map<String, Object> getParserConfig() { return parserConfig; }
        public void setParserConfig(Map<String, Object> parserConfig) { this.parserConfig = parserConfig; }
    }

    /**
     * 知识图谱
     */
    public static class KnowledgeGraph {
        private Map<String, Object> graph;
        private Map<String, Object> mindMap;

        public Map<String, Object> getGraph() { return graph; }
        public void setGraph(Map<String, Object> graph) { this.graph = graph; }
        public Map<String, Object> getMindMap() { return mindMap; }
        public void setMindMap(Map<String, Object> mindMap) { this.mindMap = mindMap; }
    }

    /**
     * 元数据摘要
     */
    public static class MetadataSummary {
        private Map<String, MetadataField> summary;

        public Map<String, MetadataField> getSummary() { return summary; }
        public void setSummary(Map<String, MetadataField> summary) { this.summary = summary; }
    }

    public static class MetadataField {
        private String type;
        private List<List<Object>> values;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public List<List<Object>> getValues() { return values; }
        public void setValues(List<List<Object>> values) { this.values = values; }
    }

    /**
     * 分块方法枚举
     */
    public enum ChunkMethod {
        NAIVE("naive"),
        BOOK("book"),
        EMAIL("email"),
        LAWS("laws"),
        MANUAL("manual"),
        ONE("one"),
        PAPER("paper"),
        PICTURE("picture"),
        PRESENTATION("presentation"),
        QA("qa"),
        TABLE("table"),
        TAG("tag");

        private final String value;

        ChunkMethod(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 权限枚举
     */
    public enum Permission {
        ME("me"),
        TEAM("team");

        private final String value;

        Permission(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
