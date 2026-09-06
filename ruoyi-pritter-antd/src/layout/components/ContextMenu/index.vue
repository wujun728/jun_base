<script setup>
import { toRaw } from 'vue'
import { useEventListener } from '@vueuse/core'

defineOptions({ name: 'TabContextMenu' })

const props = defineProps({
  width: {
    type: Number,
    default: 150,
  },
  items: {
    type: Array,
    default: () => [],
  },
})

const emit = defineEmits(['contextmenuItemClick'])

const state = reactive({
  show: false,
  axis: { x: 0, y: 0 },
  menu: undefined,
})

const menuStyle = computed(() => ({
  top: `${state.axis.y + 5}px`,
  left: `${state.axis.x - 14}px`,
  width: `${props.width}px`,
}))

function onShowContextmenu(menu, axis) {
  state.menu = menu
  state.axis = axis
  state.show = true
}

function onHideContextmenu() {
  state.show = false
}

function onContextmenuItem(item) {
  if (item.disabled) return
  emit('contextmenuItemClick', {
    ...item,
    menu: state.menu ? toRaw(state.menu) : undefined,
  })
  onHideContextmenu()
}

defineExpose({
  onShowContextmenu,
  onHideContextmenu,
})

onMounted(() => {
  useEventListener(document, 'click', onHideContextmenu)
})
</script>

<template>
  <Teleport to="body">
    <Transition name="tab-context-menu-fade">
      <div
        v-show="state.show"
        class="tab-context-menu"
        :style="menuStyle"
        @click.stop
      >
        <ul class="tab-context-menu-list">
          <li
            v-for="item in items"
            :key="item.name"
            class="tab-context-menu-item"
            :class="{ 'is-disabled': item.disabled }"
            @click="onContextmenuItem(item)"
          >
            <SvgIcon :icon-class="item.icon" :size="12" />
            <span>{{ item.label }}</span>
          </li>
        </ul>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped lang="scss">
.tab-context-menu {
  position: fixed;
  z-index: 9999;
  padding: 4px;
  border-radius: 8px;
  background: var(--body-background, #fff);
  border: 1px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
}

.tab-context-menu-list {
  margin: 0;
  padding: 0;
  list-style: none;
}

.tab-context-menu-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 13px;
  color: var(--text-color, rgba(0, 0, 0, 0.88));
  cursor: pointer;
  user-select: none;
  transition: background-color 0.2s;

  &:hover:not(.is-disabled) {
    background: rgba(0, 0, 0, 0.04);
  }

  &.is-disabled {
    color: rgba(0, 0, 0, 0.25);
    cursor: not-allowed;
  }
}

.dark {
  .tab-context-menu {
    border-color: rgba(255, 255, 255, 0.08);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.45);
  }

  .tab-context-menu-item:hover:not(.is-disabled) {
    background: rgba(255, 255, 255, 0.08);
  }

  .tab-context-menu-item.is-disabled {
    color: rgba(255, 255, 255, 0.25);
  }
}

.tab-context-menu-fade-enter-active,
.tab-context-menu-fade-leave-active {
  transition:
    opacity 0.15s ease,
    transform 0.15s ease;
}

.tab-context-menu-fade-enter-from,
.tab-context-menu-fade-leave-to {
  opacity: 0;
  transform: scale(0.95);
}
</style>
