<template>
  <a-modal :visible="visible" :title="state.title" width="50vw" @cancel="handleCancel" @ok="handleConfirm">

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
                <a-form-item field="number" :label="'用户名称'">
                  <a-input
                      v-model="state.queryParams.userName"
                      :placeholder="'请输入用户名称'"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="phonenumber" :label="'手机号码'">
                  <a-input
                      v-model="state.queryParams.phonenumber"
                      :placeholder="'请输入手机号码'"
                      maxlength="11"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="8" style="text-align: right">
                <a-space>
                  <a-button type="primary" @click="fetchUnallocatedUserData">
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
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="margin-top: 0"/>
      </a-row>
      <!-- 表格数据-->
      <a-table
          v-model:selectedKeys="state.selectedKeys"
          row-key="userId"
          :loading="loading"
          :pagination="state.pagination"
          :columns="state.columns"
          :data="state.renderData"
          :row-selection="state.rowSelection"
          :bordered="false"
          :size="'medium'"
          @page-change="onPageChange"
      >
        <template #status="{ record }">
          <a-tag :color="record.status === '0' ? 'arcoblue' : 'orangered'">{{
              record.status === '0' ? '正常' : '停用'
            }}
          </a-tag>
        </template>
      </a-table>
    </a-card>
  </a-modal>
</template>

<script lang="ts" setup>
import {reactive, watch} from "vue";
import useLoading from '@/hooks/loading';
import {authUserSelectAll, unallocatedUserList} from "@/api/system/role";
import {Message} from "@arco-design/web-vue";

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  roleId: {
    type: [Number, String],
    default: undefined,
  },
});
const emits = defineEmits(['update:visible', 'refreshDataList']);

const {loading, setLoading} = useLoading(true);
const state = reactive({
  title: "选择用户",
  pagination: {
    current: 1,
    pageSize: 10,
    total: 0,
  },
  queryParams: {
    userName: undefined,
    phonenumber: undefined,
  },
  columns: [
    {
      title: '用户名称',
      dataIndex: 'userName',
      width: 120,
    },
    {
      title: '用户昵称',
      dataIndex: 'nickName',
      width: 120,
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      slotName: 'email',
      width: 150,
    },
    {
      title: '手机号码',
      dataIndex: 'phonenumber',
      width: 150,
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
      width: 180,
    },
  ],
  renderData: [],
  selectedKeys: [],
  rowSelection: {
    type: 'checkbox',
    showCheckedAll: true,
    onlyCurrent: false,
  } as unknown,
});

// 查询未分配用户数据
const fetchUnallocatedUserData = async () => {
  setLoading(true);
  const {rows, total} = await unallocatedUserList({
    ...state.pagination,
    ...state.queryParams,
    roleId: props.roleId,
  })
  state.renderData = rows;
  state.pagination.total = total;
  setLoading(false);
};

// 重置表单
const resetQueryForm = () => {
  state.pagination = {
    current: 1,
    pageSize: 10,
    total: 1,
  };
  state.queryParams = {
    userName: undefined,
    phonenumber: undefined,
  };
};

// 分页切换
const onPageChange = (current: number) => {
  state.pagination.current = current;
  fetchUnallocatedUserData();
};

// 关闭页面
const handleCancel = () => {
  emits('update:visible', false)
};

// 确认
const handleConfirm = async () => {
  const {roleId} = props;
  if (state.selectedKeys.length === 0) {
    Message.error({
      content: '请选择要分配的用户',
      duration: 3 * 1000,
    });
    return;
  }
  const userIds = state.selectedKeys.join(",");
  const {code, msg} = await authUserSelectAll({roleId, userIds})
  if (code === 200) {
    Message.success({
      content: msg,
      duration: 3 * 1000,
    });
    handleCancel();
    emits('refreshDataList')
  } else {
    Message.error({
      content: msg || '操作失败',
      duration: 3 * 1000,
    });
  }
};

// 监听弹窗效果，打开时查询用户数据
watch(() => props.visible, (val) => {
  if (val) {
    fetchUnallocatedUserData()
  }
})
</script>

<style scoped>

</style>
