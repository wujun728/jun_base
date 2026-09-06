<template>
  <a-modal :visible="open" :title="state.title" width="50vw" @cancel="handleCancel" @ok="handleConfirm">
    <a-row>
      <!-- 搜索区块 -->
      <a-col :flex="1">
        <a-form
            :model="state.queryParams"
            :label-col-props="{ span: 6 }"
            :wrapper-col-props="{ span: 18 }"
            label-align="left"
            auto-label-width
        >
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item field="tableName" :label="'表名称'">
                <a-input
                    v-model="state.queryParams.tableName"
                    :placeholder="'请输入表名称'">
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item field="tableComment" :label="'表描述'">
                <a-input
                    v-model="state.queryParams.tableComment"
                    :placeholder="'请输入表描述'">
                </a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-col>
      <a-divider style="height: 42px" direction="vertical"/>
      <a-col :flex="'86px'" style="text-align: right">
        <a-space :size="18">
          <a-button type="primary" @click="fetchTableList">
            <template #icon>
              <icon-search/>
            </template>
            查询
          </a-button>
          <a-button @click="resetQueryForm">
            <template #icon>
              <icon-refresh/>
            </template>
            重置
          </a-button>
        </a-space>
      </a-col>
      <a-divider style="margin-top: 0"/>
    </a-row>
    <!-- 表格数据-->
    <a-table
        ref="tableRef"
        v-model:selectedKeys="state.selectedKeys"
        row-key="tableName"
        :loading="loading"
        :pagination="pagination"
        :columns="state.columns"
        :data="state.tableList"
        :row-selection="state.rowSelection"
        :bordered="false"
        :size="'medium'"
        @page-change="onPageChange"
    >
    </a-table>
  </a-modal>
</template>

<script setup lang="ts">

import {computed, reactive, watch} from "vue";
import useLoading from "@/hooks/loading";
import {Pagination} from "@/types/global";
import type { TableColumnData } from "@arco-design/web-vue";
import {importTable, listDbTable} from "@/api/tool/gen";
import {Message} from "@arco-design/web-vue";

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
});

const open = computed(() => {
  return !!props.visible;
});

const {loading, setLoading} = useLoading(true);

const basePagination: Pagination = {
  current: 1,
  pageSize: 10,
};

const pagination = reactive({
  ...basePagination,
});
const state = reactive({
  title: '导入表',
  queryParams: {
    orderByColumn: '',
    isAsc: '',
    tableName: undefined,
    tableComment: undefined
  },
  rowSelection: {
    type: 'checkbox',
    showCheckedAll: true,
    onlyCurrent: false,
  } as unknown,
  // 列表数据
  tableList: [],
  currentRow: undefined,
  // 表格选中值
  selectedKeys: [],
  // 列表显示列
  columns: computed<TableColumnData[]>(() => [
    {
      title: '表名称',
      dataIndex: 'tableName',
      slotName: 'tableName',
      ellipsis: true,
      tooltip: true,
      width: 130,
    },
    {
      title: '表描述',
      dataIndex: 'tableComment',
      slotName: 'tableComment',
      ellipsis: true,
      tooltip: {position: 'right'},
      width: 130,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      slotName: 'createTime',
      width: 160,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      slotName: 'updateTime',
      width: 160,
    },
  ]),
});

// 查询列表
const fetchTableList = async () => {
  const {rows, total} = await listDbTable({
    ...pagination,
    ...state.queryParams
  });
  state.tableList = rows;
  pagination.total = total;
  setLoading(false);
};

fetchTableList();

// 分页
const onPageChange = (current: number) => {
  pagination.current = current;
  fetchTableList();
};

const resetQueryForm = () => {
  state.queryParams = {
    orderByColumn: '',
    isAsc: '',
    tableName: undefined,
    tableComment: undefined
  };
  state.currentRow = undefined;
};

const emits = defineEmits(['update:visible', 'refreshDataList']);

// 重置表单
const reset = () => {
  state.selectedKeys = [];
};

// 退出
const handleCancel = () => {
  emits('update:visible', false);
  reset();
  state.queryParams = {
    orderByColumn: '',
    isAsc: '',
    tableName: undefined,
    tableComment: undefined
  };
  state.tableList = []
};

// 确认
const handleConfirm = async () => {
  if(state.selectedKeys.length > 0) {
    const tableNames = state.selectedKeys.join(",")
    const { msg, code } = await importTable({ tables: tableNames })
    if ( code === 200 ) {
      Message.success(msg);
      emits('update:visible', false);
      emits('refreshDataList');
    } else {
      Message.error(msg);
    }
  } else {
    Message.warning("请选择要导入的表");
  }
};

watch(
    () => props.visible,
    (value) => {
      if (value) {
        state.title = '导入表';
        fetchTableList();
      } else {
        reset();
      }
    }
    , {immediate: true}
);

</script>

<style scoped lang="less">

</style>