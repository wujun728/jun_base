<template>
  <div>
    <a-input-search
      style="margin-bottom: 8px"
      placeholder="搜索部门"
      enter-button
      width="100%"
      @change="filterNode"
      :disabled="lazyLoad"
    />
    <a-spin :spinning="loading">
      <a-tree
        v-if="!lazyLoad"
        :expanded-keys="expandedKeys"
        :auto-expand-parent="autoExpandParent"
        :tree-data="deptOptions"
        :field-names="replaceFields"
        @select="handleNodeClick"
        @expand="onExpand"
      />
      <a-tree
        v-else
        :tree-data="lazyTreeData"
        :field-names="replaceFields"
        :load-data="onLoadData"
        @select="handleNodeClick"
      />
    </a-spin>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { treeselectLazy } from '@/api/system/dept';

// 定义 Props
const props = defineProps({
  deptOptions: {
    type: Array,
    default: () => [],
  },
  // 是否启用懒加载模式
  lazyLoad: {
    type: Boolean,
    default: true,
  },
});

// 定义 Emit
const emit = defineEmits(['select']);

// 加载状态
const loading = ref(false);

// 懒加载树数据
const lazyTreeData = ref([]);

// 获取父节点 key 的辅助函数
const getParentKey = (id, tree) => {
  let parentKey;
  for (let i = 0; i < tree.length; i++) {
    const node = tree[i];
    if (node.children) {
      if (node.children.some((item) => item.id === id)) {
        parentKey = node.id;
      } else if (getParentKey(id, node.children)) {
        parentKey = getParentKey(id, node.children);
      }
    }
  }
  return parentKey;
};

// 响应式数据
const replaceFields = ref({
  children: 'children',
  title: 'label',
  key: 'id',
  value: 'id',
});
const deptNodes = ref([]);
const expandedKeys = ref([]);
const searchValue = ref('');
const autoExpandParent = ref(true);

// 获取所有部门节点的递归函数
const getAllDeptNode = (nodes) => {
  if (!nodes || nodes.length === 0) {
    return;
  }
  nodes.forEach((node) => {
    deptNodes.value.push({ id: node.id, label: node.label });
    getAllDeptNode(node.children);
  });
};

// 过滤节点并展开相关节点（仅在非懒加载模式下有效）
const filterNode = (e) => {
  if (props.lazyLoad) return;

  deptNodes.value = []; // 清空之前的节点
  getAllDeptNode(props.deptOptions);
  const value = e.target.value;
  const gData = props.deptOptions;
  expandedKeys.value = deptNodes.value
      .map((item) => {
        if (item.label.indexOf(value) > -1) {
          return getParentKey(item.id, gData);
        }
        return null;
      })
      .filter((item, i, self) => item && self.indexOf(item) === i);
  searchValue.value = value;
  autoExpandParent.value = true;
};

// 处理节点点击事件
const handleNodeClick = (keys) => {
  emit('select', keys);
};

// 处理展开事件
const onExpand = (keys) => {
  expandedKeys.value = keys;
  autoExpandParent.value = false;
};

// 懒加载数据处理函数
const onLoadData = (treeNode) => {
  return new Promise((resolve, reject) => {
    if (treeNode.dataRef.children) {
      resolve();
      return;
    }

    const parentId = treeNode.dataRef.id;
    treeselectLazy(parentId)
      .then((res) => {
        const children = res.data || [];
        // 为每个子节点添加 isLeaf 属性
        children.forEach((child) => {
          child.isLeaf = !child.hasChildren;
        });
        treeNode.dataRef.children = children;
        lazyTreeData.value = [...lazyTreeData.value];
        resolve();
      })
      .catch((error) => {
        console.error('加载部门子节点失败:', error);
        reject(error);
      });
  });
};

// 初始化懒加载根节点
const initLazyTree = async () => {
  if (!props.lazyLoad) return;

  loading.value = true;
  try {
    const res = await treeselectLazy(0);
    const rootNodes = res.data || [];
    // 为每个根节点添加 isLeaf 属性
    rootNodes.forEach((node) => {
      node.isLeaf = !node.hasChildren;
    });
    lazyTreeData.value = rootNodes;
  } catch (error) {
    console.error('加载部门根节点失败:', error);
  } finally {
    loading.value = false;
  }
};

// 组件挂载时初始化
onMounted(() => {
  if (props.lazyLoad) {
    initLazyTree();
  }
});

// 监听懒加载模式变化
watch(() => props.lazyLoad, (newVal) => {
  if (newVal) {
    initLazyTree();
  }
});

// 暴露刷新方法给父组件
defineExpose({
  refresh: initLazyTree,
});
</script>

<style lang="less" scoped>
:deep(.ant-input-search) {
  display: flex;

  .ant-input {
    background: var(--input-bg);
    color: var(--text-primary);
    border-color: var(--border-color-base);
    border-right: none;

    &::placeholder {
      color: var(--text-secondary);
    }

    &:focus {
      border-color: var(--primary-color);
      box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
    }

    &:disabled {
      background: var(--bg-color);
      cursor: not-allowed;
    }
  }

  .ant-input-search-button {
    background: var(--primary-color);
    border-color: var(--primary-color);
    border-left: none;

    &:hover {
      background: var(--primary-color-hover);
      border-color: var(--primary-color-hover);
    }

    &:disabled {
      background: var(--bg-color);
      border-color: var(--border-color-base);
    }
  }

  .ant-input-group-addon {
    padding: 0;
    border: none;
  }
}

:deep(.ant-tree) {
  background: var(--component-background);

  .ant-tree-node-content-wrapper {
    color: var(--text-primary);

    &:hover {
      background-color: rgba(24, 144, 255, 0.1);
    }

    &.ant-tree-node-selected {
      background-color: var(--primary-color);
      color: #fff;
    }
  }

  .ant-tree-switcher {
    color: var(--text-secondary);
  }

  .ant-tree-title {
    color: inherit;
  }

  .ant-tree-switcher-loading-icon {
    color: var(--primary-color);
  }
}

:deep(.ant-spin-nested-loading) {
  height: 100%;

  .ant-spin-container {
    height: 100%;
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
</style>
