<template>
  <!-- 主题库 -->
  <div class="app-container">
    <div class="task-header">
      <div class="input-wrapper">
        <el-input
          v-model="queryParams.topicName"
          prefix-icon="el-icon-search"
          placeholder="请输入关键词，按回车搜索"
          clearable
          @clear="handleQuery"
          @keyup.enter.native="handleQuery"
        />
      </div>
      <div class="actions">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="addCategory">主题分类</el-button>
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="addTopic">创建主题</el-button>
      </div>
    </div>
    <div v-for="cate in topicList" :key="cate.id">
      <div class="category-main">
        <div>
          <i class="el-icon-caret-bottom"></i>
          <span>{{ cate.categoryName }} ( {{ cate.topicInfos.length }} )</span>
          <el-dropdown v-if="hasAuth(cate.createId)" size="mini" @command="(command) => handleCommand(command, cate)" @click.native.stop>
            <span class="dropdown-trigger">
              <svg-icon icon-class="more" />
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="editCategory" icon="el-icon-edit">修改</el-dropdown-item>
              <el-dropdown-item command="deleteCategory" icon="el-icon-delete">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
      <div class="kbs_item_wrapper">
        <div class="kbs_item" :span="4" v-for="(item, index) in cate.topicInfos" :key="index" @click="goDetail(item)">
          <div class="tip" :style="{ backgroundImage: 'url(' + backgroundImage(item.coverPicId) + ')' }">
            <div class="tip_content" :style="{ backgroundImage: 'url(' + backgroundImage(item.coverPicId) + ')' }">
              <div class="btn-actions">
                <span v-if="hasAuth(item.createId)" @click.stop="editTopic(item)">
                  <i class="el-icon-edit"></i>
                </span>
                <span v-if="hasAuth(item.createId)" @click.stop="delTopic(item)">
                  <i class="el-icon-delete"></i>
                </span>
              </div>
            </div>
          </div>
          <div class="name">
            <span>{{item.name}}</span>
          </div>
        </div>
      </div>
    </div>
    <el-empty v-if="emptyData" :image-size="100"></el-empty>
    <!-- 主题 -->
    <add-topic ref="topic" :open="openTopic" :title="topicTitle" :formData="topicForm" @topic-submit="topicSubmit" @topic-close="topicClose" />
    <!-- 分类 -->
    <topic-category
      :open="openCategory"
      :title="categoryTitle"
      :formData="categoryForm"
      @category-submit="categorySubmit"
      @category-close="categoryClose"
    />
  </div>
</template>

<script>
import { listByCategory, getTopicInfo, delInfo } from "@/api/kbs/topic/info";
import { getCategory, delCategory } from "@/api/kbs/topic/category";
import AddTopic from "./components/add-topic.vue";
import TopicCategory from "./components/category/index.vue";

