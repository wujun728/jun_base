<template>
  <div class="scaleContainer">
    <el-tooltip
      class="item"
      effect="dark"
      content="缩小"
      placement="top"
    >
      <div class="nav-btn el-icon-minus" @click="narrow"></div>
    </el-tooltip>
    <div class="scaleInfo">
      <input
        ref="inputRef"
        type="text"
        v-model="scaleNum"
        @input="onScaleNumInput"
        @change="onScaleNumChange"
        @focus="onScaleNumInputFocus"
        @keydown.stop
        @keyup.stop
      />%
    </div>
    <el-tooltip
      class="item"
      effect="dark"
      content="放大"
      placement="top"
    >
      <div class="nav-btn el-icon-plus" @click="enlarge"></div>
    </el-tooltip>
  </div>
</template>

<script>
import { DRAW_CLICK, SCALE } from '../event-constant';


export default {
  name: 'Scale',
  props: {
    mindMap: {
      type: Object
    },
  },
  data() {
    return {
      scaleNum: 100,
      cacheScaleNum: 0
    }
  },
  created() {
    this.$bus.$on(SCALE, this.onScale)
    this.$bus.$on(DRAW_CLICK, this.onDrawClick)
  },
  mounted() {
    this.scaleNum = this.toPer(this.mindMap.view.scale)
  },
  beforeDestroy() {
    this.$bus.$off(SCALE, this.onScale)
    this.$bus.$off(DRAW_CLICK, this.onDrawClick)
  },
  methods: {
    // 转换成百分数
    toPer(scale) {
      return (scale * 100).toFixed(0)
    },

    // 缩小
    narrow() {
      this.mindMap.view.narrow()
    },

    // 放大
    enlarge() {
      this.mindMap.view.enlarge()
    },

    // 聚焦时缓存当前缩放倍数
    onScaleNumInputFocus() {
      this.cacheScaleNum = this.scaleNum
    },

    // 禁止输入非数字
    onScaleNumInput() {
      this.scaleNum = this.scaleNum.replace(/[^0-9]+/g, '')
    },

    // 手动输入缩放倍数
    onScaleNumChange() {
      const scaleNum = Number(this.scaleNum)
      if (Number.isNaN(scaleNum) || scaleNum <= 0) {
        this.scaleNum = this.cacheScaleNum
      } else {
        const cx = this.mindMap.width / 2
        const cy = this.mindMap.height / 2
        this.mindMap.view.setScale(this.scaleNum / 100, cx, cy)
      }
    },

    onScale(scale) {
      this.scaleNum = this.toPer(scale)
    },

    onDrawClick() {
      if (this.$refs.inputRef) this.$refs.inputRef.blur()
    }
  }
}
</script>

<style lang="scss" scoped>
.scaleContainer {
  display: flex;
  align-items: center;

  .nav-btn {
    cursor: pointer;
  }

  .scaleInfo {
    margin: 0 20px;
    display: flex;
    align-items: center;

    input {
      width: 35px;
      text-align: center;
      background-color: transparent;
      border: none;
      outline: none;
    }
  }
}
</style>
