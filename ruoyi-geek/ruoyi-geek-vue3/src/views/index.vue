<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <!-- 左侧个人信息 -->
      <el-col :lg="6" :md="8" :sm="24" :xs="24">
        <el-card class="user-info-card">
          <div class="user-profile">
            <el-avatar :size="80" :src="userInfo.avatar || profile" />
            <h2 class="welcome-text">欢迎回来，{{ userInfo.name }}</h2>
            <p class="user-role">{{ userInfo.roleName }}</p>
          </div>
          <div class="user-stats">
            <div class="stat-item">
              <div class="stat-value text-ellipsis" :title="formatDate(userInfo.loginDate) || '暂无'">
                {{ formatDate(userInfo.loginDate) || '暂无' }}
              </div>
              <p class="stat-label">上次登录</p>
            </div>
            <div class="stat-item">
              <div class="stat-value text-ellipsis" :title="userInfo.deptName || '暂无'">
                {{ userInfo.deptName || '暂无' }}
              </div>
              <p class="stat-label">所属部门</p>
            </div>
          </div>
        </el-card>

        <!-- 系统公告卡片 -->
        <el-card class="notice-card hide-on-small" v-loading="noticeLoading">
          <template #header>
            <div class="card-header">
              <span class="header-title">
                <el-icon>
                  <Bell />
                </el-icon>
                系统公告
              </span>
              <el-button v-if="noticeList.length" link @click="viewMoreNotices">
                查看更多<el-icon>
                  <ArrowRight />
                </el-icon>
              </el-button>
            </div>
          </template>
          <div v-if="noticeList.length" class="notice-list">
            <div v-for="item in noticeList" :key="item.noticeId" class="notice-item" @click="showNoticeDetail(item)">
              <el-tag size="small" :type="item.noticeType === '1' ? 'danger' : 'success'">
                {{ item.noticeType === '1' ? '通知' : '公告' }}
              </el-tag>
              <span class="notice-title">{{ item.noticeTitle }}</span>
              <span class="notice-time">{{ parseTime(item.createTime) }}</span>
            </div>
          </div>
          <el-empty v-else description="暂无公告" />
        </el-card>
      </el-col>

      <!-- 右侧内容区 -->
      <el-col :lg="18" :md="16" :sm="24" :xs="24">
        <el-row :gutter="20">
          <!-- 欢迎信息和特色功能 -->
          <el-col :span="24">
            <el-card class="welcome-card">
              <div class="welcome-container">
                <h1 class="welcome-title">欢迎使用 RuoYi-Geek-Vue3</h1>
                <p class="welcome-desc">基于 Vue3、TypeScript、Vite、Element-Plus 的后台管理系统</p>
              </div>

              <el-row :gutter="20" class="feature-section">
                <el-col :lg="8" :md="12" :sm="24" :xs="24" v-for="(feature, index) in features" :key="index">
                  <div class="feature-item">
                    <div class="feature-icon">
                      <el-icon>
                        <component :is="feature.icon" />
                      </el-icon>
                    </div>
                    <h3>{{ feature.title }}</h3>
                    <p>{{ feature.description }}</p>
                  </div>
                </el-col>
              </el-row>
            </el-card>
          </el-col>

          <!-- 移动端公告展示 -->
          <el-col :span="24">
            <el-card class="notice-card show-on-small" v-loading="noticeLoading">
              <template #header>
                <div class="card-header">
                  <span class="header-title">
                    <el-icon>
                      <Bell />
                    </el-icon>
                    系统公告
                  </span>
                  <el-button v-if="noticeList.length" link @click="viewMoreNotices">
                    查看更多<el-icon>
                      <ArrowRight />
                    </el-icon>
                  </el-button>
                </div>
              </template>
              <div v-if="noticeList.length" class="notice-list">
                <div v-for="item in noticeList" :key="item.noticeId" class="notice-item"
                  @click="showNoticeDetail(item)">
                  <el-tag size="small" :type="item.noticeType === '1' ? 'danger' : 'success'">
                    {{ item.noticeType === '1' ? '通知' : '公告' }}
                  </el-tag>
                  <span class="notice-title">{{ item.noticeTitle }}</span>
                  <span class="notice-time">{{ parseTime(item.createTime) }}</span>
                </div>
              </div>
              <el-empty v-else description="暂无公告" />
            </el-card>
          </el-col>

          <!-- 内置功能说明 -->
          <el-col :span="24">
            <el-card class="features-card">
              <template #header>
                <div class="card-header">
                  <span>内置功能说明</span>
                </div>
              </template>
              <el-row :gutter="20">
                <el-col :span="12" v-for="(section, index) in [
                  { header: '内置插件', items: plugins },
                  { header: '内置指令', items: directives }
                ]" :key="index">
                  <div class="feature-group">
                    <h3 class="group-title">{{ section.header }}</h3>
                    <div class="feature-list">
                      <div v-for="(item, idx) in section.items" :key="idx" class="feature-list-item">
                        <h4>{{ item.name }}</h4>
                        <p>{{ item.description }}</p>
                      </div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <!-- 公告详情对话框 -->
    <el-dialog v-model="noticeDialogVisible" :title="currentNotice.noticeTitle" width="50%" destroy-on-close>
      <div v-html="currentNotice.noticeContent"></div>
    </el-dialog>
  </div>
