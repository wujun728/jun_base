<template>
  <div>
    <el-drawer title="打卡记录详情" :visible.sync="drawer" size="600px" :before-close="handleClose">
      <div class="contract-body">
        <div class="mt20">
          <el-form label-width="110px">
            <div class="form-box">
              <div class="form-item">
                <el-form-item>
                  <span slot="label">姓名：</span>
                  <p>{{ info.card ? info.card.name : '--' }}</p>
                </el-form-item>
                <el-form-item>
                  <span slot="label">部门：</span>
                  <p>{{ (info.frame && info.frame.name) || '--' }}</p>
                </el-form-item>

                <el-form-item>
                  <span slot="label">考勤组名称：</span>
                  <p>{{ info.group || '--' }}</p>
                </el-form-item>
                <el-form-item>
                  <span slot="label">考勤日期：</span>
                  <p>{{ $moment(info.created_at).format('YYYY-MM-DD') }}</p>
                </el-form-item>
                <el-form-item>
                  <span slot="label">星期：</span>
                  <p>{{ getWeek(info.created_at) }}</p>
                </el-form-item>
                <el-form-item>
                  <span slot="label">班次：</span>
                  <p>{{ getShift(info.shift_data) }}</p>
                </el-form-item>
                <el-form-item>
                  <span slot="label">打卡时间：</span>
                  <p>{{ info.created_at }}</p>
                </el-form-item>
                <el-form-item>
                  <span slot="label">打卡地址：</span>
                  <p>{{ info.address }}</p>
                </el-form-item>
                <el-form-item>
                  <span slot="label">打卡备注：</span>
                  <p>{{ info.remark || '--' }}</p>
                </el-form-item>
                <el-form-item>
                  <span slot="label">打卡图片：</span>
                  <template v-if="info.image && info.image.length > 0">
                    <p v-for="(item, index) in info.image" :key="index">
                      <img class="image" :src="item" alt="" @click="lookViewer(item, '')" />
                    </p>
                  </template>
                  <span v-else>--</span>
                </el-form-item>
              </div>
            </div>
          </el-form>
        </div>
      </div>
    </el-drawer>
    <image-viewer ref="imageViewer" :src-list="srcList"></image-viewer>
  </div>
</template>

<script>
import { clockRecordDetails } from '@/api/config'
import { toGetWeek } from '@/utils/format'
export default {
  name: 'CrmebOaEntDetailsDrawer',
  data() {
    return {
      drawer: false,
      info: {},
      srcList: []
    }
  },

  components: {
    imageViewer: () => import('@/components/common/imageViewer')
  },

  methods: {
    async openBox(data) {
      const result = await clockRecordDetails(data.id)
      this.info = result.data
      this.info.image.map((item) => {
        this.srcList.push(item)
      })

      this.drawer = true
    },
    handleClose() {
      this.drawer = false
    },
    // 查看与下载附件
    lookViewer(url) {
      this.$refs.imageViewer.openImageViewer(url)
    },

    // 处理表格班次数据
    getShift(data) {
      let text2 = ''
      let text1 = ''
      if (data) {
        if (data.rules && data.rules.length > 0) {
          text1 = `${data.rules[0].first_day_after == 0 ? '当日' : '次日'}${data.rules[0].work_hours} - ${
            data.rules[0].second_day_after == 0 ? '当日' : '次日'
          }${data.rules[0].off_hours}`

          if (data.rules[1]) {
            text2 = `${data.rules[1].first_day_after == 0 ? '当日' : '次日'}${data.rules[1].work_hours} - ${
              data.rules[1].second_day_after == 0 ? '当日' : '次日'
            }${data.rules[1].off_hours}`
          }
        }
        return data.name || '--' + text1 + text2
      } else {
        return '--'
      }
    },
    getWeek(date) {
      // 参数时间戳
      return toGetWeek(date)
    }
  }
}
</script>

<style lang="scss" scoped>
.default {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 200px;
  .img {
    width: 200px;
    height: 150px;
  }
  .text {
    font-size: 14px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #c0c4cc;
  }
}
.contract-body {
  overflow-y: auto;
}

.description ::v-deep .el-form-item {
  margin-bottom: 0px;
}

::v-deep .el-form-item__label {
  font-size: 13px !important;
  font-weight: 400;
  color: #909399 !important;
}

.invoice-body1 {
  padding: 20px 30px 0px 15px !important;
}

.form-box {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  .form-item {
    ::v-deep .el-form-item__content {
      width: calc(100% - 110px);
      font-size: 13px !important;
    }
    ::v-deep .el-select--medium {
      width: 100%;
    }
    ::v-deep .el-form-item {
      margin-bottom: 0;
    }
    ::v-deep .el-textarea__inner {
      resize: none;
    }

    p {
      margin: 0;
      padding: 0;
      font-weight: 400 !important;
      color: #303133;
      font-size: 13px !important;
    }
  }
}
.image {
  width: 100px;
  height: 100px;
  margin-bottom: 5px;
}
</style>
