<template>
  <div>
    <!-- 选择供应商 -->
    <el-dialog
      title="选择供应商"
      :visible.sync="show"
      width="650px"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <div class="list">
        <div
          v-for="(item, index) in list"
          class="item"
          :class="item.value == activeValue ? 'active' : ''"
          @click="handleClick(item)"
        >
          <img :src="require(`../../../assets/chat/${item.pic}`)" alt="" class="img" />
          {{ item.label }}
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
// import modelDialog from './modelDialog'
export default {
  name: '',
  components: {},
  props: {
    list: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      show: false,
      activeValue: ''
    }
  },

  methods: {
    openBox(val) {
      if (val) {
        this.activeValue = val.value
      }
      this.show = true
    },
    getList() {
      this.handleClose()
      this.$emit('isOk')
    },
    handleClick(item) {
      this.activeValue = item.value
      this.$emit('openModelDialog', item)
      setTimeout(() => {
        this.handleClose()
      }, 500)
    },
    handleClose() {
      this.show = false
      this.activeValue = ''
    }
  }
}
</script>
<style scoped lang="scss">
::v-deep .el-dialog__body {
  min-height: 300px;
}
.list {
  box-sizing: border-box; /* 防止padding影响高度 */
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(295px, 1fr));
  grid-auto-rows: minmax(56px, auto); /* 行高自适应内容，最小150px */
  gap: 20px; /* 卡片间距 */
  padding-bottom: 10px;

  .item {
    cursor: pointer;
    background: #ffffff;
    border-radius: 4px 4px 4px 4px;
    border: 1px solid #dcdfe6;
    display: flex;
    align-items: center;
    padding-left: 14px;
    font-weight: 400;
    font-size: 14px;
    color: #303133;
  }
  .active {
    border-color: #1890ff;
  }
}
.img {
  width: 28px;
  height: 28px;
  background: #ffffff;
  border-radius: 4px 4px 4px 4px;
  margin-right: 13px;
}
</style>
