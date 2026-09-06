<template>
  <div class="app-container">
    <el-card shadow="never" body-class="search-card">
      <el-form :model="queryParams" ref="queryRef" inline v-show="showSearch" label-width="68px">
        <el-form-item label="标签名" prop="tag">
          <el-select v-model="queryParams.tag" placeholder="请选择标签名" clearable style="width: 200px;">
            <el-option v-for="dict in online_api_tag" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="标签id" prop="tagId">
          <el-input v-model="queryParams.tagId" placeholder="请输入标签id" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="参数类型" prop="parameterType">
          <el-input v-model="queryParams.parameterType" placeholder="请输入参数类型" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="结果类型" prop="resultMap">
          <el-input v-model="queryParams.resultMap" placeholder="请输入结果类型" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="请求路径" prop="path">
          <el-input v-model="queryParams.path" placeholder="请输入请求路径" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="请求方式" prop="method">
          <el-select v-model="queryParams.method" placeholder="请选择请求方式" clearable style="width: 200px;">
            <el-option v-for="dict in online_api_method" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="响应类型" prop="resultType">
          <el-select v-model="queryParams.resultType" placeholder="请选择响应类型" clearable style="width: 200px;">
            <el-option v-for="dict in online_api_result" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行器" prop="actuator">
          <el-select v-model="queryParams.actuator" placeholder="请选择执行器" clearable style="width: 200px;">
            <el-option v-for="dict in online_api_actuator" :key="dict.value" :label="dict.label" :value="dict.value" />
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
          <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['online:mb:add']">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate"
            v-hasPermi="['online:mb:edit']">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
            v-hasPermi="['online:mb:remove']">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" plain icon="Download" @click="handleExport"
            v-hasPermi="['online:mb:export']">导出</el-button>
        </el-col>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="mbList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="mbId" />
        <el-table-column label="标签名" align="center" prop="tag" width="80">
          <template #default="scope">
            <dict-tag :options="online_api_tag" :value="scope.row.tag" />
          </template>
        </el-table-column>
        <el-table-column label="请求方式" align="center" prop="method" width="80">

          <template #default="scope">
            <dict-tag :options="online_api_method" :value="scope.row.method" />
          </template>
        </el-table-column>
        <el-table-column label="标签id" align="center" prop="tagId" width="150" />
        <el-table-column label="请求路径" align="center" prop="path" width="200" />
        <el-table-column label="权限" align="center" width="150">

          <template #default="scope">
            {{ scope.row.permissionType }}("{{ scope.row.permissionValue }}")
          </template>
        </el-table-column>
        <el-table-column label="参数类型" align="center" prop="parameterType" />
        <el-table-column label="结果类型" align="center" prop="resultMap" />

        <el-table-column label="响应类型" align="center" prop="resultType">

          <template #default="scope">
            <dict-tag :options="online_api_result" :value="scope.row.resultType" />
          </template>
        </el-table-column>
        <el-table-column label="执行器" align="center" prop="actuator" width="150">

          <template #default="scope">
            <dict-tag :options="online_api_actuator" :value="scope.row.actuator" />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="200">

          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
              v-hasPermi="['online:mb:edit']">修改</el-button>
            <el-button link type="primary" icon="View" @click="previewRow = scope.row, previewShow = true"
              v-hasPermi="['online:mb:edit']">查看</el-button>
            <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
              v-hasPermi="['online:mb:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>

    <onlinePreview v-bind="previewRow" v-model="previewShow"></onlinePreview>

    <!-- 添加或修改mybatis在线接口对话框 -->
    <el-dialog :title="title" v-model="open" width="700px" append-to-body>
      <el-form ref="mbRef" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="标签名" prop="tag">
              <el-select v-model="form.tag" placeholder="请选择标签名">
                <el-option v-for="dict in online_api_tag" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标签id" prop="tagId">
              <el-input v-model="form.tagId" placeholder="请输入标签id" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="参数类型" prop="parameterType">
              <el-input v-model="form.parameterType" placeholder="请输入参数类型" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结果类型" prop="resultMap">
              <el-input v-model="form.resultMap" placeholder="请输入结果类型" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="请求路径" prop="path">
              <el-input v-model="form.path" placeholder="请输入请求路径" />
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item prop="userId">
              <el-checkbox v-model="form.userId" true-value="1" false-value="0" label="是否启用userId" />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item prop="deptId">
              <el-checkbox v-model="form.deptId" true-value="1" false-value="0" label="是否启用deptId" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="sql语句" prop="sqlText">
          <mybatis-input v-model="form.sqlText" placeholder="请输入sql语句"></mybatis-input>
        </el-form-item>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="请求方式" prop="method">
              <el-select v-model="form.method" placeholder="请选择请求方式">
                <el-option v-for="dict in online_api_method" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行器" prop="actuator">
              <el-select v-model="form.actuator" placeholder="请选择执行器">
                <el-option v-for="dict in online_api_actuator" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="响应类型" prop="resultType">
          <el-select v-model="form.resultType" placeholder="请选择响应类型">
            <el-option v-for="dict in online_api_result" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="鉴权类型" prop="resultType">
              <el-select v-model="form.permissionType" placeholder="请选择鉴权类型">
                <el-option label="无" value="无"></el-option>
                <el-option label="hasPermi(含有这些权限)" value="hasPermi"></el-option>
                <el-option label="lacksPermi(不含这些权限)" value="lacksPermi"></el-option>
                <el-option label="hasAnyPerm(含有任何权限)" value="hasAnyPermi"></el-option>
                <el-option label="hasRole(含有这些角色)" value="hasRole"></el-option>
                <el-option label="lacksRole(不含这些角色)" value="lacksRole"></el-option>
                <el-option label="hasAnyRoles(含有任何角色)" value="hasAnyRoles"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="鉴权值" prop="resultType">
              <el-input v-model="form.permissionValue" placeholder="请输入权限或者角色字符串"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { listMb, getMb, delMb, addMb, updateMb } from "@modules/online/api/mb";
