<template>
  <v-form-designer
    ref="dbDesignerRef"
    :designer-config="designerConfig"
    class="visual-design"
    @changeTitle="changeTitle"
    v-loading="loading"
  >
    <!-- 配置工具按钮 -->
    <div class="title">
      {{ title }}
      <i class="iconfont iconbianji3" @click="editFn"></i>
    </div>
    <div>
      <el-button class="view-btn" @click="previewDesign">
        <el-icon> <View /> </el-icon>预览
      </el-button>
      <el-button class="clear-btn" @click="clearCanvas">
        <el-icon> <ElIconDelete /> </el-icon>清空
      </el-button>
      <el-button type="primary" @click="saveDesign">
        <el-icon> <Finished /> </el-icon>保存
      </el-button>
    </div>
  </v-form-designer>
</template>

<script>
import { dashboard_container_schema } from '@/views/system/dashboard-design/charts/charts-schema'
import { deepClone, mlShortcutkeys } from '@/utils/util'
import { getDashboardDesign, changeDashboardDesign } from '@/api/chart'
import { getDefaultFormConfig, generateId } from '@/utils/util'
import VFormDesigner from './components/VFormDesigner.vue'
import SvgIcon from '@/components/svg-icon-nc'
import { getDatabaseApi } from '@/api/develop'

export default {
  components: {
    VFormDesigner,
    SvgIcon
  },
  data() {
    return {
      loading: false,
      chartId: '',
      designerConfig: {
        componentLib: false,
        formTemplates: false,
        logoHeader: false,
        layoutTypeButton: false,
        clearDesignerButton: false,
        previewFormButton: false,
        importJsonButton: false,
        exportJsonButton: false,
        exportCodeButton: false,
        generateSFCButton: false,
        toolbarMaxWidth: 300,
        chartLib: true
      },
      isMobile: false,
      title: '统计看板设计'
    }
  },
  mounted() {
    this.chartId = this.$route.query.chartId
    let type = this.$route.query.type
    if (!this.chartId) {
      // this.$router.push('/web/dashboard-list')
      return
    }
    this.isMobile = type === 'mobile'
    let key = this.isMobile ? 'mobileChartData' : 'chartData'
    this.initFormConfig(key)

    mlShortcutkeys(() => {
      window.advancedDevMode = !window.advancedDevMode
      this.designerConfig.componentLib = !!window.advancedDevMode
    })
  },
  methods: {
    changeTitle(title) {
      this.title = title
    },
    // 编辑
    editFn() {
      let row = {
        id: this.chartId,
        name: this.title
      }
      this.$refs.dbDesignerRef.editFn(row)
    },
    getdatabaseList() {
      /**
       * fix: 图表数据接口需要 tableNameEn 参数，该参数由 getDatabaseApi 提供
       * 必须在图表渲染之前调用此接口，之前没有报错是因为设计图表时设计器调用了相关接口
       * 相关数据保存在了 localStorage
       * 当新用户第一次打开图表页面时，由于没有实体表相关数据，缺少接口必须的 tableNameEn 参数
       * 所以会导致图表无法渲染
       */
      return getDatabaseApi().then((res) => {
        res.data.forEach((item) => {
          item.value = item.value + 'res'
        })
        let allEntityName = {}
        res.data.forEach((el) => {
          el.children.forEach((item) => {
            allEntityName[item.value] = item.table_name_en
          })
        })
        localStorage.setItem('allEntityName', JSON.stringify(allEntityName))
      });
    },
    async initFormConfig(key) {
      this.loading = true

      const task1 = this.getdatabaseList()
      const task2 = getDashboardDesign(this.chartId)

      let [_, res] = await Promise.all([task1, task2]);

      if (res) {
        this.title = res.data.name
        if (!res.data[key]) {
          this.clearCanvas()
        } else {
          let blankFormJson = JSON.parse(res.data[key])
          this.$refs.dbDesignerRef.setFormJson(blankFormJson)
          if (this.isMobile) {
            this.$refs.dbDesignerRef.changeLayoutType('H5')
          }
        }
      } else {
        this.clearCanvas()
      }
      this.loading = false
    },
    copyCanvas() {
      this.$message
        .confirm('从' + (this.isMobile ? 'PC' : '移动端') + '复制图表将会清空当前配置，是否确认复制?', '提示：', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        })
        .then(async () => {
          this.clearCanvas()
          let key = this.isMobile ? 'chartData' : 'mobileChartData'
          this.initFormConfig(key)
        })
        .catch(() => {})
    },
    clearCanvas() {
      const newDashboardCon = deepClone(dashboard_container_schema)
      newDashboardCon.id = 'dbCon' + generateId()
      newDashboardCon.options.name = newDashboardCon.id
      const blankFormJson = {
        widgetList: [newDashboardCon],
        formConfig: getDefaultFormConfig()
      }
      this.$refs.dbDesignerRef.clearSelected()
      this.$refs.dbDesignerRef.setFormJson(blankFormJson)
      if (this.isMobile) {
        this.$refs.dbDesignerRef.designer.changeLayoutType('H5')
      }
    },
    previewDesign() {
      this.$refs.dbDesignerRef.previewForm()
    },
    importJson() {
      this.$refs.dbDesignerRef.importJson()
    },
    exportJson() {
      this.$refs.dbDesignerRef.exportJson()
    },
    async saveDesign() {
      this.loading = true
      let param = {
        // entity: 'Chart',
        formModel: {},
        id: this.chartId
      }
      if (this.isMobile) {
        param.formModel.mobileChartData = JSON.stringify(this.$refs.dbDesignerRef.getFormJson(false))
      } else {
        param.formModel.chartData = JSON.stringify(this.$refs.dbDesignerRef.getFormJson(false))
      }
      await changeDashboardDesign(param.id, param.formModel).then((res) => {
        this.loading = false
      })
    }
  },
  beforeDestroy() {
    this.clearCanvas()
  }
}
</script>
<style lang="scss" scoped>
:deep(.toolbar-header .toolbar-container) {
  width: 100%;
}
.title .iconbianji3 {
  font-size: 16px;
  color: #1890ff;
  cursor: pointer;
  margin-left: 8px;
}
:deep(.vue-grid-layout) {
  min-height: 450px !important;
}
</style>
<style lang="scss">
.main-container.visual-design li {
  box-sizing: content-box !important;
}

.ds-setting-drawer .el-drawer__body {
  padding: 20px !important;
}
.view-btn.el-button {
  color: #1890ff;
  border-color: #1890ff;
}
.clear-btn.el-button {
  color: red;
  border-color: red;
}
.chart-container-widget-item,
.chart-widget-item {
  div.svg-icon-label {
    display: flex;
    height: 72px !important;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;

    .svg-icon {
      margin-top: 6px !important;
      font-size: 20px !important;
    }
  }
}
</style>
