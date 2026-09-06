<script lang="ts" setup>
import { dayjs } from "wot-design-uni";
import env from "@/config/env";
import { returnVehicle } from "@/service/api/carManage";
import { back } from "@/utils/router";
import type { FormRules } from "wot-design-uni/components/wd-form/types";

onLoad(() => {
    getType()

});
const form = ref()
const token = ref(uni.getStorageSync('token'))
const model = reactive<any>({
    vehicleName: '',
    destination: '',
    startTime: '',
    endTime: '',
    projectName: '',
    deptName: '',
    userName: '',
    otherUserNames: '',
    remark: '',
    fileList: []
})
const formData = reactive<any>({
    vehicleId: '',
    panelMileageThisTime: 0,
    // realStartTime: dayjs(new Date()).format('YYYY-MM-DD HH:mm'),
    realEndTime: dayjs(new Date()).format('YYYY-MM-DD HH:mm')
})
const vehicleUsageStatus = ref()
const usingRecord = ref()

function getType() {
    const instance: any = getCurrentInstance()?.proxy
    const eventChannel = instance!.getOpenerEventChannel();
    eventChannel.on('data', async (res) => {
        console.log("🚀 ~ eventChannel.on ~ res:", res)
        uni.setNavigationBarTitle({
            title: res.userName + '的用车记录'
        })
        formData.panelMileageThisTime = 0
        model.vehicleName = res.vehicleName
        formData.realStartTime = res.realStartTime
        model.vehicleId = res.vehicleId
        formData.vehicleId = res.vehicleId
        formData.panelMileageBefore = res.panelMileageBefore
        formData.id = res.id
        model.id = res.id
        model.destination = res.destination
        model.deptName = res.deptName
        model.phoneNumber = res.phoneNumber
        model.projectName = res.projectName
        model.UserName = res.userName
        model.otherUserNames = res.otherUserNames
        model.startTime = res.startTime
        model.endTime = res.endTime
        model.remark = res.remark
        vehicleUsageStatus.value = res.vehicleUsageStatus
        usingRecord.value = res.usingRecord
        if (res.externalPhotoBefore) {
            const arr = res.externalPhotoBefore.split(',')
            const aa = arr.map(item => {
                return {
                    url: item
                }
            })
            formData.externalPhotoBefore = aa
        }
        if (res.panelPhotoBefore) {
            const arr = res.panelPhotoBefore.split(',')
            const aa = arr.map(item => {
                return {
                    url: item
                }
            })
            formData.panelPhotoBefore = aa
        }
    })
}



function handleFileChange({ fileList }) {
    console.log("🚀 ~ handleFileChange ~ fileList:", fileList)
    formData.externalPhotoAfter = fileList
}
function handleFileChange2({ fileList }) {
    console.log("🚀 ~ handleFileChange ~ fileList:", fileList)
    formData.panelPhotoAfter = fileList
}

// 计算本次里程数
function panelMileageThisTimeFn() {
    console.log("🚀 ~ panelMileageThisTimeFn ~ Number(formData.panelMileageAfter):", Number(formData.panelMileageAfter))

    if (isNaN(Number(formData.panelMileageAfter))) {
        formData.panelMileageThisTime = ''
    } else {
        if ((Number(formData.panelMileageAfter) === 0)) {
            formData.panelMileageThisTime = ''

        } else {
            formData.panelMileageThisTime = Number(formData.panelMileageAfter) - Number(formData.panelMileageBefore)
            formData.panelMileageThisTime = formData.panelMileageThisTime.toFixed(2);
        }

    }

}

