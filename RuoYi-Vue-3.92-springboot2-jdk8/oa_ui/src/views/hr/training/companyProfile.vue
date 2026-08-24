<template>
  <div class="divBox">
    <ueditorFrom
      :border="true"
      :height="height"
      :training="true"
      ref="ueditorFrom"
      :main-width="`1000px`"
      :editor-border="false"
      :placeholder="`请输入公司介绍`"
      :content="content"
    />

    <div class="cr-bottom-button btn-shadow">
      <el-button size="small" :loading="loading" type="primary" @click="handleConfirm()">保存</el-button>
    </div>
  </div>
</template>
<script>
import { employeeTrainApi, getEmployeeTrainApi } from '@/api/config.js'
export default {
  name: '',
  components: { ueditorFrom: () => import('@/components/form-common/oa-wangeditor') },
  data() {
    return {
      height: 'calc(100vh - 200px)',
      content: '',
      loading: false
    }
  },

  mounted() {
    this.getConent()
  },
  methods: {
    async getConent() {
      let type = 'company_profile'
      const result = await getEmployeeTrainApi(type)
      this.content = result.data.content
    },
    handleConfirm() {
      this.content = this.$refs.ueditorFrom.getValue()
      if (this.content == '') {
        return this.$message.error('内容不能为空')
      }
      this.loading = true
      let type = 'company_profile'
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
// .main {
//   max-width: 800px;
//   margin: 0 auto;
//   margin-top: 20px;
// }
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
