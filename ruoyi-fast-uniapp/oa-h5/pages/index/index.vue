<template>
  <!-- #ifndef H5 -->
  <!-- <page-meta :page-style="'overflow:'+(data.showMsk?'hidden':'visible')"></page-meta> -->
  <!-- #endif -->
  <view class="content">
    <!-- <image class="banner-app" :style="{zIndex: data.scrollTop> 30 ? 999: 0}" src="/static/image/banner-app.png" mode=""></image> -->
    <view class="content-header">
      <!-- #ifndef APP-PLUS -->
      <image class="banner" src="/static/image/banber-h5.png" mode=""></image>
      <!-- #endif -->
      <!-- #ifdef APP-PLUS  -->
      <!-- 测试 -->
      <image class="banner" src="/static/image/banner-02.png" mode=""></image>
      <!-- #endif -->

      <view class="position user-info">
        <uni-row class="caption">
          <uni-col :span="4" class="caption-left">
            <avatar :src="userInfo.avatar" @change="avatarClick"></avatar>
          </uni-col>
          <uni-col :span="20" class="caption-right">
            <view class="caption-right-top display-align">
              <view class="caption-right-info display-align">
                <text class="name line1">{{ userInfo.name }}</text>
                <text v-if="userInfo.frames && userInfo.frames.length > 0" class="frames line1">({{ userInfo.frames[0].frame.name }})</text>
              </view>
              <!-- #ifdef APP-PLUS  -->
              <view class="caption-right-code" @click="getScanCode">
                <text class="iconfont icon-saoma"></text>
              </view>
              <!-- #endif -->
            </view>
            <!-- v-show="data.scrollTop> 20 ? false: true" -->
            <view class="caption-info line1">{{ data.workInfo.enterprise_culture }}</view>
          </uni-col>
        </uni-row>
      </view>
    </view>

    <view class="content-body">
      <image class="banner-logo" src="/static/image/banner-logo.png" mode=""></image>
      <view class="content-body-con">
        <view class="task-content">
          <view class="task-info">
            <uni-row class="task-info-header">
              <uni-col
                :span="8"
                class="task-info-header-list"
                v-for="(item, index) in data.taskTabData"
                :key="'tab' + index"
                :class="data.taskTabIndex === index ? 'active' : ''"
                @click="taskTab(index)"
              >
                <view class="">
                  <view class="number">{{ item.number }}</view>
                  <view class="caption">{{ item.name }}</view>
                </view>
                <image v-if="data.taskTabIndex === index" class="task-bg" :src="`/static/image/index-task0${index + 1}.png`" mode=""></image>
              </uni-col>
            </uni-row>
            <view class="task-info-content">
              <!--<uni-row class="task-info-content-title">
								<uni-col :span="20">{{data.taskTabIitle}}</uni-col>
								<uni-col :span="4" @click.native="taskNavigateTo">
									<view class="iconfont icon-fanhui"></view>
								</uni-col>
							</uni-row> -->
              <!-- 待办 -->
              <template v-if="data.taskTabIndex === 0">
                <image v-if="data.scheduleData.length > 0" class="task-content-bg" src="/static/image/index-task-bg.png" mode=""></image>
                <view class="task-info-list" v-if="data.scheduleData.length > 0">
                  <view
                    class="task-info-list-item"
                    v-for="(item, index) in data.scheduleData.slice(0, 5)"
                    :class="item.finish === 2 || item.finish === 3 ? 'active' : ''"
                    :key="'list' + index"
                    @click="taskItemNavigateTo(item)"
                  >
                    <uni-row class="display-align">
                      <uni-col :span="1">
                        <view class="task-info-circular"></view>
                      </uni-col>
                      <uni-col :span="17" class="line1">{{ item.title }}</uni-col>
                      <uni-col :span="6" class="text-right task-info-time">
                        <span v-if="!item.remind">09:00</span>
                        <uni-dateformat class="right-time" format="hh:mm:ss" :date="item.remind.remind_day + ' ' + item.remind.remind_time">
                        </uni-dateformat>
                      </uni-col>
                    </uni-row>
                  </view>
                </view>
                <image v-if="data.scheduleData.length <= 0" class="task-content-empty" src="/static/image/index-task-empty.png" mode=""></image>
              </template>
              <!-- 审批 -->
              <template v-if="data.taskTabIndex === 1">
                <image class="task-content-bg" v-if="data.examineData.length > 0" src="/static/image/index-task-bg.png" mode=""></image>
                <view class="task-info-list" v-if="data.examineData.length > 0">
                  <view class="task-info-list-item" v-for="(item, index) in data.examineData" :key="'list' + index" @click="taskItemNavigateTo(item)">
                    <uni-row class="display-align">
                      <uni-col :span="1">
                        <view class="task-info-circular"></view>
                      </uni-col>
                      <uni-col :span="17" class="line1"> {{ item.card.name }}的{{ item.approve ? item.approve.name : '--' }}审批 </uni-col>
                      <uni-col :span="6" class="text-right task-info-time">
                        <text>{{ formatDate(item.created_at) }}</text>
                      </uni-col>
                    </uni-row>
                  </view>
                </view>
                <image v-if="data.examineData.length <= 0" class="task-content-empty" src="/static/image/index-task-empty.png" mode=""></image>
              </template>
              <!-- 动态 -->
              <template v-if="data.taskTabIndex === 2">
                <image class="task-content-bg" v-if="data.noticeData.length > 0" src="/static/image/index-task-bg.png" mode=""></image>
                <view class="task-info-list" v-if="data.noticeData.length > 0">
                  <view
                    class="task-info-list-item"
                    v-for="(item, index) in data.noticeData.slice(0, 5)"
                    :key="'list' + index"
                    :class="item.is_read === 1 ? 'active' : ''"
                    @click="taskItemNavigateTo(item)"
                  >
                    <uni-row class="display-align">
                      <uni-col :span="1">
                        <view class="task-info-circular"></view>
                      </uni-col>
                      <uni-col :span="17" class="line1">{{ item.title }}</uni-col>
                      <uni-col :span="6" class="text-right task-info-time">
                        <text>{{ formatDate(item.push_time) }}</text>
                      </uni-col>
                    </uni-row>
                  </view>
                </view>
                <image v-if="data.noticeData.length <= 0" class="task-content-empty" src="/static/image/index-task-empty.png" mode=""></image>
              </template>
            </view>
          </view>
        </view>
        <view class="nav-content plr12 m10">
          <uni-row class="nav-title display-align">
            <uni-col :span="12">
              <view class="display-align"> <image class="nav-logo" src="/static/image/index-task05.png" mode=""></image>快捷入口 </view>
            </uni-col>
            <uni-col :span="12" class="text-right">
              <navigator hover-class="none" class="nav-title-right quick-nav" url="/pages/index/manage" animation-type="slide-in-right">
                <text class="iconfont icon-pcshouye-guanli"></text>
                管理
              </navigator>
            </uni-col>
          </uni-row>
          <view class="nav-content-list">
            <view class="nav-list-item" v-for="(items, indexs) in data.quickVal" @click="navItemClicks(items)">
              <view class="nav-list-item-box">
                <image v-if="items.image" class="image" :src="items.image" mode=""></image>
                <image v-else class="image" src="../../static/image/memorandum.png" mode=""></image>
              </view>
              <view class="nav-list-item-title">{{ items.name }}</view>
            </view>
          </view>
        </view>

        <!-- 有下级统计 -->
        <view class="nav-content plr12 m10" v-if="data.reportShow">
          <uni-row class="nav-title display-align">
            <uni-col :span="16">
              <view class="display-align">
                <image class="nav-logo" src="/static/image/index-task06.png" mode=""></image>工作汇报
                <text class="nav-caption">(今日统计)</text>
              </view>
            </uni-col>
            <uni-col :span="8" class="text-right">
              <navigator hover-class="none" class="nav-title-right" url="/pages/users/report/census" animation-type="slide-in-right">
                <view class="iconfont icon-fanhui"></view>
              </navigator>
            </uni-col>
          </uni-row>
          <view class="nav-content-box">
            <uni-row class="task-info-header">
              <uni-col
                :span="8"
                class="task-info-header-list"
                @click="clickReport(item)"
                v-for="(item, index) in data.reportData"
                :key="'tab' + index"
              >
                <view class="">
                  <view class="number">{{ item.number }}</view>
                  <view class="caption">{{ item.name }}</view>
                </view>
              </uni-col>
            </uni-row>
          </view>
        </view>
        <view class="nav-content plr12 m10" v-if="data.frameShow">
          <uni-row class="nav-title display-align">
            <uni-col :span="16">
              <view class="display-align">
                <image class="nav-logo" src="/static/image/index-task09.png" mode=""></image>部门业绩
                <!-- <text class="nav-caption">(本月累计)</text> -->
              </view>
            </uni-col>
            <uni-col :span="8" class="text-right">
              <navigator hover-class="none" class="nav-title-right" url="/pages/customer/list/statistics" animation-type="slide-in-right">
                <view class="iconfont icon-fanhui"></view>
              </navigator>
            </uni-col>
          </uni-row>
          <view class="nav-content-box">
            <uni-row class="task-info-header">
              <uni-col :span="8" class="task-info-header-list" v-for="(item, index) in data.frameData" :key="'tab' + index">
                <view class="">
                  <view class="number">{{ item.number || 0 }}</view>
                  <view class="caption">{{ item.name }}</view>
                </view>
              </uni-col>
            </uni-row>
          </view>
        </view>
        <view class="nav-content plr12 m10" v-if="data.oneselfShow">
          <uni-row class="nav-title display-align">
            <uni-col :span="16">
              <view class="display-align">
                <image class="nav-logo" src="/static/image/index-task07.png" mode=""></image>本人业绩
                <!-- <text class="nav-caption">(本月累计)</text> -->
              </view>
            </uni-col>
            <uni-col :span="8" class="text-right">
              <navigator hover-class="none" class="nav-title-right" url="/pages/customer/list/statistics" animation-type="slide-in-right">
                <view class="iconfont icon-fanhui"></view>
              </navigator>
            </uni-col>
          </uni-row>
          <view class="nav-content-box">
            <uni-row class="task-info-header">
              <uni-col :span="8" class="task-info-header-list" v-for="(item, index) in data.turnoverData" :key="'tab' + index">
                <view class="">
                  <view class="number">{{ item.number || 0 }}</view>
                  <view class="caption">{{ item.name }}</view>
                </view>
              </uni-col>
            </uni-row>
          </view>
        </view>
        <view class="nav-content plr12 m10">
          <uni-row class="nav-title display-align">
            <uni-col :span="12">
              <view class="display-align"> <image class="nav-logo" src="/static/image/index-task10.png" mode=""></image>团队考勤 </view>
            </uni-col>
            <uni-col :span="12" class="text-right">
              <navigator hover-class="none" class="nav-title-right quick-nav" url="/pages/attendance/statistics" animation-type="slide-in-right">
                <text class="iconfont icon-fanhui"></text>
              </navigator>
            </uni-col>
          </uni-row>
          <view class="nav-content-box">
            <uni-row class="task-info-header">
              <uni-col :span="8" class="task-info-header-list" v-for="(item, index) in data.attendanceTeamData" :key="'tab' + index">
                <view class="">
                  <view class="number">{{ item.number }}</view>
                  <view class="caption">{{ item.name }}</view>
                </view>
              </uni-col>
            </uni-row>
          </view>
        </view>
        <view class="nav-content plr12 m10">
          <uni-row class="nav-title display-align">
            <uni-col :span="12">
              <view class="display-align"> <image class="nav-logo" src="/static/image/index-task10.png" mode=""></image>我的考勤 </view>
            </uni-col>
            <uni-col :span="12" class="text-right">
              <navigator
                hover-class="none"
                class="nav-title-right quick-nav"
                url="/pages/attendance/statistics?sel=1"
                animation-type="slide-in-right"
              >
                <text class="iconfont icon-fanhui"></text>
              </navigator>
            </uni-col>
          </uni-row>
          <view class="nav-content-box">
            <uni-row class="task-info-header">
              <uni-col :span="8" class="task-info-header-list" v-for="(item, index) in data.attendanceMeData" :key="'tab' + index">
                <view class="">
                  <view class="number">{{ item.number }}</view>
                  <view class="caption">{{ item.name }}</view>
                </view>
              </uni-col>
            </uni-row>
          </view>
        </view>

        <view class="nav-content plr12 m10">
          <uni-row class="nav-title display-align">
            <uni-col :span="12">
              <view class="display-align"> <image class="nav-logo" src="/static/image/index-task04.png" mode=""></image>绩效考核 </view>
            </uni-col>
            <uni-col :span="12" class="text-right">
              <navigator hover-class="none" class="nav-title-right" url="/pages/users/assessment/index" animation-type="slide-in-right">
                <view class="iconfont icon-fanhui"></view>
              </navigator>
            </uni-col>
          </uni-row>
          <view class="nav-content-model">
            <uni-row v-if="data.assessNow.length > 0">
              <uni-col
                :span="12"
                v-for="(item, index) in data.assessNow.slice(0, 2)"
                :key="'list' + item.id"
                :class="index === 1 ? 'pull-right' : ''"
                @click="clickAssessItem(item)"
              >
                <view class="">
                  <view class="model-title line1">{{ item.name }}</view>
                  <view v-if="item.status < 3" class="model-caption">{{ getStatusTips(item.status) }} </view>
                  <view v-else class="model-caption active"
                    >绩效得分：<text>{{ item.score }}</text> 分</view
                  >
                </view>
              </uni-col>
            </uni-row>
            <view class="nav-content-empty" v-if="data.assessNow.length <= 0">
              <image class="empty-img" src="/static/image/index-task08.png" mode=""></image>
              <view class="nav-content-empty-con">
                <view class="name">暂无绩效考核</view>
                <view class="info">烈火试真金，逆境试强者</view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
    <copyright />
    <avatar-sideslip ref="avatarSideslipRef" @change="changeAvatar"> </avatar-sideslip>
    <global-index />
  </view>
