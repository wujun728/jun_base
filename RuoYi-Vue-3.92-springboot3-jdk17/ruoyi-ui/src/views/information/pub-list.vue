<template>
  <!-- 已发布的资讯 -->
  <div class="news-list">
    <!-- 固定顶部搜索栏 -->
    <div class="search-bar">
      <div class="input-wrapper">
        <el-input
          v-model="queryParams.title"
          prefix-icon="el-icon-search"
          placeholder="请输入关键词，按回车搜索"
          clearable
          @clear="handleQuery"
          @keyup.enter.native="handleQuery"
        />
      </div>
    </div>

    <!-- 新闻卡片容器 -->
    <div class="news-card-container" ref="scrollContainer" @scroll="handleScroll">
      <div v-if="informationList.length > 0" class="news-cards">
        <div v-for="item in informationList" :key="item.id" class="news-card" @click="goDetail(item)">
          <div class="card-content">
            <div class="text-wrapper">
              <h3 class="news-title">
                <span v-if="item.topFlag == '1'" class="news-tag">置顶</span>
                {{ item.title }}
              </h3>
              <p class="news-content" v-html="item.content"></p>
              <p class="news-meta">
                <span>
                  <i class="el-icon-view" />
                  {{ item.readTotal }}
                </span>
                <span>
                  <i class="el-icon-time" />
                  {{ parseTime(item.validStartTime, '{y}-{m}-{d}') }}
                </span>
              </p>
            </div>
            <div class="img-wrapper" v-if="item.imgUrl">
              <img :src="imgPath(item.imgUrl)" alt />
            </div>
          </div>
        </div>
      </div>
      <!-- 加载更多提示 -->
      <div class="loading-more">
        <span v-if="loadingMore">
          <i class="el-icon-loading"></i> 加载中...
        </span>
        <span v-else-if="informationList && informationList.length > 0">已加载全部数据！</span>
      </div>
      <el-empty v-if="!informationList.length" :image-size="100" description="暂无数据" />
    </div>
    <!-- 回到顶部按钮 -->
    <div v-if="showBackToTop" class="back-to-top" @click="backToTop">
      <i class="el-icon-arrow-up"></i>
    </div>
  </div>
</template>

<script>
import { listPubInformation, addReadNum } from "@/api/information/information";

export default {
  name: "PubInformation",
  data() {
    return {
      // 遮罩层
      loading: false,
      // 加载更多
      loadingMore: false,
      // 滚动数据
      hasMore: true,
      // 是否显示回到顶部按钮
      showBackToTop: false,
      // 数据集
      informationList: [],
      // 查询参数，默认10条
      queryParams: {
        pageNum: 1,
        pageSize: 5,
        title: null,
      },
      // 图片预览的api前缀
      baseApi: process.env.VUE_APP_BASE_API,
    };
  },
  computed: {
    imgPath() {
      return (val) => {
        return this.baseApi + val;
      };
    },
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询新闻资讯列表 */
    async getList(loadMore = false) {
      if (this.loading || !this.hasMore) return;
      this.loading = true;
      if (loadMore) this.loadingMore = true;
      const res = await listPubInformation(this.queryParams);
      if (res.code === 200) {
        const newData = res.rows || [];
        this.informationList = loadMore ? [...this.informationList, ...newData] : newData;
        this.hasMore = newData.length >= this.queryParams.pageSize;
      }
      this.loading = false;
      this.loadingMore = false;
    },
    /** 滚动事件 */
    handleScroll(e) {
      const container = e.target;
      const scrollTop = container.scrollTop;
      // 显示/隐藏回到顶部按钮
      this.showBackToTop = scrollTop > 300;
      const scrollHeight = container.scrollHeight;
      const clientHeight = container.clientHeight;

      if (scrollHeight - scrollTop <= clientHeight + 20) {
        if (this.hasMore && !this.loading) {
          this.queryParams.pageNum += 1;
          this.getList(true); // 加载更多
        }
      }
    },
    /** 回到顶部 */
    backToTop() {
      const container = this.$refs.scrollContainer;
      container.scrollTop = 0;
    },
    /** 预览 */
    goDetail(row) {
      let params = { id: row.id };
      this.$tab.openPage(row.title, "/news/information/i/detail/:t" + new Date().getTime(), params);
      addReadNum(row.id).then((res) => {
        row.readTotal += 1;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.hasMore = true;
      this.queryParams.pageNum = 1;
      this.getList();
    },
  },
};
</script>

<style lang="scss" scoped>
.news-list {
  padding: 10px 0 10px 0;
}

/* 固定顶部搜索框 */
.search-bar {
  height: 50px;
  background-color: #fff;
  z-index: 1000;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.06);
  padding: 10px 15px;
  transition: all 0.3s ease;
}

.input-wrapper {
  position: relative;
  max-width: 600px;
}

.input-wrapper .el-input input {
  border: none;
  border-bottom: 1px solid #e4e4e4;
  border-radius: 0;
  font-size: 14px;
  width: 100%;
}

.input-wrapper .el-input__inner:focus {
  border-color: #409eff;
}

/* 新闻卡片容器 */
.news-card-container {
  margin-top: 5px;
  max-height: calc(88vh - 88px);
  overflow-y: auto;
  padding: 16px 20px;
}

.news-cards {
  display: flex;
  flex-direction: column;
  gap: 24px;
  max-width: 960px;
  margin: 0 auto;
}

.news-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
}

.card-content {
  display: flex;
  align-items: center;
  padding: 15px 15px 0 20px;
}

.text-wrapper {
  flex: 1;
  min-width: 0;
  margin-right: 16px;
}
.news-tag {
  text-align: center;
  align-content: center;
  font-size: 14px;
  padding: 3px;
  color: #fff;
  font-weight: 500;
  border: 1px solid #f9b404;
  background-color: rgb(255, 166, 0);
}
.news-title {
  font-size: 18px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin: 0;
}

.news-content {
  font-size: 14px;
  color: #606266;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2; // 控制显示两行
  -webkit-box-orient: vertical;
}

.news-meta {
  color: #909399;
  font-size: 13px;
  margin-top: 12px;

  span {
    margin-right: 12px;
  }
}

.img-wrapper {
  width: 150px;
  height: 96px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 4px;
  }
}

.loading-more {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 16px;
  color: #999;
  font-size: 14px;
}

.back-to-top {
  position: absolute;
  bottom: 40px;
  right: 40px;
  width: 40px;
  height: 40px;
  background-color: #409eff;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: opacity 0.3s ease;
  z-index: 999;
  font-size: 20px;

  &:hover {
    background-color: #66b1ff;
  }
}

::v-deep .input-wrapper .el-input input {
  border: none;
  border-bottom: 1px solid #e4e4e4;
  border-radius: 0;
  font-size: 14px;
}
::v-deep .input-wrapper .el-input__inner:focus {
  border-color: #409eff;
}
</style>