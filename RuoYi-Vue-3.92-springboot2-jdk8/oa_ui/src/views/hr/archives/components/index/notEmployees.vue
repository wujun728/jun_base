<template>
  <div>
    <el-card class="employees-card-bottom">
      <div>
        <!-- 搜索条件 -->
        <formBox
          ref="formBox"
          @opendrawer="$emit('openDrawer', 'addDrawerIsShow', 'add')"
          :notEmployees="notEmployees"
          :total="total"
          @confirmData="confirmData"
          @handleDelete="handleDelete"
        ></formBox>

        <!-- 表格 -->
        <div v-loading="pageLoading">
          <Table
            ref="table"
            :tableData="tableData"
            @limitFn="limitFn"
            @pageFn="pageFn"
            :total="total"
            @multipleSelection="multipleSelection"
            :notEmployees="notEmployees"
          >
            <template #options="{ data }">
              <el-button type="text" @click="hanldeOptions(data, 'edit')" v-hasPermi="['hr:archives:check']"
                >查看</el-button
              >

              <el-button type="text" @click="hanldeOptions(data, 'delete')">删除</el-button>

              <el-button type="text" @click="hanldeOptions(data, 'entry')">入职</el-button>
            </template>
          </Table>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { enterpriseCardApi } from '@/api/enterprise'
import formBox from '../formBox.vue'
import Table from '../table.vue'
import myMixins from '../../mixins/method.js'
export default {
  created() {
    this.getList()
  },
  mixins: [myMixins],
  data() {
    return {
      notEmployees: '未入职',
      tableData: [],
      pageLoading: false,
      fromTab: {},
      total: 0,
      limit: 15,
      page: 1,
      selection: []
    }
  },

  methods: {
    // 筛选条件
    confirmData(from) {
      this.fromTab = { ...from }
      if (JSON.stringify(from) == '{}') {
        this.$refs.table.where.limit = 15
      }
      if (this.$refs.formBox.tableFrom.exportType === 1) {
        this.fromTab.limit = 0
        this.fromTab.page = 0
        this.fromTab.types = [0]
        this.getExportData(this.fromTab)
      } else {
        this.page = 1
        this.getList(1)
        this.$refs.table.where.page = 1
      }
    },
    hanldeOptions(data, type) {
      switch (type) {
        // 编辑
        case 'edit': {
          this.$emit('openDrawer', 'addDrawerIsShow', 'edit', data)
          break
        }
        // 删除
        case 'delete': {
          this.$emit('onDelete', data)
          break
        }
        // 入职
        case 'entry': {
          this.$emit('openDrawer', 'addDrawerIsShow', 'edit', data, '入职')
          break
        }
      }
    },

    // 分页
    limitFn(val) {
      this.limit = val
      this.getList(1)
    },
    pageFn(page) {
      this.page = page
      this.getList(1)
    },

    // 获取列表数据
    getList(val) {
      if (val == 1) {
        // this.page == val ? val : this.page
      } else {
        this.fromTab = {}
      }
      this.fromTab.limit = this.limit
      this.fromTab.page = this.page
      this.fromTab.types = [0]
      this.pageLoading = true

      enterpriseCardApi(this.fromTab)
        .then((res) => {
          this.tableData = res.data.list
          this.total = res.data.count
        })
        .catch((err) => {})
        .finally(() => {
          this.pageLoading = false
        })
    },

    // 多选表格
    multipleSelection(val) {
      this.selection = val
    }
  },

  components: {
    formBox,
    Table
  }
}
</script>

<style lang="scss" scoped>
.divBox {
  margin: 0;
}
</style>
