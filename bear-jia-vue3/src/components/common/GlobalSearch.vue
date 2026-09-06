<template>
  <a-modal
    :open="visible"
    @update:open="$emit('update:visible', $event)"
    title="全局搜索"
    :footer="null"
    width="600px"
    :destroy-on-close="true"
    class="global-search-modal"
  >
    <div class="search-container">
      <!-- 搜索输入框 -->
      <a-input-search
        v-model:value="searchKeyword"
        placeholder="搜索菜单、页面、功能..."
        size="large"
        @search="handleSearch"
        @input="handleInput"
        class="search-input"
      >
        <template #prefix>
          <SearchOutlined />
        </template>
      </a-input-search>

      <!-- 搜索结果 -->
      <div class="search-results" v-if="searchResults.length > 0">
        <div class="result-category">
          <h4>搜索结果</h4>
          <div class="result-list">
            <div
              v-for="(item, index) in searchResults"
              :key="index"
              class="result-item"
              @click="handleResultClick(item)"
            >
              <div class="result-icon">
                <BearJiaIcon :icon="item.icon || 'FileOutlined'" />
              </div>
              <div class="result-content">
                <div class="result-title">{{ item.title }}</div>
                <div class="result-path">{{ item.path }}</div>
              </div>
              <div class="result-type">{{ item.type }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 快捷操作 -->
      <div class="quick-actions" v-if="!searchKeyword">
        <h4>快捷操作</h4>
        <div class="action-list">
          <div class="action-item" @click="goToUserManagement">
            <UserOutlined />
            <span>用户管理</span>
          </div>
          <div class="action-item" @click="goToRoleManagement">
            <TeamOutlined />
            <span>角色管理</span>
          </div>
          <div class="action-item" @click="goToSystemSettings">
            <SettingOutlined />
            <span>系统设置</span>
          </div>
          <div class="action-item" @click="goToOperationLog">
            <FileTextOutlined />
            <span>操作日志</span>
          </div>
        </div>
      </div>

      <!-- 搜索历史 -->
      <div class="search-history" v-if="searchHistory.length > 0 && !searchKeyword">
        <h4>最近搜索</h4>
        <div class="history-list">
          <a-tag
            v-for="(item, index) in searchHistory"
            :key="index"
            @click="searchKeyword = item"
            class="history-tag"
          >
            {{ item }}
          </a-tag>
        </div>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import { BearJiaIcon } from '@/utils/BearJiaIcon.js';
import {
  SearchOutlined,
  UserOutlined,
  TeamOutlined,
  SettingOutlined,
  FileTextOutlined
} from '@ant-design/icons-vue';

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['update:visible']);

const router = useRouter();

// 搜索状态
const searchKeyword = ref('');
const searchResults = ref([]);
const searchHistory = ref(['用户管理', '角色管理', '操作日志']);

// 模拟搜索数据
const searchData = [
  { title: '用户管理', path: '/system/user', type: '菜单', icon: 'UserOutlined' },
  { title: '角色管理', path: '/system/role', type: '菜单', icon: 'TeamOutlined' },
  { title: '菜单管理', path: '/system/menu', type: '菜单', icon: 'MenuOutlined' },
  { title: '部门管理', path: '/system/dept', type: '菜单', icon: 'ApartmentOutlined' },
  { title: '操作日志', path: '/monitor/operlog', type: '菜单', icon: 'FileTextOutlined' },
  { title: '登录日志', path: '/monitor/logininfor', type: '菜单', icon: 'LoginOutlined' },
  { title: '在线用户', path: '/monitor/online', type: '菜单', icon: 'UserSwitchOutlined' },
  { title: '定时任务', path: '/monitor/job', type: '菜单', icon: 'ScheduleOutlined' },
];

// 处理搜索
const handleSearch = (value) => {
  if (value.trim()) {
    // 添加到搜索历史
    if (!searchHistory.value.includes(value)) {
      searchHistory.value.unshift(value);
      if (searchHistory.value.length > 5) {
        searchHistory.value.pop();
      }
    }
  }
};

// 处理输入
const handleInput = (e) => {
  const value = e.target.value;
  if (value.trim()) {
    // 模糊搜索
    searchResults.value = searchData.filter(item =>
      item.title.toLowerCase().includes(value.toLowerCase()) ||
      item.path.toLowerCase().includes(value.toLowerCase())
    );
  } else {
    searchResults.value = [];
  }
};

// 处理结果点击
const handleResultClick = (item) => {
  router.push(item.path);
  emit('update:visible', false);
};

// 快捷操作
const goToUserManagement = () => {
  router.push('/system/user');
  emit('update:visible', false);
};

const goToRoleManagement = () => {
  router.push('/system/role');
  emit('update:visible', false);
};

const goToSystemSettings = () => {
  router.push('/system/config');
  emit('update:visible', false);
};

const goToOperationLog = () => {
  router.push('/monitor/operlog');
  emit('update:visible', false);
};

// 监听 visible 变化
watch(() => props.visible, (newVal) => {
  if (newVal) {
    // 打开时清空搜索
    searchKeyword.value = '';
    searchResults.value = [];
  }
});
</script>

<style scoped>
.global-search-modal {
  .search-container {
    .search-input {
      margin-bottom: 20px;
    }

    .search-results {
      margin-bottom: 20px;

      .result-category h4 {
        margin-bottom: 12px;
        color: #666;
        font-size: 14px;
      }

      .result-list {
        .result-item {
          display: flex;
          align-items: center;
          padding: 12px;
          border-radius: 6px;
          cursor: pointer;
          transition: background 0.3s;

          &:hover {
            background: #f5f5f5;
          }

          .result-icon {
            margin-right: 12px;
            font-size: 16px;
            color: var(--primary-color);
          }

          .result-content {
            flex: 1;

            .result-title {
              font-weight: 500;
              margin-bottom: 4px;
            }

            .result-path {
              font-size: 12px;
              color: #999;
            }
          }

          .result-type {
            font-size: 12px;
            color: #666;
            background: #f0f0f0;
            padding: 2px 8px;
            border-radius: 4px;
          }
        }
      }
    }

    .quick-actions {
      margin-bottom: 20px;

      h4 {
        margin-bottom: 12px;
        color: #666;
        font-size: 14px;
      }

      .action-list {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 12px;

        .action-item {
          display: flex;
          align-items: center;
          padding: 12px;
          border: 1px solid #f0f0f0;
          border-radius: 6px;
          cursor: pointer;
          transition: all 0.3s;

          &:hover {
            border-color: var(--primary-color);
            background: #f0f9ff;
          }

          .anticon {
            margin-right: 8px;
            color: var(--primary-color);
          }

          span {
            font-size: 14px;
          }
        }
      }
    }

    .search-history {
      h4 {
        margin-bottom: 12px;
        color: #666;
        font-size: 14px;
      }

      .history-list {
        .history-tag {
          margin-right: 8px;
          margin-bottom: 8px;
          cursor: pointer;

          &:hover {
            color: var(--primary-color);
            border-color: var(--primary-color);
          }
        }
      }
    }
  }
}
</style>
