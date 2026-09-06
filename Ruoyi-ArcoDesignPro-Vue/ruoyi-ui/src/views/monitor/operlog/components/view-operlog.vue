<template>
  <a-modal :visible="open" :title="state.title" width="50vw" @cancel="handleCancel">
    <a-space direction="vertical" size="large" fill>
      <a-descriptions title="操作日志详细" :column="{xs:1, md:1, lg:1}">
        <a-descriptions-item label="操作模块"> {{ form.title }}</a-descriptions-item>
        <a-descriptions-item label="登录信息"> {{ form.operName }} / {{ form.operIp }} / {{
            form.operLocation
          }}
        </a-descriptions-item>
        <a-descriptions-item label="请求地址"> {{ form.operUrl }}</a-descriptions-item>
        <a-descriptions-item label="请求方式"> {{ form.requestMethod }}</a-descriptions-item>
        <a-descriptions-item label="操作方法"> {{ form.method }}</a-descriptions-item>
        <a-descriptions-item label="请求参数"> {{ form.operParam }}</a-descriptions-item>
        <a-descriptions-item label="返回参数"> {{ form.jsonResult }}</a-descriptions-item>
        <a-descriptions-item label="操作状态">
          <div v-if="form.status === 0">
            <a-tag>正常</a-tag>
          </div>
          <div v-else-if="form.status === 1">
            <a-tag color="red">失败</a-tag>
          </div>
        </a-descriptions-item>
        <a-descriptions-item label="消耗时间"> {{ form.costTime }}毫秒</a-descriptions-item>
        <a-descriptions-item label="操作时间"> {{ form.operTime }}</a-descriptions-item>
        <a-descriptions-item v-if="form.status === 1" label="异常信息"> {{ form.errorMsg }}</a-descriptions-item>
      </a-descriptions>
    </a-space>
    <template #footer>
      <a-button @click="handleCancel">取消</a-button>
    </template>
  </a-modal>
</template>

<script lang="ts" setup>

import {computed, reactive} from "vue";
import {OperlogRecord} from "@/api/monitor/operlog";

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  operlog: {
    type: Object,
    default: undefined,
  },
});
const open = computed(() => {
  return !!props.visible;
});
const form = computed(() => {
  let operlog: any = {
    title: '',
    operName: '',
    operUrl: '',
    operIp: '',
    operLocation: '',
    requestMethod: '',
    operParam: '',
    jsonResult: '',
    status: 0,
    costTime: 0,
    operTime: '',
    errorMsg: '',
  };
  if (props.operlog) {
    operlog = props.operlog;
  }
  return operlog as OperlogRecord;
});

const state = reactive({
  title: '操作日志详细',
});

const emits = defineEmits(['update:visible']);
const handleCancel = () => {
  emits('update:visible', false);
};

</script>

<style scoped>

</style>
