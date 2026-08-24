<!-- 人事-职位管理-职位管理 -->
<template>
  <div class="divBox">
    <div>
      <el-card class="employees-card-bottom">
        <div>
          <oaFromBox
            v-if="search.length > 0"
            :dropdownList="dropdownList"
            :isAddBtn="true"
            :isViewSearch="false"
            :search="search"
            :title="`职位管理`"
            :total="total"
            @addDataFn="addJob"
            @confirmData="confirmData"
            @dropdownFn="dropdownFn"
          ></oaFromBox>

          <div class="mt10 table-box" v-loading="loading">
            <el-table
              :data="tableData"
              :height="tableHeight"
              default-expand-all
              row-key="id"
              style="width: 100%"
              @selection-change="handleSelectionChange"
            >
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column :label="$t('hr.jobtitle')" min-width="150" prop="name" />
              <el-table-column :label="$t('hr.immediatesuperior')" min-width="150" prop="cate.name" />
              <el-table-column :label="$t('hr.positionlevel')" min-width="100" prop="rank.alias" />
              <el-table-column label="职位简介" min-width="240" prop="describe" show-overflow-tooltip>
                <template slot-scope="scope">
                  <div class="line1">{{ scope.row.describe || '--' }}</div>
                </template>
              </el-table-column>

              <el-table-column :label="$t('hr.founder')" min-width="120" prop="card.name" />
              <el-table-column :label="$t('public.operation')" prop="describe" show-overflow-tooltip width="200">
                <template slot-scope="scope">
                  <el-button
                    v-hasPermi="['hr:enterprise:job:jobAdministration:check']"
                    type="text"
                    @click="handleDetail(scope.row)"
                    >{{ $t('public.check') }}</el-button
                  >
                  <el-button
                    v-hasPermi="['hr:enterprise:job:jobAdministration:edit']"
                    type="text"
                    @click="handleEdit(scope.row)"
                    >{{ $t('public.edit') }}</el-button
                  >
                  <el-button
                    v-hasPermi="['hr:enterprise:job:jobAdministration:delete']"
                    type="text"
                    @click="handleDelete(scope.row, scope.$index)"
                    >{{ $t('public.delete') }}</el-button
                  >
                </template>
              </el-table-column>
            </el-table>

            <div class="page-fixed">
              <el-pagination
                :current-page="where.page"
                :page-size="where.limit"
                :page-sizes="[15, 20, 30]"
                :total="total"
                layout="total,sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="pageChange"
              />
            </div>
          </div>

          <!-- 查看详情 -->
          <el-drawer
            :before-close="handleDrawerClose"
            :modal="true"
            :title="$t('hr.joboetails')"
            :visible.sync="drawer"
            :wrapper-closable="true"
            direction="rtl"
            size="60%"
          >
            <div v-if="detailData" class="detail-box">
              <div class="item-box">
                <span>{{ $t('hr.jobtitle') }}:</span>
                <div>{{ detailData.name || '--' }}</div>
              </div>
              <div class="item-box">
                <span>{{ $t('hr.numberPosts') }}:</span>
                <div>{{ detailData.job_count || '--' }}</div>
              </div>
              <div class="item-box">
                <span>{{ $t('hr.jobdescriptiones') }}:</span>
                <div>{{ detailData.describe || '--' }}</div>
              </div>
              <div class="item-box">
                <span>{{ $t('hr.creationtime') }}:</span>
                <div>{{ detailData.created_at || '--' }}</div>
              </div>
              <div class="item-box">
                <span>{{ $t('hr.jobresponsibilities') }}:</span>
                <div v-if="detailData.duty == '<p><br></p>'">--</div>
              </div>

              <div class="content-box">
                <div class="content mt20" v-html="detailData.duty" />
              </div>
            </div>
          </el-drawer>
        </div>
      </el-card>
    </div>

    <!-- 新增职位 -->
    <draweForm
      :fromData="fromData"
      :formConfig="formConfig"
      :formDataInit="formDataInit"
      :formRules="formRules"
      ref="draweForm"
      @submit="submit"
      @getRankList="getRankList"
    ></draweForm>
  </div>
</template>

