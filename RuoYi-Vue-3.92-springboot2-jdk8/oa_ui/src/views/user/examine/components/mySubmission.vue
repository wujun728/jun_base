<template>
  <div>
    <oaFromBox :isAddBtn="false" title="我的申请" :search="search" :total="total" :isViewSearch="false"
      @confirmData="confirmData">
      <template slot="rightBtn">
        <el-dropdown trigger="click" size="small" placement="bottom-start" @command="handleBuild">
          <el-button type="primary" size="small">发起申请</el-button>
          <el-dropdown-menu slot="dropdown" class="build-dropdown">
            <el-dropdown-item v-for="(item, index) in dropdownList" :key="item.id" class="over-text" placement="top-end"
              :command="item.id">
              <i class="iconfont" :class="item.icon" :style="{ color: item.color }"></i>
              {{ item.name }}
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </template>
    </oaFromBox>
    <div class="table-box mt10">
      <el-table :data="tableData" :height="tableHeight" style="width: 100%" v-loading="loading" row-key="id"
        default-expand-all>
        <el-table-column prop="name" label="审批类型" min-width="250">
          <template slot-scope="scope">
            <el-row class="table-title">
              <el-col class="table-title-left">
                <div class="selIcon" :style="{ backgroundColor: scope.row.approve.color }">
                  <i class="icon iconfont" :class="scope.row.approve.icon"></i>
                </div>
              </el-col>
              <el-col class="table-title-right">
                <p class="title">{{ scope.row.approve.name }}</p>
                <p class="over-text">{{ getValue(scope.row.content) }}</p>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="审批状态" min-width="80">
          <template slot-scope="scope">
            <span class="status">
              <el-tag v-if="scope.row.status === -1" type="info" effect="plain" size="mini"> 已撤销 </el-tag>
              <el-tag v-if="scope.row.status === 1 && scope.row.recall" type="info" effect="plain" size="mini">
                撤销中
              </el-tag>
              <el-tag v-if="scope.row.status === 0" type="warning" effect="plain" size="mini"> 审核中 </el-tag>
              <el-tag v-if="scope.row.status === 1 && !scope.row.recall" type="info" effect="plain" size="mini">
                已通过
              </el-tag>
              <el-tag v-if="scope.row.status === 2" type="danger" effect="plain" size="mini"> 已拒绝 </el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="created_at" label="提交时间" min-width="150" />
        <el-table-column prop="name" :label="$t('public.operation')" width="200">
          <template slot-scope="scope">
            <el-button type="text" @click="handleDetail(scope.row)">详情 </el-button>
            <el-button v-if="scope.row.status === 1 && scope.row.type < 6" type="text"
              @click="handleEdit(scope.row)">再次提交</el-button>

            <el-button v-if="
              ((scope.row.status === 1 && scope.row.rules && scope.row.rules.recall && scope.row.rules.recall == 1) ||
                scope.row.status === 0) &&
              !scope.row.recall
            " type="text" @click="handleRefuse(scope.row)">
              撤销
            </el-button>

            <el-button v-if="
              (([2, -1].includes(scope.row.status) && scope.row.approve.types < 6) || scope.row.approve.types > 11) &&
              scope.row.crud_id == 0
            " type="text" @click="handleEdit(scope.row)">
              重新提交
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="page-fixed">
        <el-pagination :page-size="where.limit" :current-page="where.page" :page-sizes="[15, 20, 30]"
          layout="total,sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
          @current-change="pageChange" />
      </div>
    </div>

    <edit-examine ref="editExamine" @isOk="getTableData()" :type="type" />
    <detail-examine ref="detailExamine" @getList="getTableData" />
    <!-- 撤销 -->
    <oa-dialog ref="oaDialog" :fromData="fromData" :formConfig="formConfig" :formRules="formRules"
      :formDataInit="formDataInit" @submit="getApplyRevoke"></oa-dialog>
  </div>
</template>

