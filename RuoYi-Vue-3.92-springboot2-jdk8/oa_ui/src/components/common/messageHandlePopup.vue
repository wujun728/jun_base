<!-- 
  @FileDescription: 消息处理弹窗组件
  功能：根据消息类型展示不同的弹窗组件
-->
<template>
  <div>
    <!-- 日报相关弹窗 -->
    <addBox ref="addBox" :daily-id="1" />
    
    <!-- 审批相关弹窗 -->
    <detail-examine ref="detailExamine" />
    
    <!-- 付款申请弹窗 -->
    <apply-for-payment 
      ref="applyForPayment" 
      :form-data="applyData" 
    />
    
    <!-- 发票详情弹窗 -->
    <invoice-details 
      :form-data="invoiceData" 
      ref="invoiceView" 
    />
    
    <!-- 合同编辑弹窗 -->
    <edit-contract 
      ref="editContract" 
      :form-data="contractData" 
    />
    
    <!-- 客户编辑弹窗 -->
    <edit-customer 
      ref="editCustomer" 
      :form-data="customerData" 
    />
    
    <!-- 低代码相关弹窗 -->
    <check-drawer 
      v-if="checkDrawerShow" 
      ref="checkDrawer" 
      :keyName="keyName" 
      :info="info"
    />
  </div>
</template>

<script>
import { toMessageDetailUrl } from '@/libs/public'
import {
  clientBillDetailApi,
  clientContractDetailApi,
  clientInvoiceDetailApi,
  chargeDetailsApi
} from '@/api/enterprise'
import { crudModuleInfoApi, crudModuleFindApi } from '@/api/develop'

