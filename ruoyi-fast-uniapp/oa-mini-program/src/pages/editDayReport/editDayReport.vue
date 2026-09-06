<script lang="ts" setup>
import { formateDate } from "@/utils/formatDate";
import { back } from "@/utils/router";
import { getTemplateInfoByTemplateId, getReportDetailDraftByTemplateId, getDeptListWithUsers, provisionalReport, saveReport, usersList } from "@/service/api/user";
import piaoyiEditor from '@/components/piaoyi-editor/components/piaoyi-editor/piaoyi-editor.vue'
import nextTree from '@/components/next-tree/next-tree.vue'
import env from "@/config/env";



const dayReportList = ref<any>({})
const getReportDetailZCList = ref<any>({})
const reportSubmitType = ref<any>(1)
const userNameAvatarVOs = ref<any>([])
const userNameAvatarVOs2 = ref<any>([])
const reportReceiveUserIds = ref<any>([])
const api = ref('/file/upload')
const photoUrl = ref(env.apiBaseUrl)
const formData = ref<any>({})
const formData2 = ref<any>({})
const treeData = ref<any>([])
const uuu = ref<any>([])
const timer = ref<any>() // 定时器，自动保存
const multiple = ref(false)
const selectParent = ref(false)
const nextTreeRef = ref()
const checkStrictly = ref(false)
const showTreePro = ref(false)
const params = ref<any>({})
const saveTime = ref()
const templateId = ref()
const needInput = ref<any>([])


onLoad(() => {
    getModel()
    getDeptListWithUsersFn()

})


onMounted(() => { // 组件挂载时的生命周期执行的方法
    timer.value = setInterval(() => {
        autoSave()
    }, 15000);
})

onUnload(() => { // 离开当前组件的生命周期执行的方法
    clearInterval(timer.value);
    console.log('清除定时器！！');

})


// watch( uuu.value, (newValue) => {
//     if (newValue) {
//         console.log(`我侦听到了，当前值为${ uuu.value},从而处理相关逻辑`);
//     }
// })

// 获取发送对象
async function getDeptListWithUsersFn() {
    // const data = await getDeptListWithUsers({})
    treeData.value = await new Promise((resolve) => {
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
                                // delete item.children
                            }

                        }
                    })
                }
            }
            fn(res)
            resolve(res)

        })
    })
    console.log("🚀 ~ treeData ~ treeData:", treeData.value)
}

function saveContens(index, e) {
    formData.value['reportText' + index] = e.html
}

async function getModel() {
    // const { pageQuery } = useInit();
    const instance:any = getCurrentInstance()?.proxy
    const eventChannel = instance!.getOpenerEventChannel();
    eventChannel.on('data', async (res) => {
        templateId.value = res.templateId
        uni.setNavigationBarTitle({
            title: res.templateName
        })
        const data = await getTemplateInfoByTemplateId({
            templateId: templateId.value
        })
        dayReportList.value = data
        userNameAvatarVOs.value = JSON.parse(JSON.stringify(data.userNameAvatarVOs))
        userNameAvatarVOs2.value = JSON.parse(JSON.stringify(data.userNameAvatarVOs))

        if (data.reportTemplateParagraphs && data.reportTemplateParagraphs.length > 0) {
            // eslint-disable-next-line array-callback-return
            data.reportTemplateParagraphs.map((item, index) => {
                if (item.paragraphType === '4' || item.paragraphType === '5') {
                    formData.value['reportText' + index] = []
                    formData.value['tempReportText' + index] = []
                } else {
                    formData.value['reportText' + index] = item.reportText ? JSON.parse(JSON.stringify(item.reportText)) : ''
                }
            })
        }
        getUserList()

    })



}

// 查询所有人员
async function getUserList() {
    const data = await usersList({})
    uuu.value = data
    getReportDetailZC()
}

