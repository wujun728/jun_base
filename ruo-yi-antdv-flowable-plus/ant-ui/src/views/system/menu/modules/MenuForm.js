import { getMenu, saveMenu, checkMenuNameUnique, findMaxSort, checkMenuCodeUnique } from '@/api/system/menu'
import allIcon from '@/core/icons'
import icons from '@/utils/requireIcons'
import IconSelector from '@/components/IconSelector'
import AntModal from '@/components/pt/dialog/AntModal'

export default {
  name: 'CreateForm',
  props: {
    statusOptions: {
      type: Array,
      required: true
    },
    visibleOptions: {
      type: Array,
      required: true
    },
    menuOptions: {
      type: Array,
      required: true
    },
    menuTypeOptions: {
      type: Array,
      required: true
    }
  },
  components: {
    IconSelector,
    AntModal
  },
  data () {
    return {
      SelectIcon: 'down',
      allIcon,
      iconVisible: false,
      iconList: icons,
      loading: false,
      formTitle: '',
      currentRow: undefined,
      oldParentId: '',
      spinning: false,
      delayTime: 200,
      menuTypeEnableValue: [],
      // 表单参数
      form: {
        id: undefined,
        parentId: 0,
        menuName: undefined,
        icon: undefined,
        menuType: 'M',
        treeSort: 0,
        isFrame: '1',
        isCache: '0',
        visible: '0',
        status: '0'
      },
      open: false,
      rules: {
        menuName: [{ required: true, message: '菜单名称不能为空', trigger: 'blur' },
        { validator: this.checkMenuNameUnique }],
        menuCode: [{ required: true, message: '菜单编码不能为空', trigger: 'blur' },
        { validator: this.checkMenuCodeUnique }],
        treeSort: [{ required: true, message: '菜单顺序不能为空', trigger: 'blur' }],
        path: [{ required: true, message: '路由地址不能为空', trigger: 'blur' }]
      }
    }
  },
  filters: {},
  created () {},
  computed: {},
  watch: {},
  methods: {
    onMenuTreeChange (parentId, label, extra) {
      const menuType = extra.triggerNode.$options.propsData.dataRef.menuType
      findMaxSort(parentId !== undefined ? parentId : '0').then(response => {
        this.form.treeSort = response.data
      })
      if (menuType !== undefined) {
        this.getMenuTypeEnableValue(menuType)
      } else {
        this.menuTypeEnableValue = this.menuTypeOptions.map(function (item) { return item.menuTypeValue })
      }
    },
    filterIcons () {
      this.iconList = icons
      if (this.name) {
        this.iconList = this.iconList.filter(item => item.includes(this.name))
      }
    },
    hideIconSelect () {
      this.iconList = icons
      this.iconVisible = false
    },
    // 取消按钮
    cancel () {
      this.open = false
      this.iconVisible = false
      this.reset()
      this.$emit('close')
    },
    // 表单重置
    reset () {
    },
    getMenuTypeEnableValue (parentMenuType) {
      const id = this.form.id
      // if (parentMenuType === 'M' || parentMenuType === 'C') {
      if (parentMenuType === 'M') {
        this.menuTypeEnableValue = this.menuTypeOptions.map(function (item) { return item.menuTypeValue })
      } else if (parentMenuType === 'C') {
        this.menuTypeEnableValue = this.menuTypeOptions.filter(function (item) { return item.menuTypeValue !== 'M' })
          .map(function (item) { return item.menuTypeValue })
      } else {
        this.menuTypeEnableValue = this.menuTypeOptions.filter(function (item) { return item.menuTypeValue === 'F' })
          .map(function (item) { return item.menuTypeValue })
      }
      if (id !== null && id !== '' && id !== 'undefined' && id !== undefined) {
        // 编辑页面
        if (parentMenuType === null) {
          this.menuTypeEnableValue = this.menuTypeOptions.filter(function (item) { return item.menuTypeValue === 'M' })
            .map(function (item) { return item.menuTypeValue })
        }
        // 编辑页面当切换上级部门后需要判断当前部门类型是否在可选集合，如果在类型保持不变，如果不在需要重新赋值
        const menuType = this.form.menuType
        const selectMenuType = this.menuTypeEnableValue.filter(function (item) { return item === menuType })
        this.form.menuType = selectMenuType.length === 0 ? this.menuTypeEnableValue[0] : menuType
      } else {
        // 添加页面
        this.form.menuType = this.menuTypeEnableValue[0]
      }
    },
    /** 新增按钮操作 */
    handleAdd (row) {
      this.reset()
      /** 获取最大编号 */
      findMaxSort(row !== undefined ? row.id : '0').then(response => {
        this.form.treeSort = response.data
      })
      this.menuTypeEnableValue = this.menuTypeOptions.map(function (item) { return item.menuTypeValue })
      this.$emit('select-tree')
       this.oldParentId = ''
      if (row != null && row.id) {
        this.currentRow = row
        this.oldParentId = row.id
        this.form.parentId = row.id
        this.getMenuTypeEnableValue(row.menuType)
      } else {
        this.form.parentId = 0
      }
      this.open = true
      this.formTitle = '添加菜单'
    },
    setNodeData (data) {
      this.currentRow.menuName = data.menuName
      this.currentRow.menuCode = data.menuCode
      this.currentRow.icon = data.icon
      this.currentRow.treeSort = data.treeSort
      this.currentRow.menuType = data.menuType
      this.currentRow.visible = data.visible
      this.currentRow.perms = data.perms
      this.currentRow.component = data.component
      this.currentRow.status = data.status
      this.currentRow.createTime = data.createTime
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.spinning = !this.spinning
      this.open = true
      this.formTitle = '修改菜单'
      this.currentRow = row
      this.reset()
      this.$emit('select-tree')
      getMenu(row.id).then(response => {
        this.oldParentId = response.data.parentId
        this.form = response.data
        this.spinning = !this.spinning
        this.menuTypeEnableValue = [response.data.menuType]
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.spinning = !this.spinning
          if (this.form.id !== undefined) {
            saveMenu(this.form).then(response => {
              this.$message.success('修改成功', 3)
              if (this.oldParentId !== this.form.parentId) {
                // 如果修改父节点则刷新树
                this.$emit('ok')
              } else {
                this.setNodeData(response.data)
              }
              this.cancel()
            })
          } else {
            saveMenu(this.form).then(response => {
              this.$message.success('新增成功', 3)
              // 修改父节点后刷新整个树，如果直接添加子节点不更换父节点则追加节点
              if (this.oldParentId !== this.form.parentId) {
                // 如果修改父节点则刷新树
                this.$emit('ok')
              } else {
                 this.appendTreeNode(this.currentRow, response.data)
              }
              this.cancel()
            })
          }
        } else {
          return false
        }
      })
    },
    handleIconChange (icon) {
      this.SelectIcon = 'down'
      this.iconVisible = false
      this.form.icon = icon
    },
    changeIcon (type) {
      this.currentSelectedIcon = type
    },
    selectIcon () {
      this.iconVisible = !this.iconVisible
      if (this.iconVisible) {
        this.SelectIcon = 'up'
      } else {
        this.SelectIcon = 'down'
      }
    },
    cancelSelectIcon () {
      this.iconVisible = false
    },
    checkMenuNameUnique (rule, value, callback) {
      const msg = '菜单名称已存在'
      if (value === '') {
        callback()
      } else {
        const checkData = {
          menuName: value,
          parentId: this.form.parentId !== undefined ? this.form.parentId : '',
          id: this.form.id !== undefined ? this.form.id : ''
        }
        checkMenuNameUnique(checkData).then(response => {
          if (response.data.code === '1') {
            callback()
          } else {
            callback(msg)
          }
        })
      }
    },
    checkMenuCodeUnique (rule, value, callback) {
      const msg = '路由地址已存在'
      if (value === '') {
        callback()
      } else {
        const checkData = {
          menuCode: value,
          id: this.form.id !== undefined ? this.form.id : ''
        }
        checkMenuCodeUnique(checkData).then(response => {
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
