<template>
  <a-modal :visible="visible" :title="state.title" width="50vw" @cancel="handleCancel" @ok="handleConfirm">
    <a-form ref="formRef" :model="state.form" auto-label-width>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="角色名称" field="roleName">
            <a-input v-model="state.form.roleName" :disabled="true"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="权限字符" field="roleKey">
            <a-input v-model="state.form.roleKey" :disabled="true"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="权限范围" field="dataScope">
            <a-select v-model="state.form.dataScope">
              <a-option
                  v-for="item in state.dataScopeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              ></a-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col v-if="state.form.dataScope === '2'" :span="24">
          <a-form-item label="菜单权限">
            <a-space direction="vertical" :size="16" style="display: block; margin-top: 6px; width: 100%;">
              <a-row>
                <a-col :span="4">
                  <a-checkbox v-model="state.deptExpand" @change="handleCheckedTreeExpand($event)">展开/折叠</a-checkbox>
                </a-col>
                <a-col :span="4">
                  <a-checkbox v-model="state.deptNodeAll" @change="handleCheckedTreeNodeAll($event)">全选/全不选</a-checkbox>
                </a-col>
                <a-col :span="4">
                  <a-checkbox v-model="state.form.deptCheckStrictly" @change="handleCheckedTreeConnect($event)">父子联动
                  </a-checkbox>
                </a-col>
              </a-row>
              <a-row>
                <a-col :span="24">
                  <div style="width: 100%; padding: 2px;">
                    <a-tree
                        ref="deptRef"
                        class="tree-border"
                        :data="state.deptOptions"
                        :checkable="true"
                        :check-strictly="!state.form.deptCheckStrictly"
                        :field-names="{ key: 'id', title: 'label', children: 'children', }"
                    ></a-tree>
                  </div>
                </a-col>
              </a-row>
            </a-space>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script lang="ts" setup>

import {reactive, ref, watch} from "vue";
import {Form, Message} from "@arco-design/web-vue";
import {dataScope, deptTreeSelect, getRole, RoleRecord} from "@/api/system/role";

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  id: {
    type: [Number, String],
    default: undefined,
  },
});

const emits = defineEmits(['update:visible']);
const formRef = ref<typeof Form>();
const deptRef = ref();

const state = reactive({
  title: '分配数据权限',
  form: {} as RoleRecord,
  deptExpand: false,
  deptNodeAll: false,
  deptCheckStrictly: false,
  deptOptions: [],
  // 数据范围选项
  dataScopeOptions: [
    {
      value: "1",
      label: "全部数据权限"
    },
    {
      value: "2",
      label: "自定数据权限"
    },
    {
      value: "3",
      label: "本部门数据权限"
    },
    {
      value: "4",
      label: "本部门及以下数据权限"
    },
    {
      value: "5",
      label: "仅本人数据权限"
    }
  ],
})

const reset = () => {
  state.title = '分配数据权限';
  state.form = {status: '0', roleSort: 0,} as RoleRecord;
  state.deptExpand = false;
  state.deptNodeAll = false;
  state.deptCheckStrictly = false;
  if (deptRef.value) {
    deptRef.value.checkAll(false)
    deptRef.value.expandAll(false)
  }
};
reset();

const getDataScopeForm = async () => {
  const {data, code, msg} = await getRole(props.id as string | number);
  if (code && code === 200) {
    state.form = data;
  } else {
    Message.error(msg)
  }
  const {depts, checkedKeys} = await deptTreeSelect(props.id as number);
  state.deptOptions = depts;
  checkedKeys.forEach((v: number) => {
    if (deptRef.value) {
      deptRef.value.checkNode(v, true, false)
    }
  })
}

// 树权限（展开/折叠）
const handleCheckedTreeExpand = (value: boolean) => {
  if (deptRef.value) {
    deptRef.value.expandAll(value)
  }
}
// 树权限（全选/全不选）
const handleCheckedTreeNodeAll = (value: boolean) => {
  if (deptRef.value) {
    deptRef.value.checkAll(value)
  }
}
// 树权限（父子联动）
const handleCheckedTreeConnect = (value: boolean) => {
  state.form.deptCheckStrictly = value;
}

const handleCancel = () => {
  emits('update:visible', false)
}

// 所有部门节点数据
const getDeptAllCheckedKeys = () => {
  let checkedKeys: number[] = [];
  if (deptRef.value) {
    // 目前被选中的菜单节点
    checkedKeys = deptRef.value.getCheckedNodes()?.map((i: any) => {
      return i.id;
    });
    // 半选中的菜单节点
    const halfCheckedKeys = deptRef.value.getHalfCheckedNodes()?.map((i: any) => {
      return i.id;
    });
    checkedKeys = [...checkedKeys, ...halfCheckedKeys];
  }
  return checkedKeys;
}

const handleConfirm = () => {
  formRef.value?.validate().then((error: any) => {
    if (!error) {
      state.form.deptIds = getDeptAllCheckedKeys();
      dataScope(state.form).then(() => {
        Message.success("分配成功");
        emits('update:visible', false)
      })
    }
  })
}

watch(
    () => props.id,
    (value) => {
      if (value) {
        reset();
        getDataScopeForm();
      } else {
        reset();
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

<style scoped>

</style>