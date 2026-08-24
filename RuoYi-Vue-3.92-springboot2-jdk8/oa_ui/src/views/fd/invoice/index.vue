<!-- 财务-发票审核页面 -->
<template>
  <div class="divBox">
    <el-card :body-style="{ padding: '20px 20px 0 20px' }" class="mb14 normal-page">
      <oaFromBox
        :search="search"
        :total="total"
        :isAddBtn="false"
        :isViewSearch="false"
        :title="$route.meta.title"
        @confirmData="confirmData"
      ></oaFromBox>
      <div v-loading="loading" class="mt10">
        <div v-if="tableData.length == 0" class="defaultPage">
          <img alt="" class="img" src="../../../assets/images/defd.png" />
          <span class="text"> 暂无发票数据</span>
        </div>
        <div v-else style="width: 100%; overflow: auto" :style="{ height: tableHeight }">
          <!-- 发票页面 -->
          <div v-for="(item, index) in tableData" :key="index" class="table-box">
            <div>
              <!-- 发票顶部 -->
              <div class="header">
                <div class="header-left">
                  {{ item.types | titleTypeFn }}·{{ item.collect_type == 'mail' ? '电子' : '纸质' }}
                  <span :class="item.status | bgcFn" class="table-add">{{ item.status | statusTypeFn }}</span>
                </div>

                <div class="left">
                  <el-button v-hasPermi="['fd:invoice:check']" type="text" @click="handleCheck(item)">查看 </el-button>
                  <el-button v-if="item.status === 5 && item.num" type="text" @click="handleDown(item)"
                    >下载
                  </el-button>

                  <el-button v-if="item.status === 1" type="text" @click="handleInvoicUri(item)">在线开票 </el-button>

                  <el-button
                    v-if="item.status === 1"
                    v-hasPermi="['fd:invoice:edit']"
                    type="text"
                    @click="handleInvoicing(item)"
                    >录入发票
                  </el-button>

                  <el-button v-if="item.status == 5" v-hasPermi="['fd:invoice:void']" type="text" @click="voidFn(item)"
                    >作废
                  </el-button>
                </div>
              </div>

              <!-- 企业专用发票 -->
              <div v-if="item.types == '3'" class="content main">
                <div class="left">
                  <el-form label-width="110px">
                    <el-form-item label="发票抬头：" prop="name">
                      <span :data-clipboard-text="item.title" class="pointer copy" title="点击复制" @click="copy">{{
                        item.title
                      }}</span>
                    </el-form-item>
                    <el-form-item label="纳税人识别号：" prop="name">
                      <span :data-clipboard-text="item.ident" class="pointer copy" title="点击复制" @click="copy">{{
                        item.ident || '--'
                      }}</span>
                    </el-form-item>
                    <el-form-item label="开票金额：" prop="name">
                      <span
                        :class="item.price == item.amount ? '' : 'active'"
                        :data-clipboard-text="item.amount"
                        class="pointer copy"
                        title="点击复制"
                        @click="copy"
                        >{{ item.amount }}</span
                      >
                    </el-form-item>
                  </el-form>
                </div>
                <div class="line"></div>
                <div class="right">
                  <el-form label-width="110px">
                    <el-form-item label="客户名称：" prop="name">
                      <span>{{ item.customer ? item.customer.customer_name : '--' }}</span>
                    </el-form-item>
                    <el-form-item v-if="item.collect_type == 'mail'" label="邮箱信息：" prop="name">
                      <span
                        :data-clipboard-text="item.collect_email"
                        class="pointer copy"
                        title="点击复制"
                        @click="copy"
                        >{{ item.collect_email || '--' }}</span
                      >
                    </el-form-item>
                    <el-form-item v-else label="邮寄信息：" prop="name">
                      <span
                        :data-clipboard-text="item.collect_name + ' ' + item.collect_tel + ' ' + item.mail_address"
                        class="pointer copy"
                        title="点击复制"
                        @click="copy"
                        >{{ item.collect_name }} {{ item.collect_tel }} {{ item.mail_address }}</span
                      >
                    </el-form-item>
                    <el-form-item label="申请人：" prop="name">
                      <span> {{ item.card ? item.card.name : '--' }}</span>
                    </el-form-item>
                    <el-form-item label="申请时间：" prop="name">
                      <span> {{ item.created_at }}</span>
                    </el-form-item>
                    <el-form-item label="付款金额：" prop="name">
                      <span :class="item.price == item.amount ? '' : 'active'">{{ item.price }}</span>
                    </el-form-item>
                  </el-form>
                </div>
              </div>

              <!-- 企业普通发票 -->
              <div v-if="item.types == '2'" class="content main">
                <div class="left">
                  <el-form label-width="110px">
                    <el-form-item label="发票抬头：" prop="name">
                      <span :data-clipboard-text="item.title" class="pointer copy" title="点击复制" @click="copy">{{
                        item.title
                      }}</span>
                    </el-form-item>
                    <el-form-item label="纳税人识别号：" prop="name">
                      <span :data-clipboard-text="item.ident" class="pointer copy" title="点击复制" @click="copy">{{
                        item.ident || '--'
                      }}</span>
                    </el-form-item>
                    <el-form-item label="开票金额：" prop="name">
                      <span
                        :class="item.price == item.amount ? '' : 'active'"
                        :data-clipboard-text="item.amount"
                        class="pointer copy"
                        title="点击复制"
                        @click="copy"
                        >{{ item.amount }}</span
                      >
                    </el-form-item>
                  </el-form>
                </div>
                <div class="line"></div>
                <div class="right">
                  <el-form label-width="110px">
                    <el-form-item label="客户名称：" prop="name">
                      <span>{{ item.customer ? item.customer.customer_name : '--' }}</span>
                    </el-form-item>
                    <el-form-item v-if="item.collect_type == 'mail'" label="邮箱信息：" prop="name">
                      <span
                        :data-clipboard-text="item.collect_email"
                        class="pointer copy"
                        title="点击复制"
                        @click="copy"
                        >{{ item.collect_email || '--' }}</span
                      >
                    </el-form-item>
                    <el-form-item v-else label="邮寄信息：" prop="name">
                      <span
                        :data-clipboard-text="item.collect_name + ' ' + item.collect_tel + ' ' + item.mail_address"
                        class="pointer copy"
                        title="点击复制"
                        @click="copy"
                        >{{ item.collect_name }} {{ item.collect_tel }} {{ item.mail_address }}</span
                      >
                    </el-form-item>
                    <el-form-item label="申请人：" prop="name">
                      <span> {{ item.card ? item.card.name : '--' }}</span>
                    </el-form-item>
                    <el-form-item label="申请时间：" prop="name">
                      <span> {{ item.created_at }}</span>
                    </el-form-item>
                    <el-form-item label="付款金额：" prop="name">
                      <span :class="item.price == item.amount ? '' : 'active'">{{ item.price }}</span>
                    </el-form-item>
                  </el-form>
                </div>
              </div>

              <!-- 个人普通发票 -->
              <div v-if="item.types == '1'" class="content">
                <div class="left">
                  <el-form label-width="110px">
                    <el-form-item label="发票抬头：" prop="name">
                      <span :data-clipboard-text="item.title" class="pointer copy" title="点击复制" @click="copy">
                        {{ item.title }}
                      </span>
                    </el-form-item>
                    <el-form-item label="开票金额：" prop="name">
                      <span
                        :class="item.price == item.amount ? '' : 'active'"
                        :data-clipboard-text="item.amount"
                        class="pointer copy"
                        title="点击复制"
                        @click="copy"
                        >{{ item.amount }}</span
                      >
                    </el-form-item>
                  </el-form>
                </div>
                <div class="line"></div>
                <div class="right">
                  <el-form label-width="110px">
                    <el-form-item label="客户名称：" prop="name">
                      <span>{{ item.customer ? item.customer.customer_name : '--' }}</span>
                    </el-form-item>
                    <el-form-item v-if="item.collect_type == 'mail'" label="邮箱信息：" prop="name">
                      <span
                        :data-clipboard-text="item.collect_email"
                        class="pointer copy"
                        title="点击复制"
                        @click="copy"
                        >{{ item.collect_email || '--' }}</span
                      >
                    </el-form-item>
                    <el-form-item v-else label="邮寄信息：" prop="name">
                      <span
                        :data-clipboard-text="item.collect_name + ' ' + item.collect_tel + ' ' + item.mail_address"
                        class="pointer copy"
                        title="点击复制"
                        @click="copy"
                        >{{ item.collect_name }} {{ item.collect_tel }} {{ item.mail_address }}</span
                      >
                    </el-form-item>

                    <el-form-item label="申请人：" prop="name">
                      <span> {{ item.card ? item.card.name : '--' }}</span>
                    </el-form-item>
                    <el-form-item label="申请时间：" prop="name">
                      <span> {{ item.created_at }}</span>
                    </el-form-item>
                    <el-form-item label="付款金额：" prop="name">
                      <span :class="item.price == item.amount ? '' : 'active'">{{ item.price }}</span>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
            </div>
          </div>
        </div>
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
      </div>
    </el-card>

    <examine-dialog ref="examineDialog" :config="config" @isOk="isOk"></examine-dialog>
    <invoice-details ref="invoiceDetailsView" :form-data="invoiceData"></invoice-details>
    <invoice-view ref="invoiceView" :form-data="invoiceData"></invoice-view>
    <!-- 开票 -->
    <invoicingDialog ref="invoicingDialog" :config="invoicingDialog" @isOk="isOk"></invoicingDialog>
    <!-- 申请作废 -->
    <div class="toVoid">
      <el-dialog :visible.sync="dialogFormVisible" title="作废" width="600px">
        <div class="mt20">
          <el-form ref="voidData" :model="voidDataForm" class="from">
            <el-form-item
              :rules="[{ required: true, message: '请填写作废原因', trigger: 'blur' }]"
              label="作废原因："
              label-width="100px"
              prop="voidData"
            >
              <el-input v-model="voidDataForm.voidData" placeholder="请填写原因" type="textarea"></el-input>
            </el-form-item>
            <div class="footer">
              <el-button class="btn" size="small" @click="dialogFormVisible = false">取 消</el-button>
              <el-button size="small" type="primary" @click="submit">确 定</el-button>
            </div>
          </el-form>
        </div>
      </el-dialog>
    </div>

    <el-dialog :top="`7vh`" :visible.sync="dialogInvoiceVisible" class="headerbtn" width="90%" @close="dialogClose">
      <iframe v-if="invoiceUri" :src="invoiceUri" frameborder="0" height="650" width="100%"></iframe>
    </el-dialog>
  </div>
