<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable ref="proTable" row-key="deptId" :columns="columns" :request-api="listType" :request-auto="true">
        <template #tableHeader>
          <el-button type="primary" :icon="CirclePlus" v-hasPermi="['system:dict:add']" @click="handleAdd">新增字典</el-button>
          <el-button type="primary" :icon="Download" plain v-hasPermi="['system:dict:export']" @click="downloadFile">
            导出字典数据
          </el-button>
          <el-button type="danger" plain icon="Refresh" @click="handleRefreshCache" v-hasPermi="['system:dict:remove']">
            刷新缓存
          </el-button>
        </template>
        <template #operation="scope">
          <el-button type="primary" link :icon="EditPen" v-hasPermi="['system:menu:edit']" @click="handleUpdate(scope.row)">
            编辑
          </el-button>
          <el-button type="primary" link :icon="Delete" v-hasPermi="['system:menu:remove']" @click="deleteAccount(scope.row)">
            删除
          </el-button>
        </template>
      </ProTable>
    </div>
    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="dictRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="form.dictName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="form.dictType" placeholder="请输入字典类型" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
        </el-form-item>
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

<script setup lang="tsx" name="Dict">
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps } from "@/components/ProTable/interface";
import { Dict } from "@/api/interface";
import { getEnum } from "@/api/ruoyimodule";
import DictTag from "@/components/DictTag/index.vue";
import { useDict } from "@/hooks/useDict.js";
import { addType, delType, exportDictInfo, getType, listType, refreshCache, updateType } from "@/api/ruoyimodule/system/dict";
import { CirclePlus, Delete, Download, EditPen } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useDownload } from "@/hooks/useDownload";
import useDictStore from "@/stores/modules/dict.js";
import { useHandleData } from "@/hooks/useHandleData";

const form = ref<any>();
const title = ref("");
const open = ref(false);
const { sys_normal_disable } = useDict("sys_normal_disable");

const rules = {
  dictName: [{ required: true, message: "字典名称不能为空", trigger: "blur" }],
  dictType: [{ required: true, message: "字典类型不能为空", trigger: "blur" }]
};

// 表格配置项
const columns = reactive<ColumnProps<Dict.ResDictInfo>[]>([
  { prop: "dictId", label: "字典编号" },
  { prop: "dictName", label: "字典名称", search: { el: "input" } },
  {
    prop: "dictType",
    label: "字典类型",
    search: { el: "input" },
    render: scope => {
      return (
        <router-link to={"/system/dict-data/index/" + scope.row.dictType} class="link-type">
          <span>{scope.row.dictType}</span>
        </router-link>
      );
    }
  },
  {
    prop: "status",
    label: "状态",
    enum: getEnum("sys_normal_disable"),
    search: { el: "select", props: { filterable: true } } as any,
    fieldNames: { label: "dictLabel", value: "dictValue" },
    render: scope => {
      return <DictTag options={sys_normal_disable.value} value={scope.row.status} />;
    }
  },
  { prop: "remark", label: "备注" },
  {
    prop: "createTime",
    label: "操作日期",
    width: 150,
    search: {
      el: "date-picker",
      props: { type: "daterange", valueFormat: "YYYY-MM-DD" }
    },
    sortable: true
  },
  { prop: "operation", label: "操作", width: 140, fixed: "right" }
]);
/** 新增按钮操作 */
function handleAdd() {
  open.value = true;
  title.value = "添加字典类型";
  reset();
}
/** 表单重置 */
const dictRef = ref();
const proTable = ref();
function reset() {
  form.value = {
    dictId: undefined,
    dictName: undefined,
    dictType: undefined,
    status: "0",
    remark: undefined
  };
  if (dictRef.value) {
    dictRef.value.resetFields();
  }
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 提交按钮 */
function submitForm() {
  dictRef.value.validate(valid => {
    if (valid) {
      if (form.value.dictId != undefined) {
        updateType(form.value).then(() => {
          ElMessage.success("修改成功");
          open.value = false;
          proTable.value.getTableList();
        });
      } else {
        addType(form.value).then(() => {
          ElMessage.success("新增成功");
          open.value = false;
          proTable.value.getTableList();
        });
      }
    }
  });
}

// 导出用户列表
const downloadFile = async () => {
  ElMessageBox.confirm("确认导出字典数据?", "温馨提示", { type: "warning" }).then(() =>
    useDownload(exportDictInfo, "字典列表", proTable.value?.searchParam)
  );
};
/** 刷新缓存按钮操作 */
function handleRefreshCache() {
  refreshCache().then(() => {
    ElMessage.success("刷新成功");
    useDictStore().cleanDict();
  });
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const dictId = row.dictId;
  getType(dictId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改字典类型";
  });
}
/** 删除按钮操作 */
const deleteAccount = async (params: Dict.ResDictInfo) => {
  await useHandleData(delType, params.dictId, `删除字典【${params.dictName}】`);
  proTable.value?.getTableList();
};
</script>

<style scoped lang="scss"></style>
