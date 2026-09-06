<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统管理', '参数管理']"></Breadcrumb>
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
                <a-form-item field="configName" :label="'参数名称'">
                  <a-input
                      v-model="state.queryParams.configName"
                      :placeholder="'请输入参数名称'">
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="configKey" :label="'参数键名'">
                  <a-input
                      v-model="state.queryParams.configKey"
                      :placeholder="'请输入参数键名'">
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="configType" :label="'系统内置'">
                  <a-select v-model="state.queryParams.configType" placeholder="请选择">
                    <a-option
                        v-for="dict in state.configTypeOptions"
                        :key="dict.dictValue"
                        :value="dict.dictValue"
                        :label="dict.dictLabel"
                    >{{ dict.dictLabel }}
                    </a-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="dateRange" :label="'创建时间'">
                  <a-range-picker v-model="state.dateRange" style="width: 100%;"></a-range-picker>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 84px" direction="vertical"/>
        <a-col :flex="'86px'" style="text-align: right">
          <a-space direction="vertical" :size="18">
            <a-button type="primary" @click="fetchConfigData">
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
              <a-button v-permission="['system:config:add']" type="primary" @click="handleAdd">
                <template #icon>
                  <icon-plus/>
                </template>
                新建
              </a-button>
            </a-space>
          </a-col>
        </a-row>
      </a-row>

      <!-- 表格数据-->
      <a-table
          ref="tableRef"
          row-key="configId"
          :loading="loading"
          :pagination="pagination"
          :columns="state.columns"
          :data="state.configList"
          :bordered="false"
          :size="'medium'"
          @page-change="onPageChange"
      >
        <template #configType="{ record }">
          <dict-tag :options="state.configTypeOptions" :value="record.configType"/>
        </template>
        <template #optional="{ record }">
          <a-space>
            <a-button-group>
              <a-button
                  v-permission="['system:config:edit']" type="text" size="mini" @click="handleUpdate(record.configId)">
                <template #icon>
                  <icon-edit/>
                </template>
                修改
              </a-button>
              <a-button v-permission="['system:config:remove']" type="text" size="mini" @click="handleDelete(record)">
                <template #icon>
                  <icon-delete/>
                </template>
                删除
              </a-button>
            </a-button-group>
          </a-space>
        </template>
      </a-table>
    </a-card>
    <edit-config
        v-model:id="state.currentId"
        v-model:visible="state.editVisible"
        @refresh-data-list="fetchConfigData">
    </edit-config>
  </div>
</template>

<script lang="ts" setup>
// 定义页面加载中效果变量
import useLoading from "@/hooks/loading";
import {computed, reactive} from "vue";
import {DictDataRecord, getDicts} from "@/api/system/dict-data";
import {ConfigRecord, delConfig, listConfig} from "@/api/system/config";
import {Message, Modal} from "@arco-design/web-vue";
import {Pagination} from "@/types/global";
import {TableColumnData} from "@arco-design/web-vue/es/table/interface";
import addDateRange from "@/utils/common";
import DictTag from "@/components/dict-tag/index.vue";
import EditConfig from "./components/edit-config.vue";

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
    configName: undefined,
    configKey: undefined,
    configType: undefined
  },
  // 时间选择
  dateRange: [],
  // 列表数据
  configList: [],
  // 字典
  configTypeOptions: [] as DictDataRecord[],
  // 选中行ID
  currentId: 0,
  // 显示编辑页面
  editVisible: false,
  // 列表显示列
  columns: computed<TableColumnData[]>(() => [
    {
      title: '参数主键',
      dataIndex: 'configId',
      slotName: 'configId',
      width: 100,
    },
    {
      title: '参数名称',
      dataIndex: 'configName',
      slotName: 'configName',
      ellipsis: true,
      tooltip: {position: 'right'},
      width: 200,
    },
    {
      title: '参数键名',
      dataIndex: 'configKey',
      slotName: 'configKey',
      width: 200,
    },
    {
      title: '参数键值',
      dataIndex: 'configValue',
      slotName: 'configValue',
      width: 100,
    },
    {
      title: '系统内置',
      dataIndex: 'configType',
      slotName: 'configType',
      width: 100,
    },
    {
      title: '备注',
      dataIndex: 'remark',
      slotName: 'remark',
      ellipsis: true,
      tooltip: {position: 'right'},
      width: 300,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      slotName: 'createTime',
      width: 200,
    },
    {
      title: '操作',
      dataIndex: 'optional',
      slotName: 'optional',
      fixed: 'right',
      width: 200,
    },
  ]),
});

// 初始化字典
const initData = async () => {
  const {data: yesNoData} = await getDicts('sys_yes_no');
  state.configTypeOptions = yesNoData;
};
initData();

// 重置查询表单
const resetQueryForm = () => {
  state.queryParams = {
    configName: undefined,
    configKey: undefined,
    configType: undefined
  };
  state.dateRange = [];
  state.currentId = 0;
};

// 获取列表数据
const fetchConfigData = async () => {
  setLoading(true);
  const {rows, total} = await listConfig({
    ...pagination,
    ...addDateRange(state.queryParams, state.dateRange)
  })
  state.configList = rows;
  pagination.total = total;
  setLoading(false);
};
fetchConfigData();

// 分页
const onPageChange = (current: number) => {
  pagination.current = current;
  fetchConfigData();
};

// 打开新增
const handleAdd = () => {
  state.currentId = 0;
  state.editVisible = true;
};

// 打开编辑
const handleUpdate = (configId: number) => {
  state.currentId = configId;
  state.editVisible = true;
};

// 删除
const handleDelete = (row: ConfigRecord) => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `是否确认删除参数编号为"${row.configId}"的数据项？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      delConfig(row.configId as number).then(() => {
        Message.success("删除成功");
        fetchConfigData();
      });
    },
  });
};

</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}
</style>
