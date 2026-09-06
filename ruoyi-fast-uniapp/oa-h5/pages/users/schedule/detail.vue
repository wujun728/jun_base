<template>
  <view class="content" v-if="loading">
    <view class="cr-position-header default-header">
      <view class="status_bar"></view>
      <view class="nav-bar">
        <default-nav-bar :index="1" background-color="rgba(0,0,0,0)" default-title="详情" :is-right="isRight"
          :right-data="rightIcon" @handleNarItem="handleNarItem"></default-nav-bar>
      </view>
      <view class="header-info plr10">
        <uni-row class="display-align">
          <uni-col :span="20" class="right-top">
            <avatar :src="data.info.master.avatar"></avatar>
          </uni-col>
          <uni-col :span="4" class="right-info">
            <view class="title line1">{{data.info.title}}</view>
            <view class="caption line1">{{getScheduleTime(data.start, data.end)}}</view>
          </uni-col>
        </uni-row>
      </view>
    </view>
    <view class="report-con m10" :class="!isRight ? 'pbc' : ''">
      <view class="report-list-item pb28">
        <view class="title">参与人</view>
        <uni-row class="display-align report-list-item-top" v-for="(item,index) in data.info.user" :key="index">
          <uni-col :span="12" class="display-align">
            <avatar :src="item.avatar" :autoSize="false" :width="70" :height="70" :radius="8"></avatar>
            <view class="name">{{item.name}}</view>
            <view class="report-list-status">
              <image class="image" :src="'/static/image/'+getStatusUrl(item.status)" mode=""></image>
            </view>
          </uni-col>
          <uni-col :span="12" class="right-info">
            {{getStatusText(item.status)}}
            <text v-if="item.updated_at">·</text>
            <uni-dateformat v-if="item.updated_at" class="" format="MM/dd hh:mm:ss" :date="item.updated_at">
            </uni-dateformat>
          </uni-col>
        </uni-row>
      </view>
      <view class="report-list-item" style="padding-top: 0;padding-bottom: 0;">
        <uni-row class="report-list-item-bottom">
          <uni-col class="bottom-left" :span="24">待办内容</uni-col>
          <uni-col :span="24" style="padding-top: 20rpx;">
            <view class="content-box" v-html="data.info.content"></view>
          </uni-col>
        </uni-row>
      </view>

      <view class="report-list-item" style="padding-top: 0;padding-bottom: 0;">
        <uni-row class="report-list-item-bottom">
          <uni-col class="bottom-left" :span="24">日程类型</uni-col>
          <uni-col :span="24" style="padding-top: 20rpx;">{{data.info.type.name}}</uni-col>
        </uni-row>
        <uni-row class="report-list-item-bottom mb68">
          <uni-col class="bottom-left" :span="24">提醒时间</uni-col>
          <uni-col :span="24" style="padding-top: 20rpx;"
            v-if="data.info.cid == 2 || data.info.cid == 3 || data.info.cid == 4 || data.info.cid == 5">
            {{ data.info.remind ? data.info.remind.remind_time : '09:00:00' }}
          </uni-col>
          <uni-col v-else :span="24" style="padding-top: 20rpx;">{{data.info.remindInfo.text}}</uni-col>
        </uni-row>

      </view>

      <!-- 评论页面 -->
      <view v-if="data.list.length" class="report-list-item pb28">
        <comment-list :list="data.list" @replyFn="replyFn" @commentDel="commentDel"></comment-list>
      </view>
    </view>

    <view class="bottom-button plr10">
      <view class="btn-box">
        <view class="replay-tips" @click="openReplay">
          <image src="/static/image/replay.png" mode="" class="img"></image>
          <text>评论</text>
        </view>
        <view v-if="!isRight">
          <button class="button" v-for="(item ,index) in bottomBtn" :key="index"
            :class="data.selectInfo&&data.selectInfo.status === item.id ? 'active' : 'no-active'"
            @click="clickButton(item)"
            :style="{ '--fill-color': item.color, '--fill-bgc': hexToRGBA(item.color, 0.1) }">{{data.info.finish === item.id  ? '已' : '' }}{{item.text}}</button>
        </view>
        <view v-else>
          <button class="button1" @click="putStatus('完成', 3)">{{data.info.finish == 3 ? '已完成' : '完成' }}</button>
        </view>
      </view>


      <!-- 评论 -->
      <view class="replay" v-if="data.replayShow">
        <uni-row class="display-align">
          <uni-col :span="16" class="replay-left">
            <textarea maxlength="255" auto-height v-model="data.content" :placeholder="data.placeholder" />
          </uni-col>
          <uni-col :span="8" class="replay-right text-right">
            <text class="iconfont icon-liuyan-fasong" :style="{color: data.content ? '#1890FF' : '#E4E7ED'}"
              @click="clickReplay"></text>
          </uni-col>
        </uni-row>
      </view>
    </view>

    <drop-down ref="dropDownRef" :list-data="forumMeus" @btn-click="dropDownItem"></drop-down>
    <select-types :title="typeTitle" :edit-data="data.editData" ref="selectType" @changeOk="changeOk"></select-types>
    <global-index></global-index>
  </view>
