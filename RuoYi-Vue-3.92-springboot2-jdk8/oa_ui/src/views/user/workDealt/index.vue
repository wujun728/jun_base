<!-- @FileDescription: 待办任务页面 -->
<template>
  <div>
    <div class="dealt-content">
      <div class="mb15">
        <el-button class="add-task" @click="addTask">
          <i class="el-icon-plus" />
          {{ $t('calendar.addtask') }}
          <i class="el-icon-bell icon-task-dell" />
        </el-button>
      </div>
      <div v-if="dayArray.length > 0">
        <ul class="task-list">
          <li v-for="item in dayArray" v-show="item.finish === 0" :key="item.id" @click="handleItemClick(item)">
            <div class="task-list-left">
              <i
                class="pointer"
                :class="item.finish === 1 ? 'el-icon-success' : 'el-icon-garden'"
                @click.stop="setScheduleRecord(item)"
              />
            </div>
            <div class="task-list-right">
              <p>{{ item.content }}</p>
              <p>
                <span>{{ $t('customer.reminderdata') }}:</span>
                <span>{{ item.remind_day }} {{ item.remind_time }}</span>
              </p>
            </div>
          </li>
        </ul>
        <div class="task-text">
          <span class="pointer" @click="addCompleteTask">
            {{ complete === true ? '隐藏' : '显示' }}已完成事项
            <i :class="complete === true ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
          </span>
        </div>
        <el-collapse-transition>
          <ul class="task-list" v-show="complete">
            <li v-for="item in dayArray" v-show="item.finish === 1" :key="item.id" @click="handleItemClick(item)">
              <div class="task-list-left">
                <i
                  :class="item.finish === 1 ? 'el-icon-success' : 'el-icon-garden'"
                  @click.stop="setScheduleRecord(item)"
                />
              </div>
              <div class="task-list-right">
                <p>{{ item.content }}</p>
                <p>
                  <span>{{ $t('customer.reminderdata') }}:</span>
                  <span>{{ item.remind_day }} {{ item.remind_time }}</span>
                </p>
              </div>
            </li>
          </ul>
        </el-collapse-transition>
      </div>
      <default-page v-else v-height :min-height="510" :index="8" />
    </div>
    <!-- 通用弹窗表单   -->
    <dialog-form ref="dialogForm" :form-data="formBoxConfig" @isOk="handleList" />
    <check-dialog ref="repeatDialog" :repeat-data="repeatData" @handleRepeatData="handleRepeatData" />
  </div>
</template>

<script>
import dialogForm from './components/index'
import calendarBar from '@/views/user/calendar/components/calendarBar'
import checkDialog from '@/views/user/calendar/components/checkDialog'
import defaultPage from '@/components/common/defaultPage'
import { dealtScheduleListApi, dealtScheduleRecordApi } from '@/api/user'
export default {
  name: 'WorkDealt',
  components: {
    calendarBar,
    dialogForm,
    checkDialog,
    defaultPage
  },
  data() {
    return {
      formBoxConfig: {},
      rolesConfig: [],
      dayArray: [],
      repeatData: {},
      itemArray: [],
      types: [],
      time: this.$moment(this.time).format('yyyy-MM-DD'),
      dateTime: null,
      complete: true
    }
  },

  methods: {
    handleDate(data) {
      this.time = data.time
      const t = this.$moment(new Date()).format('HH:mm:ss')
      this.dateTime = data.time + ' ' + t
      this.types = data.type
      this.getList()
    },
    addTask() {
      this.formBoxConfig = {
        title: this.$t('calendar.addtask'),
        edit: false,
        data: {
          types: 'personal'
        },
        width: '500px',
        date: this.dateTime
      }
      this.$refs.dialogForm.openBox()
    },
    // 列表
    getList() {
      const data = {
        types: this.types,
        time: this.time,
        period: 0
      }
      dealtScheduleListApi(data).then((res) => {
        this.dayArray = res.data[0].list
      })
    },
    setScheduleRecord(item) {
      const data = {
        time: this.time,
        status: item.finish === 0 ? 1 : 0
      }
      this.scheduleRecord(item.id, data)
    },
    // 提醒状态
    scheduleRecord(id, data) {
      dealtScheduleRecordApi(id, data).then((res) => {
        this.handleList()
      })
    },
    handleRepeatData(data) {
      if (data.type === 1) {
        this.handleList()
      } else {
        this.formBoxConfig = {
          title: this.$t('calendar.edittask'),
          edit: true,
          width: '500px',
          data: this.itemArray
        }
        this.$refs.dialogForm.openBox()
      }
    },
    handleList() {
      this.getList()
      this.$emit('handDealt')
    },
    handleItemClick(item) {
      this.repeatData = {
        title: this.$t('calendar.checktask'),
        width: '480px',
        data: item
      }
      this.itemArray = item
      this.$refs.repeatDialog.handleOpen()
    },
    addCompleteTask() {
      this.complete = this.complete !== true
    }
  }
}
</script>

<style lang="scss" scoped>
.dealt-content {
  .add-task {
    width: 100%;
    text-align: left;
    .icon-task-dell {
      float: right;
    }
  }
  .task-list {
    list-style: none;
    padding: 0;
    margin: 0;
    li {
      display: flex;
      margin-bottom: 15px;
    }
    li:last-of-type {
      margin-bottom: 0;
    }
    .task-list-left {
      width: 35px;
      i {
        color: #1890ff;
        font-size: 20px;
      }
      .el-icon-garden {
        width: 17px;
        height: 17px;
        border-radius: 50%;
        border: 1px solid #000000;
      }
    }
    .task-list-right {
      width: calc(100% - 35px);
      p {
        margin: 0;
      }
      padding-bottom: 10px;
      border-bottom: 1px solid #eeeeee;
      p:first-of-type {
        font-size: 15px;
        color: #000000;
        margin-bottom: 10px;
      }
      p:nth-of-type(2) {
        color: #999999;
        font-size: 13px;
      }
    }
  }
  .task-text {
    margin: 20px 0;
    font-size: 13px;
    text-align: center;
  }
}
</style>
