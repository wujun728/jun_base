
<template>
  <!-- 资讯详情 -->
  <div class="app-container">
    <div class="notice-detail-container">
      <div v-if="!loading" class="notice-content">
        <h2 class="notice-title">{{ formData.title }}</h2>
        <div class="author-info">
          <span>作者：{{ formData.createBy }}</span>
          <span class="ml20">发布时间：{{ parseTime(formData.validStartTime, '{y}-{m}-{d}') }}</span>
        </div>
        <!-- 内容区域（HTML） -->
        <div class="html-content" v-html="formData.content"></div>
      </div>
      <div v-else class="loading">加载中...</div>
    </div>
  </div>
</template>

<script>
import { getInformation } from "@/api/information/information";

export default {
  name: "NewsDetail",
  data() {
    return {
      // 加载状态
      loading: true,
      id: this.$route.query.id,
      // 公告信息
      formData: {},
    };
  },
  mounted() {
    this.getInfo();
  },
  methods: {
    async getInfo() {
      const res = await getInformation(this.id);
      if (res.code === 200) {
        this.formData = res.data;
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped lang="scss">
.app-container {
  padding-top: 0;
}
.notice-detail-container {
  max-width: 960px;
  margin: 0 auto;
  padding: 24px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  font-family: "Microsoft YaHei", sans-serif;
  min-height: 88vh;

  .notice-title {
    text-align: center;
    font-size: 24px;
    font-weight: bold;
    color: #333;
  }

  .author-info {
    text-align: center;
    font-size: 14px;
    color: #999;
    margin-bottom: 30px;
  }

  .html-content {
    line-height: 1.8;
    font-size: 15px;
    color: #333;

    // 自动识别并美化嵌套的 HTML 内容
    h1,
    h2,
    h3,
    h4,
    h5,
    h6 {
      font-weight: bold;
      margin-top: 20px;
      margin-bottom: 10px;
    }

    p {
      margin-bottom: 16px;
    }

    img {
      max-width: 100%;
      height: auto;
      border-radius: 6px;
    }

    a {
      color: #409eff;
      text-decoration: none;

      &:hover {
        text-decoration: underline;
      }
    }

    ul,
    ol {
      padding-left: 20px;
      margin-bottom: 16px;
    }

    blockquote {
      border-left: 4px solid #409eff;
      padding-left: 16px;
      color: #666;
      margin: 16px 0;
    }
  }

  .loading {
    text-align: center;
    font-size: 14px;
    color: #999;
    padding: 40px 0;
  }
}
</style>