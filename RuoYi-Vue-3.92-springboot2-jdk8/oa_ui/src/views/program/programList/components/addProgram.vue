<!-- 新建项目侧滑弹窗  -->
<template>
  <div :class="id > 0 ? 'stationBox' : ''">
    <div class="title flex-between" v-if="id > 0">
      <div class="ml30">基本信息</div>
      <div v-if="drawerType !== 'info'">
        <el-button size="small" @click="drawerEdit('info')">{{ $t('public.cancel') }}</el-button>
        <el-button size="small" type="primary" @click="handleConfirm('ruleForm')">{{ $t('public.ok') }}</el-button>
      </div>
      <div class="editText" @click="drawerEdit('edit')" v-if="drawerType == 'info' && formInfo.operate">
        <i class="el-icon-edit"></i>编辑
      </div>
    </div>
    <div class="mr14" :class="id == 0 ? 'mt20' : ''">
      <el-form
        v-if="drawerType !== 'info'"
        ref="formData"
        :model="formData"
        label-width="110px"
        :rules="rule"
        @submit.native.prevent
      >
        <el-form-item label="项目名称：" prop="name">
          <el-input v-model="formData.name" size="small" class="input-item" placeholder="请输入项目名称" />
        </el-form-item>

        <el-form-item label="负责人：" prop="uid" class="select-bar">
          <select-member
            :only-one="true"
            :value="principal || []"
            :placeholder="`请选择负责人`"
            @getSelectList="getSelectList($event, 1)"
            style="width: 100%"
          ></select-member>
        </el-form-item>

        <el-form-item label="项目成员：" class="select-bar">
          <select-member
            :value="userList || []"
            :placeholder="`请选择成员`"
            @getSelectList="getSelectList($event, 2)"
            style="width: 100%"
          ></select-member>
        </el-form-item>

        <el-form-item label="计划开始：" class="select-bar" prop="start_date">
          <el-date-picker
            v-model="formData.start_date"
            size="small"
            type="date"
            clearable
            :format="'yyyy-MM-dd'"
            :value-format="'yyyy-MM-dd'"
            placeholder="请选择项目计划开始日期"
          ></el-date-picker>
        </el-form-item>

        <el-form-item label="计划结束：" class="select-bar" prop="end_date">
          <el-date-picker
            v-model="formData.end_date"
            size="small"
            type="date"
            clearable
            :format="'yyyy-MM-dd'"
            :value-format="'yyyy-MM-dd'"
            placeholder="请选择项目计划结束日期"
          ></el-date-picker>
        </el-form-item>

        <el-form-item label="关联客户：" prop="eid">
          <el-select
            v-model="formData.eid"
            size="small"
            clearable
            filterable
            placeholder="请选择关联客户"
            @change="handleContract"
            class="custom-select"
          >
            <el-option v-for="item in customerList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="formData.eid" label="关联合同：" prop="cid">
          <el-select
            class="custom-select"
            v-model="formData.cid"
            size="small"
            clearable
            filterable
            placeholder="请选择关联合同"
          >
            <el-option v-for="item in contractList" :key="item.id" :label="item.title" :value="item.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="项目状态：" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="0">正常</el-radio>
            <el-radio :label="1">暂停</el-radio>
            <el-radio :label="2">关闭</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="项目描述：">
          <el-input
            v-model="formData.describe"
            type="textarea"
            maxlength="1000"
            show-word-limit
            :rows="3"
            placeholder="请输入项目描述，最多可输入1000字"
          />
        </el-form-item>
      </el-form>
    </div>
    <div v-if="id == 0" class="button from-foot-btn fix btn-shadow">
      <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
      <el-button size="small" type="primary" :loading="loading" @click="handleConfirm('ruleForm')">{{
        $t('public.ok')
      }}</el-button>
    </div>
    <details-program v-if="drawerType == 'info'" ref="detailsProgram" :formData="formData" />
    <div class="delText mb20" v-if="id > 0 && formInfo.operate">
      <el-button plain class="delBtn ml30" size="small" @click="handleDelete">删除项目</el-button
      >删除项目后将删除所有工作项，且数据无法找回，请谨慎操作
    </div>
  </div>
</template>

