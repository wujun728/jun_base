<template>
  <el-dialog
    :title="config.title"
    :visible.sync="dialogVisible"
    :width="config.width"
    :append-to-body="true"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form ref="form" :model="formData" :rules="rules" :label-width="labelWidth">
      <div v-if="config.type === 1 || config.type === 3">
        <el-form-item class="mt20">
          <span slot="label">
            <span class="color-tab">*</span>
            职等区间：
          </span>
          <el-col :span="11">
            <el-form-item prop="levelMin">
              <el-input-number
                v-model="formData.levelMin"
                placeholder="最低职等"
                :min="0"
                :controls="false"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col class="text-center line" :span="2"><el-divider></el-divider></el-col>
          <el-col :span="11">
            <el-form-item prop="levelMax">
              <el-input-number
                v-model="formData.levelMax"
                placeholder="最高职等"
                :min="formData.levelMin"
                :controls="false"
              ></el-input-number>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item class="mt20">
          <span slot="label">
            <span class="color-tab">*</span>
            薪资区间：
          </span>
          <el-col :span="11">
            <el-form-item prop="salaryMin">
              <el-input-number
                v-model="formData.salaryMin"
                placeholder="最低薪资"
                :min="0"
                :controls="false"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col class="text-center line" :span="2"><el-divider></el-divider></el-col>
          <el-col :span="11">
            <el-form-item prop="salaryMax">
              <el-input-number
                v-model="formData.salaryMax"
                placeholder="最高薪资"
                :min="formData.salaryMin"
                :controls="false"
              ></el-input-number>
            </el-form-item>
          </el-col>
        </el-form-item>
      </div>
      <div v-if="config.type === 2">
        <el-form-item class="mt20" label="每级职等跨度:" prop="rank">
          <el-input-number placeholder="请输入职等跨度" v-model="formData.rank" :controls="false"></el-input-number>
        </el-form-item>
        <el-form-item class="mt20" label="跨度说明:" style="margin-bottom: 0">
          <div class="rank-explain">
            <p>1. 批量设置职等跨度，跨度只能为正整数，不能为负数；</p>
            <p>2. 职等跨度示例：1-4，跨度数字为4。</p>
          </div>
        </el-form-item>
      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
      <el-button size="small" type="primary" :loading="loading" @click="handleConfirm">{{ $t('public.ok') }}</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { rankLevelBatchApi, rankLevelEditApi, rankLevelSaveApi } from '@/api/setting'

export default {
  name: 'RankDialog',
  props: {
    config: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    const checkLevelMin = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请输入最低职等'))
      }
      setTimeout(() => {
        if (this.formData.levelMax && value > this.formData.levelMax) {
          callback(new Error('最低职等不能大于最高职等'))
        } else {
          callback()
        }
      }, 150)
    }
    return {
      dialogVisible: false,
      labelWidth: '90px',
      formData: {
        levelMin: undefined,
        levelMax: undefined,
        salaryMin: undefined,
        salaryMax: undefined,
        rank: undefined
      },
      rules: {
        levelMin: [{ required: true, validator: checkLevelMin, trigger: 'blur' }],
        levelMax: [{ required: true, message: '请输入最高职等', trigger: 'blur' }],
        salaryMin: [{ required: true, message: '请输入最低薪资', trigger: 'blur' }],
        salaryMax: [{ required: true, message: '请输入最高薪资', trigger: 'blur' }],
        rank: [{ required: true, message: '请输入职等跨度', trigger: 'blur' }]
      },
      loading: false
    }
  },
  watch: {
    config: {
      handler(nVal) {
        if (nVal.type === 2) {
          this.labelWidth = '110px'
        } else {
          this.labelWidth = '90px'
        }
        if (nVal.edit) {
          if (nVal.type === 1) {
            this.formData.levelMin = nVal.data.min_level
            this.formData.levelMax = nVal.data.max_level
            const arr = nVal.data.salary.split('-')
            this.formData.salaryMin = Number(arr[0])
            this.formData.salaryMax = Number(arr[1])
          }
        }
      },
      deep: true
    }
  },
  mounted() {},
  methods: {
    handleOpen() {
      this.dialogVisible = true
    },
    reset() {
      this.formData.levelMin = undefined
      this.formData.levelMax = undefined
      this.formData.salaryMin = undefined
      this.formData.salaryMax = undefined
      this.formData.rank = undefined
    },
    handleClose() {
      this.$refs.form.clearValidate()
      this.dialogVisible = false
    },
    handleConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          var data = {}
          if (this.config.type === 1) {
            data.salary = this.formData.salaryMin + '-' + this.formData.salaryMax
            data.min_level = this.formData.levelMin
            data.max_level = this.formData.levelMax
            if (this.config.edit === false) {
              this.rankLevelSave(data)
            } else {
              this.rankLevelEdit(this.config.data.id, data)
            }
          } else if (this.config.type === 2) {
            this.rankLevelBatch(this.formData.rank)
          }
        }
      })
    },
    // 职位等级添加
    rankLevelSave(data) {
      this.loading = true
      rankLevelSaveApi(data)
        .then((res) => {
          this.handleClose()
          this.loading = false
          this.$emit('isOk')
          this.reset()
        })
        .catch((error) => {
          this.loading = false
        })
    },
    // 职位等级编辑
    rankLevelEdit(id, data) {
      this.loading = true
      rankLevelEditApi(id, data)
        .then((res) => {
          this.handleClose()
          this.loading = false
          this.$emit('isOk')
          this.reset()
        })
        .catch((error) => {
          this.loading = false
        })
    },
    // 批量修改职位区间
    rankLevelBatch(id) {
      this.loading = true
      rankLevelBatchApi(id)
        .then((res) => {
          this.handleClose()
          this.loading = false
          this.$emit('isOk')
          this.reset()
        })
        .catch((error) => {
          this.loading = false
        })
    }
  }
}
</script>

<style scoped lang="scss">

.line {
  padding: 0 8px;
}
::v-deep .el-input-number--medium {
  width: 100%;
}
::v-deep .el-input__inner {
  text-align: left;
}
.rank-explain {
  p {
    margin: 0;
    font-size: 13px;
    color: rgba(0, 0, 0, 0.5);
  }
}
::v-deep .el-dialog__footer {
  padding: 0;
}
.dialog-footer {
  padding: 20px;
  border-top: 1px solid #e6ebf5;
}
</style>
