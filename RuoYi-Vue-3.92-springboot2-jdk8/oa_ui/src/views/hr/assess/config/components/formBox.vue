<template>
  <div>
    <el-form :inline="true">
      <el-form-item label="考核时间:" label-width="80px" style="display: flex; align-items: center">
        <el-radio-group v-model="tableFrom.time" size="small" @change="selectChange">
          <el-radio-button v-for="(itemn, indexn) in fromList" :key="indexn" :label="itemn.val"
            >{{ itemn.text }}
          </el-radio-button>
        </el-radio-group>
        <el-date-picker
          v-model="timeVal"
          type="daterange"
          :placeholder="$t('toptable.selecttime')"
          format="yyyy/MM/dd"
          value-format="yyyy/MM/dd"
          :range-separator="$t('toptable.to')"
          :start-placeholder="$t('toptable.startdate')"
          :end-placeholder="$t('toptable.endingdate')"
          style="position: relative; top: 3px"
          @change="onchangeTime"
        />
      </el-form-item>
      <div>
        <el-row :gutter="20">
          <el-col :lg="24" :xl="24">
            <el-col :lg="7" :xl="8" style="margin-left: -10px">
              <el-form-item label="部门:" label-width="80px" class="select-bar">
                <el-select
                  v-model="tableFrom.type"
                  :placeholder="$t('finance.pleaseselect')"
                  style="width: 100%"
                  @change="handType"
                >
                  <el-option v-for="item in sexOptions" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :lg="7" :xl="8">
              <el-form-item label="考核状态:" label-width="80px" class="select-bar">
                <el-select
                  v-model="tableFrom.status"
                  :placeholder="$t('finance.pleaseselect')"
                  style="flex: 1; width: 100%"
                  @change="handStatus"
                >
                  <el-option
                    v-for="(item, index) in statusOptions"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :lg="7" :xl="8">
              <el-form-item label="考核类型:" label-width="80px" class="select-bar">
                <el-select
                  v-model="tableFrom.status"
                  :placeholder="$t('finance.pleaseselect')"
                  style="flex: 1; width: 100%"
                  @change="handStatus"
                >
                  <el-option
                    v-for="(item, index) in statusOptions"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-col>
        </el-row>
      </div>
      <div>
        <el-row :gutter="20">
          <el-col :lg="24" :xl="24">
            <el-col :lg="7" :xl="8" style="margin-left: -10px">
              <el-form-item label="被考核人:" label-width="80px" class="select-bar">
                <el-select
                  v-model="tableFrom.type"
                  :placeholder="$t('finance.pleaseselect')"
                  style="width: 100%"
                  @change="handType"
                >
                  <el-option v-for="item in sexOptions" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :lg="7" :xl="8">
              <el-form-item label="考核人:" label-width="80px" class="select-bar">
                <el-select
                  v-model="tableFrom.status"
                  :placeholder="$t('finance.pleaseselect')"
                  style="flex: 1; width: 100%"
                  @change="handStatus"
                >
                  <el-option
                    v-for="(item, index) in statusOptions"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-button class="btns one" type="primary" icon="el-icon-search" @click="handleConfirm">{{
              $t('public.search')
            }}</el-button>
            <el-button class="btns" @click="reset">{{ $t('public.reset') }}</el-button>
          </el-col>
        </el-row>
      </div>
    </el-form>
  </div>
</template>

