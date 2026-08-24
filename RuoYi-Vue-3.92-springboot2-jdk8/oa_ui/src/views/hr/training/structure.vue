<template>
  <div class="divBox">
    <ueditorFrom
      :border="true"
      :height="height"
      :training="true"
      :main-width="`1000px`"
      :editor-border="false"
      ref="ueditorFrom"
      :placeholder="`支持上传组织架构图`"
      :content="content"
    />
    <div class="cr-bottom-button btn-shadow">
      <el-button size="small" :loading="loading" type="primary" @click="handleConfirm()">保存</el-button>
    </div>
  </div>
</template>
<script>
import ueditorFrom from '@/components/form-common/oa-wangeditor'
import { employeeTrainApi, getEmployeeTrainApi } from '@/api/config.js'
export default {
  name: '',
  components: { ueditorFrom },
  props: {},
  data() {
    return {
      height: 'calc(100vh - 250px)',
      content: '',
      loading: false
    }
  },

  mounted() {
    this.getConent()
  },
  methods: {
    async getConent() {
      let type = 'organization_chart'
      const result = await getEmployeeTrainApi(type)
      this.content = result.data.content
    },
    handleConfirm() {
      this.content = this.$refs.ueditorFrom.getValue()
      if (this.content == '') {
        return this.$message.error('内容不能为空')
      }
      this.loading = true
      let type = 'organization_chart'
      let data = {
        content: this.content
      }
      employeeTrainApi(type, data)
        .then((res) => {
          this.loading = false
        })
        .catch((err) => {
          this.loading = false
        })
    }
  }
}
</script>
<style scoped lang="scss">
.cr-bottom-button {
  position: fixed;
  left: -20px;
  right: 0;
  bottom: 0;
  width: calc(100% + 220px);
}
.divBox {
  margin: 0 !important;
  padding: 0;
}
</style>
