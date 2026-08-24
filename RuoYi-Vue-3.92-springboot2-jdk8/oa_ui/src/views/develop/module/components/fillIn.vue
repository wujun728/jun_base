<template>
  <div>
    <el-drawer
      title="邀请链接记录"
      :visible.sync="drawer"
      direction="rtl"
      :show-close="true"
      :wrapper-closable="true"
      :append-to-body="true"
      :before-close="handleClose"
      :size="`65%`"
    >
      <div class="table-box">
        <div class="mb10 flex-between">
          <div class="title-16">邀请链接生成记录</div>
          <el-button type="primary" size="small" @click="addShare">邀请填写</el-button>
        </div>
        <el-table :data="tableData" style="width: 100%" :height="height">
          <el-table-column prop="user.name" label="邀请链接" min-width="100px">
            <template slot-scope="scope">
              <div class="flex">
                <div class="over-text w-180">{{ scope.row.url || '--' }}</div>
                <span class="iconfont iconfuzhi-01" @click="copy(scope.row.url)"></span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="人员范围" width="90">
            <template slot-scope="scope">
              {{ scope.row.role_type == 0 ? '仅企业员工' : '所有人员' }}
            </template>
          </el-table-column>
          <el-table-column prop="invalid_time" label="失效时间" width="180"> </el-table-column>
          <el-table-column prop="updated_at" label="创建时间" width="180"> </el-table-column>
          <el-table-column prop="user.name" label="创建人" width="80"> </el-table-column>
          <el-table-column label="链接状态" width="110">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                :active-value="1"
                :inactive-value="0"
                active-text="开启"
                inactive-text="关闭"
                @change="changeStatus(scope.row)"
              >
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column prop="address" label="操作" width="80">
            <template slot-scope="scope">
              <el-button type="text" @click="delModuleShare(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="page-fixed">
          <el-pagination
            :page-size="where.limit"
            :current-page="where.page"
            :page-sizes="[15, 20, 30]"
            layout="total,sizes, prev, pager, next, jumper"
            :total="count"
            @size-change="handleSizeChange"
            @current-change="pageChange"
          />
        </div>
      </div>
    </el-drawer>
    <!-- 邀请填写弹窗 -->
    <fillInDialog ref="fillInDialog" @getList="getList"></fillInDialog>
  </div>
</template>
<script>
import fillInDialog from './fillInDialog'
import { getModuleQuestionnaireApi, delModuleQuestionnaireApi, putModuleQuestionnaireApi } from '@/api/develop'
export default {
  name: '',
  components: { fillInDialog },
  props: {},
  data() {
    return {
      drawer: false,
      keyName: '',
      count: 0,
      tableData: [],
      height: `calc(100vh - 200px)`,
      where: {
        page: 1,
        limit: 15
      }
    }
  },

  methods: {
    getList() {
      getModuleQuestionnaireApi(this.keyName, this.where).then((res) => {
        this.tableData = res.data.list
        this.count = res.data.count
      })
    },
    openBox(keyName, row) {
      this.keyName = keyName
      this.getList()
      this.drawer = true
    },
    // 添加
    addShare() {
      this.$refs.fillInDialog.openBox(this.keyName)
    },

    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    },
    pageChange(val) {
      this.where.page = val
      this.getList()
    },

    // 修改状态
    changeStatus(row) {
      putModuleQuestionnaireApi(this.keyName, row.id, { status: row.status }).then((res) => {
        this.getList()
      })
    },

    // 删除
    delModuleShare(id) {
      this.$modalSure('您确认要删除此数据吗').then(() => {
        delModuleQuestionnaireApi(this.keyName, id).then((res) => {
          this.getList()
        })
      })
    },

    handleClose() {
      this.drawer = false
    },
    copy(val) {
      clipboard.writeText(val)
      this.$message.success('复制成功')
    }
  }
}
</script>
<style scoped lang="scss">
.table-box {
  padding: 20px;
}
.w-180 {
  min-width: 180px;
}
.iconfuzhi-01 {
  margin-left: 10px;
  cursor: pointer;
  color: #1890ff;
}
</style>
