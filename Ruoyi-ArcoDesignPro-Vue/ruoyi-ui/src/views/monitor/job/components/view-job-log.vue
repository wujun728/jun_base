<template>
  <a-modal :visible="open" :title="state.title" width="50vw" @cancel="handleCancel">
    <a-space direction="vertical" size="large" fill>
      <a-descriptions title="详细信息" :column="{xs:1, md:2, lg:2}">
        <a-descriptions-item label="日志序号"> {{ form.jobLogId }}</a-descriptions-item>
        <a-descriptions-item label="任务名称"> {{ form.jobName }}</a-descriptions-item>
        <a-descriptions-item label="任务分组"> {{ jobGroupFormat(form.jobGroup) }}</a-descriptions-item>
        <a-descriptions-item label="执行时间"> {{ form.createTime }}</a-descriptions-item>
        <a-descriptions-item label="调用方法"> {{ form.invokeTarget }}</a-descriptions-item>
        <a-descriptions-item label="日志信息"> {{ form.jobMessage }}</a-descriptions-item>
        <a-descriptions-item label="执行状态">
          <div v-if="form.status === '0'">
            正常
          </div>
          <div v-else-if="form.status === '1'">
            失败
          </div>
        </a-descriptions-item>
        <a-descriptions-item v-if="form.status === '1'" label="异常信息">{{ form.exceptionInfo }}</a-descriptions-item>
      </a-descriptions>
    </a-space>
    <template #footer>
      <a-button @click="handleCancel">取消</a-button>
    </template>
  </a-modal>
</template>

<script lang="ts" setup>
import {computed, reactive} from "vue";
import {getDicts} from "@/api/system/dict-data";
import {selectDictLabel} from "@/utils/common";
import {JobLogRecord} from "@/api/monitor/job-log";

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  jobLog: {
    type: Object,
    default: undefined,
  },
});

const open = computed(() => {
  return !!props.visible;
});

const state = reactive({
  title: '操作日志详细',
  jobGroupOptions: [],
  jobStatusOptions: [],
});

const form = computed(() => {
  let jobLog: JobLogRecord = {
    jobLogId: 0,
    jobName: '',
    jobGroup: '',
    createTime: '',
    invokeTarget: '',
    jobMessage: '',
    status: '',
    exceptionInfo: '',
  };
  if (props.jobLog) {
    jobLog = props.jobLog;
  }
  return jobLog;
});

// 初始化字典
const initData = async () => {
  const {data: jobGroup} = await getDicts('sys_job_group');
  state.jobGroupOptions = jobGroup;
  const {data: jobStatus} = await getDicts('sys_job_status');
  state.jobStatusOptions = jobStatus;
};
initData();

// 任务组名字典翻译
const jobGroupFormat = (jobGroup: string) => {
  return selectDictLabel(state.jobGroupOptions, jobGroup);
};

const reset = () => {
  state.title = '操作日志详细';
};

const emits = defineEmits(['update:visible']);
const handleCancel = () => {
  emits('update:visible', false);
  reset();
};

</script>

<style scoped>

</style>
