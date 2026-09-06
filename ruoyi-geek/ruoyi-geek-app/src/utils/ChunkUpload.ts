import modal from '@/plugins/modal'
import { initChunkUpload, uploadChunk, completeChunkUpload } from '@/api/system/chunkUpload'
import { UploadOptions, PartETag, File, UploadData, ProgressInfo } from '@/types/upload'
import TaskQueue from '@/utils/TaskQueue'

// 声明微信小程序全局对象
declare const wx: any;

/**
 * 分片上传工具类
 * 
 */
export class ChunkUpload {

    /**
     * 分片大小，单位字节
     */
    private chunkSize: number;

    /**
     * 并发上传的分片数量限制
     */
    private concurrentLimit: number;

    /**
     * 进度更新间隔（百分比）
     */
    private static readonly PROGRESS_UPDATE_INTERVAL = 10;

    /**
     * 构造函数 - 初始化分片上传器
     * 设置默认分片大小为15MB，并发限制为2个分片
     */
    constructor() {
        this.chunkSize = 15 * 1024 * 1024; // 默认分片大小15MB
        this.concurrentLimit = 2;  // 并发上传的分片数量
    }


    /**
     * 主要的分片上传方法
     * @param params 上传参数对象
     * 
     * @param params.file 要上传的文件对象，包含path和size属性
     * @param params.onSuccess 上传成功回调函数，接收 { success: true } 参数
     * @param params.onError 上传失败回调函数，接收错误信息字符串参数
     * @param params.options 上传配置选项 { chunkSize?: number; concurrentLimit?: number }
     * @param params.options.chunkSize 分片大小（字节），默认15MB (15 * 1024 * 1024)
     * @param params.options.concurrentLimit 并发上传分片数量限制，默认2个分片同时上传
     * 
     * @returns Promise<boolean> 返回Promise，成功时resolve(true)，失败时resolve(false)
     */
    async upload(params: UploadOptions): Promise<boolean> {
        const {
            file,
            onSuccess,
            onError,
            options = {} as { chunkSize?: number; concurrentLimit?: number }
        } = params

        try {
            // 1.检验文件的参数
            this._validateParams(file);

            //2.获取文件信息
            const { actualFilePath, actualFileSize, actualFileName } = this.getFileInfo(file);

            modal.loading("准备上传...")

            // 3.初始化分片数据
            const chunkSize = options.chunkSize || this.chunkSize;
            const chunkCount = Math.ceil(actualFileSize / chunkSize);
            const concurrentLimit = options.concurrentLimit || this.concurrentLimit;
            let partETags: PartETag[] = [];


            //4.初始化分片上传
            const initResult = await initChunkUpload(actualFileName, actualFileSize)
            if (initResult.code !== 200) throw new Error("初始化上传失败")

            const { uploadId, filePath: serverFilePath } = initResult.data


            //5.将文件移动到应用 沙盒 目录
            // #ifdef  APP-PLUS
            const localFilePath = await this.copyFileToSandbox(actualFilePath)
            // #endif


            //6.开始上传分片
            modal.closeLoading()
            modal.loading("上传中...")

            const progressInfo: ProgressInfo = {
                completedChunks: 0,
                uploadProgress: 0,
                chunkCount
            }

            //   7.并发上传数据
            const uploadData = {
                uploadId,
                saveFilePath: serverFilePath,
                fileSize: actualFileSize,
                chunkCount,
                filePath: actualFilePath
            };

            // #ifdef  APP-PLUS
            partETags = await this.uploadChunksWithTaskQueue(
                uploadData,
                chunkSize,
                concurrentLimit,
                localFilePath,
                progressInfo
            )
            // #endif

            // #ifdef  MP-WEIXIN
            partETags = await this._uploadChunks(uploadData, concurrentLimit, progressInfo);
            // #endif

            //8.合并分片
            modal.closeLoading();
            modal.loading("正在合并分片...")

            //完成分片上传
            await completeChunkUpload(
                uploadId, serverFilePath, actualFileSize, actualFileName, partETags
            )

            // 9.将临时文件删除，防止占用空间
            // #ifdef  APP-PLUS
            await this.deleteLocalFile(localFilePath)
            // #endif


            modal.closeLoading()

            // 10.执行成功回调
            onSuccess?.({ success: true })

            return true
        } catch (error) {
            modal.closeLoading()
            const errorMessage = error instanceof Error ? error.message : `上传失败`
            onError?.(errorMessage)
            return false
        }
    }

