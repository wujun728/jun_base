package com.ruoyi.common.utils.ai.RAGFlow.dto;

import java.util.List;
import java.util.Map;

/**
 * 记忆(Memory)相关DTO
 */
public class MemoryDTO {

    /**
     * 记忆信息
     */
    public static class Memory {
        private String id;
        private String name;
        private String tenantId;
        private String agentId;
        private String embdId;
        private String llmId;
        private String status;
        private Long createTime;
        private String createDate;
        private Long updateTime;
        private String updateDate;
        private List<String> memoryTypes;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getTenantId() { return tenantId; }
        public void setTenantId(String tenantId) { this.tenantId = tenantId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getEmbdId() { return embdId; }
        public void setEmbdId(String embdId) { this.embdId = embdId; }
        public String getLlmId() { return llmId; }
        public void setLlmId(String llmId) { this.llmId = llmId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Long getCreateTime() { return createTime; }
        public void setCreateTime(Long createTime) { this.createTime = createTime; }
        public String getCreateDate() { return createDate; }
        public void setCreateDate(String createDate) { this.createDate = createDate; }
        public Long getUpdateTime() { return updateTime; }
        public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
        public String getUpdateDate() { return updateDate; }
        public void setUpdateDate(String updateDate) { this.updateDate = updateDate; }
        public List<String> getMemoryTypes() { return memoryTypes; }
        public void setMemoryTypes(List<String> memoryTypes) { this.memoryTypes = memoryTypes; }
    }

    /**
     * 记忆配置
     */
    public static class MemoryConfig {
        private String id;
        private String memoryName;
        private String agentName;
        private String agentId;
        private String embdId;
        private String embdName;
        private String llmId;
        private String llmName;
        private List<String> memoryTypes;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getMemoryName() { return memoryName; }
        public void setMemoryName(String memoryName) { this.memoryName = memoryName; }
        public String getAgentName() { return agentName; }
        public void setAgentName(String agentName) { this.agentName = agentName; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getEmbdId() { return embdId; }
        public void setEmbdId(String embdId) { this.embdId = embdId; }
        public String getEmbdName() { return embdName; }
        public void setEmbdName(String embdName) { this.embdName = embdName; }
        public String getLlmId() { return llmId; }
        public void setLlmId(String llmId) { this.llmId = llmId; }
        public String getLlmName() { return llmName; }
        public void setLlmName(String llmName) { this.llmName = llmName; }
        public List<String> getMemoryTypes() { return memoryTypes; }
        public void setMemoryTypes(List<String> memoryTypes) { this.memoryTypes = memoryTypes; }
    }

    /**
     * 消息信息
     */
    public static class Message {
        private String memoryId;
        private String messageId;
        private String agentId;
        private String sessionId;
        private String userId;
        private String content;
        private Boolean status;
        private String forgetAt;
        private String invalidAt;
        private String validAt;
        private String messageType;
        private String sourceId;

        // Getters and Setters
        public String getMemoryId() { return memoryId; }
        public void setMemoryId(String memoryId) { this.memoryId = memoryId; }
        public String getMessageId() { return messageId; }
        public void setMessageId(String messageId) { this.messageId = messageId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Boolean getStatus() { return status; }
        public void setStatus(Boolean status) { this.status = status; }
        public String getForgetAt() { return forgetAt; }
        public void setForgetAt(String forgetAt) { this.forgetAt = forgetAt; }
        public String getInvalidAt() { return invalidAt; }
        public void setInvalidAt(String invalidAt) { this.invalidAt = invalidAt; }
        public String getValidAt() { return validAt; }
        public void setValidAt(String validAt) { this.validAt = validAt; }
        public String getMessageType() { return messageType; }
        public void setMessageType(String messageType) { this.messageType = messageType; }
        public String getSourceId() { return sourceId; }
        public void setSourceId(String sourceId) { this.sourceId = sourceId; }
    }

    /**
     * 消息内容（包含向量）
     */
    public static class MessageContent {
        private String id;
        private String memoryId;
        private String messageId;
        private String agentId;
        private String sessionId;
        private String userId;
        private String content;
        private List<Float> contentEmbed;
        private Boolean status;
        private String forgetAt;
        private String invalidAt;
        private String validAt;
        private String messageType;
        private String sourceId;
        private Integer zoneId;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getMemoryId() { return memoryId; }
        public void setMemoryId(String memoryId) { this.memoryId = memoryId; }
        public String getMessageId() { return messageId; }
        public void setMessageId(String messageId) { this.messageId = messageId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public List<Float> getContentEmbed() { return contentEmbed; }
        public void setContentEmbed(List<Float> contentEmbed) { this.contentEmbed = contentEmbed; }
        public Boolean getStatus() { return status; }
        public void setStatus(Boolean status) { this.status = status; }
        public String getForgetAt() { return forgetAt; }
        public void setForgetAt(String forgetAt) { this.forgetAt = forgetAt; }
        public String getInvalidAt() { return invalidAt; }
        public void setInvalidAt(String invalidAt) { this.invalidAt = invalidAt; }
        public String getValidAt() { return validAt; }
        public void setValidAt(String validAt) { this.validAt = validAt; }
        public String getMessageType() { return messageType; }
        public void setMessageType(String messageType) { this.messageType = messageType; }
        public String getSourceId() { return sourceId; }
        public void setSourceId(String sourceId) { this.sourceId = sourceId; }
        public Integer getZoneId() { return zoneId; }
        public void setZoneId(Integer zoneId) { this.zoneId = zoneId; }
    }

    /**
     * 创建记忆请求
     */
    public static class CreateMemoryRequest {
        private String name;
        private List<String> memoryTypes;
        private String embdId;
        private String llmId;

        public CreateMemoryRequest(String name, List<String> memoryTypes, String embdId, String llmId) {
            this.name = name;
            this.memoryTypes = memoryTypes;
            this.embdId = embdId;
            this.llmId = llmId;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public List<String> getMemoryTypes() { return memoryTypes; }
        public void setMemoryTypes(List<String> memoryTypes) { this.memoryTypes = memoryTypes; }
        public String getEmbdId() { return embdId; }
        public void setEmbdId(String embdId) { this.embdId = embdId; }
        public String getLlmId() { return llmId; }
        public void setLlmId(String llmId) { this.llmId = llmId; }
    }

    /**
     * 添加消息请求
     */
    public static class AddMessageRequest {
        private List<String> memoryId;
        private String agentId;
        private String sessionId;
        private String userId;
        private String userInput;
        private String agentResponse;

        // Getters and Setters
        public List<String> getMemoryId() { return memoryId; }
        public void setMemoryId(List<String> memoryId) { this.memoryId = memoryId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getUserInput() { return userInput; }
        public void setUserInput(String userInput) { this.userInput = userInput; }
        public String getAgentResponse() { return agentResponse; }
        public void setAgentResponse(String agentResponse) { this.agentResponse = agentResponse; }
    }

    /**
     * 搜索消息请求
     */
    public static class SearchMessageRequest {
        private String query;
        private List<String> memoryId;
        private String agentId;
        private String sessionId;
        private Float similarityThreshold;
        private Float keywordsSimilarityWeight;
        private Integer topN;

        // Getters and Setters
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
        public List<String> getMemoryId() { return memoryId; }
        public void setMemoryId(List<String> memoryId) { this.memoryId = memoryId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public Float getSimilarityThreshold() { return similarityThreshold; }
        public void setSimilarityThreshold(Float similarityThreshold) { this.similarityThreshold = similarityThreshold; }
        public Float getKeywordsSimilarityWeight() { return keywordsSimilarityWeight; }
        public void setKeywordsSimilarityWeight(Float keywordsSimilarityWeight) { this.keywordsSimilarityWeight = keywordsSimilarityWeight; }
        public Integer getTopN() { return topN; }
        public void setTopN(Integer topN) { this.topN = topN; }
    }

    /**
     * 更新消息状态请求
     */
    public static class UpdateMessageStatusRequest {
        private Boolean status;

        public UpdateMessageStatusRequest(boolean status) {
            this.status = status;
        }

        public Boolean getStatus() { return status; }
        public void setStatus(Boolean status) { this.status = status; }
    }

    /**
     * 获取最近消息请求
     */
    public static class GetRecentMessagesRequest {
        private List<String> memoryId;
        private String agentId;
        private String sessionId;
        private Integer limit;

        // Getters and Setters
        public List<String> getMemoryId() { return memoryId; }
        public void setMemoryId(List<String> memoryId) { this.memoryId = memoryId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public Integer getLimit() { return limit; }
        public void setLimit(Integer limit) { this.limit = limit; }
    }
}