export default {
  components: { AddTopic, TopicCategory },
  created() {
    this.getList();
  },
  data() {
    return {
      // 主题开关
      openTopic: false,
      // 主题弹框标题
      topicTitle: "",
      // 主题对象
      topicForm: {},
      // 分类开关
      openCategory: false,
      // 分类弹框标题
      categoryTitle: "",
      // 主题分类对象
      categoryForm: {},
      // 空数据
      emptyData: true,
      // 主题数据
      topicList: [],
      // 查询参数
      queryParams: {
        topicName: null,
      },
      userId: this.$store.state.user.userInfo.userId,
      baseApi: process.env.VUE_APP_BASE_API,
      // 默认的封面图
      defaultImg: require("@/assets/images/topic.jpg"),
    };
  },
  computed: {
    // 背景图片处理
    backgroundImage() {
      return (val) => {
        return !val ? this.defaultImg : this.baseApi + val;
      };
    },
    // 全选校验
    hasAuth() {
      return (val) => {
        if (!val) return false;
        return this.userId == val;
      };
    },
  },
  methods: {
    // 查询数据
    getList() {
      listByCategory(this.queryParams).then((res) => {
        if (res.code == 200 && res.data) {
          this.topicList = res.data;
          if (res.data.length > 0) {
            this.emptyData = false;
          }
        }
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 新增主题 */
    addTopic() {
      this.openTopic = true;
      this.topicTitle = "添加知识库主题";
    },
    /** 修改主题 */
    async editTopic(row) {
      this.openTopic = true;
      this.topicTitle = "修改知识库主题";
      const res = await getTopicInfo(row.id);
      if (res.code == 200) {
        this.topicForm = res.data;
      }
    },
    /** 删除主题 */
    delTopic(row) {
      this.$modal
        .confirm("确定要删除此主题吗？")
        .then(function () {
          return delInfo(row.id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /**新增分类 */
    addCategory() {
      this.openCategory = true;
      this.categoryTitle = "新增主题分类";
    },
    // 更多操作触发
    async handleCommand(command, row) {
      switch (command) {
        case "editCategory":
          this.editCategory(row.categoryId);
          break;
        case "deleteCategory":
          this.deleteCategory(row.categoryId);
          break;
        default:
          break;
      }
    },
    /** 编辑主题分类 */
    async editCategory(id) {
      this.openCategory = true;
      this.categoryTitle = "修改主题分类";
      const res = await getCategory(id);
      if (res.code == 200) {
        this.categoryForm = res.data;
      }
    },
    /** 删除分类 */
    deleteCategory(id) {
      this.$modal
        .confirm("确定要删除此分类吗？")
        .then(function () {
          return delCategory(id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 新增、修改提交 */
    topicSubmit() {
      this.openTopic = false;
      this.topicForm = null;
      this.getList();
    },
    /** 主题分类提交 */
    categorySubmit() {
      this.openCategory = false;
      this.getList();
      this.$refs.topic.getCategoryOpts();
    },
    /** 关闭弹框 */
    topicClose() {
      this.openTopic = false;
      this.topicForm = null;
    },
    /** 关闭分类弹框 */
    categoryClose() {
      this.openCategory = false;
    },
    /** 预览 */
    goDetail(row) {
      let params = { id: row.id, title: row.name, isTree: true };
      this.$tab.openPage("主题详情", "/kbs/detail", params);
    },
  },
};
</script>
 
<style lang = "scss" scoped>
.kbs_item_wrapper {
  display: flex;
  flex-wrap: wrap;
  gap: 32px;
}
.kbs_item {
  flex-shrink: 0;
  cursor: pointer;
}

.tip {
  width: 148px;
  height: 168px;
  border-radius: 2px;
  padding: 10px 5px 10px 5px;
  background-size: auto;
  position: relative;

  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: inherit;
    filter: blur(5px);
    z-index: 1;
  }
}
.tip_content {
  position: relative;
  background: no-repeat center center / cover;
  border-radius: 2px;
  width: 100%;
  height: 100%;
  z-index: 2;

  &:hover .btn-actions {
    display: flex;
  }
}
.name {
  position: relative;
  text-align: center;
  overflow: hidden;
  white-space: nowrap;
  word-break: break-word;
  margin-top: 10px;
  font-size: 14px;
  color: #303133;
  max-width: 148px;
  text-overflow: ellipsis;
}
.btn-actions {
  position: absolute;
  top: 4px;
  right: 6px;
  display: none;
  gap: 8px;
  z-index: 999;

  i {
    font-size: 16px;
    color: #606266;
    cursor: pointer;
    transition: color 0.2s ease;
  }

  i:hover {
    color: #409eff;
  }
}

.category-main {
  display: flex;
  font-weight: 600;
  font-size: 14px;
  align-items: center;
  color: #38699e;
  padding: 20px 0;
  margin: 0;
  span {
    padding-left: 10px;
    padding-right: 20px;
  }

  & span,
  i {
    cursor: pointer;
    user-select: none;
    color: #1c84c6;
  }
}

.task-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: nowrap;
  padding: 10px 0;

  .actions {
    white-space: nowrap;
  }

  .input-wrapper {
    min-width: 500px;
    display: flex;
    align-items: center;
    white-space: nowrap;
    overflow: hidden;
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