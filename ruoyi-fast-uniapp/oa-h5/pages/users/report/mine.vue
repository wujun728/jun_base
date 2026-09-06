<template>
  <view class="content">
    <template v-if="!isDefault">
      <view class="cr-position-header">
        <view class="status_bar"></view>
        <default-nav-bar :is-right="true" :index="1" :default-title="navTitle" :right-data="detaultIfo.rightIcon"
          @handleNarItem="handleNarItem"></default-nav-bar>
      </view>
    </template>
    <view class="cr-position-header default-header" v-if="isDefault">

      <view class="status_bar"></view>
      <view class="nav-bar">
        <default-nav-bar :index="1" background-color="rgba(0,0,0,0)" default-title="详情"></default-nav-bar>
      </view>


      <view class="header-info plr10" v-if="config.dailyData.card">
        <uni-row class="display-align">
          <uni-col :span="20" class="right-top display-align">
            <avatar :src="config.dailyData.card.avatar" :auto-size="false" :width="90" :height="90" :radius="12">
            </avatar>
            <view class="info">
              <view class="">
                {{config.dailyData.card.name}} - {{getReportType(config.dailyData.types)}}
              </view>
              <view class="info-time">
                <uni-dateformat format="yyyy/MM/dd hh:mm" :date="config.dailyData.created_at"></uni-dateformat> 提交
              </view>
            </view>
          </uni-col>
          <uni-col :span="4" class="text-right">
            <text v-if="isEdit" class="iconfont icon-gongzuohuibao-bianji info-edit" @click="editClick"></text>
          </uni-col>
        </uni-row>
      </view>
    </view>
    <view class="report-con m10" :style="{paddingBottom: isDefault? '98rpx': 0}" :class="!isDefault? 'm-t-108' :''">
      <view class="process">
        <process :examine-data="detaultIfo.examineData" :isDefault="isDefault"></process>
      </view>
      <uni-forms :border="false" label-position="top" label-width="80px">
        <uni-forms-item>
          <template v-slot:label>
            <view class="uni-forms-item__label">
              <text class="label-item" :style="{color: id == 0 ? '#303133' : '#909399' }">{{detaultIfo.label01}}</text>
              <text v-if="!isDefault" class="is-required">*</text>
            </view>
          </template>
          <!-- #ifdef H5 -->
          <template v-if="isDefault">
            <textarea :inputBorder="false" :disabled="true" v-model="formData.finish"
              :placeholder-style="placeholderStyle" :maxlength="2000" :autoHeight="true"
              :placeholder="detaultIfo.placeholder01+'(最多2000字)'">
						</textarea>
          </template>
          <template v-if="!isDefault">
            <textarea :inputBorder="false" v-model="formData.finish" :placeholder-style="placeholderStyle"
              :maxlength="2000" :autoHeight="true" :placeholder="detaultIfo.placeholder01+'(最多2000字)'">
						</textarea>
          </template>

          <!-- #endif -->
          <!-- #ifndef H5 -->
          <uni-easyinput type="textarea" :inputBorder="false" :disabled="isDefault" v-model="formData.finish"
            :placeholder-style="placeholderStyle" :maxlength="2000" :autoHeight="true"
            :placeholder="detaultIfo.placeholder01+'(最多2000字)'">
          </uni-easyinput>

          <!-- #endif -->
        </uni-forms-item>
        <uni-forms-item v-if="type!=3">
          <template v-slot:label>
            <view class="uni-forms-item__label">
              <text class="label-item" :style="{color: id == 0 ? '#303133' : '#909399' }">{{detaultIfo.label02}}</text>
              <text v-if="!isDefault" class="is-required">*</text>
            </view>
          </template>
          <!-- #ifdef H5 -->
          <template v-if="isDefault">
            <textarea :inputBorder="false" :disabled="true" v-model="formData.plan"
              :placeholder-style="placeholderStyle" :maxlength="2000" :autoHeight="true"
              :placeholder="detaultIfo.placeholder02+'(最多2000字)'">
						</textarea>
          </template>
          <template v-if="!isDefault">
            <textarea :inputBorder="false" v-model="formData.plan" :placeholder-style="placeholderStyle"
              :maxlength="2000" :autoHeight="true" :placeholder="detaultIfo.placeholder02+'(最多2000字)'">
						</textarea>
          </template>

          <!-- #endif -->
          <!-- #ifndef H5 -->
          <uni-easyinput type="textarea" :inputBorder="false" :disabled="isDefault" v-model="formData.plan"
            :placeholder-style="placeholderStyle" :maxlength="2000" :autoHeight="true"
            :placeholder="detaultIfo.placeholder02+'(最多2000字)'">
          </uni-easyinput>
          <!-- #endif -->
        </uni-forms-item>
        <template v-if="!isDefault">
          <uni-forms-item>
            <template v-slot:label>
              <view class="uni-forms-item__label">
                <text class="label-item" :style="{color: id == 0 ? '#303133' : '#909399' }">备注</text>
                <text v-if="false" class="is-required">*</text>
              </view>
            </template>
            <!-- #ifdef H5 -->
            <textarea class="mask" :inputBorder="false" :disabled="isDefault" v-model="formData.mark"
              :placeholder-style="placeholderStyle" :maxlength="256" :autoHeight="true" placeholder="请填写备注">
						</textarea>
