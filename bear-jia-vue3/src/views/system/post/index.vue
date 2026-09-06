<template>
  <div>
    <ProTable
        ref="proTableRef"
        :api="tableApi"
        :columns="columns"
        :exportConfig="exportConfig"
        :initialSearchParams="initialSearchParams"
        :searchFields="searchFields"
        rowKey="postId"
    >
      <!-- 1. 自定义操作按钮 -->
      <template #actions="{ selectedRowKeys, delete: deleteRows }">
        <a-button v-hasPermi="['system:post:add']" type="primary" @click="openAddModal">
          <BearJiaIcon icon="plus-outlined"/>新增
        </a-button>
        <a-button v-hasPermi="['system:post:remove']" :disabled="selectedRowKeys.length <= 0" danger type="primary"
                  @click="() => deleteRows()">
          <BearJiaIcon icon="delete-outlined"/>删除
        </a-button>
        <a-button v-hasPermi="['system:post:export']" @click="() => proTableRef.export()">
          <BearJiaIcon icon="export-outlined"/>导出
        </a-button>
      </template>

      <!-- 2. 自定义列渲染 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sys_normal_disable" :value="record.status"/>
        </template>
        <template v-else-if="column.key === 'postTableOperateCol'">
          <TableActionBar
              :hasDelete="false"
              :hasEdit="true"
              :hasView="true"
              :record="record"
              @edit="openUpdateModal"
              @view="openDetailModal"
          />
        </template>
      </template>
    </ProTable>

    <!-- Modals -->
    <AddUpdateModal ref="addUpdateModalRef" :sysNormalDisableDict="sys_normal_disable" @refreshFatherPageTable="() => proTableRef.refresh()"/>
    <DetailModal ref="detailModalRef" :sysNormalDisableDict="sys_normal_disable"/>
  </div>
</template>

<script name="Post" setup>
import {computed, getCurrentInstance, ref} from 'vue';
import {delPost, listPost} from "@/api/system/post";

// 引入组件
import ProTable from '@/components/BearJiaProTable/index.vue';
import AddUpdateModal from './addUpdateModal.vue';
import DetailModal from './detailModal.vue';
import TableActionBar from '@/components/TableActionBar/index.vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';

const {proxy} = getCurrentInstance();
const proTableRef = ref();

// 使用 useDict 获取字典
const {sys_normal_disable} = proxy.useDict("sys_normal_disable");

// --- ProTable 配置 ---
const tableApi = {list: listPost, delete: delPost};
const initialSearchParams = {postCode: null, postName: null, status: null};
const exportConfig = {url: 'system/post/export', fileName: '岗位信息'};

const searchFields = computed(() => [
  {name: 'postCode', label: '岗位编码', type: 'input'},
  {name: 'postName', label: '岗位名称', type: 'input'},
  {name: 'status', label: '岗位状态', type: 'select', options: sys_normal_disable.value},
]);

const columns = [
  {title: '岗位编码', dataIndex: 'postCode', key: 'postCode', width: 120},
  {title: '岗位名称', dataIndex: 'postName', key: 'postName', width: 150},
  {title: '显示顺序', dataIndex: 'postSort', key: 'postSort', width: 100},
  {title: '岗位状态', dataIndex: 'status', key: 'status', width: 100},
  {title: '操作', key: 'postTableOperateCol', width: 200, fixed: 'right'},
];

// --- 页面特有逻辑 ---
const addUpdateModalRef = ref();
const detailModalRef = ref();
const openAddModal = () => addUpdateModalRef.value.openAddModal();
const openUpdateModal = (record) => addUpdateModalRef.value.openUpdateModal(record);
const openDetailModal = (record) => detailModalRef.value.openModal(record);
</script>
<style lang="less"></style>
