<script setup lang="ts">
import useTagsViewStore from '@/store/modules/tagsView'
import { computed, onBeforeUnmount, onMounted, ref } from 'vue';
import { RouteLocationNormalizedGeneric } from 'vue-router';

const tagAndTagSpacing = ref(4);
const scrollContainer = ref<any>(null);
const scrollWrapper = computed(() => scrollContainer.value!.$refs.wrapRef);

onMounted(() => {
  scrollWrapper.value.addEventListener('scroll', emitScroll, true)
})
onBeforeUnmount(() => {
  scrollWrapper.value.removeEventListener('scroll', emitScroll)
})

function handleScroll(e: WheelEvent) {
  const eventDelta = -e.deltaY * 40
  const $scrollWrapper = scrollWrapper.value;
  $scrollWrapper.scrollLeft = $scrollWrapper.scrollLeft + eventDelta / 4
}
const emits = defineEmits()
const emitScroll = () => {
  emits('scroll')
}

const tagsViewStore = useTagsViewStore()
const visitedViews = computed(() => tagsViewStore.visitedViews);

function moveToTarget(currentTag: RouteLocationNormalizedGeneric) {
  const $container = scrollContainer.value!.$el
  const $containerWidth = $container.offsetWidth
  const $scrollWrapper = scrollWrapper.value;

  let firstTag = null
  let lastTag = null

  // find first tag and last tag
  if (visitedViews.value.length > 0) {
    firstTag = visitedViews.value[0]
    lastTag = visitedViews.value[visitedViews.value.length - 1]
  }

  if (firstTag === currentTag) {
    $scrollWrapper.scrollLeft = 0
  } else if (lastTag === currentTag) {
    $scrollWrapper.scrollLeft = $scrollWrapper.scrollWidth - $containerWidth
  } else {
    const tagListDom = document.getElementsByClassName('tags-view-item');
    const currentIndex = visitedViews.value.findIndex(item => item === currentTag)
    let prevTag = null
    let nextTag = null
    for (const k in tagListDom) {
      if (k !== 'length' && Object.hasOwnProperty.call(tagListDom, k)) {
        const dom = tagListDom[k] as HTMLElement;
        if (dom.dataset.path === visitedViews.value[currentIndex - 1].path) {
          prevTag = dom;
        }
        if (dom.dataset.path === visitedViews.value[currentIndex + 1].path) {
          nextTag = dom;
        }
      }
    }

    // the tag's offsetLeft after of nextTag
    const afterNextTagOffsetLeft = nextTag!.offsetLeft + nextTag!.offsetWidth + tagAndTagSpacing.value

    // the tag's offsetLeft before of prevTag
    const beforePrevTagOffsetLeft = prevTag!.offsetLeft - tagAndTagSpacing.value
    if (afterNextTagOffsetLeft > $scrollWrapper.scrollLeft + $containerWidth) {
      $scrollWrapper.scrollLeft = afterNextTagOffsetLeft - $containerWidth
    } else if (beforePrevTagOffsetLeft < $scrollWrapper.scrollLeft) {
      $scrollWrapper.scrollLeft = beforePrevTagOffsetLeft
    }
  }
}

defineExpose({
  moveToTarget,
})
</script>

<template>
  <el-scrollbar ref="scrollContainer" :vertical="false" class="scroll-container" @wheel.prevent="handleScroll">
    <slot />
  </el-scrollbar>
</template>

<style lang='scss' scoped>
.scroll-container {
  white-space: nowrap;
  position: relative;
  overflow: hidden;
  width: 100%;

  :deep(.el-scrollbar__bar) {
    bottom: 0px;
  }

  :deep(.el-scrollbar__wrap) {
    height: 49px;
  }
}
</style>