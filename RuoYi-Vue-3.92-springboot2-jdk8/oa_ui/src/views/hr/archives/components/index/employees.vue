<template>
  <div>
    <el-card class="employees-card-bottom">
      <div>
        <!-- form表单 -->
        <formBox
          :key="showText == '收起' ? 0 : 1"
          ref="formBox"
          :notEmployees="notEmployees"
          :total="total"
          @confirmData="confirmData"
          @handleDelete="handleDelete"
          @opendrawer="$emit('openDrawer', 'addDrawerIsShow', 'add')"
          @showTextFn="showTextFn"
        ></formBox>

        <!-- 表格 -->
        <div v-loading="pageLoading">
          <Table
            ref="table"
            :showText="showText"
            :tableData="tableData"
            :total="total"
            @getList="getList"
            @limitFn="limitFn"
            @multipleSelection="multipleSelection"
            @pageFn="pageFn"
          >
            <template #options="{ data }">
              <el-button v-hasPermi="['hr:archives:check']" type="text" @click="hanldeOptions(data, 'edit')"
                >查看
              </el-button>

              <el-button v-if="data.status !== 0 && data.type !== 1" type="text" @click="hanldeOptions(data, 'worker')"
                >转正</el-button
              >

              <el-button
                v-if="data.type !== 2 || data.status !== 0"
                type="text"
                @click.native="hanldeOptions(data, 'quit')"
              >
                离职
              </el-button>

              <el-button v-if="data.status == 0" type="text" @click.native="hanldeOptions(data, 'inviteToJoin')">
                邀请完善信息
              </el-button>

              <el-dropdown v-if="data.type !== 1 && data.status !== 1">
                <span class="el-dropdown-link el-button--text el-button">
                  {{ $t('hr.more') }}
                </span>
                <el-dropdown-menu>
                  <el-dropdown-item v-if="data.type !== 1" @click.native="hanldeOptions(data, 'worker')">
                    转正
                  </el-dropdown-item>
                  <el-dropdown-item v-if="data.type !== 1" @click.native="hanldeOptions(data, 'quit')">
                    离职
                  </el-dropdown-item>

                  <el-dropdown-item v-if="data.status !== 1" @click.native="hanldeOptions(data, 'delete')"
                    >删除</el-dropdown-item
                  >
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </Table>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { enterpriseCardApi } from '@/api/enterprise'

import myMixins from '../../mixins/method.js'
export default {
  mixins: [myMixins],
  data() {
    return {
      fromTab: {},
      tableData: [],
      pageLoading: false,
      isShownewPersonnel: false,
      showText: '展开',
      total: 0,
      page: 1,
      limit: 15,
      selection: [],
      notEmployees: '在职'
    }
  },

  created() {
    this.getList()
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
        this.fromTab.types = [1, 2, 3]
        this.getExportData(this.fromTab)
      } else {
        this.page = 1

        this.getList(1)
        this.$refs.table.where.page = 1
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
      } else {
        this.fromTab = {}
      }

      this.fromTab.limit = this.limit
      this.fromTab.page = this.page
      this.fromTab.types = [1, 2, 3]
      this.pageLoading = true

      enterpriseCardApi(this.fromTab)
        .then((res) => {
          this.tableData = res.data.list
          if (this.tableData && this.tableData.length > 0) {
            this.tableData.map((value) => {
              if (value.frames.length > 1) {
                value.frames.sort((a, b) => {
                  return b.is_mastart - a.is_mastart
                }) //升序
              }
            })
          }
          this.total = res.data.count
          this.pageLoading = false
        })
        .catch((err) => {})
        .finally(() => {
          this.pageLoading = false
        })
    },

    // 展开
    showTextFn(val) {
      this.showText = val
    },

    // 多选表格
    multipleSelection(val) {
      this.selection = val
    },

    hanldeOptions(data, type) {
      switch (type) {
        // 邀请加入
        case 'invite': {
          this.$emit('invitation', data, type)
          break
        }
        // 邀请完善信息
        case 'inviteToJoin': {
          this.$emit('invitation', data, type)
          break
        }

        // 编辑
        case 'edit': {
          this.$emit('openDrawer', 'addDrawerIsShow', 'edit', data)
          break
        }

        // 离职
        case 'quit': {
          this.$emit('onQuit', data)
          break
        }

        // 转正
        case 'worker': {
          this.$emit('onWorker', data)
          break
        }

        // 删除
        case 'delete': {
          this.$emit('onDelete', data)
          break
        }
      }
    }
  },

  components: {
    formBox: () => import('../formBox.vue'),
    Table: () => import('../table.vue')
  }
}
</script>

<style lang="scss" scoped>
.divBox {
  margin: 0;
}
.add-height {
  height: 250px !important;
}
.alert {
  height: 42px;
  background: #edf5ff;
  border: 1px solid #97c3ff;
  border-radius: 6px;
  line-height: 42px;
  font-size: 13px;
  font-weight: 400;
  color: #606266;
  padding-left: 20px;
  margin-bottom: 30px;
}
.look {
  font-style: italic;
  cursor: pointer;
}
</style>
