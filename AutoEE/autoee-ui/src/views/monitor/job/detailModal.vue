<template>
  <div>
    <a-modal v-model:visible="pageData.visible" width="60%" :title="pageData.title">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form class="autoee-detail-page" ref="jobDetailFormRef" name="jobDetailForm" :model="jobDetailForm.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item name="jobId" label="任务ID">
              <span>{{ jobDetailForm.data.jobId }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="jobName" label="任务名称">
              <span>{{ jobDetailForm.data.jobName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="jobGroup" label="任务组名">
              <span>{{ AutoeeUtil.getDictLabelByKey(jobDetailForm.data.jobGroup, fatherPageData.jobGroupDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="invokeTarget" label="调用目标字符串">
              <span>{{ jobDetailForm.data.invokeTarget }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="cronExpression" label="cron执行表达式">
              <span>{{ jobDetailForm.data.cronExpression }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="misfirePolicy" label="计划执行错误策略">
              <span>{{ AutoeeUtil.getDictLabelByKey(jobDetailForm.data.misfirePolicy, fatherPageData.misfirePolicyDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="concurrent" label="是否并发执行">
              <span>{{ AutoeeUtil.getDictLabelByKey(jobDetailForm.data.concurrent, fatherPageData.sysYesNoDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="status" label="状态">
              <span>{{ AutoeeUtil.getDictLabelByKey(jobDetailForm.data.status, fatherPageData.sysJobStatusDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="remark" label="备注信息">
              <span>{{ jobDetailForm.data.remark }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { getJob } from '@/api/monitor/job';
import { reactive, ref } from 'vue';
import AutoeeUtil from '@/utils/AutoeeUtil.js';

// 父页面公用数据
const fatherPageData = defineProps({
  jobGroupDict: Array,
  misfirePolicyDict: Array,
  sysYesNoDict: Array,
  sysJobStatusDict: Array,
});

// 当前页面使用的数据
const pageData = reactive({
  title: '详细页面',
  visible: false,
  operateType: '',
});

//详细Form
const jobDetailFormRef = ref();
const jobDetailForm = reactive({ data: {} });
// 打开详细窗口
const openModal = (record) => {
  getJob(record.jobId).then((response) => {
    jobDetailForm.data = response.data;
    pageData.visible = true;
  });
};

// 对外暴露出去
defineExpose({
  openModal,
});
</script>

<style lang="less"></style>
