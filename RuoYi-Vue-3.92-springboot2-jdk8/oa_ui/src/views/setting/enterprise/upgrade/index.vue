<template>
  <div class="divBox">
    <el-card>
      <div class="header">
        <div>
          <div>
            当前版本:<span class="v"></span><span class="num">{{ version || 'Tuoluojiang' }}</span>
          </div>
          <div class="info title mt20">
            更新说明：
            <span v-if="!status">已升级至最新版本，无需更新</span>
            <ul v-if="status">
              <li>V{{ info.first_version }}.{{ info.second_version }}.{{ info.third_version }}</li>
            </ul>
          </div>
        </div>
        <el-button v-if="status" type="primary" class="primary btn update" @click="update()">立即升级</el-button>
      </div>
    </el-card>
    <el-card class="mt20 employees-card">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="系统升级" name="first">
          <el-timeline class="ml20">
            <el-timeline-item v-for="(item, index) in activities" :key="index" class="time-line mt20">
              <div class="time">{{ item.release_time }}</div>
              <el-collapse v-model="index" @change="handleChange">
                <el-collapse-item :name="0">
                  <div slot="title" :class="index == 0 ? 'active-line' : 'title-line'" @click="judgeActive(index)">
                    {{ item.title }} v{{ item.first_version }}.{{ item.second_version }}.{{ item.third_version }}
                    <i
                      :class="activeNames === index && isActive === 2 ? 'el-icon-arrow-up' : ' el-icon-arrow-down'"
                    ></i>
                  </div>
                  <ul class="content ml20">
                    <li v-html="item.content" class="content-li"></li>
                  </ul>
                </el-collapse-item>
              </el-collapse>
            </el-timeline-item>
          </el-timeline>
          <!-- 缺省页 -->
          <div class="defult" v-if="activities.length == 0">
            <img src="@/assets/images/def2.png" alt="" class="img" />
            <div class="text">暂无系统升级～</div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="升级记录" name="second">
          <el-timeline class="ml20" v-if="logActivities.length > 0">
            <el-timeline-item v-for="(item, index) in logActivities" :key="index" class="time-line mt20">
              <div class="time">{{ item.upgrade_time }}</div>
              <el-collapse v-model="index" @change="handleChange">
                <el-collapse-item :name="0">
                  <div slot="title" :class="index == 0 ? 'active-line' : 'title-line'" @click="judgeActive(index)">
                    {{ item.title }} v{{ item.first_version }}.{{ item.second_version }}.{{ item.third_version }}

                    <i
                      :class="activeNames === index && isActive === 2 ? 'el-icon-arrow-up' : ' el-icon-arrow-down'"
                    ></i>
                  </div>
                  <ul class="content ml20">
                    <li v-html="item.content" class="content-li"></li>
                  </ul>
                </el-collapse-item>
              </el-collapse>
            </el-timeline-item>
          </el-timeline>
          <!-- 缺省页 -->
          <div class="defult" v-if="logActivities.length == 0">
            <img src="@/assets/images/def2.png" alt="" class="img" />
            <div class="text">暂无升级记录哟～</div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 免责声明 -->
    <el-dialog :visible.sync="exoneration" width="672px" :show-close="false" :close-on-click-modal="false">
      <div class="dialog1">
        <div class="exoneration-header">免责声明</div>
        <div class="dialog1-content" v-html="agreemen.content">
        </div>
        <div class="footer">
          <div class="btn" @click="refuse">拒绝</div>
          <div class="btn2" @click="agree">同意</div>
        </div>
      </div>
    </el-dialog>
    <!-- 在线升级 -->
    <el-dialog :visible.sync="upgrade" width="380px" :show-close="false" :close-on-click-modal="false">
      <div class="dialog d-upgrade">
        <div class="img">升级至v{{ infoUpgrade }}</div>
        <div v-if="upgradeProgress.speed != '100.0' || this.downloadStatus != '200'">
          <div class="progress">
            <el-progress type="circle" :percentage="Number(upgradeProgress.speed)" :format="format"></el-progress>
          </div>
          <div class="flex">
            <div class="circle">{{ upgradeProgress.speed }}%</div>
            <div class="text">正在更新，请耐心等待 ~</div>
          </div>
        </div>

        <template v-if="upgradeProgress.speed == '100.0'">
          <img src="@/assets/images/ok.png" alt="" class="ok-img" />
          <div class="ok-text">升级成功</div>
          <el-button type="primary" round class="ok-btn mt20" @click="back">确认</el-button>
        </template>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  getUpgradeList,
  getUpgradeKey,
  getUpgradeLog,
  getUpgradeAgreement,
  getUpgradeStart,
  getUpgradeProgress
} from '@/api/setting.js'
import { logout } from '@/api//user.js'
import { version } from '@/api/setting'
export default {
  name: '',

  data() {
    return {
      activeName: 'first',
      activities: [],
      logActivities: [],
      agreemen: {},
      activeNames: 0,
      info: {},
      arrId: [],
      status: false,
      version: '',
      exoneration: false,
      upgrade: false,
      infoUpgrade: '',
      upgradeProgress: {
        speed: 0
      },
      isActive: 2,
      timer: null,
      downloadStatus: null,
      one: 0
    }
  },
  computed: {},
  watch: {
    // 'upgradeProgress.speed': {
    //   handler(newVal, oldVal) {
    //     if (newVal == '100.0') {
    //       clearInterval(this.timer);
    //     }
    //   },
    // },
  },
  created() {},
  mounted() {
    this.getUpgradeList()
    this.getUpgradeKey()
    this.getVersion()
    this.getUpgradeAgreement()
  },
  methods: {
    //   获取在线升级列表
    getUpgradeList() {
      getUpgradeList().then((res) => {
        this.activities = res.data.list
      })
    },

    // 获取版本号
    getVersion() {
      version().then((res) => {
        this.version = res.data.version
      })
    },

    handleChange(val) {
      if (val.length == 0) {
        this.isActive = 1
      } else {
        this.isActive = 2
      }
    },

    // 获取在线升级数据
    getUpgradeKey() {
      getUpgradeKey().then((res) => {
        this.info = res.data
        this.infoUpgrade = this.info.first_version + '.' + this.info.second_version + '.' + this.info.third_version
        if (res.status == 200) {
          this.status = true
        } else {
          this.status = false
        }
      })
    },

    // 获取升级协议
    getUpgradeAgreement() {
      getUpgradeAgreement().then((res) => {
        this.agreemen = res.data
      })
    },

    // 获取升级日志
    getUpgradeLog() {
      getUpgradeLog().then((res) => {
        this.logActivities = res.data.list
      })
    },

    // 关闭协议弹窗
    refuse() {
      this.exoneration = false
    },

    // 同意协议
    agree() {
      this.exoneration = false
      this.upgrade = true
      this.format()
      this.getUpgradeStart()
    },

    // 点击立即升级
    update() {
      this.exoneration = true
    },
    //  开始升级
    getUpgradeStart() {
      getUpgradeStart(this.info.package_key)
        .then((res) => {
          if (res.status == 200) {
            this.timer = setInterval(() => {
              this.getUpgradeProgress()
            }, 4000)
          }
        })
        .catch((err) => {
          this.$message.error('下载终止')
          clearInterval(this.timer)
          this.timer = null
          this.upgrade = false
        })
    },
    // 升级完毕确认并退出登录
    back() {
      this.upgrade = false

      logout()
        .then((res) => {
          this.$message.success('您已成功升级，系统将自动退出，请您重新登录')
          this.$router.replace('/admin/login')
          localStorage.clear()
          removeCookies('token')
          removeCookies('expires_time')
          removeCookies('uuid')
        })
        .catch((res) => {})
    },

    format() {
      // let text = '升级中！';
      return this.upgradeProgress.tip || '升级中' + '\n'
    },

    judgeActive(val) {
      this.activeNames = val
    },
    // 获取升级进度
    getUpgradeProgress() {
      getUpgradeProgress()
        .then((res) => {
          this.upgradeProgress = res.data
          this.downloadStatus = res.status
          if (this.upgradeProgress.speed == '100.0') {
            clearInterval(this.timer)
            this.timer = null
          }
        })
        .catch((err) => {
          clearInterval(this.timer)
          this.upgrade = false
        })
    },

    // 切换tab栏
    handleClick() {
      if (this.activeName == 'first') {
        this.getUpgradeList()
      } else {
        this.activeNames = 0
        this.isActive = 2
        this.getUpgradeLog()
      }
    }
  },
  beforeDestroy() {
    //销毁
    clearInterval(this.timer)
    this.timer = null
  }
}
</script>
<style scoped lang="scss">
// .upgrade {
//   padding: 20px;
// }
.active-line {
  font-size: 18px;
  font-weight: 600;
  color: #1890ff;
  margin-bottom: 20px;
}
.ml20 {
  margin-left: -40px;
}
::v-deep .el-collapse {
  border: none;
}
::v-deep .el-collapse-item__wrap {
  border: none;
}
::v-deep .el-collapse-item__header {
  border: none;
}
::v-deep .el-dialog__header {
  padding: 0;
}
::v-deep .el-icon-arrow-right:before {
  content: '';
}
::v-deep.el-card__body {
  padding: 10px 20px 0 20px;
}
::v-deep .active-line {
  margin-bottom: 28px;
}
.defult {
  width: 100%;
  height: 400px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  .img {
    width: 200px;
    height: 150px;
  }
  .text {
    font-size: 13px;
    font-weight: 400;
    color: #999999;
  }
}

