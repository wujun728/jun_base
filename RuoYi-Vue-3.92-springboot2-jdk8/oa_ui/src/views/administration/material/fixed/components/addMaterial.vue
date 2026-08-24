<!-- 新增入库弹窗 -->
<template>
  <div class="station">
    <el-drawer
      :title="formData.title"
      :visible.sync="drawer"
      :direction="direction"
      :modal="true"
      :before-close="handleClose"
      :append-to-body="true"
      :size="formData.width"
      :wrapperClosable="false"
    >
      <div class="invoice">
        <el-form ref="form" :model="rules" label-width="100px" :rules="rule">
          <div class="from-item-title mb15">
            <span>物资信息</span>
          </div>
          <div class="form-box">
            <div class="form-item">
              <el-form-item prop="name">
                <span slot="label">物资名称:</span>
                <el-select
                  v-model="rules.name"
                  v-if="!formData.edit"
                  allow-create
                  size="small"
                  filterable
                  clearable
                  placeholder="请选择(输入)物资名称"
                  class="countries-select"
                  @change="handleName"
                >
                  <el-option
                    v-for="(item, index) in formData.selectData"
                    :key="index"
                    :label="item.name"
                    :value="index"
                  />
                </el-select>
                <el-input v-else v-model="rules.name" clearable size="small" placeholder="请输入物资名称" />
              </el-form-item>
            </div>
            <div class="form-item">
              <el-form-item prop="cid">
                <span slot="label">物资分类:</span>
                <el-cascader
                  v-model="rules.cid"
                  :options="formData.treeData"
                  size="small"
                  placeholder="请选择物资分类"
                  :props="{ checkStrictly: true }"
                  clearable
                ></el-cascader>
              </el-form-item>
            </div>
            <div class="form-item overflow">
              <el-form-item>
                <span slot="label">规格型号:</span>
                <el-input
                  v-model="rules.units"
                  clearable
                  size="small"
                  :maxlength="20"
                  show-word-limit
                  placeholder="请输入规格型号"
                />
              </el-form-item>
            </div>
            <div class="form-item">
              <el-form-item prop="amount">
                <span slot="label">计量单位:</span>
                <el-input
                  v-model="rules.specs"
                  clearable
                  size="small"
                  :maxlength="8"
                  show-word-limit
                  placeholder="请输入计量单位"
                />
              </el-form-item>
            </div>
            <div class="form-item" v-if="this.formData.edit && formData.type === 1" style="width: 100%">
              <el-form-item prop="price">
                <span slot="label">单价(元):</span>
                <el-input-number
                  v-model="rules.price"
                  :controls="false"
                  :min="0"
                  :precision="2"
                  size="small"
                  placeholder="请输入单价"
                ></el-input-number>
              </el-form-item>
            </div>
            <div class="form-item" style="width: 100%">
              <el-form-item>
                <span slot="label">生产厂家:</span>
                <el-input
                  v-model="rules.factory"
                  :maxlength="50"
                  show-word-limit
                  clearable
                  size="small"
                  placeholder="请输入生产厂家"
                />
              </el-form-item>
            </div>
            <div class="form-item" style="width: 100%">
              <el-form-item>
                <span slot="label">{{ $t('public.remarks') }}:</span>
                <el-input
                  type="textarea"
                  maxlength="200"
                  show-word-limit
                  :rows="3"
                  v-model.trim="rules.mark"
                  :placeholder="$t('customer.placeholder18')"
                />
              </el-form-item>
            </div>
          </div>

          <template v-if="!this.formData.edit">
            <div class="from-item-title mb15">
              <span>入库信息</span>
            </div>
            <div class="form-box">
              <div class="form-item">
                <el-form-item prop="number">
                  <span slot="label">入库数量:</span>
                  <el-input-number
                    v-model="rules.number"
                    :controls="false"
                    :min="0"
                    :max="1000"
                    :precision="0"
                    size="small"
                    placeholder="请输入入库数量"
                  ></el-input-number>
                </el-form-item>
              </div>
              <div class="form-item">
                <el-form-item prop="price">
                  <span slot="label">单价(元):</span>
                  <el-input-number
                    v-model="rules.price"
                    :controls="false"
                    :min="0"
                    :precision="2"
                    size="small"
                    placeholder="请输入单价"
                  ></el-input-number>
                </el-form-item>
              </div>
              <div class="form-item" style="width: 100%">
                <el-form-item>
                  <span slot="label">入库说明:</span>
                  <el-input
                    type="textarea"
                    maxlength="200"
                    show-word-limit
                    :rows="3"
                    v-model.trim="rules.remark"
                    :placeholder="$t('customer.placeholder18')"
                  />
                </el-form-item>
              </div>
            </div>
          </template>
        </el-form>
        <div class="button from-foot-btn fix btn-shadow">
          <el-button @click="handleClose" size="small">{{ $t('public.cancel') }}</el-button>
          <el-button :loading="loading" size="small" type="primary" @click="handleConfirm('ruleForm')">
            {{ $t('public.ok') }}
          </el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { storageListSaveApi } from '@/api/administration'