const photo1 = ref<any>([])
const photo2 = ref<any>([])
function handleSubmit() {
    photo1.value = []
    photo2.value = []
    // eslint-disable-next-line array-callback-return
    formData.externalPhotoAfter?.map(item => {
        // eslint-disable-next-line no-param-reassign
        item = item.response ? JSON.parse(item.response).data : item
        photo1.value.push(item.url)
    })
    // eslint-disable-next-line array-callback-return
    formData.panelPhotoAfter?.map(item => {
        // eslint-disable-next-line no-param-reassign
        item = item.response ? JSON.parse(item.response).data : item
        photo2.value.push(item.url)
    })
    form.value
        .validate()
        .then(async ({ valid }) => {
            if (valid) {
                delete formData.externalPhotoBefore
                delete formData.panelPhotoBefore
                const data = await returnVehicle({
                    ...formData,
                    externalPhotoAfter: photo1.value && photo1.value.length > 0 ? photo1.value.join(',') : '',
                    panelPhotoAfter: photo2.value && photo2.value.length > 0 ? photo2.value.join(',') : ''
                })
                if (data) {
                    back(1)
                }
            }
        })
        .catch((error) => {
            console.log(error, 'error')
        })
}

const rules: FormRules = {
    panelMileageAfter: [
        {
            required: true,
            pattern: /^[+-]?(\d|([1-9]\d+))(\.\d+)?$/,
            message: '输入数字',
            validator: (value) => {
                if (value) {
                    return Promise.resolve()
                } else {
                    // eslint-disable-next-line prefer-promise-reject-errors
                    return Promise.reject('请填写仪表盘里程数')
                }
            }
        }
    ],
}

</script>

<template>
    <view class="content">
        <view class="main">
            <wd-cell-group border>
                <wd-cell title="车辆名称" :value="model.vehicleName" />
                <wd-cell title="去往地点" :value="model.destination" />
                <wd-cell title="预计开始时间" :value="model.startTime" />
                <wd-cell title="预计结束时间" :value="model.endTime" />
                <wd-cell title="使用部门" :value="model.deptName" />
                <wd-cell title="关联项目" :value="model.projectName" />
                <wd-cell title="使用人员" :value="model.UserName" />
                <wd-cell title="同行人员" :value="model.otherUserNames" />
            </wd-cell-group>
            <view v-if="model.remark" style="background: #FFFFFF;margin: 20rpx 0;">
                <view class="m-tt">用车事由</view>
                <wd-textarea v-model="model.remark" readonly></wd-textarea>
            </view>
            <view
                style="font-family: Source Han Sans CN;font-weight: bold;font-size: 30rpx;color: #333333;margin-top: 20rpx;">
                出车信息</view>
            <view class="xianzhi">
                <wd-form ref="form" :model="formData">
                    <wd-cell-group border>
                        <wd-input v-model="formData.realStartTime" label="实际开始时间" prop="value1" readonly />
                        <wd-input v-model="formData.panelMileageBefore" label="开始仪表盘里程数" label-width="200px" readonly />
                        <view class="picture">
                            <wd-cell title="车辆外观照片(前、后、侧面)" title-width="200px">
                                <wd-upload :file-list="formData.externalPhotoBefore"
                                    :action="env.apiBaseUrl + '/file/upload'" :header="{ satoken: token }" disabled
                                    @change="handleFileChange"></wd-upload>
                            </wd-cell>
                            <wd-cell title="车辆仪表盘照片" title-width="200px">
                                <wd-upload :file-list="formData.panelPhotoBefore"
                                    :action="env.apiBaseUrl + '/file/upload'" :header="{ satoken: token }" disabled
                                    @change="handleFileChange2"></wd-upload>
                            </wd-cell>
                        </view>

                    </wd-cell-group>
                </wd-form>
            </view>
            <view
                style="font-family: Source Han Sans CN;font-weight: bold;font-size: 30rpx;color: #333333;margin-top: 20rpx;">
                请填写还车信息</view>
            <view
                style="font-family: Source Han Sans CN;font-weight: 400;font-size: 20rpx;color: #F8941F;margin: 20rpx 0;">
                注意：实际开始时间自动获取当前时间
            </view>
            <view class="xianzhi">
                <wd-form ref="form" :model="formData" :rules="rules">
                    <wd-cell-group border>
                        <wd-input v-model="formData.realEndTime" label="实际结束时间" prop="value1" readonly />
                        <wd-input v-model="formData.panelMileageAfter" label="结束仪表盘里程数" label-width="200px"
                            prop="panelMileageAfter" clearable placeholder="请填写里程数" @change="panelMileageThisTimeFn" />
                        <!-- <view class="number">
                            <wd-cell title="结束仪表盘里程数" title-width="200px" prop="panelMileageAfter"
                                :rules="[{ required: true, message: '请填写仪表盘里程数' }]">
                                <wd-input-number v-model="formData.panelMileageAfter" :min="0" :precision="2"
                                    input-width="70px" @change="panelMileageThisTimeFn" />
                            </wd-cell>
                        </view> -->
                        <wd-cell title="本次里程数" :value="formData.panelMileageThisTime" />
                        <wd-input v-model="formData.parkingPlace" label="还车停放位置" label-width="200px" prop="parkingPlace"
                            clearable placeholder="请填写还车停放位置" :rules="[{ required: true, message: '请填写还车停放位置' }]" />
                        <wd-input v-model="formData.returnRemark" label="备注" label-width="200px" />
                        <view class="picture">
                            <wd-cell title="车辆外观照片(前、后、侧面)" title-width="200px" prop="externalPhotoAfter"
                                :rules="[{ required: true, message: '请上传照片' }]">
                                <wd-upload :file-list="formData.externalPhotoAfter"
                                    :action="env.apiBaseUrl + '/file/upload'" :header="{ satoken: token }" multiple
                                    :limit="4" @change="handleFileChange"></wd-upload>
                            </wd-cell>
                            <wd-cell title="车辆仪表盘照片" title-width="200px" prop="panelPhotoAfter"
                                :rules="[{ required: true, message: '请上传照片' }]">
                                <wd-upload :file-list="formData.panelPhotoAfter"
                                    :action="env.apiBaseUrl + '/file/upload'" :header="{ satoken: token }" multiple
                                    :limit="1" @change="handleFileChange2"></wd-upload>
                            </wd-cell>
                        </view>

                    </wd-cell-group>
                </wd-form>
                <view class="footer2">
                    <wd-button type="primary" block @click="handleSubmit">提交</wd-button>
                </view>
            </view>

        </view>
    </view>
