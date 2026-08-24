<template>
  <div class="box-container">
    <el-drawer :visible.sync="drawer" direction="rtl" :before-close="handleClose" size="75%" :append-to-body="true"
      :modal="true" :wrapper-closable="true">
      <slot slot="title">
        <div class="tabsEdit">
          <div class="tabs">
            <el-tabs v-model="tabsName" class="cr-header-tabs" @tab-click="handleClick">
              <el-tab-pane label="未读" name="1" />
              <el-tab-pane label="全部" name="2" />
            </el-tabs>
          </div>
        </div>
      </slot>
      <div class="flex">
        <div class="left">
          <!-- <div class="title mb15">消息类型</div> -->
          <div class="type" @click="handleTypes({ id: '' })" :class="activeId == 0 ? 'active' : ''">
            全部类型
          </div>
          <div class="type" v-for="(item, index) in options" :key="index" @click="handleTypes(item)"
            :class="activeId == item.value ? 'active' : ''">
            {{ item.cate_name }} <span class="num" v-if="item.count != 0">{{ item.count }}</span>
          </div>
        </div>
        <div class="right">
          <div class="mt20">
            <el-input v-model="where.name" prefix-icon="el-icon-search" clearable size="small" @change="getList"
              @keyup.native.stop.prevent.enter="getList" placeholder="请输入标题/内容" style="width: 250px"></el-input>
          </div>

          <!-- 消息列表 -->
          <div class="mt10">
            <el-table ref="table" :data="tableData" :height="height" v-loading="loading"
              @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="35" />
              <el-table-column label="查看" width="50">
                <template slot-scope="scope">
                  <el-image :src="scope.row.is_read === 0
                    ? require('@/assets/images/unread-icon.png')
                    : require('@/assets/images/read-icon.png')
                    "></el-image>
                </template>
              </el-table-column>
              <el-table-column prop="title" label="消息标题" min-width="80"></el-table-column>
              <el-table-column prop="message" label="消息内容" min-width="360" />
              <el-table-column prop="cate_name" label="类型" min-width="80" />
              <el-table-column prop="created_at" label="发送时间" min-width="120"></el-table-column>
              <el-table-column :label="$t('toptable.operation')" width="100" fixed="right">
                <template slot-scope="scope">
                  <el-button type="text" v-for="(item, index) in scope.row.buttons" :key="index"
                    :disabled="selectedType.includes(item.action)" @click="handleDetails(scope.row, item)">
                    <span v-if="scope.row.cate_name !== '考勤'"> {{ item.title }}</span>
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="footer">
              <div class="isSelect">
                <div class="flex" style="display: inline-block">
                  <el-button size="small" @click="handleRead()">全部已读</el-button>
                  <el-button size="small" :disabled="multipleSelection.length == 0"
                    @click="handleRead(1)">标记为已读</el-button>
                  <el-button size="small" @click="handleDelete"
                    :disabled="multipleSelection.length == 0">批量删除</el-button>
                </div>
                <!-- <span v-if="multipleSelection.length !== 0">已选中{{ multipleSelection.length }} 条</span> -->
              </div>
              <div class="paginationClass">
                <el-pagination :page-size="where.limit" :current-page="where.page" :page-sizes="[15, 20, 30]"
                  layout="total,sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
                  @current-change="pageChange" />
              </div>
            </div>
          </div>
        </div>

      </div>
    </el-drawer>
    <message-handle-popup ref="messageHandlePopup" :detail="detail" @handleClose="handleClose"></message-handle-popup>
  </div>