    /**
     * 校验上传参数
     * 
     * @param file - 要上传的文件对象
     * @throws {Error} 当文件路径不存在时抛出错误
     * @throws {Error} 当文件大小不存在时抛出错误
     */
    _validateParams(file: File) {
        if (!file.path) throw new Error("文件路径不存在");
        if (!file.size) throw new Error("文件大小不存在");
    }

    /**
     * 获取文件信息（路径、大小、文件名）
     * 
     * @param file 文件对象，包含path和size属性
     * @returns 包含文件信息的对象
     * @returns actualFilePath 实际文件路径
     * @returns actualFileSize 实际文件大小（字节）
     * @returns actualFileName 实际文件名称（根据平台调整）
     */
    getFileInfo(file: File): { actualFilePath: string; actualFileSize: number; actualFileName: string } {
        const actualFilePath = file.path;
        const actualFileSize = file.size;

        let actualFileName: string;

        // #ifdef  APP-PLUS
        actualFileName = this.getFileName(file.path);
        // #endif

        // #ifdef  MP-WEIXIN
        actualFileName = `weixin_${Date.now()}.${this.getFileExtension(file.path)}`;
        // #endif

        return {
            actualFilePath,
            actualFileSize,
            actualFileName
        };
    }

    /**
     * 获取文件名称
     * @param filePath 完整文件路径
     * @returns string 从路径中提取的文件名
     */
    getFileName(filePath: string): string {
        if (!filePath) return ""
        const slashIndex = filePath.lastIndexOf("/");
        if (slashIndex === -1) return filePath;
        return filePath.substring(slashIndex + 1);
    };

    /**
     * 获取文件的扩展名称
     * @param filePath 完整文件路径
     * @returns string 文件扩展名（小写，不包含点号）
     */
    getFileExtension(filePath: string): string {
        if (!filePath) return ""
        const dotIndex = filePath.lastIndexOf(".");
        if (dotIndex === -1) return ""
        return filePath.substring(dotIndex + 1).toLowerCase();
    };

    /**
     * 将文件复制到应用沙盒目录
     * @param srcFilePath 源文件路径
     * @returns Promise<string> 复制后的文件完整路径
     */
    copyFileToSandbox(srcFilePath: string): Promise<string> {
        return new Promise((resolve, reject) => {
            const newName = `file_${Date.now()}.${this.getFileExtension(srcFilePath)}`;
            plus.io.requestFileSystem(
                plus.io.PRIVATE_DOC,
                (dstEntry) => {
                    plus.io.resolveLocalFileSystemURL(
                        srcFilePath,
                        (srcEntry) => {
                            srcEntry.copyTo(
                                dstEntry.root,
                                newName,
                                (entry) => {
                                    if (entry.fullPath) {
                                        resolve(entry.fullPath);
                                    } else {
                                        reject(new Error('File path is undefined'));
                                    }
                                },
                                (e) => reject(e)
                            );
                        },
                        (e) => reject(e)
                    );
                },
                (e) => reject(e)
            );
        });
    };


    /**
     * 获取切片end位置
     * @param start 切片开始位置
     * @param chunkSize 切片大小
     * @param fileSize 文件总大小
     * @param index 当前切片索引
     * @param totalChunks 总切片数量
     * @returns number 切片结束位置
     */
    getSliceEnd(start: number, chunkSize: number, fileSize: number, index: number, totalChunks: number) {
        return index < totalChunks - 1 ? start + chunkSize - 1 : fileSize
    }

