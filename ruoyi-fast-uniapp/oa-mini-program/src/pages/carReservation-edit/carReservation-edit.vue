<script lang="ts" setup>
import { getDeptListWithUsers, usersList } from "@/service/api/user";
import { selectAllProject, bookVehicle, deleteVehicleRecord } from "@/service/api/carManage";
import type { FormRules } from "wot-design-uni/components/wd-form/types";
import { dayjs, useMessage } from "wot-design-uni";
import { back } from "@/utils/router";

onLoad(() => {
    getAllUserList()
    getUserList()
    // getType()
});

// const displayFormat = (items) => {
//     return `${items[0].label}年${items[1].label}月${items[2].label}日 ${items[3].label}:${items[4].label}`
// }
const formatter = (type, value) => {
    switch (type) {
        case 'year':
            return value + '年'
        case 'month':
            return value + '月'
        case 'date':
            return value + '日'
        case 'hour':
            return value + '时'
        case 'minute':
            return value + '分'
        default:
            return value
    }
}
const rules: FormRules = {
    startTime: [
        {
            required: true,
            message: '请选择开始时间',
            validator: (value) => {
                if (value) {
                    return Promise.resolve()
                } else {
                    return Promise!.reject()
                }
            }
        }
    ],
    endTime: [
        {
            required: true,
            message: '请选择开始时间',
            validator: (value) => {
                if (value) {
                    return Promise.resolve()
                } else {
                    return Promise!.reject()
                }
            }
        }
    ],
}
const model = reactive<any>({
    vehicleName: '',
    destination: '',
    startTime: '',
    endTime: '',
    project: '',
    deptName: '',
    projectName: '',
    remark: '',
    vehicleId: '',
    id: ''
})
const message = useMessage()

const form = ref()
// const projectList = ref<any>([])
const userNameAvatarVOs = ref<any>([])
const userNameAvatarVOs2 = ref<any>([])
const multiple = ref(false)
const selectParent = ref(false)
const checkStrictly = ref(false)
const nextTreeRef = ref()
const nextTreeRef2 = ref()
const nextTreeRefCell = ref()
const nextTreeRefProject = ref()
const treeData = ref<any>([])
const treeData2 = ref<any>([])
const UsersTreeData = ref<any>([])
const UsersTreeData2 = ref<any>([])
const deptId = ref<any>([])
const projectId = ref<any>([])
const usersIds = ref<any>([]) // 使用人员
const otherUserIds = ref<any>([])// 同行人员
const uuu = ref<any>([])// 所有人员
const startTime = ref<any>()
const endTime = ref<any>()
const showTreePro = ref(false)


const instance:any = getCurrentInstance()?.proxy
const eventChannel = instance!.getOpenerEventChannel();

