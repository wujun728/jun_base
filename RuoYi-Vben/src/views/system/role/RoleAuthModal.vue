<template>
  <BasicModal
    v-bind="$attrs"
    title="分配权限"
    :width="600"
    :min-height="350"
    @register="registerInnerModal"
    @ok="handleSubmit"
    @cancel="resetForm"
  >
    <BasicForm @register="registerForm">
      <template #deptTree="{ model, field }">
        <FormItemRest>
          <Card :body-style="{ padding: 0 }">
            <BasicTree
              title="部门分配"
              v-if="roleDeptTreeList.length"
              defaultExpandAll
              :tree-data="roleDeptTreeList"
              :fieldNames="{ title: 'label', key: 'id' }"
              :checkable="true"
              :selectable="false"
              :enableCustomTool="true"
              v-model:checkStrictly="deptCheckStrictly"
              v-model:checkedKeys="model[field]"
              @check="handleChecked"
              @change="handleChange"
            />
          </Card>
        </FormItemRest>
      </template>
    </BasicForm>
  </BasicModal>
</template>

<script setup lang="ts">
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';
  import { ref } from 'vue';
  import { roleInfo, roleDataScope, roleDeptTree } from '@/api/system/role';
  import { DeptOption } from '@/api/system/role/model';
  import { Card, FormItemRest } from 'ant-design-vue';
  import { BasicTree } from '@/components/Tree';
  import { authSchemas } from './role.data';
  import { CheckInfo } from 'ant-design-vue/es/vc-tree/props';
  import { findGroupParentIds, findAllIds } from '@/utils/helper/treeHelper';

  defineOptions({ name: 'RoleAuthModal' });

  const emit = defineEmits(['register', 'reload']);

  const roleDeptTreeList = ref<DeptOption[]>([]);
  /** checkable 状态下节点选择完全受控（父子节点选中状态不再关联） */
  const deptCheckStrictly = ref<boolean>(false);

  const [registerInnerModal, { modalLoading, closeModal }] = useModalInner(
    async (data: { record: Recordable }) => {
      modalLoading(true);
      const { record } = data;
      const ret = await roleInfo(record.roleId);
      /** 是否节点独立/关联  后台返回的字段和antd是反的 */
      deptCheckStrictly.value = !ret.deptCheckStrictly;
      await setFieldsValue(ret);
      // tree 拿到tree数据和选中
      const response = await roleDeptTree(ret.roleId);
      const { depts, checkedKeys } = response;
      roleDeptTreeList.value = depts;
      /** 在关联情况下 checkedKeys不包含父节点 */
      if (ret.deptCheckStrictly) {
        const parentIds = findGroupParentIds(depts, checkedKeys, { pid: 'parentId' });
        checkedDeptKeys.value = [...parentIds, ...checkedKeys];
      } else {
        checkedDeptKeys.value = checkedKeys;
      }
      /** 设置选中的菜单ID */
      await setFieldsValue({ deptIds: checkedKeys });
      modalLoading(false);
    },
  );

  const [registerForm, { setFieldsValue, validate, resetForm }] = useForm({
    layout: 'vertical',
    labelWidth: 100,
    name: 'role_modal',
    showActionButtonGroup: false,
    baseColProps: { span: 24 },
    schemas: authSchemas,
  });

  /** 已经选择的所有节点  包括子/父节点 */
  const checkedDeptKeys = ref<number[]>([]);

  type CheckedState<T = number> = T[] | { checked: T[]; halfChecked: T[] };
  function handleChecked(checkedKeys: CheckedState, info: CheckInfo) {
    // 数组的话为节点关联
    if (checkedKeys instanceof Array) {
      const halfCheckedKeys: number[] = (info.halfCheckedKeys || []) as number[];
      checkedDeptKeys.value = [...halfCheckedKeys, ...checkedKeys];
    } else {
      checkedDeptKeys.value = [...checkedKeys.checked];
    }
  }

  /**
   * 选择/取消全部只会触发change事件
   * 回调参数在选择/取消 事件时只会是数组  忽略menuCheckStrictly
   * @param checkedKeys
   */
  function handleChange(checkedKeys: CheckedState) {
    if (checkedKeys instanceof Array) {
      const menuIds = findAllIds(roleDeptTreeList.value);
      // 取消全部
      if (checkedKeys.length === 0) {
        console.log('取消全部');
        checkedDeptKeys.value = [];
      }
      // 选择全部
      if (checkedKeys.length === menuIds.length) {
        console.log('选择全部');
        checkedDeptKeys.value = menuIds;
      }
    }
  }

  async function handleSubmit() {
    try {
      modalLoading(true);
      const data = await validate();
      // 不为自定义权限的话 删除部门id
      if (data.dataScope !== '2') {
        data.deptIds = [];
      } else {
        data.deptCheckStrictly = !deptCheckStrictly.value;
        data.deptIds = checkedDeptKeys.value;
      }
      await roleDataScope(data);
      emit('reload');
      closeModal();
      await resetForm();
    } catch (e) {
      console.warn(e);
    } finally {
      modalLoading(false);
    }
  }
</script>

<style scoped></style>
