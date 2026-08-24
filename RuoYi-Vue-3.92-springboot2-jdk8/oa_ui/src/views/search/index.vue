<template>
  <div class="divBox">
    <div class="box">
      <el-card v-loading="loading" class="card normal-page">
        <div class="main">
          <!-- 搜索 -->
          <div class="search">
            <el-input v-model="keyword" placeholder="搜索功能和帮助" prefix-icon="el-icon-search" @change="getList">
              <el-button slot="suffix" type="primary" @click="getList()"> 搜索</el-button>
            </el-input>
          </div>
          <!-- tab切换 -->
          <div class="tab">
            <el-tabs v-model="activeName" @tab-click="handleClick">
              <el-tab-pane v-for="(item, index) in noticeNav" :key="item.val" :label="item.name" :name="item.val">
              </el-tab-pane
            ></el-tabs>
          </div>
          <!-- 内容 -->
          <div v-for="(item, index) in dataList" :key="index">
            <div v-if="item.type == 'document'" class="content" @click="openHelp(item)">
              <div class="header">
                <div class="title">
                  <span v-if="labelText == '全部'" class="text">【操作手册】</span>

                  <span v-html="item.question == '' ? item.title : item.question"></span>
                </div>
                <div class="read"><span class="iconfont iconzuozhezhongxin-chuangzuozhongxin" /> {{ item.visit }}</div>
              </div>
              <div class="article over-text2">
                <span v-html="item.content"></span>
              </div>
            </div>
            <div v-if="item.type == 'article'" class="content" @click="openArticle(item)">
              <div class="header">
                <div class="title">
                  <span v-if="labelText == '全部'" class="text color1">【知识社区】</span>

                  <span v-html="item.title"></span>
                </div>
                <div class="read"><span class="iconfont iconzuozhezhongxin-chuangzuozhongxin" /> {{ item.visit }}</div>
              </div>
              <div class="article over-text2">
                <span v-html="item.content"></span>
              </div>
            </div>
            <div v-if="item.type == 'file' && item.unlock == 0" class="content" @click="proview(item)">
              <div class="header">
                <div class="title"><span class="iconfont iconbiaoge"></span> <span v-html="item.title"></span></div>
                <div class="read"><span class="iconfont iconzuozhezhongxin-chuangzuozhongxin" /> {{ item.visit }}</div>
              </div>
              <div class="flex-end mt15">
                <span class="icon">￥</span>
                <span class="num">{{ item.price }}</span>
                <span class="use">{{ item.download }}人使用</span>
                <div class="down" @click.stop="fileDown(item)"><span class="iconfont iconxiazai" />下载</div>
              </div>
            </div>
            <div v-if="item.type == 'file' && item.unlock == 1" class="content" @click="proview(item)">
              <div class="header">
                <div class="title"><span class="iconfont iconppt"></span> <span v-html="item.title"></span></div>
                <div class="read"><span class="iconfont iconzuozhezhongxin-chuangzuozhongxin" /> {{ item.visit }}</div>
              </div>
              <div class="flex-end mt15">
                <span class="free">免费</span>
                <span class="use">{{ item.download }}人使用</span>
                <div class="down" @click.stop="fileDown(item)"><span class="iconfont iconxiazai" />下载</div>
              </div>
            </div>
          </div>

          <div v-if="dataList.length == 0" class="default">
            <img alt="" src="../../assets/images/help.png" />
            <div class="text">暂无搜索结果~</div>
          </div>
        </div>
      </el-card>

      <!-- 二维码组件 -->
      <payment ref="payment" :from-data="payData" @isOk="getList"></payment>
    </div>
  </div>
</template>

