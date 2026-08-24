<template>
  <div class="divBox">
    <div class="v-height-flag">
      <el-card class="mb14 normal-page" v-loading="loadingBox" :body-style="{ padding: '20px 20px 20px 20px' }">
        <oaFromBox
          :title="$route.meta.title"
          :search="search"
          :dropdownList="dropdownList"
          :total="total"
          :isViewSearch="false"
          :sortSearch="false"
          @dropdownFn="dropdownFn"
          @confirmData="confirmData"
        ></oaFromBox>
        <!-- <el-form :inline="true" class="from-s">
          <div class="felx-row flex-col">
            <div>
              <el-form-item>
                <el-button type="primary" size="small" @click="addFinance">新增</el-button>
                <el-button size="small" @click="batchDelete">批量删除</el-button>
              </el-form-item>
            </div>
            <div class="flex">
              <el-form-item label="" class="select-bar">
                <el-input
                  v-model="where.name"
                  size="small"
                  @keyup.native.stop.prevent.enter="getUserWorkList"
                  clearable
                  style="width: 250px"
                  @change="getList(true)"
                  placeholder="名称，标识关键字搜索"
                />
              </el-form-item>
              <el-form-item label="状态" class="select-bar">
                <el-select v-model="where.status" placeholder="请选择状态" size="small" @change="getList(true)">
                  <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-tooltip effect="dark" content="重置搜索条件" placement="top">
                  <div class="reset" @click="restData"><i class="iconfont iconqingchu"></i></div>
                </el-tooltip>
              </el-form-item>
            </div>
          </div>
        </el-form> -->
        <!-- <div class="splitLine mb20"></div> -->
        <!-- 表格数据 -->
        <div class="table-box mt10">
          <!-- <div class="inTotal">共有 {{ total }} 条</div> -->
          <el-table
            :data="tableData"
            :height="tableHeight"
            @selection-change="handleSelectionChange"
            style="width: 100%"
            row-key="id"
          >
            <el-table-column type="selection" width="55"> </el-table-column>
            <el-table-column prop="id" label="ID" type=""></el-table-column>
            <el-table-column prop="name" label="字典名称" show-overflow-tooltip> </el-table-column>
            <el-table-column prop="ident" label="字典标识" show-overflow-tooltip> </el-table-column>
            <el-table-column prop="type" label="状态" show-overflow-tooltip>
              <template slot-scope="scope">
                <el-switch
                  :disabled="scope.row.is_default === 1"
                  v-model="scope.row.status"
                  active-text="启用"
                  inactive-text="停用"
                  :active-value="1"
                  :inactive-value="0"
                  @change="handleStatus(scope.row)"
                />
              </template>
            </el-table-column>
            <el-table-column prop="mark" label="备注" min-width="150" show-overflow-tooltip>
              <template slot-scope="scope">
                <span>{{ scope.row.mark || '--' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="created_at" label="创建时间" show-overflow-tooltip>
              <template slot-scope="scope">
                <span>{{ scope.row.created_at || '--' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="address" :label="$t('public.operation')" width="250">
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
              layout="total, sizes,prev, pager, next, jumper"
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
import { getDictListApi, getDictCreateApi, getDictEditApi, getDictPutShowApi, getDictDeleteShowApi } from '@/api/form'
export default {
  name: 'FinanceList',
  components: {
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  data() {
    return {
      tableData: [],
      loadingBox: false,
      ids: [],
      where: {
        name: '',
        status: '',
        limit: 15,
        page: 1
      },
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
      ],
      search: [
        {
          field_name: '名称，标识关键字',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '状态',
          field_name_en: 'status',
          form_value: 'select',
          data_dict: [
            {
              name: '启用',
              value: '1'
            },
            {
              name: '停用',
              value: '0'
            }
          ]
        }
      ],
      dropdownList: [
        {
          value: 1,
          label: '批量删除'
        }
      ]
    }
  },
  created() {
    this.getList()
  },
  mounted() {
    const limit = Math.floor((window.innerHeight - 300) / 56)
    this.where.limit = limit > this.where.limit ? limit : this.where.limit
    this.getList()
  },
  methods: {
    // 批量操作表格
    handleSelectionChange(val) {
      this.ids = []
      val.map((item) => {
        this.ids.push(item.id)
      })
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
        path: `${roterPre}/customer/setup/dictionary/management`,
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
      this.where.name = ''
      this.where.status = ''
      this.getList()
    },
    confirmData(where) {
      if (where == 'reset') {
        this.where = {
          name: '',
          status: '',
          limit: 15,
          page: 1
        }
      } else {
        this.where = { ...this.where, ...where }
      }
      this.getList(true)
    },
    dropdownFn(item) {
      switch (item.value) {
        case 1:
          this.batchDelete()
          break
      }
    }
  }
}
</script>

<style lang="scss" scoped></style>
