<template>
  <!-- 表格数据-->
  <a-table
      ref="tableRef"
      row-key="columnId"
      :columns="state.columns"
      :data="tableList"
      :bordered="true"
      :pagination="false"
      :size="'medium'"
  >
    <template #index="{ record }">
      {{ tableList.indexOf(record) + 1 }}
    </template>
    <template #columnComment="{ record }">
      <a-input v-model="record.columnComment"></a-input>
    </template>
    <template #javaType="{ record }">
      <a-select v-model="record.javaType" placeholder="Select" :trigger-props="{ autoFitPopupMinWidth: true }">
        <a-option label="Long" value="Long" />
        <a-option label="String" value="String" />
        <a-option label="Integer" value="Integer" />
        <a-option label="Double" value="Double" />
        <a-option label="BigDecimal" value="BigDecimal" />
        <a-option label="Date" value="Date" />
        <a-option label="Boolean" value="Boolean" />
      </a-select>
    </template>
    <template #javaField="{ record }">
      <a-input v-model="record.javaField"></a-input>
    </template>
    <template #isInsert="{ record }">
      <a-checkbox v-model="record.isInsert"></a-checkbox>
    </template>
    <template #isEdit="{ record }">
      <a-checkbox v-model="record.isEdit"></a-checkbox>
    </template>
    <template #isList="{ record }">
      <a-checkbox v-model="record.isList"></a-checkbox>
    </template>
    <template #isQuery="{ record }">
      <a-checkbox v-model="record.isQuery"></a-checkbox>
    </template>
    <template #queryType="{ record }">
      <a-select v-model="record.queryType" placeholder="Select" :trigger-props="{ autoFitPopupMinWidth: true }">
        <a-option label="=" value="EQ" />
        <a-option label="!=" value="NE" />
        <a-option label=">" value="GT" />
        <a-option label=">=" value="GTE" />
        <a-option label="<" value="LT" />
        <a-option label="<=" value="LTE" />
        <a-option label="LIKE" value="LIKE" />
        <a-option label="BETWEEN" value="BETWEEN" />
      </a-select>
    </template>
    <template #isRequired="{ record }">
      <a-checkbox v-model="record.isRequired"></a-checkbox>
    </template>
    <template #htmlType="{ record }">
      <a-select v-model="record.htmlType" placeholder="Select" :trigger-props="{ autoFitPopupMinWidth: true }">
        <a-option label="文本框" value="input" />
        <a-option label="文本域" value="textarea" />
        <a-option label="下拉框" value="select" />
        <a-option label="单选框" value="radio" />
        <a-option label="复选框" value="checkbox" />
        <a-option label="日期控件" value="datetime" />
        <a-option label="图片上传" value="imageUpload" />
        <a-option label="文件上传" value="fileUpload" />
        <a-option label="富文本控件" value="editor" />
      </a-select>
    </template>
    <template #dictType="{ record }">
      <a-select v-model="record.dictType" placeholder="请选择">
        <a-option
            v-for="dict in state.dictOptions"
            :key="dict.dictType"
            :value="dict.dictType"
            :label="dict.dictName"
        >
          <span >{{ dict.dictName }}-</span>
          <span style=" color: #8492a6; font-size: 13px">{{ dict.dictType }}</span>
        </a-option>
      </a-select>
    </template>
  </a-table>

</template>

<script setup lang="ts">
import {computed, reactive} from "vue";
import {ColumnRow, GenTableDetailInfo} from "@/api/tool/gen";
import {TableColumnData} from "@arco-design/web-vue/es/table/interface";
import {DictTypeRecord, optionSelect as getDictOptionSelect} from "@/api/system/dict-type";

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => {},
  }
})

const emits = defineEmits(['update:modelValue']);

const tableList = computed<ColumnRow[]>({
  get() {
    return props.modelValue as ColumnRow[]
  },
  set(val) {
    emits('update:modelValue', val)
  },
})

const state = reactive({
  yesOrNoOptions: [ { label: '是', value: '1' }, { label: '否', value: '0' },],
  dictOptions: [] as DictTypeRecord[],
  columns:  computed<TableColumnData[]>(() => [
    {
      title: "序号",
      slotName: 'index',
      align: 'center',
      width: 60,
    },
    {
      title: '字段列名',
      dataIndex: 'columnName',
      slotName: 'columnName',
      width: 150,
    },
    {
      title: '字段描述',
      dataIndex: 'columnComment',
      slotName: 'columnComment',
      width: 100,
    },
    {
      title: '物理类型',
      dataIndex: 'columnType',
      slotName: 'columnType',
      width: 110,
    },
    {
      title: 'Java类型',
      dataIndex: 'javaType',
      slotName: 'javaType',
      width: 80,
    },
    {
      title: 'Java属性',
      dataIndex: 'javaField',
      slotName: 'javaField',
      width: 150,
    },
    {
      title: '插入',
      dataIndex: 'isInsert',
      slotName: 'isInsert',
      width: 50,
    },
    {
      title: '编辑',
      dataIndex: 'isEdit',
      slotName: 'isEdit',
      width: 50,
    },
    {
      title: '列表',
      dataIndex: 'isList',
      slotName: 'isList',
      width: 50,
    },
    {
      title: '查询',
      dataIndex: 'isQuery',
      slotName: 'isQuery',
      width: 50,
    },
    {
      title: '查询方式',
      dataIndex: 'queryType',
      slotName: 'queryType',
      width: 60,
    },
    {
      title: '必填',
      dataIndex: 'isRequired',
      slotName: 'isRequired',
      width: 50,
    },
    {
      title: '显示类型',
      dataIndex: 'htmlType',
      slotName: 'htmlType',
      width: 60,
    },
    {
      title: '字典类型',
      dataIndex: 'dictType',
      slotName: 'dictType',
      width: 180,
    },
  ]),
})

const initDict = async () => {
  /** 查询字典下拉列表 */
  const { data, code } = await getDictOptionSelect();
  state.dictOptions = data;
}
initDict();


</script>

<style scoped lang="less">

</style>