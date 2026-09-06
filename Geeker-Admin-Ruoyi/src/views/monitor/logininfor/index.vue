<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable
        ref="proTable"
        :columns="columns"
        :init-param="initParams"
        :request-api="listLogininfor"
        row-key="infoId"
        @sort-change="handleSortChange"
      >
        <!-- 表格 header 按钮 -->
        <template #tableHeader="scope">
          <el-button
            type="danger"
            :icon="Delete"
            plain
            v-hasPermi="['monitor:logininfor:remove']"
            :disabled="!scope.isSelected"
            @click="batchDelete(scope.selectedListIds)"
          >
            批量删除
          </el-button>
          <el-button
            type="primary"
            plain
            icon="Unlock"
            :disabled="scope.selectedList.length !== 1"
            @click="handleUnlock(scope.selectedList)"
            v-hasPermi="['monitor:logininfor:unlock']"
          >
            解锁
          </el-button>
          <el-button type="danger" plain icon="Delete" @click="handleClean" v-hasPermi="['monitor:logininfor:remove']">
            清空
          </el-button>
          <el-button type="primary" :icon="Download" plain v-hasPermi="['monitor:logininfor:export']" @click="downloadFile">
            导出登录数据
          </el-button>
        </template>
      </ProTable>
    </div>
  </div>
</template>

<script setup lang="tsx" name="Logininfor">
import { Delete, Download } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { Operlog } from "@/api/interface";
import DictTag from "@/components/DictTag/index.vue";
import { getEnum } from "@/api/ruoyimodule";
import { useDict } from "@/hooks/useDict.js";
import { useHandleData } from "@/hooks/useHandleData";
import { ElMessage, ElMessageBox } from "element-plus";
import { useDownload } from "@/hooks/useDownload";
import {
  cleanLogininfor,
  delLogininfor,
  exportLogininforInfo,
  listLogininfor,
  unlockLogininfor
} from "@/api/ruoyimodule/monitor/logininfor";
// ProTable 实例
const proTable = ref<ProTableInstance>();
// 如果表格需要初始化请求参数，直接定义传给 ProTable(之后每次请求都会自动带上该参数，此参数更改之后也会一直带上，改变此参数会自动刷新表格数据)
const initParams = reactive({
  orderByColumn: null,
  isAsc: null
});
const { sys_common_status } = useDict("sys_common_status");

const columns = reactive<ColumnProps<Operlog.ResOperlogInfo>[]>([
  { type: "selection", fixed: "left", width: 70 },
  { prop: "infoId", label: "访问编号" },
  { prop: "userName", label: "用户名称", search: { el: "input" }, sortable: true, sortOrders: ["descending", "ascending"] },
  { prop: "ipaddr", label: "登录地址", search: { el: "input" } },
  { prop: "loginLocation", label: "登录地点" },
  { prop: "os", label: "操作系统" },
  { prop: "browser", label: "浏览器" },
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
  { prop: "msg", label: "描述" },
  {
    prop: "loginTime",
    label: "访问时间",
    width: 150,
    sortable: true,
    sortOrders: ["descending", "ascending"],
    search: {
      el: "date-picker",
      props: { type: "daterange", valueFormat: "YYYY-MM-DD" }
    }
  }
]);

const batchDelete = async (id: string[]) => {
  console.log(id);
  await useHandleData(delLogininfor, id.join(), "删除所选登录信息");
  proTable.value?.clearSelection();
  proTable.value?.getTableList();
};
const downloadFile = async () => {
  ElMessageBox.confirm("确认导出登录日志数据?", "温馨提示", { type: "warning" }).then(() =>
    useDownload(exportLogininforInfo, "登录日志", proTable.value?.searchParam)
  );
};

/** 清空按钮操作 */
function handleClean() {
  ElMessageBox.confirm("是否确认清空所有登录日志数据项?")
    .then(function () {
      return cleanLogininfor();
    })
    .then(() => {
      proTable.value?.getTableList();
      ElMessage.success("清空成功");
    });
}

/** 解锁按钮操作 */
function handleUnlock(selectList) {
  console.log(selectList);
  const username = selectList[0].userName;
  ElMessageBox.confirm('是否确认解锁用户"' + username + '"数据项?')
    .then(function () {
      return unlockLogininfor(username);
    })
    .then(() => {
      ElMessage.success("用户" + username + "解锁成功");
    });
}
/** 排序触发事件 */
function handleSortChange(column) {
  console.log(column);
  initParams.orderByColumn = column.prop;
  initParams.isAsc = column.order;
  proTable.value?.getTableList();
}
</script>

<style scoped lang="scss"></style>
