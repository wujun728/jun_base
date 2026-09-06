<template>
  <view class="schedule-container">
    <view class="schedule-header" v-if="scheduleHeaderShow">
      <view class="hour" v-for="hour in hours" :key="hour.time" :style="{ top: hour.top + 'px', color: hourColor }">
        {{ hour.time }}
      </view>
      <view v-if="newScheduleObj.show" class="hour"
        :style="{ top: newScheduleObj.top - 9 + 'px', color: newScheduleColor }">{{ newScheduleObj.startHour }}</view>
      <view v-if="newScheduleObj.show" class="hour"
        :style="{ top: newScheduleObj.bottom - 9 + 'px', color: newScheduleColor }">{{ newScheduleObj.endHour }}</view>
    </view>
    <view class="schedule-content" id="nlScheduleConentRef">
      <view class="schedule-item" hover-class="schedule-item-hover" v-for="(item, index) in scheduleList" :key="index"
        :style="{
					top: item.top + 'px',
					height: item.height + 'px',
					width: item.width + 'rpx',
					left: item.left + 'rpx',
					opacity: item.isShowOverlay ? overlayOpacity : 1,
				}" @click="handleDetail(item)">
        <view class="content" :style="{
						color: item.color,
						backgroundColor: item.backgroundColor,
						borderLeftColor: item.color,
					}">
          {{ item.title }}
        </view>
      </view>
      <view class="time-line" v-for="hour in hours" :key="hour.time" :style="{ height: Number(hourHeight) + 1 + 'px' }"
        @click="addScheduleBox"></view>
      <view v-if="showCurrentTimeLine" class="current-time-line"
        :style="{ top: currentTimeLineTop + 'px', backgroundColor: currentTimeLineColor }">
        <text class="dot" :style="{ backgroundColor: currentTimeLineColor }"></text>
      </view>

      <view v-if="newScheduleObj.show" class="add-wrap" :style="{
					color: newScheduleColor,
					borderColor: newScheduleColor,
					backgroundColor: newScheduleBgColor,
					opacity: newScheduleOpacity,
					top: `${newScheduleObj.top}px`,
					height: `${newScheduleObj.height}px`,
				}" @touchstart.stop.prevent="touchStart" @touchmove.stop.prevent="touchMove" @touchend.stop="touchEnd">
        <view class="dot-warp front" @touchstart.stop.prevent="touchUpStart" @touchmove.stop.prevent="touchUpMove">
          <view class="dot" :style="{ borderColor: newScheduleColor }"></view>
        </view>
        <text>添加日程</text>
        <view class="dot-warp back" @touchstart.stop.prevent="touchDownStart" @touchmove.stop.prevent="touchDownMove">
          <view class="dot" :style="{ borderColor: newScheduleColor }"></view>
        </view>
      </view>
    </view>
  </view>
