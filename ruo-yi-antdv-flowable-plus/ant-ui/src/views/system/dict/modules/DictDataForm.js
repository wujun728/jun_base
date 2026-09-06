import AntModal from '@/components/pt/dialog/AntModal'
import { checkDictDataValueUnique, getData, saveData, findMaxSort } from '@/api/system/dict/data'
export default {
  name: 'CreateDataForm',
  props: {
    dictType: {
      type: String,
      required: true
    },
    statusOptions: {
      type: Array,
      required: true
    },
    title: String
  },
  components: {
    AntModal
  },
  data () {
    return {
      loading: false,
      formTitle: '',
      // 表单参数
      form: {
        id: undefined,
        dictLabel: undefined,
        dictValue: undefined,
        dictSort: 0,
        status: '0',
        remark: undefined
      },
      open: false,
      rules: {
        dictLabel: [{ required: true, message: '数据标签不能为空', trigger: 'blur' }],
        dictValue: [{ required: true, message: '数据键值不能为空', trigger: 'blur' },
          { validator: this.checkDictDataValueUnique }],
        dictSort: [{ required: true, message: '数据顺序不能为空', trigger: 'blur' }]
      }
    }
  },
  filters: {
  },
  created () {
  },
  computed: {
  },
  watch: {
  },
  methods: {
    // 取消按钮
    cancel () {
      this.open = false
      this.reset()
      this.$emit('close')
    },
    // 表单重置
    reset () {
    },
    /** 新增按钮操作 */
    handleAdd () {
      this.reset()
      /** 获取最大编号 */
      findMaxSort(this.dictType).then(response => {
        this.form.dictSort = response.data
        this.open = true
        this.formTitle = '添加【' + this.title + '】子表数据'
        this.form.dictType = this.dictType
      })
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset()
      const dictCode = row.id
      getData(dictCode).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = '修改【' + this.title + '】子表数据'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            saveData(this.form).then(response => {
              this.$message.success(
                '修改成功',
                3
              )
              this.open = false
              this.$emit('ok')
            })
          } else {
            saveData(this.form).then(response => {
              this.$message.success(
                '新增成功',
                3
              )
              this.open = false
              this.$emit('ok')
            })
          }
        } else {
          return false
        }
      })
    },
    checkDictDataValueUnique (rule, value, callback) {
      const msg = '数据字典值已存在'
      if (value === '') {
        callback()
      } else {
        const checkData = {
          dictValue: value,
          dictType: this.dictType,
          id: this.form.id !== undefined ? this.form.id : ''
        }
        checkDictDataValueUnique(checkData).then(response => {
          if (response.data.code === '1') {
            callback()
          } else {
            callback(msg)
          }
        })
      }
    }
  }
}
