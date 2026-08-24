<template>
  <div>
    <oaFromBox
      :isAddBtn="false"
      :isViewSearch="false"
      :search="search"
      :total="total"
      @confirmData="confirmData"
    ></oaFromBox>

    <div class="table-box mt14">
      <el-table
        :data="tableData"
        :height="tableHeight"
        :tree-props="{ children: 'children' }"
        default-expand-all
        row-key="id"
        style="width: 100%"
      >
        <el-table-column :label="$t('toptable.name')" min-width="100" prop="test.name" />
        <el-table-column :label="$t('toptable.department')" min-width="160" prop="frame.name" />
        <el-table-column :label="$t('user.work.assessmentname')" min-width="100" prop="name" />
        <el-table-column :label="$t('toptable.assessmentcycle')" align="center" min-width="100" prop="period">
          <template slot-scope="scope">
            <span>{{ getPeriodText(scope.row.period) }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('user.work.qssessmentscore')" align="center" min-width="100" prop="score" />
        <el-table-column :label="$t('access.assessmentgrade')" align="center" min-width="100" prop="level" />
        <el-table-column :label="$t('hr.assessmentstatus')" min-width="100" prop="status">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status).type">
              {{ getStatusTag(scope.row.status).text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('access.starttime')" align="center" min-width="100" prop="start_time">
          <template slot-scope="scope">
            {{ $moment(scope.row.start_time).format('yyyy-MM-DD') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('access.endtime')" align="center" min-width="100" prop="end_time">
          <template slot-scope="scope">
            {{ $moment(scope.row.end_time).format('yyyy-MM-DD') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('public.operation')" fixed="right" prop="address" width="160">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status >= 0" type="text" @click="handleCheck(scope.row, 'check')">
              查看
            </el-button>
            <el-button v-if="scope.row.status === 3" type="text" @click="handleCheck(scope.row, '')"> 审核 </el-button>
            <el-button v-if="scope.row.status == 4" type="text" @click="handleCheck(scope.row, scope.$index)">
              修改评分
            </el-button>
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
  </div>
</template>

<script>
import { userAssessSubord } from '@/api/user'
import Common from '@/components/user/accessCommon'

export default {
  name: 'Merits',
  props: ['handle'],
  components: {
    defaultPage: () => import('@/components/common/defaultPage'),
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  data() {
    return {
      tableData: [],
      where: {
        page: 1,
        limit: 15,
        type: 2,
        handle: this.handle
      },
      search: [
        {
          field_name: '被考核人',
          field_name_en: 'test_uid',
          form_value: 'test_uid',
          data_dict: []
        },
        {
          field_name: '考核周期',
          field_name_en: 'period',
          form_value: 'select',
          data_dict: Common.periodOption
        },
        {
          field_name: '考核状态',
          field_name_en: 'status',
          form_value: 'select',
          data_dict: Common.statusOptions
        },
        {
          field_name: '开始时间',
          field_name_end: '结束时间',
          field_name_en: 'time',
          form_value: 'date_picker'
        }
      ],
      total: 0,
      from: {
        status: '',
        id: '',
        tabIndex: '3',
        addBtn: 1
      },
      period: '',
      scope_frame: '',
      status: '',
      test_uid: '',
      time: '',
      date: ''
    }
  },

  methods: {
    async getTableData() {
      this.where.handle = this.handle
      const result = await userAssessSubord(this.where)
      this.tableData = result.data.list
      this.total = result.data.count
    },
    getPeriodText(id) {
      return Common.getPeriodText(id)
    },
    getStatusText(id) {
      return Common.getStatusText(id)
    },
    getStatusTag(status) {
      return Common.getStatusTag(status)
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    confirmData(data) {
      this.where.page = 1
      if (data == 'reset') {
        this.where = {
          page: 1,
          limit: this.where.limit,
          type: 2
        }
      } else {
        this.where = { ...this.where, ...data }
        this.where.handle = this.handle
      }

      this.getTableData()
    },
    handleCheck(row, str) {
      this.from.status = row.status
      this.from.id = row.id
      this.from.addBtn = 1
      this.$emit('meritsHandleCheck', this.from, str)
    },
    getFormBoxFrame() {
      // this.$refs.formBox.getFrameUser()
    }
  }
}
</script>

<style lang="scss" scoped></style>
