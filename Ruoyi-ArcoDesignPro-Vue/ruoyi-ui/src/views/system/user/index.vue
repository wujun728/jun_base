<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统管理', '用户管理']"/>
    <a-row>
      <!-- 部门树 -->
      <a-col :span="4">
        <a-card
            class="general-card l-nav-card"
            :body-style="{ padding: '15px' }"
            :header-style="{ padding: '15px' }"
        >
          <dept-tree @select="handleNodeClick"></dept-tree>
        </a-card>
      </a-col>
      <!-- 用户列表 -->
      <a-col :span="20">
        <a-card
            class="general-card r-nav-card"
            :body-style="{ padding: '15px' }"
            :header-style="{ padding: '15px' }"
        >
          <!-- 搜索区块 -->
          <a-row>
            <a-col :flex="1">
              <a-form
                  :model="queryParams"
                  :label-col-props="{ span: 6 }"
                  :wrapper-col-props="{ span: 18 }"
                  label-align="left"
                  auto-label-width
              >
                <a-row :gutter="16">
                  <a-col :span="8">
                    <a-form-item field="number" :label="'用户名称'">
                      <a-input
                          v-model="queryParams.userName"
                          :placeholder="'请输入用户名称'"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :span="8">
                    <a-form-item field="phonenumber" :label="'手机号码'">
                      <a-input
                          v-model="queryParams.phonenumber"
                          :placeholder="'请输入手机号码'"
                          maxlength="11"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :span="8">
                    <a-form-item field="status" :label="'状态'">
                      <a-select v-model="queryParams.status" placeholder="请选择">
                        <a-option
                            v-for="dict in statusOptions"
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
                      <a-range-picker v-model="dateRange"/>
                    </a-form-item>
                  </a-col>
                </a-row>
              </a-form>
            </a-col>
            <a-divider style="height: 84px" direction="vertical"/>
            <a-col :flex="'86px'" style="text-align: right">
              <a-space direction="vertical" :size="18">
                <a-button type="primary" @click="search">
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
          </a-row>
          <a-divider style="margin-top: 0"/>
          <!-- 工具栏 -->
          <a-row style="margin-bottom: 16px">
            <!-- 前置工具栏部分 -->
            <a-col :span="12">
              <a-space>
                <a-button v-permission="['system:user:add']" type="primary" @click="handleAdd">
                  <template #icon>
                    <icon-plus/>
                  </template>
                  新建
                </a-button>
                <a-upload v-permission="['system:user:import']" action="/">
                  <template #upload-button>
                    <a-button> 导入</a-button>
                  </template>
                </a-upload>
              </a-space>
            </a-col>
            <!-- 后置工具栏部分 -->
            <a-col
                :span="12"
                style="display: flex; align-items: center; justify-content: end"
            >
              <a-button v-permission="['system:user:export']" @click="handleExport">
                <template #icon>
                  <icon-download/>
                </template>
                下载
              </a-button>
            </a-col>
          </a-row>
          <!-- 表格数据-->
          <a-table
              row-key="id"
              :loading="loading"
              :pagination="pagination"
              :columns="columns"
              :data="renderData"
              :bordered="false"
              :size="'medium'"
              @page-change="onPageChange"
          >
            <template #status="{ record }">
              <a-switch
                  :model-value="record.status" checked-value="0" unchecked-value="1"
                  @change="handleStatusChange(record)"
              />
            </template>
            <template #optional="{ record }">
              <a-space v-if="record.userId !== 1">
                <a-button-group>
                  <a-button
                      v-permission="['system:user:edit']" type="text" size="mini" @click="handleUpdate(record.userId)">
                    <template #icon>
                      <icon-edit/>
                    </template>
                    修改
                  </a-button>
                  <a-button v-permission="['system:user:remove']" type="text" size="mini" @click="handleDelete(record)">
                    <template #icon>
                      <icon-delete/>
                    </template>
                    删除
                  </a-button>
                    <a-dropdown trigger="hover">
                      <a-button v-permission="['system:user:resetPwd', 'system:user:edit']" type="text" size="mini">
                        <template #icon>
                          <icon-double-right/>
                        </template>
                        更多
                      </a-button>
                        <template #content>
                          <a-doption>
                            <a-button
                                v-permission="['system:user:resetPwd']"
                                type="text" size="mini"
                                @click="handleResetPwd(record)">
                              <template #icon>
                                <icon-unlock/>
                              </template>
                              重置密码
                            </a-button>
                          </a-doption>
                          <a-doption>
                            <a-button
                                v-permission="['system:user:edit']"
                                type="text" size="mini"
                                @click="handleAuthRole(record)">
                              <template #icon>
                                <icon-check-circle/>
                              </template>
                              分配角色
                            </a-button>
                          </a-doption>
                        </template>
                    </a-dropdown>
                </a-button-group>
              </a-space>
            </template>
          </a-table>
        </a-card>
      </a-col>
      <edit-user v-model:id="currentId" v-model:visible="editVisible" @refresh-data-list="search"></edit-user>
      <reset-password v-model:id="currentId" v-model:visible="pwdResetVisible"></reset-password>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import {
  UserRecord,
  listUser,
  UserQueryParams, delUser, changeUserStatus
} from "@/api/system/user";
import {DictDataRecord, getDicts} from '@/api/system/dict-data';
import useLoading from '@/hooks/loading';
import {Message, Modal} from "@arco-design/web-vue";
import {computed, reactive, ref} from 'vue';
import {TableColumnData} from '@arco-design/web-vue/es/table/interface';
import {Pagination} from '@/types/global';
import addDateRange from '@/utils/common';
import {download} from '@/api/download';
import {useRouter} from 'vue-router';
import DeptTree from './components/dept-tree.vue';
import EditUser from './components/edit-user.vue';
import ResetPassword from './components/reset-password.vue';

