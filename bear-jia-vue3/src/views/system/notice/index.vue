<template>
  <div>
    <ProTable
        ref="proTableRef"
        :api="tableApi"
        :columns="columns"
        :exportConfig="exportConfig"
        :initialSearchParams="initialSearchParams"
        :searchFields="searchFields"
        rowKey="noticeId"
    >
      <!-- 1. 自定义操作按钮 -->
      <template #actions="{ selectedRowKeys, delete: deleteRows }">
        <a-button v-hasPermi="['system:notice:add']" type="primary" @click="openAddModal">
          <BearJiaIcon icon="plus-outlined"/>新增
        </a-button>
        <a-button v-hasPermi="['system:notice:remove']" :disabled="selectedRowKeys.length <= 0" danger type="primary"
                  @click="() => deleteRows()">
          <BearJiaIcon icon="delete-outlined"/>删除
        </a-button>
        <a-button v-hasPermi="['system:notice:export']" @click="() => proTableRef.export()">
          <BearJiaIcon icon="export-outlined"/>导出
        </a-button>
      </template>

      <!-- 2. 自定义列渲染 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'noticeType'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sys_notice_type" :value="record.noticeType"/>
        </template>
        <template v-else-if="column.key === 'status'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sys_notice_status" :value="record.status"/>
        </template>
        <template v-else-if="column.key === 'noticeTableOperateCol'">
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
    <AddUpdateModal ref="addUpdateModalRef"
                    :sysNoticeTypeDict="sys_notice_type"
                    :sysNoticeStatusDict="sys_notice_status"
                    @refreshFatherPageTable="() => proTableRef.refresh()"/>
    <DetailModal ref="detailModalRef"
                 :sysNoticeTypeDict="sys_notice_type"
                 :sysNoticeStatusDict="sys_notice_status"/>
  </div>
</template>

<script setup name="Notice">
import {computed, getCurrentInstance, ref} from 'vue';
import {listNotice, delNotice} from "@/api/system/notice";

// 引入组件
import ProTable from '@/components/BearJiaProTable/index.vue';
import AddUpdateModal from './addUpdateModal.vue';
import DetailModal from './detailModal.vue';
import TableActionBar from '@/components/TableActionBar/index.vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';

const {proxy} = getCurrentInstance();
const proTableRef = ref();

// 使用 useDict 获取字典
const {sys_notice_type, sys_notice_status} = proxy.useDict("sys_notice_type", "sys_notice_status");

// --- ProTable 配置 ---
const tableApi = {list: listNotice, delete: delNotice};
const initialSearchParams = {noticeTitle: null, noticeType: null, status: null};
const exportConfig = {url: 'system/notice/export', fileName: '通知公告'};

const searchFields = computed(() => [
  {name: 'noticeTitle', label: '公告标题', type: 'input'},
  {name: 'noticeType', label: '公告类型', type: 'select', options: sys_notice_type.value},
  {name: 'status', label: '公告状态', type: 'select', options: sys_notice_status.value},
]);

const columns = [
  {title: '公告标题', dataIndex: 'noticeTitle', key: 'noticeTitle', width: 200},
  {title: '公告类型', dataIndex: 'noticeType', key: 'noticeType', width: 100},
  {title: '公告状态', dataIndex: 'status', key: 'status', width: 100},
  {title: '创建者', dataIndex: 'createBy', key: 'createBy', width: 120},
  {title: '创建时间', dataIndex: 'createTime', key: 'createTime', width: 180},
  {title: '操作', key: 'noticeTableOperateCol', width: 200, fixed: 'right'},
];

// --- 页面特有逻辑 ---
const addUpdateModalRef = ref();
const detailModalRef = ref();

const openAddModal = () => addUpdateModalRef.value.openAddModal();
const openUpdateModal = (record) => addUpdateModalRef.value.openUpdateModal(record);
const openDetailModal = (record) => detailModalRef.value.openModal(record);

</script>
<style lang="less"></style>
