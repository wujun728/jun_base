<template>
  <div>
    <!-- 被邀请人列表 -->
    <el-dialog
      :title="$t('public.list')"
      :visible.sync="dialogFormVisible"
      width="815px"
      :modal="false"
      :close-on-click-modal="false"
      custom-class="person"
    >
      <el-table :data="dataTable" style="width: 100%;">
        <el-table-column prop="created_at" :label="$t('toptable.invitationtime')" min-width="160" />
        <el-table-column :label="$t('setting.headportrait')" min-width="120">
          <template slot-scope="scope">
            <img class="avatar" :src="scope.row.user.avatar" alt="" />
          </template>
        </el-table-column>
        <el-table-column prop="user.real_name" :label="$t('toptable.name')" min-width="120" />
        <el-table-column prop="user.phone" :label="$t('toptable.phone')" min-width="120" />
        <el-table-column prop="user.phone" :label="$t('toptable.state')" min-width="80">
          <template slot-scope="scope">
            <span v-if="scope.row.status == -1">{{ $t('toptable.audit') }}</span>
            <span v-if="scope.row.status == 0">{{ $t('toptable.refuse') }}</span>
            <span v-if="scope.row.status == 1">{{ $t('toptable.agree') }}</span>
          </template>
        </el-table-column>
        <el-table-column width="100" :label="$t('toptable.operation')" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleDelete(scope.row.id, scope.$index)">{{
              $t('public.delete')
            }}</el-button>
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
import { getApplyApi, applyDeleteApi } from '@/api/user';
export default {
  name: 'InviteesTable',
  filters: {
    filterStatus(status) {
      const obj = {
        '-1': '待审核',
        0: '拒绝',
        1: '同意',
      };
      return obj[status];
    },
  },
  data() {
    return {
      dialogFormVisible: false,
      dataTable: [],
      page: 1,
      limit: 15,
      total: 0,
    };
  },
  methods: {
    open() {
      this.dialogFormVisible = true;
      this.getList();
    },
    async getList() {
      const result = await getApplyApi({
        page: this.page,
        limit: this.limit,
      })
      this.dataTable = result.data.list;
      this.total = result.data.count;
    },
    // 删除申请人
    handleDelete(id, index) {
      this.$modalSure(this.$t('setting.group.deleapply')).then(() => {
        applyDeleteApi(id)
          .then((res) => {
            this.dataTable.splice(index, 1);
          })
      });
    },
    pageChange(num) {
      this.page = num;
      this.getList();
    },
  },
};
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