export default {
  name: 'AddMaterial',
  props: {
    formData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    const checkNumber = (rule, value, callback) => {
      if (!value && !this.formData.edit) {
        return callback(new Error('请输入入库数量'))
      } else {
        callback()
      }
    }
    const checkPrice = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请输入单价'))
      } else {
        callback()
      }
    }
    return {
      drawer: false,
      direction: 'rtl',
      rules: {
        name: '',
        cid: [],
        units: '',
        specs: '',
        factory: '',
        mark: '',
        price: undefined,
        number: undefined,
        remark: '',
        types: null
      },
      loading: false,
      itemData: {},
      rule: {
        name: [{ required: true, message: '请选择(输入)物资名称', trigger: 'change,blur' }],
        cid: [{ required: true, message: '请选择物资分类', trigger: 'change' }],
        number: [{ required: true, validator: checkNumber, trigger: 'blur' }],
        price: [{ required: true, validator: checkPrice, trigger: 'blur' }]
      }
    }
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang
    }
  },
  watch: {
    formData: {
      handler(nVal) {
        if (nVal.edit) {
          this.rules.name = nVal.data.name
          this.rules.cid = nVal.data.cid
          this.rules.units = nVal.data.units
          this.rules.specs = nVal.data.specs
          this.rules.factory = nVal.data.factory
          this.rules.mark = nVal.data.mark
          this.rules.number = 0
          if (nVal.type === 1) {
            this.rules.price = nVal.data.record[0].price
          }
        } else {
          this.countriesSelect()
        }
      },
      deep: true
    }
  },
  methods: {
    handleClose() {
      this.drawer = false
      this.reset()
    },
    openBox() {
      this.drawer = true
    },
    reset() {
      this.rules = {
        name: '',
        cid: '',
        units: '',
        specs: '',
        factory: '',
        mark: '',
        price: undefined,
        number: undefined,
        remark: '',
        types: null
      }
    },
    // 提交
    handleConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.rules.types = this.formData.type
          if (this.formData.edit) {
            if (this.rules.cid.length > 1) {
              this.rules.cid = this.rules.cid[this.rules.cid.length - 1]
            }

            this.rules.id = this.formData.data.id
            this.storageSave(this.rules, true)
          } else {
            // 添加
            if (typeof this.rules.name === 'string') {
              this.rules.cid = this.rules.cid[this.rules.cid.length - 1]
              this.storageSave(this.rules)
            } else {
              this.rules.cid = this.rules.cid[this.rules.cid.length - 1]
              this.rules.name = this.itemData.name
              if (this.getJudgeInfo()) {
                // 无任何变化时修改
                if (this.rules.types !== 1) {
                  this.rules.id = this.itemData.id
                }
                this.storageSave(this.rules)
              } else {
                this.storageSave(this.rules)
              }
            }
          }
        }
      })
    },
    // 保存物资
    storageSave(data, type = false) {
      this.loading = true
      storageListSaveApi(data)
        .then((res) => {
          this.handleClose()
          this.$emit('isOk')
          this.reset()
          this.loading = false
          let message = ''
          if (type) {
            message = '修改成功'
          } else {
            if (this.formData.type === 1) {
              message = ''
              if (res.data) {
                const len = res.data.length
                if (len > 1) {
                  message = '编号为 ' + res.data[0] + ' -- ' + res.data[len - 1] + ' 的物资添加成功'
                } else {
                  message = '编号为 ' + res.data[0] + ' 的物资添加成功'
                }
              }
            } else {
              message = '添加成功'
            }
          }
        })
        .catch((error) => {
          this.loading = false
        })
    },
    handleName(e) {
      if (typeof e === 'string') {
        // this.rules.cid = ''
        // this.rules.units = ''
        // this.rules.specs = ''
        // this.rules.factory = ''
        // this.rules.mark = ''
        // this.rules.number = undefined
        // this.rules.price = undefined
      } else {
        const data = this.formData.selectData[e]
        this.itemData = data
        this.rules.cid = data.cate.path
        if (this.rules.cid.length > 0) {
          if (!this.rules.cid.includes(data.cid)) {
            this.rules.cid.push(data.cid) // 添加当前分类
          }
        } else {
          this.rules.cid.unshift(0)
          this.rules.cid.push(data.cid)
        }
        this.rules.units = data.units
        this.rules.specs = data.specs
        this.rules.factory = data.factory
        this.rules.mark = data.mark
        this.rules.number = undefined
        this.rules.price = undefined
      }
    },
    countriesSelect() {
      this.$nextTick(() => {
        const countriesSelect = document.querySelector('.countries-select input')
        countriesSelect.addEventListener('input', (val) => {
          let unm = countriesSelect.value.length
          let max = 20
          if (unm > max) {
            countriesSelect.value = countriesSelect.value.substring(0, max)
            this.$message.error('最多可以输入' + max + '字符')
          }
        })
      })
    },
    getJudgeInfo() {
      const data = this.itemData
      let unitsStatus = data.units === '' || (data.units !== '' && this.rules.units === data.units)
      let specsStatus = data.specs === '' || (data.specs !== '' && this.rules.specs === data.specs)
      let factoryStatus = data.factory === '' || (data.factory !== '' && this.rules.factory === data.factory)
      return unitsStatus && specsStatus && factoryStatus
    }
  }
}
</script>

