<script setup name="Invoice" lang="ts">
import { SchemaForm, SchemaTable } from "@/components/Schema";
import { usePage } from "@/hook";
import { modal } from "@/plugins";
import { PayInvoice } from "@/entity/pay/PayInvoice";
import { onMounted, ref } from "vue";
const {
  handleQuery,
  handleUpdate,
  handleAdd,
  resetForm,
  updateForm,
  resetQuery,
  handleDelete,
  handleExport,
  queryParams,
  list,
  total,
  loading,
  form
} = usePage(PayInvoice)
const open = ref(false);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const title = ref("");

// 取消按钮
function cancel() {
  open.value = false;
  resetForm();
}

// 多选框选中数据
function handleSelectionChange(selection: PayInvoice[]) {
  ids.value = selection.map(item => item.invoiceId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAddClick() {
  resetForm();
  open.value = true;
  title.value = "添加发票";
}

/** 修改按钮操作 */
function handleUpdateClick(row: PayInvoice) {
  updateForm(row.invoiceId || ids.value[0]).then(() => {
    open.value = true;
    title.value = "修改发票";
  });
}

/** 提交按钮 */
function submitForm() {
  if (form.value.invoiceId != null) {
    handleUpdate().then(response => {
      modal.msgSuccess("修改成功");
      open.value = false;
    });
  } else {
    handleAdd().then(response => {
      modal.msgSuccess("新增成功");
      open.value = false;
    });
  }
}

/** 删除按钮操作 */
function handleDeleteClick(row: PayInvoice) {
  const _invoiceIds = row.invoiceId || ids.value;
  modal.confirm('是否确认删除发票编号为"' + _invoiceIds + '"的数据项？').then(() => {
    return handleDelete(_invoiceIds);
  }).then(() => {
    modal.msgSuccess("删除成功");
  }).catch(() => { });
}

onMounted(() => {
  handleQuery();
});
</script>
<template>
  <div class="app-container">
    <el-card shadow="never" body-class="search-card">
      <SchemaForm :model="queryParams" inline v-show="showSearch" label-width="100px" :target="PayInvoice"
        v-model="queryParams" schema="query">
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </SchemaForm>
    </el-card>

    <el-card shadow="never" class="mt10">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="Plus" @click="handleAddClick"
            v-hasPermi="['pay:invoice:add']">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdateClick"
            v-hasPermi="['pay:invoice:edit']">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDeleteClick"
            v-hasPermi="['pay:invoice:remove']">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" plain icon="Download" @click="handleExport"
            v-hasPermi="['pay:invoice:export']">导出</el-button>
        </el-col>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="handleQuery"></right-toolbar>
      </el-row>

      <SchemaTable v-loading="loading" :data="list" selection :target="PayInvoice"
        @selection-change="handleSelectionChange">
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdateClick(scope.row)"
              v-hasPermi="['pay:invoice:edit']">修改</el-button>
            <el-button link type="primary" icon="Delete" @click="handleDeleteClick(scope.row)"
              v-hasPermi="['pay:invoice:remove']">删除</el-button>
          </template>
        </el-table-column>
      </SchemaTable>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="handleQuery" />
    </el-card>

    <!-- 添加或修改发票对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <SchemaForm :model="form" label-width="80px" :target="PayInvoice" v-model="form" />
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>