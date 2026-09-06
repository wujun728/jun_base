export interface ApiBaseResponse {
  code?: number;
  msg?: string;
  [key: string]: any;
}

export interface LoginRequest {
  username: string;
  password: string;
  code?: string;
  uuid?: string;
}

export interface LoginResponse extends ApiBaseResponse {
  token: string;
}

export interface SysUser {
  userName: string;
  nickName?: string;
  avatar?: string;
  loginDate?: string;
  loginIp?: string;
  [key: string]: any;
}

export interface GetInfoResponse extends ApiBaseResponse {
  roles: string[];
  permissions: string[];
  user: SysUser;
}

