<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :default-title="data.defaultTitle"></default-nav-bar>
    </view>
    <view class="form-box">
      <view class="process" v-if="data.type!=3">
        <view class="user">
          <view class="lable"> {{data.type==1?'加签审核人':'转审人'}} <text class="iconfont icon-a-"></text></view>
          <view class="user-box">
            <view class="user-item" v-for="(item,index) in data.userList" :key="index">
              <image :src="item.avatar" class="img"></image>
              <view class="name">
                {{item.name}}
              </view>
              <view class="iconfont icon-shenpizhongxin-jujue" @click="deleteUsersItem(index)"></view>
            </view>
            <view class="user-add" @click="adduserFn">
              <text class="iconfont icon-xuanfuanniu-jia"></text>
            </view>
          </view>
        </view>
      </view>

      <!-- 加签方式 -->
      <view class="process">
        <template v-if="data.type==1">
          <view class="flex">
            <view class="lable"> 加签方式 <text class="iconfont icon-a-"></text></view>
            <view class="flex">
              <view class="btn flex" :class="data.form.types==1?'active':''" @click="data.form.types=1">
                在我之前
              </view>
              <view class="btn flex" :class="data.form.types==0?'active':''" @click="data.form.types=0">
                在我之后
              </view>
            </view>
          </view>
          <view class="tips" v-if="data.form.types==1">加签后，流程先经过加签审核人，再由我审核。</view>
          <view class="tips" v-if="data.form.types==0">在我之后加签，即表示同意该申请并增加审核人员。</view>
          <view class="line"></view>

          <!-- 转审 -->
          <view class="flex mt40" v-if="data.userList.length>1">
            <view class="lable"> 多人审批方式 <text class="iconfont icon-a-"></text></view>
            <view class="flex">
              <view class="btn flex" :class="data.form.examine_mode==1?'active':''" @click="data.form.examine_mode=1">
                或签
              </view>
              <view class="btn flex" :class="data.form.examine_mode==2?'active':''" @click="data.form.examine_mode=2">
                会签
              </view>
              <view class="btn flex" :class="data.form.examine_mode==3?'active':''" @click="data.form.examine_mode=3">
                依次审批
              </view>
            </view>
          </view>
          <view class="line" v-if="data.userList.length>1"></view>

        </template>
        <!-- 加签意见 -->
        <view>
          <view class="lable " :class="data.type==1?'mt40':''"> {{data.infoText}} <text class="iconfont icon-a-"
              v-if="data.type==3"></text>
          </view>
          <uni-easyinput :inputBorder="false" v-model="data.form.info" type="textarea" :clearable="false"
            :styles="styles" :placeholder-style="placeholderStyle" :maxlength="256" :autoHeight="true"
            :placeholder=" data.placeholder"></uni-easyinput>
        </view>

      </view>
    </view>

    <view class="examine-button">
      <button type="primary" :loading="data.loading" @click="handleConfirm">提交</button>
    </view>
  </view>

</template>

