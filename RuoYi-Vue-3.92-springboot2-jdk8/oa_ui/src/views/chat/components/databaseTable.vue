<template>
  <div>
    <el-dialog
      title="选择数据库"
      :visible.sync="show"
      width="650px"
      :close-on-click-modal="false"
      :before-close="handleClose"
      top="10vh"
    >
      <div>
        <span class="total">共 {{ selectList.length || '0' }} / {{ tableData.length || '0' }} 项</span>
        <el-input
          v-model="where.keyword"
          placeholder="按数据库表名称搜索"
          prefix-icon="el-icon-search"
          style="width: 250px"
          size="small"
          class="ml10"
          clearable
          @change="getList"
        >
        </el-input>
      </div>
      <el-table
        v-loading="loading"
        ref="multipleTable"
        :data="tableData"
        height="500px"
        style="width: 100%"
        class="mt20"
      >
        <el-table-column width="55">
          <template slot-scope="scope">
            <el-checkbox
              :value="selectedIds.includes(scope.row.table)"
              @change="handleCheckAllChange($event, scope.row)"
            ></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="table" label="数据库表名称" width="180"> </el-table-column>
        <el-table-column prop="comment" label="数据库表描述" width="auto">
          <template slot-scope="scope">
            {{ scope.row.comment || '--' }}
          </template>
        </el-table-column>
      </el-table>

      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose" size="small">取消</el-button>
        <el-button type="primary" @click="submitFn" size="small">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { getDatabesListApi } from '@/api/chatAi'
export default {
  name: '',
  props: {
    list: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      show: false,
      name: '',
      loading: false,
      tableData: [],
      selectList: [],
      isCheck: true,
      selectedIds: [],
      total: 0,
      where: {
        page: 1,
        keyword: ''
      }
    }
  },

  methods: {
    handleCheckAllChange(value, row) {
      if (value) {
        this.selectList.push(row)
        this.selectedIds.push(row.table)
      } else {
        this.selectList = this.selectList.filter((item) => item.table !== row.table)
        this.selectedIds = this.selectedIds.filter((item) => item !== row.table)
      }
    },

    submitFn() {
      this.$emit('submit', this.selectList)
      this.handleClose()
    },

    setInitialSelections() {
      const chunkSize = 30
      let index = 0
      const processChunk = () => {
        const start = index
        const end = Math.min(index + chunkSize, this.tableData.length)
        for (let i = start; i < end; i++) {
          const row = this.tableData[i]
          if (this.selectedIds.includes(row.table)) {
            this.selectList.push(row)
            this.$refs.multipleTable.toggleRowSelection(row, true)
          }
        }
        index = end
        if (index < this.tableData.length) {
          requestAnimationFrame(processChunk)
        }
      }
      processChunk()
    },
    getList() {
      this.loading = true
      getDatabesListApi(this.where).then((res) => {
        this.tableData = res.data
        this.loading = false
      })
    },

    openBox(val) {
      this.getList()
      this.show = true
    },
    handleClose() {
      this.show = false
    }
  }
}
</script>
<style scoped lang="scss">
.total {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 12px;
  color: #909399;
}
::v-deep .el-pagination {
  margin-top: 20px;
}
::v-deep .el-table-column--selection .cell {
  padding-left: 10px;
}
::v-deep .el-dialog__footer {
  padding-top: 6px;
}
::v-deep .el-table__header-wrapper .el-checkbox {
  display: none;
}
</style>
