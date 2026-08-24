<template>
  <div>
    <el-form-item class="mb-18" :label="chartType == 'listTable' ? '显示字段' : '维度指标设置'">
      <el-button @click="openDrawer">点击设置</el-button>
    </el-form-item>
    <el-drawer :visible.sync="drawer" title="维度指标设置" :size="360">
      <div class="drawer-main">
        <div class="form-box" ref="formBoxRefs">
          <el-form label-width="60">
            <el-form-item label="维度" v-if="isShowDimension()">
              <div class="input-box">
                <el-scrollbar max-height="132px">
                  <DimensionCom
                    v-model="dimension"
                    :needSort="false"
                    :needChangeAlias="showChangeAlias"
                    :needCalcMode="false"
                    :needFormat="false"
                    @onSort="onSort"
                    isDimension
                    :chartType="chartType"
                  />
                </el-scrollbar>
              </div>
            </el-form-item>
            <el-popover
              v-if="isShowDimension()"
              placement="right-start"
              :width="200"
              trigger="click"
              popper-class="fields-popover"
              ref="sortPopoverRefs"
            >
              <div class="fields-popover-content">
                <div
                  class="fields-popover-item"
                  type="text"
                  v-for="(item, index) in fields"
                  :key="index"
                  v-show="!item.disabled"
                  :class="item.disabled ? 'active' : ''"
                  @click.stop="addCom(item.field_name_en, 'dimension')"
                >
                  {{ item.field_name }}
                </div>
              </div>
              <template #reference>
                <el-button type="text" icon="el-icon-circle-plus" v-show="dimension.length < 1">添加维度</el-button>
              </template>
            </el-popover>
            <el-form-item label="维度行" v-if="chartType == 'pivotTable'">
              <div class="input-box">
                <el-scrollbar max-height="132px">
                  <DimensionCom v-model="dimensionRow" @onSort="onSort" isDimension :chartType="chartType" />
                  <el-popover
                    placement="right-start"
                    :width="200"
                    trigger="click"
                    popper-class="fields-popover"
                    ref="sortPopoverRefs"
                  >
                    <div class="fields-popover-content">
                      <div
                        class="fields-popover-item"
                        type="text"
                        v-for="(item, index) in fields"
                        :key="index"
                        v-show="!item.disabled"
                        :class="item.disabled ? 'active' : ''"
                        @click.stop="addCom(item.field_name_en, 'dimensionRow')"
                      >
                        {{ item.field_name }}
                      </div>
                    </div>
                    <template #reference>
                      <el-button type="text" icon="el-icon-circle-plus">添加维度行</el-button>
                    </template>
                  </el-popover>
                </el-scrollbar>
              </div>
            </el-form-item>
            <el-form-item label="维度列" v-if="chartType == 'pivotTable'">
              <div class="input-box">
                <el-scrollbar max-height="132px">
                  <DimensionCom v-model="dimensionCol" @onSort="onSort" :chartType="chartType" />
                  <el-popover
                    placement="right-start"
                    :width="200"
                    trigger="click"
                    popper-class="fields-popover"
                    ref="sortPopoverRefs"
                  >
                    <div class="fields-popover-content">
                      <div
                        class="fields-popover-item"
                        type="text"
                        v-for="(item, index) in fields"
                        :key="index"
                        v-show="!item.disabled"
                        :class="item.disabled ? 'active' : ''"
                        @click.stop="addCom(item.field_name_en, 'dimensionCol')"
                      >
                        {{ item.field_name }}
                      </div>
                    </div>
                    <template #reference>
                      <el-button type="text" icon="el-icon-circle-plus">添加维度列</el-button>
                    </template>
                  </el-popover>
                </el-scrollbar>
              </div>
            </el-form-item>
            <!-- 数据列表 -->
            <el-form-item label="指标" v-if="chartType != 'listTable'">
              <div class="input-box">
                <el-scrollbar max-height="132px">
                  <DimensionCom v-model="metrics" @onSort="onSort" :chartType="chartType" />
                </el-scrollbar>
              </div>
            </el-form-item>
            <el-popover
              v-if="chartType != 'listTable'"
              placement="right-start"
              :width="200"
              trigger="click"
              popper-class="fields-popover"
              ref="sortPopoverRefs"
            >
              <div class="fields-popover-content">
                <div
                  class="fields-popover-item"
                  type="text"
                  v-for="(item, index) in fields"
                  :key="index"
                  v-show="!item.disabled"
                  :class="item.disabled ? 'active' : ''"
                  @click.stop="addCom(item.field_name_en, 'metrics')"
                >
                  {{ item.field_name }}
                </div>
              </div>
              <template #reference>
                <el-button type="text" icon="el-icon-circle-plus">添加指标</el-button>
              </template>
            </el-popover>
            <!-- 进度条 -->
            <el-form-item label="目标值" v-if="chartType == 'progressbar'">
              <el-input-number v-model="optionModel.setDimensional.targetValue" :min="0" class="w-100" />
            </el-form-item>
            <!-- 数据列表 -->
            <el-form-item label="显示字段" v-if="chartType == 'listTable'">
              <div class="input-box">
                <el-scrollbar max-height="132px">
                  <DimensionCom
                    v-model="showFields"
                    :needSort="false"
                    :needChangeAlias="showChangeAlias"
                    :needCalcMode="false"
                    :needFormat="false"
                    @onSort="onSort"
                    :chartType="chartType"
                    isDimension
                  />
                </el-scrollbar>
              </div>
            </el-form-item>
            <el-popover
              v-if="chartType == 'listTable'"
              placement="right-start"
              :width="200"
              trigger="click"
              popper-class="fields-popover"
              ref="sortPopoverRefs"
            >
              <div class="fields-popover-content">
                <div
                  class="fields-popover-item"
                  type="text"
                  v-for="(item, index) in fields"
                  :key="index"
                  v-show="!item.disabled"
                  :class="item.disabled ? 'active' : ''"
                  @click.stop="addCom(item.field_name_en, 'showFields')"
                >
                  {{ item.field_name }}
                </div>
              </div>
              <template #reference>
                <el-button type="text" icon="el-icon-circle-plus">添加显示字段</el-button>
              </template>
            </el-popover>
          </el-form>
        </div>
      </div>
      <template #footer>
        <div style="flex: auto; padding-top: 10px">
          <!-- <el-button size="default" @click="onCancel">取消</el-button> -->
          <el-button size="default" type="primary" @click="drawer = false">关闭</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