</template>

<script setup name="Index" lang="ts">
import { ref, onMounted } from 'vue'
import { listNotice } from '@/api/system/notice'
import { parseTime } from '@/utils/ruoyi'
import { Bell, ArrowRight } from '@element-plus/icons-vue'
import { formatDate } from '@/utils'
import profile from '@/assets/images/profile.jpg'
import useUserStore from '@/store/modules/user'
import { useRouter, type RouteLocationRaw } from 'vue-router'
import { GeekResponseForList } from '@/types/request'
const router = useRouter()
interface Feature {
  icon: string;
  title: string;
  description: string;
}

interface Plugin {
  name: string;
  description: string;
}

interface Directive {
  name: string;
  description: string;
  usage: string;
}

interface Notice {
  noticeId: number;
  noticeTitle: string;
  noticeType: string;
  noticeContent: string;
  status: string;
  createBy: string;
  createTime: string;
}

// 状态定义
const features = ref<Feature[]>([
  {
    icon: 'Monitor',
    title: '技术先进',
    description: '采用Vue3、TypeScript等最新技术栈，保持与时俱进'
  },
  {
    icon: 'SetUp',
    title: '简单易用',
    description: '开箱即用的后台解决方案，内置完整的权限验证系统'
  },
  {
    icon: 'Document',
    title: '规范开发',
    description: '遵循最佳实践，统一的编码规范，让项目更易维护'
  }
])

const plugins = ref<Plugin[]>([
  {
    name: 'Auth 权限验证',
    description: '提供了权限验证相关方法，包括hasPermi、hasRole等功能，用于控制按钮和功能的访问权限'
  },
  {
    name: 'Cache 缓存',
    description: '提供了浏览器缓存操作方法，支持session和local存储的设置与获取'
  },
  {
    name: 'Modal 弹窗',
    description: '封装了Element Plus的弹窗组件，提供了更便捷的调用方式'
  }
])

const directives = ref<Directive[]>([
  {
    name: 'hasPermi',
    description: '用于控制按钮级别的权限',
    usage: 'v-hasPermi="[\'system:user:add\']"'
  },
  {
    name: 'hasRole',
    description: '用于控制角色级别的权限',
    usage: 'v-hasRole="[\'admin\']"'
  },
  {
    name: 'auth',
    description: '通用权限验证指令',
    usage: 'v-auth="\'system:user:edit\'"'
  }
])

const noticeList = ref<Notice[]>([])
const noticeLoading = ref(false)
const noticeDialogVisible = ref(false)
const currentNotice = ref<Notice>({} as Notice)

// 用户信息store
const userInfo = useUserStore()

// 获取公告列表
const getNoticeList = async () => {
  noticeLoading.value = true
  try {
    const res: GeekResponseForList<Notice> = await listNotice({ pageNum: 1, pageSize: 5 })
    noticeList.value = res.rows
  } catch (error) {
    console.error('获取公告列表失败:', error)
  } finally {
    noticeLoading.value = false
  }
}

// 显示公告详情
const showNoticeDetail = (notice: Notice) => {
  currentNotice.value = notice
  noticeDialogVisible.value = true
}

// 查看更多公告
const viewMoreNotices = () => {
  const route: RouteLocationRaw = { path: '/system/notice' }
  router.push(route)
}

onMounted(() => {
  userInfo.getInfo()
  getNoticeList()
})
</script>

