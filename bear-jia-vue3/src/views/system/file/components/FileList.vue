<template>
  <div class="file-list">
    <!-- 面包屑导航 -->
    <div class="breadcrumb-container">
      <a-breadcrumb>
        <a-breadcrumb-item>
          <a @click="navigateToFolder('0')">
            <BearJiaIcon icon="home-outlined" />
            根目录
          </a>
        </a-breadcrumb-item>
        <a-breadcrumb-item v-for="item in breadcrumbPath" :key="item.id">
          <a @click="navigateToFolder(item.id)">{{ item.fileName }}</a>
        </a-breadcrumb-item>
      </a-breadcrumb>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <a-button type="primary" @click="showUploadModal">
          <BearJiaIcon icon="upload-outlined" />
          上传文件
        </a-button>
        <a-button @click="showCreateFolderModal">
          <BearJiaIcon icon="folder-add-outlined" />
          新建文件夹
        </a-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-button>
            批量操作
            <BearJiaIcon icon="down-outlined" />
          </a-button>
          <template #overlay>
            <a-menu @click="handleBatchAction">
              <a-menu-item key="move">
                <BearJiaIcon icon="folder-open-outlined" />
                移动到
              </a-menu-item>
              <a-menu-item key="delete" danger>
                <BearJiaIcon icon="delete-outlined" />
                删除
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>

      <div class="toolbar-right">
        <a-input-search
          v-model:value="searchKeyword"
          placeholder="搜索文件..."
          style="width: 200px; margin-right: 8px;"
          @search="handleSearch"
        />
        <a-dropdown
          v-model:open="filterDropdownVisible"
          :trigger="['click']"
          placement="bottomRight"
        >
          <a-button>
            <BearJiaIcon icon="filter-outlined" />
            筛选
          </a-button>
          <template #overlay>
            <div class="filter-panel" @click.stop>
              <a-form layout="vertical" style="padding: 16px; width: 300px;">
                <a-form-item label="业务类型">
                  <a-select v-model:value="filterForm.businessType" placeholder="选择业务类型" allowClear>
                    <a-select-option value="rag">RAG文档</a-select-option>
                    <a-select-option value="avatar">用户头像</a-select-option>
                    <a-select-option value="attachment">系统附件</a-select-option>
                    <a-select-option value="common">通用文件</a-select-option>
                  </a-select>
                </a-form-item>
                <a-form-item label="文件类型">
                  <a-select v-model:value="filterForm.fileType" placeholder="选择文件类型" allowClear>
                    <a-select-option value="pdf">PDF</a-select-option>
                    <a-select-option value="doc">Word</a-select-option>
                    <a-select-option value="xls">Excel</a-select-option>
                    <a-select-option value="jpg">图片</a-select-option>
                  </a-select>
                </a-form-item>
                <a-form-item label="状态">
                  <a-select v-model:value="filterForm.status" placeholder="选择状态" allowClear>
                    <a-select-option :value="1">正常</a-select-option>
                    <a-select-option :value="2">处理中</a-select-option>
                    <a-select-option :value="3">处理失败</a-select-option>
                  </a-select>
                </a-form-item>
                <a-form-item>
                  <a-space>
                    <a-button type="primary" @click="applyFilter">应用</a-button>
                    <a-button @click="resetFilter">重置</a-button>
                  </a-space>
                </a-form-item>
              </a-form>
            </div>
          </template>
        </a-dropdown>
        <a-button-group>
          <a-button
            :type="viewMode === 'list' ? 'primary' : 'default'"
            @click="setViewMode('list')"
          >
            <BearJiaIcon icon="unordered-list-outlined" />
            列表
          </a-button>
          <a-button
            :type="viewMode === 'grid' ? 'primary' : 'default'"
            @click="setViewMode('grid')"
          >
            <BearJiaIcon icon="appstore-outlined" />
            网格
          </a-button>
        </a-button-group>
      </div>
    </div>

    <!-- 文件列表 -->
    <div class="file-content">
      <!-- 列表视图 -->
      <a-table
        v-if="viewMode === 'list'"
        :columns="columns"
        :data-source="fileList"
        :loading="loading"
        :pagination="pagination"
        :row-selection="rowSelection"
        :scroll="{ x: 1200 }"
        row-key="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'fileName'">
            <div class="file-name-cell" @dblclick="handleDoubleClick(record)">
              <BearJiaIcon :icon="getFileIcon(record)" :class="getFileIconClass(record)" />
              <span class="file-name">{{ record.fileName }}</span>
            </div>
          </template>

          <template v-else-if="column.key === 'fileSize'">
            <span>{{ formatFileSize(record.fileSize) }}</span>
          </template>

          <template v-else-if="column.key === 'businessType'">
            <a-tag :color="getBusinessTypeColor(record.businessType)">
              {{ getBusinessTypeName(record.businessType) }}
            </a-tag>
          </template>

          <template v-else-if="column.key === 'status'">
            <a-badge :status="getStatusBadge(record.status)" :text="getStatusText(record.status)" />
          </template>

          <template v-else-if="column.key === 'action'">
            <a-space size="small">
              <a-tooltip title="预览">
                <a-button type="link" size="small" @click="handlePreview(record)">
                  <BearJiaIcon icon="eye-outlined" />
                </a-button>
              </a-tooltip>
              <a-tooltip title="下载">
                <a-button type="link" size="small" @click="handleDownload(record)">
                  <BearJiaIcon icon="download-outlined" />
                </a-button>
              </a-tooltip>
              <a-tooltip title="重命名">
                <a-button type="link" size="small" @click="handleRename(record)">
                  <BearJiaIcon icon="edit-outlined" />
                </a-button>
              </a-tooltip>
              <a-tooltip title="删除">
                <a-button type="link" size="small" @click="handleDelete(record)" danger>
                  <BearJiaIcon icon="delete-outlined" />
                </a-button>
              </a-tooltip>
            </a-space>
          </template>
        </template>
      </a-table>

      <!-- 网格视图 -->
      <div v-else class="grid-view">
        <div class="grid-container">
          <div
            v-for="file in fileList"
            :key="file.id"
            class="grid-item"
            :class="{ selected: selectedRowKeys.includes(file.id) }"
            @click="handleGridItemClick(file)"
            @dblclick="handleDoubleClick(file)"
            @contextmenu.prevent="handleRightClick($event, file)"
          >
            <div class="grid-item-icon">
              <BearJiaIcon
                :icon="getFileIcon(file)"
                :class="getFileIconClass(file)"
              />
            </div>
            <div class="grid-item-name">{{ file.fileName }}</div>
            <div class="grid-item-info">
              <span>{{ formatFileSize(file.fileSize) }}</span>
              <span>{{ formatDate(file.createTime) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 重命名弹窗 -->
    <a-modal
      v-model:open="renameModalVisible"
      title="重命名文件"
      @ok="confirmRename"
      @cancel="cancelRename"
    >
      <a-form :model="renameForm" layout="vertical">
        <a-form-item label="新文件名" :rules="[{ required: true, message: '请输入文件名' }]">
          <a-input
            v-model:value="renameForm.newName"
            placeholder="请输入新文件名"
            @keyup.enter="confirmRename"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 删除确认弹窗 -->
    <a-modal
      v-model:open="deleteModalVisible"
      title="确认删除"
      @ok="confirmDelete"
      @cancel="cancelDelete"
      ok-text="删除"
      cancel-text="取消"
      ok-type="danger"
    >
      <p>确定要删除文件 "{{ deleteTarget?.fileName }}" 吗？</p>
      <p style="color: #ff4d4f; font-size: 12px;">注意：删除后无法恢复</p>
    </a-modal>

    <!-- 批量删除确认弹窗 -->
    <a-modal
      v-model:open="batchDeleteModalVisible"
      title="批量删除确认"
      @ok="confirmBatchDelete"
      @cancel="cancelBatchDelete"
      ok-text="删除"
      cancel-text="取消"
      ok-type="danger"
    >
      <p>确定要删除选中的 {{ selectedRowKeys.length }} 个文件吗？</p>
      <p style="color: #ff4d4f; font-size: 12px;">注意：删除后无法恢复</p>
    </a-modal>

    <!-- 批量移动弹窗 -->
    <a-modal
      v-model:open="batchMoveModalVisible"
      title="移动文件"
      @ok="confirmBatchMove"
      @cancel="cancelBatchMove"
      ok-text="移动"
      cancel-text="取消"
      width="600px"
    >
      <div style="margin-bottom: 16px;">
        <p>将选中的 {{ selectedRowKeys.length }} 个文件移动到：</p>
      </div>

      <!-- 文件夹树形选择器 -->
      <div class="folder-tree-container">
        <a-tree
          v-model:selectedKeys="moveTargetFolder"
          :tree-data="folderTreeData"
          :field-names="{ children: 'children', title: 'fileName', key: 'id' }"
          show-icon
          default-expand-all
          @select="handleFolderSelect"
        >
          <template #icon="{ isLeaf }">
            <BearJiaIcon v-if="!isLeaf" icon="folder-outlined" style="color: #faad14;" />
          </template>
        </a-tree>
      </div>

      <div style="margin-top: 16px; padding: 12px; background: #f5f5f5; border-radius: 6px;">
        <p style="margin: 0; font-size: 14px; color: #666;">
          目标文件夹：{{ getSelectedFolderPath() }}
        </p>
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { BearJiaIcon } from '@/utils/BearJiaIcon.js';
import {
  listFile,
  delFile,
  getFilesByParentId,
  getFolderPath,
  getFolderTree,
  renameFile,
  moveFiles,
  createFolder,
  getFileDownloadUrl,
  getFilePreviewUrl
} from '@/api/system/file';

const props = defineProps({
  currentFolderId: {
    type: [String, Number],
    default: '0'
  }
});

const emit = defineEmits(['folderChange', 'preview', 'rename', 'delete', 'batchMove', 'batchDelete', 'showUpload', 'showCreateFolder']);

// 数据
const fileList = ref([]);
const loading = ref(false);
const viewMode = ref('list'); // 'list' | 'grid'
const searchKeyword = ref('');
const selectedRowKeys = ref([]);
const breadcrumbPath = ref([]);
const filterDropdownVisible = ref(false);

// 弹窗状态
const renameModalVisible = ref(false);
const deleteModalVisible = ref(false);
const batchDeleteModalVisible = ref(false);
const batchMoveModalVisible = ref(false);

// 重命名表单
const renameForm = reactive({
  newName: '',
  targetFile: null
});

// 删除目标
const deleteTarget = ref(null);

// 批量移动相关
const moveTargetFolder = ref([]);
const folderTreeData = ref([]);
const selectedFolderInfo = ref(null);

// 筛选表单
const filterForm = reactive({
  businessType: undefined,
  fileType: undefined,
  status: undefined
});

// 分页
const pagination = reactive({
  current: 1,
  pageSize: 20,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total) => `共 ${total} 条`
});

// 表格列配置
const columns = [
  {
    title: '文件名',
    dataIndex: 'fileName',
    key: 'fileName',
    width: 300,
    ellipsis: true
  },
  {
    title: '大小',
    dataIndex: 'fileSize',
    key: 'fileSize',
    width: 100
  },
  {
    title: '类型',
    dataIndex: 'fileType',
    key: 'fileType',
    width: 80
  },
  {
    title: '业务类型',
    dataIndex: 'businessType',
    key: 'businessType',
    width: 120
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 100
  },
  {
    title: '修改时间',
    dataIndex: 'updateTime',
    key: 'updateTime',
    width: 180
  },
  {
    title: '操作',
    key: 'action',
    width: 160,
    fixed: 'right'
  }
];

// 行选择配置
const rowSelection = {
  selectedRowKeys: selectedRowKeys,
  onChange: (keys) => {
    selectedRowKeys.value = keys;
  }
};

// 获取文件图标
const getFileIcon = (record) => {
  if (record.isFolder) return 'folder-outlined';

  const ext = record.fileType?.toLowerCase();
  switch (ext) {
    case 'pdf': return 'file-pdf-outlined';
    case 'doc':
    case 'docx': return 'file-word-outlined';
    case 'xls':
    case 'xlsx': return 'file-excel-outlined';
    case 'jpg':
    case 'jpeg':
    case 'png':
    case 'gif': return 'file-image-outlined';
    case 'txt': return 'file-text-outlined';
    case 'zip':
    case 'rar':
    case '7z': return 'file-zip-outlined';
    case 'mp4':
    case 'avi':
    case 'mov': return 'video-camera-outlined';
    case 'mp3':
    case 'wav':
    case 'flac': return 'sound-outlined';
    default: return 'file-outlined';
  }
};

// 获取文件图标颜色类名
const getFileIconClass = (record) => {
  if (record.isFolder) return 'icon-folder';

  const ext = record.fileType?.toLowerCase();
  switch (ext) {
    case 'pdf': return 'icon-pdf';
    case 'doc':
    case 'docx': return 'icon-word';
    case 'xls':
    case 'xlsx': return 'icon-excel';
    case 'jpg':
    case 'jpeg':
    case 'png':
    case 'gif': return 'icon-image';
    case 'txt': return 'icon-text';
    case 'zip':
    case 'rar':
    case '7z': return 'icon-archive';
    case 'mp4':
    case 'avi':
    case 'mov': return 'icon-video';
    case 'mp3':
    case 'wav':
    case 'flac': return 'icon-audio';
    default: return 'icon-default';
  }
};

// 获取业务类型颜色
const getBusinessTypeColor = (type) => {
  const colors = {
    rag: 'blue',
    avatar: 'green',
    attachment: 'orange',
    common: 'default'
  };
  return colors[type] || 'default';
};

// 获取业务类型名称
const getBusinessTypeName = (type) => {
  const names = {
    rag: 'RAG文档',
    avatar: '用户头像',
    attachment: '系统附件',
    common: '通用文件'
  };
  return names[type] || type;
};

// 获取状态徽章
const getStatusBadge = (status) => {
  const badges = {
    1: 'success',
    2: 'processing',
    3: 'error',
    0: 'default'
  };
  return badges[status] || 'default';
};

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    1: '正常',
    2: '处理中',
    3: '处理失败',
    0: '已删除'
  };
  return texts[status] || '未知';
};