// import VueDraggableNext from 'vue-draggable-next'
import DimensionCom from './components/DimensionCom.vue'
import { dataDatabaseFieldsApi } from '@/api/develop'

export default {
  name: 'setDimensional-editor',
  components: {
    DimensionCom
  },
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  },
  model: {
    prop: 'optionModel', // 默认是value
    event: 'optionModel' // 默认是input
  },
  data() {
    return {
      selectedMetrics: '',
      selectedDimension: '',
      selectedDimensionRow: '',
      chartType: '',
      dimension: [],
      dimensionRow: [],
      dimensionCol: [],
      metrics: [],
      showFields: [],
      fields: [],
      loading: false,
      drawer: false,
      itemBoxHeight: '',
      needType: [
        'input_number',
        'input_float',
        'input_price',
        'input_percentage',
        'Percent',
        'number',
        'input_select',
        'input',
        'textarea',
        'time_range',
        'time',
        'date_picker',
        'date_time_picker',
        'Reference',
        'Boolean',
        'Option',
        'Status',
        'tag',
        'radio',
        'checkbox',
        'cascader_radio',
        'cascader'
      ],
      fieldsAdd: {
        alias: '',
        showEdit: false,
        editAlias: '',
        type: 'N',
        sort: '',
        calcMode: 'count',
        thousandsSeparator: false,
        showDecimalPlaces: false,
        decimalPlaces: 2,
        showNumericUnits: false,
        numericUnits: '无'
      }
    }
  },
  watch: {
    optionModel: {
      handler(newVal) {
        this.$emit('optionModelChange', newVal)
        this.getEntityFields(newVal.dataEntity)
        this.initDimensional()
      },
      deep: true
    }
  },
  computed: {
    showChangeAlias() {
      return this.selectedWidget.type == 'listTable'
    }
  },
  mounted() {
    this.initDimensional()
  },
  methods: {
    // ...mapActions(['getEntityFields']),
    initDimensional() {
      this.chartType = this.selectedWidget.type
      this.dimension = this.optionModel.setDimensional.dimension || []
      this.metrics = this.optionModel.setDimensional.metrics || []
      this.showFields = this.optionModel.setDimensional.showFields || []
      this.dimensionRow = this.optionModel.setDimensional.dimensionRow || []
      this.dimensionCol = this.optionModel.setDimensional.dimensionCol || []
      this.$nextTick(() => {
        this.setItemBoxHeight()
      })
    },
    async getEntityFields() {
      if (!this.optionModel.dataEntity) return
      this.loading = true
      let res = await dataDatabaseFieldsApi(this.optionModel.dataEntity)
      if (res && res.status === 200) {
        this.fields = []
        res.data.forEach((el) => {
          if (this.needType.includes(el.form_value)) {
            let newFieldsAdd = { ...this.fieldsAdd }
            newFieldsAdd.alias = el.field_name
            newFieldsAdd.type = el.form_value
            let newField = Object.assign(newFieldsAdd, { ...el })
            this.fields.push({ ...newField })
          }
        })
        this.$nextTick(() => {
          this.setItemBoxHeight()
        })
      }
      this.loading = false
    },
    openDrawer() {
      if (!this.optionModel.dataEntity) {
        this.$message.warning('请先选择图标数据实体')
        return
      }
      this.drawer = true
      this.getEntityFields()
    },
    onCancel() {
      // Your onCancel logic here
    },
    setItemBoxHeight() {
      let formHeight = this.$refs.formBox && this.$refs.formBox.offsetHeight
      if (formHeight) {
        formHeight += 60
        this.itemBoxHeight = 'calc(100% - ' + formHeight + 'px)'
      } else {
        this.itemBoxHeight = 'calc(100% - 175px)'
      }
    },
    addCom(e, target) {
      if (this.$refs.sortPopoverRefs) {
        this.$refs.sortPopoverRefs.doClose()
      }

      let i = this.fields.findIndex((el) => el.field_name_en == e)

      let cutField = { ...this.fields[i] }
      if (this.fields[i].form_value == 'input_price') {
        cutField.calcMode = 'sum'
      }
      let checkHasDimensionRow = this.dimensionRow.filter((el) => el.field_name == cutField.field_name)
      let checkHasDimensionCol = this.dimensionCol.filter((el) => el.field_name == cutField.field_name)
      let checkHasDimension = this.dimension.filter((el) => el.field_name == cutField.field_name)
      let checkHasMetrics = this.metrics.filter((el) => el.field_name == cutField.field_name)
      let checkHasShowFields = this.showFields.filter((el) => el.field_name == cutField.field_name)
      if (checkHasDimension.length > 0) {
        this.$message.warning('添加失败，同一字段不能重复添加维度')
        return
      }
      if (checkHasDimensionRow.length > 0) {
        this.$message.warning('添加失败，同一字段不能重复添加维度行')
        return
      }
      if (checkHasDimensionCol.length > 0) {
        this.$message.warning('添加失败，同一字段不能重复添加维度列')
        return
      }
      if (checkHasMetrics.length > 0) {
        this.$message.warning('添加失败，同一字段不能重复添加指标')
        return
      }
      if (checkHasShowFields.length > 0) {
        this.$message.warning('添加失败，同一字段不能重复添加显示字段')
        return
      }
      let dimensionLength = this.dimension.length
      let metricsLength = this.metrics.length
      let type = this.selectedWidget.type
      let max3 = ['barChart', 'barXChart', 'lineChart', 'radarChart']
      // 添加维度
      if (target == 'dimension') {
        // 1个维度或多个指标
        // 2个维度或1个指标
        if (max3.includes(type)) {
          if (dimensionLength > 1) {
            this.$message.warning('添加失败，最多添加2个维度')
            return
          }
          if (metricsLength > 1 && dimensionLength > 0) {
            this.$message.warning('添加失败，多个指标最多只能添加1个维度')
            return
          }
        }
        // 如果是饼图、漏斗图 最多添加1个维度
        if ((type == 'pieChart' || type == 'funnelChart') && dimensionLength > 0) {
          this.$message.warning('添加失败，最多添加1个维度')
          return
        }
        this.dimension.push(cutField)
      }
      // 添加指标
      else if (target == 'metrics') {
        // 1个维度或多个指标
        // 2个维度或1个指标
        if (max3.includes(type)) {
          if (dimensionLength > 1 && metricsLength > 0) {
            this.$message.warning('添加失败，2个维度最多只能添加1个指标')
            return
          }
        }
        // 如果是饼图、进度条 最多添加1个指标
        if ((type == 'pieChart' || type == 'progressbar' || type == 'statistic') && metricsLength > 0) {
          this.$message.warning('添加失败，最多添加1个指标')
          return
        }
        this.metrics.push(cutField)
      }
      // 添加显示字段
      else if (target == 'showFields') {
        this.showFields.push(cutField)
      }
      // 添加维度行
      else if (target == 'dimensionRow') {
        this.dimensionRow.push(cutField)
      }
      // 添加维度列
      else if (target == 'dimensionCol') {
        this.dimensionCol.push(cutField)
      }
    },
    onSort(e) {
      this.dimension.forEach((el) => {
        el.sort = ''
      })
      this.dimensionRow.forEach((el) => {
        el.sort = ''
      })
      this.dimensionCol.forEach((el) => {
        el.sort = ''
      })
      this.metrics.forEach((el) => {
        el.sort = ''
      })
      this.showFields.forEach((el) => {
        el.sort = ''
      })
      e.tag.sort = e.target
    },
    isShowDimension() {
      let notNeedDimension = ['progressbar', 'listTable', 'pivotTable', 'statistic']
      return this.showFn(notNeedDimension)
    },
    showFn(notNeed) {
      if (notNeed.includes(this.chartType)) {
        return false
      }
      return true
    }
  }
}
</script>
<style lang="scss" scoped>
.drawer-main {
  // padding: 20px;
  height: 100%;
  border-bottom: 1px solid #e5e5e5;
  .form-box {
    padding: 20px;
    padding-bottom: 0;
    .input-box {
      border-radius: 4px;
      padding: 3px 10px;
      width: 100%;
      min-height: 32px;
      .draggable-box {
        min-height: 32px;
        .item-list {
          display: none;
        }
      }
    }
  }

  .item-box {
    border-top: 1px solid #e5e5e5;
    padding: 20px 0;
    .item-title {
      font-size: 14px;
      font-weight: bold;
      padding-left: 20px;
    }
    .item-list-box {
      height: calc(100% - 12px);
      .item-list {
        padding-left: 15px;
        height: 32px;
        line-height: 32px;
        cursor: all-scroll;
        user-select: none;
        .lh {
          color: var(--el-color-primary);
        }
        &:hover {
          background: #e6e6e6;
        }
      }
    }
  }
}

.fields-popover-content {
  max-height: 500px;
  overflow-y: auto;
  .fields-popover-item {
    font-style: 14px;
    cursor: pointer;
    padding: 5px 10px;
    &:hover {
      background: #e6e6e6;
      color: #333;
    }
  }
}
.el-form-item {
  margin-bottom: 0;
}
.mb-18 {
  margin-bottom: 18px;
}
</style>