<style scoped lang="scss">
.home {
  padding: clamp(10px, 3vw, 20px);

  .welcome-container {
    text-align: center;
    margin-bottom: 40px;

    .welcome-title {
      font-size: clamp(1.8em, 4vw, 2.5em);
      color: var(--el-text-color-primary);
      margin-bottom: 20px;
    }

    .welcome-desc {
      font-size: clamp(1em, 2vw, 1.2em);
      color: var(--el-text-color-secondary);
    }
  }

  // 响应式间距调整
  :deep(.el-row) {
    margin-left: -10px !important;
    margin-right: -10px !important;

    .el-col {
      padding-left: 10px !important;
      padding-right: 10px !important;
    }
  }

  .feature-section {
    .feature-card {
      margin-bottom: 20px;
      height: 100%;
      text-align: center;
      transition: transform 0.3s;

      &:hover {
        transform: translateY(-5px);
      }

      .feature-icon {
        font-size: 2.5em;
        color: var(--el-color-primary);
        margin-bottom: 20px;
      }

      h3 {
        margin: 10px 0;
        font-size: 1.2em;
        color: var(--el-text-color-primary);
      }

      p {
        color: var(--el-text-color-secondary);
        line-height: 1.5;
      }
    }
  }

  .user-info-card {
    margin-bottom: 20px;

    .user-profile {
      text-align: center;
      padding: 20px 0;

      .welcome-text {
        margin: 15px 0 5px;
        font-size: 1.2em;
        color: var(--el-text-color-primary);
      }

      .user-role {
        color: var(--el-text-color-secondary);
        font-size: 0.9em;
      }
    }

    .user-stats {
      display: flex;
      justify-content: space-around;
      padding: 15px 0;
      border-top: 1px solid var(--el-border-color-lighter);

      .stat-item {
        text-align: center;

        .stat-value {
          font-size: 1.1em;
          color: var(--el-text-color-primary);
          margin-bottom: 5px;
        }

        .stat-label {
          color: var(--el-text-color-secondary);
          font-size: 0.9em;
        }
      }
    }
  }

  .notice-card {
    &.show-on-small {
      display: none;
      margin: 20px 0;
    }

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-title {
        display: flex;
        align-items: center;
        gap: 5px;
        font-weight: bold;
        color: var(--el-text-color-primary);
      }
    }

    .notice-list {
      .notice-item {
        padding: 10px 0;
        border-bottom: 1px solid var(--el-border-color-lighter);
        cursor: pointer;
        transition: all 0.3s;

        &:last-child {
          border-bottom: none;
        }

        &:hover {
          background-color: var(--el-fill-color-lighter);
        }

        .notice-title {
          margin: 0 10px;
          color: var(--el-text-color-primary);
          flex: 1;
        }

        .notice-time {
          font-size: 0.9em;
          color: var(--el-text-color-secondary);
        }
      }
    }
  }

  // 响应式显示控制
  @media (max-width: 768px) {
    .hide-on-small {
      display: none;
    }

    .show-on-small {
      display: block !important;
    }

    .welcome-container {
      margin-bottom: 20px;
    }

    .docs-section {
      .doc-card {
        margin-bottom: 20px;
      }
    }
  }

  @media (max-width: 576px) {
    .user-info-card {
      .user-stats {
        flex-direction: column;
        gap: 15px;

        .stat-item {
          width: 100%;
          padding: 10px 0;
          border-bottom: 1px solid var(--el-border-color-lighter);

          &:last-child {
            border-bottom: none;
          }
        }
      }
    }

    .feature-section {
      .feature-card {
        .feature-icon {
          font-size: 2em;
        }
      }
    }
  }

  .text-ellipsis {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .welcome-card {
    margin-bottom: 20px;

    .welcome-container {
      text-align: center;
      margin-bottom: 30px;

      // ...existing code...
    }

    .feature-section {
      .feature-item {
        text-align: center;
        padding: 20px;
        border-radius: 8px;
        transition: all 0.3s;
        background-color: var(--el-fill-color-light);
        margin-bottom: 20px;

        &:hover {
          transform: translateY(-5px);
          background-color: var(--el-fill-color);
        }

        .feature-icon {
          font-size: 2.5em;
          color: var(--el-color-primary);
          margin-bottom: 15px;
        }

        h3 {
          margin: 10px 0;
          font-size: 1.2em;
          color: var(--el-text-color-primary);
        }

        p {
          color: var(--el-text-color-secondary);
          line-height: 1.5;
          margin: 0;
        }
      }
    }
  }

  .features-card {
    .feature-group {
      margin-bottom: 30px;

      .group-title {
        font-size: 1.2em;
        color: var(--el-text-color-primary);
        margin-bottom: 20px;
        padding-bottom: 10px;
        border-bottom: 1px solid var(--el-border-color-lighter);
      }

      .feature-list {
        .feature-list-item {
          padding: 15px;
          margin-bottom: 15px;
          border-radius: 6px;
          background-color: var(--el-fill-color-light);

          h4 {
            color: var(--el-color-primary);
            margin: 0 0 10px;
          }

          p {
            color: var(--el-text-color-secondary);
            margin: 0 0 10px;
            line-height: 1.5;
          }
        }
      }
    }
  }

  // 响应式调整
  @media (max-width: 768px) {
    .features-card {
      .el-row {
        .el-col {
          width: 100%;
        }
      }
    }
  }
}

// 覆盖卡片样式
:deep(.el-card) {
  --el-card-padding: 20px;

  .el-card__header {
    padding: 15px 20px;
    border-bottom: 1px solid var(--el-border-color-lighter);
  }
}

// 调整卡片间距
.el-card {
  margin-bottom: clamp(15px, 3vw, 20px);

  :deep(.el-card__body) {
    padding: clamp(12px, 2vw, 20px);
  }
}

// 调整对话框响应式
:deep(.el-dialog) {
  @media (max-width: 768px) {
    width: 90% !important;
    margin: 0 auto;
  }
}
</style>