<script setup>
  import {
    entSignApi,
    entTransferApi,
    approveApplyRevokeApi
  } from '@/api/business'
  import message from '@/utils/message'
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import { navigateToDepartment } from '@/utils/autoload'
  import { clickNavigateTo, delayedReLaunch } from '@/utils/helper'
  import { useStore } from 'vuex'
  const store = useStore()
  const data = reactive({
    defaultTitle: '加签',
    infoText: '加签意见',
    placeholder: '请输入加签意见',
    userList: [],
    type: 1,
    showPerson: false,
    mode: 'selector',
    loading: false,
    id: 0, // 审批id

    form: {
      user: [],
      types: 1,
      examine_mode: 1,
      info: ''
    }
  })

  onLoad((option) => {

    data.id = option.id
    data.type = option.type
    if (data.type == 1) {
      data.defaultTitle = '加签'
      data.placeholder = '请输入加签意见'
      data.infoText = '加签意见'
    } else if (data.type == 2) {
      data.defaultTitle = '转审'
      data.placeholder = '请输入转审意见'
      data.infoText = '转审意见'
    } else if (data.type == 3) {
      data.defaultTitle = '撤销'
      data.placeholder = '填写撤销理由'
      data.infoText = '撤销理由'
    }

  })

  // 保存
  const handleConfirm = () => {
    if (data.type == 3 && !data.form.info) {
      message.error('请填写撤销理由')
      return false
    }
    if (data.userList.length == 0 && data.type != 3) {
      message.error('请选择加签审核人')
      return false
    }
    data.form.user = []
    data.loading = true
    data.userList.map(item => {
      data.form.user.push(item.id)
    })
    if (data.type == 1) {
      entSignApi(data.id, data.form).then(res => {
        if (res.status == 200) {
          message.success(res.message)
          delayedReLaunch(`/pages/users/examine/defaults?id=${data.id}`)
        }
        data.loading = false
      })
    } else if (data.type == 2) {
      if (JSON.parse(uni.getStorageSync('storageUserData')).userInfo.id == data.form.user[0]) {
        message.error('转审人不能选自己')
        data.loading = true
        return false
      }
      entTransferApi(data.id, data.form).then(res => {
        if (res.status == 200) {
          message.success(res.message)
          delayedReLaunch(`/pages/users/examine/defaults?id=${data.id}`)
        }
        data.loading = false
      })
    } else if (data.type == 3) {
      approveApplyRevokeApi(data.id, { info: data.form.info }).then(res => {
        if (res.status == 200) {
          message.success(res.message)
          uni.navigateBack({
            delta: 1,
          });
          // if (data.url) {
          //   delayedReLaunch(data.url)
          // } else {
          //   delayedReLaunch(`/pages/users/examine/center`)
          // }

        }
        data.loading = false
      })
    }

  }


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
    if (data.type == 1) {
      data.mode = 'multiSelector'
    } else if (data.type == 2) {
      data.mode = 'selector'
    }


    const query = `isShow=true&isFirst=1&isSelect=1&mode=${data.mode}&showPerson=${data.showPerson}`
    navigateToDepartment(query, 'pages/users/examine/components/addSignature')
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
    data.userList.splice(index, 1)
  }
</script>

<style lang="scss">
  .form-box {
    padding-top: calc($uni-default-bar-height + var(--status-bar-height));
    padding-bottom: 126rpx;
    margin: 10px;


    .process {
      background-color: #fff;
      border-radius: 12rpx;
      margin-bottom: 20rpx;
      padding: 40rpx 24rpx;
    }
  }

  ::v-deep .uni-easyinput__placeholder-class {
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 30rpx;
    color: #C0C4CC;
  }

  .line {
    height: 40rpx;
    width: 100%;
    border-bottom: 2rpx solid #EBEEF5;
  }

  .mt40 {
    margin-top: 40rpx;
  }

  .tips {
    margin-top: 20rpx;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 26rpx;
    color: #909399;
  }

  .flex {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }


  .btn {
    height: 60rpx;
    border-radius: 8rpx;
    border: 2rpx solid #E4E7ED;
    padding: 24rpx;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 26rpx;
    color: #606266;
    margin-left: 20rpx;
  }

  .active {
    background: rgba(48, 139, 248, 0.07);
    border: 2rpx solid #308BF8;
    color: #308BF8,
  }

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
    .user-box {
      display: flex;
      // align-items: center;
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

  .examine-button {
    height: 126rpx;
    line-height: 126rpx;
    width: 100%;
    padding: 0 20rpx;
    background-color: #fff;
    position: fixed;
    left: 0;
    bottom: 0;
    right: 0;
    display: flex;
    align-items: center;

    uni-button {
      width: 100%;
      height: 86rpx;
      line-height: 86rpx;
      font-size: $uni-font-size-default;
      border-radius: 12rpx;
    }
  }
</style>