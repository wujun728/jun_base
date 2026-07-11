
<template>
  <div class="custom-radio-group">
    <div v-for="item in typeList" :key="item.id" class="custom-radio-item" :class="{ checked: selectedId === item.id }">
      <div @click="toggle(item.id)" class="radio-content">
        <span class="radio-icon">
          <i v-if="selectedId === item.id" class="el-icon-check"></i>
        </span>
        <span class="radio-label">{{ item.title }}</span>
      </div>
      <!-- 更多操作 -->
      <el-dropdown size="mini" @command="(command) => handleCommand(command, item)">
        <span class="dropdown-trigger">
          <svg-icon icon-class="more" />
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="handleUpdate" icon="el-icon-edit">修改</el-dropdown-item>
          <el-dropdown-item command="handleDelete" icon="el-icon-delete">删除</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template> 
  
  <script>
export default {
  props: {
    value: { type: [String, Number], default: null },
    typeList: { type: Array, required: true },
  },
  data() {
    return {
      selectedId: this.value,
    };
  },
  watch: {
    value(newVal) {
      this.selectedId = newVal;
    },
    selectedId(newVal) {
      this.$emit("input", newVal);
    },
  },
  methods: {
    // 已选中则取消，否则选中
    toggle(id) {
      if (this.selectedId === id) {
        this.selectedId = null;
      } else {
        this.selectedId = id;
      }
      this.$emit("checked", this.selectedId);
    },
    /** 重置 */
    reset() {
      this.selectedId = null;
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleUpdate":
          this.$emit("handle-update", row.id);
          break;
        case "handleDelete":
          this.$emit("handle-delete", row.id);
          break;
        default:
          break;
      }
    },
  },
};
</script>
  
<style scoped lang="scss">
.custom-radio-group {
  display: flex;
  flex-direction: column;
}

.custom-radio-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 0 10px 15px;
  // cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.2s ease;

  &:hover {
    background-color: #ecf5ff;
  }

  &.checked .radio-icon {
    background-color: #409eff;
    border-color: #409eff;

    i {
      color: #fff;
      font-size: 12px;
    }
  }

  &.checked .radio-label {
    color: #409eff;
  }
}

.radio-content {
  display: flex;
  align-items: center; // 图标和文字水平排列并垂直居中
  flex-grow: 1; // 占据左侧空间
  cursor: pointer;
}

.radio-icon {
  width: 15px;
  height: 15px;
  border: 2px solid #dcdfe6;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 10px;
  transition: all 0.2s ease;
}

.radio-label {
  font-size: 14px;
  color: #606266;
}

.el-dropdown {
  margin-left: auto; // 推到最右侧
}

.dropdown-trigger {
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.svg-icon {
  width: 1.5em;
}
</style>