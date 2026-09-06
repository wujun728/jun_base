package com.ruoyi.common.utils.ai.RAGFlow.constant;

/**
 * RAGFlow API 常量
 */
public final class RAGFlowConstants {

    private RAGFlowConstants() {
        // 私有构造函数，防止实例化
    }

    // ==================== API路径常量 ====================

    /**
     * OpenAI兼容聊天API
     */
    public static final String API_CHAT_OPENAI = "/api/v1/chats_openai/{chat_id}/chat/completions";

    /**
     * OpenAI兼容代理API
     */
    public static final String API_AGENT_OPENAI = "/api/v1/agents_openai/{agent_id}/chat/completions";

    /**
     * 数据集基础路径
     */
    public static final String API_DATASETS = "/api/v1/datasets";

    /**
     * 文档基础路径
     */
    public static final String API_DOCUMENTS = "/api/v1/datasets/{dataset_id}/documents";

    /**
     * 块基础路径
     */
    public static final String API_CHUNKS = "/api/v1/datasets/{dataset_id}/documents/{document_id}/chunks";

    /**
     * 检索API
     */
    public static final String API_RETRIEVAL = "/api/v1/retrieval";

    /**
     * 聊天基础路径
     */
    public static final String API_CHATS = "/api/v1/chats";

    /**
     * 会话基础路径
     */
    public static final String API_SESSIONS = "/api/v1/chats/{chat_id}/sessions";

    /**
     * 代理基础路径
     */
    public static final String API_AGENTS = "/api/v1/agents";

    /**
     * 代理会话基础路径
     */
    public static final String API_AGENT_SESSIONS = "/api/v1/agents/{agent_id}/sessions";

    /**
     * 记忆基础路径
     */
    public static final String API_MEMORIES = "/api/v1/memories";

    /**
     * 消息基础路径
     */
    public static final String API_MESSAGES = "/api/v1/messages";

    /**
     * 文件基础路径
     */
    public static final String API_FILE = "/api/v1/file";

    /**
     * 系统健康检查
     */
    public static final String API_SYSTEM_HEALTH = "/v1/system/healthz";

    // ==================== 分块方法常量 ====================

    public static final String CHUNK_METHOD_NAIVE = "naive";
    public static final String CHUNK_METHOD_BOOK = "book";
    public static final String CHUNK_METHOD_EMAIL = "email";
    public static final String CHUNK_METHOD_LAWS = "laws";
    public static final String CHUNK_METHOD_MANUAL = "manual";
    public static final String CHUNK_METHOD_ONE = "one";
    public static final String CHUNK_METHOD_PAPER = "paper";
    public static final String CHUNK_METHOD_PICTURE = "picture";
    public static final String CHUNK_METHOD_PRESENTATION = "presentation";
    public static final String CHUNK_METHOD_QA = "qa";
    public static final String CHUNK_METHOD_TABLE = "table";
    public static final String CHUNK_METHOD_TAG = "tag";

    // ==================== 权限常量 ====================

    public static final String PERMISSION_ME = "me";
    public static final String PERMISSION_TEAM = "team";

    // ==================== 文档处理状态常量 ====================

    public static final String RUN_STATUS_UNSTART = "UNSTART";
    public static final String RUN_STATUS_RUNNING = "RUNNING";
    public static final String RUN_STATUS_CANCEL = "CANCEL";
    public static final String RUN_STATUS_DONE = "DONE";
    public static final String RUN_STATUS_FAIL = "FAIL";

    public static final int RUN_CODE_UNSTART = 0;
    public static final int RUN_CODE_RUNNING = 1;
    public static final int RUN_CODE_CANCEL = 2;
    public static final int RUN_CODE_DONE = 3;
    public static final int RUN_CODE_FAIL = 4;

    // =================/// ====================

    public static final String FILE_TYPE_FOLDER = "FOLDER";
    public static final String FILE_TYPE_VIRTUAL = "VIRTUAL";

    // ==================== 默认值常量 ====================

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_PAGE_SIZE = 30;
    public static final int DEFAULT_CHUNK_PAGE_SIZE = 1024;
    public static final int DEFAULT_TIMEOUT = 30;

    public static final float DEFAULT_SIMILARITY_THRESHOLD = 0.2f;
    public static final float DEFAULT_VECTOR_SIMILARITY_WEIGHT = 0.3f;
    public static final int DEFAULT_TOP_K = 1024;
    public static final int DEFAULT_TOP_N = 6;

    // ==================== 错误码常量 ====================

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_FORBIDDEN = 403;
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_INTERNAL_ERROR = 500;

    // ==================== 比较运算符常量 ====================

    public static final String OPERATOR_IS = "is";
    public static final String OPERATOR_NOT_IS = "not is";
    public static final String OPERATOR_CONTAINS = "contains";
    public static final String OPERATOR_NOT_CONTAINS = "not contains";
    public static final String OPERATOR_IN = "in";
    public static final String OPERATOR_NOT_IN = "not in";
    public static final String OPERATOR_START_WITH = "start with";
    public static final String OPERATOR_END_WITH = "end with";
    public static final String OPERATOR_EMPTY = "empty";
    public static final String OPERATOR_NOT_EMPTY = "not empty";

    // ==================== 元数据逻辑常量 ====================

    public static final String LOGIC_AND = "and";
    public static final String LOGIC_OR = "or";
}
