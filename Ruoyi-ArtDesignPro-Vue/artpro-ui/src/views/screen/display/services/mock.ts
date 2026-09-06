/**
 * 溜井数字化监控管理平台 - Mock 数据
 *
 * @description 提供开发环境使用的模拟数据，便于在无后端情况下进行界面开发
 * @author zhangzhen
 * @version 0.1.0
 */
import type {
  BarPayload,
  CameraCategory,
  CarsLogsPayload,
  DeviceStatus,
  LogsPayload,
  MaterialLevel,
  MineCarStats,
  OutLogsPayload,
  SelectOption,
  WarningItem,
  WarningSummary
} from './types'

/**
 * 模拟报警统计数据
 */
export const mockWarningSummary: WarningSummary = {
  total: 9,
  wcl: 9,
  cl: 0,
  ljwcl: 27033
}

/**
 * 模拟报警列表数据
 */
export const mockWarnings: WarningItem[] = [
  {
    id: "2052607724377739264",
    type: "溜井入料口堵塞",
    name: "54号溜井",
    warning_time: "2026-05-08 12:33:12",
    state: "0",
    roter: "1",
    img: "http://192.168.246.136:888/img/stream_192.168.18.119_liujing_20260508123311.jpg"
  },
  {
    id: "2052592624828813312",
    type: "溜井入料口堵塞",
    name: "54号溜井",
    warning_time: "2026-05-08 11:33:12",
    state: "0",
    roter: "1",
    img: "http://192.168.246.136:888/img/stream_192.168.18.119_liujing_20260508113311.jpg"
  },
  {
    id: "1910292467554447397",
    type: "未正确佩戴安全帽",
    name: "高峰山4号",
    warning_time: "2026-05-08 11:20:59",
    state: "0",
    roter: "2",
    img: "http://192.168.246.136:888/img/stream_192.168.18.119_helmet_20260508112059.jpg"
  },
  {
    id: "1910292467554447396",
    type: "人员进入危险区域",
    name: "高峰山4号",
    warning_time: "2026-05-08 11:20:44",
    state: "0",
    roter: "2",
    img: "http://192.168.246.136:888/img/stream_192.168.18.119_person_safety_20260508112044.jpg"
  },
  {
    id: "2052585118383607808",
    type: "溜井已满",
    name: "高峰山5号",
    warning_time: "2026-05-08 11:03:22",
    state: "0",
    roter: "1",
    img: "http://192.168.246.136:888/img/stream_192.168.18.119_liujing_20260508110321.jpg"
  },
  {
    id: "2052577524998868992",
    type: "溜井入料口堵塞",
    name: "54号溜井",
    warning_time: "2026-05-08 10:33:12",
    state: "0",
    roter: "1",
    img: "http://192.168.246.136:888/img/stream_192.168.18.119_liujing_20260508103311.jpg"
  },
  {
    id: "2052566655673962496",
    type: "车辆长时间滞留",
    name: "54号溜井",
    warning_time: "2026-05-08 09:50:00",
    state: "0",
    roter: "1",
    img: "http://192.168.246.136:888/img/stream_192.168.18.119_car_stay_20260508095000.jpg"
  },
  {
    id: "2052563063084814336",
    type: "溜井入料口堵塞",
    name: "高峰山5号",
    warning_time: "2026-05-08 09:35:44",
    state: "0",
    roter: "1",
    img: "http://192.168.246.136:888/img/stream_192.168.18.119_liujing_20260508093543.jpg"
  },
  {
    id: "2052562390293286912",
    type: "溜井入料口堵塞",
    name: "54号溜井",
    warning_time: "2026-05-08 09:33:03",
    state: "0",
    roter: "1",
    img: "http://192.168.246.136:888/img/stream_192.168.18.119_liujing_20260508093303.jpg"
  }
]

/**
 * 模拟设备状态数据
 */
export const mockDevices: DeviceStatus[] = [
  { name: '摄像头', total: 8, online: 8, offline: 0 },
  { name: 'RFID阅读器', total: 5, online: 4, offline: 1 },
  { name: '声光报警器', total: 6, online: 6, offline: 0 },
  { name: '硬盘录像机', total: 3, online: 3, offline: 0 }
]

/**
 * 模拟矿车统计数据
 */
export const mockMineCars: MineCarStats = {
  online: 10,
  total: 70,
  offline: 60
}

/**
 * 模拟柱状图数据
 */
