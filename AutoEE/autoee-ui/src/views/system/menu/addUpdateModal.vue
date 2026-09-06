<template>
  <div>
    <a-modal v-model:visible="pageData.visible" width="60%" :title="pageData.title" :destroyOnClose="true" @ok="clickModalOk" @cancel="handleModalCancel">
      <a-form ref="menuAddUpdateFormRef" name="menuAddUpdateForm" :model="menuAddUpdateForm.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item name="menuName" label="菜单名称" :rules="[{ required: true, message: '菜单名称不能为空！' }]">
              <a-input v-model:value="menuAddUpdateForm.data.menuName" :maxlength="30" placeholder="请填写菜单名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="parentId" label="上级菜单" :rules="[{ required: true, message: '上级菜单不能为空！' }]">
              <a-tree-select
                v-model:value="menuAddUpdateForm.data.parentId"
                placeholder="请选择上级菜单"
                :tree-data="pageData.menusDict"
                :fieldNames="{ children: 'children', label: 'menuName', key: 'menuId', value: 'menuId' }"
              >
              </a-tree-select>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="orderNum" label="显示顺序" :rules="[{ required: true, message: '显示顺序不能为空！' }]">
              <a-input v-model:value="menuAddUpdateForm.data.orderNum" :maxlength="30" placeholder="请填写显示顺序"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="path" label="路由地址" :rules="[{}]">
              <a-input v-model:value="menuAddUpdateForm.data.path" :maxlength="30" placeholder="请填写路由地址"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="component" label="组件路径" :rules="[{}]">
              <a-input v-model:value="menuAddUpdateForm.data.component" :maxlength="30" placeholder="请填写组件路径"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="query" label="路由参数" :rules="[{}]">
              <a-input v-model:value="menuAddUpdateForm.data.query" :maxlength="30" placeholder="请填写路由参数"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="isFrame" label="是否为外链" :rules="[{ required: true, message: '是否为外链不能为空！' }]">
              <a-radio-group v-model:value="menuAddUpdateForm.data.isFrame" :options="fatherPageData.sysYesNoDict" optionType="button" button-style="solid"></a-radio-group>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="menuType" label="菜单类型" :rules="[{ required: true, message: '菜单类型不能为空！' }]">
              <a-select v-model:value="menuAddUpdateForm.data.menuType" :options="fatherPageData.menuTypeDict" placeholder="请选择菜单类型"> </a-select>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="visible" label="是否显示" :rules="[{ required: true, message: '是否显示不能为空！' }]">
              <a-radio-group v-model:value="menuAddUpdateForm.data.visible" :options="fatherPageData.sysYesNoDict" optionType="button" button-style="solid"></a-radio-group>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="status" label="菜单状态" :rules="[{ required: true, message: '菜单状态不能为空！' }]">
              <a-radio-group v-model:value="menuAddUpdateForm.data.status" :options="fatherPageData.sysNormalDisableDict" optionType="button" button-style="solid"></a-radio-group>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="perms" label="权限标识" :rules="[{}]">
              <a-input v-model:value="menuAddUpdateForm.data.perms" :maxlength="30" placeholder="请填写权限标识"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="icon" label="菜单图标" :rules="[{}]">
              <a-input v-model:value="menuAddUpdateForm.data.icon" :maxlength="30" placeholder="请填写菜单图标">
                <template #addonBefore>
                  <AutoeeIcon :icon="menuAddUpdateForm.data.icon" />
                </template>
              </a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="remark" label="备注" :rules="[{}]">
              <a-textarea v-model:value="menuAddUpdateForm.data.remark" placeholder="请填写备注" :rows="3" />
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
import { listMenu as getMenuTreeselect, getMenu, addMenu, updateMenu } from '@/api/system/menu';
import { message } from 'ant-design-vue';
import { handleTree } from '@/utils/ruoyi.js';
import { AutoeeIcon } from '@/utils/AutoeeIcon.js';

// 父页面公用数据
const fatherPageData = defineProps({
  sysYesNoDict: Array,
  sysShowHideDict: Array,
  menuTypeDict: Array,
  sysNormalDisableDict: Array,
});

const pageEmit = defineEmits(["refreshFatherPageTable"]);

// 当前页面使用的数据
const pageData = reactive({
  title: '新增页面',
  visible: false,
  operateType: '',
  menusDict: [],
});

// 新增修改Form
const menuAddUpdateFormRef = ref();
const menuAddUpdateForm = reactive({ data: { isFrame: '1', visible: '0', status: '0' } });
// 重置Form
const resetMenuAddUpdateForm = () => {
  AutoeeUtil.resetFormFieldsToEmpty(menuAddUpdateForm.data);
};

// 打开新增窗口
const openAddModal = () => {
  menuAddUpdateForm.data.isFrame = '1';
  menuAddUpdateForm.data.visible = '0';
  menuAddUpdateForm.data.status = '0';
  pageData.operateType = 'add';
  pageData.title = '新增菜单';
  // 查询菜单下拉列表
  getMenuTreeselect().then((response) => {
    pageData.menusDict = handleTree(response.data, 'menuId');
    pageData.menusDict.push({ menuId: 0, menuName: '无' });
    pageData.visible = true;
  });
};

// 打开修改窗口
const openUpdateModal = (record, ) => {
  pageData.operateType = 'update';
  pageData.title = '修改菜单';
  getMenu(record.menuId).then((response) => {
    menuAddUpdateForm.data = response.data;
    // 查询菜单下拉列表
    getMenuTreeselect().then((response) => {
      pageData.menusDict = handleTree(response.data, 'menuId');
      pageData.menusDict.push({ menuId: 0, menuName: '无' });
      pageData.visible = true;
    });
  });
};

// 点击窗口确认
const clickModalOk = (e) => {
  menuAddUpdateFormRef.value
    .validateFields()
    .then((values) => {
      if (pageData.operateType === 'add') {
        addMenu(menuAddUpdateForm.data).then((res) => {
          pageData.visible = false;
          resetMenuAddUpdateForm();
          // 调用父页面刷新方法
          pageEmit("refreshFatherPageTable");
          AutoeeUtil.messageSuccess('新增操作成功。');
        });
      } else if (pageData.operateType === 'update') {
        updateMenu(menuAddUpdateForm.data).then((res) => {
          pageData.visible = false;
          resetMenuAddUpdateForm();
          // 调用父页面刷新方法
          pageEmit("refreshFatherPageTable");
          AutoeeUtil.messageSuccess('修改操作成功。');
        });
      }
    })
    .catch((info) => {
      console.log('Validate Failed:', info);
    });
};

// 点击窗口取消
const handleModalCancel = () => {
  resetMenuAddUpdateForm();
};

// 对外暴露出去
defineExpose({
  openAddModal,
  openUpdateModal,
});
</script>

<style lang="less"></style>
