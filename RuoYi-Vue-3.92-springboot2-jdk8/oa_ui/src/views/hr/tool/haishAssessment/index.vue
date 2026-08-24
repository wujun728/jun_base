<template>
  <div>
    <!-- 海事量表页面 -->
    <template v-if="type !== 'list'">
      <div class="divBox">
        <el-card class="card-box mt14" :body-style="{ padding: '0 0 20px 0' }">
          <div class="header-box">
            <div class="left">
              <i class="el-icon-arrow-left" @click="goBack"></i>
              <div class="invoice-logo"><i class="icon iconfont iconhetong"></i></div>
            </div>
            <div class="right">
              <span class="title">{{ name || '--' }}</span>
            </div>
          </div>

          <!-- 右边晋升表格 -->
          <div class="header mr20">
            <div class="title">
              <el-input v-model="name" placeholder="请输入评估表名称" ref="inputFocus"></el-input>
            </div>
            <span class="tips">列表支持拖拽排序</span>
          </div>
          <div class="pr20">
            <el-table :data="list" ref="table" row-key="col1" style="width: 100%" class="table ml20">
              <el-table-column prop="position" label="职位" min-width="100" fixed="left">
                <template slot="header" slot-scope="scope">
                  <select-member ref="selectMember" @getSelectList="getSelectList">
                    <template v-slot:custom>
                      <div class="department" @click.stop="selectPosition">
                        选择人员 <i class="el-icon-arrow-down"></i>
                      </div>
                    </template>
                  </select-member>
                </template>

                <template slot-scope="scope">
                  <el-select v-model="scope.row.col1" size="mini" placeholder="请选择">
                    <el-option
                      v-for="item in position"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                      :disabled="selectedList.includes(item.id)"
                    >
                    </el-option>
                  </el-select>
                </template>
              </el-table-column>

              <el-table-column label="知识水平(A)">
                <el-table-column prop="col2" label="专业知识水平" min-width="130">
                  <template slot="header" slot-scope="scope">
                    <el-popover
                      placement="top"
                      width="400"
                      trigger="hover"
                      content="专业知识水平指对该职位要求从事的职业领域的理论、实际方法与专门性知识的了解"
                    >
                      <div slot="reference">专业知识水平 <span class="el-icon-info"></span></div>
                    </el-popover>
                  </template>
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.col2" size="mini" placeholder="请选择">
                      <el-option
                        v-for="item in professionalKnowledge"
                        :key="item.id"
                        :label="item.label"
                        :value="item.id"
                      >
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>

                <el-table-column prop="col3" label="管理诀窍" min-width="100">
                  <template slot="header" slot-scope="scope">
                    <el-popover
                      placement="top"
                      width="400"
                      trigger="hover"
                      content="管理诀窍是指在领导和管理他人时所需的技巧和策略，包括有效沟通、目标设定、团队建设等方面的知识和技能"
                    >
                      <div slot="reference">管理诀窍 <span class="el-icon-info"></span></div>
                    </el-popover>
                  </template>
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.col3" size="mini" placeholder="请选择">
                      <el-option v-for="item in managementKnowHow" :key="item.id" :label="item.label" :value="item.id">
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>

                <el-table-column prop="col4" label="人际关系技巧" min-width="100">
                  <template slot="header" slot-scope="scope">
                    <el-popover
                      placement="top"
                      width="400"
                      trigger="hover"
                      content="人际关系技巧是指在与他人相处和合作时，建立良好关系、处理冲突、激发合作等方面的技巧和能力"
                    >
                      <div slot="reference">人际关系技巧<span class="el-icon-info"></span></div>
                    </el-popover>
                  </template>
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.col4" size="mini" placeholder="请选择">
                      <el-option
                        v-for="item in interpersonalRelationship"
                        :key="item.id"
                        :label="item.label"
                        :value="item.id"
                      >
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>

                <el-table-column prop="col5" label="评分" min-width="70">
                  <template slot-scope="scope">
                    <el-select
                      v-model="scope.row.col5"
                      :disabled="!score[getScore(scope.row.col2, scope.row.col3, scope.row.col4)]"
                      size="mini"
                      placeholder="请选择"
                      @change="positionScore(scope.row)"
                    >
                      <el-option
                        v-for="item in score[getScore(scope.row.col2, scope.row.col3, scope.row.col4)]"
                        :key="item.id"
                        :label="item.label"
                        :value="item.id"
                      >
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
              </el-table-column>

              <el-table-column label="解决问题(B)">
                <el-table-column prop="col6" label="思维环境" min-width="120">
                  <template slot="header" slot-scope="scope">
                    <el-popover
                      placement="top"
                      width="400"
                      trigger="hover"
                      content="思维环境是指工作中需要运用特定的思维方式和技巧的情境或背景"
                    >
                      <div slot="reference">思维环境 <span class="el-icon-info"></span></div>
                    </el-popover>
                  </template>
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.col6" size="mini" placeholder="请选择">
                      <el-option v-for="item in environment" :key="item.id" :label="item.label" :value="item.id">
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="col7" label="思维难度" min-width="100">
                  <template slot="header" slot-scope="scope">
                    <el-popover
                      placement="top"
                      width="400"
                      trigger="hover"
                      content="思维难度指的是问题的复杂程度或解决问题所需的认知挑战程度"
                    >
                      <div slot="reference">思维难度 <span class="el-icon-info"></span></div>
                    </el-popover>
                  </template>
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.col7" size="mini" placeholder="请选择">
                      <el-option v-for="item in difficulty" :key="item.id" :label="item.label" :value="item.id">
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="col8" label="评分" min-width="70">
                  <template slot-scope="scope">
                    <el-select
                      v-model="scope.row.col8"
                      :disabled="!solve[getSolve(scope.row.col6, scope.row.col7)]"
                      size="mini"
                      placeholder="请选择"
                      @change="positionScore(scope.row)"
                    >
                      <el-option
                        v-for="item in solve[getSolve(scope.row.col6, scope.row.col7)]"
                        :key="item.id"
                        :label="item.label"
                        :value="item.id"
                      >
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
              </el-table-column>

              <el-table-column label="承担责任(C)">
                <el-table-column prop="col9" min-width="120">
                  <template slot="header" slot-scope="scope">
                    <el-popover
                      placement="top"
                      width="400"
                      trigger="hover"
                      content="行动自由度指的是在职位中能够自主决策和采取行动的程度"
                    >
                      <div slot="reference">行动自由度 <span class="el-icon-info"></span></div>
                    </el-popover>
                  </template>
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.col9" size="mini" placeholder="请选择">
                      <el-option v-for="item in free" :key="item.id" :label="item.label" :value="item.id"> </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="col10" min-width="100">
                  <template slot="header" slot-scope="scope">
                    <el-popover
                      placement="top"
                      width="400"
                      trigger="hover"
                      content="职务责任是指职位所要承担的具体职责和义务"
                    >
                      <div slot="reference">职务责任 <span class="el-icon-info"></span></div>
                    </el-popover>
                  </template>
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.col10" size="mini" placeholder="请选择">
                      <el-option v-for="item in responsibility" :key="item.id" :label="item.label" :value="item.id">
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="col11" min-width="110">
                  <template slot="header" slot-scope="scope">
                    <el-popover
                      placement="top"
                      width="400"
                      trigger="hover"
                      content="职务影响结果指的是职位对于组织和业务结果的影响程度"
                    >
                      <div slot="reference">职务影响结果 <span class="el-icon-info"></span></div>
                    </el-popover>
                  </template>
                  <template slot-scope="scope">
                    <el-cascader v-model="scope.row.col11" size="mini" :options="influence"></el-cascader>
                  </template>
                </el-table-column>
                <el-table-column prop="col12" label="评分" min-width="70">
                  <template slot-scope="scope">
                    <el-select
                      v-model="scope.row.col12"
                      :disabled="!bear[getBear(scope.row.col9, scope.row.col10, scope.row.col11)]"
                      size="mini"
                      placeholder="请选择"
                      @change="positionScore(scope.row)"
                    >
                      <el-option
                        v-for="item in bear[getBear(scope.row.col9, scope.row.col10, scope.row.col11)]"
                        :key="item.id"
                        :label="item.label"
                        :value="item.id"
                      >
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
              </el-table-column>

              <el-table-column>
                <template slot="header" slot-scope="scope">
                  <el-popover
                    placement="top"
                    width="400"
                    trigger="hover"
                    content="通过技能、解决问题能力和职务责任的不同要求，区分不同类型的职位，以对各种职位进行划分，实行不同的薪酬结构。α：知识水平与解决问题权重(20<α<80) ；β：承担责任权重"
                  >
                    <div slot="reference">权重设置<span class="el-icon-info"></span></div>
                  </el-popover>
                </template>
                <el-table-column min-width="100">
                  <template slot="header" slot-scope="scope">
                    <div>（α）</div>
                  </template>
                  <template slot-scope="scope">
                    <el-input
                      class="input"
                      v-model="scope.row.col13"
                      type="number"
                      size="mini"
                      min="20"
                      max="80"
                      placeholder="请输入整数"
                      @change="positionScore(scope.row)"
                    >
                      <i slot="suffix" style="font-style: normal; margin-right: 10px">%</i>
                    </el-input>
                  </template>
                </el-table-column>
                <el-table-column min-width="100">
                  <template slot="header" slot-scope="scope">
                    <div>（β）</div>
                  </template>
                  <template slot-scope="scope">
                    <el-input class="input" v-model="scope.row.col14" size="mini" disabled placeholder="自动计算">
                      <i slot="suffix" style="font-style: normal; margin-right: 10px">%</i></el-input
                    >
                  </template>
                </el-table-column>
              </el-table-column>
              <el-table-column min-width="80">
                <template slot="header" slot-scope="scope">
                  <el-popover placement="top" width="250" trigger="hover" content="岗位分数= αA*(1+B) + βC">
                    <div slot="reference">岗位分数 <span class="el-icon-info"></span></div>
                  </el-popover>
                </template>
                <template slot-scope="scope">
                  <el-input v-model="scope.row.col15" size="mini" disabled placeholder="自动计算"></el-input>
                </template>
              </el-table-column>
              <el-table-column min-width="80"
                ><template slot="header" slot-scope="scope">
                  <el-popover placement="top" width="250" trigger="hover" content="岗位系数=岗位分数/最低岗位分数">
                    <div slot="reference">岗位系数 <span class="el-icon-info"></span></div>
                  </el-popover>
                </template>
                <template slot-scope="scope">
                  <el-input v-model="scope.row.col16" size="mini" disabled placeholder="自动计算"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100" fixed="right">
                <template slot-scope="scope">
                  <el-button v-if="scope.row.col1" type="text" @click="openHistory(scope)">历史</el-button>
                  <el-button type="text" @click="deleteFn(scope)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="add-row ml20" @click="addANewLine"><span class="el-icon-plus"></span> 添加行</div>
        </el-card>

        <div class="cr-bottom-button btn-shadow">
          <el-button size="small" @click="exportExcelData">下载</el-button>
          <el-button size="small" :loading="loading" type="primary" @click="submit()">保存</el-button>
        </div>
      </div>
    </template>
    <tableList v-if="type == 'list'" @addType="addType" @editType="editType"></tableList>

    <!-- 历史记录 -->
    <historyList ref="history" @applyFn="applyFn"></historyList>
    <!-- 下载 -->
    <export-excel :template="false" :save-name="saveName" :export-data="exportData" ref="exportExcel" />
  </div>
