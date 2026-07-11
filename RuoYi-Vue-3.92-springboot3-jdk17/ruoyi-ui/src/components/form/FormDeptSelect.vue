<template>
  <!-- form表单：选部门 -->
  <div>
    <div class="dept-select-trigger" :class="{ 'is-disabled': disabled }" @click="openSelect">
      <div v-if="deptList && deptList.length > 0" class="selected-dept">
        <span v-for="(dept, index) in deptList" :key="index" class="dept-chip">
          <span class="ml5">{{ dept.label }}</span>
          <span @click.stop="removeDept(index)">
            <el-icon name="close" class="remove-icon" />
          </span>
        </span>
      </div>
      <span v-else class="placeholder">
        <svg-icon icon-class="tree" class="mr5" />
        {{ placeholder }}
      </span>
    </div>
    <!-- 选组织组件 -->
    <org-select
      ref="orgSelect"
      :type="multiple == true ? 'multiple' : 'single'"
      :open.sync="open"
      :selectedDepts="selectedDeptList"
      @confimDept="confimDept"
    />
  </div>
</template>
  
<script>
import OrgSelect from "@/components/org/TreeSelect";

export default {
  name: "FormDeptSelect",
  components: { OrgSelect },
  props: {
    // 绑定的值
    value: [Array, String],
    // 配置的属性--start
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
    initDept: {
      type: String,
      default: null,
    },
    // 配置的属性--end
  },
  data() {
    return {
      // 选人组件开关
      open: false,
      // 选中的用户
      selectedDeptList: [],
      // 选人类型：multiple-多选，single-单选
      type: "multiple",
      baseApi: process.env.VUE_APP_BASE_API,
      user: this.$store.state.user.userInfo,
    };
  },
  computed: {
    deptList() {
      return this.value;
    },
  },
  watch: {
    value: {
      handler(val) {
        this.selectedDeptList = val || [];
      },
    },
    immediate: true,
  },
  methods: {
    /** 打开选人组件 */
    openSelect() {
      if (this.disabled) return;
      this.open = true;
    },
    /** 选人确认 */
    confimDept(selection) {
      this.open = false;
      this.selectedDeptList = selection || [];
      this.$emit("input", JSON.parse(JSON.stringify(this.selectedDeptList)));
    },
    /** 移除已选用户 */
    removeDept(index) {
      if (this.disabled) return;
      this.selectedDeptList.splice(index, 1);
      this.$emit("input", JSON.parse(JSON.stringify(this.selectedDeptList)));
    },
  },
};
</script>
<style scoped lang="scss">
.dept-select-trigger {
  min-height: 36px;
  padding: 2px 6px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  flex-wrap: wrap;

  :hover {
    border-color: #409eff;
  }

  .selected-dept {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 5px;
  }
  .dept-chip {
    display: inline-flex;
    align-items: center;
    padding: 2px 5px;
    margin: 2px 5px;
    background: #f5faff;
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

.dept-select-trigger.is-disabled {
  background-color: #f7f7f7;
  border-color: #f0f0f0;
  color: #606266;
  cursor: not-allowed;

  .dept-chip {
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
</style>