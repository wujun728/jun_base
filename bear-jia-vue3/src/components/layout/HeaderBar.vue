<template>
  <a-layout-header :class="headerClasses" class="header-bar">
    <a-row style="height: 100%">
      <!-- 左侧区域 -->
      <a-col :span="(isTopMode || isMixMode) ? 4 : (isDrawerMode ? 8 : 12)" class="header-left">
        <!-- 侧边栏模式的折叠按钮 -->
        <template v-if="!isTopMode && !isMixMode && !isDrawerMode">
          <menu-unfold-outlined
              v-if="collapsed"
              class="trigger"
              @click="toggleCollapse"
          />
          <menu-fold-outlined
              v-else
              class="trigger"
              @click="toggleCollapse"
          />
        </template>

        <!-- 抽屉模式的菜单按钮 -->
        <template v-if="isDrawerMode">
          <menu-outlined
              class="trigger"
              @click="$emit('toggleMenu')"
          />
        </template>

        <!-- Logo 和标题（顶部模式） -->
        <div v-if="isTopMode || isMixMode" class="header-logo">
          <img :src="logoUrl" class="logo-img" :alt="`${SYSTEM_INFO.shortName} Logo`" />
          <span class="logo-title">{{ SYSTEM_INFO.name }}</span>
        </div>

        <!-- 面包屑导航（侧边栏模式） -->
        <span v-if="!isTopMode && !isMixMode" class="header-title">
          {{ currentFatherMenuTitle }} / {{ currentMenuTitle }}
        </span>
      </a-col>

      <!-- 顶部菜单区域 -->
      <a-col v-if="isTopMode" :span="14" class="header-menu">
        <TopMenu
            ref="topMenuRef"
            :menu-data="menuData"
            @menu-select="$emit('menuSelect', $event)"
        />
      </a-col>

      <!-- 混合模式顶部菜单区域 -->
      <a-col v-if="isMixMode" :span="14" class="header-menu">
        <MixTopMenu
            ref="mixTopMenuRef"
            :current-first-level-menu="currentFirstLevelMenu"
            :menu-data="menuData"
            @menu-select="$emit('menuSelect', $event)"
            @first-level-menu-select="$emit('firstLevelMenuSelect', $event)"
        />
      </a-col>

      <!-- 右侧区域 -->
      <a-col :span="(isTopMode || isMixMode) ? 6 : (isDrawerMode ? 16 : 12)" class="header-right">
        <!-- 加载动画 -->
        <a-spin :spinning="loading" style="margin-right: 16px;"/>

        <!-- 搜索按钮 -->
        <a-tooltip placement="bottom" title="全局搜索">
          <a-button class="header-icon-btn" type="link" @click="showSearch">
            <SearchOutlined/>
          </a-button>
        </a-tooltip>

        <!-- 消息按钮 -->
        <a-tooltip placement="bottom" title="消息通知">
          <MessageCenter
            v-model:visible="messageVisible"
            :showButton="true"
          />
        </a-tooltip>

        <!-- 聊天按钮 -->
        <a-tooltip placement="bottom" title="在线聊天">
          <a-button class="header-icon-btn" type="link" @click="showChat">
            <MessageOutlined/>
          </a-button>
        </a-tooltip>

        <!-- 全屏按钮 -->
        <a-tooltip :title="isFullscreen ? '退出全屏' : '全屏显示'" placement="bottom">
          <a-button class="header-icon-btn" type="link" @click="toggleFullscreen">
            <FullscreenExitOutlined v-if="isFullscreen"/>
            <FullscreenOutlined v-else/>
          </a-button>
        </a-tooltip>

        <!-- 刷新按钮 -->
        <a-tooltip placement="bottom" title="刷新页面">
          <a-button class="header-icon-btn" type="link" @click="refreshCurrentPage">
            <ReloadOutlined/>
          </a-button>
        </a-tooltip>

        <!-- 布局设置 -->
        <a-tooltip placement="bottom" title="布局设置">
          <a-button class="header-icon-btn" type="link" @click="$emit('showSettings')">
            <SettingOutlined/>
          </a-button>
        </a-tooltip>

        <!-- 用户头像和下拉菜单 -->
        <a-dropdown class="user-dropdown">
          <a class="ant-dropdown-link user-info" @click.prevent>
            <!-- 智能头像 -->
            <div class="smart-avatar">
              <a-avatar
                v-if="smartAvatar.hasImage"
                :size="32"
                :src="userAvatar"
                @error="handleAvatarError"
              />
              <a-avatar
                v-else
                :size="32"
                :style="{ backgroundColor: smartAvatar.backgroundColor, color: '#fff' }"
              >
                {{ smartAvatar.initial }}
              </a-avatar>
            </div>
            <span class="username">{{ userName }}</span>
          </a>
          <template #overlay>
            <a-menu>
              <a-menu-item key="personalCenter" @click="$emit('personalCenter')">
                <UserOutlined class="menu-icon"/>
                <span>个人中心</span>
              </a-menu-item>
              <a-menu-item key="settings" @click="$emit('showSettings')">
                <SettingOutlined class="menu-icon"/>
                <span>布局设置</span>
              </a-menu-item>
              <a-menu-item key="logout" @click="$emit('logout')">
                <LogoutOutlined class="menu-icon"/>
                <span>退出登录</span>
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </a-col>
    </a-row>

    <!-- 功能组件 -->
    <!-- 全局搜索 -->
    <GlobalSearch v-model:visible="searchVisible"/>

    <!-- 消息中心 -->
