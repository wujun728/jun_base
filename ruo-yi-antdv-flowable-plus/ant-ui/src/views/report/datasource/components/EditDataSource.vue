<template>
  <el-dialog
    title="项目基础配置"
    width="50%"
    :close-on-click-modal="false"
    center
    :visible.sync="visib"
    :before-close="closeDialog"
  >
    <el-form
      ref="userForm"
      :model="dialogForm"
      :rules="rules"
      size="small"
      label-width="100px"
    >
      <el-row :gutter="10">
        <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
          <el-form-item label="数据源类型" prop="sourceType">
            <el-select
              v-model.trim="dialogForm.sourceType"
              placeholder="请选择"
              clearable
              @change="selectChange"
            >
              <el-option
                v-for="item in dictionaryOptions"
                :key="item.id"
                :label="item.text"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
          <el-form-item label="数据源编码" prop="sourceCode">
            <el-input
              :disabled="updataDisabled"
              v-model.trim="dialogForm.sourceCode"
              placeholder="唯一标识"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
          <el-form-item label="数据源名称" prop="sourceName">
            <el-input v-model.trim="dialogForm.sourceName" />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="20" :md="24" :lg="24" :xl="24">
          <el-form-item label="数据源描述">
            <el-input
              v-model.trim="dialogForm.sourceDesc"
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4 }"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col
          v-for="(data, index) in dataLink"
          :key="index"
          :xs="24"
          :sm="24"
          :md="24"
          :lg="24"
          :xl="24"
        >
          <el-form-item :label="data.labelValue">
            <el-input v-model.trim="data.value" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">取消</el-button>
      <el-button type="warning" @click="test">测试</el-button>
      <el-button
        type="primary"
        @click="UserConfirm('userForm')"
      >确定</el-button
      >
    </div>
  </el-dialog>
</template>
<script>
import {
  testConnection,
  reportDataSourceDetail,
  reportDataSourceAdd,
  reportDataSourceUpdate
} from '@/api/report/reportDataSource'
import { getDictList } from '@/api/report/dict-data' // 获取数据字典
import Dictionary from '@/components/Dictionary/index'
import { validateEngOrNum } from '@/utils/validate'
export default {
  name: 'Support',
  components: { Dictionary },
  props: {
    visib: {
      required: true,
      type: Boolean,
      default: false
    },
    dataSource: {
      required: false,
      type: Object,
      default: () => {
        return ''
      }
    }
  },
  data() {
    return {
      dictionaryOptions: [], // 数据源类型
      selectedList: [],
      clickType: '',
      formData: {},
      list: null,
      totalCount: 0,
      totalPage: 0,
      listLoading: true,
      // 弹框默认隐藏
      dialogFormVisible: false,
      basicDialog: false,
      params: {
        sourceName: '',
        sourceCode: '',
        sourceType: '',
        pageNumber: 1,
        pageSize: 10,
        order: 'DESC',
        sort: 'update_time'
      },
      dialogForm: {
        sourceName: '',
        sourceCode: '',
        sourceType: '',
        sourceDesc: '',
        sourceConfig: ''
      },
      dataLink: [],
      rules: {
        sourceType: [
          { required: true, message: '数据集名称必选', trigger: 'change' }
        ],
        sourceCode: [
          { required: true, message: '数据集编码必填', trigger: 'blur' },
          { validator: validateEngOrNum, trigger: 'blur' }
        ],
        sourceName: [
          { required: true, message: '数据源名称必选', trigger: 'blur' }
        ]
      },
      value: '',
      updataDisabled: false,
      testReplyCode: null
    }
  },
  // 在生命周期 beforeCreate里面改变this指向
  beforeCreate: function() {},
  mounted() {},
  methods: {
    async setDataSource() {
      this.dialogForm = {
        sourceName: '',
        sourceCode: '',
        sourceType: '',
        sourceDesc: '',
        sourceConfig: ''
      }
      //根据dataSourceId判断新增还是编辑
      if (this.dataSource.id) {
        const { code, data } = await reportDataSourceDetail(this.dataSource)
        if (code != '200') return
        this.dialogForm = data
        const newSourceType = this.dialogForm
        let newDataLink = []
        this.dictionaryOptions.map(item => {
          if (item.id == newSourceType.sourceType) {
            newDataLink = JSON.parse(item.extend)
            var sourceConfigJson = JSON.parse(newSourceType.sourceConfig)
            for (var i = 0; i < newDataLink.length; i++) {
              newDataLink[i].value = sourceConfigJson[newDataLink[i].label]
            }
          }
        })
        this.dataLink = newDataLink
      }
    },
    // 获取数据字典
    async getSystem() {
      const { code, data } = await getDictList('SOURCE_TYPE')
      if (code != '200') return
      this.dictionaryOptions = data
      this.dialogForm.sourceType = this.dictionaryOptions[0].text
      this.dataLink = JSON.parse(this.dictionaryOptions[0].extend)
      this.setDataSource()
    },
    // 关闭模态框
    closeDialog() {
      this.$emit('handleClose')
    },
    // 提交
    async UserConfirm(formName) {
      const newList = {}
      this.dataLink.forEach(item => {
        newList[item.label] = item.value
      })
      this.dialogForm.sourceConfig = JSON.stringify(newList)
      this.$refs[formName].validate(async (valid, obj) => {
        if (valid) {
          if (this.testReplyCode != '200') {
            this.$message.error('测试结果为成功后方可保存！')
            return
          }
          if (this.dialogForm.id == undefined) {
            const { code } = await reportDataSourceAdd(this.dialogForm)
            if (code != '200') return
            // this.queryByPage()
            this.$emit('refreshList')
          } else {
            const { code } = await reportDataSourceUpdate(this.dialogForm)
            if (code != '200') return
            // this.queryByPage()
            this.$emit('refreshList')
          }
          this.closeDialog()
        } else {
          return
        }
      })
    },
    selectChange(val) {
      this.dataLink = []
      const extendJSON = this.dictionaryOptions.find(function(obj) {
        return obj.id == val
      })
      this.dataLink = JSON.parse(extendJSON.extend)
    },
    // 测试
    test() {
      const newList = {}
      this.dataLink.forEach(item => {
        newList[item.label] = item.value
      })
      this.dialogForm.sourceConfig = JSON.stringify(newList)
      testConnection(this.dialogForm).then(data => {
        if (data.code == '200') {
          this.testReplyCode = data.code
          this.$message({
            message: '测试成功！',
            type: 'success'
          })
        } else {
          this.testReplyCode = null
        }
      })
    }
  }
}
</script>
