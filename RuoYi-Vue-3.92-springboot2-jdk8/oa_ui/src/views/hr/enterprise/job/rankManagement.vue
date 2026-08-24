<!-- 人事-职位管理-职级管理 -->
<template>
  <div class="divBox">
    <el-card class="normal-page">
      <div>
        <div class="v-height-flag">
          <el-row>
            <el-col v-bind="gridl">
              <div class="rank-left v-height-flag">
                <el-row class="rank-left-header">
                  <el-col :span="18">
                    <div class="title">{{ $t('hr.ranktypes') }}</div>
                  </el-col>
                  <el-col :span="6" class="text-right">
                    <el-tooltip effect="dark" content="添加类别" placement="top">
                      <span @click="addRankType()" class="iconfont icontianjia color-doc pointer"></span>
                    </el-tooltip>
                  </el-col>
                </el-row>
                <div class="v-height-flag">
                  <div v-height>
                    <el-scrollbar style="height: 100%">
                      <ul class="rank-left-top">
                        <li
                          v-for="(item, index) in rankData"
                          @click="handleRankClick(index, item)"
                          :key="index"
                          class="clearfix"
                          :class="rankIndex === index ? 'active' : ''"
                        >
                          <div class="pull-left">{{ item.name }}</div>
                          <div class="pull-right">
                            <el-popover
                              :ref="`pop-${item.id}`"
                              placement="bottom-end"
                              trigger="click"
                              :offset="10"
                              @after-enter="handleShow(item.id)"
                              @hide="handleHide"
                            >
                              <div class="right-item-list">
                                <div class="right-item" @click.stop="editRankType(item)">{{ $t('public.edit') }}</div>
                                <div class="right-item" @click.stop="deleteRankType(item)">
                                  {{ $t('public.delete') }}
                                </div>
                              </div>
                              <i
                                v-show="rankIndex === index"
                                slot="reference"
                                class="icon iconfont icongengduo pointer rank-icon"
                              />
                            </el-popover>
                          </div>
                        </li>
                      </ul>
                    </el-scrollbar>
                  </div>
                </div>
              </div>
            </el-col>
            <el-col v-bind="gridr">
              <div class="rank-right v-height-flag">
                <oaFromBox
                  :dropdownList="dropdownList"
                  :isViewSearch="false"
                  :total="total"
                  :title="`职级列表`"
                  :btnText="'添加职级'"
                  :isAddBtn="true"
                  @addDataFn="addRank"
                  @dropdownFn="batchHandleDelete"
                  @confirmData="confirmData"
                ></oaFromBox>

                <div class="mt10 table-box">
                  <el-table
                    :data="tableData"
                    row-key="id"
                    :height="tableHeight"
                    default-expand-all
                    :tree-props="{ children: 'children' }"
                    @selection-change="handleSelectionChange"
                  >
                    <el-table-column type="selection" width="55"></el-table-column>
                    <el-table-column prop="name" :label="$t('hr.rankname')" min-width="150" />
                    <el-table-column prop="alias" :label="$t('hr.ranktype')" min-width="100" />
                    <el-table-column prop="info" label="职级描述" min-width="200" show-overflow-tooltip>
                      <template slot-scope="scope">
                        <div class="line1">{{ scope.row.info || '--' }}</div>
                      </template>
                    </el-table-column>
                    <el-table-column prop="card.name" :label="$t('hr.founder')" min-width="120" />
                    <el-table-column prop="address" :label="$t('public.operation')" width="140">
                      <template slot-scope="scope">
                        <el-button
                          type="text"
                          @click="editRank(scope.row)"
                          v-hasPermi="['hr:enterprise:job:rankManagement:edit']"
                          >{{ $t('public.edit') }}</el-button
                        >
                        <el-button
                          type="text"
                          @click="deleteRank(scope.row)"
                          v-hasPermi="['hr:enterprise:job:rankManagement:delete']"
                          >{{ $t('public.delete') }}</el-button
                        >
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
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
        </div>
        <!-- 通用弹窗表单   -->
        <dialog-form ref="dialogForm" :roles-config="rolesConfig" :form-data="formBoxConfig" @isOk="getTableData()" />
      </div>
    </el-card>
  </div>
</template>

<script>
import dialogForm from './components/index'
import oaFromBox from '@/components/common/oaFromBox'
import {
  rankCateCreateApi,
  rankCateDeleteApi,
  rankCateEditApi,
  rankCateListApi,
  rankCreateApi,
  rankDeleteApi,
  rankEditApi,
  rankListApi
} from '@/api/setting'

