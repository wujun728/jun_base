<template>
  <view class="content">
    <steps v-if="followList.length > 0" direction="column" :options="followList" ref="stepsRef" :active="0"
      @editFollow="editFollow"> </steps>
    <empty v-else :index="7" :title="emptyTitle"></empty>
  </view>
</template>

<script setup>
  import empty from '@/components/empty/index.vue'
  import steps from './followSteps.vue'
  import { reactive, ref, toRefs } from 'vue'
  import { followDeleteApi } from '@/api/customer'
  import message from '@/utils/message'
  const props = defineProps({
    followList: {
      type: Array,
      default () {
        return [{}, {}, {}, {}, {}]
      },
      emptyTitle: {
        type: String,
        default: '暂无跟进记录，快去写记录吧！'
      },
    },
  })
  const { followList, emptyTitle } = toRefs(props)
  const data = reactive({})
  import { clickNavigateTo } from '@/utils/helper'
  const emit = defineEmits(['getfollowList'])
  const stepsRef = ref(null)
  const editFollow = (type, val) => {
    if (type === 3) {
      emit('getfollowList', val.eid)
    }
    if (type === 1) {
      let typeData = 0
      if (val.types == 1) {
        typeData = 2
      } else {
        typeData = 1
      }
      clickNavigateTo(`/pages/customer/list/addFollow?eid=${val.eid}&type=${typeData}&data=${JSON.stringify(val)}`)
    }
  }
</script>

<style lang="scss">
  .content {
    width: 100%;
    margin-top: 48rpx;
  }
</style>