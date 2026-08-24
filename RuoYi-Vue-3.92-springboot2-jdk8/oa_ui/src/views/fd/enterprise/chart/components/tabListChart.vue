<template>
  <div>
    <ul class="tab-list">
      <li class="font-weight800">
        <span>{{ $t('finance.serialnumber') }}</span>
        <span>{{ $t('finance.type') }}</span>
        <span>{{ $t('finance.amountmoney') }}</span>
        <span>{{ $t('finance.proportion') }}</span>
      </li>
    </ul>
    <ul class="tab-list tab-animate" @mouseenter="stop()" @mouseleave="up()">
      <li v-for="(item, index) in tableData" :class="{ 'animate-up': animateUp }" v-if="index < 10" :key="index">
        <span>
          <el-button type="info" size="mini" circle>{{ index + 1 }}</el-button>
        </span>
        <span v-if="item.cate_id > 0">{{ item.name }}</span>
        <span v-else>{{ $t('finance.other') }}</span>
        <span>{{ item.sum }}</span>
        <span>
          <el-progress :text-inside="true" :stroke-width="20" :percentage="Number(item.ratio)" />
        </span>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'TabListChart',
  props: {
    tableData: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      animateUp: false,
      timer: null
    }
  },
  methods: {
    scrollAnimate() {
      // 超出范围在后再进行滚动
      // if (this.tableData.length >= 5) {
      //   this.timer = setInterval(() => {
      //     this.animateUp = true
      //     setTimeout(() => {
      //       this.tableData.push(this.tableData[0])
      //       this.tableData.shift()
      //       this.animateUp = false
      //     }, 500)
      //   }, 3000)
      // }
    },
    // 鼠标移上去停止
    stop() {
      // clearInterval(this.timer)
    },
    // 鼠标离开继续滚动
    up() {
      // this.scrollAnimate()
    },
    destroyed() {
      clearInterval(this.timer)
    }
  },
  beforeDestroy() {
    clearInterval(this.timer)
    this.timer = null
  }
}
</script>

<style lang="scss" scoped>
.tab-list {
  margin: 0;
  padding: 0;
  list-style: none;
  color: #000000;
  position: relative;
  z-index: 2;
  ::v-deep .el-carousel__item {
    height: 40px;
  }
  li {
    padding: 11px 0;
    span {
      display: inline-block;
    }
    span:nth-of-type(1) {
      width: 80px;
      text-align: center;
      button {
        width: 28px;
        height: 28px;
        padding: 0;
      }
    }
    span:nth-of-type(2) {
      width: 120px;
    }
    span:nth-of-type(3) {
      width: 140px;
    }
    span:nth-of-type(4) {
      width: calc(100% - 360px);
    }
  }
}
.tab-animate {
  height: 242px;
  overflow: hidden;
  z-index: 1;
}
.animate-up {
  transition: all 0.5s ease-in-out;
  transform: translateY(-54px);
}
</style>