const basePagination: Pagination = {
  current: 1,
  pageSize: 10,
};
const pagination = reactive({
  ...basePagination,
});

const renderData = ref<UserRecord[]>([]);
const currentId = ref<number>();
const editVisible = ref<boolean>(false);
const pwdResetVisible = ref<boolean>(false);

// 初始化查询参数
const generateQueryParams = () => {
  return {
    pageNum: 1,
    pageSize: 10,
    userName: undefined,
    phonenumber: undefined,
    status: undefined,
    deptId: undefined,
    params: undefined,
  };
};

const dateRange = ref([]);
const queryParams = ref(generateQueryParams());
const resetQueryForm = () => {
  queryParams.value = generateQueryParams();
  dateRange.value = [];
};

const handleNodeClick = (id: any[]) => {
  const [deptId] = id;
  queryParams.value.deptId = deptId;
  search();
}

const columns = computed<TableColumnData[]>(() => [
  {
    title: '用户编号',
    dataIndex: 'userId',
    slotName: 'userId',
    width: 100,
  },
  {
    title: '用户名称',
    dataIndex: 'userName',
    width: 150,
  },
  {
    title: '用户昵称',
    dataIndex: 'nickName',
    width: 150,
  },
  {
    title: '部门',
    dataIndex: 'dept.deptName',
    slotName: 'deptName',
    width: 180,
  },
  {
    title: '手机号码',
    dataIndex: 'phonenumber',
    width: 150,
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
]);

// 初始化字典
const statusOptions = ref<DictDataRecord[]>([]);
const initData = async () => {
  const {data: statusData} = await getDicts('sys_normal_disable');
  statusOptions.value = statusData;
};

initData();

const {loading, setLoading} = useLoading(true);
const fetchUserData = async (params: UserQueryParams = generateQueryParams()) => {
  setLoading(true);
  try {
    const {rows, total} = await listUser((params));
    renderData.value = rows;
    pagination.current = params.pageNum;
    pagination.total = total;
  } catch (err: any) {
    // you can report use errorHandler or other
  } finally {
    setLoading(false);
  }
};
fetchUserData();

const search = () => {
  fetchUserData({
    ...basePagination,
    ...addDateRange(queryParams.value, dateRange.value),
  } as unknown as UserQueryParams);
};

const onPageChange = (current: number) => {
  queryParams.value.pageNum = current;
  fetchUserData(queryParams.value);
};

// 新增
const handleAdd = () => {
  currentId.value = undefined
  editVisible.value = true;
};
// 修改
const handleUpdate = (userId: number) => {
  currentId.value = userId
  editVisible.value = true;
};
// 启用禁用
const handleStatusChange = (row: UserRecord) => {
  const text = row.status === '0' ? '停用' : '启用';
  const value = row.status === '0' ? '1' : '0';
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `确认要"${text}""${row.nickName}"用户吗？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      changeUserStatus(row.userId, value).then(() => {
        Message.success(`${text}成功`);
        search();
      });
    },
  })
}
// 删除
const handleDelete = (row: UserRecord) => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `是否确认删除用户编号为"${row.userId}"的数据项？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      delUser(row.userId).then(() => {
        Message.success("删除成功");
        search();
      });
    },
  })
};
// 重置密码
const handleResetPwd = (row: any) => {
  currentId.value = row.userId
  pwdResetVisible.value = true;
}

const router = useRouter();
// 重置密码
const handleAuthRole = (record: any) => {
  const {userId} = record;
  router.push({name: 'AuthRole', params: {userId},});
}

const handleExport = () => {
  download('system/user/export', queryParams.value, `user_${new Date().getTime()}.xlsx`)
}

</script>

<script lang="ts">
export default {
  name: 'UserList',
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
