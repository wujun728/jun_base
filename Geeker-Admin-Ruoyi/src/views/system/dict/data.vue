<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable ref="proTable" row-key="deptId" :columns="columns" :init-param="initParams" :request-api="listData">
        <template #tableHeader>
          <el-button type="primary" :icon="CirclePlus" v-hasPermi="['system:dict:add']" @click="handleAdd">新增</el-button>
          <el-button type="primary" :icon="Download" plain v-hasPermi="['system:dict:export']" @click="downloadFile">
            导出字典数据列表
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
      <el-form ref="dataRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字典类型">
          <el-input v-model="form.dictType" :disabled="true" />
        </el-form-item>
        <el-form-item label="数据标签" prop="dictLabel">
          <el-input v-model="form.dictLabel" placeholder="请输入数据标签" />
        </el-form-item>
        <el-form-item label="数据键值" prop="dictValue">
          <el-input v-model="form.dictValue" placeholder="请输入数据键值" />
        </el-form-item>
        <el-form-item label="样式属性" prop="cssClass">
          <el-input v-model="form.cssClass" placeholder="请输入样式属性" />
        </el-form-item>
        <el-form-item label="显示排序" prop="dictSort">
          <el-input-number v-model="form.dictSort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="回显样式" prop="listClass">
          <el-select v-model="form.listClass">
            <el-option
              v-for="item in listClassOptions"
              :key="item.value"
              :label="item.label + '(' + item.value + ')'"
              :value="item.value"
            ></el-option>
          </el-select>
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

<script setup lang="tsx" name="Data">
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps } from "@/components/ProTable/interface";
import { Dict } from "@/api/interface";
import { getEnum } from "@/api/ruoyimodule";
import DictTag from "@/components/DictTag/index.vue";
import { useDict } from "@/hooks/useDict.js";
import { optionselect } from "@/api/ruoyimodule/system/dict";
import { CirclePlus, Delete, Download, EditPen } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useDownload } from "@/hooks/useDownload";
import useDictStore from "@/stores/modules/dict.js";
import { useHandleData } from "@/hooks/useHandleData";
import { addData, exportDictDetailInfo, getData, listData, updateData } from "@/api/ruoyimodule/system/data";
import { useRoute } from "vue-router";

const form = ref<any>();
const title = ref("");
const open = ref(false);
const initParams = ref<any>({});

const { sys_normal_disable } = useDict("sys_normal_disable");

const rules = {
  dictName: [{ required: true, message: "字典名称不能为空", trigger: "blur" }],
  dictType: [{ required: true, message: "字典类型不能为空", trigger: "blur" }]
};
// 数据标签回显样式
const listClassOptions = ref([
  { value: "default", label: "默认" },
  { value: "primary", label: "主要" },
  { value: "success", label: "成功" },
  { value: "info", label: "信息" },
  { value: "warning", label: "警告" },
  { value: "danger", label: "危险" }
]);
// ProTable 实例
const proTable = ref<any>();
const dataRef = ref();
const route = useRoute();

// 表格配置项
const columns = reactive<ColumnProps<Dict.ResDictInfo>[]>([
  { type: "selection", width: 50 },
  {
    prop: "dictType",
    label: "字典名称",
    isShow: false,
    enum: optionselect,
    search: { el: "select", props: { filterable: true, clearable: false }, defaultValue: route.params.dictId } as any,
    fieldNames: { label: "dictName", value: "dictType" }
  },
  { prop: "dictCode", label: "字典编码" },
  {
    prop: "dictLabel",
    label: "字典标签",
    search: { el: "input" },
    render: scope => {
      return scope.row.listClass == "" || scope.row.listClass == "default" ? (
        <span v-if="">{scope.row.dictLabel}</span>
      ) : (
        <el-tag type={scope.row.listClass == "primary" ? "" : scope.row.listClass}>{scope.row.dictLabel}</el-tag>
      );
    }
  },
  { prop: "dictValue", label: "字典键值" },
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
    label: "创建时间"
  },
  { prop: "operation", label: "操作", width: 140, fixed: "right" }
]);
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加字典类型";
  form.value.dictType = proTable.value.searchParam.dictType;
}
/** 表单重置 */
function reset() {
  form.value = {
    dictCode: undefined,
    dictLabel: undefined,
    dictValue: undefined,
    cssClass: undefined,
    listClass: "default",
    dictSort: 0,
    status: "0",
    remark: undefined
  };
  if (dataRef.value) {
    dataRef.value.resetFields();
  }
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

// 导出用户列表
const downloadFile = async () => {
  ElMessageBox.confirm("确认导出字典数据列表?", "温馨提示", { type: "warning" }).then(() =>
    useDownload(exportDictDetailInfo, "字典数据列表", proTable.value?.searchParam)
  );
};
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const dictCode = row.dictCode;
  getData(dictCode).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改字典类型";
  });
}
/** 删除按钮操作 */
const deleteAccount = async (params: Dict.ResDictInfo) => {
  await useHandleData(getData, params.dictCode, `删除字典【${params.dictCode}】`);
  proTable.value?.getTableList();
};
/** 提交按钮 */
function submitForm() {
  dataRef.value.validate(valid => {
    if (valid) {
      if (form.value.dictCode != undefined) {
        updateData(form.value).then(() => {
          useDictStore().removeDict(proTable.value.searchParam.dictType);
          ElMessage.success("修改成功");
          open.value = false;
          proTable.value.getTableList();
        });
      } else {
        addData(form.value).then(() => {
          useDictStore().removeDict(proTable.value.searchParam.dictType);
          ElMessage.success("新增成功");
          open.value = false;
          proTable.value.getTableList();
        });
      }
    }
  });
}
</script>

<style scoped lang="scss"></style>