export const mockBar: BarPayload = {
  total: 293978.847,
  xA: ['06月', '07月', '08月', '09月', '10月', '11月', '12月', '01月', '02月', '03月', '04月', '05月'],
  yA: [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 49187.12, 50346.87, 87189.19, 85289.59, 20277.94]
}

/**
 * 模拟出入料记录数据
 */
export const mockLogs: LogsPayload = {
  total: {
    rTotal: 1190.11,
    rCars: 66,
    cTotal: 953.36,
    cCars: 8
  },
  detail: [
    { name: '高峰山4号', rTotal: 152.78, cTotal: 114.26 },
    { name: '高峰山5号', rTotal: 349.92, cTotal: 218.24 },
    { name: '53号溜井', rTotal: 395.94, cTotal: 410.06 },
    { name: '54号溜井', rTotal: 291.46, cTotal: 210.8 },
    { name: '34号溜井', rTotal: 0.0, cTotal: 1004.57 },
    { name: '61号溜井', rTotal: 0.0, cTotal: 0.0 },
    { name: '55号溜井', rTotal: 0.0, cTotal: 0.0 },
    { name: '56号溜井', rTotal: 0.0, cTotal: 0.0 },
    { name: '57号溜井', rTotal: 0.0, cTotal: 0.0 },
    { name: '58号溜井', rTotal: 0.0, cTotal: 0.0 }
  ]
}

/**
 * 模拟部门选择选项数据
 */
export const mockSelectOptions: SelectOption[] = [
  { value: '5288924761098241', lable: '一坑', dept_sort: 1 },
  { value: '5288924472756225', lable: '三坑', dept_sort: 3 }
]

/**
 * 模拟溜井料位数据
 */
export const mockMaterials: MaterialLevel[] = [
  {
    id: "1001",
    now_quantity: 0.62,
    shitype: "高峰山42",
    lname: "高峰山4号",
    shitypename: "硫化矿",
    all_quantity: 22,
    ton: 10.74023,
    status: 0,
    dept: "5288924472756225"
  },
  {
    id: "1002",
    shitype: "高峰山低硫",
    lname: "高峰山5号",
    now_quantity: 22,
    shitypename: "硫化矿",
    all_quantity: 22,
    ton: 782.42,
    status: 0,
    dept: "5288924472756225"
  },
  {
    id: "1003",
    shitype: "高峰山单铜",
    ton: 654.6205872,
    lname: "53号溜井",
    all_quantity: 23,
    shitypename: "硫化矿",
    now_quantity: 22.86,
    status: 0,
    dept: "5288924472756225"
  },
  {
    id: "1004",
    now_quantity: 18.64,
    ton: 433.3418875,
    shitypename: "碴石",
    lname: "54号溜井",
    all_quantity: 23,
    shitype: "碴（石）",
    status: 0,
    dept: "5288924472756225"
  },
  {
    id: "1005",
    now_quantity: 12.5,
    ton: 256.8,
    shitypename: "碴石",
    lname: "55号溜井",
    all_quantity: 25,
    shitype: "碴（石）",
    status: 0,
    dept: "5288924472756225"
  },
  {
    id: "1006",
    now_quantity: 8.3,
    ton: 189.5,
    shitypename: "硫化矿",
    lname: "56号溜井",
    all_quantity: 20,
    shitype: "低硫矿",
    status: 0,
    dept: "5288924472756225"
  },
  {
    id: "1007",
    now_quantity: 5.2,
    ton: 98.7,
    shitypename: "硫化矿",
    lname: "57号溜井",
    all_quantity: 18,
    shitype: "高硫矿",
    status: 0,
    dept: "5288924761098241"
  },
  {
    id: "1008",
    shitype: "1660矿",
    lname: "61号溜井",
    ton: 0,
    all_quantity: 180,
    now_quantity: 0,
    shitypename: "高、低氧矿",
    status: 0,
    dept: "5288924761098241"
  }
]

/**
 * 模拟直播摄像头数据
 */