</template>
<script>
import Sortable from 'sortablejs'
import data from './mixins/index.js'
import { jobSelectApi, hayGroupApi, getHayGroupApi, putHayGroupApi } from '@/api/config.js'

export default {
  mixins: [data],
  components: {
    tableList: () => import('./components/tableList'),
    selectMember: () => import('@/components/form-common/select-member'),
    historyList: () => import('./components/historyList'),
    exportExcel: () => import('@/components/common/exportExcel')
  },
  data() {
    return {
      type: 'list',
      name: '评估表',
      title: '新增评估表',
      position: [],
      choiceEdit: 0,
      pageLoading: false,
      onlyDepartment: false,
      loading: false,
      exportData: {
        data: [],
        cols: [{ wpx: 70 }, { wpx: 70 }, { wpx: 120 }]
      },
      saveName: '下载表格.xlsx',
      selectedList: [], // 选中的职位id
      list: []
    }
  },
  computed: {
    // 智能分析
    getScore() {
      return function (x, y, z) {
        if (x && y && z) {
          let num = x.toString() + y.toString() + z.toString()
          return Number(num)
        }
      }
    },
    // 解决问题
    getSolve() {
      return function (x, y) {
        if (x && y) {
          let num = x.toString() + y.toString()
          return Number(num)
        }
      }
    },
    // 承担责任
    getBear() {
      return function (x, y, z) {
        if (x && y && z) {
          let num = x.toString() + y.toString() + z[0].toString() + z[1].toString()
          return Number(num)
        }
      }
    }
  },

  mounted() {
    this.jobSelect()
  },
  methods: {
    // 触发职位选择事件
    selectedPosition(e) {
      this.list.map((item) => {
        this.selectedList.push(item.col1)
      })
    },

    // 打开历史记录弹窗
    openHistory(data) {
      this.$refs.history.openBox(data)
    },

    // 获取职位下拉列表
    async jobSelect() {
      const result = await jobSelectApi()
      this.position = result.data
    },

    // 删除
    deleteFn(data) {
      if (data.row.col15) {
        this.$modalSure('你确定要删除这条数据吗').then(() => {
          this.list.splice(data.$index, 1)
        })
      } else {
        this.list.splice(data.$index, 1)
      }
      if (data.row.col1) {
        this.selectedList = this.selectedList.filter((item) => item == data.row.col11)
      }
    },

    // 自动新增行
    addANewLine() {
      this.list.map((item) => {
        this.selectedList.push(item.col1)
      })
      var list = {
        col1: null,
        col2: null,
        col3: null,
        col4: null,
        col5: null,
        col6: null,
        col7: null,
        col8: null,
        col9: null,
        col10: null,
        col11: null,
        col12: null,
        col13: '',
        col14: ''
      }
      this.list.push(list)
    },

    // 岗位分数和岗位系数计算
    positionScore(row) {
      if (Number(row.col13) > 80) {
        row.col13 = 80
      } else if (Number(row.col13) < 20) {
        row.col13 = 20
      }
      row.col14 = 100 - Number(row.col13)
      const index5 = this.getScore(row.col2, row.col3, row.col4)
      const index8 = this.getSolve(row.col6, row.col7)
      const index12 = this.getBear(row.col9, row.col10, row.col11)
      if (index5 && index8 && index12 && row.col13 && row.col14) {
        const col5Value = this.score[index5].find((key) => key.id == row.col5).label
        const col8Value = this.solve[index8].find((key) => key.id == row.col8).label
        const col12Value = this.bear[index12].find((key) => key.id == row.col12).label
        const col13Value = Number(row.col13) / 100
        const col14Value = Number(row.col14) / 100
        let A = col13Value * col5Value
        let B = Number(col8Value.replace('%', '')) / 100 + 1
        let C = col14Value * col12Value
        let num = A * B + C
        row.col15 = num.toFixed(2)
        let result = []
        this.list.map((item) => {
          if (item.col15) {
            result.push(Number(item.col15))
          }
        })
        let D = 0
        if (result.length == 1) {
          D = row.col15
        } else {
          D = result.reduce((x, y) => (x < y ? x : y))
        }

        let coefficient = row.col15 / D
        row.col16 = coefficient.toFixed(2)
      }
    },

    // 保存
    submit() {
      let result = true
      this.list.map((item, index) => {
        if (!item.col1) {
          result = false
        }
      })

      if (!result) {
        return this.$message.error('请选择职位')
      }
      this.loading = true
      let data = {
        name: this.name,
        list: this.list
      }
      if (this.type == 'add') {
        hayGroupApi(data)
          .then((res) => {
            this.loading = false
            this.type = 'list'
          })
          .catch((err) => {
            this.loading = false
          })
      } else if (this.type == 'edit') {
        putHayGroupApi(this.id, data)
          .then((res) => {
            this.loading = false
            this.type = 'list'
          })
          .catch((err) => {
            this.loading = false
          })
      }
    },

    // 选择部门/员工
    selectPosition() {
      this.$refs.selectMember.handlePopoverShow()
    },

    // 选择人员回调
    getSelectList(data) {
      let userList = []
      if (data.length > 0) {
        userList = data
      }
      userList.map((item) => {
        if (item.job) {
          let data = {
            id: item.job.id,
            label: item.job.name
          }
          this.selectedList.push(data.id)
          this.selectedList = Array.from(new Set(this.selectedList))
          this.list.push({
            col1: data.id,
            col2: null,
            col3: null,
            col4: null,
            col5: null,
            col6: null,
            col7: null,
            col8: null,
            col9: null,
            col10: null,
            col11: null,
            col12: null,
            col13: '',
            col14: '',
            col15: '',
            col16: ''
          })
          this.list = this.filterRepeat(this.list)
        }
      })
      this.close()
    },

    // 去重
    filterRepeat(arr) {
      arr = arr.filter((element, index, self) => {
        return self.findIndex((el) => el.col1 === element.col1) === index
      })
      return arr
    },

    // 新增
    addType() {
      this.list = []
      this.selectedList = []
      this.type = 'add'
      this.title = '新增评估表'
      this.name = '评估表' + this.$moment(new Date()).format('YYYYMMDD')
      this.addANewLine()
      setTimeout(() => {
        this.rowDrop()
        this.$refs.inputFocus.focus()
      }, 500)
    },
    // 编辑
    async editType(data) {
      this.pageLoading = true
      this.type = 'edit'
      this.title = '编辑评估表'
      this.name = data.name
      this.id = data.id
      const result = await getHayGroupApi(data.id)
      this.list = result.data
      this.pageLoading = false
    },

    // 返回到模板列表
    goBack() {
      this.type = 'list'
    },

    // 下载表格
    exportExcelData() {
      let aoaData = [['职位名称', '岗位分数', '岗位系数']]
      if (this.list.length > 0) {
        this.list.forEach((value) => {
          this.position.map((item) => {
            if (item.id == value.col1) {
              value.col1 = item.name
            }
          })
          aoaData.push([value.col1, value.col15, value.col16])
          this.exportData.data = aoaData
        })

        this.$refs.exportExcel.exportExcel()
      }
    },

    // 表格拖拽函数
    rowDrop() {
      const tbody = this.$refs.table.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]
      Sortable.create(tbody, {
        animation: 300,
        onEnd: (e) => {
          const targetRow = this.list.splice(e.oldIndex, 1)[0]
          this.list.splice(e.newIndex, 0, targetRow)
        }
      })
    },
    // 使用历史记录
    applyFn(data, index) {
      this.list[index].col1 = data.col1
      this.list[index].col2 = data.col2
      this.list[index].col3 = data.col3
      this.list[index].col4 = data.col4
      this.list[index].col5 = data.col5
      this.list[index].col6 = data.col6
      this.list[index].col7 = data.col7
      this.list[index].col8 = data.col8
      this.list[index].col9 = data.col9
      this.list[index].col10 = data.col10
      this.list[index].col11 = data.col11
      this.list[index].col12 = data.col12
      this.list[index].col13 = data.col13
      this.list[index].col14 = data.col14
      this.list[index].col15 = data.col15
      this.list[index].col16 = data.col16
    }
  }
}
</script>
<style scoped lang="scss">
.card-box {
  height: calc(100vh - 134px);
  overflow-y: scroll;
}
.cr-bottom-button {
  position: fixed;
  left: -20px;
  right: 0;
  bottom: 0;
  width: calc(100% + 220px);
}