export default {
  name: 'JobRank',
  components: {
    dialogForm,
    oaFromBox
  },
  data() {
    return {
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
      rankData: [],
      rankIndex: 0,
      tableData: [],
      activeValue: '',
      rolesConfig: [],
      formBoxConfig: {},
      dropdownList: [
        {
          label: '删除',
          value: 1
        }
      ],
      where: {
        page: 1,
        limit: 15,
        cate_id: 0
      },
      total: 0,
      multipleSelection: []
    }
  },
  created() {
    this.getRankList()
  },
  methods: {
    handleRankClick(index, item) {
      this.rankIndex = index
      this.where.cate_id = item.id
      this.where.page = 1
      this.getTableData()
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    // 获取表格数据
    async getTableData() {
      const result = await rankListApi(this.where)
      this.tableData = result.data.list
      this.total = result.data.count
    },
    // 编辑窗口显示
    handleShow(value) {
      this.activeValue = value
    },
    confirmData(data) {
      this.where = { ...this.where, ...data }
      this.where.page = 1
      this.getTableData()
    },
    // 编辑窗口隐藏
    handleHide() {
      this.activeValue = ''
    },
    async getRankList() {
      const result = await rankCateListApi({ page: 1, limit: 0 })
      this.rankData = result.data.list
      if (this.rankData.length > 0) {
        this.where.cate_id = this.rankData[0].id
        this.getTableData()
      }
    },
    // 添加职级类别
    async addRankType() {
      await this.$modalForm(rankCateCreateApi())
      this.getRankList()
    },
    // 编辑职级类别
    async editRankType(item) {
      this.closePopover()
      await this.$modalForm(rankCateEditApi(item.id))
      this.getRankList()
      this.rankIndex = 0
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 删除职级类别
    deleteRankType(item) {
      this.closePopover()
      this.$modalSure('你确定要删除该职级类别吗').then(() => {
        rankCateDeleteApi(item.id).then((res) => {
          this.rankIndex = 0
          this.getRankList()
        })
      })
    },
    closePopover() {
      this.$refs[`pop-${this.activeValue}`][0].doClose()
    },
    // 添加职级
    async addRank() {
      if (this.where.cate_id === 0) {
        this.$message.error('请先创建职级类别,再添加职级')
      } else {
        const result = await rankCreateApi({ cate_id: this.where.cate_id })
        this.formBoxConfig = {
          title: result.data.title,
          width: '500px',
          method: result.data.method,
          action: result.data.action.substr(4)
        }
        this.rolesConfig = result.data.rule
        await this.$refs.dialogForm.openBox()
      }
    },
    // 编辑职级
    async editRank(row) {
      const result = await rankEditApi(row.id)
      this.formBoxConfig = {
        title: result.data.title,
        width: '500px',
        method: result.data.method,
        action: result.data.action.substr(4)
      }
      this.rolesConfig = result.data.rule
      await this.$refs.dialogForm.openBox()
    },
    deleteRank(row) {
      this.$modalSure('你确定要删除该职级吗').then(() => {
        this.handleDelete(row.id)
      })
    },
    batchHandleDelete() {
      if (this.multipleSelection.length <= 0) {
        this.$message.error('至少选择一项要删除的内容')
      } else {
        var ids = []
        this.multipleSelection.forEach((value) => {
          ids.push(value.id)
        })
        this.$modalSure('确定要全部删除已选择的内容吗').then(() => {
          this.handleDelete(ids.join(','))
        })
      }
    },
    async handleDelete(id) {
      await rankDeleteApi(id)
      if (this.where.page > 1 && this.tableData.length <= 1) {
        this.where.page--
      }
      await this.getTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
.rank-left {
  min-height: calc(100vh - 77px);
  margin: -20px 0 -20px -20px;
  padding: 20px 0 0 0;
  border-right: 1px solid #eeeeee;
  // background-color: rgba(247, 251, 255, 1);
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  .clearfix {
    cursor: pointer;
  }
  .rank-left-header {
    margin: 0 12px;
    display: flex;
    align-items: center;
    .title {
      padding-left: 6px;
      position: relative;
      font-family: PingFang SC, PingFang SC;
      font-weight: 600;
      font-size: 14px;
      color: #303133;
    }
    i {
      font-size: 20px;
      margin-top: 2px;
    }
  }
  .rank-left-top {
    list-style: none;
    margin: 15px 0 0 0;
    padding: 0;
    li {
      height: 40px;
      padding-left: 20px;
      padding-right: 15px;
      line-height: 40px;
      color: rgba(0, 0, 0, 0.85);
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #303133;
      border-right: 2px solid transparent;
      &.active {
        background: rgba(24, 144, 255, 0.08);
        color: #1890ff;
        font-weight: 600;
        border-color: #1890ff;
      }
    }
  }
}
.rank-right {
  margin-left: 15px;
}

.rank-icon {
  font-size: 14px;
}
</style>
