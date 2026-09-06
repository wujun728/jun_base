<template>
  <div>
    <ProTable
        ref="proTableRef"
        :api="tableApi"
        :columns="columns"
        :searchFields="searchFields"
        :initialSearchParams="initialSearchParams"
        :showSelection="true"
        rowKey="userId"
    >
      <!-- 1. 自定义操作按钮 -->
      <template #actions="{ selectedRowKeys, selectedRows }">
        <a-button v-hasPermi="['system:role:add']" type="primary" @click="openSelectUserModal">
          <BearJiaIcon icon="plus-outlined"/>添加用户
        </a-button>
        <a-button
            v-hasPermi="['system:role:remove']"
            :disabled="selectedRowKeys.length === 0"
            :loading="authing"
            danger
            type="primary"
            @click="cancelAuthUserAll(selectedRowKeys)"
        >
          <BearJiaIcon icon="delete-outlined"/>取消批量授权
        </a-button>
        <a-button type="primary" @click="back">
          <BearJiaIcon icon="arrow-left-outlined"/>返回
        </a-button>
      </template>

      <!-- 2. 自定义列渲染 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <dict-tag :options="sys_normal_disable" :value="record.status"/>
        </template>
        <template v-else-if="column.key === 'createTime'">
          {{ parseTime(record.createTime) }}
        </template>
        <template v-else-if="column.key === 'operateCol'">
          <TableActionBar
              :hasDelete="false"
              :hasEdit="false"
              :hasView="false"
              :record="record"
          >
            <!-- 使用 actions 插槽添加自定义按钮 -->
            <template #actions="{ record }">
              <a
                  class="action-btn custom-btn"
                  style="margin-left: 8px;"
                  @click="cancelAuthUser(record)"
              >
                <BearJiaIcon icon="user-delete-outlined"/>
                取消授权
              </a>
            </template>
          </TableActionBar>
        </template>
      </template>
    </ProTable>

    <!-- 选择用户模态框 -->
    <SelectUser
        ref="selectUserRef"
        :role-id="roleId"
        :status-options="sys_normal_disable"
        @ok="handleUserAdded"
    />
  </div>
</template>

<script setup>
import {onMounted, reactive, ref, computed, nextTick, getCurrentInstance} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {allocatedUserList, authUserCancel, authUserCancelAll} from '@/api/system/role';
import SelectUser from './SelectUser.vue';
import {message, Modal} from 'ant-design-vue';
import DictTag from '@/components/DictTag/index.vue';
import ProTable from '@/components/BearJiaProTable/index.vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';
import TableActionBar from '@/components/TableActionBar/index.vue';

const {proxy} = getCurrentInstance();
const {sys_normal_disable} = proxy.useDict("sys_normal_disable");

// 路由
const route = useRoute();
const router = useRouter();

// 组件引用
const selectUserRef = ref();
const proTableRef = ref();

// 角色ID
const roleId = ref('');

// 加载状态
const authing = ref(false);

// --- ProTable 配置 ---
const tableApi = {
  list: async (params) => {
    // 如果 roleId 不存在,返回空数据
    if (!roleId.value) {
      return {
        rows: [],
        total: 0
      };
    }
    const response = await allocatedUserList({
      ...params,
      roleId: roleId.value
    });
    return response;
  }
};

const initialSearchParams = reactive({
  userName: '',
  phonenumber: ''
});

const searchFields = computed(() => [
  { name: 'userName', label: '用户名称', type: 'input', placeholder: '请输入用户名称' },
  { name: 'phonenumber', label: '手机号码', type: 'input', placeholder: '请输入手机号码' }
]);

const columns = [
  { title: '用户名称', dataIndex: 'userName', key: 'userName', align: 'center', width: 120 },
  { title: '用户昵称', dataIndex: 'nickName', key: 'nickName', ellipsis: true, align: 'center', width: 120 },
  { title: '邮箱', dataIndex: 'email', key: 'email', ellipsis: true, align: 'center', width: 180 },
  { title: '手机', dataIndex: 'phonenumber', key: 'phonenumber', align: 'center', width: 130 },
  { title: '状态', dataIndex: 'status', key: 'status', align: 'center', width: 80 },
  { title: '创建时间', dataIndex: 'createTime', key: 'createTime', align: 'center', width: 180 },
  { title: '操作', key: 'operateCol', width: 150, align: 'center' }
];

// 时间格式化函数（从 tableMixin 中提取）
const parseTime = (time) => {
  return time ? new Date(time).toLocaleString() : '';
};

// 打开选择用户模态框
const openSelectUserModal = () => {
  selectUserRef.value.handleAuthUser();
};

// 用户添加成功后的回调
const handleUserAdded = () => {
  proTableRef.value.refresh();
};

// 取消授权
const cancelAuthUser = (record) => {
  Modal.confirm({
    title: '确认要取消该用户的角色吗?',
    content: `当前选中用户 ${record.userName}`,
    okText: '确认',
    cancelText: '取消',
    onOk() {
      const param = {
        userId: record.userId,
        roleId: roleId.value,
      };
      return authUserCancel(param).then(() => {
        proTableRef.value.refresh();
        message.success('取消授权成功');
      });
    },
  });
};

// 批量取消授权
const cancelAuthUserAll = (selectedRowKeys) => {
  if (selectedRowKeys.length === 0) {
    message.warning('请选择要取消授权的用户');
    return;
  }

  Modal.confirm({
    title: '是否取消选中用户授权数据项?',
    okText: '确认',
    cancelText: '取消',
    onOk() {
      const param = {
        roleId: roleId.value,
        userIds: selectedRowKeys,
      };
      authing.value = true;
      return authUserCancelAll(param)
          .then(() => {
            proTableRef.value.refresh();
            message.success('取消授权成功');
          })
          .finally(() => {
            authing.value = false;
          });
    },
  });
};

// 返回
const back = () => {
  router.push({path: '/system/role'});
};

// 初始化
onMounted(() => {
  const routeRoleId = route.query?.roleId;
  if (routeRoleId) {
    roleId.value = routeRoleId;
    // 主动刷新表格数据
    nextTick(() => {
      proTableRef.value?.refresh();
    });
  }
});
</script>

<style scoped>
/* 如果需要样式，可以在这里定义 */
</style>
