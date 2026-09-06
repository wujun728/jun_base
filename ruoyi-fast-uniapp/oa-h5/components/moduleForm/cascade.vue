<template>
  <view>
    <uni-popup ref="popupRef" type="bottom" :mask-click="false">
      <view class="slider">
        <view class="share-header">
          <view class="share-title">{{ title }}</view>
          <view @click="cancel" class="iconfont icon-shenpizhongxin-jujue"></view>
        </view>
        <!-- 内容 -->
        <view class="classify rd-t-40rpx">
          <scroll-view class="scroll-view_H" scroll-x="true" show-scrollbar="false">
            <view class="list">
              <view class="item" :style="{width:data.width+'px' }">
                <view class="tips">一级分类</view>
                <scroll-view scroll-y="true" class="scroll-Y">
                  <view class="itemn line1" :class="{
                    checked: item.checkbox,
                  }" v-for="(item, index) in listData" :key="index" @click="categoryTap(index)">
                    <checkbox-group @change="checkboxChange($event, item, index)">
                      <!-- #ifndef MP -->
                      <checkbox :value="item.value.toString()" :checked="item.checkbox" style="transform: scale(0.9)" />
                      <!-- #endif -->
                      <!-- #ifdef MP -->
                      <checkbox :value="item.value" :checked="item.checkbox" style="transform: scale(0.9)" />
                      <!-- #endif -->
                    </checkbox-group>
                    <text>{{ item.name }}</text>
                  </view>
                </scroll-view>
              </view>
              <!-- 二级菜单 -->
              <view class="item on" v-if="listData[data.currentOne].children" :style="{width:data.width+'px' }">
                <view class="tips">二级分类 </view>
                <scroll-view scroll-y="true" class="scroll-Y">
                  <view class="itemn line1" :class="{checked: j.checkbox}"
                    v-for="(j, indexJ) in listData[data.currentOne].children" :key="indexJ"
                    @click="categoryTwoTap(indexJ)">
                    <checkbox-group @change="checkboxChange($event, j, indexJ)">
                      <!-- #ifndef MP -->
                      <checkbox :value="j.value.toString()" :checked="j.checkbox" style="transform: scale(0.9)" />
                      <!-- #endif -->
                      <!-- #ifdef MP -->
                      <checkbox :value="j.value" :checked="j.checkbox" style="transform: scale(0.9)" />
                      <!-- #endif -->
                    </checkbox-group>
                    <text class="over-text">{{ j.name }}</text>
                  </view>
                </scroll-view>
              </view>
              <!-- 三级菜单 -->
              <view class="item on2" :style="{width:data.width+'px' }"
                v-if="listData[data.currentOne].children&&listData[data.currentOne].children[data.currentTwo]&&listData[data.currentOne].children[data.currentTwo].hasOwnProperty('children')">
                <view class="tips on2">三级分类</view>
                <scroll-view scroll-y="true" class="scroll-Y">
                  <view class="itemn line1" :class="{checked: x.checkbox
                  }" v-for="(x, indexX) in listData[data.currentOne].children[data.currentTwo].children" :key="indexX"
                    @click="categoryThreeTap(indexX)">
                    <checkbox-group @change="checkboxChange($event, x, indexX)">
                      <!-- #ifndef MP -->
                      <checkbox :value="x.value.toString()" :checked="x.checkbox" style="transform: scale(0.9)" />
                      <!-- #endif -->
                      <!-- #ifdef MP -->
                      <checkbox :value="x.value" :checked="x.checkbox" style="transform: scale(0.9)" />
                      <!-- #endif -->
                    </checkbox-group>
                    <text>{{ x.name }}</text>
                  </view>
                </scroll-view>
              </view>

              <view class="item on3" :style="{width:data.width+'px' }"
                v-if="listData[data.currentOne].children[data.currentTwo]&&listData[data.currentOne].children[data.currentTwo].children&&listData[data.currentOne].children[data.currentTwo].children[data.currentThree]&&listData[data.currentOne].children[data.currentTwo].children[data.currentThree].hasOwnProperty('children')">
                <view class="tips on2">四级分类</view>
                <scroll-view scroll-y="true" class="scroll-Y">
                  <view class="itemn line1" :class="{checked: x.checkbox }"
                    v-for="(x, indexX) in listData[data.currentOne].children[data.currentTwo].children[data.currentThree].children"
                    :key="indexX" @click="categoryThreeTap(indexX)">
                    <checkbox-group @change="checkboxChange($event, x, indexX)">
                      <!-- #ifndef MP -->
                      <checkbox :value="x.value.toString()" :checked="x.checkbox" style="transform: scale(0.9)" />
                      <!-- #endif -->
                      <!-- #ifdef MP -->
                      <checkbox :value="x.value" :checked="x.checkbox" style="transform: scale(0.9)" />
                      <!-- #endif -->
                    </checkbox-group>
                    <text class="over-text">{{ x.name }}</text>
                  </view>
                </scroll-view>
              </view>

            </view>
          </scroll-view>


          <view class="slider-laber-bottom">
            <button class="reset laber-bottom" @click="reset">重置</button>
            <button class="laber-bottom confirm" @click="confirm">确认</button>
          </view>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
  import { reactive, computed } from "vue"
  import message from '@/utils/message'
  const props = defineProps({
    listData: {
      type: Array,
      default: () => {
        return []
      },
    },
    title: {
      type: String,
      default: '',
    },
  })
  const { title, listData } = toRefs(props)
  const emit = defineEmits(['submitOk'])
  const popupRef = ref(null)
  const data = reactive({
    currentOne: 0,
    currentTwo: 0,
    currentThree: 0,
    checkedIds: [],
    width: 125,
    num: 0,
  })


  // 打开弹出
  const popupOpen = (val, name) => {
    setTimeout(() => {
      getNum()
      uni.getSystemInfo({
        success: function(res) {
          if (data.num == 4) {
            data.width = 125
          } else {
            data.width = res.windowWidth / data.num
          }
        }
      })
    }, 300)

    popupRef.value.open()

    if (val) {
      data.checkedIds = val
    }
  }
  onMounted((e) => {

  })
  const getNum = () => {
    if (listData.value[0]) {
      data.num = 1
    }
    if (listData.value[0].children) {
      data.num = 2
    }
    if (listData.value[0].children[0].children) {
      data.num = 3
    }
    if (listData.value[0].children[0].children && listData.value[0].children[0].children[0].children) {
      data.num = 4
    }
  }

  const checkboxChange = (e, item, index) => {
    let defaultids = e.detail.value
    item.checkbox = !!defaultids.length
    //选中当前所有下级
    selectChildrenAll(item.children, item.checkbox)
    emit('change', item)
  }

  const selectChildrenAll = (children, checkbox) => {
    if (children) {
      children.map((item) => {
        item.checkbox = checkbox
        if (item.children && item.children.length) {
          selectChildrenAll(item.children, checkbox)
        }
      })
    }
  }

  const categoryTap = (index) => {
    data.currentOne = index
    data.currentTwo = 0
    data.currentThree = 0
  }

  const categoryTwoTap = (index) => {
    data.currentTwo = index
  }

  const categoryThreeTap = (index) => {
    data.currentThree = index
  }

  // 关闭
  const cancel = () => {
    popupRef.value.close()
  }

  // 标签重置
  const reset = () => {
    emit('reset')
  }

  // 标签确认
  const confirm = () => {
    const name = extractCheckboxValues(listData.value)

    if (!name.value.length) {
      return
    }
    let names = []
    name.names.map((item) => {
      names.push(item.join('/'))
    })

    emit('submitOk', {
      value: name.value,
      name: names.join(','),
    })

    cancel()
  }

  const extractCheckboxValues = (data, path = [], pathvalue = []) => {
    let result = []
    let resultName = []

    for (let i = 0; i < data.length; i++) {
      const item = data[i]

      if (item.children && item.children.length > 0) {
        // 递归处理有子节点的情况
        const childPath = [...path, item.value]
        const childPathName = [...pathvalue, item.name]
        const childValues = extractCheckboxValues(item.children, childPath, childPathName)
        result = result.concat(childValues.value)
        resultName = resultName.concat(childValues.names)
      } else if (item.checkbox === true) {
        // 只提取没有子节点且 checkbox 属性为 true 的数据
        result.push([...path, item.value])
        resultName.push([...pathvalue, item.name])
      }
    }

    return { value: result, names: resultName }
  }

  const findNamesByIds = (tree, ids) => {
    let result = []

    function traverse(node) {
      if (ids.includes(node.value)) {
        result.push(node.name)
      }
      if (node.children) {
        for (const child of node.children) {
          traverse(child)
        }
      }
    }
    for (const node of tree) {
      traverse(node)
    }
    return result
  }

  defineExpose({
    popupOpen,
  })
