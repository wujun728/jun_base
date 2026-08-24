<template>
  <div class="oa-dialog">
    <el-drawer
      :title="title"
      :visible.sync="show"
      direction="rtl"
      :wrapper-closable="true"
      :before-close="handleClose"
      size="60%"
      :wrapperClosable="false"
    >
      <div class="invite">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="auto">
          <el-form-item label="密钥名称：" prop="title">
            <el-input placeholder="请输入名称" v-model="ruleForm.title" size="small"> </el-input>
          </el-form-item>
          <el-form-item label="密钥描述：">
            <el-input type="textarea" v-model="ruleForm.info" :rows="2" size="small" placeholder="请输入内容">
            </el-input>
          </el-form-item>
        </el-form>
        <div class="mt30">
          <span class="name"> 密钥权限：</span>
          <div class="tree-box mt10">
            <el-table
              :data="treeData"
              style="width: 100%; margin-bottom: 20px"
              row-key="id"
              default-expand-all
              :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
            >
              <el-table-column prop="name" label="应用/菜单" width="300">
                <template slot-scope="scope">
                  <div class="flex-center">
                    <span
                      class="iconfont icontongyonggouxuan-01"
                      v-show="authParents.includes(scope.row.id)"
                      @click="parentsChange(false, scope.row, scope)"
                    ></span>
                    <span
                      class="iconfont icona-tongyongweigouxuanbiankuang"
                      v-show="!authParents.includes(scope.row.id)"
                      @click="parentsChange(true, scope.row, scope)"
                    ></span>
                    <span> {{ scope.row.name }}<span v-if="scope.row.id == '0'">-系统</span></span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="date" label="接口权限">
                <template slot-scope="scope">
                  <el-checkbox-group v-model="auth">
                    <el-checkbox v-for="(item, index) in scope.row.opitons" :label="item.id" :key="index">{{
                      item.name
                    }}</el-checkbox>
                  </el-checkbox-group>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
      <div class="button from-foot-btn fix btn-shadow">
        <el-button class="el-btn" size="small" @click="handleClose()">取消</el-button>
        <el-button size="small" type="primary" @click="submit">保存</el-button>
      </div>
    </el-drawer>
  </div>
