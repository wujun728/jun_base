<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable ref="proTable" row-key="tableId" :columns="columns" :request-api="listTable" :request-auto="true">
        <template #tableHeader="scope">
          <el-button
            type="primary"
            plain
            icon="Download"
            @click="handleGenTable(scope.selectedList)"
            v-hasPermi="['tool:gen:code']"
          >
            生成
          </el-button>
          <el-button type="info" plain icon="Upload" @click="openImportTable" v-hasPermi="['tool:gen:import']">导入</el-button>
          <el-button
            type="danger"
            :icon="Delete"
            plain
            v-hasPermi="['tool:gen:remove']"
            :disabled="!scope.isSelected"
            @click="batchDelete(scope.selectedListIds)"
          >
            批量删除
          </el-button>
        </template>
        <template #operation="scope">
          <el-tooltip content="预览" placement="top">
            <el-button
              link
              type="primary"
              icon="View"
              @click="handlePreview(scope.row)"
              v-hasPermi="['tool:gen:preview']"
            ></el-button>
          </el-tooltip>
          <el-tooltip content="编辑" placement="top">
            <el-button
              link
              type="primary"
              icon="Edit"
              @click="handleEditTable(scope.row)"
              v-hasPermi="['tool:gen:edit']"
            ></el-button>
          </el-tooltip>
          <el-tooltip content="删除" placement="top">
            <el-button
              link
              type="primary"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['tool:gen:remove']"
            ></el-button>
          </el-tooltip>
          <el-tooltip content="同步" placement="top">
            <el-button
              link
              type="primary"
              icon="Refresh"
              @click="handleSynchDb(scope.row)"
              v-hasPermi="['tool:gen:edit']"
            ></el-button>
          </el-tooltip>
        </template>
      </ProTable>
      <import-table ref="importRef" @ok="handleQuery" />
    </div>
    <!-- 预览界面 -->
    <el-dialog :title="preview.title" v-model="preview.open" width="80%" top="5vh" append-to-body class="scrollbar">
      <el-tabs v-model="preview.activeName">
        <el-tab-pane
          v-for="(value, key) in preview.data"
          :label="key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'))"
          :name="key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'))"
          :key="value"
        >
          <el-link :underline="false" icon="DocumentCopy" v-copy="value" style="float: right"> &nbsp;复制 </el-link>
          <pre>{{ value }}</pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script lang="jsx" setup name="Gen">
import { listTable, previewTable, delTable, genCode, synchDb } from "@/api/ruoyimodule/tool/gen";

import importTable from "./importTable.vue";
import { useRoute, useRouter } from "vue-router";
import { onActivated, reactive, ref, toRefs } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delete } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { useHandleData } from "@/hooks/useHandleData.ts";
import { saveAs } from "file-saver";
import axios from "axios";
import { useUserStore } from "@/stores/modules/user.ts";

const route = useRoute();
const router = useRouter();

const userStore = useUserStore();

const columns = reactive([
  { type: "selection", fixed: "left", width: 70 },
  { prop: "tableName", label: "表名称", search: { el: "input" } },
  { prop: "tableComment", label: "表描述", search: { el: "input" } },
  { prop: "className", label: "实体" },
  {
    prop: "createTime",
    label: "执行时间",
    width: 150,
    search: {
      el: "date-picker",
      props: { type: "daterange", valueFormat: "YYYY-MM-DD" }
    },
    sortable: true
  },
  { prop: "className", label: "更新时间" },

  { prop: "operation", label: "操作", width: 160, fixed: "right" }
]);
const proTable = ref();

const dateRange = ref([]);
const uniqueId = ref("");

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    tableName: undefined,
    tableComment: undefined
  },
  preview: {
    open: false,
    title: "代码预览",
    data: {},
    activeName: "domain.java"
  }
});

const { queryParams, preview } = toRefs(data);
const queryForm = ref();
onActivated(() => {
  const time = route.query.t;
  if (time != null && time != uniqueId.value) {
    uniqueId.value = time;
    queryParams.value.pageNum = Number(route.query.pageNum);
    dateRange.value = [];
    queryForm.value?.resetFields();
    proTable.value.getTableList();
  }
});

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  proTable.value.getTableList();
}
/** 生成代码操作 */
function handleGenTable(row) {
  const tbNames = row.map(item => item.tableName).join();
  if (tbNames == "") {
    ElMessage.error("请选择要生成的数据");
    return;
  }
  if (row.genType === "1") {
    genCode(row.tableName).then(() => {
      ElMessage("成功生成到自定义路径：" + row.genPath);
    });
  } else {
    zip(import.meta.env.VITE_API_URL + "/tool/gen/batchGenCode?tables=" + tbNames);
  }
}
/** 同步数据库操作 */
function handleSynchDb(row) {
  const tableName = row.tableName;
  ElMessageBox.confirm('确认要强制同步"' + tableName + '"表结构吗？')
    .then(function () {
      return synchDb(tableName);
    })
    .then(() => {
      ElMessage("同步成功");
    });
}
const importRef = ref();
/** 打开导入表弹窗 */
function openImportTable() {
  importRef.value.show();
}

/** 预览按钮 */
function handlePreview(row) {
  previewTable(row.tableId).then(response => {
    preview.value.data = response.data;
    preview.value.open = true;
    preview.value.activeName = "domain.java";
  });
}

/** 修改按钮操作 */
function handleEditTable(row) {
  const tableId = row.tableId;
  router.push({ path: "/tool/gen-edit/index/" + tableId, query: { pageNum: proTable.value.searchParam.pageNum } });
}
/** 删除按钮操作 */
function handleDelete(row) {
  const tableIds = row.tableId;
  ElMessageBox.confirm('是否确认删除表编号为"' + tableIds + '"的数据项？')
    .then(function () {
      return delTable(tableIds);
    })
    .then(() => {
      proTable.value.getTableList();
      ElMessage.success("删除成功");
    });
}

const batchDelete = async id => {
  console.log(id);
  await useHandleData(delTable, id.join(), "删除所选操作信息");
  proTable.value?.clearSelection();
  proTable.value?.getTableList();
};
const zip = url => {
  axios({
    method: "get",
    url: url,
    responseType: "blob",
    headers: { Authorization: "Bearer " + userStore.token }
  }).then(res => {
    console.log(res);
    const blob = new Blob([res.data], { type: "application/zip" });
    saveAs(blob, "ruoyi.zip");
  });
};
</script>
<style scoped></style>
