
<template>
  <!-- 左侧文档树 -->
  <div>
    <div class="topic-header" @click="getTopicInfo">
      <div class="topic-name">
        <!-- <svg-icon icon-class="book"></svg-icon> -->
        <!-- <i class="el-icon-arrow-right"></i> -->
        {{ topicName }}
      </div>
    </div>
    <!-- 文档树 -->
    <div class="tree-container">
      <!-- 搜索栏和新增按钮 -->
      <div class="search-actions">
        <el-input v-model="filterText" placeholder="查找目录" clearable size="small" prefix-icon="el-icon-search" />
        <el-tooltip content="展开全部" placement="top" :open-delay="200">
          <el-button type="text" size="mini" @click="expandAll">
            <svg-icon icon-class="expand"></svg-icon>
          </el-button>
        </el-tooltip>
        <el-tooltip content="折叠全部" placement="top" :open-delay="200">
          <el-button type="text" size="mini" @click="collapseAll">
            <svg-icon icon-class="coll"></svg-icon>
          </el-button>
        </el-tooltip>
        <div v-if="hasDocAuth">
          <el-tooltip content="创建文档" placement="top" :open-delay="200">
            <el-button type="text" @click="handleAdd" class="ml10" size="mini">
              <svg-icon icon-class="add"></svg-icon>
            </el-button>
          </el-tooltip>
        </div>
      </div>
      <div class="tree-scroll-container" v-if="!loading">
        <el-tree
          :data="topicTree"
          :props="defaultProps"
          :expand-on-click-node="false"
          :filter-node-method="filterNode"
          :default-expanded-keys="defaultExpandedKeys"
          ref="tree"
          node-key="id"
          highlight-current
          @node-click="handleNodeClick"
        >
          <template #default="{ node, data }">
            <div class="tree-node-content">
              <!-- 显示输入框 or 文本 -->
              <div v-if="editingNodeId === data.id" class="editing-node">
                <el-input
                  v-model="editingNodeLabel"
                  @blur="submitRename(data)"
                  @keyup.enter.native="submitRename(data)"
                  @click.native.stop
                  autofocus
                  size="mini"
                  :ref="'inputField' + data.id"
                />
              </div>
              <span v-else class="tree-node-label">{{ node.label }}</span>
              <el-dropdown v-if="!editingNodeId && hasDocAuth" size="mini" @command="(command) => handleCommand(command, data)" @click.native.stop>
                <span class="dropdown-trigger">
                  <svg-icon icon-class="more" />
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="rename" icon="el-icon-edit-outline">重命名</el-dropdown-item>
                  <el-dropdown-item command="addChild" icon="el-icon-plus">新增子文档</el-dropdown-item>
                  <el-dropdown-item command="editDoc" icon="el-icon-edit">编辑文档</el-dropdown-item>
                  <el-dropdown-item command="moveUp" icon="el-icon-top">上移</el-dropdown-item>
                  <el-dropdown-item command="moveDown" icon="el-icon-bottom">下移</el-dropdown-item>
                  <el-dropdown-item command="deleteDoc" icon="el-icon-delete">删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </template>
        </el-tree>
      </div>
    </div>
  </div>
</template>

<script>
import { docTree, delDoc, resetName, reSort } from "@/api/kbs/topic/info";

