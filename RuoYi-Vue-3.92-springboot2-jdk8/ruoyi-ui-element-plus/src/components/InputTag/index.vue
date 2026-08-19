<template>
  <div class="flex-y-center gap-2">
    <el-tag
      v-for="tag in tags"
      :key="tag"
      closable
      :disable-transitions="false"
      v-bind="config.tagAttrs"
      @close="handleClose(tag)"
    >
      {{ tag }}
    </el-tag>
    <el-input
      v-if="inputVisible"
      ref="inputRef"
      v-model.trim="inputValue"
      @keyup.enter.stop.prevent="handleInputConfirm"
      @blur.stop.prevent="handleInputConfirm"
    />
    <el-button v-else v-bind="config.buttonAttrs" @click="showInput">
      {{ config.buttonAttrs.btnText ? config.buttonAttrs.btnText : "+ New Tag" }}
    </el-button>
  </div>
</template>
<script setup>
const inputValue = ref("");
const inputVisible = ref(false);
const inputRef = ref();

// 定义 model，用于与父组件的 v-model绑定
const tags = defineModel({
  type: Array,
  default: () => [],
});

defineProps({
  config: {
    type: Object,
    default: () => ({
      buttonAttrs: {},
      inputAttrs: {},
      tagAttrs: {},
    }),
  },
});

const handleClose = (tag) => {
  if (tags.value) {
    const newTags = tags.value.filter((t) => t !== tag);
    tags.value = [...newTags];
  }
};

const showInput = () => {
  inputVisible.value = true;
  nextTick(() => inputRef.value?.focus());
};

const handleInputConfirm = () => {
  if (inputValue.value) {
    const newTags = [...(tags.value || []), inputValue.value];
    tags.value = newTags;
  }
  inputVisible.value = false;
  inputValue.value = "";
};
</script>
