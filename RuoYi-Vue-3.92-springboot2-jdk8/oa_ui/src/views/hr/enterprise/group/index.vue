<template>
  <div class="divBox">
    <!-- 组织架构页面 -->
    <el-card>
      <el-row>
        <el-col v-bind="gridl" class="p20">
          <tree :parent-tree="treeData" :frame-id="frame_id" @frameId="getFrameId" />
        </el-col>
        <el-col v-bind="gridr" class="boder-left p20">
          <div class="table-box ml20">
            <div class="header-16">
              <div class="title-16">
                员工列表
                <el-popover placement="right" trigger="hover" popper-class="monitor-yt-popover">
                  <div class="prompt-bag">
                    <p>
                      {{ $t('setting.group.text') }}
                      <span @click="goTheJob" style="color: #1890ff; cursor: pointer">{{
                        $t('setting.group.text2')
                      }}</span>
                      {{ $t('setting.group.text3') }}
                    </p>
                    <p>
                      {{ $t('setting.group.title2') }}
                      <span @click="goTheEnterprise" style="color: #1890ff; cursor: pointer">企业信息 </span>中进行修改
                    </p>
                    <p>{{ $t('setting.group.title3') }}</p>
                  </div>
                  <i class="el-icon-question" slot="reference"></i>
                </el-popover>
              </div>
              <el-button type="primary" size="small" icon="el-icon-plus" @click="openBox('addDrawerIsShow', 'add')"
                >新增</el-button
              >
            </div>
            <div class="flex lh-center mt16">
              <div class="total-16">共 {{ total }} 条</div>
              <el-input
                prefix-icon="el-icon-search"
                clearable
                placeholder="请输入姓名、电话号码"
                v-model="formData.name"
                @change="handleSearch"
                style="width: 250px"
                size="small"
                @keyup.enter.native="handleSearch"
              >
              </el-input>
            </div>
            <div class="table-container" v-loading="loading">
              <el-table
                ref="multipleTable"
                :data="tableData"
                tooltip-effect="dark"
                style="width: 100%"
                :height="tableHeight"
                @selection-change="handleSelectionChange"
              >
                <el-table-column prop="name" :label="$t('toptable.name')" min-width="100">
                  <template slot-scope="scope">
                    <div class="flex lh-center">
                      <img :src="scope.row.avatar" alt="" class="img" />
                      <div>{{ scope.row.name }}</div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="frame.name" :label="$t('toptable.department')" min-width="140">
                  <template slot-scope="scope">
                    <div class="frame-name over-text" v-for="(item, index) in scope.row.frames" :key="index">
                      <span class="icon-h">
                        {{ item.name
                        }}<span v-show="item.is_mastart === 1 && scope.row.frames.length > 1" title="主部门">(主)</span>
                        <span v-show="item.is_admin == 1" title="主管" class="guan">(管)</span>
                      </span>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="job.name" :label="$t('toptable.post')" min-width="110" />
                <el-table-column prop="phone" :label="$t('toptable.phone')" min-width="110" />
                <el-table-column prop="join_time" :label="$t('toptable.jointime')" min-width="100" />
                <el-table-column :label="$t('public.operation')" width="120">
                  <template slot-scope="scope">
                    <el-button type="text" @click="onEdit(scope.row.id)" v-hasPermi="['hr:enterprise:group:edit']">
                      {{ $t('public.edit') }}
                    </el-button>

                    <el-button type="text" @click="onDelete(scope.row.id, scope.$index)" v-if="!scope.row.uid">
                      {{ $t('public.delete') }}
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="page-fixed">
                <el-pagination
                  :page-size="formData.limit"
                  :current-page="formData.page"
                  :page-sizes="[15, 20, 30]"
                  layout="total,sizes, prev, pager, next, jumper"
                  :total="total"
                  @current-change="pageChange"
                  @size-change="handleSizeChange"
                />
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!--  添加人员弹窗/批量导入  -->
    <updataXls ref="personBox" :person-config="personConfig" :frame-id="frame_id" />
    <!--  被邀请人列表弹窗  -->
    <inviteesTable ref="inviteesTableBox" @handleInvitees="handleInvitees" @getQuantity="getQuantity" />
    <!--  编辑用户  -->
    <editUser ref="editUser" :user-id="userId" />
    <!-- 通用弹窗表单   -->
    <dialogForm ref="dialogForm" :roles-config="rolesConfig" :form-data="formBoxConfig" @isOk="isOk" />
    <!-- 人员新增/修改  -->
    <UserDetails :tabtypes="1" :actionType="``" @getList="handleTree" ref="userDetails" />
  </div>
</template>

