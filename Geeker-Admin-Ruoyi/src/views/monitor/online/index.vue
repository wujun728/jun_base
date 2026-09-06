<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable ref="proTable" :columns="columns" :request-api="initData" row-key="tokenId">
        <!--&lt;!&ndash; 表格操作 &ndash;&gt;-->
        <template #operation="scope">
          <el-button
            type="primary"
            link
            icon="Delete"
            v-hasPermi="['monitor:online:forceLogout']"
            @click="handleForceLogout(scope.row)"
          >
            强退
          </el-button>
        </template>
      </ProTable>
    </div>
  </div>
</template>

<script setup lang="ts" name="Online">
import { forceLogout, list as initData } from "@/api/ruoyimodule/monitor/online";

import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { OnLine } from "@/api/interface";
import { useHandleData } from "@/hooks/useHandleData";
// ProTable 实例
const proTable = ref<ProTableInstance>();
// 表格配置项
const columns = reactive<ColumnProps<OnLine.ResOnLineInfo>[]>([
  { prop: "tokenId", label: "会话编号" },
  { prop: "userName", label: "用户名称", search: { el: "input" } },
  { prop: "deptName", label: "所属部门" },
  { prop: "loginLocation", label: "主机" },
  { prop: "ipaddr", label: "登录地址", search: { el: "input" } },
  { prop: "os", label: "操作系统" },
  { prop: "browser", label: "浏览器" },
  { prop: "loginTime", label: "登录时间" },
  { prop: "operation", label: "操作", width: 140, fixed: "right" }
]);
async function handleForceLogout(row) {
  await useHandleData(forceLogout, row.tokenId, `是否强推用户【${row.userName}】`);
  proTable.value?.getTableList();
}
</script>

<style scoped lang="scss"></style>
