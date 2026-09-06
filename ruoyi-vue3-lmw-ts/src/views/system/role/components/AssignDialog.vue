<script setup lang="ts">
import getAssignConfig from '../config/assignConfig.ts'
import { deptTreeSelect, dataScope } from '@/api/system/role'
import getComputedConfig from '@/hooks/getPageConfig'
import to from '@/utils/to'
import { nextTick } from 'vue'
import type { TreeInstance } from 'element-plus'
type Props = {
  infoInit: anyObj
  roleId: number
}
const props = defineProps<Props>()
const modelValue = defineModel<boolean>('modelValue')
const emits = defineEmits(['commitClick'])
const baseFormRef = useTemplateRef('baseFormRef')
const treeSelectInfo = ref([])
const dictMap = {
  deptIds: treeSelectInfo,
}
const formHideItems = ref(['deptIds'])
const dataScopeChange = (newValue: string) => {
  if (newValue !== '2') {
    formHideItems.value = ['deptIds']
  } else {
    formHideItems.value = []
    setTreeData()
  }
}
const listeners = {
  dataScopeChange,
}
const assignConfig = getAssignConfig(listeners)

const assignConfigComputed = computed(() => {
  const config = getComputedConfig(assignConfig, dictMap)
  config.hideItems = formHideItems
  return config
})

const formData = ref<anyObj>({})

watch(
  () => props.infoInit,
  (newValue) => {
    dataScopeChange(props.infoInit.dataScope)
    if (Object.keys(props.infoInit).length) {
      for (const item of assignConfig.formItems) {
        formData.value[`${item.field}`] = newValue[`${item.field}`]
      }
    }
  }
)

const loading = ref(false)

const commitClick = async () => {
  loading.value = true
  const deptIds = getTreeData()
  const data = {
    ...formData.value,
    deptIds,
    roleId: props.infoInit.roleId,
  }
  const [res] = await to(dataScope(data))
  if (res) {
    modelValue.value = false
    emits('commitClick')
  }
  loading.value = false
}
const handleCancel = () => {
  modelValue.value = false
}
const checkedKeys = ref([])
const getDeptTree = async () => {
  const [res] = await to(deptTreeSelect(props.roleId))
  if (res) {
    treeSelectInfo.value = res.depts
    checkedKeys.value = res.checkedKeys
    setTreeData()
  }
}

const setTreeData = () => {
  if (Array.isArray(checkedKeys.value)) {
    nextTick(() => {
      checkedKeys.value.forEach((item) => {
        const treeRef = baseFormRef.value?.allRefs?.deptIds as TreeInstance
        treeRef.setChecked(item, true, false)
      })
    })
  }
}

const getTreeData = () => {
  const treeRef = baseFormRef.value?.allRefs?.deptIds as TreeInstance
  if (treeRef) {
    let checkedKeys = treeRef.getCheckedKeys()
    let halfCheckedKeys = treeRef.getHalfCheckedKeys()
    checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
    return checkedKeys
  } else {
    return []
  }
}

const dialogOpen = () => {
  getDeptTree()
}
</script>
<template>
  <div class="cancelDialog">
    <el-dialog
      v-model="modelValue"
      :width="getWidth('600px')"
      top="10vh"
      title="分配数据权限"
      @open="dialogOpen"
    >
      <BaseForm
        ref="baseFormRef"
        v-bind="assignConfigComputed"
        :data="formData"
      ></BaseForm>
      <template #footer>
        <el-button :loading="loading" @click="handleCancel"> 取消 </el-button>
        <el-button type="primary" @click="commitClick" :loading="loading">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.cancelDialog {
  :deep(.el-pagination) {
    padding-top: 20px;
  }
}
</style>