</template>

<script setup lang="ts">
import globalIndex from '@/components/globalIndex/index.vue'
import avatar from '@/components/avatar/index.vue'
import avatarSideslip from '@/pages/user/components/avatarSideslip.vue'
import copyright from '@/components/copyright/index.vue'
// 获取vuex存取变量
import { toLogin } from '@/libs/login'
import message from '@/utils/message'
import { getStatusTips } from '@/utils/assessment'
import moment from 'moment'
import { workIndexApi, entDailyStatisticsApi, userWorkMenusApi } from '@/api/user'
import { clickNavigateTo } from '@/utils/helper'
import type { Res, Detail, PropType } from '@/utils/typeHelper'
import { formatDate } from '@/utils/schedule'
import { userScanUserApi } from '@/api/user'
import { useStore } from 'vuex'
const store = useStore()
const isLogin = computed(() => store.state.app.isLogin)
const userInfo = computed(() => store.state.app.userInfo)

onShow(() => {
  if (!isLogin.value) {
    toLogin()
  } else {
    data.showMsk = false
    getWorkIndex()
    if (JSON.parse(uni.getStorageSync('workMenusData'))) {
      getWorkMenus()
    } else {
      refreshWorkMenusData()
    }
    getStatistics()
  }
})

// 使用定时器每隔五分钟请求一次菜单数据
function refreshWorkMenusData() {
  if (isLogin.value) {
    userWorkMenusApi()
      .then((res: Res) => {
        if (res.data.checkd) {
          uni.setStorageSync('workMenusData', JSON.stringify(res.data))
          getWorkMenus()
        }
      })
      .catch((error: Res) => {
        message.error(error.message)
      })
  }
}

