<template>
  <div class="app-container">
    <el-card shadow="never" body-class="search-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="表单名称" prop="formName">
          <el-input v-model="queryParams.formName" placeholder="请输入表单名称" clearable @keyup.enter="handleQuery" />
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
          <el-button type="primary" plain icon="Plus" @click="handleAdd"
            v-hasPermi="['form:template:add']">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate"
            v-hasPermi="['form:template:edit']">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
            v-hasPermi="['form:template:remove']">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" plain icon="Download" @click="handleExport"
            v-hasPermi="['form:template:export']">导出</el-button>
        </el-col>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="表单ID" align="center" prop="formId" />
        <el-table-column label="表单名称" align="center" prop="formName">
          <template #default="scope">
            <el-link type="primary" @click="showFormContent(scope.row)">{{ scope.row.formName }}</el-link>
          </template>
        </el-table-column>
        <el-table-column label="表单版本" align="center" prop="formVersion" />
        <el-table-column label="发布状态" align="center" prop="formStatus" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
              v-hasPermi="['form:template:edit']">修改</el-button>
            <el-button link type="primary" icon="Edit" @click="handleEdit(scope.row)"
              v-hasPermi="['form:template:edit']">编辑表单</el-button>
            <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
              v-hasPermi="['form:template:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>

    <!-- 添加或修改单模板对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="templateRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="表单名称" prop="formName">
          <el-input v-model="form.formName" placeholder="请输入表单名称" />
        </el-form-item>
        <el-form-item label="表单版本" prop="formVersion">
          <el-input v-model="form.formVersion" placeholder="请输入表单版本" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 表单内容对话框 -->
    <el-dialog title="表单内容" v-model="showForm.formContentDialogVisible" width="600px">
      <v-form-render :form-json="showForm.formSchema" ref="vFormRef" :key="showForm.formId">
      </v-form-render>
      <template #footer>
        <el-button @click="submitFormData">提交</el-button>
        <el-button @click="showForm.formContentDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup name="Template">
import { addData } from "@modules/form/api/data";
import { listTemplate, getTemplate, delTemplate, addTemplate, updateTemplate } from "@modules/form/api/template";
import tab from "@/plugins/tab";
import { onMounted, reactive } from "vue";
import { useRouter } from "vue-router";
const router = useRouter();
const { proxy } = getCurrentInstance();

const templateList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const showForm = reactive({
  formContentDialogVisible: false,
  formSchema: "",
  formId: null
})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    formName: null
  },
  rules: {
    formName: [
      { required: true, message: "表单名称不能为空", trigger: "blur" }
    ],
    formVersion: [
      { required: true, message: "表单版本不能为空", trigger: "blur" }
    ],
    formStatus: [
      { required: true, message: "发布状态不能为空", trigger: "change" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询单模板列表 */
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
    formId: null,
    formName: null,
    formSchema: null,
    formVersion: null,
    formStatus: null,
    remark: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    delFlag: null
  };
  proxy.resetForm("templateRef");
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
  ids.value = selection.map(item => item.formId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  router.push("/tool/build");
}

function handleEdit(row) {
  tab.openPage({
    path: '/tool/build',
    query: {
      id: row.formId
    }
  })
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _formId = row.formId || ids.value
  getTemplate(_formId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改单模板";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["templateRef"].validate(valid => {
    if (valid) {
      if (form.value.formId != null) {
        updateTemplate(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addTemplate(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _formIds = row.formId || ids.value;
  proxy.$modal.confirm('是否确认删除单模板编号为"' + _formIds + '"的数据项？').then(function () {
    return delTemplate(_formIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('form/template/export', {
    ...queryParams.value
  }, `template_${new Date().getTime()}.xlsx`)
}
const vFormRef = ref();
/** 显示表单内容 */
function showFormContent(row) {
  getTemplate(row.formId).then(response => {
    showForm.formSchema = JSON.parse(response.data.formSchema || "{}");
    showForm.formId = row.formId;
    showForm.formContentDialogVisible = true;
  });
}
function submitFormData() {
  nextTick(() => {
    vFormRef.value.getFormData().then(formData => {
      const data = JSON.stringify(formData)
      addData({
        formId: showForm.formId,
        dataContent: data
      }).then(() => {
        showForm.formContentDialogVisible = false;
        proxy.$modal.msgSuccess("提交成功");
      });
    });
  })
}

onMounted(() => {
  getList();
});
</script>
