<template>
  <view class="content" v-if="data.listData">
    <view class="cr-position-header" @click="changeInput">
      <!-- #ifndef APP-PLUS -->
      <image v-if="data.listData.status === 0" class="bg-img" src="/static/image/bg-examine-h5.png" mode=""></image>
      <image v-if="data.listData.status === 1" class="bg-img" src="/static/image/bg-passed-h5.png" mode=""></image>
      <image v-if="data.listData.status === 2" class="bg-img" src="/static/image/bg-refuse-h5.png" mode=""></image>
      <image v-if="data.listData.status === -1" class="bg-img" src="/static/image/bg-revoke-h5.png" mode=""></image>
      <!-- #endif -->

      <!-- #ifdef APP-PLUS -->
      <image v-if="data.listData.status === 0" class="bg-img" src="/static/image/bg-examine.png" mode=""></image>
      <image v-if="data.listData.status === 1" class="bg-img" src="/static/image/bg-passed.png" mode=""></image>
      <image v-if="data.listData.status === 2" class="bg-img" src="/static/image/bg-refuse.png" mode=""></image>
      <image v-if="data.listData.status === -1" class="bg-img" src="/static/image/bg-revoke.png" mode=""></image>
      <view class="position nav-bar">
        <default-nav-bar background-color="rgba(0,0,0,0)"></default-nav-bar>
      </view>
      <!-- #endif -->
      <view class="position examine-info plr10">
        <uni-row class="examine-info-row">

          <uni-col :span="6" class="examine-info-left">
            <avatar :src="data.listData.card ? data.listData.card.avatar : ''" :radius="12"></avatar>
          </uni-col>
          <uni-col :span="18" class="examine-info-right">
            <view class="flex">
              <view class="name">
                {{data.listData.card ? data.listData.card.name : '--'}}的{{data.listData.approve ? data.listData.approve.name : '--'}}
              </view>

              <view class="urge" v-if="data.listData.status === 0 && data.listData.verify_status !== 0" @click="urgeFn">
                <text class="iconfont icon-cuiban"></text> 催办
              </view>
            </view>

            <view v-if="data.listData.status === 0">
              <uni-tag class="examine-tag" text="审批中" type="warning" />
            </view>
          </uni-col>
        </uni-row>
      </view>
      <!-- <image v-if="data.listData.status === 0" class="item-list-status" src="/static/image/examine.png" mode=""></image> -->
      <image v-if="data.listData.status === 1" class="item-list-status" src="/static/image/passed.png" mode=""></image>
      <image v-if="data.listData.status === 2" class="item-list-status" src="/static/image/refuse.png" mode=""></image>
      <image v-if="data.listData.status === -1" class="item-list-status" src="/static/image/revoke.png" mode=""></image>
    </view>

    <view class="examine-content m10" @click="changeInput">

      <view class="steps-content mb10">
        <check-examine-from :options="data.listData.content"></check-examine-from>
        <view v-if="data.listData.apply_id" class="revoke" @click="revokeFn(data.listData.apply_id)"> 查看需要撤销的申请单 <text
            class="iconfont icon-jinru-copy"></text>
        </view>

      </view>

      <view class=" steps-content" v-if="data.stepsData&&data.stepsData.length > 0">
        <check-steps :options="data.stepsData" active-color="#007AFF"></check-steps>
      </view>

      <view class="replay-con">
        <uni-list :border="false">
          <uni-list-item v-for="(item,index) in data.replayData" :key="item.id">
            <!-- 自定义 header -->
            <template v-slot:header>
              <view class="item-list-left">
                <avatar :src="item.card.avatar" :radius="8"></avatar>
              </view>
            </template>
            <!-- 自定义 body -->
            <template v-slot:body>
              <view class="item-list-right">
                <uni-row class="right-top">
                  <uni-col :span="12">{{item.card.name}}</uni-col>
                  <uni-col :span="12" class="text-right">
                    <uni-dateformat format="MM/dd hh:mm" :date="item.created_at"></uni-dateformat>
                  </uni-col>
                </uni-row>
                <uni-row class="right-info">

                  <uni-col :span="20" :class="userInfo.uid !== item.card.uid ? 'width100' : ''">{{item.content}}
                  </uni-col>
                  <uni-col :span="4" class="text-right" v-if="userInfo.uid === item.card.uid">
                    <text class="iconfont icon-shanchu right-info-text" @click="handleDelete(item, index)"></text>
                  </uni-col>
                </uni-row>
              </view>
            </template>
          </uni-list-item>
        </uni-list>
      </view>
    </view>

    <view class="examine-bottom" v-if="data.listData.verify_status !== 1 && data.listData.status !== -1">
      <uni-row>
        <uni-col :span="7" class="examine-bottom-left">
          <view class="examine-bottom-reply" @click="examineReply">
            <image class="reply-image" src="/static/image/cloudfile/leave-icon.png" mode=""></image>
            <view class="name">留言</view>
          </view>
        </uni-col>
        <uni-col :span="17" class="examine-bottom-right">
          <view class="display-align">
            <template v-if="data.typeIndex!=-1">
              <view class="sign" v-if="data.examineData.verify_status === 0 && data.examineData.approve.types !== 11">
                <dean-popover ref=" deanPopoverRef" model-direction="right" :btnList="data.btnList" @select="selectFn">
                  <template #icon>
                    <text class="iconfont icon-shenpigengduotubiao"></text>
                  </template>
                </dean-popover>
              </view>
              <button v-if="data.listData.status !== 1 && data.listData.verify_status === 0" type="warn" plain="true"
                @click="handleRefuse()"><text class="iconfont icon-shenpizhongxin-jujue"></text>拒绝</button>
              <button v-if="data.listData.status !== 1 && data.listData.verify_status === 0" type="primary" plain="true"
                @click="handleAgree()"><text class="iconfont icon-shenpizhongxin-tongyi"></text>同意</button>

            </template>

            <template v-if="userInfo.id == data.listData.card.id ">
              <button v-if="
                              ((data.listData.status == 1 && data.listData.rules.recall == 1) ||
                                (data.listData.status == 0 )) &&
                              !data.listData.recall
                            " type="warn" plain="true" @click="handleRevoke()">撤销</button>
            </template>

          </view>
        </uni-col>
      </uni-row>
    </view>

    <!-- 评论 -->
    <view class="replay"
      v-if="data.examineReplyBtn || data.listData.verify_status === 1 || data.listData.status === -1">
      <uni-row class="display-align">
        <uni-col :span="16" class="replay-left">
          <textarea maxlength="50" auto-height :focus="data.examineReplyBtn" @confirm="changeInput"
            v-model="data.content" placeholder="您可以发布留言哟～" />
          <!-- <input type="text" ref="inputRef" :focus="data.examineReplyBtn" @confirm="changeInput" v-model="data.content" placeholder="您可以发布留言哟～" /> -->
        </uni-col>
        <uni-col :span="8" class="replay-right text-right">
          <text class="iconfont icon-liuyan-fasong" :style="{color: data.content ? '#1890FF' : '#E4E7ED'}"
            @click="clickReplay"></text>
        </uni-col>
      </uni-row>
    </view>
    <global-index></global-index>
  </view>
