<template>
  <view class="uni-steps">
    <view class="user">
      <view class="lable"> 加签审核人 <text class="iconfont icon-a-"></text></view>
      <view class="user-box">
        <view class="user-item" v-for="(item,index) in data.userList" :key="index">
          <image :src="item.avatar" class="img"></image>
          <view class="name">
            {{item.name}}
          </view>
          <view class="iconfont icon-shenpizhongxin-jujue" @click="deleteUsersItem()"></view>
        </view>
        <view class="user-add" @click="adduserFn">
          <text class="iconfont icon-xuanfuanniu-jia"></text>
        </view>
      </view>
    </view>

    <!-- 加签方式 -->

  </view>

</template>

<script setup>
  import {
    reactive,
    toRefs,
    watch,
    ref,
    computed
  } from "vue"
  import {
    navigateToDepartment,
    resetExamineIndex,
    resetSelectDepartment
  } from '@/utils/autoload'
  import avatar from '@/components/avatar/index'
  import {
    useStore
  } from 'vuex'
  const store = useStore()

  const data = reactive({
    userList: [],
    showPerson: false,
    mode: 'selector',
  })

  // 选中人的列表
  const getSelectPeople = computed(() => {
    return store.state.app.depSelectPeople
  })

  watch(
    getSelectPeople,
    (newvalue) => {
      data.userList = newvalue
    }, {
      deep: true,
    }
  )

  // 新增加签人
  const adduserFn = () => {
    data.showPerson = true
    data.mode = 'multiSelector'
    const query = `isShow=true&isFirst=1&isSelect=1&mode=${data.mode}&showPerson=${data.showPerson}`
    navigateToDepartment(query, 'pages/customer/list/statistics')
    if (data.userList && data.userList.length > 0) {
      let idsArr = []
      data.userList.map((item) => {
        idsArr.push(item.id)
      })
      store.commit('setDepSelectPeople', data.userList)
      store.commit('setDepSelectIds', idsArr)
    } else {
      store.commit('setDepSelectPeople', [])
      store.commit('setDepSelectIds', [])
    }
  }


  // 删除人
  const deleteUsersItem = (row, index) => {
    row.splice(index, 1)
  }
</script>

<style scoped lang="scss">
  $uni-primary: #2979ff !default;
  $uni-border-color: #EDEDED;

  .lable {
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 30rpx;
    color: #303133;
    display: flex;
    align-items: center;

    .icon-a- {
      font-size: 14rpx;
      color: #FF2529;
      margin-left: 8rpx;
    }
  }



  .user {
    padding: 32rpx 6rpx 32rpx 24rpx;

    .user-box {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      margin-top: 20rpx;

      .user-item {
        padding: 12rpx;
        background: #F7F7F7;
        display: flex;
        align-items: center;
        margin-right: 18rpx;
        margin-bottom: 18rpx;

        .img {
          width: 60rpx;
          height: 60rpx;
          border-radius: 8rpx;
        }

        .name {
          font-family: PingFang SC, PingFang SC;
          font-weight: 400;
          font-size: 26rpx;
          color: #303133;
          margin: 0 12rpx;
        }

        .icon-shenpizhongxin-jujue {
          font-size: 18rpx;
          color: #C0C4CC;
        }
      }

      .user-add {
        width: 84rpx;
        height: 84rpx;
        background: rgba(48, 139, 248, 0.1);
        border-radius: 8rpx;
        text-align: center;
        line-height: 84rpx;

        .icon-xuanfuanniu-jia {
          font-size: 24rpx;
          color: #308BF8;
          font-weight: 500;
        }
      }
    }

  }
</style>