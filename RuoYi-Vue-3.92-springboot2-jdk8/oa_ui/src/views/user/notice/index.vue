<template>
  <div class="divBox">
    <el-row class="flex-box">
      <!-- 左边内容 -->
      <el-col class="flex-left">
        <el-card v-if="!isDetail" class="card-box" :body-style="{ padding: '12px 20px 20px 20px' }">
          <div class="main">
            <div class="content">
              <div class="notice-nav-content">
                <el-tabs v-model="activeName" @tab-click="handleClick">
                  <el-tab-pane
                    v-for="(item, index) in noticeNav"
                    :key="index"
                    :label="item.cate_name"
                    :name="index.toString()"
                  ></el-tab-pane
                ></el-tabs>
              </div>
              <template v-if="tableData.length > 0">
                <div class="notice-box">
                  <ul class="notice-content">
                    <li v-for="item in tableData" :key="item.id" @click="itemDetail(item.id)">
                      <el-row :gutter="20">
                        <el-col class="notice-left" :class="item.cover ? '' : 'width'">
                          <p class="title1">
                            <span class="tag" v-if="item.is_top === 1" size="mini">置顶</span>
                            <span class="over-text"> {{ item.title }}</span>
                          </p>
                          <p class="info over-text2">{{ item.info }}</p>
                          <div class="bottom">
                            <span>
                              <i class="iconfont iconriqishijian" title="发布时间"></i>
                              {{ item.push_time }}
                            </span>
                            <span>
                              <i class="iconfont iconyiyuedu" title="阅读次数"></i>
                              {{ item.visit }}
                            </span>
                          </div>
                        </el-col>
                        <el-col v-if="item.cover" class="notice-right">
                          <div :style="{ backgroundImage: 'url(' + item.cover + ')' }" alt="" class="image" />
                        </el-col>
                      </el-row>
                    </li>
                  </ul>
                </div>

                <div class="page-fixed">
                  <el-pagination
                    :page-size="where.limit"
                    :current-page="where.page"
                    layout="total,sizes,prev, pager, next, jumper"
                    :page-sizes="[10, 20, 30]"
                    :total="total"
                    @size-change="handleSizeChange"
                    @current-change="pageChange"
                  />
                </div>
              </template>
              <default-page v-else v-height :min-height="300" :index="14" />
            </div>
          </div>
        </el-card>
        <notice-detail v-else :data="detailData" @handleNoticeDetail="handleNoticeDetail"></notice-detail>
      </el-col>

      <!-- 右侧最公告 -->
      <el-col class="flex-right">
        <el-card class="news-right">
          <div slot="header" class="clearfix">
            <div class="news-title">
              <span class="before"></span>
              最新公告
            </div>
          </div>
          <ul class="news-content" v-if="nweNoticeData.length > 0">
            <li v-for="(item, index) in nweNoticelist" :key="'new' + item.id" @click="itemDetail(item.id)">
              <el-row :gutter="20">
                <el-col class="notice-left" :class="item.cover ? '' : 'width100'">
                  <div style="display: flex">
                    <span class="title-index">{{ index + 1 + '.' }}</span>
                    <p class="title over-text2 blod">
                      <span class="title-alignment"> {{ item.title }}</span>
                    </p>
                  </div>

                  <div class="bottom">
                    <span>
                      <i class="iconfont iconriqishijian"></i>
                      {{ item.push_time }}
                    </span>
                    <span>
                      <span class="fixed-right">
                        <i class="iconfont iconyiyuedu"></i>
                        {{ item.visit }}
                      </span>
                    </span>
                  </div>
                </el-col>
              </el-row>
            </li>
          </ul>
          <default-page v-else :min-height="360" :index="14" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { noticeCategoryApi, noticeDetailApi, noticeListApi, allArticlesApi } from '@/api/administration'