<!-- #endif -->
            <!-- #ifndef H5 -->
            <uni-easyinput type="textarea" class="mask" :inputBorder="false" :disabled="isDefault"
              v-model="formData.mark" :placeholder-style="placeholderStyle" :maxlength="256" :autoHeight="true"
              placeholder="请填写备注">
            </uni-easyinput>
            <!-- #endif -->
          </uni-forms-item>
        </template>
        <template v-else>
          <uni-forms-item v-if="formData.mark">
            <template v-slot:label>
              <view class="uni-forms-item__label">
                <text class="label-item" :style="{color: id == 0 ? '#303133' : '#909399' }">备注</text>
                <text v-if="false" class="is-required">*</text>
              </view>
            </template>
            <!-- #ifdef H5 -->
            <textarea class="mask" :inputBorder="false" :disabled="isDefault" v-model="formData.mark"
              :placeholder-style="placeholderStyle" :maxlength="256" :autoHeight="true" placeholder="请填写备注">
					    </textarea>
<!-- #endif -->
            <!-- #ifndef H5 -->
            <uni-easyinput type="textarea" class="mask" :inputBorder="false" :disabled="isDefault"
              v-model="formData.mark" :placeholder-style="placeholderStyle" :maxlength="256" :autoHeight="true"
              placeholder="请填写备注">
            </uni-easyinput>
            <!-- #endif -->
          </uni-forms-item>
        </template>
        <!-- 上传附件 -->
        <uni-forms-item>
          <template v-slot:label>
            <view class="uni-forms-item__label mt36 p24">
              <view class="label">
                <view>
                  附件
                </view>
                <view class="iconfont icon-biaodan-tianjia" v-if="!isDefault" @click=" uploadFlieFn()"></view>
              </view>
              <view class="tips"> (建议大小不超过{{ fileSizeOne }}M，支持图片、附件、文档) </view>
            </view>
            <view class="flie">
              <view class="item" v-for="(item, index) in detaultIfo.filesList" :key="index" @click="preview(item)">
                <div class="left-view">

                  <image class="img" v-if="!isTypeImage(item.name)"
                    :src="`/static/image/cloudfile/${isFileTypeIcon(item.name)}`" mode="widthFix">
                  </image>
                  <image class="img" v-else :src="item.src"> </image>
                </div>
                <div class="right-view over-text name">
                  {{ item.name }}
                  <view class="size"> {{ formatBytes(item.size) || '--' }} </view>
                  <view class="iconfont icon-guanbi-yangshiyi1" v-if="!isDefault" @click.stop="deleteFile(item, index)">
                  </view>
                </div>
              </view>
            </view>
          </template>
        </uni-forms-item>
      </uni-forms>
      <view class="replay-con" v-if="isDefault">
        <uni-list :border="false">
          <uni-list-item v-for="(item,index) in config.replayData" :key="item.id">
            <!-- 自定义 header -->
            <template v-slot:header>
              <view class="item-list-left">
                <avatar :src="item.card.avatar" :radius="8"></avatar>
              </view>
            </template>
            <!-- 自定义 body -->
            <template v-slot:body>
              <view class="item-list-right">
                <view class="right-top">
                  {{item.card.name}}
                  <text v-if="item.paent_user" class="pl-14 pr-14">回复</text>
                  <text v-if="item.paent_user">{{item.paent_user.card.name}}</text>
                </view>
                <view class="right-info">{{item.content}}</view>
                <view class="right-bottom">
                  <uni-dateformat format="MM/dd hh:mm" :date="item.created_at"></uni-dateformat>
                  <text class="default-color" v-if="store.state.app.uid !== item.uid"
                    @click="clickReplys(item)">回复</text>
                  <text class="default-color" v-if="store.state.app.uid === item.uid"
                    @click="handleDelete(item, index)">删除</text>
                </view>
              </view>
            </template>
          </uni-list-item>
        </uni-list>
      </view>
      <!-- 评论 -->
      <view class="replay" v-if="isDefault">
        <uni-row class="display-align">
          <uni-col :span="16" class="replay-left">
            <textarea maxlength="50" auto-height v-model="formData.content" :placeholder="detaultIfo.placeholder03" />
          </uni-col>
          <uni-col :span="8" class="replay-right text-right">
            <text class="iconfont icon-liuyan-fasong" :class="formData.content ? 'default-color' : 'replay-default'"
              @click="clickReplay"></text>
          </uni-col>
        </uni-row>
      </view>
      <view class="report-button" v-if="!isDefault && loadingButton">
        <button type="primary" :loading="loading" @click="clickSubmi">提交</button>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import { formatBytes } from '@/utils/file'
  import { reactive, ref, getCurrentInstance, onMounted, nextTick, type Ref, type ComponentInternalInstance } from 'vue'
  import {
    enterpriseDailyEditApi,
    enterpriseDailyAddApi,
    enterpriseDailyUpdateApi,
    dailyReplyDeleteApi,
    dailyReplyApi,
    enterpriseDailyCompletedApi,
    reportMemberApi
  } from '@/api/user'
  import avatar from '@/components/avatar/index.vue'
  import message from '@/utils/message'
  import { delayedReLaunch, showModal, lookPreview, fileSizeOne, isTypeImage, isFileTypeIcon } from '@/utils/helper'
  import type { Res, Detail } from '@/utils/typeHelper'
  import { useStore } from 'vuex'
  import { onLoad } from '@dcloudio/uni-app'
  import process from './components/process.vue'


  import { useBarHeight } from '@/utils/useVerifyCode'
  const { height, getBarHeight } = useBarHeight()
  const instance : ComponentInternalInstance = getCurrentInstance()

  onMounted(() => {
    getBarHeight('.cr-position-header', instance)
  })

  const store = useStore()
  const navTitle : Ref<string> = ref('填写日报')
  const type : Ref<number> = ref(0)
  const placeholderStyle : Ref<string> = ref('#C0C4CC')
  const detaultIfo = reactive({
    label01: '',
    placeholder01: '',
    label02: '',
    placeholder02: '',
    placeholder03: '发表评论，表扬一下吧～',
    placeholder04: '请选择汇报人',
    pid: 0,
    examineData: [],
    rightIcon: [
      { type: 1, icon: 'icon-daiban', types: 'icon' }
    ],
    filesList: [],
    imageList: [],
    members: []
  })
  const isEdit : Ref<boolean> = ref(true)
  // 是否编辑
  const isDefault : Ref<boolean> = ref(false)
  const isAdd : Ref<boolean> = ref(true)
  // 防止提交按钮闪动
  const loadingButton : Ref<boolean> = ref(false)
  const loading : Ref<boolean> = ref(false)
  const id : Ref<number> = ref(0)
  onLoad(options => {
    type.value = options.type ? options.type : 0
    if (options.id) {
      id.value = options.id
      getDailyInfo(id.value)
      isDefault.value = true
    } else {
      isEdit.value = true
      isDefault.value = false
      loadingButton.value = true
      id.value = 0
      getDailyCompleted()
      reportMember()
    }
    getDetaultIfo()
  })
  const handleNarItem = (e) => {
    delayedReLaunch('/pages/users/schedule/index')
    // uni.switchtab({
    //   url: '/pages/users/schedule/index'
    // })
  }

  const formData = reactive({
    finish: '',
    plan: '',
    mark: '',
    content: '',
    types: 0,
    members: []
  })

  const config = reactive({
    dailyData: <any>{},
    replayData: []
  })

  const getReportType = (type : number) : string => {
    let str = ''

    if (type === 1) {
      str = '周报'
    } else if (type === 2) {
      str = '月报'
    } else if (type === 3) {
      str = '汇报'
    } else {
      str = '日报'
    }
    return str
  }
  // 图片与文档预览
  const preview = (item) => {
    lookPreview(item.src, item.name, [item.src])
  }
  // 上传附件
  import { uploadFlie } from '@/utils/file'
  const uploadFlieFn = () => {
    const datas = {
      relation_id: 0,
      relation_type: 'daily',
    }
    uploadFlie('attach/imgs', datas, fileSizeOne)
      .then((res) => {
        if (res.status == 200) {
          message.success(res.message)
          let newData = {
            url: res.data.src,
            src: res.data.src,
            id: res.data.id,
            size: res.data.size,
            name: res.data.name,
          }
          detaultIfo.filesList.push(newData)
          detaultIfo.filesList.map((item) => {
            if (isTypeImage(item.name)) {
              detaultIfo.imageList.push(item.src)
            }
          })

        }
      })
      .catch((error) => {
        message.error(error)
      })
  }


  // 附件删除
  const deleteFile = (item, index) => {
    detaultIfo.filesList.splice(index, 1)
  }
  // 获取默认汇报人
  const reportMember = () => {
    reportMemberApi().then((res : Res) => {
      detaultIfo.examineData = res.data
      const user = {
        card: res.data[0][0].card,
        name: res.data[0][0].card.name,
        id: res.data[0][0].card.id
      }

      store.commit('setDepSelectPeople', [user])
      store.commit('setDepSelectIds', [res.data[0][0].card.id])
    })
  }
  //获取详情
  const getDailyInfo = (id : number) : void => {
    enterpriseDailyEditApi(id).then((res : Res) => {
      const data = res.data
      config.dailyData = data
      config.replayData = data.replys
      detaultIfo.filesList = data.attachs
      detaultIfo.examineData.push(data.members)
      // 判断是否可编辑
      loadingButton.value = true
      // 苹果手机必须这样写，必须用'/'的格式。
      const endTime = data.end_time.replace(/-/g, '/')
      if (store.state.app.uid === data.uid && (new Date(endTime).getTime() > new Date().getTime())) {
        isEdit.value = true
      } else {
        isEdit.value = false
      }
      formData.finish = data.finish.map(item => item + '\n').join('')
      formData.plan = data.plan.map(item => item + '\n').join('')
      formData.mark = data.mark
    }).catch((error : Res) => {
      uni.hideLoading()
      message.error(error.message)
    })
  }

  const editClick = async () => {
    isDefault.value = false
    isAdd.value = false
    await nextTick()
    getBarHeight('.cr-position-header', instance)
  }

  const getDetaultIfo = () => {
    if (type.value == 1) {
      if (id.value > 0) {
        navTitle.value = '编辑周报'
      } else {
        navTitle.value = '填写周报'
      }
      detaultIfo.label01 = '本周工作'
      detaultIfo.label02 = '下周计划'
      detaultIfo.placeholder01 = '请填写本周工作'
      detaultIfo.placeholder02 = '请填写下周计划'
    } else if (type.value == 2) {
      if (id.value > 0) {
        navTitle.value = '编辑月报'
      } else {
        navTitle.value = '填写月报'
      }
      detaultIfo.label01 = '本月工作'
      detaultIfo.label02 = '下月计划'
      detaultIfo.placeholder01 = '请填写本月工作'
      detaultIfo.placeholder02 = '请填写下月计划'
    } else if (type.value == 3) {
      if (id.value > 0) { navTitle.value = '编辑汇报' } else {
        navTitle.value = '填写汇报'
      }
      detaultIfo.label01 = '填写汇报'
    } else {
      if (id.value > 0) {
        navTitle.value = '编辑日报'
      } else {
        navTitle.value = '填写日报'
      }
      detaultIfo.label01 = '今日工作'
      detaultIfo.label02 = '下个工作日计划'
      detaultIfo.placeholder01 = '请填写今日工作'
      detaultIfo.placeholder02 = '请填写下个工作日计划'
    }
  }

  // 删除评论
  const handleDelete = (item : Detail, index : number) => {
    showModal('确认要删除这条评论吗').then(() => {
      dailyReplyDeleteApi(item.id, config.dailyData.daily_id).then((res : Res) => {
        if (res.status === 200) {
          config.replayData.splice(index, 1)
        }
      }).catch((error : Res) => {
        message.error(error.message)
      })
    }).catch(() => {

    })
  }

  // 提交评论
  const clickReplay = () : boolean => {

    if (!formData.content) {
      message.error('评论内容不能为空')
      return false
    }
    const data = {
      content: formData.content,
      pid: detaultIfo.pid,
      daily_id: config.dailyData.daily_id
    }
    detaultIfo.examineData = []
    addDailyReply(data)
  }

  // 回复评论
  const clickReplys = (item : Detail) : void => {
    detaultIfo.placeholder03 = '回复' + item.card.name
    detaultIfo.pid = item.id
  }

  const clickSubmi = () => {
    detaultIfo.examineData[0].map(item => {
      let id = item.id ? item.id : item.card.id
      detaultIfo.members.push(id)
    })

    formData.members = [...new Set(detaultIfo.members)]

    if (!formData.members.length) {
      message.error(detaultIfo.placeholder04)
      return false
    }

    formData.types = type.value;
    if (formData.finish && typeof formData.finish === 'string') {
      formData.finish = formData.finish.split('\n').filter(item => item.trim() !== "");
    }

    if (formData.plan && typeof formData.plan === 'string') {
      formData.plan = formData.plan.split('\n').filter(item => item.trim() !== "");
    }
    if (formData.finish.length == 0) {
      message.error('填写汇报不能为空')
      return false
    }

    if (formData.plan.length == 0 && type.value != 3) {
      message.error('工作计划不能为空')
      return false
    }
    let ids = []
    if (detaultIfo.filesList.length > 0) {
      detaultIfo.filesList.map(item => {
        ids.push(item.id)
      })
    }
    formData.attach_ids = ids.join(',')

    if (isAdd.value) {
      if (!loading.value) {
        addDaily(formData)
      }
    } else {
      if (!loading.value) {
        editDaily(config.dailyData.daily_id, formData)
      }
    }
  }

  // 添加接口
  const addDaily = (data : object) : void => {
    loading.value = true
    enterpriseDailyAddApi(data).then((res : Res) => {
      // loading.value = true
      message.success(res.message, 'none')
      delayedReLaunch('/pages/users/report/check')
    }).catch((error : Res) => {
      loading.value = false
      message.error(error.message)
    })
  }

  // 编辑汇报
  const editDaily = (id : number, data : object) : void => {
    loading.value = true
    enterpriseDailyUpdateApi(id, data).then((res : Res) => {
      // loading.value = false
      message.success(res.message, 'none')
      delayedReLaunch('/pages/users/report/check')
    }).catch((error : Res) => {
      loading.value = false
      message.error(error.message)
    })
  }

  // 评论
  const addDailyReply = (data : object) => {
    dailyReplyApi(data).then((res : Res) => {
      getDailyInfo(config.dailyData.daily_id)
      message.error(res.message)
      detaultIfo.placeholder03 = '发表评论，表扬一下吧～'
      detaultIfo.pid = 0
      formData.content = ''
    }).catch((error : Res) => {
      message.error(error.message)
      getDailyInfo(config.dailyData.daily_id)
      detaultIfo.placeholder03 = '发表评论，表扬一下吧～'
      detaultIfo.pid = 0
      formData.content = ''
    })
  }

  // 获取当日已完成日程
  const getDailyCompleted = () => {
    enterpriseDailyCompletedApi(type.value).then((res : Res) => {
      const result = res.data
      if (result.finish) {
        formData.finish = result.finish.map(item => item + '\n').join('')
        formData.plan = result.plan.map(item => item + '\n').join('')
      }
    }).catch((error : Res) => {
      message.error(error.message)
    })
  }
