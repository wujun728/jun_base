<template>
  <anji-crud ref="listPage" :option="crudOption">
    <template v-slot:buttonLeftOnTable>
      <el-button
        type="primary"
        icon="el-icon-plus"
        @click="operateDatasource('add')"
        v-permission="'datasourceManage:insert'"
      >新增</el-button
      >
    </template>

    <template slot="edit" slot-scope="props">
      <el-button
        type="text"
        @click="operateDatasource('edit', props)"
        v-permission="'datasourceManage:update'"
      >编辑</el-button
      >
    </template>
    <!--自定义的卡片插槽，将在编辑详情页面，出现在底部新卡片-->
    <!--这里可以将自定义的弹出框代码，放入到page中
    -->
    <template v-slot:pageSection>
      <EditDataSource
        ref="EditDataSource"
        :dataSource="dataSource"
        :visib="dialogVisibleSetDataSource"
        @handleClose="dialogVisibleSetDataSource = false"
        @refreshList="refreshList"
      />
    </template>
  </anji-crud>
</template>
<script>
import {
  reportDataSourceList,
  reportDataSourceAdd,
  reportDataSourceDeleteBatch,
  reportDataSourceUpdate,
  reportDataSourceDetail
} from '@/api/report/reportDataSource'
import EditDataSource from '@/views/report/datasource/components/EditDataSource'
export default {
  name: 'ReportDataSource',
  components: {
    EditDataSource: EditDataSource
  },
  data() {
    return {
      dialogVisibleSetDataSource: false,
      dataSource: {},
      crudOption: {
        // 使用菜单做为页面标题
        title: '数据源',
        // 详情页中输入框左边文字宽度
        labelWidth: '120px',
        // 查询表单条件
        queryFormFields: [
          {
            inputType: 'input',
            label: '数据源编码',
            field: 'sourceCode'
          },
          {
            inputType: 'input',
            label: '数据源名称',
            field: 'sourceName'
          },
          {
            inputType: 'anji-select', //form表单类型 input|input-number|anji-select(传递url或者dictCode)|anji-tree(左侧树)|date|datetime|datetimerange
            anjiSelectOption: {
              dictCode: 'SOURCE_TYPE'
            },
            label: '数据源类型',
            field: 'sourceType'
          }
        ],
        // 操作按钮
        buttons: {
          customButton: {
            operationWidth: 150
          },
          query: {
            api: reportDataSourceList,
            permission: 'datasourceManage:query',
            sort: 'update_time',
            order: 'DESC'
          },
          queryByPrimarykey: {
            api: reportDataSourceDetail,
            permission: 'datasourceManage:detail'
          },
          add: {
            api: reportDataSourceAdd,
            permission: 'datasourceManage:insert',
            isShow: false
          },
          delete: {
            api: reportDataSourceDeleteBatch,
            permission: 'datasourceManage:delete'
          },
          edit: {
            api: reportDataSourceUpdate,
            permission: 'datasourceManage:update',
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
            label: '数据源编码', //数据源编码
            placeholder: '',
            field: 'sourceCode',
            editField: 'sourceCode',
            inputType: 'input',
            rules: [
              { min: 1, max: 100, message: '不超过100个字符', trigger: 'blur' }
            ],
            disabled: false
          },
          {
            label: '数据源名称', //数据源名称
            placeholder: '',
            field: 'sourceName',
            editField: 'sourceName',
            inputType: 'input',
            rules: [
              { min: 1, max: 100, message: '不超过100个字符', trigger: 'blur' }
            ],
            disabled: false
          },
          {
            label: '数据源描述', //数据源描述
            placeholder: '',
            field: 'sourceDesc',
            editField: 'sourceDesc',
            inputType: 'input',
            rules: [
              { min: 1, max: 255, message: '不超过255个字符', trigger: 'blur' }
            ],
            disabled: false
          },
          {
            label: '数据源类型', //数据源类型 DIC_NAME=SOURCE_TYPE; mysql，orace，sqlserver，elasticsearch，接口，javaBean，数据源类型字典中item-extend动态生成表单
            placeholder: '',
            field: 'sourceType',
            fieldTableRowRenderer: row => {
              return this.getDictLabelByCode('SOURCE_TYPE', row['sourceType'])
            },
            editField: 'sourceType',
            inputType: 'input',
            rules: [
              { min: 1, max: 50, message: '不超过50个字符', trigger: 'blur' }
            ],
            disabled: false
          },
          {
            label: '数据源连接配置json', //数据源连接配置json：关系库{ jdbcUrl:'', username:'', password:'' } ES{ hostList:'ip1:9300,ip2:9300,ip3:9300', clusterName:'elasticsearch_cluster' }  接口{ apiUrl:'http://ip:port/url', method:'' } javaBean{ beanNamw:'xxx' }
            placeholder: '',
            field: 'sourceConfig',
            editField: 'sourceConfig',
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

  created() {},
  methods: {
    operateDatasource(type, prop) {
      this.dialogVisibleSetDataSource = true
      if (prop) {
        this.dataSource = prop.msg
      } else {
        this.dataSource = {}
      }
      this.$refs.EditDataSource.getSystem()
    },
    refreshList() {
      this.$refs.listPage.handleQueryForm('query')
    }
  }
}
</script>
