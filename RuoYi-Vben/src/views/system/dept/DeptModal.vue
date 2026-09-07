<template>
  <BasicModal
    v-bind="$attrs"
    :title="title"
    :width="700"
    :min-height="350"
    @register="registerInnerModal"
    @ok="handleSubmit"
    @cancel="resetForm"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';
  import { computed, ref, unref } from 'vue';
  import { deptInfo, deptNodeList, deptUpdate, deptAdd, deptList } from '@/api/system/dept';
  import { modalSchemas } from './dept.data';
  import { listToTree, addFullName } from '@/utils/helper/treeHelper';

  defineOptions({ name: 'DeptModal' });
  const emits = defineEmits(['register', 'reload']);

  const isUpdate = ref<boolean>(false);
  const title = computed<string>(() => {
    return isUpdate.value ? '编辑部门' : '新增部门';
  });

  const [registerInnerModal, { modalLoading, closeModal }] = useModalInner(
    async (data: { deptId?: string | number; update: boolean }) => {
      modalLoading(true);
      const { deptId, update } = data;
      isUpdate.value = update;
      if (deptId) {
        await setFieldsValue({ parentId: deptId });
        if (unref(isUpdate)) {
          const ret = await deptInfo(deptId);
          await setFieldsValue(ret);
        }
      }
      /** 部门选择 下拉框 */
      await initDeptSelect(deptId);
      modalLoading(false);
    },
  );

  async function initDeptSelect(deptId?: string | number) {
    // 需要动态更新TreeSelect组件 这里允许为空
    const treeData = await getDeptTree(deptId, !unref(isUpdate));
    await updateSchema({
      field: 'parentId',
      componentProps: {
        treeData,
        treeLine: { showLeafIcon: false },
        fieldNames: { label: 'deptName', value: 'deptId' },
        treeDefaultExpandAll: true,
        // 选中后显示在输入框的值
        treeNodeLabelProp: 'fullName',
      },
    });
  }

  async function getDeptTree(deptId?: string | number, exclude = false) {
    let ret: any[] = [];
    if (!deptId || exclude) {
      ret = await deptList({});
    } else {
      ret = await deptNodeList(deptId);
    }
    const treeData = listToTree(ret, { id: 'deptId', pid: 'parentId' });
    // 添加部门名称 如 xx-xx-xx
    addFullName(treeData, 'deptName', ' / ');
    return treeData;
  }

  const [registerForm, { updateSchema, setFieldsValue, resetForm, validate }] = useForm({
    labelWidth: 100,
    name: 'dept_modal',
    showActionButtonGroup: false,
    schemas: modalSchemas,
    baseColProps: { span: 24 },
  });

  async function handleSubmit() {
    try {
      modalLoading(true);
      const data = await validate();
      if (unref(isUpdate)) {
        await deptUpdate(data);
      } else {
        await deptAdd(data);
      }
      emits('reload');
      closeModal();
      await resetForm();
    } catch (e) {
      console.log(e);
    } finally {
      modalLoading(false);
    }
  }
</script>

<style scoped></style>
