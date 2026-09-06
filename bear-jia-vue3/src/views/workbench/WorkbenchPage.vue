<template>
  <div class="workbench-container">
    <section class="hero-card surface-card">
      <div class="hero-content">
        <div class="hero-copy">
          <div class="hero-title-row">
            <BearJiaIcon icon="smile-outlined" class="hero-icon" />
            <h2>欢迎回来，{{ displayName }}！</h2>
          </div>
          <p>今天是个美好的一天，开始您的工作吧~</p>
          <div class="hero-meta">
            <span>
              <BearJiaIcon icon="clock-circle-outlined" />
              上次登录：{{ lastLoginTime }}
            </span>
            <span>
              <BearJiaIcon icon="environment-outlined" />
              登录地点：{{ loginLocation }}
            </span>
          </div>
        </div>

        <div class="hero-visual" aria-hidden="true">
          <div class="screen-card">
            <div class="screen-line primary"></div>
            <div class="screen-chart"></div>
          </div>
        </div>

        <div class="hero-actions">
          <a-button type="primary" @click="goToUserManagement">
            <BearJiaIcon icon="user-outlined" />
            用户管理
          </a-button>
          <a-button @click="goToSystemSettings">
            <BearJiaIcon icon="setting-outlined" />
            系统设置
          </a-button>
        </div>
      </div>
    </section>

    <section class="stats-grid">
      <article
        v-for="card in statsCards"
        :key="card.key"
        class="stats-card surface-card"
        @click="card.action"
      >
        <div class="stats-icon-wrap" :class="card.key">
          <BearJiaIcon :icon="card.icon" />
        </div>
        <div class="stats-main">
          <div class="stats-title">{{ card.title }}</div>
          <a-skeleton-button v-if="loading.stats" active size="small" />
          <div v-else class="stats-value">{{ card.value }}</div>
          <div class="stats-footer">
            <span class="stats-trend" :class="card.trendType">
              <BearJiaIcon :icon="card.trendType === 'down' ? 'caret-down-outlined' : 'caret-up-outlined'" />
              {{ card.trend }}
            </span>
            <span>较上周</span>
          </div>
        </div>
      </article>
    </section>

    <section class="content-grid">
      <a-card class="website-card surface-card" :bordered="false">
        <template #title>
          <span class="section-title">我的网站</span>
        </template>
        <template #extra>
          <a-button type="link" size="small" @click="visitAllWebsites">
            <BearJiaIcon icon="global-outlined" />
            访问全部
          </a-button>
        </template>

        <div class="website-list">
          <article
            v-for="site in mySites"
            :key="site.name"
            class="website-item"
            @click="visitWebsite(site.url)"
          >
            <div class="website-icon" :style="{ background: site.color }">
              <BearJiaIcon :icon="site.icon" />
            </div>
            <div class="website-info">
              <div class="website-head">
                <h3>{{ site.name }}</h3>
                <a-badge :status="site.status === 'online' ? 'success' : 'default'" :text="site.status === 'online' ? '在线' : '维护中'" />
              </div>
              <p>{{ site.description }}</p>
              <div class="website-tech">
                <a-tag v-for="tech in site.technologies" :key="tech" :color="getTechColor(tech)">
                  {{ tech }}
                </a-tag>
              </div>
            </div>
          </article>
        </div>
      </a-card>

      <div class="right-column">
        <a-card class="notice-card surface-card" :bordered="false">
          <template #title>
            <span class="section-title">最新通知</span>
          </template>
          <template #extra>
            <a-button type="link" size="small" @click="goToNoticeManagement">查看全部</a-button>
          </template>

          <a-list :data-source="noticeList" :loading="loading.notice" size="small">
            <template #renderItem="{ item }">
              <a-list-item class="notice-item" @click="viewNotice(item)">
                <div class="notice-row">
                  <div class="notice-dot" :class="item.noticeType === '2' ? 'orange' : 'blue'"></div>
                  <div class="notice-main">
                    <div class="notice-title">
                      <span>{{ getNoticeTypeName(item.noticeType) }}</span>
                      {{ item.noticeTitle }}
                    </div>
                    <div class="notice-time">
                      <BearJiaIcon icon="clock-circle-outlined" />
                      {{ formatTime(item.createTime) }}
                    </div>
                  </div>
                </div>
              </a-list-item>
            </template>
            <template #loadMore>
              <div v-if="noticeList.length === 0 && !loading.notice" class="empty-notice">
                <a-empty description="暂无通知" :image="false" />
              </div>
            </template>
          </a-list>
        </a-card>

        <a-card class="system-info-card surface-card" :bordered="false">
          <template #title>
            <span class="section-title">系统信息</span>
          </template>
          <div class="system-info">
            <div v-for="item in systemInfoItems" :key="item.label" class="info-item">
              <span class="info-label">{{ item.label }}</span>
              <span class="info-value">{{ item.value }}</span>
            </div>
          </div>
        </a-card>
      </div>
    </section>
  </div>