// 格式化文件大小
const formatFileSize = (size) => {
  if (!size) return '-';
  const units = ['B', 'KB', 'MB', 'GB'];
  let index = 0;
  while (size >= 1024 && index < units.length - 1) {
    size /= 1024;
    index++;
  }
  return `${size.toFixed(2)} ${units[index]}`;
};

// 格式化日期
const formatDate = (date) => {
  if (!date) return '-';
  return new Date(date).toLocaleString();
};

// 设置视图模式
const setViewMode = (mode) => {
  viewMode.value = mode;
};

// 处理表格变化
const handleTableChange = (pag) => {
  pagination.current = pag.current;
  pagination.pageSize = pag.pageSize;
  loadFileList();
};

// 处理双击
const handleDoubleClick = (record) => {
  if (record.isFolder) {
    navigateToFolder(record.id);
  } else {
    handlePreview(record);
  }
};

// 导航到文件夹
const navigateToFolder = (folderId) => {
  emit('folderChange', folderId);
};

// 加载面包屑路径
const loadBreadcrumbPath = async () => {
  try {
    const response = await getFolderPath(props.currentFolderId);
    if (response.code === 200) {
      breadcrumbPath.value = response.data || [];
    }
  } catch (error) {
    console.error('加载路径失败:', error);
    breadcrumbPath.value = [];
  }
};

