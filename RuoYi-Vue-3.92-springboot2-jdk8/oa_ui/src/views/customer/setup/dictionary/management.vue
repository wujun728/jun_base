<template>
  <div class="divBox">
    <el-card class="station-header" :body-style="{ padding: '14px' }">
      <el-row>
        <el-col :span="24">
          <el-page-header>
            <div slot="title" @click="backFn">
              <i class="el-icon-arrow-left"></i>
              {{ query.name }}
            </div>
          </el-page-header>
        </el-col>
      </el-row>
    </el-card>
    <el-card class="mt14 card-box" v-loading="loadingBox">
      <div class="title-box">
        <div class="title-text">数据管理页面</div>
        <el-form :inline="true" class="from-s">
          <div class="felx-row flex-col">
            <div>
              <el-form-item v-if="is_Show !== 1">
                <el-button type="primary" size="small" @click="addFinance">新增数据</el-button>
                <el-button size="small" @click="batchDelete">批量删除</el-button>
              </el-form-item>
            </div>
          </div>
          <!-- <div class="splitLine mb20" v-if="is_Show !== 1"></div> -->
        </el-form>
      </div>
      <!-- 表格 -->
      <div class="inTotal">共 {{ total }} 条</div>
      <el-table
        :data="tableData"
        @selection-change="handleSelectionChange"
        style="width: 100%"
        row-key="id"
        lazy
        :load="load"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column type="selection" width="55" v-if="is_Show !== 1"> </el-table-column>
        <el-table-column prop="name" label="数据名称/ID"> </el-table-column>
        <el-table-column prop="value" label="数据值"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-text="启用"
              inactive-text="停用"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatus(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="mark" label="备注">
          <template slot-scope="scope">
            <span>{{ scope.row.mark || '--' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序"> </el-table-column>
        <el-table-column prop="created_at" label="创建时间">
          <template slot-scope="scope">
            <span>{{ scope.row.created_at || '--' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="address" :label="$t('public.operation')" width="250">
          <template slot-scope="scope">
            <el-button type="text" v-if="scope.row.level < level" @click="childLevel(scope.row)">添加子级</el-button>
            <el-button type="text" @click="editFn(scope.row)" v-if="scope.row.is_default !== 1">编辑</el-button>
            <template>
              <el-button type="text" @click="handleDelete(scope.row)" v-if="scope.row.is_default !== 1">{{
                $t('public.delete')
              }}</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { roterPre } from '@/settings'
import {
  getDictDataListApi,
  getDictDataCreateApi,
  getDictDataEditeApi,
  getDictDataPutApi,
  getDictDataDeleteApi,
  getDictDatainfoApi
} from '@/api/form'
export default {
  name: 'CrmebOaEntManagement',
  data() {
    return {
      total: 0,
      where: {
        types: '',
        level: 1,
        pid: ''
      },
      loadingBox: false,
      query: {
        id: 0,
        name: ''
      },
      ids: [],
      level: 0,
      is_default: 0,
      tableData: [],
      is_Show:false
    }
  },

  mounted() {
    this.query.id = this.$route.query.id
    this.getInfo(this.query.id)
  },

  methods: {
    getInfo(id) {
      getDictDatainfoApi(id).then((res) => {
        this.query.name = res.data.name
        this.level = res.data.level
        this.is_Show = res.data.is_default
        this.where.types = res.data.ident
        this.getList(true)
      })
    },

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
      await this.$modalSure('你确定要删除这条内容吗')
      await getDictDataDeleteApi(id)
      await this.getList(true)
    },

    // 新增
    addFinance() {
      this.$modalForm(getDictDataCreateApi({ type_id: this.query.id })).then(({ message }) => {
        this.getList()
      })
    },

    childLevel(row) {
      let data = {
        type_id: this.query.id,
        pid: row.value
      }
      this.$modalForm(getDictDataCreateApi(data)).then(({ message }) => {
        this.getList(true)
      })
    },

    // 编辑
    editFn(row) {
      this.$modalForm(getDictDataEditeApi(row.id)).then(({ message }) => {
        this.getList()
      })
    },

    // 删除
    async handleDelete(row) {
      await this.$modalSure('你确定要删除这条内容吗')
      await getDictDataDeleteApi(row.id)
      await this.getList(true)
    },

    // 返回页面
    backFn() {
      this.$router.push({
        path: `${roterPre}/customer/setup/dictionary/index`
      })
    },

    // 修改状态
    async handleStatus(row) {
      await getDictDataPutApi(row.id, { status: row.status })
      await this.getList(true)
    },

    // 分页
    handleSizeChange(val) {
      this.where.limit = val
      this.getList(true)
    },

    pageChange(page) {
      this.where.page = page
      this.getList(true)
    },

    // 获取列表
    async getList(val) {
      this.loadingBox = true
      this.tableData = []
      if (val) {
        this.where.level = 1
        this.where.pid = ''
      }
      const result = await getDictDataListApi(this.where)
      this.tableData = result.data.list
      this.total = result.data.count
      this.loadingBox = false
    },

    async load(tree, treeNode, resolve) {
      const data = this.where
      data.level = tree.level + 1
      data.pid = tree.value
      const result = await getDictDataListApi(data)
      setTimeout(() => {
        resolve(result.data.list)
      }, 100)
    }
  }
}
</script>

<style lang="scss" scoped>
.card-box {
  min-height: calc(100vh - 215px);
}
::v-deep .el-icon-back {
  display: none;
}
.title-box {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .title-text {
    font-weight: 500;
    font-size: 18px;
    color: #303133;
  }
  .el-form--inline .el-form-item {
    margin: 0;
  }
}
::v-deep.el-page-header__left::after {
  display: none;
}
::v-deep.el-page-header__title {
  font-weight: 500;
  font-size: 17px;
  color: #303133;
}
</style>
