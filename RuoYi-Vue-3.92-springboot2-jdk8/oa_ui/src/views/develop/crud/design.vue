<template>
  <!-- 实体设计页面 -->
  <div class="divBox">
    <el-card class="card-box" :body-style="{ padding: '0 0 20px 0 ' }">
      <div class="header">
        <div class="left">
          <i class="el-icon-arrow-left" @click="backFn"></i>
          <div class="invoice-logo"><i class="icon iconfont iconshitosheji"></i></div>
        </div>
        <div class="right">
          <span class="title">实体设计</span>
          <el-tabs v-model="activeName" class="tab">
            <el-tab-pane v-for="(item, index) in tabArray" :key="index" :name="item.value">
              <div slot="label">
                <span>{{ item.label }}</span>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
      <div class="ml20 mr20">
        <!-- 实体属性 -->
        <basic-setting v-if="activeName == 1" :infoData="info"></basic-setting>
        <!-- 字段设计 -->
        <field-setting class="mt20" v-if="activeName == 2" :infoData="info"></field-setting>
        <!-- 表单设计 -->
        <form-design class="mt20" v-if="activeName == 3"></form-design>
        <!-- 列表设计 -->
        <list-setting v-if="activeName == 4" ref="listSetting" :infoData="info"></list-setting>
        <!-- 流程设计 -->
        <process-setting class="mt20" v-if="activeName == 5" :infoData="info"></process-setting>
        <!-- 触发器设计 -->
        <event-setting class="mt20" v-if="activeName == 6" :infoData="info"></event-setting>
      </div>
    </el-card>
  </div>
</template>
<script>
import { roterPre } from '@/settings'
import basicSetting from './components/basicSetting'
import fieldSetting from './components/fieldSetting'
import eventSetting from './components/eventSetting'
import processSetting from './components/processSetting'
import listSetting from './components/listSetting'
import { databaseInfoApi } from '@/api/develop'
import formDesign from '@/components/form-designer'
export default {
  components: { basicSetting, fieldSetting, formDesign, processSetting, eventSetting, listSetting },
  data() {
    return {
      activeName: '1',
      info: {},
      tabArray: [
        { label: '实体属性', value: '1', number: 1 },
        { label: '字段设计', value: '2', number: 2 },
        { label: '表单设计', value: '3', number: 3 },
        { label: '列表设计', value: '4', number: 4 },
        { label: '流程设计', value: '5', number: 5 },
        { label: '触发器设计', value: '6', number: 6 }
      ]
    }
  },
  watch: {
    info(val) {
      if (val.crud_id !== 0) {
        this.tabArray.splice(2, 4)
      }
    }
  },

  mounted() {
    this.id = this.$route.query.id
    this.getInfo()
    if (this.$route.query.tabIndex) {
      setTimeout(() => {
        this.activeName = this.$route.query.tabIndex + ''
      }, 200)
    }
  },

  methods: {
    // 返回页面
    backFn() {
      this.$router.push({
        path: `${roterPre}/develop/crud`,
        query: {
          tab: this.$route.query.tab
        }
      })
    },
    async getInfo() {
      const data = await databaseInfoApi(this.id)
      this.info = data.data
    },
    handleClick() {}
  }
}
</script>
<style scoped lang="scss">
.examineTabs {
  ::v-deep .el-tabs__nav-wrap::after {
    background-color: #fff;
  }
  .sp1 {
    width: 16px;
    height: 16px;
    border: 1px solid #999999;
    border-radius: 50%;
    display: inline-block;
    text-align: center;
    line-height: 14px;
    font-size: 12px;
  }
  ::v-deep.el-tabs__nav-scroll {
    display: flex;
    justify-content: left;
  }
  ::v-deep .el-tabs__item {
    color: #909399;
    font-weight: 500;
    font-size: 14px;
    margin-bottom: 10px;
    &.is-active {
      color: #1890ff;
      .sp1 {
        border-color: #1890ff;
        background-color: #1890ff;
        color: #fff;
      }
    }
  }
}
::v-deep .el-icon-back {
  display: none;
}
::v-deep .el-tabs__header {
  margin: 0;
}
::v-deep .el-page-header__content {
  font-size: 16px;
  font-weight: 500;
}
::v-deep .el-tabs__active-bar {
  height: 3px;
}
::v-deep.el-page-header {
  margin-top: 10px;
}
.pt20 {
  padding-top: 20px;
}
.card-box {
  // margin-top: 14px;
  min-height: calc(100vh - 76px);
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
}
.header {
  width: 100%;
  height: 70px;
  display: flex;
  border-bottom: 1px solid #dcdfe6;
  .el-icon-arrow-left {
    color: #606266;
    font-size: 13px;
    margin-right: 10px;
    cursor: pointer;
  }
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
      .iconshitosheji {
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
</style>
