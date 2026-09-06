<template>
  <ElButton v-if="hasAuth(`${permission}:add`)" @click="visible = true">快速新增溜井</ElButton>
  <ElDialog v-model="visible" title="快速新增溜井" width="560px">
    <ArtForm
      ref="formRef"
      v-model="form"
      :items="items"
      :rules="rules"
      :show-submit="false"
      :show-reset="false"
    />
    <template #footer>
      <ElButton @click="visible = false">取消</ElButton>
      <ElButton type="primary" :loading="saving" @click="submit">保存</ElButton>
    </template>
  </ElDialog>
</template>

<script setup lang="ts">
  import ArtForm from '@/components/core/forms/art-form/index.vue'
  import { addChute } from '@/api/device'
  import { useAuth } from '@/hooks/core/useAuth'
  import type { Entity } from '@/api/system/types'

  const props = defineProps<{
    permission: string
    deptOptions: Entity[]
  }>()
  const emit = defineEmits<{ created: [] }>()
  const { hasAuth } = useAuth()
  const visible = ref(false)
  const saving = ref(false)
  const formRef = ref<{ validate: () => Promise<boolean> }>()
  const form = reactive<Entity>({ status: '0' })
  const rules = {
    chuteName: [{ required: true, message: '请输入溜井名称', trigger: 'blur' }],
    chuteCode: [{ required: true, message: '请输入溜井编码', trigger: 'blur' }],
    chuteHeight: [{ required: true, message: '请输入溜井高度', trigger: 'blur' }],
    deptId: [{ required: true, message: '请选择所属部门', trigger: 'change' }]
  }
  const items = computed(() => [
    { key: 'chuteName', label: '溜井名称', type: 'input', span: 12 },
    { key: 'chuteCode', label: '溜井编码', type: 'input', span: 12 },
    { key: 'chuteHeight', label: '溜井高度', type: 'number', span: 12, props: { min: 0 } },
    {
      key: 'deptId',
      label: '所属部门',
      type: 'treeselect',
      span: 12,
      props: {
        data: props.deptOptions,
        nodeKey: 'deptId',
        valueKey: 'deptId',
        props: { value: 'deptId', label: 'deptName', children: 'children' },
        checkStrictly: true
      }
    },
    { key: 'location', label: '安装位置', type: 'input', span: 24 },
    { key: 'remark', label: '备注', type: 'input', span: 24, props: { type: 'textarea', rows: 3 } }
  ])

  async function submit() {
    await formRef.value?.validate()
    saving.value = true
    try {
      await addChute(form)
      Object.keys(form).forEach((key) => delete form[key])
      Object.assign(form, { status: '0' })
      visible.value = false
      emit('created')
    } finally {
      saving.value = false
    }
  }
</script>
