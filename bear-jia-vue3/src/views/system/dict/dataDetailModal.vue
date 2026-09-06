<template>
  <div>
    <a-modal v-model:open="pageData.visible" :title="pageData.title" width="60%">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form ref="dataDetailFormRef" :labelCol="{ span: 8 }" :model="dataDetailForm.data" :wrapperCol="{ span: 14 }"
              class="bearjia-detail-page" name="dataDetailForm">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item label="字典类型" name="dictType">
              <span>{{ dataDetailForm.data.dictType }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="字典名称" name="dictLabel">
              <span>{{ dataDetailForm.data.dictLabel }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="字典值" name="dictValue">
              <span>{{ dataDetailForm.data.dictValue }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="字典排序" name="dictSort">
              <span>{{ dataDetailForm.data.dictSort }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="状态" name="status">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(dataDetailForm.data.status, fatherPageData.sysNormalDisableDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="样式属性" name="cssClass">
              <span>{{ dataDetailForm.data.cssClass }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="表格回显样式" name="listClass">
              <span>{{ dataDetailForm.data.listClass }}</span>
            </a-form-item>
          </a-col>
          <!-- <a-col span="12">
            <a-form-item name="isDefault" label="是否默认">
              <span>{{ BearJiaUtil.getDictLabelByKey(dataDetailForm.data.isDefault, fatherPageData.sysYesNoDict) }}</span>
            </a-form-item>
          </a-col> -->
          <a-col span="12">
            <a-form-item label="备注" name="remark">
              <span>{{ dataDetailForm.data.remark }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {getData} from '@/api/system/dict/data';
import {reactive, ref} from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 父页面公用数据
const fatherPageData = defineProps({
  dictTypeDict: Array,
  sysYesNoDict: Array,
  sysNormalDisableDict: Array,
});

// 当前页面使用的数据
const pageData = reactive({
  title: '详细页面',
  visible: false,
  operateType: '',
});

//详细Form
const dataDetailFormRef = ref();
const dataDetailForm = reactive({data: {}});
// 打开详细窗口
const openModal = (record) => {
  getData(record.dictCode).then((response) => {
    dataDetailForm.data = response.data;
    pageData.visible = true;
  });
};

// 对外暴露出去
defineExpose({
  openModal,
});
</script>

<style lang="less"></style>