</template>

<script setup>
import {computed, onMounted, onUnmounted, reactive, ref} from 'vue';
import {useRouter} from 'vue-router';
import {useUserStore} from '@/stores/user';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';
import dayjs from 'dayjs';
import { SYSTEM_INFO } from '@/config/system.config';
import {listUser} from '@/api/system/user';
import {listRole} from '@/api/system/role';
import {listMenu} from '@/api/system/menu';
import {listNotice} from '@/api/system/notice';
import {list as listOnlineUsers} from '@/api/monitor/online';

const router = useRouter();
const userStore = useUserStore();
const systemInfo = ref(SYSTEM_INFO);
const loading = reactive({ stats: false, notice: false });
const statistics = reactive({ userCount: 0, roleCount: 0, menuCount: 0, onlineCount: 0 });
const noticeList = ref([]);
const currentTime = ref('');
const timeInterval = ref(null);

const mySites = ref([
  { name: 'JavaXiaoBear博客', description: '技术分享与学习笔记', url: 'https://javaxiaobear.cn', icon: 'read-outlined', color: 'linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%)', status: 'online', technologies: ['Vue', 'Spring Boot', 'MySQL'] },
  { name: 'BearJia Vue3', description: '高颜值现代化后台管理', url: 'https://gitee.com/javaxiaobear_admin/bear-jia-vue3.git', icon: 'tool-outlined', color: 'linear-gradient(135deg, #38bdf8 0%, #06b6d4 100%)', status: 'online', technologies: ['Ant Design', 'TypeScript', 'Vite'] },
  { name: '小熊学 Java', description: '学习指南+面试手册', url: 'https://javaxiaobear.cn', icon: 'api-outlined', color: 'linear-gradient(135deg, #34d399 0%, #22c55e 100%)', status: 'online', technologies: ['Java', '微服务', '分布式'] }
]);

const userInfo = computed(() => userStore.userInfo || {});
const displayName = computed(() => userInfo.value.nickName || userInfo.value.userName || '小熊学 Java');
const lastLoginTime = computed(() => userInfo.value.loginDate ? dayjs(userInfo.value.loginDate).format('YYYY-MM-DD HH:mm:ss') : '首次登录');
const loginLocation = computed(() => userInfo.value.loginIp || '未知');

const goToUserManagement = () => router.push('/system/user');
const goToRoleManagement = () => router.push('/system/role');
const goToMenuManagement = () => router.push('/system/menu');
const goToOnlineUsers = () => router.push('/monitor/online');
const goToNoticeManagement = () => router.push('/system/notice');
const goToSystemSettings = () => router.push('/system/config');

const statsCards = computed(() => [
  { key: 'user', title: '用户总数', value: statistics.userCount, icon: 'user-outlined', trend: '5.2%', trendType: 'up', action: goToUserManagement },
  { key: 'role', title: '角色数量', value: statistics.roleCount, icon: 'team-outlined', trend: '2.1%', trendType: 'up', action: goToRoleManagement },
  { key: 'menu', title: '菜单数量', value: statistics.menuCount, icon: 'profile-outlined', trend: '1.5%', trendType: 'up', action: goToMenuManagement },
  { key: 'online', title: '在线用户', value: statistics.onlineCount, icon: 'wifi-outlined', trend: '0.8%', trendType: 'down', action: goToOnlineUsers }
]);

const systemInfoItems = computed(() => [
  { label: '系统版本：', value: `${systemInfo.value.name} v${systemInfo.value.version}` },
  { label: '基于框架：', value: 'Vue 3.4 + TypeScript' },
  { label: '构建工具：', value: 'Vite 5.x' },
  { label: 'UI 组件库：', value: 'Ant Design Vue' },
  { label: '后端框架：', value: 'Spring Boot + MyBatis' },
  { label: '服务器时间：', value: currentTime.value }
]);

