// 请求响应参数（不包含data）
export interface Result {
  code: string;
  msg: string;
}

// 请求响应参数（包含data）
export interface ResultData<T = any> extends Result {
  data: T;
}

// 分页响应参数
export interface ResPage<T> {
  list: T[];
  rows: T[];
  pageNum: number;
  pageSize: number;
  total: number;
}

// 分页请求参数
export interface ReqPage {
  pageNum: number;
  pageSize: number;
}

// 文件上传模块
export namespace Upload {
  export interface ResFileUrl {
    fileUrl: string;
  }
}

// 登录模块
export namespace Login {
  export interface ReqLoginForm {
    username: string;
    password: string;
    code: string;
    uuid: string;
  }
  export interface ResLogin {
    access_token: string;
  }
  export interface ResAuthButtons {
    [key: string]: string[];
  }
}

// 用户管理模块
export namespace User {
  export interface ReqUserParams extends ReqPage {
    username: string;
    gender: number;
    idCard: string;
    email: string;
    address: string;
    createTime: string[] | undefined;
    status: number;
  }
  export interface ResUserList {
    id: string;
    username: string;
    gender: number;
    user: { detail: { age: number } };
    idCard: string;
    email: string;
    address: string;
    createTime: string;
    status: number;
    avatar: string;
    photo: any[];
    children?: ResUserList[];
  }
  export interface ResStatus {
    userLabel: string;
    userValue: number;
  }
  export interface ResGender {
    genderLabel: string;
    genderValue: number;
  }
  export interface ResDepartment {
    id: string;
    name: string;
    children?: ResDepartment[];
  }
  export interface ResRole {
    id: string;
    name: string;
    children?: ResDepartment[];
  }
  export interface ResUserInfo {
    userId: string;
    userName: string;
    nickName: string;
    deptName: number;
    password: string;
    phonenumber: string;
    postIds: string;
    deptId: string;
    roleIds: string[];
    sex: string;
    email: string;
    status: string | number;
    createTime: string;
    remark: string;
  }
}
export namespace Dept {
  export interface ResDeptInfo {
    deptId: string;
    deptName: string;
    status: string | number;
  }
}
export namespace Role {
  export interface ResRoleInfo {
    roleIds: string;
    roleId: string;
    roleName: string;
    roleKey: string;
    roleSort: number;
    status: string | number;
    menuCheckStrictly: boolean;
    deptCheckStrictly: boolean;
    remark: string;
    menuIds: string[];
    dataScope: string;
    deptIds: string;
  }
}

export namespace Post {
  export interface ResPostInfo {
    postId: string;
    postName: string;
    postCode: string;
    postSort: number;
    status: string | number;
    remark: string;
  }
}

export namespace Config {
  export interface ResConfigInfo {
    configId: string;
    configName: string;
    configKey: string;
    configValue: string;
    configType: "Y" | "N";
    remark: string;
    createTime: string;
  }
}
export namespace Operlog {
  export interface ResOperlogInfo {
    operId: string;
    businessType: string;
    status: string;
    operName: string;
    operIp: string;
    operLocation: string;
    operUrl: string;
    requestMethod: string;
    operParam: string;
    jsonResult: string;
    costTime: string;
    operTime: string;
    errorMsg: string;
  }
}
export namespace Logininfor {
  export interface ResLogininforInfo {}
}

export namespace Dict {
  export interface ResDictInfo {
    dictId: string;
    dictName: string;
    dictType: string;
    status: string;
    remark: string;
    dictCode: string;
  }
}

export namespace Notice {
  export interface ResNoticeInfo {
    noticeId: string;
    noticeTitle: string;
    noticeType: string;
    createBy: string;
    status: string;
    createTime: string;
  }
}

export namespace OnLine {
  export interface ResOnLineInfo {
    noticeId: string;
    noticeTitle: string;
    noticeType: string;
    createBy: string;
    status: string;
    createTime: string;
  }
}

export namespace Job {
  export interface ResJobInfo {
    jobId: string;
    jobName: string;
    jobGroup: string;
    invokeTarget: string;
    cronExpression: string;
    misfirePolicy: number;
    concurrent: number;
    status: string;
  }
}

export namespace JobLog {
  export interface ResJobLogInfo {
    jobName: string;
    jobGroup: string;
    status: string;
    jobLogId: string;
    invokeTarget: string;
    jobMessage: string;
    createTime: string;
  }
}

export namespace Gen {
  export interface ResGenInfo {
    tableName: string;
    tableComment: string;
    className: string;
    createTime: string;
    updateTime: string;
  }
}
