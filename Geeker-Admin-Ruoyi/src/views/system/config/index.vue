<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable ref="proTable" :columns="columns" :request-api="listConfig" row-key="configId">
        <!-- 表格 header 按钮 -->
        <template #tableHeader>
          <el-button type="primary" :icon="CirclePlus" v-hasPermi="['system:config:add']" @click="handleAdd">
            新增参数
          </el-button>
          <el-button type="primary" :icon="Download" plain v-hasPermi="['system:config:export']" @click="downloadFile">
            导出字典数据
          </el-button>
          <el-button type="danger" plain icon="Refresh" v-hasPermi="['system:config:remove']" @click="handleRefreshCache">
            刷新缓存
          </el-button>
        </template>
        <!--&lt;!&ndash; 表格操作 &ndash;&gt;-->
        <template #operation="scope">
          <el-tooltip class="box-item" effect="dark" content="编辑" placement="top">
            <el-button
              type="primary"
              link
              :icon="EditPen"
              v-hasPermi="['system:config:edit']"
              @click="handleUpdate(scope.row)"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="box-item" effect="dark" content="删除" placement="top">
            <el-button
              type="primary"
              link
              :icon="Delete"
              v-hasPermi="['system:config:remove']"
              @click="deleteAccount(scope.row)"
            ></el-button>
          </el-tooltip>
        </template>
      </ProTable>
    </div>
    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="configRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="参数名称" prop="configName">
          <el-input v-model="form.configName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="参数键名" prop="configKey">
          <el-input v-model="form.configKey" placeholder="请输入参数键名" />
        </el-form-item>
        <el-form-item label="参数键值" prop="configValue">
          <el-input v-model="form.configValue" placeholder="请输入参数键值" />
        </el-form-item>
        <el-form-item label="系统内置" prop="configType">
          <el-radio-group v-model="form.configType">
            <el-radio v-for="dict in sys_yes_no" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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

<script setup lang="tsx" name="Config">
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps } from "@/components/ProTable/interface";
import { Config } from "@/api/interface";
import { getEnum } from "@/api/ruoyimodule";
import { useDict } from "@/hooks/useDict.js";
import {
  addConfig,
  delConfig,
  exportConfigInfo,
  getConfig,
  listConfig,
  refreshCache,
  updateConfig
} from "@/api/ruoyimodule/system/config";
import DictTag from "@/components/DictTag/index.vue";
import { CirclePlus, Delete, Download, EditPen } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useDownload } from "@/hooks/useDownload";
import { useHandleData } from "@/hooks/useHandleData";

const { sys_yes_no } = useDict("sys_yes_no");

// 表格配置项
const columns = reactive<ColumnProps<Post.ResPostInfo>[]>([
  { type: "selection", fixed: "left", width: 70 },
  { prop: "configName", label: "参数名称", search: { el: "input" } },
  { prop: "configKey", label: "参数键名", search: { el: "input" } },
  { prop: "configValue", label: "参数键值" },
  {
    prop: "configType",
    label: "系统内置",
    enum: getEnum("sys_yes_no"),
    search: { el: "select", props: { filterable: true } } as any,
    fieldNames: { label: "dictLabel", value: "dictValue" },
    render: scope => {
      return (
        <span>
          <DictTag options={sys_yes_no.value} value={scope.row.configType} />
        </span>
      );
    }
  },
  { prop: "remark", label: "备注" },
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

const configRef = ref();
const proTable = ref();
const form = ref<Config.ResConfigInfo>();
const title = ref("");
const open = ref(false);
const rules = {
  configName: [{ required: true, message: "参数名称不能为空", trigger: "blur" }],
  configKey: [{ required: true, message: "参数键名不能为空", trigger: "blur" }],
  configValue: [{ required: true, message: "参数键值不能为空", trigger: "blur" }]
};
/** 新增按钮操作 */
function handleAdd() {
  open.value = true;
  title.value = "添加字典类型";
  reset();
}
const downloadFile = async () => {
  ElMessageBox.confirm("确认导出参数数据?", "温馨提示", { type: "warning" }).then(() =>
    useDownload(exportConfigInfo, "参数列表", proTable.value?.searchParam)
  );
};
/** 表单重置 */
const reset = () => {
  form.value = {
    createTime: "",
    configId: "",
    configName: "",
    configKey: "",
    configValue: "",
    configType: "Y",
    remark: ""
  };
  if (configRef.value) {
    configRef.value.resetFields();
  }
};
/** 刷新缓存按钮操作 */
function handleRefreshCache() {
  refreshCache().then(() => {
    ElMessage.success("刷新缓存成功");
  });
}

/** 提交按钮 */
function submitForm() {
  configRef.value.validate(valid => {
    if (valid) {
      if (form.value?.configId) {
        updateConfig(form.value).then(() => {
          ElMessage.success("修改成功");
          open.value = false;
          proTable.value.getTableList();
        });
      } else {
        addConfig(form.value).then(() => {
          ElMessage.success("新增成功");
          open.value = false;
          proTable.value.getTableList();
        });
      }
    }
  });
}
function handleUpdate(row) {
  reset();
  const configId = row.configId;
  getConfig(configId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改字典类型";
  });
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 删除按钮操作 */
const deleteAccount = async (params: Config.ResConfigInfo) => {
  await useHandleData(delConfig, params.configId, `删除字典【${params.configName}】`);
  proTable.value?.getTableList();
};
</script>

<style scoped lang="scss"></style>