// 回显暂存
async function getReportDetailZC() {
    const data = await getReportDetailDraftByTemplateId({
        templateId: templateId.value
    })
    getReportDetailZCList.value = data
    // 回显发送对象
    if (data.receiveUserIdArray && data.receiveUserIdArray.length > 0) {
        // eslint-disable-next-line array-callback-return
        userNameAvatarVOs.value.map(item => {
            if (data.receiveUserIdArray.includes(item.userId)) {
                // eslint-disable-next-line eqeqeq
                data.receiveUserIdArray = data.receiveUserIdArray.filter(item2 => item2 != item.userId)
                console.log(data.receiveUserIdArray)

            }
        })
        const bbb = uuu.value.filter(it => data.receiveUserIdArray.includes(it.userId))
        console.log("🚀 ~ getReportDetailZC ~ bbb:", bbb)
        userNameAvatarVOs.value = [...userNameAvatarVOs.value, ...bbb]
        userNameAvatarVOs.value = cutarray(userNameAvatarVOs.value)

        console.log(userNameAvatarVOs.value, 'uuuuuuuuuuuuuuuuuuuu')
    }
    // 回显内容
    if (data.reportDetails && data.reportDetails.length > 0) {
        // eslint-disable-next-line array-callback-return
        data.reportDetails.map((item, index) => {
            if (item.paragraphType === '4' || item.paragraphType === '5') {
                if (item.baseFiles && item.baseFiles.length > 0) {
                    item.reportText = item.baseFiles?.map(item => {
                        return {
                            name: item.name,
                            id: item.id,
                            url: item.url,
                            extname: item.format,
                        }
                    })
                    formData.value['reportText' + index] = JSON.parse(JSON.stringify(item.reportText))
                    formData.value['tempReportText' + index] = JSON.parse(JSON.stringify(item.reportText))
                } else {
                    formData.value['reportText' + index] = []
                    formData.value['tempReportText' + index] = []
                }

            } else {
                formData.value['reportText' + index] = item.reportText ? JSON.parse(JSON.stringify(item.reportText)) : ''
            }
        })
    }
}

// // 图片删除
const handleDelete = (e, index) => {
    const num = formData.value['reportText' + index].findIndex(v => v.url === e.tempFilePath);
    formData.value['reportText' + index].splice(num, 1);
    const num2 = formData.value['tempReportText' + index].findIndex(v => v.url === e.tempFilePath);
    formData.value['tempReportText' + index].splice(num2, 1);
}

// 文件图片上传
function upload(index, e) {
    console.log("🚀 ~ upload ~ e:", e)
    // let files: any = []
    // // eslint-disable-next-line array-callback-return
    // if (e.tempFiles && e.tempFiles.length > 0) {
    //     e.tempFiles.map(item => {
    //         files.push(item.file)
    //     })
    //     console.log(files, 'filesfilesfilesfilesfiles');
    // }
    const tempFilePaths = e.tempFilePaths;
    uni.uploadFile({
        url: env.apiBaseUrl + '/file/upload',
        filePath: tempFilePaths[0],
        header: {
            "satoken": uni.getStorageSync('token'),
            "Content-Type": "multipart/form-data",
        },
        name: 'file',
        success: res => {
            let uploaddata = JSON.parse(res.data)
            formData.value['reportText' + index].push({
                url: uploaddata.data.url,
                id: uploaddata.data.id,
                extname: uploaddata.data.extname,
                name: uploaddata.data.name
            })
            formData.value['tempReportText' + index].push({
                url: uploaddata.data.url,
                id: uploaddata.data.id,
                extname: uploaddata.data.extname,
                name: uploaddata.data.name
            })
            console.log("🚀 ~ formData.value:", formData.value)

        }
    })
}

