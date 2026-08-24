<!-- 客户-合同转移弹窗组件 -->
<template>
  <div>
    <el-dialog
      :title="fromData.title"
      :visible.sync="dialogVisible"
      :width="fromData.width"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <div class="body">
        <div class="mt14 el-input--small flex">
          <span class="label">接手人员：</span>
          <select-member
            :only-one="true"
            :value="userList || []"
            :placeholder="`请选择企业成员`"
            @getSelectList="getSelectList"
            style="width: 100%"
          ></select-member>

          <!-- <div class="el-input__inner select plan-footer-one" @click="openDepartment()">
            <span v-if="userList.length === 0">请选择企业成员（单选）</span>
            <div class="flex-box">
              <span
                v-for="(item, index) in userList"
                :key="index"
                class="el-tag el-tag--small el-tag--info el-tag--light"
                @click.stop=""
              >
                {{ item.name }}
                <i class="el-tag__close el-icon-close" @click.stop="cardTag(index)" />
              </span>
            </div>
          </div> -->
        </div>
        <div class="mt20">
          <el-checkbox-group v-model="checkList">
            <el-checkbox
              v-for="item in transfer"
              v-show="fromData.type <= item.value"
              :disabled="fromData.type === item.value"
              :key="item.value"
              :label="item.value"
              >{{ item.label }}</el-checkbox
            >
          </el-checkbox-group>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose" size="small">{{ $t('public.cancel') }}</el-button>
        <el-button :loading="loading" size="small" type="primary" @click="handleAdd">{{ $t('public.ok') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { clientContractShiftApi, customerShiftApi, clientInvoiceShiftApi } from '@/api/enterprise'
export default {
  name: 'TransferDialog',
  components: {
    selectMember: () => import('@/components/form-common/select-member')
  },
  props: {
    fromData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      dialogVisible: false,
      userList: [],
      checkList: [],
      transfer: [
        { value: 1, label: this.$t('customer.customertransfer') },
        { value: 2, label: this.$t('customer.contracttransfer') },
        { value: 3, label: this.$t('customer.invoicetransfer') }
      ],
      loading: false
    }
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang
    }
  },
  watch: {
    fromData: {
      handler(nVal) {
        this.checkList.push(nVal.type)
      },
      deep: true
    },
    lang() {
      this.setOptions()
    }
  },
  methods: {
    setOptions() {
      this.transfer = [
        { value: 1, label: this.$t('customer.customertransfer') },
        { value: 2, label: this.$t('customer.contracttransfer') },
        { value: 3, label: this.$t('customer.invoicetransfer') }
      ]
    },
    handleClose() {
      this.userList = []
      this.checkList = []
      this.dialogVisible = false
    },
    handleOpen() {
      this.dialogVisible = true
    },
    // 选择成员完成回调
    getSelectList(data) {
      this.userList = data
    },

    handleAdd() {
      if (this.userList.length <= 0) {
        this.$message.error(this.$t('customer.placeholder25'))
      } else {
        var data = {
          to_uid: this.userList[0].value,
          data: this.fromData.ids
        }
        if (this.fromData.type === 1) {
          data.contract = this.checkList.includes(2) ? 1 : 0
          data.invoice = this.checkList.includes(3) ? 1 : 0
          this.clientDataShift(data)
        } else if (this.fromData.type === 2) {
          data.invoice = this.checkList.includes(3) ? 1 : 0
          this.clientContractShift(data)
        } else if (this.fromData.type === 3) {
          this.clientInvoiceShift(data)
        }
      }
    },
    // 客户管理--批量转移
    clientDataShift(data) {
      this.loading = true
      customerShiftApi(data)
        .then((res) => {
          this.loading = false
          this.userList = []
          this.handleClose()
          this.$emit('handleTransfer')
          this.$refs.department.selectList = {}
        })
        .catch((error) => {
          this.loading = false
        })
    },
    // 合同管理--批量转移
    clientContractShift(data) {
      this.loading = true
      clientContractShiftApi(data)
        .then((res) => {
          this.loading = false
          this.userList = []
          this.handleClose()
          this.$emit('handleTransfer')
          this.$refs.department.selectList = {}
        })
        .catch((error) => {
          this.loading = false
        })
    },
    // 发票管理--批量转移
    clientInvoiceShift(data) {
      this.loading = true
      clientInvoiceShiftApi(data)
        .then((res) => {
          this.loading = false
          this.userList = []
          this.handleClose()
          this.$emit('handleTransfer')
          this.$refs.department.selectList = {}
        })
        .catch((error) => {
          this.loading = false
        })
    }
  }
}
</script>

<style scoped lang="scss">
.label {
  width: 80px;
}
.flex {
  align-items: center;
}
.el-tag {
  margin-top: 3px;
}
</style>