    /**
     * 使用TaskQueue并发上传分片（APP端）
     * 
     * @param uploadData 上传数据对象，包含 uploadId、saveFilePath、fileSize 等
     * @param chunkSize 单个分片的大小（字节）
     * @param concurrentLimit 并发上传的分片数量限制
     * @param localFilePath APP端沙盒中的本地文件路径
     * @param progressInfo 进度跟踪对象，包含completedChunks、uploadProgress、chunkCount等属性
     * 
     * @returns Promise<PartETag[]> 返回所有分片的ETag信息数组
     * 
     * @throws {Error} 当任何分片上传失败时抛出错误
     * 
     */
    async uploadChunksWithTaskQueue(
        uploadData: UploadData,
        chunkSize: number,
        concurrentLimit: number,
        localFilePath: string,
        progressInfo: ProgressInfo
    ): Promise<PartETag[]> {
        const { chunkCount, fileSize, uploadId, saveFilePath } = uploadData;
        const taskQueue = new TaskQueue(concurrentLimit);
        const partETags: PartETag[] = [];

        // 创建所有分片上传任务
        const uploadPromises: Promise<PartETag>[] = [];

        for (let i = 0; i < chunkCount; i++) {
            const task = {
                index: i + 1,
                start: i * chunkSize,
                end: this.getSliceEnd(i * chunkSize, chunkSize, fileSize, i, chunkCount),
            };

            const promise = taskQueue.add(async () => {
                const chunk = await this.readAppFileChunk(localFilePath, task.start, task.end - task.start);
                const response = await this.uploadAppChunk(uploadId, saveFilePath, task.index, chunk) as any;

                if (!response.data || !response.data.etag) throw new Error('分片上传失败');

                // 更新进度
                this.updateUploadProgress(progressInfo);

                return {
                    partNumber: task.index,
                    ETag: response.data.etag,
                };
            });

            uploadPromises.push(promise);
        }

        // 等待所有任务完成
        try {
            const results = await Promise.all(uploadPromises);
            // 收集所有 partETags
            results.forEach(partETag => {
                if (partETag) partETags.push(partETag);
            });
            // 按 partNumber 排序确保顺序正确
            partETags.sort((a, b) => a.partNumber - b.partNumber);
            return partETags;
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '分片上传失败';
            throw new Error(`分片上传失败: ${errorMessage}`);
        }
    }


    /**
     * APP端分片上传单个分片
     * @param uploadId 上传ID
     * @param filePath 服务器文件路径
     * @param chunkIndex 分片索引
     * @param chunk 分片数据，可以是ArrayBuffer或字符串
     * @returns Promise<any> 上传响应结果
     */
    async uploadAppChunk(uploadId: string, filePath: string, chunkIndex: number, chunk: ArrayBuffer | string) {
        try {
            const response = await this.startUploadAppChunk(uploadId, filePath, chunkIndex, chunk)
            return response
        } catch (error) {
            throw new Error('分片上传失败')
        }
    }

    /**
     * 执行APP端分片上传
     * @param uploadId 上传ID
     * @param filePath 服务器文件路径
     * @param chunkIndex 分片索引
     * @param chunk 分片数据，可以是ArrayBuffer或字符串
     * @returns Promise 返回上传结果的Promise
     */
    startUploadAppChunk(uploadId: string, filePath: string, chunkIndex: number, chunk: ArrayBuffer | string) {
        return new Promise(async (resolve, reject) => {
            try {
                // 1. 准备临时文件信息
                const tempFileName = `temp_chunk/chunk_${uploadId}_${chunkIndex}.bin`
                const tempDirPath = plus.io.PRIVATE_DOC

                // 2. 创建并写入临时文件
                const tempFilePath = await this.createAndWriteTempFile(
                    tempDirPath,
                    tempFileName,
                    chunk
                )

                //设置文件的全路径
                let formattedPath = tempFilePath
                if (tempFilePath && !tempFilePath.startsWith("file://")) {
                    formattedPath = `file://${tempFilePath}`
                }

                // 3. 上传文件
                const result = await uploadChunk(uploadId, filePath, chunkIndex, formattedPath)

                // 4. 删除临时文件
                await this.deleteTempFile(tempDirPath, tempFileName)

                resolve(result)
            } catch (error) {
                reject(error)
            }
        })
    }


    /**
     * 删除本地临时文件（临时文件是分片生成的）
     * @param filePath 要删除的文件路径
     * @returns Promise<boolean> 删除是否成功
     */
    deleteLocalFile(filePath: string): Promise<boolean> {
        return new Promise((resolve) => {
            if (!filePath) {
                resolve(false);
                return;
            }
            plus.io.resolveLocalFileSystemURL(
                filePath,
                (entry) => {
                    entry.remove(
                        () => { resolve(true); },
                        () => { resolve(false); }
                    );
                },
                () => { resolve(false); }
            );
        });
    };

    /**
     * 创建临时文件并写入数据
     * @param dirPath 目录路径标识（plus.io.PRIVATE_DOC等）
     * @param fileName 临时文件名
     * @param data 要写入的数据，可以是ArrayBuffer或字符串
     * @returns Promise<string> 创建的临时文件完整路径
     */
    createAndWriteTempFile(dirPath: number, fileName: String, data: ArrayBuffer | string): Promise<string> {
        return new Promise((resolve, reject) => {
            plus.io.requestFileSystem(
                dirPath,
                (dirEntry: any) => {
                    dirEntry.root.getFile(
                        fileName,
                        { create: true, exclusive: false },
                        (fileEntry: any) => {
                            fileEntry.createWriter(
                                (writer: any) => {
                                    const filePath = fileEntry.fullPath
                                    writer.onwrite = function () { resolve(filePath) }
                                    writer.onerror = function (e: any) { reject(e) }
                                    try {
                                        if (data) writer.writeAsBinary(data)
                                    } catch (e) { reject(e) }
                                },
                                (err: any) => reject(err)
                            )
                        },
                        (err: any) => reject(err)
                    )
                },
                (err) => { reject(err) }
            )
        })
    }

