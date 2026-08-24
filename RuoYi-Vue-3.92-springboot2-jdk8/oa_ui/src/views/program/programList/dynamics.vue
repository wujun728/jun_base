<!-- 项目-我的项目-项目动态页面 -->
<template>
  <div>
    <div class="divBox bill-type">
      <div class="title">动态列表</div>
      <el-form :inline="true">
        <div class="flex">
          <template>
            <el-form-item class="grey-bg" style="border: none; margin-left: 0; width: 150px"
              ><el-select v-model="type" size="small" :placeholder="$t('finance.pleaseselect')">
                <el-option
                  v-for="(item, index) in typesOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                /> </el-select
            ></el-form-item>
            <el-form-item
              ><span class="num">共 {{ type == 1 ? projectTotal : taskTotal }} 项</span></el-form-item
            >
            <el-form-item class="select-bar">
              <el-date-picker
                v-model="timeVal"
                size="small"
                type="daterange"
                clearable
                :format="'yyyy/MM/dd'"
                :value-format="'yyyy/MM/dd'"
                :range-separator="$t('toptable.to')"
                :start-placeholder="$t('toptable.startdate')"
                :end-placeholder="$t('toptable.endingdate')"
                @change="selectChange"
              ></el-date-picker>
            </el-form-item>
            <el-form-item class="select-bar">
              <div class="mr10" style="width: 200px">
                <select-member
                  :value="userList || []"
                  :is-search="true"
                  :placeholder="`请选择操作人`"
                  @getSelectList="getSelectList"
                ></select-member>
              </div>
            </el-form-item>
          </template>
          <el-form-item>
            <el-tooltip effect="dark" content="重置搜索条件" placement="top">
              <div class="reset" @click="reset"><i class="iconfont iconqingchu"></i></div>
            </el-tooltip>
          </el-form-item>
        </div>
      </el-form>

      <div>
        <el-table
          :data="type == 1 ? projectList : taskList"
          style="width: 100%"
          row-key="id"
          tooltip-effect="dark"
          :header-cell-style="{ background: '#f7fbff' }"
          :row-class-name="iconHadler"
        >
          <el-table-column prop="created_at" label="操作时间" min-width="150" />
          <el-table-column prop="operator" label="操作人" min-width="100" />
          <el-table-column prop="title" label="操作说明" min-width="600">
            <template slot-scope="scope">
              <span v-html="scope.row.title"></span>
              <span v-if="type == 2 && scope.row.task" class="pointer click-info" @click="taskInfo(scope.row)">
                #{{ scope.row.task.ident }} {{ scope.row.task.name }}（点击可查看详情）
              </span>
            </template>
          </el-table-column>
          <el-table-column v-if="type == 1" type="expand">
            <template slot-scope="scope" v-if="scope.row.describe.length">
              <div class="program-box" v-if="scope.row.describe.length > 0">
                <div class="flex program-describe" v-for="item in scope.row.describe" :key="item.id">
                  <span class="titles">{{ item.title }}</span>
                  <span>{{ item.value }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column v-if="type == 2" type="expand">
            <template slot-scope="scope">
              <div class="program-box">
                <div v-if="scope.row.describe.length > 0" class="flex expand-describe">
                  <div class="flex expand-left">
                    <span>{{ scope.row.describe[0].title }}</span>
                    <p v-html="scope.row.describe[0].value"></p>
                  </div>
                  <div class="line-short"></div>
                  <div class="flex expand-right">
                    <span>{{ scope.row.describe[1].title }}</span>
                    <p v-html="scope.row.describe[1].value"></p>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="paginationClass">
        <el-pagination
          :page-size="type == 1 ? projectForm.limit : taskForm.limit"
          :current-page="type == 1 ? projectForm.page : taskForm.page"
          :page-sizes="[10, 15, 20]"
          layout="total, prev, pager, next, jumper"
          :total="type == 1 ? projectTotal : taskTotal"
          @size-change="handleSizeChange(type, $event)"
          @current-change="pageChange(type, $event)"
        />
      </div>
    </div>

    <!-- 新增弹窗表单 -->
    <addTask
      ref="addTask"
      :projectList="projectInfoList"
      :programOptions="programOptions"
      :programMemberOptions="programMemberOptions"
      :programVersionList="programVersionList"
      @getMemberList="getMemberList"
      @getProgramVersionList="getProgramVersionList"
      @getProgramSelectList="getProgramSelectList"
      @getTableData="getTableData()"
    />
    <!-- 编辑弹窗表单 -->
    <editTask
      ref="editTask"
      :projectList="projectInfoList"
      :programOptions="programOptions"
      :programMemberOptions="programMemberOptions"
      :programVersionList="programVersionList"
      @getMemberList="getMemberList"
      @getProgramVersionList="getProgramVersionList"
      @getProgramSelectList="getProgramSelectList"
      @getTableData="getTableData()"
      @openTask="openTask"
    />
  </div>
</template>

<script>
import {
  getDynamicApi,
  getDynamicTaskApi,
  getProgramSelectApi,
  getProgramTaskSelectApi,
  getProgramVersionSelectApi,
  getProgramMemberApi
} from '@/api/program'
import editTask from '../programTask/components/editTask'
export default {
  name: 'programList',
  components: {
    editTask,
    addTask: () => import('../programTask/components/addTask'),
    selectMember: () => import('@/components/form-common/select-member')
  },
  data() {
    return {
      projectList: [],
      timeVal: [this.$moment().subtract(1, 'months').format('YYYY/MM/DD'), this.$moment().format('YYYY/MM/DD')],
      taskList: [],
      type: this.$route.query.type || 1,
      projectForm: {
        page: 1,
        limit: 15,
        time: this.$moment().subtract(1, 'months').format('YYYY/MM/DD') + '-' + this.$moment().format('YYYY/MM/DD'),
        uid: '',
        relation_id: this.$route.query.id
      },
      taskForm: {
        page: 1,
        limit: 15,
        time: this.$moment().subtract(1, 'months').format('YYYY/MM/DD') + '-' + this.$moment().format('YYYY/MM/DD'),
        uid: [],
        program_id: this.$route.query.id
      },
      projectTotal: 0,
      taskTotal: 0,
      customerList: [],
      contractList: [],
      programInfo: {},
      userList: [],
      memberShow: false,
      projectInfoList: [],
      programOptions: [],
      programMemberOptions: [],
      programVersionList: [],
      queryParams: {},
      drawer: false,
      activeName: '1',
      typesOptions: [
        {
          label: '项目动态',
          value: 1
        },
        {
          label: '任务动态',
          value: 2
        }
      ]
    }
  },
  created() {
    this.getTableData()
    this.getTableTaskData()
    this.queryParams = this.parseQueryString(window.location.search)
  },
  mounted() {
    if (this.queryParams.type) {
      this.$nextTick(() => {
        this.taskInfo(this.queryParams)
      })
    }
  },
  watch: {
    type(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.reset()
      }
    }
  },
  methods: {
    handleClose() {
      this.reset()
      this.type = 1
      this.drawer = false
    },
    parseQueryString(queryString) {
      // 去掉查询字符串中的 '?' 并以 '&' 分割成数组
      const pairs = (queryString[0] === '?' ? queryString.substr(1) : queryString).split('&')
      const params = {}
      // 对每个键值对执行分割，并存储在params对象中
      for (let pair of pairs) {
        pair = pair.split('=')
        params[decodeURIComponent(pair[0])] = decodeURIComponent(pair[1] || '')
      }
      return params
    },
    // 获取表格数据
    getTableData() {
      let data = this.projectForm
      getDynamicApi(data).then((res) => {
        this.projectList = res.data.list
        this.projectTotal = res.data.count
      })
    },
    getTableTaskData() {
      let data = this.taskForm
      getDynamicTaskApi(data).then((res) => {
        this.taskList = res.data.list
        this.taskTotal = res.data.count
      })
    },
    pageChange(type, page) {
      if (type === 1) {
        this.projectForm.page = page
      } else {
        this.taskForm.page = page
      }
      this.confirmData()
    },
    handleSizeChange(type, val) {
      if (type === 1) {
        this.projectForm.limit = val
      } else {
        this.taskForm.limit = val
      }
      this.confirmData()
    },
    // 选择成员回调
    getSelectList(data) {
      var testUid = []
      data.forEach((item) => {
        testUid.push(item.value)
      })
      if (this.type === 1) {
        this.projectForm.uid = testUid
      } else {
        this.taskForm.uid = testUid
      }
      this.userList = data
      this.confirmData()
    },

    confirmData() {
      if (this.type === 1) {
        this.getTableData()
      } else {
        this.getTableTaskData()
      }
    },
    // 时间段选择
    selectChange(e) {
      if (e == null) {
        this.projectForm.time = ''
        this.taskForm.time = ''
      } else {
        this.timeVal = e
        if (this.type == 1) {
          this.projectForm.time = this.timeVal[0] + '-' + this.timeVal[1]
        } else {
          this.taskForm.time = this.timeVal[0] + '-' + this.timeVal[1]
        }
      }
      this.confirmData()
    },
    reset() {
      this.projectForm.uid = []
      this.projectForm.page = 1
      this.taskForm.page = 1
      this.projectForm.time =
        this.$moment().subtract(1, 'months').format('YYYY/MM/DD') + '-' + this.$moment().format('YYYY/MM/DD')
      this.timeVal = [this.$moment().subtract(1, 'months').format('YYYY/MM/DD'), this.$moment().format('YYYY/MM/DD')]
      this.taskForm.uid = []
      this.taskForm.time =
        this.$moment().subtract(1, 'months').format('YYYY/MM/DD') + '-' + this.$moment().format('YYYY/MM/DD')
      this.userList = []
      this.confirmData()
    },
    iconHadler({ row }) {
      if (!row.describe || row.describe.length == 0) {
        return 'icon-no'
      }
    },
    // 获取项目数据
    async getProgram() {
      const result = await getProgramSelectApi()
      this.projectInfoList = result.data
    },
    // 获取父级任务下拉列表
    async getProgramSelect(row) {
      let data = {
        program_id: row.program_id
      }
      const result = await getProgramTaskSelectApi(data)
      result.data.map((item) => {
        if (row.id === item.value) {
          this.$set(item, 'disabled', true)
        }
      })
      this.programOptions = result.data
    },
    // 获取项目成员下拉列表
    async getProgramMember(program_id, type) {
      const result = await getProgramMemberApi({ program_id })
      this.programMemberOptions = result.data
      if (type === 1) {
        this.putProgramTask('uid', testUid[0], this.adminsId)
      }
    },
    // 获取项目版本数据
    async getProgramVersion(program_id, index) {
      const result = await getProgramVersionSelectApi({ program_id })
      this.programVersionList = result.data
      if (this.programVersionList.length) {
        this.$set(this.versionVisible, index, true)
      }
    },
    getMemberList(program_id) {
      this.getProgramMember(program_id)
    },
    getProgramVersionList(program_id) {
      this.getProgramVersion(program_id)
    },
    getProgramSelectList(row) {
      this.getProgramSelect(row)
    },
    taskInfo(row) {
      let id = row.task ? row.task.program_id : row.program_id
      this.$refs.editTask.openBox(row.relation_id)
      this.getProgramVersion(id)
      this.getMemberList(id)
      this.getProgram()
      this.getProgramSelect(row)
    },
    openTask(data, type) {
      let id = type == 1 ? data.pid : 0
      this.$refs.addTask.openBox(id, data)
    }
  }
}
</script>

