<template>
  <div>
    <el-drawer title="打卡结果处理记录" :visible.sync="drawer" size="800px" :before-close="handleClose">
      <div class="box">
        <el-table ref="multipleTable" :data="tableData" tooltip-effect="dark" style="width: 100%;">
          <el-table-column prop="shift_number" label="班次信息" width="80">
            <template slot-scope="{ row }">
              {{ getShift(row.shift_number) }}
            </template>
          </el-table-column>
          <el-table-column prop="result" label="打卡结果" width="150"> </el-table-column>
          <el-table-column prop="name" label="备注" width="210" show-overflow-tooltip>
            <template slot-scope="{ row }"> {{ row.remark }}</template>
          </el-table-column>
          <el-table-column prop="name" label="数据来源" width="90">
            <template slot-scope="{ row }">
              {{ row.source == 0 ? '手动修改' : '补卡申请' }}
            </template>
          </el-table-column>
          <el-table-column prop="card.name" label="操作人" width="80"> </el-table-column>
          <el-table-column prop="created_at" label="操作时间" width="150"> </el-table-column>
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
    </el-drawer>
  </div>
</template>

<script>
import { clockRecordApi } from '@/api/config'
export default {
  name: 'CrmebOaEntChangeResult',
  data() {
    return {
      drawer: false,
      total: 0,
      rowId: '',
      tableData: [],
      where: {
        page: 1,
        limit: 15
      }
    }
  },

  methods: {
    openBox(row) {
      this.drawer = true
      this.rowId = row.id
      this.getList()
    },
    async getList() {
      const result = await clockRecordApi(this.rowId, this.where)
      this.tableData = result.data.list
      this.total = result.data.count
    },
    pageChange(page) {
      this.where.page = page
      this.getList()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    },
    handleClose() {
      this.drawer = false
    },
    getShift(data) {
      let str = ''
      if (data == 0) {
        str = '上班1'
      } else if (data == 1) {
        str = '下班1'
      } else if (data == 2) {
        str = '上班2'
      } else if (data == 3) {
        str = '下班2'
      }
      return str
    }
  }
}
</script>

<style lang="scss" scoped>
.box {
  padding: 20px;
}
</style>
