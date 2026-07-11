<template>
  <!-- 我的收藏 -->
  <div class="container">
    <div class="search-bar">
      <div class="input-wrapper">
        <el-input
          v-model="title"
          prefix-icon="el-icon-search"
          placeholder="请输入关键词，按回车搜索"
          clearable
          @clear="handleQuery"
          @keyup.enter.native="handleQuery"
        />
      </div>
      <el-button plain icon="el-icon-refresh" size="mini" @click="resetQuery">刷新</el-button>
    </div>
    <div class="body">
      <el-collapse v-model="activeNames">
        <el-collapse-item :name="item.groupId" v-for="item in favoriteGroup" :key="item.gtoupId">
          <template slot="title">
            <div class="collapse-header">
              <div class="name">
                <span class="mr10">{{ item.groupName }}</span>
                <el-dropdown size="mini" @command="(command) => handleCommand(command, item)" @click.native.stop>
                  <span class="dropdown-trigger">
                    <svg-icon icon-class="more" />
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="edit" icon="el-icon-edit">修改</el-dropdown-item>
                    <el-dropdown-item command="delete" icon="el-icon-delete">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
              <div class="footer">
                <span>最近更新：{{ lastTime(item.favorites) }}</span>
                <span class="ml20">收藏（ {{ item.favorites.length }} ）</span>
              </div>
            </div>
          </template>
          <div class="favorites-list">
            <div v-for="line in item.favorites" :key="line.id" class="favorite-item" @click="goDetail(line)">
              <div class="item-content">
                <span class="item-type" :style="{'background-color': line.objectType === '1' ? '#409eff' : '#E6A23C'}">{{ typeDesc(line.objectType) }}</span>
                <span class="item-title">{{ line.objectName }}</span>
              </div>
              <div class="item-actions">
                <el-button type="danger" circle plain icon="el-icon-delete" size="mini" @click.stop="handleDelete(line)" />
              </div>
            </div>
          </div>
        </el-collapse-item>
      </el-collapse>
      <el-empty v-if="favoriteGroup.length === 0" :image-size="100" description="暂无数据" />
    </div>

    <!-- 收藏组修改弹框 -->
    <el-dialog title="修改分组" :visible.sync="open" width="600px" append-to-body>
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="68px">
        <el-form-item label="组名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入组名称" />
        </el-form-item>
        <el-form-item label="排序号" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序号" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFavorite, delFavorite } from "@/api/kbs/favorite/favorite";
import { getGroup, delGroup, updateGroup } from "@/api/kbs/favorite/group";
import { parseTime } from "@/utils/ruoyi";

