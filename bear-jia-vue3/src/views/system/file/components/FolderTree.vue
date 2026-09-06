<template>
  <div class="folder-tree">
    <div class="tree-header">
      <h3>文件夹</h3>
      <a-button
        type="primary"
        size="small"
        @click="showCreateFolderModal"
        :icon="h(PlusOutlined)"
      >
        新建文件夹
      </a-button>
    </div>

    <a-directory-tree
      v-model:selectedKeys="selectedKeys"
      v-model:expandedKeys="expandedKeys"
      :tree-data="treeData"
      :load-data="loadTreeData"
      :show-icon="true"
      :draggable="true"
      @select="onSelect"
      @drop="onDrop"
      @rightClick="onRightClick"
    >
      <!-- <template #icon="{ isLeaf }">
        <FolderOutlined v-if="!isLeaf" />
      </template>

      <template #title="{ title, key, isLeaf }">
        <span
          class="tree-node-title"
          @contextmenu.prevent="onRightClick({ node: { key, title, isLeaf } })"
        >
          {{ title }}
        </span>
      </template> -->
    </a-directory-tree>

    <!-- 右键菜单 -->
    <a-dropdown
      v-model:open="contextMenuVisible"
      :trigger="['contextmenu']"
      placement="bottomLeft"
    >
      <div
        :style="{
          position: 'fixed',
          left: contextMenuPosition.x + 'px',
          top: contextMenuPosition.y + 'px',
          width: '1px',
          height: '1px'
        }"
      ></div>
      <template #overlay>
        <a-menu @click="handleContextMenuClick">
          <a-menu-item key="create">
            <FolderAddOutlined />
            新建文件夹
          </a-menu-item>
          <a-menu-item key="rename" v-if="contextMenuNode.key !== '0'">
            <EditOutlined />
            重命名
          </a-menu-item>
          <a-menu-divider v-if="contextMenuNode.key !== '0'" />
          <a-menu-item key="delete" v-if="contextMenuNode.key !== '0'" danger>
            <DeleteOutlined />
            删除
          </a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>

    <!-- 创建文件夹弹窗 -->
    <a-modal
      v-model:open="createFolderVisible"
      title="新建文件夹"
      @ok="handleCreateFolder"
      @cancel="createFolderVisible = false"
    >
      <a-form :model="createFolderForm" :rules="createFolderRules" ref="createFolderFormRef">
        <a-form-item label="文件夹名称" name="folderName">
          <a-input
            v-model:value="createFolderForm.folderName"
            placeholder="请输入文件夹名称"
            @pressEnter="handleCreateFolder"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 重命名弹窗 -->
    <a-modal
      v-model:open="renameVisible"
      title="重命名"
      @ok="handleRename"
      @cancel="renameVisible = false"
    >
      <a-form :model="renameForm" :rules="renameRules" ref="renameFormRef">
        <a-form-item label="新名称" name="newName">
          <a-input
            v-model:value="renameForm.newName"
            placeholder="请输入新名称"
            @pressEnter="handleRename"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, h } from 'vue';
import { message, Modal } from 'ant-design-vue';
import {
  FolderOutlined,
  PlusOutlined,
  FolderAddOutlined,
  EditOutlined,
  DeleteOutlined
} from '@ant-design/icons-vue';
import { getFolderTree, createFolder, renameFile, delFile } from '@/api/system/file';

const emit = defineEmits(['folderSelect', 'folderChange']);

// 树形数据
const treeData = ref([]);
const selectedKeys = ref(['0']);
const expandedKeys = ref(['0']);

// 右键菜单
const contextMenuVisible = ref(false);
const contextMenuPosition = reactive({ x: 0, y: 0 });
const contextMenuNode = reactive({ key: '0', title: '根目录', isLeaf: false });

// 创建文件夹
const createFolderVisible = ref(false);
const createFolderForm = reactive({
  folderName: '',
  parentId: '0'
});
const createFolderRules = {
  folderName: [
    { required: true, message: '请输入文件夹名称', trigger: 'blur' },
    { min: 1, max: 50, message: '文件夹名称长度在 1 到 50 个字符', trigger: 'blur' }
  ]
};
const createFolderFormRef = ref();

