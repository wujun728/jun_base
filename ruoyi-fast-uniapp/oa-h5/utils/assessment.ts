/**
 * 获取考核状态
 */
enum StatusText {
  目标制定,
  自我评价,
  上级评价,
  绩效审核,
  考核结束,
  未开始
}
export const getStatusText = (id: number): string => {
  // 枚举会对枚举值到枚举名进行反向映射
  return StatusText[id];
};

/**
 * 获取考核周期
 */
enum PeriodText {
  周考核 = 1,
  月考核,
  年考核,
  半年考核,
  季度考核
}
export const getPeriodText = (id: number): string => {
  return PeriodText[id];
};

/**
 * 获取考核状态
 */
enum StatusTips {
  自我评价中 = 1,
  上级评价中,
  绩效审核中,
  已结束,
  未开始
}
export const getStatusTips = (id: number): string => {
  return StatusTips[id];
};

/**
 * 申请审批状态
 */
export const examineTabData = [
  { name: "已发起", id: -1, icon: "icon-shenqingshenpi-yifaqi", number: 1 },
  { name: "已撤销", id: 4, icon: "icon-shenqingshenpi-yichexiao", number: 132 },
];

/**
 * 申请审批状态
 */
export const approveTabData = [
  { name: "待审批", id: 1, icon: "icon-shenqingshenpi-daichuli", number: 0 },
  { name: "已处理", id: 2, icon: "icon-shenqingshenpi-yichuli", number: 0 },
  { name: "抄送我的", id: 3, icon: "icon-shenqingshenpi-chaosongwo", number: 0 },
];

/**
 * 客户管理
 */
export const customerTabData = [
  { name: "我负责的", id: 2 },
  { name: "我查看的", id: 1 },
  { name: "公海池", id: 3 },
];
// 合同管理
export const contractTabData = [
  { name: "我负责的", id: 6 },
  { name: "我查看的", id: 5 },

];

export const pendingTabData = [
  { name: "待处理", id: 1 },
  { name: "全部", id: 2 },
];

/**
 * 付款类型判断
 */
enum PayRecordTypes {
  回款,
  续费,
  支出,
}
export const getPayRecordTypes = (type: number): string => {
  return PayRecordTypes[type];
};
