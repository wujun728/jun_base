import store from "../store";
import { enterpriseAuthApi } from "@/api/public";
import { frameTreeApi } from "@/api/user";
import type { Res } from "@/utils/typeHelper";
import message from "@/utils/message";
import { clickNavigateTo } from "@/utils/helper";
import { encryptCode } from "@/utils/cryptoCode";

// 获取全局的权限设置
export const getEnterpriseAuth = () => {
  enterpriseAuthApi().then((res: Res) => {
    const auth = res.data;
    store.commit("setEnterpriseAuth", auth);
  }).catch((error: Res) => {
    message.error(error.message);
  });
};

// 获取组织架构中所有的人员
export const getFrameTree = () => {
  frameTreeApi().then((res: Res) => {
    store.commit("frameTree", res.data);
  }).catch((error: Res) => {
    message.error(error.message);
  });
};

export const autoLoad = () => {
  getFrameTree();
  getEnterpriseAuth();
};

/**
 * 重置选中人员/部门
 */
export const resetSelectDepartment = <T extends any[]>(people: T = <any>[], ids: T = <any>[]) => {
  // 重置选中人员/部门
  store.commit("setDepSelectPeople", people);
  // 重置选中部门/人员id
  store.commit("setDepSelectIds", ids);
  // 重置页面跳转
  store.commit("setTreeSelectBeforePage", "");
};

/**
 * 重置申请审批中人员选择Index
 */
export const resetExamineIndex = () => {
  // 重置选中人员/部门index
  store.commit("setDepSelectIndex", -1);
  // 重置审批流程中选择成员Index
  store.commit("setDepUserIndex", -1);
};

/**
 * 选择成员|部门链接跳转
 */
export const navigateToDepartment = (query: string, currentPageUrl: string = "", url: string = "/pages/users/organization/index") => {
  // 参数加密
  const item = encryptCode(query);
  if (currentPageUrl) {
    store.commit("setTreeSelectBeforePage", currentPageUrl);
  }
  // 页面跳转
  clickNavigateTo(`${url}?item=${item}`);
};
