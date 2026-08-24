<template>
  <div>
    <!-- 考勤班次弹窗 -->
    <el-drawer
      title="选择班次"
      :visible.sync="drawer"
      size="700px"
      :wrapperClosable="false"
      :before-close="handleClose"
    >
      <!-- 搜索 -->
      <div class="box">
        <div class="flex mb10 flex-between">
          <el-input
            size="small"
            placeholder="请输入内容"
            v-model="where.name"
            class="input-with-select"
            style="width: 250px"
            @change="getList(1)"
          >
            <el-button slot="append" icon="el-icon-search" @click="getList(1)"></el-button>
          </el-input>
          <el-button size="small" class="ml10" @click="addFn">新建班次</el-button>
        </div>

        <!-- 表格 -->
        <el-table
          ref="multipleTable"
          :data="tableData"
          tooltip-effect="dark"
          style="width: 100%"
          :row-key="getRowKeys"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" :reserve-selection="true"> </el-table-column>
          <el-table-column prop="name" label="班次名称" width="120"> </el-table-column>
          <el-table-column prop="position" label="考勤时间" width="170">
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
          <el-table-column prop="card.name" label="创建人" show-overflow-tooltip> </el-table-column>
          <el-table-column prop="address" label="操作" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-button type="text" @click="checkFn(scope.row)">查看班次</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="paginationClass">
          <el-pagination
            :page-size="where.limit"
            :current-page="where.page"
            :page-sizes="[10, 15, 20]"
            layout="total, prev, pager, next"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="pageChange"
          />
        </div>
      </div>
      <div class="button from-foot-btn fix btn-shadow">
        <el-button @click="handleClose" size="small">取消</el-button>
        <el-button type="primary" size="small" @click="submitForm">确定</el-button>
      </div>
    </el-drawer>
    <!-- 新建班次 -->
    <add-shift ref="addShift" @getList="getList"></add-shift>
  </div>
</template>

<script>
import addShift from './addShift'
import { attendanceShiftListApi } from '@/api/config'

export default {
  name: 'CrmebOaEntShiftList',
  components: { addShift },

  data() {
    return {
      drawer: false,
      tableData: [],
      total: 0,
      where: {
        page: 1,
        limit: 15,
        name: ''
      },
      rowData: [],
      selectedList: []
    }
  },

  mounted() {
    this.getList()
  },

  methods: {
    getRowKeys(row) {
      return row.id
    },
    async getList(val) {
      if (val) {
        this.where.page = val
      }
      const result = await attendanceShiftListApi(this.where)
      this.total = result.data.count
      this.tableData = result.data.list
    },
    handleSelectionChange(e) {
      this.selectedList = e
    },
    // 保存
    submitForm() {
      this.$emit('selected', this.selectedList)
      this.handleClose()
    },
    pageChange(page) {
      this.where.page = page
      this.getList()

      if (this.rowData.length !== 0) {
        setTimeout(() => {
          this.tableData.map((item) => {
            this.rowData.map((item1) => {
              if (item.id == item1.id) {
                this.$refs.multipleTable.toggleRowSelection(item, true)
              }
            })
          })
        }, 300)
      }
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    },
    openBox(data) {
      this.drawer = true
      if (data) {
        this.rowData = data
        this.$nextTick(() => {
          this.tableData.map((item) => {
            this.rowData.map((item1) => {
              if (item.id == item1.id) {
                this.$refs.multipleTable.toggleRowSelection(item, true)
              }
            })
          })
        })
      }
    },
    checkFn(val) {
      this.$refs.addShift.openBox(val.id, 'check')
    },
    addFn() {
      this.$refs.addShift.openBox()
    },
    handleClose() {
      this.drawer = false
    }
  }
}
</script>

<style lang="scss" scoped>
.box {
  padding: 20px;
  height: 100%;
  padding-bottom: 0;
  overflow-y: scroll;
  .flex {
    display: flex;
    align-items: center;
  }

  ::v-deep.el-input-group__append {
    background-color: #1890ff;
    color: #fff;
  }
  ::v-deep .el-input-group__append {
    top: 0;
  }
}
</style>
