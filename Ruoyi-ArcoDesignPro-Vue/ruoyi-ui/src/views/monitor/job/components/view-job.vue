<template>
  <a-modal :visible="open" :title="state.title" width="50vw" @cancel="handleCancel">
    <a-space direction="vertical" size="large" fill>
      <a-descriptions title="任务详细" :column="{xs:1, md:2, lg:2}">
        <a-descriptions-item label="任务编号"> {{ state.form.jobId }}</a-descriptions-item>
        <a-descriptions-item label="任务名称"> {{ state.form.jobName }}</a-descriptions-item>
        <a-descriptions-item label="任务分组"> {{ jobGroupFormat(state.form) }}</a-descriptions-item>
        <a-descriptions-item label="创建时间"> {{ state.form.createTime }}</a-descriptions-item>
        <a-descriptions-item label="cron表达式"> {{ state.form.cronExpression }}</a-descriptions-item>
        <a-descriptions-item label="下次执行时间"> {{ state.form.nextValidTime }}</a-descriptions-item>
        <a-descriptions-item label="调用目标方法"> {{ state.form.invokeTarget }}</a-descriptions-item>
        <a-descriptions-item label="任务状态">
          <div v-if="state.form.status === '0'">
            正常
          </div>
          <div v-else-if="state.form.status === '1'">
            暂停
          </div>
        </a-descriptions-item>
        <a-descriptions-item label="是否并发">
          <div v-if="state.form.concurrent === '0'">
            允许
          </div>
          <div v-else-if="state.form.concurrent === '1'">
            禁止
          </div>
        </a-descriptions-item>
        <a-descriptions-item label="执行策略">
          <div v-if="state.form.misfirePolicy === '0'">默认策略</div>
          <div v-else-if="state.form.misfirePolicy === '1'">立即执行</div>
          <div v-else-if="state.form.misfirePolicy === '2'">执行一次</div>
          <div v-else-if="state.form.misfirePolicy === '3'">放弃执行</div>
        </a-descriptions-item>
      </a-descriptions>
    </a-space>
    <template #footer>
      <a-button @click="handleCancel">取消</a-button>
    </template>
  </a-modal>
</template>

<script lang="ts" setup>
import {computed, reactive, ref, watch} from "vue";
import {getDicts} from "@/api/system/dict-data";
import {Message} from "@arco-design/web-vue";
import {getJob, JobRecord} from "@/api/monitor/job";
import {selectDictLabel} from "@/utils/common";

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  id: {
    type: Number,
    default: undefined,
  },
});

const open = computed(() => {
  return !!props.visible;
});

const state = reactive({
  title: '查看任务',
  form: {
    jobId: undefined,
    jobName: undefined,
    jobGroup: undefined,
    invokeTarget: undefined,
    cronExpression: undefined,
    createTime: undefined,
    nextValidTime: undefined,
    misfirePolicy: '1',
    concurrent: '1',
    status: "0"
  },
  jobGroupOptions: [],
  jobStatusOptions: [],
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
const jobGroupFormat = (record: JobRecord) => {
  return selectDictLabel(state.jobGroupOptions, record.jobGroup as string);
};

const reset = () => {
  state.title = '新增任务';
  state.form = {
    jobId: undefined,
    jobName: undefined,
    jobGroup: undefined,
    invokeTarget: undefined,
    cronExpression: undefined,
    createTime: undefined,
    nextValidTime: undefined,
    misfirePolicy: '1',
    concurrent: '1',
    status: "0"
  };
};

const emits = defineEmits(['update:visible']);
const handleCancel = () => {
  emits('update:visible', false);
  reset();
};

const getJobForm = async (id: number) => {
  const {data, code, msg} = await getJob(id);
  if (code && code === 200) {
    state.form = data;
  } else {
    Message.error(msg)
  }
};

watch(
    () => props.visible,
    (value) => {
      if (value) {
        state.title = '查看任务';
        if (props.id) {
          getJobForm(props.id);
        } else {
          reset();
        }
      } else {
        reset();
      }
    }
    , {immediate: true}
);

</script>

<style scoped>

</style>
