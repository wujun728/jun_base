import { completeMultipartUpload, initMultipartUpload, uploadFileChunk } from "@/api/file/info";
import TaskQueue from "./TaskQueue";
import { computed, ref } from "vue";

type Chunk = {
  partNumber: number;
  chunk: Blob;
};

export function useChunkUpload(file?: File, options?: { chunkSize?: number; concurrency?: number }) {
  const chunkBytes = options?.chunkSize || 5 * 1024 * 1024; // 5MB
  const maxConcurrency = options?.concurrency || 3; // 最大并发数
  let uploadId: string | null = null;
  let filePath: string | null = null;
  let chunks: Chunk[] = [];
  let partETags: { partNumber: number; etag: string }[] = [];

  const totalChunks = ref(0);
  const uploadedChunks = ref(0);
  const uploadPercentage = computed(() => {
    if (totalChunks.value === 0) return 0;
    return Math.round((uploadedChunks.value / totalChunks.value) * 100);
  });
  const uploadMessage = ref<string>('');
  const uploadStatus = ref<'' | 'success' | 'warning' | 'exception'>('');
  async function startUpload() {
    uploadMessage.value = '开始初始化上传...';
    uploadStatus.value = '';
    if (!file) throw new Error('未选择文件');
    chunks = [];
    partETags = [];
    const fileName = file.name;
    const fileSize = file.size;
    uploadedChunks.value = 0;
    const { data } = await initMultipartUpload({ fileName, fileSize });
    uploadId = data.uploadId;
    filePath = data.filePath;

    uploadMessage.value = '初始化成功，开始上传分片...';
    for (let i = 0; i < file.size; i += chunkBytes) {
      chunks.push({
        partNumber: i + 1,
        chunk: file.slice(i, i + chunkBytes)
      });
    }

    totalChunks.value = chunks.length;

    const taskQueue = new TaskQueue(maxConcurrency);
    chunks.forEach((chunk) => taskQueue.add(async () => {
      try {
        const { data: chunkResponse } = await uploadFileChunk(uploadId, filePath, chunk.partNumber, chunk.chunk);
        if (!chunkResponse || !chunkResponse.etag) {
          throw new Error('服务器返回的分片信息无效');
        }
        partETags.push({ partNumber: chunk.partNumber, etag: chunkResponse.etag });
        uploadedChunks.value = partETags.length;
        uploadMessage.value = `上传分片 ${uploadedChunks.value} / ${totalChunks.value}...`;
      } catch (error: any) {
        console.error('分片上传失败:', error);
        throw new Error(`分片 ${chunk.partNumber} 上传失败: ${error.message}`);
      }
    }));
    await taskQueue.waitAll();

    uploadMessage.value = '正在合并分片...';
    const formattedPartETags = partETags.map(item => ({ partNumber: item.partNumber, ETag: item.etag }));
    await completeMultipartUpload({
      uploadId: uploadId,
      filePath: filePath,
      fileSize: file.size,
      fileName: file.name,
      partETags: formattedPartETags,
    });

    uploadMessage.value = '上传完成';
    uploadStatus.value = 'success';
  }
  return {
    startUpload,
    uploadPercentage,
    uploadStatus,
    uploadMessage,
    uploadedChunks,
    totalChunks
  }
}