<!-- 配置app二维码组件 -->
<template>
  <div class="config-app">
    <!-- <el-popover placement="bottom" popper-class="user-poppqaer" trigger="click">
      <div class="popover-user">
        <div class="drop-head">
          <div class="drop-right align-center">
            <div ref="qrcode" class="qrcode">
              <img :src="imgSrc" />
            </div>
            <span>扫描配置通用APP</span>
          </div>
        </div>
        <div class="drop-config">
          <span>服务器地址：</span>
          <span>{{ qrValue }}</span>
        </div>
      </div>
      <div slot="reference" class="config-info">
        <el-tooltip content="配置App" effect="dark" placement="bottom">
          <i class="iconfont iconpeizhi pointer configapp"></i>
        </el-tooltip>
      </div>
    </el-popover> -->
  </div>

</template>

<script>

import { entSiteApi } from '@/api/public'
import QRCode from 'qrcodejs2'

export default {
  name: 'configapp',
  components: {
    QRCode
  },
  data() {
    return {
      url: '',
      qrValue: '',
      imgSrc: JSON.parse(localStorage.getItem('enterprise')).logo
    }
  },
  mounted() {
    this.entSite()
  },

  methods: {
    // 获取网站配置接口
    entSite() {
      entSiteApi().then((res) => {
        this.qrValue = res.data.address
        setTimeout(() => {
          new QRCode(this.$refs.qrcode, {
          text: this.qrValue,
          width: 128,
          height: 128,
          colorDark: '#000000',
          colorLight: '#ffffff',
          correctLevel: QRCode.CorrectLevel.H
        })
        },300)
      })
    },
  }
}
</script>

<style lang="scss" scoped>
.popover-user {
  z-index: 200;
  width: 200px;
}
.drop-head {
  padding: 18px 14px 14px 18px;
  border-bottom: 1px solid #f5f5f5;
  font-size: 13px;
  color: #999;
  .drop-right {
    display: flex;
    flex-direction: column;
  }
  .align-center {
    align-items: center;
    .qrcode {
      margin-bottom: 6px;
      position: relative;
      width: 128px;
      height: 128px;
      img {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        width: 40px;
        height: 40px;
        border-radius: 2px;
      }
    }
  }

}

</style>
