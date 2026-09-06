<template>
  <div class="app-container">
    <el-card shadow="never" body-class="search-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="变量名称" prop="variableName">
          <el-input v-model="queryParams.variableName" placeholder="请输入变量名称" style="width: 240px"
            @keyup.enter="handleQuery" clearable />
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

      <el-table v-loading="loading" :data="variableList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="变量编号" align="center" prop="variableId" />
        <el-table-column label="变量名称" align="center" prop="variableName" :show-overflow-tooltip="true" />
        <el-table-column label="变量类型" align="center" prop="variableType" />
        <el-table-column label="变量内容" align="center" prop="variableContent" :show-overflow-tooltip="true">
          <template #default="scope">
            <span>{{ formatVariableContent(scope.row) }}</span>
          </template>
        </el-table-column>
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

    <!-- 添加或修改变量管理对话框 -->
    <VariableFormDialog ref="variableFormDialog" :visible="open" :title="title" :form="form"
      :submitLoading="submitLoading" @update:visible="open = $event" @submit="submitForm" @cancel="cancel"
      @variable-type-change="handleVariableTypeChange" />
  </div>
</template>

<script setup name="Variable">
import { listVariable, getVariable, delVariable, addVariable, updateVariable } from "@modules/message/api/variable";
import { ElMessage } from "element-plus";
import VariableFormDialog from "./components/VariableFormDialog.vue";
const { proxy } = getCurrentInstance();
const { parseTime } = proxy;

const variableList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const submitLoading = ref(false);

const data = reactive({
  form: {
    variableName: null,
    variableType: null,
    variableContent: null,
    builtInVariable: null
  },
  queryParams: {
    pageNum: 1,
    pageSize: 8,
    variableName: null,
  }
});

const { queryParams, form } = toRefs(data);

/** 查询变量管理列表 */
function getList() {
  loading.value = true;
  listVariable(queryParams.value).then(response => {
    variableList.value = response.rows;
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
    variableId: null,
    variableName: null,
    variableType: null,
    variableContent: null,
    builtInVariable: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
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
  ids.value = selection.map(item => item.variableId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加变量";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _variableId = row.variableId || ids.value
  getVariable(_variableId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "编辑变量";
  });
}

// 当变量类型改变时触发
function handleVariableTypeChange(value) {
  if (value === '内置变量') {
    form.value.builtInVariable = null;
    form.value.variableContent = '';
  } else {
    form.value.variableContent = '';
  }
}

/** 提交按钮 */
async function submitForm() {
  try {
    submitLoading.value = true;
    if (form.value.variableId != null) {
      await updateVariable(form.value);
      proxy.$modal.msgSuccess("编辑成功");
    } else {
      await addVariable(form.value);
      proxy.$modal.msgSuccess("新增成功");
    }

    // 关闭对话框并刷新列表
    open.value = false;
    getList();
  } catch (error) {
    ElMessage.error('操作失败');
  } finally {
    submitLoading.value = false;
  }
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _variableIds = row.variableId || ids.value;
  proxy.$modal.confirm('是否确认删除变量管理编号为"' + _variableIds + '"的数据项？').then(function () {
    return delVariable(_variableIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('modelMessage/variable/export', {
    ...queryParams.value
  }, `variable_${new Date().getTime()}.xlsx`)
}

// 格式化变量内容展示
function formatVariableContent(item) {
  // 内置变量的显示文本配置
  const labels = {
    'time': '发送时间',
    'date': '发送日期',
    'datetime': '发送日期+时间',
    'addresser': '发件人',
    'recipients': '收件人',
    'RandomnDigits': '随机数字',
    'RandomnCharacters': '随机字母',
    'RandomNDigitLetters': '随机数字+字母'
  };
  return item.variableType === '内置变量' ? labels[item.variableContent] || item.variableContent : item.variableContent;
}

getList();
</script>