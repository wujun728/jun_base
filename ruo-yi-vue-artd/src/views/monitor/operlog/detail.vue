<template>
  <el-dialog
    title="操作日志详细"
    :model-value="visible"
    width="780px"
    append-to-body
    @update:model-value="emit('update:visible', $event)">
    <div class="detail-wrap">
      <!-- 基本信息 -->
      <div class="detail-card">
        <div class="detail-card-title">
          <el-icon><InfoFilled /></el-icon>
          基本信息
        </div>
        <el-row class="detail-row">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">操作模块</span>
              <span class="detail-value">{{ row.title }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">业务类型</span>
              <span class="detail-value">{{ typeLabel }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row class="detail-row">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">操作时间</span>
              <span class="detail-value">{{ row.operTime }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">执行状态</span>
              <el-tag v-if="row.status === 0" type="success" size="small">
                正常
              </el-tag>
              <el-tag v-else type="danger" size="small">异常</el-tag>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 操作人员 -->
      <div class="detail-card">
        <div class="detail-card-title">
          <el-icon><User /></el-icon>
          操作人员
        </div>
        <el-row class="detail-row">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">操作人员</span>
              <span class="detail-value">{{ row.operName }}</span>
            </div>
          </el-col>
          <el-col v-if="row.deptName" :span="12">
            <div class="detail-item">
              <span class="detail-label">所属部门</span>
              <span class="detail-value">{{ row.deptName }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row class="detail-row">
          <el-col :span="24">
            <div class="detail-item">
              <span class="detail-label">操作地址</span>
              <span class="detail-value">
                {{ row.operIp }}&nbsp;&nbsp;
                <span class="detail-location">{{ row.operLocation }}</span>
              </span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 请求信息 -->
      <div class="detail-card">
        <div class="detail-card-title">
          <el-icon><Sort /></el-icon>
          请求信息
        </div>
        <el-row class="detail-row">
          <el-col :span="24">
            <div class="detail-item">
              <span class="detail-label">请求地址</span>
              <span class="detail-value">
                <span :class="'method-tag method-' + row.requestMethod">
                  {{ row.requestMethod }}
                </span>
                {{ row.operUrl }}
              </span>
            </div>
          </el-col>
        </el-row>
        <el-row class="detail-row">
          <el-col :span="24">
            <div class="detail-item">
              <span class="detail-label">操作方法</span>
              <span class="detail-value mono">{{ row.method }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row class="detail-row">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">消耗时间</span>
              <span class="detail-value">{{ row.costTime }} 毫秒</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 请求参数 -->
      <div class="detail-card">
        <div class="detail-card-title">
          <el-icon><Upload /></el-icon>
          请求参数
        </div>
        <div class="code-body">
          <div class="code-wrap">
            <div class="code-action">
              <el-button
                size="small"
                :icon="CopyDocument"
                @click="copyText(row.operParam)">
                复制
              </el-button>
            </div>
            <pre class="code-pre">{{ formatJson(row.operParam) }}</pre>
          </div>
        </div>
      </div>

      <!-- 返回参数 -->
      <div class="detail-card">
        <div class="detail-card-title">
          <el-icon><Download /></el-icon>
          返回参数
        </div>
        <div class="code-body">
          <div class="code-wrap">
            <div class="code-action">
              <el-button
                size="small"
                :icon="CopyDocument"
                @click="copyText(row.jsonResult)">
                复制
              </el-button>
            </div>
            <pre class="code-pre">{{ formatJson(row.jsonResult) }}</pre>
          </div>
        </div>
      </div>

      <!-- 异常信息 -->
      <div v-if="row.status !== 0" class="detail-card">
        <div class="detail-card-title error-title">
          <el-icon><Warning /></el-icon>
          异常信息
        </div>
        <div class="error-body">
          <div class="error-msg">{{ row.errorMsg }}</div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import type { SysOperLog } from '@/types/api/monitor/operlog'

import {
  CopyDocument,
  Download,
  InfoFilled,
  Sort,
  Upload,
  User,
  Warning
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

import { useDict } from '@/hooks/core/useDict'
import { selectDictLabel } from '@/utils/sys/ruoyi'

defineOptions({ name: 'OperlogDetail' })

const props = withDefaults(
  defineProps<{
    visible: boolean
    row: SysOperLog
  }>(),
  {
    visible: false,
    row: () => ({})
  }
)

const emit = defineEmits<{
  (event: 'update:visible', value: boolean): void
}>()

const { sys_oper_type } = useDict('sys_oper_type')

const typeLabel = computed(
  () => selectDictLabel(sys_oper_type.value, props.row?.businessType) || '-'
)

function formatJson(str?: string): string {
  if (!str) return '（无数据）'
  try {
    return JSON.stringify(JSON.parse(str), null, 2)
  } catch {
    return str
  }
}

async function copyText(str?: string): Promise<void> {
  const text = formatJson(str)
  try {
    if (navigator.clipboard) {
      await navigator.clipboard.writeText(text)
    } else {
      const ta = document.createElement('textarea')
      ta.value = text
      document.body.appendChild(ta)
      ta.select()
      document.execCommand('copy')
      document.body.removeChild(ta)
    }
    ElMessage.success('已复制')
  } catch {
    ElMessage.error('复制失败，请手动复制')
  }
}
</script>

<style scoped lang="scss">
.detail-wrap {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 0 4px;
}

.detail-card {
  padding: 14px 16px;
  border: 1px solid #ebeef5;
  border-radius: 6px;
}

.detail-card-title {
  display: flex;
  gap: 6px;
  align-items: center;
  margin-bottom: 12px;
  color: #303133;
  font-size: 14px;
  font-weight: 600;

  &.error-title {
    color: #f56c6c;
  }
}

.detail-row {
  margin-bottom: 8px;

  &:last-child {
    margin-bottom: 0;
  }
}

.detail-item {
  display: flex;
  align-items: flex-start;
  min-height: 24px;
  font-size: 13px;
}

.detail-label {
  display: inline-block;
  width: 80px;
  flex-shrink: 0;
  color: #909399;
  text-align: right;
}

.detail-value {
  flex: 1;
  color: #303133;
  word-break: break-all;
}

.detail-value.mono {
  font-family: monospace;
  font-size: 12px;
}

.detail-location {
  color: #909399;
  font-size: 12px;
}

.method-tag {
  display: inline-block;
  margin-right: 8px;
  padding: 1px 6px;
  font-size: 11px;
  font-weight: 600;
  border-radius: 3px;
}

.method-GET {
  color: #67c23a;
  background: #f0f9eb;
}

.method-POST {
  color: #409eff;
  background: #ecf5ff;
}

.method-PUT {
  color: #e6a23c;
  background: #fdf6ec;
}

.method-DELETE {
  color: #f56c6c;
  background: #fef0f0;
}

.code-body {
  padding: 8px 0;
}

.code-wrap {
  position: relative;
  background: #fafafa;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

.code-action {
  position: absolute;
  top: 6px;
  right: 6px;
  z-index: 1;
}

.code-pre {
  max-height: 180px;
  padding: 10px 12px;
  margin: 0;
  overflow: auto;
  color: #303133;
  font-family: monospace;
  font-size: 12px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-all;
}

.error-body {
  padding: 8px 12px;
  color: #f56c6c;
  background: #fef0f0;
  border-radius: 4px;
}

.error-msg {
  font-family: monospace;
  font-size: 12px;
  white-space: pre-wrap;
  word-break: break-all;
}
</style>
