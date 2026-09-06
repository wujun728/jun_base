<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable
        ref="proTable"
        :columns="columns"
        :init-param="initParam"
        :request-api="listOperlog"
        row-key="operId"
        @sort-change="handleSortChange"
      >
        <!-- 表格 header 按钮 -->
        <template #tableHeader="scope">
          <el-button
            type="danger"
            :icon="Delete"
            plain
            v-hasPermi="['monitor:operlog:remove']"
            :disabled="!scope.isSelected"
            @click="batchDelete(scope.selectedListIds)"
          >
            批量删除
          </el-button>
          <el-button type="danger" plain icon="Delete" @click="handleClean" v-hasPermi="['monitor:operlog:remove']">
            清空
          </el-button>
          <el-button type="primary" :icon="Download" plain v-hasPermi="['monitor:operlog:export']" @click="downloadFile">
            导出用户数据
          </el-button>
        </template>
        <!--&lt;!&ndash; 表格操作 &ndash;&gt;-->
        <template #operation="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['monitor:operlog:query']">
            详细
          </el-button>
        </template>
      </ProTable>
    </div>
    <!-- 操作日志详细 -->
    <el-dialog title="操作日志详细" v-model="open" width="700px" append-to-body>
      <el-form :model="form" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="操作模块：">
              {{ form.title }} / {{ selectDictLabel(sys_oper_type, form.businessType) }}
            </el-form-item>
            <el-form-item label="登录信息：">{{ form.operName }} / {{ form.operIp }} / {{ form.operLocation }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求地址：">{{ form.operUrl }}</el-form-item>
            <el-form-item label="请求方式：">{{ form.requestMethod }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="操作方法：">{{ form.method }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="请求参数：">{{ form.operParam }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="返回参数：">{{ form.jsonResult }}</el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="操作状态：">
              <div v-if="form.status === 0">正常</div>
              <div v-else-if="form.status === 1">失败</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="消耗时间：">{{ form.costTime }}毫秒</el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="操作时间：">{{ form.operTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="异常信息：" v-if="form.status === 1">{{ form.errorMsg }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="open = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="tsx" name="Operlog">
import { Delete, Download } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { Operlog } from "@/api/interface";
import DictTag from "@/components/DictTag/index.vue";
import { getEnum } from "@/api/ruoyimodule";
import { useDict, selectDictLabel } from "@/hooks/useDict.js";
import { cleanOperlog, delOperlog, exportOperlogInfo, listOperlog } from "@/api/ruoyimodule/monitor/operlog";
import { useHandleData } from "@/hooks/useHandleData";
import { ElMessage, ElMessageBox } from "element-plus";
import { useDownload } from "@/hooks/useDownload";
// 如果表格需要初始化请求参数，直接定义传给 ProTable(之后每次请求都会自动带上该参数，此参数更改之后也会一直带上，改变此参数会自动刷新表格数据)
const initParam = reactive({
  orderByColumn: null,
  isAsc: null
});
// ProTable 实例
const proTable = ref<ProTableInstance>();
const { sys_oper_type, sys_common_status } = useDict("sys_oper_type", "sys_common_status");
const form = ref<any>({});
const open = ref(false);

const columns = reactive<ColumnProps<Operlog.ResOperlogInfo>[]>([
  { type: "selection", fixed: "left", width: 70 },
  { prop: "operId", label: "日志编号" },
  { prop: "title", label: "系统模块", search: { el: "input" } },
  {
    prop: "businessType",
    label: "操作类型",
    enum: getEnum("sys_oper_type"),
    search: { el: "select", props: { filterable: true } } as any,
    fieldNames: { label: "dictLabel", value: "dictValue" },
    render: scope => {
      return <DictTag options={sys_oper_type.value} value={scope.row.businessType} />;
    }
  },
  { prop: "operName", label: "操作人员", search: { el: "input" }, sortable: true, sortOrders: ["descending", "ascending"] },
  { prop: "operIp", label: "主机", search: { el: "input" } },
  {
    prop: "status",
    label: "操作状态",
    enum: getEnum("sys_common_status"),
    search: { el: "select", props: { filterable: true } } as any,
    fieldNames: { label: "dictLabel", value: "dictValue" },
    render: scope => {
      return <DictTag options={sys_common_status.value} value={scope.row.status} />;
    }
  },
  {
    prop: "operTime",
    label: "操作日期",
    width: 150,
    search: {
      el: "date-picker",
      props: { type: "daterange", valueFormat: "YYYY-MM-DD" }
    },
    sortable: true,
    sortOrders: ["descending", "ascending"]
  },
  {
    prop: "costTime",
    label: "消耗时间",
    width: 150,
    sortable: true,
    sortOrders: ["descending", "ascending"],
    render: scope => {
      return <span>{scope.row.costTime}毫秒</span>;
    }
  } as any,
  { prop: "operation", label: "操作", width: 140, fixed: "right" }
]);

/** 详细按钮操作 */
function handleView(row) {
  open.value = true;
  form.value = row;
}
const batchDelete = async (id: string[]) => {
  console.log(id);
  await useHandleData(delOperlog, id.join(), "删除所选操作信息");
  proTable.value?.clearSelection();
  proTable.value?.getTableList();
};
const downloadFile = async () => {
  ElMessageBox.confirm("确认导出用户操作日志数据?", "温馨提示", { type: "warning" }).then(() =>
    useDownload(exportOperlogInfo, "操作日志", proTable.value?.searchParam)
  );
};

/** 清空按钮操作 */
function handleClean() {
  ElMessageBox.confirm("是否确认清空所有操作日志数据项?")
    .then(function () {
      return cleanOperlog();
    })
    .then(() => {
      proTable.value?.getTableList();
      ElMessage.success("清空成功");
    });
}
/** 排序触发事件 */
function handleSortChange(column) {
  initParam.orderByColumn = column.prop;
  initParam.isAsc = column.order;
  proTable.value?.getTableList();
}
</script>

<style scoped lang="scss"></style>