// 重命名
const renameVisible = ref(false);
const renameForm = reactive({
  newName: '',
  fileId: null
});
const renameRules = {
  newName: [
    { required: true, message: '请输入新名称', trigger: 'blur' },
    { min: 1, max: 50, message: '名称长度在 1 到 50 个字符', trigger: 'blur' }
  ]
};
const renameFormRef = ref();

// 初始化树形数据
const initTreeData = () => {
  treeData.value = [
    {
      title: '根目录',
      key: '0',
      isLeaf: false,
      children: []
    }
  ];
};

// 加载树形数据
const loadTreeData = async (treeNode) => {
  try {
    const parentId = treeNode.dataRef.key;
    const response = await getFolderTree(parentId);

    if (response.code === 200) {
      const folders = response.data || [];
      const children = folders.map(folder => ({
        title: folder.fileName,
        key: folder.id.toString(),
        isLeaf: false,
        children: []
      }));

      treeNode.dataRef.children = children;
    } else {
      message.error(response.msg || '加载文件夹失败');
      treeNode.dataRef.children = [];
    }
  } catch (error) {
    console.error('加载文件夹失败:', error);
    message.error('加载文件夹失败');
    treeNode.dataRef.children = [];
  }
};

// 选择节点
const onSelect = (keys, { node }) => {
  if (keys.length > 0) {
    const folderId = keys[0];
    emit('folderSelect', folderId);
  }
};

// 拖拽
const onDrop = async ({ node, dragNode }) => {
  try {
    const targetParentId = node.key;
    const fileIds = [parseInt(dragNode.key)];

    // 调用移动API
    // await moveFiles(fileIds, targetParentId);
    message.success('移动成功');
    emit('folderChange');
  } catch (error) {
    console.error('移动失败:', error);
    message.error('移动失败');
  }
};

// 右键菜单
const onRightClick = ({ event, node }) => {
  contextMenuVisible.value = true;
  contextMenuPosition.x = event.clientX;
  contextMenuPosition.y = event.clientY;

  Object.assign(contextMenuNode, {
    key: node.key,
    title: node.title,
    isLeaf: node.isLeaf
  });
};

// 处理右键菜单点击
const handleContextMenuClick = ({ key }) => {
  contextMenuVisible.value = false;

  switch (key) {
    case 'create':
      showCreateFolderModal();
      break;
    case 'rename':
      showRenameModal();
      break;
    case 'delete':
      handleDeleteFolder();
      break;
  }
};

// 显示创建文件夹弹窗
const showCreateFolderModal = () => {
  createFolderForm.folderName = '';
  createFolderForm.parentId = contextMenuNode.key;
  createFolderVisible.value = true;
};

// 创建文件夹
const handleCreateFolder = async () => {
  try {
    await createFolderFormRef.value.validate();

    const response = await createFolder({
      folderName: createFolderForm.folderName,
      parentId: createFolderForm.parentId
    });

    if (response.code === 200) {
      message.success('创建成功');
      createFolderVisible.value = false;
      emit('folderChange');
      // 刷新树形数据
      await refreshTreeData();
    }
  } catch (error) {
    console.error('创建文件夹失败:', error);
    message.error('创建文件夹失败');
  }
};

// 显示重命名弹窗
const showRenameModal = () => {
  renameForm.newName = contextMenuNode.title;
  renameForm.fileId = contextMenuNode.key;
  renameVisible.value = true;
};

// 重命名
const handleRename = async () => {
  try {
    await renameFormRef.value.validate();

    const response = await renameFile({
      fileId: renameForm.fileId,
      newName: renameForm.newName
    });

    if (response.code === 200) {
      message.success('重命名成功');
      renameVisible.value = false;
      emit('folderChange');
      await refreshTreeData();
    }
  } catch (error) {
    console.error('重命名失败:', error);
    message.error('重命名失败');
  }
};

// 删除文件夹
const handleDeleteFolder = () => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除文件夹"${contextMenuNode.title}"吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      try {
        const response = await delFile([contextMenuNode.key]);
        if (response.code === 200) {
          message.success('删除成功');
          emit('folderChange');
          await refreshTreeData();
        }
      } catch (error) {
        console.error('删除失败:', error);
        message.error('删除失败');
      }
    }
  });
};

