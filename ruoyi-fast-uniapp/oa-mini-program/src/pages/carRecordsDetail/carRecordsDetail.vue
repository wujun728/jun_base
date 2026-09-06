<script lang="ts" setup>
// import { dayjs } from "wot-design-uni";
import env from "@/config/env";

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
    // realStartTime: dayjs(new Date()).format('YYYY-MM-DD HH:mm'),
    // realEndTime: dayjs(new Date()).format('YYYY-MM-DD HH:mm')
})
const vehicleUsageStatus = ref()
const usingRecord = ref()

function getType() {
    const instance:any = getCurrentInstance()?.proxy
    const eventChannel = instance!.getOpenerEventChannel();
    eventChannel.on('data', async (res) => {
        console.log("🚀 ~ eventChannel.on ~ res:", res)
        uni.setNavigationBarTitle({
            title: res.userName + '的用车记录'
        })
        model.vehicleName = res.vehicleName
        formData.realStartTime = res.realStartTime
        model.vehicleId = res.vehicleId
        formData.vehicleId = res.vehicleId
        formData.realEndTime = res.realEndTime
        formData.panelMileageBefore = res.panelMileageBefore
        formData.id = res.id
        formData.panelMileageAfter = res.panelMileageAfter
        formData.panelMileageThisTime = res.panelMileageThisTime
        formData.parkingPlace = res.parkingPlace
        formData.returnRemark = res.returnRemark
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
        if (res.panelPhotoAfter) {
            const arr = res.panelPhotoAfter.split(',')
            const aa = arr.map(item => {
                return {
                    url: item
                }
            })
            formData.panelPhotoAfter = aa
        }
        if (res.externalPhotoAfter) {
            const arr = res.externalPhotoAfter.split(',')
            const aa = arr.map(item => {
                return {
                    url: item
                }
            })
            formData.externalPhotoAfter = aa
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
                        <wd-cell title="实际开始时间" :value="formData.realStartTime" />
                        <wd-cell title="开始仪表盘里程数" :value="formData.panelMileageBefore" />
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
                还车信息
            </view>
            <view class="xianzhi">
                <wd-form ref="form" :model="formData">
                    <wd-cell-group border>
                        <wd-cell title="实际结束时间" :value="formData.realEndTime" />
                        <wd-cell title="结束仪表盘里程数" :value="formData.panelMileageAfter" />
                        <wd-cell title="本次里程数" :value="formData.panelMileageThisTime" />
                        <wd-cell title="还车停放位置" :value="formData.parkingPlace" />
                        <wd-cell title="备注" :value="formData.returnRemark" />
                        <!-- <wd-textarea v-model="formData.returnRemark" label="备注" label-width="200px" type="textarea"
                            placeholder="请输入备注" clearable /> -->
                        <view class="picture">
                            <wd-cell title="车辆外观照片(前、后、侧面)" title-width="200px">
                                <wd-upload :file-list="formData.externalPhotoAfter"
                                    :action="env.apiBaseUrl + '/file/upload'" :header="{ satoken: token }" disabled
                                    @change="handleFileChange"></wd-upload>
                            </wd-cell>
                            <wd-cell title="车辆仪表盘照片" title-width="200px">
                                <wd-upload :file-list="formData.panelPhotoAfter"
                                    :action="env.apiBaseUrl + '/file/upload'" :header="{ satoken: token }" disabled
                                    @change="handleFileChange2"></wd-upload>
                            </wd-cell>
                        </view>

                    </wd-cell-group>
                </wd-form>
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