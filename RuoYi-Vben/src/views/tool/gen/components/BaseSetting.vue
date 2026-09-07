<template>
  <Row justify="center">
    <Col v-bind="{ xs: 24, sm: 24, md: 20, lg: 16, xl: 16 }">
      <BasicForm @register="register">
        <template #formFooter>
          <div class="w-full flex justify-center">
            <a-button type="primary" :post-icon="IconEnum.NEXT" @click="handleNext"
              >下一步</a-button
            >
          </div>
        </template>
      </BasicForm>
    </Col>
  </Row>
</template>

<script setup lang="ts">
  import { BasicForm, useForm } from '@/components/Form';
  import { Info } from '@/api/tool/gen/data';
  import { onMounted } from 'vue';
  import { menuList } from '@/api/system/menu';
  import { listToTree, addFullName } from '@/utils/helper/treeHelper';
  import { Row, Col } from 'ant-design-vue';
  import { IconEnum } from '@/enums/appEnum';
  import { formSchemas } from './baseSetting.data';

  defineOptions({ name: 'BaseSetting' });

  const props = defineProps<{ value: Info }>();
  const emit = defineEmits(['jumpto']);

  async function handleNext() {
    try {
      const data = await validate();
      emit('jumpto', 1, data);
    } catch (e) {
      console.warn(e);
    }
  }

  onMounted(async () => {
    if (props.value !== undefined) {
      await setFieldsValue(props.value);
      await initTreeSelect();
      await initMenuSelect();
    }
  });

  /**
   * 树表需要用到的数据
   */
  async function initTreeSelect() {
    const options = props.value.columns.map((item) => {
      const label = item.columnName + ' | ' + item.columnComment;
      return { label, value: item.columnName };
    });
    await updateSchema([
      {
        field: 'treeCode',
        componentProps: {
          options,
        },
      },
      {
        field: 'treeParentCode',
        componentProps: {
          options,
        },
      },
      {
        field: 'treeName',
        componentProps: {
          options,
        },
      },
    ]);
  }

  /**
   * 加载菜单选择
   */
  async function initMenuSelect() {
    const list = await menuList();
    const tree = listToTree(list, { id: 'menuId', pid: 'parentId' });
    const treeData = [
      {
        menuName: '根目录',
        menuId: 0,
        fullName: '根目录',
        children: tree,
      },
    ];
    addFullName(treeData, 'menuName', ' / ');

    await updateSchema({
      field: 'parentMenuId',
      componentProps: {
        treeData,
        fieldNames: {
          label: 'menuName',
          value: 'menuId',
        },
        treeDefaultExpandAll: false,
        treeNodeLabelProp: 'fullName',
        treeLine: { showLeafIcon: false },
        // 默认展开的树节点
        treeDefaultExpandedKeys: [0],
        // 设置弹窗滚动高度 默认256
        listHeight: 300,
      },
    });
  }

  const [register, { setFieldsValue, validate, updateSchema }] = useForm({
    labelAlign: 'right',
    autoSubmitOnEnter: true,
    labelWidth: 150,
    schemas: formSchemas,
    baseColProps: {
      span: 12,
    },
    showActionButtonGroup: false,
    showAdvancedButton: false,
  });
</script>

<style lang="less" scoped></style>
