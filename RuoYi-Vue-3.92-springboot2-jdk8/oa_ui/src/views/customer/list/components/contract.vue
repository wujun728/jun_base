<template>
  <div class="station">
    <div class="btn-box1 mb10">
      <div class="title-16">合同列表</div>
      <el-button
        size="small"
        v-if="showAddButton"
        type="primary"
        @click="addContract"
        >添加合同</el-button
      >
    </div>
    <el-table :data="contractData" fit style="width: 100%">
      <el-table-column prop="contract_name" :label="$t('customer.contractname')"> </el-table-column>
      <el-table-column prop="contract_price" min-width="100px" :label="$t('customer.contractpay')"> </el-table-column>
      <el-table-column prop="surplus" label="付款状态">
        <template slot-scope="scope">
          <span class="pointer color-success" v-if="parseFloat(scope.row.surplus) === 0">已结清</span>
          <span class="pointer color-warning" v-else>未结清</span>
        </template>
      </el-table-column>
      <el-table-column prop="contract_status" label="合同状态">
        <template slot-scope="scope">
          <el-tag :type="getAbnormalTag(scope.row)">{{ getAbnormalTexts(scope.row) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="creator.name" label="创建人"> </el-table-column>
      <el-table-column prop="address" :label="$t('public.operation')">
        <template slot-scope="scope">
          <el-button @click="handleCheck(scope.row)" type="text">{{ $t('public.check') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
        :page-size="where.limit"
        :current-page="where.page"
        layout="total, prev, pager, next, jumper"
        :total="contractTotal"
        @current-change="pageChange"
      />
    </div>

    <!-- 弹窗 -->
    <editContract ref="editContract" :form-data="fromData" @isOk="getTableData"></editContract>
    <add-contract ref="addContract" @getTableData="getTableData()" :form-data="contractFromData"></add-contract>
  </div>
</template>

<script>
import { clientContractListApi } from '@/api/enterprise'
import { getContractTagType, getContractText } from '@/libs/customer'
export default {
  name: 'Contract',
  components: {
    addContract: () => import('@/views/customer/contract/components/addContract'),
    editContract: () => import('@/views/customer/contract/components/editContract')
  },
  props: {
    formInfo: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },

  data() {
    return {
      where: {
        eid: 0,
        page: 1,
        limit: 15,
        types: 7
      },
      userId: JSON.parse(localStorage.getItem('userInfo')).id,
      contractTotal: 0,
      contractData: [],
      fromData: {},
      contractFromData: {}
    }
  },
  computed: {
    showAddButton() {
      return this.formInfo.types == 2 || (this.formInfo.types == 1 && this.userId == this.formInfo.data.salesman.id)
    },
  },
  methods: {
    getTableData() {
      this.where.eid = this.formInfo.data.eid
      clientContractListApi(this.where).then((res) => {
        this.contractData = res.data.list
        this.contractTotal = res.data.count
      })
    },

    // 添加合同
    addContract() {
      this.contractFromData = {
        title: this.$t('customer.addcontract'),
        id: this.formInfo.data.eid,
        name: this.formInfo.data.name,
        edit: false,
        width: '570px'
      }
      this.$refs.addContract.openBox()
    },
    
    async handleCheck(item) {
      // 解构赋值获取id，避免直接修改传入的item对象
      const { id } = item;
      this.fromData = {
        title: this.$t('customer.viewcustomer'),
        width: '1000px',
        data: { ...item, cid: id }, // 使用展开运算符创建新对象并添加cid属性
        isClient: false,
        edit: true
      };

      // 等待DOM更新
      await this.$nextTick();
      // 可以考虑封装成一个方法，避免重复设置属性
      const editContractRef = this.$refs.editContract;
      editContractRef.tabIndex = '1';
      editContractRef.tabNumber = 1;
      editContractRef.openBox(this.fromData.data); // 传递新对象
    },
    getAbnormalTag(row) {
      return getContractTagType(row)
    },
    getAbnormalTexts(row) {
      return getContractText(row)
    },
    pageChange(val) {
      this.where.page = val
      this.getTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
.color1 {
  color: #ff9900;
}
.color2 {
  color: #19be6b;
}
.color3 {
  color: #ed4014;
}
.btn-box1 {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 32px;
}

::v-deep .el-input__inner {
  text-align: left;
}
.from-item-title {
  border-left: 5px solid #1890ff;
  span {
    padding-left: 10px;
    font-weight: bold;
    font-size: 14px;
  }
}
::v-deep.el-tag {
  background-color: transparent;
}
</style>
