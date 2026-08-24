<template>
  <div class="divBox">
    <el-card>
      <div class="btn-box">
        <el-button type="primary" @click="addtype">{{ $t('hr.addtype') }}</el-button>
      </div>
      <div class="table-box mt20">
        <el-table :data="tableData" style="width: 100%;" :header-cell-style="{ background: '#F2F2F2' }">
          <el-table-column prop="id" label="ID" width="50" style="padding-left: 20px;" />
          <el-table-column prop="name" :label="$t('hr.rankcategoryname')" min-width="180" />
          <el-table-column prop="number" :label="$t('hr.ranknumber')" min-width="180" />
          <el-table-column :label="$t('hr.state')" width="180">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                :active-text="$t('hr.open')"
                :inactive-text="$t('hr.close')"
                :active-value="1"
                :inactive-value="0"
                @change="handleStatus(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="address" :label="$t('public.operation')" fixed="right" width="160">
            <template slot-scope="scope">
              <el-button type="text" @click="deit(scope.row)">{{ $t('public.edit') }}</el-button>
              <el-button type="text" @click="delet(scope.row)">{{ $t('public.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="page">
        <el-pagination
          layout="total, prev, pager, next, jumper"
          :total="count"
          :page-size="pagesize"
          :current-page="page"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import { addRanktype, rankTypeInfo, rankTypeStatusApi, rankTypeEdit, rankTypeDelet } from '@/api/enterprise'
export default {
  name: 'Type',
  data() {
    return {
      tableData: [],
      count: 1,
      pagesize: 10, // 每页显示的条数
      page: 1 // 当前页数
    }
  },
  created() {
    this.Info()
  },
  methods: {
    // 添加职级类型
    addtype() {
      this.$modalForm(addRanktype()).then(({ message }) => {
        this.Info()
      })
    },
    // 获取列表
    async Info() {
      const result = await rankTypeInfo({ page: this.page, limit: this.pagesize })
      this.tableData = result.data.list
      this.count = result.data.count
    },
    // 编辑
    deit(data) {
      this.$modalForm(rankTypeEdit(data.id)).then(({ message }) => {
        this.Info()
      })
    },
    // 删除
    delet(data) {
      this.$modalSure(this.$t('hr.message7')).then(() => {
        this.tableData.splice(data.id, 1)
        rankTypeDelet(data.id).then((res) => {
          this.Info()
        })
      })
    },
    // 修改状态
    async handleStatus(item) {
      await rankTypeStatusApi(item.id, { status: item.status })
    },
    // 分页
    handleCurrentChange(val) {
      this.page = val
      this.Info()
    }
  }
}
</script>

<style scoped></style>
