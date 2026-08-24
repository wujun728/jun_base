<template>
  <div class="divBox">
    <div class="v-height-flag">
      <el-card :body-style="{ padding: '20px 20px 20px 20px' }" class="mb14 normal-page">
        <oaFromBox
          v-if="search.length > 0"
          :isViewSearch="false"
          :search="search"
          :title="`接口管理`"
          :total="total"
          @addDataFn="addFinance"
          @confirmData="confirmData"
        ></oaFromBox>

        <!-- 表格数据 -->
        <div v-loading="loading" class="table-box mt10">
          <el-table :data="tableData" :height="tableHeight" row-key="id" style="width: 100%">
            <el-table-column label="接口标题" prop="title"> </el-table-column>
            <el-table-column label="链接地址" prop="url" width="300"> </el-table-column>
            <el-table-column label="请求方式" prop="method"> </el-table-column>
            <el-table-column label="请求类型" prop="is_pre">
              <template slot-scope="scope">
                <el-tag type="warning" size="small" v-if="scope.row.is_pre == 0">直接请求</el-tag>
                <el-tag type="success" size="small" v-if="scope.row.is_pre == 1">授权请求</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" min-width="140" prop="created_at"> </el-table-column>
            <el-table-column fixed="right" label="操作" prop="address" width="110">
              <template slot-scope="scope">
                <el-button type="text" @click="editFn(scope.row.id)">编辑</el-button>
                <el-button type="text" @click="deleteFn(scope.row.id)">删除</el-button>
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
      </el-card>
    </div>
    <!-- 新增/编辑 -->
    <addData ref="addData" @getList="getList"></addData>
  </div>
</template>

<script>
import oaFromBox from '@/components/common/oaFromBox'
import addData from './components/addData.vue'
import { crudGetCurlListApi, crudDeleteCurlApi } from '@/api/develop'
export default {
  name: 'FinanceList',
  components: {
    oaFromBox,
    addData
  },
  data() {
    return {
      search: [
        {
          field_name: '标题或链接搜索',
          field_name_en: 'title',
          form_value: 'input'
        }
      ],
      tableData: [],
      loading: false,
      where: {
        limit: 15,
        page: 1
      },
      total: 0
    }
  },
  created() {
    this.getList()
  },

  methods: {
    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          page: 1,
          limit: 15
        }
      } else {
        this.where = { ...this.where, ...data }
      }
      this.where.page = 1
      this.getList()
    },
    editFn(id) {
      this.$refs.addData.openBox(id)
    },
    addFinance() {
      this.$refs.addData.openBox()
    },

    // 删除触发器
    deleteFn(id) {
      this.$modalSure('您确定要删除此数据吗').then(() => {
        crudDeleteCurlApi(id).then((res) => {
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

    // 获取接口列表
    async getList() {
      this.loading = true
      const data = await crudGetCurlListApi(this.where)
      this.total = data.data.count
      this.tableData = data.data.list
      this.loading = false
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
::v-deep el-tag {
  border: none;
}
</style>
