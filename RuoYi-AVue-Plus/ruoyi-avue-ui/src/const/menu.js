export default {
    component: [{
        label: "公共CURD组件",
        value: "views/util/curd/list",
        id: 1
    }],
    parent_component: [{
        id: -1,
        label: "超级菜单",
    }, {
        id: 1,
        label: "顶级菜单",
        children: [{
            id: 2,
            label: "一级菜单1",
        }, {
            id: 3,
            label: "一级菜单2",
        }, {
            id: 4,
            label: "一级菜单3",
        }]
    }],
    serverListData: [{
        id: 1,
        serverName: 'elab-user',
        domain: 'http://www.baidu.com',
        creator: 'liukx',
        created: '2021-08-08 21:32:11'
    }, {
        id: 2,
        serverName: 'elab-gateway',
        domain: 'http://www.baidu.com',
        creator: 'liukx',
        created: '2021-08-08 21:32:11'
    }, {
        id: 3,
        serverName: 'elab-sms',
        domain: 'http://www.baidu.com',
        creator: 'liukx',
        created: '2021-08-08 21:32:11'
    }]
}