</template>
<script>
import { noticeMessageListApi, noticeMessageDeleteApi, noticeMessageReadApi } from '@/api/user'
import { messageCateApi } from '@/api/setting'
import { messageListApi } from '@/api/public'
export default {
  name: 'noticeList',
  components: {
    messageHandlePopup: () => import('@/components/common/messageHandlePopup')
  },
  data() {
    return {
      drawer: false,
      loading: false,
      tabsName: '1',
      activeId: 0,
      height: `calc(100vh - 240px)`,
      tableData: [],
      detail: {},
      options: [],
      multipleSelection: [],
      selectedType: ['delete', 'recall'],
      total: 0,
      where: {
        page: 1,
        limit: 15,
        title: '',
        cate_id: '',
        name: '',
        is_read: '0'
      }
    }
  },

  methods: {
    openBox() {
      this.getMessageCate()
      this.getList()
      this.drawer = true
      this.tabsName = '1'
    },
    // 获取列表
    getList(type = 1) {
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
          // this.$store.commit('user/SET_MESSAGE', this.total)
        })
        .catch((error) => {
          if (type === 1) {
            this.loading = false
          }
        })
    },
    handleRead(type, id) {
      let ids = []

      if (type == 1) {
        this.multipleSelection.forEach(item => {
          ids.push(item.id)
        })
        noticeMessageReadApi(1, id ? id : { ids }).then(res => {
          this.getList()
          this.getMessageCate()

        })
      } else {
        if(this.where.cate_id){
          noticeMessageReadApi(1, {cate_id:this.where.cate_id}).then(res => {
          this.getList()
          this.getMessageCate()
        })

        } else {
          noticeMessageReadApi(1, {}).then(res => {
          this.getList()
          this.getMessageCate()
        })
        }
     
      }
     
    },

    handleTypes(item) {
      this.where.cate_id = item.id
      this.activeId = item.id
      this.where.page = 1
      this.getList()
    },
    async getMessageCate() {
      const result = await messageCateApi()
      this.options = result.data
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

    // 批量删除消息
    batchMessageDelete(data) {
      noticeMessageDeleteApi(data).then((res) => {
        let totalPage = Math.ceil((this.total - data.ids.length) / this.where.limit)
        let currentPage = this.where.page > totalPage ? totalPage : this.where.page
        this.where.page = currentPage < 1 ? 1 : currentPage
        this.getList()
        this.getMessageCate()
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    async handleDetails(row, item) {
      this.detail = row
      await this.$nextTick()
      this.$refs.messageHandlePopup.openMessage(item, row)

      if (row.is_read === 0) {
        this.handleRead(1, { ids: [row.id] })

      }
    },

    pageChange(page) {
      this.where.page = page
      this.getList()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    },
    handleClose() {
      this.drawer = false
      messageListApi({page:1,limit:1}).then((res) => {
        let count = res.data.messageNum ? res.data.messageNum : 0
        this.$store.commit('user/SET_MESSAGE', count)
      })
    },
    handleClick() {
      this.where.page = 1
      if (this.tabsName == 2) {
        this.where.is_read = ''
      } else {
        this.where.is_read = '0'
      }
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-drawer__body {
  padding-bottom: 0;
}

::v-deep .el-drawer__header {
  padding: 0;
  border-bottom: 1px solid #eeeeee;
  padding-left: 30px;
}

::v-deep .el-tabs__item {
  height: 56px;
  line-height: 56px;
  font-weight: 500;
  z-index: 9999;
}

::v-deep .el-drawer__close-btn {
  width: 50px;
}


.tabsEdit {
  display: flex;
  justify-content: space-between;

  .invitationUrl {
    margin-top: 2px;
    margin-right: 20px;
  }
}

.footer {
  padding-top: 32px;
  border-top: 1px solid #e8e8e8;
}

.right {
  width: calc(100% - 167px);
  padding-left: 20px;
}

.left {
  width: 167px;
  height: calc(100vh - 60px);
  padding: 18px 0;
  background-color: #f8f9fa;

  .title {
    margin-left: 20px;
    font-family: PingFang SC, PingFang SC;
    font-weight: 500;
    font-size: 15px;
    color: #303133;
  }

  .type {
    position: relative;
    cursor: pointer;
    padding-left: 20px;
    height: 40px;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 14px;
    color: #303133;
    line-height: 40px;
    border-right: 2px solid #f8f9fa;

    .num {
      position: absolute;
      top: 12px;
      right: 17px;
      display: inline-block;
      width: 16px;
      height: 16px;
      background: #EA0000;
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 10px;
      color: #FFFFFF;
      line-height: 16px;
      border-radius: 50%;
      text-align: center;
    }
  }

  .active {
    background-color: #f1f9ff;
    color: #1890ff;
    border-right: 2px solid #1890ff;
  }
}

.isSelect {
  height: 32px;
  line-height: 32px;
  position: absolute;
  font-size: 13px;
  margin-top: 14px;
}

.paginationClass {
  margin-top: 14px;
  display: flex;
  justify-content: flex-end;
}

::v-deep .el-image {
  width: 23px !important;
  height: 23px !important;
}

// ::v-deep .el-table th {
//   background-color: #f7fbff;
// }

::v-deep .el-table-column--selection .cell {
  padding: 0 10px;
}
</style>
