export interface LoginForm {
    username: string
    password: string
    code: string
    uuid: string
    email: string
    phonenumber: string
    rememberMe?: boolean
    autoRegister?: boolean
}

export interface RegisterForm {
    username: string
    password: string
    code: string
    uuid: string
    email: string
    phonenumber: string
}


export interface DeptInfo {
    deptId: number;
    deptName: string;
    ancestors?: string;
    leader?: string;
    orderNum?: number;
    parentId?: number;
    status?: string;
}

export interface RoleInfo {
    roleId: number;
    roleName: string;
    roleKey?: string;
    status?: string;
}

export interface UserInfo {
    userId: number;
    userName: string;
    nickName: string;
    avatar: string | null;
    dept: DeptInfo;
    roles: RoleInfo[];
    phonenumber?: string;
    email?: string;
    loginDate?: string;
    loginIp?: string;
    createTime?: string;
    status?: string;
    remark?: string;
}