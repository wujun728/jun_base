<template>
  <div class="divBox">
    <el-card class="employees-card-bottom">
      <div class="auth acea-row row-between-wrapper">
        <div class="acea-row row-middle">
          <img alt="" class="img" src="../../../../assets/images/deng.png" />
          <div v-if="status === -1 || status === -9" class="text">
            <div>体验时间剩余 {{ dayNum }}天</div>
            <div class="code">到期后移动端将不能正常使用，如果您对我们的系统满意，请支持正版！</div>
          </div>
          <div v-else-if="status === 2" class="text">
            <div>体验时间剩余 {{ dayNum }}天</div>
            <div class="code red">审核未通过</div>
          </div>
          <div v-else-if="status === 1" class="text">
            <div>商业授权</div>
            <div class="code">授权码：{{ authCode || '--' }}</div>
            <div class="code block">
              授权时间：<span v-if="authTime !== 99999">
                剩余{{ authTime || '0' }} 天
                <span v-if="authTime <= 15" class="tips">(授权到期后，系统将无法使用)</span>
              </span>
              <span v-else>永久</span>
            </div>
          </div>
          <div v-else-if="status === 0" class="text">
            <div>体验时间剩余 {{ dayNum || '--' }}天</div>
            <div class="code blue">授权申请已提交，请等待审核</div>
          </div>
        </div>
        <div>
          <template>
            <el-button @click="toCrmeb()">进入官网</el-button>
            <el-button type="primary" @click="payment('oa')">申请授权 </el-button>
          </template>
        </div>
      </div>
    </el-card>
    <!-- 商业授权 -->
    <el-dialog :visible.sync="isTemplate" :z-index="1" center title="商业授权" @on-cancel="cancel">
      <iframe :src="iframeUrl" frameborder="0" height="550" width="100%"></iframe>
    </el-dialog>
  </div>
</template>
<script>
import { auth, version, sync } from '@/api/setting'
export default {
  data() {
    return {
      baseUrl: 'https://doc.tuoluojiang.com/auth',
      iframeUrl: '',
      authCode: '',
      authTime: '',
      status: 1,
      dayNum: 0,
      copyright: '',
      isTemplate: false,
      copyrightText: '',
      payType: '',
      isShow: false, // 验证码模态框是否出现
      active: 0,
      timer: null,
      version: '',
      label: '',
      productType: ''
    }
  },

  created() {
    this.getVersion()
    this.getAuth()
  },
  mounted() {},
  methods: {
    getVersion() {
      version().then((res) => {
        this.version = res.data.version
        this.label = res.data.label
      })
    },
    //   获取商业授权信息
    getAuth() {
      auth().then((res) => {
        let data = res.data || {}
        this.authCode = data.authCode || ''
        this.authTime = data.day || ''
        this.status = data.status === undefined ? -1 : data.status
        this.dayNum = data.day || 0
        this.copyright = data.copyright
      })
    },
    // 申请商业授权
    payment(product) {
      this.productType = product
      let host = location.host
      let uniqued = JSON.parse(localStorage.getItem('enterprise')).uniqued
      let name = JSON.parse(localStorage.getItem('enterprise')).enterprise_name

      let hostData = host.split('.')
      if (hostData[0] === 'test' && hostData.length === 4) {
        host = host.replace('test.', '')
      } else if (hostData[0] === 'www' && hostData.length === 3) {
        host = host.replace('www.', '')
      }

      this.iframeUrl = this.baseUrl + '?host=' + host + '&uniqued=' + uniqued + '&name=' + name

      this.isTemplate = true
    },
    cancel() {
      this.iframeUrl = ''
      this.isTemplate = false
    },
    // 进入官网
    toCrmeb() {
      window.open('https://doc.tuoluojiang.com')
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep.el-dialog {
  border-radius: 6px;
  width: 447px;
}

.img {
  width: 40px;
  height: 40px;
  margin-right: 8px;
}
.auth-body {
  padding: 14px;
}
.auth {
  padding: 9px 16px 9px 10px;
}
.card {
  width: 100%;
  height: 98px;
  background-color: #fff;
}

.auth .iconIos {
  font-size: 40px;
  margin-right: 10px;
  color: #001529;
}

.auth .text {
  font-weight: 400;
  color: rgba(0, 0, 0, 1);
  font-size: 18px;
}

.auth .text .code {
  display: inline-block;
  margin-top: 8px;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.5);
}
.block {
  display: block !important;
}
.tips {
  font-size: 12px;
}
</style>
