<template>
  <div class="chart-styles">
    <div class="c-s-t">图表样式</div>
    <template v-for="(style, styleInx) of stylesList">
      <div class="c-s-c" v-if="style.type == selectedWidget.type" :key="styleInx">
        <div
          class="prive-box"
          v-for="(item, inx) of style.list"
          :key="inx"
          :class="{ 'is-active': option.chartStyle == item.type, 'is-disabled': checkIsDisabled(item, style.type) }"
          @click="checkIsDisabled(item, style.type) ? () => {} : chartStyleChange(item)"
        >
          <el-tooltip effect="dark" :content="item.label || 'error'" placement="bottom">
            <div class="icon-box">
              <SvgIcon class="sort-icon ml-3" :icon-class="item.icon" />
            </div>
          </el-tooltip>
        </div>
      </div>
    </template>
  </div>
</template>
<script>
import SvgIcon from '@/components/svg-icon-nc'

export default {
  name: 'chartStyle-editor',
  components: {
    SvgIcon
  },
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  },
  data() {
    return {
      option: {},
      stylesList: [
        {
          type: 'barChart',
          list: [
            {
              type: 1,
              label: '普通柱状图',
              icon: 'icona-zu10206'
            },
            {
              type: 2,
              label: '堆积状态图',
              icon: 'icona-zu10212'
            },
            {
              type: 3,
              label: '百分比堆积状态图',
              icon: 'icona-zu10210'
            }
          ]
        },
        {
          type: 'barXChart',
          list: [
            {
              type: 1,
              label: '普通条形图',
              icon: 'icona-zu10218'
            },
            {
              type: 2,
              label: '堆积条形图',
              icon: 'icona-zu10220'
            },
            {
              type: 3,
              label: '百分比堆积条形图',
              icon: 'icona-zu10222'
            }
          ]
        },
        {
          type: 'lineChart',
          list: [
            {
              type: 1,
              label: '折线图',
              icon: 'icona-zu10205'
            },
            {
              type: 2,
              label: '曲线图',
              icon: 'icona-zu10202'
            }
          ]
        },
        {
          type: 'pieChart',
          list: [
            {
              type: 1,
              label: '实心',
              icon: 'icona-zu10087'
            },
            {
              type: 2,
              label: '环形',
              icon: 'icona-zu10083'
            }
          ]
        },
        {
          type: 'progressbar',
          list: [
            {
              type: 1,
              label: '环形',
              icon: 'icona-zu10087'
            },
            {
              type: 2,
              label: '进度条',
              icon: 'icona-zu10216-01'
            },
            {
              type: 3,
              label: '水波图',
              icon: 'icona-zu10086-2'
            }
          ]
        }
      ]
    }
  },
  watch: {
    optionModel: {
      handler(newVal) {
        this.initchartStyle()
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
    this.initchartStyle()
  },
  methods: {
    initchartStyle() {
      this.option = this.optionModel
    },
    checkIsDisabled(item, chartType) {
      if (this.option.setDimensional) {
        let { dimension, metrics } = this.option.setDimensional
        if (
          (chartType == 'barChart' || chartType == 'barXChart') &&
          item.type != 1 &&
          (dimension.length > 1 || metrics.length < 2)
        ) {
          return true
        }
      }
      return false
    },
    chartStyleChange(item) {
      if (this.option.chartStyle == item.type) {
        return
      }
      this.option.chartStyle = item.type
      this.$emit('update:optionModel', this.option)
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
  }
  .c-s-c {
    .prive-box {
      width: 30px;
      height: 30px;
      display: inline-block;
      border: 3px solid #fff;
      text-align: center;
      cursor: pointer;
      .icon-box {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 30px;
        width: 30px;
        .sort-icon {
          width: 18px;
          height: 18px;
          color: #707070;
          margin-right: 7px;
          margin-bottom: 5px;
        }
      }

      &.is-active {
        border-color: var(--el-color-primary);
        .sort-icon {
          color: var(--el-color-primary);
        }
      }
      &.is-disabled {
        opacity: 0.3;
        cursor: not-allowed;
      }
    }
  }
}
</style>
