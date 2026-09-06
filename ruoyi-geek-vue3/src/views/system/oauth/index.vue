<script setup name="Oauth" lang="ts">
import { listOauth, delOauth } from "@/api/system/oauth";
import { modal } from "@/plugins";
import { download } from "@/utils/request";
import { resetForm } from "@/utils/ruoyi";
import { ref } from "vue";
type OauthUser = any
const oauthList = ref<OauthUser[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<number[]>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  uuid: null,
  userId: null,
  source: null,
  accessToken: null,
  expireIn: null,
  refreshToken: null,
  openId: null,
  uid: null,
  accessCode: null,
  unionId: null,
  scope: null,
  tokenType: null,
  idToken: null,
  macAlgorithm: null,
  macKey: null,
  code: null,
  oauthToken: null,
  oauthTokenSecret: null
})

/** 查询第三方认证列表 */
function getList() {
  loading.value = true;
  listOauth(queryParams.value).then(response => {
    oauthList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  resetForm("queryRef");
  handleQuery();
}

/** 多选框选中数据 */
function handleSelectionChange(selection: OauthUser[]) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 删除按钮操作 */
function handleDelete(row: OauthUser) {
  const _ids = row.id || ids.value;
  modal.confirm('是否确认删除第三方认证编号为"' + _ids + '"的数据项？').then(function () {
    return delOauth(_ids);
  }).then(() => {
    getList();
    modal.msgSuccess("删除成功");
  }).catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
  download('system/oauth/export', {
    ...queryParams.value
  }, `oauth_${new Date().getTime()}.xlsx`)
}

getList();
</script>
<template>
  <div class="app-container">
    <el-card shadow="never" body-class="search-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="120px">
        <el-form-item label="第三方用户来源" prop="source">
          <el-input v-model="queryParams.source" placeholder="请输入第三方用户来源" clearable @keyup.enter="handleQuery" />
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
          <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
            v-hasPermi="['system:oauth:remove']">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" plain icon="Download" @click="handleExport"
            v-hasPermi="['system:oauth:export']">导出</el-button>
        </el-col>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="oauthList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="第三方系统的唯一ID" align="center" prop="uuid" />
        <el-table-column label="用户名" align="center" prop="userName" />
        <el-table-column label="部门名称" align="center" prop="deptName" />
        <el-table-column label="第三方用户来源" align="center" prop="source" />
        <el-table-column label="用户的授权令牌" align="center" prop="accessToken" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
              v-hasPermi="['system:oauth:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
  </div>
</template>