const getStatistics = async () => {
  loading.stats = true;
  try {
    const [userRes, roleRes, menuRes, onlineRes] = await Promise.allSettled([listUser({pageNum: 1, pageSize: 1}), listRole({pageNum: 1, pageSize: 1}), listMenu({}), listOnlineUsers({})]);
    if (userRes.status === 'fulfilled') statistics.userCount = userRes.value.total || 0;
    if (roleRes.status === 'fulfilled') statistics.roleCount = roleRes.value.total || 0;
    if (menuRes.status === 'fulfilled') statistics.menuCount = Array.isArray(menuRes.value) ? menuRes.value.length : 0;
    if (onlineRes.status === 'fulfilled') statistics.onlineCount = onlineRes.value.total || 0;
  } catch (error) { console.error('获取统计数据失败:', error); }
  finally { loading.stats = false; }
};

const getNoticeData = async () => {
  loading.notice = true;
  try { const response = await listNotice({pageNum: 1, pageSize: 5, status: '0'}); noticeList.value = response.rows || []; }
  catch (error) { console.error('获取通知数据失败:', error); noticeList.value = []; }
  finally { loading.notice = false; }
};

const getTechColor = (tech) => ({ 'Vue': 'green', 'Spring Boot': 'orange', 'MySQL': 'blue', 'Ant Design': 'blue', 'Vite': 'purple', 'TypeScript': 'blue', 'Java': 'orange' }[tech] || 'default');
const visitWebsite = (url) => window.open(url, '_blank');
const visitAllWebsites = () => window.open('https://javaxiaobear.cn', '_blank');
const getNoticeTypeName = (type) => ({ '1': '通知', '2': '公告' }[type] || '通知');
const formatTime = (time) => { if (!time) return ''; const diff = dayjs().diff(dayjs(time), 'minute'); if (diff < 1) return '刚刚'; if (diff < 60) return `${diff}分钟前`; if (diff < 1440) return `${Math.floor(diff / 60)}小时前`; return dayjs(time).format('MM-DD HH:mm'); };
const updateCurrentTime = () => { currentTime.value = dayjs().format('YYYY-MM-DD HH:mm:ss'); };
const viewNotice = (notice) => console.log('查看通知:', notice);

onMounted(() => { getStatistics(); getNoticeData(); updateCurrentTime(); timeInterval.value = setInterval(updateCurrentTime, 1000); });
onUnmounted(() => { if (timeInterval.value) clearInterval(timeInterval.value); });
</script>

<style lang="less" scoped>
.workbench-container {
  min-height: 100%;
  padding: 16px 18px 20px;
  overflow-x: hidden;
  background: var(--page-background-gradient);
}

.surface-card {
  background: var(--card-background);
  border: 1px solid rgba(226, 232, 240, 0.95);
  border-radius: 14px;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.06);
  transition: transform 0.22s ease, box-shadow 0.22s ease, border-color 0.22s ease;

  &:hover {
    border-color: rgba(59, 130, 246, 0.24);
    box-shadow: 0 16px 38px rgba(15, 23, 42, 0.09);
  }
}

