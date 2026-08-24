<!-- 人事-职位管理-职级体系图 -->
<template>
  <div class="divBox">
    <div>
      <el-card class="normal-page">
        <div>
          <div class="flex-between">
            <div class="text-right">
              <div class="title-16">
                职级体系图
                <el-popover placement="right" trigger="hover" popper-class="monitor-yt-popover">
                  <div class="prompt-bag">点击职级或职等对应的格子，可进行添加或移除等操作</div>
                  <i class="el-icon-question" slot="reference"></i>
                </el-popover>
              </div>
            </div>
            <div class="flex lh-center">
              <el-button type="primary" class="mr10 h32" size="small" icon="el-icon-plus" @click="addLevel(1)"
                >添加职等</el-button
              >
              <el-dropdown>
                <span class="iconfont icongengduo2 pointer"></span>
                <el-dropdown-menu style="text-align: left">
                  <el-dropdown-item @click.native="addLevel(2)"> 批量修改职等区间 </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>

          <div class="rank-right v-height-flag">
            <div class="table-box mt14">
              <div v-if="tableData.length > 0" v-height>
                <el-table :data="tableData" border row-key="id">
                  <el-table-column prop="salary" label="薪资范围" width="120" fixed="left" />
                  <el-table-column prop="rankMax" label="职等" width="80" fixed="left">
                    <template slot-scope="scope">
                      <el-popover :ref="`pop-${scope.row.$index}`" placement="bottom-end" trigger="click" :offset="10">
                        <div class="right-item-list">
                          <div class="right-item" @click.stop="editLevel(1, scope.row)">编辑职等</div>
                          <div class="right-item" @click.stop="deleteLevel(scope.row)">删除职等</div>
                        </div>
                        <div slot="reference" class="default-color rank-title">
                          <span>{{ scope.row.min_level }}</span>
                          <span>-</span>
                          <span>{{ scope.row.max_level }}</span>
                        </div>
                      </el-popover>
                    </template>
                  </el-table-column>
                  <div v-for="info in tableHeader" :key="info.id">
                    <el-table-column label="职级" width="110px">
                      <template slot-scope="scope">
                        <div @click.stop="addRankColumn($event, scope.row, info.id)">
                          <div
                            v-for="item in scope.row.info"
                            v-show="info.id === item.id"
                            :key="'rank' + item.id"
                            class="pointer"
                          >
                            {{
                              item.info && item.info.rank ? item.info.rank.alias + ' (' + item.info.rank.name + ')' : ''
                            }}
                          </div>
                        </div>
                      </template>
                    </el-table-column>
                    <el-table-column :label="info.name">
                      <template slot-scope="scope">
                        <div v-for="item in scope.row.info" v-show="info.id === item.id" :key="item.id">
                          <span v-if="item.info && item.info.job.length > 0">
                            <span v-for="(col, index) in item.info.job" :key="index">
                              {{ col.name }}
                              {{ item.info.job.length - 1 !== index ? '、' : '' }}
                            </span>
                          </span>
                        </div>
                      </template>
                    </el-table-column>
                  </div>
                </el-table>
              </div>
              <div v-else>
                <default-page v-height :index="13" :min-height="520" />
              </div>
            </div>
          </div>

          <rank-dialog ref="rankDialog" :config="config" @isOk="getTableData()" />
          <right-click ref="rightClick" :config-data="configData" @handleRightClick="handleRightClick" />
        </div>
        <!-- <div>
          <level ref="level"></level>
        </div> -->
      </el-card>
    </div>
  </div>
</template>

<script>
import { rankLevelDeleteApi, rankLevelListApi, rankLevelRankApi } from '@/api/setting'
export default {
  name: 'JobLevel',
  components: {
    rankDialog: () => import('./components/rankDialog'),
    rightClick: () => import('./components/rightClick'),
    defaultPage: () => import('@/components/common/defaultPage')
  },
  data() {
    return {
      rankIndex: 0,
      tableData: [],
      tableHeader: [],
      config: {},
      event: null,
      configData: {}
    }
  },
  created() {
    this.getTableData()
  },
  methods: {
    handleRankClick(index, item) {
      this.rankIndex = index
    },
    // 获取表格数据
    async getTableData() {
      const result = await rankLevelListApi()
      this.tableData = result.data ? result.data : []
      this.tableHeader = result.data ? this.tableData[0].info : []
    },
    // 添加
    addLevel(type) {
      var title = ''
      if (type === 1) {
        title = '添加职等区间'
      } else if (type === 2) {
        title = '批量修改职等区间'
      }
      this.config = {
        title: title,
        width: '560px',
        type: type,
        edit: false
      }
      this.$refs.rankDialog.handleOpen()
    },
    // 编辑
    editLevel(type, row) {
      var title = ''
      if (type === 1) {
        title = '编辑职等区间'
      }
      this.config = {
        title: title,
        width: '520px',
        type: type,
        data: row,
        edit: true
      }
      this.$refs.rankDialog.handleOpen()
    },
    addRankColumn(event, row, columnId) {
      this.event = event
      var valueData = ''
      var rankItem = {}
      if (row.info.length > 0) {
        row.info.map((value) => {
          if (value.id === columnId) {
            valueData = value.info && value.info.rank ? value.info.rank.alias : ''
            rankItem = value
          }
        })
      }
      this.configData = {
        type: 1,
        cate_id: columnId,
        data: row,
        value: valueData,
        rank_item: rankItem
      }
      this.$refs.rightClick.rightClick(event)
    },
    // 删除职级类别
    deleteLevel(item) {
      this.$modalSure('你确定要删除该职等吗').then(() => {
        rankLevelDeleteApi(item.id)
          .then((res) => {
            this.getTableData()
          })
          .catch((error) => {})
      })
    },
    handleRightClick(data) {
      if (data.type === 1) {
        var valueData = ''
        var rankItem = {}
        if (data.row.info.length > 0) {
          data.row.info.map((value) => {
            if (value.id === data.id) {
              valueData = value.info && value.info.rank ? value.info.rank.alias : ''
              rankItem = value
            }
          })
        }
        rankLevelRankApi(data.id).then((res) => {
          const dataS = res.data ? res.data : []
          if (dataS.length > 0) {
            this.configData = {
              type: 2,
              cate_id: data.id,
              rank_item: rankItem,
              data: data.row,
              value: valueData,
              tableData: dataS
            }
            this.$refs.rightClick.rightClick(this.event)
          } else {
            this.$message.error('暂无可用职级')
            this.$refs.rightClick.menuVisible = false
          }
        })
      } else {
        this.getTableData()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.text-right {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #606266;
}
.rank-right {
  ::v-deep .el-table .cell {
    text-align: center;
    font-size: 13px;
    cursor: pointer;
    min-height: 44px;
    line-height: 1;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .rank-title,
  .salary-title {
    font-size: 0;
    span {
      font-size: 13px;
    }
  }
  .rank-title {
    cursor: pointer;
    span {
      font-weight: bold;
    }
  }
}

::v-deep .el-table td,
::v-deep .el-table th {
  padding: 0;
}
::v-deep .el-table .cell > div {
  min-height: 42px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  line-height: 1.5;
}
.ml {
  margin-left: 0;
}
.prompt-bag {
  background-color: #edf5ff;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 12px;
  color: #606266;
}
.el-icon-question {
  cursor: pointer;
  color: #1890ff;
  font-size: 15px;
}
.icongengduo2 {
  font-size: 32px !important;
}
</style>
