<template>
  <div>
    <a-modal v-model:open="pageDataObj.visible" :title="pageDataObj.title" width="80%">
      <template #footer>
        <a-button @click="pageDataObj.visible = false">关闭</a-button>
      </template>
      <a-form ref="detailFormRef" :labelCol="{ span: 8 }" :model="detailFormObj.data" :wrapperCol="{ span: 14 }"
              class="bearjia-detail-page" name="detailFormObj">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item label="公告标题" name="noticeTitle">
              <span>{{ detailFormObj.data.noticeTitle }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="公告类型" name="noticeType">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(detailFormObj.data.noticeType, fatherPageDataObj.sysNoticeTypeDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="24">
            <a-form-item label="公告内容" name="noticeContent" :labelCol="{ span: 4 }" :wrapperCol="{ span: 20 }">
              <div class="notice-content-display" v-html="detailFormObj.data.noticeContent"></div>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="公告状态" name="status">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(detailFormObj.data.status, fatherPageDataObj.sysNoticeStatusDict)
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
import {getNotice} from "@/api/system/notice";
import {reactive, ref} from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 父页面公用数据
const fatherPageDataObj = defineProps({
  sysNoticeTypeDict: Array,
  sysNoticeStatusDict: Array,
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
  getNotice(record.noticeId).then((response) => {
    detailFormObj.data = response.data;
    pageDataObj.visible = true;
  });
};

// 对外暴露出去
defineExpose({
  openModal,
});
</script>

<style lang="less" scoped>
.notice-content-display {
  min-height: 200px;
  max-height: 400px;
  overflow-y: auto;
  padding: 12px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  background: #fafafa;

  // 富文本内容样式
  :deep(h1), :deep(h2), :deep(h3), :deep(h4), :deep(h5), :deep(h6) {
    margin: 16px 0 8px 0;
    color: #333;
    font-weight: 600;
  }

  :deep(p) {
    margin: 8px 0;
    line-height: 1.6;
    color: #666;
  }

  :deep(ul), :deep(ol) {
    margin: 8px 0;
    padding-left: 20px;

    li {
      margin: 4px 0;
      line-height: 1.6;
      color: #666;
    }
  }

  :deep(img) {
    max-width: 100%;
    height: auto;
    border-radius: 4px;
    margin: 8px 0;
  }

  :deep(video) {
    max-width: 100%;
    height: auto;
    border-radius: 4px;
    margin: 8px 0;
  }

  :deep(blockquote) {
    margin: 16px 0;
    padding: 12px 16px;
    background: #f6f8fa;
    border-left: 4px solid #1890ff;
    color: #666;
  }

  :deep(code) {
    padding: 2px 4px;
    background: #f5f5f5;
    border-radius: 3px;
    font-family: 'Courier New', monospace;
    color: #e74c3c;
  }

  :deep(pre) {
    margin: 16px 0;
    padding: 12px;
    background: #f8f8f8;
    border-radius: 6px;
    overflow-x: auto;

    code {
      background: none;
      color: #333;
      padding: 0;
    }
  }

  :deep(table) {
    width: 100%;
    border-collapse: collapse;
    margin: 16px 0;

    th, td {
      padding: 8px 12px;
      border: 1px solid #e8e8e8;
      text-align: left;
    }

    th {
      background: #fafafa;
      font-weight: 600;
    }
  }

  :deep(a) {
    color: #1890ff;
    text-decoration: none;

    &:hover {
      text-decoration: underline;
    }
  }
}
</style>