</template>

<script setup lang="ts">
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import globalIndex from "@/components/globalIndex/index.vue"
  import dropDown from '@/pages/forum/components/dropDown.vue'
  import selectTypes from './components/selectTypes.vue'
  import commentList from './components/commentList.vue'
  import { reactive, ref, type Ref, computed } from 'vue'
  import avatar from '@/components/avatar/index.vue'
  import { onLoad } from '@dcloudio/uni-app'
  import type { GetType, Drop, Res, Detail } from '@/utils/typeHelper'
  import { showModal, clicKReLaunch, getFindIndex, clickNavigateTo, hexToRGBA } from '@/utils/helper'
  import { encryptCode } from '@/utils/cryptoCode'
  import { getScheduleTime } from '@/utils/schedule'
  import { scheduleDetailApi, scheduleDeleteApi, scheduleRecordApi, replySaveApi, replyListApi, replyDelApi } from '@/api/user'
  import message from '@/utils/message'
  import { useStore } from 'vuex'
  const store = useStore()
  const userInfo = computed(() => store.state.app.userInfo)
  const dropDownRef = ref()
  const selectType = ref()
  const loading : Ref<boolean> = ref(false)
  const isRight : Ref<boolean> = ref(false)
  const typeTitle : Ref<string> = ref('')
  const rightIcon = reactive([
    { type: 1, icon: 'icon-gengduo1', types: 'icon' }
  ])
  const forumMeus = reactive([
    { name: '编辑', id: 1, icon: 'icon-danchuang-bianji' },
    { name: '删除', id: 2, icon: 'icon-shanchu1' },
  ])

  interface NewGet extends GetType {
    start : string
    end : string
  }

  const data = reactive({
    placeholder: '您可以发布评论哟～',
    replayShow: false,
    content: '',
    reply_id: '',
    to_uid: '',
    list: [],
    info: <Detail>{},
    id: 0,
    start: '',
    end: '',
    selectInfo: <Detail>{},
    editData: {},
    selectStatus: 2,
    dropId: 0,
    isEditShowIcon: [2, 3, 4]
  })

  // 底部按钮 {
  const bottomBtn = reactive([
    { id: 1, text: '接受', color: '#19BE6B' },
    { id: 2, text: '拒绝', color: '#ED4014' },
    { id: 0, text: '待定', color: '#FF9900' },
    { id: 3, text: '完成', color: '#308BF8' }
  ])

  onLoad((options : NewGet) => {
    data.id = Number(options.id)
    data.start = options.start
    data.end = options.end
    getScheduleDetail(data.id, {
      start_time: data.start,
      end_time: data.end
    })
    getReplyList()
  })

  const putStatus = (text, status) : void => {
    console.log(text, status)
    if (data.info.finish == 3) {
      return false
    }
    showModal(`您确认修改日程状态为已${text}`).then(() => {
      const datas = {
        status: status,
        end: data.end,
        start: data.start,
      }
      scheduleRecord(data.id, datas)
    }).catch(() => {
      console.log('取消了')
    })

  }

  // 发布评论
  const clickReplay = () : void => {
    let obj = {
      content: data.content,
      schedule_id: data.id,
      end: data.end,
      start: data.start,
      reply_id: data.reply_id,
      to_uid: data.to_uid,
    }
    if (!data.content) {
      message.error('评论内容不能为空！')
      return
    }
    replySaveApi(obj).then(res => {
      data.replayShow = false
      message.success(res.message)
      data.content = ''
      getReplyList()

    }).catch(err => {
      message.error(err.message)
    })
  }

  // 打开评论弹窗
  const openReplay = (name, type) : void => {
    if (type) {
      data.placeholder = '回复@' + name
    } else {
      data.placeholder = '您可以发布评论哟～'
    }
    data.replayShow = true
  }

  // 回复评论
  const replyFn = (obj, row) : void => {
    let name = row ? row.from_user.name : obj.from_user.name
    data.reply_id = obj.id
    if (row) {
      data.to_uid = row.from_user.id
    } else {
      data.to_uid = obj.from_user.id
    }
    openReplay(name, 1)
  }

  // 获取评论列表
  const getReplyList = () : void => {
    let obj = {
      time: data.start + ' - ' + data.end,
      schedule_id: data.id,
    }
    replyListApi(obj).then(res => {
      data.list = res.data
    })
  }

  // 删除评论
  const commentDel = (obj) : void => {
    showModal('您确定要删除此评论吗').then(res => {
      replyDelApi(obj.id).then(res => {
        message.success(res.message)
        getReplyList()
      }).catch(err => {
        message.error(err.message)
      })
    }).catch(error => {
      console.log('取消了')
    })
  }

  // 选择完成后回调
  const changeOk = (e : any) => {
    data.selectStatus = e
    if (data.dropId === 1) {
      clickEide()
    } else {
      const datas = {
        type: data.selectStatus,
        end: data.end,
        start: data.start,
      }
      scheduleDelete(data.id, datas)
    }
  }
  // 编辑
  const clickEide = () : void => {
    const str = encryptCode(`id=${data.info.id}&start=${data.start}&end=${data.end}&type=${data.selectStatus}`)
    clickNavigateTo(`/pages/users/schedule/create?id=${str}`)
  }
  // 删除
  const clickDelete = () : void => {
    showModal('确定要删除当前日程吗').then(() => {
      const datas = {
        type: data.selectStatus,
        end: data.end,
        start: data.start,
      }
      scheduleDelete(data.id, datas)
    }).catch(() => {
      console.log('取消了')
    })
  }

  // 顶部菜单
  const handleNarItem = () => {
    dropDownRef.value.openDropdown()
  }

  // 下拉框选择
  const dropDownItem = (e : Drop) : void => {
    if (e.id === 1) {
      if (data.info.period === 0) {
        clickEide()
      } else {
        typeTitle.value = '编辑重复性日程'
        selectType.value.popupOpen()
      }
    } else {
      if (data.info.period === 0) {
        clickDelete()
      } else {
        typeTitle.value = '删除重复性日程'
        selectType.value.popupOpen()
      }
    }
    data.dropId = e.id
  }

  // 获取处理状态
  enum StatusUrl {
    'schedule01.png',
    'schedule02.png',
    'schedule03.png',
    'schedule04.png',
  }
  const getStatusUrl = (status : number) : string => {
    return StatusUrl[status]
  }

  // 获取处理状态
  enum StatusText {
    '待定',
    '已接受',
    '已拒绝',
    '已完成',
  }
  const getStatusText = (status : number) : string => {
    return StatusText[status]
  }

  // 获取重复类型
  enum PeriodText {
    '不重复',
    '按天重复',
    '按周重复',
    '按月重复',
    '按年重复',
  }
  const getPeriodText = (status : number) : string => {
    return PeriodText[status]
  }

  // 底部状态设置
  const clickButton = (item : Detail) => {
    console.log(data.selectInfo, item)
    if (data.selectInfo.status === item.id) return false
    showModal(`您确认修改日程状态为${getStatusText(item.id)}`).then(() => {
      const datas = {
        status: item.id,
        end: data.end,
        start: data.start,
      }
      scheduleRecord(data.id, datas)
    }).catch(() => {
      console.log('取消了')
    })
  }

  // 获取详情
  const getScheduleDetail = (id : number, datas : object) : void => {
    scheduleDetailApi(id, datas).then((res : Res) => {
      loading.value = true
      data.info = res.data
      // 是否展示删除、编辑
      isRight.value = userInfo.value.id === data.info.master.id

      // 隐藏编辑按钮
      if (data.info.cid > 1 && data.info.cid <= 9) {
        forumMeus.splice(0, 1)
      }
      // user数据处理
      if (data.info.user.length > 0) {
        if (data.info.task.length > 0) {
          const index = getFindIndex(data.info.task, data.info.master.id, 'uid')
          data.info.user.forEach((value : any) => {
            data.info.task.forEach((val : any) => {
              if (val.uid === value.id) {
                value.status = val.status
                value.updated_at = val.updated_at
              }
            })
            // 判断列表中是否存在参与人是组织人且组织人操作
            if (value.id === data.info.master.id && index === -1) {
              value.status = 1
            }
          })
        } else {
          data.info.user.forEach((value : any) => {
            if (value.id === data.info.master.id) {
              value.status = 1
            } else {
              value.status = -1
            }
          })
        }
      }
      if (!isRight.value) {
        const index = getFindIndex(data.info.user, userInfo.value.id)
        data.selectInfo = data.info.user[index]

      }

    }).catch((error : Res) => {
      message.error(error.message)
    })
  }
  // 删除日程
  const scheduleDelete = (id : number, datas : object) : void => {
    scheduleDeleteApi(id, datas).then((res : Res) => {
      message.success(res.message)
      clicKReLaunch('/pages/users/schedule/index')
    }).catch((error : Res) => {
      message.error(error.message)
    })
  }

  // 修改日程状态
  const scheduleRecord = (id : number, datas : object) => {
    scheduleRecordApi(id, datas).then((res : Res) => {
      message.success(res.message, 'none')
      getScheduleDetail(data.id, {
        start_time: data.start,
        end_time: data.end
      })
    }).catch((error : Res) => {
      message.error(error.message)
    })
  }