</template>

<script setup>
  import { reactive, computed } from "vue"
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import avatar from '@/components/avatar/index.vue'
  import examineForm from '@/components/examineForm/index.vue'
  import checkSteps from './components/checkProcess.vue'
  import checkExamineFrom from './components/checkExamineFrom.vue'
  import globalIndex from '@/components/globalIndex/index.vue'
  import deanPopover from '@/components/deanPopover/index.vue'
  import message from "@/utils/message"
  import {
    clickNavigateTo,
    delayedReLaunch,
    clicKReLaunch,
    debounce,
  } from '@/utils/helper'

  import { useStore } from 'vuex'
  const store = useStore();
  const userInfo = computed(() => store.state.app.userInfo)
  const data = reactive({
    id: 0,
    listData: {},
    stepsData: [],
    examineUser: {},
    examineData: {},
    replayData: [],
    btnList: [{
        icon: 'iconfont icon-jiaqiantubiao1',
        type: 1,
        name: '加签',
      },
      {
        icon: 'iconfont icon-zhuanshentubiao1',
        type: 2,
        name: '转审',
      },
    ],
    content: '',
    examineReplyBtn: false
  })

  import { onLoad } from '@dcloudio/uni-app'
  onLoad((options) => {
    if (options.id) {
      data.id = options.id
      getApproveApply(options.id, { types: 1 })
    }
    if (options.typeIndex) {
      data.typeIndex = options.typeIndex
    }
  })

  import { showModal, delayedNavigateTo } from '@/utils/helper'

  // 同意
  const handleAgree = () => {
    showModal('确定要 同意 申请人的申请吗').then(() => {
      getApproveVerify(data.id, 1)
    }).catch(() => {
      console.log('取消')
    })
  }

  const revokeFn = (id) => {
    clickNavigateTo(`/pages/users/examine/defaults?id=${id}`)
  }

  // 撤销
  const handleRevoke = () => {
    if (data.listData.status === 0) {
      showModal('确定要 撤销 申请吗').then(() => {
        getApplyRevoke(data.id)
      }).catch(() => {
        console.log('取消')
      })
    } else {
      clickNavigateTo(`/pages/users/examine/components/addSignature?id=${data.id}&type=3`)
    }

  }

  const selectFn = (e) => {
    // 加签or转审
    if (e.type == 1) {
      clickNavigateTo(`/pages/users/examine/components/addSignature?id=${data.examineData.id}&type=1`)
    } else {
      clickNavigateTo(`/pages/users/examine/components/addSignature?id=${data.examineData.id}&type=2`)

    }
  }

  // 拒绝
  const handleRefuse = () => {
    showModal('确定要 拒绝 申请人的申请吗').then(() => {
      getApproveVerify(data.id, 0)
    }).catch((error) => {
      console.log('取消')
    })
  }

  // 催办
  const urgeFn = () => {
    approveUrgeApi(data.id).then(res => {
      message.success(res.message)
    }).catch(err => {
      message.error(err.message)

    })
  }

  //申请审批处理
  const getApproveVerify = (id, status) => {
    approveVerifyStatusApi(id, status).then((res) => {
      message.success(res.message)
      delayedNavigateTo('/pages/users/examine/approve?id=1')
    }).catch((error) => {
      message.error(error.message)
    })
  }

  // 点击留言
  const examineReply = () => {
    data.examineReplyBtn = true
  }

  const changeInput = () => {
    data.examineReplyBtn = false
  }

  // 提交留言
  const clickReplay = () => {
    if (!data.content) {
      return false
    }
    data.examineReplyBtn = false
    const datas = {
      content: data.content,
      apply_id: data.listData.id
    }
    getApproveReply(datas)
  }

  // 删除留言
  const handleDelete = (item, index) => {
    showModal('确定要删除留言吗').then(() => {
      approveReplyDelete(item.id, index)
    }).catch(() => {
      console.log('取消')
    })
  }

  import {
    approveApplyEditApi,
    approveVerifyStatusApi,
    approveReplyApi,
    approveReplyDelApi,
    approveApplyRevokeApi,
    approveUrgeApi
  } from '@/api/business'

  //申请审批撤销
  const getApplyRevoke = (id) => {
    approveApplyRevokeApi(id).then((res) => {
      message.success(res.message)
      delayedNavigateTo('/pages/users/examine/center')
    }).catch((error) => {
      message.error(error.message);
    })
  }

  const getApproveApply = (id, datas, flage = false) => {
    if (!flage) {
      uni.showLoading({
        title: '加载中'
      })
    }
    approveApplyEditApi(id, datas).then((res) => {
      if (!flage) {
        uni.hideLoading();
      }
      data.examineData = res.data
      if (data.examineData.rules && data.examineData.rules.is_sign && data.examineData.rules.is_sign !== 1) {
        data.btnList.splice(0, 1)
      }
      if (data.examineData.verify_status !== 0) {
        data.btnList.splice(1, 1)
      }
      data.stepsData = res.data.users
      data.replayData = res.data.reply

      data.listData = res.data
      // getUnderApproval( res.data.users )
    }).catch((error) => {
      if (!flage) {
        uni.hideLoading();
      }
      message.error(error.message)
    })
  }

  // 发表留言
  const getApproveReply = (datas) => {
    approveReplyApi(datas).then((res) => {
      data.content = ''
      message.success(res.message)
      getApproveApply(data.id, { types: 1 }, true)
    }).catch((error) => {
      message.error(error.message)
    })
  }

  // 删除留言
  const approveReplyDelete = (id, index) => {
    approveReplyDelApi(id, index).then((res) => {
      message.success(res.message)
      data.replayData.splice(index, 1)
    }).catch((error) => {
      message.error(error.message)
    })
  }

  // 审批中信息判断
  const getUnderApproval = (datas) => {
    let users = []
    if (datas.length > 0) {
      datas.forEach(value => {
        if (value.types === 1) {
          users.push(...value.users)
        }
      })
    }
    if (users.length > 0) {
      for (let i = 0; i < users.length; i++) {
        if (users[i].status === 0) {
          data.examineUser = users[i]
          break
        }
      }
    }
  }
