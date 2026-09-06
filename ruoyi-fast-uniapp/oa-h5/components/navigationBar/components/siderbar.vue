<template>
  <view>
    <uni-popup ref="popupRef" type="right">
      <view class="slider">
        <view class="pt44">
          <view class="slider-header">
            <uni-row class="display-align plr10">
              <uni-col :span="20" class="header-left">请选择关注文章标签</uni-col>
              <uni-col :span="4" class="text-right header-right">
                <text class="iconfont icon-shenpizhongxin-jujue" @click="cancel"></text>
              </uni-col>
            </uni-row>
          </view>
          <view class="slider-content pl10">
            <scroll-view class="scroll-per" scroll-y :scroll-top="0">
              <view class="pr10">
                <view class="slider-laber" v-for="(item,index) in data.listData" :key="'list'+index">
                  <view class="slider-laber-header">{{item.title}}</view>
                  <view class="slider-laber-item">
                    <button class="laber-item-button" @click="selectAllLabel(item)"
                      :class="item.selectAll? 'active': ''">
                      <text class="title line1">{{item.selectAll ? '取消全选' : '全选'}}</text>
                    </button>

                    <button class="laber-item-button line1" v-for="(items, indexs) in item.children"
                      :class="data.selectLabelData.includes(items.id) ? 'active' : ''" @click="selectItemLabel(items)">
                      <text class="title line1">{{items.title}}</text>
                    </button>
                  </view>
                </view>
              </view>
            </scroll-view>
          </view>
          <view class="slider-bottom plr10">
            <button :loading="loading" @click="saveLabel">确认</button>
          </view>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
  import { ref, reactive, toRefs, onMounted, watch } from 'vue'
  import message from '@/utils/message'
  const props = defineProps({
    // 自定义导航栏列表与defaultType为1时，同时使用
    checkedTypes: {
      type: Object,
      default: () => {
        return []
      }
    },
    isFirstlogin: {
      type: Boolean,
      default: false
    },
    typeData: {
      type: Object,
      default: () => {
        return []
      }
    }
  })
  const { checkedTypes, typeData, isFirstlogin } = toRefs(props)

  const emit = defineEmits(["handleOk"]);

  const popupRef = ref(null)

  const styleId = ref(1)

  const data = reactive({
    checkbox5: [],
    listData: [],
    selectLabelData: []
  })

  watch(isFirstlogin, (newvalue) => {
    if (newvalue) {
      getArticleLabel()
      getUserLabel()
    }
  })

  // 打开弹出
  const popupOpen = () => {
    popupRef.value.open()
  }

  // 关闭
  const cancel = () => {
    popupRef.value.close()
  }

  onMounted(() => {
    getArticleLabel()
    getUserLabel()
  })

  import { articleUserLabelApi, articleLabelApi, articleLabeSavelApi } from '@/api/forum'
  // 获取用户关注文章标签
  const getUserLabel = () => {
    let where = {
      page: 1,
      limit: 0
    }
    articleUserLabelApi(where).then(res => {

      data.selectLabelData = res.data.label || []
    }).catch((error) => {
      message.error(error.message)
    })
  }


  // 获取文章分类
  const getArticleLabel = () => {
    articleLabelApi(0).then(res => {
      data.listData = res.data.list || []
      if (data.listData.length > 0) {
        data.listData.forEach(value => {
          value.children.forEach(val => {
            if (!data.selectLabelData.includes(val.id)) {
              value.selectAll = false
            } else {
              value.selectAll = true
            }
          })
        })
      }
    }).catch((error) => {
      message.error(error.message)
    })
  }

  // 标签选择
  const selectItemLabel = (item) => {
    const index = data.selectLabelData.indexOf(item.id)
    if (index > -1) {
      data.selectLabelData.splice(index, 1)
    } else {
      data.selectLabelData.push(item.id)
    }
  }

  // 全部标签选择
  const selectAllLabel = (row) => {
    if (row.children.length > 0) {
      if (row.selectAll) {
        row.children.forEach(value => {
          const indexs = data.selectLabelData.indexOf(value.id)
          if (indexs > -1) {
            data.selectLabelData.splice(indexs, 1)
          }
        })
        row.selectAll = false
      } else {
        row.children.forEach(value => {
          if (!data.selectLabelData.includes(value.id)) {
            data.selectLabelData.push(value.id)
          }
        })
        row.selectAll = true
      }
    }
  }


  // 保存标签
  const saveLabel = () => {
    if (data.selectLabelData.length <= 0) {
      message.error('至少关注一个文章标签')
      return false
    }

    articleSaveLabel({ label: data.selectLabelData })

  }

  const loading = ref(false)
  // 保存选择分类
  const articleSaveLabel = (data) => {
    loading.value = true
    articleLabeSavelApi(data).then(res => {
      loading.value = false
      emit('handleOk')
      cancel()
      message.success('保存成功')
    }).catch((error) => {
      loading.value = false
      message.error(error.message)
    })
  }

  defineExpose({ popupOpen })
