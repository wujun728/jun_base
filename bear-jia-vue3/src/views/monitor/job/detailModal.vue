<template>
  <div>
    <a-modal v-model:open="pageData.visible" :title="pageData.title" width="60%">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form ref="jobDetailFormRef" :labelCol="{ span: 8 }" :model="jobDetailForm.data" :wrapperCol="{ span: 14 }"
              class="bearjia-detail-page" name="jobDetailForm">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item label="任务ID" name="jobId">
              <span>{{ jobDetailForm.data.jobId }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="任务名称" name="jobName">
              <span>{{ jobDetailForm.data.jobName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="任务组名" name="jobGroup">
              <span>{{ BearJiaUtil.getDictLabelByKey(jobDetailForm.data.jobGroup, fatherPageData.jobGroupDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="调用目标字符串" name="invokeTarget">
              <span>{{ jobDetailForm.data.invokeTarget }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="cron执行表达式" name="cronExpression">
              <span>{{ jobDetailForm.data.cronExpression }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="计划执行错误策略" name="misfirePolicy">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(jobDetailForm.data.misfirePolicy, fatherPageData.misfirePolicyDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="是否并发执行" name="concurrent">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(jobDetailForm.data.concurrent, fatherPageData.sysYesNoDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="状态" name="status">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(jobDetailForm.data.status, fatherPageData.sysJobStatusDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="备注信息" name="remark">
              <span>{{ jobDetailForm.data.remark }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {getJob} from '@/api/monitor/job';
import {reactive, ref} from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

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
const jobDetailForm = reactive({data: {}});
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
