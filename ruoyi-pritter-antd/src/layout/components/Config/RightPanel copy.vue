<script setup>
import { useConfig } from '@/store/modules/layout'
import {
  STORE_CONFIG,
  BEFORE_RESIZE_LAYOUT,
} from '@/store/constant/cacheKey.js'
import Local from '@/utils/useStorage'
import { toggleDarkLight } from '@/utils/useDark'

defineOptions({ name: 'RightPanel' })

const open = defineModel('open', { type: Boolean, default: false })

const router = useRouter()
const configStore = useConfig()

const onCommitState = (value, name) => {
  if (typeof value === 'boolean') {
    configStore.setLayout(name, value)
  } else {
    let valueParse = Number(value)
    if (name === 'menuWidth') {
      valueParse = valueParse > 350 ? 350 : valueParse
    }
    configStore.setLayout(name, valueParse)
  }
}

const onCommitColorState = (value, name) => {
  if (!value) return
  const colors = configStore.layout[name]
  if (configStore.layout.isDark) {
    colors[1] = value
  } else {
    colors[0] = value
  }
  configStore.setLayout(name, colors)
}

const toPickerColor = (color) => {
  if (!color || color.startsWith('var(')) return '#ffffff'
  return color
}

const restoreDefault = () => {
  Local.remove(STORE_CONFIG)
  Local.remove(BEFORE_RESIZE_LAYOUT)
  router.go(0)
}
</script>

<template>
  <a-drawer
    v-model:open="open"
    placement="right"
    :width="310"
    :closable="true"
    title="布局配置"
    rootClassName="layout-config-drawer"
  >
    <div class="layout-config-scroll">
      <a-form
        class="layout-config-form"
        layout="horizontal"
        :colon="false"
        :wrapper-col="{ flex: '1' }"
        :model="configStore.layout"
      >
        <div class="layout-mode-styles-box">
          <a-divider dashed>布局方式</a-divider>
          <div class="layout-mode-box-style">
            <div class="layout-mode-style classic active">
              <div class="layout-mode-style-box">
                <div class="layout-mode-style-aside"></div>
                <div class="layout-mode-style-container-box">
                  <div class="layout-mode-style-header"></div>
                  <div class="layout-mode-style-container"></div>
                </div>
              </div>
              <div class="layout-mode-style-name">经典</div>
            </div>
          </div>

          <a-divider dashed>全局</a-divider>
          <div class="layout-config-global">
            <a-form-item label="暗黑模式">
              <div class="dark-switch-wrap" @click="toggleDarkLight">
                <a-switch
                  :checked="configStore.layout.isDark"
                  style="pointer-events: none"
                />
              </div>
            </a-form-item>
          </div>

          <a-divider dashed>侧边栏</a-divider>
          <div class="layout-config-aside">
            <a-form-item label="侧边菜单栏背景色">
              <input
                type="color"
                class="config-color-picker"
                :value="
                  toPickerColor(configStore.getColorVal('menuBackground'))
                "
                @input="
                  onCommitColorState($event.target.value, 'menuBackground')
                "
              />
            </a-form-item>
            <a-form-item label="侧边菜单文字颜色">
              <input
                type="color"
                class="config-color-picker"
                :value="toPickerColor(configStore.getColorVal('menuColor'))"
                @input="onCommitColorState($event.target.value, 'menuColor')"
              />
            </a-form-item>
            <a-form-item label="侧边菜单悬停背景颜色">
              <input
                type="color"
                class="config-color-picker"
                :value="
                  toPickerColor(configStore.getColorVal('menuHoverBackground'))
                "
                @input="
                  onCommitColorState($event.target.value, 'menuHoverBackground')
                "
              />
            </a-form-item>
            <a-form-item label="侧边菜单激活项背景色">
              <input
                type="color"
                class="config-color-picker"
                :value="
                  toPickerColor(configStore.getColorVal('menuActiveBackground'))
                "
                @input="
                  onCommitColorState(
                    $event.target.value,
                    'menuActiveBackground'
                  )
                "
              />
            </a-form-item>
            <a-form-item label="侧边菜单激活项文字色">
              <input
                type="color"
                class="config-color-picker"
                :value="
                  toPickerColor(configStore.getColorVal('menuActiveColor'))
                "
                @input="
                  onCommitColorState($event.target.value, 'menuActiveColor')
                "
              />
            </a-form-item>
            <a-form-item label="侧边菜单宽度(展开时)">
              <a-input-number
                :value="configStore.layout.menuWidth"
                :min="1"
                :max="350"
                :step="5"
                addon-after="px"
                style="width: 100%; max-width: 120px"
                @change="(val) => onCommitState(val, 'menuWidth')"
              />
            </a-form-item>
            <a-form-item label="侧边菜单水平折叠">
              <a-switch
                :checked="configStore.layout.menuCollapse"
                @change="(val) => onCommitState(val, 'menuCollapse')"
              />
            </a-form-item>
            <a-form-item label="侧边菜单手风琴">
              <a-switch
                :checked="configStore.layout.menuUniqueOpened"
                @change="(val) => onCommitState(val, 'menuUniqueOpened')"
              />
            </a-form-item>
          </div>

          <div class="layout-config-restore">
            <a-popconfirm
              title="确定要恢复全部配置到默认值吗？"
              @confirm="restoreDefault"
            >
              <a-button block>恢复默认</a-button>
            </a-popconfirm>
          </div>
        </div>
      </a-form>
    </div>
  </a-drawer>