export default {
  name: "Favorite",
  dicts: ["t_kbs_favorite_type"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 标题
      title: null,
      // 展开的分组
      activeNames: [],
      // 收藏数据
      favoriteGroup: [],
      // 收藏组弹框
      open: false,
      // 收藏组对象
      form: {},
      // 表单校验
      rules: {
        sort: [{ required: true, message: "排序号不能为空", trigger: "blur" }],
        name: [{ required: true, message: "组名称不能为空", trigger: "blur" }],
      },
    };
  },
  computed: {
    lastTime() {
      return (val) => {
        if (!val || val.length <= 0) return "无";
        const time = val[0].createTime;
        return this.formatTime(time);
      };
    },
    typeDesc() {
      return (val) => {
        return val === "1" ? "主题" : "文档";
      };
    },
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询知识库收藏列表 */
    getList() {
      this.loading = true;
      listFavorite({ objectName: this.title }).then((response) => {
        this.favoriteGroup = response.data;
        this.loading = false;
      });
    },
    /** 处理更新时间 */
    formatTime(date) {
      const now = new Date();
      const inputDate = new Date(date);
      const diffYears = now.getFullYear() - inputDate.getFullYear();
      // 同一年内
      if (diffYears === 0) {
        // 同一个月
        if (inputDate.getMonth() === now.getMonth()) {
          return parseTime(date, "{m}-{d} {h}:{i}");
        } else {
          const monthsAgo = now.getMonth() - inputDate.getMonth();
          return `${monthsAgo}个月前`;
        }
      }
      // 超过一年
      else {
        const yearsAgo = now.getFullYear() - inputDate.getFullYear();
        return `${yearsAgo}年前`;
      }
    },
    /** 查看详情 */
    goDetail(row) {
      let isTree = false;
      let docId = row.objectId;
      if (row.objectType === "1") {
        isTree = true;
        docId = null;
      }
      let params = { id: row.objectId, docId: docId, title: row.objectName, isTree: isTree };
      this.$tab.openPage("主题详情", "/kbs/detail", params);
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.title = null;
      this.handleQuery();
    },
    // 更多操作触发
    async handleCommand(command, row) {
      switch (command) {
        case "edit":
          this.edit(row.groupId);
          break;
        case "delete":
          this.delete(row.groupId);
          break;
        default:
          break;
      }
    },
    /** 编辑分组 */
    edit(id) {
      getGroup(id).then((response) => {
        this.form = response.data;
        this.open = true;
      });
    },
    /** 删除分组（同步删除其下所有收藏） */
    delete(id) {
      this.$modal
        .confirm("确定要删除此收藏组吗？")
        .then(function () {
          return delGroup(id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 提交 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          updateGroup(this.form).then((response) => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
    /** 取消提交 */
    cancel() {
      this.open = false;
      this.form = {};
    },
    /** 取消收藏按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const name = row.objectName;
      this.$modal
        .confirm("是否确认取消此收藏？")
        .then(function () {
          return delFavorite(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("取消成功");
        })
        .catch(() => {});
    },
  },
};
</script>
<style scoped lang="scss">
.container {
  padding: 10px 0;
}
.search-bar {
  height: 50px;
  background-color: #fff;
  z-index: 1000;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.06);
  padding: 10px 15px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.input-wrapper {
  position: relative;
  max-width: 600px;
}
.body {
  margin-top: 5px;
  max-height: calc(88vh - 88px);
  overflow-y: auto;
  padding: 16px 20px;
}

.collapse-header {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  flex: 1;
  text-align: left;
}

.name,
.footer {
  display: block;
  line-height: 20px;
  padding: 5px 0;
}

.name {
  font-size: 18px;
  color: #303133;
}

.footer {
  margin-top: 10px;
  color: #909399;
}

.favorites-list {
  padding: 10px 20px;
}

.favorite-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  color: #303133;
  padding: 12px 10px;
  border-bottom: 1px solid #f9f9f9;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: pointer;

  &:hover {
    color: #409eff;
    background-color: #f9f9f9;
    .item-actions {
      opacity: 1;
      visibility: visible;
    }
  }

  .item-content {
    display: flex;
    align-items: center;
    gap: 10px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .item-type {
    font-size: 12px;
    color: #fff;
    padding: 2px 8px;
    border-radius: 4px;
    flex-shrink: 0;
  }

  .item-title {
    font-size: 14px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    max-width: 300px;
  }

  .item-actions {
    flex-shrink: 0;
    opacity: 0;
    visibility: hidden;
    transition: opacity 0.3s ease, visibility 0.3s ease;
  }
}

::v-deep .input-wrapper .el-input input {
  border: none;
  border-bottom: 1px solid #e4e4e4;
  border-radius: 0;
  font-size: 14px;
  width: 100%;
  min-width: 500px;
}

::v-deep .el-collapse {
  border-top: none;
  border-bottom: none;
}
::v-deep .el-collapse-item__header {
  height: 100px;
  padding-left: 10px;
  border-bottom: none;
}

::v-deep .el-collapse-item {
  border-top: none !important;
}

::v-deep .el-collapse-item__wrap {
  color: #303133;
  border-bottom: none;
}
</style>