// 刷新树形数据
const refreshTreeData = async () => {
  try {
    const response = await getFolderTree();
    if (response.code === 200) {
      const folders = response.data || [];
      treeData.value = [
        {
          title: '根目录',
          key: '0',
          isLeaf: false,
          children: buildTreeNodes(folders)
        }
      ];
    }
  } catch (error) {
    console.error('刷新树形数据失败:', error);
    initTreeData();
  }
};

// 构建树形节点
const buildTreeNodes = (folders) => {
  return folders.map(folder => ({
    title: folder.fileName,
    key: folder.id.toString(),
    isLeaf: false,
    children: folder.children ? buildTreeNodes(folder.children) : []
  }));
};

onMounted(() => {
  initTreeData();
});

defineExpose({
  refreshTreeData,
  showCreateFolderModal
});
</script>

<style scoped>
.folder-tree {
  height: 100%;
  padding: 16px;
  background: #ffffff;
  border-right: 1px solid #f0f0f0;
}

.tree-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.tree-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
  color: #1e293b;
}

.tree-node-title {
  display: inline-block;
  width: 100%;
  padding: 2px 4px;
  border-radius: 4px;
  transition: color 0.3s, background-color 0.3s;
}

.tree-node-title:hover {
  background-color: var(--hover-bg, #f5f5f5);
}

:deep(.ant-tree-node-content-wrapper:hover) {
  background-color: transparent;
}

:deep(.ant-tree-node-content-wrapper.ant-tree-node-selected),
:deep(.ant-tree-node-selected .tree-node-title) {
  background-color: var(--primary-1, #e6f7ff);
  color: var(--primary-color, #1890ff);
}

:deep(.ant-tree-node-content-wrapper.ant-tree-node-selected .ant-tree-title),
:deep(.ant-tree-node-selected .ant-tree-iconEle) {
  color: var(--primary-color, #1890ff);
}
</style>

<style>
/* 暗黑模式适配 */
.dark-theme .folder-tree {
  background: #141414 !important;
  border-right-color: #303030 !important;
}

.dark-theme .folder-tree .tree-header {
  border-bottom-color: #303030;
}

.dark-theme .folder-tree .tree-header h3 {
  color: rgba(255, 255, 255, 0.85);
}

.dark-theme .folder-tree .tree-node-title:hover {
  background-color: var(--hover-bg);
}

.dark-theme .folder-tree :deep(.ant-tree-node-content-wrapper.ant-tree-node-selected),
.dark-theme .folder-tree :deep(.ant-tree-node-selected .tree-node-title) {
  background-color: var(--primary-1) !important;
  color: var(--primary-color) !important;
}

.dark-theme .folder-tree :deep(.ant-tree-node-selected .ant-tree-iconEle) {
  color: var(--primary-color) !important;
}

/* 暗黑模式下的目录树样式 */
.dark-theme .folder-tree :deep(.ant-tree) {
  background: transparent !important;
  color: rgba(255, 255, 255, 0.85);
}

.dark-theme .folder-tree :deep(.ant-tree-title) {
  color: rgba(255, 255, 255, 0.85);
}

.dark-theme .folder-tree :deep(.ant-tree-node-content-wrapper) {
  color: rgba(255, 255, 255, 0.85);
}

.dark-theme .folder-tree :deep(.ant-tree-node-content-wrapper:hover) {
  background-color: rgba(255, 255, 255, 0.08);
}

.dark-theme .folder-tree :deep(.ant-tree-node-content-wrapper.ant-tree-node-selected) {
  background-color: var(--primary-1) !important;
  color: var(--primary-color) !important;
}

.dark-theme .folder-tree :deep(.ant-tree-switcher) {
  color: rgba(255, 255, 255, 0.45);
}

.dark-theme .folder-tree :deep(.ant-tree-iconEle) {
  color: rgba(255, 255, 255, 0.45);
}

.dark-theme .folder-tree :deep(.ant-tree-node-content-wrapper.ant-tree-node-selected .ant-tree-title),
.dark-theme .folder-tree :deep(.ant-tree-node-content-wrapper.ant-tree-node-selected .ant-tree-iconEle) {
  color: var(--primary-color) !important;
}
</style>
