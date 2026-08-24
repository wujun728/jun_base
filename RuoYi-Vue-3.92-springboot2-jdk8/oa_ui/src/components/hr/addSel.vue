<!-- @FileDescription: 人事-审批设置-表单内容配置选项组件 -->
<template>
  <div>
    <draggable :list="activeData" :animation="340" group="selectItem" handle=".option-drag" @update="add1">
      <div v-for="(item, index) in activeData" :key="index" class="select-item mb5">
        <div class="select-line-icon option-drag mr5">
          <i class="iconfont icontuodong" />
        </div>
        <el-input
          v-model="item.label"
          placeholder="请输入内容"
          class="mr5"
          @input="setOptionValue(item, $event)"
        ></el-input>
        <div class="close-btn select-line-icon" @click="activeData.splice(index, 1)">
          <i class="el-icon-remove-outline" />
        </div>
      </div>
    </draggable>
    <div style="margin-left: 20px">
      <el-button style="padding-bottom: 0" icon="el-icon-circle-plus-outline" type="text" @click="addSelectItem"
        >添加选项</el-button
      >
    </div>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
export default {
  name: 'AddSel',
  components: {
    draggable
  },
  props: {
    value: {
      default: () => [],
      type: Array
    },
    disabled: Boolean
  },
  data() {
    return {
      activeData: this.value
    }
  },
  watch: {
    value(n) {
      this.activeData = n
    }
  },
  methods: {
    add1() {
      this.$emit('input', this.activeData)
    },
    addSelectItem() {
      this.activeData.push({
        label: '',
        value: ''
      })
      this.$emit('input', this.activeData)
    },
    setOptionValue(item, val) {
      item.label = val
      item.value = val
      this.$emit('input', this.activeData)
    }
  }
}
</script>

<style scoped lang="scss">
.icontuodong {
  cursor: pointer;
}
.select-item {
  display: flex;
  border: 1px dashed #fff;
  box-sizing: border-box;

  & .close-btn {
    cursor: pointer;
    color: #f56c6c;
  }

  & .el-input + .el-input {
    margin-left: 4px;
  }
}
</style>
