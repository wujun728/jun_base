<template>
  <el-dialog v-model="drawerVisible" width="500px" append-to-body :title="`${drawerProps?.title}用户`">
    <el-form
      ref="ruleFormRef"
      label-width="100px"
      label-suffix=" :"
      :rules="rules"
      :disabled="drawerProps?.isView"
      :model="drawerProps.row"
      :hide-required-asterisk="drawerProps?.isView"
    >
      <el-form-item label="角色名称">
        <el-input v-model="drawerProps.row!.roleName" :disabled="true" />
      </el-form-item>
      <el-form-item label="权限字符">
        <el-input v-model="drawerProps.row!.roleKey" :disabled="true" />
      </el-form-item>
      <el-form-item label="权限范围">
        <el-select v-model="drawerProps.row!.dataScope" @change="dataScopeSelectChange">
          <el-option v-for="item in dataScopeOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="数据权限" v-show="(drawerProps.row!.dataScope as any) == 2">
        <el-checkbox v-model="deptExpand" @change="handleCheckedTreeExpand($event)">展开/折叠</el-checkbox>
        <el-checkbox v-model="deptNodeAll" @change="handleCheckedTreeNodeAll($event)">全选/全不选</el-checkbox>
        <el-checkbox v-model="drawerProps.row!.deptCheckStrictly" @change="handleCheckedTreeConnect($event)">
          父子联动
        </el-checkbox>
        <el-tree
          class="tree-border"
          :data="deptOptions"
          show-checkbox
          default-expand-all
          ref="deptRef"
          node-key="id"
          :check-strictly="drawerProps.row!.deptCheckStrictly"
          empty-text="加载中，请稍候"
          :props="{ label: 'label', children: 'children' }"
        ></el-tree>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button v-show="!drawerProps?.isView" type="primary" @click="handleSubmit">确定</el-button>
      <el-button @click="drawerVisible = false">取消</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts" name="DataScopeDrawer">
import { ref, reactive, nextTick } from "vue";
import { ElMessage, FormInstance } from "element-plus";
import { Role } from "@/api/interface";
import { deptTreeSelect } from "@/api/ruoyimodule/menu";
import { getRole } from "@/api/ruoyimodule/system/role";

/** 数据范围选项*/
const dataScopeOptions = ref([
  { value: "1", label: "全部数据权限" },
  { value: "2", label: "自定数据权限" },
  { value: "3", label: "本部门数据权限" },
  { value: "4", label: "本部门及以下数据权限" },
  { value: "5", label: "仅本人数据权限" }
]);

const rules = reactive({
  roleName: [{ required: true, message: "角色名称不能为空", trigger: "blur" }],
  roleKey: [{ required: true, message: "权限字符不能为空", trigger: "blur" }],
  roleSort: [{ required: true, message: "角色顺序不能为空", trigger: "blur" }]
});

interface DrawerProps {
  title: string;
  isView: boolean;
  row: Partial<Role.ResRoleInfo>;
  api?: (params: any) => Promise<any>;
  getTableList?: () => void;
}

const drawerVisible = ref(false);
const drawerProps = ref<DrawerProps>({
  isView: false,
  title: "",
  row: {}
});

const acceptParams = (params: DrawerProps) => {
  drawerProps.value = params;
  drawerVisible.value = true;
  drawerProps.value.row.status = drawerProps.value.row.status || "0";
  drawerProps.value.row!.deptCheckStrictly = true;
  const roleId = drawerProps.value.row.roleId;
  if (roleId) {
    const deptTreeSelect = getDeptTree(roleId);
    getRole(roleId).then(() => {
      nextTick(() => {
        deptTreeSelect.then((res: any) => {
          nextTick(() => {
            if (deptRef.value) {
              deptRef.value.setCheckedKeys(res.checkedKeys);
            }
          });
        });
      });
    });
  }
};

// 提交数据（新增/编辑）
const ruleFormRef = ref<FormInstance>();
const handleSubmit = () => {
  ruleFormRef.value!.validate(async valid => {
    if (!valid) return;
    try {
      drawerProps.value.row.deptIds = getMenuAllCheckedKeys();
      await drawerProps.value.api!(drawerProps.value.row);
      ElMessage.success({ message: `${drawerProps.value.title}用户成功！` });
      drawerProps.value.getTableList!();
      drawerVisible.value = false;
    } catch (error) {
      console.log(error);
    }
  });
};

// 菜单权限选择
const deptRef = ref();

const deptExpand = ref(true);
const deptNodeAll = ref(false);
const deptOptions = ref([]);

/** 树权限（展开/折叠）*/
function handleCheckedTreeExpand(value) {
  let treeList: any = deptOptions.value;
  for (let i = 0; i < treeList.length; i++) {
    deptRef.value.store.nodesMap[treeList[i].id].expanded = value;
  }
}
/** 树权限（全选/全不选） */
function handleCheckedTreeNodeAll(value) {
  deptRef.value.setCheckedNodes(value ? deptOptions.value : []);
}
/** 树权限（父子联动） */
function handleCheckedTreeConnect(value) {
  drawerProps.value.row!.deptCheckStrictly = !!value;
}

/** 所有菜单节点数据 */
function getMenuAllCheckedKeys() {
  // 目前被选中的菜单节点
  let checkedKeys = deptRef.value.getCheckedKeys();
  // 半选中的菜单节点
  let halfCheckedKeys = deptRef.value.getHalfCheckedKeys();
  checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
  return checkedKeys;
}

/** 根据角色ID查询菜单树结构 */
function getDeptTree(roleId) {
  return deptTreeSelect(roleId).then((response: any) => {
    deptOptions.value = response.depts;
    return response;
  });
}
/** 选择角色权限范围触发 */
function dataScopeSelectChange(value) {
  if (value !== "2") {
    deptRef.value.setCheckedKeys([]);
  }
}
defineExpose({
  acceptParams
});
</script>
