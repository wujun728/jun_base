<!-- 项目-我的项目-项目列表页面 -->
<template>
  <div class="divBox bill-type">
    <el-card class="normal-page">
      <oaFromBox
        :isViewSearch="false"
        :search="search"
        :title="$route.meta.title"
        :total="total"
        :treeData="treeData"
        :treeDefault="treeDefault"
        btnText="新建项目"
        @addDataFn="addProgram"
        @confirmData="confirmData"
        @treeChange="treeChange"
      ></oaFromBox>

      <div v-loading="loading" class="mt10">
        <oa-table
          :height="tableHeight"
          :loading="false"
          :tableData="tableData"
          :tableOptions="tableOptions"
          :total="total"
          @handleSizeChange="handleSizeChange"
          @handleCurrentChange="pageChange"
        >
          <template #name="{ row }">
            <div class="flex">
              <i class="iconfont iconxiangmuguanli"></i>
              <div v-if="row.name.length < 18" class="point line1" @click="goTask(row)">
                {{ row.name || '- -' }}
              </div>
              <el-popover v-else placement="top" trigger="hover" width="250">
                <div>{{ row.name || '- -' }}</div>
                <div slot="reference" class="line1 point" style="max-width: 200px" @click="goTask(row)">
                  {{ row.name || '- -' }}
                </div>
              </el-popover>
            </div>
          </template>

          <template #status="{ row }">
            <el-tag v-if="row.status == 1" effect="plain" type="warning">已暂停</el-tag>
            <el-tag v-else-if="row.status == 2" effect="plain" type="info">已关闭</el-tag>
            <el-tag v-else-if="row.end_date && nowTime() > row.end_date" effect="plain" type="danger">已延期</el-tag>
            <el-tag v-else-if="row.start_date && nowTime() < row.start_date" effect="plain" type="success"
              >待开始</el-tag
            >
            <el-tag v-else effect="plain">进行中</el-tag>
          </template>

          <template #admins="{ row }">
            <img :src="row.admins[0].avatar" alt="" class="img" />
            <span>{{ row.admins[0].name }}</span>
          </template>
        </oa-table>

      </div>
    </el-card>

    <!-- 新建项目 -->
    <el-drawer
      :before-close="handleClose"
      :visible.sync="taskDrawer"
      :wrapper-closable="false"
      size="1120px"
      title="新建项目"
    >
      <add-program
        v-if="taskDrawer"
        ref="addProgram"
        :customer="customerList"
        :type="`edit`"
        @getTableData="getTableData"
        @handleClose="handleClose"
      />
    </el-drawer>
  </div>
</template>

<script>
import { getProgramListApi, deleteProgramApi } from '@/api/program'
import { customerSelectApi, selectContractListApi } from '@/api/enterprise'
import { roterPre } from '@/settings'

