import { databaseListApi } from '@/api/develop'

const state = {
  allEntityLabel: {},
  allEntityName: {},
  allEntityCode: {},
  unSystemEntityList: [],
  processEntityList: [],
  publicSetting: {
    webVer: '1.5.27 20240522'
  }
}
const getters = {
  queryEntityNameById: (state) => (id) => {
    return state.allEntityName[parseInt(id.split('-')[0])]
  },
  queryEntityCodeById: () => (id) => {
    return parseInt(id.split('-')[0])
  },
  queryEntityNameByLabel: (state) => (name) => {
    return state.allEntityLabel[state.allEntityCode[name]]
  },
  queryEntityNameByCode: (state) => (code) => {
    return state.allEntityName[code]
  }
}
const mutations = {
  refreshCache(state, list) {
    state.unSystemEntityList = []
    state.processEntityList = []
    list.forEach((el) => {
      Vue.set(state.allEntityLabel, el.entityCode, el.label)
      Vue.set(state.allEntityName, el.entityCode, el.name)
      Vue.set(state.allEntityCode, el.name, el.entityCode)
      if (!el.systemEntityFlag) {
        state.unSystemEntityList.push(el)
      }
      if (!el.systemEntityFlag && !el.detailEntityFlag) {
        state.processEntityList.push(el)
      }
    })
  },
  queryEntityNameByCode(state, code) {
    return state.allEntityName[code]
  },
  setPublicSetting(state, data) {
    state.publicSetting.APP_NAME = data.appName
    state.publicSetting.APP_VER = data.dbVersion
    state.publicSetting.APP_LOGO = data.logo
    state.publicSetting.APP_PAGE_FOOTER = data.pageFooter
    state.publicSetting.APP_TITLE = data.appTitle
    state.publicSetting.APP_SUB_TITLE = data.appSubtitle
    state.publicSetting.APP_INTRO = data.appIntro
    state.publicSetting.APP_WATERMARK = data.watermark
    state.publicSetting.APP_PLUGINID = data.pluginIdList
    state.publicSetting.APP_COLOR = data.themeColor
    state.publicSetting.productType = data.productType
    state.publicSetting.trialVersionFlag = data.trialVersionFlag
    state.publicSetting.pluginIdList = data.pluginIdList
    state.publicSetting.webVer += '(' + data.version + ')'
    state.publicSetting.appMode = data.appMode
    state.publicSetting.homeURL = data.homeURL
  },
  setUserInfo(state, user) {
    let userInfo = {
      userName: user.userName,
      loginName: user.loginName,
      userId: user.userId,
      dashboard: '1',
      departmentId: user.departmentId,
      jobTitle: user.jobTitle,
      email: user.email,
      mobilePhone: user.mobilePhone,
      ownerTeam: user.ownerTeam,
      departmentName: user.departmentName
    }
    tool.data.set('USER_INFO', userInfo)
  }
}

const actions = {
  async getEntityList({ commit }) {
    try {
      let res = await databaseListApi()
      commit('refreshCache', res.data)
      localStorage.setItem('entityList', JSON.stringify(res.data))
    } catch (error) {
      console.error('Failed to fetch entity list:', error)
    }
  }
}

export default {
  state,
  mutations,
  actions,
  getters
}
