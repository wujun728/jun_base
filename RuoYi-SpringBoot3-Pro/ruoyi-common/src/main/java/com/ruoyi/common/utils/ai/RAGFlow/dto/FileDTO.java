package com.ruoyi.common.utils.ai.RAGFlow.dto;

import java.util.List;
import java.util.Map;

/**
 * 文件管理相关DTO
 */
public class FileDTO {

    /**
     * 文件信息
     */
    public static class FileInfo {
        private String id;
        private String name;
        private String type;
        private Long size;
        private String parentId;
        private String location;
        private Long createTime;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Long getSize() { return size; }
        public void setSize(Long size) { this.size = size; }
        public String getParentId() { return parentId; }
        public void setParentId(String parentId) { this.parentId = parentId; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public Long getCreateTime() { return createTime; }
        public void setCreateTime(Long createTime) { this.createTime = createTime; }
    }

    /**
     * 文件夹信息
     */
    public static class FolderInfo {
        private String id;
        private String name;
        private String type;

        public FolderInfo() {}

        public FolderInfo(String id, String name) {
            this.id = id;
            this.name = name;
            this.type = "FOLDER";
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    /**
     * 文件列表响应
     */
    public static class FileListResponse {
        private Integer total;
        private List<FileInfo> files;
        private FolderInfo parentFolder;

        // Getters and Setters
        public Integer getTotal() { return total; }
        public void setTotal(Integer total) { this.total = total; }
        public List<FileInfo> getFiles() { return files; }
        public void setFiles(List<FileInfo> files) { this.files = files; }
        public FolderInfo getParentFolder() { return parentFolder; }
        public void setParentFolder(FolderInfo parentFolder) { this.parentFolder = parentFolder; }
    }

    /**
     * 根文件夹响应
     */
    public static class RootFolderResponse {
        private FolderInfo rootFolder;

        public FolderInfo getRootFolder() { return rootFolder; }
        public void setRootFolder(FolderInfo rootFolder) { this.rootFolder = rootFolder; }
    }

    /**
     * 父文件夹响应
     */
    public static class ParentFolderResponse {
        private FolderInfo parentFolder;

        public FolderInfo getParentFolder() { return parentFolder; }
        public void setParentFolder(FolderInfo parentFolder) { this.parentFolder = parentFolder; }
    }

    /**
     * 所有父文件夹响应
     */
    public static class AllParentFoldersResponse {
        private List<FolderInfo> parentFolders;

        public List<FolderInfo> getParentFolders() { return parentFolders; }
        public void setParentFolders(List<FolderInfo> parentFolders) { this.parentFolders = parentFolders; }
    }

    /**
     * 创建文件请求
     */
    public static class CreateFileRequest {
        private String name;
        private String parentId;
        private String type;

        public CreateFileRequest(String name, String type) {
            this.name = name;
            this.type = type;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getParentId() { return parentId; }
        public void setParentId(String parentId) { this.parentId = parentId; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    /**
     * 创建文件夹请求
     */
    public static class CreateFolderRequest {
        private String name;
        private String parentId;

        public CreateFolderRequest(String name) {
            this.name = name;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getParentId() { return parentId; }
        public void setParentId(String parentId) { this.parentId = parentId; }
    }

    /**
     * 重命名请求
     */
    public static class RenameRequest {
        private String fileId;
        private String name;

        public RenameRequest(String fileId, String name) {
            this.fileId = fileId;
            this.name = name;
        }

        public String getFileId() { return fileId; }
        public void setFileId(String fileId) { this.fileId = fileId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    /**
     * 移动文件请求
     */
    public static class MoveFilesRequest {
        private List<String> srcFileIds;
        private String destFileId;

        public MoveFilesRequest(List<String> srcFileIds, String destFileId) {
            this.srcFileIds = srcFileIds;
            this.destFileId = destFileId;
        }

        public List<String> getSrcFileIds() { return srcFileIds; }
        public void setSrcFileIds(List<String> srcFileIds) { this.srcFileIds = srcFileIds; }
        public String getDestFileId() { return destFileId; }
        public void setDestFileId(String destFileId) { this.destFileId = destFileId; }
    }

    /**
     * 删除文件请求
     */
    public static class DeleteFilesRequest {
        private List<String> fileIds;

        public DeleteFilesRequest(List<String> fileIds) {
            this.fileIds = fileIds;
        }

        public List<String> getFileIds() { return fileIds; }
        public void setFileIds(List<String> fileIds) { this.fileIds = fileIds; }
    }

    /**
     * 转换文件请求
     */
    public static class ConvertFilesRequest {
        private List<String> fileIds;
        private List<String> kbIds;

        public ConvertFilesRequest(List<String> fileIds, List<String> kbIds) {
            this.fileIds = fileIds;
            this.kbIds = kbIds;
        }

        public List<String> getFileIds() { return fileIds; }
        public void setFileIds(List<String> fileIds) { this.fileIds = fileIds; }
        public List<String> getKbIds() { return kbIds; }
        public void setKbIds(List<String> kbIds) { this.kbIds = kbIds; }
    }

    /**
     * 转换结果
     */
    public static class ConvertResult {
        private String id;
        private String fileId;
        private String documentId;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getFileId() { return fileId; }
        public void setFileId(String fileId) { this.fileId = fileId; }
        public String getDocumentId() { return documentId; }
        public void setDocumentId(String documentId) { this.documentId = documentId; }
    }

    /**
     * 文件类型枚举
     */
    public enum FileType {
        FOLDER("FOLDER"),
        VIRTUAL("VIRTUAL"),
        DOC("doc");

        private final String value;

        FileType(String value) {
            this.value = value;
        }

        public String getValue() { return value; }
    }

    /**
     * 排序字段枚举
     */
    public enum OrderBy {
        CREATE_TIME("create_time");

        private final String value;

        OrderBy(String value) {
            this.value = value;
        }

        public String getValue() { return value; }
    }
}
