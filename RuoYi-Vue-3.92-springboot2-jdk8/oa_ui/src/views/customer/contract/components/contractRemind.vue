<!-- 合同-付款提醒页面组件 -->
<template>
  <div class="station">
    <div class="mb10 btn-box1">
      <div class="title-16">付款提醒列表</div>
      <el-button
        v-if="!formInfo.types || formInfo.types == 2 || (formInfo.types == 1 && userId == formInfo.data.salesman.id)"
        size="small"
        type="primary"
        @click="handleContract(3, 1)"
      >
        添加付款提醒
      </el-button>
    </div>
    <el-table :data="debtData" style="width: 100%">
      <el-table-column label="付款提醒日期" min-width="150" prop="time"> </el-table-column>
      <el-table-column label="业务类型" min-width="160" prop="">
        <template slot-scope="scope">
          <span v-if="scope.row.types == 0">合同回款</span>
          <span v-if="scope.row.types == 1">合同续费</span>
        </template>
      </el-table-column>
      <el-table-column label="金额(元)" min-width="100" prop="num"> </el-table-column>
      <el-table-column label="状态" min-width="110" prop="num">
        <template #default="{ row }">
          <el-tag v-if="row.status === 1" type="danger">已放弃</el-tag>
          <el-tag v-if="row.status === 2" type="info">已处理</el-tag>
          <el-tag v-if="row.status === 0" type="warning">待处理</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="付款提醒" min-width="150" prop="mark">
        <template #default="{ row }">
          <el-popover :content="row.mark" placement="top-start" trigger="hover" width="250">
            <span v-show="row.mark" slot="reference" class="over-text hand"> {{ row.mark }} </span>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column :label="$t('hr.founder')" min-width="100" prop="card.name"> </el-table-column>

      <!-- 合同管理-编辑/删除 -->
      <el-table-column :label="$t('public.operation')" fixed="right" min-width="180" prop="address">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.types === 0"
            :disabled="scope.row.status !== 0"
            type="text"
            @click="handleBuild(scope.row, buildData.contract_refund_switch, 'contract_refund_switch')"
            >添加回款</el-button
          >
          <el-button
            v-if="scope.row.types === 1"
            :disabled="scope.row.status !== 0"
            type="text"
            @click="handleBuild(scope.row, buildData.contract_renew_switch, 'contract_renew_switch')"
            >添加续费</el-button
          >
          <el-button :disabled="scope.row.status !== 0" type="text" @click="handleContract(3, 2, scope.row)">{{
            $t('public.edit')
          }}</el-button>
          <el-button
            v-if="scope.row.bill_id > 0"
            :disabled="scope.row.status !== 0"
            type="text"
            @click="giveUpFn(scope.row)"
          >
            放弃</el-button
          >
          <el-button v-if="scope.row.bill_id <= 0" type="text" @click="handleDelete(scope.row, 1)">{{
            $t('public.delete')
          }}</el-button>
        </template>
      </el-table-column>

      <!-- 客户管理-修改备注 -->
      <!-- <el-table-column v-else prop="address" min-width="120" :label="$t('public.operation')">
        <template slot-scope="scope">
          <el-button @click="setRemarks(scope.row)" type="text">{{ $t('public.remarks') }}</el-button>
        </template>
      </el-table-column> -->
    </el-table>
    <div v-if="debtData.length > 0" class="block mt10 text-right">
      <el-pagination
        :current-page="debtPage"
        :page-size="where.limit"
        :total="debtTotal"
        layout="total, prev, pager, next, jumper"
        @current-change="debtChange"
      />
    </div>
    <edit-examine
      ref="editExamine"
      :ids="formInfo.data.id"
      :parameterData="parameterData"
      @isOk="getTableData()"
    ></edit-examine>
    <contract-dialog ref="contractDialog" :config="configContract" @isOk="isOk"></contract-dialog>
    <mark-dialog ref="markDialog" :config="configMark" @isMark="isOk"></mark-dialog>
  </div>
</template>

