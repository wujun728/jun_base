
<template>
  <!-- 文档内容 -->
  <div>
    <!-- 固定头部 -->
    <div class="right-detail-header" v-if="showDetaile && formData && formData.name">
      <div class="title">{{ formData.name }}</div>
      <div class="footer-info">
        <span>作者：{{ formData.createBy }}</span>
        <span>
          <i class="el-icon-time" />
          发布时间：{{ parseTime(formData.createTime, '{y}-{m}-{d}') }}
        </span>
        <span>
          <i class="el-icon-view"></i>
          阅读：
          {{ docStat.viewNum }}
        </span>
        <el-tooltip content="查看评论" placement="bottom" :open-delay="200">
          <el-button type="text" size="mini" @click="goComment" class="foot-btn">
            <i class="el-icon-chat-dot-round"></i>
            评论：
            {{ docStat.commentNum }}
          </el-button>
        </el-tooltip>
        <el-tooltip :content="docStat.isFavorite ? '取消收藏' : '点击收藏'" placement="bottom" :open-delay="200">
          <el-button v-if="docStat.isFavorite" type="text" size="mini" @click="cancelFavoriteDoc" class="foot-btn">
            <i class="el-icon-star-on"></i>
            收藏：
            ({{ docStat.favoriteNum }})
          </el-button>
          <el-button v-else type="text" size="mini" @click="favoriteDoc" class="foot-btn">
            <i class="el-icon-star-off"></i>
            收藏：
            ({{ docStat.favoriteNum }})
          </el-button>
        </el-tooltip>
      </div>
    </div>
    <div v-if="showEditBtn" class="right-edit-header">
      <el-button type="primary" size="mini" @click="submitDoc">保 存</el-button>
      <el-button plain size="mini" @click="handleCancel">取 消</el-button>
    </div>
    <!-- 内容区域 -->
    <div class="content-wrapper" v-if="!openEditor">
      <!-- 文档详情 -->
      <div v-if="formData && formData.content" class="content-main">
        <div class="content-body">
          <el-row :gutter="24">
            <el-col :span="5" class="out-line">
              <div class="outline-title">
                <span>
                  <svg-icon icon-class="list1" class="mr2" />大纲
                </span>
                <el-tooltip v-if="showOutline" content="隐藏大纲" placement="top" :open-delay="200">
                  <el-button type="text" size="mini" @click="showOutline = false">
                    <svg-icon icon-class="unshow" class="ml5" />
                  </el-button>
                </el-tooltip>
                <el-tooltip v-else content="显示大纲" placement="top" :open-delay="200">
                  <el-button type="text" size="mini" @click="showOutline = true">
                    <svg-icon icon-class="show" class="ml5" />
                  </el-button>
                </el-tooltip>
              </div>
              <div v-if="showOutline" class="outline-list" ref="outlineListContainer">
                <outline-item :items="outlines" :active-id="activeOutlineId" @navigate="handleNavigate" />
              </div>
            </el-col>
            <el-col :span="18" class="article-section">
              <div ref="scrollContainer" class="html-content" v-html="artical" @scroll="handleScroll"></div>
              <!-- 回到顶部按钮 -->
              <div v-if="showBackToTop" class="back-to-top" @click="backToTop" @mouseenter="showTooltip = true" @mouseleave="showTooltip = false">
                <i class="el-icon-arrow-up"></i>
                <span v-if="showTooltip" class="tooltip-text">回到顶部</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <!-- WangEditor 编辑器 -->
    <div v-if="openEditor" class="editor-section">
      <add-doc ref="editor" :topicId="topicId" :docInfoId="docInfoId" @update-node="updateNodeTitle" @close="handleCancel" />
    </div>
    <!-- 收藏 -->
    <favorite
      :topicId="topicId"
      :favoriteOpen="favoriteOpen"
      :favoriteObject="favoriteObject"
      @close-favorite="closeFavoriteDialog"
      @success-favorite="successFavoriteForm"
    />
    <!-- 评论 -->
    <comment :open="openComment" :docId="docId" :commentNum="docStat.commentNum" @close-comment="closeComment" @submit-comment="submitComment" />
  </div>
</template>

<script>
import { docInfo, addDoc, editDoc } from "@/api/kbs/topic/info";
import { statDocumentRelate, addView } from "@/api/kbs/document/document";
import { cancelFavorite } from "@/api/kbs/favorite/favorite";
import AddDoc from "./doc-editor.vue";
import OutlineItem from "./outline-item.vue";
import Favorite from "./favorite.vue";
import Comment from "./comment.vue";
import { getToken } from "@/utils/auth";

