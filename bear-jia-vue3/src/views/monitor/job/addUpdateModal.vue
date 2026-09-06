<template>
  <div>
    <a-modal v-model:open="pageData.visible" width="60%" :title="pageData.title" :destroyOnClose="true" @ok="clickModalOk" @cancel="handleModalCancel">
      <a-form ref="jobAddUpdateFormRef" name="jobAddUpdateForm" :model="jobAddUpdateForm.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item name="jobId" label="任务ID" :rules="[{ required: true, message: '任务ID不能为空！' }]">
              <a-input v-model:value="jobAddUpdateForm.data.jobId" disabled="pageData.operateType=='update'" :maxlength="20" placeholder="请填写任务ID"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="jobName" label="任务名称" :rules="[{ required: true, message: '任务名称不能为空！' }]">
              <a-input v-model:value="jobAddUpdateForm.data.jobName" :maxlength="64" placeholder="请填写任务名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="jobGroup" label="任务组名" :rules="[{ required: true, message: '任务组名不能为空！' }]">
              <a-select v-model:value="jobAddUpdateForm.data.jobGroup" :options="fatherPageData.jobGroupDict" placeholder="请选择任务组名"> </a-select>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="invokeTarget" label="调用目标字符串" :rules="[{ required: true, message: '调用目标字符串不能为空！' }]">
              <a-input v-model:value="jobAddUpdateForm.data.invokeTarget" :maxlength="500" placeholder="请填写调用目标字符串"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="cronExpression" label="cron表达式" :rules="[{ required: true, message: 'cron执行表达式不能为空！' }]">
              <a-input v-model:value="jobAddUpdateForm.data.cronExpression" :maxlength="255" placeholder="请填写cron执行表达式"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="misfirePolicy" label="执行错误策略" :rules="[{ required: true, message: '计划执行错误策略不能为空！' }]">
              <a-select v-model:value="jobAddUpdateForm.data.misfirePolicy" :options="fatherPageData.misfirePolicyDict" placeholder="请选择计划执行错误策略"> </a-select>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="concurrent" label="是否并发" :rules="[{}]">
              <a-select v-model:value="jobAddUpdateForm.data.concurrent" :options="fatherPageData.sysYesNoDict" placeholder="请选择是否并发执行"> </a-select>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="status" label="状态" :rules="[{}]">
              <a-select v-model:value="jobAddUpdateForm.data.status" :options="fatherPageData.sysJobStatusDict" placeholder="请选择状态"> </a-select>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="remark" label="备注信息" :rules="[{}]">
              <a-textarea v-model:value="jobAddUpdateForm.data.remark" :maxlength="500" :rows="3" placeholder="请填写备注信息" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup>
import { reactive, ref } from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';
import { getJob, addJob, updateJob } from '@/api/monitor/job';
import { message } from 'ant-design-vue';

// 父页面公用数据
const fatherPageData = defineProps({
  jobGroupDict: Array,
  misfirePolicyDict: Array,
  sysYesNoDict: Array,
  sysJobStatusDict: Array,
});

// 引入父页面方法
const fatherPageFunEmit = defineEmits([]);

// 当前页面使用的数据
const pageData = reactive({
  title: '新增页面',
  visible: false,
  operateType: '',
});

// 新增修改Form
const jobAddUpdateFormRef = ref();
const jobAddUpdateForm = reactive({ data: {} });
// 重置Form
const resetJobAddUpdateForm = () => {
  BearJiaUtil.resetFormFieldsToEmpty(jobAddUpdateForm.data);
};

let fatherPageTablePage;
// 打开新增窗口
const openAddModal = (pFatherPageTablePage) => {
  pageData.operateType = 'add';
  pageData.title = '新增';
  fatherPageTablePage = pFatherPageTablePage;
  pageData.visible = true;
};

// 打开修改窗口
const openUpdateModal = (record, tablePage) => {
  pageData.operateType = 'update';
  pageData.title = '修改';
  fatherPageTablePage = tablePage;
  getJob(record.jobId).then((response) => {
    jobAddUpdateForm.data = response.data;
    pageData.visible = true;
  });
};

// 点击窗口确认
const clickModalOk = (e) => {
  jobAddUpdateFormRef.value
    .validateFields()
    .then((values) => {
      if (pageData.operateType === 'add') {
        addJob(jobAddUpdateForm.data).then((res) => {
          pageData.visible = false;
          resetJobAddUpdateForm();
          fatherPageTablePage.reload();
          BearJiaUtil.messageSuccess('新增操作成功。');
        });
      } else if (pageData.operateType === 'update') {
        updateJob(jobAddUpdateForm.data).then((res) => {
          pageData.visible = false;
          resetJobAddUpdateForm();
          fatherPageTablePage.reload();
          BearJiaUtil.messageSuccess('修改操作成功。');
        });
      }
    })
    .catch((info) => {});
};

// 点击窗口取消
const handleModalCancel = () => {
  resetJobAddUpdateForm();
};

// 对外暴露出去
defineExpose({
  openAddModal,
  openUpdateModal,
});
</script>

<style lang="less"></style>