.mt20 {
  margin-top: 20px;
}
.divBox .header {
  font-size: 12px;
  color: #000;
  display: flex;
  justify-content: space-between;
}

.divBox .header .v {
  color: #1890ff;
  margin-left: 10px;
}

.divBox .header .num {
  color: #1890ff;
  font-size: 24px;
}

.divBox .header .info {
  color: #999999;
  padding-bottom: 5px;
}

.divBox .header .title {
  color: #999999;
  display: flex;
}

.divBox .header .info ul {
  color: #999999;
  display: flex;
  margin-top: 0px;
  margin-left: -20px;
}

.divBox .header .info ul li::marker {
  color: red;
}

.divBox .header .info ul li + li {
  margin-left: 40px;
}
.time-line {
  position: relative;
  margin-left: 160px;
  margin-top: 40px;
}
.time {
  position: absolute;
  left: -150px;
  font-size: 14px;
  font-weight: 400;
  color: #999999;
  margin-right: 10px;
}
.title-line {
  font-size: 16px;

  font-weight: 600;
  color: #333333;
  margin-bottom: 20px;
}
.content {
  margin-top: 5px;
  white-space: pre-wrap;
  font-size: 12px;
}
.content-li {
  list-style-type: none !important;
  color: #999;
}

.upgrade .contentTime .info {
  font-size: 12px !important;
  color: #999 !important;
  margin-top: 13px;
}
.el-icon-arrow-down {
  color: #bbbbbb;
  margin-left: 5px;
}
.el-icon-arrow-up {
  color: #bbbbbb;
  margin-left: 5px;
}

