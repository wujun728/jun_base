<template>
  <div class="divBox">
    <el-card>
      <statisticsBox ref="formBox" @confirmData="confirmData" />
    </el-card>
    <el-row :gutter="14" class="mt14">
      <el-col :span="12">
        <el-card>
          <div class="statistics-title">资产金额</div>
          <echartBox :option-data="optionData" :styles="styles1" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div class="statistics-title">物资变动情况</div>
          <div class="content border-none">
            <ul class="content-ul">
              <li>
                <el-row>
                  <el-col :span="16">采购数量</el-col>
                  <el-col :span="8" class="text-right">
                    <el-button circle size="small" type="primary">
                      <count-to :duration="1000" :end-val="Number(storageCount.put_count)" :start-val="0" />
                    </el-button>
                  </el-col>
                </el-row>
              </li>
              <li>
                <el-row>
                  <el-col :span="16">固定物资领取数量</el-col>
                  <el-col :span="8" class="text-right">
                    <el-button circle size="small" type="success">
                      <count-to :duration="1000" :end-val="storageCount.fixed_count" :start-val="0" />
                    </el-button>
                  </el-col>
                </el-row>
              </li>
              <li>
                <el-row>
                  <el-col :span="16">消耗物资领取数量</el-col>
                  <el-col :span="8" class="text-right">
                    <el-button circle size="small" type="warning">
                      <count-to :duration="1000" :end-val="storageCount.temp_count" :start-val="0" />
                    </el-button>
                  </el-col>
                </el-row>
              </li>
              <li>
                <el-row>
                  <el-col :span="16">报废数量</el-col>
                  <el-col :span="8" class="text-right">
                    <el-button circle size="small" type="info">
                      <count-to :duration="1000" :end-val="storageCount.scrap_count" :start-val="0" />
                    </el-button>
                  </el-col>
                </el-row>
              </li>
              <li>
                <el-row>
                  <el-col :span="16">维修数量</el-col>
                  <el-col :span="8" class="text-right">
                    <el-button circle size="small" type="info">
                      <count-to :duration="1000" :end-val="storageCount.repair_count" :start-val="0" />
                    </el-button>
                  </el-col>
                </el-row>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row ref="rows" :gutter="14" class="mt14">
      <el-col :span="12">
        <el-card>
          <div class="pie-tab">
            <div class="statistics-title mb20">库存预警</div>
            <div v-if="stockData.length > 0" class="content">
              <el-table :data="stockData" default-expand-all row-key="id" style="width: 100%">
                <el-table-column label="物资名称" min-width="100" prop="name" />
                <el-table-column label="规格型号" min-width="100" prop="units" />
                <el-table-column label="物资分类" min-width="100" prop="cate.cate_name" />
                <el-table-column label="当前库存" min-width="100" prop="stock" />
                <el-table-column :label="$t('public.operation')" fixed="right" prop="describe" width="80">
                  <template slot-scope="scope">
                    <el-button type="text" @click="handleManage(scope.row)">补货</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <div class="block">
                <el-pagination
                  :current-page="stockWhere.page"
                  :page-size="stockWhere.limit"
                  :total="stockTotal"
                  layout="prev, pager, next"
                  @size-change="handleStockSize"
                  @current-change="pageStockChange"
                />
              </div>
            </div>
            <div v-else>
              <default-page :height="`calc(50vh - 158px)`" :index="14"></default-page>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div class="pie-tab">
            <div class="statistics-title mb20">物资变动明细</div>
            <div v-if="detailedDat.length > 0" class="content">
              <el-table
                v-loading="detailLoading"
                :data="detailedDat"
                :height="tableHeight"
                default-expand-all
                row-key="id"
                style="width: 100%"
              >
                <el-table-column label="物资名称" min-width="130" prop="storage.name" show-overflow-tooltip />
                <el-table-column label="物资分类" min-width="100" prop="storage.cate.cate_name" show-overflow-tooltip />
                <el-table-column label="规格型号" min-width="100" prop="storage.units" show-overflow-tooltip />
                <el-table-column label="变动情况" min-width="100" prop="stock" show-overflow-tooltip>
                  <template #default="{ row }">
                    <template v-if="row.types === 0">
                      入库量
                      <span class="color1">+{{ row.num }}</span>
                    </template>
                    <template v-if="row.types === 1">
                      领用量
                      <span class="color2">+{{ row.num }}</span>
                    </template>
                    <template v-if="row.types === 4">
                      报废量
                      <span class="color-success">+1</span>
                    </template>
                  </template>
                </el-table-column>
                <el-table-column label="变动时间" min-width="100" prop="storage.units" show-overflow-tooltip>
                  <template slot-scope="scope">
                    {{ $moment(scope.row.created_at).format('yyyy-MM-DD') }}
                  </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="操作人" prop="creater.name" width="80">
                  <template slot-scope="scope">
                    <span class="none-button over-text" type="text">{{
                      scope.row.creater ? scope.row.creater.name : '--'
                    }}</span>
                  </template>
                </el-table-column>
              </el-table>
              <div class="block">
                <el-pagination
                  :current-page="where.page"
                  :page-size="where.limit"
                  :total="total"
                  layout="prev, pager, next"
                  @size-change="handleSizeChange"
                  @current-change="pageChange"
                />
              </div>
            </div>
            <div v-else>
              <default-page :index="14" :min-height="styles1.height"></default-page>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <material-dialog ref="materialDialog" :from-data="fromData" @isOk="handleMaterial"></material-dialog>
  </div>