<style lang="scss" scoped>
.grey-bg {
  .el-select,
  ::v-deep.el-input__inner {
    background: #f0f2f5;
    border-radius: 2px;
    border: none;
  }
}
.title {
  font-family: PingFang SC, PingFang SC;
  font-weight: 500;
  font-size: 18px;
  color: #303133;
  margin-bottom: 20px;
}
.bill-type {
  .header {
    margin-bottom: 14px;
    span {
      font-size: 18px;
      line-height: 32px;
      color: #303133;
    }
  }
  .text-right {
    text-align: right;
  }
  .el-radio {
    margin-right: 15px;
  }
}
.img {
  width: 26px;
  height: 26px;
  border-radius: 50%;
}

.expand-describe {
  background: #f9f9f9;
  .line-short {
    width: 1px;
    background: rgba(204, 204, 204, 0.3);
  }
  .expand-left {
    width: 50%;
    align-items: flex-start;
  }
  .expand-right {
    width: 50%;
    margin-left: 20px;
    align-items: flex-start;
  }
  span {
    color: #9e9e9e;
    min-width: 80px;
    display: inline-block;
  }
  p {
    color: #9e9e9e;
    line-height: 16px;
    margin-right: 36px;
    margin-bottom: 0;
    ::v-deepp {
      margin-bottom: 14px;
    }
  }
}
.click-info {
  color: #1890ff;
  margin-left: 10px;
}
::v-deep.icon-no .el-table__expand-icon {
  display: none;
}
::v-deep .el-table .cell {
  padding-left: 30px;
}
::v-deep.el-table th.el-table__cell > .cell {
  padding-left: 30px;
}
::v-deeptd.el-table__cell.el-table__expanded-cell {
  padding: 0;
}
::v-deep.el-table td.el-table__cell {
  border-bottom: 1px solid #f5f5f5;
}
.num {
  margin: 0 8px;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 12px;
  color: #909399;
}
.program-box {
  background: #f9f9f9;
  padding: 20px 30px 6px;
  color: #9e9e9e;
  &:last-child {
    margin-bottom: 0;
  }
  .program-describe {
    margin-bottom: 14px;
    line-height: 20px;
    .titles {
      min-width: 68px;
    }
  }
}
.divBox {
  margin: 30px 20px 40px;
}
</style>
