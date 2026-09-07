<template>
  <BasicDrawer
    v-bind="$attrs"
    :title
    :width
    show-footer
    @register="registerDrawerInner"
    @ok="handleSubmit"
    @close="handleReset"
  >
    <BasicForm @register="registerForm">
      <template #listClass="{ model, field }">
        <FormItemRest>
          <div class="flex w-full gap-8px">
            <Select
              style="width: 160px !important"
              v-model:value="selectType"
              :options="selectOptions"
            />
            <Select
              class="flex-1"
              v-if="selectType === 'default'"
              placeholder="请选择标签样式"
              :options="tagSelectOptions()"
              :allow-clear="true"
              v-model:value="model[field]"
              @deselect="model.listClass = undefined"
            />
            <ColorPicker
              class="flex-1"
              v-if="selectType === 'custom'"
              v-model:value="model[field]"
            />
          </div>
        </FormItemRest>
      </template>
    </BasicForm>
  </BasicDrawer>
</template>

<script setup lang="ts">
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { BasicForm, useForm } from '@/components/Form';
  import { computed, ref, unref } from 'vue';
  import { dictDataAdd, dictDataUpdate, dictDetailInfo } from '@/api/system/dict/dictData';
  import { modalSchemas } from './dictData.data';
  import { useMaxWidthOrDefault } from '@/hooks/web/useSize';
  import { Select, FormItemRest } from 'ant-design-vue';
  import { tagSelectOptions, tagTypes } from '@/components/Dict/src/data';
  import { ColorPicker } from '@/components/ColorPicker';

  const selectType = ref('default');
  const selectOptions = [
    { label: '默认', value: 'default' },
    { label: '自定义', value: 'custom' },
  ];

  defineOptions({ name: 'DictDataModal' });

  const emit = defineEmits(['register', 'reload']);

  const isUpdate = ref<boolean>(false);
  const width = useMaxWidthOrDefault(700);
  const title = computed<string>(() => {
    return isUpdate.value ? '编辑字典数据' : '新增字典数据';
  });

  const currentDictType = ref<string>('');

  const [registerDrawerInner, { drawerLoading, closeDrawer }] = useDrawerInner(drawerOpenCallback);

  async function drawerOpenCallback(data: {
    record?: Recordable;
    update: boolean;
    dictType: string;
  }) {
    drawerLoading(true);
    const { record, update, dictType } = data;
    isUpdate.value = update;
    currentDictType.value = dictType;
    // 设置
    await setFieldsValue({ dictType });
    if (update && record) {
      const ret = await dictDetailInfo(record.dictCode);
      // 判断是自定义还是预设
      const isDefault = Reflect.has(tagTypes, ret.listClass);
      if (isDefault) {
        selectType.value = 'default';
      } else {
        selectType.value = 'custom';
      }
      // 为什么 placeholder 不显示 ？
      // https://www.antdv.com/components/select-cn#%E4%B8%BA%E4%BB%80%E4%B9%88-placeholder-%E4%B8%8D%E6%98%BE%E7%A4%BA-%EF%BC%9F
      if (!ret.listClass) {
        ret.listClass = void 0 as unknown as string;
      }
      await setFieldsValue(ret);
    }
    drawerLoading(false);
  }

  const [registerForm, { setFieldsValue, resetForm, validate }] = useForm({
    labelWidth: 100,
    name: 'dict_data_modal',
    showActionButtonGroup: false,
    baseColProps: { span: 24 },
    schemas: modalSchemas,
  });

  async function handleSubmit() {
    try {
      drawerLoading(true);
      const data = await validate();
      // 需要手动设置listClass为空的值  默认为空不会传递
      if (!data.listClass) {
        data.listClass = '';
      }
      if (unref(isUpdate)) {
        await dictDataUpdate(data);
      } else {
        await dictDataAdd(data);
      }
      emit('reload');
      closeDrawer();
      await handleReset();
    } catch (e) {
      console.warn(e);
    } finally {
      drawerLoading(false);
    }
  }

  async function handleReset() {
    await resetForm();
    selectType.value = 'default';
  }
</script>

<style scoped></style>