export default {
  components: {
    AddDoc,
    OutlineItem,
    Favorite,
    Comment,
  },
  data() {
    return {
      // 加载状态
      loading: true,
      // 编辑器控制开关
      openEditor: false,
      // 显示文档详情
      showDetaile: false,
      // 头部按钮显示控制
      showEditBtn: false,
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
      // 文档信息
      formData: {},
      // 显示或隐藏大纲
      showOutline: true,
      // 文章大纲
      outlines: [],
      // 文章内容
      artical: null,
      // 当前激活的大纲项 ID
      activeOutlineId: null,
      // 是否显示收藏组弹出框
      favoriteOpen: false,
      // 收藏文档对象
      favoriteObject: {},
      // 文档统计信息
      docStat: {},
      // 是否显示回到顶部按钮
      showBackToTop: false,
      // 回到顶部提示文字
      showTooltip: false,
      // 打开评论
      openComment: false,
      // token
      token: getToken(),
    };
  },
  props: {
    topicId: {
      type: String,
      default: null,
    },
    docId: {
      type: String,
      default: null,
    },
    parentNodeId: {
      type: String,
      default: null,
    },
  },
  watch: {
    docId: function (newVal, oldVal) {
      if (!newVal) return;
      if (newVal != oldVal) {
        this.docInfoId = newVal;
        this.showOutline = true;
        this.getInfo();
        this.statDocInfo();
        this.addViewRecord();
      }
    },
    parentNodeId: function (newVal, oldVal) {
      if (newVal != oldVal) {
        if (newVal != oldVal) {
          this.parentDocId = newVal;
        }
      }
    },
    immediate: true,
  },
  beforeDestroy() {
    // 移除附件点击事件监听
    const attachmentSpans = document.querySelectorAll(".attachment-download");
    attachmentSpans.forEach((span) => {
      span.addEventListener("click", this.handleAttachmentClick, true);
    });
  },
  methods: {
    /** 获取文档信息 */
    async getInfo() {
      this.resDetail();
      this.formData = {};
      const res = await docInfo(this.docInfoId);
      if (res.code === 200) {
        this.formData = res.data;
        this.favoriteObject = res.data;
        this.favoriteObject.rootObjectId = this.topicId;
        this.favoriteObject.type = "2";
        if (this.formData && this.formData.content) {
          const { outlines, htmlContentWithIds } = this.generateOutlines(this.formData.content);
          this.outlines = outlines;
          this.artical = htmlContentWithIds;
          // 处理dom相关标签及事件
          this.handleDomContent();
        }
      }
    },
    /** 获取文档关联信息 */
    statDocInfo() {
      this.resetDocStat();
      statDocumentRelate(this.docInfoId).then((res) => {
        if (res.code === 200) {
          this.docStat = res.data || {};
        }
      });
    },
    /** 重置文档统计信息 */
    resetDocStat() {
      this.docStat = {
        viewNum: 0,
        commentNum: 0,
        favoriteNum: 0,
        isFavorite: false,
      };
    },
    /** 内容大纲处理 */
    generateOutlines(htmlContent) {
      // 补全 HTML 结构以便解析
      const wrappedHtml = `<!DOCTYPE html><html><body>${htmlContent}</body></html>`;
      const parser = new DOMParser();
      const doc = parser.parseFromString(wrappedHtml, "text/html");
      const headers = ["h1", "h2", "h3", "h4", "h5", "h6"];
      let outlines = [];
      let indexCounter = 0;
      let hierarchyStack = [];
      const getLevelNumber = (tag) => parseInt(tag.replace("h", ""), 10);
      Array.from(doc.body.children).forEach((el) => {
        if (!headers.includes(el.tagName.toLowerCase())) return; // 非标题跳过
        const tag = el.tagName.toLowerCase();
        const id = `section-${indexCounter++}`;
        el.setAttribute("id", id);
        const currentLevel = getLevelNumber(tag);
        const node = {
          level: tag,
          text: el.textContent.trim(),
          id: id,
          children: [],
        };
        while (hierarchyStack.length > 0) {
          const lastNode = hierarchyStack[hierarchyStack.length - 1];
          const lastLevel = getLevelNumber(lastNode.level);
          if (lastLevel < currentLevel) {
            lastNode.children.push(node);
            break;
          } else {
            hierarchyStack.pop();
          }
        }
        if (hierarchyStack.length === 0) {
          outlines.push(node);
        }
        hierarchyStack.push(node);
      });
      const serializer = new XMLSerializer();
      const updatedHtml = Array.from(doc.body.children)
        .map((child) => serializer.serializeToString(child))
        .join("");
      return { outlines, htmlContentWithIds: updatedHtml };
    },
    /** 处理大纲点击事件 */
    handleNavigate(id) {
      this.activeOutlineId = id;
      this.scrollToSection(id);
    },
    /** 滚动定位 */
    scrollToSection(id) {
      const container = document.querySelector(".html-content");
      const element = document.getElementById(id);
      if (container && element) {
        const offsetTop = element.offsetTop;
        container.scrollTo({
          top: offsetTop,
          behavior: "smooth",
        });
      }
    },
    /** 更新新增状态 */
    handleAddDocFlag() {
      this.isAddDoc = true;
      this.$nextTick(() => {
        this.resEditor();
      });
    },
    /** 取消 */
    handleCancel() {
      this.$emit("cancel");
      this.getInfo();
      this.resDetail();
    },
    /** 更新节点名称 */
    updateNodeTitle(title) {
      this.$emit("update-node", title);
    },
    /** 更新标题（节点树重命名时，同步更新详情的title） */
    updateTitle(title) {
      this.$set(this.formData, "name", title);
    },
    /** 文档保存 */
    async submitDoc() {
      let docData = this.$refs.editor.getDocData();
      if (!docData.name && !docData.content) {
        this.$message.error("请先完善文档再提交！");
        return;
      }
      let data = {
        name: docData.name,
        topicId: this.topicId,
        type: "1",
        content: docData.content,
        parentId: this.parentDocId,
      };
      let flag = false;
      if (this.isAddDoc) {
        const res = await addDoc(data);
        this.docInfoId = res.data;
        flag = res.code == 200;
        this.$emit("add-success", res.data);
      } else {
        data.id = this.docInfoId;
        const res = await editDoc(data);
        flag = res.code == 200;
      }
      if (flag) {
        this.getInfo();
        this.resDetail();
      }
    },
    /** 编辑文档 */
    async editDocInfo(id) {
      this.docInfoId = id;
      await this.getInfo();
      this.resEditor();
      this.$nextTick(() => {
        this.$refs.editor.initData(this.formData);
      });
    },
    // 重置编辑
    resEditor() {
      this.openEditor = true;
      this.showEditBtn = true;
      this.showDetaile = false;
      this.activeOutlineId = null;
    },
    // 重置详情
    resDetail() {
      this.showDetaile = true;
      this.showEditBtn = false;
      this.isAddDoc = false;
      this.openEditor = false;
      this.activeOutlineId = null;
    },
    /** 收藏文档 */
    favoriteDoc() {
      if (!this.docInfoId) {
        this.$message.warning("收藏文档为空，请选择文档！");
        return;
      }
      this.favoriteOpen = true;
    },
    // 关闭收藏弹框
    closeFavoriteDialog() {
      this.favoriteOpen = false;
      this.favoriteObject = {};
    },
    // 收藏成功
    successFavoriteForm() {
      this.favoriteOpen = false;
      this.favoriteObject = {};
      this.$set(this.docStat, "favoriteNum", this.docStat.favoriteNum + 1);
      this.$set(this.docStat, "isFavorite", true);
    },
    /** 取消收藏 */
    cancelFavoriteDoc() {
      cancelFavorite(this.docInfoId).then((res) => {
        if (res.code == 200) {
          let favoriteNum = this.docStat.favoriteNum == 0 ? 0 : this.docStat.favoriteNum - 1;
          this.$set(this.docStat, "favoriteNum", favoriteNum);
          this.$set(this.docStat, "isFavorite", false);
        }
      });
    },
    /** 打开评论 */
    goComment() {
      this.openComment = true;
    },
    /** 关闭评论 */
    closeComment() {
      this.openComment = false;
    },
    /** 提交评论 */
    submitComment() {
      this.$set(this.docStat, "commentNum", this.docStat.commentNum + 1);
    },
    /** 新增浏览记录 */
    addViewRecord() {
      addView({ docId: this.docInfoId });
    },
    /** 处理dom相关标签及事件 */
    handleDomContent() {
      this.$nextTick(() => {
        // 注册附件下载事件监听
        this.registerAttachmentClickListener();
        // 为文档内容区所有 img 标签 src 动态拼接 token
        this.addImgSrcWithToken();
        // 为文档内容区所有 video source 标签 src 动态拼接 token
        this.addVideoSrcWithToken();
      });
    },
    /** 注册附件下载事件监听 */
    registerAttachmentClickListener() {
      const attachmentSpans = document.querySelectorAll(".attachment-download");
      attachmentSpans.forEach((span) => {
        span.addEventListener("click", this.handleAttachmentClick, true);
      });
    },
    /** 事件监听：附件下载 */
    handleAttachmentClick(e) {
      const target = e.target;
      if (target.classList) {
        const link = target.getAttribute("data-link");
        if (this.token) {
          const url = this.getAuthorization(link);
          window.open(url, "_blank");
        } else {
          window.open(link, "_blank");
        }
      }
    },
    /** 为文档内容区所有 img 标签 src 动态拼接 token */
    addImgSrcWithToken() {
      const imgs = document.querySelectorAll(".html-content img");
      imgs.forEach((img) => {
        img.src = this.getAuthorization(img.src);
      });
    },
    /** 为文档内容区所有 video source 标签 src 动态拼接 token */
    addVideoSrcWithToken() {
      const videos = document.querySelectorAll(".html-content video source");
      videos.forEach((video) => {
        video.src = this.getAuthorization(video.src);
      });
    },
    /** 获取授权信息 */
    getAuthorization(src) {
      if (!src || src.includes("data:image/") || src.includes("data:video/") || src.includes("Authorization=Bearer")) {
        return src;
      }
      src += (src.includes("?") ? "&" : "?") + "Authorization=Bearer " + this.token;
      return src;
    },
    /** 滚动事件 */
    handleScroll(e) {
      const container = e.target;
      const scrollTop = container.scrollTop;
      this.showBackToTop = scrollTop > 300;
    },
    /** 回到顶部 */
    backToTop() {
      const container = this.$refs.scrollContainer;
      container.scrollTop = 0;
      this.showTooltip = false;

      const outlineContainer = this.$refs.outlineListContainer;
      outlineContainer.scrollTop = 0;
      this.activeOutlineId = null;
    },
  },
};
</script>

