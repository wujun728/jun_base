package com.ruoyi.common.utils.ai.RAGFlow.dto;

import java.util.List;
import java.util.Map;

/**
 * 文档相关DTO
 */
public class DocumentDTO {

    /**
     * 文档信息
     */
    public static class Document {
        private String id;
        private String name;
        private String type;
        private String suffix;
        private Long size;
        private String location;
        private String sourceType;
        private String status;
        private String run;
        private String datasetId;
        private String chunkMethod;
        private Integer chunkCount;
        private Integer tokenCount;
        private String createdBy;
        private Long createTime;
        private String createDate;
        private Long updateTime;
        private String updateDate;
        private String processBeginAt;
        private Double processDuration;
        private Double progress;
        private List<String> progressMsg;
        private Map<String, Object> parserConfig;
        private Map<String, String> metaFields;
        private String pipelineId;
        private String thumbnail;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getSuffix() { return suffix; }
        public void setSuffix(String suffix) { this.suffix = suffix; }
        public Long getSize() { return size; }
        public void setSize(Long size) { this.size = size; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getSourceType() { return sourceType; }
        public void setSourceType(String sourceType) { this.sourceType = sourceType; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getRun() { return run; }
        public void setRun(String run) { this.run = run; }
        public String getDatasetId() { return datasetId; }
        public void setDatasetId(String datasetId) { this.datasetId = datasetId; }
        public String getChunkMethod() { return chunkMethod; }
        public void setChunkMethod(String chunkMethod) { this.chunkMethod = chunkMethod; }
        public Integer getChunkCount() { return chunkCount; }
        public void setChunkCount(Integer chunkCount) { this.chunkCount = chunkCount; }
        public Integer getTokenCount() { return tokenCount; }
        public void setTokenCount(Integer tokenCount) { this.tokenCount = tokenCount; }
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
        public String getProcessBeginAt() { return processBeginAt; }
        public void setProcessBeginAt(String processBeginAt) { this.processBeginAt = processBeginAt; }
        public Double getProcessDuration() { return processDuration; }
        public void setProcessDuration(Double processDuration) { this.processDuration = processDuration; }
        public Double getProgress() { return progress; }
        public void setProgress(Double progress) { this.progress = progress; }
        public List<String> getProgressMsg() { return progressMsg; }
        public void setProgressMsg(List<String> progressMsg) { this.progressMsg = progressMsg; }
        public Map<String, Object> getParserConfig() { return parserConfig; }
        public void setParserConfig(Map<String, Object> parserConfig) { this.parserConfig = parserConfig; }
        public Map<String, String> getMetaFields() { return metaFields; }
        public void setMetaFields(Map<String, String> metaFields) { this.metaFields = metaFields; }
        public String getPipelineId() { return pipelineId; }
        public void setPipelineId(String pipelineId) { this.pipelineId = pipelineId; }
        public String getThumbnail() { return thumbnail; }
        public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    }

    /**
     * 文档列表响应
     */
    public static class DocumentListResponse {
        private List<Document> docs;
        private Integer totalDatasets;

        public List<Document> getDocs() { return docs; }
        public void setDocs(List<Document> docs) { this.docs = docs; }
        public Integer getTotalDatasets() { return totalDatasets; }
        public void setTotalDatasets(Integer totalDatasets) { this.totalDatasets = totalDatasets; }
    }

    /**
     * 更新文档请求
     */
    public static class UpdateDocumentRequest {
        private String name;
        private Map<String, String> metaFields;
        private String chunkMethod;
        private Map<String, Object> parserConfig;
        private Integer enabled;

        public UpdateDocumentRequest() {}

        public UpdateDocumentRequest(String name) {
            this.name = name;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Map<String, String> getMetaFields() { return metaFields; }
        public void setMetaFields(Map<String, String> metaFields) { this.metaFields = metaFields; }
        public String getChunkMethod() { return chunkMethod; }
        public void setChunkMethod(String chunkMethod) { this.chunkMethod = chunkMethod; }
        public Map<String, Object> getParserConfig() { return parserConfig; }
        public void setParserConfig(Map<String, Object> parserConfig) { this.parserConfig = parserConfig; }
        public Integer getEnabled() { return enabled; }
        public void setEnabled(Integer enabled) { this.enabled = enabled; }
    }

    /**
     * 文档处理状态枚举
     */
    public enum DocumentRunStatus {
        UNSTART("UNSTART", 0),
        RUNNING("RUNNING", 1),
        CANCEL("CANCEL", 2),
        DONE("DONE", 3),
        FAIL("FAIL", 4);

        private final String text;
        private final Integer code;

        DocumentRunStatus(String text, Integer code) {
            this.text = text;
            this.code = code;
        }

        public String getText() { return text; }
        public Integer getCode() { return code; }

        public static DocumentRunStatus fromText(String text) {
            for (DocumentRunStatus status : values()) {
                if (status.text.equalsIgnoreCase(text)) {
                    return status;
                }
            }
            return null;
        }

        public static DocumentRunStatus fromCode(Integer code) {
            for (DocumentRunStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }
    }
}
