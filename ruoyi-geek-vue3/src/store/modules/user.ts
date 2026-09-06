import { login, logout, getInfo, register, verifyPhoneCode, verifyEmailCode } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import defAva from '@/assets/images/profile.jpg'
import { defineStore } from 'pinia'
import { LoginForm, RegisterForm, RoleInfo, UserInfo } from '@/types/user'
import { ElMessageBox } from 'element-plus'
import { router } from '@/router'

const useUserStore = defineStore(
  'user',
  {
    state: () => ({
      token: getToken(),
      name: '',
      nickName: '',
      avatar: '',
      roleName: '',
      deptName: '',
      loginDate: '',
      roles: [] as string[],
      permissions: [] as string[],
      isDefaultModifyPwd: null as boolean | null,
      isPasswordExpired: null as boolean | null
    }),
    actions: {
      // 登录
      login(userInfo: LoginForm, method: 'password' | 'phone' | 'email' = 'password') {
        let handel = {
          'password': login,
          'phone': (userInfo: LoginForm) => verifyPhoneCode(userInfo, 'login'),
          'email': (userInfo: LoginForm) => verifyEmailCode(userInfo, 'login')
        }
        return new Promise((resolve, reject) => {
          handel[method](userInfo).then((res: any) => {
            const token = res.token ?? res.data ?? res.msg
            setToken(token)
            this.token = token
            resolve(null)
          }).catch(error => {
            reject(error)
          })
        })
      },
      register(registerForm: RegisterForm, method: 'password' | 'phone' | 'email' = 'password') {
        let handle = {
          'password': register,
          'phone': (registerForm: RegisterForm) => verifyPhoneCode(registerForm, 'register'),
          'email': (registerForm: RegisterForm) => verifyEmailCode(registerForm, 'register')
        }
        return handle[method](registerForm)
      },
      // 获取用户信息
      getInfo() {
        return new Promise<{ user: UserInfo, roles: RoleInfo[], permissions: string[] }>((resolve, reject) => {
          getInfo().then((res: any) => {
            const user = res.user
            if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
              this.roles = res.roles
              this.permissions = res.permissions
            } else {
              this.roles = ['ROLE_DEFAULT']
            }
            this.name = user.userName
            this.nickName = user.nickName
            this.roleName = (user.roles || [])[0] ? user.roles[0].roleName : '普通角色'
            this.deptName = user.dept ? user.dept.deptName : '暂无部门'
            this.loginDate = user.loginDate
            if (user.avatar == "" || user.avatar == null) {
              this.avatar = defAva
            } else {
              this.avatar = user.avatar.startsWith('http') ? user.avatar : import.meta.env.VITE_APP_BASE_API + user.avatar;
            }
            /* 初始密码提示 */
            if (res.isDefaultModifyPwd && this.isPasswordExpired === null) {
              this.isPasswordExpired = res.isPasswordExpired
              ElMessageBox.confirm('您的密码还是初始密码，请修改密码！', '安全提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => {
                router.push({ name: 'Profile', params: { activeTab: 'resetPwd' } })
              }).catch(() => { })
            }
            if (!res.isDefaultModifyPwd && res.isPasswordExpired && this.isPasswordExpired === null) {
              this.isDefaultModifyPwd = res.isDefaultModifyPwd
              ElMessageBox.confirm('您的密码已过期，请尽快修改密码！', '安全提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => {
                router.push({ name: 'Profile', params: { activeTab: 'resetPwd' } })
              }).catch(() => { })
            }
            resolve(res)
          }).catch(error => {
            reject(error)
          })
        })
      },
      // 退出系统
      logOut() {
        return new Promise((resolve, reject) => {
          logout().then(() => {
            this.token = ''
            this.roles = []
            this.permissions = []
            removeToken()
            resolve(null)
          }).catch(error => {
            reject(error)
          })
        })
      }
    }
  })

export default useUserStore
