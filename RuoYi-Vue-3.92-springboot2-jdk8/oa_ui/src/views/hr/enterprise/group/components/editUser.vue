<template>
  <div>
    <!-- 编辑用户弹窗 -->
    <el-drawer
      :before-close="handleClose"
      :direction="direction"
      :modal="true"
      :visible.sync="drawer"
      :with-header="false"
      :wrapper-closable="true"
      :wrapperClosable="false"
      size="700px"
      title="组织架构"
    >
      <div class="container">
        <div class="table-box">
          <span @click="activeName = 0">
            {{ $t('setting.edit.editorialmembers') }}
          </span>
          <span v-if="false" :class="{ on: activeName == 1 }" @click="activeName = 1">
            {{ $t('setting.edit.permissionsetting') }}
          </span>
          <i class="el-icon-close" @click="handleClose" />
        </div>
        <div v-show="activeName == 0">
          <div v-if="userInfo" class="form-box ml20">
            <el-form ref="ruleForm" :model="userInfo" :rules="rules" class="demo-ruleForm" label-width="auto">
              <el-form-item :label="$t('setting.edit.membername') + '：'" prop="name">
                <el-input v-model="userInfo.name" clearable size="small" />
              </el-form-item>
              <el-form-item :label="$t('setting.edit.phone') + '：'" prop="phone">
                <el-input v-model="userInfo.phone" clearable size="small" />
              </el-form-item>
              <el-form-item label="部门：" prop="name">
                <select-department
                  ref="selectDepartment"
                  :is-site="true"
                  :value="frames || []"
                  @changeMastart="changeMastart"
                >
                  <template v-slot:custom>
                    <div class="item-box">
                      <el-popover
                        v-for="(item, index) in frames"
                        :key="index"
                        :ref="`popover-${index}`"
                        :width="80"
                        class="item"
                        placement="bottom-end"
                        trigger="click"
                      >
                        <div>
                          <div class="prop-txt" @click="handleDepartment(item, index)">
                            {{ $t('setting.edit.setdepartment') }}
                          </div>
                          <div class="prop-txt" @click="handleDeleteDep(index)">{{ $t('public.delete') }}</div>
                        </div>
                        <el-button slot="reference" size="small">
                          {{ item.name }}
                          <span v-if="item.is_mastart" style="color: #1890ff; font-size: 12px"> (主) </span>
                          <i class="el-icon-arrow-right el-icon--right" />
                        </el-button>
                      </el-popover>
                      <el-button class="item" size="small" type="text" @click="handleOpen"> 修改 </el-button>
                    </div>
                  </template>
                </select-department>
              </el-form-item>
              <el-form-item :label="$t('setting.edit.position') + '：'" prop="position">
                <el-cascader
                  v-model="userInfo.position"
                  :options="treeData"
                  :props="propsPos"
                  size="small"
                ></el-cascader>
              </el-form-item>
              <el-form-item label="部门负责人：" prop="name">
                <div>
                  <el-radio-group v-model="userInfo.is_admin">
                    <el-radio :label="0">{{ $t('customer.no') }}</el-radio>
                    <el-radio :label="1">{{ $t('customer.yes') }}</el-radio>
                  </el-radio-group>
                </div>
              </el-form-item>
              <el-form-item v-if="userInfo.is_admin" label="负责部门：" prop="manage_frame">
                <el-select
                  v-model="userInfo.manage_frame"
                  multiple
                  placeholder="请选择"
                  size="small"
                  style="width: 100%"
                >
                  <el-option v-for="item in frames" :key="item.id" :label="item.name" :value="item.id"> </el-option>
                </el-select>
              </el-form-item>

              <el-form-item :label="$t('setting.edit.directsuperior') + '：'" prop="name">
                <select-member
                  ref="selectMember"
                  :only-one="true"
                  :value="superiorUser || []"
                  @getSelectList="getSelectList"
                >
                  <template v-slot:custom>
                    <div>
                      <el-button v-if="superiorUser.length > 0" size="small">
                        {{ superiorUser[0].name }}
                      </el-button>
                      <el-button size="small" type="text" @click="handlesuperiorOpen">
                        {{ superiorUser.length > 0 ? $t('public.modify') : $t('public.add') }}
                      </el-button>
                    </div>
                  </template>
                </select-member>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
      <div class="from-foot-btn fix btn-shadow">
        <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
        <el-button size="small" type="primary" :loading="loading" @click="onSubmit">{{ $t('public.save') }}</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { userCardApi, userHrUpdateApi } from '@/api/user'
import { jobsCreate } from '@/api/enterprise'

