<template>
  <div class="pt14">
    <div class="divBox normal-page">
      <div class="header">
        <div class="left">
          <i class="el-icon-arrow-left" @click="goBack"></i>
          <div class="invoice-logo"><i class="icon iconfont iconhetong"></i></div>
        </div>
        <div class="right">
          <span class="title">{{ info.name || '--' }}</span>
          <el-tabs v-model="activeName" class="tab" @tab-click="tabChange">
            <el-tab-pane label="任务详情" name="1" />
            <el-tab-pane label="项目资料" name="2" />
            <el-tab-pane label="项目动态" name="3" />
            <el-tab-pane label="项目设置" name="4" />
          </el-tabs>
        </div>
      </div>

      <!-- 我的任务 -->
      <div v-if="activeName == 1">
        <task ref="taskDrawerRef" :programId="Number(id)" :isDrawer="true"></task>
      </div>
      <!-- 项目资料 -->
      <div v-if="activeName == 2">
        <files :programId="info.id"></files>
      </div>
      <!-- 项目动态 -->
      <div v-if="activeName == 3">
        <dynamics :programId="info.id"></dynamics>
      </div>
      <!-- 项目设置 -->
      <div v-if="activeName == 4">
        <!-- 新建项目 -->
        <add-program :type="type" :formInfo="info" @goBack="goBack" @getProgramInfo="getProgramInfo" />
      </div>
    </div>
  </div>
</template>
<script>
import { roterPre } from '@/settings'
import { getProgramInfoApi } from '@/api/program'
export default {
  name: '',
  components: {
    task: () => import('../programTask/index'),
    dynamics: () => import('./dynamics'),
    files: () => import('./components/files'),
    addProgram: () => import('./components/addProgram')
  },

  data() {
    return {
      type: 'info',
      activeName: '1',
      id: 0, // 项目id
      info: {}
    }
  },
  created() {
    if (this.$route.query.id) {
      this.id = this.$route.query.id
      this.getProgramInfo()
    }
  },

  methods: {
    // 获取详情
    async getProgramInfo() {
      const result = await getProgramInfoApi(this.id)
      this.info = result.data
    },
    tabChange() {
      if (this.activeName == 4) {
        this.getProgramInfo()
      }
    },
    goBack() {
      this.activeName = '1'
      this.type = 'info'
      this.$router.push(`${roterPre}/program/programList/index`)
    }
  }
}
</script>
<style scoped lang="scss">
.pt14 {
  padding-top: 2px;
}
.el-icon-arrow-left {
  cursor: pointer;
  font-size: 19px;
  color: #606266;
  margin-right: 8px;
}
.divBox {
  padding: 0;
  background-color: #fff;

  .header {
    width: 100%;
    height: 70px;
    display: flex;
    border-bottom: 1px solid #dcdfe6;
    .left {
      padding: 11px 0 11px 20px;
      display: flex;
      align-items: center;

      .invoice-logo {
        width: 48px;
        height: 48px;
        background: #1890ff;
        border-radius: 4px;
        line-height: 48px;
        text-align: center;
        .iconhetong {
          font-size: 24px;
          color: #fff;
        }
      }
    }
    .right {
      padding: 11px 0 11px 0px;
      margin-left: 13px;
      .title {
        font-family: PingFang SC, PingFang SC;
        font-weight: 500;
        font-size: 17px;
        color: #303133;
        text-align: left;
        font-style: normal;
        text-transform: none;
      }
      .tab {
        ::v-deep .el-tabs__nav-wrap::after {
          height: 0;
        }
        ::v-deep .el-tabs__item {
          line-height: 37px;
          font-family: PingFang SC, PingFang SC;
          font-weight: 400;
          font-size: 13px;
          color: #606266;
        }
        ::v-deep .el-tabs__item.is-active {
          color: #1890ff;
        }
      }
    }
  }
}
</style>