// 在页面初始化时调用定时刷新函数
refreshWorkMenusData()
setInterval(refreshWorkMenusData, 5 * 60 * 1000) // 刷新间隔为五分钟

onPageScroll((e) => {
  data.scrollTop = e.scrollTop
})

//下拉加载
onPullDownRefresh(() => {
  const currentTime = Date.now()
  if (currentTime - data.lastDataFetchTimestamp >= 5 * 60 * 1000) {
    if (uni.getStorageSync('workMenusData')) {
      getWorkMenus()
    } else {
      refreshWorkMenusData()
    }
    getWorkIndex()
    getStatistics()
  } else {
    uni.stopPullDownRefresh() // 停止刷新
  }
})

const avatarSideslipRef = ref()
// 点击头像
const avatarClick = () => {
  clickNavigateTo('/pages/users/center/index')
  // avatarSideslipRef.value.popupOpen()
}

// 切换企业成功
const changeAvatar = (e: boolean): void => {
  data.showMsk = e
}

const data = reactive({
  taskTabData: [
    {
      name: '待办任务',
      number: 0,
    },
    {
      name: '待审批',
      number: 0,
    },
    {
      name: '企业动态',
      number: 0,
    },
  ],
  taskTabIndex: 0,
  taskTabIitle: '待办任务',
  scheduleData: [],
  examineData: [],
  noticeData: [],
  assessNow: [],
  reportData: [
    {
      name: '提交结果',
      number: 0,
      url: '/pages/users/report/census?tab=0',
    },
    {
      name: '已提交',
      number: 0,
      url: '/pages/users/report/census?tab=0',
    },
    {
      name: '未提交',
      number: 0,
      url: '/pages/users/report/census?tab=1',
    },
  ],
  repData: [
    {
      name: '应提交',
      number: 0,
    },
    {
      name: '已提交',
      number: 0,
    },
    {
      name: '未提交',
      number: 0,
    },
  ],
  turnoverData: [
    {
      name: '本月业绩',
      number: 0,
    },
    {
      name: '今日业绩',
      number: 0,
    },
    {
      name: '昨日业绩',
      number: 0,
    },
  ],
  frameData: [
    {
      name: '本月业绩',
      number: 0,
    },
    {
      name: '今日业绩',
      number: 0,
    },
    {
      name: '昨天业绩',
      number: 0,
    },
  ],
  showMsk: false,
  where: {
    types: 0,
    time: `${moment(new Date()).format('YYYY/MM/DD')}-${moment(new Date()).format('YYYY/MM/DD')}`,
    frame_id: '',
    page: 1,
    limit: 10,
  },
  reportShow: true,
  frameShow: false,
  oneselfShow: false,
  scrollTop: 0,
  workInfo: <PropType>{},
  quickData: <any>[],
  quickVal: <any>[],
  lastDataFetchTimestamp: 0, //请求时间戳
  attendanceMeData: [
    {
      name: '迟到(次)',
      number: 0,
    },
    {
      name: '早退(次)',
      number: 0,
    },
    {
      name: '缺卡(次)',
      number: 0,
    },
  ],
  attendanceTeamData: [
    {
      name: '迟到(人)',
      number: 0,
    },
    {
      name: '早退(人)',
      number: 0,
    },
    {
      name: '缺卡(人)',
      number: 0,
    },
  ],
})
//{

