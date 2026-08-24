<!-- 财务-账目记录-收支统计页面 -->
<template>
  <div class="divBox">
    <el-card class="mb12">
      <statisticsBox ref="formBox" @confirmData="confirmData" />
    </el-card>
    <el-card class="mb12">
      <div class="statistics-title">
        {{ $t('finance.businesstrends') }}
      </div>
      <echartBox :option-data="optionData" :styles="styles1" />
    </el-card>
    <el-row :gutter="24">
      <el-col :lg="12">
        <el-card>
          <div class="pie-tab">
            <el-row>
              <el-col :span="12" class="pie-title">
                {{ $t('finance.incometrends') }}
              </el-col>
              <el-col :span="12" class="text-right pie-select">
                <el-button
                  v-if="tableDataIncome.length > 0"
                  class="tab-btn"
                  icon="el-icon-sort"
                  size="mini"
                  @click="clickTab(1)"
                >
                  {{ $t('finance.switchstyles') }}
                </el-button>
              </el-col>
            </el-row>
            <div v-if="tableDataIncome.length > 0">
              <div v-if="!pieChartBtn1" class="mt10">
                <el-breadcrumb separator-class="el-icon-arrow-right">
                  <el-breadcrumb-item
                    v-for="(item, index) in incomeList"
                    :key="index"
                    :class="{ breadcrumb: active == item.name }"
                    ><span @click="changeActive(item, index)">{{ item.name }}</span></el-breadcrumb-item
                  >
                </el-breadcrumb>
              </div>
              <echartBox
                v-if="!pieChartBtn1"
                :option-data="pieChartData1"
                :styles="styles1"
                :type="`fd`"
                @pieChange="pieChange"
              />
              <div v-else class="pie-list">
                <tabListChart ref="tabListChart01" :table-data="tableDataIncome" />
              </div>
            </div>
            <div v-else>
              <default-page :index="10" :min-height="336" />
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :lg="12">
        <el-card>
          <div class="pie-tab">
            <el-row>
              <el-col :span="12" class="pie-title">
                {{ $t('finance.expendtrends') }}
              </el-col>
              <el-col :span="12" class="text-right pie-select">
                <el-button
                  v-if="tableDataExpend.length > 0"
                  class="tab-btn"
                  icon="el-icon-sort"
                  size="mini"
                  @click="clickTab(2)"
                >
                  {{ $t('finance.switchstyles') }}
                </el-button>
              </el-col>
            </el-row>
            <div v-if="tableDataExpend.length > 0">
              <div v-if="!pieChartBtn2" class="mt10">
                <el-breadcrumb separator-class="el-icon-arrow-right">
                  <el-breadcrumb-item
                    v-for="(item, index) in expenditureList"
                    :key="index"
                    :class="{ breadcrumb: expenditure == item.name }"
                    ><span @click="changeExpenditure(item, index)">{{ item.name }}</span></el-breadcrumb-item
                  >
                </el-breadcrumb>
              </div>
              <echartBox
                v-if="!pieChartBtn2"
                :option-data="pieChartData2"
                :styles="styles1"
                :type="`fd`"
                @pieChange="pieChange"
              />
              <div v-else class="pie-list">
                <tabListChart ref="tabListChart02" :table-data="tableDataExpend" />
              </div>
            </div>
            <div v-else>
              <default-page :index="11" :min-height="336" />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { billChartApi, billChangeBie } from '@/api/enterprise'
