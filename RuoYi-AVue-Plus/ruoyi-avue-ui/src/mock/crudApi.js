import Mock from 'mockjs'
import menu_const from '@/const/menu'

const serverConfig = {
  config: {
    save: '/system/server/type/add',
    update: '/system/server/type/update',
    list: '/system/server/type/list'
  },
  page: {
    pageNumber: 1,
    pageSize: 5,
    total: 20,
    // currentPage: 2,
    // pageSizes: [1, 3, 5, 7, 9]
  },
  data: [],
  option: {
    pageNumber: 'pageNumber',
    pageSize: 'pageSize',
    column: [
      {
        label: "主键",
        prop: "id",
        type: 'input',
        editDetail: true,
        addDisplay: false,
      },
      {
        label: "服务名称",
        prop: "serverName",
        span: 14,
        row: true,
        search: true,
        rules: [{
          required: true,
          trigger: "blur"
        }],
      }, {
        label: "服务路径",
        prop: "domain",
        span: 14,
        row: true,
        rules: [{
          required: true,
          trigger: "blur"
        }]
      },
      {
        label: "创建人",
        prop: "creator",
        editDetail: true,
        addDisplay: false,
        row: true
      },
      {
        label: "创建日期",
        prop: "created",
        type: "datetime",
        editDetail: true,
        addDisplay: false,
        span: 14,
        row: true
      }
    ]
  }
}

const tagConfig = {
  config: {
    save: '/system/server/type/add',
    update: '/system/server/type/update',
    list: '/system/tag/type/list'
  },
  option: {
    column: [
      {
        label: "上级菜单",
        prop: "parentId",
        type: "tree",
        search: true,
        props: {
          label: 'label',
          value: 'id'
        },
        span: 14,
        row: true,
        dicData: menu_const.parent_component,
        rules: [{
          required: true,
          trigger: "blur"
        }]
      },
      {
        label: "菜单名称",
        prop: "label",
        span: 14,
        row: true,
        search: true,

        rules: [{
          required: true,
          trigger: "blur"
        }],
      }, {
        label: "所属服务",
        type: "select",
        dicData: menu_const.serverListData,
        prop: "serverId",
        span: 14,
        row: true,
        props: {
          label: 'serverName',
          value: 'id'
        },
        rules: [{
          required: true,
          trigger: "blur"
        }]
      }, {
        label: "访问路径",
        prop: "path",
        span: 14,
        row: true,
        rules: [{
          required: true,
          trigger: "blur"
        }]
      },
      {
        label: "组件编号",
        prop: "component",
        type: 'select',
        dicData: menu_const.component,
        props: {
          label: 'label',
          value: 'id'
        },
        span: 14,
        row: true,
        rules: [{
          required: true,
          trigger: "blur"
        }]
      },
      {
        label: "图标",
        prop: "icon",
        span: 14,
        row: true,
        rules: [{
          required: true,
          trigger: "blur"
        }]
      },
      {
        label: "创建人",
        prop: "creator",
        editDetail: true,
        addDisplay: false,
        row: true
      },
      {
        label: "创建日期",
        prop: "created",
        type: "datetime",
        editDetail: true,
        addDisplay: false,
        span: 14,
        row: true
      }
    ]
  }
}

const groupMap = {
  server : serverConfig,
  tag : tagConfig
}

export default ({mock}) => {
  if (!mock) return;

  let baseUrl = process.env.VUE_APP_BASE_API;

  let remoteUrl = "http://192.168.0.1:8888"

  Mock.mock(remoteUrl + '/curd/config', 'post', (res) => {
    let groupNo = res.body.split("=")[1];

    if(!groupMap[groupNo]){
      console.warn("没有匹配到合适的group");
    }

    return {
      data: groupMap[groupNo] || serverConfig
    }
  })

  Mock.mock(remoteUrl + '/system/server/type/update', 'post', () => {
    return {
      data: top
    }
  })

}
