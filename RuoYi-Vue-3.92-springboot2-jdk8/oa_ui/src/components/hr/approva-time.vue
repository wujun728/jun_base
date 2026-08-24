<template>
  <div class="timeFrom">
    <!-- 请假表单 -->
    <div class="el-form-item el-form-item--mini">
      <label class="el-form--label-top el-form-item__label" style="width: 120px">
        <span v-if="formCreateInject.rule.effect.required" class="color-tab">*</span>
        <span>开始时间</span>
      </label>
      <div class="el-form-item__content">
        <el-date-picker
          v-model="timeData.dateStart"
          :type="timeData.timeType === 'day' ? 'date' : 'datetime'"
          placeholder="选择日期"
          :format="timeData.timeType === 'day' ? 'yyyy/MM/dd' : 'yyyy/MM/dd HH:mm:ss'"
          :value-format="timeData.timeType === 'day' ? 'yyyy/MM/dd' : 'yyyy/MM/dd HH:mm:ss'"
          :clearable="false"
          @change="onchangeTimeStart"
        ></el-date-picker>

        <el-select
          v-if="timeData.timeType === 'day'"
          v-model="timeData.timeStart"
          placeholder="请选择"
          @blur="onChange"
          @change="onchangeSel(1)"
        >
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </div>
    </div>
    <div class="el-form-item el-form-item--mini">
      <label class="el-form-item__label" style="width: 120px">
        <span v-if="formCreateInject.rule.effect.required" class="color-tab">*</span>
        <span>结束时间</span>
      </label>
      <div class="el-form-item__content">
        <el-date-picker
          v-model="timeData.dateEnd"
          :type="timeData.timeType === 'day' ? 'date' : 'datetime'"
          placeholder="选择日期"
          :format="timeData.timeType === 'day' ? 'yyyy/MM/dd' : 'yyyy/MM/dd HH:mm:ss'"
          :value-format="timeData.timeType === 'day' ? 'yyyy/MM/dd' : 'yyyy/MM/dd HH:mm:ss'"
          :clearable="false"
          @change="onchangeTimeEnd"
        ></el-date-picker>
        <el-select
          v-if="timeData.timeType === 'day'"
          v-model="timeData.timeEnd"
          @blur="onChange"
          placeholder="请选择"
          @change="onchangeSel(2)"
        >
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </div>
    </div>
    <div class="el-form-item el-form-item--mini">
      <label class="el-form-item__label" style="width: 120px">
        <span v-if="formCreateInject.rule.effect.required" class="color-tab">*</span>
        <span>{{ title }}</span>
      </label>
      <div class="el-form-item__content">
        <el-input v-model="timeData.duration" style="width: 220px" @change="changeDuration" placeholder="请输入时长">
          <span slot="suffix" class="el-input__icon">{{ timeData.timeType === 'day' ? '天' : '小时' }}</span>
        </el-input>
      </div>
    </div>
  </div>
</template>

<script>
import { divTime } from '@/utils'
export default {
  name: 'Index',
  props: {
    formCreateInject: Object,
    value: {
      type: Object,
      default: () => ({})
    },
    disabled: Boolean,
    timeType: {
      type: String,
      default: () => 'day'
    },
    time: {
      type: String,
      default: () => ''
    },
    titleIpt: {
      type: String,
      default: () => '时长'
    }
  },
  data() {
    return {
      timeNum: this.time,
      options: [
        {
          value: '1',
          label: '上午'
        },
        {
          value: '0',
          label: '下午'
        }
      ],
      timeData: {
        dateStart: this.value.dateStart,
        timeStart: this.value.timeStart,
        dateEnd: this.value.dateEnd,
        timeEnd: this.value.timeEnd,
        duration: this.value.duration,
        timeType: this.timeType
      },
      num: 0,
      title: this.titleIpt
    }
  },
  watch: {
    value(n) {
      this.timeData = n
    },
    timeType(n) {
      this.timeData.timeType = n
    },
    time(n) {
      this.timeNum = n
    },
    titleIpt(n) {
      this.title = n
    }
  },
  methods: {
    onChange() {
      const time1 = Date.parse(new Date(this.timeData.dateStart))
      const time2 = Date.parse(new Date(this.timeData.dateEnd))
      if (time1 > time2) {
        return this.$message.error('结束时间不能小于开始时间')
      }
      if (this.timeData.timeType === 'day') {
        if (time2 == time1) {
          setTimeout(() => {
            if (this.timeData.timeStart === '0' && this.timeData.timeEnd === '1') {
              return this.$message.error('结束时间不能小于开始时间')
            }
          }, 200)
        }
        this.num = divTime(this.timeData.dateStart, this.timeData.dateEnd, 'day')
        if (time2 > time1) this.count()
        if (time2 === time1) this.count()
      } else {
        if (time2 > time1) this.timeData.duration = divTime(this.timeData.dateStart, this.timeData.dateEnd, 'time')
      }
    },
    count() {
      if (this.timeData.timeStart && this.timeData.timeEnd) {
        const len = parseFloat(this.timeData.timeStart) - parseFloat(this.timeData.timeEnd)
        if (len === 1) {
          this.timeData.duration = parseFloat(this.num) + 1
        } else if (len === 0) {
          this.timeData.duration = 0.5 + parseFloat(this.num)
        } else if (len === -1) {
          this.timeData.duration = parseFloat(this.num)
        }
        this.$emit('input', this.timeData)
      }
    },
    onchangeTimeEnd(n) {
      this.timeData.dateEnd = n
      this.timeData.timeEnd = '1'
      this.onChange()
      this.$emit('input', this.timeData)
    },
    onchangeTimeStart(n) {
      this.timeData.dateStart = n
      this.timeData.timeStart = '1'
      this.onChange()
      this.$emit('input', this.timeData)
    },
    changeDuration() {
      this.$emit('input', this.timeData)
    },
    onchangeSel(n) {
      this.count()
    }
  }
}
</script>

<style scoped lang="scss">
.el-form--label-top .el-form-item__label {
  float: none;
  display: inline-block;
  text-align: left;
  padding: 0 0 10px 0;
}
.timeFrom {
  padding-top: 10px;
}
.timeFrom {
  ::v-deep .el-date-editor,
  ::v-deep .el-date-editor--date,
  ::v-deep .el-select {
    width: 220px;
  }
}
.timeFrom + ::v-deep .el-form-item__error {
  margin-left: 120px;
  position: absolute;
  top: 98%;
  left: 0;
}
</style>
