<!-- 人事-考勤管理-考勤组设置 -->
<template>
  <div class="divBox">
    <el-card class="normal-page">
      <oaFromBox
        v-if="search.length > 0"
        :dropdownList="dropdownList"
        :isAddBtn="true"
        :isViewSearch="false"
        :search="search"
        :title="`考勤组列表`"
        :total="total"
        @addDataFn="addFn"
        @confirmData="confirmData"
        @dropdownFn="drawer = true"
      ></oaFromBox>
      <div class="table-box mt10">
        <el-table :data="tableData" :height="tableHeight" style="width: 100%">
          <el-table-column label="考勤组名称" min-width="180" prop="name" show-overflow-tooltip> </el-table-column>
          <el-table-column label="考勤组成员" min-width="250" prop="members" show-overflow-tooltip>
            <template #default="{ row }">
              <span>{{ row.members.map((obj) => obj.name).join('、') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="考勤班次" min-width="300" prop="shifts">
            <template slot-scope="scope">
              <span v-for="(item, index) in scope.row.shifts" v-if="scope.row.shifts.length <= 3" :key="index"
                >{{ item.name }}&nbsp;{{ item.times[0].first_day_after == 0 ? '当日' : '次日'
                }}{{ item.times[0].work_hours }}-{{ item.times[0].second_day_after == 0 ? '当日' : '次日'
                }}{{ item.times[0].off_hours }}
                <span v-if="item.times.length > 1">
                  {{ item.times[1].first_day_after == 0 ? '当日' : '次日' }}{{ item.times[1].work_hours }} -
                  {{ item.times[1].second_day_after == 0 ? '当日' : '次日' }}{{ item.times[1].off_hours }}
                </span>
              </span>
              <el-popover v-if="scope.row.shifts.length > 3" placement="top-start" trigger="hover" width="400">
                <span v-for="(item, index) in scope.row.shifts" :key="index"
                  >{{ item.name }}&nbsp;{{ item.times[0].work_hours }}-{{ item.times[0].off_hours }}、</span
                >
                <div slot="reference">
                  <span v-for="(item, index) in scope.row.shifts" v-show="index <= 2" :key="index">
                    {{ item.name }}&nbsp;{{ item.times[0].work_hours }}-{{ item.times[0].off_hours }}&nbsp;<span>{{
                      index < 2 ? '、' : '...'
                    }}</span>
                  </span>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" prop="created_at" width="180"> </el-table-column>
          <el-table-column fixed="right" label="操作" width="130">
            <template #default="{ row }">
              <el-button
                v-if="row.admins.includes(userInfo.id) || row.super.includes(userInfo.id)"
                v-hasPermi="['hr:attendance:team:edit']"
                type="text"
                @click="editFn(row)"
                >编辑</el-button
              >

              <el-button
                v-if="row.admins.includes(userInfo.id) || row.super.includes(userInfo.id)"
                v-hasPermi="['hr:attendance:team:delete']"
                type="text"
                @click="deleteFn(row)"
                >删除</el-button
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
            @size-change="listSizeChange"
            @current-change="pageChange"
          />
        </div>
      </div>
    </el-card>
    <!-- 查看未设置考勤组人员 -->
    <el-drawer :before-close="handleClose" :visible.sync="drawer" size="700px" title="未设置考勤组人员">
      <!-- 表格 -->
      <div class="box">
        <el-table ref="multipleTable" :data="listData" style="width: 100%" tooltip-effect="dark">
          <el-table-column label="人员姓名" prop="name"> </el-table-column>
          <el-table-column label="部门" prop="position">
            <template slot-scope="scope">
              <div v-for="(item, index) in scope.row.frames" :key="index" class="frame-name over-text">
                <span class="icon-h">
                  {{ item.name
                  }}<span v-show="item.is_mastart === 1 && scope.row.frames.length > 1" title="主部门">(主)</span>
                </span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="职位" prop="job.name"> </el-table-column>
        </el-table>
        <el-pagination
          :current-page="list.page"
          :page-size="list.limit"
          :page-sizes="[15, 20, 30]"
          :total="totalList"
          layout="total,sizes, prev, pager, next, jumper"
          @size-change="listSizeChange"
          @current-change="listPageChange"
        />
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { roterPre } from '@/settings'
import oaFromBox from '@/components/common/oaFromBox'
import { attendanceGroupListApi, deleteAttendanceGroup, attendanceUnattendedMember } from '@/api/config'
import { mapGetters } from 'vuex'
export default {
  components: { oaFromBox },
  data() {
    return {
      total: 0,
      totalList: 0,
      where: {
        page: 1,
        limit: 15,
        name: '',
        sort_value: '',
        sort_field: ''
      },
      drawer: false,
      tableData: [],
      listData: [],
      search: [
        {
          field_name: '考勤组名称,人员姓名',
          field_name_en: 'name',
          form_value: 'input'
        }
      ],
      dropdownList: [
        {
          label: '未设置考勤组人员',
          value: 1
        }
      ],
      list: {
        page: 1,
        limit: 15
      }
    }
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  mounted() {
    this.getList()
    this.getdata()
  },

  methods: {
    confirmData(data) {
      if (data == 'reset') {
        this.where.name = ''
      } else {
        for (const item in data) {
          this.where[item] = data[item]
        }
      }

      this.getList(1)
    },
    async getList(val) {
      if (val) {
        this.where.page = val
      }
      const result = await attendanceGroupListApi(this.where)
      this.total = result.data.count
      this.tableData = result.data.list
    },
    async getdata() {
      const result = await attendanceUnattendedMember(this.list)
      this.totalList = result.data.count
      this.listData = result.data.list
    },

    deleteFn(val) {
      this.$modalSure('你确定要删除这条数据吗').then(() => {
        deleteAttendanceGroup(val.id).then((res) => {
          let totalPage = Math.ceil((this.total - 1) / this.where.limit)
          let currentPage = this.where.page > totalPage ? totalPage : this.where.page
          this.where.page = currentPage < 1 ? 1 : currentPage
          this.getList()
        })
      })
    },
    handleClose() {
      this.drawer = false
    },

    pageChange(page) {
      this.where.page = page
      this.getList()
    },
    listSizeChange(val) {
      this.list.limit = val
      this.getdata()
    },
    listPageChange(val) {
      this.list.page = val
      this.getdata()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    },
    addFn() {
      this.$router.push({
        path: `${roterPre}/hr/attendance/setting/addConent`,
        query: {}
      })
    },
    infoFn() {},
    editFn(val) {
      this.$router.push({
        path: `${roterPre}/hr/attendance/setting/addConent`,
        query: { id: val.id }
      })
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
.info {
  color: #1890ff;
  cursor: pointer;
}
.card-box {
  min-height: calc(100vh - 65px);
}

.frame-name {
  .iconfont {
    padding-right: 6px;
  }
}
.icon-h {
  position: relative;
  & > span {
    color: #1890ff;
  }
}

.box {
  padding: 20px;
}
</style>
