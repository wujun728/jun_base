<script lang="ts" setup>
import { useInit } from '@/hooks/useInit';
// import type { UpdateUserInfo } from '@/service/types/user';
import { updateUserInfo, getUserInfo } from '@/service/api/user'

import { useUserInfoStore, useUserStore } from '@/store';
import { back } from '@/utils/router';
import env from "@/config/env";

onLoad(() => {
    getUserInfoFn()
    const { pageName, pagePath, pageQuery } = useInit();
    console.log(pageName, pagePath, pageQuery, 'pageName,pagePath, pageQuery');
});
// onMounted(() => {
//     getUserInfoFn()
// })
const userStore = useUserInfoStore();
const userStore2 = useUserStore();

const src = ref<string>('')
const show = ref<boolean>(false)
let model = reactive<any>({
    avatar: userStore.users.users?.avatar,
    name: userStore.users.users?.name,
    gender: userStore.users.users?.gender,
    telephone: userStore.users.users?.telephone,
    signature: userStore.users.users?.signature,
    introduction: userStore.users.users?.introduction,
    postId: userStore.users.users?.postId,
    deptIds: userStore.users.users?.deptIds,
    deptNames: userStore.users.users?.deptNames,
    userId: userStore.users.users?.userId,
    fileList: userStore.users.users?.photoFiles ? userStore.users.users?.photoFiles : [],
    photo: ''
})

function upload(e) {
    console.log("🚀 ~ upload ~ e:", e)

    uni.chooseImage({
        count: 1,
        success: (res) => {
            console.log("🚀 ~ upload ~ res:", res)
            const tempFilePaths = res.tempFilePaths[0]
            src.value = tempFilePaths
            show.value = true
            uni.uploadFile({
                url: env.apiBaseUrl + '/file/upload', // 上传图片的后端接口
                filePath: tempFilePaths,
                header: {
                    "satoken": uni.getStorageSync('token'),
                    "Content-Type": "multipart/form-data",
                },
                name: 'file',
                success: res => {
                    let uploaddata = JSON.parse(res.data)
                    model.avatar = uploaddata.data.url
                    console.log("🚀 ~ upload ~ model.avatar:", model.avatar)
                }
            })
        }
    })

}
function handleConfirm() {
    // console.log("🚀 ~ handleConfirm ~ event:", event)
    // const { tempFilePath } = event
    // model.avatar = tempFilePath
}
function handleCancel(event) {
    console.log('取消', event)
}

const sex = ref<any[]>([
    {
        value: '1',
        label: '男'
    },
    {
        value: '2',
        label: '女'
    }
])
function handleFileChange({ fileList }) {
    console.log("🚀 ~ handleFileChange ~ fileList:", fileList)
    model.fileList = fileList
}
async function getUserInfoFn() {
    const res = await getUserInfo({})
    userStore.setUserInfo2({
        users: { ...res.user }
    });
    model = {
        avatar: userStore.users.users?.avatar,
        name: userStore.users.users?.name,
        gender: userStore.users.users?.gender,
        telephone: userStore.users.users?.telephone,
        signature: userStore.users.users?.signature,
        introduction: userStore.users.users?.introduction,
        postId: userStore.users.users?.postId,
        deptIds: userStore.users.users?.deptIds,
        deptNames: userStore.users.users?.deptNames,
        userId: userStore.users.users?.userId,
        fileList: userStore.users.users?.photoFiles ? userStore.users.users?.photoFiles : [],
        photo: ''
    }

}
async function submit() {
    console.log(model, "🚀 ~ model ~ model:");

    const photo: any = []
    const ff = model.fileList?.map(item => {
        // eslint-disable-next-line no-param-reassign
        item = item.response ? JSON.parse(item.response).data : item
        photo.push(item.id)
        return {
            uid: item.id,
            name: item.fileName,
            status: 'done',
            url: item.url
        }
    })
    console.log(ff);
    model.photo = photo.join()
    const res = await updateUserInfo(model)
    if (res) {
        getUserInfoFn()
        back(1)
    }

}


</script>

<template>
    <view class="content">
        <view class="c-info">
            <view>
                <wd-img-cropper v-model="show" :img-src="src" @confirm="handleConfirm" @cancel="handleCancel" />
                <view>
                    <wd-img :src="model.avatar" width="160rpx" height="160rpx" @click="upload" />
                    <view class="fushi" @click="upload">
                        <img src="../../static/icon/camera.png" alt="" style="width: 30rpx;height: 30rpx;">
                    </view>
                </view>
            </view>
            <view>
                <wd-cell-group border custom-class="group">
                    <wd-input v-model="model.name" type="text" label="姓名" label-width="180px" placeholder="请输入姓名" />
                    <wd-picker v-model="model.gender" label="性别" placeholder="请选择性别" label-width="180px"
                        prop="promotion" :columns="sex" />
                    <wd-input v-model="model.telephone" label="手机号" label-width="180px" placeholder="请输入手机号"
                        clearable />
                    <wd-textarea v-model="model.signature" label="我的签名" label-width="180px" :maxlength="20"
                        placeholder="请输入文字，最多输入20个字" clearable auto-height />
                </wd-cell-group>
            </view>
        </view>
        <view class="personalDis" style="margin-top: 20rpx;">
            <wd-textarea v-model="model.introduction" label="个人简介" label-width="180px" placeholder="请输入个人简介" clearable
                auto-height />
        </view>
        <view class="personalDis" style="margin-top: 20rpx;">
            <wd-cell title="个人照片" title-width="100px">
                <wd-upload :file-list="model.fileList" :action="env.apiBaseUrl + '/file/upload'"
                    :header="{ satoken: userStore2.user.token }" multiple @change="handleFileChange"></wd-upload>
            </wd-cell>
        </view>
        <view style="margin-top: 20rpx;">
            <wd-button type="primary" size="large" block @click="submit">提交</wd-button>
        </view>
    </view>
</template>

<style scoped lang="scss">
.content {
    height: 100vh;
    background: #F6F6F6;
    padding: 109rpx 20rpx 0 20rpx;

    .c-info {
        position: relative;
        width: 710rpx;
        background: #FFFFFF;
        border-radius: 14rpx;
        padding-top: 120rpx;

        :deep(.wd-img) {
            position: absolute;
            top: -88rpx;
            left: 50%;
            margin-left: -86rpx;
            width: 161rpx;
            height: 161rpx;
            // background: #A8C6FF !important;
            box-shadow: 0rpx 3rpx 8rpx 0rpx rgba(0, 0, 0, 0.1);
            border-radius: 50%;
            border: 6px solid #FFFFFF;
        }

        :deep(.wd-img image.data-v-2cf12824) {
            border-radius: 50%;
        }

        .fushi {
            position: absolute;
            top: 40rpx;
            right: 258rpx;
            display: flex;
            align-items: center;
            justify-content: center;
            width: 56rpx;
            height: 56rpx;
            background: #7EA4FD;
            border-radius: 50%;
            border: 2px solid #FFFFFF;
        }

    }

    :deep(.wd-textarea.is-cell.data-v-324b46d3) {
        display: block;
    }

    :deep(.wd-cell__wrapper.data-v-a65b3963) {
        display: block;
    }

    :deep(.wd-textarea.is-cell .wd-textarea__inner) {
        height: 100rpx !important;
    }
}
</style>