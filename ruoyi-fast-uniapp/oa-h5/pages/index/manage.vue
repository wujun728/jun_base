<template>
  <view class="">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <uni-nav-bar left-text="取消" @clickLeft="cancel">
        <view class="uni-nav-bar-text">管理应用</view>
        <template v-slot:right>
          <button class="save" :loading="config.loading" @click="handleSave">保存</button>
        </template>
      </uni-nav-bar>
    </view>

    <view class="content">
      <view class="nav-content plr12 m10">
        <view class="nav-title">已添加应用</view>
        <view class="nav-content-list">
          <template v-if="config.menus.length>0">
            <l-drag :list="config.menus" @change="change" :column="4" :gridHeight="`160rpx`">
              <template #grid="{active, content,indexs}">
                <!-- // grid.active 是否为当前拖拽项目 根据自己需要写样式 -->
                <view class="item-box">
                  <view class="nav-list-item-box">
                    <image v-if="content.image" :src="content.image" class="nav-image" mode=""></image>
					<image v-else src="../../static/image/record.png" class="nav-image" mode=""></image>
                    <image src="../../static/image/remove-icon.png" class="nav-icon" @click="hanleRemove(content)"
                      mode="">
                    </image>
                  </view>
                  <view class="nav-list-item-title">{{content.name}}</view>
                </view>
              </template>
            </l-drag>

            <!-- <view class="nav-list-item" v-for="(items,indexs) in config.menus" :key="indexs">
              <view class="nav-list-item-box">
                <image :src="items.image" class="nav-image" mode=""></image>
                <image src="../../static/image/remove-icon.png" class="nav-icon" @click="hanleRemove(indexs)" mode="">
                </image>
              </view>
              <view class="nav-list-item-title">{{items.name}}</view>
            </view> -->
          </template>

          <view v-else class="nav-list-empty">
            派瑞集团会更努力了解你的需求
          </view>

        </view>
      </view>
      <template v-for="(item,index) in config.fastEntry" :key="index">
        <view class="nav-content plr12 m10" v-if="item.fast_entry && item.fast_entry.length > 0">
          <view class="nav-title">{{item.cate_name}}</view>
          <view class="nav-content-list">
            <view class="nav-list-item" v-for="(items,indexs) in item.fast_entry" :key="indexs">
              <view class="nav-list-item-box">
                <image v-if="items.image" :src="items.image" class="nav-image" mode=""></image>
				<image v-else src="../../static/image/record.png" class="nav-image" mode=""></image>
                <image v-if="!config.selectIds.includes(items.id)" @click="handlePush(items)"
                  src="../../static/image/add-icon.png" class="nav-icon" mode="">
                </image>
              </view>
              <view class="nav-list-item-title">{{items.name}}</view>
            </view>
          </view>
        </view>
      </template>
    </view>
    <global-index />
  </view>
