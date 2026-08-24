<template>
  <div class="divBox">
    <el-card :body-style="{ padding: '20px 20px 0 20px' }" class="normal-page">
      <el-row>
        <el-col v-bind="gridl" class="p20">
          <tree :parentTree="options" @getTypesId="getTypesId" :type="2"></tree>
        </el-col>
        <el-col v-bind="gridr" class="boder-left pl20">
          <oaFromBox
            :search="searchData"
            :title="$route.meta.title"
            :total="total"
            :isViewSearch="false"
            :sortSearch="false"
            :isAddBtn="false"
            class="mb20"
            @confirmData="confirmData"
          >
          </oaFromBox>
          <div class="table-box">
            <el-table ref="table" :data="tableData" :height="tableHeight">
              <el-table-column prop="title" label="消息标题" min-width="100" show-overflow-tooltip />
              <el-table-column prop="user.name" label="接收人" min-width="100" />
              <el-table-column prop="message" label="消息内容" min-width="240" show-overflow-tooltip />
              <el-table-column prop="cate_name" label="消息类型" min-width="100" show-overflow-tooltip />
              <el-table-column prop="created_at" label="发送时间" min-width="100" />

              <el-table-column :label="$t('toptable.operation')" width="100" fixed="right">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    v-for="(item, index) in scope.row.buttons"
                    :key="index"
                    @click="handleDetails(scope.row, item)"
                  >
                    <span v-if="scope.row.cate_name !== '考勤'"> {{ item.title }}</span>
                  </el-button>
                  <!-- :disabled="selectedType.includes(item.action)" -->
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>
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
    </el-card>
    <message-handle-popup ref="messageHandlePopup" :detail="detail"></message-handle-popup>
  </div>
</template>

<script>
import { getCompanyMessageApi, messageCateApi } from '@/api/setting'
export default {
  name: 'Apply',
  components: {
    messageHandlePopup: () => import('@/components/common/messageHandlePopup'),
    oaFromBox: () => import('@/components/common/oaFromBox'),
    tree: () => import('./components/tree')
  },
  data() {
    return {
      tableData: [],
      options: [],
      messageData: {},
      where: {
        page: 1,
        limit: 15,
        cate_id: '',
        title: '',
        is_read: ''
      },
      total: 0,

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
      detail: {},
      loading: false,
      type: [0, 1],
      searchData: [
        {
          field_name: '标题/内容',
          field_name_en: 'title',
          form_value: 'input'
        }
      ]
    }
  },
  created() {},
  mounted() {
    this.getMessageCate()
    this.getTableData()
  },
  methods: {
    // 获取列表
    getTableData() {
      getCompanyMessageApi(this.where).then((res) => {
        this.tableData = res.data.list
        this.total = res.data.messageNum
        if (this.tableData && this.tableData.length > 0) {
          this.tableData.forEach((value) => {
            value.message_template.sort((a, b) => {
              return a.type - b.type
            }) //升序
          })
        }
      })
    },
    getTypesId(type) {
      this.where.page = 1
      this.where.cate_id = type
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
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
      this.getTableData()
    },
    confirmData(data) {
      if (data == 'reset') {
        this.where.title = ''
      } else {
        if (data.cate_id && Array.isArray(data.cate_id)) {
          // data.cate_id = data.cate_id[data.cate_id.length - 1]
        }
        this.where = { ...this.where, ...data }
      }
      this.where.page = 1
      this.getTableData()
    },

    getMessageCate() {
      messageCateApi().then((res) => {
        this.options = [{ label: '全部', value: '' }, ...res.data]
        for (let i = 0; i < res.data.length; i++) {
          res.data[i].name = res.data[i].label
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-table__row {
  height: 56px;
}
</style>
