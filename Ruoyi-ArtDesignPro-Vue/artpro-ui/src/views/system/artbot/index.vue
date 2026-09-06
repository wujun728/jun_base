<template>
  <div class="art-full-height">
    <SystemCrudPage
      ref="page"
      title="AI模型"
      permission="system:artbot"
      id-key="modelId"
      :fields="fields"
      :list-fn="fetchArtBotModelList"
      :get-fn="fetchArtBotModel"
      :add-fn="addArtBotModel"
      :update-fn="updateArtBotModel"
      :remove-fn="removeArtBotModel"
      :status-fn="updateArtBotModel"
      :actions="actions"
      :defaults="defaults"
      drawer-size="760px"
    />
  </div>
</template>

<script setup lang="ts">
  import { ElMessage } from 'element-plus'
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addArtBotModel,
    fetchArtBotModel,
    fetchArtBotModelList,
    removeArtBotModel,
    setDefaultArtBotModel,
    testArtBotModel,
    updateArtBotModel
  } from '@/api/system/artbot'

  defineOptions({ name: 'SystemArtBot' })
  const page = ref<{ refreshData: () => void }>()
  const statusOptions = [
    { label: '正常', value: '0' },
    { label: '停用', value: '1' }
  ]
  const yesNoOptions = [
    { label: '否', value: '0' },
    { label: '是', value: '1' }
  ]
  const defaults = {
    status: '0',
    isDefault: '0',
    temperature: 0.7,
    maxTokens: 2048,
    systemPrompt: '你是一个严谨、简洁的中文 AI 助手。'
  }
  const fields = computed(() => [
    { prop: 'modelName', label: '名称', required: true, search: true },
    { prop: 'modelCode', label: '模型标识', required: true, search: true },
    { prop: 'baseUrl', label: 'API Base URL', required: true, minWidth: 220 },
    {
      prop: 'apiKey',
      label: 'API Key',
      table: false,
      required: true,
      props: { type: 'password', showPassword: true, autocomplete: 'new-password' }
    },
    {
      prop: 'status',
      label: '状态',
      type: 'select',
      options: statusOptions,
      dict: statusOptions,
      required: true,
      search: true
    },
    {
      prop: 'isDefault',
      label: '默认模型',
      type: 'select',
      options: yesNoOptions,
      dict: yesNoOptions,
      required: true
    },
    { prop: 'temperature', label: 'Temperature', type: 'number', table: false },
    { prop: 'maxTokens', label: '最大 Token', type: 'number', table: false },
    {
      prop: 'systemPrompt',
      label: '系统提示词',
      table: false,
      props: { type: 'textarea', rows: 4 }
    },
    { prop: 'remark', label: '备注', table: false, props: { type: 'textarea', rows: 3 } },
    { prop: 'createTime', label: '创建时间', form: false, minWidth: 170 }
  ])
  const actions = [
    {
      key: 'default',
      label: '设为默认',
      auth: 'system:artbot:edit',
      icon: 'ri:star-line',
      handler: async (row: Record<string, any>) => {
        if (row.isDefault === '1') return ElMessage.info('当前已经是默认模型')
        await setDefaultArtBotModel(row.modelId)
        page.value?.refreshData()
      }
    },
    {
      key: 'test',
      label: '测试连接',
      auth: 'system:artbot:test',
      icon: 'ri:link',
      handler: async (row: Record<string, any>) => {
        await testArtBotModel(row.modelId)
      }
    }
  ]
</script>
