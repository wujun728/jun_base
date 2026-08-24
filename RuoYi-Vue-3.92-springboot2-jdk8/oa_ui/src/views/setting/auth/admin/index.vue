<template>
  <div class="divBox">
    <el-card class="employees-card-bottom">
      <oaFromBox
        :isTotal="false"
        :isViewSearch="false"
        :sortSearch="false"
        :title="$route.meta.title"
        @addDataFn="addAdminRole"
      ></oaFromBox>
      <div class="table-box">
        <el-table :data="roleList" style="width: 100%">
          <el-table-column label="角色名称" prop="role_name" width="150px"> </el-table-column>
          <el-table-column label="管理范围" prop="position">
            <template slot-scope="{ row }">
              {{ getDataLevel(row.data_level) }}
              <span v-if="row.frame.length !== 0">
                (<span v-for="(item, index) in row.frame" :key="index"> {{ item.name }} </span>)
              </span>
            </template>
          </el-table-column>
          <el-table-column label="直属下级数据" prop="position" width="200px">
            <template slot-scope="{ row }">
              {{ row.directly == 0 ? '不包含' : '包含' }}
            </template>
          </el-table-column>
          <el-table-column label="使用成员数量" prop="user_count" width="150px"> </el-table-column>
          <el-table-column label="启用状态" prop="position" width="100px">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                :active-value="1"
                :inactive-value="0"
                :width="60"
                active-color="#1890ff"
                active-text="启用"
                inactive-color="#909399"
                inactive-text="禁用"
                @change="setRoleStatus(scope.row)"
              >
              </el-switch>
            </template>
          </el-table-column>

          <el-table-column fixed="right" label="操作" width="180">
            <template slot-scope="scope">
              <el-button type="text" @click="handelRole(scope.row)">管理成员</el-button>
              <el-button type="text" @click="handleEdit(scope.row.id)">编辑</el-button>
              <el-button type="text" @click="handleDeleteRole(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 管理人员弹窗 -->
      <el-drawer
        :before-close="handleClose"
        :visible.sync="drawer"
        :wrapperClosable="false"
        size="700px"
        title="管理人员"
      >
        <div slot="title" class="drawer-title">
          <span>管理人员</span>
          <select-member ref="selectMember" :value="tableList || []" @getSelectList="getSelectList" disabled="true">
            <template v-slot:custom>
              <el-button size="small" type="primary" @click="openDepartment">选择成员</el-button>
            </template>
          </select-member>
        </div>
        <div class="box">
          <el-table :data="tableList" style="width: 100%">
            <el-table-column label="ID" prop="id"> </el-table-column>
            <el-table-column label="姓名" prop="name"> </el-table-column>
            <el-table-column label="部门" prop="frame.name"> </el-table-column>
            <el-table-column label="启用状态" prop="position">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.status"
                  :active-text="$t('public.enable')"
                  :active-value="1"
                  :inactive-text="$t('public.disabled')"
                  :inactive-value="0"
                  @change="handleChange($event, scope.row)"
                >
                </el-switch>
              </template>
            </el-table-column>
            <el-table-column label="操作" prop="position" width="80px">
              <template slot-scope="scope">
                <el-button type="text" @click="handleDeleteUser(scope.row.id, scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-drawer>
    </el-card>

    <addAdminRole ref="adminRole" :edit-type="editType" :role-id="roleId" @adminRole="adminRole" />
  </div>
</template>

<script>
import {
  systemRoleStatusApi,
  systemRoleListApi,
  systemRoleUserListApi,
  systemRoleAddUserApi,
  systemRoleDeleteApi,
  systemRoleEditApi,
  systemRoleDeleteUserApi,
  systemRoleShowUserApi
} from '@/api/config'

export default {
  name: 'EnterpriseAdmin',
  components: {
    addAdminRole: () => import('@/views/setting/enterprise/components/addAdminRole'),
    selectMember: () => import('@/components/form-common/select-member'),
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  data() {
    return {
      memberShow: false,
      roleId: '',
      roles: '',
      roleList: [],
      tableList: [],
      total: 0,
      editType: 0, // 0 新增 1编辑
      drawer: false
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    async getList() {
      const result = await systemRoleListApi()
      this.roleList = result.data
      this.total = result.data.count
    },
    handelRole(item) {
      this.roleId = item.id
      this.getTableList('', item.id)
      this.drawer = true
    },
    handleClose() {
      this.drawer = false
    },
    getDataLevel(data) {
      let str = ''
      if (data == 1) {
        str = '仅本人'
      } else if (data == 2) {
        str = '本部门'
      } else if (data == 3) {
        str = '自定义部门'
      } else if (data == 5) {
        str = '直属下级'
      } else if (data == 4) {
        str = '全部数据'
      }
      return str
    },
    // 添加管理员身份
    addAdminRole() {
      this.editType = 0
      this.$refs.adminRole.openBox()
    },
    adminRole() {
      this.getList()
    },
    setRoleStatus(item) {
      let data = {
        status: item.status
      }
      systemRoleStatusApi(item.id, data).then((res) => {
        this.getList()
      })
    },
    getTableList(num, id) {
      this.page = num || 1
      systemRoleUserListApi(id, {
        page: this.page,
        limit: this.limit
      }).then((res) => {
        this.tableList = res.data.list
      })
    },
    // 编辑权限
    handleEdit(id) {
      this.roleId = id
      this.editType = 1
      systemRoleEditApi(id).then(({ data }) => {
        const tempObj = {}
        const childData = this.$refs.adminRole
        childData.ruleForm.role_name = data.rule.role_name
        childData.ruleForm.status = data.rule.status
        childData.ruleForm.directly = data.rule.directly
        childData.activeMastart = data.rule.frame
        childData.defaultCheckedKeys = data.rule.rules
        childData.rolesList = data.rule.apis
        childData.activeMastartObj = tempObj
        childData.ruleForm.data_level = JSON.stringify(data.rule.data_level)
        childData.openBox(data)
      })
    },
    // 删除权限
    async handleDeleteRole(row, index) {
      await this.$modalSure(this.$t('setting.admin.deletetitle'))
      await systemRoleDeleteApi(row.id)
      await this.getList()
    },
    getSelectList(data) {
      const frameId = []
      if (data.length === 0) {
        return false
      }
      data.forEach((el) => {
        frameId.push(el.value || el.id)
      })

      systemRoleAddUserApi({
        role_id: this.roleId,
        user_id: frameId,
        frame_id: []
      })
        .then((res) => {
          this.getTableList('', this.roleId)
          this.openStatus = false
        })
        .catch((error) => {
          this.openStatus = false
        })
    },
    // 打开选择部门成员
    openDepartment() {
      this.$refs.selectMember.handlePopoverShow()
    },
    // 成员切换状态
    handleChange(value, row) {
      systemRoleShowUserApi({
        uid: row.id,
        status: value,
        role_id: this.roleId
      }).then((res) => {})
    },
    // 删除成员
    async handleDeleteUser(id, index) {
      await this.$modalSure(this.$t('setting.admin.deletetitle2'))
      await systemRoleDeleteUserApi({
        uid: id,
        role_id: this.roleId
      })
      await this.getTableList('', this.roleId)
    },
    pageChange(num) {
      this.page = num
      this.getTableList(num)
    },
    handleSizeChange(num) {
      this.limit = num
      this.getTableList()
    }
  }
}
</script>

<style lang="scss" scoped>
.fromx {
  display: flex;
  justify-content: space-between;
}
.box {
  padding: 20px;
  //height: 100%;

  overflow-y: scroll;
}
.drawer-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