    /**
     * 删除临时文件
     * @param dirPath 目录路径标识（plus.io.PRIVATE_DOC等）
     * @param fileName 要删除的临时文件名
     * @returns Promise<boolean> 删除是否成功
     */
    deleteTempFile(dirPath: number, fileName: string): Promise<boolean> {
        return new Promise((resolve, reject) => {
            plus.io.requestFileSystem(
                dirPath,
                (dirEntry) => {
                    if (!dirEntry || !dirEntry.root) {
                        reject(new Error('Directory entry or root is undefined'));
                        return;
                    }
                    dirEntry.root.getFile(
                        fileName,
                        { create: false },
                        (fileEntry) => {
                            fileEntry.remove(
                                () => { resolve(true); },
                                () => { resolve(true); }
                            );
                        },
                        () => resolve(true)
                    );
                },
                () => resolve(true)
            );
        });
    }

    /**
     * 读取APP端文件分片的数据
     * @param filePath 本地文件路径
     * @param start 读取开始位置
     * @param length 读取数据长度
     * @returns Promise<string> Base64编码的分片数据
     */
    readAppFileChunk(filePath: string, start: number, length: number): Promise<string> {
        return new Promise((resolve, reject) => {
            plus.io.resolveLocalFileSystemURL(
                filePath,
                (entry: any) => {
                    entry.file(
                        (file: any) => {
                            const reader = new plus.io.FileReader();
                            try {
                                const slice = file.slice(start, start + length);
                                reader.readAsDataURL(slice);
                            } catch (sliceError) {
                                reject(sliceError);
                            }
                            reader.onloadend = (e: any) => {
                                if (e.target.readyState == 2) {
                                    try {
                                        const base64 = e.target.result.split(",")[1];
                                        resolve(base64);
                                    } catch (err) {
                                        reject(err);
                                    }
                                }
                            };
                            reader.onerror = (err) => { reject(err); };
                        },
                        (error: any) => { reject(error); }
                    );
                },
                (error) => { reject(error); }
            );
        });
    };



    /**
     * 使用TaskQueue并发上传分片（微信小程序端）
     *
     * @param uploadData 上传数据对象
     * @param concurrentLimit 并发上传的分片数量限制，控制同时进行的上传任务数
     * @param progressInfo 进度跟踪对象
     * 
     * @returns Promise<PartETag[]> 返回所有分片的ETag信息数组，按partNumber排序
     * 
     * @throws {Error} 当任何分片上传失败时抛出错误，包含具体的错误信息
     */
    async _uploadChunks(uploadData: UploadData, concurrentLimit: number, progressInfo: ProgressInfo) {
        try {
            const { uploadId, saveFilePath, fileSize, chunkCount, filePath } = uploadData;
            const fileManager = uni.getFileSystemManager();
            const partETags: PartETag[] = [];

            const taskQueue = new TaskQueue(concurrentLimit);

            // 创建所有分片上传任务
            const uploadTasks = [];
            for (let i = 0; i < chunkCount; i++) {
                const task = taskQueue.add(async () => {
                    return await this._uploadSingleChunk(
                        fileManager,
                        uploadId,
                        saveFilePath,
                        filePath,
                        i,
                        fileSize,
                        progressInfo
                    );
                });
                uploadTasks.push(task);
            }

            // 等待所有任务完成
            const results = await Promise.all(uploadTasks);

            // 收集所有 partETags
            results.forEach(partETag => {
                if (partETag) partETags.push(partETag);
            });

            // 按 partNumber 排序确保顺序正确
            partETags.sort((a, b) => a.partNumber - b.partNumber);

            return partETags;

        } catch (e) {
            const errorMessage = e instanceof Error ? e.message : '上传分片失败';
            throw new Error(errorMessage);
        }
    }

