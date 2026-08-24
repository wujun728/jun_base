<template>
  <div>
    <el-drawer
      :before-close="handleClose"
      :destroy-on-close="true"
      :direction="direction"
      :modal="true"
      :title="config.title"
      :visible.sync="drawer"
      :with-header="false"
      :wrapper-closable="true"
      size="700px"
    >
      <div class="role-box">
        <div class="table-box">
          <span :class="{ on: activeName == 0 }" @click="activeName = 0">{{ config.title }}</span>
          <i class="el-icon-close" @click="handleClose" />
        </div>
        <div v-show="activeName == 0" class="card-box">
          <el-form ref="form" :model="formData" :rules="rules" label-width="80px" size="small">
            <el-form-item :label="$t('setting.edit.departmentname') + '：'" prop="name">
              <el-input
                v-model="formData.name"
                :placeholder="$t('setting.edit.inputcontent')"
                class="input-item"
                size="small"
              />
            </el-form-item>
            <el-form-item label="父级部门：">
              <el-cascader
                v-model="formData.path"
                :disabled="config.disabled"
                :options="parentTreeList"
                :props="{ checkStrictly: true }"
                size="small"
                style="width: 100%"
                @change="handleChange"
              />
            </el-form-item>
            <el-form-item label="默认角色：" prop="role_id">
              <el-select v-model="formData.role_id" placeholder="请选择默认角色">
                <el-option v-for="item in roleList" :key="item.id" :label="item.role_name" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="部门主管：">
              <el-select
                v-model="frameValue"
                :placeholder="$t('setting.edit.selectmembers')"
                disabled
                multiple
                size="small"
                @change="handleChangeframe"
              >
                <el-option v-for="item in frameOptions" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="部门排序：">
              <el-input-number
                v-model="formData.sort"
                :min="0"
                :pprecision="0"
                controls-position="right"
                size="small"
                @change="handleChange"
              ></el-input-number>
            </el-form-item>
            <el-form-item :label="$t('setting.edit.departmentintroduction') + '：'">
              <el-input
                v-model="formData.introduce"
                :autosize="{ minRows: 6 }"
                maxlength="250"
                placeholder="请输入部门简介"
                resize="none"
                show-word-limit
                type="textarea"
              >
              </el-input>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="from-foot-btn fix btn-shadow">
        <el-button size="small" @click="close">{{ $t('public.cancel') }}</el-button>
        <el-button size="small" type="primary" @click="onSubmit">{{ $t('public.save') }}</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { configFrameCreateApi, configFrameUpdataApi, configFrameEditApi, frameUpdataApi } from '@/api/setting'
