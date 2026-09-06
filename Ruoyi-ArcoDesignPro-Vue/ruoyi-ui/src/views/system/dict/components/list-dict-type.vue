<template>
  <a-input-search
      v-model="state.searchKey"
      placeholder="搜索"
      style="margin-bottom: 8px; width: 100%;"
      @change="searchType"
      @search="searchType"
  />
  <a-list :max-height="800" size="small" :scrollbar="true" @reach-bottom="onPageChange(state.queryParams.pageNum)">
    <a-list-item>
      <div><span style="font-size: 16px; color: #165dff">字典类型</span></div>
      <template #extra>
        <a-divider direction="vertical"/>
        <a-button size="mini" v-permission="['system:dict:add']" type="primary" @click="handleAdd">
          <template #icon>
            <icon-plus/>
          </template>
          新建
        </a-button>
      </template>
    </a-list-item>
    <template #scroll-loading>
      <div v-if="state.bottom">No more data</div>
    </template>
    <a-list-item v-for="type in state.typeList" :key="type" class="is-active">
      <template #extra>
        <a-tag v-if="type.status === '1'" color="red">停用</a-tag>
        <a-tag v-else-if="type.status === '0'" color="blue">正常</a-tag>
        <a-tag v-else color="gray">未知</a-tag>
      </template>
      <a-dropdown trigger="contextMenu" :style="{display:'block'}">
        <div @click="handleDictTypeClick(type)">
          <a-row>
            <a-col :span="12">{{ type.dictName }}</a-col>
            <a-col :span="12">{{ type.dictType }}</a-col>
          </a-row>
        </div>
        <template #content>
          <a-doption>
            <a-button v-permission="['system:dict:edit']" type="text" size="mini" @click="handleUpdate(type)">
              <template #icon>
                <icon-edit/>
              </template>
              修改
            </a-button>
          </a-doption>
          <a-doption>
            <a-button v-permission="['system:dict:remove']" type="text" size="mini" @click="handleDelete(type)">
              <template #icon>
                <icon-delete/>
              </template>
              删除
            </a-button>
          </a-doption>
        </template>
      </a-dropdown>
    </a-list-item>
  </a-list>
  <edit-dict-type
      v-model:id="state.currentId"
      v-model:parent-id="state.currentParentId"
      v-model:visible="state.editVisible"
      @refresh-data-list="searchType">
  </edit-dict-type>
</template>

<script lang="ts" setup>

import {reactive} from "vue";
import useLoading from "@/hooks/loading";
import {delType, DictTypeRecord, listType} from "@/api/system/dict-type";
import {Message, Modal} from "@arco-design/web-vue";
import EditDictType from "./edit-dict-type.vue";

const state = reactive({
  searchKey: '',
  // 字典表格数据
  typeList: [] as DictTypeRecord[],
  // 字典数量
  total: 0,
  // 查询参数
  queryParams: {
    pageNum: 1,
    pageSize: 14,
    dictName: '',
  },
  // 选中行ID
  currentId: 0,
  currentParentId: 0,
  // 显示编辑页面
  editVisible: false,
  // 底部显示
  bottom: false,
});

const emits = defineEmits(['select', 'firstDictType']);

// 定义页面加载中效果变量
const {loading, setLoading} = useLoading(true);

// 查询字典类型列表
const fetchTypeList = async () => {
  setLoading(true);
  const {rows, code, total} = await listType({...state.queryParams});
  state.typeList = rows;
  state.total = total;
  if (state.typeList.length > 0) {
    emits('firstDictType', state.typeList[0])
  }
  setLoading(false);
}
fetchTypeList();

// 懒加载下一页
const onPageChange = async (current: number) => {
  state.queryParams.pageNum = current + 1;
  const {rows, total} = await listType({...state.queryParams});
  if (total > state.typeList.length) {
    state.typeList = state.typeList.concat(rows);
  } else {
    state.bottom = true;
  }
};

const searchType = () => {
  state.queryParams.dictName = state.searchKey;
  state.queryParams.pageNum = 1;
  state.queryParams.pageSize = 14;
  state.bottom = false;
  fetchTypeList();
};

// 新增字典类型
const handleAdd = () => {
  state.currentId = 0;
  state.editVisible = true;
};

// 修改字典类型
const handleUpdate = (type: any) => {
  state.currentId = type.dictId;
  state.editVisible = true;
};

// 删除字典类型
const handleDelete = (type: any) => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `是否确认删除字典名称为"${type.dictName}"的数据项？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      delType(type.dictId).then(() => {
        Message.success("删除成功");
        searchType();
      });
    },
  });
};

// 点击选择字典类型
const handleDictTypeClick = (type: any) => {
  emits('select', type);
};

</script>

<style scoped lang="less">

.is-active {
}

.is-active:active {
  background-color: #f2f3f5;
  color: #165dff;
}

.is-active:focus {
  background-color: #f2f3f5;
  color: #165dff;
}
</style>
