<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统管理', '角色管理', '分配用户']"/>
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
                  <a-button type="primary" @click="fetchUserData">
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
        <!-- 工具栏 -->
        <a-row style="margin-bottom: 16px">
          <!-- 前置工具栏部分 -->
          <a-col :span="12">
            <a-space>
              <a-button size="small" type="primary" @click="handleAddUser">
                <template #icon>
                  <icon-plus/>
                </template>
                添加用户
              </a-button>
              <a-button size="small" status="warning" @click="handleBatchCancelAuthUser">
                <template #icon>
                  <icon-delete/>
                </template>
                批量取消授权
              </a-button>
              <a-button size="small" @click="close">
                <template #icon>
                  <icon-close/>
                </template>
                关闭
              </a-button>
            </a-space>
          </a-col>
        </a-row>
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
        <template #optional="{ record }">
          <a-button-group>
            <a-button type="text" size="mini" @click="handleCancelAuthUser(record)">
              <template #icon>
                <icon-delete/>
              </template>
              取消授权
            </a-button>
          </a-button-group>
        </template>
      </a-table>
    </a-card>
  </div>
  <select-unallocated-user
      v-model:visible="state.visible" :role-id="state.queryParams.roleId" @refresh-data-list="fetchUserData">
  </select-unallocated-user>
</template>

<script lang="ts">
import useLoading from '@/hooks/loading';
import {computed, reactive} from 'vue';
import {allocatedUserList, authUserCancel, authUserCancelAll} from "@/api/system/role";
import {useRouter} from "vue-router";
import {TagProps} from "@/store/modules/tab-bar/types";
import {useTabBarStore} from "@/store";
import {Message, Modal} from "@arco-design/web-vue";
import {UserRecord} from "@/api/system/user";
import {TableColumnData} from "@arco-design/web-vue/es/table/interface";
import SelectUnallocatedUser from "./select-unallocated-user.vue";

export default {
  name: 'EditAuthUser',
  components: {SelectUnallocatedUser},
  setup() {
    const {loading, setLoading} = useLoading(true);
    const state = reactive({
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0,
      },
      queryParams: {
        userName: undefined,
        phonenumber: undefined,
        roleId: undefined,
      },
      columns: computed<TableColumnData[]>(() => [
        {
          title: '用户名称',
          dataIndex: 'userName',
          width: 150,
        },
        {
          title: '用户昵称',
          dataIndex: 'nickName',
          width: 150,
        },
        {
          title: '邮箱',
          dataIndex: 'email',
          slotName: 'email',
          width: 180,
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
          width: 120,
        },
        {
          title: '创建时间',
          dataIndex: 'createTime',
          slotName: 'createTime',
          width: 180,
        },
        {
          title: '操作',
          dataIndex: 'optional',
          slotName: 'optional',
          fixed: 'right',
          width: 200,
        },
      ]),
      renderData: [],
      selectedKeys: [],
      rowSelection: {
        type: 'checkbox',
        showCheckedAll: true,
        onlyCurrent: false,
      } as unknown,
      // 选择用户弹窗
      visible: false,
    });

    const router = useRouter();
    state.queryParams.roleId = router.currentRoute.value.params.roleId as any;

    // 查询列表
    const fetchUserData = async () => {
      setLoading(true);
      const {rows, total} = await allocatedUserList({
        ...state.pagination,
        ...state.queryParams
      })
      state.renderData = rows;
      state.pagination.total = total;
      setLoading(false);
    };
    fetchUserData()

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
        roleId: undefined,
      };
    };

    // 分页切换
    const onPageChange = (current: number) => {
      state.pagination.current = current;
      fetchUserData();
    };

    // 添加用户
    const handleAddUser = () => {
      state.visible = true;
    }

    // 取消授权
    const handleCancelAuthUser = async (row: UserRecord) => {
      Modal.confirm({
        titleAlign: 'start',
        title: '系统提示',
        content: `确认要取消该用户"${row.userName}"的角色吗？`,
        okText: '确定',
        cancelText: '取消',
        onOk: async () => {
          setLoading(true);
          const {code} = await authUserCancel({
            userId: row.userId,
            roleId: state.queryParams.roleId
          });
          if (code === 200) {
            Message.success("取消授权成功");
            await fetchUserData();
          }
          setLoading(false);
        },
      })
    };

    // 批量取消授权
    const handleBatchCancelAuthUser = async () => {
      const userIds = state.selectedKeys.join(",");
      const {roleId} = state.queryParams;
      Modal.confirm({
        titleAlign: 'start',
        title: '系统提示',
        content: '是否取消选中用户授权数据项？',
        okText: '确定',
        cancelText: '取消',
        onOk: async () => {
          setLoading(true);
          const {code, msg} = await authUserCancelAll({
            userIds,
            roleId
          });
          if (code === 200) {
            Message.success("取消授权成功");
            await fetchUserData();
          } else {
            Message.error({
              content: msg || '操作失败',
              duration: 3 * 1000,
            });
          }
          setLoading(false);
        }
      })
    };

    const tabBarStore = useTabBarStore();
    // 关闭按钮
    const close = () => {
      const tabList = tabBarStore.getTabList;
      const tag = tabList.find((x) => x.name === router.currentRoute.value.name) as TagProps
      const idx = tabList.findIndex((x) => x.name === tag.name)
      tabBarStore.deleteTag(idx, tag);
      const latest = tabList[idx - 1]; // 获取队列的前一个tab
      router.push({name: latest.name});
    };

    return {
      loading,
      state,
      fetchUserData,
      resetQueryForm,
      onPageChange,
      handleAddUser,
      handleCancelAuthUser,
      handleBatchCancelAuthUser,
      close,
    }
  },
}
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}
</style>