<script>
import { mapMutations } from 'vuex'
import { helpCenterApi, templatePayCode, templateViewApi, templateExportApi } from '@/api/user'
import file from '@/utils/file'
import Vue from 'vue'
Vue.use(file)
import { roterPre } from '@/settings'
import payment from '@/components/form-common/dialog-payment'
export default {
  name: 'CrmebOaEntIndex',
  components: {
    payment
  },
  data() {
    return {
      keyword: '',
      activeName: '',
      loading: false,
      labelText: '全部',
      listloading: false,
      dataList: [],
      payData: {},
      page: 1,
      limit: 15,
      noticeNav: [
        {
          val: '',
          name: '全部'
        },
        {
          val: 'document',
          name: '操作手册'
        },
        {
          val: 'article',
          name: '知识社区'
        }
        // {
        //   val: 'file',
        //   name: '模板文库'
        // }
      ]
    }
  },

  mounted() {
    if (this.$route.params.index) {
      this.activeName = this.$route.params.index
    } else {
      this.activeName = ''
    }
    this.keyword = this.$route.query.keyword
    this.getList()
    window.addEventListener('scroll', this.lazyLoading)
  },
  destroyed() {
    window.removeEventListener('scroll', this.lazyLoading)
  },

  watch: {
    $route(val, from) {
      this.keyword = val.query.keyword
      this.getList()
    }
  },
  computed: {},

  methods: {
    handleClick(e) {
      this.labelText = e.label

      this.getList()
    },
    ...mapMutations('user', ['SET_MENU_LIST']),

    // 滚动加载
    lazyLoading(e) {
      let scrollTop = document.documentElement.scrollTop || document.body.scrollTop
      let clientHeight = document.documentElement.clientHeight
      let scrollHeight = document.documentElement.scrollHeight
      let bottomOfWindow = scrollTop + clientHeight >= scrollHeight - 4

      if (bottomOfWindow && this.dataList.length > 5) {
        this.page = this.page + 1
        this.getList(1)
      } else {
      }
    },

    // 预览
    proview(item) {
      templateViewApi(item.id).then((res) => {
        if (res.status == 200) {
          let url = `${roterPre}/openFile?id=${item.id}&&fid=${item.pid || this.spaceId}`
          window.open(url, '_blank')
        }
      })
    },
    // 打开知识社区
    openArticle(item) {
      this.$router.push({ path: `${roterPre}/user/forum/index`, query: { id: item.id } })
    },
    // 打开帮助中心
    openHelp(item) {
      window.open(`https://doc.tuoluojiang.com/doc/own/${item.id}`, '_blank')
    },
    fileDown(item) {
      templateExportApi(item.id)
        .then((res) => {
          this.fileLinkDownLoad(res.data.url, '模板文库')
        })
        .catch((err) => {
          this.handlePayment(item.id)
        })
    },

    handlePayment(id) {
      templatePayCode(id).then((res) => {
        this.payData = {
          title: '支付',
          width: '300px',
          type: 0,
          data: res.data
        }
        this.$refs.payment.handleOpen()
      })
    },
    getList(val) {
      this.$store.commit('app/SET_CLICK_KEY', this.keyword)
      this.loading = true
      if (this.activeName == 'all') {
        this.activeName = ''
      }
      if (!val) {
        this.page = 1
      }
      this.listloading = true

      let data = {
        word: this.keyword,
        type: this.activeName,
        page: this.page,
        limit: this.limit
      }

      helpCenterApi(data).then((res) => {
        this.loading = false

        if (val == 1) {
          this.dataList = [...this.dataList, ...res.data]
          this.listloading = false
        } else {
          this.dataList = res.data
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.box {
  // padding: 14px;

  .card {
    padding-top: 30px;
    .main {
      max-width: 800px;
      margin: 0 auto;
      ::v-deep.el-input--medium .el-input__icon {
        line-height: 42px;
      }
      ::v-deep .el-input-group__append {
        top: 0;
        background-color: #1890ff;
        font-size: 14px;
        font-family: PingFang SC-Regular, PingFang SC;
        font-weight: 400;
        color: #ffffff;
      }
      .tab {
        margin-top: 50px;
      }
      .content {
        cursor: pointer;
        padding: 25px 0;
        border-bottom: 1px solid #eeeeee;
        ::v-deep .highlight {
          background-color: #fff310 !important;
        }

        .header {
          display: flex;
          justify-content: space-between;

          .title {
            font-size: 18px;
            font-family: PingFang SC-Medium, PingFang SC;
            font-weight: 500;
            color: #303133;
            .text {
              font-size: 14px;
              color: #ff9843;
            }
            ::v-deep .highlight {
              background-color: #fff310 !important;
            }
            .iconwendang {
              color: #8737ff;
              font-size: 17px;
            }
            .iconqiyetongxunlu1 {
              color: #1837ff;
              font-size: 17px;
            }
            .iconbiaoge {
              color: #01bd79;
            }
            .iconppt {
              color: #ff9843;
            }
          }
          .read {
            font-size: 12px;
            font-family: PingFang SC-Regular, PingFang SC;
            font-weight: 400;
            color: rgba(0, 0, 0, 0.45);
            .iconzuozhezhongxin-chuangzuozhongxin {
              font-size: 12px;
            }
          }
        }
        .article {
          margin-top: 15px;
          font-size: 14px;
          font-family: PingFang SC-Regular, PingFang SC;
          font-weight: 400;
          color: #606266;
          line-height: 24px;
        }
      }
    }
  }
}

.icon {
  font-size: 12px;
  font-weight: 600;
  color: #f5222d;
}
.num {
  font-size: 16px;
  font-family: PingFang SC-Semibold, PingFang SC;
  font-weight: 600;
  color: #f5222d;
  margin-right: 20px;
}
.use {
  font-size: 14px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #606266;
  margin-right: 22px;
}
.down {
  cursor: pointer;
  font-size: 12px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #1890ff;
}
.free {
  font-size: 14px;
  font-family: PingFang SC-Semibold, PingFang SC;
  font-weight: 600;
  color: #1890ff;
  margin-right: 20px;
}
.flex-end {
  display: flex;
  align-items: center;
}

::v-deep .el-tabs__nav-wrap::after {
  height: 1px;
  background: #eeeeee;
}
::v-deep .el-tabs__header {
  margin: 0;
}
.default {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  img {
    margin-top: 100px;
    display: block;
    width: 200px;
    height: 150px;
  }
  .text {
    font-size: 13px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: #999999;
  }
}

::v-deep .el-input__suffix {
  position: absolute;
  right: 0;
}

::v-deep .el-button--medium {
  height: 42px;

  border-top-left-radius: 0;
  border-bottom-left-radius: 0;
}
::v-deep .el-input--medium .el-input__inner {
  height: 42px;
  line-height: 42px;
}
::v-deep .el-input {
  padding-right: 50px;
}
.color1 {
  color: #1890ff !important;
}
</style>
