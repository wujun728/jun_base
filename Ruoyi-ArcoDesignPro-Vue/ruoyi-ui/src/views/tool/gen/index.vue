<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统工具', '代码生成']"></Breadcrumb>

    <a-card
        class="general-card r-nav-card"
        :body-style="{ padding: '15px' }"
        :header-style="{ padding: '15px' }"
    >
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
              <a-col :span="8">
                <a-form-item field="tableName" :label="'表名称'">
                  <a-input
                      v-model="state.queryParams.tableName"
                      :placeholder="'请输入表名称'">
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="tableComment" :label="'表描述'">
                  <a-input
                      v-model="state.queryParams.tableComment"
                      :placeholder="'请输入表描述'">
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="dateRange" :label="'执行时间'">
                  <a-range-picker v-model="state.dateRange" style="width: 100%;"></a-range-picker>
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
        <!-- 工具栏 -->
        <a-row style="margin-bottom: 16px">
          <!-- 前置工具栏部分 -->
          <a-col :span="12">
            <a-space>
              <a-button v-permission="['tool:gen:code']" type="primary" @click="handleGenTable(undefined)">
                <template #icon>
                  <icon-download />
                </template>
                生成
              </a-button>
              <a-button v-permission="['tool:gen:import']" @click="handleOpenImportTable">
                <template #icon>
                  <icon-import />
                </template>
                导入
              </a-button>
            </a-space>
          </a-col>
        </a-row>
      </a-row>
      <!-- 表格数据-->
      <a-table
          ref="tableRef"
          v-model:selectedKeys="state.selectedKeys"
          row-key="tableId"
          :loading="loading"
          :pagination="pagination"
          :columns="state.columns"
          :data="state.tableList"
          :row-selection="state.rowSelection"
          :bordered="false"
          :size="'medium'"
          @select="handleSelected"
          @sorter-change="handleSorterChange"
          @page-change="onPageChange"
      >
        <template #index="{ record }">
          {{ state.tableList.indexOf(record) + 1 }}
        </template>
        <template #optional="{ record }">
          <a-space>
            <a-button-group>
              <a-button v-permission="['tool:gen:preview']" type="text" size="mini" @click="handlePreview(record)">
                <template #icon>
                  <icon-eye />
                </template>
                预览
              </a-button>
              <a-button v-permission="['tool:gen:edit']" type="text" size="mini" @click="handleEditTable(record)">
                <template #icon>
                  <icon-edit/>
                </template>
                编辑
              </a-button>
              <a-dropdown trigger="hover">
                <a-button v-permission="['tool:gen:remove', 'tool:gen:edit', 'tool:gen:code']" type="text" size="mini">
                  <template #icon>
                    <icon-double-right/>
                  </template>
                  更多
                </a-button>
                <template #content>
                  <a-doption>
                    <a-button v-permission="['tool:gen:remove']" type="text" size="mini" @click="handleDelete(record)">
                      <template #icon>
                        <icon-delete/>
                      </template>
                      删除
                    </a-button>
                  </a-doption>
                  <a-doption>
                    <a-button v-permission="['tool:gen:edit']" type="text" size="mini" @click="handleSyncDb(record)">
                      <template #icon>
                        <icon-sync />
                      </template>
                      同步
                    </a-button>
                  </a-doption>
                  <a-doption>
                    <a-button v-permission="['tool:gen:code']" type="text" size="mini" @click="handleGenTable(record)">
                      <template #icon>
                        <icon-cloud-download />
                      </template>
                      生成代码
                    </a-button>
                  </a-doption>
                </template>
              </a-dropdown>
            </a-button-group>
          </a-space>
        </template>
      </a-table>
    </a-card>
  </div>
  <import-table v-model:visible="state.importTableDialogVisible" @refresh-data-list="fetchTableList"></import-table>
  <preview-code v-model:visible="state.previewCodeDialogVisible" v-model:table-id="state.currentId"></preview-code>
</template>