<script>
import { clientRemindDeleteApi, clientRemindListApi, remindAbjureApi } from '@/api/enterprise'
import { configRuleApproveApi } from '@/api/config'
export default {
  name: 'ContractRemind',
  props: {
    formInfo: {
      type: Object,
      default: () => {
        return {}
      }
    },
    type: {
      type: Number,
      default: 0
    }
  },
  components: {
    contractDialog: () => import('@/views/customer/contract/components/contractDialog'),
    markDialog: () => import('@/views/customer/contract/components/markDialog'),
    editExamine: () => import('@/views/user/examine/components/editExamine')
  },
  data() {
    return {
      drawer: false,
      direction: 'rtl',
      handleData: {},
      debtData: [],
      debtTotal: 0,
      renewData: [],
      renewTotal: 0,
      configContract: {},
      userId: JSON.parse(localStorage.getItem('userInfo')).id,
      debtPage: 1,
      renewPage: 1,
      isShow: true,
      where: {
        page: 1,
        limit: 5,
        types: ''
      },
      mark: '',
      buildData: [],
      parameterData: {
        contract_id: '',
        customer_id: '',
        invoice_id: '',
        bill_id: ''
      },
      configMark: {}
    }
  },
  watch: {
    formInfo: {
      handler(nVal) {
        this.isShow = nVal.isClient === false
      },
      immediate: true,
      deep: true
    }
  },
  created() {
    this.getConfigApprove()
  },
  methods: {
    handleBuild(command, val, type) {
      if (this.type == 1) {
        this.parameterData.customer_id = command.eid
      } else {
        this.parameterData.contract_id = command.cid
      }

      this.$refs.editExamine.openBox(val, command.cid, type)
    },
    async getConfigApprove() {
      const result = await configRuleApproveApi(0)
      this.buildData = result.data
    },
    getTableData() {
      this.where.cid = this.formInfo.data.cid
      this.where.eid = this.formInfo.data.eid

      clientRemindListApi(this.where).then((res) => {
        this.debtData = res.data.list
        this.debtTotal = res.data.count
      })
    },
    debtChange(val) {
      this.where.page = val
      this.where.types = 0
      this.getTableData()
    },
    renewChange(val) {
      this.where.page = val
      this.where.types = 1
      this.getTableData()
    },
    addContract(row) {
      this.configContract = {
        title: '添加回款',
        width: '480px',
        data: row,
        cid: row.cid,
        eid: row.eid,
        type: 1
      }

      this.$refs.contractDialog.handleOpen()
    },
    async giveUpFn(row) {
      await this.$modalSure('确定之后变为已放弃状态，您确定此合同不再续费了吗')
      await remindAbjureApi(row.id)
      this.getTableData()
    },
    handleContract(type, edit, row = []) {
      var str = '添加付款提醒'
      if (edit == 2) {
        str = '编辑付款提醒'
      }

      this.configContract = {
        title: str,
        width: '480px',
        edit,
        data: row,
        cid: this.formInfo.data.cid || 0,
        eid: this.formInfo.data.eid || 0,
        type
      }

      setTimeout(() => {
        this.$refs.contractDialog.handleOpen()
      }, 300)
    },
    isOk() {
      this.where.types = ''
      this.getTableData()
    },
    setRemarks(row) {
      this.configMark = {
        title: this.$t('customer.remarkinformation'),
        width: '480px',
        id: row.id,
        type: 3,
        mark: row.mark
      }
      this.$refs.markDialog.handleOpen()
    },
    // 删除
    handleDelete(item, type) {
      this.$modalSure(this.$t('customer.placeholder77')).then(() => {
        clientRemindDeleteApi(item.id).then((res) => {
          if (type === 1) {
            if (this.where.page > 1 && this.debtData.length <= 1) {
              this.where.page--
              this.where.types = 0
            }
          } else {
            if (this.where.page > 1 && this.renewData.length <= 1) {
              this.where.page--
              this.where.types = 1
            }
          }
          this.getTableData()
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-input__inner {
  text-align: left;
}

.hand {
  cursor: pointer;
}
.btn-box1 {
  height: 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.station {
  height: 100%;
}
.from-item-title {
  margin-top: 8px;
  border-left: 5px solid #1890ff;
  span {
    padding-left: 10px;
    font-weight: bold;
    font-size: 14px;
  }
}
::v-deep .el-table__body {
  width: 100%;
  table-layout: fixed !important;
}
</style>
