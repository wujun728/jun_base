<template>
  <div class="divBox">
    <div class="v-height-flag">
      <el-card :body-style="{ padding: '20px 20px 20px 20px' }" class="mb14 normal-page">
        <oaFromBox
          v-if="search.length > 0"
          :isViewSearch="false"
          :search="search"
          :title="`触发器列表`"
          :total="total"
          :isAddBtn="false"
       
          @confirmData="confirmData"
        >
          <div slot="rightBtn" class="dropdown-box">
            <oa-popover
              :list="typesList"
              :isValueShow="false"
              :searchShow="false"
              :height="`340px`"
              title="新建触发器"
              @handleClick="handleClick"
            ></oa-popover>
          </div>
        </oaFromBox>

        <!-- 表格数据 -->
        <div v-loading="loading" class="table-box mt10">
          <el-table :data="tableData" :height="tableHeight" row-key="id" style="width: 100%">
            <el-table-column label="触发器名称" prop="name">
              <template slot-scope="scope">
                <span class="color-doc pointer" @click="editFn(scope.row)"> {{ scope.row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column :min-width="150" label="触发类型" prop="field_name">
              <template slot-scope="scope">
                {{ getEvent(scope.row.event) }}
              </template>
            </el-table-column>
            <el-table-column label="触发动作" prop="form_value">
              <template slot-scope="scope">
                <span v-if="scope.row.action && scope.row.action.length > 0">
                  {{ getAction(scope.row.action) }}
                </span>
                <span v-else class="color-file">（无触发动作）</span>
              </template>
            </el-table-column>
            <el-table-column label="优先级" prop="sort"> </el-table-column>
            <el-table-column label="关联实体" min-width="140" prop="crud">
              <template slot-scope="scope">
                {{ scope.row.crud.table_name || '--' }}
              </template>
            </el-table-column>
            <el-table-column label="关联应用" min-width="140" prop="cate_item">
              <template slot-scope="scope">
                <div v-if="scope.row.cate_item && scope.row.cate_item.length > 0">
                  <span>{{ getString(scope.row.cate_item) }}</span>
                </div>
                <div v-else>--</div>
              </template>
            </el-table-column>
            <el-table-column label="更新时间" prop="updated_at"> </el-table-column>
            <el-table-column label="状态" prop="is_main">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.status"
                  :active-value="1"
                  :inactive-value="0"
                  :width="60"
                  active-text="开启"
                  inactive-text="关闭"
                  @change="handleStatus(scope.row)"
                >
                </el-switch>
              </template>
            </el-table-column>

            <el-table-column fixed="right" label="操作" prop="address" width="110">
              <template slot-scope="scope">
                <el-button type="text" @click="editFn(scope.row)">编辑</el-button>
                <el-button type="text" @click="deleteFn(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="page-fixed">
            <el-pagination
              :current-page="where.page"
              :page-size="where.limit"
              :page-sizes="[15, 20, 30]"
              :total="total"
              layout="total,sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="pageChange"
            />
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import oaPopover from '@/components/form-common/oa-popover'
import oaFromBox from '@/components/common/oaFromBox'
import { roterPre } from '@/settings'
import {
  getcrudCateListApi,
  getDatabaseApi,
  dataEventGuanListApi,
  dataEventActionApi,
  dataEventTypeApi,
  dataEventDelApi,
  dataEventStatusApi
} from '@/api/develop'
export default {
  name: 'FinanceList',
  components: {
    oaPopover,
    oaFromBox
  },
  data() {
    return {
      typesList: [],
      search: [
        {
          field_name: '关键字',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '关联实体',
          field_name_en: 'crud_id',
          form_value: 'cascaderSelect',
          data_dict: []
        },
        {
          field_name: '关联应用',
          field_name_en: 'cate_id',
          form_value: 'select',
          data_dict: []
        }
      ],
      tableData: [],
      loading: false,
      ids: [],
      actionList: [],

      where: {
        name: '',
        crud_id: '',
        cate_id: '',
        id_order_dy: 1,
        limit: 15,
        page: 1
      },
      entityOptions: [], // 实体数据
      application: [], // 应用数据
      total: 0,
      options: [
        {
          label: '启用',
          value: 1
        },
        {
          label: '停用',
          value: 0
        }
      ]
    }
  },
  created() {
    this.getList()
    this.getCrudAllType()
    this.getDatabase()
    this.getOptions()
    this.getActionList()
  },

  methods: {
    // 获取应用分类
    async getCrudAllType() {
      const data = await getcrudCateListApi()
      this.application = data.data.list
      this.search[2].data_dict = this.application
    },

    // 获取应用数据
    getDatabase() {
      getDatabaseApi().then((res) => {
        this.search[1].data_dict = res.data
      })
    },

    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          name: '',
          crud_id: '',
          cate_id: '',
          id_order_dy: 1,
          limit: 15,
          page: 1
        }
      } else {
        if (data.crud_id) {
          data.crud_id = data.crud_id[data.crud_id.length - 1]
        }
        this.where = { ...this.where, ...data }
      }
      this.getList()
    },
    // 获取触发器类型
    async getOptions() {
      const data = await dataEventTypeApi(this.crud_id)
      this.typesList = [{ options: data.data }]
    },

    // 修改状态
    async handleStatus(row) {
      await dataEventStatusApi(row.id, { status: row.status })
      this.getList()
    },
    /**
     * 根据值获取触发事件的标签
     * @param {number|string} val - 触发事件的值
     * @returns {string} 触发事件的标签，如果未找到则返回 '--'
     */
    getEvent(val) {
      // 从类型列表中查找匹配的选项
      const targetOption = this.typesList[0]?.options?.find((item) => item.value === val);
      return targetOption ? targetOption.label : '--';
    },
    getString(arr) {
      return arr.join('、')
    },

    // 根据id获取触发动作
    getAction(val) {
      let textArr = []
      this.actionList.map((item) => {
        val.map((key) => {
          if (item.value == key) {
            textArr.push(item.label)
          }
        })
      })
      return textArr.join('/')
    },

    // 获取执行动作类型
    getActionList() {
      dataEventActionApi().then((res) => {
        this.actionList = res.data
      })
    },
    handleClick(data) {
      const { href } = this.$router.resolve({
        path: `${roterPre}/event`,
        query: {
          event: data.value,
          event_name: data.label
        }
      })
      window.open(href, '_blank')
    },

    // 编辑
    editFn(row) {
      const { href } = this.$router.resolve({
        path: `${roterPre}/event`,
        query: {
          crud_id: row.crud_id,
          id: row.id
        }
      })
      window.open(href, '_blank')
    },

    // 删除触发器
    deleteFn(row) {
      this.$modalSure('您确定要删除此触发器数据吗').then(() => {
        dataEventDelApi(row.id).then((res) => {
          let totalPage = Math.ceil((this.total - 1) / this.where.limit)
          let currentPage = this.where.page > totalPage ? totalPage : this.where.page
          this.where.page = currentPage < 1 ? 1 : currentPage
          this.getList()
        })
      })
    },

    // 分页
    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    },

    pageChange(page) {
      this.where.page = page
      this.getList()
    },

    // 获取触发器列表
    async getList() {
      this.loading = true
      const data = await dataEventGuanListApi(this.where)
      this.total = data.data.count
      this.tableData = data.data.list
      this.loading = false
    },

    restData() {
      this.where.page = 1
      this.where.name = ''
      this.where.cate_id = ''
      this.where.crud_id = ''
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.inTotal {
  margin: 0;
  line-height: 32px;
  margin-right: 14px;
}
.title {
  font-size: 16px;
  font-weight: 500;
}
</style>