function getType() {

    eventChannel.on('data', async (res) => {
        console.log("🚀 ~ eventChannel.on ~ res:", res)
        uni.setNavigationBarTitle({
            title: res.userName + '的用车记录'
        })
        model.vehicleName = res.vehicleName
        model.remark = res.remark
        model.vehicleId = res.vehicleId
        model.id = res.id
        model.destination = res.destination
        model.deptName = res.deptName
        model.projectName = res.projectName
        model.startTime = new Date(res.startTime).getTime()
        model.endTime = new Date(res.endTime).getTime()
        startTime.value = res.startTime
        endTime.value = res.endTime


        const userIdArr = String(res.userId).split(',')
        const otheruserIdArr = String(res.otherUserIds).split(',')
        if (userIdArr && userIdArr.length > 0) {
            const bbb = uuu.value.filter(it => userIdArr.includes(String(it.userId)))
            userNameAvatarVOs.value = bbb
        }
        if (otheruserIdArr && otheruserIdArr.length > 0) {
            const bbb2 = uuu.value.filter(it => otheruserIdArr.includes(String(it.userId)))
            userNameAvatarVOs2.value = bbb2
        }

    })
}
// 查询所有人员
async function getAllUserList() {
    console.log('uuuuuuuuuuuu');
    const data = await usersList({})
    uuu.value = data
    getType()
}
// tree的数据
async function getUserList() {
    treeData.value = await new Promise((resolve) => {
        getDeptListWithUsers({}).then((res) => {
            const fn = (list) => {
                if (list && list.length > 0) {
                    list.forEach((item) => {
                        item.label = item.deptName
                        item.id = item.deptId
                        if (item.children && item.children.length > 0) {
                            fn(item.children)
                        }
                    })
                }
            }
            fn(res)
            resolve(res)
        })
    })
    treeData2.value = await new Promise((resolve) => {
        selectAllProject({}).then((res) => {
            const fn = (list) => {
                if (list && list.length > 0) {
                    list.forEach((item) => {
                        item.label = item.name
                        if (item.children && item.children.length > 0) {
                            fn(item.children)
                        }
                    })
                }
            }
            fn(res)
            resolve(res)
        })
    })
    UsersTreeData.value = await new Promise((resolve) => {
        getDeptListWithUsers({}).then((res) => {
            const fn = (list) => {
                if (list && list.length > 0) {
                    list.forEach((item) => {
                        item.label = item.deptName || item.name
                        item.id = String(item.deptId || (item.userId + '_' + item.userId))
                        if (item.children && item.children.length > 0) {
                            if (item.users && item.users.length > 0) {
                                item.children = item.children.concat(JSON.parse(JSON.stringify(item.users)))
                            }
                            fn(item.children)
                        } else {
                            if (item.users && item.users.length > 0) {
                                item.children = JSON.parse(JSON.stringify(item.users))
                                fn(item.children)
                            } else {
                                delete item.children
                            }

                        }
                    })
                }
            }
            fn(res)
            resolve(res)

        })
    })
    UsersTreeData2.value = await new Promise((resolve) => {
        getDeptListWithUsers({}).then((res) => {
            const fn = (list) => {
                if (list && list.length > 0) {
                    list.forEach((item) => {
                        item.label = item.deptName || item.name
                        item.id = String(item.deptId || (item.userId + '_' + item.userId))
                        if (item.children && item.children.length > 0) {
                            if (item.users && item.users.length > 0) {
                                item.children = item.children.concat(JSON.parse(JSON.stringify(item.users)))
                            }
                            fn(item.children)
                        } else {
                            if (item.users && item.users.length > 0) {
                                item.children = JSON.parse(JSON.stringify(item.users))
                                fn(item.children)
                            } else {
                                delete item.children
                            }

                        }
                    })
                }
            }
            fn(res)
            resolve(res)

        })
    })
}
// 删除发送对象
function del(index) {
    userNameAvatarVOs.value.splice(index, 1)
}
function del2(index) {
    userNameAvatarVOs2.value.splice(index, 1)
}

// 选择人员弹框
function itemclick(_multiple, _selectParent, _checkStrictly = false) {
    echoDefault()
    multiple.value = _multiple
    selectParent.value = _selectParent
    checkStrictly.value = _checkStrictly
    unref(nextTreeRef).showTree = true
    showTreePro.value = true
}
function itemclick2(_multiple, _selectParent, _checkStrictly = false) {
    echoDefault2()
    multiple.value = _multiple
    selectParent.value = _selectParent
    checkStrictly.value = _checkStrictly
    unref(nextTreeRef2).showTree = true
    showTreePro.value = true
}
function itemCellclick(_multiple, _selectParent, _checkStrictly = false) {
    echoDefaultCell()
    multiple.value = _multiple
    selectParent.value = _selectParent
    checkStrictly.value = _checkStrictly
    unref(nextTreeRefCell).showTree = true
    showTreePro.value = true
}
function itemProJectclick(_multiple, _selectParent, _checkStrictly = false) {
    echoDefaultProject()
    multiple.value = _multiple
    selectParent.value = _selectParent
    checkStrictly.value = _checkStrictly
    unref(nextTreeRefProject).showTree = true
    showTreePro.value = true
}

