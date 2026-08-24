<template>
  <div class="box-container">
    <el-drawer
      :visible.sync="drawer"
      direction="rtl"
      :before-close="handleClose"
      size="760px"
      :modal="true"
      :wrapper-closable="true"
    >
      <div slot="title">
        <div class="tab-box">
          <div
            v-for="(item, index) in tabList"
            :key="index"
            class="tab-item"
            :class="{ on: index == tabCur }"
            @click="handleTab(item, index)"
          >
            {{ item.title }}
          </div>
        </div>
      </div>
      <el-form :inline="true" label-width="80px" @submit.native.prevent class="flex">
        <el-form-item class="select-bar el-input--small">
          <div style="width: 200px">
            <select-department
              :only-one="true"
              :isSearch="true"
              :value="departmentList || []"
              @changeMastart="changeMastart"
            ></select-department>
          </div>
        </el-form-item>

        <el-form-item class="select-bar">
          <el-input v-model="where.name" @change="handleOk" placeholder="请输入被考核人姓名" size="small"></el-input>
        </el-form-item>

        <el-tooltip effect="dark" content="重置搜索条件" placement="top">
          <div class="reset" @click="reset"><i class="iconfont iconqingchu"></i></div>
        </el-tooltip>
      </el-form>
      <div class="table-box">
        <el-table ref="table" :data="tableData">
          <el-table-column prop="name" label="成员姓名" min-width="140"></el-table-column>
          <el-table-column prop="frame.name" label="部门" min-width="140" />
          <el-table-column prop="job.name" label="职位" min-width="140">
            <template slot-scope="scope">
              <span>{{ scope.row.job ? scope.row.job.name : '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="verify" label="直属上级领导" min-width="140">
            <template slot-scope="scope">
              <span>{{ scope.row.super && scope.row.super ? scope.row.super.name : '-' }}</span>
            </template>
          </el-table-column>
        </el-table>
        <div style="padding-bottom: 20px">
          <el-pagination
            :page-size="where.limit"
            :current-page="where.page"
            layout="total, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="pageChange"
          />
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { assessPlanUserListApi } from '@/api/enterprise'

export default {
  name: 'assessBox',
  components: {
    selectDepartment: () => import('@/components/form-common/select-department')
  },
  data() {
    return {
      drawer: false,
      tabCur: 0,
      tabList: [
        { title: '未设置人', id: 0 },
        { title: '已设置人', id: 1 }
      ],
      where: {
        page: 1,
        limit: 20,
        name: '',
        uni: 0,
        frame_id: ''
      },
      id: 0,
      total: 0,
      tableData: [],
      departmentList: []
    }
  },
  created() {},
  methods: {
    openBox() {
      this.drawer = true
      this.getTableData()
    },
    handleClose() {
      this.drawer = false
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },

    changeMastart(data) {
      if (data.length > 0) {
        this.departmentList = data
        this.where.frame_id = this.departmentList[0].id
      } else {
        this.departmentList = []
        this.where.frame_id = ''
      }
      this.handleOk()
    },
    // 获取列表
    async getTableData() {
      this.where.id = this.id
      const result = await assessPlanUserListApi(this.where)
      this.tableData = result.data.list
      this.total = result.data.count
    },
    handleTab(item, index) {
      this.tabCur = index
      this.where.uni = item.id
      this.handleOk()
    },
    handleOk() {
      this.where.page = 1
      this.getTableData()
    },
    cardTag(index) {
      this.departmentList.splice(index, 1)
      this.activeDepartment = {}
      this.where.frame_id = ''
      this.handleOk()
    },
    reset() {
      this.where.page = 1
      this.where.name = ''
      this.departmentList = []
      this.activeDepartment = {}
      this.where.frame_id = ''
      this.getTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep.el-drawer__header {
  border-bottom: 1px solid #d8d8d8;
  padding-bottom: 14px;
  margin-bottom: 0;
}
.reset {
  margin-top: 2px;
  // margin-left: 14px;
}
::v-deep .el-drawer__body {
  padding: 20px;
  overflow-y: auto;
}
.table-box {
  height: calc(100% - 58px - 52px);
}
.tab-box {
  display: flex;
  align-items: center;
  .tab-item {
    margin-right: 30px;
    font-size: 14px;
    color: #000000;
    cursor: pointer;
    &.on {
      color: #1890ff;
    }
  }
}
.label1 {
  ::v-deep .el-form-item__label {
    width: 55px !important;
  }
}
.select-bar {
  display: flex;
}
::v-deep.el-form-item__content {
  flex: 1;
}
.plan-footer-one {
  background-color: #fff;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  color: #606266;
  display: inline-block;
  font-size: inherit;
  min-height: 32px;
  line-height: 32px;
  outline: none;
  padding: 0 15px;
  transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
  width: 100%;
}
</style>
