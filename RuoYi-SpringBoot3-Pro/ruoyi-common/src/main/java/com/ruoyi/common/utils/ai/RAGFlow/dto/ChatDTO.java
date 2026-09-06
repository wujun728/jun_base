package com.ruoyi.common.utils.ai.RAGFlow.dto;

import java.util.List;
import java.util.Map;

/**
 * 聊天助手相关DTO
 */
public class ChatDTO {

    /**
     * 聊天助手信息
     */
    public static class ChatAssistant {
        private String id;
        private String name;
        private String avatar;
        private String description;
        private List<String> datasetIds;
        private LLMConfig llm;
        private PromptConfig prompt;
        private String promptType;
        private String language;
        private String status;
        private String tenantId;
        private Integer topK;
        private String doRefer;
        private Long createTime;
        private String createDate;
        private Long updateTime;
        private String updateDate;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<String> getDatasetIds() { return datasetIds; }
        public void setDatasetIds(List<String> datasetIds) { this.datasetIds = datasetIds; }
        public LLMConfig getLlm() { return llm; }
        public void setLlm(LLMConfig llm) { this.llm = llm; }
        public PromptConfig getPrompt() { return prompt; }
        public void setPrompt(PromptConfig prompt) { this.prompt = prompt; }
        public String getPromptType() { return promptType; }
        public void setPromptType(String promptType) { this.promptType = promptType; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getTenantId() { return tenantId; }
        public void setTenantId(String tenantId) { this.tenantId = tenantId; }
        public Integer getTopK() { return topK; }
        public void setTopK(Integer topK) { this.topK = topK; }
        public String getDoRefer() { return doRefer; }
        public void setDoRefer(String doRefer) { this.doRefer = doRefer; }
        public Long getCreateTime() { return createTime; }
        public void setCreateTime(Long createTime) { this.createTime = createTime; }
        public String getCreateDate() { return createDate; }
        public void setCreateDate(String createDate) { this.createDate = createDate; }
        public Long getUpdateTime() { return updateTime; }
        public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
        public String getUpdateDate() { return updateDate; }
        public void setUpdateDate(String updateDate) { this.updateDate = updateDate; }
    }

    /**
     * LLM配置
     */
    public static class LLMConfig {
        private String modelType;
        private String modelName;
        private Double temperature;
        private Double topP;
        private Double presencePenalty;
        private Double frequencyPenalty;

        // Getters and Setters
        public String getModelType() { return modelType; }
        public void setModelType(String modelType) { this.modelType = modelType; }
        public String getModelName() { return modelName; }
        public void setModelName(String modelName) { this.modelName = modelName; }
        public Double getTemperature() { return temperature; }
        public void setTemperature(Double temperature) { this.temperature = temperature; }
        public Double getTopP() { return topP; }
        public void setTopP(Double topP) { this.topP = topP; }
        public Double getPresencePenalty() { return presencePenalty; }
        public void setPresencePenalty(Double presencePenalty) { this.presencePenalty = presencePenalty; }
        public Double getFrequencyPenalty() { return frequencyPenalty; }
        public void setFrequencyPenalty(Double frequencyPenalty) { this.frequencyPenalty = frequencyPenalty; }
    }

    /**
     * 提示配置
     */
    public static class PromptConfig {
        private Double similarityThreshold;
        private Double keywordsSimilarityWeight;
        private Integer topN;
        private List<Variable> variables;
        private String rerankModel;
        private Integer topK;
        private String emptyResponse;
        private String opener;
        private Boolean showQuote;
        private String prompt;

        // Getters and Setters
        public Double getSimilarityThreshold() { return similarityThreshold; }
        public void setSimilarityThreshold(Double similarityThreshold) { this.similarityThreshold = similarityThreshold; }
        public Double getKeywordsSimilarityWeight() { return keywordsSimilarityWeight; }
        public void setKeywordsSimilarityWeight(Double keywordsSimilarityWeight) { this.keywordsSimilarityWeight = keywordsSimilarityWeight; }
        public Integer getTopN() { return topN; }
        public void setTopN(Integer topN) { this.topN = topN; }
        public List<Variable> getVariables() { return variables; }
        public void setVariables(List<Variable> variables) { this.variables = variables; }
        public String getRerankModel() { return rerankModel; }
        public void setRerankModel(String rerankModel) { this.rerankModel = rerankModel; }
        public Integer getTopK() { return topK; }
        public void setTopK(Integer topK) { this.topK = topK; }
        public String getEmptyResponse() { return emptyResponse; }
        public void setEmptyResponse(String emptyResponse) { this.emptyResponse = emptyResponse; }
        public String getOpener() { return opener; }
        public void setOpener(String opener) { this.opener = opener; }
        public Boolean getShowQuote() { return showQuote; }
        public void setShowQuote(Boolean showQuote) { this.showQuote = showQuote; }
        public String getPrompt() { return prompt; }
        public void setPrompt(String prompt) { this.prompt = prompt; }
    }

