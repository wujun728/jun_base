module.exports = {
  // 路由前缀
  roterPre: '/admin',

  // 路由标题
  title: '陀螺匠OA系统',
  /** `
   * @type {boolean} true | false
   * @description Whether show the
   * settings right-panel
   */
  showSettings: true,

  /**
   * @type {boolean} true | false
   * @description Whether need tagsView
   */
  tagsView: false,

  /**
   * @type {boolean} true | false
   * @description Whether fix the header
   */
  fixedHeader: true,

  /**
   * @type {boolean} true | false
   * @description Whether show the logo in sidebar
   */
  sidebarLogo: true,
  /** 显示右侧帮助
   * @type {boolean} true | false
   * @description Whether show the logo in sidebar
   */
  helpShow: true,
  /** 显示论坛
   * @type {boolean} true | false
   * @description Whether show the logo in sidebar
   */
  bbsShow: true,
  /** 显示知识社区
   * @type {boolean} true | false
   * @description Whether show the logo in sidebar
   */
  forumShow: true,
  /**
   * @type {string | array} 'production' | ['production', 'development']
   * @description Need show err logs component.
   * The default is only used in the production env
   * If you want to also use it in dev, you can pass ['production', 'development']
   */
  errorLog: 'production',
};