export const mockLive: CameraCategory[] = [
  {
    label: '一坑',
    children: [
      {
        label: '61号溜井入料口',
        url: 'http://192.168.246.136:10800/play.html?channel=5&protocol=ws_flv&stretch=yes&iframe=yes&controls=no'
      }
    ]
  },
  {
    label: '二坑',
    children: [
      {
        label: '1920挂车点',
        url: 'http://192.168.246.136:10800/play.html?channel=7&protocol=ws_flv&stretch=yes&iframe=yes'
      },
      {
        label: '1920挂车点1',
        url: 'http://192.168.246.136:10800/play.html?channel=8&protocol=ws_flv&stretch=yes&iframe=yes'
      }
    ]
  },
  {
    label: '三坑',
    children: [
      {
        label: '高峰山4号入料口',
        url: 'http://192.168.246.136:10800/play.html?channel=2&protocol=ws_flv&stretch=yes&iframe=yes'
      },
      {
        label: '高峰山5号入料口',
        url: 'http://192.168.246.136:10800/play.html?channel=1&protocol=ws_flv&stretch=yes&iframe=yes'
      },
      {
        label: '53号溜井入料口',
        url: 'http://192.168.246.136:10800/play.html?channel=3&protocol=ws_flv&stretch=yes&iframe=yes'
      },
      {
        label: '54号溜井入料口',
        url: 'http://192.168.246.136:10800/play.html?channel=4&protocol=ws_flv&stretch=yes&iframe=yes'
      }
    ]
  }
]

/**
 * 模拟矿车日志数据（入料记录）
 */
export const mockCarsLogs: CarsLogsPayload = {
  totalElements: 15,
  content: [
    {
      id: 1,
      lname: '54号溜井',
      company: '西三队',
      persons: '高峰山42',
      car: 'SC-11',
      shitype: '1',
      fulls: '92.76',
      createTime: '2026-05-08 20:33:52',
      name: '17',
      bigs: '正常',
      yiwu: '无',
      rate: '95',
      timecha: '125',
      videopath: '',
      imgpath: ''
    },
    {
      id: 2,
      lname: '54号溜井',
      company: '西三队',
      persons: '高峰山42',
      car: 'SC-12',
      shitype: '1',
      fulls: '95.32',
      createTime: '2026-05-08 19:48:41',
      name: '17',
      bigs: '正常',
      yiwu: '无',
      rate: '93',
      timecha: '118',
      videopath: '',
      imgpath: ''
    },
    {
      id: 3,
      lname: '54号溜井',
      company: '西三队',
      persons: '碴（石）',
      car: 'SC-13',
      shitype: '2',
      fulls: '94.89',
      createTime: '2026-05-08 18:55:33',
      name: '17',
      bigs: '正常',
      yiwu: '无',
      rate: '94',
      timecha: '122',
      videopath: '',
      imgpath: ''
    },
    {
      id: 4,
      lname: '53号溜井',
      company: '西三队',
      persons: '高峰山单铜',
      car: 'SC-14',
      shitype: '1',
      fulls: '96.45',
      createTime: '2026-05-08 17:45:26',
      name: '18',
      bigs: '正常',
      yiwu: '无',
      rate: '96',
      timecha: '115',
      videopath: '',
      imgpath: ''
    },
    {
      id: 5,
      lname: '53号溜井',
      company: '西三队',
      persons: '高峰山42',
      car: 'SC-15',
      shitype: '1',
      fulls: '93.21',
      createTime: '2026-05-08 16:38:18',
      name: '18',
      bigs: '正常',
      yiwu: '无',
      rate: '92',
      timecha: '130',
      videopath: '',
      imgpath: ''
    },
    {
      id: 6,
      lname: '53号溜井',
      company: '西三队',
      persons: '碴（石）',
      car: 'SC-16',
      shitype: '2',
      fulls: '91.56',
      createTime: '2026-05-08 15:32:12',
      name: '18',
      bigs: '正常',
      yiwu: '无',
      rate: '91',
      timecha: '128',
      videopath: '',
      imgpath: ''
    },
    {
      id: 7,
      lname: '53号溜井',
      company: '西三队',
      persons: '高峰山单铜',
      car: 'SC-17',
      shitype: '1',
      fulls: '97.89',
      createTime: '2026-05-08 14:28:05',
      name: '18',
      bigs: '正常',
      yiwu: '无',
      rate: '97',
      timecha: '112',
      videopath: '',
      imgpath: ''
    },
    {
      id: 8,
      lname: '高峰山4号',
      company: '西三队',
      persons: '高峰山42',
      car: 'SC-18',
      shitype: '1',
      fulls: '94.33',
      createTime: '2026-05-08 13:22:58',
      name: '17',
      bigs: '正常',
      yiwu: '无',
      rate: '94',
      timecha: '120',
      videopath: '',
      imgpath: ''
    },
    {
      id: 9,
      lname: '高峰山4号',
      company: '西三队',
      persons: '碴（石）',
      car: 'SC-19',
      shitype: '2',
      fulls: '92.67',
      createTime: '2026-05-08 12:18:51',
      name: '17',
      bigs: '正常',
      yiwu: '无',
      rate: '93',
      timecha: '123',
      videopath: '',
      imgpath: ''
    },
    {
      id: 10,
      lname: '高峰山4号',
      company: '西三队',
      persons: '高峰山42',
      car: 'SC-20',
      shitype: '1',
      fulls: '95.78',
      createTime: '2026-05-08 11:15:44',
      name: '17',
      bigs: '正常',
      yiwu: '无',
      rate: '95',
      timecha: '117',
      videopath: '',
      imgpath: ''
    },
    {
      id: 11,
      lname: '高峰山5号',
      company: '西三队',
      persons: '碴（石）',
      car: 'SC-21',
      shitype: '2',
      fulls: '98.23',
      createTime: '2026-05-08 10:12:37',
      name: '17',
      bigs: '正常',
      yiwu: '无',
      rate: '98',
      timecha: '110',
      videopath: '',
      imgpath: ''
    },
    {
      id: 12,
      lname: '高峰山5号',
      company: '西三队',
      persons: '高峰山42',
      car: 'SC-22',
      shitype: '1',
      fulls: '93.45',
      createTime: '2026-05-08 09:08:30',
      name: '17',
      bigs: '正常',
      yiwu: '无',
      rate: '94',
      timecha: '125',
      videopath: '',
      imgpath: ''
    },
    {
      id: 13,
      lname: '高峰山5号',
      company: '西三队',
      persons: '高峰山42',
      car: 'SC-23',
      shitype: '1',
      fulls: '96.89',
      createTime: '2026-05-08 08:05:23',
      name: '17',
      bigs: '正常',
      yiwu: '无',
      rate: '96',
      timecha: '113',
      videopath: '',
      imgpath: ''
    },
    {
      id: 14,
      lname: '53号溜井',
      company: '西三队',
      persons: '碴（石）',
      car: 'SC-24',
      shitype: '2',
      fulls: '92.12',
      createTime: '2026-05-08 07:02:16',
      name: '18',
      bigs: '正常',
      yiwu: '无',
      rate: '92',
      timecha: '128',
      videopath: '',
      imgpath: ''
    },
    {
      id: 15,
      lname: '53号溜井',
      company: '西三队',
      persons: '高峰山单铜',
      car: 'SC-25',
      shitype: '1',
      fulls: '97.56',
      createTime: '2026-05-08 06:00:09',
      name: '18',
      bigs: '正常',
      yiwu: '无',
      rate: '97',
      timecha: '114',
      videopath: '',
      imgpath: ''
    }
  ]
}

