<template>
  <BasicModal
    v-bind="$attrs"
    title="预览"
    :width="700"
    :can-fullscreen="true"
    :showCancelBtn="false"
    :showOkBtn="false"
    @register="registerInnerModal"
  >
    <div class="flex flex-col" v-if="noticeData">
      <!-- html标签<>会被转义  所以不存在xss注入问题 -->
      <!-- eslint-disable-next-line vue/no-v-html -->
      <div v-html="noticeData.noticeContent"></div>
      <div class="text-left mt-3 color-gray-400" v-text="noticeData.createTime"></div>
    </div>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { ref } from 'vue';

  defineOptions({ name: 'NoticePreviewModal' });

  const noticeData = ref();
  const [registerInnerModal, { setModalProps }] = useModalInner((record: Recordable) => {
    noticeData.value = record;
    const { noticeTitle } = record;
    if (noticeTitle) {
      setModalProps({ title: noticeTitle });
    }
  });
</script>