</script>

<style scoped lang="scss">
  .content {
    width: 100%;
    position: relative;

    .label {
      padding-right: 24rpx;
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16rpx;

      .icon-biaodan-tianjia {
        color: #c0c4cc !important;
        font-size: 34rpx;
      }
    }

    .tips {
      font-size: 20rpx;
      font-family: PingFang SC-常规体, PingFang SC;
      font-weight: 400;
      color: #999999;
      margin-bottom: 20rpx;
    }

    .uni-forms-item__label {
      height: auto;
      padding: 0;
      line-height: 1;
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 30rpx;
      color: #303133;

      .iconfont {
        margin-top: 8rpx;
        margin-left: 5rpx;
        color: #ff2529;
      }
    }


    .default-header {
      width: 100%;
      /* #ifdef APP-PLUS */
      height: 320rpx;
      /* #endif */
      /* #ifndef APP-PLUS */
      height: 280rpx;
      /* #endif */
      background: #fff;
      position: fixed;
      top: 0;
      left: 0;
      z-index: 4;

      &::after {
        content: '';
        width: 100%;
        /* #ifdef APP-PLUS */
        height: 320rpx;
        /* #endif */
        /* #ifndef APP-PLUS */
        height: 280rpx;
        /* #endif */
        left: 0;
        top: 0;
        position: absolute;
        background: linear-gradient(0, rgba(175, 233, 253, 0.08), rgba(43, 131, 234, 0.3));
      }

      .nav-bar {
        width: 100%;
        position: absolute;
        /* #ifdef APP-PLUS */
        top: 44px;
        /* #endif */
        /* #ifndef APP-PLUS */
        top: 10px;
        /* #endif */
        left: 0;
        z-index: 3;
      }

      .header-info {
        width: 100%;
        position: absolute;
        /* #ifdef APP-PLUS */
        top: calc(88px + 24rpx);
        /* #endif */
        /* #ifndef APP-PLUS */
        top: 80px;
        /* #endif */

        left: 0;
        z-index: 3;

        .right-top {
          .info {
            padding-left: 20rpx;
            font-size: 32rpx;
            color: #2B2C32;
            font-weight: 600;

            .info-time {
              padding-top: 10rpx;
              font-weight: normal;
              font-size: 24rpx;
              color: $nui-text-color-four;
            }
          }
        }

        .info-edit {
          font-size: 34rpx;
          color: $nui-text-color-four;
        }

      }
    }

    .m-t-108 {
      //#ifdef APP-PLUS
      margin-top: 28rpx;
      //#endif
      //#ifdef H5
      margin-top: 28rpx;
      //#endif

    }

    .report-con {
      /* #ifdef APP-PLUS */
      padding-top: v-bind(height);
      /* #endif */

      /* #ifndef APP-PLUS */
      padding-top: v-bind(height);
      /* #endif */

      ::v-deep .uni-forms-item {
        background-color: #fff;
        border-radius: 12rpx;
        padding: 36rpx 24rpx 18rpx 24rpx;
        margin-bottom: 20rpx;

        .uni-forms-item__label {
          height: auto;
          padding: 0;
          font-size: $uni-font-size-default;
          line-height: 1;

          .is-required {
            color: #dd524d;
            font-weight: bold;
          }
        }

        .is-disabled {
          color: $uni-text-color;
          background-color: rgba(0, 0, 0, 0) !important;
        }

        uni-textarea {
          width: 100%;
          padding-top: 22rpx;
          min-height: 460rpx;
          margin: 0;
          ;
        }

        .mask {
          min-height: 216rpx;
        }

        .uni-textarea-placeholder {
          font-size: $uni-font-size-default;
          color: $uni-text-color-five;
        }

        .uni-textarea-textarea {
          font-size: $uni-font-size-default;
          color: $uni-text-color;
        }
      }
    }

    .replay-con {
      ::v-deep .uni-list {
        background: $uni-default-bg;

        .uni-list-item {
          margin-bottom: 16rpx;
          border-radius: 16rpx;

          .uni-list-item__container {
            padding: 28rpx 24rpx;
          }

          &:last-of-type {
            margin-bottom: 0;
          }
        }

      }

      .item-list-left {
        width: 60rpx;
        height: 60rpx;
      }

      .item-list-right {
        width: calc(100% - 60rpx);
        padding-left: 14rpx;

        .right-top,
        .right-bottom {
          font-size: 24rpx;
          color: #687383;
        }

        .right-info {
          padding-top: 14rpx;
          font-size: 26rpx;
          line-height: 1.5;
          word-wrap: break-word;
          color: #41485B;
        }

        .right-bottom {
          padding-top: 14rpx;

          uni-text {
            padding-right: 14rpx;

            &:last-of-type {
              padding-right: 0;
            }
          }
        }
      }
    }

    .replay {
      box-shadow: 0px 0px 6px 0px rgba(0, 0, 0, 0.06);
      width: 100%;
      position: fixed;
      left: 0;
      bottom: 0;
      display: flex;
      align-items: center;
      min-height: 98rpx;
      background-color: #fff;
      font-size: 28rpx;

      ::v-deep .uni-row {
        width: 100%;
        padding: 0 20rpx;

        .uni-input-placeholder {
          color: #C0C4CC;
        }
      }

      .replay-left {
        width: calc(100% - 60rpx);

        uni-textarea {
          width: 100%;
        }
      }

      .replay-right {
        width: 60rpx;

        .replay-default {
          color: #E4E7ED;
        }

        .iconfont {
          font-size: 40rpx;
        }
      }
    }

    .report-button {
      margin-top: 34rpx;

      uni-button {
        font-size: 30rpx;
        line-height: 80rpx;
        height: 80rpx;
      }
    }
  }

  ::v-deep .uni-easyinput__content-textarea {
    font-size: 30rpx;
    min-height: 460rpx;
    margin-bottom: 20rpx;
  }

  .process {
    background-color: #fff;
    border-radius: 12rpx;
    margin-bottom: 20rpx;
  }

  // 上传附件
  .flie {
    padding: 24rpx 24rpx 24rpx 0;

    .item {
      width: 100%;
      height: 42px;
      padding: 7px;
      background: #f6f7f9;
      border-radius: 4px 4px 4px 4px;
      display: flex;
      position: relative;
      margin-bottom: 20rpx;

      .icon-guanbi-yangshiyi1 {
        position: absolute;
        right: 7px;
        top: 7px;
        color: #909399;
      }

      .left-view {
        width: 26px;
        min-width: 26px;
        height: 100%;

        .img {
          display: block;
          margin: 0;
          width: 26px;
          height: 26px;
          border-radius: 2px 2px 2px 2px;
        }
      }

      .right-view {
        width: 80%;
        height: 100%;
        margin-left: 5px;

        font-family: PingFang SC, PingFang SC;
        font-weight: 400;
        font-size: 12px;
        color: #303133;

        .size {
          font-family: PingFang SC, PingFang SC;
          font-weight: 400;
          font-size: 10px;
          color: #909399;
        }
      }
    }
  }
</style>