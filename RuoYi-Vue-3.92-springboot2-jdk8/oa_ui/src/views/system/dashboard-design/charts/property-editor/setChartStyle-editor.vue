<template>
  <div class="chart-styles">
    <div class="c-s-t">图表样式</div>
    <el-form-item label="颜色选择">
      <el-color-picker v-model="optionModel.setChartStyle.useTextColor" />
    </el-form-item>
    <el-form-item label="货币符号">
      <div class="w-100 currency-symbol">
        <div class="user-left">
          <el-select
            v-model="optionModel.setChartStyle.currencySymbol"
            filterable
            placeholder
            allow-create
            default-first-option
            clearable
          >
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </div>
        <el-tooltip effect="dark" content="显示在数值前" placement="top">
          <span class="question-icon">
            <el-icon size="16">
              <ElIconQuestionFilled />
            </el-icon>
          </span>
        </el-tooltip>
      </div>
    </el-form-item>
    <el-form-item label="货币符号尺寸">
      <el-input-number class="w-100" v-model="optionModel.setChartStyle.currencySymbolSize" :min="14" />
    </el-form-item>
  </div>
</template>
<script>
export default {
  name: 'setChartStyle-editor',
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  },
  data() {
    return {
      options: [
        {
          value: '￥',
          label: '￥'
        },
        {
          value: '$',
          label: '$'
        },
        {
          value: '€',
          label: '€'
        },
        {
          value: '￡',
          label: '￡'
        }
      ],
      selectedOption: this.optionModel // 假设你想绑定 optionModel 到 select 组件
    }
  },
  watch: {
    optionModel: {
      handler(newVal) {
        this.selectedOption = newVal
      },
      immediate: true
    },
    selectedOption(newVal) {
      this.$emit('update:optionModel', newVal) // 使 v-model 工作
    }
  }
}
</script>
<style lang="scss" scoped>
.chart-styles {
  padding-top: 20px;
  .c-s-t {
    font-size: 14px;
    font-weight: bold;
    margin-bottom: 5px;
  }
  .question-icon {
    cursor: pointer;
    margin-left: 5px;
    position: relative;
    top: 4px;
  }
  .currency-symbol {
    .user-left {
      display: inline-block;
      width: calc(100% - 25px);
    }
  }
}
::v-deep .el-icon-arrow-down {
  margin-left: 0;
}
::v-deep .el-input-number .el-input__inner {
  text-align: center;
}
</style>
