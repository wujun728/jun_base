<template>
  <div>
    <div class="item-tag" v-for="(tag, inx) of list" :key="inx" :title="'原名：' + tag.fieldLabel" ref="buttonRef">
      <div class="field-name">
        <span>
          <i class="el-icon-sort-up sort-icon ml-3" v-if="tag.sort == 'asc'"></i>
          <i class="el-icon-sort-down sort-icon ml-3" v-if="tag.sort == 'desc'"></i>
          <!-- <SvgIcon class="sort-icon ml-3" v-if="tag.sort == 'ASC'" icon-name="fields-asc" />
          <SvgIcon class="sort-icon ml-3" v-if="tag.sort == 'DESC'" icon-name="fields-desc" /> -->
          {{ tag.alias }}{{ !isDimension ? '(' + CalcMode[tag.calcMode] + ')' : '' }}
        </span>
        <i class="el-icon-error close-icon" @click="delCom(inx)" />
      </div>

      <el-popover
        v-if="needChangeAlias"
        ref="fieldsPopoverRefs"
        v-model="visibles[inx]"
        placement="bottom"
        popper-class="fields-popover"
        :width="200"
        trigger="click"
      >
        <template #reference v-if="chartType !== 'statistic'">
          <div class="popover-item">
            <!-- <i class="el-icon-edit-outline"></i> -->
            <el-tooltip class="item" effect="dark" content="修改显示名" placement="top">
              <SvgIcon class="svg-icon" icon-class="iconbianji3" @click="openVisible(inx)"></SvgIcon>
            </el-tooltip>
          </div>
        </template>
        <div class="popover-div" style="padding: 10px">
          <div class="popover-name">修改显示名</div>
          <div class="w-100">
            <el-input v-model="tag.editAlias" size="small"></el-input>
          </div>
          <div class="w-100 mt-10" style="text-align: right">
            <el-button size="small" @click="cannerAlias(inx)">取消</el-button>
            <el-button size="small" type="primary" @click="confirmAlias(tag, inx)">确认</el-button>
          </div>
        </div>
      </el-popover>
      <!-- 汇总方式 -->
      <el-popover
        v-if="needCalcMode"
        placement="bottom"
        :width="60"
        trigger="click"
        popper-class="fields-popover"
        ref="summaryPopoverRefs"
      >
        <div class="popover-name">汇总方式</div>
        <div class="popover-div">
          <template v-for="(summary, summaryInx) of modeValue(tag)">
            <div
              :key="summaryInx"
              class="popover-item"
              :class="{ 'is-active': tag.calcMode == summary.code }"
              @click="onCalcModeChange(tag, summary.code, inx)"
            >
              {{ summary.label }}
            </div>
          </template>
        </div>
        <template #reference>
          <div class="popover-item">
            <!-- <i class="el-icon-document-checked"></i> -->
            <el-tooltip class="item" effect="dark" content="汇总方式" placement="top">
              <SvgIcon class="svg-icon" icon-class="iconhuizong"></SvgIcon>
            </el-tooltip>
          </div>
        </template>
      </el-popover>
      <!-- 排序 -->
      <el-popover
        v-if="needSort"
        placement="right"
        :width="60"
        trigger="click"
        popper-class="fields-popover"
        ref="sortPopoverRefs"
      >
        <div class="popover-name">排序</div>
        <div class="popover-div">
          <div class="popover-item" :class="{ 'is-active': !tag.sort }" @click="onSort(tag, '', inx)">默认</div>
          <div class="popover-item" :class="{ 'is-active': tag.sort == 'asc' }" @click="onSort(tag, 'asc', inx)">
            升序
          </div>
          <div class="popover-item" :class="{ 'is-active': tag.sort == 'desc' }" @click="onSort(tag, 'desc', inx)">
            降序
          </div>
        </div>
        <template #reference v-if="chartType !== 'statistic'">
          <div class="popover-item">
            <!-- <i class="el-icon-sort"></i> -->
            <el-tooltip class="item" effect="dark" content="排序方式" placement="top">
              <SvgIcon class="svg-icon" icon-class="iconpaixu2"></SvgIcon>
            </el-tooltip>
          </div>
        </template>
      </el-popover>
      <!-- 数据格式 -->
      <el-popover
        v-if="needFormat"
        placement="bottom"
        trigger="click"
        v-model="dialogConf.isShow[inx]"
        popper-class="fields-popover"
        ref="summaryPopoverRefs"
      >
        <div class="pr-20 thousands-separator-div">
          <div class="popover-name">数据格式</div>
          <el-form label-width="120">
            <el-form-item label="">
              <div class="w-100">
                <el-checkbox
                  :disabled="needDisabledType()"
                  v-model="dialogConf.data.thousandsSeparator"
                  label="千分符"
                />
              </div>
              <div class="w-100">
                <el-checkbox v-model="dialogConf.data.showDecimalPlaces" label="小数位数" />
                <span class="decimal-places" v-if="dialogConf.data.showDecimalPlaces">
                  <el-input-number
                    style="width: 150px"
                    size="small"
                    v-model="dialogConf.data.decimalPlaces"
                    :min="1"
                    :max="4"
                  />
                </span>
              </div>
              <div class="w-100">
                <el-checkbox
                  v-model="dialogConf.data.showNumericUnits"
                  label="数值量级"
                  :disabled="needDisabledType()"
                />
                <span class="decimal-places" v-if="dialogConf.data.showNumericUnits">
                  <el-select
                    v-model="dialogConf.data.numericUnits"
                    filterable
                    style="width: 150px"
                    size="small"
                    placeholder
                    allow-create
                    default-first-option
                    clearable
                  >
                    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </span>
              </div>
            </el-form-item>
            <el-form-item label="效果预览">
              <div
                class="show-preview yichu"
                :title="getPreviewNum() + (dialogConf.data.numericUnits == '无' ? '' : dialogConf.data.numericUnits)"
              >
                <span>{{ getPreviewNum() }}</span>
                <span class="sub" v-if="dialogConf.data.showNumericUnits && dialogConf.data.numericUnits != '无'">{{
                  dialogConf.data.numericUnits
                }}</span>
              </div>
            </el-form-item>
            <el-form-item label=" ">
              <el-button @click="confirmDataFormat" type="primary">确定</el-button>
              <el-button @click="dialogConf.isShow = false">取消</el-button>
            </el-form-item>
          </el-form>
        </div>
        <template #reference>
          <div class="popover-item">
            <!-- <i class="el-icon-finished"></i> -->
            <el-tooltip class="item" effect="dark" content="数据格式" placement="top">
              <SvgIcon class="svg-icon" icon-class="iconshuju2" @click="showDataFormat(tag, inx)"></SvgIcon>
            </el-tooltip>
          </div>
        </template>
      </el-popover>
      <span class="close-span" @click.stop="delCom(inx)">
        <el-icon>
          <ElIconCircleCloseFilled />
        </el-icon>
      </span>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import SvgIcon from '@/components/svg-icon-nc'
