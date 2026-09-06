<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="备注" prop="logicRemark">
        <el-input
          v-model="queryParams.logicRemark"
          placeholder="请输入备注"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门id" prop="deptId">
        <el-input
          v-model="queryParams.deptId"
          placeholder="请输入部门id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['logic:LogicLogic:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['logic:LogicLogic:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['logic:LogicLogic:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['logic:LogicLogic:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="LogicLogicList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" width="220"/>
      <el-table-column label="备注" align="center" prop="logicRemark" width="220"/>
      <el-table-column label="用户id" align="center" prop="userId" width="220"/>
      <el-table-column label="部门id" align="center" prop="deptId" width="220"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['logic:LogicLogic:edit']">修改</el-button>
          <el-button link type="primary" icon="Edit" @click="handleView(scope.row)" v-hasPermi="['logic:LogicLogic:edit']">详情</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['logic:LogicLogic:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改逻辑演示对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="LogicLogicRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="备注" prop="logicRemark">
              <el-input v-model="form.logicRemark" placeholder="请输入备注" :readonly="ifView"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户id" prop="userId">
              <el-input v-model="form.userId" placeholder="请输入用户id" :readonly="ifView"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门id" prop="deptId">
              <el-input v-model="form.deptId" placeholder="请输入部门id" :readonly="ifView"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm" v-if="!ifView">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="LogicLogic">
import { listLogicLogic, getLogicLogic, delLogicLogic, addLogicLogic, updateLogicLogic } from "@/api/logic/LogicLogic";

const { proxy } = getCurrentInstance();

const LogicLogicList = ref([]);
const open = ref(false);
const ifView = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    logicRemark: null,
    userId: null,
    deptId: null
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询逻辑演示列表 */
function getList() {
  loading.value = true;
  listLogicLogic(queryParams.value).then(response => {
    LogicLogicList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  ifView.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    logicRemark: null,
    createTime: null,
    createBy: null,
    updateTime: null,
    updateBy: null,
    userId: null,
    deptId: null
  };
  ifView.value = false;
  proxy.resetForm("LogicLogicRef");
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
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加逻辑演示";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getLogicLogic(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改逻辑演示";
  });
}

/** 查看详情按钮操作 */
function handleView(row) {
  reset();
  const _id = row.id || ids.value
  getLogicLogic(_id).then(response => {
    form.value = response.data;
    open.value = true;
    ifView.value = true;
    title.value = "修改逻辑演示";
  });
}



/** 提交按钮 */
function submitForm() {
  proxy.$refs["LogicLogicRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateLogicLogic(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addLogicLogic(form.value).then(response => {
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
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除选中的数据？').then(function() {
    return delLogicLogic(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('logic/LogicLogic/export', {
    ...queryParams.value
  }, `LogicLogic_${new Date().getTime()}.xlsx`)
}

getList();
</script>
