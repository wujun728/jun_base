<script setup>
import BaseForm from '@/BaseComponent/BaseForm'
import getAssignConfig from '../config/assignConfig'
import { deptTreeSelect, dataScope } from '@/api/system/role'
import getComputedConfig from '@/hooks/getPageConfig'
import to from '@/utils/to'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  infoInit: {
    type: Object,
    default: () => ({}),
  },
  roleId: {
    type: [String, Number],
    default: '',
  },
})

const emits = defineEmits(['update:modelValue', 'commitClick'])

const baseFormRef = useTemplateRef('baseFormRef')
const treeSelectInfo = ref([])
const formHideItems = ref(['deptIds'])
const formData = ref({})
const loading = ref(false)
const checkedKeys = ref([])

const dictMap = {
  deptIds: treeSelectInfo,
}

const dataScopeChange = (newValue) => {
  if (newValue !== '2') {
    formHideItems.value = ['deptIds']
  } else {
    formHideItems.value = []
    setTreeData()
  }
}

const assignConfig = getAssignConfig({
  dataScopeChange,
})

const assignConfigComputed = computed(() => {
  const config = getComputedConfig(assignConfig, dictMap)
  config.hideItems = formHideItems
  return config
})

const open = computed({
  get: () => props.modelValue,
  set: (value) => emits('update:modelValue', value),
})

watch(
  () => props.infoInit,
  (newValue) => {
    if (!newValue || !Object.keys(newValue).length) {
      return
    }
    dataScopeChange(newValue.dataScope)
    assignConfig.formItems.forEach((item) => {
      formData.value[item.field] = newValue[item.field]
    })
  },
  { deep: true }
)

watch(open, (visible) => {
  if (visible) {
    getDeptTree()
  }
})

const normalizeCheckedKeys = (keys) => ({
  checked: keys,
  halfChecked: [],
})

const setTreeData = () => {
  nextTick(() => {
    if (!Array.isArray(checkedKeys.value)) {
      return
    }
    formData.value.deptIds = normalizeCheckedKeys(checkedKeys.value)
  })
}

const getTreeData = () => {
  const deptIds = formData.value.deptIds
  if (!deptIds) {
    return []
  }
  if (Array.isArray(deptIds)) {
    return deptIds
  }
  const { checked = [], halfChecked = [] } = deptIds
  return [...halfChecked, ...checked]
}

const getDeptTree = async () => {
  const [res] = await to(deptTreeSelect(props.roleId))
  if (res) {
    treeSelectInfo.value = res.depts
    checkedKeys.value = res.checkedKeys
    setTreeData()
  }
}

const handleCancel = () => {
  open.value = false
}

const commitClick = async () => {
  loading.value = true
  const data = {
    ...formData.value,
    deptIds: getTreeData(),
    roleId: props.infoInit.roleId,
  }
  const [res] = await to(dataScope(data))
  if (res) {
    open.value = false
    emits('commitClick')
  }
  loading.value = false
}

const isSmall = window.isSmallScreen
</script>

<template>
  <a-modal
    v-model:open="open"
    title="分配数据权限"
    :width="getWidth('600px')"
    :mask-closable="false"
    :destroy-on-close="true"
    :wrap-class-name="isSmall ? 'page-dialog-fullscreen' : ''"
  >
    <BaseForm
      ref="baseFormRef"
      v-bind="assignConfigComputed"
      :data="formData"
    />
    <template #footer>
      <a-button :loading="loading" @click="handleCancel">取消</a-button>
      <a-button type="primary" :loading="loading" @click="commitClick">
        保存
      </a-button>
    </template>
  </a-modal>
</template>
