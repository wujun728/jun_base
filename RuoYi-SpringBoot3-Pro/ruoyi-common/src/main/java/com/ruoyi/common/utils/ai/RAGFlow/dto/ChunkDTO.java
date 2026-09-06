package com.ruoyi.common.utils.ai.RAGFlow.dto;

import java.util.List;
import java.util.Map;

/**
 * 块(Chunk)相关DTO
 */
public class ChunkDTO {

    /**
     * 块信息
     */
    public static class Chunk {
        private String id;
        private String content;
        private String documentId;
        private String datasetId;
        private Boolean available;
        private String docnmKwd;
        private String imageId;
        private String importantKeywords;
        private List<String> positions;
        private String createTime;
        private Double createTimestamp;
        private List<String> questions;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getDocumentId() { return documentId; }
        public void setDocumentId(String documentId) { this.documentId = documentId; }
        public String getDatasetId() { return datasetId; }
        public void setDatasetId(String datasetId) { this.datasetId = datasetId; }
        public Boolean getAvailable() { return available; }
        public void setAvailable(Boolean available) { this.available = available; }
        public String getDocnmKwd() { return docnmKwd; }
        public void setDocnmKwd(String docnmKwd) { this.docnmKwd = docnmKwd; }
        public String getImageId() { return imageId; }
        public void setImageId(String imageId) { this.imageId = imageId; }
        public String getImportantKeywords() { return importantKeywords; }
        public void setImportantKeywords(String importantKeywords) { this.importantKeywords = importantKeywords; }
        public List<String> getPositions() { return positions; }
        public void setPositions(List<String> positions) { this.positions = positions; }
        public String getCreateTime() { return createTime; }
        public void setCreateTime(String createTime) { this.createTime = createTime; }
        public Double getCreateTimestamp() { return createTimestamp; }
        public void setCreateTimestamp(Double createTimestamp) { this.createTimestamp = createTimestamp; }
        public List<String> getQuestions() { return questions; }
        public void setQuestions(List<String> questions) { this.questions = questions; }
    }

    /**
     * 块列表响应
     */
    public static class ChunkListResponse {
        private List<Chunk> chunks;
        private DocumentDTO.Document doc;
        private Integer total;

        public List<Chunk> getChunks() { return chunks; }
        public void setChunks(List<Chunk> chunks) { this.chunks = chunks; }
        public DocumentDTO.Document getDoc() { return doc; }
        public void setDoc(DocumentDTO.Document doc) { this.doc = doc; }
        public Integer getTotal() { return total; }
        public void setTotal(Integer total) { this.total = total; }
    }

    /**
     * 添加块请求
     */
    public static class AddChunkRequest {
        private String content;
        private List<String> importantKeywords;
        private List<String> questions;

        public AddChunkRequest(String content) {
            this.content = content;
        }

        // Getters and Setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public List<String> getImportantKeywords() { return importantKeywords; }
        public void setImportantKeywords(List<String> importantKeywords) { this.importantKeywords = importantKeywords; }
        public List<String> getQuestions() { return questions; }
        public void setQuestions(List<String> questions) { this.questions = questions; }
    }

    /**
     * 更新块请求
     */
    public static class UpdateChunkRequest {
        private String content;
        private List<String> importantKeywords;
        private Boolean available;

        // Getters and Setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public List<String> getImportantKeywords() { return importantKeywords; }
        public void setImportantKeywords(List<String> importantKeywords) { this.importantKeywords = importantKeywords; }
        public Boolean getAvailable() { return available; }
        public void setAvailable(Boolean available) { this.available = available; }
    }