export default {
  name: 'EditUser',
  components: {
    selectMember: () => import('@/components/form-common/select-member'),
    selectDepartment: () => import('@/components/form-common/select-department')
  },
  props: {
    userId: {
      type: [String, Number],
      default: 0
    }
  },
  data() {
    return {
      memberShow: false,
      loading: false,
      departmentShow: false,
      drawer: false,
      direction: 'rtl',
      activeName: 0,
      ruleForm: {
        name: ''
      },
      rules: {},
      options: [],
      value1: '',
      userInfo: null,
      pageData: {},
      frames: [], // 部门
      superiorFrame: [], // 上级部门
      superiorUser: [], // 上级人员
      formBoxConfig: {
        title: this.$t('setting.edit.addmembers'),
        width: '500px',
        method: 'post',
        action: '/user/create'
      },
      rolesConfig: [],
      defaultCheckedKeys: [], // 权限选中
      rolesList: {},
      mastart_id: '', // 主部门,
      rangeUser: [],
      choiceEdit: 0,
      treeData: [],
      propsPos: { value: 'id', label: 'name', multiple: false, checkStrictly: true }
    }
  },
  watch: {
    userId: {
      handler(nVal, oVal) {
        if (nVal) {
          this.getUserinfo()
          this.getTreeData()
        }
      },
      deep: true
    },
    frames: {
      handler(nVal, oVal) {
        if (nVal) {
          const preMastartId = this.mastart_id
          nVal.forEach((el) => {
            if (el.is_mastart) {
              this.mastart_id = el.id
              this.superiorFrame = this.superiorFrame.map((i) => {
                return i.id === preMastartId ? el : i
              })
            }
          })
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
    },
    getStockSymbols(stocks) {
      var symbols = []
      stocks.forEach(function (stock) {
        symbols.push(stock.name)
      })
      return symbols
    },
    unique(arr) {
      const res = new Map()
      return arr.filter((arr) => !res.has(arr.id) && res.set(arr.id, 1))
    },

    // 获取组件架构详情
    getUserinfo() {
      if (!this.userId) return
      userCardApi(this.userId).then((res) => {
        this.userInfo = res.data

        this.superiorUser = []
        this.rangeUser = []
        if (res.data.superior_uid > 0 && res.data.superior_name) {
          this.superiorUser.push({
            name: res.data.superior_name,
            value: res.data.superior_uid,
            avatar: res.data.superior_avatar
          })
        }

        if (res.data.job) {
          this.userInfo.position = [res.data.job]
        } else {
          this.userInfo.position = []
        }
        // if (res.data.scope.length > 0) {
        //   res.data.scope.map((value) => {
        //     value.frames.map((val) => {
        //       this.rangeUser.push(val)
        //     })
        //   })
        // }
        this.frames = res.data.frames
        let mastart_id = '' // 主部门id
        let mastartData = {} // 主部门数据
        this.frames.forEach((el) => {
          if (el.is_mastart) {
            mastart_id = el.id
            mastartData = el
            this.mastart_id = el.id
          }
        })
        const result = this.superiorFrame.some((item) => {
          if (item.id == mastart_id) {
            return true
          }
        })
        if (!result) {
          this.superiorFrame.push(mastartData)
        }
        this.pageData = res.data
        // this.arrToObj()
      })
    },
    // 获取职位tree数据
    async getTreeData() {
      const result = await jobsCreate()
      this.treeData = result.data.tree
    },

    //  选择部门
    handleOpen() {
      this.$refs.selectDepartment.handlePopoverShow()
    },
    // 选择部门回调
    changeMastart(data) {
      this.frames = data
    },
    // 选择人员回调
    getSelectList(data) {
      this.superiorUser = data
    },
    handlesuperiorOpen() {
      this.$refs.selectMember.handlePopoverShow()
    },

    // 保存
    async onSubmit() {
      var superior_frame_id = null
      var frames = []
      const frame_id = [] // 部门id
      let mastart_id = '' // 主部门id
      this.frames.forEach((el) => {
        frame_id.push(el.id)
        if (el.is_mastart) {
          mastart_id = el.id
        }
      })
      if (this.superiorUser.length > 0) {
        superior_frame_id = this.superiorUser[0].value
      }
      if (mastart_id === '') {
        return this.$message.error(this.$t('setting.edit.text3'))
      }
      if (frame_id.length == 1 && mastart_id == '') {
        this.userInfo.mastart_id = frame_id[0]
      } else {
        this.userInfo.mastart_id = mastart_id
      }
      this.userInfo.frame_id = frame_id
      this.userInfo.superior_uid = superior_frame_id

      if (this.userInfo.position && this.userInfo.position.length > 0) {
        this.userInfo.position = this.userInfo.position[this.userInfo.position.length - 1]
      }
      if (this.rangeUser.length > 0) {
        this.rangeUser.map((value) => {
          frames.push(value.id)
        })
      }
      this.userInfo.frames = frames
      this.loading = true
      await userHrUpdateApi(this.userId, this.userInfo)
      this.drawer = false
      this.loading = false
      await this.$parent.handleTree()
    },

    // 设为主部门
    handleDepartment(item, index) {
      this.$refs[`popover-${index}`][0].doClose()
      this.frames.forEach((el) => {
        el.is_mastart = false
      })
      item.is_mastart = true
      this.mastart_id = item.id
    },
    // 删除所在部门
    handleDeleteDep(index) {
      this.$refs[`popover-${index}`][0].doClose()
      this.frames.splice(index, 1)
    },
    handleDelSuperior(index) {
      this.$refs[`popoverSuperior-${index}`][0].doClose()
      this.superiorUser.splice(index, 1)
    },
    cardTag(index) {
      this.rangeUser.splice(index, 1)
    }
  }
}
</script>

<style lang="scss" scoped>
.table-box {
  height: 52px;
  line-height: 52px;
  padding: 0 30px;
  font-size: 15px;
  font-weight: 600;
  border-bottom: 1px solid #eeeeee;

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
    // margin-bottom: 10px;
  }
}
::v-deep.el-popover {
  min-width: 80px;
}
::v-deep .el-cascader {
  width: 100%;
}
.form-item-section {
  width: 100%;
  height: 32px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  padding: 0 15px;
  display: flex;
  align-items: center;
  span {
    margin-right: 5px;
    &:last-of-type {
      margin-right: 0;
    }
  }
}
.prop-txt {
  height: 30px;
  line-height: 30px;
  font-size: 13px;
  cursor: pointer;
}
::v-deep.el-icon-close {
  cursor: pointer;
}
</style>
