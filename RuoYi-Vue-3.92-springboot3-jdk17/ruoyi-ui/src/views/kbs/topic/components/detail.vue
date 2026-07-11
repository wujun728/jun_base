
<template>
  <!-- 文档详情 -->
  <div class="doc-container">
    <el-row :gutter="24">
      <el-col :span="!showTree ? 0 : 5" :xs="24" class="left-panel">
        <!-- 左侧文档树 -->
        <topic-tree
          v-if="showTree"
          ref="tree"
          :topicId="topicId"
          :topicName="topicName"
          :hasDocAuth="hasDocAuth"
          @add-doc="handleAddDoc"
          @edit-doc="handleEditDoc"
          @view-doc="handleViewDoc"
          @reset-name="handleReName"
          @view-topic="handleViewTopic"
        />
      </el-col>
      <el-col :span="!showTree ? 24 : 19" :xs="24" class="right-panel">
        <!-- 右侧文档内容 -->
        <doc-info
          ref="docInfos"
          v-if="showDocInfo || !showTree"
          :topicId="topicId"
          :docId="docId"
          :parentNodeId="parentNodeId"
          @update-node="handleNodeTitle"
          @add-success="handSuccess"
          @cancel="handleCancel"
        />
        <!-- 主题信息 -->
        <topic-info v-if="showTopicInfo && showTree" :topicId="topicId" @update-auth="handleDocAuth" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import TopicTree from "./info/topic-tree.vue";
import DocInfo from "./info/doc-info.vue";
import TopicInfo from "./info/topic-info.vue";
export default {
  name: "TopicDetail",
  components: { TopicTree, DocInfo, TopicInfo },
  data() {
    return {
      // 显示主题信息
      showTopicInfo: true,
      // 显示文档信息
      showDocInfo: false,
      // 主题id
      topicId: this.$route.query.id,
      // 主题名称
      topicName: this.$route.query.title,
      // 显示文档树
      showTree: this.$route.query.isTree === "true",
      // 文档id
      docId: null,
      // 父节点id
      parentNodeId: null,
      // 是否显示权限控制
      hasDocAuth: false,
    };
  },
  watch: {
    // 深度监听路由变化
    $route: {
      handler(to, from) {
        const newDocId = to.query.docId;
        if (newDocId && newDocId !== this.docId) {
          this.handleFlag();
          this.$nextTick(() => {
            this.docId = newDocId;
          });
        }
      },
      deep: true,
      immediate: true,
    },
  },
  beforeRouteUpdate(to, from, next) {
    const newDocId = to.query.docId;
    this.docId = newDocId || null;
    next();
  },
  methods: {
    /** 新增文档事件 */
    handleAddDoc(id) {
      this.handleFlag();
      this.$nextTick(() => {
        this.parentNodeId = id || null;
        this.$refs.docInfos.handleAddDocFlag();
      });
    },
    /** 编辑事件 */
    handleEditDoc(docId) {
      this.handleFlag();
      this.$nextTick(() => {
        this.parentNodeId = null;
        this.docId = docId;
        this.$refs.docInfos.editDocInfo(docId);
      });
    },
    /** 节点点击事件 */
    handleViewDoc(docId) {
      this.handleFlag();
      this.$nextTick(() => {
        this.docId = docId;
      });
    },
    /** 更新节点名称 */
    handleNodeTitle(title) {
      this.$refs.tree.updateNode(title);
    },
    /** 新增文档提交成功，刷新树 */
    handSuccess(docId) {
      this.docId = docId;
      this.$refs.tree.getTree();
    },
    /** 取消新增文档 */
    handleCancel() {
      this.$refs.tree.handleCancel();
    },
    /** 重命名 */
    handleReName(val) {
      this.handleFlag();
      this.$refs.docInfos.updateTitle(val);
    },
    /** 更新文档权限 */
    handleDocAuth(val) {
      this.hasDocAuth = val;
    },
    /** 状态处理 */
    handleFlag() {
      this.showTopicInfo = false;
      this.showDocInfo = true;
    },
    /** 查看主题信息 */
    handleViewTopic() {
      this.docId = null;
      this.showTopicInfo = true;
      this.showDocInfo = false;
    },
  },
};
</script>

<style scoped lang="scss">
.doc-container {
  padding: 0 0 12px 0;
}
.left-panel {
  padding-right: 0 !important;
}

.right-panel {
  height: calc(88vh - 10px);
  background-color: #fff;
}
</style>