export default {
  name: 'programList',
  components: {
    oaFromBox: () => import('@/components/common/oaFromBox'),
    oaTable: () => import('@/components/form-common/oa-table'),
    addProgram: () => import('./components/addProgram'),
    taskDrawer: () => import('../programTask/index'),
    dynamicsDrawer: () => import('./dynamics')
  },
  data() {
    return {
      loading: false,
      tableData: [],
      tableFrom: {
        page: 1,
        limit: 15,
        types: 0,
        status: '',
        admins: [],
        scope_frame: 'all',
        scope_normal: 0,
        eid: [],
        cid: []
      },
      total: 0,
      customerList: [],
      contractList: [],
      taskDrawer: false,
      taskTitle: '',
      programId: 0,
      treeData: [
        {
          options: [
            {
              value: 0,
              label: '全部项目'
            },
            {
              value: 1,
              label: '我负责的'
            },
            {
              value: 2,
              label: '我参与的'
            },
            {
              value: 3,
              label: '我创建的'
            }
          ]
        }
      ],
      search: [
        {
          field_name: '项目名称',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '状态',
          field_name_en: 'status',
          form_value: 'select',
          multiple: true,
          props: {
            collapseTags: true
          },
          data_dict: [
            {
              value: 5,
              name: '已延期'
            },
            {
              value: 4,
              name: '进行中'
            },
            {
              value: 3,
              name: '待开始'
            },
            {
              value: 1,
              name: '已暂停'
            },
            {
              value: 2,
              name: '已关闭'
            }
          ]
        },
        {
          field_name: '关联客户',
          field_name_en: 'eid',
          form_value: 'checkbox',
          multiple: true,
          props: {
            collapseTags: true
          },
          data_dict: []
        },
        {
          field_name: '关联合同',
          field_name_en: 'cid',
          form_value: 'checkbox',
          multiple: true,
          props: {
            collapseTags: true,
            value: []
          },
          data_dict: []
        },
        {
          form_value: 'manage'
        }
      ],
      tableOptions: [
        {
          label: '项目编号',
          prop: 'ident',
          width: '120px'
        },
        {
          label: '项目名称',
          type: 'slot',
          name: 'name'
          // width: '450px'
        },
        {
          label: '状态',
          type: 'slot',
          name: 'status'
          // width: '120px'
        },
        {
          label: '负责人',
          type: 'slot',
          name: 'admins'
          // width: '150px'
        },
        {
          label: '计划开始',
          prop: 'start_date'
          // width: '140px'
        },
        {
          label: '计划结束',
          prop: 'end_date'
          // width: '140px'
        },
        {
          label: '未完成/总任务数',
          // width: '120px',
          render: (row) => {
            return (
              <span>
                {row.task_statistics.incomplete}/{row.task_statistics.total}
              </span>
            )
          }
        }
      ],
      treeDefault: 0
    }
  },
  watch: {
    'tableFrom.eid'(val) {
      // if (this.search.length > 0) {
      //   for (let i = 0; i < this.search.length; i++) {
      //     if (this.search[i].field_name_en == 'cid') {
      //       this.search[i].props.disabled = !val.length
      //       this.search[i].data_dict = []
      //       break
      //     }
      //   }
      // }
      if (val.length) {
        this.getContractList(val)
      }
    }
  },
  created() {
    this.getCustomer()
  },
  mounted() {
    this.getTableData()
  },
  methods: {
    handleClose() {
      this.taskDrawer = false
    },
    nowTime() {
      const now = new Date()
      const year = now.getFullYear()
      const month = (now.getMonth() + 1).toString().padStart(2, '0') // 月份从0开始计数，所以加1，然后用padStart保证两位数
      const day = now.getDate().toString().padStart(2, '0') // 日期使用padStart保证两位数
      let nowData = `${year}-${month}-${day}`
      return nowData
    },

    // 获取表格数据
    async getTableData(tableFrom, type) {
      this.loading = true
      let data = tableFrom ? tableFrom : this.tableFrom
      const res = await getProgramListApi(data)
      this.tableData = res.data.list
      this.total = res.data.count
      this.loading = false
      if (type === 1) {
        this.programId = res.data.list[0].id
        this.taskTitle = res.data.list[0].name
        this.taskDrawer = false
      }
    },
    addProgram() {
      this.taskDrawer = true
    },
    addTask() {
      this.$refs.taskDrawerRef.addProgram()
    },
    batchOperation() {
      this.$refs.taskDrawerRef.batchOperation()
    },
    // 获取客户数据
    async getCustomer() {
      const result = await customerSelectApi()
      this.customerList = result.data
      for (let i = 0; i < this.search.length; i++) {
        if (this.search[i].field_name_en == 'eid') {
          this.search[i].data_dict = result.data
          break
        }
      }
    },
    getContractList(eid) {
      if (eid) {
        this.getContract(eid)
      }
    },
    // 获取合同数据
    async getContract(eid) {
      const result = await selectContractListApi({ data: eid })
      this.contractList = result.data
      for (let j = 0; j < result.data.length; j++) {
        result.data[j].value = result.data[j].id
        result.data[j].name = result.data[j].title
      }
      for (let i = 0; i < this.search.length; i++) {
        if (this.search[i].field_name_en == 'cid') {
          this.search[i].data_dict = result.data
          break
        }
      }
    },
    pageChange(page) {
      this.tableFrom.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.tableFrom.limit = val
      this.getTableData()
    },
    // 编辑项目
    handleEdit(row, type) {
      this.$refs.addProgram.openBox(row.id, type)
    },
    // 项目动态
    goDynamics(row) {
      this.programId = row.id
      this.$refs.dynamicsDrawer.drawer = true
      // this.$router.push(`${roterPre}/program/programList/dynamics?id=${row.id}`)
    },
    // 项目任务
    goTask(row) {
      this.$router.push(`${roterPre}/program/programList/taskDetails?id=${row.id}`)
    },
    // 删除项目
    handleDelete(row) {
      this.$modalSure('删除项目，同时会删除项目中的工作项！你确定要删除该项目吗').then(() => {
        deleteProgramApi(row.id).then((res) => {
          this.getTableData()
          this.$refs.addProgram.handleClose()
        })
      })
    },
    async setPayStatus(row, status) {
      var data = {
        id: row.id,
        name: row.name,
        status
      }
      await enterprisePayTypeStatusApi(data)
      this.getTableData()
    },
    confirmData(data) {
      if (data == 'reset') {
        this.tableFrom = {
          page: 1,
          limit: 15,
          types: [],
          name: '',
          status: '',
          eid: [],
          cid: []
        }
        this.treeDefault = 0
      } else {
        this.tableFrom = { ...this.tableFrom, ...data }
      }

      this.getTableData(this.tableFrom)
    },
    treeChange(data) {
      this.tableFrom.types = data.value
      this.getTableData(this.tableFrom)
    }
  }
}
</script>

<style lang="scss" scoped>
.bill-type {
  .header {
    display: flex;
    justify-content: space-between;
    span {
      font-size: 18px;
      line-height: 32px;
      color: #303133;
    }
  }
  .text-right {
    text-align: right;
  }
  .el-radio {
    margin-right: 15px;
  }
  .title {
    font-size: 15px;
    font-weight: 600;
    margin-left: 10px;
    position: relative;
    &:after {
      content: '';
      height: 100%;
      width: 3px;
      background-color: #1890ff;
      position: absolute;
      left: -10px;
      top: 0;
    }
  }
}
.img {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: inline-block;
  vertical-align: top;
  margin-right: 4px;
}
::v-deep.el-table .cell {
  line-height: 26px;
}
.point {
  cursor: pointer;
  &:hover {
    color: #1890ff;
  }
}
.iconxiangmuguanli {
  color: #ff9900;
  margin-right: 4px;
}
.more {
  margin-left: 10px;
}
.btn-box {
  display: flex;
  justify-content: space-between;
  span {
    font-size: 18px;
    line-height: 32px;
    color: #303133;
  }
  .fz30 {
    font-size: 30px;
    margin-left: 14px;
    margin-right: 8px;
    color: #909399;
    font-weight: 400;
  }
}
::v-deep.el-drawer__header {
  padding: 10px 24px 10px 20px;
  font-size: 14px;
}
.flex-between {
  height: 32px;
}
</style>
