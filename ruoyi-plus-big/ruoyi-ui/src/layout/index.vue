<template>
  <div
      :class="[classObj, { 'screen-mode': isHomeRoute }]"
      class="app-wrapper"
      :style="{ '--current-color': theme }"
  >
    <template v-if="isHomeRoute">
      <div class="main-container screen-main-container sidebarHide">
        <div ref="screenViewportRef" class="screen-viewport">
          <div :style="screenCanvasStyle" class="screen-canvas">
            <div class="screen-home">
              <div class="screen-grid-bg"></div>
              <header class="screen-header">
                <div class="screen-header__left">
                  <div class="screen-label">数据驾驶舱</div>
                  <div class="screen-subtitle">{{ screenData.header.subTitle }}</div>
                </div>
                <div class="screen-header__center">
                  <h1>{{ screenData.header.title }}</h1>
                  <p>{{ screenData.header.description }}</p>
                </div>
                <div class="screen-header__right">
                  <button class="screen-manage-btn" type="button" @click="goUserManagement">
                    管理后台
                  </button>
                  <div class="screen-clock">{{ currentTime }}</div>
                  <div class="screen-date">{{ screenData.header.dataTime }}</div>
                </div>
              </header>

              <section class="screen-metrics">
                <article class="metric-card" v-for="item in screenData.metrics" :key="item.key">
                  <div class="metric-card__head">
                    <span>{{ item.label }}</span>
                    <span class="metric-card__tag" :style="{ color: item.accentColor }">{{ item.tag }}</span>
                  </div>
                  <div class="metric-card__body">
                    <div class="metric-card__value">{{ item.value }}</div>
                    <div class="metric-card__unit">{{ item.unit }}</div>
                  </div>
                  <div class="metric-card__foot">
                    <span>{{ item.desc }}</span>
                    <span :class="['metric-card__trend', item.trendDirection]">{{ item.trend }}</span>
                  </div>
                </article>
              </section>

              <section class="screen-content">
                <div class="screen-panel">
                  <div class="screen-panel__header">
                    <span>告警动态</span>
                    <span>{{ screenData.alerts.length }} 条</span>
                  </div>
                  <div class="screen-panel__body screen-list">
                    <div class="screen-list__item" v-for="item in screenData.alerts" :key="item.id">
                      <div>
                        <div class="screen-list__title">{{ item.title }}</div>
                        <div class="screen-list__desc">{{ item.location }}</div>
                      </div>
                      <div class="screen-list__meta">
                        <span :class="['screen-status', item.level]">{{ item.levelText }}</span>
                        <span>{{ item.time }}</span>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="screen-panel screen-panel--wide">
                  <div class="screen-panel__header">
                    <span>近七日业务趋势</span>
                    <span>{{ screenData.header.reportName }}</span>
                  </div>
                  <div ref="trendChartRef" class="screen-chart screen-chart--large"></div>
                  <div class="screen-summary">
                    <div class="screen-summary__item" v-for="item in screenData.summary" :key="item.key">
                      <div class="screen-summary__label">{{ item.label }}</div>
                      <div class="screen-summary__value">{{ item.value }}</div>
                    </div>
                  </div>
                </div>

                <div class="screen-panel">
                  <div class="screen-panel__header">
                    <span>待处理事项</span>
                    <span>{{ screenData.todoList.length }} 项</span>
                  </div>
                  <div class="screen-panel__body screen-list">
                    <div class="screen-list__item" v-for="item in screenData.todoList" :key="item.id">
                      <div>
                        <div class="screen-list__title">{{ item.title }}</div>
                        <div class="screen-list__desc">{{ item.owner }}</div>
                      </div>
                      <div class="screen-list__meta">
                        <span :class="['screen-status', item.status]">{{ item.statusText }}</span>
                        <span>{{ item.deadline }}</span>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="screen-panel screen-panel--rank">
                  <div class="screen-panel__header">
                    <span>部门排名</span>
                    <span>TOP {{ screenData.deptRank.length }}</span>
                  </div>
                  <div class="screen-rank">
                    <div class="screen-rank__item" v-for="(item, index) in screenData.deptRank" :key="item.name">
                      <div class="screen-rank__name">
                        <span class="screen-rank__index">{{ index + 1 }}</span>
                        <span>{{ item.name }}</span>
                      </div>
                      <div class="screen-rank__bar">
                        <span :style="{ width: `${item.rate}%` }"></span>
                      </div>
                      <div class="screen-rank__value">{{ item.value }}</div>
                    </div>
                  </div>
                </div>

                <div class="screen-panel screen-panel--source">
                  <div class="screen-panel__header">
                    <span>来源占比</span>
                    <span>实时更新</span>
                  </div>
                  <div ref="sourceChartRef" class="screen-chart"></div>
                </div>

              </section>
            </div>
          </div>
        </div>
        <settings ref="settingRef"/>
      </div>
    </template>

    <template v-else>
      <div v-if="device === 'mobile' && sidebar.opened" class="drawer-bg" @click="handleClickOutside"/>
      <sidebar v-if="!sidebar.hide" class="sidebar-container"/>
      <div :class="{ hasTagsView: needTagsView, sidebarHide: sidebar.hide }" class="main-container">
        <div :class="{ 'fixed-header': fixedHeader }">
          <navbar @setLayout="setLayout"/>
          <tags-view v-if="needTagsView"/>
        </div>
        <app-main/>
        <settings ref="settingRef"/>
      </div>
    </template>
  </div>
