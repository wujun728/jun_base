<template>
  <div class="divBox">
    <!-- 假期类型表格 -->
    <div v-if="checkBtn">
      <el-card class="normal-page p20">
        <div>
          <div class="flex-between">
            <div class="title-16">假期类型</div>
            <el-button type="primary" icon="el-icon-plus" size="small" @click="addType()">新增</el-button>
          </div>
          <div class="total-16 mt20">共有 {{ total }} 条</div>
          <div class="mt14" v-if="tableData.length > 0">
            <el-table :data="tableData" :height="tableHeight">
              <el-table-column prop="name" label="假期类型" min-width="120">
                <template slot-scope="scope">
                  <div class="flex">
                    <div class="ml10">{{ scope.row.name }}</div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="refuse" label="请假单位" min-width="100">
                <template slot-scope="scope">
                  <span>{{ scope.row.duration_type == 1 ? '按小时' : '按天' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="sort" label="排序"></el-table-column>
              <el-table-column label="操作" width="200">
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
                layout="total,sizes, prev, pager, next, jumper"
                :total="total"
                @size-change="handleSizeChange"
                @current-change="pageChange"
              />
            </div>
          </div>
          <div v-else>
            <default-page v-height :index="14" :min-height="520" />
          </div>
        </div>
      </el-card>
    </div>

    <!-- 假期类型配置 -->
    <div v-if="!checkBtn" class="examineBox">
      <!-- <div> -->
      <el-card :body-style="{ padding: '14px' }" class="station-header">
        <el-row>
          <el-col :span="24">
            <el-page-header :content="this.id > 0 ? '编辑假期类型' : '添加假期类型'">
              <div slot="title" @click="back">
                <i class="el-icon-arrow-left"></i>
                返回
              </div>
            </el-page-header>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="box-card mt14">
        <div class="setting-container mt14">
          <el-row>
            <el-col v-bind="grid1">&nbsp;</el-col>
            <el-col v-bind="grid2">
              <el-form ref="elForm" :model="form" :rules="rules" label-width="100px" size="medium">
                <div class="card-list">
                  <div class="head">
                    <span class="title">基础信息设置</span>
                  </div>
                  <el-form-item label="假期类型：" prop="name">
                    <el-input
                      size="small"
                      style="width: 350px"
                      v-model="form.name"
                      show-word-limit
                      placeholder="请输入假期类型"
                      clearable
                    >
                    </el-input>
                  </el-form-item>
                  <el-form-item style="margin-bottom: 0" label="新员工请假：" prop="new_employee_limit">
                    <el-select v-model="form.new_employee_limit" style="width: 350px" size="small">
                      <el-option label="限制" :value="1"></el-option>
                      <el-option label="不限制" :value="0"></el-option>
                    </el-select>
                  </el-form-item>
                  <div class="infos">
                    <span v-if="form.new_employee_limit == 0" class="info">入职即可请假</span>
                    <div v-else class="info">
                      入职时长小于
                      <el-select v-model="form.new_employee_limit_month" style="width: 100px" size="small">
                        <el-option v-for="month in months" :key="month" :label="month" :value="month"></el-option>
                      </el-select>
                      个月时，不可申请此假期
                    </div>
                  </div>
                </div>

                <div class="card-list mt20">
                  <div class="head">
                    <span class="title">请假时长核算规则</span>
                  </div>
                  <el-form-item label="请假时长：" prop="duration_type">
                    <el-select v-model="form.duration_type" style="width: 350px" size="small">
                      <el-option label="按天" :value="0"></el-option>
                      <el-option label="按小时" :value="1"></el-option>
                    </el-select>
                    <div class="infos">
                      <span v-if="form.duration_type == 1" class="info">审批申请按分钟请假，打卡月报按小时统计</span>
                      <span v-else class="info">审批申请按半天请假，打卡月报按天统计</span>
                    </div>
                  </el-form-item>

                  <el-form-item label="排序：" prop="sort">
                    <el-input-number
                      v-model="form.sort"
                      :precision="0"
                      :min="0"
                      controls-position="right"
                      style="width: 350px"
                      size="small"
                    ></el-input-number>
                  </el-form-item>
                </div>
              </el-form>
            </el-col>
          </el-row>
        </div>
      </el-card>
      <!-- </div> -->
    </div>
    <div class="cr-bottom-button btn-shadow" v-if="!checkBtn">
      <el-button size="small" @click="checkBtn = true">取消</el-button>
      <el-button type="primary" size="small" @click="save">保存</el-button>
    </div>
  </div>
</template>

<script>
import {
  saveHolidayTypeApi,
  approveHolidayTypeApi,
  putHolidayTypeApi,
  approveHolidayTypeInfoApi,
  holidayTypeDeleteApi
} from '@/api/business'
export default {
  name: 'HolidayType',
  components: {
    defaultPage: () => import('@/components/common/defaultPage')
  },
  data() {
    return {
      checkBtn: true,
      tableData: [],
      where: {
        page: 1,
        limit: 15
      },
      months: [],
      rules: {
        name: [{ required: true, message: '请输入假期类型' }]
      },
      form: {
        name: '',
        new_employee_limit: 0,
        new_employee_limit_month: 1,
        duration_type: 1,
        sort: 0
        // duration_calc_type: 1
      },
      total: 0,
      repeatData: {},
      key: 'cate',
      tabArray: [
        { label: this.$t('business.formConfiguration'), value: 'formSetting', number: 2 },
        { label: this.$t('business.processSetting'), value: 'processSetting', number: 3 },
        { label: this.$t('business.ruleConfiguration'), value: 'ruleSetting', number: 4 }
      ],
      id: 0,
      grid1: {
        xl: 2,
        lg: 4,
        md: 2,
        sm: 24,
        xs: 24
      },
      grid2: {
        xl: 20,
        lg: 18,
        md: 20,
        sm: 24,
        xs: 24
      },
      conditions: ['inputNumber', 'radio', 'checkbox', 'select', 'departmentTree', 'timeFrom', 'moneyFrom'],
      loading: false
    }
  },
  created() {
    this.getTableData()
    for (let i = 1; i <= 12; i++) {
      this.months.push(i)
    }
  },
  methods: {
    goBack() {
      this.getTableData()
      this.checkBtn = !this.checkBtn
    },
    addType() {
      this.form = {
        name: '',
        new_employee_limit: 0,
        new_employee_limit_month: 1,
        duration_type: 1,
        sort: 0
        // duration_calc_type: 1
      }
      this.id = ''
      this.checkBtn = !this.checkBtn
    },
    getCmpData(name) {
      return this.$refs[name].getData()
    },
    // 保存
    save() {
      if (this.id) {
        putHolidayTypeApi(this.id, this.form).then((res) => {
          if (res.status == 200) {
            this.checkBtn = true
            this.getTableData()
          }
        })
      } else {
        saveHolidayTypeApi(this.form).then((res) => {
          if (res.status == 200) {
            this.checkBtn = true
            this.getTableData()
          }
        })
      }
    },
    // 返回
    back() {
      this.checkBtn = true
      this.getTableData()
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    // 获取假期类型列表
    async getTableData() {
      var data = {
        page: this.where.page,
        limit: this.where.limit
      }
      const result = await approveHolidayTypeApi(data)
      this.tableData = result.data.list
      this.total = result.data.count
    },
    // 编辑
    async handleEdit(item) {
      this.id = item.id
      approveHolidayTypeInfoApi(item.id).then((res) => {
        this.checkBtn = !this.checkBtn
        this.form = res.data
      })
    },
    // 删除
    handleDelete(item) {
      this.$modalSure('你确定要删除该假期类型吗').then(() => {
        holidayTypeDeleteApi(item.id)
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
.box-card {
  height: calc(100vh - 200px);
  overflow-y: scroll;
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
  height: calc(100vh - 150px);
  // overflow-y: scroll;
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
  padding-left: 20px;
  height: 70px;
  border-bottom: 1px solid #dcdfe6;
  font-family: PingFang SC, PingFang SC;
  font-weight: 500;
  font-size: 17px;
  color: #303133;
  line-height: 70px;
  .el-icon-arrow-left {
    cursor: pointer;
    margin-right: 10px;
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
  cursor: pointer;
  border-radius: 3px;
}
.iconfont {
  font-size: 13px;
  color: #fff;
}
::v-deep.el-card__body {
  padding: 0;
}
::v-deep.el-form-item {
  margin: 0;
}
.cr-bottom-button {
  position: fixed;
  left: -20px;
  right: 0;
  bottom: 0;
  width: calc(100% + 220px);
}
.setting-container {
  padding-bottom: 20px;
  .head {
    display: flex;
    margin-bottom: 20px;

    .title {
      padding-left: 6px;
      font-size: 14px;
      font-weight: 600;
      position: relative;
    }
    .title:before {
      content: '';
      background-color: #1890ff;
      width: 3px;
      height: 14px;
      position: absolute;
      left: -6px;
      top: 50%;
      margin-top: -7px;
    }
    .line {
      color: #1890ff;
    }
  }
}
.card-list {
  padding-left: 68px;
  // border-spacing: 2px;
}
.dash-line {
  width: 100%;
  height: 1px;
  background-image: linear-gradient(to right, #dcdfe6 0%, #dcdfe6 50%, transparent 50%);
  background-size: 12px 0.5px; //第一个值（20px）越大线条越长间隙越大
  background-repeat: repeat-x;
  margin-top: 20px;
}
.info {
  font-size: 12px;
  color: #909399;
  margin-left: 8px;
}

::v-deep.el-form-item {
  // display: flex;
  // align-items: center;
  // justify-content: flex-start;
  margin-bottom: 20px;
}
::v-deep.el-form-item__content {
  margin-left: 0 !important;
}
.infos {
  height: 14px;
  line-height: 14px;
  margin-left: 100px;
  margin-top: 14px;
}
</style>