<style scoped lang="scss">
.right-detail-header {
  height: 90px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  background: url("../../../../../assets/images/kbs-doc-header.jpg");
  background-position: top;
  background-size: cover;
  background-repeat: no-repeat;

  .title {
    text-align: center;
    font-size: 24px;
    color: #303133;
    font-weight: bold;
    display: block;
    margin: 10px 0;
  }
  .footer-info {
    display: inline-flex;
    align-items: center;
    gap: 15px;
    font-size: 12px;
    color: #666;
    margin-left: 0;
    span {
      margin-right: 15px;
    }
  }

  .foot-btn {
    color: #666;
    &:hover {
      color: #409eff;
    }
  }
}

.right-edit-header {
  height: 50px;
  padding: 10px 0;
  border-bottom: 1px solid #f1f1f1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.content-wrapper {
  padding: 0 12px;
}

.content-main {
  margin-bottom: 20px;

  .content-body {
    min-height: 500px;
  }
  .outline-title {
    display: flex;
    align-items: center;
    height: 40px;
    color: #606266;
    font-weight: bold;
    font-size: 14px;
    padding-left: 2px;
  }

  .out-line {
    // background: url("../../../../../assets/images/outline-background.png");
    // background-position: -50px top;
    // background-size: cover;
    // background-repeat: no-repeat;
    box-shadow: 4px 0 12px 0 rgba(0, 0, 0, 0.1);
  }
  .outline-list {
    height: calc(88vh - 140px);
    overflow-y: auto;
    overflow-x: hidden;
    padding-bottom: 12px;
  }

  .article-section {
    background-color: #fff;
    text-align: left;

    .html-content {
      height: calc(88vh - 100px);
      background: url("../../../../../assets/images/kbs-background.jpg");
      background-position: 0 top;
      background-size: contain;
      background-repeat: no-repeat;
      box-shadow: 4px 0 12px 0 rgba(0, 0, 0, 0.1);
      font-size: 14px;
      overflow-y: auto;
      overflow-x: hidden;
      line-height: 1.5;
      padding-left: 24px;
      padding-right: 24px;
      letter-spacing: 0.5px;
    }
  }
}

.editor-section {
  border-radius: 4px;
  background-color: #fff;
}

.back-to-top {
  position: absolute;
  bottom: 60px;
  right: 20px;
  width: 30px;
  height: 30px;
  background-color: #409eff;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: opacity 0.3s ease;
  z-index: 999;
  font-size: 20px;

  &:hover {
    background-color: #66b1ff;
  }
}
.tooltip-text {
  position: absolute;
  top: -35px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  white-space: nowrap;
  pointer-events: none;
}

// 滚动条样式
::-webkit-scrollbar {
  width: 3px;
  height: 3px;
}
::-webkit-scrollbar-track {
  background-color: #eee;
}
::-webkit-scrollbar-thumb {
  background-color: #ccc;
  border-radius: 3px;
}
::-webkit-scrollbar-thumb:hover {
  background-color: #aaa;
}
</style>