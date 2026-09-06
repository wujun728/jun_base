<template>
  <div>
    <a-modal v-model:open="pageDataObj.visible" :title="pageDataObj.title" width="60%">
      <template #footer>
        <a-button @click="pageDataObj.visible = false">关闭</a-button>
      </template>
      <a-form ref="detailFormRef" :labelCol="{ span: 8 }" :model="detailFormObj.data" :wrapperCol="{ span: 14 }"
              class="bearjia-detail-page" name="detailFormObj">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item label="用户账号" name="userName">
              <span>{{ detailFormObj.data.userName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="登录IP地址" name="ipaddr">
              <span>{{ detailFormObj.data.ipaddr }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="登录地点" name="loginLocation">
              <span>{{ detailFormObj.data.loginLocation }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="浏览器类型" name="browser">
              <span>{{ detailFormObj.data.browser }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="操作系统" name="os">
              <span>{{ detailFormObj.data.os }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="登录状态" name="status">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(detailFormObj.data.status, fatherPageData.sysCommonStatusDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="提示消息" name="msg">
              <span>{{ detailFormObj.data.msg }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="访问时间" name="loginTime">
              <span>{{ detailFormObj.data.loginTime }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
// import { getLogininfor } from "@/api/monitor/logininfor";
import {reactive, ref} from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 父页面公用数据
const fatherPageData = defineProps({
  sysCommonStatusDict: Array,
});

// 当前页面使用的数据
const pageDataObj = reactive({
  title: '详细页面',
  visible: false,
  operateType: '',
});

//详细Form
const detailFormRef = ref();
const detailFormObj = reactive({
  data: {}
});
// 打开详细窗口
const openModal = (record) => {
  detailFormObj.data = record;
  pageDataObj.visible = true;
  // getLogininfor(record.infoId).then((response) => {
  //   detailFormObj.data = response.data;
  //   pageDataObj.visible = true;
  // });
};

// 对外暴露出去
defineExpose({
  openModal,
});
</script>

<style lang="less"></style>
