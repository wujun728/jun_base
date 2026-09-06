<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable
        ref="proTable"
        row-key="deptId"
        :columns="columns"
        :request-api="listDept"
        :request-auto="true"
        :pagination="false"
        :default-expand-all="isExpandAll"
      >
        <!-- 表格 header 按钮 -->
        <template #tableHeader>
          <el-button type="primary" :icon="CirclePlus" v-hasPermi="['system:dept:add']" @click="handleAdd">新增菜单</el-button>
          <el-button type="info" plain icon="Sort" @click="toggleExpandAll">展开/折叠</el-button>
        </template>
        <!-- 表格操作 -->
        <template #operation="scope">
          <el-button type="primary" link :icon="EditPen" v-hasPermi="['system:dept:edit']" @click="handleUpdate(scope.row)">
            编辑
          </el-button>
          <el-button type="primary" link :icon="CirclePlus" v-hasPermi="['system:dept:add']" @click="handleAdd(scope.row)">
            新增
          </el-button>
          <el-button type="primary" link :icon="Delete" v-hasPermi="['system:dept:remove']" @click="deleteAccount(scope.row)">
            删除
          </el-button>
        </template>
      </ProTable>
    </div>

    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="deptRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="上级部门" prop="parentId">
              <el-tree-select
                v-model="form.parentId"
                :data="deptOptions"
                :props="{ value: 'deptId', label: 'deptName', children: 'children' }"
                value-key="deptId"
                placeholder="选择上级部门"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请输入部门名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="leader">
              <el-input v-model="form.leader" placeholder="请输入负责人" maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
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

<script setup lang="tsx" name="Dept">
import { CirclePlus, Delete, EditPen } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { reactive } from "vue";
import { ColumnProps } from "@/components/ProTable/interface";
import { Dept } from "@/api/interface";
import { getEnum } from "@/api/ruoyimodule";
import { addDept, delDept, getDept, listDept, listDeptExcludeChild, updateDept } from "@/api/ruoyimodule/system/dept";
import { ref, nextTick } from "vue";
import { useDict } from "@/hooks/useDict.js";
import { ElMessage } from "element-plus";
import { handleTree } from "@/api/ruoyimodule/system/menu";
import { useHandleData } from "@/hooks/useHandleData";

const deptRef = ref();
const proTable = ref();

const form = ref<any>({});
const title = ref("");
const open = ref(false);
const deptOptions = ref<Array<Dept.ResDeptInfo>>();
const { sys_normal_disable } = useDict("sys_normal_disable");
// 表格配置项
const columns = reactive<ColumnProps<Dept.ResDeptInfo>[]>([
  { prop: "deptName", label: "部门名称", search: { el: "input" } },

  {
    prop: "status",
    label: "状态",
    tag: true,
    enum: getEnum("sys_normal_disable"),
    search: { el: "tree-select" },
    fieldNames: { label: "dictLabel", value: "dictValue" },
    render: scope => {
      return <el-tag type={scope.row.status == 0 ? "success" : "danger"}>{scope.row.status == 0 ? "正常" : "禁用"}</el-tag>;
    }
  },
  { prop: "createTime", label: "创建时间", width: 180 },
  { prop: "operation", label: "操作", width: 300, fixed: "right" }
]);

const rules = {
  parentId: [{ required: true, message: "上级部门不能为空", trigger: "blur" }],
  deptName: [{ required: true, message: "部门名称不能为空", trigger: "blur" }],
  orderNum: [{ required: true, message: "显示排序不能为空", trigger: "blur" }],
  email: [{ type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
  phone: [{ pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }]
};

// 菜单新增
const handleAdd = async (row?) => {
  let response: any = await listDept();
  deptOptions.value = response.rows;
  open.value = true;
  title.value = "添加部门";
  reset();
  nextTick(() => {
    form.value.parentId = row.deptId;
  });
};
/** 表单重置 */
function reset() {
  form.value = {
    deptId: undefined,
    parentId: undefined,
    deptName: undefined,
    orderNum: 0,
    leader: undefined,
    phone: undefined,
    email: undefined,
    status: "0"
  };

  nextTick(() => {
    deptRef.value.resetFields();
  });
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 提交按钮 */
function submitForm() {
  deptRef.value.validate(valid => {
    if (valid) {
      if (form.value.deptId != undefined) {
        updateDept(form.value).then(() => {
          ElMessage.success("修改成功");
          open.value = false;
          proTable.value?.getTableList();
        });
      } else {
        addDept(form.value).then(() => {
          ElMessage.success("新增成功");
          open.value = false;
          proTable.value?.getTableList();
        });
      }
    }
  });
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  listDeptExcludeChild(row.deptId).then(response => {
    deptOptions.value = handleTree(response.data, "deptId");
  });
  getDept(row.deptId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改部门";
  });
}

const deleteAccount = async (params: Dept.ResDeptInfo) => {
  await useHandleData(delDept, params.deptId, `删除菜单【${params.deptName}】`);
  proTable.value?.getTableList();
};

/** 展开/折叠操作 */
const isExpandAll = ref(true);

function toggleExpandAll() {
  isExpandAll.value = !isExpandAll.value;
}
</script>

<style scoped lang="scss"></style>