.update {
  margin: 27px 16px;
}
.dialog1 {
  height: 534px;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  .dialog1-content {
    width: 670px;
    height: 430px;
    padding: 25px;
    overflow-y: auto;
    font-size: 14px;
    font-weight: 400;
    color: #777777;
    margin-top: 30px;
  }
  .footer {
    position: absolute;
    bottom: -4px;
    display: flex;
    justify-content: center;
    height: 60px;
    width: 672px;
    background-color: #fff;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;

    .btn {
      margin-top: 10px;
      width: 160px;
      height: 40px;
      background: #eeeeee;
      border-radius: 21px;
      text-align: center;
      line-height: 40px;
      margin-right: 10px;
      cursor: pointer;
    }
    .btn2 {
      margin-top: 10px;
      width: 160px;
      height: 40px;
      background: #2a7efb;
      border-radius: 21px;
      text-align: center;
      line-height: 40px;
      cursor: pointer;
      color: #fff;
    }
  }
}
.dialog {
  height: 365px;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;

  .img {
    margin-top: -20px;
    width: 390px;
    height: 169px;
  }
  .title {
    font-size: 22px;
    font-weight: 400;
    color: #333333;
    margin-top: 10px;
  }
  .dialog-content {
    width: 340px;
    padding: 10px;
    overflow-y: auto;
    font-size: 14px;
    font-weight: 400;
    color: #777777;
    margin-top: 30px;
  }
  .dialog-content::-webkit-scrollbar {
    height: 0;
    width: 0;
  }
  .footer {
    position: absolute;
    bottom: 0px;
    display: flex;
    height: 60px;
    background-color: #fff;
    border-radius: 10px;

    .btn {
      margin-top: 10px;
      width: 160px;
      height: 40px;
      background: #eeeeee;
      border-radius: 21px;
      text-align: center;
      line-height: 40px;
      margin-right: 10px;
      cursor: pointer;
    }
    .btn2 {
      margin-top: 10px;
      width: 160px;
      height: 40px;
      background: #2a7efb;
      border-radius: 21px;
      text-align: center;
      line-height: 40px;
      cursor: pointer;
      color: #fff;
    }
  }
}
.exoneration {
  width: 340px;
}
.exoneration-header {
  width: 673px;
  height: 96px;
  background: url('../../../../assets/images/exoneration.png') no-repeat;
  background-size: 100% 100%;
  font-size: 20px;
  text-align: center;
  line-height: 96px;
  font-weight: 500;
  color: #ffffff;
}
.d-upgrade {
  width: 380px;
  height: 390px;
  .img {
    width: 381px;
    height: 74px;
    background: url('../../../../assets/images/upgrade.png') no-repeat;
    background-size: 100% 100%;
    font-size: 24px;
    text-align: center;
    line-height: 74px;
    // font-weight: 400;
    color: #ffffff;
  }
  .progress {
    display: flex;
    justify-content: center;
    margin-top: 47px;
  }
}
.flex {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  .circle {
    font-size: 20px;
    font-weight: 600;
    color: #2a7efb;
  }
  .text {
    margin-top: 15px;
    font-size: 12px;
    font-weight: 400;
    color: #999999;
  }
}

.ok-img {
  display: block;
  width: 90px;
  height: 92px;
  margin-top: 40px;
}
.ok-text {
  font-size: 20px;
  font-weight: 400;
  color: #333333;
  margin-top: 20px;
  margin-bottom: 20px;
}
.ok-btn {
  width: 210px;
  height: 40px;
  margin-top: 15px;
  text-align: center;
  cursor: pointer;
  font-size: 14px;
}

::v-deep .el-dialog {
  border-radius: 10px;
}
::v-deep .el-dialog__body {
  padding: 0;
}
</style>
