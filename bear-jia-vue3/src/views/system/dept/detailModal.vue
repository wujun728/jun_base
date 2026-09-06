<template>
  <div>
    <a-modal v-model:open="pageData.visible" :title="pageData.title" width="60%">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form ref="deptDetailFormRef" :labelCol="{ span: 8 }" :model="deptDetailForm.data" :wrapperCol="{ span: 14 }"
              class="bearjia-detail-page" name="deptDetailForm">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item label="上级部门" name="parentId">
              <span>{{ deptDetailForm.data.parentId }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="部门名称" name="deptName">
              <span>{{ deptDetailForm.data.deptName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="显示顺序" name="orderNum">
              <span>{{ deptDetailForm.data.orderNum }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="负责人" name="leader">
              <span>{{ deptDetailForm.data.leader }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="联系电话" name="phone">
              <span>{{ deptDetailForm.data.phone }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="邮箱" name="email">
              <span>{{ deptDetailForm.data.email }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="部门状态" name="status">
              <span>{{ BearJiaUtil.getDictLabelByKey(deptDetailForm.data.status, fatherPageData.statusDict) }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {getDept} from '@/api/system/dept';
import {reactive, ref} from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

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
const deptDetailForm = reactive({data: {}});
// 打开详细窗口
const openModal = (record) => {
  pageData.visible = true;
  getDept(record.deptId).then((response) => {
    deptDetailForm.data = response.data;
    deptDetailForm.data.parentId = BearJiaUtil.getDeptNameByDeptId(deptDetailForm.data.parentId, fatherPageData.deptTreeData);
  });
};

// 对外暴露出去
defineExpose({
  openModal,
});
</script>

<style lang="less"></style>
