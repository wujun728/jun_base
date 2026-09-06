<template>
  <view>
    <uni-popup ref="popupRef" type="bottom">
      <view class="slider">
        <view class="tab">
          <view class="tab-title" @click="tabFn(1)" :class="data.tabIndex == 1 ? 'active' : ''"> 员工与部门 </view>
          <view class="tab-title" @click="tabFn(2)" :class="data.tabIndex == 2 ? 'active' : ''"> 时间筛选 </view>
        </view>
        <!--  tab切换内容 -->
        <!-- <scroll-view scroll-y="true" style="width: 100%; height: 100%"> -->
        <view class="content p30">
          <view class="btn-flex" v-if="data.tabIndex == 1">
            <view class="btn" v-for="(item, index) in data.btnList" :key="index"
              :class="data.btnIndex == item.id ? 'btnActive' : ''" @click="btnFn(item.id, '', item.title)">
              {{ item.title }}
            </view>
            <template v-if="data.infoUser.length !== 0 && data.btnIndex == 4">
              <!-- 选中人员数据 -->
              <view class="member-box" v-for="(item, index) in data.infoUser" :key="index">
                <view class="avatar_flex">
                  <image :src="item.avatar" mode="" class="avatar"></image>
                  <text>{{ item.name }}</text>
                </view>
                <text class="iconfont icon-guanbi" @click="deleteFn(item, index)"></text>
              </view>
            </template>
            <template v-if="data.infoDepartment.length !== 0 && data.btnIndex == 3">
              <!-- 选中部门数据 -->
              <view class="member-box" v-for="(item, index) in data.infoDepartment" :key="index">
                <view>
                  <text class="iconfont icon-gerenbangong"></text>
                  <text>{{ item.name }}</text>
                </view>
                <text class="iconfont icon-guanbi" @click="deleteFn(item, index)"></text>
              </view>
            </template>
          </view>
          <view class="btn-flex" v-if="data.tabIndex == 2">
            <view class="btn" v-for="(item, index) in data.TimeList" :key="index"
              :class="data.timeIndex == item.id ? 'btnActive' : ''" @click="btnFn(item.id, 'time', item.title)">
              {{ item.title }}
            </view>
            <view class="flex" style="width: 100%;" v-if="data.timeIndex == 9">
              <uni-datetime-picker type="date" placeholder="开始时间" :clear-icon="false" v-model="data.start" />
              <text class="text">-</text>
              <uni-datetime-picker type="date" placeholder="结束时间" :clear-icon="false" v-model="data.end"
                @maskClick="maskClick" />
            </view>
          </view>
        </view>
        <!-- </scroll-view> -->

        <view class="slider-laber-bottom">
          <button class="reset laber-bottom" @click="reset">重置</button>
          <button class="laber-bottom confirm" @click="confirm">确认</button>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
  import { ref, reactive, toRefs, onMounted, computed, watch } from 'vue'
  import { useStore } from 'vuex'
  import moment from 'moment'
  import message from '@/utils/message'
  import { navigateToDepartment, resetSelectDepartment, resetExamineIndex } from '@/utils/autoload'
  const store = useStore()
  const data = reactive({
    showPerson: false,
    infoUser: [],
    infoDepartment: [],
    mode: 'selector',
    tabIndex: 1,
    btnIndex: 2,
    timeIndex: 5,
    start: '',
    end: '',
    where: {
      type: 2,
      time: moment().month(moment().month()).startOf('month').format('YYYY/MM/DD') +
        '-' +
        moment().month(moment().month()).endOf('month').format('YYYY/MM/DD'),
      member: [],
      btnText: '本部门',
      timeText: '本月',
    },
    TimeList: [{
        title: '今天',
        id: 1,
      },
      {
        title: '昨天',
        id: 2,
      },
      {
        title: '本周',
        id: 3,
      },
      {
        title: '上周',
        id: 4,
      },
      {
        title: '本月',
        id: 5,
      },
      {
        title: '上月',
        id: 6,
      },
      {
        title: '今年',
        id: 7,
      },
      {
        title: '去年',
        id: 8,
      },
      {
        title: '自定义',
        id: 9,
      },
    ],
    btnList: [{
        title: '本人及下属',
        id: 0,
      },
      {
        title: '仅本人',
        id: 1,
      },
      {
        title: '本部门',
        id: 2,
      },
      {
        title: '选择部门',
        id: 3,
      },
      {
        title: '选择人员',
        id: 4,
      },
    ],
  })

  // 点击tab切换
  const tabFn = (index) => {
    data.tabIndex = index
  }
  // 删除选中部门或者人员
  const deleteFn = (val, index) => {
    if (data.btnIndex == 3) {
      if (data.infoDepartment.length > 1) {
        data.infoDepartment.splice(index, 1)
      } else {

        return message.error('最少选择一个部门')
      }
    } else if (data.btnIndex == 4) {
      if (data.infoUser.length > 1) {
        data.infoUser.splice(index, 1)
      } else {

        return message.error('最少选择一个人员')
      }
    }
  }
  // 选中人的列表
  const getSelectPeople = computed(() => {
    return store.state.app.depSelectPeople
  })

  watch(
    getSelectPeople,
    (newvalue) => {
      data.member = []
      if (data.btnIndex == 3) {
        data.infoDepartment = newvalue
      } else if (data.btnIndex == 4) {
        data.infoUser = newvalue
      }
    }, {
      deep: true,
    }
  )
  const emit = defineEmits(['change'])
  // 确定
  const confirm = () => {

    data.where.member = []
    let text = []
    if (data.where.type == 4) {
      if (data.infoUser.length > 0) {
        data.infoUser.map((item) => {
          data.where.member.push(item.id)
          text.push(item.name)
        })
        data.where.btnText = text.join(',')
      } else {
        if (data.tabIndex == 1) {
          return message.error('请选择人员')
        } else {
          data.where.type = 2
          data.tabIndex = 2
          data.where.btnText = '本部门'
        }
      }
    }
    if (data.where.type == 3) {
      if (data.infoDepartment.length > 0) {
        data.infoDepartment.map((item) => {
          data.where.member.push(item.id)
          text.push(item.name)
        })
        data.where.btnText = text.join(',')
      } else {
        if (data.tabIndex == 1) {
          return message.error('请选择部门')
        } else {
          data.where.type = 2
          data.tabIndex = 2
          data.where.btnText = '本部门'
        }
      }
    }


    if (data.timeIndex == 9) {
      if (!data.start && data.tabIndex == 2) {
        return message.error('请选择开始时间')
      }
      if (!data.end && data.tabIndex == 2) {
        return message.error('请选择结束时间')
      }
      if (data.start && data.end) {
        data.where.time = moment(data.start).format('YYYY/MM/DD') + '-' + moment(data.end).format('YYYY/MM/DD')
        data.where.timeText = moment(data.start).format('YYYY/MM/DD') + '-' + moment(data.end).format('YYYY/MM/DD')
      } else {
        data.timeIndex = 5
        data.where.timeText = '本月'
        data.where.time =
          moment().month(moment().month()).startOf('month').format('YYYY/MM/DD') +
          '-' +
          moment().month(moment().month()).endOf('month').format('YYYY/MM/DD')
      }
    }
    emit('change', data.where)
    popupRef.value.close()
  }
  const reset = () => {
    data.where.time =
      moment().month(moment().month()).startOf('month').format('YYYY/MM/DD') +
      '-' +
      moment().month(moment().month()).endOf('month').format('YYYY/MM/DD')
    data.timeIndex = 5
    data.where.timeText = '本月'
    data.tabIndex = 2
    data.where.type = 2
    data.where.btnText = '本部门'
    data.where.member = []
    data.infoUser = []
    data.infoDepartment = []
    emit('change', data.where)
    popupRef.value.close()
    data.start = ''
    data.end = ''
  }
  // 点击部门btn
  const btnFn = (index, type, name) => {
    if (type == 'time') {
      data.timeIndex = index
      data.where.timeText = name
      if (index == 1) {
        data.where.time = moment(new Date()).format('YYYY/MM/DD') + '-' + moment(new Date()).format('YYYY/MM/DD')
      } else if (index == 2) {
        data.where.time = moment().add('days', -1).format('YYYY/MM/DD') + '-' + moment().add('days', -1).format(
          'YYYY/MM/DD')
      } else if (index == 3) {
        data.where.time = moment().weekday(1).format('YYYY/MM/DD') + '-' + moment().weekday(7).format(
          'YYYY/MM/DD')
      } else if (index == 4) {
        data.where.time =
          moment()
          .week(moment().week() - 1)
          .startOf('week')
          .add(1, 'days')
          .format('YYYY/MM/DD') +
          '-' +
          moment()
          .week(moment().week() - 1)
          .endOf('week')
          .add(1, 'days')
          .format('YYYY/MM/DD')
      } else if (index == 5) {
        data.where.time =
          moment().month(moment().month()).startOf('month').format('YYYY/MM/DD') +
          '-' +
          moment().month(moment().month()).endOf('month').format('YYYY/MM/DD')
      } else if (index == 6) {
        data.where.time =
          moment()
          .month(moment().month() - 1)
          .startOf('month')
          .format('YYYY/MM/DD') +
          '-' +
          moment()
          .month(moment().month() - 1)
          .endOf('month')
          .format('YYYY/MM/DD')
      } else if (index == 7) {
        data.where.time =
          moment().year(moment().year()).startOf('year').format('YYYY/MM/DD') + '-' + moment().year(moment()
            .year())
          .endOf('year').format('YYYY/MM/DD')
      } else if (index == 8) {
        data.where.time =
          moment()
          .year(moment().year() - 1)
          .startOf('year')
          .format('YYYY/MM/DD') +
          '-' +
          moment()
          .year(moment().year() - 1)
          .endOf('year')
          .format('YYYY/MM/DD')
      }
    } else {
      data.btnIndex = index
      data.where.btnText = name
      if (index == 4) {
        data.showPerson = true
        data.mode = 'multiSelector'
        const query = `isShow=true&isFirst=1&isSelect=1&mode=${data.mode}&showPerson=${data.showPerson}`
        navigateToDepartment(query, 'pages/customer/list/statistics')
        if (data.infoUser.length > 0) {
          let idsArr = []
          data.infoUser.map((item) => {
            idsArr.push(item.id)
          })
          store.commit('setDepSelectPeople', data.infoUser)
          store.commit('setDepSelectIds', idsArr)
        } else {
          store.commit('setDepSelectPeople', [])
          store.commit('setDepSelectIds', [])
        }
      } else if (index == 3) {
        data.showPerson = false
        data.mode = 'multiSelector'
        const query = `isShow=true&isFirst=1&isSelect=1&mode=${data.mode}&showPerson=${data.showPerson}`
        navigateToDepartment(query, 'pages/customer/list/statistics')
        if (data.infoDepartment.length > 0) {
          let idsArr1 = []
          data.infoDepartment.map((item) => {
            idsArr1.push(item.id)
          })
          store.commit('setDepSelectPeople', data.infoDepartment)
          store.commit('setDepSelectIds', idsArr1)
        } else {
          store.commit('setDepSelectPeople', [])
          store.commit('setDepSelectIds', [])
        }
      }
      data.where.type = index
    }
  }
  const popupRef = ref(null)
  // 打开弹窗
  const popupOpen = (val) => {
    data.tabIndex = val
    popupRef.value.open()
  }

  defineExpose({ popupOpen })