export default {
  name: 'YourComponentName',
  components: {
    SvgIcon
  },
  props: {
    modelValue: {
      type: null
    },
    isDimension: {
      type: Boolean,
      default: false
    },
    // 是否需要排序
    needSort: {
      type: Boolean,
      default: true
    },
    // 是否数据格式
    needFormat: {
      type: Boolean,
      default: true
    },
    // 是否有汇总方式
    needCalcMode: {
      type: Boolean,
      default: true
    },
    // 是否有修改名称
    needChangeAlias: {
      type: Boolean,
      default: true
    },
    chartType: {
      type: String,
      default: ''
    }
  },
  model: {
    prop: 'modelValue', // 默认是value
    event: 'modelValue' // 默认是input
  },
  data() {
    return {
      visibles: [],
      list: this.modelValue,
      calcMode: [
        { label: '求和', type: 'N', code: 'sum' },
        { label: '计数', type: 'N|T', code: 'count' },
        { label: '去重计数', type: 'N|T', code: 'uniqid_count' },
        { label: '平均值', type: 'N', code: 'avg' },
        { label: '最大值', type: 'N', code: 'max' },
        { label: '最小值', type: 'N', code: 'min' }
      ],
      textMode: [
        { label: '计数', type: 'N|T', code: 'count' },
        { label: '去重计数', type: 'N|T', code: 'uniqid_count' }
      ],
      CalcMode: {
        sum: '求和',
        count: '计数',
        uniqid_count: '去重计数',
        avg: '平均值',
        max: '最大值',
        min: '最小值'
      },
      numType: ['Integer', 'Decimal', 'Percent', 'Money'],
      options: [
        { value: '无', label: '无' },
        { value: '%', label: '%' },
        { value: '元', label: '元' },
        { value: '万元', label: '万元' },
        { value: '亿', label: '亿' },
        { value: '美元', label: '美元' },
        { value: '个', label: '个' },
        { value: '位', label: '位' },
        { value: '天', label: '天' }
      ],
      sortPopoverRefs: null,
      dialogConf: {
        isShow: [],
        data: {},
        inx: 0
      }
    }
  },
  watch: {
    modelValue: {
      handler(val) {
        this.list = val
      },
      deep: true
    }
  },
  mounted() {
    this.list = this.modelValue
  },
  methods: {
    modeValue(tag) {
      let val = []
      let types = ['input_number', 'input_float', 'input_price', 'input_percentage']
      if (types.includes(tag.form_value)) {
        val = this.calcMode
      } else {
        val = this.textMode
      }
      return val
    },
    openVisible(inx) {},
    delCom(inx) {
      this.list.splice(inx, 1)
      this.$emit('modelValue', this.list)
    },
    editAlias(tag) {
      tag.editAlias = tag.alias
      tag.showEdit = true
    },
    confirmAlias(tag) {
      if (tag.editAlias) {
        tag.alias = tag.editAlias
        this.cannerAlias()
      }
    },

    cannerAlias() {
      this.visibles = []
    },
    onCalcModeChange(tag, target, inx) {
      // this.summaryPopoverRefs[inx].hide()
      // this.sortPopoverRefs[inx].hide()
      tag.calcMode = target
      this.$emit('modelValue', this.list)
    },
    onSort(tag, target, inx) {
      // this.fieldsPopoverRefs[inx].hide()
      // this.sortPopoverRefs[inx].hide()
      this.$emit('modelValue', this.list)
      this.$emit('onSort', { tag, target })
    },
    needDisabledType() {
      let chartTypes = ['barChart', 'barXChart', 'lineChart']
      return chartTypes.includes(this.chartType)
    },
    showDataFormat(tag, inx) {
      this.dialogConf.data = { ...tag }
      this.dialogConf.inx = inx
      this.dialogConf.isShow = true
    },
    getPreviewNum() {
      let { thousandsSeparator, showDecimalPlaces, decimalPlaces } = this.dialogConf.data
      let previewStr = '99999999'
      if (showDecimalPlaces) {
        previewStr = Number(previewStr).toFixed(decimalPlaces)
      }
      if (thousandsSeparator) {
        previewStr = this.numberToCurrencyNo(previewStr)
      }
      return previewStr
    },
    numberToCurrencyNo(value) {
      if (!value) return 0
      const intPart = Math.trunc(value)
      const intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
      let floatPart = ''
      const valueArray = value.toString().split('.')
      if (valueArray.length === 2) {
        floatPart = valueArray[1].toString()
        return intPartFormat + '.' + floatPart
      }
      return intPartFormat + floatPart
    },
    confirmDataFormat() {
      this.$set(this.list, this.dialogConf.inx, { ...this.list[this.dialogConf.inx], ...this.dialogConf.data })
      this.dialogConf.isShow = []
      this.$emit('modelValue', this.list)
    }
  }
}
</script>

