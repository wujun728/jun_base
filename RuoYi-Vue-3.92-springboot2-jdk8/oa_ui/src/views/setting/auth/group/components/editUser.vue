<template>
  <div>
    <el-drawer
      title="用户权限"
      :visible.sync="drawer"
      :direction="direction"
      :before-close="handleClose"
      :modal="true"
      :wrapper-closable="true"
      :with-header="false"
      size="700px"
    >
      <div class="container">
        <div class="table-box">
          <span> 编辑权限 </span>
          <i class="el-icon-close" @click="handleClose" />
        </div>

        <div class="tree-box">
          <div v-if="userInfo" class="form-box">
            <el-form ref="ruleForm" :model="userInfo" :rules="rules" label-width="90px" class="demo-ruleForm">
              <el-form-item label="用户角色：">
                <div>
                  <el-select
                    v-model="pageData.roles"
                    multiple
                    size="small"
                    :placeholder="$t('setting.edit.pleaseselect')"
                    @change="handleRoles"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="item in pageData.roleList"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </div>
                <span class="tips">{{ $t('setting.edit.text2') }}</span>
              </el-form-item>
            </el-form>
          </div>
          <rolesTree
            ref="rolesTree"
            :tree-data="pageData.menus"
            :default-checked-keys="defaultCheckedKeys"
            :roles-list="rolesList"
            :is-admin="true"
          />
        </div>
      </div>
      <div class="from-foot-btn fix btn-shadow">
        <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
        <el-button size="small" type="primary" @click="onSubmit">{{ $t('public.save') }}</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { systemUserRoleApi, userEntCardApi } from '@/api/user'
import rolesTree from '@/components/setting/rolesTree'
import { jobsCreate } from '@/api/enterprise'
export default {
  name: 'EditUser',
  components: {
    rolesTree
  },
  props: {
    userId: {
      type: [String, Number],
      default: 0
    }
  },
  data() {
    return {
      drawer: false,
      direction: 'rtl',
      activeName: 0,
      ruleForm: {
        name: ''
      },
      rules: {},
      options: [],
      userInfo: null,
      pageData: {},
      openStatus: false,
      defaultCheckedKeys: [], // 权限选中
      checkedKeys: [], // 权限选中
      rolesList: [],
      isEdit: 0,
      treeData: [],
      propsPos: { value: 'id', label: 'name', multiple: false, checkStrictly: true }
    }
  },
  watch: {
    userId: {
      handler(nVal, oVal) {
        if (nVal) {
          this.getUserinfo()
          // this.getTreeData();
        }
      },
      deep: true
    }
  },
  methods: {
    handleClose() {
      this.userInfo = null
      this.drawer = false
      this.$parent.userId = ''
    },
    open() {
      this.drawer = true
      // this.getUserinfo()
    },
    getUserinfo() {
      if (!this.userId) return
      userEntCardApi(this.userId).then((res) => {
        this.userInfo = {}
        this.pageData = res.data

        this.getRolesList()
      })
    },
    handleRoles() {
      this.getRolesList()
    },
    // 判断管理员所有角色
    getRolesList() {
      let roleArr = []
      let api = {}
      if (this.pageData.roles.length > 0) {
        this.pageData.roles.map((value) => {
          this.pageData.roleList.map((val) => {
            if (value === val.value) {
              roleArr.push(val.rules)
              Object.assign(api, val.apis)
            }
          })
        })
        roleArr = this.applyArray(roleArr)
        this.defaultCheckedKeys = this.checkedKeys.concat(roleArr)

        this.rolesList = api
        this.$refs.rolesTree.checkList = []
      } else {
        this.defaultCheckedKeys = this.checkedKeys
      }
    },
    uniqueArray(arr) {
      return Array.from(new Set(arr))
    },
    applyArray(arr) {
      return [].concat.apply([], arr)
    },
    // 获取职位tree数据
    getTreeData() {
      jobsCreate().then((res) => {
        this.treeData = res.data.tree
      })
    },
    // 保存
    async onSubmit() {
      this.userInfo.user_id = this.userId
      this.userInfo.role_id = this.pageData.roles
      systemUserRoleApi(this.userInfo).then((res) => {
        this.drawer = false
        this.$parent.getList()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.table-box {
  height: 52px;
  line-height: 52px;
  padding: 0 30px;
  font-size: 14px;
  border-bottom: 1px solid #d8d8d8;

  span {
    margin-right: 26px;
  }
  i {
    float: right;
    margin-top: 18px;
  }
}
.tips {
  font-size: 13px;
  color: #999;
}
.form-box {
  padding: 30px 30px 0 0;
}
.item-box {
  display: flex;
  flex-wrap: wrap;
  .item {
    margin-left: 0 !important;
    margin-right: 10px;
    margin-bottom: 10px;
  }
}
::v-deep.el-popover {
  min-width: 80px;
}
::v-deep .el-cascader--medium {
  width: 100%;
}
.prop-txt {
  height: 30px;
  line-height: 30px;
  font-size: 13px;
  cursor: pointer;
}
</style>