<style lang="scss" scoped>
.station ::v-deep.el-drawer__body {
  padding: 20px 20px 50px 20px;
}
::v-deep .el-form--inline .el-form-item {
  display: flex;
}
::v-deep .el-input-number {
  width: 100%;
  .el-input__inner {
    text-align: left;
  }
}

.overflow {
  ::v-deep .el-input--suffix .el-input__inner {
    padding-right: 62px;
  }
}
::v-deep .el-date-editor,
::v-deep .el-select,
::v-deep .el-cascader {
  width: 100%;
}
.invoice {
  margin: 20px 20px 20px 20px;
  .from-foot-btn button {
    width: auto;
    height: auto;
  }
}
.from-item-title {
  border-left: 5px solid #1890ff;
  span {
    padding-left: 10px;
    font-weight: bold;
    font-size: 14px;
  }
}
.form-box {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  .form-item {
    width: 49%;
    ::v-deep .el-form-item__content {
      width: calc(100% - 110px);
    }
    ::v-deep .el-select--medium {
      width: 100%;
    }
    ::v-deep .el-textarea__inner {
      resize: none;
    }
  }
}
.dialog-footer {
  padding-top: 20px;
  border-top: 1px solid #e6ebf5;
  text-align: right;
}
::v-deep .countries-select .el-input__suffix {
  position: absolute;
  top: -2px;
}
</style>
