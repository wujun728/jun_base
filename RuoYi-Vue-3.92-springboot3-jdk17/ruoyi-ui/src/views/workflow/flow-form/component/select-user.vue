<template>
  <div>
    <!-- 选人 -->
    <el-dialog :title="selectUserTitle" :visible.sync="selectUserOpen" width="800px" append-to-body>
      <el-divider />
      <el-form
        ref="taskForm"
        :model="formData"
        class="content"
        v-loading="loading"
        element-loading-text="正在加载中..."
        element-loading-spinner="el-icon-loading"
      >
        <div class="approval-node-container">
          <el-form-item :rules="formRules.approvers">
            <!-- 动态选人 -->
            <div class="dynamic-content">
              <el-input v-model="formData.approvers" placeholder="请选择处理人" readonly class="user-select-input" @focus="openUserSelector()">
                <template #suffix>
                  <i class="el-icon-user-solid"></i>
                </template>
              </el-input>
            </div>
          </el-form-item>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancle">取 消</el-button>
        <el-button type="primary" @click="selectConfirm" :disabled="loading">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 指定可选人员，userList传入人员 -->
    <UserSelect
      v-if="openSelectType === 'scope'"
      :open.sync="openSelect"
      :type="selectType"
      :selectedUsers="selectedUsers"
      :userList="userList"
      @confimUser="confimUser"
    />
    <!-- 全部可选（组织树） -->
    <UserAllSelect
      v-if="openSelectType === 'all'"
      :open.sync="openSelect"
      :type="selectType"
      :selectedUsers="selectedUsers"
      @confimUser="confimUser"
    />
  </div>
</template>

<script>
import FlowUser from "@/components/flow/User";
import UserSelect from "@/components/org/UserSelect/index.vue";
import UserAllSelect from "@/components/org/UserAllSelect/index.vue";

export default {
  name: "selectUser",
  components: { FlowUser, UserSelect, UserAllSelect },
  props: {
    taskForm: {
      type: Object,
      default: () => {},
    },
    selectType: {
      type: String,
      default: "single",
    },
  },
  data() {
    return {
      loading: false,
      // 选人弹框
      openSelect: false,
      // 选人弹框类型，all-组织树，dept-部门，scope-指定人员
      openSelectType: "all",
      // 选人弹框
      selectUserOpen: false,
      selectUserTitle: "选人",
      checkSendUser: false,
      // 当前已选用户
      selectedUsers: [],
      // 当前可选用户（openSelectType === scope时，需要传递）
      userList: [],
      // 结构化存储每个节点的选择值
      formData: {
        approvers: "",
      },
      // 控制是否校验的开关
      needValidate: true,
      // 校验规则
      formRules: {
        approvers: [
          {
            required: this.needValidate,
            message: "请选择处理人",
            trigger: ["blur", "change"],
          },
        ],
      },
    };
  },
  computed: {
    // 接收人
    receivers() {
      return (val) => {
        if (!val?.length) return "";
        return val.length > 1 ? val.map((u) => u.nickName).join("、") : val[0].nickName;
      };
    },
  },
  methods: {
    /** 选人 */
    openUserSelector() {
      this.openSelect = true;
    },
    /** 选人确认 */
    confimUser(selection) {
      this.openSelect = false;
      this.$set(this.selectedUsers, selection);
      if (selection && selection.length > 0) {
        const selectVal = selection.map((item) => item.userId.toString()).join(",");
        const selectUsers = selection.length > 1 ? selection.map((u) => u.nickName).join("、") : selection[0].nickName;
        this.taskForm.assignee = selectVal;
        this.formData.approvers = selectUsers;
      }
    },
    /** 流程处理人校验 */
    checkFlowUser() {
      return new Promise((resolve) => {
        if (this.isFixed) {
          resolve(true);
        } else {
          this.$refs["taskForm"].validate((valid) => {
            resolve(valid);
          });
        }
      });
    },
    async selectConfirm() {
      const isValid = await this.checkFlowUser();
      if (!isValid) {
        this.$modal.msgError("请选择流程处理人！");
        return;
      }
      this.$emit("operfunc", this.taskForm);
      this.selectUserOpen = false;
    },
    cancle() {
      this.selectedUsers = [];
      this.selectUserOpen = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.content {
  min-height: 220px;
}

.approval-node-container {
  padding: 12px;
  border-radius: 5px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  }
}

.node-header {
  display: flex;
  align-items: center;
  border-bottom: 1px solid #eee;

  .title-icon {
    font-size: 14px;
    margin-right: 5px;
    color: #409eff;
  }

  .node-title {
    font-size: 14px;
    font-weight: 600;
  }
}

.fixed-content {
  padding-left: 20px;
  padding-top: 5px;
  .assignee-wrapper {
    display: flex;
    align-items: center;
    gap: 12px;

    .assignee-label {
      display: flex;
      align-items: center;
      color: #909399;
      font-size: 14px;
      i {
        margin-right: 6px;
        font-size: 16px;
      }
    }

    .assignee-tag {
      display: inline-flex;
      align-items: center;
      padding: 6px 12px;
      border-radius: 16px;
      background: #f5f7fa;

      .user-avatar {
        margin-right: 8px;
      }
    }
  }
}

.dynamic-content {
  .user-select-input {
    ::v-deep .el-input__inner {
      cursor: pointer;
      border: 1px solid #ebeef5;
      &:hover {
        border-color: #409eff;
      }
    }
  }
}
</style>
