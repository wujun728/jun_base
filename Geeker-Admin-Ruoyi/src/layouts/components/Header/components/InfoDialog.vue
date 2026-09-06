<template>
  <el-dialog v-model="dialogVisible" title="个人信息" width="500px" draggable>
    <ul class="list-group list-group-striped">
      <li class="list-group-item">
        <svg-icon name="user" />用户名称
        <div class="pull-right">{{ userInfo.userName }}</div>
      </li>
      <li class="list-group-item">
        <svg-icon name="phone" />手机号码
        <div class="pull-right">{{ userInfo.phonenumber }}</div>
      </li>
      <li class="list-group-item">
        <svg-icon name="email" />用户邮箱
        <div class="pull-right">{{ userInfo.email }}</div>
      </li>
      <li class="list-group-item">
        <svg-icon name="tree" />所属部门
        <div class="pull-right" v-if="userInfo.dept">{{ userInfo.dept.deptName }} / {{ userInfo.postGroup }}</div>
      </li>
      <li class="list-group-item">
        <svg-icon name="peoples" />所属角色
        <div class="pull-right">{{ userInfo.roleGroup }}</div>
      </li>
      <li class="list-group-item">
        <svg-icon name="date" />创建日期
        <div class="pull-right">{{ userInfo.createTime }}</div>
      </li>
    </ul>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";
import SvgIcon from "@/components/SvgIcon/index.vue";
import { useUserStore } from "@/stores/modules/user";

const { userInfo } = useUserStore();

const dialogVisible = ref(false);
const openDialog = () => {
  dialogVisible.value = true;
};

defineExpose({ openDialog });
</script>
<style lang="scss">
.list-group-striped > .list-group-item {
  padding-right: 0;
  padding-left: 0;
  border-right: 0;
  border-left: 0;
  border-radius: 0;
}
.list-group {
  padding-left: 0;
  list-style: none;
}
.list-group-item {
  padding: 11px 0;
  margin-bottom: -1px;
  font-size: 13px;
  border-top: 1px solid #e7eaec;
  border-bottom: 1px solid #e7eaec;
}
.pull-right {
  float: right !important;
}
</style>
