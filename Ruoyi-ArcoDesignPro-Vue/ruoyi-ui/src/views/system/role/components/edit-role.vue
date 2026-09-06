<template>
  <a-modal :visible="visible" :title="state.title" width="50vw" @cancel="handleCancel" @ok="handleConfirm">
    <a-form ref="formRef" :model="state.form" auto-label-width>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item
              label="角色名称" field="roleName" :rules="[{ required: true, message: '角色名称不能为空', trigger: 'blur' }] as FieldRule[]">
            <a-input v-model="state.form.roleName" placeholder="请输入角色名称"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="权限字符" field="roleKey" :rules="[{ required: true, message: '权限字符不能为空', trigger: 'blur' }] as FieldRule[]">
            <a-tooltip content="控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasRole('admin')`)">
              <a-input v-model="state.form.roleKey" placeholder="请输入权限字符"/>
            </a-tooltip>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
              label="角色顺序" field="roleSort" :rules="[{ required: true, message: '角色顺序不能为空', trigger: 'blur' }] as FieldRule[]">
            <a-input-number v-model="state.form.roleSort" :default-value="0" :min="0"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="状态">
            <a-radio-group v-model="state.form.status">
              <a-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :value="dict.dictValue"
                  :label="dict.dictValue"
              >{{ dict.dictLabel }}
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="菜单权限">
            <a-space direction="vertical" :size="16" style="display: block; margin-top: 6px; width: 100%;">
              <a-row>
                <a-col :span="4">
                  <a-checkbox v-model="state.menuExpand" @change="handleCheckedTreeExpand($event)">展开/折叠</a-checkbox>
                </a-col>
                <a-col :span="4">
                  <a-checkbox v-model="state.menuNodeAll" @change="handleCheckedTreeNodeAll($event)">全选/全不选</a-checkbox>
                </a-col>
                <a-col :span="4">
                  <a-checkbox v-model="state.form.menuCheckStrictly" @change="handleCheckedTreeConnect($event)">父子联动
                  </a-checkbox>
                </a-col>
              </a-row>
              <a-row>
                <a-col :span="24">
                  <div style="width: 100%; padding: 2px; overflow: auto">
                    <a-tree
                        ref="menuRef"
                        class="tree-border"
                        :data="state.menuOptions"
                        :checkable="true"
                        :check-strictly="!state.form.menuCheckStrictly"
                        :field-names="{ key: 'id', title: 'label', children: 'children', }"
                    ></a-tree>
                  </div>
                </a-col>
              </a-row>
            </a-space>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="备注" field="remark">
            <a-textarea v-model="state.form.remark" type="textarea" placeholder="请输入内容"></a-textarea>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script lang="ts" setup>

import {reactive, ref, watch} from 'vue';
import {FieldRule, Form, Message} from "@arco-design/web-vue";
import {addRole, getRole, RoleRecord, updateRole} from "@/api/system/role";
import {roleMenuTreeSelect, treeSelect as menuTreeSelect} from "@/api/system/menu";

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  id: {
    type: [Number, String],
    default: undefined,
  },
  statusOptions: {
    type: Array,
    default: undefined,
  }
});
const emits = defineEmits(['update:visible', 'refreshDataList']);
const formRef = ref<typeof Form>();
const menuRef = ref();

const state = reactive({
  title: '新增角色',
  form: {} as RoleRecord,
  menuExpand: false,
  menuNodeAll: false,
  menuCheckStrictly: false,
  menuOptions: [],
})

const reset = () => {
  state.title = '新增角色';
  state.form = {status: '0', roleSort: 0,} as RoleRecord;
  state.menuExpand = false;
  state.menuNodeAll = false;
  state.menuCheckStrictly = false;
  if (menuRef.value) {
    menuRef.value.checkAll(false)
    menuRef.value.expandAll(false)
  }
};
reset();

const getRoleForm = async () => {
  state.title = '修改角色';
  const {data, code, msg} = await getRole(props.id as string | number);
  if (code && code === 200) {
    state.form = data;
    state.form.roleSort = Number(state.form.roleSort)
  } else {
    Message.error(msg)
  }
  const {menus, checkedKeys} = await roleMenuTreeSelect(props.id as number);
  state.menuOptions = menus;
  checkedKeys.forEach((v: number) => {
    if (menuRef.value) {
      menuRef.value.checkNode(v, true, false)
    }
  })
}
// 树权限（展开/折叠）
const handleCheckedTreeExpand = (value: boolean) => {
  if (menuRef.value) {
    menuRef.value.expandAll(value)
  }
}
// 树权限（全选/全不选）
const handleCheckedTreeNodeAll = (value: boolean) => {
  if (menuRef.value) {
    menuRef.value.checkAll(value)
  }
}
// 树权限（父子联动）
const handleCheckedTreeConnect = (value: boolean) => {
  state.form.menuCheckStrictly = value;
}

/** 查询菜单树结构 */
const getMenuTreeSelect = async () => {
  const {data} = await menuTreeSelect();
  state.menuOptions = data;
}

const handleCancel = () => {
  emits('update:visible', false)
}

// 所有菜单节点数据
const getMenuAllCheckedKeys = () => {
  let checkedKeys: number[] = [];
  if (menuRef.value) {
    // 目前被选中的菜单节点
    checkedKeys = menuRef.value.getCheckedNodes()?.map((i: any) => {
      return i.id;
    });
    // 半选中的菜单节点
    const halfCheckedKeys = menuRef.value.getHalfCheckedNodes()?.map((i: any) => {
      return i.id;
    });
    checkedKeys = [...checkedKeys, ...halfCheckedKeys];
  }
  return checkedKeys;
}

const handleConfirm = () => {
  formRef.value?.validate().then((error: any) => {
    if (!error) {
      if (state.form.roleId) {
        state.form.menuIds = getMenuAllCheckedKeys();
        updateRole(state.form).then(() => {
          Message.success("修改成功");
          emits('update:visible', false)
          emits('refreshDataList')
        });
      } else {
        state.form.menuIds = getMenuAllCheckedKeys();
        addRole(state.form).then(() => {
          Message.success("新增成功");
          emits('update:visible', false)
          emits('refreshDataList')
        })
      }
    }
  })
}

watch(
    () => props.id,
    (value) => {
      if (value) {
        reset();
        getRoleForm();
      } else {
        reset();
        getMenuTreeSelect();
      }
    },
    {
      immediate: true,
    }
)
</script>

<style>
/* tree border */
.tree-border {
  margin-top: 5px;
  border: 1px solid #e5e6e7;
  background: #FFFFFF none;
  border-radius: 4px;
}
</style>