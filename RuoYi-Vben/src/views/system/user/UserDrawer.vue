<template>
  <BasicDrawer
    v-bind="$attrs"
    :width="drawerWidth"
    :title="title"
    :show-footer="true"
    @register="registerInnerDrawer"
    @ok="handleSubmit"
    @close="resetForm"
  >
    <BasicForm @register="registerForm">
      <template #postIds="{ model, field }">
        <!-- optionLabelProp为回填到输入框中的属性 -->
        <!-- optionFilterProp为搜索的属性 -->
        <Select
          v-model:value="model[field]"
          optionFilterProp="title"
          placeholder="请选择岗位"
          mode="multiple"
          optionLabelProp="title"
        >
          <SelectOption
            v-for="item in postList"
            :key="item.postId"
            :value="item.postId"
            :title="item.postName"
          >
            <div class="flex flex-row gap-1.5">
              <span>{{ item.postName }}</span>
              <Tag>{{ item.postCode }}</Tag>
            </div>
          </SelectOption>
        </Select>
      </template>
      <template #roleIds="{ model, field }">
        <Select
          v-model:value="model[field]"
          optionFilterProp="title"
          placeholder="请选择角色"
          mode="multiple"
          optionLabelProp="title"
        >
          <SelectOption
            v-for="item in roleList"
            :key="item.roleId"
            :value="item.roleId"
            :title="item.roleName"
          >
            <div class="flex flex-row gap-1.5">
              <span>{{ item.roleName }}</span>
              <Tag :color="getRoleAuthOption(item.dataScope)?.color">{{
                getRoleAuthOption(item.dataScope)?.label
              }}</Tag>
            </div>
          </SelectOption>
        </Select>
      </template>
    </BasicForm>
  </BasicDrawer>
</template>

<script setup lang="ts">
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { BasicForm, useForm } from '@/components/Form';
  import { computed, ref, unref } from 'vue';
  import { userAdd, userUpdate, userInfo, departmentTree } from '@/api/system/user';
  import { drawerSchemas } from './user.data';
  import { addFullName } from '@/utils/helper/treeHelper';
  import { useMaxWidthOrDefault } from '@/hooks/web/useSize';
  import { Select, Tag } from 'ant-design-vue';
  import { Role, Post } from '@/api/system/user/model';
  import { authScopeOptions } from '@/views/system/role/role.data';

  const SelectOption = Select.Option;

  defineOptions({ name: 'UserDrawer' });

  const emit = defineEmits(['register', 'reload']);

  const isUpdate = ref<boolean>(false);
  const drawerWidth = useMaxWidthOrDefault(700);
  const title = computed<string>(() => {
    return isUpdate.value ? '编辑用户' : '新增用户';
  });

  /** 岗位select 自定义下拉框 */
  const postList = ref<Post[]>([]);
  /** 角色select 自定义下拉框 */
  const roleList = ref<Role[]>([]);

  const [registerInnerDrawer, { drawerLoading, closeDrawer }] = useDrawerInner(
    async (data: { record?: Recordable; update: boolean }) => {
      drawerLoading(true);
      const { record, update } = data;
      isUpdate.value = update;
      // 在这里更新主要是打开编辑/新增 会有一个明显的隐藏/显示效果 不美观
      await updateSchema([
        {
          field: 'userName',
          componentProps: {
            disabled: update,
          },
        },
        {
          field: 'password',
          ifShow: !update,
        },
      ]);
      const response = record ? await userInfo(record.userId) : await userInfo();
      // 外部的roleIds postIds才是真正对应的  新增时为空
      const { postIds = [], roleIds = [], data: user, roles, posts } = response;
      // user不为空为更新
      if (user) {
        user.postIds = postIds;
        user.roleIds = roleIds;
      }
      // 角色下拉框
      roleList.value = roles;
      // 岗位下拉框
      postList.value = posts;
      // 部门选择
      await initDeptSelect();
      // 更新信息
      if (user && update) {
        await setFieldsValue(user);
      }
      drawerLoading(false);
    },
  );

  /**
   * 初始化部门选择
   */
  async function initDeptSelect() {
    // updateSchema
    const deptTree = await departmentTree();
    // 选中后显示在输入框的值 即父节点 / 子节点
    addFullName(deptTree, 'label', ' / ');
    await updateSchema([
      {
        field: 'deptId',
        componentProps: {
          treeData: deptTree,
          fieldNames: { value: 'id', label: 'label' },
          treeDefaultExpandAll: true,
          treeLine: { showLeafIcon: false },
          // 选中后显示在输入框的值
          treeNodeLabelProp: 'fullName',
        },
      },
    ]);
  }

  const [registerForm, { setFieldsValue, resetForm, validate, updateSchema }] = useForm({
    // layout: 'vertical',
    labelWidth: 100,
    name: 'user-drawer',
    showActionButtonGroup: false,
    baseColProps: { span: 24 },
    schemas: drawerSchemas,
  });

  /**
   * 获取对应的权限option
   */
  function getRoleAuthOption(id: string) {
    return authScopeOptions.find((item) => item.value === id);
  }

  async function handleSubmit() {
    try {
      drawerLoading(true);
      const data = await validate();
      if (unref(isUpdate)) {
        await userUpdate(data);
      } else {
        await userAdd(data);
      }
      emit('reload');
      closeDrawer();
      await resetForm();
    } catch (e) {
      console.warn(e);
    } finally {
      drawerLoading(false);
    }
  }
</script>

<style lang="less" scoped>
  :deep(.ant-tag) {
    margin-inline-end: 0;
  }
</style>
