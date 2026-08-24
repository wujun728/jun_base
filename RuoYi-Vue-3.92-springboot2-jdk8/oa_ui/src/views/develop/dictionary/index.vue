<template>
  <div class="divBox">
    <div class="v-height-flag">
      <el-card class="mb14 normal-page" v-loading="loadingBox" :body-style="{ padding: '20px 20px 20px 20px' }">
        <oaFromBox
          v-if="search.length > 0"
          :search="search"
          :dropdownList="dropdownList"
          :viewSearch="viewSearch"
          :total="total"
          :title="`字典列表`"
          @addDataFn="addFinance"
          @dropdownFn="batchDelete"
          @confirmData="confirmData"
        ></oaFromBox>

        <!-- 表格数据 -->
        <div class="table-box mt10">
          <el-table
            :data="tableData"
            :height="tableHeight"
            @selection-change="handleSelectionChange"
            style="width: 100%"
            row-key="id"
          >
            <el-table-column type="selection" width="55"> </el-table-column>
            <el-table-column prop="id" label="ID" width="55" type=""></el-table-column>
            <el-table-column prop="name" label="字典名称" min-width="100" show-overflow-tooltip> </el-table-column>
            <el-table-column prop="crud_name" min-width="100" label="关联实体">
              <template slot-scope="scope">
                <div v-if="scope.row.crud_name.length > 0">
                  <span v-for="(item, index) in scope.row.crud_name">{{ item }}</span>
                </div>
                <span v-else>--</span>
              </template>
            </el-table-column>
            <el-table-column prop="ident" label="字典标识" min-width="100" show-overflow-tooltip />
            <el-table-column prop="level" label="层级" min-width="80" />

            <el-table-column prop="mark" label="备注" min-width="150" show-overflow-tooltip>
              <template slot-scope="scope">
                <span>{{ scope.row.mark || '--' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="created_at" label="创建时间" min-width="150" show-overflow-tooltip>
              <template slot-scope="scope">
                <span>{{ scope.row.created_at || '--' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="状态">
              <template slot-scope="scope">
                <el-switch
                  :disabled="scope.row.is_default === 1"
                  v-model="scope.row.status"
                  active-text="启用"
                  inactive-text="停用"
                  :active-value="1"
                  :inactive-value="0"
                  :width="60"
                  @change="handleStatus(scope.row)"
                />
              </template>
            </el-table-column>
            <el-table-column prop="address" :label="$t('public.operation')" width="200">
              <template slot-scope="scope">
                <el-button type="text" v-if="scope.row.is_default !== 1" @click="editFn(scope.row.id)">编辑</el-button>
                <el-button type="text" @click="dataFn(scope.row)">数据管理</el-button>
                <el-button type="text" v-if="scope.row.is_default !== 1" @click="handleDelete(scope.row)"
                  >删除</el-button
                >
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
import { roterPre } from '@/settings'
import oaFromBox from '@/components/common/oaFromBox'
import { getcrudCateListApi, getDatabaseApi } from '@/api/develop'
import { getDictListApi, getDictCreateApi, getDictEditApi, getDictPutShowApi, getDictDeleteShowApi } from '@/api/form'
export default {
  components: { oaFromBox },
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
          field_name: '状态',
          field_name_en: 'status',
          form_value: 'select',
          data_dict: [
            {
              label: '启用',
              value: '1'
            },
            {
              label: '停用',
              value: '0'
            }
          ]
        },
        {
          field_name: '关联实体',
          field_name_en: 'crud_id',
          form_value: 'cascaderSelect',
          data_dict: []
        }
      ],
      dropdownList: [
        {
          label: '批量删除',
          value: 1
        }
      ],
      tableData: [],
      loadingBox: false,
      ids: [],
      where: {
        limit: 15,
        page: 1
      },
      total: 0,
      viewSearch: [],
      entityOptions: [], // 实体数据
      application: [], // 应用数据
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

    this.getCrudAllType()
  },

  methods: {
    // 批量操作表格
    handleSelectionChange(val) {
      this.ids = []
      val.map((item) => {
        this.ids.push(item.id)
      })
    },
    // 获取应用数据v1.8
    getDatabase() {
      getDatabaseApi().then((res) => {
        this.search[2].data_dict = res.data
        this.getList()
      })
    },

    // 获取应用分类
    async getCrudAllType() {
      const data = await getcrudCateListApi()
      this.application = data.data.list
      this.application.forEach((item) => {
        item['value'] = item.id
      })

      this.viewSearch = [
        {
          field: 'cate_id',
          title: '关联应用',
          type: 'select',
          options: this.application
        }
      ]
    },

    // 批量删除
    async batchDelete() {
      if (this.ids.length === 0) {
        return this.$message.error('请先选择要删除的数据')
      }
      let id = this.ids.join(',')
      await this.$modalSure('你确定要批量删除这条内容吗')
      await getDictDeleteShowApi(id)
      let totalPage = Math.ceil((this.total - this.ids.length) / this.where.limit)
      let currentPage = this.where.page > totalPage ? totalPage : this.where.page
      this.where.page = currentPage < 1 ? 1 : currentPage
      await this.getList()
    },

    // 跳转到数据管理
    dataFn(row) {
      let str = JSON.stringify(row)
      this.$router.push({
        path: `${roterPre}/develop/dictionary/management`,
        query: {
          id: row.id
        }
      })
    },

    // 新增
    addFinance() {
      this.$modalForm(getDictCreateApi()).then(({ message }) => {
        this.getList()
      })
    },

    // 编辑
    editFn(id) {
      this.$modalForm(getDictEditApi(id)).then(({ message }) => {
        this.getList()
      })
    },

    // 修改字典状态
    async handleStatus(row) {
      await getDictPutShowApi(row.id, { status: row.status })
      await this.getList()
    },

    // 删除
    async handleDelete(row) {
      await this.$modalSure('你确定要删除这条内容吗')
      await getDictDeleteShowApi(row.id)
      let totalPage = Math.ceil((this.total - 1) / this.where.limit)
      let currentPage = this.where.page > totalPage ? totalPage : this.where.page
      this.where.page = currentPage < 1 ? 1 : currentPage
      await this.getList()
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

    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          name: '',
          crud_id: '',
          cate_id: '',
          status: '',
          limit: 15,
          page: 1
        }
      } else {
        if (data.crud_id) {
          data.crud_id = data.crud_id[data.crud_id.length - 1]
        }
        this.where = { ...this.where, ...data }
      }
      this.getList(1)
    },

    // 获取字典列表
    async getList(val) {
      if (val) {
        this.where.page = 1
      }
      this.loadingBox = true
      const result = await getDictListApi(this.where)
      this.tableData = result.data.list
      this.total = result.data.count
      this.loadingBox = false
    },

    restData() {
      this.where.page = 1
      this.where.cate_id = ''
      this.where.crud_id = ''
      this.where.name = ''
      this.where.status = ''
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
</style>
