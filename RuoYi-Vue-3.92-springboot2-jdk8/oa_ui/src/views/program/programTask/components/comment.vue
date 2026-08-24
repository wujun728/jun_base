<template>
  <div>
    <div v-clickoutside="hideReplyBtn" @click="inputFocus" class="my-reply">
      <el-avatar class="header-img" :size="36" :src="myHeader"></el-avatar>
      <div class="reply-info" v-show="!showReply">
        <div
          tabindex="0"
          contenteditable="true"
          id="replyInput"
          spellcheck="false"
          placeholder="请输入您的评论"
          class="reply-input pointer"
          @click="handleComment()"
          @focus="showReplyBtn"
          @input="onDivInput($event)"
        ></div>
      </div>
      <div class="reply-ueditor" v-if="showReply">
        <ueditor-form
          is="ueditorFrom"
          :border="true"
          ref="ueditorFrom"
          :height="`120px`"
          :type="`simple`"
          :headers="true"
          :content="formData.describe"
          :placeholder="'发表您的看法...'"
          @input="ueditorEdit"
        />
      </div>
      <div class="reply-btn-box" v-show="showReply">
        <el-button class="reply-btn" @click="cancelComment">取消</el-button>
        <el-button size="medium" @click="sendComment" type="primary">评论</el-button>
      </div>
    </div>
    <div v-if="comments.length" class="reply-father">
      <div v-for="(item, i) in comments" :key="i" class="author-title">
        <el-avatar class="header-img" :size="36" :src="item.member.avatar"></el-avatar>
        <div class="author-info">
          <span class="author-name">{{ item.member.name }}</span>
          <span class="author-time">{{ item.updated_at }}</span>
        </div>
        <div class="icon-btn icon-btn-header">
          <div v-if="myId !== item.member.id" class="pointer lh-center" @click="showReplyEvt(item, 1)">
            <i class="iconfont iconcebianlan-luntanzhongxin"></i>
            <span>回复</span>
          </div>
          <div v-else class="flex">
            <div class="edit-name pointer lh-center" @click="eidtReply(item)">
              <i class="iconfont iconbianji"></i>
              <span>编辑</span>
            </div>
            <div class="pointer lh-center" @click="deleteReply(item)">
              <i class="iconfont iconshanchu1"></i>
              <span>删除</span>
            </div>
          </div>
        </div>
        <div class="talk-box">
          <p class="content">
            <!-- <ueditor-form
              is="ueditorFrom"
              ref="ueditorFrom"
              :readOnly="true"
              :headers="false"
              :height="`120px`"
              :content="item.describe"
            /> -->
            <span class="reply" v-html="item.describe" @click="previewPicture($event)"></span>
          </p>
        </div>
        <div class="reply-box">
          <div v-for="(reply, j) in item.children" :key="j" class="author-item">
            <el-avatar class="header-img" :size="26" :src="reply.member.avatar"></el-avatar>
            <div class="author-info">
              <span class="author-name">{{ reply.member.name }}</span>
              <span class="author-time">{{ reply.updated_at }}</span>
            </div>
            <div class="icon-btn">
              <div v-if="myId !== reply.member.id" class="pointer lh-center" @click="showReplyEvt(reply, 2)">
                <i class="iconfont iconcebianlan-luntanzhongxin"></i>
                <span>回复</span>
              </div>
              <div v-else class="flex">
                <div class="edit-name pointer lh-center" @click="eidtReply(reply)">
                  <i class="iconfont iconbianji"></i>
                  <span>编辑</span>
                </div>
                <div class="pointer lh-center" @click="deleteReply(reply)">
                  <i class="iconfont iconshanchu1"></i>
                  <span>删除</span>
                </div>
              </div>
            </div>
            <div class="talk-box">
              <p>
                <!-- <span v-if="reply.reply_member.name !== item.member.name" class="reply-name">回复 {{ reply.reply_member.name }}:</span> -->
                <span class="reply" v-html="reply.describe" @click="previewPicture($event)"></span>
              </p>
            </div>
            <div class="reply-box"></div>
          </div>
        </div>
        <div v-show="_inputShow(i)" class="my-reply my-comment-reply">
          <el-avatar class="header-img" :size="40" :src="myHeader"></el-avatar>
          <div class="reply-info">
            <div
              tabindex="0"
              contenteditable="true"
              spellcheck="false"
              placeholder="输入评论..."
              @input="onDivInput($event)"
              class="reply-input reply-comment-input"
            ></div>
          </div>
          <div class="reply-btn-box">
            <el-button class="reply-btn" size="medium" @click="sendCommentReply(i, j)" type="primary"
              >发表评论</el-button
            >
          </div>
        </div>
      </div>
    </div>
    <div v-if="!comments.length" class="default">
      <default-page :index="17" :min-height="300"></default-page>
    </div>
    <image-viewer ref="imageViewer" :src-list="srcList"></image-viewer>
  </div>
