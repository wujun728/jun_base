<template>
  <div>
    <el-form label-width="82px" size="small" :rules="rules">
      <!-- <el-form-item label="异步">
        <el-switch v-model="bpmnFormData.async" active-text="是" inactive-text="否" @change="updateElementTask('async')"/>
      </el-form-item>-->
      <el-form-item label="参与方式" prop="partType">
        <el-select v-model="bpmnFormData.userType" placeholder="选择人员" @change="updateUserType">
          <el-option v-for="item in userTypeOption" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="bpmnFormData.userType === 'assignee'" prop="partUser">
        <span slot="label">
          <el-tooltip content="从下方按钮选择" placement="top">
            <i class="el-icon-question"></i>
          </el-tooltip>参与者
        </span>
        <user-select
          v-if="partSelectType === '0'"
          ref="users"
          v-model="bpmnFormData.assignee"
          :checkedUsers="selectedUsers"
          :multiple="false"
          :placeholder="'请选择审批人'"
          @input="confirmUser"
        />
        <role-select
          v-else-if="partSelectType === '1'"
          ref="roles"
          v-model="bpmnFormData.candidateUsers"
          :selectedRoles="selectedRoles"
          :multiple="false"
          :placeholder="'请选择角色'"
          @input="confirmRole"
        />
        <el-input-tag v-else-if="3" v-model="bpmnFormData.assignee" />
        <el-button-group class="ml-4" style="margin-top: 4px">
          <!--指定人员-->
          <el-tooltip class="box-item" effect="dark" content="从组织机构中选择固定人员作为审批人" placement="bottom">
            <el-button size="mini" type="primary" icon="el-icon-user" @click="openUserSelect">选择人员</el-button>
          </el-tooltip>
          <!--选择表达式-->
          <el-tooltip class="box-item" effect="dark" content="通过表达式，控制审批人员" placement="bottom">
            <el-button size="mini" type="warning" icon="el-icon-postcard" @click="singleExpCheck">表达式</el-button>
          </el-tooltip>
        </el-button-group>
      </el-form-item>

      <el-form-item v-else-if="bpmnFormData.userType === 'candidateUsers'" prop="partUser">
        <span slot="label">
          <el-tooltip content="从下方按钮选择" placement="top">
            <i class="el-icon-question"></i>
          </el-tooltip>参与者
        </span>
        <user-select
          v-if="partSelectType === '0'"
          ref="users"
          v-model="bpmnFormData.candidateUsers"
          :checkedUsers="selectedUsers"
          :multiple="true"
          :placeholder="'请选择审批人'"
          @input="confirmUser"
        />
        <role-select
          v-else-if="partSelectType === '1'"
          ref="roles"
          v-model="bpmnFormData.candidateUsers"
          :selectedRoles="selectedRoles"
          :multiple="true"
          :placeholder="'请选择角色'"
          @input="confirmRole"
        />
        <el-tag v-else-if="2" v-model="bpmnFormData.candidateUsers" type="warning">{{ bpmnFormData.candidateUsers }}</el-tag>
        <el-button-group class="ml-4" style="margin-top: 4px">
          <!--候选人员-->
          <el-tooltip class="box-item" effect="dark" content="从组织机构中选择固定人员作为审批人" placement="bottom">
            <el-button size="mini" type="primary" icon="el-icon-user" @click="openUserSelect">选择人员</el-button>
          </el-tooltip>
          <!-- 候选角色 -->
          <el-tooltip class="box-item" effect="dark" content="通过角色控制审批人" placement="bottom">
            <el-button size="mini" type="success" icon="el-icon-user" @click="openRoleSelect">选择角色</el-button>
          </el-tooltip>
          <!--选择表达式-->
          <el-tooltip class="box-item" effect="dark" content="在流程审批过程中，通过组织机构选人组件，动态确定审批人" placement="bottom">
            <el-button size="mini" type="warning" icon="el-icon-postcard" @click="defaultExpCheck">动态选人</el-button>
          </el-tooltip>
        </el-button-group>
      </el-form-item>
      <!-- <el-form-item label="优先级">
        <el-input v-model="bpmnFormData.priority" @change="updateElementTask('priority')" />
      </el-form-item>-->
      <el-form-item label="到期时间">
        <el-input v-model="bpmnFormData.dueDate" @change="updateElementTask('dueDate')" />
      </el-form-item>
    </el-form>

    <!--选择表达式-->
    <el-dialog title="选择表达式" :visible.sync="expVisible" width="60%" :close-on-press-escape="false" :show-close="false">
      <el-divider />
      <flow-exp v-if="expVisible" :selectValues="selectData.exp" @handleSingleExpSelect="expSelect"></flow-exp>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="expVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="checkExpComplete">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import FlowExp from "@/components/flow/Expression";
import ElInputTag from "@/components/flow/ElInputTag";
import { StrUtil } from "@/utils/StrUtil";
import UserSelect from "./userSelect.vue";
import RoleSelect from "./roleSelect.vue";

