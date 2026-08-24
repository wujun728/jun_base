<template>
  <div>
    <!-- 上级评价页面 -->
    <div>
      <div v-if="tableData.length > 0" class="current-list">
        <div class="mt15 mb15 flex flex-between">
          <div>
            <el-button
              v-if="!testAccess && dimensionMax > 0"
              :disabled="dimensionMax <= 0"
              size="small"
              type="primary"
              @click="addAssess"
            >
              添加考核维度
            </el-button>
            <div v-else class="title-16">绩效考核</div>
          </div>

          <div class="total-score">
            <p>
              <span class="total-score-name">{{ $t('access.totalscore') }}:</span>
              <span class="number">{{ assessInfo.max || max }}</span>
            </p>
            <p>
              <span class="total-score-name">个人评分:</span>
              <span class="color-default number">{{ finishRatioSum() }}</span>
            </p>
            <p v-if="!testAccess">
              <span class="total-score-name">当前总分:</span>
              <span class="number color-default">{{ total }}</span>
            </p>
          </div>
        </div>
        <el-scrollbar>
          <div v-for="(item, index) in tableData" :key="index" class="current-table">
            <div class="current-table-title">
              <div class="left">
                <template v-if="(!testAccess && !isShow) || processIndex == 1">
                  <span>
                    <input
                      v-model="item.name"
                      :autofocus="true"
                      :style="{ width: text(item.name) }"
                      class="current-title-input"
                      type="text"
                    />
                  </span>

                  <span>
                    {{ assessInfo.types == 0 ? $t('access.dimensionweight') : $t('access.dimensionscore') }}

                    <el-input-number
                      v-model="item.ratio"
                      :controls="false"
                      :max="item.weight"
                      :min="0"
                      :precision="0"
                    />
                    {{ assessInfo.types == 0 ? '%' : '' }}
                  </span>
                  <span class="pl10">当前得分</span>
                  <span class="color-default">{{ currentSum(item, index) }}</span>
                </template>
                <template v-else>
                  <span>{{ item.name }}</span>
                  <span>
                    {{ assessInfo.types == 0 ? $t('access.dimensionweight') : $t('access.dimensionscore') }}
                    {{ item.ratio }}
                    {{ assessInfo.types == 0 ? '%' : '' }}
                  </span>
                  <span class="pl10">当前得分</span>
                  <span class="color-default">{{ currentFinishSum(item) }}</span>
                </template>
              </div>
              <div v-if="!testAccess" class="text-right right">
                <i class="el-icon-delete" @click="deleteTargetList(index, item)" />
              </div>
            </div>
            <el-table :data="item.target" default-expand-all row-key="id" style="width: 100%">
              <el-table-column :label="$t('access.targetname')" prop="name" width="160">
                <template slot-scope="scope">
                  <el-input
                    v-if="!testAccess && !verifyAccess && strType !== 'check'"
                    v-model="scope.row.name"
                    :placeholder="$t('access.placeholder01')"
                    autosize
                    style="width: 200px"
                    type="textarea"
                  />
                  <span v-else>{{ scope.row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column :label="$t('access.targetexplain')" min-width="300" prop="content">
                <template slot-scope="scope">
                  <el-input
                    v-if="!testAccess && !verifyAccess && strType !== 'check'"
                    v-model="scope.row.content"
                    :disabled="testAccess"
                    :placeholder="$t('access.placeholder02')"
                    autosize
                    type="textarea"
                  />
                  <span v-else>{{ scope.row.content }}</span>
                </template>
              </el-table-column>

              <el-table-column label="评分标准" min-width="300" prop="info">
                <template slot-scope="scope">
                  <el-input
                    v-if="!testAccess && !verifyAccess && strType !== 'check'"
                    v-model="scope.row.info"
                    :disabled="testAccess"
                    autosize
                    placeholder="输入评分标准"
                    type="textarea"
                  />
                  <span v-else>{{ scope.row.info }}</span>
                </template>
              </el-table-column>

              <el-table-column label="完成情况 (自评)" prop="finish_info" width="200">
                <template slot-scope="scope">
                  <el-input
                    v-if="testAccess && strType !== 'check'"
                    v-model="scope.row.finish_info"
                    :placeholder="$t('access.inputcompletion')"
                    autosize
                    type="textarea"
                  />
                  <span v-else>{{ scope.row.finish_info }}</span>
                </template>
              </el-table-column>
              <el-table-column v-if="assessInfo.status > 1" label="上级评价" min-width="150" prop="check_info">
                <template slot-scope="scope">
                  <el-input
                    v-if="!testAccess && strType !== 'check'"
                    v-model="scope.row.check_info"
                    autosize
                    placeholder="上级评价"
                    type="textarea"
                  />
                  <span v-else>{{ scope.row.check_info }}</span>
                </template>
              </el-table-column>
              <el-table-column :label="assessInfo.types === 0 ? '权重%' : '分数'" min-width="60" prop="ratio">
                <template slot-scope="scope">
                  <el-input
                    v-if="!testAccess && !verifyAccess && processIndex === 1"
                    v-model="scope.row.ratio"
                    :max="1000"
                    :min="0"
                    :precision="0"
                    controls-position="right"
                    placeholder="0"
                    style="width: 120px"
                    @change="handleScore(index)"
                  >
                  </el-input>
                  <span v-else>{{ scope.row.ratio }}</span>
                  <span v-if="assessInfo.types == 0">%</span>
                </template>
              </el-table-column>
              <el-table-column v-if="assessInfo.types === 0" label="完成度 (自评)" min-width="100" prop="finish_ratio">
                <template slot-scope="scope">
                  <div class="current-task">
                    <el-progress
                      v-if="testAccess && strType !== 'check'"
                      :percentage="Number(scope.row.finish_ratio)"
                      :show-text="false"
                      :stroke-width="3"
                      :width="18"
                      type="circle"
                    />
                    <el-progress
                      v-else
                      :percentage="Number(scope.row.finish_ratio)"
                      :show-text="false"
                      :stroke-width="3"
                      :width="18"
                      type="circle"
                    />

                    <el-input
                      v-if="testAccess && strType !== 'check'"
                      v-model="scope.row.finish_ratio"
                      :min="0"
                      type="number"
                      @change="numberChange(scope.row.finish_ratio, 100, index, scope.$index)"
                    ></el-input>
                    <span v-else class="padding">{{ scope.row.finish_ratio }}</span>
                    <span>%</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column v-else label="自评分" min-width="60" prop="finish_ratio">
                <template slot-scope="scope">
                  <el-input
                    v-if="testAccess && strType !== 'check'"
                    v-model="scope.row.finish_ratio"
                    :min="0"
                    type="number"
                    @change="numberChange(scope.row.finish_ratio, scope.row.ratio, index, scope.$index)"
                  ></el-input>

                  <span v-else class="padding">{{ scope.row.finish_ratio }}</span>
                </template>
              </el-table-column>
              <!-- assessInfo.types === 0是加权方式 -->
              <el-table-column
                v-if="assessInfo.types === 0 && assessInfo.status > 1"
                label="上级评分"
                min-width="80"
                prop="score"
              >
                <template slot-scope="scope">
                  <div class="flex current-task">
                    <el-input
                      v-if="!testAccess && strType !== 'check'"
                      v-model="scope.row.score"
                      :min="0"
                      type="number"
                      @change="scopeChange(scope.row.score, 100, index, scope.$index)"
                    ></el-input>

                    <span v-else>{{ scope.row.score }}</span>
                    <span>%</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                v-if="assessInfo.types === 1 && assessInfo.status > 1"
                label="上级评分"
                min-width="80px"
                prop="score"
              >
                <template slot-scope="scope">
                  <el-input
                    v-if="!testAccess && strType !== 'check'"
                    v-model="scope.row.score"
                    :min="0"
                    type="number"
                    @change="scopeChange(scope.row.score, scope.row.ratio, index, scope.$index)"
                  ></el-input>

                  <span v-else>{{ scope.row.score }}</span>
                </template>
              </el-table-column>
              <el-table-column
                v-if="!testAccess"
                :label="$t('public.operation')"
                fixed="right"
                prop="address"
                width="70"
              >
                <template slot-scope="scope">
                  <el-button
                    class="current-delete"
                    icon="el-icon-delete"
                    type="text"
                    @click="handleDelete(index, scope.$index, scope.row)"
                  />
                </template>
              </el-table-column>
            </el-table>
            <div v-if="!testAccess" class="current-bottom">
              <el-row>
                <el-col :lg="12">
                  <el-button type="text" @click="addTarget(index)">{{ $t('access.addTarget02') }}</el-button>
                  <el-button type="text" @click="selectTarget(index)">{{ $t('access.addTarget01') }}</el-button>
                </el-col>
              </el-row>
            </div>
          </div>
          <div v-if="verifyAccess" class="comment-list">
            <el-row v-for="(item, index) in reply" :key="index" class="mb15">
              <el-col :lg="2" class="comment-list-left">
                <img :src="item.user.avatar" alt="" />
              </el-col>
              <el-col :lg="22">
                <p class="comment-list-name">
                  {{ item.user.name }}
                  <span v-if="item.types === -1">(自评)</span>
                  <span v-if="item.types === 0">(上级评价)</span>
                  <span v-if="item.types === 1">(绩效审核)</span>
                </p>

                <el-input
                  v-model="item.content"
                  :disabled="userInfo.id !== assessInfo.test_uid || item.types === 1"
                  :placeholder="$t('access.placeholder16')"
                  :rows="3"
                  resize="none"
                  type="textarea"
                />
              </el-col>
            </el-row>
          </div>
          <div class="comment-list">
            <!-- 自评 -->
            <el-row class="mb15">
              <el-col :lg="2" class="comment-list-left">
                <img :src="assessInfo.test.avatar" alt="" />
              </el-col>
              <el-col :lg="22">
                <p class="comment-list-name">{{ assessInfo.test ? assessInfo.test.name : userInfo.name }} (自评)</p>

                <el-input
                  v-model="assessInfo.self_reply"
                  :disabled="!testAccess || strType == 'check'"
                  :placeholder="$t('access.placeholder16')"
                  type="textarea"
                />
              </el-col>
            </el-row>
            <!-- 上级备注 -->
            <el-row v-if="processIndex > 1" class="mb15">
              <el-col :lg="2" class="comment-list-left">
                <img :src="assessInfo.check ? assessInfo.check.avatar : userInfo.avatar" alt="" />
              </el-col>
              <el-col :lg="22">
                <p class="comment-list-name">{{ assessInfo.check ? assessInfo.check.name : userInfo.name }} (备注)</p>
                <el-input
                  v-model="assessInfo.reply"
                  :disabled="!checkAccess || strType == 'check'"
                  :placeholder="$t('access.placeholder16')"
                  type="textarea"
                />
              </el-col>
            </el-row>
            <!-- 仅自己可见 -->
            <el-row v-if="checkAccess" class="mb15">
              <el-col :lg="2" class="comment-list-left">
                <i class="icon iconfont iconyincang1" />
              </el-col>
              <el-col :lg="22">
                <p class="comment-list-name">{{ userInfo.name }} (仅自己可见)</p>
                <el-input
                  v-model="assessInfo.hide_reply"
                  :disabled="strType == 'check'"
                  :placeholder="$t('access.tips12')"
                  type="textarea"
                />
              </el-col>
            </el-row>
          </div>
          <div class="current-footer text-center">
            <div>
              <span v-if="processIndex == 3">
                <el-button v-if="testAccess" size="small" @click="addFinance">{{ message2 }}</el-button>
                <el-button v-else v-show="verifyAccess" size="small" @click="addFinance">
                  {{ message1 }}
                </el-button>
              </span>
              <!-- <el-button v-if="checkAccess" size="small" @click="addTemplate">设置模板</el-button> -->

              <el-button
                v-if="currentButton && testAccess"
                :loading="loading"
                size="small"
                type="primary"
                @click="addPreserve"
              >
                {{ $t('access.saveandsubmit') }}
              </el-button>
            </div>
          </div>
        </el-scrollbar>
      </div>
    </div>
    <!--添加考核维度-->
    <el-dialog
      :before-close="handleClose"
      :title="$t('access.adddimension')"
      :visible.sync="dialogVisible"
      width="450px"
    >
      <div class="current-dialog mb15">
        <span>{{ $t('access.dimensionname') }}：</span>
        <el-input v-model="from.name" type="text" />
      </div>
      <div v-if="assessInfo.types === 0" class="current-dialog">
        <span>{{ $t('access.dimensionweight') }}(%)：</span>
        <div>
          <el-input-number
            v-model="from.ratio"
            :controls="false"
            :max="dimensionMax"
            :min="1"
            :precision="0"
            style="width: 100%"
          />
          <span v-if="assessInfo.types === 0" class="current-dialog-icon">%</span>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="clickCancel">{{ $t('public.cancel') }}</el-button>
        <el-button type="primary" @click="addOk">{{ $t('public.ok') }}</el-button>
      </span>
    </el-dialog>
    <setTemplate
      ref="setTemplate"
      :loading="templateLoading"
      :set-template="setTemplate"
      @handleTemplate="handleTemplate"
    ></setTemplate>
    <selectTarget ref="selectTarget" :title="$t('access.targetibrary')" @dialogChangeDada="dialogChangeDada" />
    <!-- 通用弹窗表单   -->
    <dialogForm ref="dialogForm" :form-data="formBoxConfig" :roles-config="rolesConfig" @isOk="isOk" />
  </div>
</template>

<script>
import {
  userAssessInfo,
  userAssessMark,
  userAssessTarget,
  userAssessSelfSaveApi,
  userSuperiorEvalSaveApi,
  userExamineEvalSaveApi,
  userAssessEditApi
} from '@/api/user'
export default {
  name: 'Execution',
  components: {
    selectTarget: () => import('@/components/form-common/dialog-target'),
    dialogForm: () => import('../components/index'),
    setTemplate: () => import('./setTemplate')
  },
  props: {
    id: {
      type: Number,
      default: 0
    },
    strType: {
      type: String,
      default: ''
    },
    processIndex: {
      type: Number,
      default: 0
    },
    reply: {
      type: Array,
      default: () => {
        return []
      }
    },

    isShow: {
      type: Number | Boolean,
      default: false
    },
    appeal: {
      type: Object,
      default: () => {
        return {}
      }
    },
    marks: {
      type: Object,
      default: () => {
        return {}
      }
    },
    currentButton: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      tableData: [],
      commentList: [],
      type: 0,
      is_draft: 0,
      mark: '',
      assessInfo: [],
      testAccess: false,
      checkAccess: false,
      verifyAccess: false,
      onlyMark: '',
      dialogVisible: false,
      from: {
        name: '',
        ratio: 100
      },
      max: JSON.parse(localStorage.getItem('enterprise')).maxScore,
      message1: '驳回申诉',
      message2: '绩效申诉',
      message3: '请输入驳回理由',
      message4: '请输入申诉理由',
      rolesConfig: [],
      onlyShow: '',
      formBoxConfig: {},
      spaceIds: [],
      targetIds: [],
      testShow: true,
      checkShow: true,
      loading: false,
      templateLoading: false,
      setTemplate: {
        success: 0
      },
      total: 0,
      userInfo: this.$store.state.user.userInfo,
      enterprise: this.$store.state.user.enterprise
    }
  },
  computed: {
    dimensionMax() {
      var max = 100
      if (this.assessInfo.types === 1) {
        max = Number(this.assessInfo.total)
      }
      if (this.tableData && this.tableData.length > 0) {
        this.tableData.forEach((value) => {
          max -= value.ratio
        })
      }
      return max
    },
    text() {
      return function (value) {
        if (value == '' || value == 0) {
          return '100%'
        } else {
          return String(value).length * 13 + 10 + 'px'
        }
      }
    }
  },
  watch: {
    reply: {
      handler(nVal, oVal) {
        // 判断考核人是否有评论
        if (this.reply.length > 0) {
          this.reply.forEach((value) => {
            if (value.types === -1) {
              this.testShow = false
            } else if (value.types === 0) {
              this.checkShow = false
            }
          })
        }
      },
      deep: true
    },
    marks: {
      handler(nVal) {
        this.marks.content === undefined ? (this.onlyMark = '') : (this.onlyMark = this.marks.content)
      },
      deep: true
    }
  },
  mounted() {
    this.getTableData()
  },
  methods: {
    // 控制输入分数的最大值-自评
    numberChange(val, maxNum, index, index2) {
      this.$nextTick(() => {
        if (val < 0) {
          this.tableData[index].target[index2].finish_ratio = 0
        } else if (val > maxNum) {
          this.tableData[index].target[index2].finish_ratio = maxNum
        }
      })
    },
    // 控制输入分数的最大值-上级评价
    scopeChange(val, maxNum, index, index2) {
      this.$nextTick(() => {
        if (val < 0) {
          this.tableData[index].target[index2].score = 0
        } else if (val > maxNum) {
          this.tableData[index].target[index2].score = maxNum
        }

        this.totalSum()
      })
    },
    async getTableData(val) {
      let result = {}
      result = await userAssessInfo(this.id)
      result.data == undefined ? (this.tableData = []) : (this.tableData = result.data.info)

      this.assessInfo = result.data ? result.data.assessInfo : {}
      this.totalSum()
      // 判断是否为被考核人
      this.testAccess = this.userInfo.id === this.assessInfo.test_uid
      // 判断是否为考核人
      this.checkAccess = this.userInfo.id === this.assessInfo.check_uid
      // 判断是否为审核人
      this.verifyAccess = this.userInfo.id === this.assessInfo.verify.id
    },
    addDraft() {
      this.preserve()
    },
    addPreserve(status) {
      this.onlyShow = status

      this.is_draft = 0
      this.preserve(status)
    },
    clickCancel() {
      this.dialogVisible = false
      this.from.name = ''
      this.from.ratio = this.dimensionMax
    },
    handleClose() {
      this.clickCancel()
    },
    // 添加考核维度
    addOk() {
      if (this.from.name == '') {
        this.$message.error(this.$t('access.placeholder03'))
      } else if (this.from.ratio == '') {
        this.$message.error(this.$t('access.placeholder04'))
      } else {
        this.tableData.push({
          name: this.from.name,
          ratio: this.assessInfo.types === 0 ? this.from.ratio : 0,
          target: [],
          id: 0
        })
        this.clickCancel()
      }
    },
    isOk() {},
    addAssess() {
      this.dialogVisible = true
    },
    // 编辑分数
    handleScore(index) {
      if (this.assessInfo.types === 0) {
        return false
      } else {
        this.tableData[index].ratio = 0
        this.tableData[index].target.forEach((value) => {
          this.tableData[index].ratio += Number(value.ratio)
        })
      }
    },
    // 删除维度
    deleteTargetList(index, item) {
      this.tableData.splice(index, 1)
      if (item.id !== 0) this.spaceIds.push(item.id)
    },
    // 删除指标
    handleDelete(index, $index, row) {
      this.tableData[index].target.splice($index, 1)
      if (row.id !== 0) this.targetIds.push(row.id)
      this.handleScore(index)
    },
    // 添加指标
    addTarget(index) {
      const len = this.tableData[index].target.length - 1
      if (len < 0) {
        this.tableData[index].target = [
          {
            name: '',
            content: '',
            ratio: '',
            id: 0,
            finish_info: '',
            finish_ratio: 0,
            check_info: '',
            score: 0
          }
        ]
      } else {
        if (this.tableData[index].target[len].name == '') {
          this.$message.error(this.$t('access.placeholder09'))
        } else if (this.tableData[index].target[len].content == '') {
          this.$message.error(this.$t('access.placeholder10'))
        } else if (this.tableData[index].target[len].ratio == '') {
          this.$message.error(this.$t('access.placeholder11'))
        } else {
          this.tableData[index].target.push({
            name: '',
            content: '',
            ratio: '',
            id: 0,
            finish_info: '',
            finish_ratio: 0,
            check_info: '',
            score: 0
          })
        }
      }
    },
    // 选择指标
    selectTarget(index) {
      this.index = index
      this.$refs.selectTarget.openDialog()
    },
    dialogChangeDada(data) {
      data.forEach((res) => {
        this.tableData[this.index].target.push({
          name: res.name,
          content: res.content,
          ratio: '',
          id: 0,
          finish_info: '',
          finish_ratio: 0,
          check_info: '',
          score: 0
        })
      })
    },

    // 保存绩效考核
    preserve(status) {
      let sum2 = 0
      this.tableData.map((item) => {
        sum2 += Number(item.ratio)
      })
      const res = this.judge()
      // if (sum2 != this.enterprise.maxScore) {
      //   this.$message.error(`维度分数${sum2}和总分不一致`)
      //   return false
      // }
      if (res.length <= 0) {
        return false
      }
      if (this.userInfo.id === this.assessInfo.test_uid) {
        // 自评
        this.type = 0
        this.mark = this.assessInfo.self_reply
      } else if (this.userInfo.id === this.assessInfo.check_uid) {
        this.mark = this.assessInfo.reply

        // 上级评价
        this.type = 1
      } else {
        // 绩效审核
        this.type = 2
      }
      const data = {
        types: this.type,
        is_submit: status == 'only' ? 0 : 1,
        is_draft: status == 'only' ? 1 : this.is_draft,
        mark: this.mark, // 上级评价 和自评

        hide_mark: this.assessInfo.hide_reply, // 上级评价(仅自己可见)
        space: this.spaceIds,
        target: this.targetIds,
        data: res
      }

      this.loading = true
      if (this.type == 0) {
        this.selfFn(data)
      } else if (this.type == 1) {
        this.superiorFn(data)
      } else if (this.type == 2) {
        this.examineFn(data)
      }
    },

    // 编辑绩效
    editUserAssess(data) {
      userAssessEditApi(this.id, data)
        .then((res) => {
          this.loading = false
          if (res.status == 200) {
            if (this.onlyShow !== 'only') {
              this.$emit('saveOk')
            }
          }
          this.is_draft = 0
          this.is_temp = 0
        })
        .catch((error) => {
          this.loading = false
        })
    },
    // 上级评价绩效
    evalUserAssess(data) {
      userSuperiorEvalSaveApi(this.id, data)
        .then((res) => {
          this.loading = false
          if (res.status == 200) {
            if (this.onlyShow !== 'only') {
              this.$emit('saveOk')
            }
          }
          this.is_draft = 0
          this.is_temp = 0
        })
        .catch((error) => {
          this.loading = false
        })
    },

    // 上级评价保存
    superiorFn(data) {
      if (this.strType == 'edit') {
        if (this.processIndex >= 3) {
          this.evalUserAssess(data)
        } else {
          this.editUserAssess(data)
        }
      } else {
        this.evalUserAssess(data)
      }
    },

    // 自评
    selfFn(data) {
      userAssessSelfSaveApi(this.id, data)
        .then((res) => {
          this.loading = false
          if (res.status == 200) {
            if (this.onlyShow !== 'only') {
              this.$emit('saveOk')
            }
          }
          this.is_draft = 0
          this.is_temp = 0
        })
        .catch((error) => {
          this.loading = false
        })
    },

    // 上上级评价
    examineFn(data) {
      userExamineEvalSaveApi(this.id, data)
        .then((res) => {
          this.loading = false
          if (res.status == 200) {
            if (this.onlyShow !== 'only') {
              this.$emit('saveOk')
            }
          }

          this.is_draft = 0
          this.is_temp = 0
        })
        .catch((error) => {
          this.loading = false
        })
    },
    judge() {
      var res = []
      if (this.tableData.length <= 0) {
        this.$message.error(this.$t('access.placeholder12'))
      } else if (this.dimensionMax > 0) {
        if (this.assessInfo.types === 1) {
          this.$message.error('分数之和必须为' + (this.assessInfo.max || this.max ) + '分')
        } else {
          this.$message.error(this.$t('access.placeholder13'))
        }
      } else {
        for (let i = 0; i < this.tableData.length; i++) {
          if (this.tableData[i].name == '') {
            this.$message.error('考核维度名称不能为空')
            break
          }
          if (this.tableData[i].ratio <= 0 && this.is_draft === 0) {
            this.$message.error('考核维度权重不能为零')
            break
          }
          if (this.tableData[i].target.length <= 0) {
            this.$message.error('考核指标不能空')
            break
          }
          const target = []
          for (let j = 0; j < this.tableData[i].target.length; j++) {
            if (this.tableData[i].target[j].name == '') {
              this.$message.error('指标名称不能为空')
              break
            }
            if (this.tableData[i].target[j].content == '') {
              this.$message.error('指标内容不能为空')
              break
            }
            if (this.tableData[i].target[j].ratio <= 0 && this.is_draft === 0) {
              this.$message.error('指标权重不能为零')
              break
            }
            target.push({
              id: this.tableData[i].target[j].id,
              name: this.tableData[i].target[j].name,
              content: this.tableData[i].target[j].content,
              info: this.tableData[i].target[j].info,
              finish_info: this.tableData[i].target[j].finish_info,
              finish_ratio: this.tableData[i].target[j].finish_ratio ? this.tableData[i].target[j].finish_ratio : 0,
              check_info: this.tableData[i].target[j].check_info,
              score: this.tableData[i].target[j].score === undefined ? 0 : this.tableData[i].target[j].score,
              ratio: this.tableData[i].target[j].ratio
            })
          }
          if (target.length <= 0) {
            break
          }
          res.push({
            id: this.tableData[i].id,
            name: this.tableData[i].name,
            ratio: this.tableData[i].ratio,
            target: target
          })
          if (res[i].target.length !== this.tableData[i].target.length) {
            res = []
          }
        }
      }
      if (res.length > 0) {
        return res
      } else {
        return []
      }
    },

    async preserveMark() {
      if (this.onlyMark === '') {
        this.$message.error('备注不能为空')
      } else {
        var data = {
          mark: this.onlyMark
        }
        await userAssessMark(this.id, data)
      }
    },
    // 个人评分计算
    finishRatioSum() {
      let number = 0
      let itemNumber = 0
      if (this.tableData && this.tableData.length > 0) {
        this.tableData.forEach((value) => {
          if (value.target.length > 0) {
            if (this.assessInfo.types === 0) {
              value.target.forEach((val) => {
                itemNumber += Number(
                  (value.ratio * val.ratio * (val.finish_ratio === undefined ? 0 : val.finish_ratio)) / 10000
                )
              })
            } else {
              value.target.forEach((val) => {
                itemNumber += Number(val.finish_ratio === undefined ? 0 : val.finish_ratio)
              })
            }
          } else {
            itemNumber = 0
          }
        })
      }
      if (this.assessInfo.types === 0) {
        number = Math.floor(Number(this.assessInfo.total) * itemNumber) / 100
      } else {
        number = itemNumber
      }
      return number
    },

    // 当前得分计算
    currentSum(row, index) {
      let number = 0
      if (row.target.length > 0) {
        if (this.assessInfo.types === 0) {
          row.target.forEach((val) => {
            number += Number((row.ratio * val.ratio * (val.score === undefined ? 0 : val.score)) / 10000)
          })
          number = Math.floor(Number(this.assessInfo.total) * number) / 100
        } else {
          row.target.forEach((val) => {
            number += Number(val.score === undefined ? 0 : val.score)
          })
        }
      }
      this.tableData[index].score = number
      this.totalSum()
      return number
    },

    // 当前得分计算--我的自评
    currentFinishSum(row) {
      let number = 0
      if (row.target.length > 0) {
        if (this.assessInfo.types === 0) {
          row.target.forEach((val) => {
            number += Number((row.ratio * val.ratio * (val.finish_ratio === undefined ? 0 : val.finish_ratio)) / 10000)
          })
          number = Math.floor(Number(this.assessInfo.total) * number) / 100
        } else {
          row.target.forEach((val) => {
            number += Number(val.finish_ratio === undefined ? 0 : val.finish_ratio)
          })
        }
      }
      return number
    },

    totalSum() {
      let number = 0
      let itemNumber = 0
      if (this.tableData && this.tableData.length > 0) {
        this.tableData.forEach((value) => {
          if (value.target.length > 0) {
            if (this.assessInfo.types === 0) {
              value.target.forEach((val) => {
                itemNumber += Number((value.ratio * val.ratio * (val.score === undefined ? 0 : val.score)) / 10000)
              })
            } else {
              value.target.forEach((val) => {
                itemNumber += Number(val.score === undefined ? 0 : val.score)
              })
            }
          } else {
            itemNumber = 0
          }
        })
      }
      if (this.assessInfo.types === 0) {
        number = Math.floor(Number(this.assessInfo.total) * itemNumber) / 100
      } else {
        number = itemNumber
      }
      this.total = number
    },

    // 绩效审核
    addFinance() {
      this.formBoxConfig = {
        title: this.testAccess ? this.message2 : this.message1,
        width: '500px',
        method: 'POST',
        action: '/assess/appeal/' + this.id
      }
      this.rolesConfig = [
        {
          type: 'input',
          field: 'content',
          value: '',
          title: this.testAccess ? this.message2 : this.message1,
          className: 'finance-from',
          props: {
            type: 'textarea',
            placeholder: this.testAccess ? this.message4 : '驳回原因'
          },
          col: {
            lg: { span: 22 }
          },
          validate: [
            {
              required: true,
              message: this.testAccess ? this.message4 : '驳回原因',
              type: 'string',
              trigger: 'change'
            }
          ]
        },
        {
          type: 'hidden',
          field: 'types',
          value: this.testAccess ? 0 : 1
        }
      ]
      this.$refs.dialogForm.openBox()
    },

    // 设置模板
    addTemplate() {
      const res = this.judge()
      if (res.length <= 0) {
        return false
      }
      this.$refs.setTemplate.openBox()
    },

    // 设置模板成功后监听
    handleTemplate(data) {
      const res = this.judge()
      data.data = res
      data.is_temp = 1
      data.is_draft = 0
      data.types = this.assessInfo.types
      this.preserveTemplate(data)
    },

    // 添加
    preserveTemplate(data) {
      this.templateLoading = true
      userAssessTarget(data)
        .then((res) => {
          this.templateLoading = false
          this.setTemplate.success = 1
          this.is_draft = 0
          this.is_temp = 0
        })
        .catch((error) => {
          this.templateLoading = false
          this.setTemplate.success = 1
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.text-right {
  text-align: right;
}
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
::v-deep .el-textarea__inner {
  padding-left: 0;
  padding-right: 0;
}
.total-score {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  height: 32px;
  padding: 0 10px;
  p {
    margin: 0 10px 0 0;
    padding: 0;
  }
  p:last-of-type {
    margin-right: 0;
  }
  .total-score-name {
    color: #7f7f7f;
    font-size: 13px;
  }
  .number {
    font-size: 17px;
  }
}
.current-table {
  // margin: 0 0 30px 0;
  margin: 0 12px 30px 0;
  .current-table-title {
    display: flex;
    align-items: center;
    padding: 5px 0 !important;
    background-color: #f3f8fe !important;
    .current-title-input {
      background-color: transparent;
      font-size: 13px;
      font-weight: bold;
      color: #000000;
      border: none;
    }
    .current-title-input:focus {
      outline-style: none;
    }
    .left {
      width: 90%;
      height: 46px;
      line-height: 46px;
    }
    .right {
      width: 10%;
      padding-right: 10px;
      i {
        cursor: pointer;
      }
    }
    padding: 10px 0;
    background-color: #f7fbff;
    span {
      font-size: 13px;
    }
    span:first-of-type {
      padding-left: 10px;
      font-size: 13px;
      font-weight: bold;
      color: #000000;
    }
  }
  ::v-deep .el-input-number--medium {
    width: 30px;
  }
  ::v-deep .el-table th > .cell {
    font-weight: normal;
    font-size: 13px;
  }
  ::v-deep .el-table--medium td {
    padding: 3px 0;
  }
  ::v-deep .el-input__inner {
    // height: 28px;
    line-height: 28px;
    // border: none;
    font-size: 13px;
    padding: 0 0 0 3px;
    text-align: left;
    background-color: transparent;
  }
  ::v-deep .el-textarea__inner {
    resize: none;
    overflow-y: hidden;
    border: none;
    background-color: transparent;
  }
  ::v-deep .el-table--enable-row-hover {
    .el-table__body tr:hover > td {
      background: transparent;
    }
  }
  ::v-deep .el-input__inner {
    // height: 28px;
    // line-height: 28px;
    // border: none;

    font-size: 13px;
    background-color: transparent;
  }
  .current-delete {
    color: #000000;
  }
  ::v-deep .el-input__inner {
    width: 100%;
    border: none;
    background-color: transparent;
    padding: 0 0 0 3px;
  }

  .current-task {
    display: flex;
    align-items: center;
    .el-input--medium,
    .el-input-number--medium {
      width: 30px;
    }
    .padding {
      padding: 0 5px;
    }
    .el-input__inner {
      width: 100%;
      border: none;
      background-color: transparent;
      padding: 0 0 0 3px;
    }
  }
}
.comment-list {
  .comment-list-left {
    width: 32px;
    height: 32px;
    margin-right: 15px;
    border-radius: 50%;
    background-color: #eeeeee;
    text-align: center;
    line-height: 32px;
    img {
      width: 32px;
      height: 32px;
      border-radius: 50%;
    }
    i {
      font-size: 16px;
    }
  }
  .comment-list-name {
    padding: 0;
    margin: 0 0 10px 0;
    font-size: 13px;
    font-weight: bold;
  }
  ::v-deep .el-textarea.is-disabled .el-textarea__inner {
    color: #666666;
  }

  ::v-deep .el-textarea__inner {
    padding: 5px 10px;
  }
}
.current-bottom {
  border-bottom: 1px solid #dfe6ec;
  padding: 2px 10px;
  button {
    font-size: 13px;
  }
}
.current-footer {
  margin-top: 20px;
  padding: 20px 0;
  // border-top: 4px solid #f0f2f5;
}
.current-dialog {
  display: flex;
  align-items: center;
  position: relative;
  span {
    display: block;
    width: 25%;
    text-align: right;
  }
  div {
    width: 75%;
  }
  .current-dialog-icon {
    position: absolute;
    right: 10px;
    width: auto;
    top: 12px;
  }
  ::v-deep .el-input__inner {
    text-align: left;
  }
}
::v-deep.el-table th.is-leaf {
  background-color: #fff !important;
}
// #
</style>