<script>
import { configFrameApi } from '@/api/setting'
import { userListApi } from '@/api/user'
import { roterPre } from '@/settings'
import { enterpriseCardDeleteApi, getQuantity } from '@/api/enterprise'
export default {
  name: 'Index',
  components: {
    tree: () => import('./components/tree'),
    dialogForm: () => import('./components/index'),
    editUser: () => import('./components/editUser'),
    updataXls: () => import('./components/updataXls'),
    inviteesTable: () => import('./components/inviteesTable'),
    // 档案信息弹窗
    UserDetails: () => import('@/views/hr/archives/components/userDetails.vue')
  },
  data() {
    return {
      treeData: [],
      loading: false,
      tableData: [],
      frame_id: 0,
      num: 0,
      personConfig: {},
      total: 0,
      formData: {
        page: 1,
        limit: 15,
        name: ''
      },

      gridl: {
        xl: 3,
        lg: 4,
        md: 5,
        sm: 6,
        xs: 24
      },
      gridr: {
        xl: 21,
        lg: 20,
        md: 19,
        sm: 18,
        xs: 24
      },
      rolesConfig: [],
      formBoxConfig: {
        title: this.$t('setting.box1'),
        width: '500px',
        method: 'post',
        action: '/user/create'
      },
      userId: '', // 用户id
      indexType: null
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  beforeDestroy() {
    this.$bus.$off()
  },
  created() {
    this.getList()
  },
  mounted() {
    this.$bus.$on('getList', () => {
      this.getList()
      this.$store.dispatch('user/getDepartment')
    })
  },
  methods: {
    // 获取选中id
    getFrameId(data) {
      this.frame_id = data
      this.formData.page = 1
      this.getTableList()
    },
    openBox(componentName, type) {
      this.$refs.userDetails.init({ componentName, type })
    },
    goTheJob() {
      this.$router.push({
        path: `${roterPre}/hr/archives/onTheJob`
      })
    },
    goTheEnterprise() {
      this.$router.push({
        path: `${roterPre}/setting/enterprise/info/basic`
      })
    },
    // 获取待审核数量
    async getQuantity() {
      let type = 'inviter_review'
      const result = await getQuantity(type)
      this.num = result.data.num
    },

    // 获取权限列表
    async getList() {
      const result = await configFrameApi()
      this.treeData = result.data
      await this.getTableList()
    },
    // 操作关闭
    handleClose() {
      this.isOpen = false
    },
    roleChange() {},
    isOk(data) {
      if (data) {
        this.rolesConfig[0].control[0].rule[1].value = data.url
        const oInput = document.createElement('input')
        const value =
          '【' +
          data.name +
          '】邀请你加入【' +
          this.$store.state.user.enterprise.enterprise_name +
          '】办公系统，请点击链接使用手机号验证登录 ' +
          data.url +
          ' 链接有效期为7天，请尽快登录加入团队！'
        oInput.value = value
        document.body.appendChild(oInput)
        oInput.select()
        document.execCommand('Copy')
        oInput.style.display = 'none'
        document.body.removeChild(oInput)
      }
      this.$refs.dialogForm.handleClose()
    },
    // 添加人员
    // addPerson(type) {
    //   switch (type) {
    //     case 1:
    //       this.personConfig.title = this.$t('setting.group.addpersonnel')
    //       break
    //     case 2:
    //       this.personConfig.title = this.$t('setting.group.batchpersonnel')
    //       break
    //   }
    //   this.personConfig.type = type
    //   this.$refs.personBox.open()
    // },
    // 被邀请人列表
    inviteesList() {
      this.$refs.inviteesTableBox.open()
    },
    // table 选中
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 企业成员
    getTableList() {
      this.loading = true
      userListApi({
        pid: this.frame_id,
        page: this.formData.page,
        limit: this.formData.limit,
        name: this.formData.name
      }).then((res) => {
        this.tableData = res.data.list
        this.loading = false
        if (this.tableData && this.tableData.length > 0) {
          this.tableData.map((value) => {
            if (value.frames.length > 1) {
              value.frames.sort((a, b) => {
                return b.is_mastart - a.is_mastart
              }) //升序
            }
          })
        }
        this.total = res.data.count
      })
    },
    handleInvitees() {
      this.handleSearch()
    },
    handleSearch() {
      this.formData.page = 1
      this.getTableList()
    },
    // 分页
    pageChange(page) {
      this.formData.page = page
      this.getTableList()
    },
    handleSizeChange(size) {
      this.formData.limit = size
      this.getTableList()
    },
    // 编辑用户
    onEdit(id) {
      this.userId = id
      this.$refs.editUser.open()
    },
    // 设置所在部门
    openDepartment(type) {
      this.indexType = type
      if (type === 1) {
        this.isSite = true
        this.onlyDepartment = false
        const selection = this.$refs.multipleTable.selection
        if (!selection.length) return this.$message.error(this.$t('setting.group.selectmember'))
      } else {
        this.isSite = false
        this.onlyDepartment = true
      }
      this.$refs.department.reseatData()
      this.openStatus = true
    },

    handleTree() {
      setTimeout(() => {
        this.getList()
      }, 1000)
    },
    //  删除人员
    onDelete(id, index) {
      this.$modalSure(this.$t('setting.group.deletitle')).then(() => {
        enterpriseCardDeleteApi(id).then((res) => {
          this.tableData.splice(index, 1)
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.divBox {
  height: 100%;
}
.mt16 {
  margin-top: 7px;
}
.img {
  display: block;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  margin-right: 4px;
}

.prompt-bag {
  background-color: #edf5ff;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 12px;
  color: #606266;
}
::v-deep.el-button--small {
  font-size: 13px;
}

.el-icon-question {
  cursor: pointer;
  color: #1890ff;
  font-size: 15px;
}
::v-deep .el-card__body {
  padding: 0 20px;
}
.iconzhuyaobumen {
  color: #ff9900;
}
.boder-left {
  min-height: calc(100vh - 77px);
  border-left: 1px solid #eeeeee;
}
.p20 {
  padding: 20px 0;
}

.table-container {
  margin-top: 10px;
}

.frame-name {
  .iconfont {
    padding-right: 6px;
  }
}
.icon-h {
  position: relative;
  & > span {
    color: #1890ff;
  }
  .guan {
    color: #ff9900;
  }
}
.icon {
  position: absolute;
  top: 0;
  right: -15px;
  display: inline-block;
  width: 15px;
  height: 15px;
  font-size: 10px;
  font-weight: 500;
  text-align: center;
  line-height: 15px;
  color: #fff;
  border-radius: 50%;
  background-color: #ff9900;
}
::v-deep.select-bar {
  line-height: 32px;
}
</style>
<style>
.monitor-yt-popover {
  background: #edf5ff;
  border: 1px solid #97c3ff;
  padding: 13px 15px 6px 15px;
}
</style>
