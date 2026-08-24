<!-- 合同-合同续费页面组件 -->
<template>
  <div class="station">
    <el-row class="mb14">
      <el-button @click="handleContract(2, 1)" size="small" type="primary">{{ $t('customer.addrenewal') }}</el-button>
    </el-row>
    <div class="renewal-content" v-if="renewCensusData.length > 0">
      <el-row>
        <el-col :span="20" class="over-text">
          <p v-for="item in renewCensusData" :key="item.cate_id">
            <span>{{ item.renew ? item.renew.title : '' }}</span>
            <span v-if="item.end_date !== '0000-00-00 00:00:00'" :class="getRenewDate(item.date)">{{
              $moment(item.end_date).format('yyyy-MM-DD')
            }}</span>
            <span v-else>--</span>
            到期
          </p>
        </el-col>
        <el-col :span="4" class="text-right">
          <el-button size="small" type="text" @click="handleRenewCensus"
            >查看全部 <i class="el-icon-d-arrow-right"></i
          ></el-button>
        </el-col>
      </el-row>
    </div>
    <el-table :data="renewData" style="width: 100%; height: 90%;">
      <el-table-column prop="end_date" :label="$t('customer.renewaldate')" min-width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.end_date !== '0000-00-00 00:00:00'">{{
            $moment(scope.row.end_date).format('YYYY-MM-DD')
          }}</span>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column prop="num" :label="$t('customer.renewalamount')" min-width="80"> </el-table-column>
      <el-table-column prop="renew.title" :label="$t('customer.renewaltype')" min-width="110"> </el-table-column>
      <el-table-column prop="status" :label="$t('customer.accessstatus')" min-width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="warning" size="mini"> {{ $t('customer.audit') }} </el-tag>
          <el-tag v-if="scope.row.status === 1" type="info" size="mini"> {{ $t('customer.passed') }} </el-tag>
          <el-tag v-if="scope.row.status == -1" type="info" size="mini"> 撤回</el-tag>
          <el-popover v-if="scope.row.status === 2" trigger="hover" placement="top">
            <p>{{ $t('customer.reason') }}:</p>
            <p>{{ scope.row.fail_msg }}</p>
            <div slot="reference">
              <el-tag type="danger" size="mini"> {{ $t('customer.fail') }} </el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column prop="card.name" :label="$t('setting.info.founder')" min-width="80"> </el-table-column>
      <el-table-column prop="address" min-width="160" :label="$t('public.operation')">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status !== 1" type="text" @click="handleContract(2, 2, scope.row)">
            {{ scope.row.status == 0 ? '编辑' : '重新提交' }}
          </el-button>
          <el-button @click="setRemarks(scope.row)" type="text">{{ $t('public.remarks') }}</el-button>
          <el-button v-if="scope.row.status == 0" @click="handleDelete(scope.row)" type="text">撤回</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block mt10 text-right" v-if="renewData.length > 0">
      <el-pagination
        :page-size="where.limit"
        :current-page="renewPage"
        layout="total, prev, pager, next, jumper"
        :total="renewTotal"
        @current-change="renewChange"
      />
    </div>
    <contract-dialog ref="contractDialog" :config="configContract" @isOk="isOk"></contract-dialog>
    <mark-dialog ref="markDialog" :config="configMark" @isMark="isOk"></mark-dialog>
    <renew-census ref="renewCensus" :config="configRenewCensus"></renew-census>
  </div>
</template>

<script>
import { clientBillListApi, clientBillPutApi } from '@/api/enterprise'
export default {
  name: 'ContractRecord',
  props: {
    formInfo: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  components: {
    contractDialog: () => import('@/views/customer/contract/components/contractDialog'),
    markDialog: () => import('@/views/customer/contract/components/markDialog'),
    renewCensus: () => import('@/views/customer/contract/components/renewCensus')
  },
  data() {
    return {
      handleData: {},
      renewData: [],
      renewCensusData: [],
      renewTotal: 0,
      configContract: {},
      configRenewCensus: {},
      renewPage: 1,
      where: {
        page: 1,
        limit: 15,
        types: 1
      },
      mark: '',
      configMark: {},
      renewMaxDate: 30
    }
  },
  watch: {
    formInfo: {
      handler(nVal) {
        nVal.isClient === false ? (this.where.cid = nVal.data.id) : (this.where.eid = nVal.data.id)
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    getTableData() {
      clientBillListApi(this.where).then((res) => {
        this.renewData = res.data.list
        this.renewCensusData = res.data.renew_census
        this.renewTotal = res.data.count
      })
    },
    renewChange(val) {
      this.where.page = val
      this.where.types = 1
      this.getTableData()
    },
    handleContract(type, edit, row = []) {
      let str = ''
      if (type === 1 && edit === 1) {
        str = this.$t('customer.addcollection')
      } else if (type === 1 && edit === 2) {
        str = this.$t('customer.editcollection')
      } else if (type === 2 && edit === 1) {
        str = this.$t('customer.addrenewal')
      } else if (type === 2 && edit === 2) {
        str = this.$t('customer.editrenewal')
      }

      this.configContract = {
        title: str,
        width: '480px',
        edit,
        data: row,
        cid: this.formInfo.data.id,
        eid: this.formInfo.data.eid,
        type
      }
      this.$refs.contractDialog.handleOpen()
    },
    isOk() {
      this.where.types = 1
      this.getTableData()
    },
    setRemarks(row) {
      this.configMark = {
        title: this.$t('customer.remarkinformation'),
        width: '480px',
        id: row.id,
        type: 1,
        mark: row.mark
      }
      this.$refs.markDialog.handleOpen()
    },
    handleRenewCensus() {
      this.configRenewCensus = {
        title: '合同续费',
        width: '480px',
        data: this.renewCensusData
      }
      this.$refs.renewCensus.handleOpen()
    },
    getRenewDate(data) {
      let str = ''
      let days = this.$moment(data).diff(this.$moment(new Date()), 'days')
      if (days > this.renewMaxDate) {
        str = 'color-success'
      } else if (days >= 0 && days <= this.renewMaxDate) {
        str = 'color-warning'
      } else {
        str = 'color-danger'
      }
      return str
    },
    // 删除
    handleDelete(item) {
      this.$modalSure(this.$t('customer.placeholder23')).then(() => {
        clientBillPutApi(item.id).then((res) => {
          if (this.where.page > 1 && this.renewData.length <= 1) {
            this.where.page--
            this.where.types = 1
          }

          this.getTableData()
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.station {
  height: 100%;
}
::v-deep .el-table {
  height: 100% !important;
}
::v-deep .el-input__inner {
  text-align: left;
}
.renewal-content {
  width: 100%;
  margin-bottom: 10px;
  p {
    margin: 12px 20px 0 0;
    padding: 0;
    font-size: 13px;
    display: inline-block;
    &:last-of-type {
      margin-right: 0;
    }
  }
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
</style>
