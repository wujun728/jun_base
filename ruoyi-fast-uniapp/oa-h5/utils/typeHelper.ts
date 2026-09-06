// 导航栏菜单验证
export interface Drop {
  name: string;
  id: number;
  icon: string;
}
// 搜索条件验证
export interface Box {
  status: string | number;
  type: string | number;
  time: string;
  date?: string;
  approveId?: number;
}

// 导航菜单栏切换验证
export interface Tab {
  name: string;
  id: number;
  index: number;
}

// 默认类型
export interface PropType {
  [propName: string]: any;
}

// 后端接口返回值的验证
export interface Res extends PropType {
  message: string;
  status: number;
}

// 详情内容验证
export interface Detail extends PropType {
  readonly id: number;
}

// 页面跳转get请求传参
export interface GetType {
  id?: number;
  cid?: number;
  eid?: number;
  tab?: string | number;
  type?: string;
}

// 选择下拉框类型
export interface PickerType {
  value: number;
  text: string;
}