    /**
     * 变量
     */
    public static class Variable {
        private String key;
        private Boolean optional;

        public Variable() {}

        public Variable(String key, Boolean optional) {
            this.key = key;
            this.optional = optional;
        }

        // Getters and Setters
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public Boolean getOptional() { return optional; }
        public void setOptional(Boolean optional) { this.optional = optional; }
    }

    /**
     * 创建聊天助手请求
     */
    public static class CreateChatRequest {
        private String name;
        private String avatar;
        private List<String> datasetIds;
        private LLMConfig llm;
        private PromptConfig prompt;

        public CreateChatRequest(String name, List<String> datasetIds) {
            this.name = name;
            this.datasetIds = datasetIds;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public List<String> getDatasetIds() { return datasetIds; }
        public void setDatasetIds(List<String> datasetIds) { this.datasetIds = datasetIds; }
        public LLMConfig getLlm() { return llm; }
        public void setLlm(LLMConfig llm) { this.llm = llm; }
        public PromptConfig getPrompt() { return prompt; }
        public void setPrompt(PromptConfig prompt) { this.prompt = prompt; }
    }

    /**
     * 对话请求
     */
    public static class ConversationRequest {
        private String question;
        private Boolean stream;
        private String sessionId;
        private String userId;
        private ChunkDTO.MetadataCondition metadataCondition;

        public ConversationRequest(String question) {
            this.question = question;
            this.stream = false;
        }

        // Getters and Setters
        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }
        public Boolean getStream() { return stream; }
        public void setStream(Boolean stream) { this.stream = stream; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public ChunkDTO.MetadataCondition getMetadataCondition() { return metadataCondition; }
        public void setMetadataCondition(ChunkDTO.MetadataCondition metadataCondition) { this.metadataCondition = metadataCondition; }
    }

    /**
     * 对话响应
     */
    public static class ConversationResponse {
        private String answer;
        private Reference reference;
        private String audioBinary;
        private String id;
        private String sessionId;
        private String prompt;
        private Double createdAt;

        // Getters and Setters
        public String getAnswer() { return answer; }
        public void setAnswer(String answer) { this.answer = answer; }
        public Reference getReference() { return reference; }
        public void setReference(Reference reference) { this.reference = reference; }
        public String getAudioBinary() { return audioBinary; }
        public void setAudioBinary(String audioBinary) { this.audioBinary = audioBinary; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public String getPrompt() { return prompt; }
        public void setPrompt(String prompt) { this.prompt = prompt; }
        public Double getCreatedAt() { return createdAt; }
        public void setCreatedAt(Double createdAt) { this.createdAt = createdAt; }
    }

    /**
     * 引用信息
     */
    public static class Reference {
        private Integer total;
        private List<ChunkDTO.RetrievalChunk> chunks;
        private List<ChunkDTO.DocumentAggregation> docAggs;

        // Getters and Setters
        public Integer getTotal() { return total; }
        public void setTotal(Integer total) { this.total = total; }
        public List<ChunkDTO.RetrievalChunk> getChunks() { return chunks; }
        public void setChunks(List<ChunkDTO.RetrievalChunk> chunks) { this.chunks = chunks; }
        public List<ChunkDTO.DocumentAggregation> getDocAggs() { return docAggs; }
        public void setDocAggs(List<ChunkDTO.DocumentAggregation> docAggs) { this.docAggs = docAggs; }
    }

    /**
     * 会话信息
     */
    public static class Session {
        private String id;
        private String chatId;
        private String name;
        private List<Message> messages;
        private String userId;
        private Long createTime;
        private String createDate;
        private Long updateTime;
        private String updateDate;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getChatId() { return chatId; }
        public void setChatId(String chatId) { this.chatId = chatId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public List<Message> getMessages() { return messages; }
        public void setMessages(List<Message> messages) { this.messages = messages; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public Long getCreateTime() { return createTime; }
        public void setCreateTime(Long createTime) { this.createTime = createTime; }
        public String getCreateDate() { return createDate; }
        public void setCreateDate(String createDate) { this.createDate = createDate; }
        public Long getUpdateTime() { return updateTime; }
        public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
        public String getUpdateDate() { return updateDate; }
        public void setUpdateDate(String updateDate) { this.updateDate = updateDate; }
    }

    /**
     * 消息
     */
    public static class Message {
        private String content;
        private String role;

        public Message() {}

        public Message(String content, String role) {
            this.content = content;
            this.role = role;
        }

        // Getters and Setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    /**
     * 创建会话请求
     */
    public static class CreateSessionRequest {
        private String name;
        private String userId;

        public CreateSessionRequest(String name) {
            this.name = name;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
}
