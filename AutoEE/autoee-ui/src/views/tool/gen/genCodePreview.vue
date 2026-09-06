<template>
  <div>
    <a-modal v-model:visible="pageData.visible" width="95%" style="top: 10px; height: 95%" title="代码预览" :destroyOnClose="true">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-tabs v-model:activeKey="pageData.activePanelKey" size="default" hide-add>
        <a-tab-pane v-for="(value, key) in pageData.preview.data" :tab="key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'))" :key="key">
          <!-- tab越多，点击切换tab时，触发的次数越多，如果在里面渲染组件，会造成组件多次执行生命周期 -->
          <!-- 增加 v-if="pageData.activePanelKey === key" 判断，不是切换的key不渲染 -->
          <pre v-if="pageData.activePanelKey === key" v-highlightjs><code> {{highlightedCode(value, key)}} </code></pre>
        </a-tab-pane>
      </a-tabs>
    </a-modal>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import AutoeeUtil from '@/utils/AutoeeUtil.js';
import { previewTable } from '@/api/tool/gen';

// 当前页面使用的数据
const pageData = reactive({
  visible: false,
  preview: [],
  activePanelKey: '',
});

pageData.activePanelKey = 'vm/java/domain.java.vm';

// 基本信息Form
const genBasicInfoFormRef = ref();
const genBasicInfoForm = reactive({ visible: false, data: {} });
// 重置genBasicInfoForm
const resetgenBasicInfoForm = () => {
  AutoeeUtil.resetFormFieldsToEmpty(genBasicInfoForm.data);
  // 值为数组时，特殊处理下
  // genBasicInfoForm.data.postIds = [];
  // genBasicInfoForm.data.roleIds = [];
};

let fatherTablePage;
// 打开修改窗口
const openPreviewModal = (record) => {
  pageData.visible = true;
  previewTable(record.tableId).then((response) => {
    // console.log('response.data=' + JSON.stringify(response.data));
    pageData.preview.data = response.data;
  });
};

// 高亮显示
const highlightedCode = (code, key) => {
  console.log('key=' + key);
  return code;
};

// 对外暴露出去
defineExpose({
  openPreviewModal,
});
</script>

<style lang="less"></style>