<script>
import { approveApplyApi, approveApplyRevokeApi, approveConfigSearchApi } from '@/api/business'
import func from '@/utils/preload'
export default {
  name: 'Submission',
  components: {
    detailExamine: () => import('@/views/user/examine/components/detailExamine'),
    editExamine: () => import('@/views/user/examine/components/editExamine'),
    oaDialog: () => import('@/components/form-common/dialog-form'),
    defaultPage: () => import('@/components/common/defaultPage'),
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  data() {
    return {
      tableData: [],
      formDataInit: {
        info: ''
      },
      formConfig: [
        {
          type: 'textarea',
          label: '撤销理由：',
          placeholder: '请输入撤销理由',
          key: 'info'
        }
      ],
      formRules: {
        info: [{ required: true, message: '请输入撤销理由', trigger: 'blur' }]
      },
      fromData: {
        width: '600px',
        title: '撤销',
        btnText: '确定',
        labelWidth: 'auto',
        type: ''
      },
      rowData: {},
      loading: false,
      where: {
        page: 1,
        limit: 15,
        types: 0,
        time: '',
        status: '',
        approve_id: ''
      },
      total: 0,
      search: [
        {
          field_name: '审批状态',
          field_name_en: 'status',
          form_value: 'select',
          data_dict: [
            { name: '全部', id: '' },
            { name: '审核中', id: 0 },
            { name: '已通过', id: 1 },
            { name: '已拒绝', id: 2 },
            { name: '已撤销', id: -1 }
          ]
        },
        {
          field_name: '审批类型',
          field_name_en: 'approve_id',
          form_value: 'select',
          data_dict: []
        },
        {
          field_name: '开始时间',
          field_name_end: '结束时间',
          field_name_en: 'time',
          form_value: 'date_picker'
        }
      ],
      buildData: [],
      type: 0,
      dropdownList: []
    }
  },
  created() {
    // 挂载全局工具函数
    this.$vue.prototype.$func = func;
    // 并行请求数据以提高性能
    Promise.all([
      this.getTableData(),
      this.getConfigSearch(0),
      this.getConfigSearch(3)
    ]).catch(error => {
      console.error('数据请求出错:', error);
    });
  },
  methods: {
    handleBuild(command) {
      // 检查 editExamine 引用是否存在，避免潜在的错误
      if (this.$refs.editExamine) {
        this.$refs.editExamine.isEdit = false;
        this.$refs.editExamine.openBox(command);
      }
    },

    async getConfigSearch(id) {
      const result = await approveConfigSearchApi(id)
      const data = result.data ? result.data : []
      if (id === 0) {
        // name转label id转value
        data.map((item) => {
          item.label = item.name
          item.value = item.id
        })
        this.dropdownList = data
      }
      // 1、下级审批；3、我提交过的所有类型；
      if (id === 3 || id === 1) {
        this.search[1].data_dict = data
        this.search[1].data_dict.unshift({ name: '全部', id: '' })
      }
    },
    
    confirmData(data) {
      // 当数据为 'reset' 时，重置查询条件
      if (data === 'reset') {
        this.where = {
          page: 1,
          limit: 15,
          types: 0,
          time: '',
          status: '',
          approve_id: ''
        };
      } else {
        // 合并查询条件
        this.where = { ...this.where, ...data };
      }
      this.getTableData();
    },

    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },


    async getTableData() {
      this.loading = true
      const data = this.where
      this.where.verify_status = ''
      const result = await approveApplyApi(data)
      this.tableData = result.data.list
      this.total = result.data.count
      this.loading = false
    },
    // 详情
    handleDetail(row) {
      this.type = 1
      this.$refs.detailExamine.openBox(row)
    },
    getApproveIcon(icon) {
      let str = ''
      if (icon.indexOf('iconjine') > -1 || icon.indexOf('iconwenjian') > -1) {
        str = icon + '2'
      } else if (icon.indexOf('icontupian2') > -1) {
        str = 'icontupian3'
      } else if (icon.indexOf('icona-xingzhuang2') > -1) {
        str = 'icona-xingzhuang21'
      } else if (icon === 'iconwendang2') {
        str = 'icona-xingzhuang21'
      } else if (icon === 'iconwendang1') {
        str = 'icona-xingzhuang12'
      } else if (icon === 'iconrili1') {
        str = 'iconrili2'
      } else {
        str = icon
      }

      return str
    },
    // 重新提交
    handleEdit(row) {
      this.$refs.editExamine.isEdit = true
      this.$refs.editExamine.openBox(row)
    },
    handleRefuse(row) {
      this.rowData = row
      if (row.status === 0) {
        this.$modalSure(this.$t('你确定要撤销申请吗')).then(() => {
          this.getApplyRevoke('', row.id)
          this.getTableData()
        })
      } else {
        this.$refs.oaDialog.openBox()
      }
    },
    async getApplyRevoke(data) {
      await approveApplyRevokeApi(this.rowData.id, data)
      if (data) {
        this.$refs.oaDialog.handleClose()
      }
      this.getTableData()
    },
    getValue(row) {
      let arr = []
      row.map((item) => {
        if (item.value && typeof item.value == 'string' && item.type !== 'rich_text') {
          let str = item.label + ' : ' + item.value + ' '
          arr.push(str)
        } else {
        }
      })
      arr = arr.splice(0, 3).join('')

      return arr
    }
  }
}
</script>

<style lang="scss" scoped>
.build-dropdown {
  max-height: fit-content;
  overflow: auto;
  overflow-x: hidden;

  .iconfont {
    font-size: 14px !important;
  }
}

.status {
  ::v-deep .el-tag {
    background: #fff;
  }
}

.build-dropdown::-webkit-scrollbar {
  /*width: 0;宽度为0隐藏*/
  width: 8px;
  height: 4px;
}

.build-dropdown::-webkit-scrollbar-thumb {
  border-radius: 5px;
  height: 8px;
  background: rgba(0, 0, 0, 0.2); //滚动条颜色
}

.build-dropdown::-webkit-scrollbar-track {
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 5px;
  background: #eee; //滚动条背景色
}

.table-box {

  .table-title {
    display: flex;
    align-items: center;

    .table-title-left {
      width: 56px;

      i {
        color: #fff;
        font-size: 46px;
      }
    }

    .table-title-right {
      width: calc(100% - 56px);

      p {
        margin: 0;
        font-size: 13px;
      }

      .title {
        font-weight: bold;
        font-size: 13px;
      }

      .over-text {
        margin-top: 8px;
      }
    }
  }

  ::v-deep .el-table .cell {
    line-height: 1;
  }
}

.selIcon {
  width: 25px;
  height: 25px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 3px;
}

.iconfont {
  font-size: 13px !important;
  color: #fff;
}
</style>
