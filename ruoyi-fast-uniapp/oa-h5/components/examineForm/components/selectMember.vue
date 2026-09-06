<template>
  <uni-forms-item class="input-label">
    <template v-slot:label>
      <view class="uni-forms-item__label">
        <text v-if="configData.effect && configData.effect.required" class="iconfont">
          *
        </text>
        <text class="label-item" v-if="type !== 'module'">
          {{ configData.title }}
        </text>
        <text class="label-item" v-else>
          {{ configData.field_name }}
        </text>
      </view>
    </template>
    <view class="input-right-conment" @click="addMember">
      <template v-if="type == 'module'">
        <view class="picker-input picker-input-placeholder" v-if="
            !configData.data_dict ||
            (configData.data_dict && configData.data_dict.length <= 0)
          ">
          请选择
          <view class="iconfont icon-fanhui"></view>
        </view>
        <view class="picker-input" v-else>
          <view class="user-work-item" v-for="(item, index) in configData.data_dict" :key="index">
            <avatar v-if="item" :src="item.avatar" :radius="4"></avatar>
            <view class="user-name">{{item.name}}</view>
          </view>
        </view>
      </template>
      <template v-else>
        <view class="picker-input picker-input-placeholder" v-if="
            !configData.value ||
            (configData.value && configData.value.length <= 0)
          ">
          请选择
          <view class="iconfont icon-fanhui"></view>
        </view>
        <view class="picker-input" v-else>
          <view class="user-work-item" v-for="(item, index) in configData.value" :key="index">
            <avatar v-if="item" :src="item.avatar" :radius="4"></avatar>
          </view>
        </view>
      </template>
    </view>
  </uni-forms-item>
</template>

<script setup>
  import { ref, reactive, toRefs, computed, watch } from 'vue'
  import avatar from '@/components/avatar/index'
  import message from "@/utils/message"
  import { useStore } from 'vuex'
  import { getIdsFromArray } from '@/utils/helper'
  import {
    navigateToDepartment,
    resetSelectDepartment,
    resetExamineIndex,
  } from '@/utils/autoload'

  const store = useStore()
  const props = defineProps({
    configData: {
      type: Object,
      default: () => {
        return {}
      },
    },
    type: {
      type: String,
      default: '',
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    index: {
      type: Number,
      default: -1,
    },
  })
  const { configData, index, type, disabled } = toRefs(props)
  // 选中人的列表
  const getSelectPeople = computed(() => {
    return store.state.app.depSelectPeople
  })

  const emit = defineEmits(['change'])
  const data = reactive({
    showPerson: false,
    onlyOneself: false,
    mode: 'selector',
  })

  watch(
    getSelectPeople,
    (newvalue) => {
      emit('change', newvalue)
    }, {
      deep: true,
    },
  )

  //添加成员
  const addMember = () => {
    if (disabled.value) {
      message.error('禁止手动修改数据')
      return false
    }
    resetExamineIndex()
    const config = configData.value
    store.commit('setProcess', false)
    store.commit('setDepSelectIndex', index.value)
    // 判断选择成员
    if (config._fc_drag_tag === 'memberTree') {
      if (config.props.range.includes('oneself')) {
        data.onlyOneself = true
      }
      if (config.props.range.includes('many')) {
        data.mode = 'multiSelector'
      }
      data.showPerson = true
    }

    if (
      typeof configData.value.value === 'object' &&
      configData.value.value.length > 0
    ) {
      const ids = getIdsFromArray(configData.value.value)
      resetSelectDepartment(configData.value.value, ids)
    } else {
      resetSelectDepartment()
    }
    if (type.value === 'module') {
      data.showPerson = true
      data.onlyOneself = true
      if (
        typeof configData.value.data_dict === 'object' &&
        configData.value.data_dict.length > 0
      ) {
        const ids = getIdsFromArray(configData.value.data_dict)
        resetSelectDepartment(configData.value.data_dict, ids)
      } else {
        resetSelectDepartment()
      }
    }

    const query =
      `isShow=true&isFirst=1&isSelect=1&mode=${data.mode}&onlyOneself=${data.onlyOneself}&showPerson=${data.showPerson}`
    navigateToDepartment(query, 'pages/users/examine/default')
  }
</script>

<style scoped lang="scss">
  .input-label {
    align-items: center;

    ::v-deep .uni-easyinput__content-input {
      text-align: right;
      padding-right: 0 !important;
    }

    ::v-deep .uni-forms-item__label {
      width: 148rpx;
      display: flex;
      line-height: 1.2;

      .label-item {
        width: calc(100% - 16rpx);
      }

      .iconfont {
        width: 16rpx;
      }
    }

    .input-right-conment {
      min-height: 35px;
      display: flex;
      align-items: center;
      justify-content: flex-end;
      color: $uni-text-color;

      .picker-input {
        text-align: right;
        height: 100%;
        color: $uni-text-color;
        font-size: 30rpx;
        align-items: center;
        display: flex;
        justify-content: flex-end;
        flex-wrap: wrap;
        color: #c0c4cc;

        .iconfont {
          padding-right: 16rpx;
          margin-top: 7rpx;
          transform: rotate(180deg);
          font-size: 24rpx;
          color: #c0c4cc;
        }

        .user-work-item {
          width: 70rpx;
          height: 70rpx;
          margin: 14rpx 16rpx 0 0;

          .user-name {
            font-size: 22rpx;
            text-align: center;
          }
        }
      }
    }
  }
</style>