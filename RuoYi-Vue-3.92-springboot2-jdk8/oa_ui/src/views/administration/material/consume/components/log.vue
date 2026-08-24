<template>
  <!-- 消耗物资记录 -->
  <div class="v-height-flag">
    <el-row :gutter="20">
      <el-col v-bind="gridl">
        <tree :parent-tree="treeData" :frame-id="frame_id" :types="types" @frameId="getFrameId" />
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

          <div class="table-box mt10 v-height-flag">
            <el-table :data="tableData" :height="tableHeight" style="width: 100%" row-key="id" default-expand-all>
              <el-table-column prop="id" label="领取部门 / 人员" min-width="100">
                <template slot-scope="scope">
                  <span v-if="scope.row.frame"> {{ scope.row.frame ? scope.row.frame.name : '--' }}</span>
                  <span v-else>{{ scope.row.card ? scope.row.card.name : '--' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="storage.name" label="物资名称" min-width="100" />
              <el-table-column prop="storage.units" label="规格型号" min-width="100">
                <template slot-scope="scope">
                  {{ scope.row.storage.units ? scope.row.storage.units : '--' }}
                </template>
              </el-table-column>
              <el-table-column prop="storage.cate.cate_name" label="物资分类" min-width="100" />
              <el-table-column prop="storage.specs" label="计量单位" min-width="80" />
              <el-table-column prop="num" label="领用数量" min-width="80" />
              <el-table-column prop="updated_at" label="领用时间" min-width="120">
                <template slot-scope="scope">
                  {{ $moment(scope.row.updated_at).format('yyyy-MM-DD') }}
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
        layout="total, sizes,prev, pager, next, jumper"
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
import { values } from 'xe-utils'
export default {
  name: 'Consume',
  components: {
    tree: () => import('./tree'),
    exportExcel: () => import('@/components/common/exportExcel'),
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  data() {
    return {
      pickerOptions: this.$pickerOptionsTimeEle,
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
      treeData: [],
      frame_id: 0,
      search: [
        {
          field_name: '物资名称',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '时间筛选',
          field_name_en: 'time',
          form_value: 'date_picker'
        },
        {
          field_name: '部门',
          field_name_en: 'frame_id',
          form_value: 'frame_id',
          data_dict: []
        }
      ],
      viewSearch: [
        {
          field: 'card_id',
          title: '人员',
          type: 'card_id',
          options: []
        }
      ],
      where: {
        page: 1,
        limit: 15,
        cid: '',
        types: 1,
        storage_type: 0
      },

      index: 0,
      timeVal: '',
      total: 0,
      tableData: [],
      options: [],
      fromData: {},
      recordData: {},
      types: 0,
      saveName: '',
      exportData: {
        data: [],
        cols: [{ wpx: 120 }, { wpx: 180 }, { wpx: 120 }, { wpx: 80 }, { wpx: 80 }, { wpx: 80 }, { wpx: 80 }]
      }
    }
  },
  beforeCreate() {},
  mounted() {
    this.getList()
    this.getOptionData()
  },
  methods: {
    // 获取权限列表
    getList() {
      storageCateApi({ type: this.types }).then(async (res) => {
        this.treeData = res.data
        this.getTableData()
      })
    },

    getSelectList(data) {
      this.where.card_id = data[0].value
      this.userList = data
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
    getOptionData() {
      storageRecordUsersApi({ types: '', storage_type: this.types }).then((res) => {
        this.options = res.data || []
        this.options.unshift({ id: '', name: '全部', types: -1 })
      })
    },
    handleUsers() {
      this.handleSearch()
    },
    // 获取表格数据
    getTableData() {
      storageRecordApi(this.where).then((res) => {
        this.tableData = res.data.list || []
        this.total = res.data.count
      })
    },
    handleExport() {
      // 先将对象变为字符串，然后再变为json对象，防止对象的指针指向问题，为深拷贝
      let where = JSON.parse(JSON.stringify(this.where))
      where.limit = 0
      this.saveName = '导出消耗物资记录_' + this.$moment(new Date()).format('MM_DD_HH_mm_ss') + '.xlsx'
      storageRecordApi(where).then((res) => {
        let data = res.data.list
        if (data.length <= 0) {
          this.$message.error(this.$t('access.placeholder24'))
        } else {
          const aoaData = [['领取部门/人员', '物资名称', '规格型号', '物资分类', '计量单位', '领用数量', '领用时间']]

          data.forEach((value) => {
            let name = ''
            if (value.frame && value.frame.name) {
              name = value.frame.name
            } else if (value.card && value.card.name) {
              name = value.card.name
            } else {
              name = '--'
            }
            aoaData.push([
              name,
              value.storage.name,
              value.storage.units,
              value.storage.cate.cate_name,
              value.storage.specs,
              value.num,
              this.$moment(value.updated_at).format('yyyy-MM-DD')
            ])
          })
          this.exportData.data = aoaData
          this.$refs.exportExcel.exportExcel()
        }
      })
    },

    handleSearch() {
      this.where.page = 1
      this.getTableData()
    },

    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          page: 1,
          limit: 15,
          cid: '',
          types: 1,
          storage_type: 0
        }
        this.getTableData()
      } else {
        this.where = { ...this.where, ...data }
        this.where.page = 1
        this.getTableData()
      }
    },

    getFrameId(data) {
      this.where.cid = data ? data : ''
      this.handleSearch()
    }
  }
}
</script>

<style lang="scss" scoped>
.list-header {
  padding: 0;
  border-bottom: none;
}
::v-deep .el-form-item {
  margin-bottom: 10px;
  margin-right: 10px;
}
.table-box {
  ::v-deep .el-table th {
    background-color: rgba(247, 251, 255, 1);
  }
}
</style>
