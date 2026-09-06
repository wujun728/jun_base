<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统管理', '菜单管理']"></Breadcrumb>
    <a-card
        class="general-card r-nav-card"
        :body-style="{ padding: '15px' }"
        :header-style="{ padding: '15px' }"
    >
      <a-row>
        <!-- 搜索区块 -->
        <a-col :flex="1">
          <a-form
              :model="state.queryParams"
              :label-col-props="{ span: 6 }"
              :wrapper-col-props="{ span: 18 }"
              label-align="left"
              auto-label-width
          >
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="menuName" :label="'菜单名称'">
                  <a-input
                      v-model="state.queryParams.menuName"
                      :placeholder="'请输入菜单名称'">
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="status" :label="'状态'">
                  <a-select v-model="state.queryParams.status" placeholder="请选择">
                    <a-option
                        v-for="dict in state.statusOptions"
                        :key="dict.dictValue"
                        :value="dict.dictValue"
                        :label="dict.dictLabel"
                    >{{ dict.dictLabel }}
                    </a-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 42px" direction="vertical"/>
        <a-col :flex="'86px'" style="text-align: right">
          <a-space :size="18">
            <a-button type="primary" @click="fetchMenuData">
              <template #icon>
                <icon-search/>
              </template>
              查询
            </a-button>
            <a-button @click="resetQueryForm">
              <template #icon>
                <icon-refresh/>
              </template>
              重置
            </a-button>
          </a-space>
        </a-col>
        <a-divider style="margin-top: 0"/>
        <!-- 工具栏 -->
        <a-row style="margin-bottom: 16px">
          <!-- 前置工具栏部分 -->
          <a-col :span="12">
            <a-space>
              <a-button v-permission="['system:menu:add']" type="primary" @click="handleAdd(0)">
                <template #icon>
                  <icon-plus/>
                </template>
                新建
              </a-button>
              <a-button @click="toggleExpandAll">
                <template #icon>
                  <icon-shrink v-if="state.isExpandAll"/>
                  <icon-expand v-if="!state.isExpandAll"/>
                </template>
                <span v-if="state.isExpandAll">折叠</span>
                <span v-if="!state.isExpandAll">展开</span>
              </a-button>
            </a-space>
          </a-col>
        </a-row>
      </a-row>
      <!-- 表格数据-->
      <a-table
          ref="tableRef"
          row-key="menuId"
          :loading="loading"
          :columns="state.columns"
          :data="state.menuList"
          :bordered="false"
          :size="'medium'"
      >
        <template #icon="{ record }">
          <functional-icons v-if="record.icon.includes('Icon')" :icon="record.icon"></functional-icons>
          <span v-else>{{ record.icon }}</span>
        </template>
        <template #status="{ record }">
          <a-tag v-if="record.status === '1'" color="red">停用</a-tag>
          <a-tag v-else-if="record.status === '0'" color="blue">正常</a-tag>
          <a-tag v-else color="gray">未知</a-tag>
        </template>
        <template #optional="{ record }">
          <a-space>
            <a-button-group>
              <a-button v-permission="['system:menu:add']" type="text" size="mini" @click="handleAdd(record.menuId)">
                <template #icon>
                  <icon-plus/>
                </template>
                新增
              </a-button>
              <a-button v-permission="['system:menu:edit']" type="text" size="mini"
                        @click="handleUpdate(record.menuId)">
                <template #icon>
                  <icon-edit/>
                </template>
                修改
              </a-button>
              <a-button v-permission="['system:menu:remove']" type="text" size="mini" @click="handleDelete(record)">
                <template #icon>
                  <icon-delete/>
                </template>
                删除
              </a-button>
            </a-button-group>
          </a-space>
        </template>
      </a-table>
    </a-card>
  </div>
  <edit-menu
      v-model:id="state.currentId"
      v-model:parent-id="state.currentParentId"
      v-model:visible="state.editVisible"
      @refresh-data-list="fetchMenuData"></edit-menu>
