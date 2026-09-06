<template>
  <div>
    <a-modal v-model:visible="pageData.visible" width="60%" :title="pageData.title">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form class="autoee-detail-page" ref="typeDetailFormRef" name="typeDetailForm" :model="typeDetailForm.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item name="dictType" label="字典类型">
              <span>{{ AutoeeUtil.getDictLabelByKey(typeDetailForm.data.dictType, fatherPageData.dictTypeDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="dictName" label="字典类型名称">
              <span>{{ typeDetailForm.data.dictName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="status" label="状态">
              <span>{{ AutoeeUtil.getDictLabelByKey(typeDetailForm.data.status, fatherPageData.sysNormalDisableDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="remark" label="备注">
              <span>{{ typeDetailForm.data.remark }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { getType } from '@/api/system/dict/type';
import { reactive, ref } from 'vue';
import AutoeeUtil from '@/utils/AutoeeUtil.js';

// 父页面公用数据
const fatherPageData = defineProps({
  dictTypeDict: Array,
  sysNormalDisableDict: Array,
});

// 当前页面使用的数据
const pageData = reactive({
  title: '详细页面',
  visible: false,
  operateType: '',
});

//详细Form
const typeDetailFormRef = ref();
const typeDetailForm = reactive({ data: {} });
// 打开详细窗口
const openModal = (record) => {
  getType(record.dictId).then((response) => {
    typeDetailForm.data = response.data;
    pageData.visible = true;
  });
};

// 对外暴露出去
defineExpose({
  openModal,
});
</script>

<style lang="less"></style>
