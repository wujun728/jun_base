<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-right="true" @goBackChange="goBackChange"></default-nav-bar>
    </view>

    <view class="examine-content">
      <view class="add" @click="clickAdd">
        <view class="box">
          <avatar v-if="data.selectUser.length > 0" :src="data.selectUser[0].avatar" :radius="8"></avatar>
          <text v-else class="iconfont icon-xuanfuanniu-jia" />
        </view>
        <text class="member">{{data.selectUser.length > 0 ? data.selectUser[0].name: '选择成员'}}</text>
      </view>
      <view class="content-list ">
        <template v-for="(item,index) in data.listData" :key="item.id">
          <view class="list-item" v-if="item.id >= data.type" :class="data.tabIds.includes(item.id) ? 'active': ''"
            @click="clickItem(item.id)">
            <view class="iconfont icon-center" :class="item.icon" />
            <view class="text">{{item.name}}</view>
            <view class="iconfont icon-xuanzhong1" />
          </view>
        </template>
      </view>

    </view>

    <!-- 底部 -->
    <view class="examine-button">
      <button class="button" :loading="loading" type="primary" @click="handleConfirm">提交</button>
    </view>
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import avatar from '@/components/avatar/index.vue'
  import { ref, reactive, computed, watch } from 'vue'
  import { useStore } from 'vuex'
  import { navigateToDepartment } from '@/utils/autoload'
  import { clientDataShiftApi, clientContractShiftApi, clientInvoiceShiftApi } from '@/api/customer'
  import { resetSelectDepartment } from '@/utils/autoload'
  import { onLoad } from '@dcloudio/uni-app'
  import { clickNavigateTo, delayedReLaunch } from '@/utils/helper'

  const store = useStore()
  const data = reactive({
    selectUser: [],
    listData: [
      { name: '客户转移', icon: 'icon-kehuxiangqing-kehuzhuanyi', id: 1 },
      { name: '合同转移', icon: 'icon-kehuxiangqing-hetongzhuanyi', id: 2 },
      { name: '发票转移', icon: 'icon-kehuxiangqing-fapiaozhuanyi1', id: 3 },
    ],
    tabIds: [],
    type: 1,
    eid: -1,
    cid: -1,
    iid: -1
  })

  onLoad(options => {
    data.type = Number(options.type)
    data.eid = Number(options.eid)
    data.cid = Number(options.cid)
    data.iid = Number(options.iid)
    data.tabIds.push(data.type)
  })

  //返回的回掉函数清楚选中人员
  const goBackChange = () => {
    resetSelectDepartment()
  }

  const loading = ref(false)

  // 选中人的列表
  const getSelectPeople = computed(() => {
    return store.state.app.depSelectPeople
  })

  // 数据监听
  watch(getSelectPeople, (newvalue, oldvalue) => {
    data.selectUser = newvalue
  }, { immediate: true })

  const clickItem = id => {
    if (data.selectUser.length <= 0) {
      message.error('请选择转移的业务员')
      return false
    }
    if (id <= data.type) return false
    const len = data.tabIds.indexOf(id)
    if (len > -1) {
      data.tabIds.splice(len, 1)
    } else {
      data.tabIds.push(id)
    }
  }
  //添加成员
  const clickAdd = () => {
    const str = `isShow=true&mode=selector`
    navigateToDepartment(str, 'pages/customer/list/shift')
  }

  import message from '@/utils/message'
  const handleConfirm = () => {
    if (data.selectUser.length <= 0) {
      message.error('请选择转移的业务员')
      return false
    }
    if (!loading.value) {
      // 客户转移
      if (data.type === 1) {
        const datas = {
          contract: data.tabIds.includes(2) ? 1 : 0,
          invoice: data.tabIds.includes(3) ? 1 : 0,
          to_uid: data.selectUser[0].id
        }
        clientDataShif(data.eid, datas)
      } else if (data.type === 2) {
        const datas = {
          invoice: data.tabIds.includes(3) ? 1 : 0,
          to_uid: data.selectUser[0].id
        }
        clientContractShift(data.cid, datas)
      } else if (data.type === 3) {
        const datas = {
          ids: [data.iid],
          to_uid: data.selectUser[0].id
        }
        clientInvoiceShift(datas)
      }
    }
  }

  // 客户转移
  const clientDataShif = (id, datas) => {
    loading.value = true
    clientDataShiftApi(id, datas).then(res => {
      message.success(res.message)
      loading.value = true
      delayedReLaunch('/pages/customer/list/index')
      resetSelectDepartment()
    }).catch(error => {
      loading.value = false
      message.error(error.message)
    })
  }
  // 合同转移
  const clientContractShift = (id, datas) => {
    loading.value = true
    clientContractShiftApi(id, datas).then(res => {
      message.success(res.message)
      loading.value = true
      delayedReLaunch('/pages/customer/contract/index')
      resetSelectDepartment()
    }).catch(error => {
      loading.value = false
      message.error(error.message)
    })
  }
  // 发票转移
  const clientInvoiceShift = datas => {
    loading.value = true
    clientInvoiceShiftApi(datas).then(res => {
      message.success(res.message)
      loading.value = true
      delayedReLaunch('/pages/customer/invoice/index')
      resetSelectDepartment()
    }).catch(error => {
      loading.value = false
      message.error(error.message)
    })
  }
