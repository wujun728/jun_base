<!-- @FileDescription: 二维码弹窗页面 -->
<template>
  <div>
    <el-dialog
      :title="fromData.title"
      :visible.sync="dialogVisible"
      :width="fromData.width"
      custom-class="payment"
      :show-close="true"
      :append-to-body="true"
      :before-close="handleClose"
    >
      <template slot="title">
        <el-row class="pay-title">
          <el-col :span="12" class="text-center" v-for="(item, index) in payType" :key="item.id">
            <span @click="tabPay(index)" :class="activeIndex === index ? 'active' : ''">{{ item.name }}</span>
          </el-col>
        </el-row>
      </template>
      <div class="body">
        <div class="mt20">
          <div class="amount-image">
            <div ref="QRCode_header" :style="{ width: width + 'px', height: height + 'px' }"></div>
          </div>
          <div class="amount mt20 text-center mb15" v-if="fromData.data">
            <div class="number">
              <span class="title03">支付金额：</span>
              <span class="title01">¥</span>
              <span class="title02">{{ fromData.data.amount.toString().split('.')[0] }}.</span>
              <span class="title01">{{ fromData.data.amount.toString().split('.')[1] }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import QRCode from 'qrcodejs2'
import { orderResultApi } from '@/api/system'
export default {
  name: 'Payment',
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
      openStatus: false,
      payType: [{ name: '微信支付', id: 1 }],
      activeIndex: 0,
      loading: false,
      width: 190,
      height: 190,
      qrcode: null,
      timer: null
    }
  },
  watch: {
    fromData: {
      handler(nVal) {
        this.getQRCode(nVal.data.wxpay_url)
        this.createSetInterval(nVal.data.order_id)
      },
      deep: true
    }
  },

  methods: {
    handleClose() {
      this.$refs.QRCode_header.innerHTML = ''
      this.stopSetInterval()
      this.dialogVisible = false
    },
    handleOpen() {
      this.dialogVisible = true
    },
    tabPay(index) {
      this.activeIndex = index
    },
    getOrderResult(order_id) {
      orderResultApi(order_id)
        .then((res) => {
          if (res.status == 200) {
            this.handleClose()
            this.$emit('isOk')
          }
        })
        .catch((error) => {})
    },
    createSetInterval(order_id) {
      this.stopSetInterval()
      this.timer = setInterval(() => {
        this.getOrderResult(order_id)
      }, 2000)
    },
    stopSetInterval() {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
    },
    getQRCode(url) {
      this.$nextTick(() => {
        this.qrcode = new QRCode(this.$refs.QRCode_header, {
          text: url,
          width: 190,
          height: 190,
          colorDark: '#000000',
          colorLight: '#ffffff',
          correctLevel: QRCode.CorrectLevel.H
        })
      })
    }
  },
  beforeDestroy() {
    this.stopSetInterval()
  }
}
</script>

<style scoped lang="scss">
::v-deep .el-dialog {
  border-radius: 15px;
}
::v-deep .el-dialog__header {
  border-bottom: 1px solid #eeeeee;
  padding: 0;
  height: 60px;
  line-height: 60px;
}
.payment {
  ::v-deep .el-dialog {
    border-radius: 15px;
  }
  .pay-title {
    height: 100%;
    font-size: 15px;
    span {
      display: inline-block;
      height: 100%;
      cursor: pointer;
      &.active {
        color: #1890ff;
        border-bottom: 2px solid #1890ff;
      }
    }
  }
  .body {
    .amount-image {
      width: 210px;
      height: 210px;
      border: 1px solid #eeeeee;
      padding: 10px;
      margin-left: 22px;
      .img {
        width: 190px;
        height: 190px;
        background-color: #00ff00;
      }
    }
    .amount {
      .number {
        color: #f5222d;
        font-size: 0;
        .title01 {
          font-size: 15px;
        }
        .title02 {
          font-size: 24px;
        }
        .title03 {
          font-size: 16px;
          color: rgba(0, 0, 0, 0.85);
          padding-right: 6px;
        }
      }
      .discount {
        margin-top: -6px;
        font-weight: 400;
        color: rgba(136, 136, 136, 0.85);
        line-height: 12px;
      }
    }
  }
}
</style>
