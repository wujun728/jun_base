import { login, phoneLogin, logout, userScanStatusApi } from '@/api/user'
import { loginInfo as getInfo } from '@/api/user'
import { frameUserApi,frameTreeApi } from '@/api/public'
import { getDictTreeListApi } from '@/api/form'
import router, { resetRouter } from '@/router'
import { AiEmbeddedManage } from '@/libs/ai-embedded'

const state = {
  token: localStorage.getItem('token') || '',
  name: '',
  avatar: '',
  introduction: '',
  unique:'', // 调查问卷判断是否登录
  roles: [],
  menuList: [],
  menuAuthor: [],
  activeField:{}, // 低代码详情编辑，当前编辑的字段
  formDicList: [], // 字典列表-就不用重复请求接口
  departmentList: [], // 部门列表-就不用重复请求接口
  memberList: [], // 人员列表-就不用重复请求接口
  isLogin: false,
  userInfo: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : null,
  enterprise: localStorage.getItem('enterprise') ? JSON.parse(localStorage.getItem('enterprise')) : null,
  messageCount: 0,
  permissions: []
}
const mutations = {
  SET_MENU_LIST: (state, menuList) => {
    state.menuList = menuList
  },
  SET_PERMISSIONS: (state, permissions) => {
    state.permissions = permissions
  },
  SET_MENU_AUTHOR: (state, menuAuthor) => {
    state.menuAuthor = menuAuthor
  },
  SET_FIELD: (state, activeField) => {
    state.activeField = activeField
  },
  SET_TOKEN: (state, token) => {
    state.token = token
    localStorage.setItem('token', token)
  },
  SET_UNIQUE: (state, unique) => {
    state.unique = unique
    localStorage.setItem('unique', unique)
  },
  SET_ISLOGIN: (state, isLogin) => {
    state.isLogin = isLogin
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  // 修改字典列表数据
  SET_FORMDIC: (state, data) => {
    state.formDicList.push(data)
  },
  // 修改部门列表数据
  SET_DEPARTMENT: (state, data) => {
    state.departmentList=data
  },
  // 修改人员列表数据
  SET_MEMBER: (state, data) => {
    state.memberList = data
  },
  // 重置字典列表数据
  REMOVE_FORMDIC: (state, data) => {
    state.formDicList = data
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_USERINFO: (state, data) => {
    state.userInfo = data
    localStorage.setItem('userInfo', JSON.stringify(data))
  },
  SET_ENTINFO: (state, data) => {
    state.enterprise = data
    localStorage.setItem('enterprise', JSON.stringify(data))
  },
  SET_MESSAGE: (state, count) => {
    state.messageCount = count
  }
}

const actions = {
  // 获取自定义表单关联字典数据
  getDictList({ commit, state }, val) {
    return new Promise(async (resolve, reject) => {
      let is_next = state.formDicList.some((item) => item.dict_ident == val.types)
      if (!state.formDicList || !is_next) {
        const response = await getDictTreeListApi(val)
        await commit('SET_FORMDIC', { dict_ident: val.types, list: response.data })
        await resolve(state.formDicList)
      }
      resolve(state.formDicList)
    })
  },

  // 获取人员数据
  getMember({ commit, state }){
    return new Promise(async (resolve, reject) => {
       let data = {
        role: 0,
        leave: 0
      }
        const response = await frameUserApi(data)
        await commit('SET_MEMBER', response.data[0].children)
        await resolve(state.memberList)
      resolve(state.memberList)
    })
  },
  // 获取部门数据
  getDepartment({ commit, state }){
    return new Promise(async (resolve, reject) => {
        const response = await frameTreeApi()
        await commit('SET_DEPARTMENT', response.data)
        await resolve(state.departmentList)
      resolve(state.departmentList)
    })
  },

  login({ commit }, data) {
    return new Promise(async (resolve, reject) => {
      if (data.activeName === 'passwordLogin') {
        const response = await login(data.userInfo)
        const token = response.token || (response.data && response.data.token)
        await commit('SET_TOKEN', token)
        await commit('SET_ISLOGIN', true)
        await resolve(response.data || {})
      } else {
        // 手机验证码登录暂不接若依（保留原样，接口未来实现）
        const response = await phoneLogin(data.userInfo)
        await commit('SET_TOKEN', response.data && response.data.token)
        await commit('SET_ISLOGIN', true)
        await resolve(data)
      }
    })
  },
  scanLogin({ commit }, data) {
    return new Promise(async (resolve, reject) => {
      const response = await userScanStatusApi(data)
      if (response.data.status === undefined) {
        commit('SET_TOKEN', response.data.token)
        commit('SET_ISLOGIN', true)
      }
      resolve(response.data)
    })
  },
  // get user info（若依 /getInfo：{user,roles,permissions}）
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo()
        .then((response) => {
          const user = response.user || (response.data && response.data.user) || {}
          const roles = response.roles || (response.data && response.data.roles) || []
          const permissions = response.permissions || (response.data && response.data.permissions) || []

          if (!roles || roles.length <= 0) {
            reject('getInfo: roles must be a non-null array!')
          }

          commit('SET_ROLES', roles)
          commit('SET_NAME', user.nickName || user.userName || '')
          commit('SET_AVATAR', user.avatar || '')
          commit('SET_INTRODUCTION', user.nickName || '')
          commit('SET_PERMISSIONS', permissions)
          resolve({ roles, permissions, user })
        })
        .catch((error) => {
          reject(error)
        })
    })
  },
  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token)
        .then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_MENU_LIST', [])
          commit('SET_MENU_AUTHOR', [])
          commit('SET_USERINFO', '')
          commit('SET_UNIQUE', '')

          commit('SET_ISLOGIN', false)
          localStorage.removeItem('userInfo')
          localStorage.removeItem('menuList')
          localStorage.removeItem('menuAuthor')
          localStorage.removeItem('enterprise')
          localStorage.removeItem('token')
          localStorage.removeItem('menuTabData')
          localStorage.removeItem('articleList')
          localStorage.removeItem('sidebarParentCur')
          localStorage.removeItem('categoryList')
          resetRouter()
          AiEmbeddedManage.destroyAll()
          resolve()
        })
        .catch((error) => {
          reject(error)
        })
    })
  },
  // remove token
  resetToken({ commit }) {
    return new Promise((resolve) => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      commit('SET_MENU_LIST', '')
      commit('SET_USERINFO', '')
      commit('SET_ENTINFO', '')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('entMenuList')
      localStorage.removeItem('enterprise')
      localStorage.removeItem('sidebarParentCur')
      localStorage.removeItem('entMenuAuthor')
      localStorage.removeItem('menuTabData')
      localStorage.removeItem('categoryList')
      AiEmbeddedManage.destroyAll()
      resolve()
    })
  },

  // dynamically modify permissions
  changeRoles({ commit, dispatch }, role) {
    return new Promise(async (resolve) => {
      const token = role + '-token'
      commit('SET_TOKEN', token)
      const { roles } = await dispatch('getInfo')
      resetRouter()
      const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
      router.addRoutes(accessRoutes)

      dispatch('tagsView/delAllViews', null, { root: true })
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