<script lang="ts" setup>
import useLoading from "@/hooks/loading";
import {Pagination} from "@/types/global";
import {computed, reactive} from "vue";
import {TableColumnData} from "@arco-design/web-vue/es/table/interface";
import {delTable, genCode, GenTableRecord, listTable, syncDb} from "@/api/tool/gen";
import addDateRange from "@/utils/common";
import {Message, Modal} from "@arco-design/web-vue";
import {downloadZip} from "@/api/download";
import {useRouter} from "vue-router";
import ImportTable from "./components/import-table.vue"
import PreviewCode from "./components/preview-code.vue"

const {loading, setLoading} = useLoading(true);

const basePagination: Pagination = {
  current: 1,
  pageSize: 10,
};

const pagination = reactive({
  ...basePagination,
});

const state = reactive({
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
  // 时间选择
  dateRange: [],
  // 选中行ID
  currentId: 0,
  currentRow: undefined,
  // 表格选中值
  selectedKeys: [],
  // 弹窗状态
  importTableDialogVisible: false,
  previewCodeDialogVisible: false,
  // 选中表名字
  selectedTableNames: computed<string[]>(() => []),
  // 列表显示列
  columns: computed<TableColumnData[]>(() => [
    {
      title: "序号",
      slotName: 'index',
      width: 60,
    },
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
      title: '实体',
      dataIndex: 'className',
      slotName: 'className',
      ellipsis: true,
      tooltip: true,
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
    {
      title: '操作',
      dataIndex: 'optional',
      slotName: 'optional',
      fixed: 'right',
      width: 160,
    },
  ]),
});

// 查询列表
const fetchTableList = async () => {
  setLoading(true);
  const {rows, total} = await listTable({
    ...pagination,
    ...addDateRange(state.queryParams, state.dateRange)
  })
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

// 多选框选中数据
const handleSelected = (rowKeys: Array<number>, rowKey: number, record: GenTableRecord) => {
  if (rowKeys.includes(rowKey)) {
    state.selectedTableNames.push(record.tableName as string);
  } else {
    state.selectedTableNames.splice(state.selectedTableNames.indexOf(record.tableName as string), 1)
  }
};

// 排序触发事件
const handleSorterChange = (dataIndex: string, direction: string) => {
  state.queryParams.orderByColumn = dataIndex;
  state.queryParams.isAsc = direction;
  fetchTableList();
}

// 生成代码操作
const handleGenTable = (row: GenTableRecord | undefined) => {
  const tableNames = (row && row.tableName) || state.selectedTableNames.join(",")
  if (!tableNames) {
    Message.warning("请选择要生成的数据")
  }
  if(row && row.genType === "1") {
    genCode(row.tableName as string).then(res => {
      const { code } = res;
      if (code === 200) {
        Message.success(`成功生成到自定义路径：${row.genPath}`)
      }
    })
  } else {
    downloadZip(`/tool/gen/batchGenCode?tables=${tableNames}`, 'ruoyi.zip')
  }
};

// 打开预览
const handlePreview = (row: GenTableRecord) => {
  state.currentId = row.tableId as number;
  state.previewCodeDialogVisible = true;
};

const router = useRouter();

// 打开编辑
const handleEditTable = (row: GenTableRecord) => {
  const {tableId, tableName} = row;
  router.push({name: 'GenEdit', params: {tableId}, query: {tableName}});
};

// 删除
const handleDelete = (row: GenTableRecord) => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `是否确认删除表编号为"${row.tableId}"的数据项？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      delTable(row.tableId as number).then(() => {
        Message.success("删除成功");
        fetchTableList();
      })
    }
  })
};

// 同步
const handleSyncDb = (row: GenTableRecord) => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `确认要强制同步"${row.tableName}"表结构吗？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      syncDb(row.tableName as string).then((res) => {
        const { code } = res;
        if (code === 200) {
          Message.success("同步成功");
        } else {
          Message.error("未知错误")
        }
      })
    }
  })
};

// 打开导入表弹窗
const handleOpenImportTable = () => {
  state.importTableDialogVisible = true;
};

const resetQueryForm = () => {
  state.queryParams = {
    orderByColumn: '',
    isAsc: '',
    tableName: undefined,
    tableComment: undefined
  };
  state.dateRange = [];
  state.currentRow = undefined;
};

</script>

<style scoped>
.container {
  padding: 0 20px 20px 20px;
}
</style>
