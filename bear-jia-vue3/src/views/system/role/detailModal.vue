<template>
  <div>
    <a-modal v-model:open="pageData.visible" :title="pageData.title" width="60%">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form ref="roleDetailFormRef" :labelCol="{ span: 8 }" :model="roleDetailForm.data" :wrapperCol="{ span: 14 }"
              class="bearjia-detail-page" name="roleDetailForm">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item label="角色名称" name="roleName">
              <span>{{ roleDetailForm.data.roleName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="角色编码" name="roleKey">
              <span>{{ roleDetailForm.data.roleKey }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="显示顺序" name="roleSort">
              <span>{{ roleDetailForm.data.roleSort }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="数据权限" name="dataScope">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(roleDetailForm.data.dataScope, fatherPageData.dataScopeDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="角色状态" name="status">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(roleDetailForm.data.status, fatherPageData.sysNormalDisableDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="备注" name="remark">
              <span>{{ roleDetailForm.data.remark }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="菜单权限" name="menuIds">
              <a-tree
                  v-model:checkedKeys="roleDetailForm.data.menuIds"
                  :defaultExpandAll="pageData.defaultExpandAll"
                  :disabled="true"
                  :fieldNames="{ children: 'children', title: 'menuName', key: 'menuId' }"
                  :tree-data="pageData.menusDict"
                  checkable
              >
              </a-tree>
            </a-form-item>
          </a-col>
          <!-- <a-col span="12">
            <a-form-item name="deptCheckStrictly" label="部门树父子关联">
              <a-checkbox v-model:checked="roleDetailForm.data.deptCheckStrictly" :disabled="true"></a-checkbox>
            </a-form-item>
          </a-col> -->
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {getRole} from '@/api/system/role';
import {listMenu as getMenuTreeselect, roleMenuTreeselect} from '@/api/system/menu';
import {handleTree} from '@/utils/bearjia.js';
import {reactive, ref} from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 父页面公用数据
const fatherPageData = defineProps({
  dataScopeDict: Array,
  menuCheckStrictlyDict: Array,
  sysNormalDisableDict: Array,
});

// 当前页面使用的数据
const pageData = reactive({
  title: '详细页面',
  visible: false,
  operateType: '',
  menusDict: [],
  defaultExpandAll: false,
});

//详细Form
const roleDetailFormRef = ref();
const roleDetailForm = reactive({data: {menuIds: []}});
// 打开详细窗口
const openModal = (record) => {
  getRole(record.roleId).then((response) => {
    roleDetailForm.data = response.data;
    // 查询菜单下拉列表
    getMenuTreeselect().then((response) => {
      pageData.menusDict = handleTree(response.data, 'menuId');
      pageData.menusDict.push({menuId: 0, menuName: '无'});
      roleMenuTreeselect(record.roleId).then((response) => {
        roleDetailForm.data.menuIds = response.checkedKeys;
        pageData.defaultExpandAll = false;
        pageData.visible = true;
      });
    });
  });
};

// 对外暴露出去
defineExpose({
  openModal,
});
</script>

<style lang="less"></style>
