<template>
  <div class="divBox">
    <div class="v-height-flag">
      <el-card class="mb14 normal-page" :body-style="{ padding: '20px 20px 20px 20px' }">
        <oaFromBox
          v-if="search.length > 0"
          :search="search"
          :total="total"
          :isViewSearch="false"
          :title="`流程列表`"
          @addDataFn="addFinance"
          @confirmData="confirmData"
        ></oaFromBox>

        <!-- 表格数据 -->
        <div class="table-box mt10" v-loading="loading">
          <el-table row-key="id" :height="tableHeight" :data="tableData" style="width: 100%">
            <el-table-column prop="name" :label="$t('business.businessType')" min-width="150">
              <template #default="{ row }">
                <div class="flex" @click="editFn(row)">
                  <div class="selIcon" :style="{ backgroundColor: row.color }">
                    <i class="icon iconfont" :class="row.icon"></i>
                  </div>
                  <div class="ml10 color-doc pointer">{{ row.name || '--' }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="refuse" label="发起人范围" min-width="200" show-overflow-tooltip>
              <template #default="{ row }">
                <span>{{ row.userList ? row.userList : '所有人' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="info" label="审批说明" min-width="200" show-overflow-tooltip>
              <template #default="{ row }">
                {{ row.info || '--' }}
              </template>
            </el-table-column>
            <el-table-column prop="crud" label="关联实体" min-width="140">
              <template #default="{ row }">
                {{ row.crud.table_name || '--' }}
              </template>
            </el-table-column>
            <el-table-column prop="cate_name" label="关联应用" min-width="140">
              <template #default="{ row }">
                <span>{{ row.cate_name ? row.cate_name : '--' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="sort" label="排序" />
            <el-table-column prop="updated_at" label="更新时间" min-width="150" />
            <el-table-column label="状态" width="120">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.status"
                  :active-text="$t('hr.open')"
                  :inactive-text="$t('hr.close')"
                  :active-value="1"
                  :inactive-value="0"
                  :width="50"
                  @change="handleStatus(scope.row)"
                />
              </template>
            </el-table-column>

            <el-table-column prop="address" label="操作" fixed="right" width="110">
              <template slot-scope="scope">
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
              layout="total,sizes, prev, pager, next, jumper"
              :total="total"
              @size-change="handleSizeChange"
              @current-change="pageChange"
            />
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import oaFromBox from '@/components/common/oaFromBox'
import { roterPre } from '@/settings'
import {
  getcrudCateListApi,
  databaseListApi,
  getDatabaseApi,
  dataApproveListApi,
  dataApproveStatusApi,
  dataApproveDeleteApi
} from '@/api/develop'

export default {
  components: {
    oaFromBox
  },
  name: 'FinanceList',
  data() {
    return {
      search: [
        {
          field_name: '关键字',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '关联实体',
          field_name_en: 'crud_id',
          form_value: 'cascaderSelect',
          data_dict: []
        },
        {
          field_name: '关联应用',
          field_name_en: 'cate_id',
          form_value: 'select',
          data_dict: []
        }
      ],
      loading: false,
      tableData: [],
      ids: [],
      where: {
        name: '',
        cate_id: '',
        crud_id: '',
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
    this.getDatabase()
    this.getList()
    this.getCrudAllType()
  },

  methods: {
    // 获取列表数据
    async getList() {
      this.loading = true
      const data = await dataApproveListApi(this.where)
      this.total = data.data.count
      this.tableData = data.data.list
      this.loading = false
    },
    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          name: '',
          crud_id: '',
          cate_id: '',
          limit: 15,
          page: 1
        }
      } else {
        if (data.crud_id) {
          data.crud_id = data.crud_id[data.crud_id.length - 1]
        }
        this.where = { ...this.where, ...data }
      }
      this.getList()
    },

    // 获取应用分类
    async getCrudAllType() {
      const data = await getcrudCateListApi()
      this.application = data.data.list
      this.search[2].data_dict = this.application
    },
    // 获取应用数据v1.8
    getDatabase() {
      getDatabaseApi().then((res) => {
        this.search[1].data_dict = res.data
      })
    },

    // 修改状态
    async handleStatus(row) {
      await dataApproveStatusApi(row.id, { status: row.status })
      this.getList()
    },

    // 新增
    addFinance() {
      const { href } = this.$router.resolve({
        path: `${roterPre}/process`,
        query: {
          id: 0
        }
      })
      window.open(href, '_blank')
    },

    // 编辑
    editFn(row) {
      const { href } = this.$router.resolve({
        path: `${roterPre}/process`,
        query: {
          crud_id: row.crud_id,
          id: row.id
        }
      })
      window.open(href, '_blank')
    },

    // 删除
    async deleteFn(row) {
      this.$modalSure('您确定要删除此流程吗').then(() => {
        dataApproveDeleteApi(row.id).then((res) => {
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

    restData() {
      this.where.page = 1
      this.where.name = ''
      this.where.status = ''
      this.where.cate_id = ''
      this.where.crud_id = ''

      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.title {
  font-size: 16px;
  font-weight: 500;
}
.inTotal {
  margin: 0;
  line-height: 32px;
  margin-right: 14px;
}
.selIcon {
  width: 25px;
  height: 25px;
  line-height: 25px;
  display: inline-block;
  text-align: center;
  cursor: pointer;
  border-radius: 3px;
  .iconfont {
    font-size: 13px;
    color: #fff;
  }
}
</style>
