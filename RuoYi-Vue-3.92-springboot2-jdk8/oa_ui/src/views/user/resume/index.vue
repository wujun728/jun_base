<template>
  <div class="divBox">
    <div class="content">
      <div class="left">
        <el-card class="left-card">
          <LeftNavigation :componentStatus="initData" @pageJump="pageJump" @getImageUrl="getImageUrl"
            :imageUrl="imageUrl" :user="user"></LeftNavigation>
        </el-card>
      </div>
      <div class="right">
        <el-card class="right-card">
          <formItemDataList :componentStatus="initData" @getPhoto="getPhoto" @hiddenSave="hiddenSave" :user="user"
            ref="formItemData" />
        </el-card>
      </div>
      <!-- 侧边推送 -->
      <el-card class="pushBox">
        <div @scroll.passive="getScroll($event)">
          <div class="title mb20" v-if="list.length > 0">邀请提示</div>
          <div class="send-box" v-for="(item, index) in list" :key="index">
            <div class="send-content">
              <img :src="item.enterprise.logo" alt="" class="img" />
              <div class="text">
                <div>{{ item.enterprise.enterprise_name }}</div>
                <span v-if="item.types == 0">邀请您发送个人简历信息</span>
                <span v-else>您当前所在企业，邀请您完善个人档案信息</span>
              </div>
            </div>
            <div class="footer">
              <div class="refuse" @click="putPerfectRefuse(item)">拒绝</div>
              <div class="refuse1" @click="putPerfectAgree(item)">发送</div>
            </div>
          </div>
          <div>
            <div class="title" v-if="h_list.length > 0">邀请记录</div>
            <div class="h-box" v-for="(val, index) in h_list" :key="index">
              <img :src="val.enterprise.logo" alt="" class="img" />
              <div class="text">
                <div>{{ val.enterprise.enterprise_name }}</div>
                <!-- 提取样式为类名，提高代码可维护性 -->
                <span :class="{ 'status-success': val.status === 1, 'status-fail': val.status === 2 }"
                  v-if="val.types === 0">
                  {{ val.status === 1 ? '已发送个人简历信息' : '拒绝发送个人简历信息' }}
                </span>
                <span :class="{ 'status-success': val.status === 1, 'status-fail': val.status === 2 }"
                  v-if="val.types === 1">
                  {{ val.status === 1 ? '已完善个人档案信息' : '拒绝完善个人档案信息' }}
                </span>
              </div>
            </div>
          </div>

          <!-- 缺省页 -->
          <div class="default" v-if="h_list.length == 0 && list.length == 0">
            <img src="../../../assets/images/def1.png" alt="" class="def-img" />
            <div>暂无企业邀请信息～</div>
          </div>
        </div>
      </el-card>

      <div class="cr-bottom-button btn-shadow">
        <el-button size="small" @click="restFn">重置</el-button>
        <el-button type="primary" size="small" @click="submit">保存</el-button>
      </div>
    </div>
  </div>
</template>
<script>
import formOptions from '@/views/hr/archives/mixins/index.js'
import { getPerfectIndex, putPerfectAgree, putPerfectRefuse } from '@/api/user'

export default {
  name: 'one',
  components: {
    LeftNavigation: () => import('@/views/hr/archives/components/LeftNavigation.vue'),
    formItemDataList: () => import('@/views/hr/archives/components/formItemDataList.vue')
  },
  props: {},
  mixins: [formOptions],
  data() {
    return {
      initData: { componentName: 'addDrawerIsShow', type: 'add' },
      user: '个人简历',
      imageUrl: '',
      list: [],
      h_list: [],
      loading: true,
      saveLoading: false,
      showMore: false,
      total: true,
      page: 1,
      limit: 7
    }
  },
  mounted() {
    this.getPerfectIndex()
    this.getRefuse()
    setTimeout(() => {
      this.$refs.formItemData.getResume()
    }, 200)
  },

  methods: {
    pageJump(id, index) {
      this.$nextTick(() => {
        document.getElementById(index).scrollIntoView({ behavior: 'smooth', inline: 'start' })
      })
    },

    getScroll(event) {
      let scrollHeight = event.target.scrollHeight
      let scrollTop = event.target.scrollTop
      let clientHeight = event.target.clientHeight
      let scrollBottom = scrollHeight - scrollTop - clientHeight
      this.showMore = scrollBottom < 1
      if (scrollBottom < 1 && this.total) {
        this.getRefuse(1)
      }
    },

    getPhoto(val) {
      this.imageUrl = val
      this.loading = false
    },
    getPerfectIndex() {
      this.$store.commit('app/SET_PARENTCUR', 100)
      let data = {
        status: 0
      }
      getPerfectIndex(data).then((res) => {
        this.list = res.data.list
      })
    },

    // 同意
    putPerfectAgree(data) {
      putPerfectAgree(data.id).then((res) => {
        this.getPerfectIndex()
        this.page = 1
        this.getRefuse()
      })
    },

    // 拒绝
    putPerfectRefuse(data) {
      putPerfectRefuse(data.id).then((res) => {
        this.getPerfectIndex()
        this.page = 1
        this.getRefuse()
      })
    },
    // 记录
    getRefuse(val) {
      if (val == 1) {
        this.page = this.page + 1
      }
      let data = {
        page: this.page,
        limit: this.limit
      }
      getPerfectIndex(data).then((res) => {
        if (val === 1) {
          this.h_list = [...this.h_list, ...res.data.list]
        } else {
          this.h_list = res.data.list
        }

        this.total = res.data.list.length > 4
      })
    },

    // 获取个人头像
    getImageUrl(val) {
      this.imageUrl = val
    },
    // 保存简历
    submit() {
      this.saveLoading = true
      this.$refs.formItemData.saveResume(this.imageUrl)
    },
    // 保存简历
    hiddenSave() {
      this.saveLoading = false
    },
    // 重置
    restFn() {
      this.$refs.formItemData.getResume()
    }
  }
}
</script>
<style scoped lang="scss">
.content {
  display: flex;

  .left {
    width: 200px;
  }
}

