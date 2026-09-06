export const projectDir = `
├── App.vue
├── BaseComponent
│   ├── BaseForm
│   │   ├── index.js
│   │   └── src
│   │       ├── Form.vue
│   │       └── cpn
│   │           └── inputDropdown
│   │               └── inputDropdown.vue
│   ├── BaseTable
│   │   ├── index.js
│   │   └── src
│   │       └── Table.vue
│   ├── Code
│   │   ├── index.js
│   │   └── src
│   │       └── Code.vue
│   ├── TextLink
│   │   ├── index.js
│   │   └── src
│   │       └── TextLink.vue
│   └── echart
│       ├── index.js
│       └── src
│           ├── echart.vue
│           └── hook
│               └── useEchart.js
├── api
│   ├── business
│   │   ├── config
│   │   │   └── index.js
│   │   ├── interceptors
│   │   │   └── index.js
│   │   └── main
│   │       └── index.js
│   ├── config
│   │   └── base.js
│   ├── login.js
│   ├── menu.js
│   ├── monitor
│   │   ├── cache.js
│   │   ├── job.js
│   │   ├── logininfor.js
│   │   ├── online.js
│   │   ├── operlog.js
│   │   └── server.js
│   ├── system
│   │   ├── config.js
│   │   ├── dept.js
│   │   ├── dict
│   │   │   ├── data.js
│   │   │   └── type.js
│   │   ├── menu.js
│   │   ├── role.js
│   │   └── user.js
│   └── tool
│       └── gen.js
├── assets
│   ├── 401_images
│   │   └── 401.gif
│   ├── 404_images
│   │   ├── 404.png
│   │   └── 404_cloud.png
│   ├── css
│   │   ├── app.scss
│   │   ├── dark.scss
│   │   ├── element.scss
│   │   ├── index.scss
│   │   ├── loading.scss
│   │   ├── mixins.scss
│   │   └── var.scss
│   ├── icons
│   │   └── svg
│   └── images
│       ├── avatar.png
│       ├── header-1.svg
│       └── login-background.jpg
├── components
│   ├── Crontab
│   │   ├── day.vue
│   │   ├── hour.vue
│   │   ├── index.vue
│   │   ├── min.vue
│   │   ├── month.vue
│   │   ├── result.vue
│   │   ├── second.vue
│   │   ├── week.vue
│   │   └── year.vue
│   ├── DictTag
│   │   └── index.vue
│   ├── Editor
│   │   └── index.vue
│   ├── HsjComponent
│   │   ├── delDialog
│   │   │   ├── index.js
│   │   │   └── src
│   │   │       └── DelDialog.vue
│   │   ├── importDialog
│   │   │   ├── index.js
│   │   │   └── src
│   │   │       └── ImportDialog.vue
│   │   ├── pageContent
│   │   │   ├── index.js
│   │   │   └── src
│   │   │       ├── PageContent.vue
│   │   │       └── dictCpn.vue
│   │   ├── pageDialog
│   │   │   ├── index.js
│   │   │   └── src
│   │   │       └── PageDialog.vue
│   │   └── pageSearch
│   │       ├── index.js
│   │       └── src
│   │           └── PageSearch.vue
│   ├── IconSelector
│   │   └── IconSelector.vue
│   ├── ParentView
│   │   └── index.vue
│   ├── SvgIcon
│   │   ├── index.vue
│   │   ├── svgicon.js
│   │   └── useSvg.vue
│   ├── TreeSelect
│   │   └── index.vue
│   └── iFrame
│       └── index.vue
├── directive
│   ├── common
│   │   └── copyText.js
│   ├── index.js
│   └── permission
│       ├── hasPermi.js
│       └── hasRole.js
├── hooks
│   ├── getPageConfig.js
│   └── useDialog.js
├── layout
│   ├── components
│   │   ├── Aside.vue
│   │   ├── Config
│   │   │   └── index.vue
│   │   ├── ContextMenu
│   │   │   └── index.vue
│   │   ├── DarkSwitch
│   │   │   └── index.vue
│   │   ├── Header.vue
│   │   ├── InnerLink
│   │   │   └── index.vue
│   │   ├── Logo.vue
│   │   └── Main.vue
│   ├── container
│   │   ├── Classic.vue
│   │   ├── Default.vue
│   │   ├── Double.vue
│   │   └── Streamline.vue
│   ├── index.vue
│   ├── menus
│   │   ├── Link.vue
│   │   ├── MenuHorizontal.vue
│   │   ├── MenuTree.vue
│   │   ├── MenuVertical.vue
│   │   └── helper.js
│   └── navBar
│       ├── Classic.vue
│       ├── Default.vue
│       ├── Double.vue
│       ├── NavMenus.vue
│       └── Tabs.vue
├── main.js
├── plugins
│   ├── auth.js
│   ├── download.js
│   ├── index.js
│   ├── isSmallScreen.js
│   ├── modal.js
│   └── tab.js
├── router
│   ├── index.js
│   └── routerInterceptor.js
├── store
│   ├── business
│   │   ├── businessStore.js
│   │   └── utils.js
│   ├── constant
│   │   └── cacheKey.js
│   ├── index.js
│   └── modules
│       ├── dict.js
│       ├── layout.js
│       ├── permission.js
│       ├── tagsView.js
│       └── user.js
├── utils
│   ├── auth.js
│   ├── dict.js
│   ├── errorCode.js
│   ├── hasPermi.js
│   ├── hsj
│   │   ├── bus.js
│   │   ├── service
│   │   │   ├── index.js
│   │   │   └── request
│   │   │       ├── config.js
│   │   │       └── index.js
│   │   ├── timeFormat.js
│   │   ├── useSession.js
│   │   ├── useStorage.js
│   │   └── utils.js
│   ├── iconfont.js
│   ├── jsencrypt.js
│   ├── loading.js
│   ├── pageShade.js
│   ├── random.js
│   ├── ruoyi.js
│   ├── to.js
│   ├── useDark.js
│   └── validate.js
└── views
    ├── config
    │   ├── dependencies.js
    │   ├── index.js
    │   ├── project-dir.js
    │   └── technology-stacks.js
    ├── error
    │   ├── 401.vue
    │   └── 404.vue
    ├── index.vue
    ├── login
    │   ├── config
    │   │   └── formConfig.js
    │   └── login.vue
    ├── monitor
    │   ├── cache
    │   │   ├── index.vue
    │   │   └── list.vue
    │   ├── druid
    │   │   └── index.vue
    │   ├── job
    │   │   ├── config
    │   │   │   ├── contentConfig.js
    │   │   │   ├── dialogConfig.js
    │   │   │   ├── logContent.js
    │   │   │   ├── logSearch.js
    │   │   │   ├── logViewConfig.js
    │   │   │   ├── searchConfig.js
    │   │   │   └── viewDialogConfig.js
    │   │   ├── cpns
    │   │   │   ├── LogView.vue
    │   │   │   └── Review.vue
    │   │   ├── index.vue
    │   │   └── log.vue
    │   ├── logininfor
    │   │   ├── config
    │   │   │   ├── contentConfig.js
    │   │   │   ├── dialogConfig.js
    │   │   │   └── searchConfig.js
    │   │   └── index.vue
    │   ├── online
    │   │   ├── config
    │   │   │   ├── contentConfig.js
    │   │   │   └── searchConfig.js
    │   │   └── index.vue
    │   ├── operlog
    │   │   ├── config
    │   │   │   ├── contentConfig.js
    │   │   │   ├── dialogConfig.js
    │   │   │   └── searchConfig.js
    │   │   └── index.vue
    │   └── server
    │       └── index.vue
    ├── redirect
    │   └── index.vue
    ├── system
    │   ├── config
    │   │   ├── config
    │   │   │   ├── contentConfig.js
    │   │   │   ├── dialogConfig.js
    │   │   │   └── searchConfig.js
    │   │   └── index.vue
    │   ├── dept
    │   │   ├── config
    │   │   │   ├── contentConfig.js
    │   │   │   ├── dialogConfig.js
    │   │   │   └── searchConfig.js
    │   │   └── index.vue
    │   ├── dict
    │   │   ├── config
    │   │   │   ├── contentConfig.js
    │   │   │   ├── dataContentConfig.js
    │   │   │   ├── dataDialogConfig.js
    │   │   │   ├── dataSearchConfig.js
    │   │   │   ├── dialogConfig.js
    │   │   │   └── searchConfig.js
    │   │   ├── data.vue
    │   │   └── index.vue
    │   ├── menu
    │   │   ├── config
    │   │   │   ├── contentConfig.js
    │   │   │   ├── dialogConfig.js
    │   │   │   └── searchConfig.js
    │   │   └── index.vue
    │   ├── notice
    │   │   ├── config
    │   │   │   ├── contentConfig.js
    │   │   │   ├── dialogConfig.js
    │   │   │   └── searchConfig.js
    │   │   └── index.vue
    │   ├── post
    │   │   ├── config
    │   │   │   ├── contentConfig.js
    │   │   │   ├── dialogConfig.js
    │   │   │   └── searchConfig.js
    │   │   └── index.vue
    │   ├── role
    │   │   ├── authUser.vue
    │   │   ├── components
    │   │   │   ├── AssignDialog.vue
    │   │   │   └── AuthUserDialog.vue
    │   │   ├── config
    │   │   │   ├── assignConfig.js
    │   │   │   ├── authContent.js
    │   │   │   ├── authDialogSearch.js
    │   │   │   ├── authSearch.js
    │   │   │   ├── contentConfig.js
    │   │   │   ├── dialogConfig.js
    │   │   │   └── searchConfig.js
    │   │   └── index.vue
    │   └── user
    │       ├── authRole.vue
    │       ├── config
    │       │   ├── contentConfig.js
    │       │   ├── dialogConfig.js
    │       │   └── searchConfig.js
    │       ├── index.vue
    │       └── profile
    │           ├── index.vue
    │           ├── resetPwd.vue
    │           ├── userAvatar.vue
    │           └── userInfo.vue
    └── tool
        ├── build
        │   └── index.vue
        ├── gen
        │   ├── basicInfoForm.vue
        │   ├── config
        │   │   ├── contentConfig.js
        │   │   └── searchConfig.js
        │   ├── editTable.vue
        │   ├── genInfoForm.vue
        │   ├── importTable.vue
        │   └── index.vue
        └── swagger
            └── index.vue
`
