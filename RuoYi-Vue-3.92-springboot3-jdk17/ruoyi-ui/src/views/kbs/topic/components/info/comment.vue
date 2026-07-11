<template>
  <!-- 文章评论 -->
  <div class="comment-container">
    <el-drawer :title="'评论（' + commentNum+')'" :before-close="close" :visible.sync="open" direction="rtl" ref="drawer">
      <el-divider />
      <!-- 根节点的评论输入框 -->
      <div class="comment-input-section">
        <el-form label-position="right" label-width="40px">
          <el-form-item label=" ">
            <template slot="label">
              <el-avatar :size="24" :src="avatar(user.avatar)" />
            </template>
            <comment-input v-model="commentContent" @submit="submitRootCmt" />
          </el-form-item>
        </el-form>
      </div>

      <!-- 评论展示区域 -->
      <div class="comment-list-section" @scroll="handleScroll">
        <div v-for="(comment, index) in commentList" :key="index" class="comment-item">
          <div class="comment-header">
            <div class="header-left">
              <el-avatar :size="24" :src="avatar(comment.userAvatar)" />
              <span class="comment-username">{{ comment.createBy }}</span>
              <span class="comment-time">{{ comment.createTime }}</span>
            </div>
            <div v-if="!isShowCmt(comment.id)" class="comment-actions">
              <div>
                <el-button type="text" @click="replyComment(comment)" class="reply-btn">
                  <svg-icon icon-class="cmt" class="mr5" />回复
                </el-button>
              </div>
              <el-button type="text" @click="likeComment(comment)">
                <svg-icon :icon-class="thumbsUpSvg(comment.likeFlag)" />
                {{ comment.likeNum }}
              </el-button>
            </div>
          </div>
          <div class="comment-body">
            {{ comment.comment }}
            <div v-if="isShowCmt(comment.id) " class="reply-cmt">
              <comment-input
                v-model="commentContent"
                :showCancle="true"
                :placeholder="placeholder"
                @submit="handleSubmitCmt"
                @cancle-cmt="handleCancleCmt"
              />
            </div>
            <!-- 展开/收起回复按钮 -->
            <div v-if="!comment.showReplies && comment.children && comment.children.length > 0" class="toggle-replies">
              <el-button type="text" @click="toggleReplies(comment)">
                <span class="toggle-cmt">
                  <span class="toggle-cmt-line">——</span>
                  展开（{{comment.children.length}}）条回复
                  <i class="el-icon-arrow-down"></i>
                </span>
              </el-button>
            </div>

            <!-- 回复列表 -->
            <div v-if="comment.showReplies" class="replies-list">
              <div v-for="reply in comment.children" :key="reply.id" class="reply-item">
                <el-avatar :size="24" :src="avatar(reply.userAvatar)" />
                <div class="reply-content-wrap">
                  <div class="reply-username">
                    {{ reply.createBy}}
                    <span v-if="reply.rootParentId != reply.parentId">【回复】{{ reply.replier }}</span>
                  </div>
                  <div class="reply-content">{{ reply.comment }}</div>
                  <div>
                    <span class="reply-time">{{ reply.createTime }}</span>
                    <span class="reply-group-btn">
                      <el-button type="text" @click="replyComment(reply)" class="reply-btn">
                        <svg-icon icon-class="cmt" class="mr5" />回复
                      </el-button>
                      <el-button type="text" @click="likeComment(reply)">
                        <svg-icon :icon-class="thumbsUpSvg(reply.likeFlag)" />
                        {{ reply.likeNum }}
                      </el-button>
                    </span>
                  </div>
                  <div v-if="isShowCmt(reply.id) " class="reply-cmt">
                    <comment-input
                      v-model="commentContent"
                      :showCancle="true"
                      :placeholder="placeholder"
                      @submit="handleSubmitCmt"
                      @cancle-cmt="handleCancleCmt"
                    />
                  </div>
                </div>
              </div>
              <div v-if="comment.showReplies" class="toggle-replies">
                <el-button type="text" @click="toggleReplies(comment)">
                  <span class="toggle-cmt">
                    <span class="toggle-cmt-line">——</span>收起回复
                    <i class="el-icon-arrow-up"></i>
                  </span>
                </el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 加载提示 -->
        <div v-if="loadingMore" class="loading-text">加载中...</div>
        <div v-else class="no-more">没有更多评论了</div>
      </div>
    </el-drawer>
  </div>