// 加载文件列表
const loadFileList = async () => {
  loading.value = true;
  try {
    let response;

    // 如果是根目录或指定文件夹，使用文件夹API
    if (props.currentFolderId && props.currentFolderId !== '0') {
      response = await getFilesByParentId(props.currentFolderId, filterForm.businessType);
    } else {
      // 构建查询参数
      const queryParams = {
        pageNum: pagination.current,
        pageSize: pagination.pageSize,
        parentId: props.currentFolderId === '0' ? null : props.currentFolderId,
        ...filterForm
      };

      // 添加搜索关键词
      if (searchKeyword.value) {
        queryParams.fileName = searchKeyword.value;
      }

      response = await listFile(queryParams);
    }

    if (response.code === 200) {
      fileList.value = response.rows || response.data || [];
      pagination.total = response.total || fileList.value.length;

      // 加载面包屑路径
      if (props.currentFolderId && props.currentFolderId !== '0') {
        await loadBreadcrumbPath();
      } else {
        breadcrumbPath.value = [];
      }
    } else {
      message.error(response.msg || '加载文件列表失败');
      fileList.value = [];
    }
  } catch (error) {
    console.error('加载文件列表失败:', error);
    message.error('加载文件列表失败');
    fileList.value = [];
  } finally {
    loading.value = false;
  }
};

