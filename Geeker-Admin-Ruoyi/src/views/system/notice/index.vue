<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable ref="proTable" :columns="columns" :request-api="listNotice" row-key="noticeId">
        <!-- 表格 header 按钮 -->
        <template #tableHeader="scope">
          <el-button type="primary" :icon="CirclePlus" v-hasPermi="['system:notice:add']" @click="handleAdd">
            新增岗位
          </el-button>
          <el-button
            type="danger"
            icon="Delete"
            plain
            v-hasPermi="['system:notice:remove']"
            :disabled="!scope.isSelected"
            @click="batchDelete(scope.selectedListIds)"
          >
            批量删除用户
          </el-button>
        </template>
        <!--&lt;!&ndash; 表格操作 &ndash;&gt;-->
        <template #operation="scope">
          <el-tooltip class="box-item" effect="dark" content="编辑" placement="top">
            <el-button
              type="primary"
              link
              :icon="EditPen"
              v-hasPermi="['system:notice:edit']"
              @click="handleUpdate(scope.row)"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="box-item" effect="dark" content="删除" placement="top">
            <el-button
              type="primary"
              link
              :icon="Delete"
              v-hasPermi="['system:notice:remove']"
              @click="deleteAccount(scope.row)"
            ></el-button>
          </el-tooltip>
        </template>
      </ProTable>
    </div>
    <!-- 添加或修改公告对话框 -->
    <el-dialog :title="title" v-model="open" width="780px" append-to-body>
      <el-form ref="noticeRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="公告标题" prop="noticeTitle">
              <el-input v-model="form.noticeTitle" placeholder="请输入公告标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公告类型" prop="noticeType">
              <el-select v-model="form.noticeType" placeholder="请选择">
                <el-option v-for="dict in sys_notice_type" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in sys_notice_status" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="内容">
              <WangEditor v-model:value="form.noticeContent" height="250px" />
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

<script setup lang="tsx" name="Notice">
import { CirclePlus, Delete, EditPen } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { reactive, ref } from "vue";
import { getEnum } from "@/api/ruoyimodule";
import { listNotice, updateNotice, addNotice, delNotice, getNotice } from "@/api/ruoyimodule/system/notice";
import { Notice } from "@/api/interface";
import { useDict } from "@/hooks/useDict.js";
import WangEditor from "@/components/WangEditor/index.vue";
import { ElMessage, FormInstance } from "element-plus";
import { useHandleData } from "@/hooks/useHandleData";
import { delPost } from "@/api/ruoyimodule/system/post";

const { sys_notice_status, sys_notice_type } = useDict("sys_notice_status", "sys_notice_type");

// ProTable 实例
const proTable = ref<ProTableInstance>();

// 表格配置项
const columns = reactive<ColumnProps<Notice.ResNoticeInfo>[]>([
  { type: "selection", fixed: "left", width: 70 },
  { prop: "noticeTitle", label: "公告标题", search: { el: "input" } },
  {
    prop: "noticeType",
    label: "公告类型",
    enum: getEnum("sys_notice_type"),
    fieldNames: { label: "dictLabel", value: "dictValue" },
    render: scope => {
      return <el-tag type={scope.row.status == 0 ? "success" : "danger"}>{scope.row.status == 0 ? "正常" : "禁用"}</el-tag>;
    }
  },
  { prop: "createBy", label: "操作人员", search: { el: "input" } },
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
    width: 150
    //search: {
    //  el: "date-picker",
    //  props: { type: "daterange", valueFormat: "YYYY-MM-DD" }
    //}
  },
  { prop: "operation", label: "操作", width: 140, fixed: "right" }
]);
const rules = {
  noticeTitle: [{ required: true, message: "公告标题不能为空", trigger: "blur" }],
  noticeType: [{ required: true, message: "公告类型不能为空", trigger: "change" }]
};
const title = ref("");
const open = ref(false);
const form = ref<Partial<Notice.ResNoticeInfo>>();
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加公告";
}
const noticeRef = ref<FormInstance>();
/** 表单重置 */
function reset() {
  form.value = {
    noticeId: undefined,
    noticeTitle: undefined,
    noticeType: undefined,
    noticeContent: "",
    status: "0"
  };
  noticeRef.value?.resetFields();
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 提交按钮 */
function submitForm() {
  noticeRef.value?.validate(valid => {
    if (valid) {
      if (form.value?.noticeId != undefined) {
        updateNotice(form.value).then(() => {
          ElMessage.success("修改成功");
          open.value = false;
          reset();
          proTable.value?.getTableList();
        });
      } else {
        addNotice(form.value).then(() => {
          ElMessage.success("新增成功");
          open.value = false;
          reset();
          proTable.value?.getTableList();
        });
      }
    }
  });
}
const batchDelete = async (id: string[]) => {
  console.log(id);
  await useHandleData(delNotice, id.join(), "删除所选公告信息");
  proTable.value?.clearSelection();
  proTable.value?.getTableList();
};

/**修改按钮操作 */
function handleUpdate(row) {
  reset();
  const noticeId = row.noticeId;
  getNotice(noticeId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改公告";
  });
}
const deleteAccount = async (params: Notice.ResNoticeInfo) => {
  await useHandleData(delPost, params.noticeId, `删除公告【${params.noticeTitle}】`);
  proTable.value?.getTableList();
};
</script>

<style scoped lang="scss"></style>
