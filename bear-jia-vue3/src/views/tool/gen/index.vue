<template>
  <div>
    <ProTable
        ref="proTableRef"
        :api="tableApi"
        :columns="columns"
        :initialSearchParams="initialSearchParams"
        :searchFields="searchFields"
        rowKey="tableId"
    >
      <!-- 1. 自定义操作按钮 -->
      <template #actions="{ selectedRowKeys, delete: deleteRows }">
        <a-button v-hasPermi="['tool:gen:import']" type="primary" @click="openImportTablesModal">
          <BearJiaIcon icon="plus-outlined"/>导入表
        </a-button>
        <a-button v-hasPermi="['tool:gen:create']" type="primary" @click="openCreateTableModal">
          <BearJiaIcon icon="table-outlined"/>创建表
        </a-button>
        <a-button v-hasPermi="['tool:gen:batch']" type="primary" :disabled="!selectedRowKeys || selectedRowKeys.length <= 0" @click="batchGenerateCode">
          <BearJiaIcon icon="download-outlined"/>批量生成
        </a-button>
        <a-button v-hasPermi="['tool:gen:remove']" :disabled="!selectedRowKeys || selectedRowKeys.length <= 0" danger type="primary"
                  @click="() => deleteRows()">
          <BearJiaIcon icon="delete-outlined"/>删除
        </a-button>
      </template>

      <!-- 2. 自定义列渲染 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'operateCol'">
          <TableActionBar
            :hasDelete="false"
            :hasEdit="true"
            :hasView="true"
            :record="record"
            @edit="openUpdateModal"
            @view="openPreviewModal"
          >
            <template #actions="{ record }">
              <a
                  class="action-btn warning-btn"
                  v-hasPermi="['tool:gen:code']"
                  @click="generateCode(record)"
              >
                <BearJiaIcon icon="download-outlined"/>
                生成代码
              </a>
              <a
                 class="action-btn custom-btn"
                 v-hasPermi="['tool:gen:sync']"
                 @click="syncDatabase(record)">
                <BearJiaIcon icon="sync-outlined"/>
                同步
              </a>
            </template>
          </TableActionBar>
        </template>
      </template>
    </ProTable>

    <!-- Modals -->
    <ImportTables ref="importTablesRef" @refreshFatherPageTable="() => proTableRef.refresh()"/>
    <GenCodeConfigUpdate ref="genCodeConfigUpdateRef" @refreshFatherPageTable="() => proTableRef.refresh()"/>
    <GenCodePreview ref="genCodePreviewRef"/>

    <!-- 创建表模态框 -->
    <a-modal
      v-model:open="createTableModal.visible"
      title="创建表"
      width="900px"
      @ok="handleCreateTable"
      @cancel="handleCreateTableCancel"
      :bodyStyle="{ padding: '20px' }"
    >
      <div style="margin-bottom: 16px;">
        <a-space>
          <a-button type="primary" @click="useExampleSql">
            <template #icon><FileTextOutlined /></template>
            使用示例
          </a-button>
          <a-button @click="clearSql">
            <template #icon><ClearOutlined /></template>
            清空
          </a-button>
          <a-button @click="showHelp = !showHelp">
            <template #icon><QuestionCircleOutlined /></template>
            {{ showHelp ? '隐藏帮助' : '显示帮助' }}
          </a-button>
        </a-space>
      </div>

      <a-form :model="createTableForm">
        <a-form-item name="sql" :rules="[{ required: true, message: '请输入创建表语句' }]">
          <a-textarea
            v-model:value="createTableForm.sql"
            placeholder="请输入CREATE TABLE语句..."
            :rows="16"
            style="font-family: 'Courier New', monospace; font-size: 13px; line-height: 1.4;"
          />
        </a-form-item>
      </a-form>

      <!-- 帮助信息折叠面板 -->
      <a-collapse v-if="showHelp" style="margin-top: 16px;">
        <a-collapse-panel key="example" header="📝 SQL语句示例">
          <pre style="font-size: 12px; line-height: 1.4; margin: 0; background: #f8f9fa; padding: 12px; border-radius: 4px; overflow-x: auto;">{{ exampleSql }}</pre>
        </a-collapse-panel>

        <a-collapse-panel key="datatypes" header="✅ 支持的数据类型">
          <div style="font-size: 13px; line-height: 1.6;">
            <p><strong>数值类型：</strong>TINYINT, SMALLINT, MEDIUMINT, INT, BIGINT, DECIMAL, FLOAT, DOUBLE</p>
            <p><strong>字符串类型：</strong>CHAR, VARCHAR, TEXT, LONGTEXT</p>
            <p><strong>日期时间：</strong>DATE, TIME, DATETIME, TIMESTAMP</p>
            <p><strong>JSON类型：</strong>JSON</p>
            <p><strong>二进制：</strong>BLOB, LONGBLOB</p>
          </div>
        </a-collapse-panel>

        <a-collapse-panel key="notes" header="⚠️ 注意事项">
          <div style="font-size: 13px; line-height: 1.6;">
            <ul style="margin: 0; padding-left: 20px;">
              <li>只支持CREATE TABLE语句</li>
              <li>表名和字段名建议使用下划线命名</li>
              <li>必须包含主键字段</li>
              <li>建议添加字段注释COMMENT</li>
              <li>支持AUTO_INCREMENT自增</li>
              <li>支持DEFAULT默认值</li>
            </ul>
          </div>
        </a-collapse-panel>
      </a-collapse>
    </a-modal>
  </div>
