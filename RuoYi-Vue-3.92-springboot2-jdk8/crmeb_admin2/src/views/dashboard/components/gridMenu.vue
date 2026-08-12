<template>
  <div class="divBox">
    <el-row :gutter="14">
      <el-col :xs="24" :sm="24" :md="24" :lg="12">
        <el-card class="box-card">
          <div class="header_title">快捷入口</div>
          <div class="nav_grid">
            <div class="nav_grid_item" v-for="(item, index) in quickLinks" :key="index" @click="$router.push(item.url)">
              <div class="pic_badge" :style="{ backgroundColor: item.bgColor }">
                <svg-icon :icon-class="item.icon" class-name="menu-icon" />
              </div>
              <p>{{ item.title }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="24" :lg="12">
        <el-card class="box-card">
          <div class="header_title">系统信息</div>
          <div class="nav_grid">
            <div class="nav_grid_item" v-for="(item, index) in sysInfo" :key="index" @click="$router.push(item.url)">
              <div class="pic_badge" :style="{ backgroundColor: item.bgColor }">
                <svg-icon :icon-class="item.icon" class-name="menu-icon" />
              </div>
              <p>{{ item.title }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      quickLinks: [
        { bgColor: '#1890FF', icon: 'user', title: '用户管理', url: '/system/user' },
        { bgColor: '#1BBE6B', icon: 'peoples', title: '角色管理', url: '/system/role' },
        { bgColor: '#EF9C20', icon: 'tree-table', title: '菜单管理', url: '/system/menu' },
        { bgColor: '#F56C6C', icon: 'tree', title: '部门管理', url: '/system/dept' },
        { bgColor: '#A277FF', icon: 'dict', title: '字典管理', url: '/system/dict' },
        { bgColor: '#E8B600', icon: 'edit', title: '参数设置', url: '/system/config' },
        { bgColor: '#4BCAD5', icon: 'message', title: '通知公告', url: '/system/notice' },
        { bgColor: '#1890FF', icon: 'log', title: '日志管理', url: '/system/log/operlog' },
      ],
      sysInfo: [
        { bgColor: '#1890FF', icon: 'online', title: '在线用户', url: '/system/monitor/online' },
        { bgColor: '#1BBE6B', icon: 'job', title: '定时任务', url: '/system/monitor/job' },
        { bgColor: '#EF9C20', icon: 'druid', title: '数据监控', url: '/system/monitor/druid' },
        { bgColor: '#F56C6C', icon: 'server', title: '服务监控', url: '/system/monitor/server' },
        { bgColor: '#A277FF', icon: 'redis', title: '缓存监控', url: '/system/monitor/cache' },
        { bgColor: '#E8B600', icon: 'code', title: '代码生成', url: '/tool/gen' },
        { bgColor: '#4BCAD5', icon: 'build', title: '表单构建', url: '/tool/build' },
        { bgColor: '#1890FF', icon: 'swagger', title: '系统接口', url: '/tool/swagger' },
      ],
    };
  },
};
</script>
<style lang="scss" scoped>
.ivu-mb {
  margin-bottom: 14px;
}
.dashboard-console-grid {
  text-align: center;
  .ivu-card-body {
    padding: 0;
  }
  i {
    font-size: 32px;
  }
  a {
    display: block;
    color: inherit;
  }
  p {
    margin-top: 8px;
  }
}
.header_title {
  font-size: 16px;
  font-family: PingFangSC-Semibold, PingFang SC;
  font-weight: 600;
  color: #000000;
  padding-left: 8px;
  position: relative;
  &::before {
    position: absolute;
    content: '';
    width: 2px;
    height: 18px;
    background: #1890ff;
    top: 0;
    left: 0;
  }
}
.nav_grid {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  margin-bottom: 10px;
}
.nav_grid_item {
  width: 25%;
  height: 90px;
  margin-top: 30px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  .pic_badge {
    width: 58px;
    height: 58px;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #fff;
    border-radius: 4px;
    .svg-icon {
      font-size: 30px;
      width: 1em;
      height: 1em;
    }
  }
  p {
    height: 17px;
    font-size: 14px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #000000;
    line-height: 17px;
    margin-top: 12px;
  }
}
</style>