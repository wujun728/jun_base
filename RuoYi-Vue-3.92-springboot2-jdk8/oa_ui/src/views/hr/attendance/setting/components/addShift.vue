<template>
  <div>
    <!-- 新建班次页面 -->
    <el-drawer
      :title="title"
      :visible.sync="drawer"
      size="700px"
      :wrapperClosable="type == 'check' ? true : false"
      :before-close="handleCloseFn"
    >
      <div class="box" ref="scrollRef">
        <el-form :model="formData" ref="ruleForm" :rules="rules" label-width="100px" class="demo-ruleForm">
          <el-form-item label="班次名称：" prop="name" class="form-item">
            <div class="flex" v-if="type !== 'check'">
              <el-input size="small" v-model="formData.name" placeholder="请输入班次名称" style="width: 90%"></el-input>

              <el-color-picker v-model="formData.color" class="ml14"></el-color-picker>
            </div>
            <span v-else>{{ formData.name }}</span>
          </el-form-item>
          <el-form-item label="上下班次数：" prop="number" class="form-item">
            <el-radio-group v-model="formData.number" @input="radioFn" v-if="type !== 'check'">
              <el-radio label="1">一次上下班</el-radio>
              <el-radio label="2">两次上下班</el-radio>
            </el-radio-group>
            <span v-else>{{ formData.number == 1 ? '一次上下班' : '两次上下班' }}</span>
          </el-form-item>
          <el-form-item label="工作时长：" prop="title" class="form-item">
            <span>{{ formData.work_time }}</span>
          </el-form-item>
          <el-table :data="tableData" style="width: 100%" class="mb20">
            <el-table-column prop="name" label="班次" width="140"> </el-table-column>
            <el-table-column prop="rule" label="规则">
              <template slot-scope="scope">
                <!-- 第一次上班 -->
                <div v-if="scope.row.rule == 1">
                  <div class="item">
                    上班时间 &nbsp;
                    <el-select
                      v-model="formData.number1.first_day_after"
                      disabled
                      size="small"
                      placeholder="请选择"
                      style="width: 150px"
                      v-if="type !== 'check'"
                    >
                      <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                    <el-time-picker
                      :clearable="false"
                      class="ml10"
                      size="small"
                      format="H:mm"
                      v-model="formData.number1.work_hours"
                      style="width: 150px"
                      placeholder="选择时间"
                      @change="changeTime"
                      v-if="type !== 'check'"
                    >
                    </el-time-picker>
                    <span v-else
                      >{{ formData.number1.first_day_after == 0 ? '当日' : '次日'
                      }}{{ $moment(formData.number1.work_hours).format('H:mm') }}</span
                    >
                  </div>
                  <div class="item" v-for="(item, index) in goToWork1" :key="index" v-if="type !== 'check'">
                    {{ item.premise }}

                    <div class="mo-input--number">
                      <el-input-number v-model="form[item.hour]" controls-position="right" size="small" :min="0">
                      </el-input-number>
                      <div class="define-append">小时</div>
                    </div>

                    <div class="mo-input--number">
                      <el-input-number
                        v-model="form[item.minute]"
                        controls-position="right"
                        size="small"
                        :min="0"
                      ></el-input-number>
                      <div class="define-append">分钟</div>
                    </div>
                    &nbsp;
                    {{ item.suffix }}
                  </div>
                  <div v-if="type == 'check'" class="item" v-for="(item, index) in goToWork1" :key="index">
                    {{ item.premise }} {{ form[item.hour] }} 小时{{ form[item.minute] }} 分钟{{ item.suffix }}
                  </div>
                </div>

                <!-- 第一次下班 -->
                <div v-if="scope.row.rule == 2">
                  <div class="item">
                    下班时间&nbsp;
                    <el-select
                      v-model="formData.number1.second_day_after"
                      v-if="type !== 'check'"
                      size="small"
                      placeholder="请选择"
                      style="width: 150px"
                      @change="changeTime"
                    >
                      <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                    <el-time-picker
                      :clearable="false"
                      class="ml10"
                      size="small"
                      format="H:mm"
                      v-model="formData.number1.off_hours"
                      placeholder="选择时间"
                      style="width: 150px"
                      @change="changeTime"
                      v-if="type !== 'check'"
                    >
                    </el-time-picker>
                    <span v-else
                      >{{ formData.number1.second_day_after == 0 ? '当日' : '次日'
                      }}{{ $moment(formData.number1.off_hours).format('H:mm') }}</span
                    >
                  </div>
                  <div class="item" v-for="(item, index) in goToWork2" :key="index" v-if="type !== 'check'">
                    {{ item.premise }}
                    <div class="mo-input--number">
                      <el-input-number v-model="form[item.hour]" controls-position="right" size="small" :min="0">
                      </el-input-number>
                      <div class="define-append">小时</div>
                    </div>

                    <div class="mo-input--number">
                      <el-input-number
                        v-model="form[item.minute]"
                        controls-position="right"
                        size="small"
                        :min="0"
                      ></el-input-number>
                      <div class="define-append">分钟</div>
                    </div>
                    &nbsp; {{ item.suffix }}
                  </div>
                  <el-checkbox v-model="form.free_clock" v-if="type !== 'check'">下班可免打卡</el-checkbox>

                  <div v-if="type == 'check'" class="item" v-for="(item, index) in goToWork2" :key="index">
                    {{ item.premise }} <span v-if="form[item.hour] !== 0">{{ form[item.hour] }} 小时</span>
                    <span v-if="form[item.minute] !== 0">{{ form[item.minute] }} 分钟</span>{{ item.suffix }}
                  </div>
                  <span v-if="type == 'check'">{{ form.free_clock ? '下班可免打卡' : '下班不可免打卡' }}</span>
                </div>

                <div v-if="scope.row.rule == 3">
                  <!-- 第二次上班 -->
                  <div class="item">
                    上班时间&nbsp;
                    <el-select
                      v-model="formData.number2.first_day_after"
                      v-if="type !== 'check'"
                      placeholder="请选择"
                      size="small"
                      :disabled="formData.number1.second_day_after == 1"
                      style="width: 150px"
                      @change="changeTime"
                    >
                      <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                    <el-time-picker
                      class="ml10"
                      size="small"
                      :clearable="false"
                      format="H:mm"
                      v-model="formData.number2.work_hours"
                      placeholder="选择时间"
                      @change="changeTime"
                      style="width: 150px"
                      v-if="type !== 'check'"
                    >
                    </el-time-picker>
                    <span v-else
                      >{{ formData.number2.first_day_after == 0 ? '当日' : '次日'
                      }}{{ $moment(formData.number2.work_hours).format('H:mm') }}</span
                    >
                  </div>
                  <div class="item" v-for="(item, index) in goToWork1" :key="index" v-if="type !== 'check'">
                    {{ item.premise }}
                    <div class="mo-input--number">
                      <el-input-number v-model="form1[item.hour]" controls-position="right" size="small" :min="0">
                      </el-input-number>
                      <div class="define-append">小时</div>
                    </div>

                    <div class="mo-input--number">
                      <el-input-number
                        v-model="form1[item.minute]"
                        controls-position="right"
                        size="small"
                        :min="0"
                      ></el-input-number>
                      <div class="define-append">分钟</div>
                    </div>
                    &nbsp;{{ item.suffix }}
                  </div>
                  <div v-if="type == 'check'" class="item" v-for="(item, index) in goToWork1" :key="index">
                    {{ item.premise }} {{ form1[item.hour] }} 小时{{ form1[item.minute] }} 分钟{{ item.suffix }}
                  </div>
                </div>
                <!-- 第二次下班 -->
                <div v-if="scope.row.rule == 4">
                  <div class="item">
                    下班时间 &nbsp;
                    <el-select
                      v-model="formData.number2.second_day_after"
                      v-if="type !== 'check'"
                      placeholder="请选择"
                      size="small"
                      :disabled="formData.number2.first_day_after == 1"
                      style="width: 150px"
                      @change="changeTime"
                    >
                      <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                    <el-time-picker
                      class="ml10"
                      size="small"
                      :clearable="false"
                      format="H:mm"
                      v-model="formData.number2.off_hours"
                      placeholder="选择时间"
                      style="width: 150px"
                      @change="changeTime"
                      v-if="type !== 'check'"
                    >
                    </el-time-picker>
                    <span v-else
                      >{{ formData.number2.second_day_after == 0 ? '当日' : '次日'
                      }}{{ $moment(formData.number2.off_hours).format('H:mm') }}</span
                    >
                  </div>
                  <div class="item" v-for="(item, index) in goToWork2" :key="index" v-if="type !== 'check'">
                    {{ item.premise }}
                    <div class="mo-input--number">
                      <el-input-number v-model="form1[item.hour]" controls-position="right" size="small" :min="0">
                      </el-input-number>
                      <div class="define-append">小时</div>
                    </div>

                    <div class="mo-input--number">
                      <el-input-number
                        v-model="form1[item.minute]"
                        controls-position="right"
                        size="small"
                        :min="0"
                      ></el-input-number>
                      <div class="define-append">分钟</div>
                    </div>
                    &nbsp;{{ item.suffix }}
                  </div>
                  <el-checkbox v-model="form1.free_clock" v-if="type !== 'check'">下班可免打卡</el-checkbox>
                  <div v-if="type == 'check'" class="item" v-for="(item, index) in goToWork2" :key="index">
                    {{ item.premise }} {{ form[item.hour] }} 小时{{ form[item.minute] }} 分钟{{ item.suffix }}
                  </div>
                  <span v-if="type == 'check'">{{ form1.free_clock ? '下班可免打卡' : '下班不可免打卡' }}</span>
                </div>
              </template>
            </el-table-column>
          </el-table>

          <el-form-item v-if="formData.number == 1 && type !== 'check'">
            <el-checkbox slot="label" v-model="formData.rest_time" @change="changeTime">中途休息时间：</el-checkbox>
            <el-select
              v-model="formData.rest_start_after"
              v-if="type !== 'check'"
              placeholder="请选择"
              size="small"
              class="ml20"
              :disabled="formData.number1.second_day_after == 0"
              style="width: 100px"
              @change="changeTime"
            >
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"> </el-option>
            </el-select>

            <el-time-picker
              :clearable="false"
              format="H:mm"
              size="small"
              v-model="formData.rest_start"
              placeholder="开始时间"
              style="width: 130px"
              @change="changeTime"
            >
            </el-time-picker>
            -
            <el-select
              v-model="formData.rest_end_after"
              v-if="type !== 'check'"
              size="small"
              placeholder="请选择"
              :disabled="formData.rest_start_after == 1 || formData.number1.second_day_after == 0"
              style="width: 100px"
              @change="changeTime"
            >
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"> </el-option>
            </el-select>
            <el-time-picker
              :clearable="false"
              format="H:mm"
              size="small"
              v-model="formData.rest_end"
              @change="changeTime"
              style="width: 130px"
              placeholder="结束时间"
            >
            </el-time-picker>
            <div class="tips ml25">休息时间不计入工时</div>
          </el-form-item>
          <el-form-item
            label="中途休息时间："
            v-if="formData.number == 1 && type == 'check' && formData.rest_time == 1"
          >
            <span
              >{{ $moment(formData.rest_start).format('H:mm') }}-{{ $moment(formData.rest_end).format('H:mm') }}</span
            >
            <div class="tips ml25">休息时间不计入工时</div></el-form-item
          >
          <el-form-item label="加班起算时间：" prop="title">
            <div class="item" v-if="type !== 'check'">
              最后班次下班
              <div class="mo-input--number">
                <el-input-number v-model="form.overtimeH" controls-position="right" size="small" :min="0">
                </el-input-number>
                <div class="define-append">小时</div>
              </div>
              <div class="mo-input--number">
                <el-input-number
                  v-model="form.overtimeM"
                  controls-position="right"
                  size="small"
                  :min="0"
                ></el-input-number>
                <div class="define-append">分钟</div>
              </div>
              <span class="ml10"> 后开始计算加班</span>
            </div>
            <span v-else>最后班次下班{{ form.overtimeH }}小时{{ form.overtimeM }}分钟后开始计算加班</span>
          </el-form-item>
        </el-form>
        <div class="button from-foot-btn fix btn-shadow" v-if="type !== 'check'">
          <el-button @click="handleClose(1)" size="small">取消</el-button>
          <el-button type="primary" :loading="loading" size="small" @click="submitForm">保存</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { saveAttendanceShiftApi, detailShiftListApi, putShiftListApi } from '@/api/config'