const taskTab = (index: number) => {
  data.taskTabIndex = index
  data.taskTabIitle = data.taskTabData[index].name
}

const clickReport = (row: PropType) => {
  if (!row.url) return
  clickNavigateTo(row.url)
}

const taskItemNavigateTo = (item: Detail): void => {
  let url = ''
  if (data.taskTabIndex === 0) {
    // clickNavigateTo(`)
    url = `/pages/users/schedule/detail?id=${item.id}&start=${item.start_time}&end=${item.end_time}`
  } else if (data.taskTabIndex === 1) {
    url = `/pages/users/examine/defaults?id=${item.id}`
  } else {
    url = `/pages/users/noticeDefault/index?id=${item.id}`
  }
  clickNavigateTo(url)
}

// 获取首页应用
const getWorkMenus = () => {
  const menuData = JSON.parse(uni.getStorageSync('workMenusData'))
  data.quickVal = []
  if (menuData) {
    data.quickData = menuData.cates
    data.quickVal = menuData.checkd
  }
  // 更新上次请求时间戳
  data.lastDataFetchTimestamp = Date.now()
  uni.stopPullDownRefresh() // 停止刷新
}

//数量统计页面
const getStatistics = () => {
  entDailyStatisticsApi(data.where)
    .then((res: Res) => {
      // 切换时数据清空
      if (res.data.total === 0 && res.data.submit === 0 && res.data.no_submit === 0) {
        data.reportShow = false
      } else {
        data.reportShow = true
      }
      data.reportData[0].number = res.data.total
      data.reportData[1].number = res.data.submit
      data.reportData[2].number = res.data.no_submit
      uni.stopPullDownRefresh() //停止刷新
    })
    .catch((error: Res) => {
      message.error(error.message)
    })
}

