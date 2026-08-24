<!-- 人事-职位管理-晋升说明 -->
<template>
  <div class="divBox">
    <el-card class="normal-page">
      <el-row>
        <el-col v-bind="gridl">
          <left @clickType="clickType"></left>
        </el-col>
        <!-- 右边晋升表格 -->
        <el-col class="assess-right" v-bind="gridr">
          <div class="header">
            <div class="title">
              {{ title }}
            </div>
            <span class="tips">列表支持拖拽排序</span>
          </div>

          <el-table ref="table" :data="list" class="table" row-key="id" style="width: 100%">
            <el-table-column label="职级" min-width="200" prop="rank">
              <template slot-scope="scope">
                <el-select
                  v-show="scope.row.show"
                  v-model="scope.row.rank"
                  collapse-tags
                  filterable
                  multiple
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.id"
                    :disabled="selectedList.includes(item.id)"
                    :label="item.label"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
                <div v-show="!scope.row.show">
                  <span v-if="scope.row.rank">
                    {{
                      getIdsArrayFn(options, scope.row.rank)
                        .map((item) => {
                          return item.label
                        })
                        .join('、')
                    }}</span
                  >
                </div>
              </template>
            </el-table-column>
            <el-table-column label="固定工资">
              <el-table-column label="基本工资" min-width="100" prop="benefit.basicSalary">
                <template slot-scope="scope">
                  <el-input
                    v-show="scope.row.show"
                    v-model="scope.row.benefit.basicSalary"
                    oninput="if(value<0)value=0"
                    placeholder="请输入"
                    type="number"
                  ></el-input>
                  <span v-show="!scope.row.show">{{ scope.row.benefit.basicSalary }}</span>
                </template>
              </el-table-column>
              <el-table-column label="绩效工资" min-width="100" prop="benefit.performance">
                <template slot-scope="scope">
                  <el-input
                    v-show="scope.row.show"
                    v-model="scope.row.benefit.performance"
                    :precision="2"
                    oninput="if(value<0)value=0"
                    placeholder="请输入"
                    type="number"
                  ></el-input>
                  <span v-show="!scope.row.show">{{ scope.row.benefit.performance }}</span>
                </template>
              </el-table-column>
              <el-table-column label="合计" min-width="100" prop="total">
                <template slot-scope="scope">
                  <span>{{ getTotal(scope.row.benefit) }}</span>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="效益工资">
              <el-table-column min-width="100" prop="benefit.commission">
                <template slot="header" slot-scope="scope">
                  <el-popover content="可输入百分比（%）" placement="top" trigger="hover" width="170">
                    <div slot="reference">提成工资 <span class="el-icon-info"></span></div>
                  </el-popover>
                </template>
                <template slot-scope="scope">
                  <el-input
                    v-show="scope.row.show"
                    v-model="scope.row.benefit.commission"
                    placeholder="请输入"
                  ></el-input>
                  <span v-show="!scope.row.show">{{ scope.row.benefit.commission }}</span>
                </template>
              </el-table-column>
              <el-table-column min-width="100" prop="benefit.teamAward">
                <template slot="header" slot-scope="scope">
                  <el-popover content="可输入百分比（%）" placement="top" trigger="hover" width="170">
                    <div slot="reference">团队奖 <span class="el-icon-info"></span></div>
                  </el-popover>
                </template>
                <template slot-scope="scope">
                  <el-input
                    v-show="scope.row.show"
                    v-model="scope.row.benefit.teamAward"
                    placeholder="请输入"
                  ></el-input>
                  <span v-show="!scope.row.show">{{ scope.row.benefit.teamAward }}</span>
                </template>
              </el-table-column>
              <el-table-column min-width="100" prop="benefit.dividends">
                <template slot="header" slot-scope="scope">
                  <el-popover content="可输入百分比（%）" placement="top" trigger="hover" width="170">
                    <div slot="reference">分红 <span class="el-icon-info"></span></div>
                  </el-popover>
                </template>
                <template slot-scope="scope">
                  <el-input
                    v-show="scope.row.show"
                    v-model="scope.row.benefit.dividends"
                    placeholder="请输入"
                  ></el-input>
                  <span v-show="!scope.row.show">{{ scope.row.benefit.dividends }}</span>
                </template></el-table-column
              >
            </el-table-column>

            <el-table-column fixed="right" label="操作" width="180">
              <template slot-scope="scope">
                <el-button
                  v-if="scope.row.show == false"
                  v-hasPermi="['hr:training:promotion:edit']"
                  type="text"
                  @click="editFn(scope.row)"
                  >编辑</el-button
                >
                <el-button
                  v-if="scope.row.show == true"
                  v-hasPermi="['hr:training:promotion:edit']"
                  type="text"
                  @click="addRow(scope.row)"
                  >保存</el-button
                >

                <el-button v-hasPermi="['hr:training:promotion:details']" type="text" @click="handleCheck(scope.row)"
                  >晋升标准</el-button
                >

                <el-button v-hasPermi="['hr:training:promotion:delete']" type="text" @click="onDelete(scope)"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <div class="add-row" @click="addANewLine"><span class="el-icon-plus"></span> 添加行</div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 晋升标准侧滑 -->
    <el-drawer :before-close="handleClose" :visible.sync="editDrawer" direction="rtl" size="61%" title="晋升标准">
      <div class="check-box">
        <div class="user-name mb20">
          <span>职级：</span>
          <span class="text">{{
            getIdsArrayFn(options, positions)
              .map((obj) => obj.label)
              .join('、')
          }}</span>
        </div>
        <ueditorFrom
          v-if="editDrawer"
          ref="ueditorFrom"
          :border="true"
          :content="content"
          :height="height"
          type="notepad"
          @input="ueditorEdit"
        />
      </div>
      <div class="button from-foot-btn fix btn-shadow">
        <el-button :loading="loading" size="small" type="primary" @click="handleConfirm">保存</el-button>
      </div>
    </el-drawer>
  </div>