</template>
<script>
  export default {
    components: {},
    props: {
      hourHeight: {
        type: Number,
        default: 50,
      },
      scheduleHeaderShow: {
        type: Boolean,
        default: true,
      },
      hourColor: {
        type: String,
        default: '#999',
      },
      type: {
        type: Number,
        default: '1',
      },
      list: {
        type: Array,
        default: () => [],
      },
      overlayOpacity: {
        type: Number,
        default: 0.4,
      },
      showCurrentTimeLine: {
        type: Boolean,
        default: true,
      },
      currentTimeLineColor: {
        type: String,
        default: '#ff000',
      },
      isAllowAddSchedule: {
        type: Boolean,
        default: true,
      },
      newScheduleColor: {
        type: String,
        default: '#1678ff',
      },
      newScheduleOpacity: {
        type: Number,
        default: 0.9,
      },
    },
    data() {
      return {
        currentTimeLineTop: 0,
        currentMinutes: 0,

        newScheduleObj: {
          show: false,
          top: 0,
          bottom: 0,
          height: 0,
          startHour: '',
          endHour: '',
        },
        // 拖动阀值
        dragThreshold: 10,
        defaultDuration: 15,
      }
    },
    mounted() {
      const minutes = this.getTodayMinutes()
      this.currentTimeLineTop = (minutes / 60) * this.hourHeight + Math.ceil((minutes + 1) / 60) + 9
    },
    computed: {
      scheduleList() {
        const data = this.list
          .filter(item => item.startHour && item.endHour)
          .map(item => {
            const temp = JSON.parse(JSON.stringify(item))
            // 计算开始和结束的总分钟数
            temp.startTotalMinutes = this.convertToMinutes(item.startHour)
            temp.endTotalMinutes = this.convertToMinutes(item.endHour)
            temp.duration = temp.endTotalMinutes - temp.startTotalMinutes
            temp.color = temp.color || '#1678ff'
            temp.backgroundColor = this.generateBgColor(temp.color)
            // 已过了多个小时
            temp.overHour = Math.ceil((temp.startTotalMinutes + 1) / 60)

            return temp
          })

        data.sort((a, b) => {
          return b.duration - a.duration
        })

        // 记录每个时间段的事件数量
        const timeSlots = {}
        data.forEach(item => {
          for (let minute = item.startTotalMinutes; minute < item.endTotalMinutes; minute++) {
            if (!timeSlots[minute]) {
              timeSlots[minute] = 0
            }
            timeSlots[minute]++
          }
        })
        const totalWidth = 625 / this.type
        const result = []
        data.forEach(item => {
          // 计算高度
          const height = Math.ceil((item.duration / 60) * this.hourHeight)
          // 设置一个最小高度，避免单行文字显示不全
          // item.height = Math.max(22, height)
          item.height = height
          // 计算距离顶部的距离
          item.top = (item.startTotalMinutes / 60) * this.hourHeight + item.overHour + 9

          // 计算重叠次数
          const overlapCount = Math.max(...Array.from({ length: item.duration }, (_, i) => timeSlots[item
            .startTotalMinutes + i] || 1))

          // 计算宽度
          const width = parseFloat((totalWidth / overlapCount).toFixed(2)) // 按重叠数计算宽度并保留两位小数

          // 计算左侧距离
          let count = 0

          let left = result.reduce((acc, curr) => {
            const currStart = this.convertToMinutes(curr.startHour)
            const currEnd = this.convertToMinutes(curr.endHour)

            if (item.startTotalMinutes >= currEnd || item.endTotalMinutes <= currStart || overlapCount <=
              count + 1) {
              return acc
            } else {
              count++
              return parseFloat((acc + curr.width).toFixed(2))
            }
          }, 0)

          item.width = width
          item.left = left

          // 宽度修正，若为最后一项，则占满剩余位置
          if (overlapCount === count + 1) {
            item.width = totalWidth - left
          }

          result.push(item)
        })
        return result
      },

      hours() {
        return Array.from({ length: 24 }, (_, i) => {
          return {
            time: `${String(i).padStart(2, '0')}:00`,
            top: i * this.hourHeight + i,
          }
        })
      },
      newScheduleBgColor() {
        return this.generateBgColor(this.newScheduleColor)
      },
    },
    methods: {
      convertToMinutes(date) {
        const [h, m] = date.split(':').map(Number)
        return h * 60 + m
      },
      formatTime(hour, min) {
        return `${String(hour).padStart(2, '0')}:${String(min).padStart(2, '0')}`
      },
      // 获取移动位置的分钟数
      getMinuteInput(currentY, data) {
        const clientY = currentY - data
        const top = clientY - 9 - Math.ceil((clientY + 1) / 60)
        return (top / this.hourHeight) * 60
      },
      subtractMinutes(timeStr, minutes) {
        let totalMins = this.convertToMinutes(timeStr)
        totalMins -= minutes

        const hour = Math.floor(totalMins / 60)
        const min = totalMins % 60

        return this.formatTime(hour, min)
      },
      generateBgColor(hex, percent = 0.95) {
        // 检查是否为简写模式
        if (hex.length === 4 && hex.startsWith('#')) {
          // 将简写模式扩展为完整模式
          hex = `#${hex
						.slice(1)
						.split('')
						.map(c => c + c)
						.join('')}`
        }
        // 确保输入是有效的六位十六进制颜色代码
        if (!/^#[0-9A-Fa-f]{6}$/.test(hex)) {
          throw new Error('Invalid hex color code')
        }

        // 将十六进制颜色转为RGB
        let r = parseInt(hex.slice(1, 3), 16)
        let g = parseInt(hex.slice(3, 5), 16)
        let b = parseInt(hex.slice(5, 7), 16)

        // 计算新的RGB值
        r = Math.min(255, Math.floor(r + (255 - r) * percent))
        g = Math.min(255, Math.floor(g + (255 - g) * percent))
        b = Math.min(255, Math.floor(b + (255 - b) * percent))

        // 将RGB值转换回16进制格式，并确保是两位十六进制数
        const toHex = num => {
          const hex = num.toString(16)
          return hex.length === 1 ? '0' + hex : hex
        }

        const newHex = `#${toHex(r)}${toHex(g)}${toHex(b)}`

        return newHex
      },
      handleDetail(item) {
        const temp = this.list.find(val => val.id === item.id)
        this.$emit('detail', JSON.parse(JSON.stringify(temp)))
      },
      getTodayMinutes() {
        // 获取当前时间
        const now = new Date()
        const startOfToday = new Date(now.getFullYear(), now.getMonth(), now.getDate())
        // 计算当前时间与今天00:00之间的差值（毫秒）
        const diffInMilliseconds = now - startOfToday

        // 将毫秒转换为分钟
        const minutes = Math.floor(diffInMilliseconds / (1000 * 60))
        return minutes
      },
      updateNewSchedule(start, end) {
        const startTotalMinutes = this.convertToMinutes(start)
        const endTotalMinutes = this.convertToMinutes(end)

        const duration = endTotalMinutes - startTotalMinutes

        // 已过了多个小时
        const overStartHour = Math.ceil((startTotalMinutes + 1) / 60)
        const overEndHour = Math.ceil((endTotalMinutes + 1) / 60)

        this.newScheduleObj.top = (startTotalMinutes / 60) * this.hourHeight + overStartHour + 8
        this.newScheduleObj.bottom = (endTotalMinutes / 60) * this.hourHeight + overEndHour + 8
        this.newScheduleObj.height = (duration / 60) * this.hourHeight + 1
        this.newScheduleObj.startHour = start
        this.newScheduleObj.endHour = end
      },
      addScheduleBox(event) {
        if (!this.isAllowAddSchedule) {
          return
        }
        if (this.newScheduleObj.show) {
          this.newScheduleObj.show = false
          return
        }

        this.newScheduleObj.show = true

        this.getContainerTop().then(data => {
          const clientY = event.touches[0].clientY - data
          const top = clientY - 9 - Math.ceil((clientY + 1) / 60)

          const minuteInput = (top / this.hourHeight) * 60

          const { start, end } = this.getTimeRange(minuteInput)
          this.updateNewSchedule(start, end)
        })
      },
      // 获取点击点的起始和结束时间
      getTimeRange(minuteInput, duration) {
        const segmentDuration = this.defaultDuration
        // 找到最近的15分钟刻度
        const roundedMinute = Math.round(minuteInput / segmentDuration) * segmentDuration

        let startMinute = roundedMinute - segmentDuration
        let endMinute = roundedMinute + segmentDuration

        // 拖动时处理及修正
        if (!!duration && duration > this.defaultDuration * 2) {
          const diff = (duration - this.defaultDuration * 2) / 2
          if (diff % this.defaultDuration === 0) {
            startMinute -= diff
            endMinute += diff
          } else {
            startMinute -= diff * 2
          }
        }
        // 边界的修正
        if (startMinute <= 0 || endMinute <= 30) {
          startMinute = 0
          endMinute = duration || this.defaultDuration * 2
        }
        if (endMinute >= 1440 || startMinute > 1410) {
          startMinute = 1440 - (duration || this.defaultDuration * 2)
          endMinute = 1440
        }

        const startHour = Math.floor(startMinute / 60)
        const startMin = startMinute % 60
        const endHour = Math.floor(endMinute / 60)
        const endMin = endMinute % 60

        const start = this.formatTime(startHour, startMin)
        const end = this.formatTime(endHour, endMin)
        return {
          start,
          end,
        }
      },
      getContainerTop() {
        return new Promise((resolve, reject) => {
          const query = uni.createSelectorQuery().in(this)
          query
            .select('#nlScheduleConentRef')
            .boundingClientRect(data => {
              resolve(data.top)
            })
            .exec()
        })
      },
      touchStart(event) {
        this.isDragging = false
        this.startY = event.touches[0].clientY
        this.duration = this.convertToMinutes(this.newScheduleObj.endHour) - this.convertToMinutes(this.newScheduleObj
          .startHour)
        this.flag = true
      },
      touchMove(event) {
        const currentY = event.touches[0].clientY
        const deltaY = Math.abs(currentY - this.startY)
        // 如果移动距离超过阈值，则认为是拖动
        if (deltaY > this.dragThreshold) {
          this.isDragging = true
          if (!this.flag) return
          this.flag = false

          setTimeout(() => {
            this.flag = true
            this.getContainerTop().then(data => {
              const minuteInput = this.getMinuteInput(currentY, data)
              const { start, end } = this.getTimeRange(minuteInput, this.duration)
              this.updateNewSchedule(start, end)
            })
          }, 100)
        }
      },
      touchEnd(event) {
        if (!this.isDragging) {
          // 点击
          this.newScheduleObj.show = false
          this.$emit('add', { startHour: this.newScheduleObj.startHour, endHour: this.newScheduleObj.endHour })
        }
      },

      touchUpStart(event) {
        this.isDragging = false
        this.startY = event.touches[0].clientY
        this.flag = true
      },
      touchUpMove(event) {
        const currentY = event.touches[0].clientY
        const deltaY = Math.abs(currentY - this.startY)
        // 如果移动距离超过阈值，则认为是拖动
        if (deltaY > this.dragThreshold) {
          this.isDragging = true
          if (!this.flag) return
          this.flag = false

          setTimeout(() => {
            this.flag = true
            this.getContainerTop().then(data => {
              const minuteInput = this.getMinuteInput(currentY, data)
              const { start } = this.getTimeRange(minuteInput)
              // 最小间隔为30分钟
              if (this.convertToMinutes(this.newScheduleObj.endHour) - this.convertToMinutes(start) < 30) {
                this.updateNewSchedule(this.subtractMinutes(this.newScheduleObj.endHour, 30), this
                  .newScheduleObj.endHour)
              } else {
                this.updateNewSchedule(start, this.newScheduleObj.endHour)
              }
            })
          }, 100)
        }
      },

      touchDownStart(event) {
        this.isDragging = false
        this.startY = event.touches[0].clientY
        this.flag = true
      },
      touchDownMove(event) {
        const currentY = event.touches[0].clientY
        const deltaY = Math.abs(currentY - this.startY)
        // 如果移动距离超过阈值，则认为是拖动
        if (deltaY > this.dragThreshold) {
          this.isDragging = true
          if (!this.flag) return
          this.flag = false

          setTimeout(() => {
            this.flag = true
            this.getContainerTop().then(data => {
              const minuteInput = this.getMinuteInput(currentY, data)

              const { end } = this.getTimeRange(minuteInput)
              // 最小间隔为30分钟
              if (this.convertToMinutes(end) - this.convertToMinutes(this.newScheduleObj.startHour) < 30) {
                this.updateNewSchedule(this.newScheduleObj.startHour, this.subtractMinutes(this.newScheduleObj
                  .startHour, -30))
              } else {
                this.updateNewSchedule(this.newScheduleObj.startHour, end)
              }
            })
          }, 100)
        }
      },
    },
  }
