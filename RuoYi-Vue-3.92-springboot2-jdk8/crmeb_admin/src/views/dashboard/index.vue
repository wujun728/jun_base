<template>
  <div class="dashboard-container">
    <el-row :gutter="14">
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="box-card">
          <div class="header_title">欢迎使用管理系统</div>
          <div class="welcome-content">
            <p class="welcome-text">欢迎回来，{{ userName }}！</p>
            <p class="welcome-desc">当前时间：{{ currentTime }}</p>
            <el-divider />
            <div class="nav_grid">
              <div class="nav_grid_item" v-for="(item, index) in quickLinks" :key="index" @click="navigatorTo(item.url)">
                <div class="pic_badge" :style="{ backgroundColor: item.bgColor }">
                  <i :class="item.icon"></i>
                </div>
                <p>{{ item.title }}</p>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="box-card">
          <div class="header_title">系统信息</div>
          <div class="system-info">
            <el-descriptions :column="1" size="small" border>
              <el-descriptions-item label="系统名称">若依管理系统</el-descriptions-item>
              <el-descriptions-item label="前端框架">Vue 2.6 + Element UI</el-descriptions-item>
              <el-descriptions-item label="后端框架">Spring Boot + MyBatis</el-descriptions-item>
              <el-descriptions-item label="布局主题">CRMEB 四套布局</el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {
      currentTime: '',
      timer: null,
      quickLinks: [
        { bgColor: '#1890FF', icon: 'el-icon-user', title: '用户管理', url: '/system/user' },
        { bgColor: '#1BBE6B', icon: 'el-icon-postcard', title: '角色管理', url: '/system/role' },
        { bgColor: '#EF9C20', icon: 'el-icon-menu', title: '菜单管理', url: '/system/menu' },
        { bgColor: '#F56C6C', icon: 'el-icon-document', title: '部门管理', url: '/system/dept' },
        { bgColor: '#A277FF', icon: 'el-icon-tickets', title: '岗位管理', url: '/system/post' },
        { bgColor: '#E8B600', icon: 'el-icon-collection', title: '字典管理', url: '/system/dict' },
        { bgColor: '#4BCAD5', icon: 'el-icon-set-up', title: '参数设置', url: '/system/config' },
        { bgColor: '#1890FF', icon: 'el-icon-notebook-2', title: '通知公告', url: '/system/notice' },
      ]
    }
  },
  computed: {
    userName() {
      return this.$store.state.user.nickName || this.$store.state.user.name || '管理员'
    }
  },
  mounted() {
    this.updateTime()
    this.timer = setInterval(this.updateTime, 1000)
  },
  beforeDestroy() {
    if (this.timer) clearInterval(this.timer)
  },
  methods: {
    updateTime() {
      const now = new Date()
      this.currentTime = now.toLocaleString('zh-CN')
    },
    navigatorTo(path) {
      this.$router.push(path)
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 14px 0;
}
.box-card {
  margin-bottom: 14px;
}
.header_title {
  font-size: 16px;
  font-weight: 600;
  color: #000;
  padding-left: 8px;
  position: relative;
  margin-bottom: 20px;
  &::before {
    position: absolute;
    content: '';
    width: 2px;
    height: 18px;
    background: var(--prev-color-primary);
    top: 0;
    left: 0;
  }
}
.welcome-content {
  .welcome-text {
    font-size: 18px;
    color: #333;
    font-weight: 500;
  }
  .welcome-desc {
    font-size: 13px;
    color: #999;
    margin-top: 8px;
  }
}
.nav_grid {
  display: flex;
  flex-wrap: wrap;
}
.nav_grid_item {
  width: 25%;
  height: 90px;
  margin-top: 14px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  .pic_badge {
    width: 48px;
    height: 48px;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #fff;
    border-radius: 4px;
    i {
      font-size: 24px;
    }
  }
  p {
    font-size: 13px;
    color: #000;
    margin-top: 10px;
  }
  &:hover {
    .pic_badge {
      opacity: 0.8;
    }
  }
}
.system-info {
  ::v-deep .el-descriptions {
    .el-descriptions-item__label {
      width: 100px;
    }
  }
}
</style>