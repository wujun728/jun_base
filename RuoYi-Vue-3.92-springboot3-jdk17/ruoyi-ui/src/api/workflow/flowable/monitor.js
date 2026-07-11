import request from '@/utils/request'


// 获取流程实例
export const getListProcess = query => {
    return request({
        url: '/flowable/monitor/listProcess',
        method: 'get',
        params: query
    });
};

// 挂起/唤醒流程实例
export const enableProcess = (processInstanceId, enable) => {
    const url = (
        enable
            ? `/flowable/monitor/run/${processInstanceId}`
            : `/flowable/monitor/suspend/${processInstanceId}`
    );
    return request({
        url,
        method: 'get',
    });
}

// 获取流程历史
export const getListHistoryProcess = query => {
    return request({
        url: '/flowable/monitor/listHistoryProcess',
        method: 'get',
        params: query,
    });
}

// 获取某个历史
export const getListByTypeAndId = (type, processInstanceId) => {
    const url = `/flowable/monitor/history/${processInstanceId}`
    const query = {
        pageSize: 100,
        pageNum: 1,
        isAsc: "asc"
    };
    return request({
        url,
        method: 'get',
        params: query
    });
}

// 获取可跳转环节列表
export const getJumpActivityList = query => {
    return request({
        url: '/flowable/monitor/jumpActivityList',
        method: 'get',
        params: query
    })
}

// 获取跳转环节
export const getJumpActivityNode = query => {
    return request({
        url: '/flowable/monitor/jumpActivityNode',
        method: 'get',
        params: query
    })
}

// 获取当前环节的任务
export const getFlowNodeTasks = query => {
    return request({
        url: '/flowable/monitor/flowNodeTasks',
        method: 'get',
        params: query
    })
}

// 获取当前环节的已办任务
export const getFinishFlowNodeTasks = query => {
    return request({
        url: '/flowable/monitor/flowFinishNodeTasks',
        method: 'get',
        params: query
    })
}