    /**
     * 检索结果块
     */
    public static class RetrievalChunk {
        private String id;
        private String content;
        private String documentId;
        private String documentName;
        private String datasetId;
        private String imageId;
        private String url;
        private Double similarity;
        private Double vectorSimilarity;
        private Double termSimilarity;
        private List<String> docType;
        private List<String> positions;
        private String highlight;
        private String contentLtks;
        private String documentKeyword;
        private List<String> importantKeywords;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getDocumentId() { return documentId; }
        public void setDocumentId(String documentId) { this.documentId = documentId; }
        public String getDocumentName() { return documentName; }
        public void setDocumentName(String documentName) { this.documentName = documentName; }
        public String getDatasetId() { return datasetId; }
        public void setDatasetId(String datasetId) { this.datasetId = datasetId; }
        public String getImageId() { return imageId; }
        public void setImageId(String imageId) { this.imageId = imageId; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public Double getSimilarity() { return similarity; }
        public void setSimilarity(Double similarity) { this.similarity = similarity; }
        public Double getVectorSimilarity() { return vectorSimilarity; }
        public void setVectorSimilarity(Double vectorSimilarity) { this.vectorSimilarity = vectorSimilarity; }
        public Double getTermSimilarity() { return termSimilarity; }
        public void setTermSimilarity(Double termSimilarity) { this.termSimilarity = termSimilarity; }
        public List<String> getDocType() { return docType; }
        public void setDocType(List<String> docType) { this.docType = docType; }
        public List<String> getPositions() { return positions; }
        public void setPositions(List<String> positions) { this.positions = positions; }
        public String getHighlight() { return highlight; }
        public void setHighlight(String highlight) { this.highlight = highlight; }
        public String getContentLtks() { return contentLtks; }
        public void setContentLtks(String contentLtks) { this.contentLtks = contentLtks; }
        public String getDocumentKeyword() { return documentKeyword; }
        public void setDocumentKeyword(String documentKeyword) { this.documentKeyword = documentKeyword; }
        public List<String> getImportantKeywords() { return importantKeywords; }
        public void setImportantKeywords(List<String> importantKeywords) { this.importantKeywords = importantKeywords; }
    }

    /**
     * 文档聚合信息
     */
    public static class DocumentAggregation {
        private String docId;
        private String docName;
        private Integer count;

        public String getDocId() { return docId; }
        public void setDocId(String docId) { this.docId = docId; }
        public String getDocName() { return docName; }
        public void setDocName(String docName) { this.docName = docName; }
        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
    }

    /**
     * 检索响应
     */
    public static class RetrievalResponse {
        private List<RetrievalChunk> chunks;
        private List<DocumentAggregation> docAggs;
        private Integer total;

        public List<RetrievalChunk> getChunks() { return chunks; }
        public void setChunks(List<RetrievalChunk> chunks) { this.chunks = chunks; }
        public List<DocumentAggregation> getDocAggs() { return docAggs; }
        public void setDocAggs(List<DocumentAggregation> docAggs) { this.docAggs = docAggs; }
        public Integer getTotal() { return total; }
        public void setTotal(Integer total) { this.total = total; }
    }

    /**
     * 元数据条件
     */
    public static class MetadataCondition {
        private String logic;
        private List<Condition> conditions;

        public MetadataCondition() {
            this.logic = "and";
        }

        public MetadataCondition(List<Condition> conditions) {
            this.logic = "and";
            this.conditions = conditions;
        }

        public static MetadataCondition and(List<Condition> conditions) {
            MetadataCondition condition = new MetadataCondition();
            condition.setLogic("and");
            condition.setConditions(conditions);
            return condition;
        }

        public static MetadataCondition or(List<Condition> conditions) {
            MetadataCondition condition = new MetadataCondition();
            condition.setLogic("or");
            condition.setConditions(conditions);
            return condition;
        }

        // Getters and Setters
        public String getLogic() { return logic; }
        public void setLogic(String logic) { this.logic = logic; }
        public List<Condition> getConditions() { return conditions; }
        public void setConditions(List<Condition> conditions) { this.conditions = conditions; }
    }

    /**
     * 条件
     */
    public static class Condition {
        private String name;
        private String comparisonOperator;
        private Object value;

        public Condition() {}

        public Condition(String name, String comparisonOperator, Object value) {
            this.name = name;
            this.comparisonOperator = comparisonOperator;
            this.value = value;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getComparisonOperator() { return comparisonOperator; }
        public void setComparisonOperator(String comparisonOperator) { this.comparisonOperator = comparisonOperator; }
        public Object getValue() { return value; }
        public void setValue(Object value) { this.value = value; }
    }

    /**
     * 比较运算符枚举
     */
    public enum ComparisonOperator {
        IS("is"),
        NOT_IS("not is"),
        CONTAINS("contains"),
        NOT_CONTAINS("not contains"),
        IN("in"),
        NOT_IN("not in"),
        START_WITH("start with"),
        END_WITH("end with"),
        GREATER_THAN(">"),
        LESS_THAN("<"),
        GREATER_EQUAL("≥"),
        LESS_EQUAL("≤"),
        EMPTY("empty"),
        NOT_EMPTY("not empty");

        private final String value;

        ComparisonOperator(String value) {
            this.value = value;
        }

        public String getValue() { return value; }
    }
}
