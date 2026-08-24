<!-- 人事-员工档案 -->
<template>
  <div class="divBox">
    <!-- 对应列表组件 -->
    <div class="form-wrapper">
      <keep-alive>
        <component
          :is="componentName"
          ref="dynamic"
          @invitation="invitationFn"
          @invitationFn="invitationFn"
          @onDelete="onDelete"
          @onQuit="onQuit"
          @onWorker="onWorker"
          @openDrawer="openDrawer"
        ></component>
      </keep-alive>
    </div>

    <!-- 人员新增/修改  -->
    <UserDetails ref="userDetails" :actionType="actionType" :tabtypes="tabtypes" @getList="getList" />

    <!-- 邀请链接 -->
    <el-dialog :before-close="handleClose" :title="linkTitle" :visible.sync="linkShow" top="20%" width="560px">
      <span>{{ linkTable }}</span>
      <div class="tips">提示：{{ linkContent }}</div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="linkShow = false">取 消</el-button>
        <el-button type="primary" @click="copy">复制邀请链接</el-button>
      </span>
    </el-dialog>

    <!-- 离职弹窗 -->
    <oa-dialog
      ref="oaDialog"
      :fromData="fromData"
      :formConfig="formConfig"
      :formRules="formRules"
      :formDataInit="formDataInit"
      @submit="submit"
    ></oa-dialog>
  </div>
</template>

<script>
import { perfectCard, deleteCard, formalCard, quitCard, importCardApi, getTemp, getInterview } from '@/api/enterprise'
import file from '@/utils/file'
import Vue from 'vue'

