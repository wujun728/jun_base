<!--
  该模版是用来调试静态JSON情况的，不和后台服务挂钩，全部都是静态参数，调试完毕之后，可以将代码迁移至server-crud.vue中
-->
<template>
  <div class="basic-container">
    <!--    <el-card class="basic-container__card">-->
    <h3>固定的JSON参数测试</h3>
    <!--
    @search-change : 搜索发生改变回调的方法   https://avuejs.com/crud/crud-search.html#%E6%99%AE%E9%80%9A%E7%94%A8%E6%B3%95
    -->
    <avue-crud :option="option"
               :page="page"
               ref="crud"
               :data="data"
               @row-update="updateBefore" @row-del="delBefore" @search-change="listAfter">
      <!-- https://avuejs.com/crud/crud-btn.html 参考 -->
      <!--
        slot取值的几种方式:
          - menu : 每一行的操作位置
          - menuForm : 新增修改右下角的位置 -> 新增->  v-if="type=='add'"  修改-> v-if="type=='edit'"
          - menuLeft: 表格左上角的新增位置
          - menuRight : 表格的右上角内容
          - searchMenu: 搜索的位置 > https://avuejs.com/crud/crud-search.html#%E6%99%AE%E9%80%9A%E7%94%A8%E6%B3%95
      -->
      <template v-if="option.tableRowButtons && option.tableRowButtons.length > 0" slot-scope="{row,index}" slot="menu">
        <span v-for="item in option.tableRowButtons">
<!--          <el-button type="primary" :disabled="index==0" @click="$refs.crud.rowEdit(row,index)">编辑</el-button>-->
          <el-button :type="item.type || 'text'"
                     :icon="item.icon || 'el-icon-edit'"
                     :size="item.size || 'small'"
                     plain
                     @click="btnClick(item,row,index)">{{ item.btnName }}
          </el-button>
        </span>
      </template>

      <template v-if="option.tableTopLeftButtons && option.tableTopLeftButtons.length>0" slot-scope="{row,index}"
                slot="menuLeft">
        <span v-for="item in option.tableTopLeftButtons">
          <el-button :type="item.type || 'text'"
                     :icon="item.icon || 'el-icon-edit'"
                     :size="item.size || 'small'"
                     @click="btnClick(item,row,index)">{{ item.btnName }}
          </el-button>
        </span>
      </template>

      <template v-if="option.tableTopRightButtons && option.tableTopRightButtons.length > 0" slot-scope="{row,index}"
                slot="menuRight">
        <span v-for="item in option.tableTopRightButtons">
          <el-button :type="item.type || 'text'"
                     :icon="item.icon || 'el-icon-edit'"
                     :size="item.size || 'small'"
                     plain
                     @click="btnClick(item,row,index)">{{ item.btnName }}
          </el-button>
        </span>
      </template>

      <!-- 表单层 特定属性 -->
      <template
        slot-scope="{ column,value,size,disabled,type}" v-for="item in option.registerFieldComponents"
        :slot="getSlotName(item)">
        <el-tag>{{ column }}</el-tag>
        <!-- 引入新的组件  json  JSON.stringify(jsonStr,null,2)-->
        <span v-if="column.type === 'json'">
          <b-code-editor :name="column.prop" :value="getJsonString(value)"
                         :smart-indent="true"
                         theme="idea"
                         :indent-unit="4" :lint="true"
                         :line-wrap="false" ref="editor"
                         @on-change="onJsonChange(column.prop,value)"
                         @on-init="initJsonValue(column.prop,value)"
                         @on-blur="jsonFormat(column.prop)"
          >
          </b-code-editor>
          <p>
            <el-button @click="jsonFormat(column.prop)">手动触发格式化</el-button>
          </p>
      </span>

        <span v-if="column.type === 'table'">
          <el-tag>{{ value }}</el-tag>
          <avue-crud ref="crud" :option="column.children" :data="value || []" @row-update="updateBefore"
                     @row-del="delBefore"
                     @search-change="listAfter">
          </avue-crud>
        </span>
      </template>

    </avue-crud>

    <avue-dialog ref="avueDialogForm" :dialog-option="dialogConfig.dialogOption"
                 :show-dialog-props="dialogConfig.showDialogProps"
                 :form-option="dialogConfig.formOption" :object-data="dialogConfig.objectData"
                 @submit="testDialogClickSubmit" @closeDialog="testDialogClickClose" @resetForm="testDialogResetForm">
    </avue-dialog>

  </div>
