<template>
  <div>
    <a-modal v-model:open="pageData.visible" :title="pageData.title" width="60%">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form ref="operlogDetailFormRef" :labelCol="{ span: 8 }" :model="operlogDetailForm.data"
              :wrapperCol="{ span: 14 }" class="bearjia-detail-page" name="operlogDetailForm">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item label="系统模块" name="title">
              <span>{{ operlogDetailForm.data.title }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="操作类型" name="businessType">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(operlogDetailForm.data.businessType, fatherPageData.sysOperTypeDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="方法名称" name="method">
              <span>{{ operlogDetailForm.data.method }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="请求方式" name="requestMethod">
              <span>{{ operlogDetailForm.data.requestMethod }}</span>
            </a-form-item>
          </a-col>
          <!-- <a-col span="12">
            <a-form-item name="operatorType" label="操作类别">
              <span>{{ BearJiaUtil.getDictLabelByKey(operlogDetailForm.data.operatorType, fatherPageData.sysOperTypeDict) }}</span>
            </a-form-item>
          </a-col> -->
          <a-col span="12">
            <a-form-item label="操作人员" name="operName">
              <span>{{ operlogDetailForm.data.operName }}</span>
            </a-form-item>
          </a-col>
          <!-- <a-col span="12">
            <a-form-item name="deptName" label="部门名称">
              <span>{{ operlogDetailForm.data.deptName }}</span>
            </a-form-item>
          </a-col> -->
          <a-col span="12">
            <a-form-item label="请求URL" name="operUrl">
              <span>{{ operlogDetailForm.data.operUrl }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="主机地址" name="operIp">
              <span>{{ operlogDetailForm.data.operIp }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="操作地点" name="operLocation">
              <span>{{ operlogDetailForm.data.operLocation }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="请求参数" name="operParam">
              <span>{{ operlogDetailForm.data.operParam }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="返回参数" name="jsonResult">
              <span>{{ operlogDetailForm.data.jsonResult }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="操作状态" name="status">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(operlogDetailForm.data.status, fatherPageData.sysCommonStatusDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="错误消息" name="errorMsg">
              <span>{{ operlogDetailForm.data.errorMsg }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="操作时间" name="operTime">
              <span>{{ operlogDetailForm.data.operTime }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
// import { getOperlog } from "@/api/monitor/operlog";
import {reactive, ref} from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 父页面公用数据
const fatherPageData = defineProps({
  sysOperTypeDict: Array,
  sysCommonStatusDict: Array,
});

// 当前页面使用的数据
const pageData = reactive({
  title: '详细页面',
  visible: false,
  operateType: '',
});

//详细Form
const operlogDetailFormRef = ref();
const operlogDetailForm = reactive({data: {}});
// 打开详细窗口
const openModal = (record) => {
  operlogDetailForm.data = record;
  pageData.visible = true;
  // getOperlog(record.operId).then((response) => {
  //   operlogDetailForm.data = response.data;
  //   pageData.visible = true;
  // });
};

// 对外暴露出去
defineExpose({
  openModal,
});
</script>

<style lang="less"></style>
