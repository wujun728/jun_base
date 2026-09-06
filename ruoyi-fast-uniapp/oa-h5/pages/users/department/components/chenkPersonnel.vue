<template>
  <view>
    <uni-popup ref="popupRef" type="bottom" :mask-click="false">
      <view class="check-per">
        <uni-nav-bar
          :border="false"
          :title="`已选择${newCheckPer.length}${showPerson ? '人' : '部门'}`"
          leftText="取消"
          rightText="确认"
          @clickLeft="cancelNo()"
          @clickRight="cancelOk()"
        ></uni-nav-bar>
        <view class="pl10">
          <scroll-view class="scroll-per" scroll-y :scroll-top="0">
            <view class="uni-indexed-list__item-content" v-for="(item, index) in newCheckPer" :key="item.id">
              <uni-row class="display-align">
                <uni-col :span="18" class="display-align">
                  <avatar :src="item.avatar" :radius="8" :auto-size="false" :width="80" :height="80"></avatar>
                  <view class="item-content-info">
                    <text class="text">{{ item.name }}</text>
                    <text class="caption">{{ item.card && item.card.job ? item.card.job.name : '--' }}</text>
                  </view>
                </uni-col>
                <uni-col :span="6" class="text-right pr10">
                  <template v-if="isChecked === 1">
                    <button v-if="!depIds.includes(Number(item.id))" class="item-button" size="mini" @click="handleDelete(index)">移除</button>
                  </template>
                  <template v-else>
                    <button class="item-button" size="mini" @click="handleDelete(index)">移除</button>
                  </template>
                </uni-col>
              </uni-row>
            </view>
          </scroll-view>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
import { ref, toRefs, watch, computed } from 'vue'
import avatar from '@/components/avatar/index.vue'
const popupRef = ref(null)
import { useStore } from 'vuex'
const store = useStore()
const props = defineProps({
  checkPer: {
    type: Object,
    default: () => {
      return {}
    },
  },
  isChecked: {
    type: Number,
    default: 0,
  },
  showPerson: {
    type: Boolean,
    default: true,
  },
})
const { checkPer, isChecked, showPerson } = toRefs(props)

const depCheckIds = computed(() => {
  //返回的是ref对象
  return store.state.app.depCheckIds
})

const newCheckPer = ref([])
const depIds = ref([])

const handleDelete = (index) => {
  newCheckPer.value.splice(index, 1)
}

// 打开弹出
const popupOpen = () => {
  popupRef.value.open()
}

// 关闭验证码
const cancel = () => {
  let ids = []
  if (newCheckPer.value.length > 0) {
    newCheckPer.value.forEach((value) => {
      ids.push(value.id)
    })
  }
  store.commit('setDepSelectIds', ids)
  popupRef.value.close()
}

watch(
  () => checkPer,
  (newvalue, oldalue) => {
    const values = Object.assign([], newvalue.value)
    newCheckPer.value = values
  },
  { deep: true, immediate: true }
)
// 数据监听
watch(
  depCheckIds,
  (newvalue, oldvalue) => {
    depIds.value = newvalue
  },
  { immediate: true }
)

// 取消
const cancelNo = () => {
  newCheckPer.value = checkPer.value
  cancel()
}

// 确认
const cancelOk = () => {
  store.commit('setDepSelectPeople', newCheckPer.value)
  cancel()
}

defineExpose({ popupOpen })
</script>

<style lang="scss" scoped>
.check-per {
  width: 100%;
  background-color: #fff;
  height: 70vh;
  border-radius: 20rpx 20rpx 0 0;

  ::v-deep .uni-navbar__content {
    border-radius: 20rpx 20rpx 0 0;

    .uni-navbar__header {
      border-radius: 20rpx 20rpx 0 0;
    }

    .uni-navbar-btn-icon-left {
      uni-text {
        font-size: 28rpx !important;
        color: #909399 !important;
      }
    }

    .uni-navbar__header-container-inner {
      font-weight: $uni-default-font-weight;
    }

    .uni-nav-bar-text {
      font-size: $uni-font-size-default;
    }

    .uni-navbar__header-btns-right {
      uni-text {
        font-size: 28rpx !important;
        color: #308bf8 !important;
      }
    }
  }

  .scroll-per {
    height: calc(70vh - 44px);
    padding-top: 20rpx;
  }

  .uni-indexed-list__item-content {
    padding-bottom: 40rpx;

    .avatar {
      height: 80rpx;
      width: 80rpx;
      border-radius: 8rpx;
    }

    .item-content-info {
      padding-left: 20rpx;

      uni-text {
        display: block;
      }

      .text {
        font-size: 28rpx;
        color: #2b2c32;
      }

      .caption {
        padding-top: 10rpx;
        font-size: 24rpx;
        color: #909399;
      }
    }

    .item-button {
      background-color: #f0f1f5;
      color: #909399;

      &::after {
        border: none;
      }
    }
  }
}
</style>
