export default (safe) => {
  //safe => vue的this对象
  return {
    "title": "这是一个测试",
    "column": [
      {
        "addDisplay": true,
        "prop": "id",
        "disabled": true,
        "label": "id",
        "type": "input"
      },
      {
        "prop": "username",
        "label": "用户名称",
        "type": "input",
        "rules": [{
          "type": null,
          "required": true,
          "message": null
        }]
      },
      {
        "dicData": [
          {
            "label": "已认证",
            "value": "1"
          },
          {
            "label": "未认证",
            "value": "0"
          },
          {
            "label": "已失效",
            "value": "-99"
          },
          {
            "label": "已拒绝",
            "value": "-1"
          }
        ],
        "prop": "checkStatus",
        "label": "认证状态",
        "type": "select",
        "props": {
          "label": "label",
          "value": "value"
        }, "rules": [{
          "type": "string",
          "required": true,
          "pattern": null,
          "len": null,
          "min": null,
          "max": null,
          "message": null
        }]
      },
      {
        "dicData": [
          {
            "id": "1",
            "username": "周杰伦"
          },
          {
            "id": "2",
            "username": "林俊杰"
          },
          {
            "id": "3",
            "username": "王力宏"
          },
          {
            "id": "4",
            "username": "许嵩"
          }
        ],
        "prop": "likeStar",
        "label": "喜欢明星",
        "type": "select",
        "props": {
          "label": "username",
          "value": "id"
        }
      },
      {
        "labelTip": "这是选择年龄的地方",
        "prop": "age",
        "label": "年龄",
        "type": "number"
      },
      {
        "border": true,
        "dicData": [
          {
            "label": "男",
            "value": 1
          },
          {
            "label": "女",
            "value": 0
          },
          {
            "label": "未知",
            "value": -1
          }
        ],
        "prop": "sex",
        "label": "性别",
        "type": "radio",
        "props": {
          "label": "label",
          "value": "value"
        }
      },
      {
        "prop": "validDate",
        "label": "有效时间",
        "type": "datetime"
      },
      {
        "dicData": [
          {
            "label": "篮球",
            "value": "basketball"
          },
          {
            "label": "足球",
            "value": "football"
          },
          {
            "label": "羽毛球",
            "value": "badminton"
          },
          {
            "label": "乒乓球",
            "value": "pingpong"
          },
          {
            "label": "桌球",
            "value": "billiards"
          }
        ],
        "prop": "interest",
        "label": "兴趣爱好",
        "type": "checkbox",
        "props": {
          "label": "label",
          "value": "value"
        }
      },
      {
        "prop": "status",
        "label": "状态",
        "type": "switch"
      }
    ]
  }
}
