<template>
  <div>
    <a-modal v-model:visible="pageData.visible" width="60%" :title="pageData.title" :destroyOnClose="true" @ok="clickModalOk" @cancel="handleModalCancel">
      <a-form ref="roleAddUpdateFormRef" name="roleAddUpdateForm" :model="roleAddUpdateForm.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item name="roleName" label="角色名称" :rules="[{ required: true, message: '角色名称不能为空！' }]">
              <a-input v-model:value="roleAddUpdateForm.data.roleName" :maxlength="30" placeholder="请填写角色名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="roleKey" label="角色编码" :rules="[{ required: true, message: '角色编码不能为空！' }]">
              <a-input v-model:value="roleAddUpdateForm.data.roleKey" :maxlength="100" placeholder="请填写角色编码"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="roleSort" label="显示顺序" :rules="[{ required: true, message: '显示顺序不能为空！' }]">
              <a-input-number v-model:value="roleAddUpdateForm.data.roleSort" :min="1" :max="9999" />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="dataScope" label="数据权限" :rules="[{ required: true, message: '数据权限不能为空！' }]">
              <a-select v-model:value="roleAddUpdateForm.data.dataScope" :options="fatherPageData.dataScopeDict" placeholder="请选择数据权限"> </a-select>
            </a-form-item>
          </a-col>
          <!-- <a-col span="12">
            <a-form-item name="deptCheckStrictly" label="部门树父子关联" :rules="[{}]">
              <a-checkbox v-model:checked="roleAddUpdateForm.data.deptCheckStrictly"></a-checkbox>
            </a-form-item>
          </a-col> -->
          <a-col span="12">
            <a-form-item name="status" label="角色状态" :rules="[{ required: true, message: '角色状态不能为空！' }]">
              <a-select v-model:value="roleAddUpdateForm.data.status" :options="fatherPageData.sysNormalDisableDict" placeholder="请选择角色状态"> </a-select>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="remark" label="备注" :rules="[{}]">
              <a-input v-model:value="roleAddUpdateForm.data.remark" :maxlength="500" placeholder="请填写备注" />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="menuIds" label="菜单权限" :rules="[{ required: true, message: '菜单权限不能为空！' }]">
              <a-tree
                v-model:checkedKeys="roleAddUpdateForm.data.checkedMenuIds"
                checkable
                :checkStrictly="false"
                :tree-data="pageData.menusDict"
                :fieldNames="{ children: 'children', title: 'menuName', key: 'menuId' }"
                @check="onCheck"
              >
              </a-tree>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup>
import { reactive, ref } from 'vue';
import AutoeeUtil from '@/utils/AutoeeUtil.js';
import { listMenu as getMenuTreeselect } from '@/api/system/menu';
import { handleTree } from '@/utils/ruoyi.js';
import { listRole, getRole, delRole, addRole, updateRole, dataScope, changeRoleStatus } from '@/api/system/role';
import { message } from 'ant-design-vue';
import { treeselect as menuTreeselect, roleMenuTreeselect } from '@/api/system/menu';
import { treeselect as deptTreeselect, roleDeptTreeselect } from '@/api/system/dept';

// 父页面公用数据
const fatherPageData = defineProps({
  dataScopeDict: Array,
  menuCheckStrictlyDict: Array,
  sysNormalDisableDict: Array,
});

// 引入父页面方法
const pageEmit = defineEmits(["refreshFatherPageTable"]);

// 当前页面使用的数据
const pageData = reactive({
  title: '新增页面',
  visible: false,
  operateType: '',
  menusDict: [],
});

// 新增修改Form
const roleAddUpdateFormRef = ref();
const roleAddUpdateForm = reactive({ data: { checkedMenuIds: [], menuIds: [], deptIds: [], menuCheckStrictly: 'true' } });
// 重置Form
const resetRoleAddUpdateForm = () => {
  AutoeeUtil.resetFormFieldsToEmpty(roleAddUpdateForm.data);
};

// 打开新增窗口
const openAddModal = () => {
  pageData.operateType = 'add';
  pageData.title = '新增角色';
  // 查询菜单下拉列表
  getMenuTreeselect().then((response) => {
    pageData.menusDict = handleTree(response.data, 'menuId');
    // pageData.menusDict.push({ menuId: 0, menuName: '无' });
    pageData.visible = true;
  });
};

