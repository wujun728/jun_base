

export interface UploadOptions {
    /**文件 */
    file: File
    /**成功回调 */
    onSuccess?: (result: any) => void;
    /**失败回调 */
    onError?: (error: any) => void;
    /**上传配置 */
    options?: UploadConfig;
}

export interface File {
    /**文件路径 */
    path: string;
    /**文件大小 */
    size: number;
}

export interface UploadConfig {
    /**分片大小，单位字节 */
    chunkSize?: number;
    /**并发上传限制 */
    concurrentLimit?: number;
}

export interface UploadData {
    /**上传编号 */
    uploadId: string;
    /**文件在云端保存路径 */
    saveFilePath: string;
    /**上传文件的大小 */
    fileSize: number;
    /**分片数量 */
    chunkCount: number;
    /**上传文件的路径 */
    filePath: string;
}

export interface PartETag {
    partNumber: number;
    ETag: string;
}


export interface ChunkTask {
    index: number;
    start: number;
    end: number;
}

/**
 * 上传进度信息接口
 */
export interface ProgressInfo {
    /** 已完成的分片数量 */
    completedChunks: number;
    /** 当前显示的上传进度（整数，如 0, 10, 20...） */
    uploadProgress: number;
    /** 总分片数量 */
    chunkCount: number;
}

