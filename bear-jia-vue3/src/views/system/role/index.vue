<template>
  <div>
    <ProTable
        ref="proTableRef"
        :api="tableApi"
        :columns="columns"
        :exportConfig="exportConfig"
        :initialSearchParams="initialSearchParams"
        :searchFields="searchFields"
        rowKey="roleId"
        persistenceId="system-role"
    >
      <!-- 1. 自定义操作按钮 -->
      <template #actions="{ selectedRowKeys, delete: deleteRows }">
        <a-button v-hasPermi="['system:role:add']" type="primary" @click="openAddModal"> <BearJiaIcon icon="plus-outlined"/>新增</a-button>
        <a-button v-hasPermi="['system:role:remove']" :disabled="selectedRowKeys.length <= 0" danger type="primary"
                  @click="() => deleteRows()"><BearJiaIcon icon="delete-outlined"/>删除
        </a-button>
        <a-button v-hasPermi="['system:role:export']" @click="() => proTableRef.export()"> <BearJiaIcon icon="export-outlined"/>导出</a-button>
      </template>

      <!-- 2. 自定义列渲染 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sys_normal_disable" :value="record.status"/>
        </template>
        <template v-else-if="column.key === 'roleTableOperateCol'">
          <TableActionBar
              :hasDelete="true"
              :hasEdit="true"
              :hasView="true"
              :record="record"
              @delete="() => proTableRef.delete([record.roleId])"
              @edit="openUpdateModal"
              @view="openDetailModal"
          >
            <!-- 使用 actions 插槽添加自定义按钮 -->
            <template #actions="{ record }">
              <a
                  class="action-btn custom-btn"
                  @click="handleAuthUser(record)"
              >
                <BearJiaIcon icon="user"/>
                分配用户
              </a>
            </template>
          </TableActionBar>

        </template>
      </template>
    </ProTable>

    <!-- Modals -->
    <AddUpdateModal
      ref="addUpdateModalRef"
      :dataScopeDict="dataScopeDict"
      :sysNormalDisableDict="sys_normal_disable"
      @refresh="() => proTableRef.refresh()"
    />
    <DetailModal
      ref="detailModalRef"
      :dataScopeDict="dataScopeDict"
      :sysNormalDisableDict="sys_normal_disable"
    />
  </div>
</template>

<script name="Role" setup>
import {computed, getCurrentInstance, ref} from 'vue';
import {useRouter} from 'vue-router';
import {delRole, listRole} from "@/api/system/role";

// 引入组件
import ProTable from '@/components/BearJiaProTable/index.vue';
import AddUpdateModal from './addUpdateModal.vue';
import DetailModal from './detailModal.vue';
import TableActionBar from '@/components/TableActionBar/index.vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';

const {proxy} = getCurrentInstance();
const router = useRouter();
const proTableRef = ref();

// 使用 useDict 获取字典
const {sys_normal_disable, sys_data_scope} = proxy.useDict("sys_normal_disable", "sys_data_scope");

// 数据权限字典 - 如果后端字典为空，使用默认配置
const dataScopeDict = computed(() => {
  if (sys_data_scope.value && sys_data_scope.value.length > 0) {
    return sys_data_scope.value;
  }
  // 默认数据权限配置
  return [
    { value: "1", label: "所有数据权限" },
    { value: "2", label: "自定义数据权限" },
    { value: "3", label: "本部门数据权限" },
    { value: "4", label: "本部门及以下数据权限" },
    { value: "5", label: "仅本人数据权限" }
  ];
});

// --- ProTable 配置 ---
const tableApi = {list: listRole, delete: delRole};
const initialSearchParams = {roleName: null, roleKey: null, status: null};
const exportConfig = {url: 'system/role/export', fileName: '角色数据'};

const searchFields = computed(() => [
  {name: 'roleName', label: '角色名称', type: 'input'},
  {name: 'roleKey', label: '角色编码', type: 'input'},
  {name: 'status', label: '状态', type: 'select', options: sys_normal_disable.value},
]);

const columns = [
  {title: '角色名称', dataIndex: 'roleName', key: 'roleName', width: 150},
  {title: '角色编码', dataIndex: 'roleKey', key: 'roleKey', width: 150},
  {title: '状态', dataIndex: 'status', key: 'status', width: 100},
  {title: '操作', key: 'roleTableOperateCol', width: 400, fixed: 'right'},
];

// --- 页面特有逻辑 ---
const addUpdateModalRef = ref();
const detailModalRef = ref();
const openAddModal = () => addUpdateModalRef.value.openAddModal();
const openUpdateModal = (record) => addUpdateModalRef.value.openUpdateModal(record);
const openDetailModal = (record) => detailModalRef.value.openModal(record);
const handleAuthUser = (row) => router.push(`/system/role/module/authUser?roleId=${row.roleId}`);
</script>
