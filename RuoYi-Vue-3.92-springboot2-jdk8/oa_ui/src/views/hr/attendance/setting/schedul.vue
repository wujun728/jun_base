<template>
  <div class="divBox">
    <!-- 排班管理列表页面 -->
    <template v-if="type == 'list'">
      <el-card class="normal-page">
        <oaFromBox
          v-if="search.length > 0"
          :isAddBtn="true"
          :isViewSearch="false"
          :search="search"
          :title="`排班管理`"
          :total="total"
          @addDataFn="addFn"
          @confirmData="confirmData"
        ></oaFromBox>
        <!-- 表格 -->
        <div class="table-box mt10">
          <el-table :data="tableData" :height="tableHeight" style="width: 100%">
            <el-table-column label="排班月份">
              <template slot-scope="scope">
                {{ $moment(scope.row.date).format('yyyy年MM月') }}
              </template>
            </el-table-column>
            <el-table-column label="考勤组名称" prop="group.name"> </el-table-column>
            <el-table-column label="考勤组成员" prop="position" show-overflow-tooltip>
              <template #default="{ row }">
                <span v-if="row.group.members.length <= 3">
                  {{ row.group.members.map((obj) => obj.name).join('、') }}
                </span>
                <span v-else
                  >{{
                    row.group.members
                      .slice(0, 3)
                      .map((obj) => obj.name)
                      .join('、')
                  }}&nbsp;等{{ row.group.members.length }} 人</span
                >
              </template>
            </el-table-column>

            <el-table-column fixed="right" label="操作" width="180">
              <template slot-scope="scope">
                <el-button
                  v-if="scope.row.group.is_delete !== 1"
                  v-hasPermi="['hr:attendance:schedul:edit']"
                  type="text"
                  @click="editFn(scope.row)"
                  >排班</el-button
                >
                <!-- v-if="$moment(scope.row.date).format('YYYY-MM') >= $moment().format('YYYY-MM')" -->
                <el-button v-if="scope.row.group.is_delete == 1" type="text" @click="checkFn(scope.row)"
                  >查看</el-button
                >

                <el-button v-hasPermi="['hr:attendance:schedul:copy']" type="text" @click="copyFn(scope.row)"
                  >复制</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <div class="page-fixed">
            <el-pagination
              :current-page="where.page"
              :page-size="where.limit"
              :page-sizes="[15, 20, 30]"
              :total="total"
              layout="total,sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="pageChange"
            />
          </div>
        </div>
      </el-card>
    </template>
    <!-- 新增排班 -->
    <oa-dialog
      ref="oaDialog"
      :formConfig="formConfig"
      :formDataInit="formDataInit"
      :formRules="formRules"
      :fromData="fromData"
      @submit="submit"
    ></oa-dialog>
    <!-- 排班组件 -->
    <add-schedul v-if="type == 'add'" :id="id" :newData="newData" @backFn="backFn"></add-schedul>
    <!-- 查看排班 -->
    <check-schedul v-if="type == 'check'" :id="id" :newData="newData" @backFn="backFn"></check-schedul>
  </div>
</template>

<script>
import { attendanceGroupListApi, attendanceArrangeApi, attendanceArrangeListApi } from '@/api/config'
import oaFromBox from '@/components/common/oaFromBox'

export default {
  components: {
    oaDialog: () => import('@/components/form-common/dialog-form'),
    addSchedul: () => import('./addSchedul'),
    checkSchedul: () => import('./components/checkSchedul'),
    oaFromBox
  },
  data() {
    return {
      type: 'list',
      id: null, // 考勤组id
      total: 0,
      timeVal: [],
      where: {
        page: 1,
        limit: 15
      },
      newData: {},
      search: [
        {
          field_name: '考勤组名称',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '考勤时间',
          field_name_en: 'time',
          form_value: 'monthrange'
        }
      ],

      fromData: {
        with: '600px',
        title: '新增晋排班',
        btnText: '确定',
        labelWidth: '100px',
        type: ''
      },
      formDataInit: {
        date: '',
        groups: []
      },
      formRules: {
        date: { required: true, message: '请选择考勤时间', trigger: 'blur' },
        groups: { required: true, message: '请选择考勤组', trigger: 'change' }
      },
      formConfig: [
        {
          type: 'month',
          label: '考勤时间：',
          placeholder: '请输入晋升表名称',
          key: 'date'
        },
        {
          type: 'multipleSelect',
          label: '考勤组名称：',
          placeholder: '请选择考勤组(多选)',
          key: 'groups',
          options: []
        }
      ],

      tableData: []
    }
  },

  mounted() {
    this.getList()
    this.getTeamList()
  },

  methods: {
    // 考勤组数据
    async getTeamList() {
      let data = {
        page: 1
      }
      const result = await attendanceGroupListApi(data)
      this.formConfig[1].options = result.data.list
    },
    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          page: 1,
          limit: 15
        }
        this.getList()
      } else {
        this.where = { ...this.where, ...data }
        this.where.page = 1
        this.getList()
      }
    },

    async getList() {
      if (this.timeVal.length > 0) {
        this.where.time =
          this.$moment(this.timeVal[0]).format('YYYY/MM') + '-' + this.$moment(this.timeVal[1]).format('YYYY/MM')
      }

      const result = await attendanceArrangeListApi(this.where)
      this.total = result.data.count
      this.tableData = result.data.list
    },

    async submit(data) {
      data.date = this.$moment(data.date).format('yyyy-MM')
      await attendanceArrangeApi(data)
      await this.$refs.oaDialog.handleClose()

      await this.getList()
    },
    restFn() {
      this.timeVal = []
      this.where = {
        page: 1,
        limit: 15,
        name: '',
        time: ''
      }

      this.getList()
    },

    pageChange(page) {
      this.where.page = page
      this.getList()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    },
    addFn() {
      this.formDataInit = {
        date: '',
        groups: []
      }
      this.fromData.type = 'add'
      this.fromData.title = '新增排班'
      this.$refs.oaDialog.openBox()
    },
    checkFn(data) {
      this.id = data.group.id
      this.newData = data
      this.type = 'check'
    },
    copyFn(data) {
      this.getTeamList()
      this.formDataInit.groups = []
      this.fromData.title = '复制排班'
      this.fromData.type = 'edit'
      this.formDataInit.groups.push(data.group.id)
      this.$refs.oaDialog.openBox()
    },
    // 排班
    editFn(data) {
      this.id = data.group.id
      this.newData = data
      this.type = 'add'
    },
    backFn() {
      this.type = 'list'
      this.show = false
    }
  }
}
</script>

<style lang="scss" scoped>
.fromx {
  display: flex;
  justify-content: space-between;
}
.info {
  color: #1890ff;
  cursor: pointer;
}
.card-box {
  min-height: calc(100vh - 65px);
}
.alert {
  width: 100%;

  padding-top: 0;
}
::v-deep .el-alert {
  padding-left: 30px;
  border: 1px solid #1890ff;
  color: #1890ff;
  font-size: 13px;
  background-color: #edf7ff;
  line-height: 1;
  margin-bottom: 14px;
}
::v-deep .el-alert--info .el-alert__description {
  color: #303133;
  font-size: 13px;
  font-weight: 500;
}
::v-deep .el-alert__icon.is-big {
  font-size: 16px;
  width: 15px;
}
</style>
