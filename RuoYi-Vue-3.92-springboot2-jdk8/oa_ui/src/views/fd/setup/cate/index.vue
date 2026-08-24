<!-- 财务-财务科目-收入/支出分类页面 -->
<template>
  <div class="divBox">
    <el-card :body-style="{ padding: '20px 20px 0 20px' }" class="employees-card-bottom card-box">
      <oaFromBox
        :isTotal="false"
        :isViewSearch="false"
        :search="search"
        :sortSearch="false"
        :title="$route.meta.title"
        btnText="新增分类"
        @addDataFn="addFinance"
        @confirmData="confirmData"
      ></oaFromBox>
      <div class="v-height-flag table-box mt10" v-loading="loading">
        <el-table
          :data="tableData"
          :expand-row-keys="expands"
          :max-height="tableHeight"
          :row-key="getRowKeys"
          :tree-props="{ children: 'children' }"
          style="width: 100%"
        >
          <el-table-column :label="$t('finance.accounttabtype')" min-width="180" prop="name" />

          <el-table-column :label="$t('finance.accounttabtitle')" min-width="100" prop="types">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.types === 0" plain size="small" type="warning">{{ $t('finance.pay') }}</el-tag>
              <el-tag v-else plain size="small" type="success">{{ $t('finance.income') }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="$t('toptable.sort')" min-width="100" prop="sort" />

          <el-table-column :label="$t('public.operation')" fixed="right" prop="address" width="230">
            <template slot-scope="scope">
              <el-button type="text" @click="handleAdd(scope.row)"> 添加子分类 </el-button>
              <el-button v-hasPermi="['fd:setup:cate:income:edit']" type="text" @click="handleEdit(scope.row)">
                {{ $t('public.edit') }}
              </el-button>
              <el-button
                v-if="!scope.row.children"
                v-hasPermi="['fd:setup:cate:income:delete']"
                type="text"
                @click="handleDelete(scope.row, scope.$index)"
              >
                {{ $t('public.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 通用弹窗表单   -->
    <dialogForm
      ref="dialogForm"
      :form-data="formBoxConfig"
      :roles-config="rolesConfig"
      @add="addCategory"
      @isOk="getTableData()"
    />
  </div>
</template>

<script>
import dialogForm from './components/index'
import oaFromBox from '@/components/common/oaFromBox'
import { billCateApi, billCateCreateApi, billCateEditApi, billCateDeleteApi } from '@/api/enterprise'

export default {
  name: 'FinanceCate',
  components: {
    dialogForm,
    oaFromBox
  },
  props: {
    activeName: {
      type: String,
      default: '1'
    },
    types: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      drawer: false,
      input: '',
      formBoxConfig: {
        title: this.$t('hr.addposition'),
        width: '500px',
        method: 'post',
        action: 'jobs'
      },
      where: {
        page: 1,
        limit: 15,
        types: 1,
        name: ''
      },
      expands: [],

      total: 0,
      tableHeight: 0,
      rolesConfig: [],
      loading: false,
      tableData: [],
      detailData: null,
      treeData: null,
      companyData: null,
      typeOptions: [
        { label: this.$t('finance.all'), value: '' },
        { label: this.$t('finance.income'), value: '1' },
        { label: this.$t('finance.pay'), value: '0' }
      ],
      search: [
        {
          form_value: 'input',
          field_name_en: 'name',
          field_name: '账目分类'
        }
      ]
    }
  },
  created() {
    this.tableHeight = window.innerHeight - 215
    this.where.types = this.types
    this.getTableData()
  },
  methods: {
    getRowKeys(row) {
      return row.id
    },
    // 获取表格数据
    getTableData() {
      this.loading = true
      billCateApi(this.where).then((res) => {
        this.tableData = res.data
        this.total = res.data.count
        this.loading = false
        this.tableData.map((i) => {
          this.expands.push(i.id + '')
        })
      })
    },
    handleAdd(row) {
      billCateCreateApi(row.id).then((res) => {
        this.formBoxConfig = {
          title: '添加子分类',
          width: '500px',
          method: res.data.method,
          type: 1,
          action: res.data.action.substr(4)
        }
        if (this.activeName == '1') {
          res.data.rule[0].value = 1
        } else {
          res.data.rule[0].value = 0
        }
        if (row.path.length == 0) {
          this.$nextTick(() => {
            this.$refs.dialogForm.$refs.fc.$f.setValue({ path: [0, row.id] })
          })
        } else {
          row.path.unshift(0)
          row.path.push(row.id)
          this.$nextTick(() => {
            this.$refs.dialogForm.$refs.fc.$f.setValue({ path: row.path })
          })
        }
        res.data.rule[2].type = 'input'
        res.data.rule[2].props.type = 'number'
        this.rolesConfig = res.data.rule
        this.$refs.dialogForm.openBox()
      })
    },
    addCategory() {
      this.rolesConfig[2].value = ''
      this.getTableData()
    },
    // 添加分类
    async addFinance() {
      billCateCreateApi(0).then((res) => {
        this.formBoxConfig = {
          title: this.$t('finance.addpositionType'),
          width: '500px',
          type: 1,
          method: res.data.method,
          action: res.data.action.substr(4)
        }
        if (this.activeName == 1) {
          res.data.rule[0].value = 1
        } else {
          res.data.rule[0].value = 0
        }

        this.rolesConfig = res.data.rule
        this.$refs.dialogForm.openBox()
      })
    },
    // 编辑分类
    async handleEdit(item) {
      billCateEditApi(item.id).then((res) => {
        var data = res.data
        this.formBoxConfig = {
          title: data.title,
          width: '500px',
          method: data.method,
          action: data.action.substr(4)
        }

        this.rolesConfig = data.rule
        this.$refs.dialogForm.openBox()
      })
    },
    // 删除
    async handleDelete(item, index) {
      await this.$modalSure(this.$t('finance.message3'))
      await billCateDeleteApi(item.id)
      this.getTableData()
    },
    confirmData(data) {
      if (data === 'reset') {
        this.where = {
          page: 1,
          limit: 15,
          types: this.types
        }
      } else {
        this.where = { ...this.where, ...data }
      }

      this.getTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
.card-box {
  height: calc(100vh - 210px);
  overflow-y: scroll;
}
::v-deep .el-drawer__body {
  height: 100%;
  overflow-y: auto;
}
</style>
