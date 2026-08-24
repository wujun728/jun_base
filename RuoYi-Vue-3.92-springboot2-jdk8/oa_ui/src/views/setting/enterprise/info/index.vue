<template>
  <div class="divBox">
    <div class="content-con">
      <el-card class="employees-card-bottom">
        <el-row :gutter="30">
          <el-col :span="14" :offset="5">
            <div class="intro-box acea-row row-middle">
              <el-upload
                class="upload-box"
                action="##"
                :show-file-list="false"
                :headers="myHeaders"
                :http-request="uploadServerLog"
              >
                <img :src="ruleForm.logo" alt="" class="img" />
                <div class="btn acea-row row-center-wrapper">
                  <i class="iconfont iconbianji3"></i>
                </div>
              </el-upload>
              <div>
                <div class="name">{{ enterprise_name }}</div>
                <div>{{ cityVals[0] }}{{ cityVals[1] }}{{ cityVals[2] }}{{ address }}</div>
              </div>
            </div>
            <div class="panel-box">
              <div class="panel-title acea-row row-middle row-between">
                <div class="title">基本信息</div>
                <div>
                  <el-button v-if="!isEdit" type="text" icon="el-icon-edit" @click="onEdit">编辑</el-button>
                  <el-button v-if="isEdit" @click="onCancel">取消</el-button>
                  <el-button v-if="isEdit" type="primary" @click="handleSubmit">保存</el-button>
                </div>
              </div>
              <div class="panel-content">
                <el-form ref="ruleForm" :model="ruleForm" :rules="rules">
                  <el-row :gutter="30">
                    <el-col :span="12">
                      <el-form-item label="企业名称" prop="enterprise_name">
                        <el-input
                          v-show="isEdit"
                          v-model="ruleForm.enterprise_name"
                          :placeholder="$t('setting.info.title1')"
                          clearable
                        ></el-input>
                        <div v-show="!isEdit" class="value">{{ ruleForm.enterprise_name }}</div>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="企业简称" prop="short_name">
                        <el-input
                          v-show="isEdit"
                          v-model="ruleForm.short_name"
                          :maxlength="6"
                          placeholder="请填写企业简称"
                          show-word-limit
                          clearable
                        ></el-input>
                        <div v-show="!isEdit" class="value">{{ ruleForm.short_name }}</div>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row :gutter="30">
                    <el-col :span="12">
                      <el-form-item :label="$t('setting.info.enterprisecity')">
                        <el-cascader
                          v-show="isEdit"
                          v-model="cityVal"
                          :options="cityData"
                          :props="{ value: 'label', label: 'label' }"
                          clearable
                          style="width: 100%"
                        ></el-cascader>
                        <div v-show="!isEdit" class="value">{{ cityVal[0] }} - {{ cityVal[1] }} - {{ cityVal[2] }}</div>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item :label="$t('setting.info.businessaddress')" prop="address">
                        <el-input
                          v-show="isEdit"
                          v-model="ruleForm.address"
                          :placeholder="$t('setting.info.title2')"
                          clearable
                        ></el-input>
                        <div v-show="!isEdit" class="value">{{ ruleForm.address }}</div>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row :gutter="30">
                    <el-col :span="12">
                      <el-form-item :label="$t('setting.info.contactnumber')" prop="phone">
                        <el-input
                          v-show="isEdit"
                          v-model="ruleForm.phone"
                          :placeholder="$t('setting.info.title3')"
                          clearable
                          readonly
                        ></el-input>
                        <div v-show="!isEdit" class="value">{{ ruleForm.phone }}</div>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </el-form>
              </div>
            </div>
            <div class="panel-box" :class="{ gray: isEdit }">
              <div class="panel-title acea-row row-middle row-between">
                <div class="title">企业信息</div>
              </div>
              <div class="panel-content">
                <el-form ref="ruleForm" :model="ruleForm">
                  <el-row :gutter="30">
                    <el-col :span="12">
                      <el-form-item label="企业成员">
                        <div class="value">{{ pageData.enterprises }}个成员</div>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="企业部门">
                        <div class="value">{{ pageData.frames }}个部门</div>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row :gutter="30">
                    <el-col :span="12">
                      <el-form-item label="创建人">
                        <div class="value">{{ pageData.lead }}</div>
                      </el-form-item>
                    </el-col>

                    <el-col :span="12">
                      <el-form-item label="企业状态">
                        <div class="value">
                          <span v-if="pageData.status == 0" class="txt">{{ $t('setting.info.disabled') }}</span>
                          <span v-if="pageData.status == 1" class="txt">{{ $t('setting.info.normal') }}</span>
                          <span v-if="pageData.status == 2" class="txt">{{ $t('setting.info.behalfpayment') }}</span>
                          <span v-if="pageData.status == 3" class="txt">{{ $t('setting.info.expired') }}</span>
                        </div>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </el-form>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getCityListApi } from '@/api/public'
import { mapGetters } from 'vuex'
import { enterpriseEntInfoApi, entInfoUpdateApi } from '@/api/enterprise'
import { getToken } from '@/utils/auth'
import { uploader } from '@/utils/uploadCloud'
import { edit } from 'ace-builds'

