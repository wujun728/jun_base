<template>
  <SystemCrudPage
    title="岗位"
    permission="system:post"
    id-key="postId"
    :fields="fields"
    :list-fn="fetchPostList"
    :get-fn="fetchPost"
    :add-fn="addPost"
    :update-fn="updatePost"
    :remove-fn="removePost"
    export-url="/system/post/export"
    :defaults="{ status: '0', postSort: 0 }"
  />
</template>
<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import { addPost, fetchPost, fetchPostList, removePost, updatePost } from '@/api/system/post'
  import { useDict } from '@/hooks/core/useDict'
  defineOptions({ name: 'SystemPost' })
  const { dict } = useDict('sys_normal_disable')
  const fields = computed(() => [
    { prop: 'postCode', label: '岗位编码', required: true, search: true },
    { prop: 'postName', label: '岗位名称', required: true, search: true },
    { prop: 'postSort', label: '岗位排序', type: 'number', required: true },
    {
      prop: 'status',
      label: '状态',
      type: 'select',
      options: dict.sys_normal_disable,
      dict: dict.sys_normal_disable,
      search: true
    },
    { prop: 'remark', label: '备注', table: false }
  ])
</script>
