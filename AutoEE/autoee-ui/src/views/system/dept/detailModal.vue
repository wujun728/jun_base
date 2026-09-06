<template>
  <div>
    <a-modal v-model:visible="pageData.visible" width="60%" :title="pageData.title">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form class="autoee-detail-page" ref="deptDetailFormRef" name="deptDetailForm" :model="deptDetailForm.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item name="parentId" label="上级部门">
              <span>{{ deptDetailForm.data.parentId }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="deptName" label="部门名称">
              <span>{{ deptDetailForm.data.deptName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="orderNum" label="显示顺序">
              <span>{{ deptDetailForm.data.orderNum }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="leader" label="负责人">
              <span>{{ deptDetailForm.data.leader }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="phone" label="联系电话">
              <span>{{ deptDetailForm.data.phone }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="email" label="邮箱">
              <span>{{ deptDetailForm.data.email }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="status" label="部门状态">
              <span>{{ AutoeeUtil.getDictLabelByKey(deptDetailForm.data.status, fatherPageData.statusDict) }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { getDept } from '@/api/system/dept';
import { reactive, ref } from 'vue';
import AutoeeUtil from '@/utils/AutoeeUtil.js';

// 父页面公用数据
const fatherPageData = defineProps({
  statusDict: Array,
  deptTreeData: Array,
});

// 当前页面使用的数据
const pageData = reactive({
  title: '详细页面',
  visible: false,
  operateType: '',
});

//详细Form
const deptDetailFormRef = ref();
const deptDetailForm = reactive({ data: {} });
// 打开详细窗口
const openModal = (record) => {
  pageData.visible = true;
  getDept(record.deptId).then((response) => {
    deptDetailForm.data = response.data;
    deptDetailForm.data.parentId = AutoeeUtil.getDeptNameByDeptId(deptDetailForm.data.parentId, fatherPageData.deptTreeData);
  });
};

// 对外暴露出去
defineExpose({
  openModal,
});
</script>

<style lang="less"></style>
