<template>
  <view class="examine-content-list">
    <uni-list :border="false" v-if="listData.length > 0">
      <uni-list-item v-for="(item,index) in listData" :key="'list'+item.id">
        <template v-slot:body>
          <view class="item-list">
            <view class="item-list-top" @tap="examineList(item)" v-if="item.card">
              {{item.card.name}}的{{item.approve.name}}
            </view>
            <view class="" @tap="examineList(item)">
              <examine-list-item :content="item.content"></examine-list-item>
              <uni-row class="item-list-content">
                <uni-col :span="6" class="left">提交时间</uni-col>
                <uni-col :span="18">
                  <uni-dateformat format="yyyy/MM/dd hh:mm" :date="item.created_at"></uni-dateformat>
                </uni-col>
              </uni-row>
            </view>
            <view class="item-list-content item-list-button">
              <template v-if="typeIndex < 0">
                <!-- <button class="default-color" v-if="item.status === 1">再次提交</button> -->
                <button class="default-error" @click="getApplyRevoke(item)"
                  v-if="((item.status === 1 && item.rules&&item.rules.recall == 1) || item.status === 0)&& !item.recall">撤销</button>
                <button class="default-color" @click="resubmit(item)"
                  v-if="item.status === 1 && item.type < 6">重新提交</button>
              </template>
              <template v-else>
                <template v-if=" item.status !==1">
                  <button v-if="item.verify_status === 0" class="default-error"
                    @tap="handleRefuse(item, index)">拒绝</button>
                  <button v-if="item.verify_status === 0" class="default-color"
                    @tap="handleAgree(item, index)">同意</button>
                </template>
              </template>
            </view>

            <image v-if="item.status === 0" class="item-list-status" src="/static/image/approval.png" mode=""></image>
            <image v-if="item.status === 1" class="item-list-status" src="/static/image/passed.png" mode=""></image>
            <image v-if="item.status === 2" class="item-list-status" src="/static/image/refuse.png" mode=""></image>
            <image v-if="item.status === -1" class="item-list-status" src="/static/image/revoke.png" mode=""></image>

          </view>
        </template>
      </uni-list-item>
    </uni-list>
    <empty v-else :index="4" :title="emptyTitle"></empty>
  </view>
</template>

<script setup>
  import empty from '@/components/empty/index.vue'
  import examineListItem from './examineListItem.vue'
  import { ref, reactive, toRefs, watch } from 'vue'
  import message from '@/utils/message'
  const props = defineProps({
    listData: {
      type: Array,
      default () {
        return []
      }
    },
    typeIndex: {
      type: Number,
      default: 0
    },
    emptyTitle: {
      type: String,
      default: ''
    },
  })
  const { listData, typeIndex, emptyTitle } = toRefs(props)

  import { clickNavigateTo } from '@/utils/helper'
  const examineList = item => {
    clickNavigateTo(`/pages/users/examine/defaults?id=${item.id}&typeIndex=${typeIndex.value}`)
  }

  //重新提交
  const resubmit = item => {
    if (!item.approve) {
      message.error('审批流程已被删除')
      return false
    }
    clickNavigateTo(
      `/pages/users/examine/default?id=${item.approve.id}&name=${item.approve.name}&isEdit=true&aid=${item.id}`)
  }

  import { approveApplyRevokeApi, approveVerifyStatusApi } from '@/api/business'
  //申请审批撤销
  const getApplyRevoke = item => {
    if (item.status === 0) {
      showModal('确定要撤销该审批记录吗').then(() => {
        approveApplyRevokeApi(item.id).then(res => {
          message.success(res.message)
          item.status = -1
        }).catch(error => {
          message.error(error.message)
        })
      }).catch(() => {
        console.log('取消')
      })
    } else {
      clickNavigateTo(`/pages/users/examine/components/addSignature?id=${item.id}&type=3`)
    }

  }

  import { showModal } from '@/utils/helper'
  // 同意
  const handleAgree = (row, index) => {
    showModal('确定要同意申请人的申请吗').then(() => {
      getApproveVerify(row.id, 1, index)
    }).catch(() => {
      console.log('取消')
    })
  }

  // 拒绝
  const handleRefuse = (row, index) => {
    showModal('确定要 拒绝 申请人的申请吗').then(() => {
      getApproveVerify(row.id, 0, index)
    }).catch(error => {
      console.log('取消')
    })
  }

  //申请审批处理
  const getApproveVerify = (id, status, index) => {
    approveVerifyStatusApi(id, status).then(res => {
      message.success(res.message)
      listData.value.splice(index, 1)
    }).catch(error => {
      message.error(error.message)
    })
  }

  // 数据监听
  watch(() => listData, (newvalue, oldvalue) => {

  }, { immediate: true, deep: true })
</script>

<style scoped lang="scss">
  .examine-content-list {
    ::v-deep .uni-list {
      background-color: $uni-default-bg;

      .uni-list--border {
        top: auto;
        left: auto;
      }

      .uni-list-item {
        margin-bottom: 20rpx;
        border-radius: 8rpx;

        .uni-list-item__container {
          padding: 32rpx 24rpx 36rpx 24rpx;
        }
      }
    }

    .item-list {
      width: 100%;
      position: relative;

      .item-list-top {
        font-size: 32rpx;
        font-weight: 600;
        padding-bottom: 24rpx;
        color: $uni-text-color;
      }

      .item-list-content {
        font-size: 28rpx;
        color: $uni-text-color;
        font-weight: 400;

        .left {
          color: $nui-text-color-four;
        }

        uni-button {
          width: 100%;
          height: 74rpx;
          line-height: 74rpx;
          font-size: 30rpx;
          background-color: #F0F1F5;
          margin-right: 32rpx;

          &:last-of-type {
            margin-right: 0;
          }

          &::after {
            border-radius: 8rpx;
            border: none;
          }
        }
      }

      .item-list-button {
        padding-top: 24rpx;
        display: flex;
        justify-content: space-between;
      }

      .item-list-status {
        position: absolute;
        top: -32rpx;
        right: -24rpx;
        width: 160rpx;
        height: 188rpx;
      }
    }
  }
</style>