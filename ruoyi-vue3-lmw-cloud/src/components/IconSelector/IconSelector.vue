<template>
  <el-popover
    :placement="placement"
    trigger="focus"
    :hide-after="0"
    :width="state.selectorWidth"
    :visible="state.popoverVisible"
  >
    <div
      @mouseover.stop="state.iconSelectorMouseover = true"
      @mouseout.stop="state.iconSelectorMouseover = false"
      class="icon-selector"
    >
      <transition name="el-zoom-in-center">
        <div class="icon-selector-box">
          <div class="selector-header">
            <div class="selector-title">
              {{ title ? title : '请选择图标' }}
            </div>
            <div class="selector-tab">
              <span
                @click="onChangeTab('ele')"
                :class="state.iconType == 'ele' ? 'active' : ''"
                >ele</span
              >
              <span
                @click="onChangeTab('local')"
                :class="state.iconType == 'local' ? 'active' : ''"
                >local</span
              >
            </div>
          </div>
          <div class="selector-body">
            <el-scrollbar ref="selectorScrollbarRef">
              <div v-if="renderFontIconNames.length > 0">
                <div
                  class="icon-selector-item"
                  :title="item"
                  @click="onIcon(item)"
                  v-for="(item, key) in renderFontIconNames"
                  :key="key"
                >
                  <SvgIcon :iconClass="item" />
                </div>
              </div>
            </el-scrollbar>
          </div>
        </div>
      </transition>
    </div>
    <template #reference>
      <el-input
        v-model="state.inputValue"
        :size="size"
        :disabled="disabled"
        placeholder="搜索图标"
        ref="selectorInput"
        @focus="onInputFocus"
        @blur="onInputBlur"
        :class="'size-' + size"
      >
        <template #prepend>
          <div class="icon-prepend">
            <SvgIcon
              :key="'icon' + state.iconKey"
              :iconClass="
                state.prependIcon ? state.prependIcon : state.defaultModelValue
              "
            />
            <div v-if="showIconName" class="name">
              {{
                state.prependIcon ? state.prependIcon : state.defaultModelValue
              }}
            </div>
          </div>
        </template>
        <template #append>
          <el-icon @click="onInputRefresh"><RefreshRight /></el-icon>
        </template>
      </el-input>
    </template>
  </el-popover>
</template>

<script setup>
import { reactive, ref, onMounted, nextTick, watch, computed } from 'vue'
import {
  getElementPlusIconfontNames,
  getLocalIconfontNames,
} from '@/utils/iconfont'
import { useEventListener } from '@vueuse/core'

const props = defineProps({
  size: {
    type: String,
    default: 'default',
  },

  disabled: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: '',
  },
  type: {
    type: String,
    default: 'local',
  },
  placement: {
    type: String,
    default: 'bottom-start',
  },
  modelValue: {
    type: String,
    default: '',
  },
  showIconName: {
    type: Boolean,
    default: true,
  },
})

const emits = defineEmits(['update:modelValue', 'change'])

const selectorInput = ref()
const selectorScrollbarRef = ref()
const state = reactive({
  iconType: props.type,
  selectorWidth: 0,
  popoverVisible: false,
  inputFocus: false,
  iconSelectorMouseover: false,
  fontIconNames: [],
  inputValue: '',
  prependIcon: props.modelValue,
  defaultModelValue: props.modelValue || 'list',
  iconKey: 0, // 给icon标签准备个key，以随时使用 h 函数重新生成元素
})

const onInputFocus = () => {
  state.inputFocus = state.popoverVisible = true
}
const onInputBlur = () => {
  state.inputFocus = false
  state.popoverVisible = state.iconSelectorMouseover
}
const onInputRefresh = () => {
  state.iconKey++
  state.prependIcon = state.defaultModelValue
  state.inputValue = ''
  emits('update:modelValue', state.defaultModelValue)
  emits('change', state.defaultModelValue)
}
const onChangeTab = (name) => {
  state.iconType = name
  state.fontIconNames = []
  if (name == 'ele') {
    getElementPlusIconfontNames().then((res) => {
      state.fontIconNames = res
    })
  } else if (name == 'local') {
    getLocalIconfontNames().then((res) => {
      state.fontIconNames = res
    })
  }
}
const onIcon = (icon) => {
  state.iconSelectorMouseover = state.popoverVisible = false
  state.iconKey++
  state.prependIcon = icon
  state.inputValue = ''
  emits('update:modelValue', icon)
  emits('change', icon)
  nextTick(() => {
    selectorInput.value.blur()
  })
}

const renderFontIconNames = computed(() => {
  if (!state.inputValue) return state.fontIconNames

  let inputValue = state.inputValue.trim().toLowerCase()
  return state.fontIconNames.filter((icon) => {
    if (icon.toLowerCase().indexOf(inputValue) !== -1) {
      return icon
    }
  })
})

// 获取 input 的宽度
const getInputWidth = () => {
  nextTick(() => {
    state.selectorWidth =
      selectorInput.value.$el.offsetWidth < 260
        ? 260
        : selectorInput.value.$el.offsetWidth
  })
}

const popoverVisible = () => {
  state.popoverVisible =
    state.inputFocus || state.iconSelectorMouseover ? true : false
}

watch(
  () => props.modelValue,
  () => {
    state.iconKey++
    if (props.modelValue != state.prependIcon)
      state.defaultModelValue = props.modelValue
    if (props.modelValue == '') state.defaultModelValue = 'fa fa-circle-o'
    state.prependIcon = props.modelValue
  }
)
onMounted(() => {
  getInputWidth()
  useEventListener(document, 'click', popoverVisible)
  getLocalIconfontNames().then((res) => {
    state.fontIconNames = res
  })
})
</script>

<style scoped lang="scss">
.size-small {
  height: 24px;
}
.size-large {
  height: 40px;
}
.size-default {
  height: 32px;
}
.icon-prepend {
  display: flex;
  align-items: center;
  justify-content: center;
  .name {
    padding-left: 5px;
  }
}
.selector-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}
.selector-tab {
  margin-left: auto;
  span {
    padding: 0 5px;
    cursor: pointer;
    user-select: none;
    &.active,
    &:hover {
      color: var(--el-color-primary);
      text-decoration: underline;
    }
  }
}
.selector-body {
  height: 250px;
}
.icon-selector-item {
  display: inline-block;
  padding: 10px 10px 6px 10px;
  margin: 3px;
  border: 1px solid var(--ba-border-color);
  border-radius: var(--el-border-radius-base);
  cursor: pointer;
  font-size: 18px;
  .icon {
    height: 18px;
    width: 18px;
  }
  &:hover {
    border: 1px solid var(--el-color-primary);
  }
}
:deep(.el-input-group__prepend) {
  padding: 0 10px;
}
:deep(.el-input-group__append) {
  padding: 0 10px;
}
</style>
