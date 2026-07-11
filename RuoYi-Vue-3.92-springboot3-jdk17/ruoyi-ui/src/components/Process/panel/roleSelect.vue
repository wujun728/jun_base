<template>
  <!-- form表单：选角色 -->
  <div>
    <div class="role-select-trigger" :class="{ 'is-disabled': disabled }">
      <div v-if="selectedRoleList && selectedRoleList.length > 0" class="selected-role">
        <span v-for="(role, index) in selectedRoleList" :key="index" class="role-chip">
          <span class="ml5">{{ role.roleName }}</span>
          <span @click.stop="removeRole(index)">
            <el-icon name="close" class="remove-icon" />
          </span>
        </span>
      </div>
      <span v-else class="placeholder">
        <svg-icon icon-class="tree" class="mr5" />
        {{ placeholder }}
      </span>
    </div>

    <!-- 选角色 -->
    <el-dialog title="角色选择" :visible.sync="open" width="800px" append-to-body>
      <el-divider />
      <div class="select-box">
        <div class="select">
          <div class="select-content">
            <div class="content-box">
              <div class="header">
                <div class="check-box">
                  <svg-icon icon-class="user-list" />
                </div>
                <div class="content-title">角色列表</div>
                <div class="step-actions" v-if="type == 'multiple'">
                  <el-button type="text" size="small" @click="handleCheckedNodeAll(true)">全选</el-button>
                  <el-button type="text" size="small" @click="handleCheckedNodeAll(false)">取消全选</el-button>
                </div>
              </div>
              <div class="user-box">
                <div class="user-list">
                  <el-checkbox-group
                    ref="userCheckBoxs"
                    v-model="checkedRoles"
                    @change="handleCheckedChange"
                    :max="type == 'single' ? 1 : 2147483647"
                  >
                    <el-checkbox v-for="item in roleList" :label="item.roleId" :key="item.roleId" class="user-card">
                      <div class="user-card-content">
                        <div class="user-info">
                          <div class="user-name">{{ item.roleName }}</div>
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
                  已选择角色
                  <span class="selected-count">({{ selectedRoleList.length }}人)</span>
                </div>
                <div class="step-actions">
                  <el-button v-if="type == 'multiple'" type="text" size="small" @click="clearAll">清空</el-button>
                </div>
              </div>
              <div class="user-box">
                <div class="user-list">
                  <div v-for="item in selectedRoleList" :key="item.id" class="user-card">
                    <div class="user-card-content">
                      <div class="user-info">
                        <div class="user-name ml20">{{ item.roleName }}</div>
                      </div>
                      <i class="el-icon-delete pointer" @click="deleteRole(item.roleId)"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
  
<script>
import { listRole } from "@/api/system/role";

export default {
  props: {
    // 已选择的角色
    selectedRoles: [Array, String],
    multiple: {
      type: Boolean,
      default: false,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    placeholder: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      // 开关
      open: false,
      // 待选列表
      roleList: [],
      // 待选列表-选中的角色
      checkedRoles: [],
      // 已选中的角色
      selectedRoleList: [],
      // 所有角色map对象
      allDeptMap: {},
      // 选人类型：multiple-多选，single-单选
      type: "multiple",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 999,
        roleName: undefined,
      },
    };
  },
  watch: {
    selectedRoles: {
      handler(val) {
        if (val && val.length > 0) {
          this.selectedRoleList = val;
          this.checkedRoles = val.map((role) => role.roleId);
          // 处理已选择栏
          val.forEach((role) => {
            this.$set(this.allDeptMap, role.roleId, role);
          });
        } else {
          this.checkedRoles = [];
          this.selectedRoleList = [];
        }
      },
    },
    immediate: true,
    deep: true,
  },
  created() {
    this.getList();
  },
  methods: {
    /** 打开选人组件 */
    openSelect() {
      if (this.disabled) return;
      this.open = true;
    },
    /** 查询角色列表 */
    getList() {
      this.loading = true;
      listRole(this.queryParams).then((response) => {
        this.roleList = response.rows;
        this.roleList.forEach((role) => {
          this.$set(this.allDeptMap, role.roleId, role);
        });
        this.loading = false;
      });
    },
    /** 确认 */
    confirm() {
      this.open = false;
      this.$emit("input", JSON.parse(JSON.stringify(this.selectedRoleList)));
    },
    /** 取消 */
    cancel() {
      this.open = false;
    },
    /** 移除已选用户 */
    removeRole(index) {
      if (this.disabled) return;
      this.selectedRoleList.splice(index, 1);
      this.checkedRoles.splice(index, 1);
    },
    /** 删除已选 */
    deleteRole(id) {
      const index = this.checkedRoles.indexOf(id);
      if (index > -1) {
        this.checkedRoles.splice(index, 1);
        this.updateCheckedRoles();
      }
    },
    /** 清空 */
    clearAll() {
      this.selectedRoleList = [];
      this.checkedRoles = [];
      this.updateCheckedRoles();
    },
    /** 全选操作 */
    handleCheckedNodeAll(checked) {
      if (checked) {
        this.roleList.forEach((item) => {
          if (!this.checkedRoles.includes(item.roleId)) {
            this.checkedRoles.push(item.roleId);
          }
        });
      } else {
        this.checkedRoles = this.checkedRoles.filter((roleId) => !this.roleList.some((item) => item.roleId === roleId));
      }
      this.updateCheckedRoles();
    },
    /** 选择用户处理 */
    handleCheckedChange() {
      this.updateCheckedRoles();
    },
    /** 更新已选角色 */
    updateCheckedRoles() {
      this.selectedRoleList = this.checkedRoles.map((roleId) => this.allDeptMap[roleId]).filter(Boolean);
    },
  },
};
</script>
<style scoped lang="scss">
.role-select-trigger {
  min-height: 36px;
  padding: 4px 6px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;

  :hover {
    border-color: #409eff;
  }

  .selected-role {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 5px;
  }
  .role-chip {
    display: inline-flex;
    align-items: center;
    padding: 2px 5px;
    margin: 2px 5px;
    background: #f0f9eb;
    border-radius: 2px;
    font-size: 12px;
    height: 24px;
    line-height: 24px;
  }

  .remove-icon {
    margin-left: 6px;
    cursor: pointer;
    color: #999;
    font-size: 12px;
  }

  .remove-icon:hover {
    color: #f56c6c;
  }
}

.role-select-trigger.is-disabled {
  background-color: #f7f7f7;
  border-color: #f0f0f0;
  color: #606266;
  cursor: not-allowed;

  .role-chip {
    pointer-events: none;
  }

  .remove-icon {
    display: none;
  }
}

.placeholder {
  color: #c0c4cc;
  padding-left: 5px;
  height: 28px;
  line-height: 28px;
}
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