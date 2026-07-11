<template>
  <!-- 选人组件-组织树可选 -->
  <div>
    <el-dialog title="用户选择" :visible.sync="dialogVisible" :width="width || '1050px'" append-to-body>
      <el-divider />
      <div class="select-box">
        <div class="search-area" v-show="showSearch">
          <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="0" @submit.native.prevent>
            <el-row :gutter="24">
              <el-col :span="5">
                <el-form-item prop="nickName">
                  <el-input
                    v-model="queryParams.nickName"
                    size="small"
                    prefix-icon="el-icon-user"
                    style="width:200px"
                    placeholder="请输入用户姓名"
                    clearable
                    @keyup.enter.native="handleQuery"
                    @clear="handleQuery"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item>
                  <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
                  <el-button size="mini" @click="resetQuery">重置</el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <div class="select">
          <div class="select-content">
            <div class="content-box">
              <div class="header">
                <div class="check-box">
                  <svg-icon icon-class="tree" />
                </div>
                <div class="content-title">组织机构</div>
              </div>
              <div class="content-list">
                <div class="search-input">
                  <el-input v-model="deptName" prefix-icon="el-icon-search" size="small" placeholder="请输入组织名称" clearable />
                </div>
                <div class="tree-box">
                  <el-tree
                    ref="treeRef"
                    :data="treeOptions"
                    :props="defaultProps"
                    :expand-on-click-node="false"
                    :filter-node-method="filterNode"
                    :default-expanded-keys="defaultExpandedKeys"
                    node-key="id"
                    highlight-current
                    accordion
                    @node-click="handleNodeClick"
                  />
                </div>
              </div>
            </div>
          </div>
          <div class="select-content">
            <div class="content-box">
              <div class="header">
                <div class="check-box">
                  <svg-icon icon-class="user-list" />
                </div>
                <div class="content-title">用户列表</div>
                <div class="step-actions" v-if="type == 'multiple'">
                  <el-button type="text" size="small" @click="handleCheckedNodeAll(true)">全选</el-button>
                  <el-button type="text" size="small" @click="handleCheckedNodeAll(false)">取消全选</el-button>
                </div>
              </div>
              <div class="user-box">
                <div class="user-list" v-infinite-scroll="loadMore" :infinite-scroll-disabled="loading" :infinite-scroll-distance="10">
                  <el-checkbox-group
                    ref="userCheckBoxs"
                    v-model="checkedUsers"
                    @change="handleCheckedUsersChange"
                    :max="type == 'single' ? 1 : 999999"
                  >
                    <el-checkbox v-for="item in userList" :label="item.userId" :key="item.userId" class="user-card">
                      <div class="user-card-content">
                        <div class="avatar">
                          <image-preview :src="item.avatar" :width="32" :height="32" />
                        </div>
                        <div class="user-info">
                          <div class="user-name">{{ item.nickName }}</div>
                          <div class="user-detail">
                            <span class="dept-name">{{ userDeptName(item.dept) }}</span>
                          </div>
                        </div>
                      </div>
                    </el-checkbox>
                  </el-checkbox-group>
                  <!-- 加载更多提示 -->
                  <div v-if="loading" class="loading-more">
                    <i class="is-loading"></i>
                    <span>加载中...</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="select-content">
            <div class="content-box">
              <div class="header">
                <div class="check-box">
                  <svg-icon icon-class="selected-user" />
                </div>
                <div class="content-title">
                  已选择用户
                  <span class="selected-count">({{ selectedUserList.length }}人)</span>
                </div>
                <div class="step-actions">
                  <el-button v-if="type == 'multiple'" type="text" size="small" @click="clearAll">清空</el-button>
                </div>
              </div>
              <div class="user-box">
                <div class="user-list">
                  <div v-for="item in selectedUserList" :key="item.id" class="user-card">
                    <div class="user-card-content">
                      <div class="avatar">
                        <image-preview :src="item.avatar" :width="32" :height="32" />
                      </div>
                      <div class="user-info">
                        <div class="user-name">{{ item.nickName }}</div>
                        <div class="user-detail">
                          <span class="dept-name">{{ userDeptName(item.dept) }}</span>
                        </div>
                      </div>
                      <i class="el-icon-delete pointer" @click="deleteUserByUserName(item.userId)"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
  
<script>
import { listDeptUser, deptTreeSelect, corpTree } from "@/api/system/user";