</script>

<style lang="scss" scoped>
  ::v-deep .uni-popup {
    z-index: 102;
  }

  ::v-deep .uni-popup__wrapper.right {
    padding-top: 0 !important;
  }

  .slider {
    position: relative;
    height: 100vh;
    width: calc(100vw - 90rpx);
    background-color: #fff;


    .pt44 {
      // #ifndef APP-PLUS
      padding-top: 0;
      // #endif
      // #ifdef APP-PLUS
      padding-top: 44px;
      // #endif
    }

    .slider-header {
      width: 100%;
      height: 90rpx;
      line-height: 90rpx;
      background-color: #fff;
      border-bottom: 1px solid #EBEEF5;

      .header-left {
        font-size: 32rpx;
        color: $uni-text-color;
        font-weight: 600;
      }

      .header-right {
        font-size: 32rpx;
        color: #C0C4CC;
      }
    }

    .slider-content {
      // #ifndef APP-PLUS
      height: calc(100vh - 1px - 90rpx - 140rpx);
      // #endif
      // #ifdef APP-PLUS
      height: calc(100vh - 45px - 90rpx - 140rpx);
      // #endif

      .scroll-per {
        height: 100%;
      }

      .slider-laber {
        .slider-laber-header {
          padding-top: 32rpx;
          padding-bottom: 8rpx;
          font-size: 30rpx;
          color: $uni-text-color;
          font-weight: $uni-default-font-weight;

          .iconfont {
            color: $nui-text-color-two;
            font-weight: normal;
          }
        }

        .slider-laber-item {
          width: 100%;
          display: flex;
          align-items: center;
          flex-wrap: wrap;

          .laber-item-button {
            position: relative;
            width: 184rpx;
            height: 70rpx;
            line-height: 26rpx;
            background-color: #F0F1F5;
            border-radius: 8rpx;
            font-size: 26rpx;
            padding: 0;
            margin: 0;
            color: $uni-text-color;
            overflow: visible;
            margin-top: 24rpx;
            margin-right: 24rpx;

            &:nth-of-type(3n) {
              margin-right: 0;
            }

            &::after {
              border-color: #F0F1F5;
              border-radius: 8rpx;
            }

            &.active {
              background: rgba(24, 144, 255, 0.1);
              color: $uni-color-primary;

              &::after {
                border-color: $uni-color-primary;

              }

              .icon {
                display: block;
              }
            }

            .title {
              display: block;
              width: 100%;
              height: 100%;
              display: flex;
              align-items: center;
              justify-content: center;
            }

            .icon {
              display: none;
              position: absolute;
              right: 0;
              bottom: 0;
              border: 20rpx solid $uni-color-primary;
              border-left: 20rpx solid transparent;
              border-top: 20rpx solid transparent;

              .iconfont {
                position: absolute;
                left: 0;
                top: -6rpx;
                color: #fff;
                z-index: 2;
                font-size: 22rpx;
                line-height: 1;
              }
            }
          }

        }
      }
    }


    .slider-bottom {
      position: absolute;
      height: 140rpx;
      display: flex;
      align-items: center;
      width: 100%;
      left: 0;
      bottom: 0;
      right: 0;

      uni-button {
        width: 100%;
        height: 80rpx;
        line-height: 80rpx;
        font-size: 30rpx;
        background-color: $uni-color-primary;
        color: #fff;
        border-radius: 8rpx;
      }
    }

  }
</style>