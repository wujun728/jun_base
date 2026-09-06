<template>
  <div>
    <a-row :gutter="16">
      <a-col span="6">
        <!-- 部门树（改为全量加载以支持搜索） -->
        <a-card>
          <dept-tree
            ref="deptTreeRef"
            :dept-options="deptOptions"
            :lazy-load="false"
            @select="clickDeptNode"
          />
        </a-card>
      </a-col>
      <a-col span="18">
        <ProTable
            ref="proTableRef"
            :api="tableApi"
            :columns="columns"
            :exportConfig="exportConfig"
            :initialSearchParams="initialSearchParams"
            :searchFields="searchFields"
            rowKey="userId"
        >
          <!-- 1. 自定义操作按钮 -->
          <template #actions="{ selectedRowKeys, delete: deleteRows }">
            <a-button v-hasPermi="['system:user:add']" type="primary" @click="openAddModal">
              <BearJiaIcon icon="plus-outlined"/>新增
            </a-button>
            <a-button v-hasPermi="['system:user:remove']" :disabled="selectedRowKeys.length <= 0" danger type="primary"
                      @click="() => deleteRows()">
              <BearJiaIcon icon="delete-outlined"/>删除
            </a-button>
            <a-button v-hasPermi="['system:user:export']" @click="() => proTableRef.export()">
              <BearJiaIcon icon="export-outlined"/>导出
            </a-button>
          </template>

          <!-- 2. 自定义列渲染 -->
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'sex'">
              <!-- 使用字典标签组件 -->
              <dict-tag :options="sys_user_sex" :value="record.sex"/>
            </template>
            <template v-else-if="column.key === 'status'">
              <span>
                <a-badge status="success"/>
                <dict-tag :options="sys_normal_disable" :value="record.status"/>
              </span>
            </template>
            <template v-else-if="column.key === 'userTableOperateCol'">
              <TableActionBar
                  :record="record"
                  :actions="[
                    {
                      label: '查看',
                      icon: 'eye-outlined',
                      type: 'view',
                      permissions: ['system:user:query'],
                      onClick: () => openDetailModal(record)
                    },
                    {
                      label: '修改',
                      icon: 'edit-outlined',
                      type: 'edit',
                      permissions: ['system:user:edit'],
                      onClick: () => openUpdateModal(record)
                    },
                    {
                      label: '重置密码',
                      icon: 'key-outlined',
                      type: 'custom',
                      permissions: ['system:user:resetPwd'],
                      onClick: () => openResetPasswordModal(record)
                    }
                  ]"
                  :auto-adaptive="true"
              />
            </template>
          </template>
        </ProTable>
      </a-col>
    </a-row>

    <!-- Modals -->
    <AddUpdateModal
        ref="addUpdateModalRef"
        :sys-normal-disable-dict="sys_normal_disable"
        :sys-user-sex-dict="sys_user_sex"
        @refresh-father-page-table="() => proTableRef.refresh()"
    />
    <DetailModal
        ref="detailModalRef"
        :sys-normal-disable-dict="sys_normal_disable"
        :sys-user-sex-dict="sys_user_sex"
    />
    <UseResetPassword ref="useResetPasswordRef"/>
  </div>
</template>

<script setup>
import {computed, getCurrentInstance, reactive, ref} from 'vue';
import {delUser, listUser} from '@/api/system/user';
import {treeselect} from '@/api/system/dept';

// 引入组件
import ProTable from '@/components/BearJiaProTable/index.vue';
import AddUpdateModal from './addUpdateModal.vue';
import DetailModal from './detailModal.vue';
import UseResetPassword from './useResetPassword.vue';
import DeptTree from './DeptTree.vue';
import TableActionBar from '@/components/TableActionBar/index.vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';

const {proxy} = getCurrentInstance();
const proTableRef = ref();
const deptOptions = ref([]);

// 使用 useDict 获取字典
const {sys_user_sex, sys_normal_disable} = proxy.useDict("sys_user_sex", "sys_normal_disable");

// 获取部门数据
function getDeptTree() {
  treeselect().then(response => {
    deptOptions.value = response.data;
  });
}

getDeptTree();

// --- ProTable 配置 ---
const tableApi = {list: listUser, delete: delUser};
const initialSearchParams = reactive({
  userName: null,
  nickName: null,
  sex: null,
  status: null,
  deptId: null, // 部门ID，由部门树选择控制
});
const exportConfig = {url: 'system/user/export', fileName: '用户数据'};

const searchFields = computed(() => [
  {name: 'userName', label: '用户账号', type: 'input'},
  {name: 'nickName', label: '用户名称', type: 'input'},
  {name: 'sex', label: '用户性别', type: 'select', options: sys_user_sex.value},
  {name: 'status', label: '帐号状态', type: 'select', options: sys_normal_disable.value},
]);

const columns = [
  {title: '用户账号', dataIndex: 'userName', key: 'userName', width: 120},
  {title: '用户名称', dataIndex: 'nickName', key: 'nickName', width: 120},
  {title: '手机号码', dataIndex: 'phonenumber', key: 'phonenumber', width: 140},
  {title: '性别', dataIndex: 'sex', key: 'sex', width: 80},
  {title: '帐号状态', dataIndex: 'status', key: 'status', width: 100},
  {title: '操作', key: 'userTableOperateCol', width: 180, fixed: 'right'},
];

// 点击部门节点 - 更新ProTable的查询参数
const clickDeptNode = (keys) => {
  if (keys.length > 0) {
    // 直接更新 ProTable 的 searchFormData
    proTableRef.value.searchFormData.deptId = keys[0];
    // 触发ProTable刷新
    proTableRef.value?.refresh();
  } else {
    proTableRef.value.searchFormData.deptId = null;
    proTableRef.value?.refresh();
  }
}

// --- 页面特有逻辑 ---
const addUpdateModalRef = ref();
const detailModalRef = ref();
const useResetPasswordRef = ref();
const deptTreeRef = ref();

const openAddModal = () => addUpdateModalRef.value.openAddModal();
const openUpdateModal = (record) => addUpdateModalRef.value.openUpdateModal(record);
const openDetailModal = (record) => detailModalRef.value.openModal(record);
const openResetPasswordModal = (record) => useResetPasswordRef.value.openModal(record);
</script>

<style lang="less" scoped>
.ant-row {
  height: 100%;

  .ant-col {
    &:first-child {
      .ant-card {
        height: calc(100vh - 124px);

        :deep(.ant-card-body) {
          height: calc(100% - 57px);
          padding: 16px;
          overflow: hidden;
          display: flex;
          flex-direction: column;

          .dept-tree-header {
            flex-shrink: 0;
          }

          > div:last-child {
            flex: 1;
            display: flex;
            flex-direction: column;
            overflow: hidden;

            .ant-input-search {
              margin-bottom: 12px;
              flex-shrink: 0;
            }

            .ant-tree {
              flex: 1;
              overflow-y: auto;

              &::-webkit-scrollbar {
                width: 8px;
                height: 8px;
              }

              &::-webkit-scrollbar-track {
                border-radius: 999px;
                background: transparent;
              }

              &::-webkit-scrollbar-thumb {
                border: 2px solid transparent;
                border-radius: 999px;
                background: rgba(148, 163, 184, 0.45);
                background-clip: content-box;

                &:hover {
                  background: rgba(100, 116, 139, 0.58);
                  background-clip: content-box;
                }
              }
            }
          }
        }
      }
    }
  }
}
</style>
