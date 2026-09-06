<script lang="ts" setup>
import { getCurrentInstance } from 'vue';
onLoad(() => {
    getQuery()
})
const dayReportList = ref<any>({})

function getQuery() {
    const instance:any = getCurrentInstance()?.proxy
    const eventChannel = instance!.getOpenerEventChannel();
    eventChannel.on('data', (res) => {
        dayReportList.value = res
        uni.setNavigationBarTitle({
            title: res.type === '1' ? '温馨提示' : '内部通知'
        })
        console.log("🚀 ~ eventChannel.on ~ dayReportList.value:", dayReportList.value)
    })
}
function online(item) {
    let file = item.name;
    let fileType: any = null;
    if (file.endsWith('.doc')) {
        fileType = 'doc';
    } else if (file.endsWith('.docx')) {
        fileType = 'docx';
    } else if (file.endsWith('.xls')) {
        fileType = 'xls';
    } else if (file.endsWith('.xlsx')) {
        fileType = 'xlsx';
    } else if (file.endsWith('.ppt')) {
        fileType = 'ppt';
    } else if (file.endsWith('.pptx')) {
        fileType = 'pptx';
    } else if (file.endsWith('.pdf')) {
        fileType = 'pdf';
    }

    const imageRegex = /(\.jpg|\.jpeg|\.png)$/i;
    if (imageRegex.test(item.name)) {
        console.log("图片类型");
        lookImage(item.url)
    }
    function lookImage(url) {
        let imgArray:any = [];
        imgArray[0] = url
        uni.previewImage({
            current: 0,
            urls: imgArray
        })
    }
    uni.downloadFile({
        url: item.url,
        success: function (res) {
            let filePath = res.tempFilePath;
            uni.openDocument({
                filePath: filePath,
                fileType,
                success: function () {
                    console.log('打开文档成功');
                }
            });
        }
    },
    );
}
</script>
<template>
    <view class="content">
        <view class="main">
            <view class="title">{{ dayReportList.title }}</view>
            <view class="name">{{ dayReportList.creatorName }}发布于{{ dayReportList.createTime }}</view>
            <view class="richText">
                <u-parse :content="dayReportList.description"></u-parse>
            </view>
            <view v-if="dayReportList.baseFiles"
                style="font-family: Source Han Sans CN;font-weight: 500;font-size: 26rpx;color: #333333;margin-left: 20rpx;">
                附件</view>
            <view v-for="(it, i) in dayReportList.baseFiles" :key="i" class="files">
                <view class="flex justify-start align-center">
                    <view style="width: 70%;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;"
                        @click="online(it)">
                        {{ it.name }}
                    </view>
                    <view style="margin-left: 40rpx;color: #5581f8;" @click="online(it)">预览</view>
                </view>
            </view>
        </view>
    </view>
</template>
<style scoped lang="scss">
.content {
    height: 100vh;

    .main {
        width: 100%;
        // height: 400rpx;

        .title {
            margin: 20rpx 0;
            font-family: Source Han Sans CN;
            font-weight: bold;
            font-size: 40rpx;
            color: #333333;
            line-height: 46rpx;
            text-align: center;

        }

        .name {
            font-family: Source Han Sans CN;
            font-weight: 500;
            font-size: 24rpx;
            color: #666666;
            line-height: 46rpx;
            text-align: center;

        }

        .richText {
            padding: 20rpx;
            font-family: Source Han Sans CN;
            font-weight: 400;
            font-size: 26rpx;
            color: #666666;
            line-height: 52rpx;
        }

        .files {
            padding: 0 20rpx;
            font-family: Source Han Sans CN;
            font-weight: 400;
            font-size: 26rpx;
            color: #666666;
            line-height: 52rpx;
        }
    }
}
</style>