<template>
  <div class="main-box">
    <TreeFilter title="部门列表(单选)" :request-api="getUserDepartment" @change="changeTreeFilter" />
    <div class="table-box">
      <ProTable ref="proTable" :columns="columns" :request-api="getUserList" :init-param="initParam">
        <!-- 表格 header 按钮 -->
        <template #tableHeader>
          <el-button type="primary" :icon="CirclePlus" @click="openDrawer('新增')" v-hasPermi="['system:user:add']">
            新增用户
          </el-button>
          <el-button type="primary" :icon="Upload" plain @click="batchAdd" v-hasPermi="['system:user:import']">
            批量添加用户
          </el-button>
          <el-button type="primary" :icon="Download" plain @click="downloadFile" v-hasPermi="['system:user:export']">
            导出用户数据
          </el-button>
        </template>
        <!--&lt;!&ndash; 表格操作 &ndash;&gt;-->
        <template #operation="scope">
          <template v-if="scope.row.userId !== 1">
            <el-tooltip class="box-item" effect="dark" content="编辑" placement="top">
              <el-button
                type="primary"
                link
                :icon="EditPen"
                @click="openDrawer('编辑', scope.row)"
                v-hasPermi="['system:user:edit']"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="box-item" effect="dark" content="删除" placement="top">
              <el-button
                type="primary"
                link
                :icon="Delete"
                @click="deleteAccount(scope.row)"
                v-hasPermi="['system:user:remove']"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="box-item" effect="dark" content="重置密码" placement="top">
              <el-button
                type="primary"
                link
                :icon="Key"
                @click="resetPass(scope.row)"
                v-hasPermi="['system:user:resetPwd']"
              ></el-button>
            </el-tooltip>
          </template>
        </template>
      </ProTable>
      <UserDrawer ref="drawerRef" />
      <ImportExcel ref="dialogRef" />
    </div>
  </div>
</template>

<script setup lang="tsx" name="User">
import {
  getUserDepartment,
  getUserList,
  updateUser,
  changeUserStatus,
  addUser,
  delUser,
  resetUserPwd,
  userTempDownload,
  userUpload,
  exportUserInfo
} from "@/api/ruoyimodule/system/user";
import TreeFilter from "@/components/TreeFilter/index.vue";
import { reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
//import { CirclePlus, Delete, Download, EditPen, Refresh, Upload, View } from "@element-plus/icons-vue";
import ImportExcel from "@/components/ImportExcel/index.vue";
import UserDrawer from "./components/UserDrawer.vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { User } from "@/api/interface";
import { useHandleData } from "@/hooks/useHandleData";
import { CirclePlus, Delete, Download, EditPen, Key, Upload } from "@element-plus/icons-vue";
import { useDownload } from "@/hooks/useDownload";
import { getEnum } from "@/api/ruoyimodule";

const initParam = reactive({ deptId: "" });
// ProTable 实例
const proTable = ref<ProTableInstance>();
const changeTreeFilter = (val: string) => {
  ElMessage.success("请注意查看请求参数变化 🤔");
  proTable.value!.pageable.pageNum = 1;
  initParam.deptId = val;
};

// 表格配置项
const columns = reactive<ColumnProps<User.ResUserInfo>[]>([
  { prop: "userName", label: "用户名称", search: { el: "input" } },
  { prop: "nickName", label: "用户昵称" },
  { prop: "dept.deptName", label: "部门" },
  { prop: "phonenumber", label: "手机号码", search: { el: "input" } },
  {
    prop: "status",
    label: "状态",
    enum: getEnum("sys_normal_disable"),
    search: { el: "select", props: { filterable: true } } as any,
    fieldNames: { label: "dictLabel", value: "dictValue" },
    render: scope => {
      return (
        //<el-switch
        //  model-value={Number(scope.row.status)}
        //  active-text={scope.row.status == 0 ? "启用" : "禁用"}
        //  active-value={0}
        //  inactive-value={1}
        //  onClick={() => changeStatus(scope.row)}
        ///>
        <el-tag type={scope.row.status == 0 ? "success" : "danger"}>{scope.row.status == 0 ? "正常" : "禁用"}</el-tag>
      );
    }
  },
  {
    prop: "createTime",
    label: "创建时间",
    search: {
      el: "date-picker",
      props: { type: "daterange", valueFormat: "YYYY-MM-DD" }
    }
  },
  { prop: "operation", label: "操作", width: 140, fixed: "right" }
]);

//eslint-disable-next-line @typescript-eslint/no-unused-vars
const changeStatus = async (row: User.ResUserInfo) => {
  await useHandleData(
    changeUserStatus,
    { userId: row.userId, status: row.status == 1 ? 0 : 1 },
    `切换【${row.userName}】用户状态`
  );
  proTable.value?.getTableList();
};

// 打开 drawer(新增、查看、编辑)
const drawerRef = ref<InstanceType<typeof UserDrawer> | null>(null);
const openDrawer = (title: string, row: Partial<User.ResUserInfo> = {}) => {
  const params = {
    title,
    isView: title === "查看",
    row: { ...row },
    api: title === "新增" ? addUser : title === "编辑" ? updateUser : undefined,
    getTableList: proTable.value?.getTableList
  };
  drawerRef.value?.acceptParams(params);
};

const deleteAccount = async (params: User.ResUserInfo) => {
  await useHandleData(delUser, params.userId, `删除【${params.userName}】用户`);
  proTable.value?.getTableList();
};
// 重置用户密码
const resetPass = async (params: User.ResUserInfo) => {
  ElMessageBox.prompt('请输入"' + params.userName + '"的新密码', "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    closeOnClickModal: false,
    inputPattern: /^.{5,20}$/,
    inputErrorMessage: "用户密码长度必须介于 5 和 20 之间"
  }).then(({ value }) => {
    resetUserPwd({
      userId: params.userId,
      password: value
    }).then(() => {
      ElMessage("修改成功，新密码是：" + value);
    });
  });
};

// 批量添加用户
const dialogRef = ref<InstanceType<typeof ImportExcel> | null>(null);
const batchAdd = () => {
  const params = {
    title: "用户",
    tempApi: userTempDownload,
    importApi: userUpload,
    getTableList: proTable.value?.getTableList
  };
  dialogRef.value?.acceptParams(params);
};
// 导出用户列表
const downloadFile = async () => {
  ElMessageBox.confirm("确认导出用户数据?", "温馨提示", { type: "warning" }).then(() =>
    useDownload(exportUserInfo, "用户列表", proTable.value?.searchParam)
  );
};
</script>

<style scoped lang="scss"></style>
