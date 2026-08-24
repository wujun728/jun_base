<!-- 添加客户弹窗组件 -->
<template>
  <div class="station">
    <el-drawer
      :title="formData.title"
      :visible.sync="drawer"
      :direction="direction"
      :modal="true"
      :append-to-body="true"
      :wrapper-closable="false"
      :before-close="handleClose"
      :size="formData.width"
    >
      <oaForm
        :form-info="fromInfo"
        ref="oaForm"
        @handleClose="handleClose"
        :btnShow="false"
        @submitOk="submitOk"
      ></oaForm>
    </el-drawer>
  </div>
</template>
<script>
import { liaisonCreateApi, liaisonSaveApi, liaisonEditCreateApi, liaisonEditSaveApi } from '@/api/enterprise'
export default {
  name: 'liaisonDialog',
  props: {
    formData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  components: {
    oaForm: () => import('@/components/customer/oaForm')
  },
  data() {
    return {
      drawer: false,
      direction: 'rtl',
      fromInfo: [],
      row: {},
      customInfo: {},
      contractFromData: {},
      edit: ''
    }
  },

  methods: {
    // 新增联系人表单
    getData() {
      liaisonCreateApi().then((res) => {
        this.fromInfo = res.data
      })
    },
    handleClose() {
      this.drawer = false
      this.$refs.oaForm.resetForm()
    },

    // 编辑联系人表单
    getEditData(id) {
      liaisonEditCreateApi(id).then((res) => {
        this.fromInfo = res.data
      })
    },

    // 提交成功
    submitOk(data) {
      data.eid = this.customInfo.id
      if (this.edit === 'edit') {
        liaisonEditSaveApi(this.row.id, data)
          .then((res) => {
            if (res.status == 200) {
              this.drawer = false
              this.$emit('isLiaison')
              this.$refs.oaForm.resetForm()
            }
          })
          .catch((err) => {
            this.$refs.oaForm.resetForm()
          })
      } else {
        liaisonSaveApi(data)
          .then((res) => {
            if (res.status == 200) {
              this.drawer = false
              this.$emit('isLiaison')
              this.$refs.oaForm.resetForm()
            }
          })
          .catch((err) => {
            this.$refs.oaForm.resetForm()
          })
      }
    },

    openBox(row, customInfo, edit) {
      this.edit = edit
      if (customInfo) {
        this.customInfo = customInfo
      }
      if (row) {
        this.row = row
        this.getEditData(row.id)
      } else {
        this.getData()
      }
      this.drawer = true
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep.el-drawer__body {
  padding: 30px 24px 50px 24px;
}
::v-deep .el-form--inline .el-form-item {
  display: flex;
}
.dialog-footer {
  padding-top: 20px;
  border-top: 1px solid #e6ebf5;
  text-align: right;
}
.from-foot-btn {
  button {
    height: auto;
  }
}
</style>
