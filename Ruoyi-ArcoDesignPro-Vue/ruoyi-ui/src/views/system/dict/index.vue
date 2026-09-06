<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统管理', '字典管理']"/>
    <a-row>
      <!-- 左侧字典类型布局-->
      <a-col :span="6">
        <a-card
            class="general-card l-nav-card"
            :body-style="{ padding: '15px' }"
            :header-style="{ padding: '15px' }"
        >
          <dict-type-list @select="handleDictTypeClick" @first-dict-type="handleDictTypeClick"></dict-type-list>
        </a-card>
      </a-col>
      <!-- 右侧布局 -->
      <a-col :span="18">
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
                    <a-form-item field="postCode" :label="'字典类型'">
                      <a-input
                          v-model="state.queryParams.dictType"
                          :disabled="true"
                          :placeholder="'请输入字典名称'">
                      </a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :span="8">
                    <a-form-item field="postName" :label="'字典标签'">
                      <a-input
                          v-model="state.queryParams.dictLabel"
                          :placeholder="'请输入字典标签'">
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
                <a-button type="primary" @click="fetchDictDataList">
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
                  <a-button v-permission="['system:dict:add']" type="primary" @click="handleAdd">
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
              :data="state.dictDataList"
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
                      v-permission="['system:dict:edit']" type="text" size="mini"
                      @click="handleUpdate(record.dictCode)">
                    <template #icon>
                      <icon-edit/>
                    </template>
                    修改
                  </a-button>
                  <a-button v-permission="['system:dict:remove']" type="text" size="mini" @click="handleDelete(record)">
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
        <edit-dict-data
            v-model:dict-code="state.currentId"
            v-model:dict-type="state.selectedTpye"
            v-model:visible="state.editVisible"
            @refresh-data-list="fetchDictDataList">
        </edit-dict-data>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import {Pagination} from "@/types/global";
import {computed, reactive} from "vue";
import {delData, DictDataRecord, getDicts, listData} from "@/api/system/dict-data";
import {Message, Modal} from "@arco-design/web-vue";
import {TableColumnData} from "@arco-design/web-vue/es/table/interface";
import useLoading from "@/hooks/loading";
import EditDictData from "./components/edit-dict-data.vue";
import DictTypeList from "./components/list-dict-type.vue";

const basePagination: Pagination = {
  current: 1,
  pageSize: 10,
};
const pagination = reactive({
  ...basePagination,
});

const state = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    dictName: undefined,
    dictType: undefined,
    dictLabel: undefined,
    status: undefined
  },
  // 列表数据
  dictDataList: [],
  // 字典
  statusOptions: [] as DictDataRecord[],
  // 选中行ID
  currentId: '',
  // 字典类型ID
  selectedTpye: {},
  // 显示编辑页面
  editVisible: false,
  // 列表显示列
  columns: computed<TableColumnData[]>(() => [
    {
      title: '字典编码',
      dataIndex: 'dictCode',
      slotName: 'dictCode',
      width: 100,
    },
    {
      title: '字典标签',
      dataIndex: 'dictLabel',
      slotName: 'dictLabel',
      ellipsis: true,
      tooltip: true,
      width: 200,
    },
    {
      title: '字典键值',
      dataIndex: 'dictValue',
      slotName: 'dictValue',
      ellipsis: true,
      tooltip: true,
      width: 150,
    },
    {
      title: '字典排序',
      dataIndex: 'dictSort',
      slotName: 'dictSort',
      width: 100,
    },
    {
      title: '状态',
      dataIndex: 'status',
      slotName: 'status',
      width: 100,
    },
    {
      title: '备注',
      dataIndex: 'remark',
      slotName: 'remark',
      width: 150,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      slotName: 'createTime',
      width: 180,
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
    pageNum: 1,
    pageSize: 10,
    dictName: undefined,
    dictType: state.queryParams.dictType,
    dictLabel: undefined,
    status: undefined
  };
  state.currentId = '';
};

const {loading, setLoading} = useLoading(true);
// 获取列表数据
const fetchDictDataList = async () => {
  setLoading(true);
  const {rows, total} = await listData({
    ...state.queryParams,
  })
  state.dictDataList = rows;
  pagination.current = state.queryParams.pageNum;
  pagination.total = total;
  setLoading(false);
};

// 分页
const onPageChange = (current: number) => {
  state.queryParams.pageNum = current;
  fetchDictDataList();
};

// 打开新增
const handleAdd = () => {
  state.currentId = '';
  state.editVisible = true;
};

// 打开编辑
const handleUpdate = (dictCode: string) => {
  state.currentId = dictCode;
  state.editVisible = true;
};

// 删除菜单
const handleDelete = (row: any) => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `是否确认删除字典编码为"${row.dictCode}"的数据项？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      delData(row.dictCode).then(() => {
        Message.success("删除成功");
        fetchDictDataList();
      });
    },
  });
};

// 点击字典类型 筛选左侧树
const handleDictTypeClick = (type: any) => {
  state.selectedTpye = type;
  state.queryParams.dictType = type.dictType;
  fetchDictDataList();
};
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}

:deep(.arco-table-th) {
  &:last-child {
    .arco-table-th-item-title {
      margin-left: 16px;
    }
  }
}

.action-icon {
  margin-left: 12px;
  cursor: pointer;
}

.active {
  color: #0960bd;
  background-color: #e3f4fc;
}

.setting {
  display: flex;
  align-items: center;
  width: 200px;

  .title {
    margin-left: 12px;
    cursor: pointer;
  }
}

.l-nav-card {
  margin-right: 5px;
}

.r-nav-card {
  margin-left: 5px;
}
</style>
