<template>
  <!-- 公司资讯 -->
  <div class="news-container">
    <div class="position-title">
      <span>
        <svg-icon icon-class="news" class="news-flag" />公司资讯
      </span>
      <span class="load-more" @click="loadMore">
        更多
        <i class="el-icon-arrow-right" />
      </span>
    </div>
    <div class="news-body">
      <el-carousel :height="carouselH" :interval="4000" type="card" v-if="newsList && newsList.length > 0">
        <el-carousel-item v-for="item in newsList" :key="item.id">
          <div class="news-card" @click="goDetail(item)" :style="{ backgroundImage: 'url(' + imgPath(item.imgUrl) + ')' }">
            <div class="news-title">{{ item.title }}</div>
            <div class="info">
              <span>
                <i class="el-icon-view"></i>
                {{ item.readTotal }}
              </span>
              <span>
                <i class="el-icon-time"></i>
                {{ item.validStartTime }}
              </span>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
      <el-empty v-else :image-size="50"></el-empty>
    </div>
  </div>
</template>

<script>
import { listPubInformation, addReadNum } from "@/api/information/information";

export default {
  name: "NewsModule",
  data() {
    return {
      // 加载状态
      loading: false,
      // 数据列表
      newsList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 5,
      },
      // 高度自适应
      carouselH: "calc(24vh - 80px)",
      baseApi: process.env.VUE_APP_BASE_API,
    };
  },
  computed: {
    imgPath() {
      return (val) => {
        if (!val) return "";
        return this.baseApi + val;
      };
    },
  },
  mounted() {
    this.getList();
  },
  methods: {
    /** 获取数据 */
    async getList() {
      const res = await listPubInformation(this.queryParams);
      if (res.code === 200) {
        this.newsList = res.rows;
      }
    },
    /** 查看详情 */
    goDetail(row) {
      let params = { id: row.id };
      this.$tab.openPage(row.title, "/news/information/i/detail/" + new Date().getTime(), params);
      addReadNum(row.id).then((res) => {
        if (res.code === 200) {
          row.readTotal += 1;
        }
      });
    },
    /** 查看更多 */
    loadMore() {
      this.$router.push("/news/information/i/list");
    },
  },
};
</script>

<style scoped lang="scss">
.news-container {
  height: calc(24vh - 10px);
  border-radius: 4px;
  border: 1px solid #f5f5f5;
  box-sizing: border-box;
  background-color: #ffffff;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  margin-bottom: 10px;
}
.position-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  font-weight: bold;
  border-bottom: 1px solid #f1f1f1;
  padding: 12px;
}

.load-more {
  font-size: 12px;
  color: #999;
  cursor: pointer;
  transition: color 0.3s;
  font-weight: 500;
  &:hover {
    color: #409eff;
  }
}

.news-flag {
  margin-right: 4px;
  width: 14px;
  height: 14px;
}

.news-body {
  flex: 1;
  overflow: hidden;
  padding: 5px 5px 0 5px;
}

.news-card {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  border-radius: 4px;
  padding-left: 12px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: #fff;
  position: relative;
  cursor: pointer;

  .news-title {
    font-size: 16px;
    font-weight: bold;
    text-shadow: 0 2px 5px rgba(0, 0, 0, 0.9);
    max-width: 280px;
    z-index: 10;
  }

  .info {
    font-size: 12px;
    margin-top: 12px;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.9);
    z-index: 10;

    span {
      margin-right: 10px;
    }
  }
}

::v-deep .el-empty {
  padding-top: 10px;
}
</style>

