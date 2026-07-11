<template>
  <div>
    <div class="approval-node-container">
      <!-- 类型 -->
      <div class="node-header">
        <i class="el-icon-caret-bottom title-icon"></i>
        <span class="node-title">{{ itemName }}</span>
      </div>
      <el-form-item :rules="formRules.approvers">
        <!-- 动态选人 -->
        <div class="dynamic-content">
          <el-input v-model="formData.approvers" placeholder="请选择用户" readonly class="user-select-input" @focus="openUserSelector()">
            <template #suffix>
              <i class="el-icon-user-solid"></i>
            </template>
          </el-input>
        </div>
      </el-form-item>
    </div>
    <!-- 全部可选（组织树） -->
    <user-all-select :open.sync="openSelect" :type="selectType" :selectedUsers="selectedUsers" @confimUser="confimUser" />
  </div>
</template>

<script>
import UserAllSelect from "@/components/org/UserAllSelect/index.vue";

export default {
  name: "TopicAuthtUser",
  components: { UserAllSelect },
  props: {
    authUsers: {
      type: Array,
      default: () => [],
    },
    sysUsers: {
      type: Array,
      default: () => [],
    },
    authUserType: {
      type: String,
      default: "",
    },
    selectType: {
      // single-单选，multiple-多选
      type: String,
      default: "single",
    },
    itemName: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      // 选人弹框
      openSelect: false,
      // 当前已选用户
      selectedUsers: [],
      // 结构化存储每个节点的选择值
      formData: {
        approvers: "",
      },
      // 校验规则
      formRules: {
        approvers: [
          {
            required: true,
            message: "请选择用户",
            trigger: ["blur", "change"],
          },
        ],
      },
    };
  },
  watch: {
    sysUsers: {
      handler(newVal) {
        if (newVal && newVal.length > 0) {
          this.selectedUsers = newVal;
          this.formData.approvers = newVal.length > 0 ? (newVal.length > 1 ? newVal.map((u) => u.nickName).join("、") : newVal[0].nickName) : "";
        } else {
          this.selectedUsers = [];
          this.formData.approvers = "";
        }
      },
      immediate: true,
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
      this.selectedUsers = [...selection];
      let authUsers = [];
      if (selection && selection.length > 0) {
        this.formData.approvers = selection.length > 1 ? selection.map((u) => u.nickName).join("、") : selection[0].nickName;
        let i = 0;
        selection.forEach((element) => {
          authUsers.push({
            sort: ++i,
            userId: element.userId,
            name: element.nickName,
            type: this.authUserType,
            deptId: element.dept?.deptId,
            key: Date.now(),
          });
        });
      }
      this.$emit("auth-user-change", { type: this.authUserType, authUsers: authUsers, sysUsers: selection });
    },
  },
};
</script>

<style lang="scss" scoped>
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

::v-deep .el-input__inner {
  cursor: pointer;
  border: 1px solid #ebeef5;
  &:hover {
    border-color: #409eff;
  }
}
</style>
