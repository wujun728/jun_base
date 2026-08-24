<template>
  <div class="colorContainer">
    <div class="colorList">
      <span class="colorItem xmind-iconfont" v-for="item in colorList" :style="{ backgroundColor: item }"
        :class="{ icontouming: item === 'transparent' }" :key="item" @click="clickColorItem(item)"></span>
    </div>
    <div class="moreColor">
      <span>更多颜色</span>
      <el-color-picker size="mini" show-alpha v-model="selectColor" @change="changeColor"></el-color-picker>
    </div>
  </div>
</template>

<script>
import { colorList } from "../config";

export default {
  name: 'Color',
  props: {
    color: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      colorList,
      selectColor: ''
    }
  },
  watch: {
    color() {
      this.selectColor = this.color
    }
  },
  created() {
    this.selectColor = this.color
  },
  methods: {

    clickColorItem(color) {
      this.$emit('change', color)
    },


    changeColor() {
      this.$emit('change', this.selectColor)
    }
  }
}
</script>

<style lang="scss" scoped>
.colorList {
  width: 240px;
  display: flex;
  flex-wrap: wrap;

  .colorItem {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 15px;
    height: 15px;
    margin-right: 5px;
    margin-bottom: 5px;
    cursor: pointer;
  }
}

.moreColor {
  display: flex;
  align-items: center;

  span {
    margin-right: 5px;
  }
}
</style>