</template>

<script setup>
import {computed, ref, reactive} from 'vue';
import { FileTextOutlined, ClearOutlined, QuestionCircleOutlined } from '@ant-design/icons-vue';
import {listTable, delTable, genCode, batchGenCode, synchDb, createTable} from '@/api/tool/gen';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 引入组件
import ProTable from '@/components/BearJiaProTable/index.vue';
import GenCodeConfigUpdate from './genCodeConfigUpdate.vue';
import GenCodePreview from './genCodePreview.vue';
import ImportTables from './importTables.vue';
import TableActionBar from '@/components/TableActionBar/index.vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';

const proTableRef = ref();

// 创建表模态框
const createTableModal = reactive({
  visible: false
});

// 创建表表单
const createTableForm = reactive({
  sql: ''
});

// 帮助信息显示状态
const showHelp = ref(false);

// 示例SQL语句
const exampleSql = `CREATE TABLE example_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '编码',
    type VARCHAR(20) DEFAULT 'normal' COMMENT '类型',
    description TEXT COMMENT '描述',
    sort_order INT DEFAULT 0 COMMENT '排序',
    enabled TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    config_data JSON COMMENT '配置数据',
    created_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='示例表'`;

// --- ProTable 配置 ---
const tableApi = {list: listTable, delete: delTable};
const initialSearchParams = {tableName: null, tableComment: null, createTimeRange: null};

const searchFields = computed(() => [
  {name: 'tableName', label: '表名称', type: 'input'},
  {name: 'tableComment', label: '表描述', type: 'input'},
  {name: 'createTimeRange', label: '创建时间', type: 'daterange'},
]);

const columns = [
  {title: '表名称', dataIndex: 'tableName', key: 'tableName', width: 180},
  {title: '表描述', dataIndex: 'tableComment', key: 'tableComment', width: 200},
  {title: '实体', dataIndex: 'className', key: 'className', width: 180},
  {title: '创建时间', dataIndex: 'createTime', key: 'createTime', width: 180},
  {title: '更新时间', dataIndex: 'updateTime', key: 'updateTime', width: 180},
  {title: '操作', key: 'operateCol', width: 360, fixed: 'right'},
];

// --- 页面特有逻辑 ---
const importTablesRef = ref();
const genCodeConfigUpdateRef = ref();
const genCodePreviewRef = ref();

const openImportTablesModal = () => importTablesRef.value.openModal();
const openUpdateModal = (record) => genCodeConfigUpdateRef.value.openUpdateModal(record);
const openPreviewModal = (record) => genCodePreviewRef.value.openPreviewModal(record);

// 打开创建表模态框
const openCreateTableModal = () => {
  createTableModal.visible = true;
  createTableForm.sql = '';
};

