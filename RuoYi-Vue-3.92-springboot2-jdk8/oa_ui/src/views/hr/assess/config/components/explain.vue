<template>
  <div>
    <el-row>
      <el-col>
        <div class="main">
          <div class="title-16 mb20">考核评分</div>
          <div class="v-height-flag">
            <div class="input-suffix mb-14 pt-20" style="border: none">
              <label for="">{{ $t('access.scoretype') }}：</label>
              <el-radio v-model="radio" :label="1">{{ $t('access.scoretype1') }}</el-radio>
              <el-radio v-model="radio" :label="0">{{ $t('access.scoretype2') }}</el-radio>
            </div>
            <div class="input-suffix">
              <label for="">{{ $t('access.scoreshows') }}：</label>
              <el-input
                v-model="scoreMark"
                type="textarea"
                resize="none"
                autosize
                size="small"
                :placeholder="$t('access.placeholder16')"
              />
            </div>
            <div class="suffix-content v-height-flag">
              <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="name" :label="$t('access.classname')" width="130">
                  <template slot-scope="scope">
                    <el-input
                      style="width: 130px"
                      v-model="scope.row.name"
                      type="text"
                      size="small"
                      :placeholder="$t('access.placeholder17')"
                    />
                  </template>
                </el-table-column>
                <el-table-column :label="$t('access.fractionalrange')" width="260">
                  <template slot-scope="scope">
                    <div class="number-section">
                      <span class="number-box">
                        <span>{{ scope.row.min }}</span> <span>{{ scope.$index > 0 ? '&lt;' : '≤' }}</span>
                        <span>{{ $t('access.score') }}</span>
                        <span>≤</span>
                      </span>
                      <el-input-number
                        class="ml14"
                        v-model="scope.row.max"
                        :placeholder="$t('access.inputthescore')"
                        :controls="false"
                        :min="scope.row.min + 1"
                        :max="10000"
                        size="small"
                        @change="maxChange(scope.$index)"
                      />
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="mark" :label="$t('access.levelthat')">
                  <template slot-scope="scope">
                    <div class="number-explain">
                      <el-input
                        size="small"
                        v-model="scope.row.mark"
                        style="width: 367px"
                        clearable
                        type="text"
                        :placeholder="$t('access.levelthat')"
                      />
                      <div class="text-delete" @click="handleDelete(scope.$index)">{{ $t('public.delete') }}</div>
                    </div>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div class="color189 add-btn">
              <span @click="addAssess">+{{ $t('access.addarating') }}</span>
            </div>
            <!-- <div v-hasPermi="['hr:assessConfig:assessmentScore:save']">
              <el-button type="primary" size="small">{{ $t('public.save') }} </el-button>
            </div> -->
          </div>
        </div>
      </el-col>
    </el-row>
    <div class="cr-bottom-button">
      <el-button type="primary" size="small" :loading="loading" @click="handlePreserve">{{
        $t('public.save')
      }}</el-button>
    </div>
  </div>
</template>

