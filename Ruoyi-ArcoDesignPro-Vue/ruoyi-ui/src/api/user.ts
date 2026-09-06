import axios from 'axios';
import {RouteRecordNormalized} from "vue-router";
import {UserAction} from "@/store/modules/user/types";

export interface LoginData {
    username: string;
    password: string;
    code: string;
    uuid: string;
}

export interface LoginRes {
    token: string;
}

export function login(data: LoginData) {
    return axios({
        method: 'post',
        url: '/login',
        data
    });
}

export function logout() {
    return axios('/logout');
}

export function getUserInfo() {
    return axios.get<UserAction>('/getInfo');
}

export function getMenuList() {
    return axios.get<RouteRecordNormalized[]>('/getRouters');
}

//
// export function getMenuList() {
//   return axios({
//     url: '/getRouters',
//     method: 'get'
//   });
// }
