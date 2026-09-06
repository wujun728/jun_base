<template>
  <view>
    <view class="compos">
      <view class="base-btn" :style="{ zIndex: data.show ? 22 : 0 }" @tap.stop="open($event)">
        <slot name="icon"></slot>
      </view>
      <view v-show="data.show" class="compos-content" @click="close"></view>
      <view
        class="modal"
        :style="{
          overflow: data.show ? '' : 'hidden',
          top: data.top,
          [modelDirection]: data.modalLeft,
        }"
        v-show="data.show"
      >
        <view
          class="modal-ang"
          :style="{
            [direction]: '10px',
            top: data.iTop,
          }"
          :class="data.iPosition === 'top' ? 'top' : 'bottom'"
        ></view>
        <view class="modal-content">
          <slot>
            <view class="modal-item" v-for="(item, index) in btnList" :index="index" :key="index" @tap.stop="callRes(item)">
              <text class="iconfont" :class="item.icon"></text>
              {{ item.name || item.menu_name }}
            </view>
          </slot>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
/**
 * :btnList				按钮列表
 * :modalWidth			弹出层宽度
 * :direction			弹出层箭头位置 left right
 * :modelDirection	    弹出层默认方向 left right
 * @select				选中列表触发事件
 * */
import { reactive, toRefs, getCurrentInstance, nextTick } from 'vue'
import type { PropType } from '@/utils/typeHelper'

const props = withDefaults(
  defineProps<{
    btnList?: Array<any>
    direction?: string
    modelDirection?: string
    id?: number
    index?: number
  }>(),
  {
    btnList: <any>[],
    direction: 'right',
    modelDirection: 'left',
    id: 0,
    index: -1,
  }
)
const { btnList, direction, modelDirection, id, index } = toRefs(props)

const data = reactive({
  show: false,
  dotShow: false,
  height: 0,
  width: 0,
  top: <string | number>0,
  modalLeft: <string | number>0,
  iTop: '-7px',
  iPosition: 'top',
})
// --end-- {

const close = () => {
  data.show = false
  data.dotShow = false
}

const open = (e: Event) => {
  data.show = !data.show
  getCustomerHeight(e)
}

const instance = getCurrentInstance()
const getCustomerHeight = (e: PropType) => {
  const { windowHeight } = uni.getSystemInfoSync()
  const x = e.detail.x
  const y = e.detail.y

  nextTick(() => {
    let query = uni.createSelectorQuery().in(instance)
    query.select('.modal').fields({ size: true }, () => {})
    query.select('.base-btn').fields({ size: true }, () => {})
    query.exec((res) => {
      // const width:number = res[0].width
      const height: number = res[0].height
      const width1: number = res[1].width
      const height1: number = res[1].height

      data.modalLeft = -width1 / 2 + 'px'
      if (modelDirection.value === 'left') {
        data.modalLeft = -140 + 'px'
      }
      if (windowHeight < y + height) {
        data.top = -(height + 6) + 'px'
        data.iTop = height + 'px'
        data.iPosition = 'bottom'
      } else {
        data.top = height1 + 6 + 'px'
        data.iPosition = 'top'
      }
    })
  })
}

let emit = defineEmits(['select'])
const callRes = (e: any) => {
  e.id = id.value
  e.index = index.value
  data.show = false
  data.dotShow = false
  emit('select', e)
}

defineExpose({ close })
</script>

<style lang="scss" scoped>
.compos-content {
  width: 100vw;
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 100;
}

.compos {
  position: relative;

  .modal {
    background: #ffffff;
    box-shadow: 0px 0px 8px 0px rgba(0, 0, 0, 0.08);
    position: absolute;
    border-radius: 12rpx;
    transition: height ease-out 100ms;
    z-index: 999;
    border: 1px solid $uni-line-style-color-three;
    // opacity: 0;

    .modal-content {
      min-width: 200rpx;
      display: flex;
      flex-direction: column;
      padding: 32rpx 32rpx;

      ::v-deep .modal-item {
        width: 100%;
        z-index: 99;
        color: #333333;
        text-align: left;
        font-size: 28rpx;
        margin-bottom: 44rpx;
        display: flex;
        align-items: center;
        font-weight: normal;
        // justify-content: center;

        &:last-of-type {
          margin-bottom: 0;
        }

        .iconfont {
          font-size: 30rpx;
          color: $nui-text-color-four;
          margin-right: 12rpx;
        }
      }
    }

    .modal-ang {
      width: 0px;
      height: 0px;
      position: absolute;
      top: -7px;
      border-color: transparent;
      border-style: solid;
      border-width: 6px;
      filter: drop-shadow(0 2px 12px rgba(0, 0, 0, 0.16));
      z-index: 2;

      &::after {
        content: '';
        position: absolute;
        display: block;
        width: 0;
        height: 0;
        border-color: transparent;
        border-style: solid;
        border-width: 6px;

        margin-left: -6px;
      }

      &.top {
        border-top-width: 0;
        border-bottom-color: $uni-line-style-color-three;

        &::after {
          top: 1px;
          border-top-width: 0;
          border-bottom-color: #fff;
        }
      }

      &.bottom {
        border-top-color: $uni-line-style-color-three;
        border-bottom-width: 0;

        &::after {
          bottom: 2px;
          border-top-color: #fff;
          border-bottom-width: 0;
        }
      }
    }
  }
}

.base-btn {
  position: relative;
  border: 0upx;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  z-index: 22;
}
</style>