// 打开修改窗口
const openUpdateModal = (record, tablePage) => {
  pageData.operateType = 'update';
  pageData.title = '修改角色';
  getRole(record.roleId).then((response) => {
    roleAddUpdateForm.data = response.data;
    // 查询菜单下拉列表
    getMenuTreeselect().then((response) => {
      pageData.menusDict = handleTree(response.data, 'menuId');
      // pageData.menusDict.push({ menuId: 0, menuName: '无' });
      roleMenuTreeselect(record.roleId).then((response) => {
        // roleAddUpdateForm.data.checkedMenuIds = response.data.checkedKeys;
        roleAddUpdateForm.data.menuIds = response.checkedKeys;
        initMenuCheckedKeys(pageData.menusDict, response.checkedKeys);
        pageData.visible = true;
      });
    });
  });
};

// 点击窗口确认
const clickModalOk = (e) => {
  // console.log('roleAddUpdateForm.data.menuIds=' + JSON.stringify(roleAddUpdateForm.data.menuIds));
  roleAddUpdateFormRef.value
    .validateFields()
    .then((values) => {
      if (pageData.operateType === 'add') {
        addRole(roleAddUpdateForm.data).then((res) => {
          roleAddUpdateForm.data.menuCheckStrictly = false;
          roleAddUpdateForm.data.deptIds = [];
          dataScope(roleAddUpdateForm.data).then((response) => {
            pageData.visible = false;
            resetRoleAddUpdateForm();
		    // 调用父页面刷新方法
		    pageEmit("refreshFatherPageTable");
            AutoeeUtil.messageSuccess('新增操作成功。');
          });
        });
      } else if (pageData.operateType === 'update') {
        updateRole(roleAddUpdateForm.data).then((res) => {
          roleAddUpdateForm.data.menuCheckStrictly = false;
          roleAddUpdateForm.data.deptIds = [];
          dataScope(roleAddUpdateForm.data).then((response) => {
            pageData.visible = false;
            resetRoleAddUpdateForm();
            // 调用父页面刷新方法
            pageEmit("refreshFatherPageTable");
            AutoeeUtil.messageSuccess('修改操作成功。');
          });
        });
      }
    })
    .catch((info) => {
      console.log('Validate Failed:', info);
    });
};

// 树形控件，点击子节点时，将父节点已放入选中节点数组中
const onCheck = (checkedKeys, e) => {
  //注意：halfCheckedKeys 是没有全部勾选状态下的父节点
  roleAddUpdateForm.data.menuIds = checkedKeys.concat(e.halfCheckedKeys);
};

// 菜单树反显时，父节点下的子节点没有全部选中时，保证父节点为半选中状态，而不能是全选中状态，否则导致父节点下的子节点全部被选中
let allMenuLeafNodes = [];
const initMenuCheckedKeys = (menusDict, resCheckedKeys) => {
  // 1.循环遍历出最深层子节点，存放在一个数组中
  allMenuLeafNodes = [];
  getAllMenuLeafNodes(menusDict);
  // 2.将后台返回的含有父节点的数组和第一步骤遍历的数组做比较
  // 3.如果有相同值，将相同值取出来，push到一个新数组中
  let allResCheckedMenuLeafNodes = getAllResCheckedMenuLeafNodes(allMenuLeafNodes, resCheckedKeys);
  // 4.利用这个新的重组的数组给Tree组件selected赋值
  roleAddUpdateForm.data.checkedMenuIds = allResCheckedMenuLeafNodes;
};

//  循环遍历出最深层叶子节点，存放在一个数组中
const getAllMenuLeafNodes = (data) => {
  data &&
    data.map((item) => {
      if (item.children && item.children.length > 0) {
        getAllMenuLeafNodes(item.children);
      } else {
        allMenuLeafNodes.push(item.menuId);
      }
      return null;
    });
  return allMenuLeafNodes;
};

//  将后台返回的含有父节点的数组和第一步骤遍历的数组做比较,如果有相同值，将相同值取出来，push到allResCheckedMenuLeafNodes数组中
const getAllResCheckedMenuLeafNodes = (allMenuLeafNodes, resCheckedKeys) => {
  let allResCheckedMenuLeafNodes = [];
  for (var i in resCheckedKeys) {
    for (var k in allMenuLeafNodes) {
      if (allMenuLeafNodes[k] === resCheckedKeys[i]) {
        allResCheckedMenuLeafNodes.push(allMenuLeafNodes[k]);
      }
    }
  }
  return allResCheckedMenuLeafNodes;
};

// 点击窗口取消
const handleModalCancel = () => {
  resetRoleAddUpdateForm();
};

// 对外暴露出去
defineExpose({
  openAddModal,
  openUpdateModal,
});
</script>

<style lang="less"></style>
