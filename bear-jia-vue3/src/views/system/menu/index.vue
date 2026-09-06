<template>
  <div>
    <ProTable
        ref="proTableRef"
        :api="tableApi"
        :columns="columns"
        :initialSearchParams="initialSearchParams"
        :searchFields="searchFields"
        :isTreeTable="true"
        :rowSelection="{ type: 'checkbox' }"
        rowKey="menuId"
    >
      <!-- 1. 自定义操作按钮 -->
      <template #actions="{ selectedRowKeys, delete: deleteRows }">
        <a-button v-hasPermi="['system:menu:add']" type="primary" @click="openMenuAddModal">
          <BearJiaIcon icon="plus-outlined"/>新增
        </a-button>
        <a-button v-hasPermi="['system:menu:remove']" :disabled="selectedRowKeys.length <= 0" danger type="primary"
                  @click="() => deleteRows()">
          <BearJiaIcon icon="delete-outlined"/>删除
        </a-button>
      </template>

      <!-- 2. 自定义列渲染 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'menuName'">
          <!-- 菜单名称带图标显示 -->
          <span>
            <BearJiaIcon :icon="record.icon" style="padding-right: 10px" />{{ record.menuName }}
          </span>
        </template>
        <template v-else-if="column.key === 'visible'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sys_show_hide" :value="record.visible"/>
        </template>
        <template v-else-if="column.key === 'menuType'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="menuTypeDict" :value="record.menuType"/>
        </template>
        <template v-else-if="column.key === 'status'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sys_normal_disable" :value="record.status"/>
        </template>
        <template v-else-if="column.key === 'menuTableOperateCol'">
          <TableActionBar
              :hasDelete="false"
              :hasEdit="true"
              :hasView="true"
              :record="record"
              @edit="openMenuUpdateModal"
              @view="openMenuDetailModal"
          />
        </template>
      </template>
    </ProTable>

    <!-- Modals -->
    <MenuAddUpdateModal ref="menuAddUpdateModalRef"
                        :sysYesNoDict="sysYesNoDict"
                        :sysShowHideDict="sys_show_hide"
                        :menuTypeDict="menuTypeDict"
                        :sysNormalDisableDict="sys_normal_disable"
                        @refreshFatherPageTable="() => proTableRef.refresh()"/>
    <MenuDetailModal ref="menuDetailModalRef"
                     :sysYesNoDict="sysYesNoDict"
                     :sysShowHideDict="sys_show_hide"
                     :menuTypeDict="menuTypeDict"
                     :sysNormalDisableDict="sys_normal_disable"
                     @refreshFatherPageTable="() => proTableRef.refresh()"/>
  </div>
</template>

<script setup name="Menu">
import {computed, getCurrentInstance, reactive, ref} from 'vue';
import {listMenu, delMenu} from '@/api/system/menu';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 引入组件
import ProTable from '@/components/BearJiaProTable/index.vue';
import MenuAddUpdateModal from './addUpdateModal.vue';
import MenuDetailModal from './detailModal.vue';
import TableActionBar from '@/components/TableActionBar/index.vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';

const {proxy} = getCurrentInstance();
const proTableRef = ref();

// 使用 useDict 获取字典
const {sys_show_hide, sys_normal_disable} = proxy.useDict("sys_show_hide", "sys_normal_disable");

// 自定义字典数据
const sysYesNoDict = reactive([
  {value: '0', label: '是'},
  {value: '1', label: '否'},
]);

const menuTypeDict = reactive([
  {value: 'M', label: '目录'},
  {value: 'C', label: '菜单'},
  {value: 'F', label: '按钮'},
]);

// --- ProTable 配置 ---
const tableApi = {
  list: listMenu,
  delete: delMenu,
  // 特殊处理：菜单是树形结构，需要将扁平数据转换为树形结构
  processListData: (data) => {
    // 使用BearJiaUtil的handleTree方法将扁平数据转换为树形结构
    // 参数：数据源, id字段, parentId字段, children字段
    return BearJiaUtil.handleTree(data, 'menuId', 'parentId', 'children');
  }
};
const initialSearchParams = {menuName: null};
// 菜单管理没有导出功能
const exportConfig = null;

const searchFields = computed(() => [
  {name: 'menuName', label: '菜单名称', type: 'input'},
]);

const columns = [
  {title: '菜单名称', dataIndex: 'menuName', key: 'menuName', width: 200},
  {title: '路由地址', dataIndex: 'path', key: 'path', width: 150},
  {title: '权限标识', dataIndex: 'perms', key: 'perms', width: 180},
  {title: '菜单类型', dataIndex: 'menuType', key: 'menuType', width: 100},
  {title: '是否显示', dataIndex: 'visible', key: 'visible', width: 100},
  {title: '菜单状态', dataIndex: 'status', key: 'status', width: 100},
  {title: '操作', key: 'menuTableOperateCol', width: 200, fixed: 'right'},
];

// --- 页面特有逻辑 ---
const menuAddUpdateModalRef = ref();
const menuDetailModalRef = ref();

const openMenuAddModal = () => menuAddUpdateModalRef.value.openAddModal();
const openMenuUpdateModal = (record) => menuAddUpdateModalRef.value.openUpdateModal(record);
const openMenuDetailModal = (record) => menuDetailModalRef.value.openModal(record);


</script>
<style lang="less"></style>