</template>

<script>
import { storageListApi, storageRecordApi, storageRecordCensusApi } from '@/api/administration'
export default {
  name: 'FinanceChart',
  components: {
    statisticsBox: () => import('./components/statisticsBox'),
    echartBox: () => import('@/components/common/echarts'),
    materialDialog: () => import('@/views/administration/material/fixed/components/materialDialog'),
    defaultPage: () => import('@/components/common/defaultPage'),
    countTo: () => import('vue-count-to')
  },
  data() {
    return {
      time:
        this.$moment().subtract(30, 'days').format('YYYY/MM/DD') + '-' + this.$moment(new Date()).format('YYYY/MM/DD'),
      optionData: {},
      styles1: {
        height: 'calc(50vh - 158px)',
        width: '100%'
      },

      stockWhere: {
        page: 1,
        limit: 5,
        types: 0,
        stock: 0
      },
      where: {
        page: 1,
        limit: 10,
        types: [0, 1, 4],
        time: ''
      },
      total: 0,
      stockTotal: 0,
      storageCount: [],
      detailedDat: [],
      stockData: [],
      fromData: {},
      detailLoading: false,
      tableHeight: '234px'
    }
  },
  mounted() {
    this.getChartList()
    this.getStockData()
    this.getDetaileData()
    setTimeout(() => {
      this.tableHeight = this.$refs.rows.$el.offsetHeight - 140 + 'px'
    }, 300)
  },
  methods: {
    getChartList() {
      const data = {
        time: this.time
      }
      storageRecordCensusApi(data).then((res) => {
        const data = res.data
        this.storageCount = data.storage_count
        this.optionData = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'line',
              lineStyle: {
                color: '#CCCCCC'
              }
            }
          },
          legend: {
            data: [],
            show: true,
            right: 10,
            top: 0
          },
          grid: {
            left: 60,
            top: 40,
            right: 20,
            bottom: 40
          },
          toolbox: {},
          dataZoom: {
            type: 'inside'
          },
          xAxis: [
            {
              type: 'category',
              nameTextStyle: {
                color: '#CCCCCC'
              },
              axisLine: {
                lineStyle: {
                  color: '#CCCCCC'
                }
              },
              axisLabel: {
                color: '#666666'
              },
              data: ['物资采购金额', '物资报废金额', '物资维修金额']
            }
          ],
          yAxis: [
            {
              type: 'value',
              position: 'left',
              axisTick: {
                show: true,
                alignWithLabel: true
              },
              min: 0,
              nameTextStyle: {
                color: '#CCCCCC'
              },
              axisLine: {
                lineStyle: {
                  color: '#CCCCCC'
                }
              },
              axisLabel: {
                // color: '#666666',
              },
              splitLine: {
                lineStyle: {
                  type: 'dashed'
                }
              }
            }
          ],
          series: [
            {
              name: '',
              type: 'bar',
              smooth: true,
              itemStyle: {
                normal: {
                  label: {
                    show: true,
                    color: '#000000',
                    position: 'top',
                    formatter: '{c}'
                  },
                  color: function (params) {
                    var colorList = ['#1890FF', '#ED4014', '#FF9900']
                    return colorList[params.dataIndex]
                  }
                }
              },
              data: []
            }
          ]
        }
        this.optionData.series[0].data = [
          data.storage_price.put_price,
          data.storage_price.scrap_price,
          data.storage_price.repair_price
        ]
      })
    },
    confirmData(data) {
      this.time = data.time
      this.getChartList()
      this.getStockData()
      this.getDetaileData()
    },
    handleStockSize(val) {
      this.stockWhere.limit = val
      this.getStockData()
    },
    pageStockChange(page) {
      this.stockWhere.page = page
      this.getStockData()
    },
    // 获取表格数据
    async getStockData() {
      const result = await storageListApi(this.stockWhere)
      this.stockData = result.data.list || []
      this.stockTotal = result.data.count
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getDetaileData()
    },
    pageChange(page) {
      this.where.page = page
      this.getDetaileData()
    },
    // 物资变动记录
    getDetaileData() {
      this.detailLoading = true
      this.where.time = this.time
      storageRecordApi(this.where)
        .then((res) => {
          this.detailedDat = res.data.list || []
          this.total = res.data.count
          this.detailLoading = false
        })
        .catch((error) => {
          this.detailLoading = false
        })
    },
    handleManage(row) {
      this.fromData = {
        title: '补货',
        width: '520px',
        data: row,
        label: '入库说明',
        placeholder: '请填写入库说明',
        type: 4
      }
      this.$refs.materialDialog.handleOpen()
    },
    handleMaterial() {
      this.stockWhere.page = 1
      this.getStockData()
    }
  }
}
</script>

