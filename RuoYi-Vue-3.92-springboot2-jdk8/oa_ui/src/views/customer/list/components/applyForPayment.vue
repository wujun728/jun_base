<!-- 付款记录详情侧滑组件 -->
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
    >
      <div class="invoice-title" slot="title">
        <el-row class="invoice-header">
          <div class="invoice-left">
            <img :src="list.card ? list.card.avatar : ''" alt="" class="img" />
          </div>
          <div class="invoice-right">
            <span class="title">{{ list.card ? list.card.name : '--' }}的合同付款申请</span>
            <span class="text color1" v-if="list.status === 0"> 待审核 </span>
            <span class="text color2" v-if="list.status === 1"> 已通过 </span>
            <span class="text color3" v-if="list.status === 2"> 未通过 </span>
            <span class="text color2" v-if="list.status === -1"> 已撤销 </span>
          </div>
        </el-row>
      </div>

      <div class="content">
        <el-form label-width="100px">
          <div class="from-item-title mb15">
            <span>{{ list.bill_types == 0 ? '支出信息' : '付款信息' }}</span>
          </div>
          <div class="form-box">
            <div class="form-item">
              <el-form-item :label="list.types == 2 ? '支出时间：' : '付款时间：'">
                <span>{{ list.date }}</span>
              </el-form-item>
            </div>
            <div class="form-item">
              <el-form-item :label="list.types == 2 ? '支出金额(元)：' : '付款金额(元)：'">
                <span>{{ list.num }}</span>
              </el-form-item>
            </div>
            <div class="form-item">
              <el-form-item label="支付方式：">
                <span>{{ list.pay_type }}</span>
              </el-form-item>
            </div>
            <div class="form-item">
              <el-form-item :label="list.types == 2 ? '支出单号：' : '付款单号：'">
                <span>{{ list.bill_no || '--' }}</span>
              </el-form-item>
            </div>
            <div class="form-item">
              <el-form-item :label="list.types == 2 ? '支出凭证：' : '付款凭证：'">
                <span v-if="JSON.stringify(list.attachs) == '[]'">--</span>

                <img
                  v-else
                  @click="lookViewer(list.attachs[0].src, '')"
                  :src="list.attachs ? list.attachs[0].src : ''"
                  alt=""
                  class="img"
                />
              </el-form-item>
            </div>
            <div class="line"></div>
            <div class="from-item-title mb15">
              <span>其他信息</span>
            </div>
            <div class="form-item">
              <el-form-item label="客户名称：">
                <span>{{ list.client ? list.client.name : '--' }}</span>
              </el-form-item>
            </div>
            <div class="form-item">
              <el-form-item label="合同名称：">
                <span>{{ list.treaty ? list.treaty.contract_name : '--' }}</span>
              </el-form-item>
            </div>
            <div class="form-item" v-if="list.types !== 2">
              <el-form-item label="业务类型：">
                <span v-if="list.types === 0">合同回款</span>
                <span v-if="list.types === 1">合同续费 - {{ list.renew.title }}</span>
              </el-form-item>
            </div>
            <div class="form-item" v-if="list.types === 1 && list.types !== 2">
              <el-form-item label="续费结束日期：">
                <span>{{ list.end_date == '0000-00-00' ? '--' : list.end_date }}</span>
              </el-form-item>
            </div>
            <div class="form-item" v-if="list.types !== 2">
              <el-form-item label="发票审核状态：">
                <template v-if="list.invoice">
                  <span> {{ getInvoiceStatus(list.invoice.status) }} </span>
                </template>
                <span v-else>--</span>
              </el-form-item>
            </div>
            <div class="form-item">
              <el-form-item label="备注：">
                <span class="lh24">{{ list.mark || '--' }}</span>
              </el-form-item>
            </div>
          </div>
        </el-form>
      </div>
      <div class="button from-foot-btn fix btn-shadow" v-if="list.status === 0 && formData.type !== 1">
        <el-button size="small" type="primary" @click="handleContract">通过</el-button>
        <el-button size="small" type="danger" @click="refuse">拒绝</el-button>
      </div>
    </el-drawer>
    <expend-dialog ref="expendDialog" :config="operationDialog" @isOk="isOk"></expend-dialog>
    <operationDialog
      ref="operationDialog"
      :statusOption="statusOptions"
      :config="operationDialog"
      @isOk="isOk"
    ></operationDialog>

    <!-- 拒绝 -->
    <el-dialog :title="title" top="25vh" :append-to-body="true" :visible.sync="dialogVisible" width="540px">
      <div class="line" />
      <el-form :model="form" ref="from" :rules="rules" class="from">
        <el-form-item :label="reason + '：'" label-width="90px" prop="remarks">
          <el-input type="textarea" v-model="form.remarks"></el-input>
        </el-form-item>
        <div class="footer">
          <el-button size="small" class="btn" @click="cancelFn">取消</el-button>
          <el-button size="small" type="primary" @click="submitFn" class="btn">确定</el-button>
        </div>
      </el-form>
    </el-dialog>
    <image-viewer ref="imageViewer" :src-list="srcList"></image-viewer>
  </div>
