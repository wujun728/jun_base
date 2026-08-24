<template>
  <div class="statistics">
    <oaFromBox
      v-if="!searchBtn"
      :isAddBtn="false"
      :isTotal="false"
      :isViewSearch="false"
      :search="isMine ? search : search2"
      :sortSearch="false"
      :total="total"
      @confirmData="confirmData"
    >
    </oaFromBox>
    <div class="statistics-con">
      <div id="statistics-con" :style="styles1" />
    </div>
  </div>
</template>

<script>
import { assessCensusLineApi } from '@/api/enterprise'
import Commnt from '@/components/user/accessCommon'
import echarts from 'echarts'
export default {
  name: 'AssessStatistics',
  components: {
    manageRange: () => import('@/components/form-common/select-manageRange'),
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  props: {
    searchBtn: {
      type: Boolean,
      default: false
    },
    planId: {
      type: String,
      default: ''
    },
    isMine: {
      type: Boolean,
      default: false
    },
    type: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      colorList: [
        '#679AFA',
        '#6FE9EA',
        '#657899',
        '#FBB221',
        '#F67878',
        '#00BFFF',
        '#00FFFF',
        '#FFD700',
        '#FAF0E6',
        '#F0F8FF'
      ],
      periodOptions: Commnt.periodOptions,
      frameArray: [{ label: this.$t('setting.selectdepartment'), value: '' }],
      userArray: [{ label: this.$t('access.selectpersonnel'), value: '' }],
      tableData: [],
      frame: [],
      period: 2,
      time: [this.$moment().subtract(1, 'years').format('YYYY-MM-DD'), this.$moment(new Date()).format('YYYY-MM-DD')],
      frame_id: 0,
      scope_frame: 'all',
      test_uid: '',
      start: this.$moment().subtract(1, 'years').format('YYYY-MM-DD'),
      end: this.$moment(new Date()).format('YYYY-MM-DD'),
      total: 0,
      height: 420,
      styles1: {
        height: '100%',
        width: '100%'
      },
      optionData: {},
      pickerOptions: this.$pickerOptionsTimeEle,
      search: [
        {
          field_name: '考核周期',
          field_name_en: 'period',
          form_value: 'select',
          data_dict: Commnt.periodOption,
          value: 2
        },
        {
          field_name: '开始时间',
          field_name_end: '结束时间',
          field_name_en: 'time',
          form_value: 'date_picker',
          data_dict: [this.$moment().subtract(1, 'year').format('YYYY/MM/DD'), this.$moment().format('YYYY/MM/DD')]
        }
      ],
      search2: [
        {
          field_name: '管理范围',
          field_name_en: 'manage',
          form_value: 'manage',
          data_dict: []
        }
      ]
    }
  },
  watch: {
    lang() {
      this.setOptions()
    }
  },
  methods: {
    confirmData(data) {
      if (data == 'reset') {
        this.scope_frame = 'all'
        this.period = 2
        this.start = this.$moment().subtract(1, 'years').format('YYYY-MM-DD')
        this.end = this.$moment(new Date()).format('YYYY-MM-DD')
        this.time = [
          this.$moment().subtract(1, 'years').format('YYYY-MM-DD'),
          this.$moment(new Date()).format('YYYY-MM-DD')
        ]
        this.frame_id = 0
        this.test_uid = ''
      } else {
        for (let i in data) {
          this[i] = data[i]
        }
      }
      this.getTableData()
    },
    setOptions() {
      this.frameArray[0].label = this.$t('setting.selectdepartment')
      this.userArray[0].label = this.$t('access.selectpersonnel')
    },
    reset() {
      this.period = 2
      this.start = this.$moment().subtract(1, 'years').format('YYYY-MM-DD')
      this.end = this.$moment(new Date()).format('YYYY-MM-DD')
      this.time = [
        this.$moment().subtract(1, 'years').format('YYYY-MM-DD'),
        this.$moment(new Date()).format('YYYY-MM-DD')
      ]
      this.frame_id = 0
      this.scope_frame = 'all'
      this.$refs.manageRange.reset()
      this.planId = ''

      this.getTableData()
    },
    getTableData() {
      const data = {
        period: this.period,
        start: this.start,
        end: this.end,
        time: this.time,
        frame_id: this.frame_id ? this.frame_id : '',
        test_uid: this.test_uid,
        number: this.planId,
        types: this.type,
        scope_frame: this.scope_frame
      }
      assessCensusLineApi(data).then((res) => {
        this.tableData = res.data
        this.init()
      })
    },
   
   
  
    init() {
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
          type: 'scroll',
           
          right: 10,
          top: 20
        },
        grid: {
          left: 30,
          top: 80,
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
        series: []
      }
      var seriesData = []
      var legendName = []
      this.tableData.series.forEach((value, index) => {
        legendName.push(value.name)
        seriesData.push({
          name: value.name,
          type: value.type,
          smooth: value.smooth,
          itemStyle: {
            normal: {
              color: this.colorList[index],
              label: {
                show: true,
                color: '#000000',
                position: 'top',
                formatter: '{c}'
              }
            }
          },
          markLine: {
            data: [
              {
                type: 'average',
                name: '平均值'
              }
            ]
          },
          data: value.data
        })
      })
      this.optionData.xAxis[0].data = this.tableData.xAxis
      this.optionData.legend.data = legendName
      this.optionData.series = seriesData

      this.myChart = echarts.init(document.getElementById('statistics-con'))
      let option = null
      option = this.optionData
      // 基于准备好的dom，初始化echarts实例
      this.myChart.setOption(option, true)
    },
    handleFrame(num) {
      this.frame_id = num
      this.test_uid = ''
      this.userArray = [{ label: this.$t('access.selectpersonnel'), value: '' }]
      if (num != '') {
        const index = this.getFindIndex(this.frame, 'id', num)
        this.frame[index].user.forEach((value) => {
          this.userArray.push({ label: value.card.name, value: value.id })
        })
      }
      this.getTableData()
    },
    handleTest(num) {
      this.test_uid = num
      this.getTableData()
    },
    getFindIndex(arr, name, number) {
      const index = arr.findIndex(function (item) {
        return item[name] == number
      })
      return index
    }
  }
}
</script>

<style lang="scss" scoped>
.el-month-table td.end-date .cell {
  color: #ffffff !important;
}
body .el-month-table td.start-date .cell,
.el-month-table td.end-date .cell {
  background-color: transparent;
}
.statistics {
  height: calc(100vh - 90px - 70px - 80px);
}
.el-form-item {
  margin-right: 10px;
}

.statistics-con {
  height: calc(100% - 32px);
  .v-height-box {
    overflow: hidden !important;
  }
  .statistics-con-ul {
    p {
      margin: 0;
      padding: 0;
    }
    ul,
    li {
      padding: 0;
      margin: 0;
      list-style: none;
    }
    display: flex;
    align-items: flex-end;
    align-content: center;
    justify-content: center;
    li {
      text-align: center;
      font-size: 13px;
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 70px;
      margin-right: 20px;
      p {
        margin-bottom: 15px;
      }
      p:last-of-type {
        margin-bottom: 0;
      }
      .icon-bar {
        width: 100%;
        height: 85%;
        background-color: #00b7ee;
      }
      .people {
        color: rgba(0, 0, 0, 0.3);
      }
      .level {
        color: rgba(0, 0, 0, 0.85);
      }
    }
    li:last-of-type {
      margin-right: 0;
    }
  }
}
::v-deep .from-s .flex-row .el-form-item {
  margin-right: 10px;
  margin-left: 0;
}
</style>
