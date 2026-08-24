<template>
  <el-dialog
    :title="config.title"
    :modal="true"
    :visible.sync="dialogVisible"
    :width="config.width"
    :append-to-body="true"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form ref="form" class="mt15" :model="ruleForm" :rules="rules" label-width="90px">
      <el-form-item :label="$t('hr.jobtitle') + '：'" prop="name">
        <el-input size="small" type="text" v-model="ruleForm.name" :placeholder="$t('hr.message')"></el-input>
      </el-form-item>
      <el-form-item label="职级类别：" prop="cateId">
        <el-select v-model="ruleForm.cateId" size="small" clearable placeholder="请选择职级类别" @change="handleCate">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('hr.rank') + '：'" prop="rankId">
        <el-select v-model="ruleForm.rankId" size="small" clearable :placeholder="$t('hr.message9')">
          <el-option v-for="item in rankData" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
      <el-button size="small" class="mr20" :loading="loading" type="primary" @click="handleConfirm">{{
        $t('public.ok')
      }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { endJobSaveApi } from '@/api/enterprise'
import { rankCateListApi, rankListApi } from '@/api/setting'

export default {
  name: 'PositionDialog',
  props: {
    config: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      dialogVisible: false,
      ruleForm: {
        name: '',
        cateId: '',
        rankId: ''
      },
      rules: {
        name: [{ required: true, message: this.$t('hr.message'), trigger: 'blur' }],
        cateId: [{ required: true, message: '请选择职级类别', trigger: 'change' }],
        rankId: [{ required: true, message: '请选择职级', trigger: 'change' }]
      },
      loading: false,
      propsPos: { value: 'id', label: 'name', multiple: false, checkStrictly: true },
      options: [],
      rankData: []
    }
  },
  mounted() {
    this.getRankCate()
  },
  methods: {
    handleOpen() {
      this.dialogVisible = true
    },

    handleClose() {
      this.ruleForm.name = ''
      this.ruleForm.cateId = ''
      this.ruleForm.rankId = ''
      this.$refs.form.resetFields()
      this.dialogVisible = false
    },
    handleConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          const data = {
            name: this.ruleForm.name,
            cate_id: this.ruleForm.cateId,
            rank_id: this.ruleForm.rankId
          }
          this.endJobs(data)
        }
      })
    },
    endJobs(data) {
      this.loading = true
      endJobSaveApi(data)
        .then((res) => {
          this.loading = false
          this.ruleForm.name = ''
          this.ruleForm.cateId = ''
          this.ruleForm.rankId = ''
          this.handleClose()
          this.$emit('handleConfig')
        })
        .catch((error) => {
          this.loading = false
        })
    },
    async getRankCate() {
      const data = {
        page: 1,
        limit: 0
      }
      const result = await rankCateListApi(data)
      this.options = result.data.list
    },
    handleCate(e) {
      this.getRankData(e)
      this.ruleForm.rankId = ''
    },
    async getRankData(id) {
      const data = {
        page: 1,
        limit: 0,
        cate_id: id
      }
      const result = await rankListApi(data)
      this.rankData = result.data.list
    }
  }
}
</script>

<style scoped lang="scss">

::v-deep .el-dialog__footer {
  padding: 20px 0;
}
::v-deep .el-select {
  width: 100%;
}
.dialog-footer {
  padding-top: 20px;
  border-top: 1px solid #e6ebf5;
}
</style>
