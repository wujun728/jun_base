<template>
  <view class="popup" v-show="show">
    <view class="bg"></view>
    <view class="selectMultiple">
      <view class="multipleBody">
        <view class="title">
          <view class="close" @tap="cancelMultiple"> 取消 </view>
          <view class="name">
            {{ data.title }}
          </view>
          <view class="confirm " @tap="confirmMultiple">

            确定 </view>
        </view>
        <view class="list">
          <view class="mask mask-top"></view>
          <view class="mask mask-bottom"></view>
          <scroll-view class="diet-list" scroll-y="true">

            <view v-for="(item, index) in columns" class="check-flex" :class="['item', item.selected ? 'checked' : '']"
              @tap="onChange(index, item)">
              <view>{{ item.text || item.name }}</view>
              <view class="icon iconfont icon-a-zu9739-01" v-if="item.selected" />
              <view class="icon iconfont icon-a-juxing8761biankuang" v-else />
            </view>
          </scroll-view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
  import { ref, reactive, toRefs, onMounted } from 'vue'
  const props = defineProps({
    show: {
      type: Boolean,
      default: false,
    },
    // 标题
    title: {
      type: String,
      default: '',
    },
    //数据列表
    columns: {
      type: Array,
      default: [],
    },
    // 默认选中
    defaultIndex: {
      type: Array,
      default: [],
    },
  })
  const { show, title, columns, defaultIndex } = toRefs(props)
  const data = reactive({
    value: [],
    // 选中列表
    selected: [],
    // 列表数据
    list: [],
    // 出场动画
    animationData: {},
  })
  let emit = defineEmits(['cancel', 'confirm', 'change'])
  watch(
    () => [show.value],
    (newvalue) => {
      if (newvalue[0]) {
        data.value = defaultIndex.value
        columns.value.forEach((item, index) => {
          item.selected = false
          if (data.value.indexOf(item.value.toString()) >= 0) {
            item.selected = true
          }
        })
      }

    }, {
      deep: true,
      immediate: true,
    }
  )

  // 列点击事件
  const onChange = (index, item) => {
    // 是否已选中
    if (data.value.indexOf(item.value.toString()) >= 0) {
      columns.value[index].selected = false
    } else {
      columns.value[index].selected = true
    }
    // 筛选已勾选数据
    data.value = []
    data.selected = []
    columns.value.forEach((col_item, col_index) => {
      if (col_item.selected) {
        data.value.push(col_item.value.toString())
        data.selected.push({
          text: col_item.text || col_item.name,
          value: col_item.value,
        })
      }
    })
  }
  // 弹出框开启触发事件
  const openMultiple = () => {
    // 初始化列表数据,默认勾选数据

    data.value = defaultIndex.value
    columns.value.forEach((item, index) => {
      item.selected = false
      if (data.value.indexOf(item.value.toString()) >= 0) {
        item.selected = true
      }
    })

    // 弹出动画
    // openAnimation();
  }

  // 确认
  const confirmMultiple = () => {
    emit('change', { selected: data.selected, value: data.value })
  }
  // 关闭/取消
  const cancelMultiple = () => {
    emit('cancel')
  }

  // 展开动画
  const openAnimation = () => {
    var animation = uni.createAnimation()
    animation.translate(0, 300).step({ duration: 0 })
    data.animationData = animation.export()
    // setTimeOut(() => {
    animation.translate(0, 0).step({ duration: 300, timingFunction: 'ease' })
    data.animationData = animation.export()
    // }, 200)
  }
</script>

<style scoped lang="scss">
  .popup {
    width: 100%;
    height: 100vh;
    position: fixed;
    z-index: 99999;
    left: 0;
    bottom: 0;

    .bg {
      width: 100%;
      height: 100%;
      background-color: rgba(black, 0.2);
    }
  }

  .selectMultiple {
    width: 100%;
    position: absolute;
    left: 0;
    bottom: 0;
    background-color: white;

    .multipleBody {
      width: 100%;
      padding: 30rpx;
      box-sizing: border-box;
      padding-bottom: 80rpx;

      .title {
        font-size: 28rpx;
        display: flex;
        flex-direction: row;

        .close {
          width: 80rpx;
          opacity: 0.5;
          z-index: 100;
        }

        .name {
          width: 530rpx;
          text-align: center;
          overflow: hidden;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 1;
        }

        .confirm {
          width: 80rpx;
          text-align: right;
          color: #2d8dff;
          z-index: 100;
        }
      }

      .check-flex {
        display: flex;
      }

      .list {
        width: 100%;
        padding-top: 30rpx;
        position: relative;

        .mask {
          width: 100%;
          height: 120rpx;
          position: absolute;
          left: 0;
          z-index: 2;
          pointer-events: none;

          &.mask-top {
            top: 30rpx;
            background-image: linear-gradient(to bottom, #fff, rgba(#fff, 0));
          }

          &.mask-bottom {
            bottom: 0;
            background-image: linear-gradient(to bottom, rgba(#fff, 0), #fff);
          }
        }

        .diet-list {
          max-height: 400rpx;
        }

        .item {
          position: relative;
          width: 100%;
          line-height: 40rpx;
          border-bottom: 1px solid rgba($color: #000000, $alpha: 0.05);
          padding: 20rpx 0;
          font-size: 30rpx;
          box-sizing: border-box;
          text-align: center;

          span {
            overflow: hidden;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 1;
            padding: 0 40rpx;
          }

          .icon {
            position: absolute;
            right: 10rpx;
            top: 50%;
            transform: translateY(-50%);
            height: 16px;
          }

          &.checked {
            color: #2d8dff;
          }

          &:last-child {
            border-bottom: none;
            margin-bottom: 60rpx;
          }

          &:first-child {
            margin-top: 60rpx;
          }
        }
      }
    }
  }

  .icon-a-juxing8761biankuang {
    color: #D8D8D8
  }
</style>