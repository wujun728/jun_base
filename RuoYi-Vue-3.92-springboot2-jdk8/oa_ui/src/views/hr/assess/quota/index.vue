<template>
  <div class="divBox">
    <el-card>
      <div class="head-box">
        <el-col :lg="10" :xl="8">
          <span>指标搜索</span>
          <el-input v-model="input" placeholder="请输入模板名称" style="width: 60%" />
          <el-button type="primary" icon="el-icon-search" @click="getSearch">搜索</el-button>
        </el-col>
      </div>
    </el-card>
    <el-card class="mt20">
      <el-col :lg="3" :xl="8">
        <!--部门-->
        <departmentNot ref="department" :type="type" @eventOptionData="eventOptionData" />
      </el-col>
      <el-col :lg="21" :xl="8">
        <div class="assess-right">
          <div class="mb15">
            <el-button type="primary" icon="el-icon-plus" @click="addTargetCreate">添加指标</el-button>
            <el-button type="success" icon="el-icon-plus" @click="addCate">添加分类</el-button>
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
              <el-table-column prop="name" label="指标名称" min-width="200" />
              <el-table-column prop="content" label="指标内容" min-width="300" />
              <el-table-column prop="status" label="状态" min-width="100">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.status"
                    :active-text="$t('hr.display')"
                    :inactive-text="$t('hr.hide')"
                    :active-value="1"
                    :inactive-value="0"
                    @change="handleStatus(scope.row)"
                  />
                </template>
              </el-table-column>
              <el-table-column prop="address" :label="$t('public.operation')" fixed="right" width="180">
                <template slot-scope="scope">
                  <el-button type="text" @click="handleEdit(scope.row)">{{ $t('public.edit') }}</el-button>

                  <el-button type="text" @click="handleDelete(scope.row, scope.$index)">
                    {{ $t('public.delete') }}
                  </el-button>
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
    <!-- 通用弹窗表单   -->
    <dialogForm ref="dialogForm" :roles-config="rolesConfig" :form-data="formBoxConfig" @isOk="getTableData()" />
  </div>
</template>

<script>
import {
  assessTargetCreateApi,
  assessTargetDeleteApi,
  assessTargetEditApi,
  assessTargetListApi,
  assessTargetStatusApi
} from '@/api/enterprise'
export default {
  name: 'AssessQuota',
  components: {
    dialogForm: () => import('./components/index'),
    departmentNot: () => import('@/components/user/department')
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
      types: '',
      type: 0,
      cateId: ''
    }
  },
  mounted() {
    this.getTableData()
  },
  methods: {
    // 获取表格数据
    async getTableData() {
      var data = {
        page: this.where.page,
        limit: this.where.limit,
        name: this.input,
        cate_id: this.cateId
      }
      const result = await assessTargetListApi(data)
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
    addCate() {
      this.$refs.department.addCate()
    },
    getSearch() {
      this.where.page = 1
      this.getTableData()
    },
    addTargetCreate() {
      assessTargetCreateApi().then((res) => {
        this.input = ''
        this.formBoxConfig = {
          title: res.data.title,
          width: '500px',
          method: res.data.method,
          action: res.data.action.substr(4)
        }
        res.data.rule[0].value = this.cateId
        this.rolesConfig = res.data.rule
        this.$refs.dialogForm.openBox()
      })
    },
    handleEdit(item) {
      assessTargetEditApi(item.id).then((res) => {
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
    handleDelete(item, index) {
      this.$modalSure('你确定要删除这条指标模板吗').then(() => {
        assessTargetDeleteApi(item.id).then((res) => {
          this.tableData.splice(index, 1)
        })
      })
    },
    eventOptionData(data) {
      this.cateId = data.id
      this.where.page = 1
      this.getTableData()
    },
    handleStatus(item) {
      assessTargetStatusApi(item.id, { status: item.status })
        .then((res) => {
          this.getTableData()
        })
        .catch((error) => {
          item.status = !item.status
        })
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