// 删除发送对象
function del(index) {
    userNameAvatarVOs.value.splice(index, 1)
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

// 选择人员数据变化
function changeVerify(current, chooseList) {
    // 注意：返回非空字符串会阻止原有行为，并提示返回的字符串
    // 如果函数体不做return返回值，即验证通过，控件正常处理业务
    console.log('当前变化的数据', current)
    console.log('已选择的数据', chooseList)
}


// 默认勾选--回显
function echoDefault() {
    reportReceiveUserIds.value = []
    // eslint-disable-next-line array-callback-return
    userNameAvatarVOs.value?.map(item => {
        reportReceiveUserIds.value.push((item.userId + '_' + item.userId))
    })
    const selectIds = reportReceiveUserIds.value
    checkedTreeData(treeData.value, selectIds)
    multiple.value = true
    nextTreeRef.value.showTree = true
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
function oncancel() {
    // userNameAvatarVOs.value = JSON.parse(JSON.stringify(userNameAvatarVOs2.value))
    // 清除treeData的选中状态
    checkedTreeData(unref(treeData), [])
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
    userNameAvatarVOs.value = JSON.parse(JSON.stringify(userNameAvatarVOs2.value))
    console.log('选中项的数量列表list：', list)
    userNameAvatarVOs.value = [...userNameAvatarVOs.value, ...list]
    userNameAvatarVOs.value = cutarray(userNameAvatarVOs.value)
    console.log('userNameAvatarVOs.value：', userNameAvatarVOs.value)
    showTreePro.value = false
}

/*
 *@param {Array} target 目标数组
 *@param {Array || String} keys 删除的key
 */
// function deleteJsonKey(target , keys) {
//     if (target.length === 0) {
//         return target
//     }
//     let result = []
//     if (Array.isArray(keys)) {
//         const mixed = Object.keys(...target).filter(x => !new Set(keys).has(x))
//         result = JSON.parse(JSON.stringify(target, mixed))
//     } else {
//         result = JSON.parse(JSON.stringify(target, (k, v) => {
//             if (k !== keys) {
//                 return v
//             }
//         }))
//     }
//     return result
// }
function deleteJsonKey<T extends Record<string, any>>(target: T | T[], keys: string | string[]): T | T[] {
    if (Array.isArray(target)) {
        return target.map(obj => {
            let filteredObj: Partial<T> = {};
            for (let key in obj) {
                if (!keys.includes(key)) {
                    filteredObj[key] = obj[key];
                }
            }
            return filteredObj as T;
        });
    } else if (typeof target === 'object' && target !== null) {
        let filteredObj: Partial<T> = {};
        for (let key in target) {
            if (!keys.includes(key)) {
                filteredObj[key] = (target as Record<string, any>)[key];
            }
        }
        return filteredObj as T;
    } else {
        return target;
    }
}

// 整理参数
function make(val) {
    reportReceiveUserIds.value = []
    // eslint-disable-next-line array-callback-return
    userNameAvatarVOs.value?.map(item => {
        reportReceiveUserIds.value.push(item.userId)
    })

    formData2.value = JSON.parse(JSON.stringify(formData.value))
    const arr4:any = []
    const arr5:any = []
    if (dayReportList.value.reportTemplateParagraphs && dayReportList.value.reportTemplateParagraphs.length > 0) {
        // eslint-disable-next-line array-callback-return
        dayReportList.value.reportTemplateParagraphs.map((item, index) => {
            if (item.paragraphType === '4') {
                // eslint-disable-next-line array-callback-return
                formData2.value['tempReportText' + index]?.map(it => {
                    arr4.push(it.id)
                })
                formData2.value['reportText' + index] = arr4.join(',')
            } else if (item.paragraphType === '5') {
                // eslint-disable-next-line array-callback-return
                formData2.value['tempReportText' + index]?.map(it => {
                    arr5.push(it.id)
                })
                formData2.value['reportText' + index] = arr5.join(',')
            }
        })
        const dlarr = JSON.parse(JSON.stringify(dayReportList.value.reportTemplateParagraphs))
        const reportDetails = deleteJsonKey(dlarr, ['creatorId', 'createTime', 'isDeleted', 'reportDetailId', 'reportNumber', 'reportReceiveUserIds', 'reportRecordId', 'updaterId', 'updateTime', 'sort', 'isLatest'])
        // eslint-disable-next-line array-callback-return
        reportDetails.map((item, index) => {
            item.reportTemplateId = item.templateId
            item.reportTemplateParagraphId = item.templateParagraphId
            item.reportText = formData2.value['reportText' + index] ? formData2.value['reportText' + index] : ''
        })
        params.value = {
            reportSubmitType: reportSubmitType.value,
            reportDetails: reportDetails,
            templateId: templateId.value,
            reportReceiveUserIds: reportReceiveUserIds.value.join(','),
            isProvisional: val // 1暂存 0提交
        }
        console.log("🚀 ~ handleSubmit ~ params:", params.value)
    }
}
// 自动保存
async function autoSave() {
    console.log('自动保存！！！');
    make('1')
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const data = await provisionalReport(params.value)
    saveTime.value = formateDate(new Date(), "yyyy-MM-dd HH:mm:ss (week)");
    console.log("🚀 ~ autoSave ~ formData.value:", formData.value)
}

async function handleSubmit() {
    console.log("🚀 ~ autoSave ~ formData.value:", formData.value)
    needInput.value = []
    make('0')
    if (params.value.reportDetails && params.value.reportDetails.length > 0) {
        // eslint-disable-next-line array-callback-return
        params.value.reportDetails.map((item) => {
            if (item.isRequired === '1' && !item.reportText) {
                needInput.value.push(item)
            }
        })
    }
    if (needInput.value && needInput.value.length > 0) {
        uni.showToast({
            icon: 'none',
            title: '请输入必填项！'
        });
    } else {
        const data = await saveReport(params.value)
        if (data) {
            back(1)
        }
    }

}


// 获取上传状态
function select(index, e) {
    console.log("🚀 ~ select ~ e:", e)
    const tempFilePaths = e.tempFilePaths;
    const name = e.tempFiles[0].name
    uni.uploadFile({
        url: env.apiBaseUrl + '/file/uploadOfApplet',
        filePath: tempFilePaths[0],
        header: {
            "satoken": uni.getStorageSync('token'),
            "Content-Type": "multipart/form-data",
        },
        name: 'file',
        formData: {
            fileName: name
        },
        success: res => {
            let uploaddata = JSON.parse(res.data)
            console.log("🚀 ~ select ~ uploaddata:", uploaddata)
            formData.value['reportText' + index].push({
                url: uploaddata.data.url,
                id: uploaddata.data.id,
                extname: uploaddata.data.extname,
                name: uploaddata.data.name
            })
            formData.value['tempReportText' + index].push({
                url: uploaddata.data.url,
                id: uploaddata.data.id,
                extname: uploaddata.data.extname,
                name: uploaddata.data.name
            })
            console.log("🚀 ~ formData.value:", formData.value)
        }
    })
}
function deleteFile(index, e) {
    console.log('删除文件：', e)
    formData.value['reportText' + index].value.forEach((item, index) => {
        if (e.tempFilePath === item.path) {
            formData.value['reportText' + index].value.splice(index, 1)
        }
    })
    console.log(formData.value['reportText' + index].value);
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
        // filePath:env.apiBaseUrl +'/'+item.name,
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
function delFile(i, index) {
    formData.value['reportText' + index].splice(i, 1)
    formData.value['tempReportText' + index].splice(i, 1)
    console.log(formData.value);
}
</script>
<template>
    <page-meta :page-style="`overflow:${showTreePro ? 'hidden' : 'visible'};`"></page-meta>
    <view class="content">
        <div style="padding:10rpx 20rpx ;"><span
                style="font-size: 14px;font-family: Source Han Sans CN;font-weight: 400;color: #999999;">{{ saveTime
                }}{{ saveTime ? '自动保存成功' : '' }}</span></div>
        <view class="main">
            <view v-for="(item, index) in dayReportList.reportTemplateParagraphs" :key="index" class="box">
                <view class="tt">
                    <view v-if="item.isRequired === '1'" style="color: red;">*</view>{{ item.paragraphName }}
                </view>
                <view v-if="(item.paragraphType === '1') || (item.paragraphType === '2')" class="tt2">
                    <wd-textarea v-model="formData['reportText' + index]" auto-height />
                </view>
                <view v-if="item.paragraphType === '3'" class="tt2">
                    <piaoyi-editor :values="formData['reportText' + index]" :maxlength="3000" :api="api"
                        :photoUrl="photoUrl" name="file" @changes=" saveContens(index, $event)" />
                </view>
                <view v-if="item.paragraphType === '4'" class="tt2">
                    <uni-file-picker v-model="formData['reportText' + index]" fileMediatype="image" mode="grid"
                        :auto-upload="false" limit="4" @select="(e) => upload(index, e)"
                        @delete="(e) => handleDelete(e, index)" />
                </view>
                <!-- <view v-if="item.paragraphType === '5'" class="tt2">
                    <uni-file-picker v-model="formData['reportText' + index]" limit="5" file-mediatype="all"
                        title="最多选择5个文件" :auto-upload="false" @select="(e) => upload(index, e)"
                        @delete="(e) => handleDelete(e, index)"></uni-file-picker>
                </view> -->
                <view v-if="item.paragraphType === '5'" class="tt2">
                    <view style="margin-bottom: 20rpx;">
                        <uni-file-picker ref="files" v-model="formData['reportText' + index]"
                            :sourceType="['album', 'camera']" file-mediatype="all" :auto-upload="false"
                            @select="(e) => select(index, e)" @delete="(e) => deleteFile(index, e)">
                            <!-- <view class="clickBtn">点击选择文件</view> -->
                            <wd-button size="small">点击选择文件</wd-button>
                        </uni-file-picker>
                    </view>
                    <view v-for="(item, i) in formData['reportText' + index]" :key="i" style="margin-bottom: 20rpx;">
                        <view class="flex justify-start align-center">
                            <view style="width: 70%;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;" @click="online(item)">
                                {{ item.name }}
                            </view>
                            <view style="margin-left: 40rpx;color: #5581f8;" @click="online(item)">预览</view>
                            <view style="margin-left: 40rpx;color: #5581f8;" @click="delFile(i, index)">删除</view>
                        </view>
                    </view>
                </view>
            </view>
            <view class="choose">
                <wd-radio-group v-model="reportSubmitType" inline shape="dot">
                    <view class="tt">请选择汇报关联周期</view>
                    <wd-radio :value="1">提交本次汇报</wd-radio>
                    <wd-radio :value="0">补交上次汇报</wd-radio>
                </wd-radio-group>
            </view>
            <view class="choose">
                <view class="tt"><span style="color: red;">*</span>发送对象</view>
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
                            :multiple="multiple" :treeData="treeData" ifSearch searchModel="depHighlight"
                            :changeVerify="changeVerify" @cancel="oncancel" @confirm="onconfirm" @clear="clear"></next-tree>
                    </view>
                </view>
            </view>
            <view class="footer">
                <wd-button type="primary" size="large" block @click="handleSubmit">提交日报</wd-button>
            </view>
        </view>
    </view>

</template>
<style scoped lang="scss">
.content {
    height: 100vh;
    background: #F6F6F6;

    // padding: 20rpx;
    :deep(.wd-textarea.is-auto-height::after) {
        display: none;
    }

    :deep(.ql-container) {
        height: auto;
    }

    :deep(.file-picker__box) {
        width: 25% !important;
        padding-top: 25% !important;
    }

    .main {
        background: #F6F6F6;
        padding: 0 20rpx 20rpx 20rpx;

        .box {
            margin-bottom: 20rpx;
            background: #FFFFFF;
            padding: 20rpx;

        }

        .cu-editor {
            height: 300rpx !important;
        }

        .tt {
            font-family: Source Han Sans CN;
            font-weight: bold;
            // font-weight: 500;
            font-size: 30rpx;
            color: #333333;
            margin-bottom: 30rpx;
            display: flex;
            align-items: center;
        }

        .tt2 {
            font-family: Source Han Sans CN;
            font-weight: 400;
            font-size: 26rpx;
            color: #333333;
            margin-bottom: 30rpx;

            :deep(.uni-file-picker__lists) {
                display: none;
            }
        }

        :deep(.uni-file-picker__files button) {
            width: 50%;
            background: #5581f8;
        }
    }

    .footer {
        :deep(.wd-button.is-medium.is-round.data-v-aa3a6253) {
            width: 100%;
        }
    }

    .choose {
        margin-bottom: 20rpx;
        background: #FFFFFF;
        padding: 20rpx;

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
                z-index: 99;
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
</style>