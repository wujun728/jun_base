<template>
  <div class="box divBox">
    <!--顶部-->
    <el-row :gutter="14" type="flex">
      <el-col :span="16">
        <el-card body-style="padding:0">
          <ul class="header-need">
            <li v-for="(item, index) in needInfo" :key="'needInfo' + index" @click="needInfoItem(item)">
              <el-image class="image" :src="item.icon" />
              <div class="header-need-right">
                <p class="num">{{ item.num }}</p>
                <p class="text">{{ item.text }}</p>
              </div>
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card body-style="padding:0">
          <div class="header-pic">
            <el-image class="image" :src="require('@/assets/images/personal-logo.png')" fit="cover" />
            <div class="text">
              <div class="over-text1">
                {{ realName }},
                <span class="time">
                  {{ noon === 'am' ? $t('user.work.goodmorning') : $t('user.work.goodafternoon') }}！
                </span>
              </div>
              <div class="info over-text2">{{ enterprise.culture }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 业绩统计 -->
    <el-row class="mt14" type="flex">
      <el-col :span="24">
        <el-card body-style="padding:20px; height:150px">
          <el-row class="calendar_title achievementPb">
            <el-col :span="12" class="row-middle">
              <div class="dynamics">
                业绩统计
                <el-select
                  v-model="achievementTypes"
                  @change="workStatisticsList"
                  filterable
                  style="width: 110px"
                  placeholder="请选择"
                >
                  <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </div>
            </el-col>
            <el-col :span="12" class="text-right calendar_title-left">
              <span class="display-align" @click="handlePerformance">
                <i class="iconfont iconpcshouye-guanli"></i>
                管理
              </span>
            </el-col>
          </el-row>
          <div class="achievementContent">
            <div class="item" v-for="(item, index) in achievementList" :key="index">
              <span class="num">{{ item.value }}</span>
              <span class="text">{{ item.title }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!--待办日程-->
    <el-row :gutter="14" class="mt14" type="flex">
      <el-col :span="16">
        <el-card body-style="padding:0;">
          <el-row class="calendar_title clearfix calendar-dealt">
            <el-col :span="12" class="acea-row row-middle">
              <div class="dynamics">
                {{ time }} 待办
                <i
                  :style="{ visibility: hasFinished ? 'visible' : 'hidden' }"
                  :class="pend.status ? 'iconyincang' : 'icondakai'"
                  class="iconfont dynamics-icon"
                  :title="`${pend.status ? '隐藏' : '显示'}已完成的待办`"
                  @click="pend.status = pend.status ? 0 : 3"
                ></i>
              </div>
            </el-col>
            <el-col :span="12" class="text-right">
              <el-button-group v-model="timeTab">
                <el-button
                  size="mini"
                  v-for="(item, index) in timeTabData"
                  :class="timeTabIndex === index ? 'active' : ''"
                  :key="index"
                  @click="timeTabInput(item, index)"
                  >{{ item.text }}</el-button
                >
              </el-button-group>
            </el-col>
          </el-row>
          <el-row>
            <div
              class="dealt-content-list"
              v-loading="timeTabLoading"
              v-if="needList.length && (hasUnfinished || pend.status)"
            >
              <el-image class="dealt-content-image" :src="require('@/assets/images/index/index-logo.png')"></el-image>
              <el-scrollbar style="height: 100%; width: 100%">
                <div class="calendar-dealt-list">
                  <div
                    v-for="(item, index) in needList"
                    :key="'need' + index"
                    :class="item.finish === 1 ? 'active' : ''"
                    v-show="pend.status == 0 ? item.finish <= 1 : item.finish >= -1"
                    class="item"
                    :style="{ '--fill-color': item.color }"
                  >
                    <div class="item-list">
                      <div class="over-text2">
                        <span class="pointer" @click="handleItemClick(item)">{{ item.title }}</span>
                      </div>
                      <div class="time" v-if="[2, 3, 4, 5].includes(item.cid)">
                        {{ item.remind ? item.remind.remind_time : '09:00:00' }}
                      </div>
                      <div class="time" v-else>
                        {{ $moment(item.start_time).format('HH:mm:ss') }}
                      </div>
                    </div>
                    <i
                      class="dealt-icon el-icon-success"
                      v-if="item.finish == 3"
                      :style="{ color: item.color }"
                      @click="setScheduleRecord(item, 0)"
                    ></i>
                    <i
                      v-else-if="item.finish == 2"
                      class="dealt-icon el-icon-error"
                      :style="{ color: item.color }"
                      @click="setScheduleRecord(2)"
                    ></i>
                    <i
                      v-else
                      class="dealt-icon yuan"
                      :style="{ color: item.color }"
                      @click="setScheduleRecord(item, 3)"
                    ></i>
                  </div>
                </div>
              </el-scrollbar>
            </div>
            <default-page v-else :min-height="294" :index="14" />
          </el-row>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card body-style="padding:0">
          <el-row class="calendar_title" style="padding-bottom: 12px">
            <el-col :span="12" class="row-middle">
              <div class="dynamics">快捷入口</div>
            </el-col>
            <el-col :span="12" class="text-right calendar_title-left">
              <span class="display-align" @click="handleQuick">
                <i class="iconfont iconpcshouye-guanli"></i>
                管理
              </span>
            </el-col>
          </el-row>
          <div class="quick-content">
            <div class="quick-list" v-if="quickVal.length > 0">
              <div
                class="quick-list-item"
                v-for="(item, index) in quickVal"
                @click="quickItem(item)"
                :key="'quick' + index"
              >
                <div class="pointer">
                  <el-image :src="item.image" class="image"></el-image>
                  <div class="name">{{ item.name }}</div>
                </div>
              </div>
            </div>
            <default-page v-else :min-height="294" :index="14" />
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!--绩效考核-->
    <el-row :gutter="14" class="mt14">
      <template v-if="assessLoading && assessNowData.length > 0">
        <el-col :span="8">
          <el-card body-style="padding:0">
            <div class="calendar_title clearfix">
              <div class="pull-left acea-row row-middle">
                <div class="dynamics">当前考核</div>
              </div>
              <div class="pull-right acea-row row-middle">
                <div class="notice-more" @click="handleAssessMore()">
                  更多
                  <i class="el-icon-arrow-right"></i>
                </div>
              </div>
            </div>
            <echarts :id="assessNowData[0].id" />
          </el-card>
        </el-col>
      </template>
      <el-col :span="assessLoading && assessNowData.length > 0 ? 8 : 16">
        <el-card body-style="padding:0" class="table-box note">
          <div class="calendar_title clearfix">
            <div class="pull-left acea-row row-middle">
              <div class="dynamics">系统通知</div>
            </div>
            <div class="pull-right acea-row row-middle">
              <div class="notice-more" @click="handleNewMore()">
                更多
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
          <div v-if="systemNote.length" class="list" style="height: 406px">
            <div
              v-for="(item, index) in systemNote"
              :key="index"
              class="item acea-row row-between-wrapper pointer"
              @click="handleDetails(item)"
              :class="item.is_read ? 'finish' : ''"
            >
              <div class="item-list system-note">
                <div class="line1">
                  <span class="label">【{{ item.title }}】</span>
                  <span>{{ item.message }}</span>
                </div>
                <div class="time">{{ $moment(item.created_at).format('MM-DD HH:mm') }}</div>
              </div>
            </div>
          </div>
          <default-page v-else :min-height="406" :index="14" />
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card body-style="padding:0">
          <div class="calendar_title clearfix">
            <div class="pull-left acea-row row-middle">
              <div class="dynamics">企业动态</div>
            </div>
            <div class="pull-right acea-row row-middle">
              <div class="notice-more" @click="handleNoticeMore('')">
                更多
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
          <ul class="news-content" v-if="nweNoticeData.length > 0">
            <li v-for="item in nweNoticeData.slice(0, 5)" :key="'new' + item.id" @click="handleNoticeMore(item.id)">
              <el-row :gutter="14">
                <el-col class="notice-left" :class="item.cover ? '' : 'width100'">
                  <p class="title over-text">{{ item.title }}</p>
                  <div class="bottom">
                    <span>
                      <i class="iconfont iconyiyuedu"></i>
                      {{ item.visit }}
                    </span>
                    <span>
                      <i class="iconfont iconriqishijian"></i>
                      {{ item.push_time.split(' ')[0] }}
                    </span>
                  </div>
                </el-col>
                <el-col class="notice-right" v-if="item.cover">
                  <img v-if="item.cover" :src="item.cover" alt="" />
                </el-col>
              </el-row>
            </li>
          </ul>
          <default-page v-else :min-height="406" :index="14" />
        </el-card>
      </el-col>
    </el-row>
    <password ref="password" :form-data="passwordData"></password>
    <!-- 系统通知 -->
    <noticeList ref="noticeList" v-if="noticeListVisible"></noticeList>
    <!--新建待办-->
    <calendarDetails
      v-if="calendarDetailsVisible"
      ref="calendarDetails"
      @deleteFn="getDailyTodo"
      @editFn="editFn"
    ></calendarDetails>
    <message-details ref="messageDetails" :message-data="messageData" />
    <quick-manage ref="quickManage" @isSuccess="quickSuccess" :config="configData"></quick-manage>
    <addTodo ref="addTodo" v-if="addTodoVisible" @getList="getDailyTodo"></addTodo>
    <contract-dialog
      ref="contractDialog"
      v-if="contractDialogVisible"
      :config="configContract"
      @isOk="getDailyTodo"
    ></contract-dialog>
    <!-- 跟进弹窗 -->
    <el-dialog title="添加跟进记录" class="record" :visible.sync="dialogVisible" width="40%">
      <recordUpload :form-info="formInfo" @change="recordChange"></recordUpload>
    </el-dialog>
    <edit-examine
      ref="editExamine"
      :parameterData="parameterData"
      :ids="formInfo.data.id"
      @scheduleRecord="scheduleRecord"
    ></edit-examine>
    <copyright />
  </div>
</template>
<script>
import {
  dealtScheduleListApi,
  noticeMessageListApi,
  userAssessSubord,
  userWorkMenusApi,
  userWorkCountApi,
  scheduleListApi,
  scheduleTypesApi,
  scheduleStatusApi,
  enterpriseUserJoinApi,
  workStatisticsApi,
  workStatisticsApiAll
} from '@/api/user'
import { auth } from '@/api/setting'
import { noticeListApi } from '@/api/administration'
import { clientRemindDetailApi } from '@/api/enterprise'
import { mapGetters } from 'vuex'
import { pageJumpTo } from '@/libs/public'
import ElementUI from 'element-ui'
import { roterPre } from '@/settings'
import { configRuleApproveApi } from '@/api/config'
export default {
  name: 'Workbench',
  components: {
    echarts: () => import('./components/echart'),
    enterprise: () => import('./components/enterprise'),
    password: () => import('./components/password'),
    contractDialog: () => import('@/views/customer/contract/components/contractDialog'),
    defaultPage: () => import('@/components/common/defaultPage'),
    messageDetails: () => import('@/views/user/news/components/messageDetails'),
    quickManage: () => import('./components/quickManage'),
    calendarDetails: () => import('@/views/user/calendar/components/calendarDetails'),
    recordUpload: () => import('@/views/customer/list/components/recordUpload'),
    addTodo: () => import('@/views/user/calendar/components/addTodo'),
    noticeList: () => import('@/layout/components/Notice/noticeList'),
    editExamine: () => import('@/views/user/examine/components/editExamine'),
    copyright: () => import('@/layout/components/copyright.vue')
  },
  data() {
    const currentDate = new Date();
    const moment = this.$moment;
    return {
      noticeListVisible: false,
      addTodoVisible: false,
      calendarDetailsVisible: false,
      contractDialogVisible: false,
      workBackground: '',
      enterpriseCulture: '',
      realName: this.$store.state.user.userInfo.name,
      noon: moment(currentDate).format('a'),
      pend: {
        status: 3
      },
      dialogVisible: false,
      configContract: {},
      formInfo: {
        avatar: '',
        type: 'add',
        show: 1,
        data: {},
        follow_id: 0
      },
      needList: [],
      value: currentDate,
      calendar: {
        time: moment(currentDate).format('YYYY-MM') // 当月
      },
      dailyList: {}, // 点击当前天数的计划与总结
      currentDay: moment(currentDate).format('DD'), // 当天
      dailyDay: [], // 当月有日报的天数组合
      imgList: [],
      achievementList: [], // 业绩统计数组
      rolesConfig: {},
      formBoxConfig: {},
      entConfig: {},
      entBoxConfig: {},
      types: [],
      scheduleTypes: [],
      time: moment(currentDate).format('yyyy-MM-DD'),
      itemArray: [],
      passwordData: {},
      calendarConut: [],
      userId: '',
      nweNoticeData: [],
      defaultImage: require('@/assets/images/notice.png'),
      userInfo: {},
      systemNote: [], // 系统通知
      timeTab: 1,
      timeTabData: [
        { text: '前一天', label: 0 },
        { text: '今天', label: 1 },
        { text: '后一天', label: 2 }
      ],
      timeTabIndex: 1,
      timeTabLoading: false,
      timeTabNum: 0,
      needInfo: [
        {
          icon: require('@/assets/images/personal-icon-01.png'),
          text: '待办任务',
          num: 0,
          path: '/user/calendar/index',
          type: -1
        },
        {
          icon: require('@/assets/images/personal-icon-04.png'),
          text: '我的申请',
          num: 0,
          path: '/user/examine/mine',
          type: 1
        },
        {
          icon: require('@/assets/images/personal-icon-03.png'),
          text: '待我审批',
          num: 0,
          path: '/user/examine/approval',
          type: 0
        },
        {
          icon: require('@/assets/images/personal-icon-02.png'),
          text: '企业动态',
          num: 0,
          path: '/user/notice/index',
          type: -1
        }
      ],
      options: [
        {
          value: 0,
          label: '部门业绩'
        },
        {
          value: 1,
          label: '本人业绩'
        }
      ],
      achievementTypes: 0,
      quickData: [],
      quickVal: [],
      assessNowData: [],
      assessLoading: false,
      messageData: {},
      configData: {},
      parameterData: {
        contract_id: '',
        customer_id: '',
        invoice_id: '',
        bill_id: ''
      },
      buildData: [],
      item: {},
      status: 0
    };
  },
  computed: {
    hasFinished() {
      return this.needList.some((item) => item.finish >= 2);
    },
    hasUnfinished() {
      return this.needList.some((item) => item.finish <= 1)
    },
    ...mapGetters(['enterprise'])
  },
  watch: {
    value: function (value) {
      this.getScheduleDay(this.$moment(value).format('yyyy-MM-DD'), 1)
    }
  },

  mounted() {
// 集中获取用户信息，避免多次解析localStorage
const userInfo = JSON.parse(localStorage.getItem('userInfo'));
if (userInfo) {
  this.userId = userInfo.id;
  this.formInfo.avatar = userInfo.avatar;
}
// 定义一个异步函数数组，用于并行执行异步操作
const asyncTasks = [
  this.workStatisticsList(),
  this.getAssessMine(),
  this.getNewTableData(),
  this.getTypes(),
  this.getNewListData(),
  this.getUserWorkMenus(),
  this.entAuth(),
  this.getUserWorkCount(),
  this.getConfigApprove()
];

// 并行执行异步任务
Promise.all(asyncTasks)
  .then(() => {
    // 所有异步任务完成后执行的操作
  })
  .catch(error => {
    console.error('异步任务执行出错:', error);
  });

// 同步执行的操作
this.getInvitation();
this.getPassword();
  },
  methods: {
    // 业绩统计
    async workStatisticsList() {
      const result = await workStatisticsApi(this.achievementTypes)
      this.achievementList = result.data
    },

    // 跳转到业绩统计
    performanceFn() {
      this.$router.push({
        path: `${roterPre}/customer/turnover/index`,
        query: {}
      })
    },

    // 打开业绩统计弹窗
    async handlePerformance() {
      const result = await workStatisticsApiAll()
      let otherData = [
        {
          cate_name: '业绩简报',
          fast_entry: result.data.list
        }
      ]
      this.configData = {
        title: '业绩统计管理',
        type: 'statistics',
        width: '600px',
        data: result.data.select,
        otherArr: otherData
      }
      await this.$refs.quickManage.handleOpen(this.configData)
    },

    // 关闭跟进弹窗
    recordChange() {
      this.dialogVisible = false
      this.getDailyTodo()
    },

    // 获取授权信息
    async entAuth() {
      const obj = await auth()
      const data = obj.data
      if (data.status === -1) {
        await this.getAuthMessage(`您的授权证书还有${data.day}天过期,请及时前往陀螺匠官方进行授权认证!`)
      }
    },

    // 提醒授权弹窗
    getAuthMessage(message) {
      const content = `<div class='el-row display-align'>
        <div class="el-col el-col-24 right width100">
          <p class='title over-text'>授权提醒</p>
          <p class='caption over-text2'>${message}</p>
        </div>
      </div>
      <div class='text-right'>
        <button id="messageOpen" type="button" class="el-button el-button--text el-button--small"><span>立即授权</span></button>
      </div>`

      const notify = ElementUI.Notification({
        title: '消息',
        dangerouslyUseHTMLString: true,
        message: content,
        duration: 10000,
        offset: 60,
        iconClass: 'iconfont iconxiaoxi',
        customClass: 'message-socket'
      })

      const oBtn = document.getElementById('messageOpen')
      oBtn.addEventListener('click', () => {
        pageJumpTo('/setting/auth/auth/index')
        notify.close()
      })
    },

    // 编辑代办日程
    editFn(id, type, date) {
      let data = {
        id,
        type,
        edit: true,
        date
      }
      this.addTodoVisible = true
      setTimeout(() => {
        this.$refs.addTodo.openBox(data)
      }, 200)
    },

    // 打开系统消息弹窗
    handleDetails(row) {
      this.messageData = {
        width: '560px',
        data: row
      }
      this.$refs.messageDetails.handleOpen()
    },

    // 处理数据
    findItem(arr, key, val) {
      for (var i = 0; i < arr.length; i++) {
        if (arr[i].id === val || arr[i].id == this.userId) {
          return 2
        }
      }
      return -1
    },

    getDailyTodo() {
      this.getDailyTodoInfo(this.time)
    },

    getDailyTodoInfo(time, load = false) {
      if (load) {
        this.timeTabLoading = true
      }
      let start_time = this.$moment(time).format('YYYY-MM-DD') + ' 00:00:00'
      let end_time = this.$moment(time).format('YYYY-MM-DD') + ' 23:59:59'
      const data = {
        start_time: start_time,
        end_time: end_time,
        cid: this.types,
        period: 1
      }
      scheduleListApi(data)
        .then((res) => {
          res.data.forEach((item, index) => {
            let len = this.findItem(item.user, 'id', item.master.id)
            if (len == -1) {
              res.data.splice(index, 1)
            }
          })

          this.needList = res.data
          if (this.needList.length > 1) {
            this.needList.sort((a, b) => a.finish - b.finish)
          }
          if (load) {
            this.timeTabLoading = false
          }
        })
        .catch((error) => {
          if (load) {
            this.timeTabLoading = false
          }
        })
    },

    async getTypes() {
      const result = await scheduleTypesApi()
      this.scheduleTypes = result.data
      result.data.map((value) => {
        this.types.push(value.id)
      })
      await this.getDailyTodoInfo(this.$moment(new Date()).format('yyyy-MM-DD'))
    },

    dealtItemColor(type) {
      let str = ''
      if (this.scheduleTypes.length > 0) {
        for (let i = 0; i < this.scheduleTypes.length; i++) {
          const value = this.scheduleTypes[i]
          if (value.key === type) {
            str = value.color
            break
          }
        }
      }
      return str
    },

    // 当天提醒
    pendList() {
      const time = this.$moment(new Date()).format('yyyy-MM-DD')
      this.getScheduleDay(time, 1)
    },

    async getScheduleDay(time, type) {
      const data = {
        time: time,
        types: this.types,
        period: 0
      }
      const result = await dealtScheduleListApi(data)
      const res = result.data[0].list
      if (type === 1) {
        res.sort((a, b) => a.finish - b.finish)
        this.needList = res
      }
    },

    // 提醒状态
    async scheduleRecord(data, status) {
      let info = {
        status: status ? status : this.status,
        start: data ? data.start_time : this.item.start_time,
        end: data ? data.end_time : this.item.end_time
      }
      let id = data ? data.id : this.item.id
      await scheduleStatusApi(id, info)
      await this.getDailyTodo()
    },
    async getConfigApprove() {
      const result = await configRuleApproveApi(0)
      this.buildData = result.data
    },
    // 修改代办日程状态
    setScheduleRecord(item, status) {
      this.item = item
      this.status = status
      this.contractDialogVisible = true
      if (item == 2) {
        return this.$message.error('您已拒绝该任务！')
      }
      if (item.cid == 3 || item.cid == 4) {
        let id = item.relation.bill_id !== 0 ? item.relation.bill_id : item.relation.remind_id
        if (status == 3) {
          clientRemindDetailApi(id).then((res) => {
            res.data.id = item.relation.remind_id
            this.parameterData.customer_id = res.data.eid
            this.parameterData.contract_id = res.data.cid
            if (res.data.types == 0) {
              let data = {
                id: this.buildData.contract_refund_switch
              }

              this.$refs.editExamine.openBox(data, res.data.cid, 'contract_refund_switch', item, status)
            } else {
              let data = {
                id: this.buildData.contract_renew_switch
              }
              this.$refs.editExamine.openBox(data, res.data.cid, 'contract_renew_switch', item, status)
            }
          })
        }
      } else if (item.cid == 2) {
        if (item.finish == 3) {
          return false
        }
        this.formInfo.data.eid = item.link_id

        this.formInfo.follow_id = item.relation ? item.relation.follow_id : 0
        this.dialogVisible = true
      } else {
        let newArr = []
        this.needList.map((val) => {
          if (val.id == item.id) {
            val.finish = status
          }
          newArr.push(val)
        })
        this.needList = newArr
        this.scheduleRecord(item, status)
      }
    },

    // 初始密码修改
    getPassword() {
      if (this.$store.state.user.userInfo.is_init === 1) {
        this.passwordData = {
          title: '修改密码',
          width: '540px'
        }
        setTimeout(() => {
          this.$refs.password.handleOpen()
        }, 300)
      }
    },

    // 链接邀请
    getInvitation() {
      let invitationStorage = localStorage.getItem('invitationStorage')
      const invitation = JSON.parse(invitationStorage)
      if (invitationStorage !== null) {
        this.getEnterpriseInfo(invitation)
      }
    },

    async getEnterpriseInfo(invitation) {
      await enterpriseUserJoinApi({
        invitation: invitation.invitation
      })
      await localStorage.removeItem('invitationStorage')
    },

    // 点击我的待办与我的已办
    handleItemClick(item) {
      item.itemId = item.id
      this.calendarDetailsVisible = true

      setTimeout(() => {
        this.$refs.calendarDetails.openBox(item)
      }, 200)
    },

    // 跳转到企业动态
    handleNoticeMore(id = '') {
      const data = id ? { id: id } : {}
      pageJumpTo('/user/notice/index', data)
    },

    // 打开系统通知弹窗
    handleNewMore() {
      this.noticeListVisible = true
      this.$nextTick(() => {
        this.$refs.noticeList.openBox()
      })
    },

    // 点击快捷菜单跳转
    quickItem(item) {
      pageJumpTo(item.pc_url)
    },

    // 跳转到绩效考核
    handleAssessMore() {
      pageJumpTo('/user/assessment/my')
    },

    // 业绩考核
    async getAssessMine() {
      let data = {
        handle: 1,
        time: '',
        status: 1
      }
      const result = await userAssessSubord(data)
      this.assessLoading = true
      this.assessNowData = result.data.list ? result.data.list : []
    },

    // 顶部页面跳转
    needInfoItem(item) {
      if (!item.path) return
      pageJumpTo(item.path)
    },

    // 获取系统消息列表
    async getNewListData() {
      const result = await noticeMessageListApi({ page: 1, limit: 11, cate_id: '', is_read: '' })
      this.systemNote = result.data.list
    },

    // 获取企业动态列表
    async getNewTableData() {
      const result = await noticeListApi({ is_new: 1, status: 1 })
      this.nweNoticeData = result.data.list || []
    },

    // 获取快捷入口列表
    async getUserWorkMenus() {
      this.quickVal = []
      const result = await userWorkMenusApi()
      this.quickData = result.data
      this.quickVal = result.data.checkd
    },

    // 快捷管理子传父事件
    quickSuccess() {
      this.workStatisticsList()
      this.getUserWorkMenus()
    },

    // 获取顶部四列数据
    async getUserWorkCount() {
      const result = await userWorkCountApi()
      const data = result.data
      this.needInfo[0].num = data.scheduleCount
      this.needInfo[1].num = data.applyCount
      this.needInfo[2].num = data.approveCount
      this.needInfo[3].num = data.noticeCount
    },

    // 日期切换
    timeTabInput(e, index) {
      this.timeTabIndex = index
      if (e.label === 1 && this.timeTabNum === 0) return
      if (e.label === 0) {
        this.timeTabNum--
      } else if (e.label === 1) {
        this.timeTabNum = 0
      } else {
        this.timeTabNum++
      }
      this.time = this.$moment(new Date()).add(this.timeTabNum, 'day').format('yyyy-MM-DD')
      this.getDailyTodoInfo(this.time, true)
    },

    // 打开快捷管理弹窗
    handleQuick() {
      let arr = []
      arr = JSON.parse(JSON.stringify(this.quickVal))
      let otherArr = this.quickData.cates
      this.configData = {
        title: '快捷入口管理',
        width: '600px',
        data: arr,
        otherArr: otherArr
      }

      this.$refs.quickManage.handleOpen(this.configData)
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep.el-calendar__header {
  align-items: center;
  height: 58px;
  border-bottom: 0;
}

.box {
  ::v-deep .divBox {
    margin: 0;
    padding: 0;
  }
}
.dynamics {
  font-size: 16px;
  font-weight: 700;
  display: flex;
  align-items: center;
  ::v-deep .el-input--suffix .el-input__inner {
    border: none;
    font-size: 12px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: #1890ff;
    margin-left: 15px;
  }
  ::v-deep .el-input .el-select__caret {
    font-size: 12px;
    margin-top: 1px;
    color: #606266;
  }
}
.achievementContent {
  margin-top: 18px;
  display: flex;
  .item {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    width: 25%;
    .num {
      font-size: 20px;
      font-family: PingFang SC-Semibold, PingFang SC;
      font-weight: 700;
      color: #000000;
    }
    .text {
      margin-top: 10px;
      font-size: 14px;
      font-family: PingFang SC-Regular, PingFang SC;
      font-weight: 400;
      color: #909399;
    }
  }
}

::-webkit-scrollbar-thumb {
  -webkit-box-shadow: inset 0 0 6px #ccc;
  display: none;
}
::-webkit-scrollbar {
  width: 4px !important; /*对垂直流动条有效*/
}
.table-box .list:hover::-webkit-scrollbar-thumb,
.enterprise:hover::-webkit-scrollbar-thumb {
  display: block;
}

::v-deep.el-calendar__body {
  padding: 10px 20px 10px;
}
::v-deep.el-calendar-table td {
  border: 0;
}
::v-deep.el-calendar-table tr:first-child td {
  border: 0;
}
::v-deep.el-calendar-table tr td:first-child {
  border: 0;
}
::v-deep.el-calendar-table .el-calendar-day {
  text-align: center;
  height: 36px;
  line-height: 36px;
  padding: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}
::v-deep.el-calendar-table td p {
  width: 28px;
  height: 28px;
  line-height: 28px;
  position: relative;
  i {
    position: absolute;
    top: 10px;
    left: 4px;
    font-size: 20px;
    font-weight: bold;
    color: rgba(0, 192, 80, 0.6);
  }
}
::v-deep.el-calendar-table td.is-selected {
  .title {
    position: absolute;
    z-index: 10;
  }
  .dealt-content {
    background: #1890ff;
    border-radius: 50%;
  }
}
.notice-more {
  color: #999;
  cursor: pointer;
  font-size: 13px;
  margin-top: 4px;
}
::v-deep.el-tabs__active-bar {
  height: 0;
}
::v-deep .el-tabs__header {
  margin-bottom: 0;
}
::v-deep .el-tabs__nav-wrap::after {
  display: none;
}
::v-deep.el-divider--vertical {
  margin: 0 5px;
}
.enterprise {
  height: 280px;
  padding: 5px 20px 0 20px;
  overflow-x: hidden;
  overflow-y: auto;
  ::v-deep.el-button--text {
    padding: 0 !important;
  }
  .item {
    margin-top: 16px;
    font-size: 13px;
    .line1 {
      width: 75%;
    }
    .label {
      color: #1890ff;
      margin-right: 5px;
    }
  }
}
.calendar_area {
  width: 100%;
  text-align: center;
  display: flex;
  justify-content: center;
  height: 36px;
  align-items: center;
  position: relative;
}
::v-deep.el-calendar-table td .dealt-content {
  line-height: 28px;
  i {
    position: absolute;
    right: 0;
    top: 0;
    font-size: 14px;
    font-weight: bold;
  }
}
.calendar_title {
  padding: 20px;
  .icon {
    color: #1890ff;
    margin-right: 5px;
    .iconfont {
      font-size: 20px;
    }
  }
  .calendar_title-left {
    span {
      cursor: pointer;
      justify-content: flex-end;
      color: #909399;
      i {
        margin-right: 2px;
      }
    }
  }
}
.calendar-dealt {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 0;
  .active {
    background-color: #1890ff;
    color: #fff;
    border: 1px solid transparent;
  }
  .dynamics-icon {
    color: #dcdfe6;
  }
}
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
.dealt-content-list {
  height: 294px;
  width: 100%;
  padding: 14px 0 14px 0;
  position: relative;
  .dealt-content-image {
    position: absolute;
    right: 0;
    bottom: 0;
    width: 332px;
    height: 178px;
  }
}
.calendar-dealt-list {
  padding: 0 20px 0 20px;
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  .item {
    width: calc((100% - 26px) / 3);
    display: flex;
    align-items: center;
    padding: 0 12px;
    height: 80px;
    margin-bottom: 13px;
    margin-right: 13px;
    background: #f2f5fc;
    border-radius: 6px;
    position: relative;
    &:nth-of-type(3n) {
      margin-right: 0;
    }
    &:last-of-type {
      margin-right: 0;
    }
    &.active {
      background: #f6f6f6;
      .item-list {
        &:after {
          background-color: #dcdfe6;
        }
      }
    }
    .item-list {
      color: #303133;
      font-size: 14px;
      line-height: 1.5;
      padding-left: 16px;
      padding-right: 20px;
      position: relative;
      .over-text2 {
        word-break: break-all;
      }
      &:after {
        position: absolute;
        left: 0;
        top: 0;
        content: '';
        width: 3px;
        height: 100%;
        background-color: var(--fill-color);
      }
      .time {
        font-size: 12px;
        color: #909399;
      }
    }
    .dealt-icon {
      position: absolute;
      top: 13px;
      right: 13px;
      cursor: pointer;
    }
    .el-icon-success {
      vertical-align: bottom;
      font-size: 16px;
      color: var(--fill-color);
    }
    .yuan {
      display: inline-block;
      width: 13px;
      height: 13px;
      border-radius: 50%;
      border: 1px solid #999;
    }
  }
}

.quick-content {
  width: 100%;
  height: 292px;
  padding: 0 14px;
  .quick-list {
    height: 100%;
    padding-bottom: 14px;
    .quick-list-item {
      width: 25%;
      text-align: center;
      height: 92px;
      display: flex;
      float: left;
      align-items: center;
      justify-content: center;
      .image {
        width: 40px;
        height: 40px;
        display: inline-block;
      }
      .name {
        font-size: 13px;
        color: #606266;
        padding-top: 8px;
      }
    }
  }
}
.iconqiyewendang {
  color: #1890ff;
  font-size: 19px;
  margin-right: 5px;
}
.calendar_list {
  padding: 0 20px 0 30px;
  height: 112px;
  overflow-x: hidden;
  margin-bottom: 20px;
  overflow-y: auto;
  .item {
    font-size: 13px;
    color: rgba(0, 0, 0, 0.85);
    line-height: 20px;
    margin-bottom: 12px;
    .label {
      margin-right: 5px;
    }
  }
}
.table-box {
  //   padding-bottom: 24px;
  ::v-deep .table-box-title {
    padding-left: 21px;
    height: 58px;
    // border-bottom: 1px solid rgba(216, 216, 216, 0.3);
    border-bottom: 0;
    display: flex;
    align-items: center;
    color: #000000;
  }
  .el_tabs {
    padding: 5px 30px;
  }
  .list {
    padding: 0 20px 0 16px;
    height: 280px;
    overflow-x: hidden;
    overflow-y: auto;
    .item {
      margin-top: 23px;
      font-size: 13px;
      font-weight: 400;
      color: rgba(0, 0, 0, 0.85) !important;
      &:first-of-type {
        margin-top: 0;
      }
      &.finish {
        color: #909399 !important;
        .time {
          color: #909399;
        }
        .el-icon-check {
          margin-right: 6px;
          vertical-align: bottom;
          font-size: 14px;
        }
        .line1 {
          color: #909399 !important;
          .label {
            color: #909399 !important;
          }
        }
      }
      .item-list {
        width: 100%;
        display: flex;
      }
      .line1 {
        width: 70%;
        color: #303133;
      }
      .system-note {
        .line1 {
          width: calc(100% - 80px);
        }
        .time {
          width: 80px;
        }
      }
      .label {
        color: #1890ff;
        margin-right: 4px;
      }
      .time {
        width: 30%;
        text-align: right;
      }
      .el-icon-remove-outline {
        // margin-right: 6px;
        // vertical-align: bottom;
        // font-size: 14px;
        color: #1890ff;
      }
    }
  }

  &.note .list .item .label {
    color: rgba(0, 0, 0, 0.85) !important;
  }
}
.achievementPb {
  padding: 0;
}

.header-pic {
  width: 100%;
  height: 126px;
  position: relative;
  &.on {
    background-color: #ccc;
  }
  .image {
    width: 100%;
    height: 100%;
  }
  .text {
    position: absolute;
    top: 32px;
    left: 38%;
    padding-right: 14px;
    color: #303133;
    font-weight: 500;
    font-size: 18px;
    .time {
      margin-left: 6px;
    }
    .info {
      font-size: 13px;
      margin-top: 10px;
      color: #909399;
      line-height: 1.5;
    }
  }
}

.header-need {
  width: 100%;
  height: 126px;
  list-style: none;
  padding: 0;
  margin: 0;
  background-color: #fff;
  li {
    margin-top: 38px;
    cursor: pointer;
    width: 25%;
    float: left;
    display: flex;
    justify-content: center;
    border-right: 1px solid #f0f2f5;
    &:last-of-type {
      border-right: 0;
    }
    .image {
      width: 50px;
      height: 50px;
    }
    .header-need-right {
      height: 50px;
      padding-left: 14px;
      display: flex;
      justify-content: space-between;
      flex-direction: column;
      p {
        margin: 0;
        padding: 0;
      }
      .num {
        color: #ff9900;
        font-size: 24px;
        font-weight: 600;
      }
      .text {
        color: #606266;
        font-size: 13px;
      }
    }
  }
}
.news-content {
  height: 406px;
  overflow: auto;
  margin: 0;
  padding: 0 20px;
  list-style: none;
  li {
    margin-top: 18px;
    cursor: pointer;
    &:last-of-type {
      padding-bottom: 18px;
    }
    .notice-left {
      width: calc(100% - 110px);
      p {
        margin: 0;
      }
      .title {
        font-size: 14px;
        font-weight: 500;
        margin-bottom: 8px;
      }
      .bottom {
        margin-top: 24px;
        color: #999999;
        font-size: 12px;
        i {
          font-size: 12px;
        }
        span:last-of-type {
          padding-left: 10px;
        }
      }
    }
    .notice-right {
      width: 110px;
      img {
        width: 100%;
        height: 60px;
        border-radius: 5px;
        object-fit: cover;
      }
    }
    &:first-of-type {
      margin-top: 0;
    }
  }
}
.width100 {
  width: 100% !important;
}
.left-line {
  position: relative;
}
.left-line::before {
  content: '';
  position: absolute;
  top: 22px;
  bottom: 22px;
  left: 0;
  width: 1px;
  background-color: #f2f6fc;
}
.icondakai,
.iconyincang {
  cursor: pointer;
}
.dealt-date {
  ::v-deep .el-calendar__header {
    padding: 20px;
  }
  .calendar_area {
    width: 100%;
    text-align: center;
    display: flex;
    justify-content: center;
    height: 40px;
    align-items: center;
    position: relative;
  }
  ::v-deep.el-calendar-table td .dealt-content {
    i {
      position: absolute;
      right: 0;
      top: 0;
      font-size: 14px;
      font-weight: bold;
    }
  }
  .dealt-date-item {
    width: 100%;
    height: 100%;
  }
  .is-selected {
    display: inline-block;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background-color: #1890ff;
  }
  position: relative;
  ::v-deep .el-calendar-table .el-calendar-day {
    height: 40px;
    padding: 0;
  }
  ::v-deep .el-calendar__body {
    padding-bottom: 20px;
  }
  ::v-deep .el-calendar-table__row td {
    text-align: center;
  }
  ::v-deep .el-calendar__header {
    justify-content: center;
  }
  ::v-deep .el-calendar__button-group {
    .el-button-group {
      button {
        color: #000000;
        border: none;
      }
      button:nth-of-type(1) {
        position: absolute;
        left: 20px;
        top: 18px;
      }
      button:nth-of-type(2) {
        display: none;
      }
      button:nth-of-type(3) {
        position: absolute;
        right: 20px;
        top: 18px;
      }
    }
  }
}
</style>
