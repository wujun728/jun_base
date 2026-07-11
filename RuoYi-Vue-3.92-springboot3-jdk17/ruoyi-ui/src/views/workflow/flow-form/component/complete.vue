<template>
  <!--审批任务-->
  <div>
    <el-dialog :title="completeTitle" :visible.sync="completeOpen" width="800px" append-to-body>
      <el-divider />
      <el-form
        ref="taskForm"
        :model="formData"
        :rules="dynamicRules"
        class="content"
        v-loading="loading"
        element-loading-text="正在加载中..."
        element-loading-spinner="el-icon-loading"
      >
        <div class="approval-node-container">
          <el-form-item v-for="item in nextApprovers" :key="item.nodeId" :prop="'approvers.' + item.nodeId">
            <!-- 节点标题 -->
            <div class="node-header">
              <i class="el-icon-caret-bottom title-icon"></i>
              <span class="node-title">{{ item.nodeName }}</span>
            </div>
            <!-- 流程结束 -->
            <div v-if="item.dataType === 'finish'">
              <span>提交后，流程结束。</span>
            </div>
            <!-- 固定选人 -->
            <div v-else-if="item.dataType === 'fixed'" class="fixed-content">
              <div class="assignee-wrapper">
                <div class="assignee-label">
                  <i class="el-icon-user"></i>
                  <span>处理人：</span>
                </div>
                {{ receivers(item.assignees) }}
              </div>
            </div>
            <!-- 动态选人 -->
            <div v-else class="dynamic-content">
              <el-input
                v-model="formData.approvers[item.nodeId]"
                placeholder="请选择处理人"
                readonly
                class="user-select-input"
                @focus="openUserSelector(item)"
              >
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
        <el-button type="primary" @click="flowSubmit" :disabled="loading">确 定</el-button>
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
      :selectUserScope="selectUserScope"
      @confimUser="confimUser"
    />
  </div>
</template>

<script>
import FlowUser from "@/components/flow/User";
import { getNextFlowNode } from "@/api/workflow/task";
import { commonSubmit, checkCompleteCondition } from "@/api/workflow/process";
import UserSelect from "@/components/org/UserSelect/index.vue";
import UserAllSelect from "@/components/org/UserAllSelect/index.vue";

export default {
  name: "Complete",
  components: { FlowUser, UserSelect, UserAllSelect },
  props: {
    taskForm: {
      type: Object,
      default: () => {},
    },
    completeTitle: {
      type: String,
      default: "",
    },
    selectUserScope: {
      type: String,
      default: "corp",
    },
  },
  data() {
    return {
      loading: false,
      // 选人弹框
      openSelect: false,
      // 选人弹框类型，all-组织树，dept-部门，scope-指定人员
      openSelectType: "",
      // 审批弹框
      completeOpen: false,
      checkSendUser: false,
      // 会签节点
      multiInstance: false,
      // 下个审批节点
      nextApprovers: [],
      // 节点id
      nodeId: null,
      // 节点变量
      nodeVar: null,
      // 选人类型，single：单选
      selectType: "",
      // 是否指定审批人
      isFixed: false,
      // 当前选人框-已选用户map（按nodeId区分每个选人框）
      selectedUserMap: {},
      // 当前已选用户
      selectedUsers: null,
      // 当前可选用户（openSelectType === scope时，需要传递）
      userList: [],
      // 结构化存储每个节点的选择值
      formData: {
        approvers: {},
      },
      // 动态校验规则
      dynamicRules: {},
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
    /** 初始化节点 */
    initFlowNode() {
      this.loading = true;
      const params = { taskId: this.taskForm.taskId };
      this.completeOpen = true;
      getNextFlowNode(params).then((res) => {
        if (res.code == 200 && res.data) {
          this.nextApprovers = res.data;
          const initialValues = {};
          this.nextApprovers.forEach((item) => {
            if (item.dataType === "fixed") {
              this.isFixed = true;
              const users = item.assignees.map((u) => u.userId.toString());
              this.$set(this.taskForm.variables, item.vars, users.join(","));
            }
            initialValues[item.nodeId] = "";
          });
          // 生成校验规则
          this.dynamicRules = this.nextApprovers.reduce((acc, cur) => {
            if (cur.dataType !== "finish") {
              let prop = "approvers." + cur.nodeId;
              acc[prop] = [
                {
                  required: true,
                  message: "请选择处理人",
                  trigger: "change",
                },
              ];
            }
            return acc;
          }, {});
          this.formData.approvers = Object.assign({}, initialValues);

          // 重置校验状态
          this.$nextTick(() => {
            if (this.$refs.taskForm) {
              this.$refs.taskForm.resetFields();
            }
          });

          this.loading = false;
        }
      });
    },
    /** 选人 */
    openUserSelector(item) {
      this.openSelect = true;
      this.nodeId = item.nodeId;
      this.nodeVar = item.vars;
      this.multiInstance = item.type === "multiInstance";
      this.selectedUsers = this.selectedUserMap[this.nodeId] || [];
      this.openSelectType = this.getOpenSelectType(item);
      this.selectType = this.getSelectType(item);
    },
    /** 获取选人弹框类型 */
    getOpenSelectType(item) {
      if (item.dataType === "roles") {
        this.userList = item.assignees;
        return "scope";
      }
      return "all";
    },
    /** 获取选人类型 */
    getSelectType(item) {
      if (item.type === "assignee") {
        return "single";
      }
      return "multiple";
    },
    /** 选人确认 */
    confimUser(selection) {
      this.$set(this.selectedUserMap, this.nodeId, selection);
      this.openSelect = false;
      if (selection && selection.length > 0) {
        const selectVal = selection.map((item) => item.userId.toString());
        const selectUsers = selection.length > 1 ? selection.map((u) => u.nickName).join("、") : selection[0].nickName;
        this.$set(this.formData.approvers, this.nodeId, selectUsers);
        if (this.multiInstance) {
          this.$set(this.taskForm.variables, this.nodeVar, selectVal);
        } else {
          this.$set(this.taskForm.variables, this.nodeVar, selectVal.join(","));
        }
      } else {
        this.$set(this.formData.approvers, this.nodeId, "");
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
    /** 提交审批 */
    async flowSubmit() {
      const isValid = await this.checkFlowUser();
      if (!isValid) {
        this.$modal.msgError("请选择流程处理人！");
        return;
      }
      this.loading = true;
      let submitParams = {
        operateType: "200", //操作类型，提交、驳回、退回等
        flowTask: this.taskForm,
      };
      checkCompleteCondition(this.taskForm)
        .then((res) => {
          this.loading = false;
          if (res.data) {
            commonSubmit(submitParams)
              .then((response) => {
                this.$modal.msgSuccess(response.msg);
                this.loading = false;
                this.goBack();
              })
              .catch((this.loading = false));
          }
        })
        .catch((this.loading = false));
    },
    cancle() {
      this.loading = false;
      this.completeOpen = false;
      this.selectedUserMap = {};
      if (this.$refs.taskForm) {
        this.$refs.taskForm.resetFields();
      }
    },
    /** 返回页面 */
    goBack() {
      // 关闭当前标签页并返回上个页面
      const obj = { path: "/my/todo", query: { t: Date.now() } };
      this.$tab.closeOpenPage(obj);
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