import { systemRoleListApi } from '@/api/config'
export default {
  name: 'RoleBox',
  components: {},
  props: {
    frameId: {
      type: Number,
      default: () => {
        return 0
      }
    },
    config: {
      type: Object,
      default: function () {
        return {}
      }
    },
    parentTree: {
      type: Array,
      default: function () {
        return []
      }
    },
    parentId: {
      type: Array,
      default: function () {
        return []
      }
    }
  },
  data() {
    return {
      drawer: false,
      direction: 'rtl',
      activeName: '0',
      formData: {
        name: '',
        path: [],
        sort: 0,
        user_id: [],
        introduce: '',
        rules: [],
        is_show: 1,
        other: {},
        role_id: ''
      },
      defaultProps: {
        children: 'children',
        label: 'label',
        disabled: function (data, node) {
          return data.status
        }
      },
      rules: {
        role_id: [{ required: true, message: '请选择默认角色', trigger: 'change' }],
        name: [{ required: true, message: '请填写部门名称', trigger: 'blur' }]
      },
      treeList: [],
      treeRoleData: null, // 权限数据
      defaultCheckedKeys: [], // 权限选中
      rolesList: {}, // 权限复选框选中
      parentTreeList: [],
      frameOptions: [],
      frameValue: [],
      departmentObj: {
        userList: [], // 选择部门的成员
        frames: []
      },
      roleList: []
    }
  },
  watch: {
    parentId(nVal, oVal) {
      this.formData.path = nVal
    }
  },
  mounted() {
    this.getRoleList()
  },
  methods: {
    // 获取角色列表
    async getRoleList() {
      const result = await systemRoleListApi()
      this.roleList = result.data
      // this.total = result.data.count
    },
    handelOpen() {
      this.drawer = true
      if (this.config.type) {
        this.getFrameEdit()
      } else {
        this.getParentTree()
      }
    },
    handleClose(done) {
      this.close()
      done()
    },
    close() {
      this.formData = {
        name: '',
        path: [],
        sort: 0,
        user_id: [],
        introduce: '',
        rules: [],
        is_show: 1
      }
      this.departmentObj.userList = []
      this.drawer = false
    },
    async getDepartmentHead() {
      this.frameOptions = []
      this.frameValue = []
    },
    handleChangeframe(e) {
      if (e.length > 1) {
        e.splice(0, 1)
      }
    },
    handleChange() {},
    async onSubmit() {
      this.formData.user_id = ''
      this.formData.user = this.departmentObj.userList
      this.formData.user_id = this.frameValue[0]
      this.formData.other = {
        userList: this.departmentObj.userList,
        frames: this.departmentObj.frames
      }
      if (this.config.type) {
        frameUpdataApi(this.config.id, this.formData).then((res) => {
          if (res.status == 200) {
            this.drawer = false
            this.departmentObj.userList = []
            this.$bus.$emit('getList')
          }
        })
      } else {
        configFrameUpdataApi(this.formData).then((res) => {
          if (res.status == 200) {
            if (this.activeName == 0) {
              this.formData.name = ''
              this.formData.introduce = ''
            }
            this.departmentObj.userList = []
            this.drawer = false
            this.$bus.$emit('getList')
          }
        })
      }
    },
    // 新增子菜单获取权限
    async getParentTree() {
      const result = await configFrameCreateApi()
      this.treeRoleData = result.data
      this.defaultCheckedKeys = result.data.rules
      this.rolesList = result.data.apis
      this.parentTreeList = result.data.tree
    },
    unique(arr) {
      const res = new Map()
      return arr.filter((arr) => !res.has(arr.id) && res.set(arr.id, 1))
    },
    // 获取编辑子菜单的内容和权限
    async getFrameEdit() {
      this.frameValue = []
      const res = await configFrameEditApi(this.config.id)
      res.data.frameInfo.path = [...new Set(res.data.frameInfo.path)]
      this.formData = res.data.frameInfo

      this.parentTreeList = res.data.tree
      if (res.data.frameInfo.super) {
        this.departmentObj.userList = [res.data.frameInfo.super]
        this.frameOptions = [res.data.frameInfo.super]
        this.frameValue.push(res.data.frameInfo.super.id)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-select,
::v-deep .el-input-number {
  width: 100%;
}
::v-deep .el-input-number {
  input {
    text-align: left;
  }
}
::v-deep.el-form-item__label {
  width: 88px !important;
}
::v-deep.el-form-item__content {
  margin-left: 88px !important;
}
.table-box {
  height: 52px;
  line-height: 52px;
  padding: 0 30px;
  font-size: 14px;
  border-bottom: 1px solid #d8d8d8;

  span {
    margin-right: 26px;
    cursor: pointer;

    &.on {
      color: #1890ff;
    }
  }

  i {
    float: right;
    margin-top: 18px;
  }
}

.card-box {
  padding: 24px 48px;
}

.input-box {
  display: flex;
  align-items: center;
  font-size: 13px;
  margin-bottom: 20px;

  .input-item {
    width: 100%;
  }
}

.tree-wrapper {
  display: flex;
  margin-top: 20px;

  .tree-box {
    display: flex;

    .tree-con {
      width: 230px;
      border-right: 1px solid #eeeeee;
    }

    .check-box {
      padding-left: 38px;

      .check-item {
        margin-bottom: 10px;
      }
    }
  }
}

.label-txt {
  height: 40px;
  line-height: 40px;
  font-size: 13px;
}
.flex-box {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  height: 100%;
  .item {
    margin-right: 10px;
  }
}
.select {
  height: auto;
  line-height: 1;
  padding: 8px 10px;
}
</style>
