<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统管理', '角色管理']"></Breadcrumb>
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
                <a-form-item field="number" :label="'角色名称'">
                  <a-input
                      v-model="state.queryParams.roleName"
                      :placeholder="'请输入角色名称'"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="roleKey" :label="'权限字符'">
                  <a-input
                      v-model="state.queryParams.roleKey"
                      :placeholder="'请输入权限字符'"
                      maxlength="11"></a-input>
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
            <a-button type="primary" @click="fetchRoleData">
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
              <a-button v-permission="['system:role:add']" type="primary" @click="handleAdd">
                <template #icon>
                  <icon-plus/>
                </template>
                新建
              </a-button>
              <a-upload v-permission="['system:role:export']" action="/">
                <template #upload-button>
                  <a-button>导出</a-button>
                </template>
              </a-upload>
            </a-space>
          </a-col>
        </a-row>
      </a-row>
      <!-- 表格数据-->
      <a-table
          row-key="id"
          :loading="loading"
          :pagination="state.pagination"
          :columns="state.columns"
          :data="state.roleList"
          :bordered="false"
          :size="'medium'"
          @page-change="onPageChange"
      >
        <template #status="{ record }">
          <a-switch
              checked-value="0" unchecked-value="1"
              :model-value="record.status"
              @change="handleStatusChange(record)">
          </a-switch>
        </template>
        <template #optional="{ record }">
          <a-space v-if="record.roleId !== 1">
            <a-button-group>
              <a-button v-permission="['system:role:edit']" type="text" size="mini" @click="handleUpdate(record)">
                <template #icon>
                  <icon-edit/>
                </template>
                修改
              </a-button>
              <a-button v-permission="['system:role:remove']" type="text" size="mini" @click="handleDelete(record)">
                <template #icon>
                  <icon-delete/>
                </template>
                删除
              </a-button>
              <a-dropdown trigger="hover">
                <a-button v-permission="['system:role:edit']" type="text" size="mini">
                  <template #icon>
                    <icon-double-right/>
                  </template>
                  更多
                </a-button>
                <template #content>
                  <a-doption>
                    <a-button
                        v-permission="['system:role:edit']"
                        type="text" size="mini"
                        @click="handleDataScope(record)">
                      <template #icon>
                        <icon-check-circle/>
                      </template>
                      数据权限
                    </a-button>
                  </a-doption>
                  <a-doption>
                    <a-button
                        v-permission="['system:role:edit']"
                        type="text" size="mini"
                        @click="handleAuthUser(record)">
                      <template #icon>
                        <icon-user-group/>
                      </template>
                      分配用户
                    </a-button>
                  </a-doption>
                </template>
              </a-dropdown>
            </a-button-group>
          </a-space>
        </template>
      </a-table>
    </a-card>
    <edit-role
        v-model:id="state.currentId" v-model:visible="state.editVisible"
        :status-options="state.statusOptions"
        @refresh-data-list="fetchRoleData">
    </edit-role>
    <edit-data-scope
        v-model:id="state.currentId" v-model:visible="state.editDataScopeVisible">
    </edit-data-scope>
  </div>
</template>

<script lang="ts" setup>

// 初始化查询参数
import {computed, reactive} from "vue";
import addDateRange from "@/utils/common";
import {changeRoleStatus, delRole, listRole, RoleRecord} from "@/api/system/role";
import useLoading from "@/hooks/loading";
import {Message, Modal} from "@arco-design/web-vue";
import {DictDataRecord, getDicts} from "@/api/system/dict-data";
import {useRouter} from 'vue-router';
import {TableColumnData} from "@arco-design/web-vue/es/table/interface";
import EditRole from "./components/edit-role.vue"
import EditDataScope from "./components/edit-data-scope.vue"

const {loading, setLoading} = useLoading(true);
const router = useRouter();

const state = reactive({
  pagination: {
    current: 1,
    pageSize: 10,
    total: 0,
  },
  queryParams: {
    roleName: undefined,
    roleKey: undefined,
    status: undefined,
    params: undefined,
  },
  dateRange: [],
  // 字典
  statusOptions: [] as DictDataRecord[],
  // 角色表格数据
  roleList: [],
  currentId: 0,
  editVisible: false,
  editDataScopeVisible: false,
  editAuthUserVisible: false,
  columns: computed<TableColumnData[]>(() => [
    {
      title: '角色编号',
      dataIndex: 'roleId',
      slotName: 'roleId',
      width: 120,
    },
    {
      title: '角色名称',
      dataIndex: 'roleName',
      width: 150,
    },
    {
      title: '权限字符',
      dataIndex: 'roleKey',
      width: 150,
    },
    {
      title: '显示顺序',
      dataIndex: 'roleSort',
      width: 100,
    },
    {
      title: '状态',
      dataIndex: 'status',
      slotName: 'status',
      width: 120,
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
  ])
})

const initData = async () => {
  const {data: statusData} = await getDicts('sys_normal_disable');
  state.statusOptions = statusData;
};
initData();

const resetQueryForm = () => {
  state.pagination = {
    current: 1,
    pageSize: 10,
    total: 1,
  };
  state.queryParams = {
    roleName: undefined,
    roleKey: undefined,
    status: undefined,
    params: undefined,
  };
  state.dateRange = [];
  state.currentId = 0;
};

const fetchRoleData = async () => {
  setLoading(true);
  const {rows, total} = await listRole({
    ...state.pagination,
    ...addDateRange(state.queryParams, state.dateRange)
  })
  state.roleList = rows;
  state.pagination.total = total;
  setLoading(false);
}

fetchRoleData();

const onPageChange = (current: number) => {
  state.pagination.current = current;
  fetchRoleData();
};

// 新增
const handleAdd = () => {
  state.currentId = 0
  state.editVisible = true;
};
// 修改
const handleUpdate = (record: RoleRecord) => {
  state.currentId = record.roleId
  state.editVisible = true;
};
// 启用禁用
const handleStatusChange = (row: RoleRecord) => {
  const text = row.status === '0' ? '停用' : '启用';
  const value = row.status === '0' ? '1' : '0';
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `确认要"${text}""${row.roleName}"角色吗？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      changeRoleStatus(row.roleId, value).then(() => {
        Message.success(`${text}成功`);
        fetchRoleData();
      });
    },
  })
}
// 删除
const handleDelete = (row: RoleRecord) => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `是否确认删除角色编号为"${row.roleId}"的数据项？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      delRole(row.roleId).then(() => {
        Message.success("删除成功");
        fetchRoleData();
      });
    },
  })
};

// 分配数据权限操作
const handleDataScope = (record: RoleRecord) => {
  state.currentId = record.roleId
  state.editDataScopeVisible = true;
}

// 分配用户操作
const handleAuthUser = (record: RoleRecord) => {
  const {roleId} = record;
  router.push({name: 'AuthUser', params: {roleId},});
}

</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}
</style>
