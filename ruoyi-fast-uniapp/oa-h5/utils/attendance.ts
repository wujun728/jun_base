/**
 * 考勤明细筛选
 */
export const customerTabData = [
  {
    name: "异常",
    id: 1,
  },
  {
    name: "迟到",
    id: 2,
  },
  {
    name: "严重迟到",
    id: 3,
  },
  {
    name: "早退",
    id: 4,
  },
  {
    name: "缺卡",
    id: 5,
  },
  {
    name: "旷工",
    id: 6,
  },
  {
    name: "外勤卡",
    id: 7,
  },
  {
    name: "异常外勤",
    id: 8,
  },
];

/**
 * 加班明细
 */
export const overTimeTabData = [
  {
    name: "工作日加班",
    id: 1,
  },
  {
    name: "休息日加班",
    id: 2,
  },
  {
    name: "节假日加班",
    id: 3,
  },
];

export const getStatusString = (status: number[]) => {
  const statusMap = {
    2: "迟到",
    3: "严重迟到",
    4: "早退",
    5: "晚到缺卡",
    6: "早退缺卡",
    7: "缺卡",
  };

  type StatusKey = keyof typeof statusMap;

  const existingStatuses = status.filter(s => statusMap[s as StatusKey]);

  const statusStrings = existingStatuses.map(s => statusMap[s as StatusKey]);

  return statusStrings.join(", ");
};

export const getLacationString = (status: number[]) => {
  const statusMap = {
    1: "外勤",
    2: "地点异常",
  };

  type StatusKey = keyof typeof statusMap;

  const existingStatuses = status.filter(s => statusMap[s as StatusKey]);

  const statusStrings = existingStatuses.map(s => statusMap[s as StatusKey]);

  return statusStrings.join(", ");
};
// 类型过滤
// 1：异常；2：迟到；3：严重迟到；4：早退；5：缺卡；6：旷工；7：外勤卡；8：异常外勤；
export const typeText = (type: number) => {
  let name;
  switch (type) {
    case 0:
      name = "正常";
      break;
    case 1:
      name = "异常";
      break;
    case 2:
      name = "迟到";
      break;
    case 3:
      name = "严重迟到";
      break;
    case 4:
      name = "早退";
      break;
    case 5:
      name = "缺卡";
      break;
    case 6:
      name = "旷工";
      break;
    case 7:
      name = "外勤卡";
      break;
    case 8:
      name = "异常外勤";
      break;
  }
  return name;
};