<script>
import { saveProgramApi, putProgramApi, getProgramInfoApi } from '@/api/program'
import debounce from '@form-create/utils/lib/debounce'
import { customerSelectApi, selectContractListApi } from '@/api/enterprise'
import { deleteProgramApi } from '@/api/program'
export default {
  name: 'addProgram',
  components: {
    selectMember: () => import('@/components/form-common/select-member'),
    detailsProgram: () => import('../components/detailsProgram')
  },
  props: {
    type: {
      type: String,
      default: 'info'
    },
    customer: {
      type: Array,
      default: () => []
    },
    formInfo: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      drawer: false,
      loading: false,
      id: 0,

      drawerType: this.type,
      contractList: [],
      customerList: [],
      userList: [],
      principal: [],
      departType: 0,
      memberShow: false,
      onlyOne: false,
      edit: false,
      formData: {
        status: 0,
        cid: 0
      },
      detailsData: {},
      rule: {
        name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
        uid: [{ required: true, message: '请选择负责人', trigger: 'blur' }],
        end_date: [
          {
            required: false,
            message: '请选择项目计划结束日期',
            trigger: 'blur'
          },
          {
            validator: this.checkEndDate,
            trigger: 'change' // 当结束日期变化时触发校验
          }
        ]
      }
    }
  },

  mounted() {
    if (this.formInfo && this.formInfo.id) {
      this.formData = this.formInfo
      this.getCustomer()
      if (this.formData.eid) {
        this.getContract(this.formData.eid)
      }
      this.userList = this.formData.members
      this.id = this.formData.id
      this.principal = this.formData.admins
      this.formData.eid = this.formData.eid != 0 ? this.formData.eid : ''
      this.formData.cid = this.formData.cid != 0 ? this.formData.cid : ''
    } else {
      this.customerList = this.customer
    }
  },
  methods: {
    checkEndDate(rule, value, callback) {
      if (value && this.formData.start_date) {
        if (value < this.formData.start_date) {
          callback(new Error('结束日期不能早于开始日期'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    },

    drawerEdit(type) {
      this.drawerType = type
    },
    // 获取客户数据
    async getCustomer() {
      const result = await customerSelectApi()
      this.customerList = result.data
    },

    // 获取合同数据
    async getContract(eid) {
      const result = await selectContractListApi({ data: eid })
      this.contractList = result.data
    },

    // 删除项目
    handleDelete() {
      this.$modalSure('确定删除此项目').then(() => {
        deleteProgramApi(this.id).then((res) => {
          if (res.status == 200) {
            this.$emit('goBack')
          }
        })
      })
    },

    // 选择客户
    handleContract(eid) {
      this.formData.cid = ''
      this.getContract(eid)
      // this.$emit('getContractList', eid)
    },

    // 选择成员回调
    getSelectList(data, type) {
      if (type === 1) {
        this.formData.uid = data[0].value
        this.principal = data
        this.formData.admins = data
      } else if (type === 2) {
        var checkUid = []
        data.forEach((item) => {
          checkUid.push(item.value)
        })
        this.formData.members = checkUid
        this.userList = data
      }
    },
    // 获取项目详情
    async getProgramInfo(id) {
      const result = await getProgramInfoApi(id)
      this.formData = result.data
      this.detailsData = result.data
      this.userList = result.data.members
      this.principal = result.data.admins
      this.formData.eid = result.data.eid != 0 ? result.data.eid : ''
      this.formData.cid = result.data.cid != 0 ? result.data.cid : ''

      this.$emit('getContractList', this.formData.eid)
    },
    // 删除标签
    cardTag(type, index) {
      if (type === 1) {
        this.principal.splice(index, 1)
        this.formData.uid = ''
      } else {
        this.userList.splice(index, 1)
        this.formData.members.splice(index, 1)
      }
    },
    handleClose() {
      this.reset()
      if (this.drawerType !== 'info') {
        this.$refs.formData.resetFields()
      }
      this.$emit('handleClose')
    },
    openBox(id, type) {
      this.drawerType = type
      this.drawer = true
      this.id = id ? id : 0
      if (this.drawerType) {
        // this.getProgramInfo(id)
      }
    },
    reset() {
      this.formData = {
        name: '',
        uid: '',
        eid: '',
        cid: '',
        members: [],
        admins: [],
        contract: {},
        customer: {},
        start_date: '',
        end_date: '',
        status: 0,
        describe: ''
      }
      // this.edit = false
      this.userList = []
      this.principal = []
    },
    // 提交
    handleConfirm: debounce(function () {
      this.$refs.formData.validate((valid) => {
        if (valid) {
          this.loading = true
          if (this.drawerType == 'edit' && this.id > 0) {
            let members = []
            members = this.userList.map((item) => {
              return item.value ? item.value : item.id
            })
            this.formData.members = members
            if (this.formData.name.trim()) {
              putProgramApi(this.id, this.formData).then((res) => {
                if (res.status == '200') {
                  this.drawerType = 'info'
                  this.getProgramInfo(this.id)
                }
                this.loading = false
              })
            } else {
              this.$message('项目名称不能为空')
            }
          } else {
            if (this.formData.name.trim()) {
              saveProgramApi(this.formData).then((res) => {
                if (res.status == 200) {
                  this.reset()
                  this.formData = {}
                  this.$emit('getTableData', {}, 1)
                  this.$refs.formData.clearValidate()
                }
                this.loading = false
              })
            } else {
              this.$message('项目名称不能为空')
            }
          }
        }
      })
    }, 500),
    async addRow() {
      this.drawerType = ''
      this.reset()
    },
    async editRow() {
      this.drawerType = 'edit'
    },
    async deleteRow() {
      let row = {
        id: this.id
      }
      this.$emit('handleDelete', row)
    }
  }
}
</script>

<style lang="scss" scoped>
.stationBox {
  width: 600px;
  margin: 0 auto;
  margin-top: 30px;
  padding-bottom: 20px;
}
.stationBox ::v-deep.edui-editor-iframeholder {
  height: 300px !important;
}
.mb-10 {
  margin-bottom: -10px;
}
.ml30 {
  margin-left: 30px;
}
.el-tag--small {
  line-height: 24px;
}
.delText {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 12px;
  color: #909399;

  ::v-deep .el-button--small {
    color: #ed4014;
  }

  ::v-deep .el-button {
    border-color: #ed4014;
  }
}

.avatar-box {
  display: inline-block;
  height: 32px;
  background: #f7f7f7;
  border-radius: 4px;
  padding: 0 10px;
  margin-bottom: 10px;
  margin-right: 12px;
  line-height: 32px;
  .el-icon-error {
    cursor: pointer;
    color: #c0c4cc;
  }

  .name {
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 13px;
    color: #303133;
    line-height: 18px;
    margin-bottom: 5px;
  }
  img {
    width: 16px;
    height: 16px;
    border-radius: 50%;
  }
}

.editText {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #1890ff;
  cursor: pointer;
}
.el-icon-edit {
  margin-right: 4px;
}
.delBtn {
  margin-right: 12px;
  ::v-deep .el-button--small {
    color: #ed4014;
  }

  ::v-deep .el-button {
    border-color: #ed4014;
  }
}

.title {
  font-family: PingFang SC, PingFang SC;
  font-weight: 500;
  font-size: 18px;
  color: #303133;
  margin-bottom: 20px;
}
::v-deep.el-cascader,
::v-deep .el-input-number,
::v-deep .el-select,
::v-deep .el-date-editor {
  width: 100%;
}
::v-deep.el-input__inner {
  height: 32px;
  line-height: 32px;
}

.fz30 {
  font-size: 30px;
  margin-left: 10px;
  color: #909399;
}
.title-btn {
  display: flex;
  align-items: center;
  .icongengduo2 {
    font-weight: 400;
    margin: 0 10px;
  }
}
::v-deep .custom-select .el-icon-circle-close {
  transform: translateY(0);
}
.flex-between {
  height: 32px;
}
.addPeople {
  display: inline-block;
  margin-right: 8px;
  cursor: pointer;
  width: 66px;
  height: 32px;
  background-color: rgba(24, 144, 255, 0.08);
  font-size: 13px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #1890ff;
  text-align: center;
  line-height: 32px;
  border-radius: 4px;
  .icontianjia {
    font-size: 12px;
    margin-right: 4px;
  }
}
</style>
