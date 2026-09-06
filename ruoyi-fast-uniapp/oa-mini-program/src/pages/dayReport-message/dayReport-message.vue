<script lang="ts" setup>
import { reportStatusListByTwoIds,reportByTwoIds } from '@/service/api/user';
import { getCurrentInstance } from 'vue';
onLoad(() => {
    getQuery()
})
const dayReportList = ref<any>({})
const templateId = ref<any>()
const recordId = ref<any>()
function getQuery() {
    const instance:any = getCurrentInstance()?.proxy
    const eventChannel = instance!.getOpenerEventChannel();

    eventChannel.on('data', async (res) => {
        const resData = await reportByTwoIds({
            templateId:res.reportTemplateId,
            recordId:res.reportRecordId
        })
        dayReportList.value = resData[0]
        templateId.value = res.reportTemplateId
        recordId.value = res.reportRecordId
        uni.setNavigationBarTitle({
            title: resData[0].userNameAvatarVO.name+'提交的'+resData[0].reportTemplateVO?.templateName
        })
        getPerson()
    })
}
const list1 = ref([
    { name: '已读' },
    { name: '未读' }
])
const active = ref(0)
function clickRead(val) {
    active.value = val.index
}
const personList = ref<any>([])
async function getPerson() {
    const data = await reportStatusListByTwoIds({
        templateId: templateId.value,
        recordId: recordId.value
    })
    personList.value = data
    console.log("🚀 ~ getPerson ~ personList.value:", personList.value)
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
        <view style="background: #f6f6f6;padding: 20rpx;">
            <wd-cell-group border>
                <wd-cell title="汇报编号" :value=dayReportList.reportNumber></wd-cell>
                <wd-cell title="汇报周期" :value=dayReportList.reportPeriodStr></wd-cell>
                <wd-cell title="提交人" :value=dayReportList.userNameAvatarVO?.name></wd-cell>
                <wd-cell title="提交时间" :value=dayReportList.reportTimeInfo></wd-cell>
            </wd-cell-group>
            <view class="main">
                <view v-for="(item, index) in dayReportList.reportDetails" :key="index">
                    <view class="tt">{{ item.paragraphName }}</view>
                    <view
                        v-if="(item.paragraphType === '1' && item.reportText) || (item.paragraphType === '2' && item.reportText)"
                        class="tt2">
                        <wd-textarea v-model="item.reportText" readonly auto-height />
                    </view>
                    <view v-if="item.paragraphType === '3'" class="tt2">
                        <u-parse :content="item.reportText"></u-parse>
                    </view>
                    <view v-if="item.paragraphType === '4'" class="tt2">
                        <uni-file-picker v-model="item.baseFiles" fileMediatype="image" mode="grid" :auto-upload="false"
                            readonly />
                    </view>
                    <view v-if="item.paragraphType === '5'" class="tt2">
                        <!-- <uni-file-picker v-model="item.baseFiles" limit="5" file-mediatype="all"
                            readonly></uni-file-picker> -->
                        <!-- <view v-for="(it, index) in item.baseFiles" :key="index"
                            style="width: 70%;margin: 0 auto;margin-bottom: 20rpx;">
                        </view> -->
                        <view v-for="(it, i) in item.baseFiles" :key="i" style="margin-bottom: 20rpx;">
                            <view class="flex justify-start align-center">
                                <view style="width: 70%;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;" @click="online(item)">
                                    {{ it.name }}
                                </view>
                                <view style="margin-left: 40rpx;color: #5581f8;" @click="online(it)">预览</view>
                            </view>
                        </view>
                    </view>
                </view>
            </view>
            <view class="readPerson">
                <u-tabs :list="list1" @click="clickRead"></u-tabs>
                <view v-if="active === 0" class="box">
                    <view v-for="(item) in personList.readUsers" :key="item" class="avatar">
                        <img :src=item.avatar alt="" style="width: 66rpx;height: 66rpx;">
                        <view
                            style="font-family: Source Han Sans CN;font-weight: 400;font-size: 26rpx;color: #666666;line-height: 46rpx;">
                            {{ item.name }}
                        </view>
                    </view>
                </view>
                <view v-if="active === 1" class="box">
                    <view v-for="(item, index) in personList.unreadUsers" :key="index" class="avatar">
                        <img :src=item.avatar alt="" style="width: 66rpx;height: 66rpx;">
                        <view
                            style="font-family: Source Han Sans CN;font-weight: 400;font-size: 26rpx;color: #666666;line-height: 46rpx;">
                            {{ item.name }}
                        </view>
                    </view>
                </view>

            </view>
        </view>
        <view style="padding: 20rpx;color: #666666;">
            <wd-divider>评论功能正在开发中</wd-divider>
        </view>
    </view>

</template>
<style scoped lang="scss">
.content {
    height: 100vh;
    background: #F6F6F6;

    // padding: 20rpx;
    :deep(.wd-cell__value.data-v-a65b3963) {
        width: 440rpx;
    }

    :deep(.wd-textarea.is-auto-height.data-v-324b46d3::after) {
        display: none;
    }

    :deep(.file-picker__box) {
        width: 25% !important;
        padding-top: 25% !important;
    }

    .main {
        margin-top: 20rpx;
        background: #FFFFFF;
        padding: 20rpx;

        // :deep(.uni-file-picker__lists) {
        //     display: none;
        // }

        .tt {
            font-family: Source Han Sans CN;
            font-weight: bold;
            font-size: 30rpx;
            color: #333333;
            margin-bottom: 30rpx;
        }

        .tt2 {
            font-family: Source Han Sans CN;
            font-weight: 400;
            font-size: 26rpx;
            color: #333333;
            margin-bottom: 30rpx;

        }
    }

    .readPerson {
        margin-top: 20rpx;
        background: #FFFFFF;

        .box {
            overflow-x: scroll;
            display: flex;
            align-items: center;
            padding: 20rpx;

            .avatar {
                display: flex;
                margin-right: 30rpx;
                flex-direction: column;
                align-items: center;
                flex-shrink: 0;
                justify-content: center;
            }
        }
    }


}
</style>