</template>
<style lang="scss">
.layout-config-drawer {
  .ant-drawer-body {
    padding: 0;
  }
}
</style>
<style scoped lang="scss">
.layout-config-scroll {
  height: 100%;
  overflow-y: auto;
}

.layout-mode-styles-box {
  padding: 0 20px 20px 20px;
}

.layout-config-form {
  :deep(.ant-form-item) {
    margin-bottom: 12px;
  }

  :deep(.ant-form-item-row) {
    align-items: center;
  }

  :deep(.ant-form-item-control) {
    min-width: 0;
  }
}

.config-color-picker {
  width: 32px;
  height: 32px;
  padding: 0;
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 6px;
  cursor: pointer;
  background: transparent;

  &::-webkit-color-swatch-wrapper {
    padding: 2px;
  }

  &::-webkit-color-swatch {
    border: none;
    border-radius: 4px;
  }
}

.dark-switch-wrap {
  display: inline-flex;
  cursor: pointer;
}

.layout-mode-style {
  position: relative;
  height: 100px;
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 6px;

  &.active {
    border-color: #1677ff;
  }

  .layout-mode-style-name {
    position: absolute;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #91caff;
    border-radius: 50%;
    height: 50px;
    width: 50px;
    border: 1px solid #69b1ff;
  }

  .layout-mode-style-box {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
  }
  &.classic {
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    .layout-mode-style-aside {
      width: 18%;
      height: 100%;
      background-color: #ebeef5;
    }
    .layout-mode-style-container-box {
      width: 82%;
      height: 100%;
      .layout-mode-style-header {
        width: 100%;
        height: 10%;
        background-color: #dcdfe6;
      }
      .layout-mode-style-container {
        width: 100%;
        height: 90%;
        background-color: #f2f6fc;
      }
    }
  }
}

.layout-config-restore {
  margin-top: 8px;
}

.dark {
  .layout-mode-style {
    border-color: rgba(255, 255, 255, 0.15);

    &.active {
      border-color: #1677ff;
    }

    &.default .layout-mode-style-aside,
    &.default .layout-mode-style-header {
      background-color: rgba(255, 255, 255, 0.12);
    }

    &.default .layout-mode-style-container {
      background-color: rgba(255, 255, 255, 0.06);
    }
  }

  .config-color-picker {
    border-color: rgba(255, 255, 255, 0.15);
  }
}
</style>
