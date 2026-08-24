<template>
  <div>
    <div class="flex-between">
      <div class="title-16">流程列表</div>
      <el-button size="small" type="primary" icon="el-icon-plus" @click="openBox">新建流程</el-button>
    </div>
    <!-- 筛选 -->
    <div class="flex mb10 h32">
      <div class="inTotal">共 {{ tableData.length }} 项</div>
      <div class="ml14">
        <el-input
          v-model="where.name"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入关键字"
          clearable
          style="width: 250px"
          @change="getList"
          @keyup.native.stop.prevent.enter="getList"
          class="input"
        ></el-input>
      </div>
    </div>
    <!-- 表格 -->
    <div class="table-box" v-loading="loading">
      <el-table row-key="id" :data="tableData" :height="height" style="width: 100%">
        <el-table-column prop="name" :label="$t('business.businessType')" min-width="230">
          <template slot-scope="scope">
            <div class="flex">
              <div class="selIcon" :style="{ backgroundColor: scope.row.color }">
                <i class="icon iconfont" :class="scope.row.icon"></i>
              </div>
              <div class="ml10">{{ scope.row.name || '--' }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="refuse" label="发起人范围" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.userList ? row.userList : '所有人' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="info" label="审批说明" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.info || '--' }}
          </template>
        </el-table-column>

        <el-table-column prop="updated_at" label="更新时间" min-width="140" />
        <el-table-column label="状态" width="160">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-text="$t('hr.open')"
              :inactive-text="$t('hr.close')"
              :active-value="1"
              :inactive-value="0"
              :width="60"
              @change="handleStatus(scope.row)"
            />
          </template>
        </el-table-column>

        <el-table-column prop="address" label="操作" fixed="right" width="170">
          <template slot-scope="scope">
            <el-button type="text" @click="editFn(scope.row)">编辑</el-button>
            <el-button type="text" @click="deleteFn(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="page-fixed">
        <el-pagination
          :page-size="where.limit"
          :current-page="where.page"
          :page-sizes="[15, 20, 30]"
          layout="total,sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="pageChange"
        />
      </div>
    </div>
  </div>
</template>
<script>
import { roterPre } from '@/settings'
import { dataApproveListApi, dataApproveDeleteApi, dataApproveStatusApi } from '@/api/develop'
export default {
  components: {},
  props: {
    infoData: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      total: 0,
      where: {
        name: '',
        page: 1,
        limit: 15,
        crud_id: 0
      },
      height: window.innerHeight - 350 + 'px',
      loading: false,
      tableData: [] // 表格数据
    }
  },

  mounted() {
    this.where.crud_id = this.infoData.id
    this.getList()
  },
  methods: {
    // 获取字段列表
    async getList() {
      this.loading = true
      const data = await dataApproveListApi(this.where)
      this.tableData = data.data.list
      this.total = data.data.count
      this.loading = false
    },

    // 删除流程
    deleteFn(row) {
      this.$modalSure('您确定要删除此流程吗').then(() => {
        dataApproveDeleteApi(row.id).then((res) => {
          this.getList()
        })
      })
    },

    // 修改状态
    async handleStatus(row) {
      await dataApproveStatusApi(row.id, { status: row.status })
      this.getList()
    },

    // 编辑流程
    editFn(row) {
      const { href } = this.$router.resolve({
        path: `${roterPre}/process`,
        query: {
          crud_id: this.infoData.id,
          id: row.id
        }
      })
      window.open(href, '_blank')
    },

    // 新建流程
    openBox() {
      const { href } = this.$router.resolve({
        path: `${roterPre}/process`,
        query: {
          crud_id: this.infoData.id
        }
      })
      window.open(href, '_blank')
    },
    pageChange(page) {
      this.where.page = page
      this.getList()
    },

    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    }
  }
}
</script>
<style scoped lang="scss">
.selIcon {
  width: 25px;
  height: 25px;
  line-height: 25px;
  display: inline-block;
  text-align: center;
  cursor: pointer;
  border-radius: 3px;
}
.iconfont {
  font-size: 13px;
  color: #fff;
}
.title {
  font-size: 16px;
  font-weight: 500;
}
.flex {
  margin: 10px 0;
  display: flex;
  align-items: center;
}
.line {
  border-bottom: 1px solid #e8ebf2;
  margin: 10px 0;
}
.field-text {
  cursor: pointer;
  height: 32px;
  line-height: 32px;
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  //   color: #303330;
}
.field-text:hover {
  background: #f7fbff;
  color: #1890ff;
}
.field-box {
  height: 500px;
  overflow-y: scroll;
}
.field-box::-webkit-scrollbar {
  height: 0;
  width: 0;
}
</style>
