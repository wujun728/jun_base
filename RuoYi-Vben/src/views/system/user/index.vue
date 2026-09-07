<template>
  <PageWrapper dense>
    <Row>
      <!-- 左边部门选择 -->
      <Col v-bind="{ xs: 24, sm: 24, md: 24, lg: 4 }" class="h-[calc(100vh-80px)]">
        <Skeleton :active="true" :paragraph="{ rows: 8 }" :loading="showTreeSkeleton">
          <BasicTree
            v-if="data.tree.length"
            :fieldNames="{ title: 'label', key: 'id' }"
            :tree-data="data.tree"
            :showLine="{ showLeafIcon: false }"
            :search="true"
            v-model:searchValue="deptSearchText"
            defaultExpandAll
            @select="handleSelectDept"
            v-model:selectedKeys="data.selectDeptId"
          />
          <!-- 仅本人数据权限 可以考虑直接不显示 -->
          <div class="mt-5" v-else>
            <Empty :image="Empty.PRESENTED_IMAGE_SIMPLE" description="无部门数据" />
          </div>
        </Skeleton>
      </Col>
      <!-- 右边表格及菜单 -->
      <Col v-bind="{ xs: 24, sm: 24, md: 24, lg: 20 }">
        <BasicTable @register="registerTable">
          <template #toolbar>
            <Space class="<sm:hidden">
              <a-button @click="handleImport" v-auth="'system:user:import'">导入</a-button>
              <a-button
                v-auth="'system:user:export'"
                @click="downloadExcel(userExport, '用户数据', getForm().getFieldsValue())"
                >导出</a-button
              >
              <a-button
                class="<sm:hidden"
                type="primary"
                danger
                @click="multipleRemove(userRemove)"
                v-auth="'system:user:remove'"
                :disabled="!selected"
                >删除</a-button
              >
              <a-button type="primary" @click="handleAdd" v-auth="'system:user:add'">新增</a-button>
            </Space>
          </template>
          <template #bodyCell="{ column, record }">
            <template v-if="column.dataIndex === 'avatar'">
              <avatar v-if="record.avatar" :src="apiUrl + record.avatar" />
              <avatar
                v-else
                src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
              />
            </template>
            <template v-if="column.dataIndex === 'status'">
              <TableSwitch
                v-model="record.status"
                :disabled="record.userId === 1 || !hasPermission('system:user:edit')"
                :api="() => userStatusChange(record)"
                :reload="reload"
              />
            </template>
            <template v-if="column.key === 'action'">
              <TableAction
                stopButtonPropagation
                dropDownBtnDisplay
                :actions="[
                  {
                    label: '修改',
                    icon: IconEnum.EDIT,
                    type: 'primary',
                    ghost: true,
                    auth: 'system:user:edit',
                    ifShow: record.userId !== 1,
                    onClick: handleEdit.bind(null, record),
                  },
                  {
                    label: '删除',
                    icon: IconEnum.DELETE,
                    type: 'primary',
                    danger: true,
                    ghost: true,
                    auth: 'system:user:remove',
                    ifShow: record.userId !== 1,
                    popConfirm: {
                      placement: 'left',
                      title: `是否删除用户[${record.userName}]-${record.nickName}?`,
                      confirm: handleDelete.bind(null, record),
                    },
                  },
                ]"
                :dropDownActions="[
                  {
                    label: '用户信息',
                    icon: 'ph:user-light',
                    auth: 'system:user:list',
                    ifShow: record.userId !== 1,
                    onClick: handleUserInfo.bind(null, record),
                  },
                  {
                    label: '重置密码',
                    icon: IconEnum.RESET_PWD,
                    auth: 'system:user:resetPwd',
                    ifShow: record.userId !== 1,
                    onClick: handleResetPassword.bind(null, record),
                  },
                ]"
              />
            </template>
          </template>
        </BasicTable>
      </Col>
    </Row>
    <UserDrawer @register="registerDrawer" @reload="reload" />
    <UserResetPwdModal @register="registerPwdModal" @reload="reload" />
    <UserImportModal @register="registerImportModal" @reload="reload" />
    <UserInfoModal @register="registerInfoModal" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { Row, Col, Space, Empty, Skeleton, Avatar } from 'ant-design-vue';
  import { BasicTree } from '@/components/Tree/index';
  import {
    departmentTree,
    userList,
    userExport,
    userRemove,
    userStatusChange,
  } from '@/api/system/user';
  import type { DeptTree } from '@/api/system/user/model';
  import { onMounted, reactive, ref } from 'vue';
  import { BasicTable, useTable, TableAction, TableSwitch } from '@/components/Table';
  import UserResetPwdModal from './UserResetPwdModal.vue';
  import UserImportModal from './UserImportModal.vue';
  import { useModal } from '@/components/Modal';
  import { downloadExcel } from '@/utils/file/download';
  import { columns, schemas } from './user.data';
  import { IconEnum } from '@/enums/appEnum';
  import UserDrawer from './UserDrawer.vue';
  import { useDrawer } from '@/components/Drawer';
  import UserInfoModal from './UserInfoModal.vue';
  import { eachTree } from '@/utils/helper/treeHelper';
  import { usePermission } from '@/hooks/web/usePermission';
  import { useGlobSetting } from '@/hooks/setting';

  // 缓存
  defineOptions({ name: 'User' });
  const { hasPermission } = usePermission();
  const { apiUrl } = useGlobSetting();

  interface TreeProps {
    tree: DeptTree[];
    selectDeptId: string[];
  }
  /** 左边搜索框 */
  const deptSearchText = ref<string>('');
  const data: TreeProps = reactive({
    tree: [],
    selectDeptId: [],
  });
  /** 骨架屏加载 */
  const showTreeSkeleton = ref<boolean>(true);

  // 选中部门后刷新表格
  async function handleSelectDept() {
    await reload();
  }

  onMounted(async () => {
    const ret = await departmentTree();
    // 添加部门前图标 不需要 注释eachTree即可
    eachTree(ret, (item) => {
      item.icon = 'el:group';
    });
    data.tree = ret;
    showTreeSkeleton.value = false;
  });

  const [registerTable, { reload, selected, multipleRemove, getForm }] = useTable({
    rowSelection: {
      type: 'checkbox',
      getCheckboxProps: (record: Recordable) => ({
        // 内置管理员不可选
        disabled: record.userId === 1,
      }),
    },
    title: '用户列表',
    showIndexColumn: false,
    api: userList,
    rowKey: 'userId',
    useSearchForm: true,
    formConfig: {
      schemas: schemas,
      name: 'user',
      baseColProps: {
        xs: 24,
        sm: 24,
        md: 24,
        lg: 8,
      },
      labelWidth: 100,
      resetFunc: async () => {
        data.selectDeptId = [];
        deptSearchText.value = '';
        await reload();
      },
      // 日期选择格式化
      fieldMapToTime: [
        ['createTime', ['params[beginTime]', 'params[endTime]'], ['YYYY-MM-DD', 'YYYY-MM-DD']],
      ],
    },
    columns,
    // 需要添加上部门参数
    beforeFetch(params: Recordable) {
      if (data.selectDeptId.length === 1) {
        params.deptId = data.selectDeptId[0];
      }
      return params;
    },
    actionColumn: {
      width: 220,
      title: '操作',
      key: 'action',
      fixed: 'right',
    },
  });

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerPwdModal, { openModal: openPwdModal }] = useModal();
  const [registerImportModal, { openModal: openImportModal }] = useModal();
  const [registerInfoModal, { openModal: openInfoModal }] = useModal();

  function handleImport() {
    openImportModal(true);
  }

  function handleResetPassword(record: Recordable) {
    openPwdModal(true, record);
  }

  function handleUserInfo(record: Recordable) {
    const { userId } = record;
    openInfoModal(true, userId);
  }

  function handleEdit(record: Recordable) {
    openDrawer(true, { update: true, record });
  }

  function handleAdd() {
    openDrawer(true, { update: false });
  }

  async function handleDelete(record: Recordable) {
    await userRemove([record.userId]);
    await reload();
  }
</script>

<style lang="less" scoped>
  /** 去掉表格padding */
  :deep(.vben-basic-table-form-container) {
    padding: 5px;
  }
</style>
