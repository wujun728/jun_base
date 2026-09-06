<script setup lang="ts">
import DarkSwitch from '../DarkSwitch/index.vue'

import toggleDark from '@/utils/useDark.ts'
import useConfig from '@/store/modules/layout.ts'
import usePermission from '@/store/modules/permission'
import {
  STORE_CONFIG,
  BEFORE_RESIZE_LAYOUT,
} from '@/store/constant/cacheKey.ts'
import Local from '@/utils/hsj/useStorage.ts'
import { proxy } from '@/utils/provide'
const router = useRouter()
const configStore = useConfig()
const permissionStore = usePermission()

const onCloseDrawer = () => {
  configStore.setLayout('showDrawer', false)
}

const setLayoutMode = (mode: LayoutMode) => {
  Local.set(BEFORE_RESIZE_LAYOUT, {
    layoutMode: mode,
    menuCollapse: configStore.layout.menuCollapse,
  })
  if (configStore.layout.layoutMode === 'Double' && mode !== 'Double') {
    permissionStore.setSidebarRouters(permissionStore.defaultRoutes)
  }
  configStore.setLayoutMode(mode)
}
const onCommitState = (
  value: number | string | boolean,
  name: keyof LayoutState
) => {
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

const onCommitColorState = (
  value: any,
  name:
    | 'menuBackground'
    | 'menuColor'
    | 'menuHoverBackground'
    | 'menuActiveBackground'
    | 'menuActiveColor'
    | 'menuTopBarBackground'
    | 'headerBarBackground'
    | 'headerBarTabColor'
    | 'headerBarHoverBackground'
    | 'headerBarTabActiveBackground'
    | 'headerBarTabActiveColor'
) => {
  if (value === null) return
  const colors = configStore.layout[name]
  if (configStore.layout.isDark) {
    colors[1] = value
  } else {
    colors[0] = value
  }
  configStore.setLayout(name, colors)
}
const toggleDarkLight = (e: MouseEvent) => {
  if (!document.startViewTransition) {
    toggleDark()
    return
  }
  const transition = document.startViewTransition(() => toggleDark())
  transition.ready.then(() => {
    // 由于我们要从鼠标点击的位置开始做动画，所以我们需要先获取到鼠标的位置
    const { clientX, clientY } = e

    // 计算半径，以鼠标点击的位置为圆心，到四个角的距离中最大的那个作为半径
    const radius = Math.hypot(
      Math.max(clientX, innerWidth - clientX),
      Math.max(clientY, innerHeight - clientY)
    )
    const clipPath = [
      `circle(0% at ${clientX}px ${clientY}px)`,
      `circle(${radius}px at ${clientX}px ${clientY}px)`,
    ]
    const isDark = configStore.layout.isDark
    // 自定义动画
    document.documentElement.animate(
      {
        clipPath: isDark ? [...clipPath].reverse() : clipPath,
      },
      {
        duration: 600,
        easing: 'ease',
        pseudoElement: isDark
          ? '::view-transition-old(root)'
          : '::view-transition-new(root)',
      }
    )
  })
}
const restoreDefault = () => {
  Local.remove(STORE_CONFIG)
  Local.remove(BEFORE_RESIZE_LAYOUT)
  router.go(0)
}
</script>
<template>
  <div class="layout-config-drawer">
    <el-drawer
      size="310px"
      title="布局配置"
      :model-value="configStore.layout.showDrawer"
      @close="onCloseDrawer"
    >
      <el-scrollbar class="layout-mode-style-scrollbar">
        <el-form ref="formRef" :model="configStore.layout">
          <div class="layout-mode-styles-box">
            <el-divider border-style="dashed"> 布局方式 </el-divider>
            <div class="layout-mode-box-style">
              <el-row class="layout-mode-box-style-row" :gutter="10">
                <el-col :span="12" v-if="!proxy.$isSmallScreen">
                  <div
                    @click="setLayoutMode('Default')"
                    class="layout-mode-style default"
                    :class="
                      configStore.layout.layoutMode == 'Default' ? 'active' : ''
                    "
                  >
                    <div class="layout-mode-style-box">
                      <div class="layout-mode-style-aside"></div>
                      <div class="layout-mode-style-container-box">
                        <div class="layout-mode-style-header"></div>
                        <div class="layout-mode-style-container"></div>
                      </div>
                    </div>
                    <div class="layout-mode-style-name">默认</div>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div
                    @click="setLayoutMode('Classic')"
                    class="layout-mode-style classic"
                    :class="
                      configStore.layout.layoutMode == 'Classic' ? 'active' : ''
                    "
                  >
                    <div class="layout-mode-style-box">
                      <div class="layout-mode-style-aside"></div>
                      <div class="layout-mode-style-container-box">
                        <div class="layout-mode-style-header"></div>
                        <div class="layout-mode-style-container"></div>
                      </div>
                    </div>
                    <div class="layout-mode-style-name">经典</div>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="10" v-if="!proxy.$isSmallScreen">
                <el-col :span="12">
                  <div
                    @click="setLayoutMode('Streamline')"
                    class="layout-mode-style streamline"
                    :class="
                      configStore.layout.layoutMode == 'Streamline'
                        ? 'active'
                        : ''
                    "
                  >
                    <div class="layout-mode-style-box">
                      <div class="layout-mode-style-container-box">
                        <div class="layout-mode-style-header"></div>
                        <div class="layout-mode-style-container"></div>
                      </div>
                    </div>
                    <div class="layout-mode-style-name">单栏</div>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div
                    @click="setLayoutMode('Double')"
                    class="layout-mode-style double"
                    :class="
                      configStore.layout.layoutMode == 'Double' ? 'active' : ''
                    "
                  >
                    <div class="layout-mode-style-box">
                      <div class="layout-mode-style-aside"></div>
                      <div class="layout-mode-style-container-box">
                        <div class="layout-mode-style-header"></div>
                        <div class="layout-mode-style-container"></div>
                      </div>
                    </div>
                    <div class="layout-mode-style-name">双栏</div>
                  </div>
                </el-col>
              </el-row>
            </div>
            <el-divider border-style="dashed"> 全局 </el-divider>
            <div class="layout-config-global">
              <el-form-item size="large" label="暗黑模式">
                <DarkSwitch @click="toggleDarkLight" />
              </el-form-item>
              <el-form-item label="后台页面切换动画">
                <el-select
                  @change="onCommitState($event, 'mainAnimation')"
                  :model-value="configStore.layout.mainAnimation"
                  placeholder="请选择切换动画"
                >
                  <el-option
                    label="slide-right"
                    value="slide-right"
                  ></el-option>
                  <el-option label="slide-left" value="slide-left"></el-option>
                  <el-option
                    label="el-fade-in-linear"
                    value="el-fade-in-linear"
                  ></el-option>
                  <el-option label="el-fade-in" value="el-fade-in"></el-option>
                  <el-option
                    label="el-zoom-in-center"
                    value="el-zoom-in-center"
                  ></el-option>
                  <el-option
                    label="el-zoom-in-top"
                    value="el-zoom-in-top"
                  ></el-option>
                  <el-option
                    label="el-zoom-in-bottom"
                    value="el-zoom-in-bottom"
                  ></el-option>
                </el-select>
              </el-form-item>
            </div>
            <el-divider border-style="dashed"> 侧边栏 </el-divider>
            <div class="layout-config-aside">
              <el-form-item label="侧边菜单栏背景色">
                <el-color-picker
                  @change="onCommitColorState($event, 'menuBackground')"
                  :model-value="configStore.getColorVal('menuBackground')"
                />
              </el-form-item>
              <el-form-item label="侧边菜单文字颜色">
                <el-color-picker
                  @change="onCommitColorState($event, 'menuColor')"
                  :model-value="configStore.getColorVal('menuColor')"
                />
              </el-form-item>
              <el-form-item label="侧边菜单悬停背景颜色">
                <el-color-picker
                  @change="onCommitColorState($event, 'menuHoverBackground')"
                  :model-value="configStore.getColorVal('menuHoverBackground')"
                />
              </el-form-item>
              <el-form-item label="侧边菜单激活项背景色">
                <el-color-picker
                  @change="onCommitColorState($event, 'menuActiveBackground')"
                  :model-value="configStore.getColorVal('menuActiveBackground')"
                />
              </el-form-item>
              <el-form-item label="侧边菜单激活项文字色">
                <el-color-picker
                  @change="onCommitColorState($event, 'menuActiveColor')"
                  :model-value="configStore.getColorVal('menuActiveColor')"
                />
              </el-form-item>

              <el-form-item label="显示侧边菜单顶栏(LOGO栏)">
                <el-switch
                  @change="onCommitState($event, 'menuShowTopBar')"
                  :model-value="configStore.layout.menuShowTopBar"
                ></el-switch>
              </el-form-item>
              <el-form-item label="侧边菜单顶栏背景色">
                <el-color-picker
                  @change="onCommitColorState($event, 'menuTopBarBackground')"
                  :model-value="configStore.getColorVal('menuTopBarBackground')"
                />
              </el-form-item>

              <el-form-item label="侧边菜单宽度(展开时)">
                <el-input
                  @input="onCommitState($event, 'menuWidth')"
                  type="number"
                  :step="5"
                  :max="350"
                  :model-value="configStore.layout.menuWidth"
                >
                  <template #append>px</template>
                </el-input>
              </el-form-item>
              <el-form-item label="侧边菜单水平折叠">
                <el-switch
                  @change="onCommitState($event, 'menuCollapse')"
                  :model-value="configStore.layout.menuCollapse"
                ></el-switch>
              </el-form-item>
              <el-form-item label="侧边菜单手风琴">
                <el-switch
                  @change="onCommitState($event, 'menuUniqueOpened')"
                  :model-value="configStore.layout.menuUniqueOpened"
                ></el-switch>
              </el-form-item>
            </div>
            <el-divider border-style="dashed"> 顶栏 </el-divider>
            <div class="layout-config-aside">
              <el-form-item label="顶栏背景色">
                <el-color-picker
                  @change="onCommitColorState($event, 'headerBarBackground')"
                  :model-value="configStore.getColorVal('headerBarBackground')"
                />
              </el-form-item>
              <el-form-item label="顶栏文字色">
                <el-color-picker
                  @change="onCommitColorState($event, 'headerBarTabColor')"
                  :model-value="configStore.getColorVal('headerBarTabColor')"
                />
              </el-form-item>
              <el-form-item
                label="
                  顶栏悬停时背景色
                "
              >
                <el-color-picker
                  @change="
                    onCommitColorState($event, 'headerBarHoverBackground')
                  "
                  :model-value="
                    configStore.getColorVal('headerBarHoverBackground')
                  "
                />
              </el-form-item>

              <el-form-item label="顶栏菜单激活项背景色">
                <el-color-picker
                  @change="
                    onCommitColorState($event, 'headerBarTabActiveBackground')
                  "
                  :model-value="
                    configStore.getColorVal('headerBarTabActiveBackground')
                  "
                />
              </el-form-item>
              <el-form-item label="顶栏菜单激活项文字色">
                <el-color-picker
                  @change="
                    onCommitColorState($event, 'headerBarTabActiveColor')
                  "
                  :model-value="
                    configStore.getColorVal('headerBarTabActiveColor')
                  "
                />
              </el-form-item>
            </div>
            <el-popconfirm
              @confirm="restoreDefault"
              title="
                确定要恢复全部配置到默认值吗？
              "
            >
              <template #reference>
                <div class="flexCenter">
                  <el-button class="w80" type="info"> 恢复默认 </el-button>
                </div>
              </template>
            </el-popconfirm>
          </div>
        </el-form>
      </el-scrollbar>
    </el-drawer>
  </div>
</template>

<style scoped lang="scss">
.layout-config-drawer :deep(.el-input__inner) {
  padding: 0 0 0 6px;
}
.layout-config-drawer :deep(.el-input-group__append) {
  padding: 0 10px;
}
.layout-config-drawer :deep(.el-drawer__header) {
  margin-bottom: 0 !important;
}
.layout-config-drawer :deep(.el-drawer__body) {
  padding: 0;
}
.layout-mode-styles-box {
  padding: 20px;
}
.layout-mode-box-style-row {
  margin-bottom: 15px;
}
.layout-mode-style {
  position: relative;
  height: 100px;
  border: 1px solid var(--el-border-color-light);
  border-radius: var(--el-border-radius-small);
  &:hover,
  &.active {
    border: 1px solid var(--el-color-primary);
  }
  .layout-mode-style-name {
    position: absolute;
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--el-color-primary-light-5);
    border-radius: 50%;
    height: 50px;
    width: 50px;
    border: 1px solid var(--el-color-primary-light-3);
  }
  .layout-mode-style-box {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
  }
  &.default {
    display: flex;
    align-items: center;
    justify-content: center;
    .layout-mode-style-aside {
      width: 18%;
      height: 90%;
      background-color: var(--el-border-color-lighter);
    }
    .layout-mode-style-container-box {
      width: 68%;
      height: 90%;
      margin-left: 4%;
      .layout-mode-style-header {
        width: 100%;
        height: 10%;
        background-color: var(--el-border-color-lighter);
      }
      .layout-mode-style-container {
        width: 100%;
        height: 85%;
        background-color: var(--el-border-color-extra-light);
        margin-top: 5%;
      }
    }
  }
  &.classic {
    display: flex;
    align-items: center;
    justify-content: center;
    .layout-mode-style-aside {
      width: 18%;
      height: 100%;
      background-color: var(--el-border-color-lighter);
    }
    .layout-mode-style-container-box {
      width: 82%;
      height: 100%;
      .layout-mode-style-header {
        width: 100%;
        height: 10%;
        background-color: var(--el-border-color);
      }
      .layout-mode-style-container {
        width: 100%;
        height: 90%;
        background-color: var(--el-border-color-extra-light);
      }
    }
  }
  &.streamline {
    display: flex;
    align-items: center;
    justify-content: center;
    .layout-mode-style-container-box {
      width: 100%;
      height: 100%;
      .layout-mode-style-header {
        width: 100%;
        height: 10%;
        background-color: var(--el-border-color);
      }
      .layout-mode-style-container {
        width: 100%;
        height: 90%;
        background-color: var(--el-border-color-extra-light);
      }
    }
  }
  &.double {
    display: flex;
    align-items: center;
    justify-content: center;
    .layout-mode-style-aside {
      width: 18%;
      height: 100%;
      background-color: var(--el-border-color);
    }
    .layout-mode-style-container-box {
      width: 82%;
      height: 100%;
      .layout-mode-style-header {
        width: 100%;
        height: 10%;
        background-color: var(--el-border-color);
      }
      .layout-mode-style-container {
        width: 100%;
        height: 90%;
        background-color: var(--el-border-color-extra-light);
      }
    }
  }
}
.w80 {
  width: 90%;
}
</style>
