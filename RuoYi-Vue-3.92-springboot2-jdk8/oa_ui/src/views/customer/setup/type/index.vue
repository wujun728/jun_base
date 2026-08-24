<template>
  <div class="divBox">
    <el-card class="normal-page">
      <el-button type="primary" class="mb14" size="small" @click="addFinance">{{ $t('customer.addtype') }}</el-button>
      <div class="splitLine mt10"></div>
      <div class="inTotal">共 {{ total }} 条</div>
      <el-table
        :data="tableData"
        style="width: 100%"
        row-key="id"
        default-expand-all
        :tree-props="{ children: 'children' }"
      >
        <el-table-column prop="value.title" :label="$t('customer.typename')" min-width="220"> </el-table-column>
        <el-table-column prop="sort" :label="$t('toptable.sort')" min-width="100" />
        <el-table-column prop="address" :label="$t('public.operation')" width="200">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">{{ $t('public.edit') }}</el-button>
            <el-button type="text" @click="handleDelete(scope.row)">{{ $t('public.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :page-size="where.limit"
        :current-page="where.page"
        :page-sizes="[10, 15, 20]"
        layout="total, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="pageChange"
      />
    </el-card>
    <!-- 通用弹窗表单   -->
    <dialog-form ref="repeatDialog" :repeat-data="repeatData" @isOk="getTableData()" />
  </div>
</template>

<script>
import dialogForm from './components/addDialog'
import { clientConfigListApi, clientConfigSaveApi } from '@/api/enterprise'
export default {
  name: 'FinanceList',
  components: {
    dialogForm
  },
  data() {
    return {
      tableData: [],
      where: {
        page: 1,
        limit: 15
      },
      total: 0,
      repeatData: {},
      key: 'cate'
    }
  },
  created() {
    this.getTableData()
  },
  methods: {
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    // 获取表格数据
    getTableData(val) {
      this.where.page = val ? val : this.where.page
      var data = {
        page: this.where.page,
        limit: this.where.limit,
        key: this.key
      }
      clientConfigListApi(data).then((res) => {
        this.tableData = res.data.list
        this.total = res.data.count
      })
    },
    // 添加分类
    async addFinance() {
      this.repeatData = {
        title: this.$t('customer.addtype'),
        width: '480px',
        label: 1,
        type: 1,
        data: []
      }
      this.$refs.repeatDialog.dialogVisible = true
    },
    // 编辑分类
    async handleEdit(item) {
      this.repeatData = {
        title: this.$t('customer.edittype'),
        width: '480px',
        label: 1,
        type: 2,
        data: item
      }
      this.$refs.repeatDialog.dialogVisible = true
    },
    // 删除
    handleDelete(item) {
      this.$modalSure(this.$t('customer.message01')).then(() => {
        var deleteArr = []
        deleteArr.push(item.id)
        this.clientConfigSave({ key: this.key, delete: deleteArr })
      })
    },
    // 保存企业设置--客户分类
    clientConfigSave(data) {
      clientConfigSaveApi(data).then((res) => {
        if (this.tableData.length == 1) {
          this.getTableData(1)
        } else {
          this.getTableData()
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped></style>
