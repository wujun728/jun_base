<template>
  <div class="m14">
    <!-- 未读页面 -->
    <el-card class="content employees-card">
      <div class="from-s mt14">
        <div class="flex-row flex-col">
          <form-box ref="formBox" @confirmData="confirmData" />
        </div>
      </div>
      <div class="mt10">
        <el-table
          ref="table"
          :data="tableData"
          :height="tableHeight"
          v-loading="loading"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="35"> </el-table-column>
          <el-table-column label="查看" width="50">
            <template slot-scope="scope">
              <el-image
                :src="
                  scope.row.is_read === 0
                    ? require('@/assets/images/unread-icon.png')
                    : require('@/assets/images/read-icon.png')
                "
              ></el-image>
            </template>
          </el-table-column>
          <el-table-column prop="title" label="消息标题" min-width="80"></el-table-column>
          <el-table-column prop="message" label="消息内容" min-width="360">
            <template slot-scope="scope">
              <div class="notice">{{ scope.row.message }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="cate_name" label="类型" min-width="80" />
          <el-table-column prop="created_at" label="发送时间" min-width="120"></el-table-column>
          <el-table-column :label="$t('toptable.operation')" width="100" fixed="right">
            <template slot-scope="scope">
              <el-button
                type="text"
                v-for="(item, index) in scope.row.buttons"
                :key="index"
                :disabled="selectedType.includes(item.action)"
                @click="handleDetails(scope.row, item)"
              >
                <span v-if="scope.row.cate_name !== '考勤'"> {{ item.title }}</span></el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="footer">
        <div class="isSelect">
          <div class="flex" style="display: inline-block">
            <el-button @click="handleIsRead" size="small" :disabled="multipleSelection.length == 0"
              >标记为已读</el-button
            >
            <el-button size="small" @click="handleDelete" :disabled="multipleSelection.length == 0">批量删除</el-button>
          </div>
          <span v-if="multipleSelection.length !== 0">已选中{{ multipleSelection.length }} 条</span>
        </div>
        <div class="paginationClass">
          <el-pagination
            :page-size="where.limit"
            :current-page="where.page"
            :page-sizes="[15, 20, 30]"
            layout="sizes,total, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="pageChange"
          />
        </div>
      </div>
    </el-card>

    <!-- 通用弹窗表单   -->
    <dialog-form ref="dialogForm" :roles-config="rolesConfig" :form-data="formBoxConfig" @isOk="getList()" />
    <message-details ref="messageDetails" :message-data="messageData" @isOk="isOk" />
    <message-handle-popup ref="messageHandlePopup" :detail="detail"></message-handle-popup>
  </div>
</template>

<script>
import { noticeMessageListApi, noticeMessageReadApi, noticeMessageDeleteApi } from '@/api/user'
import { messageListApi } from '@/api/public'
export default {
  name: 'Apply',
  components: {
    dialogForm: () => import('./components/index'),
    formBox: () => import('./components/formBox'),
    messageDetails: () => import('./components/messageDetails'),
    messageHandlePopup: () => import('@/components/common/messageHandlePopup')
  },
  data() {
    return {
      rolesConfig: [],
      formBoxConfig: {},
      tableData: [],
      activeName: '1',
      multipleSelection: [],
      messageData: {},
      where: {
        page: 1,
        limit: 15,
        title: '',
        cate_id: '',
        is_read: 0
      },
      total: 0,
      loading: false,
      detail: {},
      selectedType: ['delete', 'recall']
    }
  },
  mounted() {
    this.getTableData()
  },
  methods: {
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    // 获取列表
    getTableData(type = 1) {
      if (type === 1) {
        this.loading = true
      }
      noticeMessageListApi(this.where)
        .then((res) => {
          if (type === 1) {
            this.loading = false
          }
          this.tableData = res.data.list
          this.total = res.data.messageNum
          if (this.activeName === '1') {
            this.$store.commit('user/SET_MESSAGE', this.total)
          }
        })
        .catch((error) => {
          if (type === 1) {
            this.loading = false
          }
        })
    },

    confirmData(data, val) {
      if (val == 'reset') {
        this.where.limit = 15
      }
      this.where.page = 1
      this.where.title = data.name
      this.where.cate_id = data.types
      this.getTableData()
    },
    handleDelete() {
      if (this.multipleSelection.length <= 0) {
        this.$message.error('至少选择一项内容')
      } else {
        this.$modalSure('删除后不可恢复,您确认要删除吗').then(() => {
          const ids = []
          this.multipleSelection.map((value) => {
            ids.push(value.id)
          })
          this.batchMessageDelete({ ids: ids })
        })
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    async handleDetails(row, item) {
      this.detail = row
      await this.$nextTick()
      this.$refs.messageHandlePopup.openMessage(item)
      if (row.is_read === 0) {
        this.batchMessageRead(1, { ids: [row.id] }, 0, 2)
      }
    },
    isOk() {
      this.getTableData(0)
    },
    handleIsRead() {
      if (this.multipleSelection.length <= 0) {
        this.$message.error('至少选择一项内容')
      } else {
        const ids = []
        this.multipleSelection.map((value) => {
          ids.push(value.id)
        })
        this.batchMessageRead(1, { ids: ids }, 1)
      }
    },
    // 批量标记未已读
    batchMessageRead(status, data, type, val) {
      noticeMessageReadApi(status, data).then(() => {
        this.getMessage()
        this.getTableData()
      })
    },
    // 批量删除消息
    batchMessageDelete(data) {
      noticeMessageDeleteApi(data).then(() => {
        this.getMessage()
        this.getTableData()
      })
    },
    // 消息数量
    getMessage() {
      messageListApi({ page: 1, limit: 1 }).then((res) => {
        const num = res.data.messageNum ? res.data.messageNum : 0
        this.$store.commit('user/SET_MESSAGE', num)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.m14 {
  margin: 0 14px;
}
.from-s {
  height: 40px;
}
.footer {
  border-top: 1px solid #e8e8e8;
  padding-bottom: 20px;
}
.isSelect {
  height: 32px;
  line-height: 32px;
  position: absolute;
  font-size: 13px;
  margin-top: 14px;
}
.content {
  ::v-deep .el-card__body {
    height: 100%;
  }
  .el-table {
    height: calc(100vh - 184px) !important;
  }
  ::v-deep .el-image {
    width: 23px !important;
    height: 23px !important;
  }
}
::v-deep .el-table th {
  background-color: #f7fbff;
}
::v-deep .el-table-column--selection .cell {
  padding: 0 10px;
}
.paginationClass {
  margin-top: 14px;
  display: flex;
  justify-content: flex-end;
}
.notice {
  white-space: pre-line;
}
</style>
