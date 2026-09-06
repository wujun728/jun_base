/**
 * 服务注册的页面JSON配置数据
 * @author liukx
 * @date  2021年8月20日 14:22:19
 */
export default (safe) => {
  return {
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
        prop: "domainPath",
        span: 14,
        row: true,
        rules: [{
          required: true,
          trigger: "blur"
        }]
      }, {
        label: "授权token",
        prop: "acceptToken",
        labelWidth: "100",
        span: 14,
        row: true,
        rules: [{
          required: true,
          trigger: "blur"
        }]
      },
      {
        label: "状态",
        type: "switch",
        dicData: [{
          label: '无效',
          value: '-1'
        }, {label: '有效', value: '1'}],
        prop: "status",
        // editDetail: true,
        addDisplay: false,
        span: 14,
        row: true,
        value: '1'
      },
      {
        label: "创建人",
        prop: "createBy",
        editDetail: true,
        addDisplay: false,
        row: true
      },
      {
        label: "创建日期",
        prop: "createTime",
        type: "datetime",
        editDetail: true,
        addDisplay: false,
        span: 14,
        row: true
      },
      {
        label: "修改人",
        prop: "updateBy",
        editDisplay: false,
        addDisplay: false,
        row: true
      },
      {
        label: "修改时间",
        prop: "updateTime",
        type: "datetime",
        editDisplay: false,
        addDisplay: false,
        span: 14,
        row: true
      }
    ]
  }
}