<script>
import oaFromBox from '@/components/common/oaFromBox'
import draweForm from '@/components/form-common/drawer-form'
import { endJobApi, endJobStatusApi, endJobInfoApi, endJobDeleteApi, enterpriseEntInfoApi } from '@/api/enterprise'
import { rankCateListApi, rankListApi, rankJobsPutApi, rankJobsApi, rankJobsAddApi } from '@/api/setting'
export default {
  name: 'JobManage',
  components: {
    oaFromBox,
    draweForm
  },
  data() {
    return {
      drawer: false,
      input: '',
      fromData: {
        title: '添加职位',
        width: '850px',
        type: 'add'
      },
      formDataInit: { name: '', cate_id: '', rank_id: '', describe: '', duty: '' },
      formConfig: [
        {
          type: 'input',
          label: '职位名称：',
          maxlength: 20,
          placeholder: '请输入职位名称',
          key: 'name'
        },
        {
          type: 'select',
          label: '职级类别：',
          placeholder: '请选择职级类别',
          key: 'cate_id',
          options: []
        },
        {
          type: 'select',
          label: '职级：',
          placeholder: '请选择职级',
          key: 'rank_id',
          options: []
        },
        {
          type: 'textarea',
          label: '工作描述：',
          placeholder: '请输入工作描述',
          key: 'describe'
        },
        {
          type: 'richText',
          label: '职位职责：',
          placeholder: '请输入职位职责',
          key: 'duty'
        }
      ],

      formRules: {
        name: [
          { required: true, message: '请输入职位名称', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
        cate_id: [{ required: true, message: '请选择职级类别', trigger: 'change' }],
        rank_id: [{ required: true, message: '请选择职级', trigger: 'change' }]
      },

      rolesConfig: [],
      loading: false,
      tableData: [],
      rankData: [],
      rankDataList: [],
      detailData: null,
      treeData: null,
      companyData: null,
      itemId: 0,
      where: {
        page: 1,
        limit: 15
      },
      total: 0,
      search: [],
      dropdownList: [
        {
          label: '删除',
          value: 1
        }
      ],
      multipleSelection: []
    }
  },
  created() {
    this.getCateList()
    this.getTableData()
    this.getCompanyData()
  },
  methods: {
    // 获取公司信息
    async getCompanyData() {
      const result = await enterpriseEntInfoApi()
      this.companyData = result
    },

    getRankList(id) {
      let obj = {
        page: 1,
        limit: 0,
        cate_id: id
      }
      rankJobsApi(obj).then((res) => {
        this.$nextTick(() => {
          this.formConfig[2].options = res.data.list
        })
      })
    },
    async getCateList() {
      const result = await rankCateListApi({ page: 1, limit: 0 })
      this.rankDataList = result.data.list
      this.formConfig[1].options = this.rankDataList
      this.search = [
        {
          field_name: '职位名称',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '职级类别',
          field_name_en: 'cate_id',
          form_value: 'select',
          data_dict: this.rankDataList
        }
      ]
    },
    dropdownFn() {
      this.batchHandleDelete()
    },
    handleRankClick(index, item) {
      this.rankIndex = index
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    confirmData(data) {
      if (data === 'reset') {
        this.where = {
          page: 1,
          limit: 15
        }
      } else {
        this.where = { ...this.where, ...data }
      }

      this.getTableData(1)
    },
    // 获取表格数据
    async getTableData(val) {
      this.loading = true
      if (val == 1) {
        this.where.page = 1
      }
      const result = await endJobApi(this.where)
      this.tableData = result.data.list || []
      this.loading = false
      this.total = result.data.count
    },
    // 添加职位
    async addJob() {
      this.fromData.title = '添加职位'
      this.fromData.type = 'add'
      this.formDataInit = { name: '', cate_id: '', rank_id: '', describe: '', duty: '' }
      setTimeout(() => {
        this.$refs.draweForm.openBox()
      }, 300)
    },
    // 修改状态
    async handleStatus(item) {
      await endJobStatusApi(item.id, item.status)
    },
    // 查看详情
    async handleDetail(item) {
      const result = await endJobInfoApi(item.id)
      this.detailData = result.data
      this.drawer = true
    },
    handleDrawerClose() {
      this.drawer = false
      this.detailData = null
    },

    submit(data) {
      if (this.itemId > 0) {
        rankJobsPutApi(this.itemId, data).then((res) => {
          if (res.status == 200) {
            this.$refs.draweForm.handleClose()
            this.itemId = 0
          }
        })
      } else {
        rankJobsAddApi(data).then((res) => {
          if (res.status == 200) {
            this.$refs.draweForm.handleClose()
            this.itemId = 0
          }
        })
      }
      setTimeout(() => {
        this.getTableData()
      }, 500)
    },
    // 编辑
    async handleEdit(item) {
      this.itemId = item.id
      try {
        const { data } = await endJobInfoApi(item.id)
        for (let key in this.formDataInit) {
          this.formDataInit[key] = data[key]
        }
        this.getRankList(this.formDataInit.cate_id)
        this.fromData.title = '编辑职位'
        this.fromData.type = 'edit'
        setTimeout(() => {
          this.$refs.draweForm.openBox()
        }, 300)
      } catch (e) {
        this.$messgae.error(e.message)
      }
    },
    // 删除
    handleDelete(item, index) {
      this.$modalSure(this.$t('hr.message3')).then(() => {
        this.endJobDelete(item.id)
      })
    },
    batchHandleDelete() {
      if (this.multipleSelection.length <= 0) {
        this.$message.error('至少选择一项要删除的内容')
      } else {
        var ids = []
        this.multipleSelection.forEach((value) => {
          ids.push(value.id)
        })
        this.$modalSure('确定要全部删除已选择的内容吗').then(() => {
          this.endJobDelete(ids.join(','))
        })
      }
    },
    async endJobDelete(id) {
      await endJobDeleteApi(id)
      if (this.where.page > 1 && this.tableData.length <= 1) {
        this.where.page--
      }
      await this.getTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-box {
  padding: 20px;
  color: #333;
  .item-box {
    display: flex;
    margin-bottom: 20px;
    font-size: 14px;
    span {
      /*width: 80px;*/
    }
    div {
      margin-left: 20px;
    }
  }
  .content-box {
    span {
      font-size: 14px;
    }
    ::v-deep table {
      border: 1px solid #ccc;
    }

    ::v-deep table th {
      border: 1px solid #ccc;
    }
    ::v-deep table td {
      padding: 10px 5px;
      border: 1px solid #ccc;
    }

    ::v-deep p img {
      max-width: 800px;
    }
  }
  .content {
    ::v-deep p {
      font-size: 16px;
    }
    ::v-deep td {
      border: 1px solid;
    }
  }
}
::v-deep .el-drawer__body {
  height: 100%;
  overflow-y: auto;
}
</style>
