<template>
  <div class="divBox">
    <el-row :gutter="14">
      <el-col :xs="24" :sm="24" :md="24" :lg="12">
        <el-card class="box-card">
          <div class="header_title">快捷入口</div>
          <div class="nav_grid">
            <div class="nav_grid_item" v-for="(item, index) in quickLinks" :key="index" @click="$router.push(item.url)">
              <div class="pic_badge" :style="{ backgroundColor: item.bgColor }">
                <i :class="'el-icon-' + item.icon"></i>
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
                <i :class="'el-icon-' + item.icon"></i>
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
        { bgColor: '#1BBE6B', icon: 's-custom', title: '角色管理', url: '/system/role' },
        { bgColor: '#EF9C20', icon: 'menu', title: '菜单管理', url: '/system/menu' },
        { bgColor: '#F56C6C', icon: 'share', title: '部门管理', url: '/system/dept' },
        { bgColor: '#A277FF', icon: 'collection', title: '字典管理', url: '/system/dict' },
        { bgColor: '#E8B600', icon: 'edit', title: '参数设置', url: '/system/config' },
        { bgColor: '#4BCAD5', icon: 'message', title: '通知公告', url: '/system/notice' },
        { bgColor: '#1890FF', icon: 'document', title: '日志管理', url: '/system/log/operlog' },
      ],
      sysInfo: [
        { bgColor: '#1890FF', icon: 'connection', title: '在线用户', url: '/system/monitor/online' },
        { bgColor: '#1BBE6B', icon: 'timer', title: '定时任务', url: '/system/monitor/job' },
        { bgColor: '#EF9C20', icon: 'odometer', title: '数据监控', url: '/system/monitor/druid' },
        { bgColor: '#F56C6C', icon: 'cpu', title: '服务监控', url: '/system/monitor/server' },
        { bgColor: '#A277FF', icon: 'coin', title: '缓存监控', url: '/system/monitor/cache' },
        { bgColor: '#E8B600', icon: 'document-copy', title: '代码生成', url: '/tool/gen' },
        { bgColor: '#4BCAD5', icon: 'document-add', title: '表单构建', url: '/tool/build' },
        { bgColor: '#1890FF', icon: 'connection', title: '系统接口', url: '/tool/swagger' },
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
    i {
      font-size: 30px;
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