</template>
<script setup lang="ts">
  import globalIndex from '@/components/globalIndex/index.vue'
  import message from '@/utils/message'
  import { delayedReLaunch } from '@/utils/helper'
  import { useStore } from 'vuex'
  import { toLogin } from '@/libs/login'
  import { userWorkMenusApi, userWorkFastEntryApi, userWorkMenusSaveApi } from '@/api/user'
  import type { Res, Detail } from '@/utils/typeHelper'

  const store = useStore();
  const isLogin = computed(() => store.state.app.isLogin)
  onShow(() => {
    if (!isLogin.value) {
      toLogin()
    }
  })

  onMounted(() => {
    getWorkMenus()
    // getWorkFastEntry()
  })


  let config = reactive({
    assessNow: <any>[],
    menus: <any>[],
    fastEntry: <any>[],
    selectIds: [],
    selectMax: 12,
    loading: false
  })

  const cancel = () : void => {
    uni.navigateBack()
  }

  // 获取首页应用
  const getWorkMenus = () : void => {
    userWorkMenusApi().then((res : Res) => {
      config.menus = []
      config.fastEntry = res.data.cates
      config.menus = res.data.checkd

      // res.data.map((item) => {
      //   item.fast_entry.map((item2) => {
      //     if (item2.checked == 1) {
      //       config.menus.push(item2);
      //     }
      //   });
      // });
    }).catch((error : Res) => {
      message.error(error.message)
    })
  }

  // 获取所有内容
  const getWorkFastEntry = () : void => {
    userWorkFastEntryApi().then((res : Res) => {
      config.fastEntry = res.data
    }).catch((error : Res) => {
      message.error(error.message)
    })
  }

  const hanleRemove = (row : Object) : void => {
    config.menus = config.menus.filter((item) => {
      return item.id != row.id;
    })
    // config.menus.splice(index, 1)
  }

  const handlePush = (item : Detail) : boolean => {
    if (config.menus.length >= config.selectMax) {
      message.error('最多只能添加' + config.selectMax + '个应用');
      return false;
    }
    config.menus.push(item);
    const datalist = ref([])
    config.menus.forEach((per) => {
      datalist.value.push(per)
    })
    config.menus = datalist.value
  }


  const change = (list) : void => {
    config.menus = []
    list.map(item => {
      config.menus.push(item.content)
    })
  }

  //保存
  const handleSave = () : boolean => {
    if (config.menus.length < 1) {
      message.error('至少添加一个应用');
      return false
    }
    const data = []
    if (config.menus.length > 0) {
      config.menus.forEach(value => {
        data.push(value.id)
      })
    }
    config.loading = true
    userWorkMenusSaveApi({ data }).then((res : Res) => {
      message.success(res.message, 'icongou')
      config.loading = false
      delayedReLaunch('/pages/index/index')
    }).catch((error : Res) => {
      config.loading = false
      message.error(error.message)
    })
  }

  watch(
    () => config.menus, (newvalue) => {
      config.selectIds = []
      if (newvalue.length > 0) {
        newvalue.forEach(value => {
          config.selectIds.push(Number(value.id))
        })
      } else {
        config.selectIds = []
      }
    }, {
    deep: true,
    immediate: true
  })
</script>
<style scoped lang="scss">
  .item-box {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20rpx 20rpx 20rpx 20rpx;
  }

  .cr-position-header {
    position: sticky;

    ::v-deep .uni-navbar {
      .uni-navbar__header-container {
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .uni-navbar-btn-text {
        uni-text {
          padding-top: 8rpx;
          font-size: 32rpx !important;
          color: $uni-color-primary !important;
        }
      }

      .uni-nav-bar-text {
        font-size: 34rpx;
        font-weight: 500;
      }

      .uni-navbar__header-btns-right {
        justify-content: flex-end;
      }
    }

    .save {
      width: 92rpx;
      height: 54rpx;
      line-height: 54rpx;
      background: #1890FF;
      border-radius: 8rpx;
      font-size: 26rpx;
      padding: 0;
      color: #fff;
      margin: 0;
    }
  }

  .nav-content {
    padding-top: 28rpx;
    background-color: #fff;
    border-radius: 16rpx;

    .nav-title {
      font-size: 32rpx;
      padding-bottom: 34rpx;
      font-weight: 600;
      color: $uni-text-color;

      .nav-title-right {
        font-size: 24rpx;
        color: $nui-text-color-four;
        font-weight: normal;

        .iconfont {
          display: inline-block;
          font-size: 24rpx;
          padding-left: 10rpx;
          transform: rotate(180deg);
        }
      }
    }

    .nav-content-list {
      border-radius: 16rpx;
      display: flex;
      justify-content: flex-start;
      flex-wrap: wrap;
      width: calc(100% + 58rpx);
      margin-left: -29rpx;

      .nav-list-item {
        width: 25%;
        text-align: center;
        margin-bottom: 36rpx;
      }

      .nav-list-item-box {
        width: 90rpx;
        height: 90rpx;
        display: inline-block;
        position: relative;

        .nav-image {
          width: 100%;
          height: 100%;
        }

        .nav-icon {
          width: 30rpx;
          height: 30rpx;
          position: absolute;
          top: -15rpx;
          right: -15rpx;
          cursor: pointer;
        }
      }

      .nav-list-item-title {
        padding-top: 8rpx;
        font-size: 24rpx;
        color: $uni-text-color;
      }
    }


    .nav-list-empty {
      height: 136rpx;
      width: 100%;
      margin: 0 24rpx 36rpx 24rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      color: $uni-text-color-disable;
      font-size: 26rpx;
      border: 1px dashed $uni-text-color-disable;
    }
  }
</style>