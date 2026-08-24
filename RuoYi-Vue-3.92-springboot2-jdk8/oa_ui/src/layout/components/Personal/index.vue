<!-- 配置app二维码组件 -->
<template>
  <el-popover ref="popoverUser" placement="bottom" popper-class="user-popper" trigger="click">
    <div class="popover-user">
      <div class="drop-head">
        <div class="flex">
          <img
            :src="userInfo ? userInfo.avatar : '../../assets/images/f.png'"
            alt=""
            class="drop-avatar"
          />
          <div class="ml10 drop-right">
            <div class="name">
              {{ userInfo ? userInfo.name : '' }}
              <span v-if="userInfo.jobInfo">({{ userInfo.jobInfo.name || '--' }})</span>
            </div>
            <div class="frame">
              {{ frameName }}
            </div>
          </div>
        </div>
      </div>

      <div class="drop-item" @click="goMyMenu">
        <i class="iconfont icongerenzhongxin2"></i>
     
       个人信息
      </div>
      <div class="drop-item" @click="toSubscribe">
        <i class="iconfont icondingyuexiaoxi"></i>
        订阅消息
      </div>

      <div class="drop-item" @click="toResume">
        <i class="iconfont iconjianli"></i>
        个人简历
      </div>
      <!-- <div class="drop-item" @click="toCommunity" v-if="this.$store.state.settings.forumShow">
        <i class="iconfont icongudingwuzi"></i>
        知识社区
      </div> -->
      <div class="drop-item drop-body" @click="toForum" v-if="this.$store.state.settings.bbsShow">
        <i class="iconfont iconcebianlan-luntanzhongxin"></i>
        论坛中心
      </div>
      <!-- 退出登录 -->
      <div class="drop-item" @click="loginOut">
        <i class="iconfont icontuichudenglu"></i>
        退出登录
      </div>
    </div>
    <div v-if="userInfo" slot="reference" class="user-info">
      <img :src="userInfo.avatar || userInfo.avatar" alt="" class="img" />
      <span>{{ userInfo.account }}</span>
    </div>
    <!-- 个人信息 -->
    <personalCenter ref="personalCenter"></personalCenter>
  </el-popover>

</template>

<script>
import { mapGetters } from 'vuex'
import defaultSettings, { roterPre } from '@/settings'

export default {
  name: 'configapp',
  components: {
    personalCenter: () => import('./personalCenter'),
  },
  props: ['helpShow'],

  data() {
    return {
    }
  },
  computed: {
    ...mapGetters(['userInfo']),
    frameName() {
      if (this.userInfo.frames && this.userInfo.frames[0] && this.userInfo.frames[0].frame) {
        return this.userInfo.frames[0].frame.name
      }
      return '--';
    }
  },
  created() {

  },

  methods: {
    // 去论坛中心
    toForum() {
      window.open('https://www.crmeb.com/ask/thread/list/157', '_blank')
    },
    // 去订阅消息
    toSubscribe() {
      this.$refs.popoverUser.doClose()
      this.$router.push(`${roterPre}/user/news/subscribe`)
    },
    // 去个人简历
    toResume() {
      this.$refs.popoverUser.doClose()
      this.$router.push(`${roterPre}/user/resume`)
    },
    // 去知识社区
    toCommunity() {
      this.$refs.popoverUser.doClose()
      this.$router.push(`${roterPre}/user/forum/index`)
    },
    // 去个人中心
    goMyMenu() {
      this.closePopoverUser()
      this.$refs.personalCenter.openBox()
    },
    // 退出登录
    loginOut() {
      this.$modalSure('确定退出登录吗').then(async () => {
        await this.$store.dispatch('user/logout')
        this.$router.push(`${roterPre}/login?redirect=${this.$route.fullPath}`)
      })
    },
    // 关闭popper
    closePopoverUser() {
      this.$refs.popoverUser.doClose()
    }
  }
}
</script>

<style lang="scss" scoped>
.popover-user {
  z-index: 200;
  margin: -12px;
}
.user-info {
  // display: flex;
  // align-items: center;
  font-size: 14px;
  color: #fff;
  width: 34px;
  height: 34px;
  cursor: pointer;
  border-radius: 50%;
  overflow: hidden;
  border: 1px solid #ffffff;
  .img {
    display: flex;
    width: 34px;
    height: 34px;
    border-radius: 50%;
    object-fit: cover;
  }
}
.drop-head {
  padding: 18px 14px 14px 18px;
  border-bottom: 1px solid #f5f5f5;
  font-size: 13px;
  color: #999;
  .drop-avatar {
    display: flex;
    width: 45px;
    height: 45px;
    border-radius: 50%;
    object-fit: cover;
  }

  .drop-right {
    display: flex;
    flex-direction: column;
  }
  .align-center {
    align-items: center;
  }
  .name {
    font-size: 16px;
    font-family: PingFang SC-Medium, PingFang SC;
    font-weight: 500;
    color: #303133;
    span {
      font-size: 14px;
      font-weight: 400;
      color: #909399;
    }
  }
  .frame {
    margin-top: 4px;
    width: 100%;
    font-size: 14px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: #909399;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
}
.drop-item {
  line-height: 40px;
  cursor: pointer;
  padding-left: 18px;
  color: #606266;
  &:hover {
    background-color: #e8f4ff;
    color: #46a6ff;
  }
}
</style>
