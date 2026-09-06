import axios from 'axios';

export interface PostRecord {
    searchValue?: any;
    createBy?: string;
    createTime?: string;
    updateBy?: any;
    updateTime?: any;
    remark?: string;
    params?: any;
    postId?: number;
    postCode?: string;
    postName?: string;
    postSort?: number;
    status?: string;
    flag?: boolean;
}

// 查询岗位列表
export function listPost(query: any) {
    return axios({
        url: '/system/post/list',
        method: 'get',
        params: query
    })
}

// 查询岗位详细
export function getPost(postId: number) {
    return axios({
        url: `/system/post/${postId}`,
        method: 'get'
    })
}

// 新增岗位
export function addPost(data: PostRecord) {
    return axios({
        url: '/system/post',
        method: 'post',
        data
    })
}

// 修改岗位
export function updatePost(data: PostRecord) {
    return axios({
        url: '/system/post',
        method: 'put',
        data
    })
}

// 删除岗位
export function delPost(postId: number) {
    return axios({
        url: `/system/post/${postId}`,
        method: 'delete'
    })
}
