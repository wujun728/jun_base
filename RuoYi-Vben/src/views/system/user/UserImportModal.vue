<template>
  <BasicModal
    v-bind="$attrs"
    title="导入用户数据"
    @register="registerInnerModal"
    @ok="handleSubmit"
    @cancel="clearFileList"
  >
    <!-- z-index不设置会遮挡模板下载loading -->
    <!-- 手动处理 而不是放入文件就上传 -->
    <UploadDragger
      v-model:fileList="fileList"
      :maxCount="1"
      :showUploadList="true"
      :beforeUpload="() => false"
      accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
    >
      <p class="ant-upload-drag-icon">
        <inbox-outlined />
      </p>
      <p class="ant-upload-text">点击或者拖拽到此处上传文件</p>
    </UploadDragger>
    <div class="flex flex-col gap-2 mt-2">
      <div class="flex flex-row items-center gap-3">
        <span>允许导入xlsx, xls文件</span>
        <a-button
          type="link"
          link
          pre-icon="vscode-icons:file-type-excel"
          @click="downloadExcel(downloadImportTemplate, '用户导入模板')"
          >下载模板</a-button
        >
      </div>
      <div class="flex flex-row gap-2 items-center">
        <span :class="{ 'text-red-500': checked }">是否更新/覆盖已存在的用户数据</span>
        <Switch v-model:checked="checked" />
      </div>
    </div>
  </BasicModal>
</template>

<script setup lang="ts">
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { downloadImportTemplate, userImportData } from '@/api/system/user';
  import { Upload, Switch } from 'ant-design-vue';
  import { InboxOutlined } from '@ant-design/icons-vue';
  import { ref, unref } from 'vue';
  import { downloadExcel } from '@/utils/file/download';
  import { UploadFile } from 'ant-design-vue/es/upload/interface';

  // https://blog.csdn.net/weixin_35498622/article/details/126969884
  const UploadDragger = Upload.Dragger;

  defineOptions({ name: 'UserImportModal' });

  const emit = defineEmits(['register', 'reload']);

  const fileList = ref<UploadFile[]>([]);
  const checked = ref<boolean>(false);

  const [registerInnerModal, { modalLoading, closeModal }] = useModalInner();

  async function handleSubmit() {
    try {
      modalLoading(true);
      if (fileList.value.length !== 1) {
        closeModal();
        return;
      }
      const data = {
        updateSupport: unref(checked),
        file: unref(fileList)[0].originFileObj!,
      };
      await userImportData(data);
      emit('reload');
      closeModal();
    } catch (e) {
      console.warn(e);
      closeModal();
    } finally {
      clearFileList();
      modalLoading(false);
    }
  }

  function clearFileList() {
    fileList.value = [];
  }
</script>

<style scoped></style>
