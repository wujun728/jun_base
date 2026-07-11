<template>
  <el-dialog title="在线接口预览" :visible.sync="visible" width="800px" append-to-body :close-on-click-modal="false">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="Mapper XML" name="xml">
        <pre class="code-preview">&lt;{{ data.tag }} id="{{ data.tagId }}"<span v-if="data.parameterType"> parameterType="{{ data.parameterType }}"</span><span v-if="data.resultMap"> resultMap="{{ data.resultMap }}"</span><span v-if="data.resultType"> resultType="{{ data.resultType }}"</span>&gt;
  {{ data.sqlText }}
&lt;/{{ data.tag }}&gt;</pre>
      </el-tab-pane>
      <el-tab-pane label="API调用" name="api">
        <pre class="code-preview">import request from '@/utils/request'

export function {{ data.tagId }}(<span v-if="data.method === 'GET' || data.method === 'DELETE'">params</span><span v-else>data</span>) {
  return request({
    url: '/online/api{{ data.path }}',
    method: '{{ data.method }}'<span v-if="data.method === 'GET' || data.method === 'DELETE'">,
    params</span><span v-else>,
    data</span>
  })
}</pre>
      </el-tab-pane>
      <el-tab-pane label="在线测试" name="test">
        <el-form :inline="true" label-width="80px">
          <el-form-item label="请求参数">
            <el-input v-model="testParams" type="textarea" :rows="4" placeholder='{"key": "value"}' style="width:400px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-video-play" @click="runTest" :loading="testing">执行测试</el-button>
          </el-form-item>
        </el-form>
        <el-divider />
        <div v-if="testResult !== null" class="test-result">
          <h4>响应结果:</h4>
          <pre>{{ formattedResult }}</pre>
        </div>
      </el-tab-pane>
    </el-tabs>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import request from '@/utils/request'

export default {
  name: "OnlinePreview",
  data() {
    return {
      visible: false,
      activeTab: 'xml',
      data: {},
      testParams: '',
      testResult: null,
      testing: false
    }
  },
  computed: {
    formattedResult() {
      if (this.testResult === null) return ''
      try {
        return JSON.stringify(this.testResult, null, 2)
      } catch {
        return String(this.testResult)
      }
    }
  },
  methods: {
    open(row) {
      this.data = row
      this.activeTab = 'xml'
      this.testParams = ''
      this.testResult = null
      this.visible = true
    },
    runTest() {
      let params = {}
      if (this.testParams.trim()) {
        try {
          params = JSON.parse(this.testParams)
        } catch (e) {
          this.$modal.msgError('参数JSON格式错误')
          return
        }
      }
      this.testing = true
      this.testResult = null
      const config = {
        url: '/online/api' + this.data.path,
        method: this.data.method.toLowerCase()
      }
      if (['get', 'delete'].includes(config.method)) {
        config.params = params
      } else {
        config.data = params
      }
      request(config).then(response => {
        this.testResult = response
        this.testing = false
      }).catch(error => {
        this.testResult = { error: error.message || '请求失败' }
        this.testing = false
      })
    }
  }
}
</script>

<style scoped>
.code-preview {
  background: #f5f7fa;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
  font-family: Consolas, Monaco, 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.6;
  white-space: pre-wrap;
  color: #303133;
  max-height: 400px;
  overflow: auto;
}
.test-result {
  background: #f5f7fa;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
  max-height: 300px;
  overflow: auto;
}
.test-result pre {
  margin: 0;
  font-family: Consolas, Monaco, 'Courier New', monospace;
  font-size: 13px;
  white-space: pre-wrap;
}
</style>
