<script setup>
import { useEventListener } from '@vueuse/core'
import { ReloadOutlined } from '@ant-design/icons-vue'
import { getAntdIconfontNames, getLocalIconfontNames } from '@/utils/iconfont'

const props = defineProps({
  size: {
    type: String,
    default: 'middle',
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
    default: 'bottomLeft',
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

const emit = defineEmits(['update:modelValue', 'change'])

const selectorInputRef = useTemplateRef('selectorInputRef')
const iconSelectorRef = useTemplateRef('iconSelectorRef')
const popoverOpen = ref(false)
const iconType = ref(props.type)
const selectorWidth = ref(260)
const fontIconNames = ref([])
const inputValue = ref('')
const prependIcon = ref(props.modelValue)
const defaultModelValue = ref(props.modelValue || 'list')
const iconKey = ref(0)

const renderFontIconNames = computed(() => {
  if (!inputValue.value) return fontIconNames.value
  const keyword = inputValue.value.trim().toLowerCase()
  return fontIconNames.value.filter((icon) =>
    icon.toLowerCase().includes(keyword)
  )
})

const closePopover = () => {
  popoverOpen.value = false
}

const onInputFocus = () => {
  if (props.disabled) return
  popoverOpen.value = true
}

const onPopoverOpenChange = (open) => {
  if (open) {
    popoverOpen.value = true
  }
}

const onClickOutside = (event) => {
  if (!popoverOpen.value) return

  const rootEl = iconSelectorRef.value
  if (rootEl?.contains(event.target)) return
  if (event.target.closest('.ant-popover')) return

  closePopover()
}

const onInputRefresh = () => {
  iconKey.value++
  prependIcon.value = defaultModelValue.value
  inputValue.value = ''
  emit('update:modelValue', defaultModelValue.value)
  emit('change', defaultModelValue.value)
}

const loadIcons = (name) => {
  iconType.value = name
  fontIconNames.value = []
  if (name === 'antd') {
    getAntdIconfontNames().then((res) => {
      fontIconNames.value = res
    })
  } else if (name === 'local') {
    getLocalIconfontNames().then((res) => {
      fontIconNames.value = res
    })
  }
}

const onChangeTab = (name) => {
  loadIcons(name)
}

const onIcon = (icon) => {
  closePopover()
  iconKey.value++
  prependIcon.value = icon
  inputValue.value = ''
  emit('update:modelValue', icon)
  emit('change', icon)
  nextTick(() => {
    selectorInputRef.value?.blur()
  })
}

const getInputWidth = () => {
  nextTick(() => {
    const width = selectorInputRef.value?.$el?.offsetWidth || 260
    selectorWidth.value = width < 260 ? 260 : width
  })
}

watch(
  () => props.modelValue,
  (value) => {
    iconKey.value++
    if (value !== prependIcon.value) {
      defaultModelValue.value = value
    }
    if (value === '') {
      defaultModelValue.value = 'fa fa-circle-o'
    }
    prependIcon.value = value
  }
)

onMounted(() => {
  getInputWidth()
  useEventListener(window, 'resize', getInputWidth)
  useEventListener(document, 'mousedown', onClickOutside)
  loadIcons(props.type)
})
</script>

<template>
  <div ref="iconSelectorRef" class="icon-selector">
    <a-popover
      :open="popoverOpen"
      :trigger="[]"
      :placement="placement"
      :overlay-style="{ width: `${selectorWidth}px` }"
      @update:open="onPopoverOpenChange"
    >
      <template #content>
        <div class="icon-selector-panel">
          <div class="selector-header">
            <div class="selector-title">{{ title || '请选择图标' }}</div>
            <div class="selector-tab">
              <span
                :class="{ active: iconType === 'antd' }"
                @click="onChangeTab('antd')"
              >
                antd
              </span>
              <span
                :class="{ active: iconType === 'local' }"
                @click="onChangeTab('local')"
              >
                local
              </span>
            </div>
          </div>
          <div class="selector-body">
            <div v-if="renderFontIconNames.length > 0" class="icon-list">
              <div
                v-for="item in renderFontIconNames"
                :key="item"
                class="icon-selector-item"
                :title="item"
                @click="onIcon(item)"
              >
                <SvgIcon :key="'icon' + iconKey + item" :icon-class="item" />
              </div>
            </div>
            <a-empty v-else description="暂无图标" />
          </div>
        </div>
      </template>

      <a-input
        ref="selectorInputRef"
        v-model:value="inputValue"
        :size="size"
        :disabled="disabled"
        placeholder="搜索图标"
        @focus="onInputFocus"
      >
        <template #addonBefore>
          <div class="icon-prepend">
            <SvgIcon
              :key="'icon' + iconKey"
              :icon-class="prependIcon || defaultModelValue"
            />
            <div v-if="showIconName" class="name">
              {{ prependIcon || defaultModelValue }}
            </div>
          </div>
        </template>
        <template #addonAfter>
          <ReloadOutlined class="refresh-icon" @click.stop="onInputRefresh" />
        </template>
      </a-input>
    </a-popover>
  </div>
</template>

<style scoped lang="scss">
.icon-prepend {
  display: flex;
  align-items: center;
  justify-content: center;

  .name {
    padding-left: 5px;
    max-width: 120px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.refresh-icon {
  cursor: pointer;
}

.selector-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.selector-title {
  font-weight: 500;
}

.selector-tab {
  margin-left: auto;

  span {
    padding: 0 5px;
    cursor: pointer;
    user-select: none;

    &.active,
    &:hover {
      color: #1677ff;
      text-decoration: underline;
    }
  }
}

.selector-body {
  height: 250px;
  overflow-y: auto;
}

.icon-list {
  display: flex;
  flex-wrap: wrap;
}

.icon-selector-item {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 8px;
  margin: 3px;
  border: 1px solid var(--ba-border-color);
  border-radius: 6px;
  cursor: pointer;
  font-size: 18px;

  &:hover {
    border-color: #1677ff;
  }
}
</style>