// 默认勾选--回显
function echoDefault() {
    usersIds.value = []
    // eslint-disable-next-line array-callback-return
    userNameAvatarVOs.value?.map(item => {
        usersIds.value.push((item.userId + '_' + item.userId))
    })
    const selectIds = usersIds.value
    checkedTreeData(UsersTreeData.value, selectIds)
    multiple.value = true
    nextTreeRef.value.showTree = true
    showTreePro.value = true
}
function echoDefault2() {
    otherUserIds.value = []
    // eslint-disable-next-line array-callback-return
    userNameAvatarVOs2.value?.map(item => {
        otherUserIds.value.push((item.userId + '_' + item.userId))
    })
    const selectIds = otherUserIds.value
    checkedTreeData(UsersTreeData2.value, selectIds)
    multiple.value = true
    nextTreeRef2.value.showTree = true
    showTreePro.value = true
}
function echoDefaultCell() {
    const selectIds = deptId.value
    checkedTreeData(treeData.value, selectIds)
    multiple.value = false
    nextTreeRefCell.value.showTree = true
    showTreePro.value = true
}
function echoDefaultProject() {
    const selectIds = projectId.value
    checkedTreeData(treeData2.value, selectIds)
    multiple.value = false
    nextTreeRefProject.value.showTree = true
    showTreePro.value = true
}
function checkedTreeData(treeData, selectIds) {
    // eslint-disable-next-line array-callback-return
    treeData.map(item => {
        if (selectIds.indexOf(item.id) !== -1) {
            item.checked = true
        } else {
            item.checked = false
        }
        if (item.children && item.children.length) {
            checkedTreeData(item.children, selectIds)
        }
    })
}
function oncancel(val) {
    // 清除treeData的选中状态
    checkedTreeData(unref(val), [])
    showTreePro.value = false
}
function clear() {
    console.log('clearclearclearclear');
    showTreePro.value = true
}
// 数组去重合并
function cutarray(arr) {
    const obj = {} // obj用来记录该项重复出现了几次
    const brr:any = [] // brr是去重后的数组
    arr.forEach((item) => {
        // eslint-disable-next-line eqeqeq
        if (obj[item.userId] == undefined) {
            obj[item.userId] = 1
            brr.push(item)
        } else {
            obj[item.userId] += 1
        }
    })
    return brr
}

// 人员确定
function onconfirm(list) {
    userNameAvatarVOs.value = []
    console.log('选中项的数量列表list：', list)
    userNameAvatarVOs.value = [...userNameAvatarVOs.value, ...list]
    userNameAvatarVOs.value = cutarray(userNameAvatarVOs.value)
    // console.log('userNameAvatarVOs.value：', userNameAvatarVOs.value)
    showTreePro.value = false
}
function onconfirm2(list) {
    userNameAvatarVOs2.value = []
    console.log('选中项的数量列表list：', list)
    userNameAvatarVOs2.value = [...userNameAvatarVOs2.value, ...list]
    userNameAvatarVOs2.value = cutarray(userNameAvatarVOs2.value)
    // console.log('userNameAvatarVOs.value：', userNameAvatarVOs2.value)
    showTreePro.value = false
}
function onconfirmCell(list) {
    showTreePro.value = false
    if (list && list.length > 0) {
        model.deptName = list[0].label
        deptId.value = []
        deptId.value.push(list[0].id)
    } else {
        model.deptName = ''
        deptId.value = []
    }
}
function onconfirmProject(list) {
    showTreePro.value = false
    if (list && list.length > 0) {
        model.projectName = list[0].label
        projectId.value = []
        projectId.value.push(list[0].id)
    } else {
        model.projectName = ''
        projectId.value = []
    }

}

const userId = ref<any>([])
const userId2 = ref<any>([])
// 编辑提交的时候
function handleSubmit() {
    userId.value = []
    userId2.value = []
    if (userNameAvatarVOs.value && userNameAvatarVOs.value.length > 0) {
        // eslint-disable-next-line array-callback-return
        userNameAvatarVOs.value?.map(item => {
            userId.value.push(item.userId)
        })
        // eslint-disable-next-line array-callback-return
        userNameAvatarVOs2.value?.map(item => {
            userId2.value.push(item.userId)
        })
        form.value.validate().then(({ valid }) => {
            if (valid) {
                console.log('111111111111');

                message.confirm({
                    msg: '是否确认修改预约用车?'
                })
                    .then(async () => {
                        const data = await bookVehicle({
                            ...model,
                            startTime: model.startTime ? dayjs(model.startTime).format('YYYY-MM-DD HH:mm') : '',
                            endTime: model.endTime ? dayjs(model.endTime).format('YYYY-MM-DD HH:mm') : '',
                            deptId: deptId.value && deptId.value.length > 0 ? deptId.value.join() : '',
                            projectId: projectId.value && projectId.value.length > 0 ? projectId.value.join() : '',
                            userId: userId.value && userId.value.length > 0 ? userId.value.join() : '',
                            otherUserIds: userId2.value && userId2.value.length > 0 ? userId2.value.join() : ''
                        })
                        if (data) {
                            back(1)
                        }
                    })
                    .catch(() => {

                    })
            }
        })
            .catch((error) => {
                console.log(error, 'error')
            })
    } else {
        uni.showToast({
            icon: 'none',
            title: '请选择使用人员!'
        });
    }


}
// 取消用车
function deleteFn() {
    message.confirm({
        msg: '是否确取消改预约用车?'
    })
        .then(async () => {
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
            const data = await deleteVehicleRecord({
                id: model.id
            })
            back(1)
        })
        .catch(() => {

        })

}
</script>

