package com.ruoyi.common.utils.ai.RAGFlow.dto;

import java.util.List;
import java.util.Map;

/**
 * 代理(Agent)相关DTO
 */
public class AgentDTO {

    /**
     * 代理信息
     */
    public static class Agent {
        private String id;
        private String title;
        private String description;
        private String dsl;
        private Map<String, Object> dslObj;
        private String tenantId;
        private Long createTime;
        private String createDate;
        private Long updateTime;
        private String updateDate;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getDsl() { return dsl; }
        public void setDsl(String dsl) { this.dsl = dsl; }
        public Map<String, Object> getDslObj() { return dslObj; }
        public void setDslObj(Map<String, Object> dslObj) { this.dslObj = dslObj; }
        public String getTenantId() { return tenantId; }
        public void setTenantId(String tenantId) { this.tenantId = tenantId; }
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
     * 代理会话信息
     */
    public static class AgentSession {
        private String id;
        private String agentId;
        private String userId;
        private String dsl;
        private Map<String, Object> dslObj;
        private Map<String, Object> inputs;
        private String status;
        private Long createTime;
        private String createDate;
        private Long updateTime;
        private String updateDate;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getDsl() { return dsl; }
        public void setDsl(String dsl) { this.dsl = dsl; }
        public Map<String, Object> getDslObj() { return dslObj; }
        public void setDslObj(Map<String, Object> dslObj) { this.dslObj = dslObj; }
        public Map<String, Object> getInputs() { return inputs; }
        public void setInputs(Map<String, Object> inputs) { this.inputs = inputs; }
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
    }

    /**
     * 创建代理请求
     */
    public static class CreateAgentRequest {
        private String title;
        private String description;
        private Map<String, Object> dsl;

        public CreateAgentRequest(String title) {
            this.title = title;
        }

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Map<String, Object> getDsl() { return dsl; }
        public void setDsl(Map<String, Object> dsl) { this.dsl = dsl; }
    }

    /**
     * 对话请求
     */
    public static class AgentConversationRequest {
        private String question;
        private Boolean stream;
        private String sessionId;
        private Map<String, Object> inputs;
        private String userId;
        private Boolean returnTrace;

        public AgentConversationRequest(String question) {
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
        public Map<String, Object> getInputs() { return inputs; }
        public void setInputs(Map<String, Object> inputs) { this.inputs = inputs; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public Boolean getReturnTrace() { return returnTrace; }
        public void setReturnTrace(Boolean returnTrace) { this.returnTrace = returnTrace; }
    }

    /**
     * 对话响应
     */
    public static class AgentConversationResponse {
        private String answer;
        private String reference;
        private String audioBinary;
        private String id;
        private String sessionId;
        private String prompt;
        private String trace;

        // Getters and Setters
        public String getAnswer() { return answer; }
        public void setAnswer(String answer) { this.answer = answer; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public String getAudioBinary() { return audioBinary; }
        public void setAudioBinary(String audioBinary) { this.audioBinary = audioBinary; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public String getPrompt() { return prompt; }
        public void setPrompt(String prompt) { this.prompt = prompt; }
        public String getTrace() { return trace; }
        public void setTrace(String trace) { this.trace = trace; }
    }

    /**
     * DSL组件
     */
    public static class DSLComponent {
        private List<String> upstream;
        private List<String> downstream;
        private ComponentObject obj;

        // Getters and Setters
        public List<String> getUpstream() { return upstream; }
        public void setUpstream(List<String> upstream) { this.upstream = upstream; }
        public List<String> getDownstream() { return downstream; }
        public void setDownstream(List<String> downstream) { this.downstream = downstream; }
        public ComponentObject getObj() { return obj; }
        public void setObj(ComponentObject obj) { this.obj = obj; }
    }

    /**
     * 组件对象
     */
    public static class ComponentObject {
        private String componentName;
        private ComponentParams params;

        // Getters and Setters
        public String getComponentName() { return componentName; }
        public void setComponentName(String componentName) { this.componentName = componentName; }
        public ComponentParams getParams() { return params; }
        public void setParams(ComponentParams params) { this.params = params; }
    }

    /**
     * 组件参数
     */
    public static class ComponentParams {
        private Map<String, Object> inputs;
        private Map<String, Object> outputs;
        private Map<String, Object> debugInputs;
        private String description;
        private String exceptionGoto;
        private String exceptionMethod;
        private Object exceptionDefaultValue;
        private Double delayAfterError;
        private Integer maxRetries;
        private Boolean stream;
        private Boolean enablePrologue;
        private Boolean enableTips;
        private List<String> content;
        private MessageHistoryConfig messageHistoryWindowSize;

        // Getters and Setters
        public Map<String, Object> getInputs() { return inputs; }
        public void setInputs(Map<String, Object> inputs) { this.inputs = inputs; }
        public Map<String, Object> getOutputs() { return outputs; }
        public void setOutputs(Map<String, Object> outputs) { this.outputs = outputs; }
        public Map<String, Object> getDebugInputs() { return debugInputs; }
        public void setDebugInputs(Map<String, Object> debugInputs) { this.debugInputs = debugInputs; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getExceptionGoto() { return exceptionGoto; }
        public void setExceptionGoto(String exceptionGoto) { this.exceptionGoto = exceptionGoto; }
        public String getExceptionMethod() { return exceptionMethod; }
        public void setExceptionMethod(String exceptionMethod) { this.exceptionMethod = exceptionMethod; }
        public Object getExceptionDefaultValue() { return exceptionDefaultValue; }
        public void setExceptionDefaultValue(Object exceptionDefaultValue) { this.exceptionDefaultValue = exceptionDefaultValue; }
        public Double getDelayAfterError() { return delayAfterError; }
        public void setDelayAfterError(Double delayAfterError) { this.delayAfterError = delayAfterError; }
        public Integer getMaxRetries() { return maxRetries; }
        public void setMaxRetries(Integer maxRetries) { this.maxRetries = maxRetries; }
        public Boolean getStream() { return stream; }
        public void setStream(Boolean stream) { this.stream = stream; }
        public Boolean getEnablePrologue() { return enablePrologue; }
        public void setEnablePrologue(Boolean enablePrologue) { this.enablePrologue = enablePrologue; }
        public Boolean getEnableTips() { return enableTips; }
        public void setEnableTips(Boolean enableTips) { this.enableTips = enableTips; }
        public List<String> getContent() { return content; }
        public void setContent(List<String> content) { this.content = content; }
        public MessageHistoryConfig getMessageHistoryWindowSize() { return messageHistoryWindowSize; }
        public void setMessageHistoryWindowSize(MessageHistoryConfig messageHistoryWindowSize) { this.messageHistoryWindowSize = messageHistoryWindowSize; }
    }

    /**
     * 消息历史窗口配置
     */
    public static class MessageHistoryConfig {
        private Integer size;
        private Boolean enabled;

        public Integer getSize() { return size; }
        public void setSize(Integer size) { this.size = size; }
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    }
}