.header {
  margin-left: 5px;

  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  margin-bottom: 10px;

  .title {
    font-size: 18px;
    font-weight: 700;
    font-family: PingFangSC-Semibold, PingFang SC;
    ::v-deep .el-input--medium {
      font-size: 18px !important;

      .el-input__inner {
        border: none;
        font-size: 18px;
      }
    }
  }
  .tips {
    cursor: default;
    font-size: 12px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: #909399;
  }
}
.add-row {
  margin-top: 20px;
  width: 80px;
  height: 32px;
  text-align: center;
  line-height: 32px;
  border: 1px solid #1890ff;
  font-size: 12px;
  font-family: PingFang SC-Medium, PingFang SC;
  font-weight: 500;
  color: #1890ff;
  border-radius: 4px;
}

.el-icon-info {
  color: #1890ff;
}
.department {
  color: #1890ff;
  font-size: 13px;
  font-weight: normal;
  cursor: pointer;
  font-family: PingFangSC-Semibold, PingFang SC;
  .el-icon-arrow-down {
    font-size: 12px;
    margin-top: 4px;
  }
}

.divBox {
  .input {
    ::v-deep.el-input__suffix {
      position: absolute;
      top: 2px;
    }
  }
  ::v-deep .el-icon-back {
    display: none;
  }
  ::v-deep .el-table thead.is-group th {
    background-color: rgba(247, 251, 255, 1);
    border-color: #ebeef5;
  }
  ::v-deep .el-table td {
    border: none;
  }
  .el-table {
    border: none;
    ::v-deep .el-input--mini {
      font-size: 12px;
    }
    ::v-deep .cell {
      padding-left: 6px;
      padding-right: 0;
    }
    ::v-deep .el-input__inner {
      padding: 3px 6px;
      font-size: 13px;
    }
    ::v-deep .el-input__suffix {
      right: 0;
    }
  }
}
.header-box {
  width: 100%;
  height: 70px;
  display: flex;
  border-bottom: 1px solid #dcdfe6;
  .left {
    padding: 11px 0 11px 20px;
    display: flex;
    align-items: center;
    .el-icon-arrow-left {
      cursor: pointer;
    }

    .invoice-logo {
      width: 48px;
      height: 48px;
      background: #1890ff;
      border-radius: 4px;
      line-height: 48px;
      text-align: center;
      margin-left: 10px;
      .iconhetong {
        font-size: 24px;
        color: #fff;
      }
    }
  }
  .right {
    padding: 11px 0 11px 0px;
    margin-left: 13px;
    display: flex;
    align-items: center;
    .title {
      font-family: PingFang SC, PingFang SC;
      font-weight: 500;
      font-size: 17px;
      color: #303133;
      text-align: left;
      font-style: normal;
      text-transform: none;
    }
  }
}
::v-deep.el-table__fixed::before {
  height: 0;
}
</style>
