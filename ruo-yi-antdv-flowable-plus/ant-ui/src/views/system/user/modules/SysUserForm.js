import { getUser, addUser, updateUser, checkUserNameUnique, checkEmailUnique, checkPhoneUnique } from '@/api/system/user'
import { listDeptTree } from '@/api/system/dept'
import SelectDept from '@/components/pt/selectDept/SelectDept'
import AntModal from '@/components/pt/dialog/AntModal'
export default {
  name: 'CreateForm',
  props: {
    deptCheckedValue: {
      type: Object
    },
    statusOptions: {
      type: Array,
      required: true
    },
    sexOptions: {
      type: Array,
      required: true
    },
    userTypeOptions: {
      type: Array,
      required: true
    },
    defalutExpandedKeys: {
      type: Array
    }
  },
  components: {
    AntModal,
    SelectDept
  },
  data () {
    const validateDeptId = (rule, value, callback) => {
      if (value.ids === '' || value.ids === undefined || value.ids === null) {
        callback(new Error('部门不允许为空'))
      } else {
        callback()
      }
    }
    return {
      expandedKeys: this.defalutExpandedKeys,
      spinning: false,
      delayTime: 100,
      replaceFields: { children: 'children', title: 'label', key: 'id', value: 'id' },
      customStyle: 'background: #fff;ssborder-radius: 4px;margin-bottom: 24px;border: 0;overflow: hidden',
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 默认密码
      initPassword: undefined,
      formTitle: '',
      // 表单参数
      form: {
        id: undefined,
        deptId: 0,
        userName: undefined,
        nickName: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: '2',
        status: '0',
        userType: '2',
        remark: undefined,
        postIds: [],
        roleIds: []
      },
      open: false,
      rules: {
        name: [{ required: true, message: '用户名称不能为空', trigger: 'blur' }],
        no: [{ required: true, message: '用户编号不能为空', trigger: 'blur' }],
        userName: [{ required: true, message: '登录名不能为空', trigger: 'blur' },
         { validator: this.checkUserNameUnique, trigger: 'change' }
         ],
        postIds: [{ required: true, message: '岗位不能为空', trigger: 'blur' }],
        secretLevel: [{ required: true, message: '密级不能为空', trigger: 'blur' }],
        deptId: [{ required: true, message: '部门不能为空', trigger: 'blur', validator: validateDeptId }],
        email: [
          {
            type: 'email',
            message: '请正确填写邮箱地址',
            trigger: ['blur', 'change']
          },
            { validator: this.checkEmailUnique }
        ],
        phonenumber: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: '请正确填写手机号',
            trigger: 'blur'
          },
           { validator: this.checkPhoneUnique }
        ]
      }
    }
  },
  filters: {},
  created () {
    this.getConfigKey('sys.user.initPassword').then(response => {
      this.initPassword = response.msg
    })
  },
  computed: {},
  watch: {},
  methods: {
    // 取消按钮
    cancel () {
      this.open = false
      this.$emit('close')
    },
    // 表单重置
    reset () {
      if (this.$refs.form !== undefined) {
        this.$refs.form.resetFields()
      }
    },
    /** 新增按钮操作 */
    handleAdd () {
      // this.$emit('select-tree')
      this.open = true
      this.formTitle = '新增用户'
      getUser().then(response => {
        this.postOptions = response.posts
        this.roleOptions = response.roles
        this.form.deptId = this.deptCheckedValue
      })
    },
    /** 修改按钮操作 */
    handleUpdate (row, ids) {
        this.open = true
        this.formTitle = '修改【' + row.name + '】信息'
        this.spinning = !this.spinning
        // this.$emit('select-tree')
        const id = row ? row.id : ids
        getUser(id).then(response => {
        this.form = response.data
        this.form.deptId = { ids: response.data.deptId, names: response.data.sysDept.deptName }
        this.postOptions = response.posts
        this.roleOptions = response.roles
        this.form.postIds = response.postIds
        this.form.roleIds = response.roleIds
         this.spinning = !this.spinning
        })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
            const saveForm = JSON.parse(JSON.stringify(this.form))
            if (this.form.deptId !== undefined) {
              saveForm.deptId = this.form.deptId.ids
            }
            if (this.form.id !== undefined) {
              updateUser(saveForm).then(response => {
                this.$message.success(
                  '修改成功',
                  3
                )
                this.open = false
                this.$emit('ok')
              })
            } else {
              addUser(saveForm).then(response => {
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
    onLoadData (treeNode) {
          return new Promise(resolve => {
            if (treeNode.dataRef.children) {
              resolve()
              return
            }
            listDeptTree(treeNode.dataRef.id, 1).then(response => {
               treeNode.dataRef.children = response.data
                resolve()
            })
          })
        },
     checkUserNameUnique (rule, value, callback) {
       const msg = '登陆名称已存在'
       if (value === '') {
         callback()
       } else {
         const checkData = {
           userName: value,
           id: this.form.id !== undefined ? this.form.id : ''
         }
         checkUserNameUnique(checkData).then(response => {
           if (response.data.code === '1') {
             callback()
           } else {
            callback(msg)
           }
         })
       }
     },
     checkEmailUnique (rule, value, callback) {
       const msg = '登陆名称已存在'
       if (value === '') {
         callback()
       } else {
         const checkData = {
           email: value,
           id: this.form.id !== undefined ? this.form.id : ''
         }
         checkEmailUnique(checkData).then(response => {
           if (response.data.code === '1') {
             callback()
           } else {
            callback(msg)
           }
         })
       }
     },
     checkPhoneUnique (rule, value, callback) {
       const msg = '手机号已存在'
       if (value === '') {
         callback()
       } else {
         const checkData = {
           phonenumber: value,
           id: this.form.id !== undefined ? this.form.id : ''
         }
         checkPhoneUnique(checkData).then(response => {
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
