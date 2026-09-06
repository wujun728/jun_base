<template>
  <div class="dashboard">
    <!-- 顶部统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :xs="12" :sm="6" v-for="(item, index) in statsData" :key="index">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: item.color }">
              <el-icon :size="24">
                <component :is="item.icon" />
              </el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-label">{{ item.label }}</div>
              <div class="stat-change" :class="item.trend">
                <el-icon :size="12">
                  <ArrowUp v-if="item.trend === 'up'" />
                  <ArrowDown v-if="item.trend === 'down'" />
                </el-icon>
                {{ item.change }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <!-- 销售趋势折线图 -->
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-title">销售趋势</span>
              <el-select v-model="salesPeriod" size="small" style="width: 100px">
                <el-option label="本周" value="week" />
                <el-option label="本月" value="month" />
                <el-option label="本年" value="year" />
              </el-select>
            </div>
          </template>
          <div ref="salesChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 用户分布饼状图 -->
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <span class="card-title">用户分布</span>
          </template>
          <div ref="userChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <!-- 订单统计柱状图 -->
      <el-col :xs="24" :lg="16">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <span class="card-title">订单统计</span>
          </template>
          <div ref="orderChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 实时数据 -->
      <el-col :xs="24" :lg="8">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <span class="card-title">实时数据</span>
          </template>
          <div class="realtime-data">
            <div class="realtime-item" v-for="(item, index) in realtimeData" :key="index">
              <div class="realtime-label">{{ item.label }}</div>
              <div class="realtime-value">{{ item.value }}</div>
              <el-progress 
                :percentage="item.percentage" 
                :color="item.color"
                :show-text="false"
                :stroke-width="6"
              />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-row :gutter="20" class="table-row">
      <el-col :span="24">
        <el-card class="table-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-title">最新订单</span>
              <el-button type="primary" size="small">查看全部</el-button>
            </div>
          </template>
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="id" label="订单号" width="120" />
            <el-table-column prop="customer" label="客户" width="120" />
            <el-table-column prop="product" label="产品" />
            <el-table-column prop="amount" label="金额" width="100">
              <template #default="scope">
                <span class="amount">¥{{ scope.row.amount }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="date" label="日期" width="120" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import {
  User,
  ShoppingCart,
  TrendCharts,
  Money,
  ArrowUp,
  ArrowDown
} from '@element-plus/icons-vue'

const chartTextColor = '#bdd3f2'
const chartLineColor = 'rgba(72, 145, 255, 0.18)'

// 响应式数据
const salesPeriod = ref('month')
const salesChart = ref(null)
const userChart = ref(null)
const orderChart = ref(null)

// 统计卡片数据
const statsData = ref([
  {
    label: '总用户数',
    value: '12,345',
    change: '+12.5%',
    trend: 'up',
    color: '#2F7BFF',
    icon: User
  },
  {
    label: '总订单数',
    value: '8,765',
    change: '+8.2%',
    trend: 'up',
    color: '#24D69A',
    icon: ShoppingCart
  },
  {
    label: '总销售额',
    value: '¥234,567',
    change: '+15.3%',
    trend: 'up',
    color: '#FFB347',
    icon: Money
  },
  {
    label: '转化率',
    value: '3.45%',
    change: '-2.1%',
    trend: 'down',
    color: '#FF6B8B',
    icon: TrendCharts
  }
])

// 实时数据
const realtimeData = ref([
  { label: 'CPU使用率', value: '45%', percentage: 45, color: '#2F7BFF' },
  { label: '内存使用率', value: '67%', percentage: 67, color: '#24D69A' },
  { label: '磁盘使用率', value: '23%', percentage: 23, color: '#FFB347' },
  { label: '网络带宽', value: '89%', percentage: 89, color: '#FF6B8B' }
])

// 表格数据
const tableData = ref([
  { id: 'ORD001', customer: '张三', product: 'iPhone 15', amount: '7999', status: '已完成', date: '2024-01-15' },
  { id: 'ORD002', customer: '李四', product: 'MacBook Pro', amount: '15999', status: '处理中', date: '2024-01-15' },
  { id: 'ORD003', customer: '王五', product: 'iPad Air', amount: '4599', status: '已发货', date: '2024-01-14' },
  { id: 'ORD004', customer: '赵六', product: 'AirPods Pro', amount: '1999', status: '已完成', date: '2024-01-14' },
  { id: 'ORD005', customer: '钱七', product: 'Apple Watch', amount: '2999', status: '待付款', date: '2024-01-13' }
])

// 获取状态类型
const getStatusType = (status) => {
  const statusMap = {
    '已完成': 'success',
    '处理中': 'warning',
    '已发货': 'info',
    '待付款': 'danger'
  }
  return statusMap[status] || 'info'
}

// 初始化销售趋势图
const initSalesChart = () => {
  const chart = echarts.init(salesChart.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['销售额', '订单量'],
      textStyle: {
        color: chartTextColor
      }
    },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
      axisLine: {
        lineStyle: {
          color: chartLineColor
        }
      },
      axisLabel: {
        color: chartTextColor
      }
    },
    yAxis: [
      {
        type: 'value',
        name: '销售额(万元)',
        position: 'left',
        nameTextStyle: {
          color: chartTextColor
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: chartLineColor
          }
        },
        splitLine: {
          lineStyle: {
            color: 'rgba(72, 145, 255, 0.12)'
          }
        },
        axisLabel: {
          color: chartTextColor
        }
      },
      {
        type: 'value',
        name: '订单量',
        position: 'right',
        nameTextStyle: {
          color: chartTextColor
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: chartLineColor
          }
        },
        splitLine: {
          show: false
        },
        axisLabel: {
          color: chartTextColor
        }
      }
    ],
    series: [
      {
        name: '销售额',
        type: 'line',
        smooth: true,
        data: [120, 132, 101, 134, 90, 230, 210, 182, 191, 234, 290, 330],
        itemStyle: {
          color: '#2F7BFF'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(47, 123, 255, 0.35)' },
            { offset: 1, color: 'rgba(47, 123, 255, 0.06)' }
          ])
        }
      },
      {
        name: '订单量',
        type: 'bar',
        yAxisIndex: 1,
        data: [45, 52, 38, 54, 35, 87, 78, 69, 72, 89, 105, 125],
        itemStyle: {
          color: '#24D69A'
        }
      }
    ]
  }
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 初始化用户分布图
const initUserChart = () => {
  const chart = echarts.init(userChart.value)
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      textStyle: {
        color: chartTextColor
      }
    },
    series: [
      {
        name: '用户分布',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['60%', '50%'],
        data: [
          { value: 1048, name: '新用户', itemStyle: { color: '#2F7BFF' } },
          { value: 735, name: '活跃用户', itemStyle: { color: '#24D69A' } },
          { value: 580, name: '沉睡用户', itemStyle: { color: '#FFB347' } },
          { value: 484, name: '流失用户', itemStyle: { color: '#FF6B8B' } }
        ],
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
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 初始化订单统计图
const initOrderChart = () => {
  const chart = echarts.init(orderChart.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['已完成', '处理中', '已取消'],
      textStyle: {
        color: chartTextColor
      }
    },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
      axisLine: {
        lineStyle: {
          color: chartLineColor
        }
      },
      axisLabel: {
        color: chartTextColor
      }
    },
    yAxis: {
      type: 'value',
      axisLine: {
        show: true,
        lineStyle: {
          color: chartLineColor
        }
      },
      splitLine: {
        lineStyle: {
          color: 'rgba(72, 145, 255, 0.12)'
        }
      },
      axisLabel: {
        color: chartTextColor
      }
    },
    series: [
      {
        name: '已完成',
        type: 'bar',
        stack: 'total',
        data: [120, 132, 101, 134, 90, 230, 210],
        itemStyle: { color: '#24D69A' }
      },
      {
        name: '处理中',
        type: 'bar',
        stack: 'total',
        data: [20, 32, 21, 34, 20, 30, 40],
        itemStyle: { color: '#FFB347' }
      },
      {
        name: '已取消',
        type: 'bar',
        stack: 'total',
        data: [10, 12, 8, 14, 5, 15, 12],
        itemStyle: { color: '#FF6B8B' }
      }
    ]
  }
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 组件挂载后初始化图表
onMounted(() => {
  nextTick(() => {
    initSalesChart()
    initUserChart()
    initOrderChart()
  })
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
  background: transparent;
  min-height: 100%;
  box-sizing: border-box;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 16px;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 16px 30px rgba(0, 0, 0, 0.22);
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  color: white;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: var(--screen-text-primary);
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: var(--screen-text-secondary);
  margin-bottom: 4px;
}

.stat-change {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 2px;
}

.stat-change.up {
  color: #67C23A;
}

.stat-change.down {
  color: #F56C6C;
}

.charts-row {
  margin-bottom: 20px;
}

.chart-card {
  border-radius: 8px;
  height: 400px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--screen-text-primary);
}

.chart-container {
  height: 320px;
  width: 100%;
}

.realtime-data {
  padding: 20px 0;
}

.realtime-item {
  margin-bottom: 24px;
}

.realtime-item:last-child {
  margin-bottom: 0;
}

.realtime-label {
  font-size: 14px;
  color: var(--screen-text-secondary);
  margin-bottom: 8px;
}

.realtime-value {
  font-size: 18px;
  font-weight: 600;
  color: var(--screen-text-primary);
  margin-bottom: 8px;
}

.table-row {
  margin-bottom: 20px;
}

.table-card {
  border-radius: 8px;
}

.amount {
  font-weight: 600;
  color: #FFB347;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .dashboard {
    padding: 10px;
  }
  
  .chart-card {
    height: 300px;
  }
  
  .chart-container {
    height: 220px;
  }
  
  .stat-content {
    flex-direction: column;
    text-align: center;
  }
  
  .stat-icon {
    margin-right: 0;
    margin-bottom: 10px;
  }
}
</style>
