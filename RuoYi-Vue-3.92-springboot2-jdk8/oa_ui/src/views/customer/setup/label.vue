<template>
  <div class="divBox">
    <el-card class="normal-page">
      <oaFromBox
        :title="$route.meta.title"
        btnText="添加标签组"
        :search="search"
        :total="total"
        :isViewSearch="false"
        :sortSearch="false"
        @addDataFn="addFinance"
      ></oaFromBox>
      <div>
        <div>
          <el-table
            :data="tableData"
            :height="tableHeight"
            style="width: 100%"
            row-key="id"
            :tree-props="{ hasChildren: 'hasChildren', children: 'child' }"
            class="mt10"
          >
            <el-table-column prop="name" :label="$t('customer.labelename')" min-width="150"></el-table-column>
            <el-table-column prop="cate.name" :label="$t('customer.label')" min-width="520">
              <template slot-scope="scope">
                <div class="label-list">
                  <div class="label-name">
                    <div class="el-badge item label-hover" v-for="item in scope.row.children" :key="item.id">
                      <el-button plain size="mini" type="primary" @click="handlePlus(scope.$index, item, 'edit')">{{
                        item.name
                      }}</el-button>
                      <i class="el-icon-error icon-error" @click="handleDelete(item, 2)"></i>
                    </div>
                    <!--操作-->
                    <div class="el-badge item" v-if="tabIndex === scope.$index">
                      <el-input
                        type="text"
                        maxlength="16"
                        size="mini"
                        v-model="label"
                        v-focus
                        @blur="handleSubmit(scope.$index, scope.row)"
                        @keyup.enter.native="handleSubmit(scope.$index, scope.row)"
                        :placeholder="$t('customer.placeholder76')"
                      ></el-input>
                    </div>
                    <div class="el-badge item">
                      <el-button
                        plain
                        size="mini"
                        type="primary"
                        icon="el-icon-plus"
                        @click="handlePlus(scope.$index, scope.row)"
                      ></el-button>
                    </div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="sort" :label="$t('toptable.sort')" min-width="60" />
            <el-table-column prop="address" :label="$t('public.operation')" fixed="right" width="120">
              <template slot-scope="scope">
                <el-button type="text" v-hasPermi="['customer:setup:label:edit']" @click="handleEdit(scope.row)">{{
                  $t('public.edit')
                }}</el-button>

                <el-button
                  type="text"
                  v-hasPermi="['customer:setup:label:delete']"
                  @click="handleDelete(scope.row, 1)"
                  >{{ $t('public.delete') }}</el-button
                >
              </template>
            </el-table-column>
          </el-table>

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
        </div>
      </div>
    </el-card>

    <!-- 通用弹窗表单   -->
    <dialog-form ref="repeatDialog" :repeat-data="repeatData" @isOk="getTableData()" />
  </div>
</template>

<script>
import dialogForm from './type/components/addDialog'
import {
  clientConfigLabelApi,
  clientConfigLabelDeleteApi,
  clientConfigLabelSaveApi,
  putcLientLabel
} from '@/api/enterprise'

export default {
  name: 'FinanceList',
  components: {
    dialogForm,
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  directives: {
    // 注册一个局部的自定义指令v-focus
    focus: {
      // 指令的定义
      inserted: function (el) {
        // 聚焦元素
        el.querySelector('input').focus()
      }
    }
  },
  data() {
    return {
      repeatData: {},
      tableData: [],
      where: {
        page: 1,
        limit: 15
      },
      editData: {},
      editType: 'add',
      total: 0,
      label: '',
      tabIndex: -1,
      search: []
    }
  },
  created() {
    this.getTableData()
  },
  mounted() {},
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
      let data = {
        page: this.where.page,
        limit: this.where.limit
      }
      clientConfigLabelApi(data).then((res) => {
        this.tableData = res.data.list
        this.total = res.data.count
      })
    },
    // 添加标签组
    async addFinance() {
      this.repeatData = {
        title: this.$t('customer.addlabel'),
        width: '480px',
        label: 2,
        type: 1,
        data: []
      }
      this.$refs.repeatDialog.dialogVisible = true
    },
    // 编辑分类
    async handleEdit(item) {
      this.repeatData = {
        title: this.$t('customer.editlabel'),
        width: '480px',
        label: 2,
        type: 2,
        data: item
      }
      this.$refs.repeatDialog.dialogVisible = true
    },
    // 删除
    handleDelete(item, type) {
      const mes = type === 1 ? this.$t('customer.message03') : this.$t('customer.message04')
      this.$modalSure(mes).then(() => {
        clientConfigLabelDeleteApi(item.id).then((res) => {
          if (this.where.page > 1 && this.tableData.length <= 1) {
            this.where.page--
          }
          if (this.tableData.length == 1) {
            this.getTableData(1)
          } else {
            this.getTableData()
          }
        })
      })
    },
    // 客户标签保存
    labelSave(data, type) {
      if (this.editType == 'edit') {
        this.putcLientLabel(this.editData)
      } else {
        clientConfigLabelSaveApi(data).then((res) => {
          this.label = ''
          if (type === 2) {
            this.tabIndex = -1
          }
          this.getTableData()
        })
      }
    },
    handlePlus(index, row, type) {
      this.tabIndex = index
      if (type === 'edit') {
        this.label = row.name
        this.editType = type
        this.editData = row
      } else {
        if (this.label !== '') {
          this.labelSave({ name: this.label, pid: row.id }, 1)
        }
      }
    },
    putcLientLabel(row) {
      let data = {
        name: this.label,
        pid: row.pid
      }
      putcLientLabel(row.id, data).then((res) => {
        this.label = ''
        this.tabIndex = -1
        this.getTableData()
        this.editType = 'add'
      })
    },
    handleSubmit(index, row) {
      if (this.label === '') {
        this.tabIndex = -1
      } else {
        this.labelSave({ name: this.label, pid: row.id }, 2)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
.label-list {
  padding: 0 0 10px 0;
  display: flex;
  align-items: center;
  ::v-deep .el-button--primary.is-plain:hover,
  .el-button--primary.is-plain:focus {
    color: #1890ff;
    background: #e8f4ff;
    border-color: #a3d3ff;
  }
  button {
    outline: none;
  }
  ::v-deep .el-input {
    width: 150px;
  }
  .label-name {
    margin-right: 10px;
    ::v-deep .el-badge {
      margin-right: 6px;
      margin-top: 10px;
    }
    .label-hover:hover .icon-error {
      display: block;
    }
    .icon-error {
      color: #f5222d;
      font-size: 14px;
      position: absolute;
      right: -7px;
      top: -7px;
      display: none;
    }
  }
}
</style>
