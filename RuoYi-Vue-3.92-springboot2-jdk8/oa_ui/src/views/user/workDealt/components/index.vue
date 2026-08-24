<template>
  <div class="station">
    <el-drawer
      :title="formData.title"
      :visible.sync="drawer"
      :direction="direction"
      :modal="true"
      :wrapper-closable="true"
      :before-close="handleClose"
      size="650px"
    >
      <div class="content">
        <el-form ref="form" :model="rules" :rules="rule" label-width="100px">
          <el-form-item prop="content" :label="$t('calendar.todocontent') + ':'">
            <el-input
              v-model="rules.content"
              size="small"
              class="input-item"
              :maxlength="50"
              :placeholder="$t('calendar.placeholder09')"
            />
          </el-form-item>
          <el-form-item :label="$t('calendar.remarks') + ':'">
            <el-input
              v-model="rules.marks"
              type="textarea"
              :rows="2"
              :maxlength="200"
              class="textarea-item"
              :placeholder="$t('finance.pleaseremark')"
            />
          </el-form-item>
          <el-form-item prop="time" :label="$t('customer.reminderdata') + ':'">
            <el-date-picker
              v-model="rules.time"
              size="small"
              type="datetime"
              prefix-icon="el-icon-date"
              :picker-options="pickerOptions"
              :placeholder="$t('calendar.placeholder11')"
            />
          </el-form-item>
          <el-form-item :label="$t('calendar.repeatreminder') + ':'">
            <el-select
              v-model="rules.repeat"
              size="small"
              :placeholder="$t('calendar.placeholder10')"
              @change="handleRepeat"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                @click.native="rulesOptionChange(item.value)"
              />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div class="button from-foot-btn fix btn-shadow">
        <el-button size="small" @click="drawer = false">{{ $t('public.cancel') }}</el-button>
        <el-button size="small" type="primary" :loading="loading" @click="handleConfirm('ruleForm')">
          {{ $t('public.ok') }}
        </el-button>
      </div>
    </el-drawer>
    <repeat-dialog ref="repeatDialog" :repeat-data="repeatData" @handleRepeatData="handleRepeatData" />
  </div>
</template>

