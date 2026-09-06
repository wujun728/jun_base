<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统管理', '通知管理']"></Breadcrumb>
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
                <a-form-item field="noticeTitle" :label="'公告标题'">
                  <a-input
                      v-model="state.queryParams.noticeTitle"
                      :placeholder="'请输入公告标题'">
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="createBy" :label="'操作人员'">
                  <a-input
                      v-model="state.queryParams.createBy"
                      :placeholder="'请输入操作人员'">
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="noticeType" :label="'公告类型'">
                  <a-select v-model="state.queryParams.noticeType" placeholder="请选择">
                    <a-option
                        v-for="dict in state.noticeTypeOptions"
                        :key="dict.dictValue"
                        :value="dict.dictValue"
                        :label="dict.dictLabel"
                    >{{ dict.dictLabel }}
                    </a-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 42px" direction="vertical"/>
        <a-col :flex="'86px'" style="text-align: right">
          <a-space :size="18">
            <a-button type="primary" @click="fetchNoticeData">
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
              <a-button v-permission="['system:notice:add']" type="primary" @click="handleAdd">
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
          row-key="noticeId"
          :loading="loading"
          :pagination="pagination"
          :columns="state.columns"
          :data="state.noticeList"
          :bordered="false"
          :size="'medium'"
          @page-change="onPageChange"
      >
        <template #noticeType="{ record }">
          <dict-tag :options="state.noticeTypeOptions" :value="record.noticeType"/>
        </template>
        <template #status="{ record }">
          <dict-tag :options="state.noticeStatusOptions" :value="record.status"/>
        </template>
        <template #optional="{ record }">
          <a-space>
            <a-button-group>
              <a-button
                  v-permission="['system:notice:edit']" type="text" size="mini" @click="handleUpdate(record.noticeId)">
                <template #icon>
                  <icon-edit/>
                </template>
                修改
              </a-button>
              <a-button v-permission="['system:notice:remove']" type="text" size="mini" @click="handleDelete(record)">
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
    <edit-notice
        v-model:id="state.currentId"
        v-model:visible="state.editVisible"
        @refresh-data-list="fetchNoticeData">
    </edit-notice>
  </div>
</template>

<script lang="ts" setup>
// 定义页面加载中效果变量
import useLoading from "@/hooks/loading";
import {computed, reactive} from "vue";
import {DictDataRecord, getDicts} from "@/api/system/dict-data";
import {delNotice, listNotice, NoticeRecord} from "@/api/system/notice";
import {Message, Modal} from "@arco-design/web-vue";
import {Pagination} from "@/types/global";
import {TableColumnData} from "@arco-design/web-vue/es/table/interface";
import DictTag from "@/components/dict-tag/index.vue";
import EditNotice from "./components/edit-notice.vue";

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
    noticeTitle: undefined,
    noticeType: undefined,
    createBy: undefined,
    status: undefined
  },
  // 列表数据
  noticeList: [],
  // 字典
  noticeStatusOptions: [] as DictDataRecord[],
  noticeTypeOptions: [] as DictDataRecord[],
  // 选中行ID
  currentId: 0,
  // 显示编辑页面
  editVisible: false,
  // 列表显示列
  columns: computed<TableColumnData[]>(() => [
    {
      title: '序号',
      dataIndex: 'noticeId',
      slotName: 'noticeId',
      width: 50,
    },
    {
      title: '公告标题',
      dataIndex: 'noticeTitle',
      slotName: 'noticeTitle',
      ellipsis: true,
      tooltip: {position: 'right'},
      width: 300,
    },
    {
      title: '公告类型',
      dataIndex: 'noticeType',
      slotName: 'noticeType',
      width: 100,
    },
    {
      title: '状态',
      dataIndex: 'status',
      slotName: 'status',
      width: 100,
    },
    {
      title: '创建者',
      dataIndex: 'createBy',
      slotName: 'createBy',
      width: 100,
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
  const {data: noticeStatus} = await getDicts('sys_notice_status');
  state.noticeStatusOptions = noticeStatus;
  const {data: noticeType} = await getDicts('sys_notice_type');
  state.noticeTypeOptions = noticeType;
};
initData();

// 重置查询表单
const resetQueryForm = () => {
  state.queryParams = {
    noticeTitle: undefined,
    noticeType: undefined,
    createBy: undefined,
    status: undefined
  };
  state.currentId = 0;
};

// 获取列表数据
const fetchNoticeData = async () => {
  setLoading(true);
  const {rows, total} = await listNotice({
    ...pagination,
    ...state.queryParams
  })
  state.noticeList = rows;
  pagination.total = total;
  setLoading(false);
};
fetchNoticeData();

// 分页
const onPageChange = (current: number) => {
  pagination.current = current;
  fetchNoticeData();
};

// 打开新增
const handleAdd = () => {
  state.editVisible = true;
  state.currentId = 0;
};

// 打开编辑
const handleUpdate = (noticeId: number) => {
  state.editVisible = true;
  state.currentId = noticeId;
};

// 删除菜单
const handleDelete = (row: NoticeRecord) => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `是否确认删除公告编号为"${row.noticeId}"的数据项？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      delNotice(row.noticeId as number).then(() => {
        Message.success("删除成功");
        fetchNoticeData();
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
