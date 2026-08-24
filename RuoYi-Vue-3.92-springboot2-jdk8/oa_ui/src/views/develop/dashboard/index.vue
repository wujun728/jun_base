<template>
  <div class="dashboard-container">
    <VFormRender v-if="!loading" ref="preForm" :form-json="designer" :preview-state="true"> </VFormRender>
  </div>
</template>
<script>
import VFormRender from '@/components/form-render/index'
import { getDashboardDesign } from '@/api/chart'
import { getDatabaseApi } from '@/api/develop'
import { addWindowResizeHandler } from '@/utils/util'

export default {
  name: 'Dashboard',
  components: {
    VFormRender
  },
  data() {
    return {
      designer: {},
      chartId: '',
      loading: false,
      screenWidth: null
    }
  },
  watch: {},
  provide() {
    return {
      getScreenWidth: () => this.screenWidth
    }
  },
  created() {
    const routeString = this.$route.path
    const routeArray = routeString.split('/').filter((item) => item !== '')
    this.chartId = routeArray[3]
    this.initFormConfig('chartData')
  },
  mounted() {
    addWindowResizeHandler(() => {
      this.$nextTick(() => {
        this.screenWidth = window.innerWidth
      })
    })
  },

  methods: {
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
      })
    },
    initFormConfig(key) {
      this.loading = true

      const task1 = this.getdatabaseList()
      const task2 = getDashboardDesign(this.chartId)

      Promise.all([task1, task2]).then(([_, res]) => {
        let blankFormJson = JSON.parse(res.data[key])
        this.$set(this, 'designer', blankFormJson)
        this.loading = false
      })
    }
  }
}
</script>
<style scoped lang="scss">
.dashboard-container {
  margin-top: -10px;
  ::v-deep .smartwidget .widget-header .widget-header__toolbar a {
    display: inline-block;
    text-decoration: none;
    text-align: center;
    height: 24px;
    line-height: 28px;
    padding: 0;
    margin: 0;
    color: #333;
    min-width: 35px;
    position: relative;
    font-family: Arial, Helvetica, sans-serif;
    border: none !important;
  }
  ::v-deep .widget-header__title {
    font-weight: 600 !important;
  }
}
</style>