<script>
import { billCateApi } from '@/api/enterprise';
export default {
  name: 'FormBox',
  data() {
    return {
      fromList: [
        { text: this.$t('toptable.all'), val: '' },
        { text: this.$t('toptable.today'), val: 'today' },
        { text: this.$t('toptable.yesterday'), val: 'yesterday' },
        { text: this.$t('toptable.day7'), val: 'lately7' },
        { text: this.$t('toptable.day30'), val: 'lately30' },
        { text: this.$t('toptable.thismonth'), val: 'month' },
        { text: this.$t('toptable.thisyear'), val: 'year' },
      ],
      timeVal: [],
      tableFrom: {
        time: '',
        type: '',
        status: '',
      },
      sexOptions: [
        {
          label: this.$t('finance.all'),
          value: '',
        },
        {
          label: this.$t('finance.income'),
          value: '0',
        },
        {
          label: this.$t('finance.pay'),
          value: '1',
        },
      ],
      statusOptions: [{ label: this.$t('toptable.all'), value: '' }],
    };
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang;
    },
  },
  watch: {
    lang() {
      this.setOptions();
    },
  },
  methods: {
    setOptions() {
      this.fromList = [
        { text: this.$t('toptable.all'), val: '' },
        { text: this.$t('toptable.today'), val: 'today' },
        { text: this.$t('toptable.yesterday'), val: 'yesterday' },
        { text: this.$t('toptable.day7'), val: 'lately7' },
        { text: this.$t('toptable.day30'), val: 'lately30' },
        { text: this.$t('toptable.thismonth'), val: 'month' },
        { text: this.$t('toptable.thisyear'), val: 'year' },
      ];
      this.sexOptions = [
        {
          label: this.$t('finance.all'),
          value: '',
        },
        {
          label: this.$t('finance.income'),
          value: '0',
        },
        {
          label: this.$t('finance.pay'),
          value: '1',
        },
      ];
      this.statusOptions = [
        {
          label: this.$t('toptable.all'),
          value: '',
        },
      ];
    },
    // 时间段选择
    selectChange() {
      this.timeVal = '';
      this.confirmData();
    },
    // 重置
    reset() {
      this.timeVal = [];
      this.tableFrom = {
        time: '',
        type: '',
        status: '',
      };
      this.statusOptions = [{ label: this.$t('toptable.all'), value: '' }];
      this.$emit('confirmData', this.tableFrom);
    },
    changeTimeDate(days) {
      if (days == '') return '';
      if (days.indexOf('-') > 0) return days;
      const end = new Date();
      const start = new Date();
      switch (days) {
        case 'today':
          start.setDate(start.getDate());
          break;
        case 'yesterday':
          start.setDate(start.getDate() - 1);
          break;
        case 'lately7':
          start.setDate(start.getDate() - 7);
          break;
        case 'lately30':
          start.setDate(start.getDate() - 30);
          break;
        case 'month':
          start.setDate(start.getDate() - (start.getDate() - 1));
          break;
        case 'year':
          start.setDate(start.getDate() - 365);
          break;
        default:
      }
      const endYear = end.getFullYear();
      const endMonth = this.getAutoZero(end.getMonth() + 1);
      const endDate = this.getAutoZero(end.getDate());
      const startYear = start.getFullYear();
      const startMonth = this.getAutoZero(start.getMonth() + 1);
      const startDate = this.getAutoZero(start.getDate());
      return startYear + '/' + startMonth + '/' + startDate + '-' + endYear + '/' + endMonth + '/' + endDate;
    },
    getAutoZero(number) {
      return number < 9 ? '0' + number : number;
    },
    // 获取收入类型
    async getBillCate(type) {
      const data = {
        types: type,
      };
      const list = [{ label: this.$t('toptable.all'), value: '' }];
      const result = await billCateApi(data)
        if (result.data.length > 0) {
          result.data.forEach((val) => {
            list.push({ label: val.name, value: val.id });
            if (val.children.length > 0) {
              val.children.forEach((vall) => {
                list.push({ label: vall.name, value: vall.id });
              });
            }
          });
        }
      return list;
    },
    // 选择时间区域
    onchangeTime(e) {
      if (e == null) {
        this.tableFrom.time = '';
        this.confirmData();
      } else {
        this.timeVal = e;
        this.tableFrom.time = this.timeVal[0] + '-' + this.timeVal[1];
        this.confirmData();
      }
    },
    // 确认
    handleConfirm() {
      this.confirmData();
    },
    // 搜索记账类型
    handType(e) {
      this.tableFrom.type = e;
      this.tableFrom.status = '';
      this.statusOptions = this.getBillCate(e);
      this.confirmData();
    },
    // 类型
    handStatus(e) {
      this.tableFrom.status = e;
      this.confirmData();
    },
    confirmData() {
      this.$emit('confirmData', this.tableFrom);
    },
  },
};
</script>

<style lang="scss" scoped>
.select-bar {
  display: flex;
}
::v-deep.el-form-item__content {
  flex: 1;
}
</style>