</template>

<script lang="ts" setup>

import {reactive, ref} from "vue";
import {getDicts} from "@/api/system/dict-data";
import useLoading from "@/hooks/loading";
import {delMenu, listMenu, MenuRecord} from "@/api/system/menu";
import {Message, Modal} from "@arco-design/web-vue";
import FunctionalIcons from '@/components/functional-icons/index.vue';
import EditMenu from "./components/edit-menu.vue";

// 定义页面加载中效果变量
const {loading, setLoading} = useLoading(true);

const state = reactive({
  queryParams: {
    menuName: undefined,
    status: undefined,
    params: undefined,
  },
  // 菜单列表数据
  menuList: [],
  // 字典
  statusOptions: [],
  // 选中行ID
  currentId: 0,
  currentParentId: 0,
  // 显示编辑页面
  editVisible: false,
  // 是否全部展开
  isExpandAll: false,
  // 列表显示列
  columns: [
    {
      title: '菜单名称',
      dataIndex: 'menuName',
      slotName: 'menuName',
      width: 160,
    },
    {
      title: '图标',
      dataIndex: 'icon',
      slotName: 'icon',
      width: 100,
    },
    {
      title: '排序',
      dataIndex: 'orderNum',
      slotName: 'orderNum',
      width: 60,
    },
    {
      title: '权限标识',
      dataIndex: 'perms',
      slotName: 'perms',
      width: 250,
    },
    {
      title: '组件路径',
      dataIndex: 'component',
      slotName: 'component',
      width: 250,
    },
    {
      title: '状态',
      dataIndex: 'status',
      slotName: 'status',
      width: 100,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      slotName: 'createTime',
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'optional',
      slotName: 'optional',
      fixed: 'right',
      width: 200,
    },
  ],
});

// 初始化字典
const initData = async () => {
  const {data: statusData} = await getDicts('sys_normal_disable');
  state.statusOptions = statusData;
};
initData();

// 重置查询表单
const resetQueryForm = () => {
  state.queryParams = {
    menuName: undefined,
    status: undefined,
    params: undefined,
  };
  state.currentId = 0;
  state.currentParentId = 0;
};

// 数组转树结构集合
const listToTree: any = (list: Array<any>) => {
  const map: any = {};
  const treeData: Array<any> = [];
  for (let i = 0; i < list.length; i += 1) {
    map[list[i].menuId] = i;
    list[i].children = [];
  }
  for (let i = 0; i < list.length; i += 1) {
    const node = list[i];
    if (node.parentId && list[map[node.parentId]]) {
      list[map[node.parentId]].children.push(node);
    } else {
      treeData.push(node);
    }
  }
  return treeData;
};

// 获取菜单数据
const fetchMenuData = async () => {
  setLoading(true);
  const {data} = await listMenu({
    ...state.queryParams,
  })
  state.menuList = listToTree(data);
  setLoading(false);
}
fetchMenuData();

// 打开新增
const handleAdd = (menuId: number) => {
  state.currentId = 0;
  state.currentParentId = menuId;
  state.editVisible = true;
};

// 打开编辑
const handleUpdate = (menuId: number) => {
  state.currentId = menuId;
  state.currentParentId = 0;
  state.editVisible = true;
};

// 删除菜单
const handleDelete = (row: MenuRecord) => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `是否确认删除菜单名称为"${row.menuName}"的数据项？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      delMenu(row.menuId).then(() => {
        Message.success("删除成功");
        fetchMenuData();
      });
    },
  });
};

// 页面表格元素选择
const tableRef = ref();
// 展开/折叠操作
const toggleExpandAll = () => {
  state.isExpandAll = !state.isExpandAll;
  if (tableRef.value) {
    tableRef.value.expandAll(state.isExpandAll)
  }
};

</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}
</style>