import { numberFormat } from '@/utils/numberFormat'
export default {
  name: 'FinanceChart',
  components: {
    statisticsBox: () => import('./components/statisticsBox'),
    echartBox: () => import('@/components/common/echarts'),
    tabListChart: () => import('./components/tabListChart'),
    defaultPage: () => import('@/components/common/defaultPage')
  },
  data() {
    return {
      time: '',
      optionData: {},
      pieChartData1: {},
      pieChartData2: {},
      pieChartBtn1: false,
      pieChartBtn2: false,
      styles1: {
        height: '300px',
        width: '100%'
      },
      expenditureId: '',
      expenditure: '支出',
      expenditureList: [
        {
          name: '支出',
          cate_id: 0,
          types: 2
        }
      ],
      activeId: '',
      active: '收入',
      incomeList: [
        {
          name: '收入',
          cate_id: 0,
          types: 1
        }
      ],
      incomeValue: 1,
      expendValue: 1,
      tableDataIncome: [],
      tableDataExpend: [],
      showNumber: 5,
      cateIds: []
    }
  },
  mounted() {},
  methods: {
    // 点击切换饼状图
    pieChange(data) {
      if (data.types == 1) {
        this.active = data.name
        if (this.activeId == data.cate_id) {
        } else {
          this.activeId = data.cate_id
          this.deconstruction(data)
          this.$nextTick(() => {
            this.incomeList.push(data)
          })
        }
      } else {
        this.expenditure = data.name
        if (this.expenditureId == data.cate_id) {
        } else {
          this.expenditureId = data.cate_id
          this.deconstruction(data)
          this.$nextTick(() => {
            this.expenditureList.push(data)
          })
        }
      }
    },
    // 收入面包屑
    changeActive(row, index) {
      this.active = row.name
      this.incomeList.splice(index + 1)

      if (row.name == '收入') {
        this.incomeList = [
          {
            name: '收入',
            cate_id: 0,
            types: 1
          }
        ]
        this.deconstruction(row)
      } else {
        this.deconstruction(row)
      }
    },
    // 支出面包屑
    changeExpenditure(row, index) {
      this.expenditure = row.name
      this.expenditureList.splice(index + 1)
      if (row.name == '支出') {
        this.expenditureList = [
          {
            name: '支出',
            cate_id: 0,
            types: 2
          }
        ]
        this.deconstruction(row)
      } else {
        this.deconstruction(row)
      }
    },

    // 获取饼状图数据
    deconstruction(data) {
      if (data.types == 2) {
        data.types = 0
      }
      let item = {
        time: this.time,
        types: data.types,
        cate_id: data.cate_id,
        cate_ids: this.cateIds
      }
      billChangeBie(item).then((res) => {
        if (data.types === 1) {
          this.shouruChart(res.data)
        } else {
          this.zhichuChart(res.data)
        }
      })
    },
    // 切换样式
    clickTab(tab) {
      if (tab === 1) {
        if (!this.pieChartBtn1) {
          this.pieChartBtn1 = true
          this.$nextTick(() => {
            this.$refs.tabListChart01.scrollAnimate()
          })
        } else {
          this.pieChartBtn1 = false
          this.$refs.tabListChart01.stop()
        }
      } else if (tab === 2) {
        if (!this.pieChartBtn2) {
          this.pieChartBtn2 = true
          this.$nextTick(() => {
            this.$refs.tabListChart02.scrollAnimate()
          })
        } else {
          this.pieChartBtn2 = false
          this.$refs.tabListChart02.stop()
        }
      }
    },
    // 趋势图
    xianchart(data) {
      // 趋势图
      this.optionData = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: [this.$t('finance.revenueamount'), this.$t('finance.expenditureamount')],
          show: true,
          right: 10,
          top: 0
        },
        grid: {
          left: 0,
          top: 40,
          right: 20,
          bottom: 0,
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
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
          data: []
        },
        dataZoom: {
          type: 'inside'
        },
        yAxis: {
          type: 'value',
          name: this.$t('finance.amountmoney'),
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
          splitLine: {
            lineStyle: {
              type: 'dashed'
            }
          }
        },
        series: [
          {
            name: this.$t('finance.totalrevenue'),
            type: 'line',
            itemStyle: {
              normal: {
                color: '#19BE6B'
              }
            },
            smooth: true,
            lineStyle: {
              width: 3
            },
            data: []
          },
          {
            name: this.$t('finance.totalexpenditure'),
            type: 'line',
            itemStyle: {
              normal: {
                color: '#FF9900'
              }
            },
            smooth: true,
            lineStyle: {
              width: 3
            },
            data: []
          }
        ]
      }
      this.optionData.xAxis.data = data.xAxis
      this.optionData.series[0].name = data.series[0].name
      this.optionData.series[0].data = data.series[0].data
      this.optionData.series[1].name = data.series[1].name
      this.optionData.series[1].data = data.series[1].data
    },
    // 收入饼状图
    shouruChart(data) {
      const incomeLegendData = []
      const incomeSeriesData = []
      this.pieChartData1 = {
        color: [
          'tomato',
          'deepskyblue',
          'orange',
          'mediumseagreen',
          'violet',
          'dodgerblue',
          'khaki',
          'turquoise',
          'salmon',
          'darkslategray'
        ],
        //中心标题
        title: {
          text: '',
          top: '42%',
          right: '61%'
        },
        // 鼠标移动展示
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        // 分类
        legend: [
          {
            type: 'scroll',
            orient: 'vertical',
            right: '0%', // 距离右侧位置
            itemGap: 18,
            itemHeight: 16, // 图标大小设置
            icon: 'circle',
            data: incomeLegendData,
            y: 'center',
            pageIconSize: 12,
            textStyle: {
              fontSize: 12,

              color: '#333',
              lineHeight: 16, // 解决提示语字显示不全
              rich: {
                a: {
                  padding: [0, 10, 0, 10],
                  color: '#FF0000'
                },
                b: {
                  color: '#666'
                }
              }
            },
            formatter: function (name) {
              let target
              let ratio
              for (let i = 0; i < data.length; i++) {
                if (data[i].name === name) {
                  target = numberFormat(data[i].sum)
                  ratio = data[i].ratio
                }
              }
              let arr = [name + '{a|' + target + '元' + '}' + '{b|' + ratio + '%' + '}']
              return arr.join('\n')
            }
          }
        ],
        series: [
          {
            name: this.$t('finance.sourceincome'),
            type: 'pie',
            radius: '55%',
            right: '40%',
            data: incomeSeriesData,
            label: {
              show: true
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
      this.tableDataIncome = data

      if (data.length > 0) {
        data.forEach((val, index) => {
          incomeLegendData.push(val.name)
          incomeSeriesData.push({
            value: val.sum,
            name: val.name,
            cate_id: val.cate_id,
            types: 1
          })
        })
      }
    },
    // 支出饼状图
    zhichuChart(data) {
      this.tableDataExpend = data
      const expendLegendData = []
      const expendSeriesData = []
      // 支出
      this.pieChartData2 = {
        color: ['#5B8FF9', '#5AD8A6', '#5D7092', '#F6BD16', '#E8684A', '#87CEFA'],
        title: {
          text: '',
          top: '42%',
          right: '61%'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: [
          {
            type: 'scroll',
            orient: 'vertical',
            right: '0%', // 距离右侧位置
            itemGap: 18,
            itemHeight: 16, // 图标大小设置
            icon: 'circle',
            data: expendLegendData,
            y: 'center',
            pageIconSize: 12,
            textStyle: {
              fontSize: 12,

              color: '#333',
              lineHeight: 16, // 解决提示语字显示不全
              rich: {
                a: {
                  padding: [0, 10, 0, 10],
                  color: '#FF0000'
                },
                b: {
                  color: '#666'
                }
              }
            },
            formatter: function (name) {
              let target
              let ratio
              for (let i = 0; i < data.length; i++) {
                if (data[i].name === name) {
                  target = numberFormat(data[i].sum)
                  ratio = data[i].ratio
                }
              }
              let arr = [name + '{a|' + target + '元' + '}' + '{b|' + ratio + '%' + '}']
              return arr.join('\n')
            }
          }
        ],
        series: [
          {
            name: this.$t('finance.sourceexpenditure'),
            type: 'pie',
            radius: '55%',
            right: '40%',
            center: ['40%', '50%'],
            data: expendSeriesData,
            label: {
              show: true
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }

      if (data.length > 0) {
        data.forEach((val, index) => {
          expendLegendData.push(val.name)
          expendSeriesData.push({
            value: val.sum,
            name: val.name,
            cate_id: val.cate_id,
            types: 2
          })
        })
      }
    },
    getChartList(data) {
      billChartApi(data).then((res) => {
        const cdata = res.data
        this.xianchart(cdata)
        this.shouruChart(cdata.incomeRank)
        this.zhichuChart(cdata.expendRank)
      })
    },
    confirmData(data) {
      this.activeId = ''
      this.time = data.time
      this.cateIds = data.cate_id
      this.expenditureList = [
        {
          name: '支出',
          cate_id: 0,
          types: 2
        }
      ]
      this.incomeList = [
        {
          name: '收入',
          cate_id: 0,
          types: 1
        }
      ]
      this.incomeValue = 1
      this.expendValue = 1

      // let arr = []
      // // 处理账目分类的数据结构
      // data.cate_id = eval(data.cate_id)
      // if (data.cate_id && data.cate_id.length > 0) {
      //   data.cate_id.map((item) => {
      //     item = eval(item)
      //     arr.push(item[item.length - 1])
      //   })
      // }

      // data.cate_id = arr
      // data.status = arr
      this.getChartList(data)
    }
  }
}
</script>

<style lang="scss" scoped>
.el-breadcrumb ::v-deep .el-breadcrumb__inner {
  color: #1890ff;

  span {
    cursor: pointer;
    font-weight: 500;
    font-size: 14px;
  }
}
.breadcrumb ::v-deep .el-breadcrumb__inner {
  color: #909399;

  span {
    font-weight: 500;
    font-size: 14px;
    cursor: pointer;
  }
}

.statistics-title {
  color: #000000;
  font-weight: 600;
}
.divBox {
  height: auto !important;
}
.el-row {
  margin-right: 0px !important;
}
.el-col {
  padding-right: 0px !important;
  padding-bottom: 12px;
}
.bgc-12 {
  color: #f0f2f5;
}
.pie-tab {
  position: relative;
  .tab-btn {
    font-size: 13px;
    margin: 1px 0 0 15px;
    color: #111111;
    ::v-deep i {
      transform: rotate(-90deg);
      font-size: 15px;
    }
  }
  ::v-deep .tab-btn:hover {
    background-color: transparent;
  }
  .pie-select {
    ::v-deep .el-input--mini {
      width: 80px;
    }
  }
  .pie-title {
    font-size: 15px;
    color: #000000;
    font-weight: bold;
    margin: 2px 0 0 0;
  }
  .pie-list {
    margin-top: 20px;
  }
}
.head-box {
  display: flex;
  align-items: center;
  .input {
    width: 240px;
    margin: 0 20px 0 10px;
  }
}
.detail-box {
  padding: 20px;
  color: #333;
  .item-box {
    display: flex;
    margin-bottom: 20px;
    font-size: 14px;
    span {
      /*width: 80px;*/
    }
    div {
      margin-left: 20px;
    }
  }
  .content-box {
    span {
      font-size: 14px;
    }
  }
}
::v-deep .el-drawer__body {
  height: 100%;
  overflow-y: auto;
}
</style>

<style lang="scss">
.content-box {
  font-size: 13px;
}
</style>
