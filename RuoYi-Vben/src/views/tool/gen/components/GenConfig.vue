<template>
  <div>
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
    <div class="flex gap-2 justify-center mt-5">
      <a-button :pre-icon="IconEnum.PREV" @click="handleJumpTo(0)">上一步</a-button>
      <a-button :post-icon="IconEnum.NEXT" type="primary" @click="handleJumpTo(2)">完成</a-button>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { Info } from '@/api/tool/gen/data';
  import { reactive, ref } from 'vue';
  import { IconEnum } from '@/enums/appEnum';
  import { vxeTableColumns, validRules } from './genConfig.data';
  import { BasicTableProps, VxeBasicTable, VxeGridInstance } from '@/components/VxeTable';
  import { useMessage } from '@/hooks/web/useMessage';
  import { cloneDeep } from 'lodash-es';

  const props = defineProps<{ value: Info }>();
  const emit = defineEmits<{ (e: 'jumpto', value: number, data?: Info): void }>();

  const tableRef = ref<VxeGridInstance>();

  const gridOptions = reactive<BasicTableProps>({
    id: 'CodeGenVxeTable',
    keepSource: true,
    border: true,
    editConfig: { trigger: 'click', mode: 'cell', showStatus: true },
    columns: vxeTableColumns,
    toolbarConfig: {
      enabled: false,
    },
    columnConfig: {
      resizable: true,
    },
    data: props.value.columns,
    proxyConfig: {
      // 必须设置
      enabled: false,
    },
    editRules: validRules,
  });

  const { createMessage } = useMessage();
  async function handleValidate() {
    const hasError = await tableRef.value?.validate();
    if (hasError) {
      createMessage.error('校验未通过');
      return Promise.reject(new Error('校验未通过'));
    }
    return Promise.resolve();
  }

  async function handleJumpTo(id: number) {
    try {
      // 校验
      await handleValidate();
      // const tableData = tableRef.value?.data;
      // 响应式 不需要从tableRef拿到
      const data = cloneDeep(props.value);

      emit('jumpto', id, data);
    } catch (e) {
      console.warn(e);
    }
  }
</script>

<style lang="less" scoped>
  /**
  去掉VxeTable的padding
  */
  :deep(.py-4) {
    padding-top: 0;
    padding-bottom: 0;
  }

  :deep(.px-6) {
    padding-right: 0;
    padding-left: 0;
  }
</style>
