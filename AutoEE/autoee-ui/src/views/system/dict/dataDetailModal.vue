<template>
  <div>
    <a-modal v-model:visible="pageData.visible" width="60%" :title="pageData.title">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form class="autoee-detail-page" ref="dataDetailFormRef" name="dataDetailForm" :model="dataDetailForm.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item name="dictType" label="字典类型">
              <span>{{ dataDetailForm.data.dictType }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="dictLabel" label="字典名称">
              <span>{{ dataDetailForm.data.dictLabel }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="dictValue" label="字典值">
              <span>{{ dataDetailForm.data.dictValue }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="dictSort" label="字典排序">
              <span>{{ dataDetailForm.data.dictSort }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="status" label="状态">
              <span>{{ AutoeeUtil.getDictLabelByKey(dataDetailForm.data.status, fatherPageData.sysNormalDisableDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="cssClass" label="样式属性">
              <span>{{ dataDetailForm.data.cssClass }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="listClass" label="表格回显样式">
              <span>{{ dataDetailForm.data.listClass }}</span>
            </a-form-item>
          </a-col>
          <!-- <a-col span="12">
            <a-form-item name="isDefault" label="是否默认">
              <span>{{ AutoeeUtil.getDictLabelByKey(dataDetailForm.data.isDefault, fatherPageData.sysYesNoDict) }}</span>
            </a-form-item>
          </a-col> -->
          <a-col span="12">
            <a-form-item name="remark" label="备注">
              <span>{{ dataDetailForm.data.remark }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { getData } from '@/api/system/dict/data';
import { reactive, ref } from 'vue';
import AutoeeUtil from '@/utils/AutoeeUtil.js';

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
const dataDetailForm = reactive({ data: {} });
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
