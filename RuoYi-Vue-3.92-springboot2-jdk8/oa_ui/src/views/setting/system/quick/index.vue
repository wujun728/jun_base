<template>
  <div class="divBox">
    <el-card class="normal-page" :body-style="{ padding: 0 }">
      <el-row>
        <el-col v-bind="gridl">
          <left @eventOptionData="eventOptionData"></left>
        </el-col>
        <el-col v-bind="gridr" class="assess-right">
          <div class="ml14 mr14">
            <div class="clearfix el-card__header">
              <oaFromBox
                :search="searchData"
                :title="$route.meta.title"
                :total="total"
                :isViewSearch="false"
                :sortSearch="false"
                btnText="新建入口"
                @addDataFn="handleNews"
                @confirmData="confirmData"
              ></oaFromBox>
            </div>
            <div class="mt14 table-box">
              <el-table :data="tableData" style="width: 100%" row-key="id" :height="tableHeight" default-expand-all>
                <el-table-column prop="name" label="标题" min-width="100">
                  <template slot-scope="scope">
                    <div class="over-text2">{{ scope.row.name }}</div>
                  </template>
                </el-table-column>
                <el-table-column prop="pc_url" label="pc端地址" min-width="120">
                  <template slot-scope="scope">
                    <div class="over-text2">{{ scope.row.pc_url }}</div>
                  </template>
                </el-table-column>
                <el-table-column prop="uni_url" label="移动端地址" min-width="120">
                  <template slot-scope="scope">
                    <div class="over-text2">{{ scope.row.uni_url }}</div>
                  </template>
                </el-table-column>
                <el-table-column prop="name" label="封面" min-width="80">
                  <template slot-scope="scope">
                    <img v-if="scope.row.image" class="table-img" :src="scope.row.image" alt="" />
                    <img v-else class="table-img" src="../../../../assets/images/index/quick-icon-09.png" alt="" />
                  </template>
                </el-table-column>
                <el-table-column prop="card.name" label="pc端显示" min-width="80">
                  <template slot-scope="scope">
                    <span>{{ scope.row.pc_show === 1 ? '显示' : '隐藏' }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="card.name" label="移动端显示" min-width="90">
                  <template slot-scope="scope">
                    <span>{{ scope.row.uni_show === 1 ? '显示' : '隐藏' }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="状态" min-width="80">
                  <template slot-scope="scope">
                    <el-switch
                      v-model="scope.row.status"
                      active-text="开启"
                      inactive-text="关闭"
                      :active-value="1"
                      :inactive-value="0"
                      @change="handleStatus(scope.row)"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="describe" :label="$t('public.operation')" fixed="right" width="120">
                  <template slot-scope="scope">
                    <el-button type="text" @click="handleEdit(scope.row)" v-hasPermi="['system:quick:edit']">{{
                      $t('public.edit')
                    }}</el-button>
                    <el-button type="text" @click="handleDelete(scope.row.id)" v-hasPermi="['system:quick:delete']">
                      {{ $t('public.delete') }}
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <div class="paginationClass">
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
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import left from './components/left'
import {
  configQuickCreateApi,
  configQuickDeleteApi,
  configQuickEditApi,
  configQuickListApi,
  configQuickShowApi
} from '@/api/setting'

export default {
  name: 'SystemQuick',
  components: {
    left,
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  data() {
    return {
      where: {
        page: 1,
        limit: 15,
        cid: '',
        name: ''
      },
      gridl: {
        xl: 3,
        lg: 4,
        md: 5,
        sm: 6,
        xs: 24
      },
      gridr: {
        xl: 21,
        lg: 20,
        md: 19,
        sm: 18,
        xs: 24
      },
      total: 0,
      tableData: [],
      isCheck: true,
      noticeData: {
        id: 0,
        isEdit: false,
        category: 0,
        optionData: []
      },
      searchData: [
        {
          field_name: '标题',
          field_name_en: 'name',
          form_value: 'input'
        }
      ]
    }
  },

  methods: {
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    // 获取表格数据
    getTableData() {
      configQuickListApi(this.where).then((res) => {
        this.tableData = res.data.list || []
        this.total = res.data.count
      })
    },
    handleSearch() {
      this.where.page = 1
      this.getTableData()
    },
    handleNews() {
      this.$modalForm(configQuickCreateApi({ cid: this.where.cid })).then(({ message }) => {
        this.getTableData()
      })
    },
    isNotice() {
      this.isCheck = true
      this.where.page = 1
      this.getTableData()
    },
    handleEdit(row) {
      this.$modalForm(configQuickEditApi(row.id)).then(({ message }) => {
        this.getTableData()
      })
    },
    async handleDelete(id) {
      await this.$modalSure('你确定要删除这条内容吗')
      await configQuickDeleteApi(id)

      await this.getTableData()
    },
    async handleStatus(row) {
      await configQuickShowApi(row.id, { status: row.status })
      await this.getTableData()
    },
    eventOptionData(data) {
      this.where.cid = data.id
      this.noticeData.category = data.id
      this.handleSearch()
    },
    confirmData(data) {
      if (data == 'reset') {
        this.where.name = ''
      } else {
        this.where = { ...this.where, ...data }
      }
      this.where.page = 1
      this.getTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
.assess-right {
  padding: 14px;
  border-left: 1px solid #eeeeee;
  ::v-deep .el-card__header {
    border-bottom: none;
    padding: 0;
  }
}
::v-deep .el-row,
::v-deep .el-card,
::v-deep .el-card__body,
::v-deep .el-col {
  height: 100%;
}
.right-con {
  display: flex;
  justify-content: flex-end;
}
.table-img {
  width: 40px;
  height: 40px;
}
.select-bar {
  margin-bottom: 0;
  ::v-deep .el-input-group__append {
    top: 0;
    button {
      color: #fff;
      background-color: #1890ff;
      border-color: #1890ff;
      border-radius: 0 5px 5px 0;
    }
  }
}
</style>