// 获取信息
const getWorkIndex = () => {
  workIndexApi()
    .then((res: Res) => {
      data.workInfo = res.data
      let len1 = 0
      data.scheduleData = []
      if (res.data.schedule && res.data.schedule.length > 0) {
        const schedule = res.data.schedule
        schedule.forEach((value: any) => {
          if (value.finish !== 2 && value.finish !== 3) {
            len1++
          }
        })
        data.scheduleData = schedule
        // 升序排序
        if (data.scheduleData.length > 1) {
          data.scheduleData.sort((a, b) => {
            return a.finish - b.finish
          })
        }

        data.taskTabData[0].number = len1
      }
      if (res.data.approve) {
        data.examineData = res.data.approve.list
        data.taskTabData[1].number = res.data.approve.count
      }

      if (res.data.daily) {
        data.repData[0].number = res.data.daily.count
        data.repData[1].number = res.data.daily.finish
        data.repData[2].number = res.data.daily.surplus
      }

      if (res.data.client) {
        data.oneselfShow = !(Number(res.data.client.income) <= 0 && res.data.client.new_customer <= 0 && res.data.client.new_contract <= 0)
        data.turnoverData[0].number = res.data.client.income
        data.turnoverData[1].number = res.data.client.today_income
        data.turnoverData[2].number = res.data.client.yesterday_income
        data.attendanceTeamData[0].number = res.data.team_statistics.late
        data.attendanceTeamData[1].number = res.data.team_statistics.leave_early
        data.attendanceTeamData[2].number = res.data.team_statistics.lack_card
        data.attendanceMeData[0].number = res.data.person_statistics.late
        data.attendanceMeData[1].number = res.data.person_statistics.leave_early
        data.attendanceMeData[2].number = res.data.person_statistics.lack_card
      }

      if (res.data.frame) {
        data.frameShow = res.data.frame.auth
        data.frameData[0].number = res.data.frame.income
        data.frameData[1].number = res.data.frame.new_customer
        data.frameData[2].number = res.data.frame.new_contract
      }

      if (res.data.assess) {
        data.assessNow = res.data.assess
      }

      if (res.data.notice) {
        data.noticeData = res.data.notice.list
        data.taskTabData[2].number = res.data.notice.count
      }
      uni.stopPullDownRefresh() //停止刷新
    })
    .catch((error: Res) => {
      message.error(error.message)
    })
}