<script>
import { assessScoreApi, assessScoreUpdateApi } from '@/api/enterprise'
import { loginInfo } from '@/api/user'
export default {
  name: 'AssessExplain',
  data() {
    return {
      tableData: [],
      scoreMark: '',
      radio: 0,
      loading: false
    }
  },
  created() {
    this.getAssessList()
  },
  methods: {
    async getAssessList() {
      const result = await assessScoreApi()
      this.scoreMark = result.data.score_mark
      this.radio = result.data.compute_mode
      this.tableData = result.data.score_list.list ? result.data.score_list.list : []
    },
    handleDelete(index) {
      this.$modalSure(this.$t('access.tips01')).then(() => {
        this.tableData.splice(index, 1)
      })
    },
    async getInfo() {
      const res = await loginInfo()
      this.$store.commit('user/SET_ENTINFO', res.data.enterprise)
    },
    handlePreserve() {
      const t = this.checkAssessList()
      if (t) {
        const data = {
          compute_mode: this.radio,
          score_mark: this.scoreMark,
          score_list: this.tableData
        }
        this.loading = true
        assessScoreUpdateApi(data)
          .then((res) => {
            if (res.status == 200) {
              this.loading = false
              this.getInfo()
            }
            this.getAssessList()
          })
          .catch((error) => {
            this.loading = false
          })
      }
    },
    maxChange(index) {
      this.tableData[index + 1].min = this.tableData[index].max
    },
    addAssess() {
      if (this.tableData && this.tableData.length > 0) {
        const lastIndex = this.tableData.length - 1
        const t = this.checkAssessList()
        if (t) {
          this.tableData.push({
            name: '',
            min: this.tableData[lastIndex].max,
            mark: '',
            max: ''
          })
        }
      } else {
        this.tableData.push({
          name: '',
          min: 0,
          mark: '',
          max: ''
        })
      }
    },
    checkAssessList() {
      if (this.tableData.length === 0) {
        this.$message.error(this.$t('access.tips10'))
        return false
      }
      const lastIndex = this.tableData.length - 1
      if (this.tableData[lastIndex].max == '') {
        this.$message.error(this.$t('access.tips02'))
      } else if (this.tableData[lastIndex].min >= this.tableData[lastIndex].max) {
        this.$message.error(this.$t('access.tips03'))
      } else if (this.tableData[lastIndex].name == '') {
        this.$message.error(this.$t('access.tips04'))
      } else if (this.tableData[lastIndex].mark == '') {
        this.$message.error(this.$t('access.tips05'))
      } else {
        return true
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.main {
  max-width: 800px;
}

.pl50 {
  padding-left: 250px;
}
.color189 {
  color: #1890ff;
}
.add-btn {
  margin-top: 20px;
  margin-bottom: 10px;
  font-size: 13px;
  span {
    cursor: pointer;
  }
}
.input-suffix {
  display: flex;
  padding-bottom: 20px;
  margin-bottom: 15px;

  label {
    width: 80px;
    font-weight: normal;
  }
  ::v-deep .el-textarea__inner {
    height: 80px !important;
  }
}
.suffix-content {
  ::v-deep .el-table:before {
    height: 0;
  }

  ::v-deep.el-table__row > td {
    border: none;
  }
  ::v-deep.el-table::before {
    height: 0px;
  }
  ::v-deep .el-table th {
    border: none !important;
    padding: 0px 0;
    background: #ffffff;

    .cell {
      background: #ffffff !important;
      font-weight: normal;
      font-size: 13px;
    }
  }
  ::v-deep .el-table td {
    border-bottom: none;
    // padding-top: 20px;
  }
  ::v-deep td.el-table__cell {
    border: none !important;
  }
  ::v-deep .el-table th {
    background: #ffffff !important;
  }
  ::v-deep .el-input-number--medium {
    width: 80px;
  }
  ::v-deep .el-input__inner {
    width: 80%;
    text-align: left;
    font-size: 14px;
  }
  .number-section {
    .number-box {
      display: inline-block;
      width: 80px;
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 13px;
      color: rgba(0, 0, 0, 0.851);
    }
    ::v-deep .el-input {
      width: 140px;
    }
  }
  .number-explain {
    display: flex;
    align-items: center;
    ::v-deep .el-input {
      width: 80%;
      .el-input__inner {
        width: 100%;
      }
    }
    .text-delete {
      cursor: pointer;
      width: 10%;

      text-align: right;
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 13px;
      color: rgba(0, 0, 0, 0.1922);
    }
    ::v-deep .disable {
      pointer-events: none;
    }
  }
}
.cr-bottom-button {
  position: fixed;
  left: -20px;
  right: 0;
  bottom: 0;
  width: calc(100% + 220px);
}

::v-deep.el-table .cell {
  padding-right: 0;
}
</style>
