<template>
  <el-drawer v-model="visible" title="公告详情" size="60%" append-to-body>
    <el-descriptions :column="1" border label-width="150px">
      <el-descriptions-item label="公告标题">
        {{ row.noticeTitle }}
      </el-descriptions-item>
      <el-descriptions-item label="公告类型">
        <dict-tag :options="sys_notice_type" :value="row.noticeType" />
      </el-descriptions-item>
      <el-descriptions-item label="状态">
        <dict-tag :options="sys_notice_status" :value="row.status" />
      </el-descriptions-item>
      <el-descriptions-item label="创建者">
        {{ row.createBy }}
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">
        {{ row.createTime }}
      </el-descriptions-item>
      <el-descriptions-item label="公告内容">
        <div class="notice-content" v-html="row.noticeContent"></div>
      </el-descriptions-item>
    </el-descriptions>
  </el-drawer>
</template>

<script setup lang="ts">
import type { SysNotice } from '@/types/api/system/notice'

import { useDict } from '@/hooks/core/useDict'

const { sys_notice_status, sys_notice_type } = useDict(
  'sys_notice_status',
  'sys_notice_type'
)

const visible = ref(false)
const row = ref<SysNotice>({})

function open(data: SysNotice) {
  row.value = { ...data }
  visible.value = true
}

defineExpose({ open })
</script>

<style scoped>
.notice-content {
  max-height: 400px;
  overflow-y: auto;
  line-height: 1.8;
}
</style>