</template>

<script setup>
import {useResizeObserver, useWindowSize} from '@vueuse/core'
import * as echarts from 'echarts'
import Sidebar from './components/Sidebar/index.vue'
import {AppMain, Navbar, Settings, TagsView} from './components'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'

const appStore = useAppStore()
const settingsStore = useSettingsStore()
const route = useRoute()
const router = useRouter()
const theme = computed(() => settingsStore.theme)
const sideTheme = computed(() => settingsStore.sideTheme)
const sidebar = computed(() => appStore.sidebar)
const device = computed(() => appStore.device)
const needTagsView = computed(() => settingsStore.tagsView)
const fixedHeader = computed(() => settingsStore.fixedHeader)
const isHomeRoute = computed(() => route.path === '/index')

const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened,
  openSidebar: sidebar.value.opened,
  withoutAnimation: sidebar.value.withoutAnimation,
  mobile: device.value === 'mobile'
}))

const {width, height} = useWindowSize()
const WIDTH = 992 // refer to Bootstrap's responsive design
const SCREEN_DESIGN_WIDTH = 1720
const SCREEN_DESIGN_HEIGHT = 1080
const trendChartRef = ref(null)
const sourceChartRef = ref(null)
const currentTime = ref('')
const settingRef = ref(null)
const screenViewportRef = ref(null)
const screenScale = ref(1)

const screenCanvasStyle = computed(() => ({
  width: `${SCREEN_DESIGN_WIDTH}px`,
  height: `${SCREEN_DESIGN_HEIGHT}px`,
  '--screen-design-height': `${SCREEN_DESIGN_HEIGHT}px`,
  transform: `scale(${screenScale.value})`
}))

let trendChartInstance = null
let sourceChartInstance = null
let alarmChartInstance = null
let clockTimer = null

