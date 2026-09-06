<template>
  <div>
    <a-modal
      v-model:open="visible"
      title="选择用户"
      width="90%"
      :destroyOnClose="true"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <!-- 使用 ProTable 组件 -->
      <ProTable
        ref="proTableRef"
        :api="tableApi"
        :columns="columns"
        :initialSearchParams="initialSearchParams"
        :searchFields="searchFields"
        rowKey="userId"
        :row-selection="{
          selectedRowKeys,
          onChange: onSelectChange,
          getCheckboxProps: getCheckboxProps
        }"
        :pagination="false"
        :showToolbar="false"
        :showTableSetting="false"
        :showRefresh="false"
        :showDensity="false"
        :showColumnSelector="false"
        :showFullscreen="false"
        :showSearch="true"
        :showReset="true"
        :searchLabelWidth="80"
        :searchCol="3"
        :searchGutter="16"
        :searchLayout="'horizontal'"
        :searchStyle="{ padding: '16px', background: '#fafafa', borderRadius: '6px', marginBottom: '16px' }"
      >
        <!-- 自定义列渲染 -->
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <dict-tag :options="statusOptions" :value="record.status" />
          </template>
        </template>
      </ProTable>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { message } from 'ant-design-vue'
import { unallocatedUserList, authUserSelectAll } from '@/api/system/role'
import DictTag from '@/components/DictTag/index.vue'
import ProTable from '@/components/BearJiaProTable/index.vue'

// Props
const props = defineProps({
  roleId: {
    type: [String, Number],
    required: true
  },
  statusOptions: {
    type: Array,
    default: () => []
  }
})

// Emits
const emit = defineEmits(['ok'])

// 响应式数据
const visible = ref(false)
const selectedRowKeys = ref([])
const selectedRows = ref([])
const proTableRef = ref()

// ProTable 配置
const tableApi = {
  list: async (params) => {
    const response = await unallocatedUserList({
      ...params,
      roleId: props.roleId
    })
    return response
  }
}

const initialSearchParams = reactive({
  userName: '',
  phonenumber: ''
})

const searchFields = computed(() => [
  { name: 'userName', label: '用户名称', type: 'input', placeholder: '请输入用户名称' },
  { name: 'phonenumber', label: '手机号码', type: 'input', placeholder: '请输入手机号码' }
])

// 表格列定义
const columns = [
  { title: '用户名称', dataIndex: 'userName', key: 'userName', width: 120 },
  { title: '用户昵称', dataIndex: 'nickName', key: 'nickName', width: 120 },
  { title: '手机号码', dataIndex: 'phonenumber', key: 'phonenumber', width: 130 },
  { title: '邮箱', dataIndex: 'email', key: 'email', width: 180 },
  { title: '状态', dataIndex: 'status', key: 'status', width: 80 },
  { title: '创建时间', dataIndex: 'createTime', key: 'createTime', width: 180 }
]

// 获取用户列表（通过 ProTable 自动处理）
const getUserList = () => {
  proTableRef.value?.refresh()
}

// 选择变化
const onSelectChange = (keys, rows) => {
  selectedRowKeys.value = keys
  selectedRows.value = rows
}

// 获取复选框属性
const getCheckboxProps = (record) => ({
  disabled: record.status === '1' // 禁用状态的用户不可选
})

// 确认选择
const handleOk = async () => {
  if (selectedRowKeys.value.length === 0) {
    message.warning('请选择要授权的用户')
    return
  }

  try {
    const params = {
      roleId: props.roleId,
      userIds: selectedRowKeys.value
    }
    await authUserSelectAll(params)
    message.success('用户授权成功')
    emit('ok')
    handleCancel()
  } catch (error) {
    console.error('用户授权失败:', error)
    message.error('用户授权失败')
  }
}

// 取消
const handleCancel = () => {
  visible.value = false
  selectedRowKeys.value = []
  selectedRows.value = []
  // 重置搜索参数
  Object.assign(initialSearchParams, {
    userName: '',
    phonenumber: ''
  })
}

// 打开模态框
const handleAuthUser = () => {
  visible.value = true
  getUserList()
}

// 暴露方法给父组件
defineExpose({
  handleAuthUser
})


</script>

<style scoped>
.search-wrapper {
  background: #fafafa;
  padding: 16px;
  border-radius: 6px;
}
</style>
