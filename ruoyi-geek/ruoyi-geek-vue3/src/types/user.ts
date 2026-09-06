interface Point {
    x?: number;
    y?: number;
    [key: string]: any;
}

export interface CaptchaVO {
    captchaId?: string;
    projectCode?: string;
    captchaType?: string;
    captchaOriginalPath?: string;
    captchaFontType?: string;
    secretKey?: string;
    originalImageBase64?: string;
    jigsawImageBase64?: string;
    pointJson?: string;
    token?: string;
    captchaVerification?: string;
    clientUid?: string;
    browserInfo?: string;
    captchaFontSize?: number;
    ts?: number;
    result?: boolean;
    point?: Point;
    wordList?: string[];
    pointList?: Point[];
}

export interface LoginForm {
    username: string
    password?: string
    code?: string
    uuid?: string
    email?: string
    phonenumber?: string
    rememberMe?: boolean
    autoRegister?: boolean
    captcha?: CaptchaVO
}

export interface RegisterForm extends LoginForm { }

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