</template>

<script>

import * as eventMethod from "@/api/crud/event/rowClickEvent"
import aVueDialog from "./avue-dialog"
import dialogSubmitEvent from "@/api/crud/event/dialogSubmitEvent"
// 注册组件

const jsonData = `{
    "employees": [{
      "firstName": "Bill",
      "lastName": "Gates"
    }, {
      "firstName": "George",
      "lastName": "Bush"
    }, {
      "firstName": "Thomas",
      "lastName": "Carter"
    }]
  }`

export default {
  beforeCreate() {

  },
  components: {
    'avue-dialog': aVueDialog
  },
  created() {
    let option = this.option;
    let column = option.column;
    let _self = this;

  },
  data() {
    return {
      jsonTemp: "",
      jsonStr: jsonData,
      dialogConfig: {
        config: {},
        showDialogProps: false,
        formOption: {},
        objectData: {},
        dialogOption: {}
      },
      "config": {
        "save": "/liukx/update",
        "update": "/liukx/update",
        "list": "/liukx/list",
        "domain": "http://127.0.0.1:8765",
        acceptToken: "abc12345",
        successField: "success",
        successKeyword: "true"
      },
      "data": [
        {
          "id": "0",
          "username": "测试数据_0",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 0,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
          , "dataJson": JSON.stringify({
            "test": "a",
            "test1": "b",
            "test2": "c",
          })
          , "extComponents": JSON.stringify({
            "extTest": "a",
            "extTest1": "b",
            "extTest2": "c",
          })
        },
        {
          "id": "1",
          "username": "测试数据_1",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 2,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "2",
          "username": "测试数据_2",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 4,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "3",
          "username": "测试数据_3",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 6,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "4",
          "username": "测试数据_4",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 8,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "5",
          "username": "测试数据_5",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 10,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "6",
          "username": "测试数据_6",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 12,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "7",
          "username": "测试数据_7",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 14,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "8",
          "username": "测试数据_8",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 16,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "9",
          "username": "测试数据_9",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 18,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        }
      ],
      "form": {},
      "params": {},
      "api": {},
      "loading": false,
      "page": {
        "pageSize": 10,
        "total": 100
      },
      "option": {
        "title": "这是一个测试",
        viewBtn: true,
        dialogDrag: true,
        registerFieldComponents: [
          "dataJson",
          "nodeTableModel",
          "extJson"
        ],
        tableRowButtons: [
          {
            btnName: '行按钮1',
            methodName: 'testA',
            type: "text",
            icon: "el-icon-check",
            size: "mini"
          },
          {
            btnName: '行按钮2',
            methodName: 'testB'
          },
          {
            btnName: '确认按钮',
            methodName: 'confirmClickRemoteApi',
            type: "text",
            icon: "el-icon-check",
            size: "mini",
            attrExt: {
              title: '小心点喔,你确定要点击吗?有惊喜喔!',
              url: '/test/abc'
            }
          },
          {
            btnName: '弹层表单按钮',
            methodName: 'openWindowJsonRemote',
            type: "text",
            icon: "el-icon-check",
            size: "mini",
            attrExt: {
              group: 'test-config',
              url: '/avue/crud',
              submitUrl: '/test/body'
            }
          }],
        tableTopLeftButtons: [{
          btnName: '左上角1',
          methodName: 'testA'
        },
          {
            btnName: '左上角2',
            methodName: 'testB'
          }],
        tableTopRightButtons: [
          {
            btnName: '右上角1',
            methodName: 'testA'
          },
          {
            btnName: '右上角2',
            methodName: 'testB'
          }],
        "column": [
          {
            "addDisplay": true,
            "prop": "id",
            "disabled": true,
            "label": "id",
            "type": "input"
          },
          {
            label: "时间范围",
            prop: 'timerange',
            type: 'timerange',
            hide: true,
            search: true,
            searchRange: true,
            defaultTime: "['12:00:00', '08:00:00']",
            format: 'HH:mm:ss',
            valueFormat: 'HH:mm:ss',
            startPlaceholder: '时间开始范围自定义',
            endPlaceholder: '时间结束范围自定义',
          }, {
            label: '日期',
            prop: 'date2',
            type: 'datetime',
            searchSpan: 12,
            searchRange: true,
            search: true,
          },
          {
            "prop": "username",
            "label": "用户名称",
            "type": "input",
            search: true,
            "rules": [{
              "type": null,
              "required": true,
              "message": "用户昵称你得填啊老哥"
            }]
            , "onClick": "btn"
            , "fieldClick": "btnTestClick"
            // searchRules: [{
            //   required: false,
            //   message: "请输入姓名",
            //   trigger: "blur"
            // }],
            // hide: true
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
            "type": "number",
            "viewDisplay": false
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
            label: '照片墙',
            prop: 'imgUrlList',
            type: 'upload',
            span: 24,
            limit: 2,
            listType: 'picture-card',
            tip: '只能上传jpg/png文件，且不超过500kb',
            loadText: '附件上传中，请稍等',
            propsHttp: {
              home: 'https://avuejs.com',
              res: 'single',
              name: 'fileName'
            },
            action: 'http://127.0.0.1:8765/upload'
          },
          {
            label: '单图',
            prop: 'imgUrl3',
            type: 'upload',
            listType: 'picture-img',
            // canvasOption: {
            //   text: 'avue',
            //   ratio: 0.1
            // },
            tip: '只能上传jpg/png用户头像，且不超过500kb',
            drag: true,
            propsHttp: {
              res: 'single',
              name: 'fileName'
            },
            action: 'http://127.0.0.1:8765/upload'
          },
          {
            label: '文本图',
            prop: 'imgUrlText',
            type: 'upload',
            span: 24,
            listType: 'text',
            tip: '只能上传jpg/png文件，且不超过500kb',
            loadText: '附件上传中，请稍等',
            propsHttp: {
              res: 'single',
              name: 'fileName'
            },
            action: 'http://127.0.0.1:8765/upload'
          },
          {
            "prop": "status",
            "label": "状态",
            "type": "switch"
          }
          , {
            "prop": "dataJson",
            "label": "拓展参数",
            "type": "json"
          }
          , {
            "prop": "remark",
            "label": "备注",
            "type": "textarea"
          }
          , {
            "prop": "nodeTableModel",
            "label": "子表单测试",
            "type": "table",
            "row": true,
            span: 24,
            "children": {
              "column": [
                {
                  "addDisplay": false,
                  "prop": "id",
                  "editDisabled": true,
                  "label": "主键",
                  "row": true,
                  "type": "input"
                },
                {
                  "prop": "configGroup",
                  "rules": [
                    {
                      "message": "组名称要填咧",
                      "type": "string",
                      "required": true
                    },
                    {
                      "min": 5,
                      "max": 10,
                      "message": "我跟你讲最小5个,最大10个.",
                      "type": "string"
                    }
                  ],
                  "label": "组名称",
                  "row": true,
                  "type": "input"
                },
                {
                  "prop": "configName",
                  "label": "配置名称",
                  "row": true,
                  "type": "input",
                  "rules": [
                    {
                      "message": "配置名称 是必填项!",
                      "required": true
                    }
                  ]
                },
                {
                  "dicData": [
                    {
                      "label": "无效",
                      "value": -1
                    },
                    {
                      "label": "有效",
                      "value": 1
                    }
                  ],
                  "prop": "configCode",
                  "label": "配置值",
                  "row": true,
                  "type": "select",
                  "props": {
                    "label": "label",
                    "value": "value"
                  },
                  "rules": [
                    {
                      "message": "配置值 是必填项!",
                      "required": true
                    }
                  ]
                },
                {
                  "prop": "validDay",
                  "label": "有效天数",
                  "row": true,
                  "type": "number",
                  "rules": [
                    {
                      "message": "有效天数 是必填项!",
                      "required": true
                    }
                  ]
                },
                {
                  "dicData": [
                    {
                      "label": "无效",
                      "value": -1
                    },
                    {
                      "label": "有效",
                      "value": 1
                    }
                  ],
                  "prop": "status",
                  "label": "状态",
                  "row": true,
                  "type": "switch",
                  "value": 0,
                  "props": {
                    "label": "label",
                    "value": "value"
                  }
                }
              ]
            }
          }
          // , {
          //   prop: "extJson",
          //   label: "拓展组件",
          //   labelWidth: 40,
          //   span: 24,
          //   type: "json",
          //   params: {},
          //   theme: "idea",
          //   height: 120
          // }
        ]
      },
      rotateOption: {
        span: 8,
        data: [
          {
            click: function (item) {
              alert(JSON.stringify(item));
            },
            count: '150',
            title: '新订单',
            icon: 'el-icon-warning',
            color: 'rgb(49, 180, 141)'
          }, {
            click: function (item) {
              alert(JSON.stringify(item));
            },
            count: '53%',
            title: '跳出率',
            icon: 'el-icon-view',
            color: '#00a65a'
          }, {
            click: function (item) {
              alert(JSON.stringify(item));
            },
            count: '44',
            title: '用户注册数',
            icon: 'el-icon-setting',
            color: '#f39c12'
          }
        ]
      }
    };
  }, methods: {
    commitUpdate(row, index, done, loading) {
      console.log("提交更新", row);
      this.$message.success('修改成功')
    },
    eventDel(row, index) {
    },
    //列表后操作方法
    listAfter(row, done) {
      debugger;
    },

    //新增前操作方法
    addBefore(row, done, loading, data) {
      this.form.createUser = 'small'
      debugger;
    },
    //新增后操作方法
    addAfter(row, done, loading, data) {
      this.$message.success('新增成功');
    },
    //修改前操作方法
    updateBefore(form, index, done, loading) {
      this.form.updateUser = 'small';
      debugger;
    },

    //修改后操作方法
    updateAfter() {
    },

    //删除前操作方法
    delBefore(row, done, loading, data) {
      debugger;
      this.$message.success("点击了删除按钮 : " + JSON.stringify(row));
    },

    //删除后操作方法
    delAfter(row, done, loading, data) {
      debugger;
      this.$message.success("点击了删除按钮 : " + JSON.stringify(row));
    },
    // 按钮点击触发的方法
    btnClick(item, row, index) {
      eventMethod[item.methodName](this, item, row, index);
    },
    getSlotName(item) {
      return item + "Form";
    }
    , onJsonChange(name, value) {
      this.jsonTemp = value;
      debugger;
    }
    , initJsonValue(name, value) {
      if (value) {
        this.jsonFormat(name);
      }
    }
    , getJsonString(value) {
      if (!value) {
        return "";
      }
      if (value instanceof Object) {
        return JSON.stringify(value);
      }
      return value;
    },
    jsonFormat(name) {
      let self = this;
      let jsonObject = self.$refs['editor'];
      for (let i = 0; i < jsonObject.length; i++) {
        let attrName = jsonObject[i].$attrs['name'];
        if (name === attrName) {
          jsonObject[i].formatCode();
        }
      }
      debugger;
    },
    getTestJsonValue(val) {
      if (val) {
        debugger;
      }

      return val;
    },

    openDialog() {
      this.dialogConfig.showDialogProps = true;
    },
    closeDialog() {
      this.dialogConfig.showDialogProps = false;
    },
    btnTestClick(value, column) {
      this.$message.success('click事件查看控制台')
      console.log('点击事件', value, column)
    },
    testDialogClickSubmit(row, hide) {
      dialogSubmitEvent[this.dialogConfig.config["submitEventName"] || 'defaultSubmit'](this, row, hide);
    },
    testDialogClickClose() {
      this.closeDialog();
    },
    testDialogResetForm() {

    }
  }
}
;
</script>

<style lang="scss">
</style>
