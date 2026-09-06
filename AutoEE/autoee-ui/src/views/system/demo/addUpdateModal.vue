<template>
  <div>
    <a-modal v-model:visible="pageDataObj.visible" width="60%" :title="pageDataObj.title" :destroyOnClose="true" @ok="clickModalOk" @cancel="handleModalCancel">
      <a-form ref="addUpdateFormRef" name="addUpdateFormObj" :model="addUpdateFormObj.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
        <a-row :gutter="24">
        <a-col span="12">
            <a-form-item name="deptId" label="部门id" :rules="[{required: false}]">
              <a-input v-model:value="addUpdateFormObj.data.deptId" :maxlength="11" placeholder="请填写部门id" ></a-input>
            </a-form-item>
          </a-col>
        <a-col span="12">
            <a-form-item name="userId" label="用户id" :rules="[{required: false}]">
              <a-input v-model:value="addUpdateFormObj.data.userId" :maxlength="11" placeholder="请填写用户id" ></a-input>
            </a-form-item>
          </a-col>
        <a-col span="12">
            <a-form-item name="orderNum" label="排序号" :rules="[{required: false}]">
              <a-input v-model:value="addUpdateFormObj.data.orderNum" :maxlength="11" placeholder="请填写排序号" ></a-input>
            </a-form-item>
          </a-col>
        <a-col span="12">
            <a-form-item name="testKey" label="key键" :rules="[{required: false}]">
              <a-input v-model:value="addUpdateFormObj.data.testKey" :maxlength="255" placeholder="请填写key键" ></a-input>
            </a-form-item>
          </a-col>
        <a-col span="12">
            <a-form-item name="value" label="值" :rules="[{required: false}]">
              <a-input v-model:value="addUpdateFormObj.data.value" :maxlength="255" placeholder="请填写值" ></a-input>
            </a-form-item>
          </a-col>
        <a-col span="12">
            <a-form-item name="version" label="版本" :rules="[{required: false}]">
              <a-input v-model:value="addUpdateFormObj.data.version" :maxlength="11" placeholder="请填写版本" ></a-input>
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
import { getDemo,  addDemo, updateDemo } from "@/api/system/demo";
import { message } from 'ant-design-vue';

// 父页面公用数据
const fatherPageDataObj = defineProps({
});

// 引入父页面方法
const pageEmit = defineEmits(["refreshFatherPageTable"]);

// 当前页面使用的数据
const pageDataObj = reactive({
  title: '新增页面',
  visible: false,
  operateType: '',
});

// 新增修改Form
const addUpdateFormRef = ref();
const addUpdateFormObj = reactive({ data: {} });
// 重置Form
const resetAddUpdateForm = () => {
  AutoeeUtil.resetFormFieldsToEmpty(addUpdateFormObj.data);
};

// 打开新增窗口
const openAddModal = () => {
  pageDataObj.operateType = 'add';
  pageDataObj.title = '新增';
  pageDataObj.visible = true;
};

// 打开修改窗口
const openUpdateModal = (record, tablePage) => {
  pageDataObj.operateType = 'update';
  pageDataObj.title = '修改';
  getDemo(record.id).then((response) => {
    addUpdateFormObj.data = response.data;
    pageDataObj.visible = true;
  });
};

// 点击窗口确认
const clickModalOk = (e) => {
  addUpdateFormRef.value
    .validateFields()
    .then((values) => {
      if (pageDataObj.operateType === 'add') {
        addDemo(addUpdateFormObj.data).then((res) => {
          pageDataObj.visible = false;
          resetAddUpdateForm();
          // 调用父页面刷新方法
          pageEmit("refreshFatherPageTable");
          AutoeeUtil.messageSuccess('新增操作成功。');
        });
      } else if (pageDataObj.operateType === 'update') {
        updateDemo(addUpdateFormObj.data).then((res) => {
          pageDataObj.visible = false;
          resetAddUpdateForm();
          // 调用父页面刷新方法
          pageEmit("refreshFatherPageTable");
          AutoeeUtil.messageSuccess('修改操作成功。');
        });
      }
    }).catch((info) => {
      // console.log('Validate Failed:', info);
    });
};

// 点击窗口取消
const handleModalCancel = () => {
  resetAddUpdateForm();
};

// 对外暴露出去
defineExpose({
  openAddModal,
  openUpdateModal,
});
</script>

<style lang="less"></style>
