<template>
    <view class="upload-box">
        <view>
            <view class="upload-container" v-if="!videoCover" @click="chooseVideo">
                <uni-icons type="cloud-upload-filled" size="60" color="#ff6634"></uni-icons>
                <text class="upload-text">{{ uploadText }}</text>
                <text class="upload-desc">{{ displayUploadDesc }}</text>
            </view>
            <view v-else class="video-preview">
                <view class="cover-container">
                    <view class="icon-cuo" @tap.stop="delectVideo">
                        <uni-icons type="close" size="16" color="#ffffff"></uni-icons>
                    </view>
                    <video :src="videoCover" class="video-cover" />
                </view>
            </view>
        </view>
    </view>
    <ConfirmDialog v-model:visible="showModal" title="是否要删除此视频" @confirm="confirmDelete" />
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { defineProps, defineEmits } from 'vue'
import modal from '@/plugins/modal';
import ConfirmDialog from '@/components/geek-xd/components/geek-confirm-dialog/geek-confirm-dialog.vue'

const props = defineProps({
    uploadText: {
        type: String,
        default: '点击上传视频文件'
    },
    uploadDesc: {
        type: String,
        default: ''
    },
    sourceType: {
        type: Array,
        default: () => ['album', 'camera']
    },
    maxSize: {
        type: Number,
        default: 500  // 默认500M
    },
    supportedTypes: {
        type: Array,
        default: () => ['mp4', 'mov', 'avi']  // 支持的视频格式
    }
})

const emit = defineEmits(['upload', 'delete'])

const videoCover = ref('')
const showModal = ref(false)

/**
 * 动态生成上传描述文本
 * 如果用户提供了自定义描述则使用，否则根据支持的格式和文件大小限制自动生成
 * @returns {string} 上传描述文本
 */
const displayUploadDesc = computed(() => {
    return props.uploadDesc || `支持${props.supportedTypes.join('/')}等格式，文件大小不超过${props.maxSize}M`
})

/**
 * 删除视频处理函数
 * 阻止事件冒泡并显示确认删除对话框
 * @param {Event} event - 点击事件对象
 */
const delectVideo = (event) => {
    event.stopPropagation();
    showModal.value = true;
}

/**
 * 确认删除视频
 * 关闭确认对话框，清空视频封面，并触发删除事件
 */
const confirmDelete = () => {
    showModal.value = false
    videoCover.value = null
    emit('delete')
}

/**
 * 从文件路径中提取文件扩展名
 * @param {string} filePath - 文件路径
 * @returns {string} 文件扩展名（小写），如果没有扩展名则返回空字符串
 */
const getFileExtension = (filePath) => {
    if (!filePath) {
        return '';
    }
    const dotIndex = filePath.lastIndexOf('.');
    if (dotIndex === -1) {
        return '';
    }
    return filePath.substring(dotIndex + 1).toLowerCase();
}

/**
 * 验证视频文件类型是否符合支持的格式
 * 统一的跨平台文件类型验证
 * @param {string} filePath - 视频文件路径
 * @returns {boolean} 验证通过返回true，否则返回false
 */
const validateVideoFileType = (filePath) => {
    if (!filePath) {
        modal.msg('无法识别文件类型');
        return false;
    }

    const fileExtension = getFileExtension(filePath);
    console.log('文件类型:', fileExtension);

    if (props.supportedTypes.includes(fileExtension)) return true;

    modal.msg(`请上传正确格式的视频`);
    return false;
}

/**
 * 检查视频文件大小是否超过限制
 * @param {number} fileSize - 文件大小（字节）
 * @returns {boolean} 文件大小符合要求返回true，否则返回false
 */
const checkVideoFileSize = (fileSize) => {
    const maxSize = props.maxSize * 1024 * 1024;
    if (fileSize <= maxSize) return true;
    modal.msg(`视频大小不能超过${props.maxSize}M`);
    return false;
}

/**
 * 组装视频数据对象
 * 根据不同平台返回不同格式的视频数据
 * @param {Object} res - uni.chooseVideo返回的结果对象
 * @returns {Object} 组装后的视频数据对象
 */
const buildVideoData = (res) => {
    let videoData = {}
    console.log('选择的视频文件:', res);

    // #ifdef APP-PLUS || H5
    videoData = {
        path: res.tempFilePath,
        size: res.size,
    }
    // #endif

    // #ifdef MP-WEIXIN
    videoData = {
        path: res.tempFilePath,
        size:res.size
    }
    // #endif


    console.log('组装后的视频数据:', videoData);

    return videoData;
}

/**
 * 选择视频文件
 * 调用uni-app的chooseVideo API选择视频文件
 * 包含文件类型验证、大小验证和数据处理
 */
const chooseVideo = () => {
    uni.chooseVideo({
        count: 1,
        compressed: false,
        sourceType: props.sourceType,
        success: (res) => {
            if (!validateVideoFileType(res.tempFilePath)) return;
            if (!checkVideoFileSize(res.size)) return;
            videoCover.value = res.tempFilePath
            const videoData = buildVideoData(res)
            setTimeout(() => {
                modal.msgSuccess('上传成功')
                emit('upload', videoData)
            }, 1000)
        },
        fail: (err) => {
            modal.msg('选择视频失败')
        }
    })
}
</script>

<style lang="scss" scoped>
.upload-box {
    height: 350rpx;
    margin: 0 20rpx;
    background: #FFFCFA;
    border: 2rpx dashed #ff6634;
    border-radius: 8rpx;
    padding: 40rpx 0;
    text-align: center;
    margin-bottom: 30rpx;
    margin-top: 20rpx;

    .upload-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100%;
        cursor: pointer;
    }

    .upload-text {
        display: block;
        font-size: 28rpx;
        color: #ff6634;
        margin-bottom: 10rpx;
    }

    .upload-desc {
        display: block;
        font-size: 24rpx;
        color: #999;
    }

    .video-preview {
        position: relative;
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;

        .cover-container {
            position: relative;
            width: 80%;
            height: 100%;

            .icon-cuo {
                position: absolute;
                top: -20rpx;
                right: -30rpx;
                width: 44rpx;
                height: 44rpx;
                display: flex;
                align-items: center;
                justify-content: center;
                background-color: rgba(0, 0, 0, 0.7);
                color: #fff;
                border-radius: 50%;
                z-index: 10;
                box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.3);
                transition: all 0.2s ease;

                &:active {
                    transform: scale(0.9);
                    background-color: #ff4d4f;
                }
            }

            .video-cover {
                width: 100%;
                height: 220rpx;
                border-radius: 8rpx;
                object-fit: cover;
            }
        }
    }
}
</style>