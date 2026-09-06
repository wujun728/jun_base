<template>
  <div>
    <a-modal v-model:open="pageData.visible" width="60%" :title="pageData.title" :destroyOnClose="true" @ok="clickModalOk" @cancel="handleModalCancel">
      <a-form ref="deptAddUpdateFormRef" name="deptAddUpdateForm" :model="deptAddUpdateForm.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
        <a-row :gutter="24">
          <a-col span="12" v-if="pageData.operateType == 'add' || (pageData.operateType == 'update' && deptAddUpdateForm.data.parentId != '0')">
            <a-form-item name="parentId" label="上级部门" :rules="[{ required: true, message: '上级部门不能为空！' }]">
              <a-tree-select
                v-model:value="deptAddUpdateForm.data.parentId"
                :tree-data="fatherPageData.deptTreeData"
                :fieldNames="{ children: 'children', label: 'label', key: 'id', value: 'id' }"
                placeholder="请选择上级部门"
              >
              </a-tree-select>
            </a-form-item>
          </a-col>
          <!-- <a-col span="12">
            <a-form-item name="ancestors" label="祖级列表" :rules="[{ required: true, message: '祖级列表不能为空！' }]">
              <a-input v-model:value="deptAddUpdateForm.data.ancestors" :maxlength="30" placeholder="请填写祖级列表"></a-input>
            </a-form-item>
          </a-col> -->
          <a-col span="12">
            <a-form-item name="deptName" label="部门名称" :rules="[{ required: true, message: '部门名称不能为空！' }]">
              <a-input v-model:value="deptAddUpdateForm.data.deptName" :maxlength="30" placeholder="请填写部门名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="orderNum" label="显示顺序" :rules="[{ required: true, message: '显示顺序不能为空！' }]">
              <a-input v-model:value="deptAddUpdateForm.data.orderNum" :maxlength="30" placeholder="请填写显示顺序"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="leader" label="负责人" :rules="[{}]">
              <a-input v-model:value="deptAddUpdateForm.data.leader" :maxlength="30" placeholder="请填写负责人"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="phone" label="联系电话" :rules="[{}]">
              <a-input v-model:value="deptAddUpdateForm.data.phone" :maxlength="30" placeholder="请填写联系电话"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="email" label="邮箱" :rules="[{ type: 'email', message: '请填写正确的邮箱！' }]">
              <a-input v-model:value="deptAddUpdateForm.data.email" :maxlength="30" placeholder="请填写邮箱"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="status" label="部门状态" :rules="[{ required: true, message: '部门状态不能为空！' }]">
              <a-select v-model:value="deptAddUpdateForm.data.status" :options="fatherPageData.statusDict" placeholder="请选择部门状态"> </a-select>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup>
import { reactive, ref } from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';
import { getDept, addDept, updateDept } from '@/api/system/dept';

// 父页面公用数据
const fatherPageData = defineProps({
  statusDict: Array,
  deptTreeData: Array,
});

// 引入父页面方法
const pageEmit  = defineEmits(['querDeptTreeData','refreshFatherPageTable']);

// 当前页面使用的数据
const pageData = reactive({
  title: '新增页面',
  visible: false,
  operateType: '',
});

// 新增修改Form
const deptAddUpdateFormRef = ref();
const deptAddUpdateForm = reactive({ data: {} });
// 重置Form
const resetDeptAddUpdateForm = () => {
  BearJiaUtil.resetFormFieldsToEmpty(deptAddUpdateForm.data);
};

// 打开新增窗口
const openAddModal = () => {
  pageData.visible = true;
  pageData.operateType = 'add';
  pageData.title = '新增部门';
  console.log('deptAddUpdateForm.data=' + JSON.stringify(deptAddUpdateForm.data));
};

// 打开修改窗口
const openUpdateModal = (record, tablePage) => {
  pageData.visible = true;
  pageData.operateType = 'update';
  pageData.title = '修改部门';
  getDept(record.deptId).then((response) => {
    deptAddUpdateForm.data = response.data;
    console.log('deptAddUpdateForm.data=' + JSON.stringify(deptAddUpdateForm.data));
  });
};

// 点击窗口确认
const clickModalOk = (e) => {
  deptAddUpdateFormRef.value
    .validateFields()
    .then((values) => {
      if (pageData.operateType === 'add') {
        addDept(deptAddUpdateForm.data).then((res) => {
          pageData.visible = false;
          resetDeptAddUpdateForm();
          // 操作后，更新部门下拉列表数据
          pageEmit('querDeptTreeData');
          // 调用父页面刷新方法
          pageEmit("refreshFatherPageTable");
          BearJiaUtil.messageSuccess('新增操作成功。');
        });
      } else if (pageData.operateType === 'update') {
        updateDept(deptAddUpdateForm.data).then((res) => {
          pageData.visible = false;
          resetDeptAddUpdateForm();
          // 操作后，更新部门下拉列表数据
          pageEmit('querDeptTreeData');
          // 调用父页面刷新方法
          pageEmit("refreshFatherPageTable");
          BearJiaUtil.messageSuccess('修改操作成功。');
        });
      }
    })
    .catch((info) => {
      console.log('Validate Failed:', info);
    });
};

// 点击窗口取消
const handleModalCancel = () => {
  resetDeptAddUpdateForm();
};

// 对外暴露出去
defineExpose({
  openAddModal,
  openUpdateModal,
});
</script>

<style lang="less"></style>