export default {
  data() {
    return {
      // 加载状态
      loading: true,
      // 文档id
      docInfoId: null,
      // 父文档id
      parentDocId: null,
      // 新增文档id
      newDocId: null,
      // 新增文档的父节点
      newDocParentNode: null,
      // 是否新增文档
      isAddDoc: false,
      // 主题文档树
      topicTree: [],
      // 树展开状态
      expandFlag: false,
      // 默认展开的节点
      defaultExpandedKeys: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
      editingNodeId: null,
      editingNodeLabel: "",
      filterText: "",
      // 主题信息
      topicInfo: {},
    };
  },
  props: {
    topicId: {
      type: String,
      default: null,
    },
    topicName: {
      type: String,
      default: null,
    },
    hasDocAuth: {
      type: Boolean,
      default: false,
    },
  },
  mounted() {
    this.getTree();
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
    immediate: true,
  },
  methods: {
    /** 获取文档目录树 */
    async getTree() {
      let param = {
        topicId: this.topicId,
      };
      const res = await docTree(param);
      if (res.code === 200) {
        this.topicTree = res.data || [];
        const expandedIds = this.getExpandedNodeIds();
        this.defaultExpandedKeys = expandedIds || [];
        this.loading = false;
      }
    },
    /** 查询主题信息 */
    getTopicInfo() {
      this.$emit("view-topic");
    },
    /** 展开 */
    expandAll() {
      this.expandFlag = true;
      const node = this.$refs.tree.store.root;
      this.changeTreeNodeStatus(node);
    },
    /** 收起 */
    collapseAll() {
      this.expandFlag = false;
      const node = this.$refs.tree.store.root;
      this.changeTreeNodeStatus(node);
    },
    changeTreeNodeStatus(node) {
      node.expanded = this.expandFlag;
      for (let i = 0; i < node.childNodes.length; i++) {
        node.childNodes[i].expanded = this.expandFlag;
        if (node.childNodes[i].childNodes.length > 0) {
          this.changeTreeNodeStatus(node.childNodes[i]);
        }
      }
    },
    /** 获取当前已展开的节点 id 列表 */
    getExpandedNodeIds() {
      const expandedNodes = this.$refs.tree?.store?.root?.childNodes || [];
      const ids = [];
      const traverse = (nodes) => {
        for (const node of nodes) {
          if (node.expanded && node.data.id) {
            ids.push(node.data.id);
          }
          if (node.childNodes && node.childNodes.length > 0) {
            traverse(node.childNodes);
          }
        }
      };
      traverse(expandedNodes);
      return ids;
    },
    /** 新增文档（根节点新增） */
    handleAdd() {
      let node = {
        id: Date.now(),
        name: "无标题文档",
        parentId: null,
        isNew: true,
        children: [],
      };
      this.isAddDoc = true;
      this.newDocId = node.id;
      this.parentDocId = null;
      this.topicTree.push(node);
      this.$emit("add-doc", this.parentDocId);
    },
    /** 取消 */
    handleCancel() {
      // 新增节点则删除
      if (this.newDocId) {
        if (this.newDocParentNode && this.newDocParentNode.children) {
          const index = this.newDocParentNode.children.findIndex((child) => child.id === this.newDocId);
          if (index > -1) {
            this.newDocParentNode.children.splice(index, 1);
          }
        } else {
          const index = this.topicTree.findIndex((child) => child.id === this.newDocId);
          if (index > -1) {
            this.topicTree.splice(index, 1);
          }
        }
      } else {
        // 编辑已有节点，则重置树结构
        this.getTree();
      }
      this.resDetail();
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    // 节点单击事件
    async handleNodeClick(data) {
      this.docInfoId = data.id;
      this.resDetail();
      this.$emit("view-doc", data.id);
    },
    // 重置详情
    resDetail() {
      this.isAddDoc = false;
      this.editingNodeId = null;
      this.newDocId = null;
      this.newDocParentNode = null;
      this.parentDocId = null;
    },
    // 更多操作触发
    async handleCommand(command, row) {
      switch (command) {
        case "rename":
          this.editingNodeId = row.id;
          this.editingNodeLabel = row.name;
          this.$nextTick(() => {
            this.$refs.inputField && this.$refs.inputField[0].focus();
          });
          break;
        case "addChild":
          this.parentDocId = row.id;
          this.defaultExpandedKeys.push(this.parentDocId);
          this.addChildDocument(row);
          break;
        case "editDoc":
          this.docInfoId = row.id;
          this.$emit("edit-doc", row.id);
          break;
        case "deleteDoc":
          this.deleteDoc(row.id);
          break;
        case "moveUp":
          this.moveUp(row.id);
          break;
        case "moveDown":
          this.moveDown(row.id);
          break;
        default:
          break;
      }
    },
    /** 新增子文档 */
    addChildDocument(parentNode) {
      const newChild = {
        id: Date.now(),
        name: "无标题文档",
        parentId: parentNode.id,
        isNew: true,
      };
      // 插入子节点
      if (!parentNode.children) {
        this.$set(parentNode, "children", []);
      }
      this.newDocId = newChild.id;
      this.newDocParentNode = parentNode;
      this.isAddDoc = true;
      parentNode.children.push(newChild);
      this.$emit("add-doc", this.parentDocId);
    },
    /** 删除文档 */
    deleteDoc(id) {
      this.$modal
        .confirm("确定要删除此文档吗？")
        .then(() => {
          return delDoc(id);
        })
        .then(() => {
          this.removeTreeNodeById(id);
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 更新节点树名称 */
    updateNode(val) {
      const node = this.findTreeNodeById(this.isAddDoc ? this.newDocId : this.docInfoId);
      if (node) {
        node.name = val;
      }
    },
    /** 根据 id 查找树节点（递归查找） */
    findTreeNodeById(id, nodes = this.topicTree) {
      for (const node of nodes) {
        if (node.id === id) {
          return node;
        }
        if (node.children) {
          const found = this.findTreeNodeById(id, node.children);
          if (found) return found;
        }
      }
      return null;
    },
    /** 根据 id 查找树节点及其父节点（递归查找） */
    findTreeNodeAndParent(id, nodes = this.topicTree, parent = null) {
      for (const node of nodes) {
        if (node.id === id) {
          return { node, parent };
        }
        if (node.children) {
          const result = this.findTreeNodeAndParent(id, node.children, node);
          if (result) return result;
        }
      }
      return null;
    },
    /** 根据 id 删除树节点 */
    removeTreeNodeById(id) {
      const result = this.findTreeNodeAndParent(id);
      if (!result) return;
      const { node, parent } = result;
      if (!parent) {
        this.topicTree = this.topicTree.filter((n) => n.id !== id);
      } else {
        parent.children = parent.children.filter((n) => n.id !== id);
      }
      this.defaultExpandedKeys = this.getExpandedNodeIds() || [];
    },
    /** 重命名 */
    async submitRename(data) {
      if (!this.editingNodeLabel || this.editingNodeLabel.trim() === "") {
        return;
      }
      try {
        // 调用接口更新
        await resetName({
          id: data.id,
          name: this.editingNodeLabel,
        });
        // 更新本地树数据
        data.name = this.editingNodeLabel;
        this.$emit("reset-name", this.editingNodeLabel);
        // 清空状态
        this.editingNodeId = null;
        this.editingNodeLabel = "";
      } catch (error) {
        this.$message.error("重命名失败");
        console.error(error);
      }
    },
    /** 上移 */
    moveUp(id) {
      const { parentNode, index } = this.findNodeAndParent(this.topicTree, id);
      const siblings = parentNode ? parentNode.children : this.topicTree;
      if (index <= 0) return; // 第一个节点无法上移
      // 交换位置
      [siblings[index], siblings[index - 1]] = [siblings[index - 1], siblings[index]];
      // 更新 sort 字段
      const nodeA = siblings[index];
      const nodeB = siblings[index - 1];
      const sortA = nodeA.sort;
      const sortB = nodeB.sort;
      this.$set(nodeA, "sort", sortB);
      this.$set(nodeB, "sort", sortA);
      // 更新树结构
      this.defaultExpandedKeys = this.getExpandedNodeIds();
      if (parentNode) {
        const newParentNode = { ...parentNode, children: [...siblings] };
        const parentIndex = this.topicTree.findIndex((n) => n.id === newParentNode.id);
        this.$set(this.topicTree, parentIndex, newParentNode);
      } else {
        this.topicTree = [...siblings];
      }
      // 提交排序更新到后端
      let data = [nodeA, nodeB];
      this.updateSort(data);
    },
    /** 下移 */
    moveDown(id) {
      const { parentNode, index } = this.findNodeAndParent(this.topicTree, id);
      const siblings = parentNode ? parentNode.children : this.topicTree;
      if (index === -1 || index >= siblings.length - 1) return; // 最后一个节点无法下移
      // 交换位置
      [siblings[index], siblings[index + 1]] = [siblings[index + 1], siblings[index]];
      // 更新 sort 字段
      const nodeA = siblings[index];
      const nodeB = siblings[index + 1];
      const sortA = nodeA.sort;
      const sortB = nodeB.sort;
      this.$set(nodeA, "sort", sortB);
      this.$set(nodeB, "sort", sortA);
      // 更新树结构
      this.defaultExpandedKeys = this.getExpandedNodeIds();
      if (parentNode) {
        const newParentNode = { ...parentNode, children: [...siblings] };
        const parentIndex = this.topicTree.findIndex((n) => n.id === newParentNode.id);
        this.$set(this.topicTree, parentIndex, newParentNode);
      } else {
        this.topicTree = [...siblings];
      }
      // 提交排序更新到后端
      let data = [nodeA, nodeB];
      this.updateSort(data);
    },
    // 查找节点及其父节点
    findNodeAndParent(nodes, id, parent = null) {
      for (let i = 0; i < nodes.length; i++) {
        const node = nodes[i];
        if (node && node.id != null && node.id == id) {
          return { parentNode: parent, index: i };
        }
        if (node && node.children) {
          const result = this.findNodeAndParent(node.children, id, node);
          if (result && result.index !== -1) return result;
        }
      }
      return { parentNode: null, index: -1 };
    },
    // 提交排序
    updateSort(nodes) {
      reSort({ documents: nodes });
    },
  },
};
</script>

<style scoped lang="scss">
.topic-header {
  height: 90px;
  display: flex;
  align-items: center;
  font-size: 16px;
  color: #333;
  font-weight: bold;
  cursor: pointer;
  background: url("../../../../../assets/images/kbs-doc-header.jpg");
  background-position: right top;
  background-size: cover;
  background-repeat: no-repeat;
}

.topic-name {
  padding-left: 16px;
}
.tree-container {
  height: calc(88vh - 90px);
  display: flex;
  flex-direction: column;

  .search-actions {
    height: 40px;
    padding: 5px 10px 5px 5px;
    margin-bottom: 5px;
    border-bottom: 1px solid #f1f1f1;
    flex-shrink: 0;
    z-index: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .tree-scroll-container {
    padding-left: 12px;
    flex: 1;
    overflow-y: auto;
    overflow-x: hidden;
    padding-right: 12px;
    margin-bottom: 12px;
  }
}

.tree-node-content {
  display: flex;
  align-items: center;
  width: 100%;
}
.tree-node-label {
  flex: 1;
  font-size: 14px;
  width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.dropdown-trigger {
  z-index: 999;
}

::v-deep .search-actions .el-input input {
  border: none;
  border-radius: 0;
  font-size: 14px;
}
::v-deep .el-tree-node__content {
  height: 36px;
}

::-webkit-scrollbar {
  width: 3px;
}
::-webkit-scrollbar-track {
  background: #eee;
}
::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}
::-webkit-scrollbar-thumb:hover {
  background: #aaa;
}
</style>