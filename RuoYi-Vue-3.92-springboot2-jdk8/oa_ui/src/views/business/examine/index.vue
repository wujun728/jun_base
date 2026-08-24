<!-- 人事-办公审批-审批设置 checkBtn：审批列表/审批设置 -->
<template>
  <div class="divBox">
    <div v-if="checkBtn">
      <el-card class="normal-page p20" style="padding-bottom: 0">
        <div class="flex-between">
          <div class="title-16">审批列表</div>
          <el-button
            type="primary"
            size="small"
            icon="el-icon-plus"
            @click="
              checkBtn = !checkBtn
              mockData = {}
              activeName = 'basicSetting'
            "
          >
            {{ $t('business.addBusiness') }}
          </el-button>
        </div>
        <div class="total-16 mb10 mt20">共 {{ total }} 条</div>
        <div v-if="tableData.length > 0">
          <el-table :data="tableData" v-loading="loading" :height="tableHeight1">
            <el-table-column prop="name" :label="$t('business.businessType')" min-width="230" show-overflow-tooltip>
              <template slot-scope="scope">
                <div class="flex">
                  <div class="selIcon" :style="{ backgroundColor: scope.row.color }">
                    <i class="icon iconfont" :class="scope.row.icon"></i>
                  </div>
                  <div class="ml10">{{ scope.row.name || '--' }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="refuse" :label="$t('business.Range')" min-width="200" show-overflow-tooltip>
              <template slot-scope="scope">
                <span
                  v-if="
                    scope.row.process &&
                    scope.row.process.info.depList.length <= 0 &&
                    scope.row.process.info.userList.length <= 0
                  "
                >
                  所有人
                </span>
                <div v-else class="refuse-info">
                  <div v-if="scope.row.process">
                    <span v-for="item in scope.row.process.info.depList" :key="item.id">{{ item.name }}</span>
                    <span v-for="item in scope.row.process.info.userList" :key="item.id">{{
                      item.card ? item.card.name : item.name
                    }}</span>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="sort" label="排序" min-width="110" show-overflow-tooltip />

            <el-table-column
              prop="updated_at"
              :label="$t('toptable.updatetime')"
              min-width="110"
              show-overflow-tooltip
            />
            <el-table-column :label="$t('hr.state')" width="160" show-overflow-tooltip>
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.status"
                  :active-text="$t('hr.open')"
                  :inactive-text="$t('hr.close')"
                  :active-value="1"
                  :inactive-value="0"
                  @change="handleStatus(scope.row)"
                />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" show-overflow-tooltip>
              <template slot-scope="scope">
                <el-button type="text" @click="handleEdit(scope.row)" v-hasPermi="['business:examine:index:edit']">{{
                  $t('public.edit')
                }}</el-button>

                <el-button
                  type="text"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['business:examine:index:delete']"
                  >{{ $t('public.delete') }}</el-button
                >
              </template>
            </el-table-column>
          </el-table>

          <div class="page-fixed">
            <el-pagination
              :page-size="where.limit"
              :current-page="where.page"
              :page-sizes="[15, 20, 30]"
              layout="total, sizes,prev, pager, next, jumper"
              :total="total"
              @size-change="handleSizeChange"
              @current-change="pageChange"
            />
          </div>
        </div>
        <div v-else>
          <default-page v-height :index="14" :min-height="520" />
        </div>
      </el-card>
    </div>

    <!-- 审批设置配置 -->
    <div v-if="!checkBtn" class="examineBox">
      <el-card class="examineCard">
        <el-row>
          <el-col :span="8">
            <el-page-header content="添加审批流程页面">
              <div slot="title" @click="backFn">
                <i class="el-icon-arrow-left"></i>
                返回
              </div>
            </el-page-header>
          </el-col>
          <el-col :span="16">
            <el-tabs v-model="activeName" class="examineTabs" @tab-click="handleClick(activeName)">
              <el-tab-pane v-for="(item, index) in tabArray" :key="index" :name="item.value">
                <div slot="label">
                  <span class="sp1">{{ item.number }}</span>
                  <span>{{ item.label }}</span>
                </div>
              </el-tab-pane>
            </el-tabs>
          </el-col>
        </el-row>
      </el-card>
      <div class="mt14 card-box">
        <!-- 基础设置 -->
        <basic-setting
          v-show="activeName === 'basicSetting'"
          ref="basicSetting"
          tab-name="basicSetting"
          :conf="mockData.baseConfig"
        ></basic-setting>
        <!-- 表单配置 -->
        <form-setting
          v-show="activeName === 'formSetting'"
          ref="formSetting"
          tab-name="formSetting"
          :conf="mockData.formConfig"
        ></form-setting>
        <!-- 流程配置 -->
        <rule-setting
          v-show="activeName === 'ruleSetting'"
          ref="ruleSetting"
          tab-name="ruleSetting"
          :conf="mockData.ruleConfig"
        ></rule-setting>
        <!-- 规则配置 -->
        <process-setting
          v-show="activeName === 'processSetting'"
          ref="processSetting"
          tab-name="processSetting"
          :conf="mockData.processConfig"
        ></process-setting>
      </div>
    </div>
    <div class="cr-bottom-button btn-shadow" v-if="!checkBtn">
      <el-button size="small" @click="previousStep" v-if="activeName !== 'basicSetting'">上一步</el-button>
      <el-button size="small" @click="nextStep" v-if="activeName !== 'ruleSetting'">下一步</el-button>
      <el-button type="primary" size="small" @click="publish">发布</el-button>
    </div>
  </div>
</template>

<script>
import { entAddApi, entListApi, entInfoApi, entEditApi, entDeleteApi, entChangeApi } from '@/api/business'
export default {
  name: 'BusinessExamine',
  components: {
    basicSetting: () => import('../components/basicSetting/index'),
    formSetting: () => import('../components/formSetting/index'),
    processSetting: () => import('../components/processSetting/index'),
    ruleSetting: () => import('../components/ruleSetting/index'),
    defaultPage: () => import('@/components/common/defaultPage')
  },
  data() {
    return {
      mockData: {},
      checkBtn: true,
      tableData: [],
      where: {
        page: 1,
        limit: 15
      },
      tableHeight1: window.innerHeight - 244 + 'px',
      total: 0,
      repeatData: {},
      key: 'cate',
      activeName: 'basicSetting',
      tabArray: [
        { label: this.$t('business.basicConfiguration'), value: 'basicSetting', number: 1 },
        { label: this.$t('business.formConfiguration'), value: 'formSetting', number: 2 },
        { label: this.$t('business.processSetting'), value: 'processSetting', number: 3 },
        { label: this.$t('business.ruleConfiguration'), value: 'ruleSetting', number: 4 }
      ],
      id: 0,
      conditions: ['inputNumber', 'radio', 'checkbox', 'select', 'departmentTree', 'timeFrom', 'moneyFrom'],
      loading: false
    }
  },
  mounted() {
    this.getTableData()
  },
  methods: {
    backFn() {
      this.checkBtn = true
      this.id = 0
    },
    goBack() {
      this.getTableData()
      this.checkBtn = !this.checkBtn
    },
    getCmpData(name) {
      return this.$refs[name].getData()
    },
    // 上一步
    previousStep() {
      if (this.activeName == 'basicSetting') {
        this.activeName = 'basicSetting'
      }
      if (this.activeName == 'formSetting') {
        this.activeName = 'basicSetting'
      }
      if (this.activeName == 'processSetting') {
        this.activeName = 'formSetting'
      }
      if (this.activeName == 'ruleSetting') {
        this.activeName = 'processSetting'
      }
    },

    // 下一步
    nextStep() {
      if (this.activeName == 'basicSetting') {
        this.activeName = 'formSetting'
      } else if (this.activeName == 'formSetting') {
        this.activeName = 'processSetting'
        this.handleClick('processSetting')
      } else if (this.activeName == 'processSetting') {
        this.activeName = 'ruleSetting'
      } else if (this.activeName == 'ruleSetting') {
        this.$message.error('没有下一步')
        this.activeName = 'ruleSetting'
      }
    },
    publish() {
      const p1 = this.getCmpData('basicSetting')
      const p2 = this.getCmpData('formSetting')
      const p3 = this.getCmpData('ruleSetting')
      const p4 = this.getCmpData('processSetting')
      Promise.all([p1, p2, p3, p4])
        .then((res) => {
          const param = {
            baseConfig: res[0].examineFrom,
            formConfig: res[1].formData,
            ruleConfig: res[2].ruleFrom,
            processConfig: res[3]
          }
          const propsParam = param.formConfig.props
          for (let i = 0; i < propsParam.length; i++) {
            if (propsParam[i].type === 'approvalBill' && !propsParam[i].children) {
              this.$message.error(`${propsParam[i].title}中至少添加一项内容`)
              return false
            }
          }
          this.sendToServer(param)
        })
        .catch((err) => {
          this.activeName = err.target
          if (this.activeName === 'processSetting') {
            this.$refs.processSetting.tipVisible = true
          } else {
            this.$refs.processSetting.tipVisible = false
          }
          err.target && (this.activeStep = err.target)
          err.msg && this.$message.warning(err.msg)
        })
    },
    sendToServer(param) {
      this.loading = true
      const config = {
        refillFrom: '2',
        leaveFrom: '1',
        overtimeFrom: '3',
        outFrom: '4',
        tripFrom: '5',
        contractPayment: '6',
        contractRenewal: '7',
        contractExpenditure: '8',
        issueInvoice: '9',
        voidedInvoice: '10'
      }
      param.baseConfig.types = 0
      param.formConfig.props.map((val) => {
        if (config.hasOwnProperty(val.type)) {
          param.baseConfig.types = config[val.type]
        }
      })
      if (this.id > 0) {
        entEditApi(this.id, param)
          .then((res) => {
            this.id = 0
            this.goBack()
            this.loading = false
          })
          .catch((err) => {
            this.loading = false
          })
      } else {
        entAddApi(param)
          .then((res) => {
            this.id = 0
            this.goBack()
            this.loading = false
          })
          .catch((err) => {
            this.loading = false
          })
      }
    },
    handleClick(tab) {
      this.activeName = tab
      if (tab === 'processSetting') {
        const p2 = this.getCmpData('formSetting')
        Promise.all([p2])
          .then((res) => {
            const formConfig = []
            const resData = res[0].formData.props
            if (resData.length > 0) {
              resData.forEach((value) => {
                if (value.children) {
                  value.children.map((per) => {
                    formConfig.push(per)
                  })
                }

                if (this.conditions.includes(value.type)) {
                  value.disabled = false
                  if (value.type === 'timeFrom') {
                    value.title = value.props.titleIpt
                    value.timeType = value.props.timeType
                  }

                  formConfig.push(value)
                }
              })
            }
            formConfig.unshift({
              disabled: false,
              field: '',
              props: {
                member: false
              },
              title: '申请人',
              type: 'departmentTree'
            })

            this.$store.commit('upDateFormSetting', formConfig)
          })
          .catch((err) => {
            err.target && (this.activeStep = err.target)
            err.msg && this.$message.warning(err.msg)
          })
      }
    },
    pageChange(page) {
      this.where.page = page
      this.loading = true
      this.getTableData()
      this.loading = false
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    // 获取表格数据
    async getTableData() {
      const result = await entListApi(this.where)
      this.tableData = result.data.list
      this.total = result.data.count
    },
    // 编辑状态
    async handleStatus(item) {
      await entChangeApi(item.id, { status: item.status })
      this.getTableData()
    },
    // 编辑
    async handleEdit(item) {
      this.activeName = 'basicSetting'
      this.id = item.id
      entInfoApi(item.id).then((res) => {
        this.checkBtn = !this.checkBtn
        if (res.data) {
          let data = res.data.formConfig.props
          if (data) {
            data.forEach((item) => {
              if ((item.type === 'moneyFrom' || item.type === 'inputNumber') && item.props) {
                if (item.props.max) {
                  item.props.max = Number(item.props.max)
                }
                if (item.props.min) {
                  item.props.min = Number(item.props.min)
                }
                if (item.props.precision) {
                  item.props.precision = Number(item.props.precision)
                }
              }
            })
          }
        }
        this.mockData = res.data
      })
    },
    // 删除
    handleDelete(item) {
      this.$modalSure(this.$t('business.message01')).then(() => {
        entDeleteApi(item.id)
          .then((res) => {
            if (this.where.page > 1 && this.tableData.length <= 1) {
              this.where.page--
            }
            this.getTableData()
          })
          .catch((error) => {})
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@media screen and (max-width: 995px) {
  .btn {
    float: left !important;
    margin-bottom: 20px;
  }
}

.p20 {
  padding: 20px;
}
::v-deep .el-card {
  transition: 0s;
}
::v-deep .from-foot-btn {
  // width: calc(100% - 29px);
  width: 100%;
}
::v-deep .el-tabs__header {
  margin: 0;
  margin-top: 8px;
}
::v-deep.el-page-header {
  margin-top: 4px;
}

::v-deep .el-table th {
  background-color: #f7fbff;
}
::v-deep .el-icon-back {
  display: none;
}
.examineTabs {
  ::v-deep .el-tabs__nav-wrap::after {
    background-color: #fff;
  }
  .sp1 {
    width: 16px;
    height: 16px;
    border: 1px solid #999999;
    border-radius: 50%;
    display: inline-block;
    text-align: center;
    line-height: 14px;
    font-size: 12px;
  }
  ::v-deep.el-tabs__nav-scroll {
    display: flex;
    justify-content: left;
  }
  ::v-deep .el-tabs__item {
    color: #999999;
    font-weight: 600;
    &.is-active {
      color: #1890ff;
      .sp1 {
        border-color: #1890ff;
        background-color: #1890ff;
        color: #fff;
      }
    }
  }
}
.examineBox {
  // padding: 14px;
}
.card-box {
  height: calc(100vh - 190px);
  overflow-y: scroll;
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
  .main {
    width: 800px;
    margin: 0 auto;
  }
}
.examineCont {
  min-width: 798px;
}
.normal-page {
  transition: 0s;
}
.examineCard {
  margin: 0;

  border: none;
  ::v-deep .el-card__body {
    padding: 10px 20px 0 20px;
  }
  .return {
    margin-top: 6px;
  }
  .title {
    display: block;
    line-height: 37px;
  }
}
.refuse-info {
  span {
    padding-right: 6px;
    &:last-of-type {
      padding-right: 0;
    }
  }
}
.selIcon {
  width: 25px;
  height: 25px;
  line-height: 25px;
  display: inline-block;
  text-align: center;
  border-radius: 3px;
  .iconfont {
    font-size: 13px;
    color: #fff;
  }
}

::v-deep.el-card__body {
  padding: 0;
}
.cr-bottom-button {
  position: fixed;
  left: -20px;
  right: 0;
  bottom: 0;
  width: calc(100% + 220px);
}
</style>
