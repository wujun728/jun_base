<script setup lang="ts">
import { isExternal } from "@/utils/validate";
import { computed } from "vue";

const props = withDefaults(defineProps<{
  src?: string,
  width?: number | string,
  height?: number | string
}>(), {
  src: "",
  width: "",
  height: ""
});

const realSrc = computed(() => {
  if (!props.src) {
    return;
  }
  let real_src = props.src.split(",")[0];
  // 如果是绝对路径（以 http(s):// 或 / 开头），直接返回
  if (/^(https?:)?\//.test(real_src)) {
    return real_src;
  }
  if (isExternal(real_src)) {
    return real_src;
  }
  return import.meta.env.VITE_APP_BASE_API + real_src;
});

const realSrcList = computed(() => {
  if (!props.src) {
    return;
  }
  let real_src_list = props.src.split(",");
  let srcList: string[] = [];
  real_src_list.forEach(item => {
    if (/^(https?:)?\//.test(item)) {
      return srcList.push(item);
    }
    if (isExternal(item)) {
      return srcList.push(item);
    }
    return srcList.push(import.meta.env.VITE_APP_BASE_API + item);
  });
  return srcList;
});

const realWidth = computed(() =>
  typeof props.width == "string" ? props.width : `${props.width}px`
);

const realHeight = computed(() =>
  typeof props.height == "string" ? props.height : `${props.height}px`
);
</script>
<template>
  <el-image :src="`${realSrc}`" fit="cover" :style="`width:${realWidth};height:${realHeight};`"
    :preview-src-list="realSrcList" preview-teleported>
    <template #error>
      <div class="image-slot">
        <el-icon><picture-filled /></el-icon>
      </div>
    </template>
  </el-image>
</template>
<style lang="scss" scoped>
.el-image {
  border-radius: 5px;
  background-color: #ebeef5;
  box-shadow: 0 0 5px 1px #ccc;

  :deep(.el-image__inner) {
    transition: all 0.3s;
    cursor: pointer;

    &:hover {
      transform: scale(1.2);
    }
  }

  :deep(.image-slot) {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    color: #909399;
    font-size: 30px;
  }
}
</style>