</template>
  
<script>
import CommentInput from "./comment-input.vue";
import { listComment, addComment } from "@/api/kbs/document/comment";
import { addLike, delLike } from "@/api/kbs/document/like";
import { parseTime } from "@/utils/ruoyi";
import { StrUtil } from "@/utils/StrUtil";

export default {
  name: "Comment",
  components: {
    CommentInput,
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      // 加载更多
      loadingMore: false,
      // 滚动数据
      hasMore: true,
      // 评论内容
      commentContent: "",
      // 评论列表
      commentList: [],
      // 回复的评论
      replyCmt: null,
      // 提示语
      placeholder: null,
      // 评论查询参数
      queryParam: {
        pageNum: 1,
        pageSize: 10,
      },
      user: this.$store.state.user.userInfo,
      baseApi: process.env.VUE_APP_BASE_API,
    };
  },
  props: {
    docId: null,
    commentNum: {
      type: Number,
      default: 0,
    },
    open: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    isShowCmt() {
      return (id) => {
        if (!this.replyCmt) return false;
        return this.replyCmt.id === id;
      };
    },
    thumbsUpSvg() {
      return (val) => {
        if (!val) return "thumbsup";
        return "thumbsUped";
      };
    },
  },
  watch: {
    docId: function (newVal, oldVal) {
      if (!newVal) return;
      if (newVal != oldVal) {
        this.docInfoId = newVal;
        this.showOutline = true;
        this.getList();
      }
    },
    immediate: true,
  },
  methods: {
    /** 头像处理 */
    avatar(val) {
      if (StrUtil.isBlank(val)) return;
      return this.baseApi + val;
    },
    /** 获取评论列表 */
    async getList(loadMore = false) {
      if (this.loading || (loadMore && !this.hasMore)) return;
      if (loadMore) this.loadingMore = true;
      this.loading = true;
      this.commentList = [];
      this.queryParam.docId = this.docId;
      const res = await listComment(this.queryParam);
      if (res.code == 200) {
        const newData = res.rows || [];
        this.commentList = loadMore ? [...this.commentList, ...newData] : newData;
        this.hasMore = newData.length >= this.queryParam.pageSize;
      }
      this.loading = false;
      this.loadingMore = false;
    },
    /** 根节点的评论 */
    async submitRootCmt(content) {
      if (content.trim()) {
        let data = {
          docId: this.docId,
          comment: content.trim(),
        };
        const res = await addComment(data);
        if (res.code === 200) {
          const newReply = {
            ...data,
            id: res.data,
            createBy: this.user.userName,
            createTime: parseTime(new Date(), "{y}-{m}-{d} {h}:{i}:{s}"),
            likeNum: null,
            likeFlag: false,
            userAvatar: this.user.avatar,
          };
          this.commentList.unshift(newReply);
          this.commentContent = "";
          this.$message.success("评论成功");
          this.$emit("submit-comment");
        }
      }
    },
    /** 提交评论 */
    async handleSubmitCmt(content) {
      if (content.trim()) {
        let data = {
          docId: this.docId,
          comment: content.trim(),
          rootParentId: this.replyCmt.rootParentId || this.replyCmt.id,
          parentId: this.replyCmt.id,
          replierId: this.replyCmt.createId,
          replier: this.replyCmt.createBy,
        };
        const res = await addComment(data);
        if (res.code === 200) {
          const newReply = {
            ...data,
            id: res.data,
            createBy: this.user.userName,
            createTime: parseTime(new Date(), "{y}-{m}-{d} {h}:{i}:{s}"),
            likeNum: null,
            likeFlag: false,
            userAvatar: this.user.avatar,
          };
          // 找到父评论
          const parentComment = this.findCommentById(this.replyCmt.rootParentId || this.replyCmt.id);

          if (parentComment) {
            // 添加新回复
            if (!parentComment.children) {
              this.$set(parentComment, "children", []);
            }
            parentComment.children.unshift(newReply);
            this.commentContent = "";
            this.replyCmt = null;
            this.$message.success("评论成功");
            this.$emit("submit-comment");
          }
        }
      }
    },
    /** 查找评论 */
    findCommentById(id) {
      // 查找一级评论
      const found = this.commentList.find((c) => c.id === id);
      if (found) return found;
      // 检查所有一级评论下的 children
      for (const comment of this.commentList) {
        if (comment.children && comment.children.length > 0) {
          const child = comment.children.find((c) => c.id === id);
          if (child) return child;
        }
      }
      return null;
    },
    /** 取消评论 */
    handleCancleCmt() {
      this.replyCmt = null;
      this.placeholder = null;
    },
    /** 回复评论 */
    replyComment(cmt) {
      this.placeholder = "回复：【" + cmt.createBy + "】";
      this.replyCmt = {
        id: cmt.id,
        rootParentId: cmt.rootParentId || cmt.id,
        createId: cmt.createId || this.user.userId,
        createBy: cmt.createBy,
      };
    },
    /** 点赞、取消点赞 */
    likeComment(cmt) {
      // 取消点赞
      if (cmt.likeFlag) {
        let num = cmt.likeNum ? (cmt.likeNum - 1 <= 0 ? null : cmt.likeNum - 1) : null;
        this.$set(cmt, "likeNum", num);
        this.$set(cmt, "likeFlag", false);
        delLike(cmt.id);
      } else {
        // 点赞
        let data = {
          commentId: cmt.id,
        };
        this.$set(cmt, "likeNum", cmt.likeNum ? cmt.likeNum + 1 : 1);
        this.$set(cmt, "likeFlag", true);
        addLike(data);
      }
    },
    /** 关闭评论窗口 */
    close() {
      this.$emit("close-comment");
    },
    /** 滚动 */
    handleScroll(e) {
      const element = e.target;
      if (element.scrollHeight - element.scrollTop <= element.clientHeight + 20 && !this.loading && !this.noMoreData) {
        this.loadMoreComments();
      }
    },
    /** 加载更多 */
    async loadMoreComments() {
      this.queryParam.pageNum += 1;
      await this.getList(true);
    },
    /** 展开回复 */
    toggleReplies(comment) {
      let show = !comment.showReplies ? true : false;
      this.$set(comment, "showReplies", show);
    },
  },
};
</script>
  
  <style scoped>