<template>
    <page-meta :page-style="`overflow:${showTreePro ? 'hidden' : 'visible'};`"></page-meta>

    <view class="content">
        <view class="main">
            <wd-form ref="form" :model="model" :rules="rules">
                <wd-cell-group border>
                    <wd-input v-model="model.vehicleName" label="选择车辆" label-width="100px" prop="vehicleName"
                        placeholder="请输入用户名" readonly :rules="[{ required: true, message: '请选择车辆' }]" />
                    <wd-input v-model="model.destination" label="去往地点" label-width="100px" prop="destination" clearable
                        placeholder="请输入去往地点" :rules="[{ required: true, message: '请填写去往地点' }]" />
                    <wd-datetime-picker v-model="model.startTime" label="预计开始时间" label-width="100px"
                        placeholder="请选择预计开始时间" :formatter="formatter" align-right prop="startTime" :minDate="model.startTime"/>
                    <wd-datetime-picker v-model="model.endTime" label="预计结束时间" label-width="100px"
                        placeholder="请选择预计结束时间" :formatter="formatter" align-right prop="endTime" :minDate="model.startTime"/>
                    <wd-cell title="使用部门" is-link :value="model.deptName" prop="deptName"
                        :rules="[{ required: true, message: '请选择使用部门' }]" @click="itemCellclick(false, true)"></wd-cell>
                    <wd-cell title="关联项目" is-link :value="model.projectName"
                        @click="itemProJectclick(false, true)"></wd-cell>
                </wd-cell-group>
                <next-tree ref="nextTreeRefCell" :checkStrictly="checkStrictly" :selectParent="selectParent"
                    :multiple="multiple" :treeData="treeData" ifSearch searchModel="depHighlight"
                    @cancel="oncancel(treeData)" @confirm="onconfirmCell" @clear="clear"></next-tree>
                <next-tree ref="nextTreeRefProject" :checkStrictly="checkStrictly" :selectParent="selectParent"
                    :multiple="multiple" :treeData="treeData2" ifSearch searchModel="depHighlight"
                    @cancel="oncancel(treeData2)" @confirm="onconfirmProject" @clear="clear"></next-tree>

                <view style="margin-top: 20rpx;" class="main-box">
                    <view class="tt"><span style="color: red;margin-right: 10rpx;">*</span>使用人员</view>
                    <view style="display: flex;overflow-x: scroll;flex-shrink: 0;">
                        <div v-for="(item, index) in userNameAvatarVOs" :key="index" class="user">
                            <up-avatar :src="item.avatar" style="margin-top:10rpx ;"></up-avatar>
                            <span class="name">{{ item.name }}</span>
                            <img v-if="item.locked === '1'" src="../../static/icon/suoding.png" alt="" class="img">
                            <img v-if="item.locked !== '1'" src="../../static/icon/del.png" alt="" class="img"
                                @click="del(index)">
                        </div>
                        <view>
                            <wd-button plain :round="false" @click="itemclick(true, false)"><wd-icon
                                    name="add"></wd-icon></wd-button>
                            <next-tree ref="nextTreeRef" :checkStrictly="checkStrictly" :selectParent="selectParent"
                                :multiple="multiple" :treeData="UsersTreeData" ifSearch searchModel="depHighlight"
                                @cancel="oncancel(UsersTreeData)" @confirm="onconfirm" @clear="clear"></next-tree>
                        </view>
                    </view>
                </view>
                <view style="margin-top: 20rpx;" class="main-box">
                    <view class="tt">同行人员</view>
                    <view style="display: flex;overflow-x: scroll;flex-shrink: 0;">
                        <div v-for="(item, index) in userNameAvatarVOs2" :key="index" class="user">
                            <up-avatar :src="item.avatar" style="margin-top:10rpx ;"></up-avatar>
                            <span class="name">{{ item.name }}</span>
                            <img v-if="item.locked === '1'" src="../../static/icon/suoding.png" alt="" class="img">
                            <img v-if="item.locked !== '1'" src="../../static/icon/del.png" alt="" class="img"
                                @click="del2(index)">
                        </div>
                        <view>
                            <wd-button plain :round="false" @click="itemclick2(true, false)"><wd-icon
                                    name="add"></wd-icon></wd-button>
                            <next-tree ref="nextTreeRef2" :checkStrictly="checkStrictly" :selectParent="selectParent"
                                :multiple="multiple" :treeData="UsersTreeData2" ifSearch searchModel="depHighlight"
                                @cancel="oncancel(UsersTreeData2)" @confirm="onconfirm2" @clear="clear"></next-tree>
                        </view>
                    </view>
                </view>
                <view style="margin-top: 20rpx;" class="main-box">
                    <view class="tt"><span style="color: red;margin-right: 10rpx;">*</span>用车事由</view>
                    <wd-cell-group border>
                        <wd-textarea v-model="model.remark" auto-height prop="remark" placeholder="请输入用车事由"
                            :rules="[{ required: true, message: '请输入用车事由' }]" />
                    </wd-cell-group>
                </view>
                <view class="footer">
                    <wd-button type="info" block @click="deleteFn">取消用车</wd-button>
                    <wd-button type="primary" block @click="handleSubmit">确认修改</wd-button>
                </view>
            </wd-form>

            <wd-message-box />
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

        :deep(.wd-input__body.data-v-7397cfb5) {
            text-align: right;
        }

        :deep(.wd-calendar__value.data-v-f9f098b9) {
            text-align: right;
        }

        :deep(.wd-input__error-message.data-v-7397cfb5) {
            text-align: right;
        }

        :deep(.wd-picker__error-message.data-v-294821f1) {
            text-align: right;
        }

        :deep(.wd-cell__error-message.data-v-a65b3963) {
            text-align: right;
        }

        :deep(.wd-picker-view-column__item) {
            text-overflow: initial !important;
        }

        .main-box {
            background: #FFFFFF;
            border-radius: 8rpx;
            padding: 20rpx 30rpx;

            :deep(.wd-textarea.is-auto-height.data-v-324b46d3::after) {
                display: none;
            }

            .tt {
                display: flex;
                align-items: center;
                line-height: 1;
                font-family: Source Han Sans CN;
                font-weight: 400;
                font-size: 26rpx;
                color: #333333;
                margin-bottom: 20rpx;
            }

            :deep(.wd-button.is-medium.is-plain.data-v-aa3a6253) {
                width: 76rpx !important;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .user {
                display: flex;
                flex-direction: column;
                align-items: center;
                position: relative;
                margin-right: 40rpx;

                .img {
                    border-radius: 50%;
                    height: 30rpx;
                    width: 30rpx;
                    position: absolute;
                    top: 0;
                    right: -4rpx;
                    // z-index: 99;
                }

                .name {
                    font-family: Source Han Sans CN;
                    font-weight: 400;
                    font-size: 26rpx;
                    color: #666666;
                }
            }

            .line-block {
                display: flex;
                flex-direction: row;
                justify-content: flex-start;
                align-items: center;

                .img {
                    width: 40rpx;
                    height: 40rpx;
                    border-radius: 10rpx;
                    margin: 0 20rpx;
                }
            }
        }
    }



    .footer {
        display: flex;
        align-items: center;
        margin-top: 20rpx;

        :deep(.wd-button.is-medium.is-round.is-primary.data-v-aa3a6253) {
            background: #7EA4FD;
            border-radius: 8rpx;
            font-family: Source Han Sans CN;
            font-weight: 500;
            font-size: 30rpx;
            color: #FFFFFF;
            flex: 1;
        }

        :deep(.wd-button.is-medium.is-round.is-info.data-v-aa3a6253) {
            flex: 1;
            border-radius: 8rpx;
            font-family: Source Han Sans CN;
            font-weight: 500;
            font-size: 30rpx;
            color: #999999;
            margin-right: 20rpx;
            background: #FFFFFF;
        }
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