<style lang="scss" scoped>
.item-tag {
  display: flex;
  align-items: center;
  line-height: 32px;
  cursor: pointer;
  position: relative;
  margin-bottom: 10px;
  .drop-down-box {
    display: inline-block;
    background: var(--el-color-primary);
    color: #fff;
    margin-right: 5px;
    border-radius: 20px;
    height: 24px;
    line-height: 24px;
    padding: 0 25px;
    padding-left: 10px;
    &.isDimension {
      background: var(--el-color-success);
    }
    .arrow-down {
      position: relative;
      top: 2px;
    }
  }

  .close-span {
    display: none;
    position: absolute;
    right: 10px;
    top: 2px;
  }
  &:hover {
    .close-span {
      display: inline-block;
    }
  }
  .field-name {
    width: 170px;
    border: 1px solid #e6e6e6;
    border-radius: 4px;
    padding: 0px 10px;
    margin-right: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .popover-item {
    font-size: 16px;
    margin-right: 10px;
    color: #909399;
  }
}
.decimal-places {
  margin-left: 5px;
  position: relative;
  top: -2px;
}
.show-preview {
  height: 60px;
  line-height: 60px;
  background: #f5f6f8;
  width: 100%;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  color: #404040;
  .sub {
    font-size: 14px;
    margin-left: 5px;
  }
}
.fields-popover {
  padding: 0 !important;
  width: max-content !important;
  .popover-div {
    padding: 10px 0;
    .popover-item {
      height: 26px;
      line-height: 26px;
      padding: 0 10px;
      cursor: pointer;
      &:hover {
        background: #e6e6e6;
      }
      &.is-active {
        color: var(--el-color-primary);
      }
    }
  }
  .thousands-separator-div {
    width: 250px;
  }
}
.close-icon {
  color: #333;
}
.mt-10 {
  margin-top: 10px;
}
.svg-icon {
  width: 0.9em;
  height: 0.9em;
}
</style>
