<!-- 财务-财务设置-支付方式页面 -->
<template>
  <div class="divBox bill-type">
    <el-card class="normal-page">
      <oaFromBox
        :total="total"
        :title="$route.meta.title"
        :isViewSearch="false"
        :sortSearch="false"
        btnText="添加支付方式"
        @addDataFn="addFinance"
      ></oaFromBox>

      <div class="table-box mt10">
        <el-table :data="tableData" :height="tableHeight" style="width: 100%" row-key="id" default-expand-all>
          <el-table-column prop="name" label="支付名称" min-width="100" />
          <el-table-column prop="info" label="支付简介" min-width="140">
            <template slot-scope="scope">
              {{ scope.row.info || '--' }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="开启状态" min-width="140">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status == 1" type="success">开启</el-tag>
              <el-tag v-else type="danger">关闭</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" min-width="140" />
          <el-table-column prop="info" :label="$t('public.operation')" fixed="right" width="120">
            <template slot-scope="scope">
              <el-button type="text" @click="handleEdit(scope.row)" v-hasPermi="['fd:setup:type:edit']">
                {{ $t('public.edit') }}
              </el-button>
              <el-button
                type="text"
                @click="handleDelete(scope.row, scope.$index)"
                v-hasPermi="['fd:setup:type:delete']"
              >
                {{ $t('public.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="page-fixed">
        <el-pagination
          :page-size="where.limit"
          :current-page="where.page"
          :page-sizes="[15, 20, 30]"
          layout="total, sizes,prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="pageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import {
  enterprisePayTypeApi,
  enterprisePayTypeCreateApi,
  enterprisePayTypeDeleteApi,
  enterprisePayTypeEditApi,
  enterprisePayTypeStatusApi
} from '@/api/enterprise'
import oaFromBox from '@/components/common/oaFromBox'

export default {
  name: 'FinanceCate',
  components: {
    oaFromBox
  },
  data() {
    return {
      tableData: [],
      where: {
        page: 1,
        limit: 15
      },
      total: 0
    }
  },
  created() {
    this.getTableData()
  },
  methods: {
    // 获取表格数据
    getTableData(val) {
      this.where.page = val ? val : this.where.page
      const data = {
        page: this.where.page,
        limit: this.where.limit
      }
      enterprisePayTypeApi(data).then((res) => {
        this.tableData = res.data.list
        this.total = res.data.count
      })
    },
    addFinance() {
      this.$modalForm(enterprisePayTypeCreateApi()).then(({ message }) => {
        this.getTableData()
      })
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData('')
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData(1)
    },
    handleEdit(row) {
      this.$modalForm(enterprisePayTypeEditApi(row.id)).then(({ message }) => {
        this.getTableData()
      })
    },
    handleDelete(row) {
      this.$modalSure('你确定要删除这条分类吗').then(() => {
        enterprisePayTypeDeleteApi(row.id).then((res) => {
          if (this.tableData.length == 1) {
            this.getTableData(1)
          } else {
            this.getTableData()
          }
        })
      })
    },
    async setPayStatus(row, status) {
      var data = {
        id: row.id,
        name: row.name,
        status
      }
      await enterprisePayTypeStatusApi(data)
      this.getTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
.bill-type {
  .text-right {
    text-align: right;
  }
  .el-radio {
    margin-right: 15px;
  }
  .title {
    font-size: 15px;
    font-weight: 600;
    margin-left: 10px;
    position: relative;
    &:after {
      content: '';
      height: 100%;
      width: 3px;
      background-color: #1890ff;
      position: absolute;
      left: -10px;
      top: 0;
    }
  }
}
</style>
