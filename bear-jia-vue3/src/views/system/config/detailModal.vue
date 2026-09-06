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
            <a-form-item label="参数名称" name="configName">
              <span>{{ detailFormObj.data.configName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="参数键名" name="configKey">
              <span>{{ detailFormObj.data.configKey }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="参数键值" name="configValue">
              <span>{{ detailFormObj.data.configValue }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="系统内置" name="configType">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(detailFormObj.data.configType, fatherPageDataObj.sysYesNoDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="备注" name="remark">
              <span>{{ detailFormObj.data.remark }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {getConfig} from "@/api/system/config";
import {reactive, ref} from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 父页面公用数据
const fatherPageDataObj = defineProps({
  sysYesNoDict: Array,
});

// 当前页面使用的数据
const pageDataObj = reactive({
  title: '详细页面',
  visible: false,
  operateType: '',
});

//详细Form
const detailFormRef = ref();
const detailFormObj = reactive({data: {}});
// 打开详细窗口
const openModal = (record) => {
  getConfig(record.configId).then((response) => {
    detailFormObj.data = response.data;
    pageDataObj.visible = true;
  });
};

// 对外暴露出去
defineExpose({
  openModal,
});
</script>

<style lang="less"></style>