<style lang="scss" scoped>
.border-none {
  border: none !important;
}
.divBox {
  height: auto !important;
}
.statistics-title {
  color: #000000;
  font-weight: 600;
  padding-left: 10px;
  position: relative;
  &:after {
    position: absolute;
    left: 0;
    top: 0;
    content: '';
    width: 2px;
    height: 18px;
    background-color: #1890ff;
  }
}
.color1 {
  color: #19be6b;
}
.color2 {
  color: #ff9900;
}
.content {
  width: 100%;
  height: calc(50vh - 158px);
  padding-top: 5px;
  border-top: 1px solid #dfe6ec;
  ::v-deep .el-table td {
    padding: 7px 0;
  }
  ::v-deep .el-pagination {
    margin-top: 20px;
  }
  .none-button {
    color: #606266;
    cursor: auto;
  }
  .content-ul {
    padding: 0;
    margin: 25px 0 0 0;
    list-style: none;
    li {
      font-size: 13px;
      margin-bottom: 10px;
      padding-bottom: 10px;
      border-bottom: 1px solid #dfe6ec;
      &:last-of-type {
        border: none;
      }
      ::v-deep .el-row {
        display: flex;
        align-items: center;
      }
    }
  }
}
::v-deep .el-button--small.is-circle {
  padding: 0;
  width: 36px;
  height: 36px;
}
::v-deep .el-drawer__body {
  height: 100%;
  overflow-y: auto;
}
</style>
