<template>
  <view class="list-content " v-if="tableData.length > 0">
    <view class="list pt15" v-for="(item, index) in tableData" :key="index">
      <view>
        <uni-row class="list-info " v-for="(val, valIndex) in info.showField || info.association_field" :key="valIndex"
          @click="goDetails(item)">
          <view class="title"
            v-if="val.field_name_en == (type == 'oneOnOne' ? info.index_field_name : info.crudInfo.main_field_name)">
            <text style="width: 80%" class="over-text">{{ item[val.field_name_en] || '--' }}</text>
            <template v-if="type !== 'oneOnOne'">
              <dean-popover ref="deanPopoverRef" model-direction="right" :btnList="data.btnList"
                @select="selectFn($event, item.id)">
                <template #icon>
                  <text class="iconfont icon-yunwenjian-gengduo"></text>
                </template>
              </dean-popover>
            </template>

            <!-- 选择一对一 -->
            <template v-else>
              <view class="iconfont icon-denglu-tongyi" @click="check(item)" v-if="data.activeData.id == item.id" />
              <view v-else class="iconfont icon-xuanzeanniu-weixuan" @click="check(item, index)" />
            </template>
          </view>
          <template
            v-if="val.field_name_en !== (type == 'oneOnOne' ? info.index_field_name : info.crudInfo.main_field_name)">
            <uni-col :span="7" :pull="1" class="text-right info-item-left">
              {{ val.field_name }}
            </uni-col>
            <uni-col :span="17" class="info-item-right">
              <view v-if="val.form_value === 'image'" class="imgFlex">
                <img class="img" :src="imgItem.url" alt="" v-for="(imgItem, index) in item[val.field_name_en]"
                  :key="index" @click="preview(imgItem)" />
              </view>
              <view v-else-if="val.form_value === 'tag'" class="imgFlex">
                <view v-for="(item, index) in item[val.field_name_en]" class="tag">{{ item }}</view>
              </view>
              <view v-else-if="val.form_value === 'switch'">
                <view>{{ item[val.field_name_en] == 1 ? '是' : '否' }}</view>
              </view>
              <view v-else-if="val.form_value == 'checkbox'">
                <view v-for="(el, index) in item[val.field_name_en]" class="dictionaries-tag over-text mr10" :style="{
                                    color: el.color ? el.color : '#1890ff',
                                    background: el.color ? getColorFn(el.color, '0.1') : getColorFn('#1890ff', '0.1')
                 }">
                  {{ el.name }}
                </view>

                <view v-if="item[val.field_name_en].length==0">--</view>
              </view>
              <!-- 关联字典颜色 -->
              <view v-else-if="item[val.field_name_en] &&Object.prototype.hasOwnProperty.call(item[val.field_name_en], 'color')
                                " class="dictionaries-tag over-text" :style="{
                                  color: item[val.field_name_en].color ? item[val.field_name_en].color : '#1890ff',
                                  background: item[val.field_name_en].color
                                    ? getColorFn(item[val.field_name_en].color, '0.1')
                                    : getColorFn('#1890ff', '0.1')
                                }">
                {{ item[val.field_name_en].name }}
              </view>
              <view v-else-if="data.users.includes(val.field_name_en) ">
                <view>{{item[val.field_name_en]}}</view>
              </view>
              <text v-else>
                {{ getValue(item[val.field_name_en], val.form_value) }}
              </text>
            </uni-col>
          </template>
        </uni-row>
      </view>
      <view class="footer" v-if="type !== 'oneOnOne'">
        <view class="left">
          <image class="img" :src="item.owner_user.avatar" mode="" />
          {{ item.owner_user.name }}
          负责
        </view>
        <view class="left">
          <text class="info-item-left mr8">创建于</text>
          <uni-dateformat format="yyyy/MM/dd" :date="item.created_at"></uni-dateformat>
        </view>
      </view>
    </view>
  </view>
  <empty v-else :index="7" :title="`暂无数据`"></empty>
