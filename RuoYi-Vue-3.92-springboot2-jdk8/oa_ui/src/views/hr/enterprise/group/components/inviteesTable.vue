<template>
  <div>
    <!-- 被邀请人列表弹窗  -->
    <el-dialog
      :title="$t('public.list')"
      :visible.sync="dialogFormVisible"
      width="920px"
      :modal="true"
      :close-on-click-modal="true"
      custom-class="person"
    >
      <el-table :data="dataTable" style="width: 100%;">
        <el-table-column :label="$t('setting.headportrait')" min-width="100" align="center">
          <template slot-scope="scope">
            <img class="avatar" :src="scope.row.user.avatar" alt="" />
          </template>
        </el-table-column>
        <el-table-column prop="user.real_name" :label="$t('toptable.name')" min-width="100" />
        <el-table-column prop="user.phone" :label="$t('toptable.phone')" min-width="120" />
        <el-table-column prop="created_at" :label="$t('toptable.invitationtime')" min-width="150" />
        <el-table-column prop="user.phone" label="用户状态" min-width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === -1" type="warning" size="mini"> 待处理 </el-tag>
            <el-tag v-if="scope.row.status === 0" type="danger" size="mini"> 已拒绝 </el-tag>
            <el-tag v-if="scope.row.status === 1" type="info" size="mini"> 已同意 </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="user.phone" label="审核状态" min-width="100">
          <template slot-scope="scope" v-if="scope.row.status === 1">
            <el-tag v-if="scope.row.verify === -1" type="danger" size="mini"> 已拒绝 </el-tag>
            <el-tag v-if="scope.row.verify === 0" type="danger" size="mini"> 待审核 </el-tag>
            <el-tag v-if="scope.row.verify === 1" type="info" size="mini"> 已同意 </el-tag>
          </template>
        </el-table-column>
        <el-table-column width="160" :label="$t('toptable.operation')">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleDelete(scope.row.id, scope.$index)">
              {{ $t('public.delete') }}
            </el-button>
            <template v-if="scope.row.status == 1 && scope.row.verify == 0">
              <el-button type="text" size="small" @click="handleAgree(scope.row.id)">
                {{ $t('toptable.agree') }}
              </el-button>

              <el-button type="text" size="small" @click="handleRefuse(scope.row.id)">
                {{ $t('toptable.refuse') }}
              </el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :page-size="limit"
        :current-page="page"
        layout="total,prev, pager, next, jumper"
        :total="total"
        @current-change="pageChange"
      />
    </el-dialog>
  </div>
</template>

<script>
import { getApplyApi, applyDeleteApi, applyAgreeApi, applyRefuseApi } from '@/api/user'
export default {
  name: 'InviteesTable',
  filters: {
    filterStatus(status) {
      const obj = {
        '-1': '待审核',
        0: '拒绝',
        1: '同意'
      }
      return obj[status]
    }
  },
  data() {
    return {
      dialogFormVisible: false,
      dataTable: [],
      page: 1,
      limit: 15,
      total: 0
    }
  },
  methods: {
    open() {
      this.dialogFormVisible = true
      this.getList()
    },
    async getList() {
      const result = await getApplyApi({
        page: this.page,
        limit: this.limit
      })
      this.dataTable = result.data.list
      this.total = result.data.count
    },
    // 删除申请人
    handleDelete(id, index) {
      this.$modalSure(this.$t('setting.group.deleapply')).then(() => {
        applyDeleteApi(id).then((res) => {
          this.dataTable.splice(index, 1)
          this.getList()
        })
      })
    },
    // 同意申请人
    async handleAgree(id) {
      await applyAgreeApi(id)
      this.getList()
      this.handleEmit()
      await this.$emit('getQuantity')
    },
    // 拒绝申请人
    async handleRefuse(id) {
      await applyRefuseApi(id)
      this.getList()
      this.handleEmit()
      await this.$emit('getQuantity')
    },
    pageChange(num) {
      this.page = num
      this.getList()
    },
    handleEmit() {
      this.$emit('handleInvitees')
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep.el-table__body-wrapper {
  height: 500px;
  overflow-y: auto;
}
::-webkit-scrollbar-thumb {
  -webkit-box-shadow: inset 0 0 6px #ccc;
}
::-webkit-scrollbar {
  width: 4px !important; /*对垂直流动条有效*/
}
.avatar {
  width: 40px;
  height: 40px;
}
</style>