    /**
     * 上传单个分片（微信小程序端）
     * @param fileManager 文件管理器
     * @param uploadId 上传ID
     * @param saveFilePath 服务器保存路径
     * @param filePath 本地文件路径
     * @param chunkIndex 分片索引（从0开始）
     * @param fileSize 文件总大小
     * @param progressInfo 进度信息对象
     * @returns Promise<PartETag> 返回分片的ETag信息
     */
    private async _uploadSingleChunk(
        fileManager: UniApp.FileSystemManager,
        uploadId: string,
        saveFilePath: string,
        filePath: string,
        chunkIndex: number,
        fileSize: number,
        progressInfo: ProgressInfo
    ): Promise<PartETag> {
        const start = chunkIndex * this.chunkSize;
        const end = Math.min(start + this.chunkSize, fileSize);
        const tempChunkPath = `${wx.env.USER_DATA_PATH}/chunk_${chunkIndex}_${Date.now()}.tmp`;

        try {
            // 1. 处理分片数据
            await this._processChunk(fileManager, filePath, tempChunkPath, start, end - start);

            // 2. 上传分片
            const partNumber = chunkIndex + 1;
            const response = await uploadChunk(uploadId, saveFilePath, partNumber, tempChunkPath);

            if (!response.data?.etag) {
                throw new Error(`分片 ${partNumber} 上传失败，无效响应`);
            }

            // 3. 更新进度
            this.updateUploadProgress(progressInfo);

            return {
                partNumber,
                ETag: response.data.etag,
            };

        } finally {
            // 4. 清理临时文件（无论成功失败都要清理）
            this._cleanupTempFile(fileManager, tempChunkPath);
        }
    }


    /**
     * 处理单个分片数据
     * 
     * @param fileManager - uni-app文件系统管理器实例
     * @param filePath - 原始文件的完整路径
     * @param tempChunkPath - 临时分片文件的保存路径
     * @param start - 在原始文件中的起始位置
     * @param length - 要读取的数据长度
     * @returns Promise<void> - 操作完成的Promise
     * @throws {Error} 当文件读取或写入失败时抛出错误
     */
    async _processChunk(fileManager: UniApp.FileSystemManager, filePath: string, tempChunkPath: string, start: number, length: number) {
        const readRes = await new Promise<ArrayBuffer | string>((resolve, reject) => {
            fileManager.readFile({
                filePath: filePath,
                position: start,
                length: length,
                success: (res: any) => {
                    resolve(res.data as ArrayBuffer | string)
                },
                fail: (err) => {
                    reject(err)
                },
            });
        });
        // 写入临时文件
        await new Promise((resolve, reject) => {
            fileManager.writeFile({
                filePath: tempChunkPath,
                data: readRes,
                success: () => {
                    resolve(true)
                },
                fail: (err) => {
                    reject(err)
                },
            });
        });
    }

    /**
     * 清理临时分片文件
     * 
     * @param fileManager - uni-app文件系统管理器实例
     * @param tempChunkPath - 要删除的临时文件路径
     * @throws {Error} 当文件删除失败时抛出错误
     */
    _cleanupTempFile(fileManager: UniApp.FileSystemManager, tempChunkPath: string) {
        try {
            fileManager.unlinkSync(tempChunkPath);
        } catch (e) {
            const errorMessage = e instanceof Error ? e.message : '未知错误';
            throw new Error(`删除临时文件失败: ${tempChunkPath}, 错误: ${errorMessage}`);
        }
    }

    /**
     * 更新上传进度并显示加载状态
     * 
     * @param progressInfo 进度信息对象，包含completedChunks、uploadProgress、chunkCount等属性
     */
    private updateUploadProgress(progressInfo: ProgressInfo): void {
        // 增加已完成分片数
        progressInfo.completedChunks++;

        // 计算当前进度百分比
        const percent = Math.floor((progressInfo.completedChunks / progressInfo.chunkCount) * 100);

        // 计算显示进度（按间隔更新，避免过于频繁的UI更新）
        const displayPercent = Math.floor(percent / ChunkUpload.PROGRESS_UPDATE_INTERVAL) * ChunkUpload.PROGRESS_UPDATE_INTERVAL;

        // 当显示进度发生变化或上传完成时，更新UI
        if (displayPercent !== progressInfo.uploadProgress || progressInfo.completedChunks === progressInfo.chunkCount) {
            modal.closeLoading();
            const displayPercentForUI = progressInfo.completedChunks === progressInfo.chunkCount ? 100 : displayPercent;
            modal.loading(`上传中 ${displayPercentForUI}% (请勿离开此页面)`);
            progressInfo.uploadProgress = displayPercent;
        }
    }



}

export const chunkUpload = new ChunkUpload();