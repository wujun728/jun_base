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
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="drawerProps.row!.roleName" placeholder="请输入角色名称" />
      </el-form-item>
      <el-form-item prop="roleKey">
        <template #label>
          <span>
            <el-tooltip content="控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasRole('admin')`)" placement="top">
              <el-icon><question-filled /></el-icon>
            </el-tooltip>
            权限字符
          </span>
        </template>
        <el-input v-model="drawerProps.row!.roleKey" placeholder="请输入权限字符" />
      </el-form-item>
      <el-form-item label="角色顺序" prop="roleSort">
        <el-input-number v-model="drawerProps.row!.roleSort" controls-position="right" :min="0" />
      </el-form-item>
      <el-form-item label="状态">
        <el-radio-group v-model="drawerProps.row!.status">
          <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="菜单权限">
        <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event)">展开/折叠</el-checkbox>
        <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event)">全选/全不选</el-checkbox>
        <el-checkbox v-model="drawerProps.row!.menuCheckStrictly" @change="handleCheckedTreeConnect($event)">
          父子联动
        </el-checkbox>
        <el-tree
          class="tree-border"
          :data="menuOptions"
          show-checkbox
          ref="menuRef"
          node-key="id"
          :check-strictly="!drawerProps.row!.menuCheckStrictly"
          empty-text="加载中，请稍候"
          :props="{ label: 'label', children: 'children' }"
        ></el-tree>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="drawerProps.row!.remark" type="textarea" placeholder="请输入内容"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button v-show="!drawerProps?.isView" type="primary" @click="handleSubmit">确定</el-button>
      <el-button @click="drawerVisible = false">取消</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts" name="RoleDrawer">
import { ref, reactive, nextTick } from "vue";
import { ElMessage, FormInstance } from "element-plus";
import { Role } from "@/api/interface";
import { useDict } from "@/hooks/useDict.js";
import { menuTreeselect, roleMenuTreeselect } from "@/api/ruoyimodule/menu";
import { getRole } from "@/api/ruoyimodule/system/role";

const { sys_normal_disable } = useDict("sys_normal_disable");

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
  getMenuTreeselect();
  drawerProps.value = params;
  drawerVisible.value = true;
  drawerProps.value.row.status = drawerProps.value.row.status || "0";
  drawerProps.value.row!.menuCheckStrictly = true;
  const roleId = drawerProps.value.row.roleId;
  if (roleId) {
    const roleMenu = getRoleMenuTreeselect(roleId);
    getRole(roleId).then(() => {
      nextTick(() => {
        roleMenu.then((res: any) => {
          let checkedKeys = res.checkedKeys;
          checkedKeys.forEach(v => {
            nextTick(() => {
              menuRef.value.setChecked(v, true, false);
            });
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
      drawerProps.value.row.menuIds = getMenuAllCheckedKeys();
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
const menuRef = ref();

const menuExpand = ref(false);
const menuNodeAll = ref(false);
const menuOptions = ref([]);

/** 树权限（展开/折叠）*/
function handleCheckedTreeExpand(value) {
  let treeList: any = menuOptions.value;
  for (let i = 0; i < treeList.length; i++) {
    menuRef.value.store.nodesMap[treeList[i].id].expanded = value;
  }
}
/** 树权限（全选/全不选） */
function handleCheckedTreeNodeAll(value) {
  menuRef.value.setCheckedNodes(value ? menuOptions.value : []);
}
/** 树权限（父子联动） */
function handleCheckedTreeConnect(value) {
  drawerProps.value.row!.menuCheckStrictly = !!value;
}

/** 查询菜单树结构 */
function getMenuTreeselect() {
  menuTreeselect().then((response: any) => {
    menuOptions.value = response.data;
  });
}

/** 所有菜单节点数据 */
function getMenuAllCheckedKeys() {
  // 目前被选中的菜单节点
  let checkedKeys = menuRef.value.getCheckedKeys();
  // 半选中的菜单节点
  let halfCheckedKeys = menuRef.value.getHalfCheckedKeys();
  checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
  return checkedKeys;
}

/** 根据角色ID查询菜单树结构 */
function getRoleMenuTreeselect(roleId) {
  return roleMenuTreeselect(roleId).then((response: any) => {
    menuOptions.value = response.menus;
    return response;
  });
}

defineExpose({
  acceptParams
});
</script>
