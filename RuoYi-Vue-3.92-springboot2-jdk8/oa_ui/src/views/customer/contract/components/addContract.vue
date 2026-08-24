<!-- 合同-添加合同页面 -->
<template>
  <div class="station">
    <el-drawer
      :title="formData.title"
      :visible.sync="drawer"
      :direction="direction"
      :modal="true"
      :append-to-body="true"
      :wrapper-closable="true"
      :before-close="handleClose"
      :size="formData.width"
      :wrapperClosable="false"
    >
      <oaForm
        :form-info="fromInfo"
        ref="oaForm"
        :showContractBtn="true"
        :type="`contract`"
        @handleClose="handleClose"
        @submitOk="submitOk"
        @addContinueOk="addContinueOk"
      ></oaForm>
    </el-drawer>
    <edit-contract ref="editContract" :form-data="fromData"></edit-contract>
  </div>
</template>

<script>
import { contractCreateApi, contractEditCreateApi, contractAddApi, contractEditApi } from '@/api/enterprise'
export default {
  name: 'AddContract',
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
    editContract: () => import('@/views/customer/contract/components/editContract')
  },
  data() {
    return {
      row: {},
      drawer: false,
      direction: 'rtl',
      fromInfo: [],
      addText: '',
      fromData: {},
      itemData: null
    }
  },
  watch: {},
  methods: {
    handleClose() {
      this.drawer = false
      this.row = {}
    },
    // 获取新增表单
    getData() {
      contractCreateApi().then((res) => {
        if (this.formData && this.formData.id) {
          res.data.forEach((item) => {
            item.data.forEach((val) => {
              if (val.key == 'contract_customer') {
                val.value = this.formData.id
              }
            })
          })
        }
        this.fromInfo = res.data
      })
    },
    // 获取编辑表单
    getEditData() {
      contractEditCreateApi(this.row.id).then((res) => {
        this.fromInfo = res.data
      })
    },
    async openBox(data) {
      if (data) {
        this.row = data
        await this.getEditData()
      } else {
        await this.getData()
      }

      this.drawer = true
    },

    // 提交成功
    submitOk(data, type) {
      if (this.row.id) {
        contractEditApi(this.row.id, data)
          .then((res) => {
            if (res.status == 200) {
              if (type == 1) {
                data.eid = data.contract_customer
                data.cid = this.row.id
                this.fromData = {
                  title: '查看合同',
                  width: '1000px',
                  data: data,
                  isClient: false,
                  name: this.formData.name,
                  id: this.row.id,
                  edit: true
                }
                this.$nextTick()
                this.$refs.editContract.tabIndex = '2'
                this.$refs.editContract.tabNumber = 2
                this.$refs.editContract.openBox(this.row)
              } else {
                this.drawer = false
                this.$emit('getTableData')
                this.$refs.oaForm.resetForm()
              }
              this.$refs.oaForm.saveLoading = false
              this.$refs.oaForm.addContractLoading = false
            } else {
              this.$refs.oaForm.saveLoading = false
              this.$refs.oaForm.addContractLoading = false
            }
          })
          .catch((err) => {
            this.$refs.oaForm.resetForm()
          })
      } else {
        contractAddApi(data)
          .then((res) => {
            if (res.status == 200) {
              if (type == 1) {
                this.drawer = false
                data.eid = data.contract_customer
                data.cid = res.data.id
                this.fromData = {
                  title: '查看合同',
                  width: '1000px',
                  data: data,
                  isClient: false,
                  name: this.formData.name,
                  id: res.data.id,
                  edit: true
                }
                this.$refs.editContract.tabIndex = '2'
                this.$refs.editContract.tabNumber = 2
                this.$refs.editContract.openBox(this.fromData)
              } else {
                this.drawer = false
                this.$refs.oaForm.resetForm()
                this.$emit('getTableData')
              }
              this.$refs.oaForm.saveLoading = false
              this.$refs.oaForm.addContractLoading = false
            } else {
              this.$refs.oaForm.saveLoading = false
              this.$refs.oaForm.addContractLoading = false
            }
          })
          .catch((err) => {
            this.$message.error(err)
            // this.$refs.oaForm.resetForm()
          })
      }
    },
    addContinueOk(data) {
      this.submitOk(data, 1)
    },
    handlePayment() {
      this.addText = 'on'
      this.$refs.contractInfo.handleConfirm().then(() => {
        setTimeout(() => {
          this.fromData = {
            title: this.$t('customer.viewcustomer'),
            width: '1000px',
            data: this.itemData,
            isClient: false,
            name: this.formData.name,
            id: this.itemData.id,
            edit: true
          }

          this.$refs.editContract.tabIndex = '2'
          this.$refs.editContract.tabNumber = 2
          this.drawer = false
          setTimeout(() => {
            this.$refs.editContract.openBox()
            this.addText = ''
            this.$refs.contractInfo.reset()
            this.$refs.contractInfo.contractList()
          }, 500)
        }, 300)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-form--inline .el-form-item {
  display: flex;
}
::v-deep .el-input-number--medium {
  width: 100%;
}
::v-deep .el-input__inner {
  text-align: left;
}
::v-deep .el-date-editor {
  width: 100%;
}
.from-item-title {
  border-left: 5px solid #1890ff;
  span {
    padding-left: 10px;
    font-weight: bold;
    font-size: 14px;
  }
}
.form-box {
  display: flex;
  flex-wrap: wrap;
  margin: 0 20px;
  justify-content: space-between;
  .form-item {
    width: 48%;
    ::v-deep .el-form-item__content {
      width: calc(100% - 90px);
    }
    ::v-deep .el-select--medium {
      width: 100%;
    }
    ::v-deep .el-form-item {
      margin-bottom: 20px;
    }
    ::v-deep .el-textarea__inner {
      resize: none;
    }
  }
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
::v-deep.el-drawer__body {
  padding: 20px;
  padding-bottom: 40px;
}
</style>
