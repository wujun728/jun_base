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
            <a-form-item label="岗位编码" name="postCode">
              <span>{{ detailFormObj.data.postCode }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="岗位名称" name="postName">
              <span>{{ detailFormObj.data.postName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="显示顺序" name="postSort">
              <span>{{ detailFormObj.data.postSort }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="岗位状态" name="status">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(detailFormObj.data.status, fatherPageDataObj.sysNormalDisableDict)
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
import {getPost} from "@/api/system/post";
import {reactive, ref} from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 父页面公用数据
const fatherPageDataObj = defineProps({
  sysNormalDisableDict: Array,
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
  getPost(record.postId).then((response) => {
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