</script>

<style lang="scss">
  .content {
    height: 100vh;
    background-color: #fff;

    .examine-content {
      padding: 30rpx;
      width: 100%;
      padding-top: calc($uni-default-bar-height + var(--status-bar-height));

      .content-list {
        padding-bottom: 40rpx;
        display: flex;
        border-top: 1px solid #EBEEF5;
      }

      .list-item {
        position: relative;
        margin-top: 60rpx;
        margin-right: 16rpx;
        width: calc((100% - 32rpx) / 3);
        height: 232rpx;
        border-radius: 8rpx;
        border: 1px solid #EBEEF5;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        &:last-of-type {
          margin-right: 0;
        }

        &.active {
          border-color: #1890FF;

          .icon-center {
            color: #1890FF;
          }

          .text {
            color: #303133;
          }

          .icon-xuanzhong1 {
            display: block;
          }
        }

        .icon-center {
          font-size: 80rpx;
          color: #C0C4CC;
        }

        .icon-xuanzhong1 {
          display: none;
          position: absolute;
          bottom: -4rpx;
          right: -2rpx;
          font-size: 40rpx;
          color: #1890FF;
        }

        .text {
          font-size: 28rpx;
          font-family: PingFang SC-常规体, PingFang SC;
          font-weight: 400;
          color: #C0C4CC;
        }
      }
    }

    .add {
      padding: 42rpx 30rpx;
      display: flex;
      align-items: center;

      .member {
        display: inline-block;
        font-size: 28rpx;
        font-family: PingFang SC-常规体, PingFang SC;
        font-weight: 400;
        color: #2B2C32;
        margin-left: 20rpx;
      }

      .box {
        width: 80rpx;
        height: 80rpx;
        border-radius: 7rpx;
        background: rgba(236, 237, 240, 0.5);
        text-align: center;
        line-height: 80rpx;

        .icon-xuanfuanniu-jia {
          font-size: 25rpx;
          color: #BCBDC0;
        }
      }
    }


    .examine-button {
      height: 126rpx;
      line-height: 126rpx;
      width: 100%;
      padding: 0 20rpx;
      position: fixed;
      left: 0;
      bottom: 0;
      right: 0;
      display: flex;
      align-items: center;

      .button {
        width: 100%;
        height: 86rpx;
        line-height: 86rpx;
        font-size: $uni-font-size-default;
        border-radius: 12rpx;
      }
    }
  }
</style>