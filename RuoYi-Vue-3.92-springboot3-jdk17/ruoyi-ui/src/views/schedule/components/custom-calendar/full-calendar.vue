<template>
  <div class="comp-full-calendar">
    <fc-body
      :current-date="currentDate"
      :eventList="eventList"
      :month-names="monthNames"
      :week-names="weekNames"
      :first-day="firstDay"
      :scheduleData="scheduleData"
      @eventclick="emitEventClick"
      @dayclick="emitDayClick"
    >
      <div slot="body-card">
        <slot name="fc-body-card"></slot>
      </div>
    </fc-body>
  </div>
</template>
<script type="text/babel">
import langSets from "./data-map/lang-sets";
import body from "./body.vue";
export default {
  props: {
    currentDate: {},
    eventList: {
      type: Array,
      default: () => [],
    },
    lang: {
      type: String,
      default: "en",
    },
    firstDay: {
      type: Number | String,
      validator(val) {
        let res = parseInt(val);
        return res >= 0 && res <= 6;
      },
      default: 0,
    },
    titleFormat: {
      type: String,
      default() {
        return langSets[this.lang].titleFormat;
      },
    },
    scheduleData: {
      type: Object,
      default: () => {},
    },
    monthNames: {
      type: Array,
      default() {
        return langSets[this.lang].monthNames;
      },
    },
    weekNames: {
      type: Array,
      default() {
        let arr = langSets[this.lang].weekNames;
        return arr.slice(this.firstDay).concat(arr.slice(0, this.firstDay));
      },
    },
  },
  data() {
    return {};
  },
  methods: {
    emitChangeMonth(start, end, currentStart, current) {
      this.$emit("changeMonth", start, end, currentStart, current);
    },
    emitEventClick(date, event, jsEvent, pos) {
      this.$emit("eventClick", date, event, jsEvent, pos);
    },
    emitDayClick(day, jsEvent) {
      this.$emit("dayClick", day, jsEvent);
    },
  },
  components: {
    "fc-body": body,
  },
};
</script>
<style lang="scss">
.comp-full-calendar {
  background: #fff;
  width: 100%;
  ul,
  p {
    margin: 0;
    padding: 0;
    font-size: 14px;
  }
}
</style>
