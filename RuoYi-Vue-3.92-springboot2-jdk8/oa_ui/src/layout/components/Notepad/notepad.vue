<template>
  <div class="notepad">
    <el-tooltip content="记事本" effect="dark" placement="bottom">
      <i class="iconfont iconjishiben2 pointer notepad" @click="handleNotepad"></i>
    </el-tooltip>
    <el-drawer
      title="记事本"
      :with-header="false"
      :visible.sync="drawer"
      direction="rtl"
      :before-close="handleClose"
      size="90%"
      :append-to-body="true"
      :modal="true"
      :wrapper-closable="true"
    >
      <memorandum ref="memorandum" :style="style1" @handleClose="handleClose"></memorandum>
    </el-drawer>
  </div>
</template>
<script>
export default {
  name: 'Notepad',
  components: {
    memorandum: () => import('@/views/user/memorandum/index')
  },
  data() {
    return {
      drawer: false,
      style1: '1'
    }
  },
  methods: {
    // 去记事本
    handleNotepad() {
      if (this.$route.name === 'user-memorandum') return
      this.openBox()
    },
    openBox() {
      this.drawer = true
      setTimeout(() => {
        this.$refs.memorandum.createdEvent()
      }, 300)
    },
    handleClose() {
      this.drawer = false
      this.$refs.memorandum.handleRemove()
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep.el-drawer__header {
  border-bottom: 1px solid #eeeeee;
  padding: 0 24px;
  height: 50px;
  line-height: 20px;
  padding-right: 14px;
}

::v-deep .el-drawer__body {
  padding-bottom: 0;
  overflow-y: hidden !important;
}
.divBox {
  margin: 0px;
  padding-top: 0;
}
.tab-box {
  display: flex;
  align-items: center;
  .tab-item {
    margin-right: 30px;
    font-size: 14px;
    color: #000000;
    cursor: pointer;
    &.on {
      color: #1890ff;
    }
  }
}
.form-wrapper {
  padding-right: 63px;

  .form-box {
    display: flex;
    flex-wrap: wrap;
    margin-top: 40px;
    .form-item {
      width: 50%;
    }
  }
}
.tips-txt {
  font-size: 13px;
  color: #999999;
}
.table-box {
  padding: 0 13px;
  .table-item {
    padding-bottom: 29px;
    border-bottom: 1px dashed rgba(102, 102, 102, 0.3);
    .title {
      margin: 20px 0 14px;
      padding-left: 10px;
      border-left: 2px solid #1890ff;
      font-size: 14px;
    }
  }
}
.add-btn {
  margin-top: 15px;
}
.item-box {
  display: flex;
  flex-wrap: wrap;
  .item {
    margin-left: 0 !important;
    margin-right: 10px;
    margin-bottom: 10px;
  }
}
::v-deep.el-popover {
  min-width: 166px;
}
.prop-txt {
  width: 166px;
  margin-right: 10px;
  height: 30px;
  line-height: 30px;
  font-size: 13px;
  cursor: pointer;
}
</style>