//当前考核详情查看
const clickAssessItem = (item: Detail): void => {
  clickNavigateTo(`/pages/users/assessment/default?id=${item.id}`)
}

const navItemClicks = (item: Detail): void => {
  if (!item.uni_url) return
  clickNavigateTo(item.uni_url)
}

// 过去扫码登录
const getScanCode = () => {
  uni.scanCode({
    onlyFromCamera: true,
    success(res) {
      const key = res.result
      store.commit('setCodeSuccessKey', key)
      getUserScan({
        key: key,
      })
    },
  })
}
// 扫码页面跳转
const getUserScan = (data: object) => {
  userScanUserApi(data)
    .then(() => {
      clickNavigateTo(`/pages/users/scanCode/index?type=1`)
    })
    .catch(() => {
      clickNavigateTo(`/pages/users/scanCode/index?type=0`)
    })
}
</script>

<style scoped lang="scss">
.content {
  width: 100%;

  .banner-app {
    width: 100%;
    height: 220rpx;
    position: fixed;
    left: 0;
    top: 0;
  }

  .task-info-header {
    .task-info-header-list {
      height: 118rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-wrap: wrap;
      position: relative;

      &.active {
        background-color: #f2f6fc;
        border-radius: 12rpx;

        .number {
          color: $uni-color-primary;
        }

        .caption {
          color: $uni-color-primary;
        }
      }

      .number {
        text-align: center;
        width: 100%;
        font-size: 40rpx;
        font-weight: 600;
        color: $uni-text-color;
        line-height: 40rpx;
      }

      .caption {
        padding-top: 16rpx;
        text-align: center;
        font-size: 26rpx;
        font-weight: 400;
        width: 100%;
        color: $nui-text-color-four;
        line-height: 26rpx;
      }

      .task-bg {
        position: absolute;
        right: 0;
        bottom: 0;
        width: 94rpx;
        height: 88rpx;
      }
    }
  }

  .content-header {
    position: fixed;
    left: 0;
    top: 0;
    z-index: 100;
    width: 100%;

    .banner {
      width: 100%;
      // #ifndef APP-PLUS
      height: 212rpx;
      // #endif
      /* #ifdef APP-PLUS */
      height: 250rpx;
      /* #endif */
    }

    .position {
      position: absolute;
      left: 0;
      width: 100%;
    }

    .user-info {
      // #ifndef APP-PLUS
      top: 60rpx;
      // #endif
      // #ifdef APP-PLUS
      top: calc($uni-default-bar-height + 36rpx);
      // #endif
      padding: 0 20rpx;

      .caption {
        font-size: 26rpx;
        font-weight: 400;
        color: #ffffff;

        .caption-left {
          width: 90rpx;
          height: 90rpx;
          border-radius: 10rpx;
          border: 1px solid #ebeef5;

          .avatar {
            width: 100%;
            height: 100%;
            border-radius: 10rpx;
          }
        }

        .caption-right {
          width: calc(100% - 90rpx);
          padding-left: 24rpx !important;
          height: 90rpx;
          display: flex;
          justify-content: space-between;
          flex-direction: column;

          .caption-right-top {
            line-height: 1.2;

            .caption-right-info {
              width: calc(100% - 80rpx);
            }

            .caption-right-code {
              width: 80rpx;
              text-align: right;
              height: 100%;

              .iconfont {
                font-weight: normal;
                font-size: 46rpx;
                display: inline-block;
              }
            }

            .name {
              font-size: 40rpx;
              font-weight: $uni-default-font-weight;
              max-width: 50%;
            }

            .frames {
              padding-left: 14rpx;
              max-width: 50%;
            }
          }

          .caption-info {
            // #ifndef APP-PLUS
            width: 100%;
            // #endif
            // #ifdef APP-PLUS
            width: calc(100% - 80rpx);
            // #endif
          }
        }
      }
    }
  }

  .task-content {
    padding: 0 20rpx;

    .task-info {
      background-color: #fff;
      width: 100%;
      height: 448rpx;
      border-radius: 8rpx;
      padding: 24rpx 24rpx 40rpx 24rpx;

      .task-info-content {
        height: calc(100% - 118rpx);
        padding-top: 10rpx;
        position: relative;

        .task-info-content-title {
          padding: 40rpx 0 30rpx 0;
          font-size: 32rpx;
          font-weight: $uni-default-font-weight;
          color: $uni-text-color;
          line-height: 32rpx;

          .iconfont {
            font-size: 26rpx;
            font-weight: normal;
            color: $uni-text-color-five;
            transform: rotate(180deg);
          }
        }

        .task-info-list {
          .task-info-list-item {
            font-size: 28rpx;
            font-weight: 400;
            color: $uni-text-color;
            line-height: 42rpx;
            margin-bottom: 16rpx;

            &:last-of-type {
              margin-bottom: 0;
            }

            &.active {
              color: $uni-text-color-five;

              .task-info-circular {
                background-color: $uni-text-color-five;
              }

              .task-info-time {
                color: $uni-text-color-five;
              }
            }

            .task-info-circular {
              width: 8rpx;
              height: 8rpx;
              border-radius: 100%;
              background-color: $uni-color-primary;
            }

            .task-info-time {
              font-size: 24rpx;
              font-weight: normal;
              color: $nui-text-color-four;
            }
          }
        }

        .task-content-bg {
          position: absolute;
          right: -22rpx;
          bottom: -40rpx;
          width: 476rpx;
          height: 304rpx;
          z-index: 0;
        }

        .task-content-empty {
          width: 476rpx;
          height: 304rpx;
          position: absolute;
          right: -22rpx;
          bottom: -40rpx;
        }
      }
    }
  }

  .content-body {
    position: relative;
    width: 100%;
    // #ifndef APP-PLUS
    padding-top: 212rpx;
    // #endif

    // #ifdef APP-PLUS
    padding-top: 250rpx;
    // #endif

    .banner-logo {
      width: 100%;
      height: 64rpx;
      position: absolute;
      left: 0;
      // #ifndef APP-PLUS
      top: 212rpx;
      // #endif

      // #ifdef APP-PLUS
      top: 250rpx;
      // #endif
      z-index: 1;
    }

    .content-body-con {
      width: 100%;
      position: relative;
      z-index: 2;
    }

    .nav-content {
      padding-top: 30rpx;
      background-color: #fff;
      border-radius: 16rpx;

      .nav-title {
        font-size: 32rpx;
        line-height: 32rpx;
        padding-bottom: 34rpx;
        font-weight: $uni-default-font-weight;
        color: $uni-text-color;

        .nav-logo {
          width: 32rpx;
          height: 32rpx;
          margin-right: 10rpx;
        }

        .nav-caption {
          font-size: 24rpx;
          line-height: 24rpx;
          font-weight: normal;
          margin-left: 10rpx;
          color: $uni-text-color-five;
        }

        .nav-title-right {
          font-size: 24rpx;
          color: $nui-text-color-four;
          font-weight: normal;

          .iconfont {
            display: inline-block;
            font-size: 24rpx;
            padding-left: 10rpx;
            transform: rotate(180deg);
            color: $uni-text-color-five;
          }
        }

        .quick-nav {
          display: flex;
          align-items: center;
          justify-content: flex-end;
          padding-left: 0;
        }
      }

      .nav-content-box {
        padding-top: 6rpx;
        padding-bottom: 50rpx;

        .task-info-header-list {
          height: auto;

          .caption {
            padding-top: 20rpx;
          }
        }
      }

      .nav-content-list {
        border-radius: 16rpx;
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        // width: 720rpx;
        // margin-left: -30rpx;

        .nav-list-item {
          width: 25%;
          text-align: center;
          margin-bottom: 36rpx;

          .nav-list-item-box {
            width: 90rpx;
            height: 90rpx;
            display: inline-block;

            .image {
              width: 100%;
              height: 100%;
            }
          }

          .nav-list-item-title {
            padding-top: 8rpx;
            font-size: 24rpx;
            color: $uni-text-color;
          }
        }
      }

      .nav-content-model {
        padding-bottom: 34rpx;

        ::v-deep .uni-col {
          background-color: #f2f6fc;
          width: calc(50% - 11rpx);
          padding: 0 28rpx !important;
          height: 156rpx;
          border-radius: 16rpx;
          display: flex;
          align-items: center;
        }

        .model-title {
          width: 100%;
          font-size: 24rpx;
          color: $nui-text-color-four;
        }

        .model-caption {
          width: 100%;
          padding-top: 24rpx;
          font-size: 24rpx;
          color: $uni-text-color-five;

          uni-text {
            font-size: 30rpx;
            font-weight: 600;
          }

          &.active {
            color: $nui-text-color-two;
          }
        }
      }

      .nav-content-empty {
        padding: 0 6rpx;
        display: flex;
        align-items: center;
        justify-content: center;

        .empty-img {
          width: 124rpx;
          height: 124rpx;
        }

        .nav-content-empty-con {
          padding-left: 28rpx;

          .name {
            font-size: 26rpx;
            font-weight: 400;
            color: $nui-text-color-four;
            line-height: 26rpx;
          }

          .info {
            padding-top: 16rpx;
            font-size: 24rpx;
            font-weight: 400;
            color: $uni-text-color-five;
          }
        }
      }
    }
  }
}
</style>
