<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable ref="proTable" :columns="columns" :request-api="listRole">
        <!-- 表格 header 按钮 -->
        <template #tableHeader>
          <el-button type="primary" :icon="CirclePlus" v-hasPermi="['system:role:add']" @click="openDrawer('新增')">
            新增角色
          </el-button>
          <el-button type="primary" :icon="Download" plain v-hasPermi="['system:role:export']" @click="downloadFile">
            导出角色数据
          </el-button>
        </template>
        <!--&lt;!&ndash; 表格操作 &ndash;&gt;-->
        <template #operation="scope">
          <template v-if="scope.row.roleId !== 1">
            <el-tooltip class="box-item" effect="dark" content="编辑" placement="top">
              <el-button
                type="primary"
                link
                :icon="EditPen"
                v-hasPermi="['system:role:edit']"
                @click="openDrawer('编辑', scope.row)"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="box-item" effect="dark" content="删除" placement="top">
              <el-button
                type="primary"
                link
                :icon="Delete"
                v-hasPermi="['system:role:remove']"
                @click="deleteAccount(scope.row)"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="box-item" effect="dark" content="数据权限" placement="top">
              <el-button
                type="primary"
                v-hasPermi="['system:role:edit']"
                link
                :icon="CircleCheck"
                @click="handleDataScope(scope.row)"
              ></el-button>
            </el-tooltip>
          </template>
        </template>
      </ProTable>
      <RoleDrawer ref="drawerRef" />
      <DataScopeDrawer ref="dataScopeDrawer"></DataScopeDrawer>
    </div>
  </div>
</template>

<script setup lang="tsx" name="Role">
import { CircleCheck, CirclePlus, Delete, Download, EditPen } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { addRole, dataScope, delRole, exportRoleInfo, listRole, updateRole } from "@/api/ruoyimodule/system/role";
import { reactive } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { Role } from "@/api/interface";
import { getEnum } from "@/api/ruoyimodule";
import RoleDrawer from "@/views/system/role/components/RoleDrawer.vue";
import { ref } from "vue";
import { ElMessageBox } from "element-plus";
import { useDownload } from "@/hooks/useDownload";
import { useHandleData } from "@/hooks/useHandleData";
import DataScopeDrawer from "@/views/system/role/components/DataScopeDrawer.vue";

// ProTable 实例
const proTable = ref<ProTableInstance>();

// 表格配置项
const columns = reactive<ColumnProps<Role.ResRoleInfo>[]>([
  { prop: "roleName", label: "角色名称", search: { el: "input" } },
  { prop: "roleKey", label: "权限字符", search: { el: "input" } },
  { prop: "roleSort", label: "显示顺序" },
  {
    prop: "status",
    label: "状态",
    enum: getEnum("sys_normal_disable"),
    search: { el: "select", props: { filterable: true } } as any,
    fieldNames: { label: "dictLabel", value: "dictValue" },
    render: scope => {
      return <el-tag type={scope.row.status == 0 ? "success" : "danger"}>{scope.row.status == 0 ? "正常" : "禁用"}</el-tag>;
    }
  },
  {
    prop: "createTime",
    label: "创建时间",
    width: 150,
    search: {
      el: "date-picker",
      props: { type: "daterange", valueFormat: "YYYY-MM-DD" }
    }
  },
  { prop: "operation", label: "操作", width: 140, fixed: "right" }
]);

// 打开 drawer(新增、查看、编辑)
const drawerRef = ref<InstanceType<typeof RoleDrawer> | null>(null);
const openDrawer = (title: string, row: Partial<Role.ResRoleInfo> = {}) => {
  const params = {
    title,
    isView: title === "查看",
    row: { ...row },
    api: title === "新增" ? addRole : title === "编辑" ? updateRole : undefined,
    getTableList: proTable.value?.getTableList
  };
  drawerRef.value?.acceptParams(params);
};
//数据权限操作
const dataScopeDrawer = ref<InstanceType<typeof DataScopeDrawer>>();
const handleDataScope = (row: Role.ResRoleInfo) => {
  const params = {
    title: "分配数据权限",
    row: { ...row },
    api: dataScope,
    getTableList: proTable.value?.getTableList
  };
  dataScopeDrawer.value?.acceptParams(params as any);
};
const downloadFile = async () => {
  ElMessageBox.confirm("确认导出用户角色数据?", "温馨提示", { type: "warning" }).then(() =>
    useDownload(exportRoleInfo, "角色列表", proTable.value?.searchParam)
  );
};

const deleteAccount = async (params: Role.ResRoleInfo) => {
  await useHandleData(delRole, params.roleId, `删除角色【${params.roleName}】`);
  proTable.value?.getTableList();
};
</script>

<style scoped lang="scss"></style>
