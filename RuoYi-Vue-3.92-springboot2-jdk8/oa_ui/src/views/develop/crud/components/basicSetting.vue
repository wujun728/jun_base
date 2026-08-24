<template>
  <div class="main">
    <!-- <div class="title-16 mt20">属性信息</div> -->
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="90px" class="mt20">
      <el-form-item label="显示名称:" prop="table_name">
        <el-input v-model="ruleForm.table_name" size="small"></el-input>
      </el-form-item>
      <el-form-item label="实体名称:" prop="table_name_en">
        <el-input disabled v-model="ruleForm.table_name_en" size="small"></el-input>
      </el-form-item>
      <el-form-item label="所属主实体:" prop="region">
        <el-cascader
          disabled
          v-model="ruleForm.crud_id"
          :options="options"
          style="width: 100%"
          size="small"
          :show-all-levels="false"
          :props="{ checkStrictly: true, label: 'table_name', value: 'id', emitPath: false }"
        >
          <template slot-scope="{ node, data }">
            <span>{{ data.table_name }}</span>
            <span> （{{ data.table_name_en }}）</span>
          </template>
        </el-cascader>
        <div class="tips">关联主实体后，则此实体为从表，无表单、审批流、触发器设置。</div>
      </el-form-item>
      <el-form-item label="操作日志:">
        <el-switch
          v-model="ruleForm.show_log"
          active-value="1"
          inactive-value="0"
          inactive-text="关闭"
          active-text="开启"
          size="small"
        ></el-switch>
      </el-form-item>

      <el-form-item label="评论功能:">
        <div :class="ruleForm.show_comment == 1 ? 'flex-col-center' : ''">
          <el-switch
            v-model="ruleForm.show_comment"
            active-value="1"
            inactive-value="0"
            inactive-text="关闭"
            active-text="开启"
          ></el-switch>
          <el-input
            v-if="ruleForm.show_comment == 1"
            class="ml10"
            v-model="ruleForm.comment_title"
            maxlength="5"
            size="small"
            placeholder="评论重命名"
          ></el-input>
        </div>
      </el-form-item>

      <el-form-item label="关联应用:" prop="region">
        <el-select
          style="width: 100%"
          v-model="ruleForm.cate_ids"
          multiple
          filterable
          size="small"
          placeholder="请搜索选择应用（多选）"
        >
          <el-option v-for="(v, index) in cateOptions" :key="v.id" :label="v.name" :value="v.id"> </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="实体说明:" prop="region">
        <el-input
          type="textarea"
          v-model="ruleForm.info"
          :autosize="{ minRows: 4, maxRows: 8 }"
          size="small"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" :loading="loading" @click="submit">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import Commnt from '@/components/develop/commonData'
import { getcrudCateListApi, databaseListApi, databasePutApi } from '@/api/develop'

export default {
  props: {
    infoData: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      ruleForm: Commnt.formDataInit,
      rules: Commnt.formRules,
      cateOptions: [],
      options: [],
      loading: false
    }
  },
  created() {
    this.getList()
    this.getCateList()
  },
  watch: {
    infoData(val) {
      this.ruleForm.table_name = val.table_name
      this.ruleForm.table_name_en = val.table_name_en
      this.ruleForm.info = val.info
      this.ruleForm.crud_id = val.crud_id
      this.ruleForm.show_log = val.show_log + ''
      this.ruleForm.comment_name = val.comment_title || '评论'
      this.ruleForm.show_comment = val.show_comment + ''
      if (val.cate_ids && val.cate_ids.length > 0) {
        this.ruleForm.cate_ids = val.cate_ids.map(Number)
      } else {
        this.ruleForm.cate_ids = []
      }
    }
  },

  methods: {
    submit() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          this.loading = true
          databasePutApi(this.infoData.id, this.ruleForm).then((res) => {
            this.loading = false
          })
        }
      })
    },
    async getList() {
      let obj = {
        cate_id: ''
      }
      const data = await databaseListApi(obj)
      this.options = data.data.list
    },
    async getCateList() {
      const result = await getcrudCateListApi()
      this.cateOptions = result.data.list
    }
  }
}
</script>
<style scoped lang="scss">
.main {
  width: 800px;
  margin: 0 auto;

  .title {
    font-size: 16px;
    font-weight: 500;
  }
}
.tips {
  font-size: 12px;
  color: #909399;
}
.flex-col-center {
  display: flex;
  align-items: center;
}
</style>
