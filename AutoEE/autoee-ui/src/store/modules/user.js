import { login, logout, getInfo } from '../../api/login';
import { getToken, setToken, removeToken } from '/src/utils/auth';

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    roles: [],
    permissions: [],
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
    },
    SET_NAME: (state, name) => {
      state.name = name;
    },
    SET_NICKNAME: (state, nickName) => {
      state.nickName = nickName;
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar;
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles;
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions;
    },
  },

  // actions中的命名增加“_Action”结尾，便于全局查找定位
  actions: {
    // 登录
    Login_Action({ commit }, loginInfo) {
      const username = loginInfo.username;
      const password = loginInfo.password;
      const code = loginInfo.code;
      const uuid = loginInfo.uuid;
      return new Promise((resolve, reject) => {
        login(loginInfo)
          .then((res) => {
            setToken(res.token);
            commit('SET_TOKEN', res.token);
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 获取用户信息
    GetUserInfo_Action({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((res) => {
            // console.log('res=' + JSON.stringify(res));
            const user = res.user;
            // console.log('user=' + JSON.stringify(user));
            const avatar = user.avatar;
            // const avatar = user.avatar == "" ? require("/src/assets/images/profile.jpg") : user.avatar;
            if (res.roles && res.roles.length > 0) {
              // 验证返回的roles是否是一个非空数组
              commit('SET_ROLES', res.roles);
              commit('SET_PERMISSIONS', res.permissions);
            } else {
              commit('SET_ROLES', ['ROLE_DEFAULT']);
            }
            commit('SET_NAME', user.userName);
            commit('SET_NICKNAME', user.nickName);
            commit('SET_AVATAR', avatar);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 退出系统
    LogOut_Action({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token)
          .then(() => {
            commit('SET_TOKEN', '');
            commit('SET_ROLES', []);
            commit('SET_PERMISSIONS', []);
            removeToken();
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise((resolve) => {
        commit('SET_TOKEN', '');
        removeToken();
        resolve();
      });
    },
  },
};

export default user;