import settings from '@/settings'
export default {
  name: 'IndexVue',
  components: {
    noticeDetail: () => import('./components/noticeDetail'),
    defaultPage: () => import('@/components/common/defaultPage')
  },
  data() {
    return {
      where: {
        page: 1,
        limit: 10,
        cate_id: '',
        name: '',
        status: 1
      },
      noticeNav: [],
      total: 0,
      tableData: [],
      defaultImage: require('@/assets/images/notice.png'),
      navIndex: true,
      nweNoticeData: [],
      isDetail: false,
      detailData: {},
      activeName: '0'
    }
  },
  computed: {
    nweNoticelist() {
      if (this.nweNoticeData.length > 5) {
        return this.nweNoticeData.splice(0, 5)
      } else {
        return this.nweNoticeData
      }
    }
  },
  mounted() {
    this.getTargetCate()
    // const limit = Math.floor((window.innerHeight - 240) / 155)
    // this.where.limit = limit > 0 ? limit : this.where.limit
    this.getAllArticles()
    this.getNewTableData()
    if (this.$route.query.id) {
      this.itemDetail(this.$route.query.id)
    }
  },
  methods: {
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    handleClick(e) {
      const index = Number(e.name)
      const data = this.noticeNav[index]
      this.where.cate_id = data.id

      this.where.page = 1
      if (index == 0) {
        this.getAllArticles()
      } else {
        this.getTableData()
      }
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    // 获取全部文章
    getAllArticles() {
      this.where.cate_id = ''
      allArticlesApi(this.where).then((res) => {
        this.tableData = res.data.list
        this.total = res.data.count
      })
    },
    // 获取表格数据
    getTableData() {
      noticeListApi(this.where).then((res) => {
        this.tableData = res.data.list || []
        this.total = res.data.count
      })
    },
    getNewTableData() {
      const data = {
        is_new: 1,
        status: 1
      }
      noticeListApi(data).then((res) => {
        this.nweNoticeData = res.data.list || []
      })
    },
    getTargetCate() {
      noticeCategoryApi({ is_show: 1 }).then((res) => {
        this.noticeNav = res.data ? res.data : []
        this.noticeNav.unshift({
          cate_name: '全部',
          id: 0
        })
      })
    },
    // 获取详情页
    itemDetail(id) {
      noticeDetailApi(id).then((res) => {
        this.detailData = res.data
        this.isDetail = true
        this.getNewTableData()
      })
    },
    handleSearch() {
      this.where.page = 1
      this.getTableData()
    },
    handleNoticeDetail() {
      this.isDetail = false
      if (this.$route.query.id) {
        this.$router.push({
          path: settings.roterPre + '/user/notice/index',
          query: {}
        })
      }
      if (this.where.cate_id == 0) {
        this.getAllArticles()
      } else {
        this.getTableData()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.divBox {
  //width: calc(100% - 42px);

  .flex-box {
    display: flex;
    justify-content: space-between;
    .card-box {
      height: calc(100vh - 77px);
    }
  }
  .notice-box {
    height: calc(100vh - 200px);
    overflow-y: auto;
    scrollbar-width: none; /* firefox */
    -ms-overflow-style: none; /* IE 10+ */
    overflow-x: hidden;
  }
  .flex-right {
    width: 300px;
    margin: 0 14px;
    min-height: calc(100vh - 77px);
  }
  .flex-left {
    width: calc(100% - 320px);
    .main {
      max-width: 800px;
      margin: 0 auto;
    }
  }
}

.title-alignment {
  display: inline-block;
  width: calc(100% - 20px);
}

.mt0 ::v-deep .divBox .el-pagination {
  margin-top: 0px;
}
.pagination {
  display: flex;
  justify-content: flex-end;
}

.width100 {
  width: 100% !important;
}
.table-box {
  ::v-deep .el-table th {
    background-color: #f5f5f5;
  }
  .table-img {
    width: 39px;
    height: auto;
  }
}
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
::v-deep .el-scrollbar__thumb {
  display: none;
}
.content {
  ul {
    list-style: none;
    margin: 0;
    padding: 0;
  }
  .notice-nav-content {
    overflow: hidden;

    border-bottom: 1px solid #eeeeee;
    position: relative;
    ::v-deep .el-tabs__header {
      margin: 0;
    }
    ::v-deep .el-tabs__nav-scroll {
      // padding: 0 20px;
    }
    ::v-deep .el-tabs__item.is-active {
      color: #1890ff !important;
    }
    ::v-deep .el-tabs__nav-wrap::after {
      height: 1px;
    }
    ::v-deep .el-tabs__item {
      height: 35px;
      font-size: 14px;
      font-family: PingFang SC-中黑体, PingFang SC;
      font-weight: 600;
      color: #303133;
      margin-top: 11px;
    }
    .notice-nav {
      padding: 0 30px;
      li {
        padding: 0 0 15px 0;
        margin: 0 10px;
        float: left;
        cursor: pointer;
        &.active {
          border-bottom: 2px solid #1890ff;
        }
      }
    }
    button {
      position: absolute;
      top: -15px;
      font-size: 16px;
      background: #fff;
      height: 50px;
      width: 40px;
    }
    .icon-left {
      left: 0;
      padding-left: 10px;
    }
    .icon-right {
      right: 0;
      padding-right: 10px;
    }
  }
  .notice-content {
    li {
      width: calc(100% - 0px);
      border-bottom: 1px solid #eee;
      cursor: pointer;
      .notice-left {
        width: calc(100% - 181px);
        padding-top: 24px;

        p {
          margin: 0;
        }
        .title1 {
          display: flex;
          align-items: cneter;

          font-size: 18px;
          font-family: PingFang SC-中黑体, PingFang SC;
          font-weight: 600;
          color: #303133;
          .tag {
            display: inline-block;
            width: 30px;
            height: 18px;
            margin-top: 2px;
            margin-right: 4px;
            text-align: center;
            line-height: 18px;
            background: #ff9900;
            border-radius: 2px 2px 2px 2px;
            font-size: 13px;

            font-weight: normal;
            color: #ffffff;
          }
        }
        .title {
          font-size: 15px;
          font-weight: bold;
          margin: 10px 0;
          display: flex;
          align-items: flex-start;
          ::v-deep .el-tag--warning {
            background-color: #ffba00;
            color: #ffffff;
            font-size: 13px;
            font-weight: normal;
            line-height: 20px;
          }
        }
        .info {
          margin-top: 10px;
          font-size: 14px;
          font-family: PingFang SC-常规体, PingFang SC;
          font-weight: normal;
          color: #606266;
          line-height: 22px;
        }
      }
      .width {
        width: 100%;
        // margin-bottom: 10px;
      }
      .notice-right {
        width: 171px;
        max-height: 157px;
        margin-top: 20px;
        margin-bottom: 20px;
        display: flex;
        flex-direction: column;
        justify-content: center;

        .image {
          width: 160px;
          height: 108px;
          border-radius: 6px 6px 6px 6px;
          background-position: center center;
          background-size: cover;
          background-repeat: no-repeat;
          border: 1px solid #f0f2f5;
        }
      }
      .bottom {
        margin-top: 14px;
        margin-bottom: 24px;

        // padding-left: 6px;

        color: #999999;
        i {
          font-size: 13px;
        }
        span:last-of-type {
          padding-left: 20px;
        }
      }
    }
  }
}
.before {
  display: inline-block;
  width: 12px;
  height: 18px;
  border-left: 3px solid #1890ff;
  border-radius: 2px;
}
.news-title {
  font-size: 18px;
  font-family: PingFang SC-中粗体, PingFang SC;
  font-weight: normal;
  color: #333333;
  font-weight: 600;
  display: flex;
  align-items: center;
  position: relative;
  margin-top: 10px;
}

.news-content {
  margin: 0;
  padding-left: 0;
  padding-top: 20px;
  list-style: none;
  li {
    box-sizing: border-box;
    padding: 10px;
    padding-left: 0px;
    padding-top: 20px;
    padding-bottom: 19px;
    border-bottom: 1px solid #eee;
    cursor: pointer;
    .notice-left {
      p {
        margin: 0;
      }
      .title-index {
        font-size: 16px;
        font-family: PingFang SC-常规体, PingFang SC;
        font-weight: normal;
        color: #1890ff;
        display: inline-block;
        margin-right: 4px;
        margin-top: 2px;
      }
      .title {
        width: 100%;
        font-size: 16px;
        font-weight: 600;
        font-family: PingFang SC-常规体, PingFang SC;
        color: rgba(0, 0, 0, 0.85);
        line-height: 22px;
        margin-bottom: 8px;
      }
      .info {
        color: #999999;
        font-size: 13px;
        line-height: 1.3;
      }
      .bottom {
        padding-left: 19px;
        margin-top: 10px;
        color: #999999;
        font-family: PingFang SC-常规体, PingFang SC;
        font-weight: normal;
        font-size: 13px;
        position: relative;
        .fixed-right {
          position: absolute;
          bottom: 0;
          right: 0;
        }
        i {
          font-size: 13px;
        }
        span:last-of-type {
          padding-left: 10px;
        }
      }
    }
    .notice-right {
      width: 114px;
      img {
        width: 94px;
        height: 63px;
      }
    }
  }
}
.news-right {
  width: 328px;
  border-radius: 4px 4px 4px 4px;
  min-height: calc(100vh - 77px);
  opacity: 1;
  ::v-deep .el-card__header {
    padding: 12px 20px 0 20px;
    border-bottom: none;
  }
  ::v-deep .el-card__body {
    padding-top: 0;
    padding-bottom: 0;
  }
}
.page-fixed {
  position: relative;
}
::v-deep.el-pagination {
  margin-top: 40px;
}
</style>
