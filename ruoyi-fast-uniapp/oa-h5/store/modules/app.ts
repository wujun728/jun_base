import type { AppState } from "../index";

const state: AppState = {
  token: null,
  forumToken: null,
  isLogin: false,
  userInfo: {},
  backgroundColor: "",
  currentIndex: 0, // 客户管理底部导航index
  enterprise: {},
  uid: 0,
  loginBackUrl: "",
  homeActive: false,
  phoneStatus: true,
  depSelectPeople: [],
  isProcess: false,
  depSelectIds: [],
  depCheckIds: [],
  isOrgShow: false,
  frameTree: {},
  selectFrameTree: {},
  enterpriseAuth: {},
  treeSelectBeforePage: "",
  customerFormType: {},
  codeSuccessKey: "",
  menus: [],
  oneOnOneData: {},
  isNoticeJumpPage: false, // 判断消息是否通过消息列表跳转页面
  depSelectIndex: -1, // 申请审批选中部门/成员
  depUserIndex: -1, // 申请流程中成员选择
  selectCustomUsers: [] // 自定义选择成员
};

const mutations = {
  init(state: AppState) {
    const tokenValue = uni.getStorageSync("storageTokenData");
    const userValue = uni.getStorageSync("storageUserData");
    const menuValue = uni.getStorageSync("storageMenuData");
    const forumData = uni.getStorageSync("storageForumData");
    const tree = uni.getStorageSync("storageFrameTree");
    if (forumData) {
      state.forumToken = forumData;
    }
    if (tokenValue) {
      const data = JSON.parse(tokenValue);
      state.token = data.token;
      state.isLogin = true;
    }
    if (userValue) {
      const data = JSON.parse(userValue);
      state.userInfo = data.userInfo;
      state.uid = data.userInfo.uid;
      state.enterprise = data.enterprise;
    }
    if (menuValue) {
      const data = JSON.parse(menuValue);
      state.menus = data.menu;
    }
    if (tree) {
      state.frameTree = JSON.parse(tree);
    }
  },
  // 客户管理-底部导航高亮
  currentIndexFn(state: AppState, data: number) {
    state.currentIndex = data;
  },
  // 登录
  login(state: AppState, data: { token: string }) {
    state.token = data.token;
    state.isLogin = true;
    uni.setStorageSync("storageTokenData", JSON.stringify(data));
  },
  // 登录用户信息
  loginInfo(state: AppState, data: any) {
    state.userInfo = data.userInfo;
    state.uid = data.userInfo.uid;
    state.enterprise = data.enterprise;
    uni.setStorageSync("storageUserData", JSON.stringify(data));
  },
  // 登录用户菜单
  loginMenu(state: AppState, data: any) {
    state.menus = data.menus;
    uni.setStorageSync("storageMenuData", JSON.stringify(data));
  },
  // 登录
  forumLogin(state: AppState, data: string) {
    state.forumToken = data;
    uni.setStorageSync("storageForumData", data);
  },

  // 退出登录
  logout(state: AppState) {
    state.token = "";
    state.userInfo = {};
    state.enterprise = {};
    state.uid = 0;
    state.isLogin = false;
    state.frameTree = {};
    state.frameTree = [];
    uni.removeStorageSync("storageUserData");
    uni.removeStorageSync("storageFrameTree");
    uni.removeStorageSync("storageTokenData");
  },
  frameTree(state: AppState, frameTree: any) {
    state.frameTree = frameTree;
    uni.setStorageSync("storageFrameTree", JSON.stringify(frameTree));
  },
  // 选择部门
  selectFrameTree(state: AppState, selectFrameTree: any) {
    state.selectFrameTree = selectFrameTree;
  },
  setProcess(state: AppState, isProcess: boolean) {
    state.isProcess = isProcess;
  },
  setoneOnOneData(state: AppState, one: any) {
    state.oneOnOneData = one;
  },
  setLoginBackUrl(state: AppState, url: string) {
    state.loginBackUrl = url;
  },
  setDepSelectPeople(state: AppState, depSelectPeople: any) {
    state.depSelectPeople = depSelectPeople;
  },
  setDepSelectIds(state: AppState, depSelectIds: any) {
    state.depSelectIds = depSelectIds;
  },
  setDepCheckIds(state: AppState, depCheckIds: any) {
    state.depCheckIds = depCheckIds;
  },
  setIsOrgShow(state: AppState, isOrgShow: boolean) {
    state.isOrgShow = isOrgShow;
  },
  setEnterpriseAuth(state: AppState, auth: any) {
    state.enterpriseAuth = auth;
  },
  setTreeSelectBeforePage(state: AppState, page: string) {
    state.treeSelectBeforePage = page;
  },
  setCustomerFormType(state: AppState, type: any) {
    state.customerFormType = type;
  },
  setCodeSuccessKey(state: AppState, key: string) {
    state.codeSuccessKey = key;
  },
  setiINoticeJumpPage(state: AppState, arg: boolean) {
    state.isNoticeJumpPage = arg;
  },
  // 设置选中部门/成员
  setDepSelectIndex(state: AppState, arg: number) {
    state.depSelectIndex = arg;
  },
  // 设置审批流程中选择成员
  setDepUserIndex(state: AppState, arg: number) {
    state.depUserIndex = arg;
  },
  // 设置审批流程中选择成员
  setSelectCustomUsers(state: AppState, users: any) {
    state.selectCustomUsers = users;
  }
};

const actions = {};

export default {
  state,
  mutations,
  actions
};