export default {
  name: 'MessageHandlePopup',
  components: {
    // 异步加载组件
    addBox: () => import('@/views/user/daily/components/addBox'),
    detailExamine: () => import('@/views/user/examine/components/detailExamine'),
    checkDrawer: () => import('@/views/develop/module/components/checkDrawer'),
    applyForPayment: () => import('@/views/customer/list/components/applyForPayment'),
    invoiceDetails: () => import('@/components/invoice/invoiceDetails'),
    editContract: () => import('@/views/customer/contract/components/editContract'),
    editCustomer: () => import('@/views/customer/list/components/editCustomer')
  },
  
  props: {
    // 消息详情对象
    detail: {
      type: Object,
      default: () => ({})
    }
  },
  
  data() {
    return {
      keyName: '',          // 低代码模块key
      info: {},             // 低代码模块信息
      checkDrawerShow: false, // 是否显示低代码弹窗
      applyData: {},         // 付款申请数据
      invoiceData: {},       // 发票数据
      contractData: {},      // 合同数据
      customerData: {},      // 客户数据
      selectedType: ['delete', 'recall'], // 可选操作类型
      
      // 支持弹窗打开的消息类型
      openTemplateType: [
        'daily_remind',
        'daily_show_remind',
        'daily_update_remind',
        'business_approval',
        'business_adopt_apply',
        'business_adopt_cc',
        'business_fail',
        'business_recall',
        'contract_renew',
        'contract_return_money',
        'finance_verify_success',
        'finance_verify_fail',
        'contract_invoice',
        'finance_invoice_open',
        'finance_invoice_close',
        'finance_invoice_verify_fail',
        'finance_invoice_verify_success',
        'contract_urgent_renew',
        'contract_day_remind',
        'contract_overdue_remind',
        'contract_overdue_day_remind',
        'contract_abnormal',
        'contract_soon_overdue_remind',
        'dealt_client_work',
        'dealt_money_work',
        'contract_expend',
        'system_crud_type',
        'unsettled_follow_up_remind'
      ]
    }
  },
  
  methods: {
    /**
     * 打开消息对应的弹窗
     * @param {Object} item - 消息项
     * @param {Object} row - 行数据
     */
    async openMessage(item, row) {
      let id = null

      // 判断是否支持弹窗打开
      if (!this.openTemplateType.includes(this.detail.template_type)) {
        // 跳转到详情页
        this.$bus.$emit('message-close-pop')
        toMessageDetailUrl(item)
        this.$emit('handleClose')
      } else {
        // 根据消息类型处理不同弹窗
        const type = this.detail.template_type
        
        // 获取ID逻辑
        if ([
          'contract_expend',
          'contract_renew',
          'contract_return_money',
          'finance_verify_success',
          'finance_verify_fail'
        ].includes(type)) {
          id = this.detail.other.id
        } else {
          id = this.detail.link_id
        }

        // 日报相关处理
        if (type === 'daily_remind') {
          this.$refs.addBox.openBox(0, row.type)
        } 
        // 查看日报
        else if (type === 'daily_show_remind' || type === 'daily_update_remind') {
          this.$refs.addBox.openBox(id, 'check', { types: row.type })
        }
        // 审批相关
        else if ([
          'business_approval',
          'business_adopt_apply',
          'business_adopt_cc',
          'business_fail',
          'business_recall'
        ].includes(type)) {
          this.$refs.detailExamine.openBox({ 
            id: id == 0 ? this.detail.other.id : id 
          })
        }
        // 付款审核
        else if ([
          'contract_renew',
          'contract_return_money',
          'finance_verify_success',
          'finance_verify_fail',
          'contract_expend'
        ].includes(type)) {
          this.getClientBillDetail(id)
        }
        // 发票相关
        else if ([
          'contract_invoice',
          'finance_invoice_open',
          'finance_invoice_close',
          'finance_invoice_verify_fail',
          'finance_invoice_verify_success'
        ].includes(type)) {
          this.getClientInvoiceDetail(id)
        }
        // 合同相关
        else if ([
          'contract_urgent_renew',
          'contract_day_remind',
          'contract_overdue_remind',
          'contract_overdue_day_remind',
          'contract_abnormal',
          'contract_soon_overdue_remind',
          'dealt_money_work'
        ].includes(type)) {
          this.getClientContractDetail(item, row)
        }
        // 客户相关
        else if (type === 'dealt_client_work' || type === 'unsettled_follow_up_remind') {
          this.getClientDataDetail(id)
        }
        // 低代码相关
        else if (type === 'system_crud_type') {
          await this.handleCrudType(row)
        }
      }
    },

    /**
     * 处理低代码类型消息
     * @param {Object} row - 行数据
     */
    async handleCrudType(row) {
      this.keyName = row.other.table_name_en
      this.checkDrawerShow = true
      
      const data = await crudModuleFindApi(this.keyName, row.other.id)
      const infoObj = await crudModuleInfoApi(this.keyName, 0)
      this.info = infoObj.data

      await this.$refs.checkDrawer.openBox(
        row.other, 
        data.data, 
        this.info, 
        this.info.crudInfo.table_name
      )
    },

    /**
     * 获取付款详情并打开弹窗
     * @param {Number} id - 付款ID
     */
    getClientBillDetail(id) {
      clientBillDetailApi(id).then((res) => {
        this.applyData = {
          title: this.$t('customer.viewcustomer'),
          width: '500px',
          data: res.data,
          isClient: false,
          edit: true
        }
        this.$refs.applyForPayment.openBox()
      })
    },

    /**
     * 获取发票详情并打开弹窗
     * @param {Number} id - 发票ID
     */
    getClientInvoiceDetail(id) {
      clientInvoiceDetailApi(id).then((res) => {
        this.invoiceData = {
          title: '发票查看',
          width: '1000px',
          data: res.data
        }
        
        if (this.detail.template_type === 'contract_invoice') {
          this.invoiceData.follType = 'fd'
        }
        
        this.$refs.invoiceView.openBox(res.data.link_id)
      })
    },

    /**
     * 获取合同详情并打开弹窗
     * @param {Object} item - 消息项
     * @param {Object} row - 行数据
     */
    getClientContractDetail(item, row) {
      let link_id = item.link_id ? item.link_id : row.link_id
      item.cid = item.link_id
      item.contract_name = item.linkName

      clientContractDetailApi(link_id)
        .then(async (res) => {
          const data = res.data
          item.eid = res.data.contract_customer.id

          this.contractData = {
            title: '查看合同',
            width: '1000px',
            data: row || item,
            isClient: false,
            name: row && row.client ? row.client.name : item.linkName,
            id: row && row.client ? row.client.id : item.link_id,
            edit: true
          }
          
          this.$refs.editContract.tabIndex = '1'
          this.$refs.editContract.tabNumber = 1
          
          await this.$nextTick()
          
          let obj = {
            cid: link_id
          }
          
          if (row) {
            row.id = link_id
            row.cid = row.id
          }

          this.$refs.editContract.openBox(row || obj)
        })
        .catch((error) => {
          this.$message.error(error.message)
        })
    },

    /**
     * 获取客户详情并打开弹窗
     * @param {Number} id - 客户ID
     */
    getClientDataDetail(id) {
      chargeDetailsApi(id).then(async (res) => {
        const data = res.data
        data.eid = id
        data.cid = 0
        
        this.customerData = {
          title: this.$t('customer.editcustomer'),
          width: '1000px',
          data: data,
          isClient: true,
          edit: true
        }

        this.$refs.editCustomer.tabIndex = '1'
        this.$refs.editCustomer.tabNumber = 1
        this.$refs.editCustomer.openBox(id)
      })
    }
  }
}
</script>

<style scoped lang="scss">
/* 组件样式 */
</style>