export default {
  name: "UserAllSelect",
  props: {
    // 是否显示用户搜索
    showSearch: {
      type: Boolean,
      default: true,
    },
    // 初始化已选择用户
    selectedUsers: {
      type: Array,
      default: () => [],
    },
    // 弹框宽度
    width: String,
    // 弹框高度
    height: String,
    // 选人类型，单选：single，多选：multiple
    type: {
      type: String,
      default: "single",
    },
    open: {
      type: Boolean,
      default: false,
    },
    selectUserScope: {
      type: String,
      default: "corp",
    },
  },
  data() {
    return {
      loading: false,
      dialogVisible: this.open,
      // 树节点默认展开的节点id（根据实际根节点的id调整）
      defaultExpandedKeys: [],
      // 组织树选项数据
      treeOptions: [],
      // 部门名称
      deptName: "",
      // 待选用户列表
      userList: [],
      // 待选用户列表-选中的用户
      checkedUsers: [],
      // 已选择用户列表
      selectedUserList: [],
      // 所有用户map对象
      allUserMap: {},
      // 搜索参数
      queryParams: {
        pageNum: 1,
        pageSize: 999, // 全选时，为了避免只加载一页时，未能全部选中，把每页的条数设置大些
        userName: null,
        deptId: null,
      },
      // 用户总数
      total: 0,
      // 是否更多
      noMore: false,
      // el-tree配置选项
      defaultProps: {
        children: "children",
        label: "label",
      },
      userDept: this.$store.state.user.userInfo.dept,
    };
  },
  watch: {
    open(newVal) {
      this.dialogVisible = newVal;
      if (newVal && this.selectedUsers && this.selectedUsers.length > 0) {
        this.checkedUsers = this.selectedUsers.map((user) => user.userId);
        this.selectedUsers.forEach((user) => {
          this.$set(this.allUserMap, user.userId, user);
        });
        this.updateCheckedUsers();
      } else {
        this.checkedUsers = [];
        this.selectedUserList = [];
      }
    },
    dialogVisible(newVal) {
      this.$emit("update:open", newVal);
    },
    deptName(val) {
      if (this.$refs.treeRef) {
        this.$refs.treeRef.filter(val);
      }
    },
    deep: true,
    immediate: true,
  },
  computed: {
    // 接收人
    userDeptName() {
      return (dept) => {
        return dept ? dept.deptName : "";
      };
    },
  },
  created() {
    this.getTree();
    this.initDeptId();
  },
  methods: {
    /** 获取组织树 */
    async getTree() {
      try {
        if (this.selectUserScope === "corp") {
          corpTree().then((response) => {
            this.treeOptions = response.data;
          });
        } else {
          deptTreeSelect().then((response) => {
            this.treeOptions = response.data;
          });
        }
      } catch (error) {
        console.error("获取部门树失败:", error);
        this.treeOptions = [];
      }
    },
    /** 初始化部门id */
    initDeptId() {
      if (this.selectUserScope === "corp") {
        this.queryParams.deptId = this.userDept && this.userDept.parentId ? this.userDept.parentId : this.userDept.deptId;
        this.defaultExpandedKeys.push(this.queryParams.deptId);
      } else {
        this.queryParams.deptId = this.userDept.deptId || null;
      }
    },
    /** 组织树节点点击事件 */
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.handleQuery();
    },
    /** 选择用户处理 */
    handleCheckedUsersChange() {
      this.checkedUsers = [...this.checkedUsers];
      this.updateCheckedUsers();
    },
    /** 搜索 */
    handleQuery() {
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.resetForm("queryForm");
      this.getList();
    },
    /** 获取用户列表 */
    getList() {
      this.queryParams.pageNum = 1;
      this.noMore = false;
      this.loadMore(false);
    },
    /** 加载用户列表 */
    async loadMore(isLoadMore = true) {
      if (this.loading || (this.noMore && isLoadMore)) return;
      try {
        this.loading = true;
        const { rows, total } = await listDeptUser(this.queryParams);
        if (rows.length === 0) {
          if (isLoadMore) {
            this.noMore = true;
          } else {
            this.userList = [];
          }
          return;
        }
        // 处理分页逻辑
        if (isLoadMore) {
          this.userList = [...this.userList, ...rows];
        } else {
          this.userList = rows;
        }
        // 更新 allUserMap（仅当前页）
        rows.forEach((user) => {
          this.$set(this.allUserMap, user.userId, user);
        });
        this.updateCheckedUsers();
        this.total = total;
        this.queryParams.pageNum++;
        // 判断是否还有更多数据
        if (this.userList.length >= total) {
          this.noMore = true;
        }
      } catch (err) {
        this.$message.error("加载常用意见失败");
      } finally {
        this.loading = false;
      }
    },
    /** 全选操作 */
    handleCheckedNodeAll(checked) {
      if (checked) {
        const newUsers = this.userList.map((item) => item.userId).filter((id) => !this.checkedUsers.includes(id));
        this.checkedUsers = [...this.checkedUsers, ...newUsers];
      } else {
        const currentIds = this.userList.map((item) => item.userId);
        this.checkedUsers = this.checkedUsers.filter((id) => !currentIds.includes(id));
      }
      this.updateCheckedUsers();
    },
    /** 删除已选用户 */
    deleteUserByUserName(userId) {
      this.checkedUsers = this.checkedUsers.filter((id) => id !== userId);
      this.updateCheckedUsers();
    },
    /** 清空 */
    clearAll() {
      this.checkedUsers = [];
      this.updateCheckedUsers();
    },
    /** 确认 */
    submitForm() {
      this.$emit("confimUser", this.selectedUserList);
      this.dialogVisible = false;
    },
    /** 取消 */
    cancel() {
      this.dialogVisible = false;
      this.$emit("cancel");
    },
    /** 过滤节点 */
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    /** 更新已选用户 */
    updateCheckedUsers() {
      this.selectedUserList = this.checkedUsers.map((userId) => this.allUserMap[userId]).filter((user) => Boolean(user));
    },
  },
};
</script>

