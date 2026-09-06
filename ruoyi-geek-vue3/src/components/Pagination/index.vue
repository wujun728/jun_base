<script setup lang="ts">
import { scrollTo } from '@/utils/scroll-to'
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  total: number
  pageSizes?: number[]
  pagerCount?: number
  layout?: string
  background?: boolean
  autoScroll?: boolean
  hidden?: boolean
}>(), {
  pageSizes: () => [10, 20, 30, 50],
  pagerCount: document.body.clientWidth < 992 ? 5 : 7,
  layout: 'total, sizes, prev, pager, next, jumper',
  background: true,
  autoScroll: true,
  hidden: false
})

const emit = defineEmits();

const pageSize = defineModel<number>('limit', { default: 20 })
const currentPage = defineModel<number>('page', { default: 1 })
function handleSizeChange(val: number) {
  if (currentPage.value * val > +props.total) {
    currentPage.value = 1
  }
  emit('pagination', { page: currentPage.value, limit: val })
  if (props.autoScroll) {
    scrollTo(0, 800)
  }
}
function handleCurrentChange(val: number) {
  emit('pagination', { page: val, limit: pageSize.value })
  if (props.autoScroll) {
    scrollTo(0, 800)
  }
}

</script>
<template>
  <div :class="{ 'hidden': hidden }" class="pagination-container">
    <el-pagination :background="background" v-model:current-page="currentPage" v-model:page-size="pageSize"
      :layout="layout" :page-sizes="pageSizes" :pager-count="pagerCount" :total="+total" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>
<style scoped lang="scss">
.pagination-container {
  padding: 32px 16px;
  position: relative;

  &.hidden {
    display: none;
  }
}
</style>