import { getInervalHour, getInervalTwoHour, getHour } from '@/libs/public'
export default {
  name: 'CrmebOaEntAddShift',

  data() {
    return {
      drawer: false,
      loading: false,
      title: '新增班次',
      options: [
        {
          value: '0',
          label: '当日'
        },
        {
          value: '1',
          label: '次日'
        }
      ],

      goToWork1: [
        // 上班
        {
          premise: '晚到超过',
          suffix: '记为迟到',
          hour: 'lateH',
          minute: 'lateM'
        },
        {
          premise: '晚到超过',
          suffix: '记为严重迟到',
          hour: 'extreme_lateH',
          minute: 'extreme_lateM'
        },
        {
          premise: '晚到超过',
          suffix: '记为半天缺卡',
          hour: 'late_lack_cardH',
          minute: 'late_lack_cardM'
        },
        {
          premise: '最早提前',
          suffix: '进行打卡',
          hour: 'early_cardH',
          minute: 'early_cardM'
        }
      ],
      goToWork2: [
        // 下班
        {
          premise: '提前',
          suffix: '打卡记为早退',
          hour: 'early_leaveH',
          minute: 'early_leaveM'
        },
        {
          premise: '提前',
          suffix: '打卡记为半天缺卡',
          hour: 'early_lack_cardH',
          minute: 'early_lack_cardM'
        },
        {
          premise: '最晚可延后',
          suffix: '进行打卡',
          hour: 'delay_cardH',
          minute: 'delay_cardM'
        }
      ],

      form1: {
        // 下班form
        lateH: 0, // 迟到小时
        lateM: 3, // 迟到分钟
        extreme_lateH: 0, // 严重迟到小时
        extreme_lateM: 30, // 严重迟到分钟
        late_lack_cardH: 2, // 缺卡小时
        late_lack_cardM: 30, // 缺卡分钟
        early_cardH: 0, // 提前打卡小时
        early_cardM: 30, // 提前打卡分钟

        early_leaveH: '0', // 早退
        early_leaveM: 30, // 早退
        early_lack_cardH: '2', // 缺卡
        early_lack_cardM: '30', // 缺卡
        delay_cardH: '0', // 延后打卡
        delay_cardM: '30', // 延后打卡
        free_clock: true // 下班免打卡
      },
      form: {
        // 上班form
        lateH: 0, // 迟到小时
        lateM: 3, // 迟到分钟
        extreme_lateH: 0, // 严重迟到小时
        extreme_lateM: 30, // 严重迟到分钟
        late_lack_cardH: 2, // 缺卡小时
        late_lack_cardM: 30, // 缺卡分钟
        early_cardH: 0, // 提前打卡小时
        early_cardM: 30, // 提前打卡分钟

        early_leaveH: '0', // 早退
        early_leaveM: 30, // 早退
        early_lack_cardH: '2', // 缺卡
        early_lack_cardM: '30', // 缺卡
        delay_cardH: '1', // 延后打卡
        delay_cardM: '29', // 延后打卡
        overtimeH: '0', // 加班时长
        overtimeM: '30', // 加班时长
        free_clock: true // 下班免打卡
      },
      type: 'add', // 判断新增 'add'/编辑 'edit'/查看 'check'
      formData: {
        // 给接口提交的数据
        color: '#1890ff',
        name: '',
        number: '1',
        rest_time: true, // 中途休息时间
        rest_start: new Date(0, 0, 0, 12, 0, 0), // 休息开始时间
        rest_end: new Date(0, 0, 0, 13, 30, 0), //  休息结束时间
        rest_start_after: '0',
        rest_end_after: '0',
        overtime: 1800, // 加班起算时间
        work_time: '0',
        number1: {
          first_day_after: '0',
          second_day_after: '0',
          work_hours: new Date(0, 0, 0, 9, 0, 0), // work_hours
          late: 600, // 迟到
          extreme_late: 1800, // 严重迟到
          late_lack_card: 3600, // 半天缺卡
          early_card: 1800, // 提前打卡

          off_hours: new Date(0, 0, 0, 18, 0, 0), // 下班时间
          early_leave: 600, // 早退
          early_lack_card: 1800, // 半天缺卡
          delay_card: 1800, // 延后打卡
          free_clock: true // 下班可免打卡
        },

        number2: {
          first_day_after: '0',
          work_hours: new Date(0, 0, 0, 14, 0, 0), // work_hours
          late: 600, // 迟到
          extreme_late: 1800, // 严重迟到
          late_lack_card: 3600, // 半天缺卡
          early_card: 1800, // 提前打卡
          second_day_after: '0',
          off_hours: new Date(0, 0, 0, 18, 0, 0), // 下班时间
          early_leave: 600, // 早退
          early_lack_card: 1800, // 半天缺卡
          delay_card: 1800, // 延后打卡
          free_clock: true // 下班可免打卡
        }
      },
      rules: {
        name: [{ required: true, message: '请输入班次名称', trigger: 'blur' }],
        number: [{ required: true, message: '请选择班次', trigger: 'change' }]
      },
      tableData: [
        {
          name: '上班1',
          rule: 1
        },

        {
          name: '下班1',
          rule: 2
        }
      ],
      id: null
    }
  },

  methods: {
    // 计算工作时长
    changeTime() {
      let duration1 = null
      if (this.formData.number == 1 && this.formData.rest_time == 1) {
        if (this.formData.number1.second_day_after == 0) {
          if (this.formData.number1.work_hours > this.formData.rest_start) {
            return this.$message.error('中途休息开始时间要大于上班时间')
          }
          if (this.formData.number1.off_hours < this.formData.rest_end) {
            return this.$message.error('中途休息结束时间要小于下班时间')
          }
        } else {
          if (this.formData.rest_start_after == 0) {
            if (this.formData.number1.work_hours > this.formData.rest_start) {
              return this.$message.error('中途休息开始时间要大于上班时间')
            }
          }

          if (this.formData.rest_end_after == 1) {
            if (this.formData.number1.off_hours < this.formData.rest_end) {
              return this.$message.error('中途休息结束时间要小于下班时间')
            }
          }
        }
        if (!this.formData.rest_time) {
          if (this.formData.number1.work_hours && this.formData.number1.off_hours) {
            if (this.formData.number1.second_day_after == 0) {
              duration1 = getInervalHour(this.formData.number1.work_hours, this.formData.number1.off_hours)
            } else {
              duration1 = getInervalHour(this.formData.number1.work_hours, this.formData.number1.off_hours)
              duration1[0] = parseInt(duration1[0] + 24)
            }
          }
        } else {
          if (this.formData.rest_start && this.formData.rest_end) {
            if (this.formData.number1.second_day_after == 0) {
              this.formData.rest_start_after = '0'
              this.formData.rest_end_after = '0'

              duration1 = getInervalTwoHour(
                this.formData.number1.work_hours,
                this.formData.number1.off_hours,
                this.formData.rest_start,
                this.formData.rest_end
              )
            } else {
              if (this.formData.rest_start_after == 1) {
                this.formData.rest_end_after = '1'
              }

              duration1 = getInervalTwoHour(
                this.formData.number1.work_hours,
                this.formData.number1.off_hours,
                this.formData.rest_start,
                this.formData.rest_end
              )
              if (this.formData.rest_start_after == 0 && this.formData.rest_end_after == 1) {
                duration1[0] = parseInt(duration1[0] + 24 - 24)
              } else {
                duration1[0] = parseInt(duration1[0] + 24)
              }
            }
          }
        }
      } else {
        if (this.formData.number1.second_day_after == 1) {
          this.formData.number2.first_day_after = '1'
        }
        if (this.formData.number2.first_day_after == 1) {
          this.formData.number2.second_day_after = '1'
        }
        if (
          this.formData.number1.second_day_after == 0 &&
          Date.parse(new Date(this.formData.number1.off_hours)) < Date.parse(new Date(this.formData.number1.work_hours))
        ) {
          return this.$message.error('下班时间要大于上班时间')
        }
        if (this.formData.number == 1) {
          duration1 = getHour(this.formData.number1.work_hours, this.formData.number1.off_hours, 0, 0)
        } else {
          duration1 = getHour(
            this.formData.number1.work_hours,
            this.formData.number1.off_hours,
            this.formData.number2.work_hours,
            this.formData.number2.off_hours
          )
        }

        if (this.formData.number1.second_day_after == 1) {
          duration1[0] = parseInt(duration1[0] + 24)
        }
        if (this.formData.number2.first_day_after == 0 && this.formData.number2.second_day_after == 1) {
          duration1[0] = parseInt(duration1[0] + 24)
        }
      }

      this.formData.work_time = duration1[0] + '小时' + duration1[1] + '分钟'
    },

    // 切换单选
    radioFn() {
      let obj = [
        {
          name: '上班2',
          rule: 3
        },

        {
          name: '下班2',
          rule: 4
        }
      ]
      if (this.formData.number == 2) {
        obj.map((item) => {
          this.tableData.push(item)
        })
        this.formData.number1.off_hours = new Date(0, 0, 0, 12, 0, 0)
      }
      if (this.formData.number == 1) {
        this.formData.number1.off_hours = new Date(0, 0, 0, 18, 0, 0)
        this.tableData = [
          { name: '上班1', rule: 1 },
          { name: '下班1', rule: 2 }
        ]
      }

      this.changeTime()
    },
    getTimestampData(timeString) {
      const [hours, minutes] = timeString.split(':')
      const timestamp = new Date(0, 0, 0, hours, minutes, 0)
      return timestamp
    },
    // 秒转化为小时和分钟
    format(num) {
      return [
        ''.repeat(2 - String(Math.floor(num / 3600)).length) + Math.floor(num / 3600),
        ''.repeat(2 - String(Math.floor((num % 3600) / 60)).length) + Math.floor((num % 3600) / 60)
      ]
    },

    // 编辑-查看回显数据
    openBox(id, type) {
      this.type = type
      if (type == 'edit') {
        this.title = '编辑班次'
      } else if (type == 'check') {
        this.title = '查看班次'
      } else {
        this.type = 'add'
        this.title = '新建班次'
        this.changeTime()
      }
      if (id) {
        this.id = id
        detailShiftListApi(id).then((res) => {
          this.formData.number = res.data.number + ''
          this.radioFn()

          this.formData.name = res.data.name
          this.formData.number1.first_day_after = res.data.number1.first_day_after + ''
          this.formData.number1.second_day_after = res.data.number1.second_day_after + ''
          this.form.free_clock = res.data.number1.free_clock == 0 ? false : true
          this.form1.free_clock = res.data.number2.free_clock == 0 ? false : true
          this.formData.number1.free_clock = this.form.free_clock
          this.formData.number2.free_clock = this.form1.free_clock
          this.formData.rest_time = res.data.rest_time == 1 ? true : false
          this.form.overtimeH = this.format(res.data.overtime)[0]
          this.form.overtimeM = this.format(res.data.overtime)[1]
          this.formData.work_time = res.data.work_time
          let { number1, number2 } = res.data

          this.formData.rest_end = this.getTimestampData(res.data.rest_end)
          this.formData.rest_start = this.getTimestampData(res.data.rest_start)
          this.formData.color = res.data.color

          // 秒转化为时分赋值
          if (number1) {
            this.formData.number1.work_hours = this.getTimestampData(number1.work_hours)
            this.formData.number1.off_hours = this.getTimestampData(number1.off_hours)
            this.form.lateH = this.format(number1.late)[0]
            this.form.lateM = this.format(number1.late)[1]
            this.form.extreme_lateH = this.format(number1.extreme_late)[0]
            this.form.extreme_lateM = this.format(number1.extreme_late)[1]
            this.form.late_lack_cardH = this.format(number1.late_lack_card)[0]
            this.form.late_lack_cardM = this.format(number1.late_lack_card)[1]
            this.form.early_cardH = this.format(number1.early_card)[0]
            this.form.early_cardM = this.format(number1.early_card)[1]
            this.form.early_leaveH = this.format(number1.early_leave)[0]
            this.form.early_leaveM = this.format(number1.early_leave)[1]
            this.form.early_lack_cardH = this.format(number1.early_lack_card)[0]
            this.form.early_lack_cardM = this.format(number1.early_lack_card)[1]
            this.form.delay_cardH = this.format(number1.delay_card)[0]
            this.form.delay_cardM = this.format(number1.delay_card)[1]
            this.form.free_clock = number1.free_clock == 0 ? false : true
          }
          if (number2.length !== 0) {
            this.formData.number2.work_hours = this.getTimestampData(number2.work_hours)
            this.formData.number2.off_hours = this.getTimestampData(number2.off_hours)
            this.form1.lateH = this.format(number2.late)[0]
            this.form1.lateM = this.format(number2.late)[1]
            this.form1.extreme_lateH = this.format(number2.extreme_late)[0]
            this.form1.extreme_lateM = this.format(number2.extreme_late)[1]
            this.form1.late_lack_cardH = this.format(number2.late_lack_card)[0]
            this.form1.late_lack_cardM = this.format(number2.late_lack_card)[1]
            this.form1.early_cardH = this.format(number2.early_card)[0]
            this.form1.early_cardM = this.format(number2.early_card)[1]
            this.form1.early_leaveH = this.format(number2.early_leave)[0]
            this.form1.early_leaveM = this.format(number2.early_leave)[1]
            this.form1.early_lack_cardH = this.format(number2.early_lack_card)[0]
            this.form1.early_lack_cardM = this.format(number2.early_lack_card)[1]
            this.form1.delay_cardH = this.format(number2.delay_card)[0]
            this.form1.delay_cardM = this.format(number2.delay_card)[1]
            this.form1.free_clock = number2.free_clock == 0 ? false : true
            this.formData.number2.first_day_after = res.data.number2.first_day_after + ''
            this.formData.number2.second_day_after = res.data.number2.second_day_after + ''
          }
        })
      }

      this.drawer = true
    },

    // 提交表单
    submitForm() {
      if (!this.formData.name) {
        return this.$message.error('班次名称不能为空')
      }
      // 计算工作时长函数
      this.changeTime()
      // 数据转化秒函数
      this.conversion()

      if (this.formData.number1.late > this.formData.number1.extreme_late) {
        return this.$message.error('严重迟到值要大于迟到')
      }
      if (this.formData.number1.extreme_late > this.formData.number1.late_lack_card) {
        return this.$message.error('半天缺卡值要大于严重迟到')
      }
      if (this.formData.number1.early_leave > this.formData.number1.early_lack_card) {
        return this.$message.error('半天缺卡要大于早退值要大于严重迟到')
      }

      if (this.formData.number == 2) {
        if (this.formData.number2.late > this.formData.number2.extreme_late) {
          return this.$message.error('严重迟到值要大于迟到')
        }
        if (this.formData.number2.extreme_late > this.formData.number2.late_lack_card) {
          return this.$message.error('半天缺卡值要大于严重迟到')
        }
        if (this.formData.number2.early_leave > this.formData.number2.early_lack_card) {
          return this.$message.error('半天缺卡要大于早退值要大于严重迟到')
        }
        this.formData.number2.work_hours = this.$moment(this.formData.number2.work_hours).format('H:mm')
        this.formData.number2.off_hours = this.$moment(this.formData.number2.off_hours).format('H:mm')
      }
      this.formData.number1.work_hours = this.$moment(this.formData.number1.work_hours).format('H:mm')
      this.formData.number1.off_hours = this.$moment(this.formData.number1.off_hours).format('H:mm')
      this.formData.rest_start = this.$moment(this.formData.rest_start).format('H:mm')
      this.formData.rest_end = this.$moment(this.formData.rest_end).format('H:mm')
      this.formData.rest_time = this.formData.rest_time ? 1 : 0
      this.formData.number1.free_clock = this.form.free_clock ? 1 : 0
      this.formData.number2.free_clock = this.form1.free_clock ? 1 : 0

      if (this.type == 'add') {
        this.loading = true
        saveAttendanceShiftApi(this.formData)
          .then((res) => {
            if (res.status == 200) {
              this.handleClose()
              this.drawer = false
              this.loading = false
              this.$emit('getList')
            } else {
              this.loading = false
              this.abbreviatedData()
            }
          })
          .catch((err) => {
            this.loading = false
            this.abbreviatedData()
          })
      } else {
        this.loading = true
        putShiftListApi(this.id, this.formData)
          .then((res) => {
            if (res.status == 200) {
              this.handleClose()
              this.drawer = false
              this.loading = false
              this.$emit('getList')
            } else {
              this.loading = false
              this.abbreviatedData()
            }
          })
          .catch((err) => {
            this.loading = false
            this.abbreviatedData()
          })
      }
    },

    // 格式化日期
    abbreviatedData() {
      this.formData.rest_end = this.getTimestampData(this.formData.rest_end)
      this.formData.rest_start = this.getTimestampData(this.formData.rest_start)
      this.formData.number1.work_hours = this.getTimestampData(this.formData.number1.work_hours)
      this.formData.number1.off_hours = this.getTimestampData(this.formData.number1.off_hours)
      if (this.formData.number == 2) {
        this.formData.number2.work_hours = this.getTimestampData(this.formData.number2.work_hours)
        this.formData.number2.off_hours = this.getTimestampData(this.formData.number2.off_hours)
      }
    },
    // 提交时分转化为秒拼接字段传给后端
    conversion() {
      this.formData.number1.late = parseInt(this.form.lateH * 3600) + parseInt(this.form.lateM * 60)
      this.formData.number1.extreme_late =
        parseInt(this.form.extreme_lateH * 3600) + parseInt(this.form.extreme_lateM * 60)
      this.formData.number1.late_lack_card =
        parseInt(this.form.late_lack_cardH * 3600) + parseInt(this.form.late_lack_cardM * 60)
      this.formData.number1.early_card = parseInt(this.form.early_cardH * 3600) + parseInt(this.form.early_cardM * 60)
      this.formData.overtime = parseInt(this.form.overtimeH * 3600) + parseInt(this.form.overtimeM * 60)
      this.formData.number1.early_leave =
        parseInt(this.form.early_leaveH * 3600) + parseInt(this.form.early_leaveM * 60)
      this.formData.number1.early_lack_card =
        parseInt(this.form.early_lack_cardH * 3600) + parseInt(this.form.early_lack_cardM * 60)
      this.formData.number1.delay_card = parseInt(this.form.delay_cardH * 3600) + parseInt(this.form.delay_cardM * 60)

      this.formData.number1.rest_time = this.formData.number1.rest_time ? '1' : '0'

      if (this.formData.number == 2) {
        this.formData.number2.late = parseInt(this.form1.lateH * 3600) + parseInt(this.form1.lateM * 60)
        this.formData.number2.extreme_late =
          parseInt(this.form1.extreme_lateH * 3600) + parseInt(this.form1.extreme_lateM * 60)
        this.formData.number2.late_lack_card =
          parseInt(this.form1.late_lack_cardH * 3600) + parseInt(this.form1.late_lack_cardM * 60)
        this.formData.number2.early_card =
          parseInt(this.form1.early_cardH * 3600) + parseInt(this.form1.early_cardM * 60)
        this.formData.number2.early_leave =
          parseInt(this.form1.early_leaveH * 3600) + parseInt(this.form1.early_leaveM * 60)
        this.formData.number2.early_lack_card =
          parseInt(this.form1.early_lack_cardH * 3600) + parseInt(this.form1.early_lack_cardM * 60)
        this.formData.number2.delay_card =
          parseInt(this.form1.delay_cardH * 3600) + parseInt(this.form1.delay_cardM * 60)
      }
    },

    handleCloseFn() {
      this.handleClose(1)
    },
    handleClose(val) {
      this.tableData = [
        {
          name: '上班1',
          rule: 1
        },

        {
          name: '下班1',
          rule: 2
        }
      ]

      this.formData.color = '#1890ff'
      this.formData.name = ''
      this.formData.number = '1'
      this.formData.rest_start = new Date(0, 0, 0, 12, 0, 0)
      this.formData.rest_end = new Date(0, 0, 0, 13, 30, 0)
      this.formData.number1.work_hours = new Date(0, 0, 0, 9, 0, 0)
      this.formData.number1.off_hours = new Date(0, 0, 0, 18, 0, 0)
      this.formData.number2.work_hours = new Date(0, 0, 0, 14, 0, 0)
      this.formData.number2.off_hours = new Date(0, 0, 0, 18, 0, 0)
      this.form1 = {
        lateH: 0,
        lateM: 3,
        extreme_lateH: 0,
        extreme_lateM: 30,
        late_lack_cardH: 2,
        late_lack_cardM: 30,
        early_cardH: 1,
        early_cardM: 0,
        early_leaveH: '0',
        early_leaveM: 30,
        early_lack_cardH: '2',
        early_lack_cardM: '30',
        delay_cardH: '0',
        delay_cardM: '30',
        free_clock: true
      }

      this.form = {
        lateH: 0,
        lateM: 3,
        extreme_lateH: 0,
        extreme_lateM: 30,
        late_lack_cardH: 2,
        late_lack_cardM: 30,
        early_cardH: 2,
        early_cardM: 0,
        early_leaveH: '0',
        early_leaveM: 30,
        early_lack_cardH: '2',
        early_lack_cardM: '30',
        delay_cardH: '0',
        delay_cardM: '30',
        overtimeH: '0',
        overtimeM: '30',
        free_clock: true
      }
      if (val == 1) {
        this.drawer = false
      }
      this.$refs.ruleForm.resetFields()
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-icon-arrow-down {
  margin-left: 0;
}
.box {
  padding: 20px;
  height: 100%;
  padding-bottom: 0;
  overflow-y: scroll;
}
.ml25 {
  margin-left: 25px;
}
.item {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
}

::v-deep.el-checkbox__label {
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
}
::v-deep.el-color-picker__trigger {
  border: none;
}
::v-deep.el-radio__label {
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
}
::v-deep .el-input--small .el-input__inner {
  height: 28px;
}

.mo-input--number {
  height: 32px;
  margin-left: 6px;
  width: 130px;
  border: 1px solid #dcdfe6;
  display: flex;
  border-radius: 4px;

  ::v-deep .el-input__inner {
    border: none !important;
  }
}
.form-item {
  margin-bottom: 5px;
}
.tips {
  font-size: 12px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #909399;
}
.define-append {
  font-size: 12px;
  width: 40px;
  display: inline-block;
  background: #f5f7fa;
  padding: 0px 3px;
  border-left: none;
  height: 32px;
  line-height: 32px;
  color: #909399;
  font-size: 12px;
  text-align: center;
}
::v-deep .el-drawer__body {
  padding-bottom: 70px;
}
</style>
