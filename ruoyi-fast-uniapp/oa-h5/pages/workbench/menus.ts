export const menu = [{
  menu_name: '个人办公',
  menu_path: '',
  children: [
    {
      menu_name: '工作汇报',
      menu_path: '/pages/users/report/index',
      logo: ''
    },
    {
      menu_name: '绩效考核',
      menu_path: '/pages/users/assessment/index',
      logo: ''
    },
    {
      menu_name: '企业动态',
      menu_path: '/pages/users/notice/index',
      logo: ''
    },
    {
      menu_name: '我的日程',
      menu_path: '/pages/users/schedule/index',
      logo: ''
    },
    {
      menu_name: '申请审批',
      menu_path: '/pages/users/examine/index',
      logo: ''
    },
    {
      menu_name: '通讯录',
      menu_path: '/pages/user/index',
      logo: ''
    },
    {
      menu_name: '记事本',
      menu_path: '/pages/users/memorandum/index',
      logo: 'https://oamanage.oss-accelerate.aliyuncs.com/attach/2022/11/59463202211281031203199.png'
    },
    {
      menu_name: '考勤打卡',
      menu_path: '/pages/attendance/index',
      logo: 'https://crmebent.oss-cn-hangzhou.aliyuncs.com/files/%E5%91%98%E5%B7%A5%E6%A1%A3%E6%A1%88%402x(1).png'
    },
  ]
},
{
  menu_name: '客户管理',
  menu_path: '',
  children: [{
    menu_name: '客户列表',
    menu_path: '/pages/customer/list/index',
    logo: ''
  },
  {
    menu_name: '合同管理',
    menu_path: '/pages/customer/contract/index',
    logo: ''
  },
  {
    menu_name: '发票管理',
    menu_path: '/pages/customer/invoice/index',
    logo: ''
  }, {
    menu_name: '业绩统计',
    menu_path: '/pages/customer/list/statistics',
    logo: ''
  },
  ]
},
{
  menu_name: '财务管理',
  menu_path: '',
  children: [
    //   {
    //   menu_name: '付款审核',
    //   menu_path: '/pages/finance/payment/index',
    //   logo: ''
    // },
    {
      menu_name: '发票审核',
      menu_path: '/pages/finance/invoice/index',
      logo: ''
    }
  ]
}
]