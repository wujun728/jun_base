<template>
  <div class="comment-input-wrapper" ref="container">
    <el-input type="textarea" v-model="inputContent" :rows="4" :placeholder="placeholder"></el-input>
    <div class="input-buttons">
      <span class="char-count">{{ inputContent.length }}/1000</span>
      <div class="button-group">
        <el-button type="text" size="mini" @click="toggleEmojiPicker">😊</el-button>
        <el-button v-if="showCancle" type="info" plain size="mini" @click="handleCancle">取 消</el-button>
        <el-button type="primary" size="mini" @click="handleSubmit">提 交</el-button>
      </div>
    </div>
    <div v-show="showEmojiPicker" class="emoji-picker" ref="emojiPicker">
      <div class="emoji-tabs">
        <span
          v-for="(emojis, category) in emojiCategories"
          :key="category"
          :class="['emoji-tab', { active: currentCategory === category }]"
          @click="currentCategory = category"
        >{{ getCategoryLabel(category) }}</span>
        <span class="emoji-tab close-btn" @click="showEmojiPicker = false">
          <i class="el-icon-close"></i>
        </span>
      </div>
      <div class="emoji-content">
        <span v-for="(emoji, index) in emojiCategories[currentCategory]" :key="index" @click="insertEmoji(emoji)">{{ emoji }}</span>
      </div>
    </div>
  </div>
</template>
  
<script>
export default {
  name: "CommentInput",
  data() {
    return {
      // 评论内容
      inputContent: "",
      // 显示表情面板控制
      showEmojiPicker: false,
      // 当前分类
      currentCategory: "smileys",
      // 表情定义
      emojiCategories: {
        smileys: [
          "😀",
          "😁",
          "😂",
          "🤣",
          "😃",
          "😄",
          "😅",
          "😆",
          "😉",
          "😊",
          "😎",
          "😍",
          "😘",
          "😗",
          "😙",
          "😚",
          "🙂",
          "🤗",
          "🤔",
          "😐",
          "😑",
          "😶",
          "🙄",
          "😏",
          "😣",
          "😥",
          "😮",
          "🤐",
          "😴",
          "🤤",
          "😪",
          "😵",
          "😲",
          "👿",
          "😈",
          "👻",
          "💀",
        ],
        animals: [
          "🐶",
          "🐱",
          "🐭",
          "🐹",
          "🐰",
          "🦊",
          "🐻",
          "🐼",
          "🐨",
          "🐯",
          "🦁",
          "🐮",
          "🐷",
          "🐽",
          "🐸",
          "🐵",
          "🙈",
          "🙉",
          "🙊",
          "🐒",
          "🐔",
          "🐧",
          "🐦",
          "🐤",
          "🐣",
          "🐥",
          "🦆",
          "🦅",
          "🦉",
          "🦇",
          "🐺",
          "🦋",
          "🐛",
          "🐝",
        ],
        food: ["👋", "👌", "✌️", "🤞", "🤟", "🤘", "🤙", "👈", "👉", "👆", "👇", "☝️", "✋", "🤚", "🖐️", "🖖", "💪", "🙏", "🤝", "✍️"],
        symbols: [
          "❤️",
          "🧡",
          "💛",
          "💚",
          "💙",
          "💜",
          "🖤",
          "🤍",
          "💯",
          "💥",
          "🌟",
          "🍉",
          "🍒",
          "🍓",
          "🍎",
          "🍇",
          "🍊",
          "🍋",
          "🍍",
          "🍌",
          "🥑",
          "🍔",
          "🍕",
          "🍟",
        ],
      },
    };
  },
  props: {
    showCancle: false,
    placeholder: {
      type: String,
      default: "请填写评论内容...",
    },
  },
  methods: {
    /** 提交 */
    handleSubmit() {
      if (this.inputContent.trim()) {
        this.$emit("submit", this.inputContent);
        this.inputContent = "";
        this.showEmojiPicker = false;
      } else {
        this.$message.warning("请输入评论内容");
      }
    },
    /** 取消 */
    handleCancle() {
      this.$emit("cancle-cmt");
    },
    /** 打开表情面板 */
    toggleEmojiPicker() {
      this.showEmojiPicker = true;
      this.$nextTick(() => {
        const containerRect = this.$refs.container.getBoundingClientRect();
        const emojiPicker = this.$refs.emojiPicker;
        if (emojiPicker) {
          const spaceBelow = window.innerHeight - containerRect.bottom;
          const pickerHeight = 300;
          emojiPicker.style.width = `${containerRect.width}px`;
          // 判断下方是否够空间
          if (spaceBelow > pickerHeight) {
            // 向下展开
            emojiPicker.style.top = `${containerRect.height + 5}px`;
            emojiPicker.style.bottom = "auto";
          } else {
            // 向上展开
            emojiPicker.style.bottom = `${containerRect.height + 5}px`;
            emojiPicker.style.top = "auto";
          }
        }
      });
    },
    /** 关闭表情面板 */
    closeEmojiPicker() {
      this.showEmojiPicker = false;
    },
    /** 表情分类 */
    getCategoryLabel(category) {
      const labels = {
        smileys: "😊",
        animals: "🐶",
        food: "👌",
        symbols: "❤️",
      };
      return labels[category] || category;
    },
    /** 插入表情 */
    insertEmoji(emoji) {
      this.inputContent += emoji;
    },
  },
};
</script>
  
<style scoped>
.comment-input-wrapper {
  position: relative;
  width: 100%;
}

.input-buttons {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #f1f1f1;
  height: 40px;
}

.char-count {
  font-size: 12px;
  color: #999;
  padding-left: 5px;
}

.button-group {
  display: flex;
  gap: 6px;
  padding-right: 5px;
}
.button-group .el-button {
  padding: 5px 10px;
  font-size: 12px;
}

.emoji-picker {
  position: absolute;
  max-height: 300px;
  overflow-y: auto;
  z-index: 1000;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding-bottom: 10px;
  width: 100%;
}

.emoji-tabs {
  display: flex;
  border-bottom: 1px solid #eee;
  background-color: #f9f9f9;
}

.emoji-tab {
  cursor: pointer;
  padding: 5px 10px;
  font-size: 16px;
  transition: all 0.2s ease;
}

.emoji-content {
  display: flex;
  flex-wrap: wrap;
  padding: 10px;
  gap: 10px;
}

.emoji-content span {
  font-size: 20px;
  padding: 5px;
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

.emoji-content span:hover {
  background-color: #f0f0f0;
  cursor: pointer;
}

.emoji-tabs {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  position: relative;
  border-bottom: 1px solid #eee;
  background-color: #f9f9f9;
  padding: 5px 10px;
}

.close-btn {
  position: absolute;
  right: 10px;
  cursor: pointer;
  font-size: 20px;
}

::v-deep .el-button {
  margin-left: 0;
}
</style>