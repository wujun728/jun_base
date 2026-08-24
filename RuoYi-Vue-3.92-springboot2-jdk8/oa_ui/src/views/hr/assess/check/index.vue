<template>
  <div class="divBox">
    <el-card>
      <div class="head-box">
        <el-col :lg="10" :xl="8">
          <span>模板搜索</span>
          <el-input v-model="input" placeholder="请输入模板名称" style="width: 60%" />
          <el-button type="primary" icon="el-icon-search">搜索</el-button>
        </el-col>
      </div>
    </el-card>
    <el-card v-if="!templateBtn" class="mt20">
      <el-col :lg="3" :xl="8">
        <!--部门-->
        <departmentNot ref="department" :type="type" />
      </el-col>
      <el-col :lg="21" :xl="8">
        <div class="assess-right">
          <div class="mb15">
            <el-button type="primary" icon="el-icon-plus" @click="addTemplate">添加模板</el-button>
            <el-button type="success" icon="el-icon-plus" @click="addCheck">添加分类</el-button>
          </div>
          <div class="table-box">
            <el-table
              :data="tableData"
              style="width: 100%"
              row-key="id"
              default-expand-all
              :tree-props="{ children: 'children' }"
            >
              <el-table-column prop="id" label="ID" min-width="90" />
              <el-table-column prop="name" label="模板名称" min-width="200" />
              <el-table-column prop="info" label="模板内容" min-width="200" />
              <el-table-column prop="user.name" label="创建人" min-width="160" />
              <el-table-column prop="address" :label="$t('public.operation')" fixed="right" width="200">
                <template slot-scope="scope">
                  <el-button type="text" @click="handleEdit(scope.row)">{{ $t('public.edit') }}</el-button>
                  <el-button type="text" @click="handleImage(scope.row)">编辑封面</el-button>
                  <el-button type="text" @click="handleDelete(scope.row, scope.$index)">{{
                    $t('public.delete')
                  }}</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
        <div class="block">
          <el-pagination
            :page-size="where.limit"
            :current-page="where.page"
            layout="total, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="pageChange"
          />
        </div>
      </el-col>
    </el-card>
    <el-card v-else class="mt20">
      <goalSetting :id="id" ref="goalSetting" :edit-button="isEdit" :add-button="addBtn" @handleGoal="handleGoal" />
    </el-card>
    <!-- 通用弹窗表单   -->
    <dialogForm ref="dialogForm" :roles-config="rolesConfig" :form-data="formBoxConfig" @isOk="getTableData()" />
    <!-- 选择封面   -->
    <preview ref="preview" title="选择封面" :data="rowData" />
  </div>
</template>

<script>
import { assessTemplateListApi, templateDeleteApi } from '@/api/enterprise'
export default {
  name: 'AssessCheck',
  components: {
    dialogForm: () => import('./components/index'),
    preview: () => import('./components/preview'),
    departmentNot: () => import('@/components/user/department'),
    GoalSetting: () => import('@/views/user/assessment/components/goalSetting')
  },
  data() {
    return {
      drawer: false,
      input: '',
      where: {
        page: 1,
        limit: 15
      },
      tableData: [],
      total: 0,
      rolesConfig: [],
      formBoxConfig: {},
      typeOptions: [],
      types: '',
      type: 1,
      id: 0,
      addBtn: true,
      templateBtn: false,
      isEdit: 1,
      rowData: {}
    }
  },
  created() {
    this.getTableData()
  },
  methods: {
    clickDepart(index) {
      this.tabIndex = index
    },
    // 获取表格数据
    async getTableData() {
      var data = {
        page: this.where.page,
        limit: this.where.limit,
        name: this.input,
        cate_id: this.cateId
      }
      const result = await assessTemplateListApi(data)
      this.tableData = result.data.list
      this.total = result.data.count
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    addCheck() {
      this.$refs.department.addCate()
    },
    handleEdit(item) {
      this.templateBtn = true
      this.id = item.id
      this.isEdit = 2
    },
    addTemplate() {
      this.isEdit = 1
      this.templateBtn = true
    },
    handleImage(row) {
      this.rowData = row
      this.$refs.preview.openDialog()
    },
    handleDelete(item, index) {
      this.$modalSure('你确定要删除这条考核模板吗').then(() => {
        templateDeleteApi(item.id).then((res) => {
          this.tableData.splice(index, 1)
        })
      })
    },
    handleGoal() {
      this.templateBtn = false
      this.getTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
.btn-type {
  padding: 2px 12px;
  font-size: 13px;
}
.head-box {
  display: flex;
  align-items: center;
  .input {
    width: 240px;
    margin: 0 20px 0 10px;
  }
}
.assess-right {
  margin-left: 20px;
}
</style>
