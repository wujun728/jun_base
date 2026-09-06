<template>
  <div>
    <a-form ref="queryFormObj.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
        <a-row :gutter="24">
       <a-col span="8">
          <a-form-item name="deptId" label="所属部门">
           <a-tree-select v-model:value="queryFormObj.data.deptId" :tree-data="pageDataObj.deptTreeData" :fieldNames="{ children: 'children', label: 'label', key: 'id', value: 'id' }"> </a-tree-select>
          </a-form-item>
      </a-col>
      <a-col span="8">
          <a-form-item name="userName" label="用户账号">
            <a-input v-model:value="queryFormObj.data.userName" allowClear></a-input>
          </a-form-item>
      </a-col>
      <a-col span="8">
          <a-form-item name="nickName" label="用户名称">
            <a-input v-model:value="queryFormObj.data.nickName" allowClear></a-input>
          </a-form-item>
      </a-col>
     <a-col span="8">
          <a-form-item name="sex" label="用户性别">
            <a-select v-model:value="queryFormObj.data.sex" :options="pageDataObj.sysUserSexDict" allowClear> </a-select>
          </a-form-item>
      </a-col>
     <a-col span="8">
          <a-form-item name="status" label="帐号状态">
            <a-select v-model:value="queryFormObj.data.status" :options="pageDataObj.sysNormalDisableDict" allowClear> </a-select>
          </a-form-item>
      </a-col>
      </a-row>
      <a-row :gutter="24" class="autoee-button-row">
        <a-col :span="12" style="text-align: left">
          <a-button type="primary" @click="openAddModal()" v-hasPermi="['system:user:add']"><AutoeeIcon icon="plus-outlined" />新增</a-button>
          <a-button type="primary" danger @click="clickDelete()" :disabled="tableObj.selectedRowKeys.length <= 0" v-hasPermi="['system:user:remove']"><AutoeeIcon icon="delete-outlined" />删除</a-button>
           <a-button type="primary" @click="clickExport()" v-hasPermi="['system:user:export']"><AutoeeIcon icon="export-outlined" />导出</a-button>
        </a-col>
        <a-col :span="12" style="text-align: right">
          <a-button type="primary" @click="queryTableData()"> <AutoeeIcon icon="SearchOutlined" />查询 </a-button>
          <a-button @click="resetQueryForm()"><AutoeeIcon icon="redo-outlined" />重置</a-button>
        </a-col>
      </a-row>
    </a-form>

    <a-table
      rowKey="userId"
      :columns="tableObj.columns"
      :data-source="tableObj.dataSource"
      :loading="tableObj.loading"
      :pagination="tablePagination"
      @change="tableHandChangePage"
      :row-selection="{ selectedRowKeys: tableObj.selectedRowKeys, onChange: onTableSelectChange }"
      bordered
      size="small"
      tableLayout="fixed"
    >
      <template #bodyCell="{ index, column, record }">
        <template v-if="column.key === 'pageIndex'">
          {{ index + 1 }}
        </template>
       <template v-else-if="column.key === 'deptId'">
          <span>
             {{ AutoeeUtil.getDictLabelByKey(record.deptId , pageDataObj.deptIdDict) }}
          </span>
        </template>
        <template v-else-if="column.key === 'sex'">
          <span>
            {{ AutoeeUtil.getDictLabelByKey(record.sex , pageDataObj.sysUserSexDict) }}
          </span>
        </template>
        <template v-else-if="column.key === 'status'">
          <span>
            {{ AutoeeUtil.getDictLabelByKey(record.status , pageDataObj.sysNormalDisableDict) }}
          </span>
        </template>
        <template v-else-if="column.key === 'userTableOperateCol'">
          <span>
            <a @click="openDetailModal(record)"> 查看 </a>
            <a-divider type="vertical" v-hasPermi="['system:user:edit']"/>
            <a @click="openUpdateModal(record)"  v-hasPermi="['system:user:edit']"> 修改 </a>
            <a-divider type="vertical" v-hasPermi="['system:user:edit']" />
            <a @click="openResetPasswordModal(record)"> 重置密码 </a>
          </span>
        </template>
      </template>
    </a-table>

    <AddUpdateModal ref="addUpdateModalRef" @refreshFatherPageTable="queryTableData"
        :deptIdDict="pageDataObj.deptIdDict"
        :userTypeDict="pageDataObj.userTypeDict"
        :sysUserSexDict="pageDataObj.sysUserSexDict"
        :sysNormalDisableDict="pageDataObj.sysNormalDisableDict"
    />
    <DetailModal ref="detailModalRef"
        :deptIdDict="pageDataObj.deptIdDict"
        :userTypeDict="pageDataObj.userTypeDict"
        :sysUserSexDict="pageDataObj.sysUserSexDict"
        :sysNormalDisableDict="pageDataObj.sysNormalDisableDict"
    />

    <UseResetPassword ref="useResetPasswordRef" />
  </div>
