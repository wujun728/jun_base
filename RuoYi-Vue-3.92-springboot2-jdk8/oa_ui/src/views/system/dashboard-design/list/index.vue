<template>
  <div class="divBox">
    <div class="v-height-flag">
      <el-card class="mb14 normal-page" :body-style="{ padding: '20px 20px 20px 20px' }">
        <div class="flex-between">
          <div class="title-16">图表列表</div>
          <div class="flex">
            <el-button type="primary" size="small" icon="el-icon-plus" @click="addFinance">新建图表</el-button>
          </div>
        </div>

        <el-form :inline="true" class="from-s mt14" @submit.native.prevent>
          <div class="flex">
            <div class="inTotal">共 {{ total }} 项</div>
            <el-form-item label="" class="select-bar">
              <el-input
                v-model="where.name"
                size="small"
                @keyup.native.stop.prevent.enter="getList"
                clearable
                style="width: 250px"
                @change="getList"
                placeholder="请输入关键字"
              />
            </el-form-item>
            <el-form-item>
              <el-tooltip effect="dark" content="重置搜索条件" placement="top">
                <div class="reset" @click="restData"><i class="iconfont iconqingchu"></i></div>
              </el-tooltip>
            </el-form-item>
          </div>
        </el-form>

        <!-- 表格数据 -->
        <div class="table-box" v-loading="loading">
          <el-table :data="tableData" :height="tableHeight" style="width: 100%" row-key="id">
            <el-table-column prop="name" label="图表名称">
              <template slot-scope="scope">
                <span class="color-doc pointer" @click="designFn(scope.row)"> {{ scope.row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="updated_at" label="更新时间"> </el-table-column>
            <el-table-column prop="address" label="操作" fixed="right" width="150">
              <template slot-scope="scope">
                <el-button type="text" @click="designFn(scope.row)">设计</el-button>
                <el-button type="text" @click="editFn(scope.row)">编辑</el-button>
                <el-button type="text" @click="deleteFn(scope.row)">删除</el-button>
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
      </el-card>
    </div>
    <!-- 新建字段弹窗 -->
    <oa-dialog
      ref="oaDialog"
      :fromData="fromData"
      :formConfig="formConfig"
      :formRules="formRules"
      :formDataInit="formDataInit"
      @submit="submit"
    ></oa-dialog>
  </div>
</template>

<script>
import oaDialog from '@/components/form-common/dialog-form'
import { roterPre } from '@/settings'
import {
  getcrudCateListApi,
  databaseListApi,
  dataEventGuanListApi,
  dataEventActionApi,
  dataEventTypeApi,
  dataEventSaveApi,
  dashboardDelApi
} from '@/api/develop'
import { dashboardList, saveDashboard, changeDashboard } from '@/api/chart'
export default {
  name: 'dashboard-list',
  components: {
    oaDialog
  },
  data() {
    return {
      tableData: [],
      loading: false,
      ids: [],
      fromData: {
        width: '600px',
        title: '新建图表',
        btnText: '确定',
        labelWidth: '100px',
        type: ''
      },
      formConfig: [
        {
          type: 'input',
          label: '图表名称：',
          placeholder: '请输入图表名称',
          key: 'name',
          value: '',
          options: []
        }
      ],
      formRules: {
        name: [
          {
            required: true,
            message: '请输入图表名称',
            trigger: 'blur'
          }
        ]
      },
      actionList: [],
      formDataInit: {
        name: ''
      },
      where: {
        name: '',
        limit: 15,
        page: 1
      },
      entityOptions: [], // 实体数据
      application: [], // 应用数据
      total: 0,
      options: [
        {
          label: '启用',
          value: 1
        },
        {
          label: '停用',
          value: 0
        }
      ]
    }
  },
  created() {
    this.getList()
  },

  methods: {
    async getCrudAllType() {
      const data = await getcrudCateListApi()
      this.application = data.data.list
    },
    getEvent(val) {
      let index = this.formConfig[1].options.findIndex((item) => item.value === val)
      return this.formConfig[1].options[index] ? this.formConfig[1].options[index].label : '--'
    },
    getString(arr) {
      return arr.join('、')
    },
    getAction(val) {
      let textArr = []
      this.actionList.map((item) => {
        val.map((key) => {
          if (item.value == key) {
            textArr.push(item.label)
          }
        })
      })
      return textArr.join('/')
    },

    // 获取执行动作类型
    getActionList() {
      dataEventActionApi().then((res) => {
        this.actionList = res.data
      })
    },

    // 新建图表
    submit(data) {
      let fuc = this.id ? changeDashboard(this.id, data) : saveDashboard(data)
      fuc.then((res) => {
        this.getList()
        this.$refs.oaDialog.handleClose()
      })
    },

    // 新增
    addFinance() {
      this.id = 0
      this.formDataInit.name = ''
      this.fromData.title = '新建图表'
      this.$refs.oaDialog.openBox()
    },

    // 编辑
    editFn(row) {
      this.id = row.id
      this.fromData.title = '编辑图表'
      this.formDataInit.name = row.name
      this.$refs.oaDialog.openBox()
    },

    // 删除图表
    deleteFn(row) {
      this.$modalSure('您确定要删除此看板吗').then(() => {
        dashboardDelApi(row.id).then((res) => {
          let totalPage = Math.ceil((this.total - 1) / this.where.limit)
          let currentPage = this.where.page > totalPage ? totalPage : this.where.page
          this.where.page = currentPage < 1 ? 1 : currentPage
          this.getList()
        })
      })
    },

    // 分页
    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    },

    pageChange(page) {
      this.where.page = page
      this.getList()
    },

    // 获取图表列表
    async getList() {
      this.loading = true
      const data = await dashboardList(this.where)
      this.total = data.data.count
      this.tableData = data.data.list
      this.loading = false
    },
    // 设计
    designFn(row) {
      // 新页面打开
      window.open(roterPre + '/dashboard-design?chartId=' + row.id, '_blank')
    },
    restData() {
      this.where.page = 1
      this.where.name = ''
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.inTotal {
  margin: 0;
  line-height: 32px;
  margin-right: 14px;
}
.title {
  font-size: 16px;
  font-weight: 500;
}
</style>
