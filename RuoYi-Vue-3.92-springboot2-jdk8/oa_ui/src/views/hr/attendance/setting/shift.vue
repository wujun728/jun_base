<!-- 人事-考勤管理-班次设置 -->
<template>
  <div class="divBox">
    <el-card class="normal-page">
      <oaFromBox
        v-if="search.length > 0"
        :search="search"
        :isViewSearch="false"
        :total="total"
        :title="`班次列表`"
        :isAddBtn="true"
        @addDataFn="addFn"
        @confirmData="confirmData"
      ></oaFromBox>

      <div class="table-box mt10">
        <el-table :data="tableData" :height="tableHeight" style="width: 100%">
          <el-table-column prop="name" label="班次名称" width="200"> </el-table-column>
          <el-table-column prop="position" label="考勤时间" width="340">
            <template slot-scope="scope">
              {{ scope.row.times[0].first_day_after == 0 ? '当日' : '次日' }} {{ scope.row.times[0].work_hours }} -
              {{ scope.row.times[0].second_day_after == 0 ? '当日' : '次日' }}{{ scope.row.times[0].off_hours }}
              <span v-if="scope.row.times.length > 1"
                >、 {{ scope.row.times[1].first_day_after == 0 ? '当日' : '次日' }}
                {{ scope.row.times[1].work_hours }} - {{ scope.row.times[1].second_day_after == 0 ? '当日' : '次日'
                }}{{ scope.row.times[1].off_hours }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="card.name" label="创建人">
            <template slot-scope="scope">{{ scope.row.card ? scope.row.card.name : '--' }}</template>
          </el-table-column>
          <el-table-column prop="created_at" label="创建时间"> </el-table-column>
          <el-table-column prop="updated_at" label="最后更新时间"> </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template slot-scope="scope">
              <el-button type="text" @click="checkFn(scope.row)" v-hasPermi="['hr:attendance:shift:check']"
                >查看</el-button
              >

              <el-button type="text" @click="editFn(scope.row)" v-hasPermi="['hr:attendance:shift:edit']"
                >编辑</el-button
              >

              <el-button
                type="text"
                v-if="scope.row.id !== 2"
                @click="deleteFn(scope.row)"
                v-hasPermi="['hr:attendance:shift:delete']"
                >删除</el-button
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
    </el-card>
    <!-- 新增班次 -->
    <addShift ref="addShift" @getList="getList"></addShift>
  </div>
</template>

<script>
import { attendanceShiftListApi, deleteShiftListApi } from '@/api/config'
import oaFromBox from '@/components/common/oaFromBox'
export default {
  components: { addShift: () => import('./components/addShift'), oaFromBox },
  data() {
    return {
      type: 'add',
      total: 0,
      where: {
        page: 1,
        limit: 15
      },
      tableData: [],
      search: [
        {
          field_name: '班次名称或创建人',
          field_name_en: 'name',
          form_value: 'input'
        }
      ]
    }
  },
  mounted() {
    this.getList()
  },

  methods: {
    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          page: 1,
          limit: 15
        }
        this.getList(1)
      } else {
        this.where = { ...this.where, ...data }
        this.getList(1)
      }
    },
    async getList(val) {
      if (val) {
        this.where.page = val
      }
      const result = await attendanceShiftListApi(this.where)
      this.total = result.data.count
      this.tableData = result.data.list
    },

    deleteFn(val) {
      this.$modalSure('你确定要删除这条数据吗').then(() => {
        deleteShiftListApi(val.id).then((res) => {
          this.getList()
        })
      })
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
      this.$refs.addShift.openBox()
    },
    editFn(val) {
      this.$refs.addShift.openBox(val.id, 'edit')
    },
    checkFn(val) {
      this.$refs.addShift.openBox(val.id, 'check')
    }
  }
}
</script>

<style lang="scss" scoped>
.from {
  display: flex;
  justify-content: space-between;
  padding-bottom: 20px;
  border-bottom: 1px solid #eeeeee;
}
.card-box {
  min-height: calc(100vh - 65px);
}
</style>