// 其他方法...
const showUploadModal = () => {
  emit('showUpload');
};

const showCreateFolderModal = () => {
  emit('showCreateFolder');
};

const handleSearch = () => {
  pagination.current = 1;
  loadFileList();
};

const applyFilter = () => {
  filterDropdownVisible.value = false;
  pagination.current = 1;
  loadFileList();
};

const resetFilter = () => {
  Object.keys(filterForm).forEach(key => {
    filterForm[key] = undefined;
  });
  pagination.current = 1;
  loadFileList();
};

const handlePreview = async (record) => {
  if (record.isFolder) {
    message.info('文件夹无法预览');
    return;
  }

  const ext = record.fileType?.toLowerCase();
  const previewableTypes = ['pdf', 'jpg', 'jpeg', 'png', 'gif', 'txt'];

  if (previewableTypes.includes(ext)) {
    try {
      const response = await getFilePreviewUrl(record.id);
      if (response.code === 200 && response.data.url) {
        window.open(response.data.url, '_blank');
      } else {
        message.error('获取预览链接失败');
      }
    } catch (error) {
      console.error('获取预览链接失败:', error);
      message.error('获取预览链接失败');
    }
  } else {
    message.info('该文件类型暂不支持预览');
  }

  emit('preview', record);
};

const handleDownload = async (record) => {
  try {
    const response = await getFileDownloadUrl(record.id);
    if (response.code === 200 && response.data.url) {
      window.open(response.data.url, '_blank');
    } else {
      message.error('获取下载链接失败');
    }
  } catch (error) {
    console.error('获取下载链接失败:', error);
    message.error('获取下载链接失败');
  }
};