.comment-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.comment-input-section {
  position: relative;
  padding: 0 15px;
}

.button-group {
  display: flex;
  padding-right: 5px;
}
.button-group .el-button {
  padding: 5px 10px;
  font-size: 12px;
}

.comment-list-section {
  flex: 1;
  padding: 20px;
  max-height: calc(88vh - 188px);
  overflow-y: auto;
}

.comment-item {
  font-size: 14px;
  margin-bottom: 24px;
  line-height: 24px;
}

.comment-time {
  margin-left: 10px;
  font-size: 12px;
}

.comment-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 5px;
  height: 30px;
}
.header-left {
  display: flex;
  align-items: center;
}
.comment-header .el-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  flex-shrink: 0;
  margin-right: 6px;
}

.header-left .comment-username,
.header-left .comment-time {
  color: #909399;
  display: inline-block;
  line-height: 30px;
  white-space: nowrap;
}

.comment-body {
  color: #606266;
  padding-left: 32px;
}

.comment-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.comment-actions .el-button:first-child {
  margin-left: auto;
}

.reply-btn {
  color: #606266;
  font-size: 13px;
}

.loading-text,
.no-more {
  font-size: 12px;
  text-align: center;
  color: #999;
  margin-top: 40px;
}
.reply-cmt {
  margin-top: 10px;
  margin-bottom: 10px;
}
.toggle-replies {
  font-size: 12px;
  color: #606266;
}

.reply-item {
  display: flex;
  padding: 5px;
  background-color: #f9f9f9;
  border-radius: 4px;
  margin-top: 10px;
  font-size: 13px;
  line-height: 24px;
}

.reply-item .el-avatar {
  margin-right: 8px;
  flex-shrink: 0;
}
.reply-content-wrap {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.reply-username {
  color: #909399;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 4px;
  max-width: 100%;
  margin-right: 8px;
}

.reply-content {
  color: #606266;
  word-break: break-all;
  white-space: pre-line;
}

.reply-time {
  color: #c0c4cc;
}

.reply-group-btn {
  color: #909399;
  margin-left: 30px;
}

.toggle-cmt {
  font-size: 12px;
  color: #909399;
}

.toggle-cmt-line {
  margin-right: 5px;
  color: #c0c4cc;
}
::v-deep .el-drawer__header {
  margin-bottom: 20px;
}
</style>