</template>

<script>
import { financeInvoiceListApi, clientInvoiceStatus, clientInvoiceUriApi } from '@/api/enterprise'
import ClipboardJS from 'clipboard'
import { getInvoiceClassName, getInvoiceText, getInvoiceType, selectInvoiceFd } from '@/libs/customer'
import { clientInvoiceDetailApi, clientInvoiceStatusPutApi } from '@/api/client'
import { fileLinkDownLoad } from '@/libs/public'

export default {
  components: {
    oaFromBox: () => import('@/components/common/oaFromBox'),
    examineDialog: () => import('@/views/fd/examine/components/examineDialog'),
    invoiceDetails: () => import('@/components/invoice/invoiceDetails'),
    invoicingDialog: () => import('./components/invoicingDialog'),
    invoiceView: () => import('@/views/customer/invoice/components/invoiceView')
  },
  props: {
    activeName: {
      type: String,
      default: '1'
    },
    types: {
      type: String | Array,
      default: ''
    }
  },
  data() {
    return {
      invoiceUri: '',
      invoiceId: 0, // 发票id
      dialogInvoiceVisible: false,
      dialogFormVisible: false,
      invoicingDialog: {},
      tableData: [],
      where: {
        page: 1,
        status: 1,
        time: '',
        name: '',
        invoiced: '',
        limit: 15,
        from: 1,
        time_field: '',
        types: ''
      },
      loading: false,
      voidId: '', // 发票id
      voidDataForm: {
        voidData: ''
      },

      total: 0,
      config: {},
      invoiceData: {},
      search: [
        {
          field_name: '关键字搜索',
          field_name_en: 'search',
          form_value: 'input'
        },
        {
          field_name: '审核状态',
          field_name_en: 'status',
          form_value: 'select',
          data_dict: selectInvoiceFd
        },
        {
          field_name: '发票类型',
          field_name_en: 'types',
          form_value: 'select',
          data_dict: [
            { value: '', label: this.$t('toptable.all') },
            { value: 1, label: this.$t('customer.personalinvoice') },
            { value: 2, label: this.$t('customer.enterpriseinvoice') },
            { value: 3, label: this.$t('customer.specialinvoice') }
          ]
        },
        {
          field_name: '时间类型',
          field_name_en: 'time_field',
          form_value: 'select',
          data_dict: [
            { name: '全部', value: '' },
            { name: '申请日期', value: 'time' },
            { name: '开票日期', value: 'real_date' }
          ]
        },
        {
          field_name: '开始日期',
          field_name_end: '结束日期',
          field_name_en: 'time',
          form_value: 'date_picker'
        }
      ]
    }
  },
  mounted() {
    if (this.activeName != 2) {
      let index = this.search.findIndex((val) => val.field_name_en == 'status')
      if (index != -1) {
        this.search.splice(index, 1)
      }
    }
    this.where.status = this.types
    this.getTableData()
    window.addEventListener('message', (event) => {
      switch (event.data.event) {
        case 'onCancel':
          this.dialogInvoiceVisible = false
          this.invoiceUri = ''
          this.invoiceId = 0
          break
        case 'onSuccess':
          this.dialogInvoiceVisible = false
          this.invoiceUri = ''
          if (this.invoiceId) {
            clientInvoiceStatusPutApi(this.invoiceId, event.data.data)
          }
          break
      }
    })
  },
  filters: {
    statusTypeFn(val) {
      return getInvoiceText(val)
    },
    bgcFn(val) {
      return getInvoiceClassName(val)
    },
    titleTypeFn(val) {
      return getInvoiceType(val)
    }
  },
  methods: {
    dialogClose() {
      this.dialogInvoiceVisible = false
      this.invoiceId = 0
    },
    handleInvoicUri(item) {
      if (!item.id) {
        return this.$message.error('缺少发票记录ID')
      }
      clientInvoiceUriApi(item.id).then((res) => {
        if (res.status === 400) {
          // this.$message.error(res.msg)
        } else {
          this.dialogInvoiceVisible = true
          this.invoiceUri = res.data.uri
          this.invoiceId = item.id
        }
      })
    },
    // 下载
    handleDown(item) {
      clientInvoiceDetailApi(item.id).then((res) => {
        if (res.data.attach) {
          fileLinkDownLoad(res.data.attach, '发票详情')
        } else {
          this.$message.error('获取下载地址失败')
        }
      })
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    getStatus(id) {
      var str = ''
      if (id === 0) {
        str = this.$t('customer.audit')
      } else if (id === 1) {
        str = this.$t('customer.passed')
      } else {
        str = this.$t('customer.fail')
      }
      return str
    },

    // 作废
    voidFn(data) {
      this.voidId = data.id
      this.dialogFormVisible = true
      this.voidDataForm.voidData = ''
    },
    getInvoiceType(id) {
      var str = ''
      if (id === 1) {
        str = this.$t('customer.personalinvoice')
      } else if (id === 2) {
        str = this.$t('customer.enterpriseinvoice')
      } else {
        str = this.$t('customer.specialinvoice')
      }
      return str
    },
    // 获取表格数据
    getTableData() {
      this.loading = true
      financeInvoiceListApi(this.where).then((res) => {
        this.loading = false
        this.tableData = res.data.list
        this.total = res.data.count
      })
    },

    // 开票
    handleInvoicing(row) {
      this.invoicingDialog = {
        title: '开票审核',
        data: row
      }
      this.$refs.invoicingDialog.openBox()
    },
    // c查看
    async handleCheck(item) {
      this.invoiceData = {
        title: '发票查看',
        width: '1000px',
        data: item,
        follType: 'fd'
      }
      if (item.link_id == 0) {
        this.$refs.invoiceView.openBox(item.link_id)
      } else {
        this.$refs.invoiceDetailsView.openBox(item.link_id)
      }
    },

    submit() {
      let data = {
        invalid: 2,
        remark: this.voidDataForm.voidData
      }
      this.$refs.voidData.validate((valid) => {
        if (valid) {
          clientInvoiceStatus(this.voidId, data).then((res) => {
            this.dialogFormVisible = false
            this.form = {}
            this.getTableData()
          })
        }
      })
    },
    // 发票审核
    async handleEdit(item, type) {
      if (type === 1) {
        this.config = {
          title: '作废审核',
          width: '480px',
          type: 2,
          data: item
        }
        this.$refs.examineDialog.handleOpen()
      } else {
        this.config = {
          title: this.$t('customer.invoicereview'),
          width: '480px',
          type: 2,
          data: item
        }
        this.$refs.examineDialog.handleOpen()
      }
    },

    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          limit: this.where.limit
        }
      } else {
        this.where.types = data.types
        this.where.time = data.time
        this.where.time_field = data.time_field
        this.where.invoiced = data.type
        this.where.name = data.search
      }
      this.where.page = 1
      this.where.status = data.status
      this.getTableData()
    },
    isOk() {
      this.getTableData()
    },
    copy() {
      this.$nextTick(() => {
        const clipboard = new ClipboardJS('.copy')
        clipboard.on('success', () => {
          this.$message.success(this.$t('setting.copytitle'))
          clipboard.destroy()
        })
        this.$store.commit('app/SET_CLICK_TAB', false)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.btn-type {
  padding: 4px 10px;
  font-size: 13px;
}

.footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.text-height {
  line-height: 18px !important;
}

.line {
  width: 100%;
  height: 4px;

  border-bottom: 1px solid #f2f6fc;
  margin-bottom: 30px;
}

.active {
  color: red !important;
}

.from {
  margin: 0 24px;
}

.toVoid {
  ::v-deep .el-dialog {
    border-radius: 6px;
    height: 249px;
  }

  ::v-deep .el-dialog__body {
    padding: 0;
  }

  ::v-deep .el-textarea__inner {
    width: 400px;
    height: 90px;
    font-size: 13px;
  }
}

.defaultPage {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 100px;

  .img {
    width: 200px;
    height: 150px;
    display: block;
  }

  .text {
    font-size: 13px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #999999;
  }
}

.head-box {
  display: flex;
  align-items: center;

  .input {
    width: 240px;
    margin: 0 20px 0 10px;
  }
}

.invoice-info {
  color: #909399;
}

.invoice-info > div > span {
  color: #606266;
}

.table-box {
  width: 100%;
  border: 1px solid #eaf4ff;
  margin-bottom: 20px;

  .header {
    padding: 0 20px;
    height: 48px;
    background: #f7fbff;
    border-radius: 4px 4px 0px 0px;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .header-left {
      font-size: 13px;
      font-family: PingFang SC-中黑体, PingFang SC;
      font-weight: 600;
      color: #303133;
    }

    .left .el-button--medium {
      font-size: 13px;
    }

    .left {
      font-size: 13px;
      font-family: PingFang SC-常规体, PingFang SC;
      font-weight: normal;
      color: #1890ff;
      line-height: 12px;
    }
  }

  .content {
    width: 100%;
    padding: 20px 40px 0px 40px;
    display: flex;
    position: relative;
    justify-content: space-between;

    .line {
      position: absolute;
      top: 0;
      left: 50%;
      width: 4px;
      height: calc(100% - 40px);
      border-left: 1px dashed #ebf5ff;
      margin: 20px auto;
      transform: translateX(-10px);
    }

    .left {
      flex: 1;
    }

    .right {
      flex: 1;
    }

    ::v-deep .el-form-item__label {
      font-size: 12px !important;
      font-family: PingFang SC-常规体, PingFang SC;
      font-weight: normal;
      color: #909399;
      line-height: 12px;
    }

    ::v-deep .el-form-item {
      margin-bottom: 20px;
    }

    ::v-deep .el-form-item--medium .el-form-item__content {
      line-height: 12px;
    }

    span {
      font-size: 13px;
      font-family: PingFang SC-常规体, PingFang SC;
      font-weight: 400;
      color: #303133;
      line-height: 12px;
    }
  }
}

.table-add {
  display: inline-block;
  // width: 50px;
  height: 24px;
  padding: 0 8px;
  border: 1px solid transparent;
  line-height: 22px;
  border-radius: 2px;
  font-size: 12px;
  text-align: center;
  cursor: pointer;
  margin-left: 10px;

  &.blue {
    // background: rgba(24, 144, 255, 0.05);
    border-color: #1890ff;
    color: #1890ff;
  }

  &.yellow {
    // background: rgba(255, 153, 0, 0.05);
    border-color: #ff9900;
    color: #ff9900;
  }

  &.red {
    // background: rgba(255, 153, 0, 0.05);
    border-color: #ed4014;
    color: #ed4014;
  }

  &.green {
    // background: rgba(0, 192, 80, 0.05);
    border-color: #00c050;
    color: #00c050;
  }

  &.gray {
    // background: rgba(153, 153, 153, 0.05);
    border-color: #999999;
    color: #999999;
  }
}

.detail-box {
  padding: 20px;
  color: #333;

  .item-box {
    display: flex;
    margin-bottom: 20px;
    font-size: 14px;

    span {
      /*width: 80px;*/
    }

    div {
      margin-left: 20px;
    }
  }

  .content-box {
    span {
      font-size: 14px;
    }
  }
}

::v-deep .el-drawer__body {
  height: 100%;
  overflow-y: auto;
}
::v-deep.headerbtn .el-dialog__headerbtn {
  position: absolute;
  top: 12px;
  right: 25px;
}
</style>

<style lang="scss">
.content-box {
  font-size: 13px;
}
</style>
