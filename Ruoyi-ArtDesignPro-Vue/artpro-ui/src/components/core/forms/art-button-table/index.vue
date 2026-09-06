<!-- 表格按钮 -->
<template>
  <div
    :class="[
      'inline-flex items-center justify-center min-w-8 h-8 px-2.5 mr-2.5 text-sm c-p rounded-md align-middle'
    ]"
    :style="resolvedStyle"
    @click="handleClick"
  >
    <ArtSvgIcon :icon="iconContent" />
  </div>
</template>

<script setup lang="ts">
  defineOptions({ name: 'ArtButtonTable' })

  interface Props {
    /** 按钮类型 */
    type?: 'add' | 'edit' | 'delete' | 'more' | 'view'
    /** 按钮图标 */
    icon?: string
    /** 按钮样式类 */
    iconClass?: string
    /** icon 颜色 */
    iconColor?: string
    /** 按钮背景色 */
    buttonBgColor?: string
  }

  const props = withDefaults(defineProps<Props>(), {})

  const emit = defineEmits<{
    (e: 'click'): void
  }>()

  // 默认按钮配置
  const defaultButtons = {
    add: {
      icon: 'ri:add-fill',
      bg: 'var(--el-color-primary)',
      color: 'var(--el-color-primary)'
    },
    edit: {
      icon: 'ri:pencil-line',
      bg: 'var(--el-color-primary)',
      color: 'var(--el-color-primary)'
    },
    delete: {
      icon: 'ri:delete-bin-5-line',
      bg: 'var(--el-color-danger)',
      color: 'var(--el-color-danger)'
    },
    view: { icon: 'ri:eye-line', bg: 'var(--el-color-info)', color: 'var(--el-color-info)' },
    more: { icon: 'ri:more-2-fill', bg: '', color: '' }
  } as const

  const iconContent = computed(() => {
    return props.icon || (props.type ? defaultButtons[props.type]?.icon : '') || ''
  })

  const resolvedStyle = computed(() => {
    if (props.iconColor || props.buttonBgColor) {
      return {
        backgroundColor: props.buttonBgColor || undefined,
        color: props.iconColor || undefined
      }
    }
    const type = props.type
    if (!type || props.iconClass) return {}
    const cfg = defaultButtons[type]
    return {
      backgroundColor: cfg.bg
        ? `color-mix(in srgb, ${cfg.bg} 8%, transparent)`
        : undefined,
      color: cfg.color || undefined
    }
  })

  const handleClick = () => {
    emit('click')
  }
</script>
