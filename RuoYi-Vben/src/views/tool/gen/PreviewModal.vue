<template>
  <BasicModal
    v-bind="$attrs"
    okText="复制代码"
    :title="title"
    :canFullscreen="false"
    :defaultFullscreen="true"
    @register="registerInnerModal"
    @ok="handleCopy"
    @cancel="handleReset"
  >
    <!-- 只能全屏进行预览 -->
    <div class="h-full w-full flex flex-row gap-3">
      <BasicTree class="w-350px h-full" ref="treeRef" :treeData="treeData" @select="handleSelect" />
      <CodeEditor class="w-full h-full" :value="codeValue" :mode readonly />
    </div>
  </BasicModal>
</template>

<script setup lang="ts">
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { preview } from '@/api/tool/gen';
  import { CodeEditor, MODE } from '@/components/CodeEditor';
  import { ref, nextTick } from 'vue';
  import { copyText } from '@/utils/copyTextToClipboard';
  import { BasicTree } from '@/components/Tree';

  defineOptions({ name: 'CodePreviewModal' });

  interface TreeNode {
    children: TreeNode[];
    title: string;
    key: string;
    icon: string; // 树左边图标
  }

  const iconMap = [
    { key: 'java', value: 'skill-icons:java-light' },
    { key: 'xml', value: 'tabler:file-type-xml' },
    { key: 'sql', value: 'carbon:sql' },
    { key: 'ts', value: 'skill-icons:typescript' },
    { key: 'vue', value: 'logos:vue' },
    { key: 'folder', value: 'flat-color-icons:folder' },
  ];
  function findIcon(path: string) {
    const defaultFileIcon = 'bx:file';
    const defaultFolderIcon = 'flat-color-icons:folder';
    if (path.endsWith('.vm')) {
      // todo 这里写死的
      const realPath = path.slice(0, -3);
      // 是否为指定拓展名
      const icon = iconMap.find((item) => realPath.endsWith(item.key));
      if (icon) {
        return icon.value;
      }
      return defaultFileIcon;
    }
    // 其他的为文件夹
    return defaultFolderIcon;
  }

  function createTree(data: string[]): TreeNode[] {
    const tree: TreeNode[] = [];

    for (const item of data) {
      let current_node = tree;
      const parts = item.split('/');

      let key = '';
      for (let part of parts) {
        if (part.endsWith('.vm')) {
          // 以.vm结尾不需要添加末尾的/
          key += part;
          // 移除末尾的 .vm
          part = part.slice(0, -3);
        } else {
          // 添加路径
          key += part + '/';
        }

        let child_node = current_node.find((node) => node.title === part);

        if (!child_node) {
          child_node = {
            children: [],
            title: part,
            key: key,
            icon: findIcon(key),
          };
          current_node.push(child_node);
        }

        current_node = child_node.children;
      }
    }
    return tree;
  }

  // instance
  const treeRef = ref<InstanceType<typeof BasicTree>>();
  // treeData
  const treeData = ref<TreeNode[]>([]);
  // 当前预览的代码
  const codeValue = ref<string>('左侧选择进行预览');
  // 保存所有的代码
  const codeData = ref<{ [key: string]: string }>({});

  const title = ref<string>('代码预览');

  const [registerInnerModal, { modalLoading }] = useModalInner(async (tableId: string) => {
    try {
      modalLoading(true);
      // 原始数据
      const ret = await preview(tableId);
      codeData.value = ret;

      // 生成树
      const tree = createTree(Object.keys(ret));
      treeData.value = tree;

      // 展开全部
      nextTick(() => {
        (treeRef.value as any)?.expandAll(true);
      });
    } catch (e) {
      console.warn(e);
    } finally {
      modalLoading(false);
    }
  });

  function handleCopy() {
    copyText(codeValue.value, '复制成功');
  }

  // 代码类型
  const mode = ref<MODE>(MODE.HTML);
  function changeCodeType(filename: string) {
    const typeList = [
      { type: '.ts', mode: MODE.TYPESCRIPT },
      { type: '.java', mode: MODE.JAVA },
      { type: '.xml', mode: MODE.XML },
      { type: 'sql', mode: MODE.SQL },
      { type: '.vue', mode: MODE.VUE },
    ];
    const type = typeList.find((item) => filename.includes(item.type));
    if (type) {
      mode.value = type.mode;
    } else {
      // 默认Html
      mode.value = MODE.HTML;
    }
  }

  function handleSelect(selectedKeys: string[]) {
    const [currentFile = ''] = selectedKeys;
    const currentCode = codeData.value[currentFile];
    if (currentCode) {
      // 设置代码type
      changeCodeType(currentFile);
      // 内容
      codeValue.value = currentCode;
      // 修改标题
      title.value = '代码预览: ' + currentFile.replace('.vm', '');
    }
  }

  function handleReset() {
    codeValue.value = '左侧选择进行预览';
    codeData.value = {};
    treeData.value = [];
    title.value = '代码预览';
    mode.value = MODE.XML;
    nextTick(() => {
      (treeRef.value as any)?.setSelectedKeys([]);
    });
  }
</script>

<style lang="less" scoped>
  :deep(.CodeMirror) {
    height: 100%;
  }
</style>
