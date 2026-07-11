<template>
  <!-- 选人组件-组织树可选 -->
  <div>
    <el-dialog title="组织选择" :visible.sync="dialogVisible" :width="width || '1050px'" append-to-body>
      <el-divider />
      <div class="select-box">
        <div class="select">
          <div class="select-content">
            <div class="content-box">
              <div class="header">
                <div class="check-box">
                  <svg-icon icon-class="tree" />
                </div>
                <div class="content-title">组织机构</div>
              </div>
              <div class="content-list">
                <div class="search-input">
                  <el-input v-model="deptName" prefix-icon="el-icon-search" size="small" placeholder="请输入组织名称" clearable />
                </div>
                <div class="tree-box">
                  <el-tree
                    ref="treeRef"
                    :data="treeOptions"
                    :props="defaultProps"
                    :expand-on-click-node="false"
                    :filter-node-method="filterNode"
                    :default-expanded-keys="defaultExpandedKeys"
                    node-key="id"
                    highlight-current
                    accordion
                    @node-click="handleNodeClick"
                  />
                </div>
              </div>
            </div>
          </div>
          <div class="select-content">
            <div class="content-box">
              <div class="header">
                <div class="content-title">部门列表</div>
                <div class="step-actions" v-if="type == 'multiple'">
                  <el-button type="text" size="small" @click="handleCheckedNodeAll(true)">全选</el-button>
                  <el-button type="text" size="small" @click="handleCheckedNodeAll(false)">取消全选</el-button>
                </div>
              </div>
              <div class="dept-box">
                <div class="dept-list">
                  <el-checkbox-group v-model="checkedDepts" @change="handleCheckedChange" :max="type == 'single' ? 1 : 2147483647">
                    <el-checkbox v-for="item in deptList" :label="item.id" :key="item.id" class="dept-card">
                      <div class="dept-card-content">
                        <div class="dept-info">
                          <div class="dept-name">{{ item.label }}</div>
                        </div>
                      </div>
                    </el-checkbox>
                  </el-checkbox-group>
                </div>
              </div>
            </div>
          </div>
          <div class="select-content">
            <div class="content-box">
              <div class="header">
                <div class="content-title">
                  已选择部门
                  <span class="selected-count">({{ selectedDeptList.length }})</span>
                </div>
                <div class="step-actions">
                  <el-button v-if="type == 'multiple'" type="text" size="small" @click="clearAll">清空</el-button>
                </div>
              </div>
              <div class="dept-box">
                <div class="dept-list">
                  <div v-for="item in selectedDeptList" :key="item.id" class="dept-card">
                    <div class="dept-card-content">
                      <div class="dept-info">
                        <div class="dept-name">{{ item.label }}</div>
                      </div>
                      <i class="el-icon-delete pointer" @click="deleteDept(item.id)"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
  
<script>
import { corpTree } from "@/api/system/user";

export default {
  name: "TreeSelect",
  props: {
    // 是否显示用户搜索
    showSearch: {
      type: Boolean,
      default: true,
    },
    // 初始化已选择部门
    selectedDepts: {
      type: Array,
      default: () => [],
    },
    // 弹框宽度
    width: String,
    // 弹框高度
    height: String,
    // 选人类型，单选：single，多选：multiple
    type: {
      type: String,
      default: "single",
    },
    open: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      loading: false,
      dialogVisible: this.open,
      // 树节点默认展开的节点id（根据实际根节点的id调整）
      defaultExpandedKeys: [100],
      // 组织树选项数据
      treeOptions: [],
      // 部门名称
      deptName: "",
      // 待选列表
      deptList: [],
      // 待选列表-选中的部门
      checkedDepts: [],
      // 已选择部门列表
      selectedDeptList: [],
      // 所有部门map对象
      allDeptMap: {},
      // el-tree配置选项
      defaultProps: {
        children: "children",
        label: "label",
      },
    };
  },
  watch: {
    open(newVal) {
      this.dialogVisible = newVal;
      if (newVal && this.selectedDepts && this.selectedDepts.length > 0) {
        this.checkedDepts = this.selectedDepts.map((dept) => dept.id);
        // 处理已选择栏
        this.selectedDepts.forEach((dept) => {
          this.$set(this.allDeptMap, dept.id, dept);
        });
        const parentNode = this.findParentNode(this.treeOptions, this.selectedDepts[0].id);
        // 模拟点击树
        this.handleNodeClick(parentNode);
        this.updateCheckedDepts();
      } else {
        this.checkedDepts = [];
        this.selectedDeptList = [];
      }
    },
    dialogVisible(newVal) {
      this.$emit("update:open", newVal);
    },
    deptName(val) {
      if (this.$refs.treeRef) {
        this.$refs.treeRef.filter(val);
      }
    },
    deep: true,
    immediate: true,
  },
  mounted() {
    this.getTree();
  },
  methods: {
    /** 获取组织树 */
    async getTree() {
      try {
        corpTree().then((response) => {
          this.treeOptions = response.data;
        });
      } catch (error) {
        console.error("获取部门树失败:", error);
        this.treeOptions = [];
      }
    },
    /** 组织树节点点击事件 */
    handleNodeClick(node) {
      if (!node) return;
      const children = node.children || [];
      this.deptList = [...children];
      children.forEach((dept) => {
        this.$set(this.allDeptMap, dept.id, dept);
      });
    },
    /** 选择用户处理 */
    handleCheckedChange() {
      this.updateCheckedDepts();
    },
    /** 全选操作 */
    handleCheckedNodeAll(checked) {
      if (checked) {
        this.deptList.forEach((item) => {
          if (!this.checkedDepts.includes(item.id)) {
            this.checkedDepts.push(item.id);
          }
        });
      } else {
        this.checkedDepts = this.checkedDepts.filter((id) => !this.deptList.some((item) => item.id === id));
      }
      this.updateCheckedDepts();
    },
    /** 删除已选 */
    deleteDept(id) {
      const index = this.checkedDepts.indexOf(id);
      if (index > -1) {
        this.checkedDepts.splice(index, 1);
        this.updateCheckedDepts();
      }
    },
    /** 清空 */
    clearAll() {
      this.checkedDepts = [];
      this.updateCheckedDepts();
    },
    /** 确认 */
    submitForm() {
      this.$emit("confimDept", this.selectedDeptList);
      this.dialogVisible = false;
    },
    /** 取消 */
    cancel() {
      this.dialogVisible = false;
      this.$emit("cancel");
    },
    /** 过滤节点 */
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    /**
     * 根据部门 id 查找所属的父节点
     */
    findParentNode(tree, deptId) {
      for (const node of tree) {
        if (node.children && node.children.some((child) => child.id === deptId)) {
          return node;
        }
        if (node.children) {
          const result = this.findParentNode(node.children, deptId);
          if (result) return result;
        }
      }
      return null;
    },
    /** 更新已选部门 */
    updateCheckedDepts() {
      this.selectedDeptList = this.checkedDepts.map((id) => this.allDeptMap[id]).filter(Boolean);
    },
  },
};
</script>

