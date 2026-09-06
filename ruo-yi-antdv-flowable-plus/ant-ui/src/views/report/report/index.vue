<!--
 * @Author: lide1202@hotmail.com
 * @Date: 2021-3-13 11:04:24
 * @Last Modified by:   lide1202@hotmail.com
 * @Last Modified time: 2021-3-13 11:04:24
 !-->
<template>
  <anji-crud ref="listPage" :option="crudOption">
    <template slot="rowButton" slot-scope="props">
      <el-button
        type="text"
        @click="preview(props.msg)"
        v-permission="'bigScreenManage:view'"
      >预览</el-button
      >
      <el-button
        type="text"
        @click="design(props.msg)"
        v-permission="'bigScreenManage:design'"
      >设计</el-button
      >
      <el-button
        type="text"
        @click="shareReport(props.msg)"
        v-permission="'bigScreenManage:share'"
      >分享</el-button
      >
    </template>

    <template v-slot:pageSection>
      <Share
        :visib="visibleForShareDialog"
        :reportCode="reportCodeForShareDialog"
        :reportName="reportNameForShareDialog"
        @handleClose="visibleForShareDialog = false"
      />
    </template>
  </anji-crud>
</template>
<script>
import {
  reportList,
  reportAdd,
  reportDeleteBatch,
  reportUpdate,
  reportDetail
} from '@/api/report/reportmanage'
import Share from '@/views/report/report/components/share'
import {validateEngOrNum} from '@/utils/validate'
export default {
  name: 'Report',
  components: {
    anjiCrud: require('@/components/AnjiPlus/anji-crud/anji-crud').default,
    Share
  },
  data() {
    return {
      // 分享
      visibleForShareDialog: false,
      reportCodeForShareDialog: '',
      reportNameForShareDialog: '',
      crudOption: {
        // 使用菜单做为页面标题
        title: '报表管理',
        // 详情页中输入框左边文字宽度
        labelWidth: '120px',
        // 查询表单条件
        queryFormFields: [
          {
            inputType: 'anji-select', //form表单类型 input|input-number|anji-select(传递url或者dictCode)|anji-tree(左侧树)|date|datetime|datetimerange
            anjiSelectOption: {
              dictCode: 'REPORT_TYPE'
            },
            label: '报表类型',
            field: 'reportType'
          },
          {
            inputType: 'input',
            label: '报表名称',
            field: 'reportName'
          },
          {
            inputType: 'input',
            label: '报表编码',
            field: 'reportCode'
          },
        ],
        // 操作按钮
        buttons: {
          query: {
            api: reportList,
            permission: 'reportManage:query',
            sort: 'update_time',
            order: 'DESC'
          },
          queryByPrimarykey: {
            api: reportDetail,
            permission: 'reportManage:detail'
          },
          add: {
            api: reportAdd,
            permission: 'reportManage:insert'
          },
          delete: {
            api: reportDeleteBatch,
            permission: 'reportManage:delete'
          },
          edit: {
            api: reportUpdate,
            permission: 'reportManage:update'
          },
          customButton: {
            operationWidth: '150px'
          }
        },
        // 表格列
        columns: [
          {
            label: '',
            field: 'id',
            primaryKey: true, // 根据主键查询详情或者根据主键删除时, 主键的
            tableHide: true, // 表格中不显示
            editHide: true // 编辑弹框中不显示
          },
          {
            label: '报表名称', //名称
            placeholder: '',
            field: 'reportName',
            editField: 'reportName',
            inputType: 'input',
            rules: [
              { required: true, message: '请输入报表名称', trigger: 'blur' },
              { min: 1, max: 100, message: '不超过100个字符', trigger: 'blur' }
            ],
            disabled: false
          },
          {
            label: '报表编码', //报表编码
            placeholder: '唯一标识',
            field: 'reportCode',
            editField: 'reportCode',
            inputType: 'input',

            rules: [
              { required: true, message: '请输入报表编码', trigger: 'blur' },
              { min: 1, max: 100, message: '不超过100个字符', trigger: 'blur' },
              { validator: validateEngOrNum, trigger: 'blur' },
            ],
            disabled: 'disableOnEdit'
          },
          {
            label: '分组', //分组,暂时不展示出来
            placeholder: '',
            field: 'reportGroup',
            tableHide: true,
            editHide: true,
            editField: 'reportGroup',
            inputType: 'input',
            rules: [
              { min: 1, max: 100, message: '不超过100个字符', trigger: 'blur' }
            ],
            disabled: false,
            defaultValue: 'default'
          },
          {
            label: '报表类型', //报表类型-目前仅支持大屏报表，暂时不展示出来
            placeholder: '',
            field: 'reportType',
            editField: 'reportType',
            inputType: 'anji-select',
            fieldTableRowRenderer: row => {
              return this.getDictLabelByCode('REPORT_TYPE', row['reportType'])
            },
            anjiSelectOption: {
              dictCode: 'REPORT_TYPE'
            },
            rules: [
              { required: true, message: '请输入报表类型', trigger: 'blur' },
              { min: 1, max: 20, message: '不超过20个字符', trigger: 'blur' }
            ],
            disabled: 'disableOnEdit',
          },
          {
            label: '描述', //报表描述
            placeholder: '',
            field: 'reportDesc',
            editField: 'reportDesc',
            inputType: 'input',
            rules: [
              { min: 1, max: 255, message: '不超过255个字符', trigger: 'blur' }
            ],
            disabled: false
          },
          {
            label: '状态', //0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG
            placeholder: '',
            field: 'enableFlag',
            fieldTableRowRenderer: row => {
              return this.getDictLabelByCode('ENABLE_FLAG', row['enableFlag'])
            },
            colorStyle: {
              0: 'table-danger', //key为editField渲染的值（字典的提交值）'红色': 'danger','蓝色': 'primary','绿色': 'success','黄色': 'warning','灰色': 'info','白色'：''
              1: 'table-success'
            },
            editField: 'enableFlag',
            inputType: 'anji-select',
            anjiSelectOption: {
              dictCode: 'ENABLE_FLAG'
            },
            rules: [],
            disabled: false,
            defaultValue: 1
          },
          {
            label: '删除标记', //0--未删除 1--已删除 DIC_NAME=DELETE_FLAG
            placeholder: '',
            field: 'deleteFlag',
            editHide: true,
            editField: 'deleteFlag',
            tableHide: true,
            inputType: 'input',
            rules: [],
            disabled: false
          },
          {
            label: '报表缩略图', //报表缩略图
            placeholder: '',
            field: 'reportImage',
            editField: 'reportImage',
            inputType: 'anji-upload',
            tableHide: true,
            anjiUpload: {
              limit: 1 // 文件大小 Number 类型
            }
          }
        ],
        // 弹出框表单对应的值有改动时
        // formData 整个表单，通过编辑打开弹出框，根据主键查询数据时，fieldName, fieldVal, fieldExtend为空
        // fieldName 触发修改的input name
        // fieldVal input最新值
        // fieldExtend 对于select型的扩展值
        formChange: (formData, fieldName, fieldVal, fieldExtend) => {}
      }
    }
  },

  created() {},
  methods: {
    handleOpenDialog1() {
      alert('自定义按钮1点击事件')
    },
    // 预览
    preview(val) {
      let routePath = ''
      if (val.reportType === 'report_excel') {
        routePath = '/excelreport/viewer'

      } else {
        routePath = '/bigscreen/viewer'
      }
      var routeUrl = this.$router.resolve({
        path: routePath,
        query: { reportCode: val.reportCode }
      })
      window.open(routeUrl.href, '_blank')
    },
    // 设计
    design(val) {
      let routePath = ''
      if (val.reportType === 'report_excel') {
        routePath = '/excelreport/designer'

      } else {
        routePath = '/bigscreen/designer'
      }
      var routeUrl = this.$router.resolve({
        path: routePath,
        query: {
          reportCode: val.reportCode,
        }
      })
      window.open(routeUrl.href, '_blank')

    },
    //分享
    shareReport(val){
      if (val.reportType == 'report_excel') {
        //excel暂不支持
        this.$message.warning('暂不支持excel报表分享')
        return
      }
      this.reportCodeForShareDialog = val.reportCode
      this.reportNameForShareDialog = val.reportName
      this.visibleForShareDialog = true
    }
  }
}
</script>
