<!-- 行政-企业动态页面 -->
<template>
  <div class="divBox">
    <div v-show="isCheck">
      <el-card :body-style="{ padding: '20px' }" class="card-box">
        <div>
          <el-row>
            <el-col v-bind="gridl">
              <left @eventOptionData="eventOptionData" ref="left"></left>
            </el-col>
            <el-col v-bind="gridr" class="assess-right">
              <div class="ml14">
                <oaFromBox
                  :search="search"
                  :total="total"
                  :isViewSearch="false"
                  :sortSearch="false"
                  :title="$route.meta.title"
                  btnText="新建公告"
                  @addDataFn="handleNews"
                  @confirmData="confirmData"
                ></oaFromBox>

                <div class="mt14">
                  <el-table :data="tableData" :height="tableHeight" style="width: 100%" row-key="id" default-expand-all>
                    <el-table-column prop="name" label="封面" min-width="80">
                      <template slot-scope="scope">
                        <img class="table-img" v-if="!scope.row.cover" :src="defaultImage" alt="" />
                        <img class="table-img" v-else :src="scope.row.cover" alt="" />
                      </template>
                    </el-table-column>
                    <el-table-column prop="title" label="标题" min-width="150" show-overflow-tooltip />
                    <el-table-column prop="visit" label="已读人数" min-width="80" />
                    <el-table-column prop="info" label="公告简介" min-width="200" show-overflow-tooltip>
                      <template slot-scope="scope">
                        <div class="over-text2">{{ scope.row.info || '--' }}</div>
                      </template>
                    </el-table-column>
                    <el-table-column prop="card.name" label="创建人" min-width="80" />
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
                    <el-table-column prop="created_at" label="创建时间" min-width="120">
                      <template slot-scope="scope">
                        {{ $moment(scope.row.created_at).format('yyyy-MM-DD') }}
                      </template>
                    </el-table-column>
                    <el-table-column prop="is_top" label="置顶" width="100">
                      <template slot-scope="scope">
                        <el-switch
                          v-model="scope.row.is_top"
                          active-text="已置顶"
                          inactive-text="未置顶"
                          :active-value="1"
                          :inactive-value="0"
                          :width="70"
                          @change="putNoticeTop(scope.row.id)"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column prop="describe" :label="$t('public.operation')" fixed="right" width="120">
                      <template slot-scope="scope">
                        <el-button
                          type="text"
                          @click="handleEdit(scope.row)"
                          v-hasPermi="['administration:notice:edit']"
                          >{{ $t('public.edit') }}</el-button
                        >

                        <el-button
                          type="text"
                          @click="handleDelete(scope.row.id)"
                          v-hasPermi="['administration:notice:delete']"
                        >
                          {{ $t('public.delete') }}
                        </el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </div>
            </el-col>
          </el-row>
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
    <!-- 新建公告页面 -->

    <add-notice v-show="!isCheck" ref="addNotice" :form-data="noticeData" @isNotice="isNotice"></add-notice>
  </div>
</template>

<script>
import { noticeCategoryApi, noticeDeleteApi, noticeListApi, noticeStatusApi, noticeTopApi } from '@/api/administration'
export default {
  name: 'IndexVue',
  components: {
    left: () => import('./components/left'),
    addNotice: () => import('./components/addNotice'),
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  data() {
    return {
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
      where: {
        page: 1,
        limit: 15,
        cate_id: '',
        title: '',
        status: ''
      },
      tabIndex: 0,
      total: 0,
      tableData: [],
      isCheck: true,
      noticeData: {
        id: 0,
        isEdit: false,
        category: 0,
        optionData: []
      },
      defaultImage: require('@/assets/images/notice.png'),
      search: [
        {
          form_value: 'input',
          field_name_en: 'title',
          field_name: '标题'
        }
      ]
    }
  },
  mounted() {
    this.getTargetCate()
  },
  methods: {
    // 动态置顶
    async putNoticeTop(id) {
      await noticeTopApi(id)
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    // 获取表格数据
    getTableData(id) {
      noticeListApi(this.where).then((res) => {
        this.tableData = res.data.list || []
        this.total = res.data.count
      })
    },
    handleSearch() {
      this.where.page = 1
      this.getTableData()
    },
    handleNews() {
      if (this.$refs.left.department.length == 0) {
        this.$message.error('请先新建公告类型')
        return false
      }
      this.getTargetCate()
      this.noticeData.isEdit = false
      this.isCheck = false
      setTimeout(() => {
        this.$refs.addNotice.form.categoryId = this.where.cate_id
      }, 300)
    },
    isNotice(id) {
      this.isCheck = true
      this.where.page = 1
      this.where.cate_id = id
      this.getTableData(id)
      setTimeout(() => {
        this.$refs.left.tabIndex = this.tabIndex
      }, 300)
    },
    handleEdit(row) {
      this.noticeData.id = row.id
      this.noticeData.isEdit = true
      this.isCheck = false
      setTimeout(() => {
        this.$refs.addNotice.getNoticeCreate(row.id)
      }, 300)
    },
    async handleDelete(id) {
      await this.$modalSure('你确定要删除这条内容吗')
      await noticeDeleteApi(id)
      this.where.page = 1
      this.getTableData()
    },
    async handleStatus(row) {
      await noticeStatusApi(row.id, { status: row.status })
      this.getTableData()
    },
    eventOptionData(data, index) {
      this.where.cate_id = data.id
      this.noticeData.category = data.id
      this.tabIndex = JSON.parse(JSON.stringify(index))
      this.handleSearch()
    },
    async getTargetCate() {
      const result = await noticeCategoryApi()
      this.noticeData.optionData = result.data ? result.data : []
    },
    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          page: 1,
          cate_id: this.where.cate_id,
          title: '',
          limit: this.where.limit,
          status: ''
        }
      } else {
        this.where = { ...this.where, ...data }
      }
      this.handleSearch()
    }
  }
}
</script>

<style lang="scss" scoped>
.card-box {
  height: calc(100vh - 77px);
}
.assess-right {
  ::v-deep .el-card__header {
    border-bottom: none;
    padding: 0;
  }
}
// ::v-deep .el-switch__label--right {
//   margin-left: 6px;
// }

.right-con {
  display: flex;
  justify-content: flex-end;
}
.table-img {
  width: 39px;
  height: 26px;
  border-radius: 4px;
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
::v-deep.el-textarea__inner,
.el-input__inner {
  font-size: 13px !important;
}
::v-deep.el-input__inner {
  font-size: 13px !important;
}
::v-deep.is-top .el-switch__core {
  width: 69px !important;
}
</style>