const handleRename = (record) => {
  renameForm.newName = record.fileName;
  renameForm.targetFile = record;
  renameModalVisible.value = true;
};

const handleDelete = (record) => {
  deleteTarget.value = record;
  deleteModalVisible.value = true;
};

const handleBatchAction = ({ key }) => {
  if (key === 'move') {
    showBatchMoveModal();
  } else if (key === 'delete') {
    batchDeleteModalVisible.value = true;
  }
};

// 显示批量移动弹窗
const showBatchMoveModal = async () => {
  try {
    // 加载文件夹树数据
    const response = await getFolderTree();
    if (response.code === 200) {
      folderTreeData.value = buildFolderTreeData(response.data || []);
      batchMoveModalVisible.value = true;
    } else {
      message.error('加载文件夹数据失败');
    }
  } catch (error) {
    console.error('加载文件夹数据失败:', error);
    message.error('加载文件夹数据失败');
  }
};

// 构建文件夹树数据
const buildFolderTreeData = (folders) => {
  const rootNode = {
    id: '0',
    fileName: '根目录',
    children: folders.map(folder => buildFolderNode(folder))
  };
  return [rootNode];
};

// 构建文件夹节点
const buildFolderNode = (folder) => {
  return {
    id: folder.id,
    fileName: folder.fileName,
    children: folder.children ? folder.children.map(child => buildFolderNode(child)) : []
  };
};

// 处理文件夹选择
const handleFolderSelect = (selectedKeys) => {
  moveTargetFolder.value = selectedKeys;
  if (selectedKeys.length > 0) {
    const folderId = selectedKeys[0];
    selectedFolderInfo.value = findFolderById(folderTreeData.value, folderId);
  }
};

