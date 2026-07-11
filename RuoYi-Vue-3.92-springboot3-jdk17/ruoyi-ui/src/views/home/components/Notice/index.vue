<template>
  <!-- 公司公告 -->
  <div class="notice-container">
    <div class="position-title">
      <span>
        <svg-icon icon-class="notice" class="notice-flag" />公告
      </span>
      <span class="load-more" @click="loadMore">
        更多
        <i class="el-icon-arrow-right" />
      </span>
    </div>
    <div class="notice-body">
      <div v-for="item in noticeList" :key="item.noticeId" @click="goDetail(item)" class="notice-item" :class="item.readFlag == '0' ? 'no-read' : ''">
        <span class="notice-title">
          <span class="notice-type">{{ noticeTypeDesc(item.noticeType) }}</span>
          <el-tooltip v-if="item.noticeTitle.length >= 16" class="item" effect="dark" :content="item.noticeTitle" placement="top">
            <span>{{ item.noticeTitle }}</span>
          </el-tooltip>
          <span v-else>{{ item.noticeTitle }}</span>
        </span>
        <span class="notice-date">{{ parseTime(item.validStartTime, '{y}-{m}-{d}') }}</span>
      </div>
      <el-empty v-if="!hasData" :image-size="50"></el-empty>
    </div>
  </div>
</template>

<script>
import { listHomeNotice, readNotice } from "@/api/system/notice";

export default {
  name: "NoticeModule",
  data() {
    return {
      // 是否有数据
      hasData: true,
      // 公告数据
      noticeList: [],
      // 默认查20条作为总数据，动态计算实际显示多少
      allNoticeList: [],
      // 总条数
      total: 0,
      // 公告查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        status: "0",
        noticeType: 2,
      },
      visibleCount: 10,
    };
  },
  computed: {
    noticeTypeDesc() {
      return (val) => {
        if (!val) return "";
        let desc = "";
        switch (val) {
          case "0":
            desc = "【公司】";
            break;
          case "1":
            desc = "【部门】";
            break;
        }
        return desc;
      };
    },
  },
  mounted() {
    /** 动态计算显示数量，需要调整参数：queryParams.pageSize改为20，visibleCount调整为10或者更大即可 */
    this._resizeHandler = this.debounce(() => {
      this.handleResize();
    }, 300);
    window.addEventListener("resize", this._resizeHandler);
    this.getList();
  },
  beforeDestroy() {
    window.removeEventListener("resize", this._resizeHandler);
  },
  methods: {
    /** 获取数据 */
    async getList() {
      const response = await listHomeNotice(this.queryParams);
      if (response.code === 200) {
        this.allNoticeList = response.rows;
        this.total = response.total;
        this.hasData = this.total > 0 ? true : false;
        this.noticeList = this.allNoticeList.slice(0, this.visibleCount);
        this.$nextTick(() => {
          this.handleResize();
        });
      }
    },
    debounce(func, delay) {
      let timer;
      return (...args) => {
        clearTimeout(timer);
        timer = setTimeout(() => func.apply(this, args), delay);
      };
    },
    /** 根据屏幕大小计算显示数量 */
    handleResize() {
      const container = document.querySelector(".notice-body");
      const item = document.querySelector(".notice-item");
      if (container && item) {
        const containerHeight = container.clientHeight;
        const itemHeight = item.offsetHeight;
        this.visibleCount = Math.floor(containerHeight / itemHeight);
        this.queryParams.pageSize = this.visibleCount;
        this.noticeList = this.allNoticeList.slice(0, this.visibleCount);
      } else {
        this.visibleCount = 5;
        this.queryParams.pageSize = 5;
        this.noticeList = this.allNoticeList.slice(0, 5);
      }
    },
    /** 查看详情 */
    goDetail(row) {
      // 设置为已读
      readNotice(row.noticeId);
      row.readFlag = "1";
      let params = { id: row.noticeId };
      this.$tab.openPage(row.noticeTitle, "/news/notice/n/detail/:t" + new Date().getTime(), params);
    },
    /** 查看更多 */
    loadMore() {
      this.$router.push("/news/notice/n/list");
    },
  },
};
</script>

<style scoped lang="scss">
.notice-container {
  flex-direction: column;
  display: flex;
  height: calc(36vh - 10px);
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

.notice-flag {
  margin-right: 3px;
  width: 15px;
  height: 15px;
}
.notice-body {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  transition: all 0.3s;
  background-color: #ffffff;
}

.notice-body:hover {
  white-space: normal;
  overflow: auto;
}

.notice-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  cursor: pointer;
  transition: background-color 0.3s;

  &:hover {
    background-color: #f5f7fa;
  }
  .notice-type {
    color: #1c84c6;
  }
  .notice-title {
    flex: 1;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-size: 14px;
    color: #333;
  }

  .notice-date {
    margin-left: 12px;
    font-size: 12px;
    color: #999;
    white-space: nowrap;
    margin-right: 5px;
  }
}
.no-read {
  font-weight: bold;
  position: relative;
  padding-right: 12px;

  &::after {
    content: "";
    position: absolute;
    top: 50%;
    right: 8px;
    transform: translateY(-50%);
    width: 5px;
    height: 5px;
    background-color: #ff4d4f;
    border-radius: 50%;
    box-shadow: 0 0 0 2px rgba(255, 77, 79, 0.3);
  }
}
</style>

