<template>
  <BasicDrawer
    v-bind="$attrs"
    :title="title"
    :width="drawerWidth"
    :show-footer="true"
    @register="registerInnerDrawer"
    @ok="handleSubmit"
    @close="clearFormFields"
  >
    <BasicForm @register="registerForm">
      <template #menuTree="{ model, field }">
        <FormItemRest>
          <Card :body-style="{ padding: 0 }">
            <BasicTree
              ref="roleTreeRef"
              v-if="roleMenuTree.length"
              title="菜单分配"
              :tree-data="roleMenuTree"
              :fieldNames="{ title: 'label', key: 'id' }"
              :checkable="true"
              :selectable="false"
              :enableCustomTool="true"
              v-model:checkStrictly="menuCheckStrictly"
              v-model:checkedKeys="model[field]"
              @check="handleChecked"
              @change="handleChange"
            />
          </Card>
        </FormItemRest>
      </template>
    </BasicForm>
  </BasicDrawer>
</template>

<script setup lang="ts">
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { BasicForm, useForm } from '@/components/Form';
  import { computed, ref, unref, nextTick } from 'vue';
  import { roleInfo, roleAdd, roleUpdate } from '@/api/system/role';
  import { roleMenuTreeSelect, menuTreeSelect } from '@/api/system/menu';
  import { MenuOption } from '@/api/system/menu/model';
  import { Card, FormItemRest } from 'ant-design-vue';
  import { BasicTree } from '@/components/Tree';
  import { modalSchemas, excludeIds } from './role.data';
  import { findGroupParentIds, findAllIds, filter } from '@/utils/helper/treeHelper';
  import { CheckInfo } from 'ant-design-vue/es/vc-tree/props';
  import { useMaxWidthOrDefault } from '@/hooks/web/useSize';

  defineOptions({ name: 'RoleDrawer' });

  const emit = defineEmits(['register', 'reload']);

  const isUpdate = ref<boolean>(false);
  const drawerWidth = useMaxWidthOrDefault(700);
  const title = computed<string>(() => {
    return isUpdate.value ? '编辑角色' : '新增角色';
  });

  const roleMenuTree = ref<MenuOption[]>([]);
  /** checkable 状态下节点选择完全受控（父子节点选中状态不再关联） */
  const menuCheckStrictly = ref<boolean>(false);

  // 展开的层级
  const defaultExpandLevel = 1;
  const roleTreeRef = ref<InstanceType<typeof BasicTree>>();

  const [registerInnerDrawer, { drawerLoading, closeDrawer }] = useDrawerInner(
    async (data: { record?: Recordable; update: boolean }) => {
      drawerLoading(true);
      // 先默认给一个节点关联 update的话自己会对应修改
      menuCheckStrictly.value = false;
      const { record, update } = data;
      isUpdate.value = update;
      if (update && record) {
        const ret = await roleInfo(record.roleId);
        // 是否节点独立/关联  后台返回的字段和antd是反的
        menuCheckStrictly.value = !ret.menuCheckStrictly;
        await setFieldsValue(ret);
        // tree 拿到tree数据和选中
        const response = await roleMenuTreeSelect(ret.roleId);
        const { menus, checkedKeys } = response;

        // 不显示租户相关菜单
        const excludeMenus = transformMenu(menus);
        // 菜单
        roleMenuTree.value = excludeMenus;

        /**
         * 找到所有的父节点ID
         * 主要是处理未点击菜单直接保存(即未触发handleChecked)是不带父节点ID的
         */
        /** 节点关联情况下是不带父节点的 */
        if (ret.menuCheckStrictly) {
          const parentIds = findGroupParentIds(menus, checkedKeys);
          checkedMenuKeys.value = [...parentIds, ...checkedKeys];
        } else {
          checkedMenuKeys.value = checkedKeys;
        }
        /**
         * 这里只用于显示数据
         * 这里用后端返回的不带父节点的ID antd会自动勾选父节点
         */
        await setFieldsValue({ menuIds: checkedKeys });
      } else {
        // tree 这里直接返回的数组
        const menus = await menuTreeSelect();
        // 不显示租户相关菜单
        const excludeMenus = transformMenu(menus);
        roleMenuTree.value = excludeMenus;
      }

      // 默认展开层级
      nextTick(() => {
        (roleTreeRef.value as any)?.filterByLevel(defaultExpandLevel);
      });

      drawerLoading(false);
    },
  );

  /**
   * 不显示租户相关菜单  分配了也没法使用
   * @param menuTree menus
   */
  function transformMenu(menuTree: MenuOption[]) {
    return filter(
      menuTree,
      (item) => {
        if (excludeIds.includes(item.id)) {
          return false;
        }
        return true;
      },
      { id: 'id', pid: 'parentId', children: 'children' },
    );
  }

  const [registerForm, { setFieldsValue, resetForm, validate }] = useForm({
    labelWidth: 100,
    layout: 'vertical',
    name: 'role_drawer',
    showActionButtonGroup: false,
    baseColProps: { span: 24 },
    schemas: modalSchemas,
  });

  // 已经选择的节点 包括父节点
  const checkedMenuKeys = ref<number[]>([]);
  /**
   *
   * @param checkedKeys 已经选中的子节点的ID
   * @param info info.halfCheckedKeys为父节点的ID
   */
  type CheckedState<T = number> = T[] | { checked: T[]; halfChecked: T[] };
  function handleChecked(checkedKeys: CheckedState, info: CheckInfo) {
    // 数组的话为节点关联
    if (checkedKeys instanceof Array) {
      const halfCheckedKeys: number[] = (info.halfCheckedKeys || []) as number[];
      checkedMenuKeys.value = [...halfCheckedKeys, ...checkedKeys];
    } else {
      checkedMenuKeys.value = [...checkedKeys.checked];
    }
  }

  /**
   * 选择/取消全部只会触发change事件
   * 回调参数在选择/取消 事件时只会是数组  忽略menuCheckStrictly
   * @param checkedKeys
   */
  function handleChange(checkedKeys: CheckedState) {
    if (checkedKeys instanceof Array) {
      const menuIds = findAllIds(roleMenuTree.value);
      // 取消全部
      if (checkedKeys.length === 0) {
        console.log('取消全部');
        checkedMenuKeys.value = [];
      }
      // 选择全部
      if (checkedKeys.length === menuIds.length) {
        console.log('选择全部');
        checkedMenuKeys.value = menuIds;
      }
    }
  }

  async function handleSubmit() {
    try {
      drawerLoading(true);
      const formData = await validate();
      // 需要传节点状态  关联还是独立  跟antd字段是相反的
      formData.menuCheckStrictly = !unref(menuCheckStrictly);
      formData.menuIds = [...unref(checkedMenuKeys)];
      if (unref(isUpdate)) {
        await roleUpdate(formData);
      } else {
        await roleAdd(formData);
      }
      emit('reload');
      closeDrawer();
      await clearFormFields();
    } catch (e) {
      console.warn(e);
    } finally {
      drawerLoading(false);
    }
  }

  async function clearFormFields() {
    await resetForm();
    checkedMenuKeys.value = [];
  }
</script>

<style scoped></style>
