<template>
  <a-modal :visible="open" :title="state.title" width="50vw" @cancel="handleCancel" @ok="handleConfirm">
    <a-form ref="formRef" :model="state.form" :rules="state.rules" auto-label-width>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="任务名称" field="jobName">
            <a-input v-model="state.form.jobName" placeholder="请输入任务名称"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item field="jobGroup" :label="'任务分组'">
            <a-select v-model="state.form.jobGroup" placeholder="请选择">
              <a-option
                  v-for="dict in state.jobGroupOptions"
                  :key="dict.dictValue"
                  :value="dict.dictValue"
                  :label="dict.dictLabel"
              >{{ dict.dictLabel }}
              </a-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="调用方法" field="invokeTarget">
            <a-input v-model="state.form.invokeTarget" placeholder="请输入调用目标字符串"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
              label="cron表达式" field="cronExpression" tooltip="Bean调用示例：ryTask.ryParams('ry')
          Class类调用示例：com.ruoyi.quartz.task.RyTask.ryParams('ry')
          参数说明：支持字符串，布尔类型，长整型，浮点型，整型">
            <cron-input  v-model="state.form.cronExpression"></cron-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="执行策略" field="misfirePolicy">
            <a-radio-group v-model="state.form.misfirePolicy">
              <a-radio label="1" value="1">立即执行</a-radio>
              <a-radio label="2" value="2">执行一次</a-radio>
              <a-radio label="3" value="3">放弃执行</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="是否并发" field="concurrent">
            <a-radio-group v-model="state.form.concurrent">
              <a-radio label="0" value="0">允许</a-radio>
              <a-radio label="1" value="1">禁止</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item field="status" :label="'状态'">
            <a-radio-group v-model="state.form.status">
              <a-radio
                  v-for="dict in state.jobStatusOptions"
                  :key="dict.dictValue"
                  :value="dict.dictValue"
                  :label="dict.dictLabel"
              >{{ dict.dictLabel }}
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script lang="ts" setup>
import {computed, reactive, ref, watch} from "vue";
import {DictDataRecord, getDicts} from "@/api/system/dict-data";
import {Form, Message} from "@arco-design/web-vue";
import {addJob, getJob, updateJob} from "@/api/monitor/job";
import CronInput from "@dangojs/vue3-cron";

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
  title: '新增任务',
  form: {
    jobId: undefined,
    jobName: undefined,
    jobGroup: undefined,
    invokeTarget: undefined,
    cronExpression: undefined,
    misfirePolicy: '1',
    concurrent: '1',
    status: "0"
  },
  // 表单校验
  rules: {
    jobName: [
      {required: true, message: "任务名称不能为空", trigger: "blur"}
    ],
    invokeTarget: [
      {required: true, message: "调用目标字符串不能为空", trigger: "blur"}
    ],
    cronExpression: [
      {required: true, message: "cron执行表达式不能为空", trigger: "blur"}
    ]
  },
  jobGroupOptions: [] as DictDataRecord[],
  jobStatusOptions: [] as DictDataRecord[],
});

// 初始化字典
const initData = async () => {
  const {data: jobGroup} = await getDicts('sys_job_group');
  state.jobGroupOptions = jobGroup;
  const {data: jobStatus} = await getDicts('sys_job_status');
  state.jobStatusOptions = jobStatus;
};
initData();

const reset = () => {
  state.title = '新增任务';
  state.form = {
    jobId: undefined,
    jobName: undefined,
    jobGroup: undefined,
    invokeTarget: undefined,
    cronExpression: undefined,
    misfirePolicy: '1',
    concurrent: '1',
    status: "0"
  };
};

const emits = defineEmits(['update:visible', 'refreshDataList']);
const handleCancel = () => {
  emits('update:visible', false);
  reset();
};

const formRef = ref<typeof Form>();
const handleConfirm = () => {
  formRef.value?.validate().then((error: any) => {
    if (!error) {
      if (state.form.jobId !== undefined) {
        updateJob(state.form).then(() => {
          Message.success("修改成功");
          emits('update:visible', false);
          emits('refreshDataList');
        });
      } else {
        addJob(state.form).then(() => {
          Message.success("新增成功");
          emits('update:visible', false);
          emits('refreshDataList');
        });
      }
    }
  });
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
        state.title = '修改任务';
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
