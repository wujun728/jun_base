<template>
  <div>
    <a-modal v-model:open="pageData.visible" :title="pageData.title" width="60%">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form ref="typeDetailFormRef" :labelCol="{ span: 8 }" :model="typeDetailForm.data" :wrapperCol="{ span: 14 }"
              class="bearjia-detail-page" name="typeDetailForm">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item label="字典类型" name="dictType">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(typeDetailForm.data.dictType, fatherPageData.dictTypeDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="字典类型名称" name="dictName">
              <span>{{ typeDetailForm.data.dictName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="状态" name="status">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(typeDetailForm.data.status, fatherPageData.sysNormalDisableDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="备注" name="remark">
              <span>{{ typeDetailForm.data.remark }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {getType} from '@/api/system/dict/type';
import {reactive, ref} from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

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
const typeDetailForm = reactive({data: {}});
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