.hero-card {
  position: relative;
  min-height: 138px;
  margin-bottom: 14px;
  padding: 28px 30px;
  overflow: hidden;

  &::before {
    position: absolute;
    inset: 0;
    content: '';
    background:
      linear-gradient(90deg, transparent 52%, var(--header-background) 100%),
      radial-gradient(circle at 72% 42%, rgba(96, 165, 250, 0.16), transparent 28%);
    pointer-events: none;
  }

  .hero-content {
    position: relative;
    z-index: 1;
    display: grid;
    grid-template-columns: minmax(0, 1fr) 240px auto;
    gap: 24px;
    align-items: center;
  }

  .hero-title-row {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;

    .hero-icon {
      color: #f59e0b;
      font-size: 24px;
      filter: drop-shadow(0 4px 8px rgba(250, 204, 21, 0.28));
    }

    h2 {
      margin: 0;
      color: #111827;
      font-size: 25px;
      font-weight: 750;
      letter-spacing: -0.02em;
    }
  }

  .hero-copy p {
    margin: 0 0 18px;
    color: #64748b;
    font-size: 14px;
  }

  .hero-meta {
    display: flex;
    flex-wrap: wrap;
    gap: 22px;
    color: #64748b;
    font-size: 13px;

    span {
      display: inline-flex;
      align-items: center;
      gap: 6px;
    }
  }

  .hero-visual {
    display: flex;
    justify-content: center;
    opacity: 0.8;

    .screen-card {
      position: relative;
      width: 104px;
      height: 76px;
      border: 2px solid rgba(191, 219, 254, 0.9);
      border-radius: 14px;
      background: rgba(255, 255, 255, 0.68);
      box-shadow: 0 14px 35px rgba(59, 130, 246, 0.16);
      transform: perspective(180px) rotateY(-8deg);

      &::after {
        position: absolute;
        left: 36px;
        bottom: -18px;
        width: 32px;
        height: 12px;
        content: '';
        border-bottom: 2px solid rgba(191, 219, 254, 0.9);
        border-left: 2px solid rgba(191, 219, 254, 0.9);
        border-right: 2px solid rgba(191, 219, 254, 0.9);
        border-radius: 0 0 10px 10px;
      }
    }

    .screen-line {
      width: 52px;
      height: 6px;
      margin: 17px 0 0 18px;
      border-radius: 999px;
      background: rgba(147, 197, 253, 0.72);
    }

    .screen-chart {
      width: 58px;
      height: 28px;
      margin: 10px 0 0 23px;
      border-bottom: 3px solid rgba(96, 165, 250, 0.45);
      border-left: 3px solid rgba(96, 165, 250, 0.25);
      clip-path: polygon(0 76%, 26% 44%, 49% 62%, 76% 20%, 100% 36%, 100% 100%, 0 100%);
      background: linear-gradient(180deg, rgba(147, 197, 253, 0.4), rgba(219, 234, 254, 0.26));
    }
  }

  .hero-actions {
    display: flex;
    gap: 12px;

    .ant-btn {
      height: 40px;
      padding: 0 18px;
      border-radius: 8px;
      font-weight: 600;
      box-shadow: none;
    }
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
  margin-bottom: 14px;

  .stats-card {
    display: flex;
    align-items: center;
    gap: 18px;
    min-height: 106px;
    padding: 20px 22px;
    cursor: pointer;

    &:hover {
      transform: translateY(-2px);
    }
  }

  .stats-icon-wrap {
    display: grid;
    flex: 0 0 54px;
    width: 54px;
    height: 54px;
    place-items: center;
    border-radius: 50%;
    font-size: 26px;

    &.user {
      color: #2563eb;
      background: #eef2ff;
    }

    &.role {
      color: #16a34a;
      background: #dcfce7;
    }

    &.menu {
      color: #f59e0b;
      background: #fef3c7;
    }

    &.online {
      color: #ef4444;
      background: #fee2e2;
    }
  }

  .stats-title {
    margin-bottom: 6px;
    color: #334155;
    font-size: 13px;
    font-weight: 650;
  }

  .stats-value {
    color: #0f172a;
    font-size: 30px;
    font-weight: 760;
    line-height: 1.05;
  }

  .stats-footer {
    display: flex;
    align-items: center;
    gap: 7px;
    margin-top: 10px;
    color: #94a3b8;
    font-size: 12px;
  }

  .stats-trend {
    display: inline-flex;
    align-items: center;
    gap: 3px;
    padding: 1px 6px;
    border-radius: 999px;
    font-weight: 650;

    &.up {
      color: #16a34a;
      background: rgba(34, 197, 94, 0.1);
    }

    &.down {
      color: #ef4444;
      background: rgba(239, 68, 68, 0.1);
    }
  }
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 360px;
  gap: 14px;
  align-items: stretch;

  :deep(.ant-card) {
    border-radius: 14px;
  }

  :deep(.ant-card-head) {
    min-height: 54px;
    border-bottom: 1px solid rgba(226, 232, 240, 0.74);
  }

  :deep(.ant-card-body) {
    padding: 18px 20px 20px;
  }

  .section-title {
    position: relative;
    display: inline-flex;
    align-items: center;
    padding-left: 12px;
    color: #0f172a;
    font-size: 16px;
    font-weight: 720;

    &::before {
      position: absolute;
      left: 0;
      width: 4px;
      height: 17px;
      content: '';
      border-radius: 999px;
      background: #2563eb;
    }
  }
}

.website-card {
  min-height: 292px;

  .website-list {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 14px;
  }

  .website-item {
    display: flex;
    gap: 14px;
    min-height: 108px;
    padding: 16px;
    border: 1px solid transparent;
    border-radius: 14px;
    cursor: pointer;
    transition: all 0.22s ease;

    &:hover {
      border-color: rgba(59, 130, 246, 0.2);
      background: rgba(248, 250, 252, 0.88);
      transform: translateY(-2px);
    }
  }

  .website-icon {
    display: grid;
    flex: 0 0 46px;
    width: 46px;
    height: 46px;
    place-items: center;
    border-radius: 12px;
    color: #fff;
    font-size: 22px;
    box-shadow: 0 10px 20px rgba(15, 23, 42, 0.1);
  }

  .website-info {
    min-width: 0;
    flex: 1;
  }

  .website-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 10px;

    h3 {
      margin: 0;
      overflow: hidden;
      color: #111827;
      font-size: 15px;
      font-weight: 720;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  p {
    margin: 5px 0 10px;
    color: #64748b;
    font-size: 12px;
  }

  .website-tech {
    display: flex;
    flex-wrap: wrap;
    gap: 5px;

    :deep(.ant-tag) {
      margin-inline-end: 0;
      border-radius: 6px;
      font-size: 11px;
    }
  }
}

.right-column {
  display: grid;
  gap: 14px;
  grid-template-rows: minmax(132px, auto) minmax(146px, auto);
}

.notice-card {
  .notice-item {
    padding: 9px 0 !important;
    cursor: pointer;

    &:hover .notice-title {
      color: #2563eb;
    }
  }

  .notice-row {
    display: flex;
    width: 100%;
    gap: 10px;
  }

  .notice-dot {
    flex: 0 0 8px;
    width: 8px;
    height: 8px;
    margin-top: 7px;
    border-radius: 50%;

    &.blue {
      background: #2563eb;
    }

    &.orange {
      background: #f59e0b;
    }
  }

  .notice-main {
    min-width: 0;
    flex: 1;
  }

  .notice-title {
    overflow: hidden;
    color: #111827;
    font-size: 13px;
    font-weight: 550;
    text-overflow: ellipsis;
    white-space: nowrap;
    transition: color 0.2s ease;

    span {
      margin-right: 6px;
      color: #64748b;
      font-weight: 650;
    }
  }

  .notice-time {
    display: flex;
    align-items: center;
    gap: 5px;
    margin-top: 5px;
    color: #94a3b8;
    font-size: 12px;
  }

  .empty-notice {
    padding: 10px 0 2px;
  }
}

.system-info {
  .info-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 14px;
    padding: 7px 0;
    color: #64748b;
    font-size: 13px;
  }

  .info-value {
    max-width: 210px;
    overflow: hidden;
    color: #0f172a;
    font-weight: 600;
    text-align: right;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

:global(.dark-theme) .workbench-container {
  background: linear-gradient(180deg, rgba(15, 23, 42, 0.96), rgba(2, 6, 23, 0.98));

  .surface-card {
    background: rgba(15, 23, 42, 0.82);
    border-color: rgba(148, 163, 184, 0.18);
    box-shadow: 0 12px 34px rgba(0, 0, 0, 0.3);
  }

  .hero-title-row h2,
  .stats-value,
  .content-grid .section-title,
  .website-card .website-head h3,
  .notice-card .notice-title,
  .system-info .info-value {
    color: #e5e7eb;
  }

  .hero-copy p,
  .hero-meta,
  .stats-title,
  .website-card p,
  .notice-card .notice-title span,
  .system-info .info-item {
    color: #94a3b8;
  }

  .website-card .website-item:hover {
    background: rgba(30, 41, 59, 0.72);
  }
}

@media (max-width: 1360px) {
  .hero-card .hero-content {
    grid-template-columns: minmax(0, 1fr) auto;
  }

  .hero-card .hero-visual {
    display: none;
  }
}

@media (max-width: 1180px) {
  .stats-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .content-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .workbench-container {
    padding: 12px;
  }

  .hero-card {
    padding: 22px;

    .hero-content {
      grid-template-columns: 1fr;
    }

    .hero-actions {
      flex-wrap: wrap;
    }
  }

  .stats-grid,
  .website-card .website-list {
    grid-template-columns: 1fr;
  }
}
</style>