export default {
  name: 'Index',
  filters: {
    filterStatus(val) {
      const obj = {
        0: '禁用',
        1: '正常',
        2: '待缴费',
        3: '已过期'
      }
      return obj[val]
    }
  },
  props: {
    tabCur: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      ruleForm: {
        logo: '',
        enterprise_name: '',
        short_name: '',
        province: '',
        city: '',
        area: '',
        address: '',
        phone: '',
        lead: '',
        business_license: ''
      },
      rules: {
        enterprise_name: [{ required: true, message: this.$t('setting.info.title1'), trigger: 'blur' }],
        short_name: [{ required: true, message: '请填写企业简称', trigger: 'blur' }],
        address: [{ required: true, message: this.$t('setting.info.title2'), trigger: 'blur' }],
        phone: [{ required: true, message: this.$t('setting.info.title3'), trigger: 'blur' }]
      },
      pageData: {},
      upLoadData: {},
      cityData: [],
      cityVal: [],
      cityVals: [],
      enterprise_name: '',
      address: '',
      myHeaders: {
        authorization: 'Bearer ' + getToken()
      },
      uploadSize: 2,
      loading: false,
      isEdit: false
    }
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  created() {
    this.getCitylist()
    this.getData()
  },
  methods: {
    // 获取城市列表
    getCitylist() {
      getCityListApi().then((res) => {
        this.cityData = res.data
      })
    },
    handleClick(number) {
      this.tabCur = number.index
    },

    // 上传文件方法
    uploadServerLog(params) {
      const file = params.file
      let options = {
        way: 2,
        relation_type: '',
        relation_id: 0,
        eid: 0
      }
      uploader(file, 1, options)
        .then((res) => {
          // 获取上传文件渲染页面
          if (res.data.name) {
            this.ruleForm.logo = res.data.url
            this.handleSubmit()
          }
        })
        .catch((err) => {})
    },

    handleRemove() {},
    handleExceed() {},
    // 获取数据
    getData() {
      enterpriseEntInfoApi().then(({ data }) => {
        this.pageData = data
        this.cityVal = [data.province, data.city, data.area]
        this.cityVals = [data.province, data.city, data.area]
        this.enterprise_name = data.enterprise_name
        this.address = data.address
        this.ruleForm = {
          logo: data.logo,
          enterprise_name: data.enterprise_name,
          short_name: data.short_name,
          province: data.province,
          city: data.city,
          area: data.area,
          address: data.address,
          phone: data.phone,
          lead: data.lead,
          business_license: data.business_license
        }
      })
    },
    handleSubmit() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          this.ruleForm.province = this.cityVal[0]
          this.ruleForm.city = this.cityVal[1]
          this.ruleForm.area = this.cityVal[2]
          this.loading = true
          entInfoUpdateApi(this.ruleForm)
            .then((res) => {
              if (res.status == 200) {
                this.loading = false
                this.isEdit = false
                this.getCitylist()
                this.getData()
              }
            })
            .catch((error) => {
              this.loading = false
            })
        }
      })
    },
    onEdit() {
      this.isEdit = true
    },
    onCancel() {
      this.isEdit = false
    }
  }
}
</script>

<style lang="scss" scoped>
.content-con {
  height: 100%;
  position: relative;
}
.content {
  .item {
    padding: 10px;
    display: flex;
    line-height: 2;
    font-size: 13px;
    .label {
      display: inline-block;
      width: 148px;
      text-align: right;
    }
    .txt {
      flex: 1;
      margin-left: 15px;
      color: rgba(0, 0, 0, 0.65);
    }
    .name {
      margin-left: 15px;
      color: rgba(0, 0, 0, 0.65);
    }
    .btn {
      margin-left: 13px;
      color: #f5222d;
      cursor: pointer;
    }
  }
}
::v-deep .el-upload--picture-card {
  border: none;
  background-color: #e4e7ed;
}
.form-height {
  height: calc(100vh - 200px);
}
.img-box {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 148px;
  height: 148px;
  img {
    width: 148px;
    height: 148px;
    // max-width: 100%;
    // height: 100%;
  }
}
.upload-demo {
  width: 148px;
  height: 148px;
  box-shadow: 0 0 3px 3px #e1e3e7;
  text-align: center;
  line-height: 148px;
  .el-icon-plus {
    font-size: 18px;
    color: #909399;
  }
}
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
.intro-box {
  padding: 20px;
  border-radius: 6px;
  margin-bottom: 32px;
  background: rgba(24, 144, 255, 0.05);
  font-size: 14px;
  line-height: 20px;
  color: #606266;
  &:hover .btn {
    display: flex;
  }

  .upload-box {
    position: relative;
    width: 66px;
    height: 66px;
    margin-right: 14px;
  }

  .img {
    width: 66px;
    height: 66px;
    border-radius: 9px;
  }

  .btn {
    display: none;
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    border-radius: 9px;
    background: rgba(43, 42, 42, 0.4);
  }

  .iconfont {
    font-size: 22px;
    color: #ffffff;
  }

  .name {
    margin-bottom: 11px;
    font-weight: 600;
    font-size: 15px;
    line-height: 21px;
    color: #333333;
  }
}
.panel-box {
  margin-bottom: 10px;
  .panel-title {
    height: 26px;
    margin-bottom: 30px;
    .title {
      padding-left: 10px;
      border-left: 2px solid #1890ff;
      font-weight: 600;
      font-size: 15px;
      line-height: 15px;
      color: #333333;
    }
    .el-button {
      width: 46px;
      height: 26px;
      padding: 0;
      font-size: 13px;
    }
  }
  .panel-content {
    .value {
      position: relative;
      display: inline-block;
      width: 100%;
      font-size: 14px;
    }
  }
  &.gray {
    .panel-content {
      .value {
        padding-left: 12px;
        background: #f7f7f7;
      }
    }
  }
}
::v-deep.el-form-item--medium .el-form-item__label {
  margin-bottom: 12px;
  line-height: 13px;
}
</style>
