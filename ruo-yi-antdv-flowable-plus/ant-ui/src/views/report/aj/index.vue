<!--
 * @Author: lide1202@hotmail.com
 * @Date: 2021-3-13 11:04:24
 * @Last Modified by:   lide1202@hotmail.com
 * @Last Modified time: 2021-3-13 11:04:24
 !-->
<template>
  <div>
    <el-dialog
      title="请输入分享码"
      :visible.sync="dialogVisible"
      width="30%"
      :close-on-click-modal="false"
      :before-close="handleClose">
      <el-input v-model="password" placeholder="请输入分享码"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="checkPassword()">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>
<script>
import { reportShareDetailByCode} from '@/api/report/reportShare'
import { setShareToken } from '@/utils/auth'
export default {
  name: 'Report',
  components: {
  },
  data() {
    return {
      password: '',
      sharePassword: '',
      dialogVisible: false,
      reportCode: '',
      shareToken: ''
    }
  },

  created() {
    this.handleOpen()
  },
  methods: {
    async handleOpen() {
      const url = window.location.href
      const shareCode = url.substring(url.lastIndexOf('/') + 1)
      console.log(shareCode)

      const {code, data} = await reportShareDetailByCode(shareCode)
      if (code != '200') return
      console.log(data)
      this.reportCode = data.reportCode
      this.sharePassword = data.sharePassword
      this.shareToken = data.shareToken
      if (this.sharePassword) {
        console.log(this.sharePassword)
        this.dialogVisible = true
      }else {
        this.pushAj()
      }
    },
    checkPassword(){
      const md5 = require('js-md5')
      const inputPassword = md5(this.password)
      console.log(this.password +'--------------'+inputPassword + '--------' + this.sharePassword)
      if (inputPassword == this.sharePassword) {
        this.pushAj()
      }else {
        this.$message.error('分享码输入不正确')
      }

    },
    pushAj(){
      setShareToken(this.shareToken)
      this.$router.push({
        path: '/bigscreen/viewer',
        query: {
          reportCode: this.reportCode
        },
      })
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done()
        })
        .catch(_ => {})
    }

  }

}
</script>