Vue.use(file)
export default {
  props: {
    componentName: {
      type: String,
      default: 'notNewEmployees'
    }
  },
  data() {
    return {
      // 组件名称 -> 对应 Vue - component :is 属性 动态组件
      // componentName: 'notNewEmployees',

      isLoading: false,
      linkShow: false,

      formDataInit: {
        name: '',
        quit_time: '',
        info: '',
        mark: '',
        user_id: ''
      },
      formConfig: [
        {
          type: 'input',
          label: '人员姓名：',
          placeholder: '人员姓名',
          key: 'name',
          disabled: true
        },
        {
          type: 'date',
          label: '离职时间：',
          placeholder: '选择时间',
          key: 'quit_time',
          format: 'yyyy-MM-dd'
        },
        {
          type: 'input',
          label: '离职原因：',
          placeholder: '请输入离职原因',
          key: 'info'
        },
        {
          type: 'textarea',
          label: '离职备注：',
          placeholder: '请输入备注',
          key: 'mark'
        },
        {
          type: 'user_id',
          label: '交接人员：',
          placeholder: '请选择人员',
          only_one: true,
          key: 'user_id',
          tips: '提示：办理离职后，系统将自动停用离职人员的账号，离职人员所有待办事项转移交接人员处理。'
        }
      ],
      copyName: '',
      url: '',
      tabtypes: 0,
      dataType: 0,
      joinType: '',
      linkTitle: '邀请个人完善档案信息',
      linkTable: '复制邀请链接，粘贴至微信/QQ等，快速邀请员工加入并完善个人档案！',
      linkContent:
        '提示：被邀请人员登录《陀螺匠·企业助手》，同意加入企业之后，填写「个人简历」之后会同步至员工档案中。',
      quit: {},
      userList: [],
      actionType: '',
      fromData: {
        width: '600px',
        title: '办理离职',
        btnText: '确定',
        labelWidth: '90px',
        type: ''
      },
      formRules: {
        quit_time: [{ required: true, message: '请选择离职时间', trigger: 'blur' }],
        info: [
          {
            required: true,
            message: '请输入离职原因',
            trigger: 'blur'
          }
        ],
        user_id: [
          {
            required: true,
            message: '请选择交接人员',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    setTabTypes(tab = 0) {
      localStorage.setItem('tabTypes', tab.toString())
      this.tabtypes = tab
    },

    // 删除: 单个
    onDelete(data) {
      this.$modalSure(this.$t('business.message10')).then(() => {
        deleteCard(data.id).then((res) => {
          this.$refs.dynamic.getList()
        })
      })
    },

    // 刷新档案列表数据
    getList() {
      this.$refs.dynamic.getList(1)
    },

    // 点击转正
    onWorker(data) {
      this.$modalForm(formalCard(data.id)).then(({ message }) => {
        this.getList()
      })
    },

    // 打开离职
    onQuit(data) {
      this.$refs.oaDialog.openBox()
      this.formDataInit.name = data.name
      this.formDataInit.id = data.id
    },

    submit(data) {
      data.user_id = data.user_id[0]
      quitCard(data.id, data)
        .then((res) => {
          this.$refs.oaDialog.handleClose()
          this.$refs.dynamic.getList()
        })
        .catch((err) => {
          this.isLoading = false
        })
    },

    // 导出上传模板
    async exportTemplate() {
      const result = await getTemp()
      this.fileLinkDownLoad(result.data.url, '员工档案导入模板.xlsx')
    },

    // 选择交接人员关闭
    departmentClose() {
      this.openStatus = false
    },

    // 选择成员完成回调
    changeMastart(data) {
      this.userList = data
    },
    

    // 打开邀请链接弹窗
    async invitationFn(data, type) {
      let dataId
      this.joinType = type
      if (data !== 1) {
        this.copyName = data.name
        dataId = data.id
      } else {
        this.dataType = data
        dataId = 0
      }
      if (this.tabtypes == 0) {
        const result = await getInterview()
        this.linkShow = true
        this.url = result.data.url.url
      } else {
        const result = await perfectCard(dataId)
        if (result.data.url.url == '') {
        } else {
          this.linkShow = true
          this.url = result.data.url.url
        }
      }

      if (this.tabtypes === 0) {
        this.linkTitle = '邀请填写求职登记表'
        this.linkTable = '复制邀请链接，粘贴至微信/QQ等，快速邀请求职者填写求职登记表！'
        this.linkContent =
          '被邀请人员点击邀请链接，通过手机号码登录《陀螺匠·企业助手》，填写个人求职信息之后会同步至员工档案-未入职人员数据中。'
      } else if (this.tabtypes === 1 && data === 1) {
        this.linkTitle = '邀请个人完善档案信息'
        this.linkTable = '复制邀请链接，粘贴至微信/QQ等，快速邀请员工加入并完善个人档案！'
        this.linkContent =
          '被邀请人员登录《陀螺匠·企业助手》，同意加入企业之后，填写「个人简历」之后会同步至员工档案中。'
      }

      let id = 0
      if (data !== 1) {
        id = data.id
        this.linkTitle = '邀请成员'
        this.linkTable = '复制邀请链接，粘贴至微信/QQ等，快速邀请成员加入！'
        if (type == 'invite') {
          this.linkContent = '被邀请员工点击邀请链接，通过手机号码登录陀螺匠办公系统，同意后直接加入组织架构。'
        } else {
          this.linkContent =
            '被邀请员工点击邀请链接，通过手机号码登录陀螺匠办公系统，填写「个人简历」之后会同步至员工档案中。'
        }
      } else {
        id = 0
      }
    },

    // 复制邀请链接
    copy() {
      const oInput = document.createElement('input')
      const name = JSON.parse(localStorage.getItem('enterprise'))
      let value = ''

      if (this.tabtypes == 0) {
        value =
          '【' +
          name.enterprise_name +
          '】邀请您填写求职登记表' +
          ',' +
          '请您点击链接使用手机号验证登录' +
          this.url +
          ' 感谢您的配合，我们期待您的加入！'
      } else if (this.tabtypes === 1 && this.dataType === 1) {
        let realName = JSON.parse(localStorage.getItem('userInfo'))
        value =
          '【' +
          realName.real_name +
          '】邀请您加入' +
          '【' +
          name.enterprise_name +
          '】' +
          '自动化办公系统，请您点击链接使用手机号验证码登录，同意加入企业并完善个人档案' +
          this.url +
          ' 感谢您的配合，谢谢！'
      } else if (this.tabtypes === 1 && this.joinType === 'invite') {
        value =
          '【' +
          this.copyName +
          '】邀请您加入' +
          '【' +
          name.enterprise_name +
          '】' +
          '自动化办公系统，请您点击链接使用手机号验证码登录，同意加入企业并完善个人档案' +
          this.url +
          ' 感谢您的配合，谢谢！'
      } else if (this.tabtypes === 1 && this.dataType !== 1) {
        value =
          '亲爱的【' +
          this.copyName +
          '】请您完善员工档案信息' +
          ',' +
          '请点击链接使用手机号验证登录' +
          this.url +
          ' 在「个人简历」中完善信息之后，发送给【' +
          name.enterprise_name +
          '】'
      }

      oInput.value = value
      document.body.appendChild(oInput)
      oInput.select()
      document.execCommand('Copy')
      oInput.style.display = 'none'
      document.body.removeChild(oInput)
      this.$message.success('复制成功')
      this.handleClose()
    },

    // 关闭邀请链接弹窗
    handleClose() {
      this.linkShow = false
    },

    openDrawer(componentName, type, data, text) {
      switch (componentName) {
        // 人员详情
        case 'addDrawerIsShow': {
          if (type === 'edit') {
            this.$refs.userDetails.id = data.id
            this.actionType = text
          }
          this.$refs.userDetails.init({ componentName, type })
          break
        }
      }
    }
  },

  components: {
    Table: () => import('./components/table.vue'),
    // 在职员工
    jobEmployees: () => import('./components/index/employees.vue'),
    // 未入职员工
    notNewEmployees: () => import('./components/index/notEmployees.vue'),
    // 离职员工
    resignedEmployee: () => import('./components/index/quitEmployees.vue'),
    // 档案信息弹窗
    UserDetails: () => import('./components/userDetails.vue'),

    oaDialog: () => import('@/components/form-common/dialog-form'),
    // 导入
    importExcel: () => import('@/components/common/importExcel')
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-form-item__content {
  flex: 1;
}
::v-deep .input-with-select .el-input-group__prepend {
  background-color: #fff;
}
.tips {
  display: inline-block;
  line-height: 24px;
}

.header {
  display: flex;
  .header-tab {
    margin-left: 700px;
  }
}

.btn-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.table-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 22px;
  border-radius: 3px;
  font-size: 13px;

  &.blue {
    background: rgba(24, 144, 255, 0.05);
    border: 1px solid #1890ff;
    color: #1890ff;
  }

  &.yellow {
    background: rgba(255, 153, 0, 0.05);
    border: 1px solid #ff9900;
    color: #ff9900;
  }

  &.green {
    background: rgba(0, 192, 80, 0.05);
    border: 1px solid #00c050;
    color: #00c050;
  }

  &.gray {
    background: rgba(153, 153, 153, 0.05);
    border: 1px solid #999999;
    color: #999999;
  }
}

.table-txt {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  font-size: 13px;

  i {
    width: 4px;
    height: 4px;
    margin-right: 5px;
    border-radius: 50%;
  }
}
.page-header {
  margin: 10px 0 15px 0;
}
.tips {
  margin-top: 20px;
  font-size: 11px;
  color: #666;
}
::v-deep .el-page-header__title {
  font-size: 13px;
}
::v-deep .el-page-header__content {
  font-size: 17px;
  font-weight: 500;
}
::v-deep.el-dialog__body {
  padding-bottom: 0;
}
</style>
