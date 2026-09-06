<template>
  <div class="app-container home" style="text-align: center; font-weight: 700; color: black">
    <h1>欢迎进入企业信息管理系统</h1>
    <el-calendar v-model="value"> </el-calendar>
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      value: new Date(),
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
  },
};
</script>

<style>
.Calendarselect {
  top: 61px;
  position: relative;
  text-align: right;
  padding-right: 16px;
}

/* 日历表格今天样式 */
.el-backtop,
.el-calendar-table td.is-today {
  color: #7000bd;
}

/* 除去日历表格的padding */
.el-calendar-table .el-calendar-day {
  height: 40px;
  line-height: 40px;
  padding: 0;
}

/* 日历表格鼠标滑动样式 */
.el-calendar-table .el-calendar-day:hover {
  background-color: #f26c08 !important;
  color: white;
}

/* 日历表格选择日期样式 */
.el-calendar-table td.is-selected {
  background-color: #7000bd;
  color: white;
}
</style>