.status-success {
  color: #1890ff;
}

.status-fail {
  color: #ed4014;
}

.cr-bottom-button {
  position: fixed;
  right: 0;
  left: 0;
  bottom: 0;
  width: 100%;
  background: #fff !important;
  text-align: center;
  padding: 14px 0;
  z-index: 1;
  box-shadow: 2px 0px 4px 0px rgba(0, 0, 0, 0.06);
}

::v-deep.el-card {
  border: none;
}

::v-deep .assess-left .head-portrait .avatar {
  margin-top: 0px !important;
}

.right-card {
  padding: 20px;
  padding-bottom: 300px;
}

.left-card {
  height: calc(100vh - 0px);
  padding-top: 20px;
}

.right {
  width: calc(100% - 350px);
  height: calc(100vh - 0px);
  overflow: scroll;
  padding-right: 14px;
}

.right::-webkit-scrollbar {
  height: 0;
  width: 0;
}

.from-item-title {
  display: flex;
  justify-content: space-between;
  height: 20px;
  border-left: 2px solid #1890ff;

  .editBtn {
    margin-right: 20px;
  }

  span {
    padding-left: 10px;
    font-weight: 500;
    font-size: 13px;
  }
}

.pushBox {
  width: 350px;
  height: calc(100vh - 0px);
  overflow-y: auto;
  padding-bottom: 120px;
  padding: 0 15px;
  padding-top: 20px;
}

.mt20 {
  margin-top: 20px;
}

.default {
  margin-top: 150px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

::v-deep .head-portrait .avatar-uploader-icon {
  width: 90px !important;
  height: 110px !important;
  line-height: 110px !important;
}

.title {
  font-size: 16px;
  font-weight: 600;
  color: #333333;
  width: 74px;
  height: 18px;
  border-left: 2px solid #1890ff;
  text-align: center;
  line-height: 18px;
}

.h-box {
  display: flex;
  width: 100%;
  height: 80px;
  padding-top: 20px;
  border-bottom: 1px solid #ebeef5;

  .img {
    width: 50px;
    height: 50px;
    background: #f2f6fc;
    border-radius: 4px;
  }

  .text {
    margin-left: 10px;
    margin-top: 4px;

    div {
      font-size: 14px;
      margin-bottom: 4px;
    }

    span {
      font-size: 13px;
    }
  }
}

.def-img {
  width: 200px;
  height: 150px;
}

.send-box {
  width: 100%;
  height: 123px;
  background-color: #f2f6fc;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 15px;
  position: relative;

  .tips {
    position: absolute;
    top: 0;
    right: 0;
    background-color: pink;
    width: 60px;
    height: 20px;
    text-align: center;
    line-height: 20px;
    font-size: 13px;
  }
}

.send-content {
  display: flex;

  .img {
    width: 50px;
    height: 50px;
    background: #f2f6fc;
    border-radius: 4px;
  }

  .text {
    margin-left: 10px;
    margin-top: 4px;

    div {
      font-size: 14px;
      margin-bottom: 4px;
    }

    span {
      font-size: 13px;
    }
  }
}

.footer {
  display: flex;
  margin-top: 10px;
  margin-left: 140px;

  .refuse {
    width: 54px;
    height: 28px;
    background-color: #ed4014;
    cursor: pointer;
    font-size: 13px;
    border-radius: 2px;
    font-weight: 400;
    color: #ffffff;
    margin-right: 10px;
    text-align: center;
    line-height: 28px;
  }

  .refuse1 {
    width: 54px;
    height: 28px;
    background: #1890ff;
    border-radius: 2px;
    cursor: pointer;
    font-size: 13px;
    font-weight: 400;
    color: #ffffff;
    text-align: center;
    line-height: 28px;
  }
}

::v-deep .from-foot-btn {
  width: calc(100% - 0px);
}

::v-deep .el-card__body {
  padding: 0;
}

.divBox {
  // margin-top: -50px !important;
  margin-left: -145px;
  position: relative;
  padding-top: 0px;
  margin-top: 0;
}
</style>