<!--    <MessageCenter v-model:visible="messageVisible"/>-->

    <!-- 聊天面板 -->
    <ChatPanel v-model:visible="chatVisible"/>

    <!-- 全屏处理器 -->
    <FullscreenHandler ref="fullscreenHandlerRef"/>
  </a-layout-header>
</template>

<script setup>
import {computed, ref, watch} from 'vue';
import {useUserStore} from '@/stores/user';
import {SYSTEM_INFO} from '@/config/system.config';
import logoUrl from '@/assets/images/logo.png';
import TopMenu from './TopMenu.vue';
import MixTopMenu from './MixTopMenu.vue';
import GlobalSearch from '@/components/common/GlobalSearch.vue';
import MessageCenter from '@/components/common/MessageCenter.vue';
import ChatPanel from '@/components/common/ChatPanel.vue';
import FullscreenHandler from '@/components/common/FullscreenHandler.vue';

import {
  BellOutlined,
  FullscreenExitOutlined,
  FullscreenOutlined,
  LogoutOutlined,
  MenuFoldOutlined,
  MenuOutlined,
  MenuUnfoldOutlined,
  MessageOutlined,
  ReloadOutlined,
  SearchOutlined,
  SettingOutlined,
  UserOutlined
} from '@ant-design/icons-vue';

const userStore = useUserStore();

// 预设颜色数组
const avatarColors = [
  '#1890ff', '#52c41a', '#faad14', '#f5222d', '#fa541c',
  '#eb2f96', '#722ed1', '#13c2c2', '#096dd9', '#389e0d',
  '#d48806', '#cf1322', '#d4380d', '#c41d7f', '#531dab',
  '#08979c', '#1d39c4', '#7cb305', '#ad6800', '#a8071a'
];

// 生成用户名 hash
const generateHash = (str) => {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    const char = str.charCodeAt(i);
    hash = ((hash << 5) - hash) + char;
    hash = hash & hash; // 转换为32位整数
  }
  return Math.abs(hash);
};

// 获取用户名背景色
const getUserAvatarColor = (userName) => {
  if (!userName) return avatarColors[0];
  const hash = generateHash(userName);
  return avatarColors[hash % avatarColors.length];
};

// 获取用户名首字符
const getUserInitial = (userName) => {
  if (!userName) return 'U';
  return userName.charAt(0).toUpperCase();
};

const props = defineProps({
  collapsed: {
    type: Boolean,
    default: false
  },
  currentFatherMenuTitle: {
    type: String,
    default: ''
  },
  currentMenuTitle: {
    type: String,
    default: ''
  },
  loading: {
    type: Boolean,
    default: false
  },
  layoutSettings: {
    type: Object,
    default: () => ({})
  },
  menuData: {
    type: Array,
    default: () => []
  },
  currentFirstLevelMenu: {
    type: Object,
    default: null
  }
});

const emit = defineEmits([
  'update:collapsed',
  'showSettings',
  'personalCenter',
  'logout',
  'refreshPage',
  'menuSelect',
  'firstLevelMenuSelect',
  'toggleMenu'
]);

// 计算属性
const isTopMode = computed(() => props.layoutSettings?.navMode === 'top');
const isMixMode = computed(() => props.layoutSettings?.navMode === 'mix');
const isDrawerMode = computed(() => props.layoutSettings?.navMode === 'drawer');

const headerClasses = computed(() => ({
  'header-top-mode': isTopMode.value,
  'header-mix-mode': isMixMode.value,
  'header-side-mode': !isTopMode.value && !isMixMode.value
}));

const userAvatar = computed(() => {
  const avatar = userStore.avatar;
  if (!avatar) return '';
  // 如果已经是完整URL，直接返回
  if (avatar.startsWith('http://') || avatar.startsWith('https://')) {
    return avatar;
  }
  // 如果是相对路径，拼接API基础路径
  const baseApi = import.meta.env.VITE_APP_BASE_API || '';
  return baseApi + avatar;
});
const userName = computed(() => userStore.nickName || userStore.userName || '用户');

// 头像加载失败状态
const avatarLoadError = ref(false);

