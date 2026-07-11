<template>
  <!-- 选人组件-指定范围可选 -->
  <div>
    <el-dialog title="用户选择" :visible.sync="dialogVisible" :width="width || '800px'" append-to-body>
      <el-divider />
      <div class="select-box">
        <div class="select">
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
                <div class="user-list">
                  <el-checkbox-group
                    ref="userCheckBoxs"
                    v-model="checkedUsers"
                    @change="handleCheckedUsersChange"
                    :max="type == 'single' ? 1 : 2147483647"
                  >
                    <el-checkbox v-for="item in userList" :label="item.userName" :key="item.userId" class="user-card">
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
                </div>
              </div>
            </div>
          </div>
          <div class="trans">
            <i class="el-icon-d-arrow-right" />
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
                      <i class="el-icon-delete pointer" @click="deleteUserByUserName(item.userName)"></i>
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
export default {
  props: {
    // 待选用户列表
    userList: {
      type: Array,
      default: () => [],
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
  },
  data() {
    return {
      dialogVisible: this.open,
      // 部门名称
      deptName: "",
      // 待选用户列表-选中的用户
      checkedUsers: [],
      // 已选择用户列表
      selectedUserList: [],
      // 所有用户map对象
      allUserMap: {},
    };
  },
  watch: {
    open(newVal) {
      this.dialogVisible = newVal;
    },
    dialogVisible(newVal) {
      this.$emit("update:open", newVal);
    },
    deptName(val) {
      if (this.$refs.treeRef) {
        this.$refs.treeRef.filter(val);
      }
    },
    selectedUsers(newVal) {
      if (newVal && newVal.length > 0) {
        this.checkedUsers = newVal.map((user) => user.userName);
        this.updateCheckedUsers();
      } else {
        this.selectedUserList = [];
        this.checkedUsers = [];
      }
    },
  },
  computed: {
    // 接收人
    userDeptName() {
      return (dept) => {
        return dept ? dept.deptName : "";
      };
    },
  },
  mounted() {
    this.initUserList();
  },
  methods: {
    /** 选择用户处理 */
    handleCheckedUsersChange() {
      this.updateCheckedUsers();
    },
    /** 加载用户列表 */
    async initUserList() {
      // 处理用户数据
      this.allUserMap = this.userList.reduce((acc, user) => {
        acc[user.userName] = user;
        return acc;
      }, {});
      // 初始化选中用户
      if (this.selectedUsers && this.selectedUsers.length > 0) {
        this.checkedUsers = this.selectedUsers.filter((user) => user && user.userName).map((user) => user.userName);
      }
      this.updateCheckedUsers();
    },
    /** 全选操作 */
    handleCheckedNodeAll(checked) {
      if (checked) {
        this.userList.forEach((item) => {
          if (!this.checkedUsers.includes(item.userName)) {
            this.checkedUsers.push(item.userName);
          }
        });
      } else {
        this.checkedUsers = this.checkedUsers.filter((userName) => !this.userList.some((item) => item.userName === userName));
      }
      this.updateCheckedUsers();
    },
    /** 删除已选用户 */
    deleteUserByUserName(userName) {
      const index = this.checkedUsers.indexOf(userName);
      if (index > -1) {
        this.checkedUsers.splice(index, 1);
        this.updateCheckedUsers();
      }
    },
    /** 清空 */
    clearAll() {
      this.checkedUsers = [];
      this.updateCheckedUsers();
    },
    /** 确认 */
    submitForm() {
      this.$emit("confimUser", this.selectedUserList);
    },
    /** 取消 */
    cancel() {
      this.dialogVisible = false;
      this.$emit("cancel");
    },
    /** 更新已选用户 */
    updateCheckedUsers() {
      this.selectedUserList = this.checkedUsers.map((userName) => this.allUserMap[userName]).filter((user) => !!user);
    },
  },
};
</script>

<style lang="scss" scoped>
.select-box {
  width: 100%;
  min-width: 750px;
  display: flex;
  flex-direction: column;
  border: 1px solid rgb(235, 235, 235);
  padding: 12px;

  .select {
    width: 100%;
    display: flex;
    margin-top: 0;
    .select-content {
      width: 350px;
      height: 420px;
      margin-right: 12px;
      box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.12), 0 0 6px 0 rgba(0, 0, 0, 0.04);

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

  .trans {
    display: flex;
    align-items: center;
    margin-right: 20px;
    color: #409eff;
  }
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
  border-bottom: 1px solid #f0f0f0;
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

.user-list {
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 0;
}

.header {
  width: 100%;
  height: 40px;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  padding: 0 12px;
  position: relative;
  border-bottom: 1px solid #e4e7ed;

  .check-box {
    font-size: 14px;
    margin-right: 5px;
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

::v-deep .el-image {
  display: inherit;
  border-radius: 50px;
}
::v-deep .el-checkbox {
  height: auto;
}
::v-deep .el-checkbox__input {
  margin-right: 4px;
  padding-left: 6px;
}

::v-deep .el-checkbox__label {
  padding-left: 0;
  flex: 1;
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