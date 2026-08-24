<!-- 固定物资记录 -->
<template>
  <div ref="bodyRef">
    <el-row :gutter="20">
      <el-col v-bind="gridl">
        <tree :parent-tree="treeData" :frame-id="frame_id" :types="types" @frameId="getFrameId" @getList="getList" />
      </el-col>
      <el-col v-bind="gridr">
        <div class="v-height-flag">
          <oaFromBox
            v-if="search.length > 0"
            :search="search"
            :viewSearch="viewSearch"
            :total="total"
            :title="`物资记录`"
            :btnText="`导出`"
            :btnIcon="false"
            :isAddBtn="true"
            @addDataFn="handleExport"
            @confirmData="confirmData"
          ></oaFromBox>

          <el-table
            :data="tableData"
            :height="tableHeight"
            style="width: 100%"
            class="mt10"
            row-key="id"
            default-expand-all
          >
            <el-table-column prop="id" label="领取部门 / 人员" min-width="100" show-overflow-tooltip>
              <template slot-scope="scope">
                <span v-if="scope.row.frame">{{ scope.row.frame.name }}</span>
                <span v-if="scope.row.card">{{ scope.row.card.name }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="storage.number" label="物资编号" min-width="130" show-overflow-tooltip />
            <el-table-column prop="storage.name" label="物资名称" min-width="100" show-overflow-tooltip />
            <el-table-column prop="storage.units" label="规格型号" min-width="100" show-overflow-tooltip />
            <el-table-column prop="storage.cate.cate_name" label="物资分类" min-width="100" show-overflow-tooltip />
            <el-table-column prop="storage.specs" label="计量单位" min-width="80" show-overflow-tooltip />
            <el-table-column pstatus label="状态" min-width="80" show-overflow-tooltip>
              <template slot-scope="scope">
                <el-tag v-if="scope.row.status === 1" size="mini">已领用</el-tag>
                <el-tag v-if="scope.row.status === 2" type="success" size="mini">已归还</el-tag>
                <el-tag v-if="scope.row.status === 3" type="warning" size="mini">维修中</el-tag>
                <el-tag v-if="scope.row.status === 4" type="danger" size="mini">已报废</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="updated_at" label="操作时间" min-width="120" show-overflow-tooltip>
              <template slot-scope="scope">
                {{ $moment(scope.row.updated_at).format('yyyy-MM-DD') }}
              </template>
            </el-table-column>
          </el-table>
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

    <export-excel :template="false" :save-name="saveName" :export-data="exportData" ref="exportExcel" />
  </div>
</template>

<script>
import { storageCateApi, storageRecordApi, storageRecordUsersApi } from '@/api/administration'
import 'animate.css'
export default {
  name: 'Fixed',
  components: {
    tree: () => import('./tree'),
    exportExcel: () => import('@/components/common/exportExcel'),

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
      pickerOptions: this.$pickerOptionsTimeEle,
      search: [
        {
          field_name: '物资名称/物资编号',
          field_name_en: 'name',
          form_value: 'input',
          data_dict: this.options
        },
        {
          field_name: '部门',
          field_name_en: 'frame_id',
          form_value: 'frame_id',
          data_dict: []
        },
        {
          field_name: '时间筛选',
          field_name_en: 'time',
          form_value: 'date_picker'
        }
      ],
      viewSearch: [
        {
          field: 'card_id',
          title: '人员',
          type: 'card_id',
          options: []
        },
        {
          field: 'status',
          title: '物资状态',
          type: 'select',
          options: [
            { value: '', name: '全部' },
            { value: 1, name: '已领用' },
            { value: 2, name: '已归还' },
            { value: 3, name: '维修中' },
            { value: 4, name: '已报废' }
          ]
        }
      ],

      treeData: [],
      frame_id: 0,
      where: {
        page: 1,
        limit: 15,
        cid: '',
        types: 1,
        storage_type: 1
      },
      total: 0,
      tableData: [],
      options: [],
      userOptions: [],

      types: 1,
      saveName: '',
      exportData: {
        data: [],
        cols: [
          { wpx: 120 },
          { wpx: 140 },
          { wpx: 180 },
          { wpx: 120 },
          { wpx: 80 },
          { wpx: 80 },
          { wpx: 80 },
          { wpx: 80 }
        ]
      }
    }
  },
  created() {
    this.getOptionData()
  },
  mounted() {
    this.getList()
  },
  methods: {
    // 获取权限列表
    getList() {
      storageCateApi({ type: this.types }).then(async (res) => {
        this.treeData = res.data
        this.getTableData()
      })
    },

    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          page: 1,
          limit: 15,
          cid: '',
          types: 1,
          storage_type: 1
        }
        this.getTableData()
      } else {
        this.where = { ...this.where, ...data }
        this.where.page = 1
        this.getTableData()
      }
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
    async getTableData() {
      const result = await storageRecordApi(this.where)
      this.tableData = result.data.list || []
      this.total = result.data.count
    },
    async handleExport() {
      // this.getSearchFrame()
      // 先将对象变为字符串，然后再变为json对象，防止对象的指针指向问题，为深拷贝
      let where = JSON.parse(JSON.stringify(this.where))
      where.limit = 0
      this.saveName = '导出固定物资记录_' + this.$moment(new Date()).format('MM_DD_HH_mm_ss') + '.xlsx'
      const res = await storageRecordApi(where)
      let data = res.data.list
      if (data.length <= 0) {
        this.$message.error(this.$t('access.placeholder24'))
      } else {
        const aoaData = [
          ['领取部门/人员', '物资编号', '物资名称', '规格型号', '物资分类', '计量单位', '物资状态', '操作时间']
        ]

        data.forEach((value) => {
          if (value.frame) {
            value.frame = value.frame.name
          } else if (value.card) {
            value.frame = value.card.name
          } else {
            value.frame = ''
          }
          aoaData.push([
            value.frame,
            value.storage.number ? value.storage.number : '',
            value.storage.name,
            value.storage.units,
            value.storage.cate.cate_name,
            value.storage.specs,
            this.getMaterialStatus(value.status),
            this.$moment(value.updated_at).format('yyyy-MM-DD')
          ])
        })
        this.exportData.data = aoaData
        this.$refs.exportExcel.exportExcel()
      }
    },
    handleSearch() {
      this.where.page = 1
      this.getTableData()
    },

    getMaterialStatus(value) {
      let str = ''
      if (value === 1) {
        str = '已领用'
      } else if (value === 2) {
        str = '已归还'
      } else if (value === 3) {
        str = '维修中'
      } else if (value === 4) {
        str = '已报废'
      }
      return str
    },
    async getOptionData() {
      const res = await storageRecordUsersApi({ types: '', storage_type: this.types })
      this.userOptions = res.data || []
      this.userOptions.unshift({ id: '', name: '全部', types: -1 })
    },

    getFrameId(data) {
      this.where.cid = data ? data : ''
      this.handleSearch()
    }
  }
}
</script>

<style lang="scss" scoped>
.form-top-line._show {
  width: 100%;
}
.list-header {
  padding: 0;
}

.box {
  display: flex;
  flex-wrap: wrap;
}

.table-box {
  ::v-deep .el-table th {
    background-color: rgba(247, 251, 255, 1);
  }
}
</style>
