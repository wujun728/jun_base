<template>
  <div class="divBox">
    <div class="v-height-flag">
      <el-card :body-style="{ padding: '20px 20px 20px 20px' }" class="mb14 normal-page">
        <oaFromBox
          v-if="search.length > 0"
          :isViewSearch="false"
          :search="search"
          :title="`授权密钥`"
          :total="total"
          @addDataFn="addFinance"
          @confirmData="confirmData"
        ></oaFromBox>

        <!-- 表格数据 -->
        <div v-loading="loading" class="table-box mt10">
          <el-table :data="tableData" :height="tableHeight" row-key="id" style="width: 100%">
            <el-table-column label="密钥名称" prop="title" min-width="100px">
              <template #default="{ row }">
                <span>{{ row.title || '--' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="API Key" prop="ak" min-width="200px">
              <template slot-scope="scope">
                <div class="flex">
                  <div class="over-text w-180">{{ scope.row.ak || '--' }}</div>
                  <span class="iconfont iconfuzhi-01" @click="copy(scope.row.ak)"></span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="Secret Key" prop="info" min-width="220px">
              <template slot-scope="scope">
                <div v-if="!ids.includes(scope.row.id)" class="flex">
                  <div class="over-text w-180 mr10">***********************************</div>
                  <span class="iconfont iconyincang pointer" @click="isShow(scope.row)"></span>
                </div>

                <div v-else class="flex">
                  <div class="over-text w-180 mr10">{{ scope.row.sk || '--' }}</div>
                  <span class="iconfont iconchakan pointer" @click="isShow(scope.row)"></span>
                  <span class="iconfont iconfuzhi-01" @click="copy(scope.row.sk)"></span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="最后登录时间" prop="last_time">
              <template slot-scope="scope">
                <span>{{ scope.row.last_time || '--' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="状态" prop="status">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.status"
                  :active-value="1"
                  :inactive-value="0"
                  active-text="开启"
                  inactive-text="关闭"
                  @change="changeStatus(scope.row)"
                >
                </el-switch>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" prop="created_at" min-width="110px"> </el-table-column>
            <el-table-column fixed="right" label="操作" min-width="80px">
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
    <!-- 新建授权信息 -->
    <addForeign ref="addForeign" @submitOk="getList"></addForeign>
    <!-- 复制密钥 -->
    <el-dialog title="查看密钥" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
      <span>{{ sk || '--' }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="copy()">点击复制</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import oaFromBox from '@/components/common/oaFromBox'
import addForeign from './components/addForeign'
import { deleteOpenKeyApi, getOpenKeyApi, getOpenKeyStatusApi, getFindskApi } from '@/api/develop'
export default {
  name: 'FinanceList',
  components: {
    oaFromBox,
    addForeign
  },
  data() {
    return {
      dialogVisible: false,
      search: [
        {
          field_name: '关键字搜索',
          field_name_en: 'title',
          form_value: 'input'
        }
      ],
      ids: [],
      rowData: {},
      tableData: [],
      loading: false,
      where: {
        limit: 15,
        page: 1
      },
      total: 0,
      sk: ''
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
    handleClose() {
      this.dialogVisible = false
    },
    editFn(id) {
      this.$refs.addForeign.openBox(id)
    },
    addFinance() {
      this.$refs.addForeign.openBox()
    },
    isShow(row) {
      if (this.ids.includes(row.id)) {
        this.ids = this.ids.filter((item) => item != row.id)
      } else {
        this.ids.push(row.id)
      }
    },

    copy(val) {
      clipboard.writeText(val)
      this.$message.success('复制成功')
    },

    // 修改状态
    changeStatus(row) {
      getOpenKeyStatusApi(row.id, { status: row.status }).then((res) => {
        this.getList()
      })
    },

    // 删除触发器
    deleteFn(id) {
      this.$modalSure('您确定要删除此数据吗').then(() => {
        deleteOpenKeyApi(id).then((res) => {
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

    // 获取接口列表
    async getList(val) {
      if (val) {
        this.where.page = val
      }
      this.loading = true
      const data = await getOpenKeyApi(this.where)
      this.total = data.data.count
      this.tableData = data.data.list
      this.loading = false
      if(!this.tableData0) return
      this.tableData.forEach((item) => {
        item.show = false
      })
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
.w-180 {
  min-width: 180px;
}
::v-deep el-tag {
  border: none;
}
.iconfuzhi-01 {
  margin-left: 10px;
  cursor: pointer;
  color: #1890ff;
}
</style>