</template>
<script setup>
  import { reactive } from 'vue'
  import { useStore } from 'vuex'
  import empty from '@/components/empty/index.vue'
  import deanPopover from '@/components/deanPopover/index.vue'
  const deanPopoverRef = ref(null)
  let store = useStore()
  const props = defineProps({
    tableData: {
      type: Array,
      default: [],
    },
    info: {
      type: Object,
      default: {},
    },
    type: {
      type: String,
      default: '',
    },
    keyName: {
      type: String,
      default: '',
    },
    tableName: {
      type: String,
      default: '',
    },
  })
  const { info, tableData, keyName, type, tableName } = toRefs(props)
  const data = reactive({
    ids: [],
    activeData: store.state.app.oneOnOneData,
    users: ['frame_id'],
    btnList: [{
        icon: 'iconfont icon-gongzuohuibao-bianji',
        type: 1,
        name: '编辑',
      },
      {
        icon: 'iconfont icon-shanchu',
        type: 2,
        name: '删除',
      },
    ],
  })
  // 数组转成字符串
  const getValue = (val, type) => {
    let str = ''
    if (val == '') {
      str = '--'
    } else if (type == 'input_select' && typeof val !== "string") {
      if (val.type) {
        str = val.name + val.type ? '（' + val.type + '）' : ''
      } else {
        str = val.name
      }
    } else if (Array.isArray(val)) {
      str = val.toString()
    } else {

      str = val
    }
    return str || '--'
  }
  const getColorFn = (thisColor, thisOpacity) => {

    var theColor = thisColor.toLowerCase()
    var r = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/
    if (theColor && r.test(theColor)) {
      if (theColor.length === 4) {
        var sColorNew = '#'
        for (var i = 1; i < 4; i += 1) {
          sColorNew += theColor.slice(i, i + 1).concat(theColor.slice(i, i + 1))
        }
        theColor = sColorNew
      }
      var sColorChange = []
      for (var i = 1; i < 7; i += 2) {
        sColorChange.push(parseInt('0x' + theColor.slice(i, i + 2)))
      }
      return 'rgba(' + sColorChange.join(',') + ',' + thisOpacity + ')'
    }

  }

  import { clickNavigateTo } from '@/utils/helper'

  const goDetails = (val) => {
    if (type.value == 'oneOnOne' || type.value == 'detail') return false
    let name = val[info.value.crudInfo.main_field_name] || '查看'
    let key = tableName.value ? tableName.value : keyName.value
    clickNavigateTo(`/pages/module/details?id=${val.id}&key=${key}&&name=${name}`)
  }

  let emit = defineEmits(['selectFn'])
  // 删除
  const selectFn = (e, row) => {
    emit('selectFn', e, row)
  }
  const check = (val, index) => {
    let obj = {
      id: val.id,
      name: val[info.value.index_field_name],
    }
    data.activeData = obj
    store.commit('setoneOnOneData', obj)
    uni.navigateBack()
  }
</script>

<style lang="scss" scoped>
  .imgFlex {
    display: flex;
    flex-wrap: wrap;
  }

  .pt15 {
    padding-top: 15px;
  }

  .tag {
    padding: 0 12rpx;
    height: 48rpx;
    line-height: 50rpx;
    border: 1rpx solid #1890ff;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 26rpx;
    color: #1890ff;
    border-radius: 4rpx;
    margin-right: 16rpx;
    margin-bottom: 10rpx;
  }

  .dictionaries-tag {
    max-width: 100px;
    display: inline-block;
    margin: 0;
    box-sizing: border-box;
    height: 24px;
    padding: 0 8px;
    text-align: center;
    line-height: 24px;
    font-size: 12px;
    border-radius: 3px;
    margin-right: 10rpx;
  }

  .img {
    display: block;
    width: 92rpx;
    height: 92rpx;
    border-radius: 8rpx;
    margin-right: 10rpx;
    margin-bottom: 10rpx;
  }

  .list-content {
    margin-top: 16rpx;
    padding: 0 20rpx;

    .footer {
      height: 48px;
      width: 100%;
      padding: 0 24rpx;
      border-top: 2rpx solid #ebeef5;
      font-size: 28rpx;
    }

    .list {
      // padding-top: 12rpx;
      width: 100%;
      background-color: #ffffff;
      border-radius: 16rpx;
      margin-bottom: 20rpx;

      .icon-yunwenjian-gengduo {
        color: #909399;
      }

      .list-info {
        padding: 0 12px 12px;
      }

      .info-item-left {
        font-family: PingFang SC, PingFang SC;
        font-weight: 400;
        font-size: 28rpx;
        color: #909399;
      }

      .info-item-right {
        padding-left: 20rpx !important;
        font-family: PingFang SC, PingFang SC;
        font-weight: 400;
        font-size: 28rpx;
        color: #303133;
      }

      .footer {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .left {
          display: flex;
          align-items: center;
        }

        .mr8 {
          margin-right: 8rpx;
        }

        .img {
          width: 24px;
          height: 24px;
          border-radius: 50%;
          border: 2rpx solid #ebeef5;
          margin-right: 16rpx;
          margin-bottom: 0;
        }
      }

      .title {
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-family: PingFang SC, PingFang SC;
        font-weight: 500;
        font-size: 32rpx;
        color: #303133;

        margin-bottom: 3px;
      }
    }
  }

  .icon-denglu-tongyi {
    color: #1890ff;
    font-size: 18px;
  }

  .icon-xuanzeanniu-weixuan {
    font-size: 18px;
  }
</style>