// 智能头像计算属性
const smartAvatar = computed(() => {
  const name = userName.value;
  return {
    hasImage: !avatarLoadError.value && userAvatar.value,
    backgroundColor: getUserAvatarColor(name),
    initial: getUserInitial(name)
  };
});

// 处理头像加载失败
const handleAvatarError = () => {
  avatarLoadError.value = true;
};

// 监听用户头像变化，重置错误状态
watch(() => userStore.avatar, () => {
  avatarLoadError.value = false;
});

// 功能状态
const messageCount = ref(5); // 消息数量

// 组件显示状态
const searchVisible = ref(false);
const messageVisible = ref(false);
const chatVisible = ref(false);

// 全屏处理器引用
const fullscreenHandlerRef = ref(null);

// 菜单组件引用
const topMenuRef = ref(null);
const mixTopMenuRef = ref(null);

// 暴露菜单组件引用
defineExpose({
  topMenuRef,
  mixTopMenuRef
});

const toggleCollapse = () => {
  emit('update:collapsed', !props.collapsed);
};

const refreshCurrentPage = () => {
  emit('refreshPage');
};

// 搜索功能
const showSearch = () => {
  searchVisible.value = true;
};

// 消息功能
const showMessages = () => {
  messageVisible.value = true;
};

// 聊天功能
const showChat = () => {
  chatVisible.value = true;
};

// 全屏功能
const toggleFullscreen = () => {
  if (fullscreenHandlerRef.value) {
    fullscreenHandlerRef.value.toggleFullscreen();
  }
};

// 计算全屏状态
const isFullscreen = computed(() => {
  return fullscreenHandlerRef.value?.isFullscreen?.value || false;
});
</script>

<style lang="less" scoped>
.menu-icon {
  margin-right: 8px;
}

.header-bar {
  background: var(--page-background-gradient);
  border-bottom: 0;
  padding: 0;
  height: 56px !important;
  line-height: 56px;

  .header-left {
    display: flex;
    align-items: center;

    .header-logo {
      display: flex;
      align-items: center;
      padding: 0 24px;
      background: transparent;

      .logo-img {
        height: 32px;
        width: auto;
        margin-right: 12px;
        transition: transform 0.3s ease;

        &:hover {
          transform: scale(1.05);
        }
      }

      .logo-title {
        font-size: 20px;
        font-weight: 600;
        color: var(--primary-color);
      }
    }

    .header-title {
      //margin-left: 16px;
      font-size: 16px;
      color: var(--text-primary);
    }
  }

  .header-menu {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  // 顶部模式样式
  &.header-top-mode {
    .header-left {
      justify-content: flex-start;
    }

    .header-menu {
      flex: 1;
    }
  }

  // 顶部模式样式
  &.header-mix-mode {
    .header-left {
      justify-content: flex-start;
    }

    .header-menu {
      flex: 1;
    }
  }

  .trigger {
    color: var(--text-secondary);
    padding: 0 24px;
    cursor: pointer;
    transition: color 0.3s, transform 0.3s;

    &:hover {
      color: #1890ff;
      transform: rotate(180deg);
    }
  }

  .header-left {
    .header-title {
      color: var(--text-secondary);
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding-right: 12px;
    height: 56px;
    gap: 8px; /* 添加子元素间距 */
    min-width: 0; /* 允许 flex 收缩 */

    .header-icon-btn {
      color: var(--text-secondary);
      padding: 0 12px;
      background: transparent !important;
      transition: color 0.3s, transform 0.3s;

      &:hover,
      &:focus,
      &:active {
        color: var(--primary-color);
        background: transparent !important;
        background-color: transparent !important;
        box-shadow: none !important;
        transform: rotate(360deg);
      }
    }

    .username {
      margin-left: 4px;
      color: var(--text-secondary);
    }

    .ant-dropdown-link {
      transition: all 0.3s ease;
      border-radius: 4px;

      &:hover,
      &:focus,
      &:active {
        background: transparent !important;
        box-shadow: none;
      }
    }

    /* 用户信息区域样式 */
    .user-dropdown {
      margin-right: 12px;
      min-width: 120px; /* 确保有足够的最小宽度 */
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 0 12px;
      height: 40px;
      border-radius: 6px;
      transition: color 0.3s;
      white-space: nowrap; /* 防止用户名换行 */
      max-width: 150px; /* 设置最大宽度 */

      &:hover,
      &:focus,
      &:active {
        background-color: transparent !important;
      }

      .smart-avatar {
        .ant-avatar {
          font-weight: 600;
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }

      .username {
        color: var(--text-primary);
        font-size: 14px;
        font-weight: 500;
        overflow: hidden;
        text-overflow: ellipsis;
        max-width: 80px; /* 用户名最大宽度 */
      }
    }
  }
}
</style>
