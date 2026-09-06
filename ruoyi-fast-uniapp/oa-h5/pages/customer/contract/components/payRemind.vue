<template>
  <view class="list">
    <!-- 付款提醒列表组件 -->
    <!-- 回款提醒 -->
    <view v-if="listData.length > 0" class="list-item" v-for="(item,index) in listData" :key="item.id">
      <view class="header">
        <view class="title">
          <text class="iconfont" :class="item.types === 0 ? 'icon-hetong-hetonghuikuan' : 'icon-tixing-fukuantixing'" />
          {{item.types === 0 ? '回款提醒' : '续费提醒'}}
        </view>
        <dean-popover ref="deanPopoverRef" model-direction="right">
          <template #icon>
            <text class="iconfont icon-yunwenjian-gengduo"></text>
          </template>

          <view class="modal-item" :class="item.status !== 0 ? 'pointer-none' : ''" @click="onEdit(index,item, 2)"><text
              class="iconfont icon-danchuang-tianjiafukuan"></text> {{item.types === 0 ? '回款' : '续费'}}
          </view>
          <view class="modal-item" :class="item.status !== 0 ? 'pointer-none' : ''" @click="onEdit(index,item, 1)"><text
              class="iconfont icon-gongzuohuibao-bianji"></text>编辑
          </view>
          <view class="modal-item" :class="item.status !== 0 ? 'pointer-none' : ''" v-if="item.bill_id > 0"
            @click="onDelete(index, item, 2)"><text class="iconfont icon-danchuang-fangqi"></text>放弃</view>
          <view class="modal-item" v-if="item.bill_id <= 0" @click="onDelete(index, item, 1)"><text
              class="iconfont icon-shanchu"></text>删除</view>
        </dean-popover>
      </view>
      <template v-if="item.types === 0">
        <view class="form">
          <view class="label">预计回款 </view>
          <view class="item red">￥{{item.num}}</view>
        </view>
        <view class="form">
          <view class="label">回款状态 </view>
          <view class="item">{{getCustomerStatus(item.status)}}</view>
        </view>
      </template>

      <template v-else>
        <!--  <view class="form">
          <view class="label">续费类型 </view>
          <view class="item">{{item.renew.title}}</view>
        </view> -->
        <view class="form">
          <view class="label">续费状态 </view>
          <view class="item">{{getCustomerStatus(item.status)}}</view>
        </view>
        <view class="form">
          <view class="label">续费金额 </view>
          <view class="item red">￥{{item.num}}</view>
        </view>
      </template>

      <view class="form">
        <view class="label">提醒内容 </view>
        <view class="item">{{item.mark}}</view>
      </view>
      <view class="tiemBox">
        <image src="@/static/image/cloudfile/tag.png" mode="" class="img"></image> {{item.types === 0?'回款':'续费'}}提醒日期：
        <uni-dateformat format="yyyy/MM/dd hh:mm" :date="item.time"></uni-dateformat>
      </view>
      <view class="reminder">
        <view>
          提醒人：{{item.card.name}}
        </view>
        <view class="time">
          <uni-dateformat format="yyyy/MM/dd hh:mm" :date="item.created_at"></uni-dateformat>
        </view>
      </view>
    </view>
    <empty v-else :index="9" :title="emptyTitle"></empty>
  </view>
</template>