<script>
import repeatDialog from './repeatDialog';
import { dealtScheduleAddApi, dealtScheduleEditApi } from '@/api/user';
import { clientFollowEditApi } from '@/api/enterprise';
export default {
  name: 'Index',
  components: {
    repeatDialog,
  },
  props: {
    formData: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      drawer: false,
      direction: 'rtl',
      rules: {
        time: '',
        marks: '',
        content: '',
        repeat: -1,
      },
      rule: {
        content: [{ required: true, message: this.$t('calendar.placeholder12'), trigger: 'blur' }],
        time: [{ required: true, message: this.$t('calendar.placeholder13'), trigger: 'change' }],
      },
      options: [
        { value: -1, label: this.$t('calendar.norepetition') },
        { value: 0, label: this.$t('calendar.repeatbyday') },
        { value: 1, label: this.$t('calendar.repeatweekly') },
        { value: 2, label: this.$t('calendar.repeatmonthly') },
        { value: 3, label: this.$t('calendar.repeatyear') },
        { value: 4, label: this.$t('calendar.custom') },
      ],
      repeatData: {},
      repeatDialogData: {},
      width: '520px',
      pickerOptions: {
        firstDayOfWeek: 1,
      },
      loading: false,
    };
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang;
    },
  },
  watch: {
    formData: {
      handler(nVal) {
        if (this.formData.edit) {
          this.rules.content = this.formData.data.content;
          this.rules.marks = this.formData.data.mark;
          this.rules.time = this.formData.data.remind_day + ' ' + this.formData.data.remind_time;
          this.rules.repeat = this.formData.data.repeat;
        } else {
          this.rules.time = this.formData.date;
        }
      },
      deep: true,
    },
    lang() {
      this.setOptions();
    },
  },
  methods: {
    setOptions() {
      this.options = [
        { value: -1, label: this.$t('calendar.norepetition') },
        { value: 0, label: this.$t('calendar.repeatbyday') },
        { value: 1, label: this.$t('calendar.repeatweekly') },
        { value: 2, label: this.$t('calendar.repeatmonthly') },
        { value: 3, label: this.$t('calendar.repeatyear') },
        { value: 4, label: this.$t('calendar.custom') },
      ];
    },
    handleClose() {
      this.drawer = false;
      // 取消编辑
      if (this.formData.edit) {
        this.rules.content = '';
        this.rules.marks = '';
        this.rules.time = '';
        this.rules.repeat = '';
      }
    },
    openBox() {
      this.drawer = true;
    },
    handleRepeat(e) {},
    rulesOptionChange(e) {
      if (e === 4) {
        this.repeatData = {
          title: this.$t('calendar.customrepeat'),
          width: this.width,
        };
        if (this.formData.edit) {
          this.repeatData.data = this.formData.data;
          this.repeatData.edit = true;
        } else {
          this.repeatData.edit = false;
        }
        this.$refs.repeatDialog.dialogVisible = true;
      }
    },
    // 提交
    handleConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          var data = {};

          if (this.formData.data.types === 'client_track') {
            data = {
              eid: this.formData.data.id,
              types: 0,
              time: this.$moment(this.rules.time).format('YYYY-MM-DD HH:mm:ss'),
              content: this.rules.content,
            };
          } else {
            data = {
              content: this.rules.content,
              mark: this.rules.marks,
              remind: this.$moment(this.rules.time).format('YYYY-MM-DD HH:mm:ss'),
              repeat: this.rules.repeat,
              types: this.formData.data.types,
            };

            if (this.rules.repeat === 4) {
              data.period = this.repeatDialogData.repeat;
              data.rate = this.repeatDialogData.rate;
              if (this.repeatDialogData.repeat === 1) {
                data.days = this.repeatDialogData.weekDays;
              } else if (this.repeatDialogData.repeat === 2) {
                data.days = this.repeatDialogData.monthDays;
              } else {
                data.days = [];
              }
              data.end_time = this.repeatDialogData.end_time;
            }
          }
          // 编辑待办
          if (this.formData.edit) {
            this.getScheduleEdit(this.formData.data.id, data);
          } else if (this.formData.edit && this.formData.data.types === 'client_track') {
            this.clientFollowEdit(this.formData.data.id, data);
          } else {
            this.getScheduleAdd(data);
          }
        }
      });
    },
    repeatReset() {
      this.$refs.repeatDialog.rules.repeat = 0;
      this.$refs.repeatDialog.rules.rate = 1;
      this.$refs.repeatDialog.rules.end_time = '';
      this.$refs.repeatDialog.rules.weekDays = [];
      this.$refs.repeatDialog.rules.monthDays = [];
      this.$refs.repeatDialog.rules.time = '';
    },
    // 添加待办
    getScheduleAdd(data) {
      this.loading = true;
      dealtScheduleAddApi(data)
        .then((res) => {
          this.rules.time = '';
          this.rules.marks = '';
          this.rules.content = '';
          this.rules.repeat = -1;
          this.drawer = false;
          this.repeatReset();
          this.$emit('isOk', 1);
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    // 编辑待办
    getScheduleEdit(id, data) {
      this.loading = true;
      dealtScheduleEditApi(id, data)
        .then((res) => {
          this.rules.time = '';
          this.rules.marks = '';
          this.rules.content = '';
          this.rules.repeat = -1;
          this.drawer = false;
          this.$emit('isOk', 2);
          this.loading = false;
          this.repeatReset();
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    handleRepeatData(data) {
      if (data.close) {
      } else {
        this.repeatDialogData = data;
      }
    },
    // 跟进记录--修改客户提醒
    clientFollowEdit(id, data) {
      this.loading = true;
      clientFollowEditApi(id, data)
        .then((res) => {
          this.rules.time = '';
          this.rules.marks = '';
          this.rules.content = '';
          this.rules.repeat = -1;
          this.drawer = false;
          this.$emit('isOk', 2);
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.content {
  padding: 20px 15px 0 10px;
  ::v-deep .el-textarea__inner {
    resize: none;
  }
  ::v-deep .el-select,
  ::v-deep .el-input {
    width: 100%;
  }
}
.from-foot-btn button {
  width: auto;
  height: auto;
}
</style>
