<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统管理', '岗位管理']"></Breadcrumb>
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
                <a-form-item field="postCode" :label="'岗位编码'">
                  <a-input
                      v-model="state.queryParams.postCode"
                      :placeholder="'请输入岗位编码'">
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="postName" :label="'岗位名称'">
                  <a-input
                      v-model="state.queryParams.postName"
                      :placeholder="'请输入岗位名称'">
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="status" :label="'状态'">
                  <a-select v-model="state.queryParams.status" placeholder="请选择">
                    <a-option
                        v-for="dict in state.statusOptions"
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
            <a-button type="primary" @click="fetchPostData">
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
              <a-button v-permission="['system:post:add']" type="primary" @click="handleAdd">
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
          row-key="postId"
          :loading="loading"
          :pagination="pagination"
          :columns="state.columns"
          :data="state.postList"
          :bordered="false"
          :size="'medium'"
          @page-change="onPageChange"
      >
        <template #status="{ record }">
          <a-tag v-if="record.status === '1'" color="red">停用</a-tag>
          <a-tag v-else-if="record.status === '0'" color="blue">正常</a-tag>
          <a-tag v-else color="gray">未知</a-tag>
        </template>
        <template #optional="{ record }">
          <a-space>
            <a-button-group>
              <a-button
                  v-permission="['system:post:edit']" type="text" size="mini" @click="handleUpdate(record.postId)">
                <template #icon>
                  <icon-edit/>
                </template>
                修改
              </a-button>
              <a-button v-permission="['system:post:remove']" type="text" size="mini" @click="handleDelete(record)">
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
    <edit-post
        v-model:id="state.currentId"
        v-model:visible="state.editVisible"
        @refresh-data-list="fetchPostData">
    </edit-post>
  </div>
</template>

<script lang="ts" setup>
// 定义页面加载中效果变量
import useLoading from "@/hooks/loading";
import {computed, reactive} from "vue";
import {DictDataRecord, getDicts} from "@/api/system/dict-data";
import {delPost, listPost, PostRecord} from "@/api/system/post";
import {Message, Modal} from "@arco-design/web-vue";
import {Pagination} from "@/types/global";
import {TableColumnData} from "@arco-design/web-vue/es/table/interface";
import EditPost from "./components/edit-post.vue";

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
    postCode: undefined,
    postName: undefined,
    status: undefined,
    params: undefined,
  },
  // 列表数据
  postList: [],
  // 字典
  statusOptions: [] as DictDataRecord[],
  // 选中行ID
  currentId: 0,
  // 显示编辑页面
  editVisible: false,
  // 列表显示列
  columns: computed<TableColumnData[]>(() => [
    {
      title: '岗位编号',
      dataIndex: 'postId',
      slotName: 'postId',
      width: 100,
    },
    {
      title: '岗位编码',
      dataIndex: 'postCode',
      slotName: 'postCode',
      width: 150,
    },
    {
      title: '岗位名称',
      dataIndex: 'postName',
      slotName: 'postName',
      width: 260,
    },
    {
      title: '岗位排序',
      dataIndex: 'postSort',
      slotName: 'postSort',
      width: 100,
    },
    {
      title: '状态',
      dataIndex: 'status',
      slotName: 'status',
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
  const {data: statusData} = await getDicts('sys_normal_disable');
  state.statusOptions = statusData;
};
initData();

// 重置查询表单
const resetQueryForm = () => {
  state.queryParams = {
    postCode: undefined,
    postName: undefined,
    status: undefined,
    params: undefined,
  };
  state.currentId = 0;
};

// 获取列表数据
const fetchPostData = async () => {
  setLoading(true);
  const {rows, total} = await listPost({
    ...pagination,
    ...state.queryParams,
  })
  state.postList = rows;
  pagination.total = total;
  setLoading(false);
};
fetchPostData();

// 分页
const onPageChange = (current: number) => {
  pagination.current = current;
  fetchPostData();
};

// 打开新增
const handleAdd = () => {
  state.currentId = 0;
  state.editVisible = true;
};

// 打开编辑
const handleUpdate = (postId: number) => {
  state.currentId = postId;
  state.editVisible = true;
};

// 删除菜单
const handleDelete = (row: PostRecord) => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `是否确认删除岗位名称为"${row.postName}"的数据项？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      delPost(row.postId as number).then(() => {
        Message.success("删除成功");
        fetchPostData();
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