</script>

<style scoped lang="scss">
  .sign {
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 30rpx;
    color: #606266;
    margin-right: 30rpx;

    .icon-shenpigengduotubiao {
      margin-right: 7rpx;
    }
  }

  .revoke {
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 26rpx;
    color: #1890ff;
    margin-left: 20rpx;
    padding-bottom: 20rpx;

    .icon-jinru-copy {
      color: #c0c4cc !important;
      font-size: 12px;

    }
  }

  .content {
    width: 100%;
    position: relative;


    .cr-position-header {
      width: 100%;

      .bg-img {
        width: 100%;
        // #ifndef APP-PLUS
        height: 170rpx;
        // #endif
        // #ifdef APP-PLUS
        height: 320rpx;
        // #endif
      }

      .position {
        position: absolute;
        left: 0;
        width: 100%;
      }

      .nav-bar {
        top: 88rpx;
      }



      .examine-info {

        // #ifndef APP-PLUS
        top: 36rpx;
        // #endif
        // #ifdef APP-PLUS
        top: calc(88rpx + $uni-default-bar-height + 26rpx);

        // #endif
        .examine-info-row {
          display: flex;
          flex: 1;

          .examine-info-left {
            width: 90rpx;
            height: 90rpx;
          }

          .examine-info-right {
            width: calc(100% - 90rpx);
            padding-left: 26rpx !important;
            color: #2B2C32;
            display: flex;
            flex-direction: column;
            justify-content: space-around;

            .flex {
              display: flex;
              justify-content: space-between;
              align-items: center;

              .urge {
                font-size: 30rpx;
                font-family: PingFang SC-Regular, PingFang SC;
                font-weight: 400;
                color: #1890FF;
              }
            }

            .name {
              font-size: 32rpx;
              font-weight: $uni-default-font-weight;
            }

            .examine-tag {
              font-size: 24rpx;
              padding: 0 2rpx;
              background-color: #FF8E32;
            }
          }
        }
      }



      .item-list-status {
        position: absolute;
        right: 0;
        bottom: 12rpx;
        // #ifndef APP-PLUS
        width: 140rpx;
        height: 164rpx;
        // #endif
        // #ifdef APP-PLUS
        width: 160rpx;
        height: 188rpx;
        // #endif
      }
    }

    .examine-content {
      // #ifndef APP-PLUS
      padding-top: 170rpx;
      // #endif
      // #ifdef APP-PLUS
      padding-top: 320rpx;
      // #endif
      padding-bottom: 108rpx;

      .steps-content {
        width: 100%;
        border-radius: 16rpx;
        background-color: #fff;
      }
    }

    .examine-bottom {
      position: fixed;
      left: 0;
      bottom: 0;
      height: 108rpx;
      width: 100%;
      box-shadow: 0px 0px 8px 0px rgba(215, 215, 215, 0.5);
      background-color: #fff;
      padding: 0 30rpx 0 42rpx;


      .examine-bottom-left {
        height: 100%;
        height: 108rpx;
        display: flex;
        align-items: center;
      }

      .examine-bottom-reply {
        line-height: 1;
        text-align: center;

        .reply-image {
          width: 36rpx;
          height: 34rpx;
        }

        .name {
          font-size: 24rpx;
          font-weight: 400;
          color: $nui-text-color-four;
        }
      }

      .examine-bottom-right {
        display: flex;
        height: 108rpx;
        align-items: center;
        justify-content: flex-end;

        uni-button {
          width: 150rpx;
          height: 70rpx;
          line-height: 1;
          padding: 0;
          font-size: 28rpx;
          margin-right: 34rpx;
          border-radius: 4rpx;
          display: flex;
          align-items: center;
          justify-content: center;

          &:last-of-type {
            margin-right: 0;
          }

          &::after {
            border-radius: 0;
          }

          .iconfont {
            padding-right: 14rpx;
          }
        }
      }
    }

    .replay-con {
      margin-top: 20rpx;
      border-radius: 16rpx;

      ::v-deep .uni-list {
        border-radius: 16rpx;

        .uni-list-item {
          background-color: rgba(0, 0, 0, 0);

          .uni-list-item__container {
            padding: 28rpx 24rpx 0 24rpx;
          }

          &:last-of-type {
            margin-bottom: 0;
          }
        }

        .uni-list--border {
          left: auto;
          top: auto;
        }

      }

      .item-list-left {
        width: 60rpx;
        height: 60rpx;
      }

      .item-list-right {
        width: calc(100% - 60rpx);
        padding-left: 14rpx;
        border-bottom: 1px solid #EBEEF5;
        padding-bottom: 28rpx;

        .right-top {
          font-size: 24rpx;
          color: #687383;
        }

        .right-info {
          padding-top: 20rpx;
          font-size: 28rpx;
          color: #41485B;

          .right-info-text {
            font-size: 32rpx;
            color: #C0C4CC;
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
      min-height: 108rpx;
      background-color: #fff;
      font-size: 28rpx;

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

      .replay-right {
        width: 60rpx;

        .iconfont {
          color: #E4E7ED;
          font-size: 40rpx;
        }
      }
    }
  }
</style>