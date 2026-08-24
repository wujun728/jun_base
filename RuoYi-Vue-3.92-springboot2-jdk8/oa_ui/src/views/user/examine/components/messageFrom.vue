<template>
  <div>
    <div class="mb10 title">留言</div>
    <div v-for="(item, index) in reply" :key="index" class="acea-row row-between mb25">
      <div class="flex-between" style="width: 100%">
        <div class="mb15 flex">
          <div class="pic mr10">
            <img :src="item.card.avatar" />
          </div>
          <div>
            <span class="fonts">{{ item.card.name }}</span>

            <div class="fonts mt5">{{ item.content }}</div>
          </div>
        </div>
        <div class="del">
          <div class="time">{{ item.created_at }}</div>
          <div
            v-if="$store.state.user.userInfo.uid === item.card.uid"
            @click="del(item.id)"
            class="el-icon-delete mr5 mt10"
          >
            删除
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { approveReplyApi, approveReplyDelApi } from '@/api/business'
export default {
  name: 'MessageFrom',
  props: {
    examineData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      textarea: '',
      reply: []
    }
  },
  watch: {
    examineData: {
      handler(nVal) {
        this.reply = nVal.reply
      },
      immediate: true
    }
  },
  methods: {
    async add() {
      if (this.textarea == '') {
        return this.$message.error('请输入留言')
      }
      await approveReplyApi({
        apply_id: this.examineData.id,
        content: this.textarea
      })
      this.textarea = ''
      await this.$emit('upDate', this.examineData.id)
    },
    async del(id) {
      await approveReplyDelApi(id)
      await this.$emit('upDate', this.examineData.id)
    }
  }
}
</script>

<style scoped lang="scss">
.title {
  font-size: 13px;
  color: #666666;
}
.clear {
  clear: both;
}
.add {
  float: right;
}
.inpBox {
  background-color: #f9f9f9;
  ::v-deep.el-textarea__inner {
    border: none;
    resize: none;
    background-color: #f9f9f9;
  }
}
.pic {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  overflow: hidden;
  img {
    width: 100%;
    height: 100%;
  }
}
.fonts {
  max-width: 400px;
  font-family: PingFang SC, PingFang SC;
  font-weight: 500;
  font-size: 13px;
  color: #303133;
}
.time {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 11px;
  color: #909399;
}
.del {
  cursor: pointer;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #909399;
}
</style>