</template>
<script>
import { getTaskCommentApi, saveTaskCommentApi, putTaskCommentApi, deleteTaskCommentApi } from '@/api/program'
import imageViewer from '@/components/common/imageViewer'

const clickoutside = {
  // 初始化指令
  bind(el, binding, vnode) {
    function documentHandler(e) {
      // 这里判断点击的元素是否是本身，是本身，则返回
      if (el.contains(e.target)) {
        return false
      }
      // 判断指令中是否绑定了函数
      if (binding.expression) {
        // 如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
        binding.value(e)
      }
    }
    // 给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
    el.vueClickOutside = documentHandler
    document.addEventListener('click', documentHandler)
  },
  update() {},
  unbind(el, binding) {
    // 解除事件监听
    document.removeEventListener('click', el.vueClickOutside)
    delete el.vueClickOutside
  }
}
export default {
  name: 'ArticleComment',
  components: {
    ueditorFrom: () => import('@/components/form-common/oa-wangeditor'),
    defaultPage: () => import('@/components/common/defaultPage'),
    imageViewer
  },
  props: {
    pid: {
      type: Number,
      default: 0
    },
    taskId: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      btnShow: false,
      showReply: false,
      index: '0',
      replyComment: '',
      myName: '',
      isEdit: false,
      myHeader: JSON.parse(localStorage.getItem('userInfo')).avatar,
      myId: JSON.parse(localStorage.getItem('userInfo')).id,
      to: '',
      toId: -1,
      comments: [],
      formData: {
        describe: ''
      },
      replyUid: 0,
      replyPid: 0,
      replyId: 0,
      srcList: []
    }
  },
  watch: {
    taskId: function (newValue, oldValue) {
      if (newValue !== oldValue) {
        this.getTaskComment()
      }
    },
    'formData.describe': function (newValue, oldValue) {
      if (newValue !== oldValue) {
        this.formData.describe = newValue
      }
    }
  },
  directives: { clickoutside },
  created() {
    this.getTaskComment()
  },
  methods: {
    // 获取评论
    getTaskComment() {
      let data = {
        task_id: this.taskId
      }
      getTaskCommentApi(data).then((res) => {
        this.comments = res.data.list
        this.$emit('gettotalCount', res.data.total_count)
      })
    },
    inputFocus() {
      var replyInput = document.getElementById('replyInput')
      replyInput.style.padding = '8px 8px'
    },
    showReplyBtn() {
      this.btnShow = true
    },
    hideReplyBtn() {
      this.btnShow = false
      replyInput.style.padding = '10px'
      replyInput.style.border = 'none'
    },
    showReplyInput(i, name, id) {
      this.comments[this.index].inputShow = false
      this.index = i
      this.comments[i].inputShow = true
      this.to = name
      this.toId = id
    },
    _inputShow(i) {
      return this.comments[i].inputShow
    },
    handleComment() {
      this.showReply = true
      this.replyUid = ''
      this.replyPid = ''
    },
    showReplyEvt(item, type) {
      this.isEdit = false
      this.showReply = true
      this.replyPid = type == 1 ? item.id : item.pid
      this.replyUid = item.uid
      if (type === 2) {
        let htmlContent = `<span style="font-size: 13px; color: #909399">回复</span><span style="font-size: 13px;"> ${item.member.name}：</span>${this.formData.describe}`
        this.formData.describe = htmlContent
      }
    },
    // 发表评论
    sendComment() {
      if (!this.formData.describe || this.formData.describe == '<p><br></p>') {
        this.$message({
          showClose: true,
          type: 'warning',
          message: '评论不能为空'
        })
      } else {
        let data = {
          pid: this.replyPid,
          task_id: this.taskId,
          reply_uid: this.replyUid,
          describe: this.formData.describe
        }
        if (this.isEdit) {
          putTaskCommentApi(this.replyId, data).then((res) => {
            this.showReply = false
            this.isEdit = false
            this.getTaskComment()
            this.formData.describe = ''
          })
        } else {
          saveTaskCommentApi(data).then((res) => {
            this.showReply = false
            this.isEdit = false
            this.getTaskComment()
            this.formData.describe = ''
          })
        }
      }
    },
    // 取消评论
    cancelComment() {
      this.isEdit = false
      this.showReply = false
      this.formData.describe = ''
    },
    // 编辑评论
    eidtReply(item) {
      this.showReply = true
      this.isEdit = true
      this.replyId = item.id
      this.formData.describe = item.describe
    },
    // 删除评论
    deleteReply(item) {
      this.$modalSure('你确定要删除此评论吗').then(() => {
        deleteTaskCommentApi(item.id).then((res) => {
          this.getTaskComment()
        })
      })
    },
    sendCommentReply(i, j) {
      if (!this.replyComment) {
        this.$message({
          showClose: true,
          type: 'warning',
          message: '评论不能为空'
        })
      }
    },
    onDivInput: function (e) {
      this.replyComment = e.target.innerHTML
    },
    ueditorEdit(e) {
      this.formData.describe = e
    },
    //预览图片
    previewPicture(e) {
      if (e.target.tagName == 'IMG') {
        this.srcList = [e.target.src]
        this.$refs.imageViewer.openImageViewer(e.target.src)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.lh-center {
  display: flex;
  align-items: center;
}
.my-reply {
  position: fixed;
  bottom: 0;
  padding: 10px;
  padding-right: 0;
  width: 859px;
  background-color: #fafafa;
  z-index: 10;

  .header-img {
    display: inline-block;
    vertical-align: top;
  }
  .reply-info {
    display: inline-block;
    margin-left: 5px;
    width: 90%;
    @media screen and (max-width: 1200px) {
      width: 80%;
    }
    .reply-input {
      min-height: 20px;
      line-height: 22px;
      padding: 10px 10px;
      color: #ccc;
      background-color: #fff;
      border-radius: 5px;
      &:empty:before {
        content: attr(placeholder);
      }
      &:focus:before {
        content: none;
      }
      &:focus {
        padding: 8px 8px;
        box-shadow: none;
        outline: none;
      }
    }
  }
  .reply-ueditor {
    width: calc(100% - 46px);
    display: inline-block;
  }
  .reply-btn-box {
    height: 25px;
    margin: 10px 0;
    .reply-btn {
      position: relative;
      float: left;
      margin-left: 40px;
    }
  }
}
.my-comment-reply {
  margin-left: 50px;
  .reply-input {
    width: flex;
  }
}
.reply-box .author-item:last-child {
  border: none;
}
.author-title {
  min-height: 100px;
  padding: 20px;

  .author-item {
    min-height: 100px;
    margin: 0 20px;
    padding: 20px 0;
    border-bottom: 1px dashed #eeeeee;
  }
  .header-img {
    display: inline-block;
    vertical-align: top;
  }
  .author-info {
    display: inline-block;
    margin-left: 5px;
    width: 60%;
    line-height: 20px;

    > span {
      cursor: pointer;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
    .author-name {
      display: inline-block;
      color: #303133;
    }
    .author-time {
      font-size: 12px;
      color: #909399;
      vertical-align: top;
    }
  }
  .icon-btn-header {
    margin-right: 20px;
  }
  .icon-btn {
    padding: 0 !important;
    float: right;
    font-size: 13px;
    color: #606266;
    display: flex;
    @media screen and (max-width: 1200px) {
      width: 20%;
      padding: 7px;
    }
    > span {
      cursor: pointer;
    }
    .iconfont {
      margin: 0 5px;
    }
    .edit-name {
      margin-right: 20px;
    }
  }
  .talk-box {
    margin: 0 35px;
    > p {
      margin: 0;
    }
    .content {
      margin-left: 11px;
    }
    .reply {
      font-size: 14px;
      color: #303133;
      display: inline-block;
      ::v-deep p {
        img {
          width: 148px;
        }
      }
    }
    .reply-name {
      font-size: 14px;
      color: #909399;
      display: inline-block;
    }
  }
  .reply-box {
    margin: 10px 0 0 50px;
    background-color: #f7fbff;
  }
}
::v-deep.el-avatar > img {
  width: 100% !important;
  height: 100% !important;
}
.reply-father {
  margin-bottom: 62px;
}
.default {
  padding: 20px 0 0 20px;
  color: #909399;
}
::v-deep.w-e-text-container {
  padding-top: 10px;
}
</style>
