<!-- 消息通知 -->
<template>
  <div class="notice">
    <div class="header-notice right-menu-item">
      <span class="el-dropdown-link">
        <el-badge
          v-if="$store.state.user.messageCount > 0"
          @click.native="goMessageCenter"
          :is-dot="false"
          :value="$store.state.user.messageCount"
          :max="99"
        >
          <el-tooltip effect="dark" content="消息通知" placement="bottom">
            <i class="el-icon-bell el-icon-notice" />
          </el-tooltip>
        </el-badge>
        <span v-else @click="goMessageCenter">
          <el-tooltip effect="dark" content="消息通知" placement="bottom">
            <i class="el-icon-bell el-icon-notice" style="margin-top: 2px;" />
          </el-tooltip>
        </span>
      </span>
      <!-- 消息列表 -->
      <noticeList ref="noticeList"></noticeList>
    </div>
  </div>
</template>
<script>
import { messageListApi } from '@/api/public'
import { roterPre } from '@/settings'
export default {
  name: 'Notice',
  components: {
    noticeList: () => import('./noticeList')
  },
  data() {
    return {
      count: 0,
      roterPre: roterPre,
      where:{
        page:1,
        limit:1,
      }
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    // 列表
    getList() {
      messageListApi(this.where).then((res) => {
        this.count = res.data.messageNum ? res.data.messageNum : 0
        this.$store.commit('user/SET_MESSAGE', this.count)
      })
    },
    goMessageCenter() {
      if (this.$store.name == 'user-news-unread') return
      this.$refs.noticeList.openBox()
    }
  }
}
</script>

<style lang="scss" scoped>
.header-notice {
  position: relative;
  cursor: pointer;
  .el-icon-notice {
    font-size: 22px;
    color: #606266;
  }
}
.el-badge {
  top: 1px;
}
::v-deep .el-badge__content {
  border: none;
  top: 4px;
}
.el-icon-message-solid {
  color: #2d8cf0;
  cursor: pointer;
  font-size: 16px;
  &:focus {
    outline: 0;
  }
}
</style>
