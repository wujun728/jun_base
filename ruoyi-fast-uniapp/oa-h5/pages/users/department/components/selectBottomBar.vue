<template>
  <view class="">
    <view class="select-content">
      <view class="plr10">
        <uni-row>
          <uni-col :span="18">
            <view class="select-content-left">
              <view class="top" @click="handleCheckPer">
                已选择 {{ selectPersonnel.length }} {{showPerson ? '人' : '部门'}}
                <uni-icons type="top"></uni-icons>
              </view>
              <view class="bottom line1">

                <text v-for="(item,index) in selectPersonnel"
                  :key="item.id">{{ item.name }}{{ selectPersonnel.length -1 === index ? '' : '、' }}</text>
              </view>
            </view>
          </uni-col>
          <uni-col :span="6" class="select-content-right text-center">
            <button type="primary" size="mini" @click="clickOk">确定</button>
          </uni-col>
        </uni-row>
      </view>
    </view>
    <chenk-personnel ref="chenkPersonnelRef" :show-person="showPerson" :is-checked="isChecked"
      :checkPer="selectPersonnel"></chenk-personnel>
  </view>
</template>

<script setup>
  import chenkPersonnel from './chenkPersonnel.vue'
  import { ref, toRefs, reactive, watch, computed } from 'vue'
  import { clickNavigateTo } from '@/utils/helper'
  import { useStore } from 'vuex'
  import message from '@/utils/message'
  const props = defineProps({
    isChecked: {
      type: Number,
      default: 0
    },
    showPerson: {
      type: Boolean,
      default: true
    }
  })
  const { isChecked, showPerson } = toRefs(props)

  const chenkPersonnelRef = ref(null)
  const selectPersonnel = ref([])
  const store = useStore()
  let emit = defineEmits(["handleOk"])

  const getSelectPeople = computed(() => {
    //返回的是ref对象
    return store.state.app.depSelectPeople
  })

  // 查看选中的人员
  const handleCheckPer = () => {
    chenkPersonnelRef.value.popupOpen()
  }

  const clickOk = () => {
    if (getSelectPeople.value.length > 0) {
      emit('handleOk')
    } else {
      message.error(`至少选择一个${showPerson ? '人' : '部门'}`)
    }
  }

  // 数据监听
  watch(getSelectPeople, (newvalue, oldvalue) => {
    selectPersonnel.value = newvalue
  }, { immediate: true, deep: true });
</script>

<style lang="scss" scoped>
  .select-content {
    position: fixed;
    left: 0;
    bottom: 0;
    right: 0;
    width: 100%;
    height: 124rpx;
    background-color: #fff;
    box-shadow: 0px 0px 0 0px #D7D7D7,
      -6px 0px 6px 0px rgba(0, 0, 0, 0.1),
      6px 0px 6px 0px rgba(0, 0, 0, 0.1),
      0px 5px 6px 0px rgba(0, 0, 0, 0.1);

    .select-content-left {
      padding: 28rpx 0;

      .top {
        color: #308BF8;
        font-size: 26rpx;

        uni-text {
          color: #308BF8 !important;
          font-size: 26rpx !important;
        }
      }

      .bottom {
        margin-top: 10rpx;
        color: #909399;
        font-size: 24rpx;

        uni-text {
          display: inline-block;
        }
      }
    }

    .select-content-right {
      padding: 22rpx 0 22rpx 22rpx !important;

      uni-button {
        width: 100%;
        height: 80rpx;
        font-size: 28rpx;
        line-height: 80rpx;
      }
    }
  }
</style>