</script>
<style scoped>
  .schedule-container {
    padding-top: 10px;
    display: flex;
    background-color: #ffffff;
  }

  .schedule-header {
    width: 100rpx;
    position: relative;
  }

  .schedule-header .hour {
    position: absolute;
    width: 100rpx;
    text-align: center;
    box-sizing: border-box;
    text-align: center;
    font-size: 13px;
  }

  .schedule-content {
    padding: 9px 30rpx 20px 0px;
    position: relative;
    flex: 1;
    overflow-y: auto;
  }

  .schedule-content .schedule-item {
    box-sizing: border-box;
    position: absolute;
    overflow: hidden;
    word-break: break-all;
    padding: 1px;
    background-color: #fff;
    border-radius: 4px;
  }

  .schedule-content .schedule-item .content {
    box-sizing: border-box;
    border-left: 3px solid;
    font-size: 13px;
    border-radius: 4px;
    height: 100%;
    padding: 0px 3px;
  }

  .schedule-item-hover {
    background-color: red;
  }

  .schedule-content .time-line {
    box-sizing: border-box;
    border-top: 1px solid #f0f0f0;
  }

  .schedule-content .add-wrap {
    display: flex;
    align-items: center;
    justify-content: center;
    position: absolute;
    z-index: 996;
    width: 620rpx;
    height: 50rpx;
    top: 10px;
    border: 1px solid;
    border-radius: 12rpx;
    font-size: 26rpx;
    opacity: 0.9;
  }

  .add-wrap .dot-warp {
    width: 70px;
    height: 70px;
    position: absolute;
    z-index: 999;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .add-wrap .dot-warp .dot {
    width: 7px;
    height: 7px;
    border-radius: 50%;
    position: absolute;
    background-color: #fff;
    border: 2px solid;
    z-index: 999;
  }

  .add-wrap .front {
    top: -35px;
    right: 0;
  }

  .add-wrap .back {
    bottom: -36px;
    left: 0;
  }

  .front .dot {
    margin-right: 25px;
  }

  .back .dot {
    margin-left: 25px;
  }

  .current-time-line {
    height: 1px;
    background-color: red;
    width: 604rpx;
    position: absolute;
    left: 16rpx;
    z-index: 5;
  }

  .current-time-line .dot {
    display: block;
    box-sizing: content-box;
    width: 7px;
    height: 7px;
    background-color: red;
    border-radius: 50%;
    position: absolute;
    top: -3px;
    left: -16rpx;
  }
</style>