</template>
<script>
import { extractArrayIds } from '@/libs/public'
import { getOenapiApi, saveOpenKeyApi, getOpenKeyInfoApi, putOpenKeyApi } from '@/api/develop'
export default {
  name: '',
  components: {},
  props: {},
  data() {
    return {
      show: false,
      title: '新建授权密钥',
      ruleForm: {
        title: '',
        info: ''
      },
      rules: {
        title: [{ required: true, message: '请输入密钥名称', trigger: 'blur' }]
      },
      info: '',
      id: 0,
      authParents: [],
      treeData: [],
      auth: []
    }
  },
  computed: {},
  mounted() {
    this.getOenapi()
  },
  methods: {
    handleClose() {
      this.show = false
      this.auth = []
      this.authParents = []
      this.ruleForm.info = ''
      this.ruleForm.title = ''
      this.id = 0
    },

    // 勾选/取消父级数据
    parentsChange(e, row, scope) {
      if (e) {
        this.selectAllChildren(row)
        if (row.opitons) {
          row.opitons.map((item) => {
            this.auth.push(item.id)
          })
        }
        this.auth = Array.from(new Set(this.auth))
        this.getidsFn(this.auth)
      } else {
        // 取消勾选
        if (!row.children) {
          // 点击二级数据取消
          let ids = extractArrayIds(row.opitons, 'id')
          this.authParents = this.authParents.filter((item) => item != row.id)
          this.auth = this.auth.filter((item) => !ids.some((ele) => ele == item))
        } else {
          // 点击一级数据取消
          this.authParents = this.authParents.filter((item) => item != row.id)
          let childrenIds = extractArrayIds(row.children, 'id')
          this.authParents = this.authParents.filter((item) => !childrenIds.some((ele) => ele == item))
          let optionIds = []
          row.children.map((item) => {
            optionIds = optionIds.concat(extractArrayIds(item.opitons, 'id'))
          })
          this.auth = this.auth.filter((item) => !optionIds.some((ele) => ele == item))
        }
      }
    },

    // 根据父级勾选子级
    selectAllChildren(row) {
      if (row.children && row.children.length) {
        row.children.map((item) => {
          item.opitons.map((item) => {
            this.auth.push(item.id)
          })
        })
      }

      if (row.opitons && row.opitons.length) {
        row.opitons.map((item) => {
          this.auth.push(item.id)
        })
      }
    },

    // 判断一个数组是否包含另一个数组
    getInclude4(arr1, arr2) {
      let temp = []
      for (const item of arr2) {
        arr1.indexOf(item) !== -1 ? temp.push(item) : ''
      }
      return temp.length ? true : false
    },

    submit() {
      if (this.auth.length == 0) {
        this.$message.error('请选择密钥权限')
        return false
      }
      let obj = {
        info: this.ruleForm.info,
        title: this.ruleForm.title,
        auth: this.auth
      }
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          if (this.id) {
            putOpenKeyApi(this.id, obj).then((res) => {
              if (res.status == 200) {
                this.handleClose()
                this.$emit('submitOk', 1)
              }
            })
          } else {
            saveOpenKeyApi(obj).then((res) => {
              if (res.status == 200) {
                this.handleClose()
                this.$emit('submitOk', 1)
              }
            })
          }
        }
      })
    },

    getOenapi() {
      getOenapiApi().then((res) => {
        // 修改数据结构是为了实现需求样式-树形表格
        let keyMap = {
          children: 'opitons'
        }
        res.data.forEach((item) => {
          // 设置为字符串0，为了表格可以展开收起父级
          if (item.id === 0) {
            item.id = item.id + ''
          }
          item.checkAll = false
          item.children.forEach((child) => {
            child.checkAll = false
            for (let key in child) {
              let newKey = keyMap[key]

              if (newKey) {
                child[newKey] = child[key]
                delete child[key]
              }
            }
          })
        })
        this.treeData = res.data
      })
    },
    openBox(id) {
      this.show = true
      if (id) {
        this.title = '编辑授权密钥'
        this.id = id
        getOpenKeyInfoApi(id).then((res) => {
          this.ruleForm.info = res.data.info
          this.ruleForm.title = res.data.title

          this.auth = res.data.auth.map(Number)
          this.getidsFn(this.auth)
        })
      } else {
        this.title = '新建授权密钥'
      }
    },

    getidsFn(ids) {
      this.treeData.map((item) => {
        item.children.map((child) => {
          // 根据三级查找二级数据
          const idsToCheck = child.opitons.map((item) => item.id)
          const result = idsToCheck.every((childrenId) => ids.some((id) => id === childrenId))
          if (result) {
            this.authParents.push(child.id)
          }
          // 根据二级查找一级数据
          if (this.authParents.length > 0) {
            const idsToCheckone = item.children.map((item) => item.id)
            const result = idsToCheckone.every((childrenId) => this.authParents.some((id) => id === childrenId))
            if (result) {
              this.authParents.push(item.id)
            }
          }
        })
      })
    },

    findNamesByIds(tree, id) {
      let ids = id.map((str) => parseInt(str))
      let result = []
      function traverse(node) {
        if (ids.includes(node.id)) {
          result.push(node.id)
        }
        if (node.children) {
          for (const child of node.children) {
            traverse(child)
          }
        }
      }
      for (const node of tree) {
        traverse(node)
      }
      return result
    }
  }
}
</script>
<style scoped lang="scss">
.invite {
  padding: 20px;
  .name {
    font-size: 14px;
    font-weight: 500;
    color: #333333;
  }
}
.custom-tree-node {
  font-size: 14px;
}
.icontongyonggouxuan-01 {
  color: #1890ff;
  font-size: 13px;
  margin-right: 4px;
}
.icona-tongyongweigouxuanbiankuang {
  font-size: 13px;
  font-weight: 400;
  color: #909399;
  margin-right: 4px;
}
.flex-center {
  display: inline-block;
  height: 30px;
  line-height: 30px;
}

.delete-icon {
  ::v-deep.el-tree-node__content {
    display: none !important;
  }
}

.tree-box {
  padding-bottom: 54px;
}
</style>
