export interface FontSizeConfig {
  size: number,
  realSize: number
}


export const fontSizeList: FontSizeConfig[] = [
  {
    "size": 12, // 编辑器要使用的大小
    "realSize": 12, // 实际在工具栏中显示的大小
  },
  {
    "size": 14,
    "realSize": 14,
  },
  {
    "size": 16,
    "realSize": 15,
  }, {
    "size": 18,
    "realSize": 16,
  }, {
    "size": 20,
    "realSize": 17,
  }, {
    "size": 24,
    "realSize": 18,
  },
];

export const colorList = [
  "#222222",
  "#A6A6A6",
  "#EA0000",
  "#0256FF",
  "#4ADD85",
  "#FFC235",
];

export const hightlightColorList = [
  "#FFFFFF",
  "#E1E0E0",
  "#FF788A",
  "#49B9FF",
  "#A9FFCC",
  "#FFDF78",
];