export default {
  name: "TaskPanel",
  components: {
    FlowExp,
    ElInputTag,
    UserSelect,
    RoleSelect,
  },
  /** 组件传值  */
  props: {
    id: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      roleVisible: false,
      expVisible: false,
      isIndeterminate: true,
      userType: "",
      userTypeOption: [
        { label: "固定人员", value: "assignee" },
        { label: "候选人员", value: "candidateUsers" },
        // { label: "候选角色", value: "candidateGroups" },
      ],
      bpmnFormData: {
        userType: "",
        assignee: "",
        candidateUsers: "",
        // candidateGroups: "",
        dueDate: "",
        priority: "",
        dataType: "",
        expId: "",
      },
      // 数据回显
      selectData: {
        assignee: null,
        candidateUsers: null,
        // candidateGroups: null,
        exp: null,
      },
      otherExtensionList: [],
      // 多实例状态控制
      multipleFlag: false,
      rules: {
        partType: [{ required: true, message: "参与方式不能为空", trigger: "blur" }],
        partUser: [{ required: true, message: "参与者不能为空", trigger: "blur" }],
      },
      partSelectType: "0", // 0-选人，1-选角色，2-动态
      // 当前已选中用户
      selectedUsers: [],
      // 当前已选中的角色
      selectedRoles: [],
    };
  },

  /** 传值监听 */
  watch: {
    id: {
      handler(newVal) {
        if (StrUtil.isNotBlank(newVal)) {
          this.resetTaskForm();
        }
      },
      immediate: true, // 立即生效
    },
  },
  created() {},
  methods: {
    // 初始化表单
    resetTaskForm() {
      this.selectedUsers = [];
      this.selectedRoles = [];
      // 初始化设为空值
      this.bpmnFormData = {
        userType: "",
        assignee: "",
        candidateUsers: "",
        // candidateGroups: "",
        dueDate: "",
        priority: "",
        dataType: "",
        expId: "",
      };
      this.selectData = {
        assignee: null,
        candidateUsers: null,
        // candidateGroups: null,
        exp: null,
      };
      // 流程节点信息上取值
      for (let key in this.bpmnFormData) {
        const value = this.modelerStore.element?.businessObject[key] || this.bpmnFormData[key];
        this.$set(this.bpmnFormData, key, value);
      }
      // 人员信息回显
      this.checkValuesEcho(this.bpmnFormData);
    },

    // 更新节点信息
    updateElementTask(key) {
      const taskAttr = Object.create(null);
      taskAttr[key] = this.bpmnFormData[key] || "";
      this.modelerStore.modeling.updateProperties(this.modelerStore.element, taskAttr);
    },

    // 更新自定义流程节点/参数信息
    updateCustomElement(key, value) {
      const taskAttr = Object.create(null);
      taskAttr[key] = value;
      this.modelerStore.modeling.updateProperties(this.modelerStore.element, taskAttr);
    },

    // 更新人员类型
    updateUserType(val) {
      // 删除xml中已选择数据类型节点
      this.deleteFlowAttar();
      delete this.modelerStore.element.businessObject[`userType`];
      // 清除已选人员数据
      this.bpmnFormData[val] = null;
      this.selectData = {
        assignee: null,
        candidateUsers: null,
        // candidateGroups: null,
        exp: null,
      };
      // 写入userType节点信息到xml
      this.updateCustomElement("userType", val);
      if (val !== "assignee") {
        this.updateMultipleFlag(false);
      }
    },

    // 设计器右侧表单数据回显
    checkValuesEcho(formData) {
      if (StrUtil.isNotBlank(formData.expId) && formData.dataType !== "roles") {
        if (formData.userType == "candidateUsers") {
          this.partSelectType = "2";
          formData.candidateUsers = "动态选人";
        } else {
          this.partSelectType = "3";
          this.$nextTick(() => {
            this.getExpList(formData.expId, formData.userType);
          });
        }
      } else {
        if ("roles" === formData.dataType) {
          this.partSelectType = "1";
          this.$nextTick(() => {
            this.getRoleList(formData.expId, formData.userType);
          });
        } else {
          this.partSelectType = "0";
          this.$nextTick(() => {
            this.getUserList(formData[formData.userType], formData.userType);
          });
        }
      }
      const flag = formData.userType === "assignee" && formData.dataType === "dynamic" ? true : false;
      this.updateMultipleFlag(flag);
    },

    // 获取表达式信息
    getExpList(val, key) {
      if (StrUtil.isNotBlank(val)) {
        this.bpmnFormData[key] = this.modelerStore.expList?.find((item) => item.id.toString() === val).name;
        this.selectData.exp = this.modelerStore.expList?.find((item) => item.id.toString() === val).id;
      }
    },

    // 获取人员信息
    getUserList(val, key) {
      if (StrUtil.isNotBlank(val)) {
        const userIds = val.toString().split(",");
        const newArr = this.modelerStore.userList?.filter((i) => userIds.includes(i.userId.toString()));
        if ("assignee" === key) {
          this.selectData[key] = newArr.find((item) => item.userId.toString() === val.toString()).userId;
          const user = newArr.find((item) => item.userId.toString() === val.toString());
          this.selectedUsers = [user];
        } else {
          this.selectData[key] = newArr.map((item) => item.userId);
          this.selectedUsers = newArr;
        }
      }
    },

    // 获取角色信息
    getRoleList(val, key) {
      if (StrUtil.isNotBlank(val)) {
        const valStr = Array.isArray(val) ? val.join(",") : val.toString();
        const newArr = this.modelerStore.roleList?.filter((i) => valStr.split(",").includes(i.roleId.toString()));
        if ("assignee" === key) {
          this.selectData[key] = newArr.find((item) => item.roleId.toString() === val).roleId;
          const role = newArr.find((item) => item.roleId.toString() === val);
          this.selectedRoles = [role];
        } else {
          this.selectData[key] = newArr.map((item) => item.roleId);
          this.selectedRoles = newArr;
        }
      }
    },

    // ------ 流程审批人员信息弹出框 start---------
    /*选人*/
    openUserSelect() {
      this.partSelectType = "0";
      this.$nextTick(() => {
        this.$refs.users.openSelect();
      });
    },
    /** 选角色确认 */
    openRoleSelect() {
      this.partSelectType = "1";
      this.$nextTick(() => {
        this.$refs.roles.openSelect();
      });
    },

    /*单选表达式*/
    singleExpCheck() {
      this.partSelectType = "3";
      this.expVisible = true;
    },

    /* 设置默认的表达式 */
    defaultExpCheck() {
      this.partSelectType = "2";
      this.$nextTick(() => {
        this.deleteFlowAttar();
        this.bpmnFormData[this.bpmnFormData.userType] = "动态选人";
        this.updateCustomElement("dataType", "dynamic");
        this.updateCustomElement("expId", this.id);
        this.updateCustomElement(this.bpmnFormData.userType, "${" + this.id + "_user}");
      });
    },

    // 表达式选中数据
    expSelect(selection) {
      if (selection) {
        this.deleteFlowAttar();
        this.bpmnFormData[this.bpmnFormData.userType] = selection.name;
        this.updateCustomElement("dataType", selection.dataType);
        this.updateCustomElement("expId", selection.id.toString());
        this.updateCustomElement(this.bpmnFormData.userType, selection.expression);
        this.handleSelectData("exp", selection.id);
        this.multipleFlag = selection.dataType === "dynamic" ? true : false;
      }
    },

    userSelect(selection) {
      if (selection) {
        this.deleteFlowAttar();
        this.updateCustomElement("dataType", "fixed");
        if (selection instanceof Array) {
          const userIds = selection.map((item) => item.userId);
          const nickName = selection.map((item) => item.nickName);
          // userType = candidateUsers
          this.bpmnFormData[this.bpmnFormData.userType] = nickName.join(",");
          this.updateCustomElement(this.bpmnFormData.userType, userIds.join(","));
          this.handleSelectData(this.bpmnFormData.userType, userIds);
        } else {
          // userType = assignee
          this.bpmnFormData[this.bpmnFormData.userType] = selection.nickName;
          this.updateCustomElement(this.bpmnFormData.userType, selection.userId);
          this.handleSelectData(this.bpmnFormData.userType, selection.userId);
        }
      }
    },
    /** 选人确认 */
    confirmUser(selection) {
      this.selectedUsers = selection || [];
      this.userSelect(selection);
    },
    /** 选角色确认 */
    confirmRole(selection) {
      this.selectedRoles = selection || [];
      const idList = selection.map((item) => item.roleId);
      const nameList = selection.map((item) => item.roleName);
      this.roleSelect(idList, nameList);
    },

    // 角色选中数据
    roleSelect(selection, name) {
      if (selection && name) {
        this.deleteFlowAttar();
        this.bpmnFormData[this.bpmnFormData.userType] = name;
        this.updateCustomElement("dataType", "roles");
        // userType = candidateGroups
        this.updateCustomElement("expId", selection);
        this.updateCustomElement(this.bpmnFormData.userType, "${" + this.id + "_user}");
        this.handleSelectData(this.bpmnFormData.userType, selection);
      }
    },

    // 处理人员回显
    handleSelectData(key, value) {
      for (let oldKey in this.selectData) {
        if (key !== oldKey) {
          this.$set(this.selectData, oldKey, null);
        } else {
          this.$set(this.selectData, oldKey, value);
        }
      }
    },

    /*候选角色选中赋值*/
    checkRoleComplete() {
      this.roleVisible = false;
    },

    /*表达式选中赋值*/
    checkExpComplete() {
      this.expVisible = false;
      this.updateMultipleFlag(this.multipleFlag);
    },
    /** 更新多实例配置状态 */
    updateMultipleFlag(flag) {
      this.$emit("flowUserConfirm", flag);
    },
    // 删除节点
    deleteFlowAttar() {
      delete this.modelerStore.element.businessObject[`dataType`];
      delete this.modelerStore.element.businessObject[`expId`];
      delete this.modelerStore.element.businessObject[`assignee`];
      delete this.modelerStore.element.businessObject[`candidateUsers`];
      delete this.modelerStore.element.businessObject[`loopCharacteristics`];
    },
  },
};
</script>

<style scoped>
::v-deep .el-form-item__label {
  font-weight: normal;
}
</style>