// 根据ID查找文件夹
const findFolderById = (folders, id) => {
  for (const folder of folders) {
    if (folder.id === id) {
      return folder;
    }
    if (folder.children) {
      const found = findFolderById(folder.children, id);
      if (found) return found;
    }
  }
  return null;
};

// 获取选中文件夹路径
const getSelectedFolderPath = () => {
  if (moveTargetFolder.value.length === 0) {
    return '未选择';
  }

  const folderId = moveTargetFolder.value[0];
  if (folderId === '0') {
    return '根目录';
  }

  const folder = findFolderById(folderTreeData.value, folderId);
  return folder ? folder.fileName : '未知文件夹';
};

// 确认批量移动
const confirmBatchMove = async () => {
  if (moveTargetFolder.value.length === 0) {
    message.error('请选择目标文件夹');
    return;
  }

  try {
    const response = await moveFiles({
      fileIds: selectedRowKeys.value,
      targetParentId: moveTargetFolder.value[0]
    });

    if (response.code === 200) {
      message.success(`成功移动 ${selectedRowKeys.value.length} 个文件`);
      selectedRowKeys.value = [];
      batchMoveModalVisible.value = false;
      loadFileList(); // 刷新列表
    } else {
      message.error(response.msg || '移动失败');
    }
  } catch (error) {
    console.error('移动失败:', error);
    message.error('移动失败');
  }
};

// 取消批量移动
const cancelBatchMove = () => {
  batchMoveModalVisible.value = false;
  moveTargetFolder.value = [];
  selectedFolderInfo.value = null;
};

// 重命名相关方法
const confirmRename = async () => {
  if (!renameForm.newName.trim()) {
    message.error('请输入文件名');
    return;
  }

  try {
    const response = await renameFile({
      fileId: renameForm.targetFile.id,
      newName: renameForm.newName
    });

    if (response.code === 200) {
      // 更新本地数据
      const targetIndex = fileList.value.findIndex(f => f.id === renameForm.targetFile.id);
      if (targetIndex !== -1) {
        fileList.value[targetIndex].fileName = renameForm.newName;
      }

      message.success('重命名成功');
      renameModalVisible.value = false;
      emit('rename', { ...renameForm.targetFile, fileName: renameForm.newName });
    } else {
      message.error(response.msg || '重命名失败');
    }
  } catch (error) {
    console.error('重命名失败:', error);
    message.error('重命名失败');
  }
};

const cancelRename = () => {
  renameModalVisible.value = false;
  renameForm.newName = '';
  renameForm.targetFile = null;
};

// 删除相关方法
const confirmDelete = async () => {
  try {
    const response = await delFile([deleteTarget.value.id]);

    if (response.code === 200) {
      // 从列表中移除
      fileList.value = fileList.value.filter(f => f.id !== deleteTarget.value.id);

      message.success('删除成功');
      deleteModalVisible.value = false;
      emit('delete', deleteTarget.value);
    } else {
      message.error(response.msg || '删除失败');
    }
  } catch (error) {
    console.error('删除失败:', error);
    message.error('删除失败');
  }
};

const cancelDelete = () => {
  deleteModalVisible.value = false;
  deleteTarget.value = null;
};

// 批量删除相关方法
const confirmBatchDelete = async () => {
  try {
    const response = await delFile(selectedRowKeys.value);

    if (response.code === 200) {
      // 从列表中移除选中的文件
      fileList.value = fileList.value.filter(f => !selectedRowKeys.value.includes(f.id));

      message.success(`成功删除 ${selectedRowKeys.value.length} 个文件`);
      const deletedIds = [...selectedRowKeys.value];
      selectedRowKeys.value = [];
      batchDeleteModalVisible.value = false;
      emit('batchDelete', deletedIds);
    } else {
      message.error(response.msg || '批量删除失败');
    }
  } catch (error) {
    console.error('批量删除失败:', error);
    message.error('批量删除失败');
  }
};

