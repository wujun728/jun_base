//日历搜索快捷键
export default {
  shortcuts: [
    {
      text: '今天',
      onClick(picker) {
        const end = new Date();
        const start = new Date();
        picker.$emit('pick', [start, end]);
      },
    },
    {
      text: '昨天',
      onClick(picker) {
        const end = new Date();
        const start = new Date();
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 1);
        picker.$emit('pick', [start, start]);
      },
    },
    {
      text: '本月',
      onClick(picker) {
        const start = new Date();
        const end = new Date();
        start.setMonth(start.getMonth());
        start.setDate(1);
        end.setMonth(end.getMonth() + 1);
        end.setDate(0);
        picker.$emit('pick', [start, end]);
      },
    },
    {
      text: '上月',
      onClick(picker) {
        const start = picker.$moment().subtract(1, 'month').startOf('month').format('YYYY/MM/DD');
        const end = picker.$moment().subtract(1, 'month').endOf('month').format('YYYY/MM/DD');
        picker.$emit('pick', [start, end]);
      },
    },
    {
      text: '最近7天',
      onClick(picker) {
        const end = new Date();
        const start = new Date();
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
        picker.$emit('pick', [start, end]);
      },
    },
    {
      text: '最近30天',
      onClick(picker) {
        const end = new Date();
        const start = new Date();
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
        picker.$emit('pick', [start, end]);
      },
    },
    {
      text: '最近90天',
      onClick(picker) {
        const end = new Date();
        const start = new Date();
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
        picker.$emit('pick', [start, end]);
      },
    },
    {
      text: '最近1年',
      onClick(picker) {
        const end = new Date();
        const start = new Date();
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 365);
        picker.$emit('pick', [start, end]);
      },
    },
    {
      text: '本年',
      onClick(picker) {
        const start = picker.$moment().startOf('year').format('YYYY/MM/DD');
        const end = picker.$moment().format('YYYY/MM/DD');
        picker.$emit('pick', [start, end]);
      },
    },
    {
      text: '去年',
      onClick(picker) {
        const start = picker.$moment().subtract(1, 'year').startOf('year').format('YYYY/MM/DD');
        const end = picker.$moment().subtract(1, 'year').endOf('year').format('YYYY/MM/DD');
        picker.$emit('pick', [start, end]);
      },
    },
  ]
};