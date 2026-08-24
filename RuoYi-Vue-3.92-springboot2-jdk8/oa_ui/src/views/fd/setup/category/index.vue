<!-- 财务-财务设置-发票类目 -->
<template>
  <div class="divBox table-box">
    <el-card class="normal-page">
      <oaFromBox
        :isViewSearch="false"
        :title="$route.meta.title"
        :total="total"
        btnText="添加类目"
        @addDataFn="addFinance"
      ></oaFromBox>
      <el-table
        :data="tableData"
        :tree-props="{ children: 'children' }"
        class="mt10"
        default-expand-all
        row-key="id"
        style="width: 100%"
      >
        <el-table-column label="类目名称" min-width="220" prop="name"> </el-table-column>
        <el-table-column :label="$t('toptable.sort')" min-width="100" prop="sort" />
        <el-table-column :label="$t('public.operation')" prop="address" width="200">
          <template slot-scope="scope">
            <el-button v-hasPermi="['fd:setup:category:edit']" type="text" @click="handleEdit(scope.row)">{{
              $t('public.edit')
            }}</el-button>
            <el-button v-hasPermi="['fd:setup:category:delete']" type="text" @click="deleteFn(scope.row)">{{
              $t('public.delete')
            }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="paginationClass">
        <el-pagination
          :current-page.sync="where.page"
          :page-size.sync="where.limit"
          :page-sizes="[10, 15, 20]"
          :total="total"
          layout="total, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="pageChange"
        />
      </div>
    </el-card>

    <dialog-form ref="repeatDialog" :repeat-data="repeatData" @isOk="invoiceCategoryList()" />
  </div>
</template>
<script>
import { invoiceCategoryList, deleteInvoiceCategory } from '@/api/enterprise'
import dialogForm from '@/views/customer/setup/type/components/addDialog'
import oaFromBox from '@/components/common/oaFromBox'
export default {
  name: '',
  components: {
    dialogForm,
    oaFromBox
  },
  props: {},
  data() {
    return {
      tableData: [],
      where: {
        page: 1,
        limit: 15
      },

      total: 0,
      repeatData: {}
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.invoiceCategoryList()
  },
  methods: {
    // 发票类目列表
    invoiceCategoryList(val) {
      this.where.page = val ? val : this.where.page
      let data = {
        page: this.where.page,
        limit: this.where.limit
      }
      invoiceCategoryList(data).then((res) => {
        this.tableData = res.data.list
        this.total = res.data.count
      })
    },
    pageChange(page) {
      this.where.page = page
      this.invoiceCategoryList('')
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.invoiceCategoryList(1)
    },
    // 添加分类
    async addFinance() {
      this.repeatData = {
        title: '添加类目',
        width: '480px',
        label: 3,
        type: 1,
        data: []
      }
      this.$refs.repeatDialog.dialogVisible = true
    },

    // 编辑分类
    async handleEdit(item) {
      this.repeatData = {
        title: '编辑类目',
        width: '480px',
        label: 3,
        type: 2,
        data: item
      }
      this.$refs.repeatDialog.dialogVisible = true
    },
    // 删除发票分类
    async deleteFn(item) {
      await this.$modalSure(this.$t('customer.message01'))
      await deleteInvoiceCategory(item.id)
      if (this.tableData.length == 1) {
        this.invoiceCategoryList(1)
      } else {
        this.invoiceCategoryList()
      }
    }
  }
}
</script>
<style lang="scss" scoped></style>