</script>

<style scoped lang="scss">
  .content {
    width: 100%;
    position: relative;

    padding-bottom: 68px;

    .default-header {
      width: 100%;
      /* #ifdef APP-PLUS */
      height: 320rpx;
      /* #endif */
      /* #ifndef APP-PLUS */
      height: 240rpx;
      /* #endif */
      background: #fff;
      position: sticky;
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
        height: 240rpx;
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
        top: 0;
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
        top: calc(44px + 24rpx);
        /* #endif */
        left: 0;
        z-index: 3;

        .right-top {
          width: 90rpx;
          height: 90rpx;
        }

        .right-info {
          width: calc(100% - 90rpx);
          height: 90rpx;
          display: flex;
          justify-content: space-between;
          flex-direction: column;
          padding-left: 26rpx !important;
          color: #2B2C32;

          .title {
            font-size: 32rpx;
            font-weight: 600;
          }

          .caption {
            font-size: 24rpx;
          }
        }
      }
    }

    .report-con {
      &.pbc {
        padding-bottom: 136rpx;
      }

      .report-list-item {
        margin-bottom: 20rpx;
        border-radius: 12rpx;
        background-color: #fff;
        padding: 24rpx 30rpx;

        &:last-of-type {
          margin-bottom: 0;
        }

        &.pb28 {
          padding-bottom: 52rpx;
        }

        .title {
          font-size: 28rpx;
          color: $nui-text-color-two;
        }

        .report-list-item-top {
          margin-top: 42rpx;
          position: relative;

          .report-list-status {
            position: absolute;
            left: 55rpx;
            bottom: 0;
            width: 30rpx;
            height: 30rpx;

            .image {
              width: 100%;
              height: 100%;
            }
          }

          .name {
            padding-left: 24rpx;
            font-size: 26rpx;
            color: #41485B;
          }

          .right-info {
            text-align: right;
            font-size: 24rpx;
            color: $nui-text-color-four;
          }
        }

        .mb68 {
          margin-bottom: 146rpx;
        }

        .report-list-item-bottom {
          font-size: 28rpx;
          padding-top: 36rpx;
          color: $uni-text-color;



          &:last-of-type {
            padding-bottom: 36rpx;
          }

          .bottom-left {
            color: $nui-text-color-four;
          }
        }
      }
    }

    .bottom-button {
      width: 100%;
      position: fixed;
      left: 0;
      bottom: 0;
      min-height: 116rpx;
      background-color: #fff;
      padding: 0;

      .btn-box {
        padding: 22rpx 28rpx;
        box-shadow: 0px 0px 8px 0px rgba(215, 215, 215, 0.5);
        display: flex;
        justify-content: space-between;
      }

      .button1 {
        padding: 0 48rpx;
        height: 72rpx;
        line-height: 72rpx;
        font-size: 28rpx;
        display: inline-block;
        color: #308BF8;
        background-color: rgba(0, 0, 0, 0);
        border: 1px solid #308BF8;
      }

      .button {
        width: 130rpx;
        height: 72rpx;
        line-height: 72rpx;
        font-size: 28rpx;
        display: inline-block;

        // margin-top: 22rpx;
        margin: 0 24rpx 0 0;
        background-color: rgba(0, 0, 0, 0);
        color: var(--fill-color);

        &::after {
          border: none;
        }

        &.no-active {
          border: 1px solid var(--fill-color);
        }

        &.active {
          border: 1px solid var(--fill-color);
          // background-color: var(--fill-bgc);
        }

        &:last-of-type {
          margin-right: 0;
        }
      }
    }
  }


  .replay-tips {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .img {
      display: block;
      width: 36rpx;
      height: 34rpx;
    }

    text {
      font-size: 24rpx;
      font-family: PingFang SC-Regular, PingFang SC;
      font-weight: 400;
      color: #909399;
    }
  }

  .replay {
    width: 100%;
    display: flex;
    align-items: center;
    min-height: 108rpx;
    background-color: #fff;
    font-size: 28rpx;
    margin-top: 22rpx;

    ::v-deep .uni-row {
      width: 100%;
      padding: 0 20rpx;

      .uni-input-placeholder {
        font-size: 28rpx;
        color: #C0C4CC;
      }
    }

    .replay-left {
      width: calc(100% - 60rpx);

      uni-textarea {
        width: 100%;
      }
    }
  }

  .replay-right {
    width: 60rpx;

    .iconfont {
      color: #E4E7ED;
      font-size: 40rpx;
    }
  }

  .content-box {
    ::v-deep img {
      max-width: 80% !important;
    }
  }
</style>