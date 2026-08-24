const getters = {
  sidebar: (state) => state.app.sidebar,
  size: (state) => state.app.size,
  device: (state) => state.app.device,
  visitedViews: (state) => state.tagsView.visitedViews,
  cachedViews: (state) => state.tagsView.cachedViews,
  token: (state) => state.user.token,
  activeField: (state) => state.user.activeField,
  unique: (state) => state.user.unique,
  avatar: (state) => state.user.userInfo.avatar,
  name: (state) => state.user.userInfo.real_name,
  uid: (state) => state.user.userInfo.uid,
  userInfo: (state) => state.user.userInfo,
  introduction: (state) => state.user.introduction,
  permissions: (state) => state.user.permissions,
  roles: (state) => state.user.roles,
  permission_routes: (state) => state.permission.routes,
  errorLogs: (state) => state.errorLog.logs,
  menuList: (state) => state.user.menuList,
  isLogin: (state) => state.user.isLogin,
  lang: (state) => state.app.language,
  sidebarType: (state) => state.app.sidebarType,
  parentMenuId: (state) => state.app.parentMenuId,
  sidebarParentCur: (state) => state.app.sidebarParentCur,
  defaultOpen: (state) => state.app.defaultOpen,
  isClickTab: (state) => state.app.isClickTab,
  menuStatus: (state) => state.app.sidebar.opened,
  enterprise: (state) => state.user.enterprise,
  updatePromoter: (state) => state.business.updatePromoter,
  processConditions: (state) => state.business.processConditions,
  menuTabData: (state) => state.app.menuTabData,
  crudList: (state) => state.app.crudList,
  // 控制云文件上传数据
  show: (state) => state.cloudfile.show,  // 弹窗显示或隐藏
  fileList: (state) => state.cloudfile.fileList, // 文件列表
  index: (state) => state.cloudfile.index,     // 当时上传文件index
  openUpload: (state) => state.cloudfile.openUpload,  // 开始上传

};
export default getters;