<style lang="scss" scoped>
/* 修改深度选择器 */
::v-deep .el-tree {
  .el-tree-node__content {
    height: 32px;

    &:hover {
      background-color: #f5f7fa;
    }
  }
}

.select-box {
  width: 100%;
  min-width: 1000px;
  display: flex;
  flex-direction: column;
  border: 1px solid rgb(235, 235, 235);
  padding: 12px;

  .select {
    width: 100%;
    display: flex;
    margin-top: 0;

    .select-content {
      width: 320px;
      height: 420px;
      margin-right: 12px;
      background: white;
      border-radius: 4px;
      border: 1px solid #f5f5f5;
      overflow: hidden;
      box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.12), 0 0 6px 0 rgba(0, 0, 0, 0.04);

      &:last-child {
        margin-right: 0;
      }
      .content-box {
        height: 100%;
        display: flex;
        flex-direction: column;
      }
    }
  }
}

.tree-box {
  width: 100%;
  height: 300px;
  overflow-x: hidden;
  overflow-y: auto;
  flex: 1;
  overflow: auto;
  background-color: white;
  border-radius: 4px;
}

.dept-box {
  width: 100%;
  height: 370px;
  display: flex;
  flex-direction: column;

  .dept-list {
    flex: 1;
    overflow-x: hidden;
    overflow-y: auto;
    height: 370px;
  }
}

.dept-card {
  display: flex;
  align-items: center;
  padding: 8px 10px;
  width: 100%;
  border-bottom: 1px solid #f5f5f5;
  transition: all 0.3s;

  &:hover {
    background-color: #f5f7fa;
  }

  .dept-card-content {
    width: 100%;
    padding: 4px;
    position: relative;
    display: flex;
    align-items: center;

    .dept-info {
      flex: 1;
      min-width: 0;

      .dept-name {
        font-size: 13px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 2px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
  }

  &:hover .delete-icon {
    opacity: 1;
  }
}

.loading-more {
  text-align: center;
  padding: 10px 0;
  color: #909399;
  font-size: 12px;

  .el-icon {
    margin-right: 4px;
    font-size: 14px;
  }
}

.header {
  width: 100%;
  height: 40px;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  padding: 0 12px;
  position: relative;
  border-bottom: 1px solid #f5f5f5;

  .check-box {
    font-size: 14px;
    margin-right: 3px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .content-title {
    font-size: 14px;
    font-weight: 500;
    color: #303133;
    display: flex;
    align-items: center;
    gap: 4px;

    .selected-count {
      color: #409eff;
      font-size: 13px;
      font-weight: normal;
    }
  }

  .step-actions {
    position: absolute;
    right: 12px;
    display: flex;
    flex-wrap: wrap;
    align-content: flex-end;
    justify-content: space-between;
    align-items: center;
  }
}

.content-list {
  padding: 12px;
  height: calc(100% - 40px);
  display: flex;
  flex-direction: column;
}

.search-input {
  margin-bottom: 12px;
  width: 100%;
}

::v-deep .el-checkbox {
  height: auto;
}
::v-deep .el-checkbox__input {
  margin-right: 4px;
  padding-left: 4px;
}

::v-deep .el-checkbox__label {
  padding-left: 0;
  flex: 1;
}
::-webkit-scrollbar {
  width: 4px;
}

::-webkit-scrollbar-thumb {
  background-color: #dcdfe6;
  border-radius: 3px;
}

::-webkit-scrollbar-track {
  background-color: #f5f7fa;
}
</style>