<style lang="scss" scoped>
.select-box {
  width: 100%;
  min-width: 1000px;
  display: flex;
  flex-direction: column;
  border: 1px solid rgb(235, 235, 235);
  padding: 12px;

  .select {
    width: 100%;
    display: flex;
    margin-top: 0;

    .select-content {
      width: 320px;
      height: 420px;
      margin-right: 12px;
      background: white;
      border-radius: 4px;
      border: 1px solid #f5f5f5;
      overflow: hidden;
      box-shadow: 0 3px 6px 0 rgba(0, 0, 0, 0.09), 0 0 3px 0 rgba(0, 0, 0, 0.04);

      &:last-child {
        margin-right: 0;
      }

      .content-box {
        height: 100%;
        display: flex;
        flex-direction: column;
      }
    }
  }
}

.tree-box {
  width: 100%;
  height: 300px;
  overflow-x: hidden;
  overflow-y: auto;
  flex: 1;
  overflow: auto;
  background-color: white;
  border-radius: 4px;
}

.user-box {
  width: 100%;
  height: 370px;
  display: flex;
  flex-direction: column;

  .user-list {
    flex: 1;
    overflow-x: hidden;
    overflow-y: auto;
    height: 370px;
  }
}

.user-card {
  display: flex;
  align-items: center;
  padding: 8px;
  width: 100%;
  border-bottom: 1px solid #f5f5f5;
  transition: all 0.3s;

  &:hover {
    background-color: #f5f7fa;
  }

  .user-card-content {
    width: 100%;
    padding: 4px;
    position: relative;
    display: flex;
    align-items: center;

    .avatar {
      flex-shrink: 0;
      margin-right: 12px;
    }

    .user-info {
      flex: 1;
      min-width: 0;

      .user-name {
        font-size: 13px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 2px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      .user-detail {
        display: flex;
        align-items: center;
        font-size: 12px;
        color: #909399;
        line-height: 1.4;

        .dept-name {
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
    }
  }
}

.loading-more {
  text-align: center;
  padding: 10px 0;
  color: #909399;
  font-size: 12px;
}

.header {
  width: 100%;
  height: 40px;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  padding: 0 12px;
  position: relative;
  border-bottom: 1px solid #f5f5f5;

  .check-box {
    font-size: 14px;
    margin-right: 3px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .content-title {
    font-size: 14px;
    font-weight: 500;
    color: #303133;
    display: flex;
    align-items: center;
    gap: 4px;

    .selected-count {
      color: #409eff;
      font-size: 13px;
      font-weight: normal;
    }
  }

  .step-actions {
    position: absolute;
    right: 12px;
    display: flex;
    flex-wrap: wrap;
    align-content: flex-end;
    justify-content: space-between;
    align-items: center;
  }
}

.content-list {
  padding: 12px;
  height: calc(100% - 40px);
  display: flex;
  flex-direction: column;
}

.search-area {
  background-color: #fff;
  padding-left: 6px;
  padding-top: 6px;
  height: 50px;
  border-radius: 1px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  margin-bottom: 12px;
}

.search-input {
  margin-bottom: 12px;
  width: 100%;
}
::v-deep .el-tree {
  .el-tree-node__content {
    height: 32px;

    &:hover {
      background-color: #f5f7fa;
    }
  }
}
::v-deep .el-checkbox__input {
  margin-right: 4px;
  padding-left: 6px;
}

::v-deep .el-checkbox__label {
  padding-left: 0;
  flex: 1;
}
::v-deep .el-checkbox {
  height: auto;
}
::v-deep .el-image {
  display: inherit;
  border-radius: 50px;
}
::-webkit-scrollbar {
  width: 4px;
}

::-webkit-scrollbar-thumb {
  background-color: #dcdfe6;
  border-radius: 3px;
}

::-webkit-scrollbar-track {
  background-color: #f5f7fa;
}
</style>