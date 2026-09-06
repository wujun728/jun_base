<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="公告"
      permission="system:notice"
      id-key="noticeId"
      :fields="fields"
      :list-fn="fetchNoticeList"
      :get-fn="fetchNotice"
      :add-fn="addNotice"
      :update-fn="updateNotice"
      :remove-fn="removeNotice"
      :actions="actions"
      :defaults="{ noticeType: '1', status: '0' }"
      drawer-size="960px"
    />
    <ElDrawer v-model="visible" title="已读用户" size="960px">
      <ElTable :data="readUsers"
        ><ElTableColumn prop="userName" label="用户名称" /><ElTableColumn
          prop="nickName"
          label="用户昵称" /><ElTableColumn prop="deptName" label="部门" /><ElTableColumn
          prop="readTime"
          label="阅读时间"
      /></ElTable>
    </ElDrawer>
  </div>
</template>
<script setup lang="ts">
  import { h } from 'vue'
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import ArtWangEditor from '@/components/core/forms/art-wang-editor/index.vue'
  import {
    addNotice,
    fetchNotice,
    fetchNoticeList,
    fetchNoticeReadUsers,
    removeNotice,
    updateNotice
  } from '@/api/system/notice'
  import { useDict } from '@/hooks/core/useDict'
  defineOptions({ name: 'SystemNotice' })
  const { dict } = useDict('sys_notice_type', 'sys_notice_status')
  const visible = ref(false),
    readUsers = ref<Record<string, any>[]>([])
  const fields = computed(() => [
    { prop: 'noticeTitle', label: '公告标题', required: true, search: true },
    {
      prop: 'noticeType',
      label: '公告类型',
      type: 'radiogroup',
      options: dict.sys_notice_type,
      dict: dict.sys_notice_type,
      required: true,
      search: true
    },
    {
      prop: 'status',
      label: '状态',
      type: 'select',
      options: dict.sys_notice_status,
      dict: dict.sys_notice_status,
      required: true
    },
    { prop: 'createBy', label: '创建者', form: false },
    { prop: 'createTime', label: '创建时间', form: false },
    {
      prop: 'noticeContent',
      label: '公告内容',
      table: false,
      required: true,
      span: 24,
      render: () => h(ArtWangEditor)
    }
  ])
  const actions = [
    {
      key: 'read',
      label: '已读用户',
      auth: 'system:notice:list',
      icon: 'ri:user-follow-line',
      handler: async (row: Record<string, any>) => {
        readUsers.value = (
          await fetchNoticeReadUsers({ noticeId: row.noticeId, pageNum: 1, pageSize: 100 })
        ).rows
        visible.value = true
      }
    }
  ]
</script>
