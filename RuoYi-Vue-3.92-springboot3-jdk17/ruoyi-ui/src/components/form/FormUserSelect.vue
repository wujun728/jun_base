<template>
  <!-- form表单：选人 -->
  <div>
    <div class="user-select-trigger" :class="{ 'is-disabled': disabled }" @click="openSelect">
      <div v-if="selectedUsers && selectedUsers.length > 0" class="selected-users">
        <span v-for="(user, index) in selectedUsers" :key="index" class="user-chip">
          <el-avatar :size="22" :src="avatar(user.avatar)" />
          <span class="ml5">{{ user.nickName }}</span>
          <span @click.stop="removeUser(index)">
            <el-icon name="close" class="remove-icon" />
          </span>
        </span>
      </div>
      <span v-else class="placeholder">
        <i class="el-icon-user mr5"></i>
        {{ placeholder }}
      </span>
    </div>
    <!-- 选人组件 -->
    <user-select
      ref="orgSelect"
      :type="multiple == true ? 'multiple' : 'single'"
      :open.sync="open"
      :selectedUsers="selectedUsers"
      @confimUser="confimUser"
    />
  </div>
</template>
  
<script>
import UserSelect from "@/components/org/UserAllSelect";
import { StrUtil } from "@/utils/StrUtil";

export default {
  name: "FormUserSelect",
  components: { UserSelect },
  props: {
    // 已选择的用户
    checkedUsers: [Array, String],
    // 是否多选
    multiple: {
      type: Boolean,
      default: false,
    },
    // 是否禁用
    disabled: {
      type: Boolean,
      default: false,
    },
    // 提示语
    placeholder: {
      type: String,
      default: null,
    },
    // 配置的属性--end
  },
  data() {
    return {
      // 选人组件开关
      open: false,
      // 当前选中的用户
      selectedUsers: [],
      // 选人类型：multiple-多选，single-单选
      type: "multiple",
      baseApi: process.env.VUE_APP_BASE_API,
      user: this.$store.state.user.userInfo,
    };
  },
  watch: {
    checkedUsers: {
      handler(val) {
        this.selectedUsers = val || [];
      },
    },
    deep: true,
    immediate: true,
  },
  methods: {
    /** 头像处理 */
    avatar(val) {
      if (StrUtil.isBlank(val)) return;
      return this.baseApi + val;
    },
    /** 打开选人组件 */
    openSelect() {
      if (this.disabled) return;
      this.open = true;
    },
    /** 选人确认 */
    confimUser(selection) {
      this.open = false;
      this.selectedUsers = selection || [];
      this.$emit("input", JSON.parse(JSON.stringify(this.selectedUsers)));
    },
    /** 移除已选用户 */
    removeUser(index) {
      if (this.disabled) return;
      this.selectedUsers.splice(index, 1);
      this.$emit("input", JSON.parse(JSON.stringify(this.selectedUsers)));
    },
  },
};
</script>
<style scoped lang="scss">
.user-select-trigger {
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

  .selected-users {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 5px;
  }
  .user-chip {
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

.user-select-trigger.is-disabled {
  background-color: #f7f7f7;
  border-color: #f0f0f0;
  color: #606266;
  cursor: not-allowed;

  .user-chip {
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