<script setup lang="ts">
  import deanPopover from '@/components/deanPopover/index.vue'
  import empty from '@/components/empty/index.vue'
  import { ref, toRefs } from 'vue'
  import message from '@/utils/message'
  import { clickNavigateTo, showModal } from '@/utils/helper'
  import { clientRemindDeletaApi, clientRemindAbjureApi } from '@/api/customer'
  import type { Res, PropType } from '@/utils/typeHelper'

  const props = defineProps({
    listData: {
      type: Array,
      default() {
        return []
      }
    },
    buildData: {
      type: Object,
      default() {
        return {}
      }
    },
    emptyTitle: {
      type: String,
      default: '暂无付款提醒，快去添加吧！'
    },
  })
  const { listData, buildData } = toRefs(props)

  // 判断年月
  enum RedindPeriod {
    '天',
    '周',
    '月',
    '年'
  }
  const getRedindPeriod = (period : number) : string => {
    return RedindPeriod[period]
  }

  // 合同状态
  enum CustomerStatus {
    '待处理',
    '已放弃',
    '已处理'
  }
  const getCustomerStatus = (status : number) : string => {
    return CustomerStatus[status]
  }

  const indexItem = ref(-1)
  const deanPopoverRef = ref(null)

  //编辑
  const onEdit = (index : number, item : PropType, type : number) => {
    indexItem.value = index
    deanPopoverRef.value[index].close()
    let query = ''

    if (type === 1) {
      // 编辑提醒
      query = `/pages/customer/contract/addRemind?id=${item.id}&cid=${item.cid}&eid=${item.eid}`
    } else {
      if (item.types == 0) {
        // 添加回款
        query = `/pages/users/examine/default?id=${buildData.value.contract_refund_switch}&cid=${item.cid}&eid=${item.eid}`
      } else {
        // 添加续费
        query = `/pages/users/examine/default?id=${buildData.value.contract_renew_switch}&cid=${item.cid}&eid=${item.eid}`
      }
    }
    clickNavigateTo(query)
  }
  // 删除
  const onDelete = (index : number, item : PropType, type : number) => {
    indexItem.value = index
    deanPopoverRef.value[index].close()
    if (type === 1) {
      showModal('确定要删除该付款提醒吗').then(() => {
        remindDeleta(item.id)
      }).catch(() => {

      })
    } else {
      showModal('确定之后变为已放弃状态，您确定此合同不再续费了吗').then(() => {
        remindAbjure(item.id)
      }).catch(() => {

      })
    }
  }

  const remindDeleta = (id : number) => {
    clientRemindDeletaApi(id).then((res : Res) => {
      message.success(res.message)
      listData.value.splice(indexItem.value, 1)
    }).catch((error : Res) => {
      message.error(error.message)
    })
  }
  // 付款放弃
  const remindAbjure = (id : number) => {
    clientRemindAbjureApi(id).then((res : Res) => {
      message.success(res.message)
      listData.value[indexItem.value].status = 1
    }).catch((error : Res) => {
      message.error(error.message)
    })
  }
</script>

<style lang="scss" scoped>
  .list {
    width: 100%;
    padding-top: 10rpx;
    padding-bottom: 20rpx;

    .red {
      color: #E93323 !important;
    }

    .list-item {
      width: 100%;
      border-radius: 16rpx;
      background: linear-gradient(180deg, rgba(24, 144, 255, 0.08) 0%, rgba(255, 255, 255, 0) 97%);
      margin-top: 20rpx;
      padding: 0 24rpx;

      border: 1px solid #E8F4FF;

      .icon-tixing-fukuantixing {
        color: #FF9900;
        font-size: 52rpx;
        margin-right: 14rpx;
      }

      .header {
        width: 100%;
        height: 104rpx;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .title {
          display: flex;
          align-items: center;
          font-size: 32rpx;
          font-family: PingFang SC-中黑体, PingFang SC;
          font-weight: 500;
          color: #303133;
        }

        .icon-hetong-hetonghuikuan {
          font-size: 52rpx;
          color: #19BE6B;
          margin-right: 14rpx;
        }
      }

      .form {
        display: flex;
        margin-bottom: 30rpx;
        // align-items: center;

        .label {
          width: 152rpx;
          font-size: 28rpx;
          font-family: PingFang SC-常规体, PingFang SC;
          font-weight: 400;
          color: #909399;
          margin-right: 30rpx;
        }

        .item {
          width: 100%;
          word-wrap: break-word;
          word-break: normal;
          font-size: 28rpx;
          font-family: PingFang SC-常规体, PingFang SC;
          font-weight: 400;
          color: #303133;

        }
      }

      .tiemBox {
        width: 100%;
        height: 70rpx;
        border-radius: 8rpx;
        background: rgba(24, 144, 255, 0.1);
        display: flex;
        align-items: center;
        color: #1890FF;
        font-size: 28rpx;
        font-family: PingFang SC-常规体, PingFang SC;
        font-weight: 400;
        padding-left: 34rpx;
        margin-top: 44rpx;

        .img {
          width: 34rpx;
          height: 38rpx;
          display: block;
          margin-right: 14rpx;
        }
      }

      .reminder {
        margin-top: 36rpx;
        display: flex;
        justify-content: space-between;
        align-items: flex-end;
        font-size: 28rpx;
        font-family: PingFang SC-常规体, PingFang SC;
        font-weight: 400;
        color: #303133;
        margin-bottom: 40rpx;

        .time {
          font-size: 24rpx;
          font-family: PingFang SC-常规体, PingFang SC;
          font-weight: 400;
          color: #909399;
        }
      }
    }
  }
</style>