// 首页大屏假数据统一入口，后续接后台接口时可直接替换同结构数据。
function createMockScreenData() {
  return {
    header: {
      title: '三云科技大屏系统',
      subTitle: '现代化数据可视化平台',
      description: '面向大数据场景构建，兼顾实时监测、智能分析与多维展示，满足现代化大屏系统的业务决策需求。',
      dataTime: '数据时间 2026-04-17 13:30:00',
      reportName: '业务监测日报'
    },
    metrics: [
      {
        key: 'onlineDevice',
        label: '在线设备',
        value: '1,286',
        unit: '台',
        desc: '较昨日新增 36 台',
        trend: '+2.9%',
        trendDirection: 'up',
        tag: '实时',
        accentColor: '#34d0ff'
      },
      {
        key: 'todayOrder',
        label: '今日工单',
        value: '324',
        unit: '单',
        desc: '已闭环 278 单',
        trend: '+12.4%',
        trendDirection: 'up',
        tag: '工单',
        accentColor: '#2f7bff'
      },
      {
        key: 'warningCount',
        label: '告警数量',
        value: '18',
        unit: '条',
        desc: '高优先级 5 条',
        trend: '-8.1%',
        trendDirection: 'down',
        tag: '预警',
        accentColor: '#ffb347'
      },
      {
        key: 'serviceRate',
        label: '服务完成率',
        value: '98.6',
        unit: '%',
        desc: '目标值 98%',
        trend: '+0.6%',
        trendDirection: 'up',
        tag: '达标',
        accentColor: '#24d69a'
      }
    ],
    summary: [
      {key: 'orderAmount', label: '今日成交额', value: '568,900'},
      {key: 'newUser', label: '新增用户', value: '126'},
      {key: 'visitPv', label: '访问总量', value: '21,904'},
      {key: 'responseTime', label: '平均响应', value: '236ms'}
    ],
    trend: {
      categories: ['04-11', '04-12', '04-13', '04-14', '04-15', '04-16', '04-17'],
      orderSeries: [220, 246, 231, 278, 290, 301, 324],
      visitorSeries: [1280, 1420, 1360, 1580, 1660, 1750, 1820]
    },
    sourceShare: [
      {value: 38, name: 'PC端'},
      {value: 27, name: '移动端'},
      {value: 21, name: '小程序'},
      {value: 14, name: '第三方接入'}
    ],
    alarmRegions: {
      categories: ['华北', '华东', '华南', '西南', '西北', '华中'],
      values: [6, 9, 4, 3, 2, 5]
    },
    alerts: [
      {
        id: 'a1',
        title: '园区一号楼空调主机离线',
        location: '设备中心 / 1号楼',
        level: 'danger',
        levelText: '严重',
        time: '10:28'
      },
      {
        id: 'a2',
        title: '数据采集延迟超过阈值',
        location: '采集服务 / 数据总线',
        level: 'warning',
        levelText: '预警',
        time: '10:15'
      },
      {
        id: 'a3',
        title: '视频巡检任务未按时完成',
        location: '安防中心 / 巡检组',
        level: 'info',
        levelText: '提示',
        time: '09:42'
      }
    ],
    todoList: [
      {
        id: 't1',
        title: '处理昨日高优先级告警复盘',
        owner: '负责人: 运维主管',
        status: 'warning',
        statusText: '待处理',
        deadline: '今日 15:00'
      },
      {
        id: 't2',
        title: '同步设备在线率周报',
        owner: '负责人: 数据分析',
        status: 'primary',
        statusText: '进行中',
        deadline: '今日 18:00'
      },
      {
        id: 't3',
        title: '核对本周工单闭环明细',
        owner: '负责人: 客服经理',
        status: 'success',
        statusText: '已安排',
        deadline: '明日 10:00'
      }
    ],
    deptRank: [
      {name: '研发中心', value: '92.4', rate: 92},
      {name: '客户运营', value: '88.6', rate: 89},
      {name: '交付实施', value: '84.1', rate: 84},
      {name: '售后服务', value: '80.7', rate: 81},
      {name: '市场拓展', value: '76.2', rate: 76}
    ]
  }
}

const screenData = ref(createMockScreenData())

// 后续接接口时，可在请求成功后直接调用这个方法替换首页展示数据。
function setHomeScreenData(payload = {}) {
  screenData.value = {
    ...screenData.value,
    ...payload,
    header: payload.header ?? screenData.value.header,
    metrics: payload.metrics ?? screenData.value.metrics,
    summary: payload.summary ?? screenData.value.summary,
    trend: payload.trend ?? screenData.value.trend,
    sourceShare: payload.sourceShare ?? screenData.value.sourceShare,
    alarmRegions: payload.alarmRegions ?? screenData.value.alarmRegions,
    alerts: payload.alerts ?? screenData.value.alerts,
    todoList: payload.todoList ?? screenData.value.todoList,
    deptRank: payload.deptRank ?? screenData.value.deptRank
  }
}

watch(() => device.value, () => {
  if (device.value === 'mobile' && sidebar.value.opened) {
    appStore.closeSideBar({withoutAnimation: false})
  }
})

watchEffect(() => {
  if (isHomeRoute.value) {
    appStore.toggleDevice('desktop')
    return
  }

  if (width.value - 1 < WIDTH) {
    appStore.toggleDevice('mobile')
    appStore.closeSideBar({withoutAnimation: true})
  } else {
    appStore.toggleDevice('desktop')
  }
})

watch(isHomeRoute, async value => {
  if (value) {
    startClock()
    await nextTick()
    updateScreenScale()
    renderScreenCharts()
  } else {
    screenScale.value = 1
    stopClock()
    disposeScreenCharts()
  }
}, {immediate: true})

watch([width, height], async () => {
  if (isHomeRoute.value) {
    await nextTick()
    updateScreenScale()
  }
  resizeScreenCharts()
})

