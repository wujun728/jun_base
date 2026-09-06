<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统监控', '在线用户']"></Breadcrumb>
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
                <a-form-item field="ipaddr" :label="'登录地址IP'">
                  <a-input
                      v-model="state.queryParams.ipaddr"
                      :placeholder="'请输入登录地址IP'">
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="userName" :label="'用户名称'">
                  <a-input
                      v-model="state.queryParams.userName"
                      :placeholder="'请输入用户名称'">
                  </a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 42px" direction="vertical"/>
        <a-col :flex="'86px'" style="text-align: right">
          <a-space :size="18">
            <a-button type="primary" @click="fetchListData">
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
          <a-col :span="12"></a-col>
        </a-row>
      </a-row>

      <!-- 表格数据-->
      <a-table
          ref="tableRef"
          row-key="tokenId"
          :loading="loading"
          :pagination="pagination"
          :columns="state.columns"
          :data="state.list"
          :bordered="false"
          :size="'medium'"
          @page-change="onPageChange"
      >
        <template #optional="{ record }">
          <a-space>
            <a-button-group>
              <a-button
                  v-permission="['monitor:online:forceLogout']" type="text" size="mini"
                  @click="handleForceLogout(record)">
                <template #icon>
                  <icon-delete/>
                </template>
                强退
              </a-button>
            </a-button-group>
          </a-space>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
// 定义页面加载中效果变量
import useLoading from "@/hooks/loading";
import {computed, reactive} from "vue";
import {Pagination} from "@/types/global";
import {forceLogout, listOnline, OnlineRecord} from "@/api/monitor/online";
import {Message, Modal} from "@arco-design/web-vue";
import {delJob} from "@/api/monitor/job";
import {TableColumnData} from "@arco-design/web-vue/es/table/interface";

const {loading, setLoading} = useLoading(true);

const basePagination: Pagination = {
  current: 1,
  pageSize: 10,
};
const pagination = reactive({
  ...basePagination,
});

const state = reactive({
  // 列表显示列
  columns: computed<TableColumnData[]>(() => [
    {
      title: '会话编号',
      dataIndex: 'tokenId',
      slotName: 'tokenId',
      tooltip: {position: 'right'},
      width: 300,
    },
    {
      title: '用户名称',
      dataIndex: 'userName',
      slotName: 'userName',
      ellipsis: true,
      tooltip: {position: 'right'},
      width: 130,
    },
    {
      title: '部门名称',
      dataIndex: 'deptName',
      slotName: 'deptName',
      width: 130,
    },
    {
      title: '主机',
      dataIndex: 'ipaddr',
      slotName: 'ipaddr',
      width: 130,
    },
    {
      title: '登录地点',
      dataIndex: 'loginLocation',
      slotName: 'loginLocation',
      width: 110,
    },
    {
      title: '浏览器',
      dataIndex: 'browser',
      slotName: 'browser',
      width: 130,
    },
    {
      title: '操作系统',
      dataIndex: 'os',
      slotName: 'os',
      width: 130,
    },
    {
      title: '登录时间',
      dataIndex: 'loginTime',
      slotName: 'loginTime',
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
  queryParams: {
    orderByColumn: '',
    isAsc: '',
    ipaddr: undefined,
    userName: undefined
  },
  // 列表数据
  list: [],
  // 字典
  // 选中行ID
  currentId: '',
});

// 初始化字典
const initData = async () => {

};
initData();

// 重置查询表单
const resetQueryForm = () => {
  state.queryParams = {
    orderByColumn: '',
    isAsc: '',
    ipaddr: undefined,
    userName: undefined
  };
};

// 获取列表数据
const fetchListData = async () => {
  setLoading(true);
  const {rows, total} = await listOnline({
    ...pagination,
    ...state.queryParams,
  })
  state.list = rows;
  pagination.total = total;
  setLoading(false);
};
fetchListData();

// 分页
const onPageChange = (current: number) => {
  pagination.current = current;
  fetchListData();
};

// 强退
const handleForceLogout = async (record: OnlineRecord) => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `是否确认强退名称为"${record.userName}"的用户？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      forceLogout(record.tokenId).then(() => {
        Message.success("删除成功");
        fetchListData();
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
