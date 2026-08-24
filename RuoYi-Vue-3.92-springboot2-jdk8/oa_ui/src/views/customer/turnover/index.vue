<template>
  <div class="divBox" style="">
    <el-card class="mb12">
      <oaFromBox
        :isAddBtn="false"
        :isTotal="false"
        :isViewSearch="false"
        :search="search"
        :sortSearch="false"
        class="from-box"
        @confirmData="confirmData"
      ></oaFromBox>
      <!-- <form-box ref="formBox" @confirmData="confirmData" :options="options" /> -->
    </el-card>
    <div class="item-info-card">
      <el-row :gutter="24" class="elRow">
        <el-col :lg="8" :md="6" :sm="12" :xl="4">
          <div class="item-info-list">
            <el-card>
              <div class="item">
                <div class="item-top">
                  <div class="tit over-text">总收入（元）</div>
                  <el-popover placement="top-start" trigger="hover" width="150">
                    <div class="text-align">
                      环比{{ income.ratio > 0 ? '增加' : '减少' }}
                      <span v-if="income.ratio > 0">{{ income.ratio }}%</span>
                      <span v-else-if="income.ratio < 0">{{ income.ratio | absNum }}%</span>
                      <span v-else>0%</span>
                    </div>
                    <div slot="reference" class="img-box">
                      <span v-if="income.ratio > 0" class="iconfont icontongji-shangzhang" />
                      <span v-if="income.ratio < 0" class="iconfont icontongji-xiajiang" />
                      <div :class="income.ratio > 0 ? 'per' : 'fper'">
                        {{ income.ratio > 0 ? '+' : '' }}{{ income.ratio || 0 }}%
                      </div>
                    </div>
                  </el-popover>
                </div>
                <div class="num">
                  {{ income.price || 0 }}
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
        <el-col :lg="8" :md="6" :sm="12" :xl="4">
          <div class="item-info-list">
            <el-card>
              <div class="item">
                <div class="item-top">
                  <div class="tit over-text">{{ $t('customer.newcustomer') }}</div>
                  <el-popover placement="top-start" trigger="hover" width="150">
                    <div class="text-align">
                      环比{{ new_customer.ratio > 0 ? '增加' : '减少' }}
                      <span v-if="new_customer.ratio > 0">{{ new_customer.ratio }}%</span>
                      <span v-else-if="new_customer.ratio < 0">{{ new_customer.ratio | absNum }}%</span>
                      <span v-else>0%</span>
                    </div>
                    <div slot="reference" class="img-box">
                      <span v-if="new_customer.ratio > 0" class="iconfont icontongji-shangzhang" />
                      <span v-if="new_customer.ratio < 0" class="iconfont icontongji-xiajiang" />
                      <div :class="new_customer.ratio > 0 ? 'per' : 'fper'">
                        {{ new_customer.ratio > 0 ? '+' : '' }}{{ new_customer.ratio || 0 }}%
                      </div>
                    </div>
                  </el-popover>
                </div>
                <div class="num">
                  {{ new_customer.count || 0 }}
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
        <el-col :lg="8" :md="6" :sm="12" :xl="4">
          <div class="item-info-list">
            <el-card>
              <div class="item">
                <div class="item-top">
                  <el-popover placement="top-start" trigger="hover">
                    <div class="text-align">按开始日期统计</div>
                    <div slot="reference" class="tit over-text hand">{{ $t('customer.newcontract') }}</div>
                  </el-popover>
                  <el-popover placement="top-start" trigger="hover" width="150">
                    <div class="text-align">
                      环比{{ new_contract.ratio > 0 ? '增加' : '减少' }}
                      <span v-if="new_contract.ratio > 0">{{ new_contract.ratio }}%</span>
                      <span v-else-if="new_contract.ratio < 0">{{ new_contract.ratio | absNum }}%</span>
                      <span v-else>0%</span>
                    </div>
                    <div slot="reference" class="img-box">
                      <span v-if="new_contract.ratio > 0" class="iconfont icontongji-shangzhang" />
                      <span v-if="new_contract.ratio < 0" class="iconfont icontongji-xiajiang" />
                      <div :class="new_contract.ratio > 0 ? 'per' : 'fper'">
                        {{ new_contract.ratio > 0 ? '+' : '' }}{{ new_contract.ratio || 0 }}%
                      </div>
                    </div>
                  </el-popover>
                </div>
                <div class="num">
                  {{ new_contract.count || 0 }}
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
        <el-col :lg="8" :md="6" :sm="12" :xl="4" style="min-height: 1px">
          <div class="item-info-list">
            <el-card>
              <div class="item">
                <div class="item-top">
                  <el-popover placement="top-start" trigger="hover">
                    <div class="text-align">按开始日期统计</div>
                    <div slot="reference" class="tit hand over-text">{{ $t('customer.newamount') }}</div>
                  </el-popover>
                  <el-popover placement="top-start" trigger="hover" width="150">
                    <div class="text-align">
                      环比{{ new_contract_price.ratio > 0 ? '增加' : '减少' }}

                      <span v-if="new_contract_price.ratio > 0">{{ new_contract_price.ratio }}%</span>
                      <span v-else-if="new_contract_price.ratio < 0">{{ new_contract_price.ratio | absNum }}%</span>
                      <span v-else>0%</span>
                    </div>
                    <div slot="reference" class="img-box">
                      <span v-if="new_contract_price.ratio > 0" class="iconfont icontongji-shangzhang" />
                      <span v-if="new_contract_price.ratio < 0" class="iconfont icontongji-xiajiang" />
                      <div :class="new_contract_price.ratio > 0 ? 'per' : 'fper'">
                        {{ new_contract_price.ratio > 0 ? '+' : '' }}{{ new_contract_price.ratio || 0 }}%
                      </div>
                    </div>
                  </el-popover>
                </div>
                <div class="num">
                  {{ new_contract_price.price || 0 }}
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
        <el-col :lg="8" :md="6" :sm="12" :xl="4">
          <div class="item-info-list">
            <el-card>
              <div class="item">
                <div class="item-top">
                  <div class="tit over-text">{{ $t('customer.contractrenewal') }}</div>
                  <el-popover placement="top-start" trigger="hover" width="150">
                    <div class="text-align">
                      环比{{ renew.ratio > 0 ? '增加' : '减少' }}
                      <span v-if="renew.ratio > 0">{{ renew.ratio }}%</span>
                      <span v-else-if="renew.ratio < 0">{{ renew.ratio | absNum }}%</span>
                      <span v-else>0%</span>
                    </div>
                    <div slot="reference" class="img-box">
                      <span :class="renew.ratio > 0 ? 'per' : 'fper'"></span>
                      <span v-if="renew.ratio > 0" class="iconfont icontongji-shangzhang" />
                      <span v-if="renew.ratio < 0" class="iconfont icontongji-xiajiang" />
                      <div :class="renew.ratio > 0 ? 'per' : 'fper'">
                        {{ renew.ratio > 0 ? '+' : '' }}{{ renew.ratio || 0 }}%
                      </div>
                    </div>
                  </el-popover>
                </div>
                <div class="num">
                  {{ renew.price || 0 }}
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
        <el-col :lg="8" :md="6" :sm="12" :xl="4">
          <div class="item-info-list">
            <el-card>
              <div class="item">
                <div class="item-top">
                  <div class="tit over-text">{{ $t('customer.uncollectedamount') }}</div>
                </div>
                <div class="num">
                  {{ uncollected_price.price || 0 }}
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>
    <el-row :gutter="24">
      <el-col :lg="24">
        <el-card>
          <div class="statistics-title">业绩趋势</div>
          <div>
            <echartBox :option-data="optionData1" :styles="styles1" />
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :lg="12">
        <el-row class="mb14">
          <el-card>
            <div class="statistics-title">
              {{ $t('customer.departmentperformance') }}
            </div>
            <div v-if="frame_rank.length > 0" class="statistics-department">
              <div class="item"></div>
              <div class="item"></div>
            </div>
            <div v-else class="default">
              <img alt="" src="../../../assets/images/def1.png" />
              <span>{{ $t('public.message14') + '~' }}</span>
            </div>

            <echartBox v-if="frame_rank.length > 0" :option-data="optionData" :styles="styles1" />
          </el-card>
        </el-row>

        <el-row>
          <el-card style="height: 525px">
            <div class="statistics-title">
              {{ $t('customer.contracttype') }}
            </div>
            <div v-if="contract_rank.length > 0" class="ml10">
              <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item
                  v-for="(item, index) in breadcrumbList"
                  :key="index"
                  :class="{ breadcrumb: active == item.name }"
                  ><span @click="changeActive(item, index)">{{ item.name }}</span></el-breadcrumb-item
                >
              </el-breadcrumb>
            </div>

            <div v-if="contract_rank.length > 0" ref="init" class="mt50">
              <echartBox :option-data="pieChartData" :styles="styles1" @pieChange="pieChange" />
            </div>
            <div v-else class="default">
              <img alt="" src="../../../assets/images/def1.png" style="width: 200px; height: 150px" />
              <span>{{ $t('public.message14') + '~' }}</span>
            </div>
          </el-card>
        </el-row>
      </el-col>
      <el-col :lg="12">
        <el-card>
          <!-- 业绩排行榜 -->
          <div class="statistics-title">
            {{ $t('customer.salestop') }}
          </div>
          <div class="table-box mt14 v-height-flag">
            <div style="height: 838px">
              <el-table
                ref="table"
                :data="tableData"
                default-expand-all
                max-height="800"
                row-key="id"
                style="width: 100%"
              >
                <el-table-column type="index" :label="$t('customer.ranking')" width="50" />
                <el-table-column :label="$t('toptable.name')" min-width="80" prop="name" />
                <el-table-column :label="$t('toptable.department')" min-width="90" prop="frame_name">
                  <template #default="{ row }">
                    <el-tooltip :content="row.frame_name" placement="top">
                      <div class="over-text">{{ row.frame_name }}</div>
                    </el-tooltip>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('customer.completeproportion')" min-width="120" prop="ratio">
                  <template slot-scope="scope">
                    <el-progress
                      :color="customColorMethod(scope.row)"
                      :percentage="scope.row.ratio"
                      :show-text="true"
                    ></el-progress>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('customer.completeamount')" min-width="120" prop="price" />
                <el-table-column label="支出金额（元）" min-width="120" prop="expend" />
                <el-table-column label="净额（元）" min-width="120" prop="net_amount" />
              </el-table>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { salesmanDataListApi, performanceStatisticsApi, contracRankApi, trendStatisticsApi } from '@/api/enterprise'