</template>

<style scoped lang="scss">
.content {
    height: 100vh;
    background: #F6F6F6;

    .main {
        padding: 20rpx;
        background: #F6F6F6;

        .m-tt {
            font-family: Source Han Sans CN;
            font-weight: bold;
            font-size: 30rpx;
            color: #333333;
            padding: 20rpx;
        }

        .m-img {
            font-family: Source Han Sans CN;
            font-weight: bold;
            font-size: 30rpx;
            color: #333333;
            padding: 20rpx 0;
        }

        :deep(.wd-textarea.data-v-324b46d3) {
            padding: 0 20rpx;
        }

        :deep(.wd-upload__evoke.is-disabled.data-v-7c014dc0) {
            display: none;
        }

        .xianzhi {
            margin-top: 20rpx;

            .picture {
                :deep(.wd-cell__wrapper.data-v-a65b3963) {
                    display: block;
                }
            }

            :deep(.wd-input.is-cell .wd-input__inner.data-v-7397cfb5) {
                text-align: right;
            }
            :deep(.wd-upload__preview.data-v-7c014dc0 ){
                width: 73px;
            }

        }

        // :deep(.wd-cell__value.data-v-a65b3963) {
        //     display: flex;
        //     align-items: center;
        // }
    }


    .footer2 {
        :deep(.wd-button.is-medium.is-round.is-primary) {
            background: #7EA4FD;
            border-radius: 8rpx;
            font-family: Source Han Sans CN;
            font-weight: 500;
            font-size: 30rpx;
            color: #FFFFFF;
            flex: 1;
            margin-top: 20rpx;
        }
    }
}
</style>