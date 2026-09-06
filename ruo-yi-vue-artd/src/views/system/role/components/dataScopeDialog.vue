<template>
  <el-dialog
    title="分配数据权限"
    v-model="visible"
    width="500px"
    append-to-body
    @close="handleClose">
    <ArtForm
      ref="formRef"
      v-model="formData"
      :items="formItems"
      :show-submit="false"
      :show-reset="false"
      label-width="80px">
      <template #deptTree>
        <div>
          <el-checkbox v-model="deptExpand" @change="handleCheckedTreeExpand">
            展开/折叠
          </el-checkbox>
          <el-checkbox v-model="deptNodeAll" @change="handleCheckedTreeNodeAll">
            全选/全不选
          </el-checkbox>
          <el-checkbox
            v-model="formData.deptCheckStrictly"
            @change="handleCheckedTreeConnect">
            父子联动
          </el-checkbox>
          <el-tree
            class="tree-border"
            :data="deptOptions"
            show-checkbox
            default-expand-all
            ref="deptRef"
            node-key="id"
            :check-strictly="!formData.deptCheckStrictly"
            empty-text="加载中，请稍候"
            :props="{ label: 'label', children: 'children' }" />
        </div>
      </template>
    </ArtForm>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancelDataScope">取 消</el-button>
        <el-button type="primary" @click="submitDataScope">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import type { TreeSelect } from '@/types/api'
import type { SysRole } from '@/types/api/system/role'
import type { CheckboxValueType, ElTree } from 'element-plus'

import { ElMessage } from 'element-plus'

import ArtForm from '@/components/core/forms/art-form/index.vue'

import { deptTreeSelect, dataScope } from '../api'

defineOptions({ name: 'DataScopeDialog' })

const emit = defineEmits<{
  success: []
}>()

const visible = ref(false)
const formData = ref<SysRole>({
  roleId: undefined,
  roleName: undefined,
  roleKey: undefined,
  dataScope: '1',
  deptIds: [],
  deptCheckStrictly: true
})
const deptOptions = ref<TreeSelect[]>([])
const deptExpand = ref(false)
const deptNodeAll = ref(false)
const deptRef = useTemplateRef<InstanceType<typeof ElTree>>('deptRef')

const dataScopeOptions = [
  { value: '1', label: '全部数据权限' },
  { value: '2', label: '自定数据权限' },
  { value: '3', label: '本部门数据权限' },
  { value: '4', label: '本部门及以下数据权限' },
  { value: '5', label: '仅本人数据权限' }
]

const formItems = computed(() => [
  {
    label: '角色名称',
    key: 'roleName',
    type: 'input',
    props: { disabled: true },
    span: 24
  },
  {
    label: '权限字符',
    key: 'roleKey',
    type: 'input',
    props: { disabled: true },
    span: 24
  },
  {
    label: '权限范围',
    key: 'dataScope',
    type: 'select',
    span: 24,
    props: {
      options: dataScopeOptions
    }
  },
  {
    label: '数据权限',
    key: 'deptTree',
    useSlot: true,
    slotName: 'deptTree',
    span: 24,
    hidden: formData.value.dataScope !== '2'
  }
])

/** 打开弹窗 */
async function open(row: SysRole) {
  formData.value = {
    roleId: row.roleId,
    roleName: row.roleName,
    roleKey: row.roleKey,
    dataScope: row.dataScope || '1',
    deptIds: [],
    deptCheckStrictly: row.deptCheckStrictly ?? true
  }
  deptExpand.value = false
  deptNodeAll.value = false
  visible.value = true

  if (row.roleId) {
    const res = await deptTreeSelect(row.roleId)
    deptOptions.value = res.depts
    nextTick(() => {
      if (deptRef.value) {
        deptRef.value.setCheckedKeys(res.checkedKeys)
      }
    })
  }
}

/** 关闭弹窗 */
function handleClose() {
  deptOptions.value = []
}

/** 树权限（展开/折叠） */
function handleCheckedTreeExpand(value: CheckboxValueType) {
  const treeList = deptOptions.value
  for (let i = 0; i < treeList.length; i++) {
    if (deptRef.value) {
      deptRef.value.store.nodesMap[treeList[i].id as number].expanded =
        Boolean(value)
    }
  }
}

/** 树权限（全选/全不选） */
function handleCheckedTreeNodeAll(value: CheckboxValueType) {
  if (deptRef.value) {
    const keys = value ? getAllKeys(deptOptions.value) : []
    deptRef.value.setCheckedKeys(keys)
  }
}

/** 获取所有子节点的 id */
function getAllKeys(nodes: TreeSelect[]): number[] {
  const keys: number[] = []
  const loop = (list: TreeSelect[]) => {
    list.forEach((item) => {
      if (item.id != null) {
        keys.push(item.id)
      }
      if (item.children?.length) {
        loop(item.children)
      }
    })
  }
  loop(nodes)
  return keys
}

/** 树权限（父子联动） */
function handleCheckedTreeConnect(value: CheckboxValueType) {
  formData.value.deptCheckStrictly = Boolean(value)
}

/** 获取所有选中的部门节点数据 */
function getDeptAllCheckedKeys(): number[] {
  if (!deptRef.value) return []
  const checkedKeys = deptRef.value.getCheckedKeys()
  const halfCheckedKeys = deptRef.value.getHalfCheckedKeys()
  return [...checkedKeys, ...halfCheckedKeys] as number[]
}

/** 提交数据权限 */
async function submitDataScope() {
  const params: SysRole = {
    ...formData.value,
    deptIds: getDeptAllCheckedKeys()
  }
  await dataScope(params)
  ElMessage.success('数据权限分配成功')
  visible.value = false
  emit('success')
}

/** 取消 */
function cancelDataScope() {
  visible.value = false
}

defineExpose({ open })
</script>