/**
 * 模拟出料记录数据
 */
export const mockOutLogs: OutLogsPayload = {
  totalElements: 2666,
  content: [
    {
      "weighingTime": "2026-05-08 21:16:53",
      "ydNo": "2026050800116",
      "plateNumber": "BC-30",
      "car_type": "侧卸式",
      "taskTime": "2026-05-08",
      "totalTonnage": "114.375",
      "releaseConfirmation": "2026-05-08 19:48:53",
      "wellingPoint": "34号溜井",
      "team": "17",
      "releaseDate": "2026-05-08 19:43:48",
      "schedule": "中班",
      "miners": "白高博",
      "leader": "杨航状",
      "mineralType": "1660矿",
      "create_time": "2026-05-08 21:16:30",
      "vehicles": "谭晶",
      "unloadingTime": "2026-05-08 19:07:48",
      "welling_point_id": "1632567808053252097",
      "discharge": "17",
      "instruction": "运矿三队"
    },
    {
      "weighingTime": "2026-05-08 21:30:19",
      "ydNo": "2026050800120",
      "plateNumber": "BC-27",
      "car_type": "侧卸式",
      "taskTime": "2026-05-08",
      "totalTonnage": "115.565",
      "releaseConfirmation": "2026-05-08 20:01:07",
      "wellingPoint": "34号溜井",
      "team": "17",
      "releaseDate": "2026-05-08 19:59:40",
      "schedule": "中班",
      "miners": "谭晶",
      "leader": "杨航状",
      "mineralType": "1660矿",
      "create_time": "2026-05-08 21:30:19",
      "vehicles": "马魁",
      "unloadingTime": "2026-05-08 19:58:14",
      "welling_point_id": "1632567808053252097",
      "discharge": "17",
      "instruction": "运矿三队"
    },
    {
      "weighingTime": "2026-05-08 20:55:58",
      "ydNo": "2026050800128",
      "plateNumber": "BC-13",
      "car_type": "底卸式",
      "taskTime": "2026-05-08",
      "totalTonnage": "129.150",
      "releaseConfirmation": "2026-05-08 22:55:58",
      "wellingPoint": "高峰山4#仓",
      "team": "18",
      "releaseDate": "2026-05-08 22:55:40",
      "schedule": "中班",
      "miners": "付博民",
      "leader": "杨航状",
      "mineralType": "高峰山42",
      "create_time": "2026-05-09 02:23:54",
      "vehicles": "李继洋",
      "unloadingTime": "2026-05-08 20:56:38",
      "welling_point_id": "1872131974206918657",
      "discharge": "18",
      "instruction": "运矿一队"
    },
    {
      "weighingTime": "2026-05-09 01:55:47",
      "ydNo": "2026050900012",
      "plateNumber": "BC-04",
      "car_type": "侧卸式",
      "taskTime": "2026-05-09",
      "totalTonnage": "108.890",
      "releaseConfirmation": "2026-05-09 01:00:10",
      "wellingPoint": "34号溜井",
      "team": "17",
      "releaseDate": "2026-05-09 00:31:56",
      "schedule": "夜班",
      "miners": "侯坤",
      "leader": "梁正达",
      "mineralType": "1660矿",
      "create_time": "2026-05-09 01:58:37",
      "vehicles": "李祝云",
      "unloadingTime": "2026-05-08 23:44:42",
      "welling_point_id": "1632567808053252097",
      "discharge": "17",
      "instruction": "运矿一队"
    },
    {
      "weighingTime": "2026-05-09 01:38:43",
      "ydNo": "2026050900006",
      "plateNumber": "BC-01",
      "car_type": "侧卸式",
      "taskTime": "2026-05-09",
      "totalTonnage": "104.115",
      "releaseConfirmation": "2026-05-09 00:21:46",
      "wellingPoint": "34号溜井",
      "team": "16",
      "releaseDate": "2026-05-09 00:07:15",
      "schedule": "夜班",
      "miners": "李祝云",
      "leader": "梁正达",
      "mineralType": "1660矿",
      "create_time": "2026-05-09 01:38:11",
      "vehicles": "马辉",
      "unloadingTime": "2026-05-08 23:20:52",
      "welling_point_id": "1632567808053252097",
      "discharge": "16",
      "instruction": "运矿一队"
    },
    {
      "weighingTime": "2026-05-09 03:01:41",
      "ydNo": "2026050900016",
      "plateNumber": "BC-10",
      "car_type": "底卸式",
      "taskTime": "2026-05-09",
      "totalTonnage": "112.035",
      "releaseConfirmation": "2026-05-09 02:20:52",
      "wellingPoint": "高峰山4#仓",
      "team": "17",
      "releaseDate": "2026-05-09 02:20:37",
      "schedule": "夜班",
      "miners": "杨三泽",
      "leader": "梁正达",
      "mineralType": "高峰山42",
      "create_time": "2026-05-09 03:00:25",
      "vehicles": "叶涛",
      "unloadingTime": "2026-05-09 00:15:31",
      "welling_point_id": "1872131974206918657",
      "discharge": "17",
      "instruction": "运矿一队"
    },
    {
      "weighingTime": "2026-05-09 02:28:28",
      "ydNo": "2026050900019",
      "plateNumber": "BC-21",
      "car_type": "侧卸式",
      "taskTime": "2026-05-09",
      "totalTonnage": "113.590",
      "releaseConfirmation": "2026-05-09 01:09:36",
      "wellingPoint": "34号溜井",
      "team": "18",
      "releaseDate": "2026-05-09 01:09:08",
      "schedule": "夜班",
      "miners": "侯坤",
      "leader": "梁正达",
      "mineralType": "1660矿",
      "create_time": "2026-05-09 02:29:06",
      "vehicles": "闵洪",
      "unloadingTime": "2026-05-09 00:26:54",
      "welling_point_id": "1632567808053252097",
      "discharge": "18",
      "instruction": "运矿一队"
    }
  ]
}