</template>

<script setup name="User">
import { listUser, getUser, delUser, addUser, updateUser } from "@/api/system/user";
import AddUpdateModal from './addUpdateModal.vue';
import DetailModal from './detailModal.vue';
import UseResetPassword from './useResetPassword.vue';
import { computed, reactive, ref } from 'vue';
import { AutoeeIcon } from '@/utils/AutoeeIcon.js';
import AutoeeUtil from '@/utils/AutoeeUtil.js';
import { message } from 'ant-design-vue';
import moment from 'moment';

// 当前页面使用的数据
const pageDataObj = reactive({
  deptTreeData: [],
  userTypeDict: [],
  sysUserSexDict: [],
  sysNormalDisableDict: [],
});

AutoeeUtil.getDeptTreeData().then((res) => {
  pageDataObj.deptTreeData = res.data;
});
// 自定义转码字典
pageDataObj.userTypeDict = [ { value: 'v1', label: '自定义1' }, { value: 'v2', label: '自定义2' }];
AutoeeUtil.getDictsByType('sys_user_sex').then((res) => {
  pageDataObj.sysUserSexDict = res;
});
AutoeeUtil.getDictsByType('sys_normal_disable').then((res) => {
  pageDataObj.sysNormalDisableDict = res;
});


// 查询Form
const queryFormRef = ref();
const queryFormObj = reactive({
  data: {
    pageNum: 1,
    pageSize: 10,
    params: {}
  }
});

// 重置查询Form
const resetQueryForm = () => {
  queryFormObj.data = {
    pageNum: 1,
    pageSize: 10,
    params: {}
  }
  queryTableData();
};

// 列表定义
const tableObj = reactive({
  // 列表数据集
  dataSource: [],
  // 列表总记录数
  total: 0,
  // 列表是否加载中
  loading: false,
  // 列表选中行数组
  selectedRowKeys: [],
  // 列表列定义
  columns: [
    {
      title: '序号',
      dataIndex: 'pageIndex',
      key: 'pageIndex',
      width: 50,
      align: 'center',
    },
    // {
    //   title: '所属部门',
    //   key: 'deptId',
    //   dataIndex: 'deptId',
    // },
    {
      title: '用户账号',
      key: 'userName',
      dataIndex: 'userName',
    },
    {
      title: '用户名称',
      key: 'nickName',
      dataIndex: 'nickName',
    },
    {
      title: '手机号码',
      key: 'phonenumber',
      dataIndex: 'phonenumber',
    },
    {
      title: '用户性别',
      key: 'sex',
      dataIndex: 'sex',
    },
    {
      title: '帐号状态',
      key: 'status',
      dataIndex: 'status',
    },
    {
      title: '操作',
      key: 'userTableOperateCol',
      width: 180,
    },
  ],
});

// 查询列表数据方法
const queryTableData = () => {
  // 调用后端列表查询方法，通过返回结果设置tableObj.total，tableObj.dataSource，tableObj.loading
  AutoeeUtil.getTableDataByQueryFunc(listUser(queryFormObj.data),tableObj);
}

// 初始化查询列表数据
queryTableData();

// 列表翻页工具条：必须通过计算函数每次重新生成
const tablePagination = computed(() => AutoeeUtil.createTablePagination(tableObj, queryFormObj));

// 手动翻页方法
const tableHandChangePage = (page, filters, sorter) => {
  queryFormObj.data.pageSize = page.pageSize;
  queryFormObj.data.pageNum = page.current;
  queryTableData();
};

// 列表行选中方法
const onTableSelectChange = (selectedRowKeys) => {
  tableObj.selectedRowKeys = selectedRowKeys;
};

// 打开新增窗口
const addUpdateModalRef = ref();
const openAddModal = () => {
  addUpdateModalRef.value.openAddModal();
};

// 打开修改窗口
const openUpdateModal = (record) => {
  addUpdateModalRef.value.openUpdateModal(record);
};

//重置密码
const useResetPasswordRef = ref();

// 打开重置密码窗口
const openResetPasswordModal = (record) => {
  useResetPasswordRef.value.openModal(record);
};

// 打开详细窗口
const detailModalRef = ref();
const openDetailModal = (record) => {
  detailModalRef.value.openModal(record);
};

// 点击删除
const clickDelete = () => {
  AutoeeUtil.confirmDeleteSelectedData(() => {
    delUser(tableObj.selectedRowKeys).then((res) => {
      AutoeeUtil.messageSuccess('删除操作成功。');
      userTablePage.reload();
    });
  });
};

 // 点击导出
const clickExport = () => {
  AutoeeUtil.download('system/user/export', queryFormObj.data, "用户信息_" + moment().format('YYYY-MM-DD_HH-mm-ss') + ".xlsx");
};

</script>
<style lang="less"></style>