import { numberFormat } from '@/utils/numberFormat'

export default {
  name: 'Turnover',
  components: {
    oaFromBox: () => import('@/components/common/oaFromBox'),
    // formBox: () => import('./components/formBox'),
    echartBox: () => import('@/components/common/echarts')
  },
  data() {
    return {
      optionData: {},
      optionData1: {},
      pieChartData: {},
      income: {},
      renew: {},
      active: '合同分类',
      breadcrumbList: [
        {
          name: '合同分类',
          id: ''
        }
      ],
      category_id: '',
      contract_rank: [],
      frame_rank: [],
      new_customer: {},
      new_contract: {},
      new_contract_price: {},
      uncollected_price: {},
      styles1: {
        height: '300px',
        width: '100%',
        margin: 'auto'
      },
      tableData: [],
      where: {
        page: 1,
        limit: 15,
        scope_frame: 'all',
        time: this.$moment().subtract(1, 'months').format('YYYY/MM/DD') + '-' + this.$moment().format('YYYY/MM/DD')
      },
      activeId: '',
      total: 0,
      search: [
        {
          field_name: '开始时间',
          field_name_end: '结束时间',
          field_name_en: 'time',
          form_value: 'date_picker',
          data_dict: [
            this.$moment().startOf('months').format('YYYY/MM/DD'),
            this.$moment().endOf('months').format('YYYY/MM/DD')
          ]
        },
        {
          field_name: '管理范围',
          field_name_en: 'scope_frame',
          form_value: 'manage'
        },
        {
          field_name: '合同分类',
          field_name_en: 'category_id',
          form_value: 'cascader',
          data_dict: []
        }
      ],
      timeVal: [
        this.$moment().startOf('months').format('YYYY/MM/DD'),
        this.$moment().endOf('months').format('YYYY/MM/DD')
      ],
      time: ''
    }
  },
  filters: {
    absNum(a) {
      if (a < 0) {
        a = a * -1
      }
      return a
    }
  },
  mounted() {
    this.where.time = `${this.timeVal[0]}-${this.timeVal[1]}`
    this.contractList()
    this.getChartList()
    this.trendStatistics()
    this.getTableData()
    this.getActive()
  },
  methods: {
    // 获取合同分类列表
    contractList() {
      let data = {
        level: '',
        types: 'contract_type'
      }
      this.$store.dispatch('user/getDictList', data).then((res) => {
        setTimeout(() => {
          const resultDict = res.find((item) => item.dict_ident == data.types)
          if (resultDict.list) {
            this.search[2].data_dict = resultDict.list
          }
        }, 300)
      })
    },
    format(percentage) {
      if (percentage == 100) {
        return '完成'
      } else {
        return `${percentage}%`
      }
    },
    confirmData(data) {
      if (data == 'reset') {
        this.search[0].data_dict = this.timeVal
        this.where = {
          time: this.timeVal[0] + '-' + this.timeVal[1],
          category_id: '',
          scope_frame: 'all'
        }
      } else {
        this.where = { ...this.where, ...data }
      }
      this.breadcrumbList = []
      this.getChartList(data.id)
      this.getTableData(data.id)
      let newName = {
        name: '合同分类',
        id: ''
      }
      this.activeId = ''
      this.breadcrumbList.push(newName)
      this.trendStatistics()
      this.getActive()
      this.contractList()
    },
    // 获取业绩趋势图数据
    trendStatistics() {
      trendStatisticsApi(this.where).then((res) => {
        this.xianchart(res.data)
      })
    },

    // 获取表格数据
    getTableData(id) {
      let obj = {}
      for (let key in this.where) {
        obj[key] = this.where[key]
      }
      obj.limit = 0
      salesmanDataListApi(obj).then((res) => {
        this.tableData = res.data.list.map((item) => {
          item.price = numberFormat(Number(item.price))
          return item
        })
        function compare(property) {
          return function (a, b) {
            var value1 = a[property]
            var value2 = b[property]
            return value2 - value1
          }
        }
        this.tableData.sort(compare('ratio'))
        this.total = res.data.count
      })
    },

    getChartList(id) {
      performanceStatisticsApi(this.where).then((res) => {
        const data = res.data
        this.income = data.income
        this.income.price = numberFormat(data.income.price)
        this.renew = data.renew
        this.renew.price = numberFormat(data.renew.price)
        this.new_customer = data.new_customer
        this.new_contract = data.new_contract
        this.new_contract_price = data.new_contract_price
        this.new_contract_price.price = numberFormat(data.new_contract_price.price)
        this.uncollected_price = data.uncollected_price
        this.uncollected_price.price = numberFormat(data.uncollected_price.price)
        this.frame_rank = res.data.frame_rank

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

          color: ['#1890FF'],
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
              data: []
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
                color: '#666666'
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
              barWidth: 25,
              itemStyle: {
                normal: {
                  label: {
                    show: true,
                    color: '#fff',
                    fontSize: 14,
                    lineHeight: 20,
                    backgroundColor: '#666',
                    padding: [3, 5, 3, 5],
                    borderRadius: 5,
                    position: 'top',
                    formatter: '{c}'
                  }
                }
              },
              data: []
            }
          ]
        }
        if (data.frame_rank.length > 0) {
          this.optionData.series[0].data = data.frame_rank.map((item) => item.price)
          this.optionData.xAxis[0].data = data.frame_rank.map((item) => item.name)
        }
      })
    },
    // 趋势图
    xianchart(data) {
      // 趋势图
      this.optionData1 = {
        color: ['#19BE6B', '#FF9900'],

        tooltip: {
          trigger: 'axis',
          formatter: (option) => {
            return `${this.$moment(option[0].axisValue).format('YYYY年MM月DD日')}<br/>${option[0].seriesName}：${
              option[0].value
            }元<br/>${option[1].seriesName}：${option[1].value}元`
          }
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
          boundaryGap: true,
          nameTextStyle: {
            color: '#86909C'
          },
          axisTick: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: '#C9CDD4'
            }
          },
          axisLabel: {
            color: '#666666'
          },
          data: data.xAxis
        },
        yAxis: {
          axisLine: {
            lineStyle: {
              color: '#fff'
            }
          },
          axisLabel: {
            color: '#86909C'
          },
          splitLine: {
            lineStyle: {
              type: 'dashed'
            }
          }
        },
        series: [
          {
            name: '流入',
            type: 'line',
            itemStyle: {
              normal: {
                color: '#19BE6B'
              }
            },
            smooth: true,
            data: data.series[0].data
          },
          {
            name: '流出',
            type: 'line',
            itemStyle: {
              normal: {
                color: '#FF9900'
              }
            },
            smooth: true,
            data: data.series[1].data
          },
          {
            name: this.$t('finance.totalexpenditure'),
            type: 'line',
            symbol: 'circle',
            // symbolSize: 5, // 圆点大小
            smooth: true,
            lineStyle: {
              width: 1,
              shadowColor: 'rgba(0, 0, 0, 0.5)',
              shadowBlur: 10,
              color: '#19BE6B'
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: '#19BE6B' // 0% 处的颜色
                  },
                  {
                    offset: 0.9,
                    color: '#fff' // 100% 处的颜色
                  }
                ],
                global: false
              }
            },
            data: data.series[0].data
          },
          {
            name: this.$t('finance.totalexpenditure'),
            type: 'line',
            symbol: 'circle',
            // symbolSize: 5, // 圆点大小
            smooth: true,
            lineStyle: {
              width: 1,
              shadowColor: 'rgba(0, 0, 0, 0.5)',
              shadowBlur: 10,
              color: '#FF9900'
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: '#FF9900' // 0% 处的颜色
                  },
                  {
                    offset: 0.9,
                    color: '#FFF' // 100% 处的颜色
                  }
                ],
                global: false
              }
            },
            data: data.series[1].data
          }
        ]
      }
    },
    // 处理圆环显示数据
    deconstruction(data) {
      let boxData = []
      let totalData = ''
      this.contract_rank.map((item) => {
        let itemData = {
          value: item.price,
          name: item.category_name,
          id: item.category_id
        }
        boxData.push(itemData)
      })
      totalData = this.contract_rank.reduce((totalData, obj) => (totalData += Number(obj.price)), 0)
      totalData = numberFormat(parseFloat(totalData).toFixed(2))

      this.pieChartData = {
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
        // 鼠标移动展示
        tooltip: {
          trigger: 'item',
          enterable: true,
          formatter: (option) => {
            return `${option.seriesName} <br/> ${option.name}(${data[option.dataIndex].count}): ${option.value}元
            ${option.percent}%`
          }
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
            data: '',

            y: 'center',
            pageIconSize: 12,
            textStyle: {
              fontSize: 12,
              color: '#333',
              lineHeight: 20, // 解决提示语字显示不全
              rich: {
                a: {
                  padding: [0, 10, 0, 10],
                  color: '#FF0000'
                },
                b: {
                  color: '#666'
                },
                c: {
                  color: '#ff9900'
                }
              }
            },
            formatter: function (name) {
              let target
              let ratio
              let count
              let expend
              let total = 0
              // 首先计算总数
              data.forEach((item) => {
                total += Number(item.price)
              })

              // 然后找到每个项目，并计算其百分比
              for (let i = 0; i < data.length; i++) {
                if (data[i].category_name === name) {
                  target = numberFormat(data[i].price)
                  ratio = ((Number(data[i].price) / total) * 100).toFixed(2) // 计算百分比
                  count = data[i].count
                  expend = data[i].expend
                  if (data[i].expend != 0) {
                    expend = '-' + data[i].expend
                  }
                }
              }
              let arr = [`${name}(${count}){a|${target}元}{c|${expend}元} {b|${ratio}%}`]
              return arr.join('\n')
            }
          }
        ],

        series: [
          {
            type: 'pie',
            data: boxData,
            label: {
              show: true,
              position: 'center',
              formatter: [totalData, '{name|总计合同金额(元)}'].join('\n'),
              color: '#333',
              textStyle: {
                fontSize: 18,
                fill: '#333',
                lineHeight: 26,
                rich: {
                  name: {
                    fontSize: 12,
                    color: '#3D3D3D'
                  }
                }
              }
            },

            right: '40%',
            name: this.$t(`customer.contracttype`),
            radius: ['55%', '70%'],
            center: ['40%', '50%']
          }
        ]
      }
    },
    indexAdd(index) {
      const page = Number(this.where.page) // 当前页码
      const pagesize = Number(this.where.limit) // 每页条数
      index = Number(index)
      return index + 1 + (page - 1) * pagesize
    },
    customColorMethod(row) {
      return 'rgb(' + (255 - row.ratio * 12) + ',' + (255 - row.ratio * 12) + ',255)'
    },
    getActive() {
      let data = {
        time: this.where.time,
        scope_frame: this.where.scope_frame,
        category_id: []
      }
      // if (this.where.category_id)
      //   this.where.category_id.forEach((item, index) => {
      //     data.category_id[index] = '[' + item.join(',') + ']'
      //   })
      contracRankApi(data).then((res) => {
        this.contract_rank = res.data
        this.deconstruction(this.contract_rank)
      })
    },
    async changeActive(row, index) {
      this.active = row.name
      this.activeId = row.id
      this.breadcrumbList.splice(index + 1)

      if (row.name == '合同分类') {
        let item = {
          time: this.where.time,
          scope_frame: this.where.scope_frame,
          category_id: []
        }
        if (this.where.category_id)
          this.where.category_id.forEach((item, index) => {
            item.category_id[index] = '[' + item.join(',') + ']'
          })
        const result = await contracRankApi(item)
        this.contract_rank = result.data
        this.deconstruction(result.data)
        this.breadcrumbList = [
          {
            name: '合同分类',
            id: ''
          }
        ]
      } else {
        let item = {
          time: this.where.time,
          scope_frame: this.where.scope_frame,
          category_id: [],
          category: row.id
        }
        if (this.where.category_id)
          this.where.category_id.forEach((item, index) => {
            item.category_id[index] = '[' + item.join(',') + ']'
          })
        const result = await contracRankApi(item)
        this.contract_rank = result.data
        this.deconstruction(result.data)
      }
    },
    // 点击圆环图
    async pieChange(row) {
      this.active = row.name
      if (this.activeId === row.id) {
        return
      } else {
        this.activeId = row.id
        let item = {
          time: this.where.time,
          scope_frame: this.where.scope_frame,
          category_id: [],
          category: row.id
        }
        if (this.where.category_id)
          this.where.category_id.forEach((item, index) => {
            item.category_id[index] = '[' + item.join(',') + ']'
          })

        const result = await contracRankApi(item)
        this.contract_rank = result.data
        this.deconstruction(result.data)
        this.breadcrumbList.push(row)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.el-breadcrumb ::v-deep .el-breadcrumb__inner {
  color: #1890ff;
  span {
    font-size: 14px;
    cursor: pointer;
  }
}
.hand {
  cursor: pointer;
}
.text-align {
  width: 100%;
  text-align: center;
  font-size: #606266;
  font-size: 13px;
}
.breadcrumb ::v-deep .el-breadcrumb__inner {
  color: #909399;
  span {
    font-size: 14px;
    cursor: pointer;
  }
}

.mt50 {
  margin: 0 auto;
  margin-top: 40px;
  min-width: 400px;
  max-width: 700px;
  height: 300px;
}

.divBox {
  height: auto !important;
}

.el-row {
  margin-right: 0px !important;
}
.elRow {
  display: flex;
  flex-wrap: wrap;
}

.el-col {
  padding-right: 0px !important;
  padding-bottom: 12px;
}
.item-info-card {
  width: 100%;
  .item-info-list {
    width: 100%;
    &:last-of-type {
      margin-right: 0;
    }
  }
  ::v-deep .el-card {
    min-width: 220px;
  }
}
.per {
  font-size: 13px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #19be6b;
}
.fper {
  font-size: 13px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #ff9900;
}
.icontongji-shangzhang {
  color: #19be6b;
  margin-right: 4px;
  margin-left: 4px;
}
.icontongji-xiajiang {
  color: #ff9900;
  margin-right: 4px;
  margin-left: 4px;
}
.item {
  display: flex;
  flex-direction: column;
  padding-right: 10px;
  .item-top {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
    .img-box {
      display: flex;
      align-items: center;
      cursor: pointer;
      img {
        width: 15px;
        height: 7px;
        margin-right: 8px;
      }
      .tit {
        font-size: 13px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #909399;
      }
      .per {
        font-size: 13px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #19be6b;
      }
      .fper {
        font-size: 13px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ff9900;
      }
    }
    .tit {
      font-size: 13px;
      font-weight: 400;
      color: #909399;
    }
  }
  .num {
    font-size: 22px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #303133;
  }
}
.statistics-title {
  font-weight: 600;
  color: #333333;
  padding-left: 10px;
  margin-bottom: 28px;
  border-left: 2px solid #1890ff;
}
::v-deep .el-progress-bar {
  width: 90% !important;
}
.statistics-department {
  display: flex;
  .item {
    width: 50%;
    display: flex;
    .item-top {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      img {
        width: 24px;
        height: 24px;
        margin-right: 6px;
      }
      .tit {
        font-size: 16px;
        margin-left: 6px;
        margin-top: 2px;
      }
    }
  }
}
.default {
  height: 369px;
  display: flex;
  flex-direction: column;
  align-items: center;
  img {
    width: 200px;
    height: 150px;
  }
}
.from-box ::v-deep .search {
  margin-top: 0;
}
</style>