useResizeObserver(screenViewportRef, entries => {
  if (!isHomeRoute.value) return
  const viewportRect = entries[0]?.contentRect
  updateScreenScale(viewportRect?.width, viewportRect?.height)
})

function handleClickOutside() {
  appStore.closeSideBar({withoutAnimation: false})
}

function setLayout() {
  settingRef.value.openSetting()
}

function goUserManagement() {
  if (!sidebar.value.opened && !sidebar.value.hide) {
    appStore.toggleSideBar(false)
  }
  router.push('/system/user')
}

function startClock() {
  updateCurrentTime()
  if (clockTimer) return
  clockTimer = window.setInterval(updateCurrentTime, 1000)
}

function stopClock() {
  if (!clockTimer) return
  window.clearInterval(clockTimer)
  clockTimer = null
}

function updateCurrentTime() {
  const now = new Date()
  const date = `${now.getFullYear()}-${`${now.getMonth() + 1}`.padStart(2, '0')}-${`${now.getDate()}`.padStart(2, '0')}`
  const time = `${`${now.getHours()}`.padStart(2, '0')}:${`${now.getMinutes()}`.padStart(2, '0')}:${`${now.getSeconds()}`.padStart(2, '0')}`
  currentTime.value = `${date} ${time}`
}

function updateScreenScale(viewportWidth = screenViewportRef.value?.clientWidth, viewportHeight = screenViewportRef.value?.clientHeight) {
  if (!viewportWidth || !viewportHeight) return
  const nextScale = Math.min(viewportWidth / SCREEN_DESIGN_WIDTH, viewportHeight / SCREEN_DESIGN_HEIGHT)
  screenScale.value = Number.isFinite(nextScale) && nextScale > 0 ? nextScale : 1
}

function renderScreenCharts() {
  if (!isHomeRoute.value) return
  renderTrendChart()
  renderSourceChart()
}

function renderTrendChart() {
  if (!trendChartRef.value) return
  trendChartInstance?.dispose()
  trendChartInstance = echarts.init(trendChartRef.value)
  trendChartInstance.setOption({
    grid: {left: 48, right: 24, top: 36, bottom: 36},
    tooltip: {trigger: 'axis'},
    legend: {
      top: 0,
      right: 0,
      textStyle: {color: '#bdd3f2'},
      data: ['工单量', '访问量']
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: screenData.value.trend.categories,
      axisLine: {lineStyle: {color: 'rgba(72, 145, 255, 0.24)'}},
      axisLabel: {color: '#89a7ce'}
    },
    yAxis: [
      {
        type: 'value',
        name: '工单量',
        axisLine: {show: false},
        splitLine: {lineStyle: {color: 'rgba(72, 145, 255, 0.1)'}},
        axisLabel: {color: '#89a7ce'}
      },
      {
        type: 'value',
        name: '访问量',
        axisLine: {show: false},
        splitLine: {show: false},
        axisLabel: {color: '#89a7ce'}
      }
    ],
    series: [
      {
        name: '工单量',
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        data: screenData.value.trend.orderSeries,
        itemStyle: {color: '#34d0ff'},
        lineStyle: {width: 3, color: '#34d0ff'},
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {offset: 0, color: 'rgba(52, 208, 255, 0.35)'},
            {offset: 1, color: 'rgba(52, 208, 255, 0.04)'}
          ])
        }
      },
      {
        name: '访问量',
        type: 'bar',
        yAxisIndex: 1,
        barWidth: 16,
        data: screenData.value.trend.visitorSeries,
        itemStyle: {
          borderRadius: [6, 6, 0, 0],
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {offset: 0, color: '#2f7bff'},
            {offset: 1, color: '#1f4fd4'}
          ])
        }
      }
    ]
  })
}

function renderSourceChart() {
  if (!sourceChartRef.value) return
  sourceChartInstance?.dispose()
  sourceChartInstance = echarts.init(sourceChartRef.value)
  sourceChartInstance.setOption({
    tooltip: {trigger: 'item'},
    legend: {
      bottom: 0,
      textStyle: {color: '#89a7ce'}
    },
    series: [
      {
        type: 'pie',
        radius: ['48%', '70%'],
        center: ['50%', '46%'],
        label: {color: '#bdd3f2'},
        labelLine: {lineStyle: {color: 'rgba(72, 145, 255, 0.3)'}},
        data: screenData.value.sourceShare.map((item, index) => ({
          ...item,
          itemStyle: {
            color: ['#34d0ff', '#2f7bff', '#24d69a', '#ffb347'][index % 4]
          }
        }))
      }
    ]
  })
}


