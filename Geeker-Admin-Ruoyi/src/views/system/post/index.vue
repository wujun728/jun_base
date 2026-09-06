<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable ref="proTable" :columns="columns" :request-api="listPost" row-key="postId">
        <!-- 表格 header 按钮 -->
        <template #tableHeader="scope">
          <el-button type="primary" :icon="CirclePlus" v-hasPermi="['system:post:add']" @click="openDrawer('新增')">
            新增岗位
          </el-button>
          <el-button
            type="danger"
            :icon="Delete"
            plain
            v-hasPermi="['system:post:remove']"
            :disabled="!scope.isSelected"
            @click="batchDelete(scope.selectedListIds)"
          >
            批量删除用户
          </el-button>
          <el-button type="primary" :icon="Download" plain v-hasPermi="['system:post:export']" @click="downloadFile">
            导出用户数据
          </el-button>
        </template>
        <!--&lt;!&ndash; 表格操作 &ndash;&gt;-->
        <template #operation="scope">
          <el-tooltip class="box-item" effect="dark" content="编辑" placement="top">
            <el-button
              type="primary"
              link
              :icon="EditPen"
              v-hasPermi="['system:post:edit']"
              @click="openDrawer('编辑', scope.row)"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="box-item" effect="dark" content="删除" placement="top">
            <el-button
              type="primary"
              link
              :icon="Delete"
              v-hasPermi="['system:post:remove']"
              @click="deleteAccount(scope.row)"
            ></el-button>
          </el-tooltip>
        </template>
      </ProTable>
    </div>
    <PostDrawer ref="PostDrawerRef"></PostDrawer>
  </div>
</template>

<script setup lang="tsx" name="Post">
import { CirclePlus, Delete, Download, EditPen } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { reactive, ref } from "vue";
import { Post, Role } from "@/api/interface";
import { getEnum } from "@/api/ruoyimodule";
import { addPost, delPost, exportPostInfo, listPost, updatePost } from "@/api/ruoyimodule/system/post";
import PostDrawer from "./components/PostDrawer.vue";
import { useHandleData } from "@/hooks/useHandleData";
import { ElMessageBox } from "element-plus";
import { useDownload } from "@/hooks/useDownload";

// ProTable 实例
const proTable = ref<ProTableInstance>();

const PostDrawerRef = ref<InstanceType<typeof PostDrawer>>();
// 表格配置项
const columns = reactive<ColumnProps<Post.ResPostInfo[]>>([
  { type: "selection", fixed: "left", width: 70 },
  { prop: "postCode", label: "岗位编码", search: { el: "input" } },
  { prop: "postName", label: "岗位名称", search: { el: "input" } },
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

const openDrawer = (title: string, row: Partial<Role.ResRoleInfo> = {}) => {
  const params = {
    title,
    isView: title === "查看",
    row: { ...row },
    api: title === "新增" ? addPost : title === "编辑" ? updatePost : undefined,
    getTableList: proTable.value?.getTableList
  };
  PostDrawerRef.value?.acceptParams(params);
};
const deleteAccount = async (params: Post.ResPostInfo) => {
  await useHandleData(delPost, params.postId, `删除角色【${params.postName}】`);
  proTable.value?.getTableList();
};
const downloadFile = async () => {
  ElMessageBox.confirm("确认导出用户岗位数据?", "温馨提示", { type: "warning" }).then(() =>
    useDownload(exportPostInfo, "岗位列表", proTable.value?.searchParam)
  );
};
const batchDelete = async (id: string[]) => {
  console.log(id);
  await useHandleData(delPost, id.join(), "删除所选岗位信息");
  proTable.value?.clearSelection();
  proTable.value?.getTableList();
};
</script>

<style scoped lang="scss"></style>
