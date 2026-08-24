<template>
  <div>
    <el-dialog :visible.sync="show" width="650px" :show-close="false" :close-on-click-modal="false">
      <div slot="title" class="flex-between">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>
            <span class="title" @click="backFn">{{
              supplierVal ? supplierVal.label : '选择供应商'
            }}</span></el-breadcrumb-item
          >
          <el-breadcrumb-item><span class="title2">模型设置 </span></el-breadcrumb-item>
        </el-breadcrumb>
        <span class="el-icon-close" @click="handleClose"></span>
      </div>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="auto" label-position="top">
        <el-form-item prop="name">
          <span slot="label" class="flex"
            >模型名称 <popover style="display: inline-block" :tips="`系统中自定义的模型名称`" :width="250"></popover
          ></span>
          <el-input
            v-model="ruleForm.name"
            maxlength="60"
            size="small"
            show-word-limit
            placeholder="系统中自定义的模型名称"
          ></el-input>
        </el-form-item>
        <el-form-item prop="models_type">
          <span slot="label" class="flex"
            >模型类型 <popover style="display: inline-block" :tips="`在应用中与AI对话的推理模型`" :width="250"></popover
          ></span>

          <el-select
            v-model="ruleForm.models_type"
            filterable
            clearable
            placeholder="请选择模型类型"
            size="small"
            style="width: 100%"
            @change="modelsTypeChange"
          >
            <el-option v-for="item in modelsOptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="模型" prop="is_model">
          <el-select
            v-model="ruleForm.is_model"
            allow-create
            filterable
            clearable
            placeholder="列表中未列出的模型，直接输入模型名称，回车即可添加"
            size="small"
            style="width: 100%"
          >
            <el-option v-for="item in isModelOptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="API Key" prop="key" v-if="ruleForm.models_type">
          <el-input
            v-model="ruleForm.key"
            size="small"
            limit-word-show
            :type="[flag ? 'text' : 'password']"
            placeholder="请输入API Key"
          >
            <i
              slot="suffix"
              :class="[flag ? 'iconfont iconchakan pointer' : ' iconfont iconyincang  pointer']"
              style="margin-top: 8px; font-size: 18px"
              autocomplete="auto"
              @click="flag = !flag"
            />
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="submitFn">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { getModelsSelectApi, saveModelsApi, getModelsInfoApi, editModelsApi } from '@/api/chatAi'
import popover from './popover'

export default {
  name: '',
  components: { popover },
  props: {
    optionList: {
      type: Array,
      default: () => []
    },
    supplierVal: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      isModelOptions: [],
      id: '',
      show: false,
      flag: false,
      ruleForm: { name: '', is_model: '', models_type: '', key: '', json: {}, provider: '' },
      options: [],
      modelsOptions: [],
      rules: {
        name: [{ required: true, message: '请输入模型名称', trigger: 'blur' }],
        models_type: [{ required: true, message: '请选择模型类型', trigger: 'change' }],
        is_model: [{ required: true, message: '请选择基础模型', trigger: 'change' }],
        key: [{ required: true, message: '请输入key', trigger: 'blur' }]
      }
    }
  },
  watch: {
    supplierVal(val) {
      this.getoptions(val.value)
    }
  },

  methods: {
    modelsTypeChange(e) {
      this.modelsOptions.map((item) => {
        if (e === item.value) {
          this.isModelOptions = item.children
        }
      })
    },
    backFn() {
      if (this.id) {
        return false
      }
      this.$emit('openSupplierDialog', this.supplierVal, 'back')
    },
    getoptions(val) {
      getModelsSelectApi({ type: val }).then((res) => {
        this.modelsOptions = res.data
      })
    },
    handleClose() {
      this.show = false
      this.info = {}
      this.ruleForm.is_model = ''
      this.flag = false
      this.$refs['ruleForm'].resetFields()
      this.ruleForm.key = ''
      this.id = ''
    },
    openBox(item) {
      if (item && item.id) {
        this.getInfo(item.id)
        this.id = item.id
      } else {
        this.supplierVal = item
        this.ruleForm.json = item
        this.ruleForm.provider = item.value
      }

      this.show = true
    },

    getInfo(id) {
      getModelsInfoApi(id).then((res) => {
        this.info = res.data
        this.ruleForm.json = this.info.json

        for (let key in this.ruleForm) {
          this.ruleForm[key] = this.info[key]
        }
      })
    },
    submitFn() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          if (this.info && this.info.id) {
            editModelsApi(this.info.id, this.ruleForm).then((res) => {
              if (res.status == '200') {
                this.handleClose()
                this.$refs.ruleForm.resetFields()
                this.$emit('isOk')
              }
            })
          } else {
            saveModelsApi(this.ruleForm).then((res) => {
              if (res.status == '200') {
                this.handleClose()
                this.$refs.ruleForm.resetFields()
                this.$emit('isOk')
              }
            })
          }
        }
      })
    }
  }
}
</script>
<style scoped lang="scss">
::v-deep .el-form-item__label {
  line-height: 13px;
  display: flex;
}
::v-deep .el-dialog__footer {
  padding-top: 0;
}
.title {
  cursor: pointer;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 14px;
  color: #909399;
}
.title2 {
  cursor: pointer;
  font-family: PingFang SC, PingFang SC;
  font-weight: 500;
  font-size: 14px;
  color: #303133;
}
.el-icon-close {
  color: #c0c4cc;
  font-size: 15px;
}
</style>