</template>
<script>
import { getbillCate, billCateApi, clientBillStatusApi } from '@/api/enterprise'
import { getInvoiceText } from '@/libs/customer'
export default {
  name: 'applyForPayment',
  components: {
    operationDialog: () => import('@/views/fd/examine/components/operationDialog'),
    expendDialog: () => import('@/views/customer/list/components/expendDialog'),
    imageViewer: () => import('@/components/common/imageViewer')
  },
  props: {
    paymentType: {
      type: String,
      default: ''
    },
    formData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      drawer: false,
      direction: 'rtl',
      list: {},
      srcList: [],
      catePath: [],
      operationDialog: {},
      statusOptions: [],
      dialogVisible: false,
      form: {
        remarks: ''
      },
      rules: {
        remarks: [{ required: true, message: '请填写拒绝原因', trigger: 'blur' }]
      },

      reason: '拒绝原因',
      title: '审核未通过'
    }
  },

  watch: {
    formData: {
      handler(nVal) {
        this.list = nVal.data
        if (this.list.attachs[0]) {
          this.srcList.push(this.list.attachs[0].src)
        }

        if (this.list.status === 0) {
          this.getOption()
        }
      }
    }
  },

  methods: {
    handleClose() {
      this.drawer = false
    },

    openBox() {
      this.drawer = true
    },

    isOk() {
      this.drawer = false
      this.$emit('isOk')
    },

    async getOption() {
      billCateApi().then((res) => {
        this.statusOptions = res.data
      })
    },

    async getbillCate(id) {
      getbillCate(id).then((res) => {
        this.catePath = res.data.bill_cate_path
      })
    },
    getInvoiceStatus(status) {
      return getInvoiceText(status)
    },

    // 查看与下载附件
    lookViewer(url) {
      this.$refs.imageViewer.openImageViewer(url)
    },

    // 拒绝
    refuse() {
      this.dialogVisible = true
    },

    // 通过
    handleContract() {
      this.catePath = []
      this.getbillCate(this.list.cid)
      let str = '审核通过'
      let type = ''
      setTimeout(() => {
        this.operationDialog = {
          title: str,
          data: this.list,
          path: this.catePath,
          type,
          source: 'fd'
        }
      }, 200)
      if (this.list.types !== 2) {
        this.$refs.operationDialog.handleOpen()
      } else {
        this.$refs.expendDialog.handleOpen()
      }
    },

    submitFn() {
      let data = {
        fail_msg: this.form.remarks,
        status: 2
      }
      this.$refs.from.validate((valid) => {
        if (valid) {
          clientBillStatusApi(this.list.id, data).then((res) => {
            this.dialogVisible = false
            this.form.remarks = ''
            this.isOk()
          })
        }
      })
    },
    cancelFn() {
      this.dialogVisible = false
      this.form.remarks = ''
    }
  }
}
</script>
<style scoped lang="scss">
.invoice-header {
  display: flex;
  .invoice-left {
    .img {
      width: 55px;
      height: 55px;
      display: block;
      border-radius: 3px;
    }
  }
  .invoice-right {
    margin-left: 15px;
    margin-top: 4px;
    height: 50px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    .title {
      font-size: 16px;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #333333;
      margin-bottom: 15px;
    }
    .text {
      font-size: 13px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
    }
    .color1 {
      color: #f90;
    }
    .color2 {
      color: #909399;
    }
    .color3 {
      color: #ed4014;
    }
  }
}
.footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.content {
  padding: 26px;
  .from-item-title {
    border-left: 3px solid #1890ff;
    span {
      padding-left: 10px;
      font-weight: 600;
      font-size: 14px;
    }
  }
  .form-box {
    .line {
      width: 100%;
      height: 4px;
      border-bottom: 1px dashed #f2f5fc;
      margin-bottom: 30px;
    }
    .form-item {
      .img {
        width: 50px;
        height: 50px;
        display: block;
      }
      ::v-deep .el-form-item {
        margin-bottom: 20px;
      }
      ::v-deep .el-form-item--medium .el-form-item__content {
        line-height: 12px;
      }
      ::v-deep .el-form-item__label {
        font-size: 12px !important;
        font-weight: normal;
        color: #909399;
        line-height: 12px;
      }
      span {
        font-size: 13px;

        font-weight: 400;
        color: #303133;
        line-height: 12px;
      }
    }
  }
}
.lh24 {
  margin-top: -6px;
  line-height: 24px !important;
}
</style>
