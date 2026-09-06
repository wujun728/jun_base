<!--
  该模版是用来调试弹出层的测试
-->
<template>
  <div>
    <el-button type="primary" @click="testBtnClick">弹窗测试按钮</el-button>
    <avue-dialog :form-option="option" :show-dialog-props="showDialog" :object-data="obj" @submit="testClickSubmit"
                 @closeDialog="closeDialog" :dialog-option="dialogOption">
    </avue-dialog>
  </div>
</template>

<script>

import avueDialog from './avue-dialog'

let DIC = {
  VAILD: [{
    label: '真',
    value: 'true'
  }, {
    label: '假',
    value: 'false'
  }],
  SEX: [{
    label: '男',
    value: 0
  }, {
    label: '女',
    value: 1
  }]
}
export default {
  name: "avue-dialog-test",
  data() {
    return {
      count_number: 10,
      showDialog: false,
      dialogOption: {
        title: "测试弹窗喔"
      },
      obj: {
        name: 'smallwei'
      },
      sizeValue: 'small'
    }
  },
  computed: {
    option() {
      return {
        size: this.sizeValue,
        //mockBtn: true,
        submitText: '完成',
        //printBtn: true,
        //box: false,
        column: [{
          label: "用户名",
          prop: "username",
          tip: '这是信息提示',
          span: 8,
          maxlength: 3,
          suffixIcon: 'el-icon-tickets',
          prefixIcon: 'el-icon-tickets',
          minlength: 2,
          mock: {
            type: 'name',
            en: true,
          },
          rules: [{
            required: true,
            message: "请输入用户名",
            trigger: "blur"
          }],
          click: ({value, column}) => {
            this.$message.success('click')
          }
        },
          {
            label: "姓名",
            prop: "name",
            disabled: true,
            mock: {
              type: 'name'
            },
            span: 8
          },
          {
            label: "密码",
            prop: "password",
            type: 'password',
            mock: {
              type: 'name'
            },
            span: 8
          },
          {
            label: "类型",
            prop: "type",
            type: "select",
            dicData: DIC.VAILD,
            span: 6,
            mock: {
              type: 'dic',
            },
          },
          {
            label: "权限",
            prop: "grade",
            span: 6,
            type: "checkbox",
            dicData: DIC.VAILD,
            mock: {
              type: 'dic',
            },
          },
          {
            label: "开关",
            prop: "switch",
            span: 6,
            type: "switch",
            dicData: DIC.SEX,
            mock: {
              type: 'dic'
            },
            hide: true,
            row: true,
          },
          {
            label: "性别",
            prop: "sex",
            span: 6,
            type: "radio",
            dicData: DIC.SEX,
            mock: {
              type: 'dic'
            }
          },
          {
            label: "数字",
            prop: "number",
            type: 'number',
            span: 6,
            precision: 2,
            mock: {
              type: 'number',
              max: 1,
              min: 2,
              precision: 2
            },
            minRows: 0,
            maxRows: 3,
            row: true,
          },
          {
            label: "网站",
            span: 12,
            prop: "url",
            prepend: 'http://',
            mock: {
              type: 'url',
              header: false,
            },
            append: 'com',
            row: true,
          },
          {
            label: "日期",
            prop: "date",
            type: "date",
            span: 8,
            format: 'yyyy-MM-dd',
            valueFormat: 'yyyy-MM-dd',
            mock: {
              type: 'datetime',
              format: 'yyyy-MM-dd'
            },
          },
          {
            label: "日期时间",
            prop: "datetime",
            type: "datetime",
            span: 8,
            format: 'yyyy-MM-dd hh:mm:ss',
            valueFormat: 'yyyy-MM-dd hh:mm:ss',
            mock: {
              type: 'datetime',
              format: 'yyyy-MM-dd hh:mm:ss',
              now: true,
            },
          },
          {
            label: "时间",
            prop: "time",
            type: "time",
            span: 8,
            format: 'hh:mm:ss',
            valueFormat: 'hh:mm:ss',
            mock: {
              type: 'datetime',
              format: 'hh:mm:ss'
            },
          },
          {
            label: "地址",
            span: 24,
            prop: "address",
            mock: {
              type: 'county'
            },
          },
          {
            label: "建议",
            span: 24,
            prop: "adit",
            mock: {
              type: 'word',
              min: 10,
              max: 30
            },
          }]
      }
    }
  },
  components: {
    "avue-dialog": avueDialog
  },
  mounted() {
  },
  methods: {
    testBtnClick() {
      this.count_number = this.count_number * 10;
      this.option.submitText = "完成" + this.count_number + "次";
      this.showDialog = true;
    },
    testClickSubmit(row, hide) {
      debugger;
      this.$message.success("提交结果 : " + JSON.stringify(row));
      hide();
      this.showDialog = false;
    }, closeDialog() {
      this.showDialog = false;
    }
  }
}
</script>

<style scoped>

</style>
