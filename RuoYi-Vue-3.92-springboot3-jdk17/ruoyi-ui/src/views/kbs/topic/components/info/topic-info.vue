<template>
  <!-- 文档主题信息 -->
  <div v-if="!loading" class="topic-container" :style="{ backgroundImage: 'url(' + backgroundImage(topicInfo.coverPicId) + ')' }">
    <div class="topic-info">
      <div class="header">
        <div class="title">
          <svg-icon icon-class="Book" class="icon-tp"></svg-icon>
          <span>{{ topicInfo.name }}</span>
        </div>
        <div>
          <el-tooltip :content="topicInfo.favoriteFlag ? '取消收藏' : '点击收藏'" placement="top" :open-delay="200">
            <el-button v-if="topicInfo.favoriteFlag" plain size="small" @click="cancelFavorite">
              <i class="el-icon-star-on"></i>
              收藏
            </el-button>
            <el-button v-else plain size="small" @click="favorite">
              <i class="el-icon-star-off"></i>
              收藏
            </el-button>
          </el-tooltip>
        </div>
      </div>
      <!-- 文档统计信息 -->
      <div class="stats">
        <span>共</span>
        <span class="num">{{ topicInfo.docNums }}</span>
        <span>篇文档</span>
      </div>
      <div class="desc">
        <p>{{ topicInfo.remark }}</p>
      </div>
    </div>
    <div class="foot">
      <span>{{ wisdom }}</span>
    </div>
    <!-- 收藏 -->
    <favorite
      :topicId="topicId"
      :favoriteOpen="favoriteOpen"
      :favoriteObject="favoriteObject"
      @close-favorite="closeFavoriteDialog"
      @success-favorite="successFavoriteForm"
    />
  </div>
</template>
<script>
import Favorite from "./favorite.vue";
import { getAllInfo } from "@/api/kbs/topic/info";
import { cancelFavorite } from "@/api/kbs/favorite/favorite";

export default {
  components: {
    Favorite,
  },
  data() {
    return {
      loading: true,
      // 主题信息
      topicInfo: {},
      // 是否显示收藏组弹出框
      favoriteOpen: false,
      // 收藏文档对象
      favoriteObject: {},
      // 默认背景
      defaultImg: require("@/assets/images/topic.jpg"),
      baseApi: process.env.VUE_APP_BASE_API,
      wisdom: "-------- 成功的路上，处处潜伏着失败；你若顽强的走下去，终将拨云见日。",
    };
  },
  props: {
    topicId: {
      type: String,
      default: null,
    },
  },
  computed: {
    backgroundImage() {
      return (val) => {
        return !val ? this.defaultImg : this.baseApi + val;
      };
    },
  },
  mounted() {
    this.getTopicInfo();
  },
  methods: {
    /** 获取主题信息 */
    async getTopicInfo() {
      const res = await getAllInfo(this.topicId);
      if (res.code === 200) {
        this.topicInfo = res.data;
        this.favoriteObject = res.data;
        this.favoriteObject.type = "1";
        this.favoriteObject.rootObjectId = res.data.categoryId;
        this.favoriteObject.parentId = res.data.categoryId;
        this.$emit("update-auth", res.data.manageFlag || false);
        this.loading = false;
      }
    },
    // 关闭收藏弹框
    closeFavoriteDialog() {
      this.favoriteOpen = false;
    },
    // 收藏成功
    successFavoriteForm() {
      this.favoriteOpen = false;
      this.$set(this.topicInfo, "favoriteFlag", true);
    },
    /** 取消收藏 */
    cancelFavorite() {
      cancelFavorite(this.topicId).then((res) => {
        if (res.code == 200) {
          this.$set(this.topicInfo, "favoriteFlag", false);
        }
      });
    },
    /** 收藏主题 */
    favorite() {
      this.favoriteOpen = true;
    },
  },
};
</script>

<style scoped lang="scss">
.topic-container {
  max-width: 820px;
  margin: 0 auto;
  margin-top: 24px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  font-family: "Microsoft YaHei", sans-serif;
  min-height: 80vh;
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.topic-info {
  padding: 32px;
  max-width: 720px;
  .icon-tp {
    height: 24px;
    width: 24px;
    margin-right: 10px;
  }

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;

    .title {
      display: flex;
      align-items: center;
      font-size: 26px;
      font-weight: bold;
      color: #303133;
      margin: auto 0;
      text-shadow: 0 2px 5px #fff;
    }
  }

  .stats {
    color: #606266;
    font-size: 14px;
    margin-bottom: 20px;
    margin-left: 34px;
    .num {
      font-size: 18px;
      margin-right: 5px;
      margin-left: 5px;
      color: #303133;
      font-weight: bold;
      text-shadow: 0 2px 5px #fff;
    }
  }

  .desc {
    color: #999;
    margin-left: 34px;
    margin-top: 40px;
  }
}
.foot {
  font-size: 12px;
  height: 40px;
  margin-top: auto;
  padding-right: 12px;
  text-align: right;
  color: #999;
}
</style>