function resizeScreenCharts() {
  trendChartInstance?.resize()
  sourceChartInstance?.resize()
  alarmChartInstance?.resize()
}

function disposeScreenCharts() {
  trendChartInstance?.dispose()
  sourceChartInstance?.dispose()
  alarmChartInstance?.dispose()
  trendChartInstance = null
  sourceChartInstance = null
  alarmChartInstance = null
}

onBeforeUnmount(() => {
  stopClock()
  disposeScreenCharts()
})
</script>

<style lang="scss" scoped>
@use "@/assets/styles/mixin.scss" as mix;
@use "@/assets/styles/variables.module.scss" as vars;

.app-wrapper {
  @include mix.clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }

  &.screen-mode {
    overflow-x: hidden;
    overflow-y: auto;
    background: radial-gradient(circle at top, rgba(52, 208, 255, 0.14), transparent 24%),
    linear-gradient(180deg, #020b1d 0%, #051127 100%);
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - #{vars.$base-sidebar-width});
  transition: width 0.28s;
}

.hideSidebar .fixed-header {
  width: calc(100% - 54px);
}

.sidebarHide .fixed-header {
  width: 100%;
}

.mobile .fixed-header {
  width: 100%;
}

.screen-main-container {
  height: 100vh;
  background: transparent;
  overflow: hidden;
}

.screen-viewport {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.screen-canvas {
  flex: none;
  transform-origin: center center;
}

.screen-home {
  position: relative;
  display: grid;
  grid-template-rows: auto auto minmax(0, 1fr);
  width: 100%;
  height: 100%;
  min-height: 0;
  box-sizing: border-box;
  padding: 18px 22px 22px;
  color: var(--screen-text-primary);
  overflow: visible;
}

.screen-grid-bg {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(var(--screen-grid) 1px, transparent 1px),
  linear-gradient(90deg, var(--screen-grid) 1px, transparent 1px);
  background-size: 30px 30px;
  pointer-events: none;
  opacity: 0.55;
}

.screen-header,
.screen-metrics,
.screen-content {
  position: relative;
  z-index: 1;
}

.screen-header {
  display: grid;
  grid-template-columns: 260px 1fr 260px;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
}

.screen-label {
  display: inline-flex;
  align-items: center;
  height: 30px;
  padding: 0 14px;
  border-radius: 999px;
  border: 1px solid var(--screen-border-strong);
  background: rgba(52, 208, 255, 0.08);
  color: var(--screen-primary-light);
  font-size: 13px;
  letter-spacing: 1px;
}

.screen-subtitle,
.screen-date {
  margin-top: 8px;
  color: var(--screen-text-secondary);
  font-size: 13px;
}

.screen-header__center {
  text-align: center;
}

.screen-header__center h1 {
  margin: 0;
  font-size: 34px;
  line-height: 1.1;
  letter-spacing: 3px;
}

.screen-header__center p {
  margin: 8px 0 0;
  color: var(--screen-text-secondary);
  font-size: 13px;
}

.screen-header__right {
  width: 280px;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
  text-align: right;
}

.screen-manage-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 34px;
  padding: 0 18px;
  border: 1px solid rgba(52, 208, 255, 0.32);
  border-radius: 999px;
  background: linear-gradient(90deg, rgba(47, 123, 255, 0.22) 0%, rgba(52, 208, 255, 0.2) 100%);
  color: #e8f6ff;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 1px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.screen-manage-btn:hover {
  border-color: rgba(52, 208, 255, 0.56);
  box-shadow: 0 10px 24px rgba(31, 79, 212, 0.2);
  transform: translateY(-1px);
}

.screen-clock {
  font-size: 28px;
  font-weight: 600;
  color: #ffffff;
}

.screen-metrics {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.metric-card,
.screen-panel {
  position: relative;
  background: linear-gradient(180deg, rgba(8, 24, 52, 0.94) 0%, rgba(5, 18, 40, 0.98) 100%);
  border: 1px solid var(--screen-border);
  box-shadow: var(--screen-shadow), inset 0 0 0 1px rgba(52, 208, 255, 0.05);
  border-radius: 16px;
  overflow: hidden;
}

.metric-card::before,
.screen-panel::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 120px;
  height: 2px;
  background: linear-gradient(90deg, #34d0ff 0%, transparent 100%);
}

.metric-card {
  padding: 18px 18px 16px;
}

.metric-card__head,
.metric-card__foot,
.screen-panel__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.metric-card__head {
  color: var(--screen-text-secondary);
  font-size: 14px;
}

.metric-card__tag {
  font-size: 12px;
}

.metric-card__body {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin: 16px 0 12px;
}

.metric-card__value {
  font-size: 34px;
  line-height: 1;
  font-weight: 700;
  color: #ffffff;
}

.metric-card__unit,
.metric-card__foot {
  color: var(--screen-text-secondary);
  font-size: 13px;
}

.metric-card__trend.up {
  color: var(--screen-success);
}

.metric-card__trend.down {
  color: var(--screen-warning);
}

.screen-content {
  display: grid;
  grid-template-columns: 22% 1fr 22%;
  grid-template-rows: minmax(0, 1fr) 272px;
  gap: 16px;
  min-height: 0;
  height: 100%;
}

.screen-panel {
  padding: 16px;
  min-height: 0;
}

.screen-panel--wide {
  grid-column: 2 / 3;
  grid-row: 1 / 3;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.screen-panel__header {
  margin-bottom: 14px;
  color: var(--screen-text-primary);
  font-size: 15px;
}

.screen-panel__header span:last-child {
  color: var(--screen-text-secondary);
  font-size: 12px;
}

.screen-panel__body {
  height: calc(100% - 34px);
  min-height: 0;
}

.screen-chart {
  height: 216px;
}

.screen-chart--large {
  flex: 1;
  min-height: 320px;
}

.screen-summary {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
  margin-top: 12px;
}

.screen-summary__item {
  padding: 10px 14px;
  border-radius: 12px;
  background: rgba(47, 123, 255, 0.08);
  border: 1px solid rgba(72, 145, 255, 0.18);
}

.screen-summary__label {
  color: var(--screen-text-secondary);
  font-size: 12px;
}

.screen-summary__value {
  margin-top: 6px;
  color: #ffffff;
  font-size: 20px;
  font-weight: 600;
}

.screen-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.screen-list__item {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  padding: 14px;
  border-radius: 12px;
  background: rgba(47, 123, 255, 0.06);
  border: 1px solid rgba(72, 145, 255, 0.12);
}

.screen-list__title {
  color: #ffffff;
  font-size: 14px;
  line-height: 1.4;
}

.screen-list__desc {
  margin-top: 6px;
  color: var(--screen-text-secondary);
  font-size: 12px;
}

.screen-list__meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-between;
  color: var(--screen-text-secondary);
  font-size: 12px;
  white-space: nowrap;
}

.screen-status {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 54px;
  height: 24px;
  padding: 0 10px;
  border-radius: 999px;
}

.screen-status.danger {
  color: #ffd9e2;
  background: rgba(255, 107, 139, 0.18);
}

.screen-status.warning {
  color: #ffe5b6;
  background: rgba(255, 179, 71, 0.18);
}

.screen-status.info,
.screen-status.primary {
  color: #d7ecff;
  background: rgba(47, 123, 255, 0.18);
}

.screen-status.success {
  color: #d8fff0;
  background: rgba(36, 214, 154, 0.18);
}

.screen-rank {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-top: 4px;
}

.screen-rank__item {
  display: grid;
  grid-template-columns: 120px 1fr 52px;
  align-items: center;
  gap: 10px;
}

.screen-rank__name {
  display: flex;
  align-items: center;
  gap: 10px;
  color: var(--screen-text-regular);
  font-size: 13px;
}

.screen-rank__index {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  border-radius: 50%;
  background: rgba(52, 208, 255, 0.12);
  color: #ffffff;
  font-size: 12px;
}

.screen-rank__bar {
  height: 10px;
  border-radius: 999px;
  background: rgba(47, 123, 255, 0.08);
  overflow: hidden;
}

.screen-rank__bar span {
  display: block;
  height: 100%;
  border-radius: inherit;
  background: linear-gradient(90deg, #2f7bff 0%, #34d0ff 100%);
}

.screen-rank__value {
  color: #ffffff;
  font-size: 14px;
  text-align: right;
}

.screen-panel--rank,
.screen-panel--source {
  padding: 14px 16px;
}

</style>