</script>

<style lang="scss" scoped>
  ::v-deep .uni-popup {
    z-index: 100;
  }


  ::v-deep .uni-checkbox-input {
    width: 28rpx;
    height: 28rpx;
  }

  ::v-deep .uni-scroll-view ::-webkit-scrollbar {
    display: none;
  }

  .over-text {
    width: 100%;
    display: inline-block;
    text-overflow: ellipsis;
    overflow: hidden;
    word-break: break-all;
  }

  .slider {
    position: relative;
    width: 100%;
    background-color: #fff;
    border-radius: 20rpx 20rpx 0 0;

    .share-header {
      width: 100%;
      height: 104rpx;
      position: relative;

      .image {
        width: 100%;
        height: 100%;
      }

      .share-title {
        position: absolute;
        top: 38rpx;
        left: 50%;
        transform: translate(-50%, 0);
        font-family: PingFang SC, PingFang SC;
        font-weight: 500;
        font-size: 15px;
        color: $uni-text-color;
      }

      .iconfont {
        font-size: 32rpx;
        color: #c0c4cc;
        position: absolute;
        top: 30rpx;
        right: 32rpx;
      }
    }

    .slider-laber-bottom {
      position: absolute;
      display: flex;
      bottom: 30rpx;
      height: 86rpx;
      left: 30rpx;
      right: 30rpx;
      align-items: center;
      // background-color: #fff;

      .laber-bottom {
        height: 100%;
        border-radius: 8rpx;
        border: none;
        font-size: $uni-font-size-default;
        line-height: 86rpx;

        &::after {
          border: 0;
        }
      }

      .reset {
        width: 184rpx;
        margin-right: 30rpx;
        background: #f0f1f5;
        color: $uni-text-color;
      }

      .confirm {
        width: calc(100% - 214rpx);
        background: #308bf8;
        color: #fff;
      }
    }
  }

  .classify {
    background-color: #fff;
    padding-bottom: 60rpx;

    .list {
      display: flex;
      height: 770rpx;
      margin-bottom: 40px;

      .scroll-Y {
        height: 784rpx;
      }

      .item {
        flex-shrink: 0;
        padding: 22rpx 32rpx 0 32rpx;
        font-size: 24rpx;
        font-family: PingFang SC, PingFang SC;
        font-weight: 400;
        color: #666666;


        .tips {
          font-family: PingFang SC, PingFang SC;
          font-weight: 400;
          font-size: 24rpx;
          color: #999999;
          margin-bottom: 30rpx;
        }

        .itemn {
          margin-bottom: 58rpx;
          display: flex;
          align-items: center;

          &.checked {
            color: #1890FF;
            // ::v-deep .uni-checkbox-input {
            //   background-color: #1890FF;
            // }

            // ::v-deep .uni-checkbox-input svg {
            //   fill: #fff !important;
            //   font-size: 12px;
            // }
          }

          &.on {
            color: #2a7efb;
          }
        }

        &.on {
          background-color: #fafafa;
        }

        &.on2 {
          background-color: #f5f5f5 !important;
        }

        &.on3 {
          background-color: #f0f0f0 !important;
        }
      }
    }

    .footer {
      box-sizing: border-box;
      padding: 0 20rpx;
      width: 100%;
      height: 112rpx;
      background-color: #fff;
      position: fixed;
      bottom: 0;
      z-index: 30;
      height: calc(112rpx + constant(safe-area-inset-bottom)); ///兼容 IOS<11.2/
      height: calc(112rpx + env(safe-area-inset-bottom)); ///兼容 IOS>11.2/
      padding-bottom: constant(safe-area-inset-bottom); ///兼容 IOS<11.2/
      padding-bottom: env(safe-area-inset-bottom); ///兼容 IOS>11.2/
      left: 0;

      .bnt {
        width: 347rpx;
        height: 72rpx;
        border-radius: 50rpx;
        border: 1px solid #2a7efb;
        color: #2a7efb;
        font-size: 26rpx;
        font-family: PingFang SC, PingFang SC;
        font-weight: 500;

        &.on {
          background-color: #2a7efb;
          color: #fff;
        }
      }
    }
  }
</style>