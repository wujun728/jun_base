<template>
  <!-- 欢迎组件 -->
  <div class="welcome">
    <div class="welcome-flex">
      <div class="avatar">
        <!-- 这里可以切换系统logo或者是当前用户头像 -->
        <!-- <img :src="logo" class="logo" /> -->
        <image-preview :src="avatar" />
      </div>
      <div class="welcome-text">
        <div class="welcome-title">{{ nickName }}，{{ greeting }}</div>
        <div class="welcome-time">
          <div class="scroll-text">{{ welcomeMessage }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import logoImg from "@/assets/logo/logo.png";

export default {
  name: "WelcomeModule",
  data() {
    return {
      // 系统logo
      logo: logoImg,
      // 昵称
      nickName: (this.$store.state.user && this.$store.state.user.userInfo.nickName) || "",
      // 用户头像
      avatar: (this.$store.state.user && this.$store.state.user.userInfo.avatar) || "",
    };
  },
  computed: {
    /** 计算问候语 */
    greeting() {
      const hour = new Date().getHours();
      if (hour < 9) {
        return "早上好！";
      } else if (hour < 12) {
        return "上午好！";
      } else if (hour < 18) {
        return "下午好！";
      } else {
        return "晚上好！";
      }
    },
    /** 计算欢迎语 */
    welcomeMessage() {
      const hour = new Date().getHours();
      if (hour < 6) {
        return "夜深了，注意休息哦~";
      } else if (hour < 12) {
        return "新的一天，冲鸭 🚀";
      } else if (hour < 18) {
        return "继续努力，加油 💪";
      } else {
        return "辛苦一天了，记得保存进度哦 🌙";
      }
    },
  },
  mounted() {},
  methods: {},
};
</script>

<style scoped lang="scss">
.welcome {
  display: flex;
  height: calc(12vh - 10px);
  align-items: center;
  margin-bottom: 10px;
  border-radius: 4px;
  border: 1px solid #f5f5f5;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  background: url("../../../../assets/images/welcome.png");
  background-size: auto;
  background-position: left top;
  background-repeat: no-repeat;
}
.welcome-flex {
  display: flex;
  align-items: center;
  padding: 10px;
  justify-content: flex-start;
  width: 100%;
}
.avatar {
  flex-shrink: 0;
  margin-right: 16px;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  background: #fff;
  border: 2px solid #dbe3ef;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: transform 0.3s ease;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease;
  }

  &:hover img {
    transform: scale(1.1);
  }
}
.logo {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.welcome-text {
  flex: 1;
  color: #333;
}

.welcome-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.welcome-time {
  font-size: 13px;
  color: #fff;
  overflow: hidden;
  white-space: nowrap;
  position: relative;
  height: 24px;
}

.scroll-text {
  display: inline-block;
  padding-left: 100%;
  animation: scrollText 25s linear infinite;
}

@keyframes scrollText {
  0% {
    transform: translateX(-100%);
  }
  50% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(-100%);
  }
}
.current-date {
  font-size: 12px;
  color: #999;
  margin-left: 6px;
}
</style>

