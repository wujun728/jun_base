<template>
  <div class="bar-chart" v-resize="handleResize" @click.stop="setSelected">
    <template v-if="isShowEmpty || (myOption.isNoData && previewState)">
      <div class="empty-div">
        <img src="../../assets/images/empty/statistics.png" alt="" class="img" />
        <span>暂无数据</span>
      </div>
    </template>
    <template v-else>
      <scEcharts
        class="chart"
        ref="scEchartsRefs"
        :option="myOption"
        :field="field"
        :screenWidth="screenWidth"
        v-if="!myOption.isNoData"
      ></scEcharts>
      <div class="no-data" v-else>
        请通过右侧
        <span class="lh">维度指标设置</span> 维度、指标栏来添加数据
      </div>
    </template>
  </div>
</template>

<script>
import scEcharts from './index.vue'

export default {
  name: 'chartWidget',
  components: {
    scEcharts
  },
  inject: ['previewState'],
  props: {
    option: {
      type: Object,
      default: () => ({})
    },
    field: {
      type: Object,
      default: () => ({})
    },
    designer: {
      type: Object,
      default: () => ({})
    },
    isShowEmpty: {
      type: Boolean,
      default: true
    },
    screenWidth: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      myOption: {
        isNoData: true
      },
      scEchartsRefs: null
    }
  },
  watch: {
    option: {
      handler(newVal) {
        this.myOption = newVal
      },
      deep: true
    }
  },
  mounted() {
    this.myOption = this.option
  },
  methods: {
    handleResize() {
      if (!this.myOption.isNoData) {
        this.$nextTick(() => {
          this.$refs.scEchartsRefs?.myChart?.resize()
        })
      }
    },
    setSelected() {
      if (this.previewState) return
      this.designer?.setSelected(this.field)
    }
  }
}
</script>
<style lang="scss" scoped>
.img {
  width: 128px;
  height: 128px;
}
.bar-chart {
  width: 100%;
  height: 100%;
  .chart {
    width: 100%;
    height: 100%;
  }
}
.no-data {
  font-size: 14px;
  .lh {
    color: var(--el-color-primary);
  }
}
.empty-div {
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  container-type: inline-size;
  span {
    // font-size: 8cqw;
    // color: var(--el-text-color-secondary);
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 13px;
    color: #9e9e9e;
  }
}
</style>