const cancelBatchDelete = () => {
  batchDeleteModalVisible.value = false;
};

const handleGridItemClick = (file) => {
  const index = selectedRowKeys.value.indexOf(file.id);
  if (index > -1) {
    selectedRowKeys.value.splice(index, 1);
  } else {
    selectedRowKeys.value.push(file.id);
  }
};

const handleRightClick = (event, file) => {
  // 右键菜单逻辑
};

// 监听文件夹变化
watch(() => props.currentFolderId, () => {
  pagination.current = 1;
  selectedRowKeys.value = [];
  loadFileList();
}, { immediate: true });

defineExpose({
  refreshList: loadFileList
});
</script>

<style scoped>
.file-list {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.breadcrumb-container {
  padding: 16px 24px 8px;
  border-bottom: 1px solid #f0f0f0;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 16px;
  /* border-bottom: 1px solid #f0f0f0; */
}

.toolbar-left {
  display: flex;
  gap: 8px;
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.toolbar-right :deep(.ant-btn-group) {
  display: inline-flex;
  gap: 0 !important;
}

.toolbar-right :deep(.ant-btn-group .ant-btn) {
  margin-left: 0 !important;
  margin-right: 0 !important;
}

.toolbar-right :deep(.ant-btn-group .ant-btn:not(:first-child)) {
  margin-left: -1px !important;
}

.file-content {
  flex: 1;
  padding: 0 16px;
  overflow: auto;
}

.file-name-cell {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.file-icon {
  margin-right: 8px;
  font-size: 16px;
}

.file-name {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.grid-view {
  height: 100%;
  padding-top: 16px;
}

.grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 16px;
}

.grid-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 8px;
  border: 1px solid transparent;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.grid-item:hover {
  background-color: var(--hover-bg, #f5f5f5);
  border-color: var(--border-color-base, #d9d9d9);
}

.grid-item.selected {
  background-color: var(--primary-1, #e6f7ff);
  border-color: var(--primary-color, #1890ff);
}

.grid-item-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.grid-item-name {
  font-size: 12px;
  text-align: center;
  word-break: break-all;
  line-height: 1.4;
  margin-bottom: 4px;
  max-height: 2.8em;
  overflow: hidden;
}

.grid-item-info {
  font-size: 11px;
  color: #999;
  text-align: center;
  line-height: 1.2;
}

.grid-item-info span {
  display: block;
}

.filter-panel {
  background: white;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 文件图标颜色样式 */
.icon-folder {
  color: #faad14; /* 文件夹 - 橙色 */
}

.icon-pdf {
  color: #f5222d; /* PDF - 红色 */
}

.icon-word {
  color: #1890ff; /* Word - 蓝色 */
}

.icon-excel {
  color: #52c41a; /* Excel - 绿色 */
}

.icon-image {
  color: #722ed1; /* 图片 - 紫色 */
}

.icon-text {
  color: #13c2c2; /* 文本 - 青色 */
}

.icon-archive {
  color: #fa8c16; /* 压缩包 - 橙色 */
}

.icon-video {
  color: #eb2f96; /* 视频 - 粉色 */
}

.icon-audio {
  color: #a0d911; /* 音频 - 青绿色 */
}

.icon-default {
  color: #8c8c8c; /* 默认 - 灰色 */
}

/* 批量移动弹窗样式 */
.folder-tree-container {
  max-height: 300px;
  overflow-y: auto;
  overflow-x: hidden;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  padding: 8px;
  background: #fafafa;
  /* 确保滚动条可见 */
  scrollbar-width: thin;
  scrollbar-color: rgba(148, 163, 184, 0.45) transparent;
}

/* Webkit浏览器滚动条样式 */
.folder-tree-container::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.folder-tree-container::-webkit-scrollbar-track {
  border-radius: 999px;
  background: transparent;
}

.folder-tree-container::-webkit-scrollbar-thumb {
  border: 2px solid transparent;
  border-radius: 999px;
  background: rgba(148, 163, 184, 0.45);
  background-clip: content-box;
}

.folder-tree-container::-webkit-scrollbar-thumb:hover {
  background: rgba(100, 116, 139, 0.58);
  background-clip: content-box;
}

.folder-tree-container .ant-tree {
  background: transparent;
  /* 确保树组件不会阻止滚动 */
  min-height: auto;
}

.folder-tree-container .ant-tree-node-content-wrapper {
  padding: 4px 8px;
  border-radius: 4px;
  /* 确保节点内容不会超出容器 */
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.folder-tree-container .ant-tree-node-content-wrapper:hover {
  background-color: var(--primary-1, #e6f7ff);
}

.folder-tree-container .ant-tree-node-selected .ant-tree-node-content-wrapper {
  background-color: var(--primary-1, #bae7ff) !important;
  color: var(--primary-color, #1890ff) !important;
}

/* 确保树节点标题不会过长 */
.folder-tree-container .ant-tree-title {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 200px;
}

.grid-item-name {
  font-size: 12px;
  text-align: center;
  word-break: break-all;
  margin-bottom: 4px;
  max-height: 32px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-clamp: 2;
}

.grid-item-info {
  font-size: 11px;
  color: #999;
  text-align: center;
}

.grid-item-info span {
  display: block;
}

.filter-panel {
  background: white;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
</style>

<style>
/* ========== 暗黑模式 - 文件管理特殊样式 ========== */
.dark-theme .file-list {
  /* 确保表格行之间和底部没有白色边框 */
  .ant-table-tbody > tr > td {
    border-bottom: 1px solid rgba(255, 255, 255, 0.05) !important;
  }

  /* 消除表格最后一行的底部边框 */
  .ant-table-tbody > tr:last-child > td {
    border-bottom: none !important;
  }

  /* 消除表格底部可能的白色线条 */
  .ant-table,
  .ant-table-container,
  .ant-table-content {
    border-bottom: none !important;
  }

  /* ========== 修复文件图标颜色显示 ========== */
  /* 确保图标在暗色模式下显示正确的颜色 */
  .icon-folder {
    color: #faad14 !important; /* 文件夹 - 橙色 */
  }

  .icon-pdf {
    color: #f5222d !important; /* PDF - 红色 */
  }

  .icon-word {
    color: #1890ff !important; /* Word - 蓝色 */
  }

  .icon-excel {
    color: #52c41a !important; /* Excel - 绿色 */
  }

  .icon-image {
    color: #722ed1 !important; /* 图片 - 紫色 */
  }

  .icon-text {
    color: #13c2c2 !important; /* 文本 - 青色 */
  }

  .icon-archive {
    color: #fa8c16 !important; /* 压缩包 - 橙色 */
  }

  .icon-video {
    color: #eb2f96 !important; /* 视频 - 粉色 */
  }

  .icon-audio {
    color: #a0d911 !important; /* 音频 - 青绿色 */
  }

  .icon-default {
    color: #8c8c8c !important; /* 默认 - 灰色 */
  }

  /* 网格视图中的图标颜色 */
  .grid-item-icon {
    .icon-folder { color: #faad14 !important; }
    .icon-pdf { color: #f5222d !important; }
    .icon-word { color: #1890ff !important; }
    .icon-excel { color: #52c41a !important; }
    .icon-image { color: #722ed1 !important; }
    .icon-text { color: #13c2c2 !important; }
    .icon-archive { color: #fa8c16 !important; }
    .icon-video { color: #eb2f96 !important; }
    .icon-audio { color: #a0d911 !important; }
    .icon-default { color: #8c8c8c !important; }
  }

  /* 表格中的文件名单元格图标 */
  .file-name-cell {
    .icon-folder { color: #faad14 !important; }
    .icon-pdf { color: #f5222d !important; }
    .icon-word { color: #1890ff !important; }
    .icon-excel { color: #52c41a !important; }
    .icon-image { color: #722ed1 !important; }
    .icon-text { color: #13c2c2 !important; }
    .icon-archive { color: #fa8c16 !important; }
    .icon-video { color: #eb2f96 !important; }
    .icon-audio { color: #a0d911 !important; }
    .icon-default { color: #8c8c8c !important; }
  }
}
</style>
