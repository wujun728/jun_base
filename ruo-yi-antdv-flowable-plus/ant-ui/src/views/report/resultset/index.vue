<template>
  <anji-crud ref="listPage" :option="crudOption">
    <template v-slot:buttonLeftOnTable>
      <el-dropdown
        placement="bottom"
        @command="operateDataset"
      >
        <el-button type="primary" icon="el-icon-plus">
          新增
          <i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="sql">SQL</el-dropdown-item>
          <el-dropdown-item command="http">HTTP</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </template>

    <template slot="edit" slot-scope="props">
      <el-button
        type="text"
        @click="operateDataset('edit', props)"
        v-permission="'resultsetManage:update'"
      >编辑
      </el-button>
    </template>

    <template slot="rowButton" slot-scope="props">
      <el-button
        type="text"
        @click="dataView(props)"
        v-permission="'resultsetManage:query'"
      >数据预览
      </el-button>
    </template>
    <!--自定义的卡片插槽，将在编辑详情页面，出现在底部新卡片-->
    <!--这里可以将自定义的弹出框代码，放入到page中
    -->
    <template v-slot:pageSection>
      <EditDataSet
        ref="EditDataSet"
        :dataSet="dataSet"
        :visib="dialogVisibleSetDataSet"
        @handleClose="dialogVisibleSetDataSet = false"
        @refreshList="refreshList"
      />
      <DataView
        ref="DataView"
        :visib="dialogCaseResult"
        @handleClose="dialogCaseResult = false"
      />
    </template>
  </anji-crud>
</template>
<script>
import {
  reportDataSetList,
  reportDataSetAdd,
  reportDataSetDeleteBatch,
  reportDataSetUpdate,
  reportDataSetDetail
} from '@/api/report/reportDataSet'
import EditDataSet from '@/views/report/resultset/components/EditDataSet'
import DataView from '@/views/report/resultset/components/DataView'

export default {
  name: 'ReportDataSet',
  components: {
    EditDataSet: EditDataSet,
    DataView: DataView
  },
  data() {
    return {
      dialogVisibleSetDataSet: false,
      dataSet: {},
      dialogCaseResult: false,
      crudOption: {
        // 使用菜单做为页面标题
        title: '数据集',
        // 详情页中输入框左边文字宽度
        labelWidth: '120px',
        // 查询表单条件
        queryFormFields: [
          {
            inputType: 'input',
            label: '数据集编码',
            field: 'setCode'
          },
          {
            inputType: 'input',
            label: '数据集名称',
            field: 'setName'
          },
          {
            inputType: 'anji-select', //form表单类型 input|input-number|anji-select(传递url或者dictCode)|anji-tree(左侧树)|date|datetime|datetimerange
            anjiSelectOption: {
              url: '/dataSource/queryAllDataSource',
              label: 'sourceName',
              option: 'sourceCode',
              mergeLabel: true
            },
            label: '数据源',
            field: 'sourceCode'
          },
          {
            inputType: 'anji-select', //form表单类型 input|input-number|anji-select(传递url或者dictCode)|anji-tree(左侧树)|date|datetime|datetimerange
            anjiSelectOption: {
              dictCode: 'SET_TYPE'
            },
            label: '数据集类型',
            field: 'setType'
          },
        ],
        // 操作按钮
        buttons: {
          customButton: {
            operationWidth: 150
          },
          query: {
            api: reportDataSetList,
            permission: 'resultsetManage:query',
            sort: 'update_time',
            order: 'DESC'
          },
          queryByPrimarykey: {
            api: reportDataSetDetail,
            permission: 'resultsetManage:detail'
          },
          add: {
            api: reportDataSetAdd,
            permission: 'resultsetManage:insert',
            isShow: false
          },
          delete: {
            api: reportDataSetDeleteBatch,
            permission: 'resultsetManage:delete'
          },
          edit: {
            api: reportDataSetUpdate,
            permission: 'resultsetManage:update',
            isShow: false
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
            label: '数据集编码', //数据集编码
            placeholder: '',
            field: 'setCode',
            editField: 'setCode',
            inputType: 'input',
            rules: [
              {min: 1, max: 50, message: '不超过50个字符', trigger: 'blur'}
            ],
            disabled: false
          },
          {
            label: '数据集名称', //数据集名称
            placeholder: '',
            field: 'setName',
            editField: 'setName',
            inputType: 'input',
            rules: [
              {min: 1, max: 100, message: '不超过100个字符', trigger: 'blur'}
            ],
            disabled: false
          },
          {
            label: '描述', //数据集描述
            placeholder: '',
            field: 'setDesc',
            editField: 'setDesc',
            inputType: 'input',
            rules: [
              {min: 1, max: 255, message: '不超过255个字符', trigger: 'blur'}
            ],
            disabled: false
          },
          {
            label: '数据源编码', //数据源编码
            placeholder: '',
            field: 'sourceCode',
            editField: 'sourceCode',
            inputType: 'input',
            rules: [
              {min: 1, max: 50, message: '不超过50个字符', trigger: 'blur'}
            ],
            disabled: false
          },
          {
            label: '数据集类型', //数据源编码
            placeholder: '',
            field: 'setType',
            editField: 'setType',
            inputType: 'input',
            rules: [
              {min: 1, max: 50, message: '不超过50个字符', trigger: 'blur'}
            ],
            disabled: false
          },
          {
            label: '动态查询sql或者接口中的请求体', //动态查询sql或者接口中的请求体
            placeholder: '',
            field: 'dynSentence',
            editField: 'dynSentence',
            tableHide: true,
            inputType: 'input',
            rules: [
              {
                min: 1,
                max: 2048,
                message: '不超过2048个字符',
                trigger: 'blur'
              }
            ],
            disabled: false
          },
          {
            label: '结果案例', //结果案例
            placeholder: '',
            field: 'caseResult',
            tableHide: true,
            editField: 'caseResult',
            inputType: 'input',
            rules: [
              {
                min: 1,
                max: 65535,
                message: '不超过65535个字符',
                trigger: 'blur'
              }
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
            inputType: 'input',
            rules: [],
            disabled: false
          },
          {
            label: '删除标记', //0--未删除 1--已删除 DIC_NAME=DELETE_FLAG
            placeholder: '',
            field: 'deleteFlag',
            editField: 'deleteFlag',
            tableHide: true,
            inputType: 'input',
            rules: [],
            disabled: false
          }
        ]
      }
    }
  },

  created() {
  },
  methods: {
    operateDataset(type, prop) {
      this.dialogVisibleSetDataSet = true
      if (prop && prop.msg) {
        this.dataSet = prop.msg
        type = prop.msg.setType
      } else {
        this.dataSet = {}
      }
      this.$refs.EditDataSet.addOrEditDataSet(this.dataSet, type)
    },
    refreshList() {
      this.$refs.listPage.handleQueryForm('query')
    },
    dataView(prop) {
      this.dialogCaseResult = true
      this.$refs.DataView.dataViewPreview(
        prop.msg.setName,
        JSON.parse(prop.msg.caseResult)
      )
    }
  }
}
</script>