</script>

<style lang="scss" scoped>
  .p30 {
    padding: 0 30rpx;
  }

  .avatar_flex {
    display: flex;
    align-items: center;
  }

  .avatar {
    display: block;
    width: 56rpx;
    height: 56rpx;
    border-radius: 10rpx;
    margin-right: 18rpx;
  }

  .icon-gerenbangong {
    display: inline-block;
    width: 56rpx;
    height: 56rpx;
    border-radius: 10rpx;
    background-color: #f2f6fc;
    line-height: 56rpx;
    text-align: center;
    color: #1890ff;
    font-size: 34rpx;
    margin-right: 18rpx;
  }

  .member-box {
    width: 100%;
    height: 92rpx;
    border-radius: 8rpx;
    line-height: 92rpx;
    border: 2rpx solid #ebeef5;
    padding: 0 34rpx;
    display: flex;
    justify-content: space-between;
    margin-bottom: 20rpx;

    .icon-guanbi {
      color: rgba(192, 196, 204, 1);
      font-size: 24rpx;
    }
  }

  .flex {
    margin-top: 20rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .text {
      margin: 0 20rpx;
    }
  }

  .slider {
    position: relative;
    height: 772rpx;
    width: 100%;
    background-color: #fff;
    border-radius: 20rpx 20rpx 0 0;
    padding-top: 44rpx;
    padding-bottom: 190rpx;
  }

  .slider-laber-bottom {
    position: absolute;
    display: flex;
    bottom: 20rpx;
    height: 86rpx;
    left: 30rpx;
    right: 30rpx;
    align-items: center;
    background-color: #fff;

    .laber-bottom {
      height: 86rpx;
      border-radius: 8rpx;
      border: none;
      font-size: $uni-font-size-default;
      line-height: 86rpx;

      &::after {
        border: 0;
      }
    }
  }

  .confirm {
    width: calc(100% - 214rpx);
    background: #308bf8;
    color: #fff;
  }

  .reset {
    width: 184rpx;
    margin-right: 30rpx;
    background: #f0f1f5;
    color: $uni-text-color;
  }



  .tab {
    padding: 0 120rpx;
    display: flex;
    justify-content: space-between;

    .tab-title {
      // width: 150rpx;
      font-size: 30rpx;
      padding-bottom: 28rpx;
      font-family: PingFang SC-Medium, PingFang SC;
      font-weight: 600;
      color: rgba(48, 49, 51, 1);
      text-align: center;
      border-bottom: 4rpx solid #333;
    }
  }

  .active {
    color: rgba(24, 144, 255, 1) !important;
    border-color: rgba(24, 144, 255, 1) !important;
  }

  .btnActive {
    background-color: rgba(24, 144, 255, 1) !important;
    color: #fff !important;
  }

  .content {
    height: 100%;
    overflow-y: auto;
    z-index: 99;

    .btn-flex {
      display: flex;
      flex-wrap: wrap;
      margin-top: 40rpx;

      .btn {
        width: 210rpx;
        height: 72rpx;
        line-height: 72rpx;
        text-align: center;
        font-size: 28rpx;
        font-family: PingFang SC-Regular, PingFang SC;
        font-weight: 400;
        color: #303133;
        background-color: rgba(240, 241, 245, 1);
        border-radius: 8rpx;
        margin-bottom: 30rpx;
        margin-right: 20rpx;
      }
    }
  }
</style>