</template>
<script>
import Sortable from 'sortablejs'
import { getIdsArray, getArrayDifference } from '@/libs/public'

import {
  jobSelectApi,
  promotionDataApi,
  savePromotionDataApi,
  deletePromotionDataApi,
  putPromotionDataApi,
  sortPromotionDataApi,
  standardDataApi
} from '@/api/config.js'
import { rankListApi } from '@/api/setting'

export default {
  name: '',
  components: {
    left: () => import('./components/left'),
    ueditorFrom: () => import('@/components/form-common/oa-wangeditor')
  },

  data() {
    return {
      gridl: {
        xl: 3,
        lg: 4,
        md: 5,
        sm: 6,
        xs: 24
      },
      gridr: {
        xl: 21,
        lg: 20,
        md: 19,
        sm: 18,
        xs: 24
      },
      title: '',
      editDrawer: false,
      content: '',
      loading: false,
      userName: '',
      height: 'calc(100vh - 240px)',
      list: [],
      promotion_id: '', // 晋升表id
      options: [],
      id: '',
      positions: [],
      selectedList: [] // 已选中的职位
    }
  },

  mounted() {
    this.rowDrop()
  },

  methods: {
    getIdsArrayFn(arr1, arr2) {
      return getIdsArray(arr1, arr2, 'id')
    },
    // 切换表格
    clickType(item, val) {
      if (item) {
        this.promotion_id = item.id
        this.title = item.name
        this.getList(item.id, 1)
        this.selectedList = []
      } else {
        this.list = []
      }
    },

    // 获取表格数据
    async getList(id, val) {
      const result = await promotionDataApi({ promotion_id: id })
      this.list = []
      result.data.map((item) => {
        item.show = false
        this.list.push(item)
      })
      if (val == 1) {
        this.getJobSelect()
      }
      this.jobChange()
    },
    // 获取职位下拉数据
    async getJobSelect() {
      // const result = await jobSelectApi()
      let data = {
        page: 0,
        limit: 0,
        cate_id: ''
      }
      const result = await rankListApi(data)
      this.options = result.data.list
    },
    // 编辑
    editFn(row) {
      let newArr = this.selectedList.filter((v) => row.rank.every((val) => val != v))
      this.selectedList = newArr
      return (row.show = true)
    },
    // 删除
    onDelete(row) {
      this.selectedList = getArrayDifference(this.selectedList, row.row.rank, 'id')
      if (!row.row.id) {
        this.list.splice(row.$index, 1)
      } else {
        this.$modalSure('您确定要删除该行数据吗').then(() => {
          deletePromotionDataApi(row.row.id).then((res) => {
            this.getList(this.promotion_id)
          })
        })
      }
    },
    formatNum(val, key) {
      let temp = val.toString()
      temp = temp.replace(/。/g, '.')
      temp = temp.replace(/[^\d.]/g, '') //清除"数字"和"."以外的字符
      temp = temp.replace(/^\./g, '') //验证第一个字符是数字
      temp = temp.replace(/\.{2,}/g, '') //只保留第一个, 清除多余的
      temp = temp.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.')
      temp = temp.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3') //只能输入两个小数

      return Number(temp)
    },

    getTotal(row) {
      return Number(row.basicSalary) + Number(row.performance)
    },

    jobChange(e) {
      this.list.map((item) => {
        item.rank.map((key) => {
          this.selectedList.push(key)
        })
        this.selectedList = Array.from(new Set(this.selectedList))
      })
    },
    // 打开晋升标准
    handleCheck(row) {
      if (!row.id) {
        return this.$message.error('请先保存数据')
      }

      this.editDrawer = true
      // setTimeout(() => {
      //   this.$refs.ueditorFrom.tabButton = true
      // }, 200)
      this.content = row.standard
      this.positions = row.rank
      this.id = row.id
    },
    // 关闭晋升标准
    handleClose() {
      this.editDrawer = false
    },

    // 保存晋升标准
    async handleConfirm() {
      let data = {
        standard: this.content
      }
      await standardDataApi(this.id, data)
      this.editDrawer = false
      this.getList(this.promotion_id)
    },
    ueditorEdit(e) {
      this.content = e
    },
    // 保存
    async addRow(row) {
      if (!row.rank.length) {
        return this.$message.error('请先选择职位')
      }
      let data = {
        promotion_id: this.promotion_id,
        rank: row.rank,
        total: row.total,
        standard: row.standard,
        benefit: {
          basicSalary: row.benefit.basicSalary,
          performance: row.benefit.performance,
          commission: row.benefit.commission,
          teamAward: row.benefit.teamAward,
          dividends: row.benefit.dividends
        },
        id: 0
      }
      if (!row.id) {
        const result = await savePromotionDataApi(data)
        row.id = result.data.id
        this.selectedList = [...this.selectedList, ...data.rank]
        row.show = false
      } else {
        await putPromotionDataApi(row.id, data)
        this.selectedList = [...this.selectedList, ...data.rank]
        row.show = false
      }
      // setTimeout(() => {
      //   this.getList(this.promotion_id)
      // }, 300)
    },
    addANewLine() {
      this.list.push({
        rank: '',
        total: 0,
        benefit: {
          basicSalary: '',
          commission: '',
          teamAward: '',
          dividends: '',
          performance: ''
        },
        show: true
      })
    },

    // 表格拖拽函数
    rowDrop() {
      const tbody = this.$refs.table.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]

      Sortable.create(tbody, {
        animation: 300,
        onEnd: (e) => {
          const targetRow = this.list.splice(e.oldIndex, 1)[0]
          this.list.splice(e.newIndex, 0, targetRow)
          let data = []
          this.list.map((item) => {
            data.push(item.id)
          })
          let obj = {
            data
          }
          sortPromotionDataApi(this.promotion_id, obj).then((res) => {})
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.el-icon-info {
  color: #1890ff;
}
.assess-right {
  padding-left: 14px;
  ::v-deep .el-table thead.is-group th {
    background-color: rgba(247, 251, 255, 1);
    border-color: #ebeef5;
  }
  ::v-deep .el-table td {
    border: none;
  }
  .el-table {
    border: none;
  }
  .header {
    cursor: pointer;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 4px;
    margin-bottom: 24px;

    .title {
      cursor: default;
      text-align: center;
      font-family: PingFang SC, PingFang SC;
      font-weight: 500;
      font-size: 18px;
      color: #303133;
    }
    .tips {
      cursor: default;
      font-size: 12px;
      font-family: PingFang SC-Regular, PingFang SC;
      font-weight: 400;
      color: #909399;
    }
  }
}
.add-row {
  cursor: pointer;
  margin-top: 20px;
  width: 87px;
  height: 32px;
  text-align: center;
  line-height: 32px;
  border: 1px solid #1890ff;
  font-size: 13px;
  font-family: PingFang SC-Medium, PingFang SC;
  font-weight: 400;
  color: #1890ff;
  border-radius: 4px;
}
.check-box {
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  color: #666666;
  padding: 20px;
  padding-bottom: 0;

  .content {
    ::v-deep p {
      text-indent: 2em;
      font-size: 14px;
      line-height: 1.5;
    }
    ::v-deep td {
      border: 1px solid;
    }
  }
}
</style>