// 获取选中的行数据
const getSelectedRows = () => {
  if (!proTableRef.value?.tableState) {
    console.warn('ProTable tableState 不可用');
    return [];
  }

  // 从 ProTable 获取选中的行键和数据源
  const selectedRowKeys = proTableRef.value.tableState.selectedRowKeys || [];
  const dataSource = proTableRef.value.tableState.dataSource || [];

  console.log('选中的行键:', selectedRowKeys);
  console.log('数据源:', dataSource);

  // 根据选中的键找到对应的行数据
  const selectedRows = dataSource.filter(row => selectedRowKeys.includes(row.tableId));
  console.log('选中的行数据:', selectedRows);

  return selectedRows;
};

// 生成代码
const generateCode = (record) => {
  const tableNames = record.tableName;

  if (record.genType === '1') {
    genCode(record.tableName).then((response) => {
      BearJiaUtil.messageSuccess('成功生成到自定义路径：' + record.genPath);
    }).catch((error) => {
      console.error('生成代码失败:', error);
      BearJiaUtil.messageError('生成代码失败，请检查表配置');
    });
  } else {
    try {
      // 添加时间戳和随机数避免文件名重复
      const timestamp = Date.now();
      const random = Math.floor(Math.random() * 1000);
      const fileName = `${tableNames}_代码文件_${timestamp}_${random}.zip`;

      BearJiaUtil.messageInfo('正在生成代码，请稍候...');
      BearJiaUtil.zip('/tool/gen/batchGenCode?tables=' + tableNames, fileName);
    } catch (error) {
      console.error('生成代码失败:', error);
      BearJiaUtil.messageError('生成代码失败，请稍后重试');
    }
  }
};

// 批量生成代码
const batchGenerateCode = () => {
  const selectedRows = getSelectedRows();

  if (!selectedRows || selectedRows.length === 0) {
    BearJiaUtil.messageWarning('请选择要生成代码的表');
    return;
  }

  try {
    // 获取选中表的表名，去重处理
    const uniqueTableNames = [...new Set(selectedRows.map(row => row.tableName))];
    const tableNames = uniqueTableNames.join(',');

    if (!tableNames) {
      BearJiaUtil.messageWarning('未找到有效的表名');
      return;
    }

    // 添加时间戳和随机数避免文件名重复
    const timestamp = Date.now();
    const random = Math.floor(Math.random() * 1000);
    const fileName = `批量生成代码_${uniqueTableNames.length}个表_${timestamp}_${random}.zip`;

    BearJiaUtil.messageInfo(`正在批量生成 ${uniqueTableNames.length} 个表的代码，请稍候...`);
    BearJiaUtil.zip('/tool/gen/batchGenCode?tables=' + tableNames, fileName);
  } catch (error) {
    console.error('批量生成代码失败:', error);
    BearJiaUtil.messageError('批量生成代码失败，请稍后重试');
  }
};

// 同步数据库
const syncDatabase = (record) => {
  synchDb(record.tableName).then((response) => {
    BearJiaUtil.messageSuccess('同步数据库成功');
    proTableRef.value.refresh();
  }).catch((error) => {
    console.error('同步数据库失败:', error);
    BearJiaUtil.messageError('同步数据库失败，请检查数据库连接');
  });
};

// 创建表
const handleCreateTable = async () => {
  if (!createTableForm.sql.trim()) {
    BearJiaUtil.messageWarning('请输入创建表语句');
    return;
  }

  try {
    await createTable(createTableForm.sql);

    BearJiaUtil.messageSuccess('创建表成功');
    createTableModal.visible = false;
    createTableForm.sql = '';
    proTableRef.value.refresh();
  } catch (error) {
    console.error('创建表失败:', error);
    BearJiaUtil.messageError('创建表失败，请检查SQL语句是否正确');
  }
};

// 取消创建表
const handleCreateTableCancel = () => {
  createTableModal.visible = false;
  createTableForm.sql = '';
  showHelp.value = false;
};

// 使用示例SQL
const useExampleSql = () => {
  createTableForm.sql = exampleSql;
};

// 清空SQL
const clearSql = () => {
  createTableForm.sql = '';
};
</script>

<style lang="less"></style>
