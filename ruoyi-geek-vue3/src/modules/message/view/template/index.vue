<template>
  <div class="app-container">
    <el-card shadow="never" body-class="search-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="模版名称" prop="templateName">
          <el-input v-model="queryParams.templateName" placeholder="请输入模版名称" clearable style="width: 240px"
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="模版类型" prop="templateType">
          <el-select v-model="queryParams.templateType" placeholder="请选择模版类型" clearable style="width: 240px">
            <el-option v-for="dict in template_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt10">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" plain icon="Download" @click="handleExport">导出</el-button>
        </el-col>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="模版编号" align="center" prop="templateId" />
        <el-table-column label="模版名称" align="center" prop="templateName" :show-overflow-tooltip="true" />
        <el-table-column label="模版CODE" align="center" prop="templateCode" :show-overflow-tooltip="true" />
        <el-table-column label="模版类型" align="center" prop="templateType" width="120">
          <template #default="scope">
            <dict-tag :options="template_type" :value="scope.row.templateType" />
          </template>
        </el-table-column>
        <el-table-column label="模版内容" align="center" prop="templateContent" :show-overflow-tooltip="true" />
        <el-table-column label="变量" align="center" prop="templateVariable" :show-overflow-tooltip="true" />
        <el-table-column label="场景说明" align="center" prop="remark" :show-overflow-tooltip="true" />
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>

    <!-- 添加或修改模版管理对话框 -->
    <TemplateFormDialog :visible="open" :title="title" :formData="form" :variable="variable"
      @update:visible="open = $event" @submit="handleFormSubmit" @cancel="cancel" ref="templateFormDialogRef" />
  </div>
</template>

<script setup name="Template">
import { listTemplate, getTemplate, delTemplate, addTemplate, updateTemplate } from "@modules/message/api/template";
import { selectVariable } from "@modules/message/api/variable";
import TemplateFormDialog from './components/TemplateFormDialog.vue';

const { proxy } = getCurrentInstance();
const { template_type } = proxy.useDict("template_type");
const { parseTime } = proxy;

const templateList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const variable = ref([]);
const templateFormDialogRef = ref(null);

const data = reactive({
  form: { templateContent: '' },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    templateName: null,
    templateType: null,
  }
});

const { queryParams, form } = toRefs(data);

/** 查询模版管理列表 */
function getList() {
  loading.value = true;
  listTemplate(queryParams.value).then(response => {
    templateList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    templateId: null, templateName: null, templateCode: null, templateType: null, templateContent: '',
    templateVariable: [], createBy: null, createTime: null, updateBy: null, updateTime: null, remark: null
  };
  if (templateFormDialogRef.value) {
    templateFormDialogRef.value.resetForm();
  }
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.templateId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  getVariable();
  open.value = true;
  title.value = "添加模版";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  getVariable();
  const _templateId = row.templateId || ids.value
  getTemplate(_templateId).then(response => {
    form.value = response.data;
    form.value.templateVariable = response.data.templateVariable.split('/');
    open.value = true;
    title.value = "编辑模版";
  });
}

/** 处理表单提交 */
function handleFormSubmit(formData) {
  const submitData = { ...formData };
  submitData.templateVariable = submitData.templateVariable.join('/');

  if (submitData.templateId != null) {
    updateTemplate(submitData).then(response => {
      proxy.$modal.msgSuccess("编辑模版成功");
      open.value = false;
      getList();
    });
  } else {
    addTemplate(submitData).then(response => {
      proxy.$modal.msgSuccess("新增模版成功");
      open.value = false;
      getList();
    });
  }
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _templateIds = row.templateId || ids.value;
  proxy.$modal.confirm('是否确认删除模版管理编号为"' + _templateIds + '"的数据项？').then(function () {
    return delTemplate(_templateIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('modelMessage/template/export', {
    ...queryParams.value
  }, `template_${new Date().getTime()}.xlsx`)
}

async function getVariable() {
  try {
    variable.value = (await selectVariable()).data;
  } catch (error) {
    console.error('未获取到变量信息', error);
  }
}

getVariable();
getList();
</script>