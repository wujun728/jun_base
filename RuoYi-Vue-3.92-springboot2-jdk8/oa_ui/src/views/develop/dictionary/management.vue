<template>
  <div class="divBox">
    <el-card class="card-box" v-loading="loadingBox">
      <div>
        <oaFromBox
          v-if="search.length > 0"
          :search="search"
          :dropdownList="dropdownList"
          :isViewSearch="false"
          :total="total"
          @addDataFn="addFinance"
          @dropdownFn="batchDelete"
          @confirmData="confirmData"
        >
          <div slot="title" class="station-header" @click="backFn">
            <i class="el-icon-arrow-left"></i>
            数据管理列表
          </div>
        </oaFromBox>

        <!-- 表格 -->
        <div class="table-box mt10">
          <el-table
            :data="tableData"
            @selection-change="handleSelectionChange"
            style="width: 100%"
            row-key="id"
            :key="keyVal"
            lazy
            :load="load"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          >
            <el-table-column type="selection" width="55" v-if="is_Show !== 1"> </el-table-column>
            <el-table-column prop="name" label="数据名称" show-overflow-tooltip> </el-table-column>
            <el-table-column prop="value" label="数据值" show-overflow-tooltip></el-table-column>
            <el-table-column prop="status" label="状态">
              <template slot-scope="scope">
                <el-switch
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
            <el-table-column prop="mark" label="备注" show-overflow-tooltip>
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
                <el-button type="text" v-if="scope.row.level < level" @click="childLevel(scope.row)"
                  >添加子级</el-button
                >
                <el-button type="text" @click="editFn(scope.row)" v-if="scope.row.is_default !== 1">编辑</el-button>
                <template>
                  <el-button type="text" @click="handleDelete(scope.row)" v-if="scope.row.is_default !== 1">{{
                    $t('public.delete')
                  }}</el-button>
                </template>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
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
import oaFromBox from '@/components/common/oaFromBox'
export default {
  name: 'CrmebOaEntManagement',
  components: { oaFromBox },
  data() {
    return {
      total: 0,
      search: [
        {
          field_name: '关键字',
          field_name_en: 'name',
          form_value: 'input'
        }
      ],
      dropdownList: [
        {
          label: '批量删除',
          value: 1
        }
      ],
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
      keyVal: 0,
      is_Show: false,
      ids: [],
      level: 0,
      is_default: 0,
      tableData: []
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
    restData() {
      this.where.name = ''
      this.where.status = ''
      this.getList(true)
    },
    // 批量删除
    async batchDelete() {
      if (this.ids.length === 0) {
        return this.$message.error('请先选择要删除的数据')
      }
      let id = this.ids.join(',')
      await this.$modalSure('你确定要批量删除这条内容吗')
      await getDictDataDeleteApi(id)
      await this.getList(true)
    },

    // 新增
    addFinance() {
      this.$modalForm(getDictDataCreateApi({ type_id: this.query.id })).then(({ message }) => {
        this.getList(true)
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
        this.getList(true)
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
        path: `${roterPre}/develop/dictionary`
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
    confirmData(data) {
      if (data == 'reset') {
        this.where.name = ''
        this.where.level = 1
        this.where.pid = ''
        this.where.types = this.where.types
      } else {
        this.where = { ...this.where, ...data }
      }
      this.getList()
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
      this.keyVal = this.keyVal + 1
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
  min-height: calc(100vh - 90px);
}
.station-header {
  .el-icon-arrow-left {
    cursor: pointer;
  }
}
.p20 {
  padding: 0 20px;
}
</style>
