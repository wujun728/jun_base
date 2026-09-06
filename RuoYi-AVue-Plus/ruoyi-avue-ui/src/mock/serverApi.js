import Mock from 'mockjs'

const serverList = {
  pageSize: 5,
  // pagerCount: 5,
  total: 20,
  data: [{
    id: 1,
    serverName: 'elab-user',
    domain: 'http://127.0.0.1:8765',
    creator: 'liukx',
    created: Mock.mock('@datetime')
  }, {
    id: 2,
    serverName: 'elab-gateway',
    domain: 'http://192.168.0.1:8888',
    creator: 'liukx',
    created: Mock.mock('@datetime')
  }, {
    id: 3,
    serverName: 'elab-sms',
    domain: 'http://192.168.0.1:8888',
    creator: 'liukx',
    created: Mock.mock('@datetime')
  }, {
    id: 4,
    serverName: 'elab-abc',
    domain: 'http://192.168.0.1:8888',
    creator: 'liukx',
    created: Mock.mock('@datetime')
  }, {
    id: 5,
    serverName: 'elab-sfd',
    domain: 'http://192.168.0.1:8888',
    creator: 'liukx',
    created: Mock.mock('@datetime')
  }]
}

const tagList = {
  pageSize: 5,
  // pagerCount: 5,
  total: 20,
  data: [
    {
      id: 1,
      parentId: 2,
      label: "标签11",
      serverId: 1,
      path: '/tags',
      component: 1,
      icon: 'icon-caidan',
      creator: 'liukx',
      created: '2021-02-01 23:55:33'
    },
    {
      id: 2,
      parentId: 1,
      label: "标签2",
      serverId: 2,
      path: '/tags',
      component: 1,
      icon: 'icon-caidan',
      creator: 'liukx',
      created: '2021-02-01 23:55:33'
    }
  ]
}

export default ({mock}) => {
  if (!mock) return;

  let baseUrl = process.env.VUE_APP_BASE_API;

  let remoteUrl = "http://192.168.0.1:8888"

  Mock.mock(remoteUrl + '/system/tag/type/list', 'post', (res) => {
    return {
      data: tagList || []
    }
  })

  Mock.mock(baseUrl + '/system/server/type/list', 'post', (res) => {
    return {
      data: serverList || []
    }
  })

  Mock.mock(remoteUrl + '/system/server/type/list', 'post', (res) => {
    return {
      data: serverList || []
    }
  })

  Mock.mock(baseUrl + '/system/server/type/object', 'post', (res) => {
    let body = JSON.parse(res.body);
    return {
      data: serverList.data[body.id - 1] || serverList.data[Mock.Random.integer(0, 4)]
    }
  })

  Mock.mock(remoteUrl + '/system/server/type/object', 'post', (res) => {
    return {
      data: serverList.data[Mock.Random.integer(0, 4)]
    }
  })

}
