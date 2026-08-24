<!-- 添加客户弹窗组件 -->
<template>
  <div class="station">
    <el-drawer
      :title="formData.title"
      :visible.sync="drawer"
      :direction="direction"
      :modal="true"
      :wrapperClosable="false"
      :before-close="handleClose"
      :size="formData.width"
    >
      <oaForm
        :form-info="fromInfo"
        :types="types"
        ref="oaForm"
        @handleClose="handleClose"
        @submitOk="submitOk"
        @addContinueOk="addContinueOk"
      ></oaForm>
    </el-drawer>

    <add-contract ref="addContract" :form-data="contractFromData"></add-contract>
  </div>
</template>
<script>
import { chargeCreateApi, clientCustomerSaveApi, chargeEditApi, chargeEditSubmitApi } from '@/api/enterprise'
export default {
  name: 'Index',
  props: {
    formData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  components: {
    oaForm: () => import('@/components/customer/oaForm'),
    addContract: () => import('@/views/customer/contract/components/addContract')
  },
  data() {
    return {
      drawer: false,
      direction: 'rtl',
      fromInfo: [],
      row: {},
      types: 0,
      force: 0,
      contractFromData: {}
    }
  },

  methods: {
    // 新增客户表单
    getData() {
      chargeCreateApi().then((res) => {
        this.fromInfo = res.data
      })
    },
    getEditData(id) {
      chargeEditApi(id).then((res) => {
        this.fromInfo = res.data
      })
    },
    handleClose() {
      this.fromInfo = []
      this.row = {}
      this.drawer = false
    },
    // 提交并继续添加合同
    addContinueOk(data) {
      this.submitOk(data, 1)
    },

    // 提交成功
    submitOk(data, type) {
      data.types = this.types
      data.force = this.force
      // 编辑客户信息
      if (this.row.id) {
        chargeEditSubmitApi(this.row.id, data)
          .then((res) => {
            if (res.status == 200) {
              if (type == 1) {
                this.contractFromData = {
                  title: '添加合同',
                  width: '570px',
                  id: this.row.id
                }
                this.$refs.addContract.openBox()
                this.$refs.oaForm.resetForm()
              } else {
                this.drawer = false
                this.$refs.oaForm.resetForm()
              }
              this.$emit('isOkEdit')
            } else {
              this.$refs.oaForm.saveLoading = false
              this.$refs.oaForm.addContractLoading = false
            }
          })
          .catch((err) => {
            if (err.data.status == 2001) {
              this.$modalSure(err.data.message).then(() => {
                this.force = 1
                this.submitOk(data, type)
                this.force = 0
              })

              this.force = 0
            }
            this.$refs.oaForm.saveLoading = false
            this.$refs.oaForm.addContractLoading = false
          })
      } else {
        // 添加新客户
        clientCustomerSaveApi(data)
          .then((res) => {
            if (res.status == 200) {
              if (type == 1) {
                this.contractFromData = {
                  title: '添加合同',
                  width: '570px',
                  id: res.data.id
                }
                this.$refs.addContract.openBox()
                this.$refs.oaForm.resetForm()
              } else {
                this.drawer = false
                this.$refs.oaForm.resetForm()
              }
              this.$refs.oaForm.saveLoading = false
              this.$emit('isOkEdit')
            } else {
              this.$refs.oaForm.saveLoading = false
            }
          })
          .catch((err) => {
            if (err.data.status == 2001) {
              this.$modalSure(err.data.message).then(() => {
                this.force = 1
                this.submitOk(data, type)
                this.force = 0
              })

              this.force = 0
            }

            this.$refs.oaForm.saveLoading = false
          })
      }
    },

    openBox(type, row, str) {
      this.types = type
      if (str === 'edit') {
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
.station ::v-deep.el-drawer__body {
  padding: 30px 24px 50px 24px;
}
::v-deep .el-form--inline .el-form-item {
  display: flex;
}

.from-foot-btn {
  button {
    height: auto;
  }
}
::v-deep.el-tag.el-tag--info {
  border: none;
}
</style>
