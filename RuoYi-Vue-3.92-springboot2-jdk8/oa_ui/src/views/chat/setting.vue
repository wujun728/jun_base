<template>
  <div class="divBox">
    <el-card class="normal-page" body-style="padding:0 0 0 0; ">
      <div class="header">
        <div class="flex" style="height: 69px">
          <div class="left">
            <i class="el-icon-arrow-left" @click="backFn"></i>
            <div class="invoice-logo"><i class="icon iconfont iconshitosheji"></i></div>
          </div>
          <div class="right">
            <span class="title">{{ info.name }}</span>
            <el-tabs v-model="activeName" class="tab">
              <el-tab-pane v-for="(item, index) in tabArray" :key="index" :name="item.value">
                <div slot="label">
                  <span>{{ item.label }}</span>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
        <div>
          <el-button plain size="small" v-loading="loading" @click="submitOk('')">保存</el-button>
          <el-button type="primary" size="small" class="mr30" @click="submitOk(1)">发布</el-button>
        </div>
      </div>
      <el-row style="height: 100%">
        <el-col :span="8">
          <div class="content">
            <div class="title">应用信息</div>
            <applicationForm ref="applicationForm" :info="info" tab-name="applicationForm"></applicationForm>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="content p0">
            <modelForm ref="modelForm" :info="info" tab-name="modelForm" @update-preview="handleUpdatePreviewState">
            </modelForm>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="content">
            <div class="title">调试预览</div>

            <div class="mobile-box">
              <iframe ref="previewIframe" :src="previewIframeUrl" class="mobile-box-iframe" frameborder="0"></iframe>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>
<script>
import { getApplicationsInfoApi, chatPutApplicationsApi, chatReleasesApplicationsApi } from '@/api/chatAi'
import applicationForm from './components/applicationForm'
import modelForm from './components/modelForm'
import { roterPre } from '@/settings'
import { AiEmbeddedManage } from '@/libs/ai-embedded'

export default {
  name: '',
  components: { applicationForm, modelForm },
  props: {},
  data() {
    const id = this.$route.query.id
    return {
      previewIframeUrl: AiEmbeddedManage.getPreviewIframeUrl(id),
      activeName: '1',
      id,
      info: {},
      loading: false,
      tabArray: [{ value: '1', label: '设置' }]
    }
  },

  mounted() {
    this.getInfo(this.id)
  },
  methods: {
    handleUpdatePreviewState(state) {
      const nextState = {
        ...state,
        appId: Number(this.id)
      }

      AiEmbeddedManage.updateAiAppPreviewState(this.$refs.previewIframe, nextState)
    },
    getInfo(id) {
      getApplicationsInfoApi(id).then((res) => {
        this.info = res.data
      })
    },
    backFn() {
      this.$router.push({
        path: `${roterPre}/chat/index`
      })
    },
    submitOk(val) {
      const p1 = this.getCmpData('applicationForm', val)
      const p2 = this.getCmpData('modelForm', val)
      Promise.all([p1, p2])
        .then((res) => {
          const param = {
            ...res[0].applicationForm,
            ...res[1].modelForm
          }
          if (val === 1) {
            param.status = 1
            this.sendToServer(param, val)
          } else {
            this.loading = true
            param.status = 0
            this.sendToServer(param)
          }
        })
        .catch((err) => { })
    },
    sendToServer(data, val) {
      chatPutApplicationsApi(this.id, data).then((res) => {
        if (val == 1 && res.status == '200') {
          this.backFn()
        }
        this.loading = false
        AiEmbeddedManage
          .getAiEmbedded()
          ?.instance
          ?.refreshAppList?.()
      })
    },
    publishAndSave(data) {
      chatReleasesApplicationsApi(this.id, data).then((res) => {
        if (res.status == '200') {
          this.backFn()
        }
        this.loading = false
      })
    },
    getCmpData(name, val) {
      return this.$refs[name].getData(val)
    }
  }
}
</script>
<style scoped lang="scss">
.header {
  width: 100%;
  height: 66px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eeeeee;

  .el-icon-arrow-left {
    color: #606266;
    font-size: 13px;
    margin-right: 10px;
    cursor: pointer;
  }

  .mr30 {
    margin-right: 30px;
  }

  .left {
    padding-left: 20px;
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
      font-size: 14px;
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
        font-weight: 500;
        font-size: 13px;
        color: #606266;
      }

      ::v-deep .el-tabs__item.is-active {
        font-weight: 500;
        color: #1890ff;
        border-bottom: 2px solid;
      }
    }
  }
}

.content {
  height: calc(100vh - 167px);
  padding: 20px;
  overflow-y: auto;
  border-right: 1px solid #eeeeee;
  font-family: PingFang SC, PingFang SC;
  scrollbar-width: none;
  /* firefox */
  -ms-overflow-style: none;
  /* IE 10+ */

  display: flex;
  flex-flow: column;

  .mobile-box {
    flex: 1;
    width: 100%;
    background: #f9fbfc;
    border-radius: 8px;
  }

  .mobile-box-iframe {
    height: 100%;
    width: 100%;
  }

  .title {
    font-weight: 500;
    font-size: 16px;
    color: #303133;
    position: relative;
    padding-left: 13px;
    margin-bottom: 20px;
  }

  .title:before {
    content: '';
    background-color: #1890ff;
    width: 3px;
    height: 14px;
    position: absolute;
    left: 0px;
    top: 50%;
    margin-top: -7px;
  }
}

.p0 {
  padding: 20px 0 !important;
}
</style>
