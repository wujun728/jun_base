<template>
  <div>
    <div class="flex-between">
      <div class="title-16">触发器列表</div>
      <oa-popover
        :list="typesList"
        :isValueShow="false"
        :searchShow="false"
        :height="`340px`"
        title="新建触发器"
        @handleClick="handleClick"
      ></oa-popover>
    </div>
    <!-- 筛选 -->
    <div class="flex mb10 h32">
      <div class="inTotal">共 {{ total }} 项</div>
      <div class="ml14">
        <el-input
          v-model="where.name"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入关键字搜索"
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
        <el-table-column prop="name" label="触发器名称" />
        <el-table-column prop="field_name" label="触发类型">
          <template slot-scope="scope">
            {{ getEvent(scope.row.event) }}
          </template>
        </el-table-column>
        <el-table-column prop="form_value" label="触发动作">
          <template slot-scope="scope">
            <span v-if="scope.row.action && scope.row.action.length > 0">
              {{ getAction(scope.row.action) }}
            </span>
            <span v-else class="color-file">（无触发动作）</span>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="优先级"> </el-table-column>
        <el-table-column prop="updated_at" label="更新时间"> </el-table-column>
        <el-table-column prop="is_main" label="状态">
          <template slot-scope="scope">
            <el-switch
              @change="handleStatus(scope.row)"
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              :width="60"
              active-text="开启"
              inactive-text="关闭"
            >
            </el-switch>
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
import oaPopover from '@/components/form-common/oa-popover'

import {
  dataEventListApi,
  dataEventTypeApi,
  dataEventSaveApi,
  dataEventDelApi,
  dataEventStatusApi,
  dataEventActionApi
} from '@/api/develop'
import { roterPre } from '@/settings'

export default {
  name: '',
  components: { oaPopover },
  props: {
    infoData: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      loading: false,
      total: 0,
      typesList: [],
      height: window.innerHeight - 360 + 'px',

      crud_id: 0, // 实体id
      typeList: [],
      where: {
        name: '',
        page: 1,
        limt: 15
      },
      actionList: [],
      tableData: [] // 表格数据
    }
  },

  mounted() {
    this.crud_id = this.infoData.id
    this.getOptions()
    this.getActionList()
    this.getList()
  },
  methods: {
    // 获取触发器列表
    async getList() {
      this.loading = true
      const data = await dataEventListApi(this.crud_id, this.where)
      this.total = data.data.count
      this.tableData = data.data.list
      this.loading = false
    },

    // 获取执行动作类型
    getActionList() {
      dataEventActionApi().then((res) => {
        this.actionList = res.data
      })
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

    // 获取触发器类型
    async getOptions() {
      const data = await dataEventTypeApi(this.crud_id)
      this.typeList = data.data
      this.typesList = [{ options: data.data }]
    },

    // 修改状态
    async handleStatus(row) {
      await dataEventStatusApi(row.id, { status: row.status })
      this.getList()
    },

    getEvent(val) {
      let index = this.typeList.findIndex((item) => item.value === val)
      return this.typeList.length > 0 ? this.typeList[index].label : '--'
    },
    handleClick(data) {
      let obj = {
        crud_id: this.infoData.id,
        event: data.value,
        name: data.label
      }
      dataEventSaveApi(obj).then((res) => {
        const { href } = this.$router.resolve({
          path: `${roterPre}/event`,
          query: {
            crud_id: this.infoData.id,
            id: res.data.id
          }
        })
        window.open(href, '_blank')
      })
    },

    // 删除触发器
    deleteFn(row) {
      this.$modalSure('您确定要删除此触发器数据吗').then(() => {
        dataEventDelApi(row.id).then((res) => {
          this.getList()
        })
      })
    },

    // 编辑
    editFn(row) {
      const { href } = this.$router.resolve({
        path: `${roterPre}/event`,
        query: {
          crud_id: this.infoData.id,
          id: row.id
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
.title {
  font-size: 16px;
  font-weight: 500;
}
.flex {
  margin: 10px 0;
  display: flex;
  align-items: center;
}
</style>