import mybatisInput from './mybatis-input.vue'
import onlinePreview from './online-preview.vue'

const previewRow = ref(null)
const previewShow = ref(false)
const { proxy } = getCurrentInstance();
const { online_api_method, online_api_result, online_api_actuator, online_api_tag } = proxy.useDict('online_api_method', 'online_api_result', 'online_api_actuator', 'online_api_tag');

const mbList = ref([]);
const open = ref(false);
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
    tag: null,
    tagId: null,
    parameterType: null,
    resultMap: null,
    sqlText: null,
    path: null,
    method: null,
    resultType: null,
    actuator: null
  },
  rules: {
    path: [
      { required: true, message: "请求路径不能为空", trigger: "blur" },
      { pattern: /^[^<>"' |\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" },
      {
        validator: (rule, value, callback) => {
          if (value[0] !== '/') {
            callback(new Error('请以斜杠开头'))
          } else {
            callback()
          }
        }
      }
    ],
    method: [
      { required: true, message: "请求方式不能为空", trigger: "change" }
    ],
    actuator: [
      { required: true, message: "执行器不能为空", trigger: "change" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询mybatis在线接口列表 */
function getList() {
  loading.value = true;
  listMb(queryParams.value).then(response => {
    mbList.value = response.rows;
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
    mbId: null,
    tag: null,
    tagId: null,
    parameterType: null,
    resultMap: null,
    sqlText: null,
    path: null,
    method: null,
    resultType: null,
    actuator: null
  };
  proxy.resetForm("mbRef");
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
  ids.value = selection.map(item => item.mbId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加mybatis在线接口";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _mbId = row.mbId || ids.value
  getMb(_mbId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改mybatis在线接口";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["mbRef"].validate(valid => {
    if (valid) {
      if (form.value.mbId != null) {
        updateMb(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addMb(form.value).then(response => {
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
  const _mbIds = row.mbId || ids.value;
  proxy.$modal.confirm('是否确认删除mybatis在线接口编号为"' + _mbIds + '"的数据项？').then(function () {
    return delMb(_mbIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => { });
}



/** 导出按钮操作 */
function handleExport() {
  proxy.download('online/mb/export', {
    ...queryParams.value
  }, `mb_${new Date().getTime()}.xlsx`)
}

getList();

</script>