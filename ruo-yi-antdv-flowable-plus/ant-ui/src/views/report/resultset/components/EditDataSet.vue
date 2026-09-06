<template>
  <div>
    <el-dialog
      :title="dialogFormVisibleTitle"
      :visible.sync="visib"
      :close-on-click-modal="false"
      :before-close="closeDialog"
      width="65%"
    >
      <el-form
        ref="form"
        :model="formData"
        :rules="formRules"
        size="small"
        label-width="130px"
      >
        <el-row :gutter="10">
          <el-col
            v-if="this.setType=='sql'"
            :xs="24"
            :sm="20"
            :md="8"
            :lg="8"
            :xl="8">
            <el-form-item label="数据源" prop="sourceCode">
              <el-select
                v-model.trim="formData.sourceCode"
                class="organisation"
                size="mini"
                @change="changeSource"
              >
                <el-option
                  v-for="item in sourceList"
                  :key="item.sourceName"
                  :label="item.sourceName"
                  :value="item.sourceCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="20" :md="7" :lg="7" :xl="7">
            <el-form-item label="数据集编码" prop="setCode">
              <el-input
                :disabled="updataDisabled"
                v-model.trim="formData.setCode"
                placeholder="唯一标识"
                size="mini"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="20" :md="7" :lg="7" :xl="7">
            <el-form-item label="数据集名称" prop="setName">
              <el-input v-model.trim="formData.setName" size="mini" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="20" :md="22" :lg="22" :xl="22">
            <el-form-item label="数据集描述">
              <el-input v-model.trim="formData.setDesc" size="mini" />
            </el-form-item>
          </el-col>


        </el-row>
        <el-row v-if="this.setType=='sql'" :gutter="10">
          <el-col
            :xs="24"
            :sm="20"
            :md="22"
            :lg="22"
            :xl="22"
            class="code-mirror-form"
          >
            <el-form-item label="查询SQL">
              <div class="codemirror">
                <monaco-editor
                  v-model.trim="formData.dynSentence"
                  language="sql"
                  style="height: 500px"
                />
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="this.setType=='http'">
          <el-form-item label="请求路径">
            <el-input placeholder="请输入请求路径..." v-model="httpForm.apiUrl" class="input-with-select">
              <el-select v-model="httpForm.method" slot="prepend" placeholder="请选择">
                <el-option label="GET" value="GET"></el-option>
                <el-option label="POST" value="POST"></el-option>
                <el-option label="PUT" value="PUT"></el-option>
                <el-option label="DELETE" value="DELETE"></el-option>
              </el-select>
            </el-input>
          </el-form-item>
          <el-form-item label="请求头">
            <el-input v-model.trim="httpForm.header" size="mini" placeholder="请输入请求头..."/>
          </el-form-item>
          <el-form-item label="请求体">
            <el-input v-model.trim="httpForm.body" size="mini" placeholder="请输入请求体..."/>
          </el-form-item>

        </el-row>
        <el-row :gutter="10">
          <el-col :xs="24" :sm="20" :md="22" :lg="22" :xl="22">
            <el-form label-width="100px" class="demo-ruleForm">
              <el-tabs
                v-model.trim="tabsActiveName"
                type="card"
                @tab-click="handleClickTabs"
              >
                <el-tab-pane label="查询参数" name="first">
                  <el-button
                    v-if="tableData.length == 0"
                    type="text"
                    size="small"
                    @click="addRow()"
                  >添加
                  </el-button>
                  <el-table :data="tableData" border style="width: 100%">
                    <el-table-column
                      align="center"
                      label="序号"
                      type="index"
                      min-width="80"
                    />
                    <el-table-column label="参数名" align="center">
                      <template slot-scope="scope">
                        <el-input
                          v-model.trim="tableData[scope.$index].paramName"
                          :disabled="
                            tableData[scope.$index].paramName == 'pageSize' ||
                              tableData[scope.$index].paramName == 'pageNumber'
                          "
                        />
                      </template>
                    </el-table-column>
                    <el-table-column label="描述" align="center">
                      <template slot-scope="scope">
                        <el-input
                          v-model.trim="tableData[scope.$index].paramDesc"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column label="数据类型" align="center">
                      <template slot-scope="scope">
                        <el-input
                          v-model.trim="tableData[scope.$index].paramType"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column label="示例值" align="center">
                      <template slot-scope="scope">
                        <el-input
                          v-model.trim="tableData[scope.$index].sampleItem"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column label="校验" width="220" align="center">
                      <template slot-scope="scope">
                        <el-checkbox
                          v-model="tableData[scope.$index].mandatory"
                          @change="Mandatory(scope.$index)"
                        >必选
                        </el-checkbox>
                        <el-button
                          type="primary"
                          icon="el-icon-plus"
                          @click="permissionClick(scope.row, scope.$index)"
                        >
                          高级规则
                        </el-button>
                      </template>
                    </el-table-column>
                    <el-table-column label="操作" width="200" align="center">
                      <template slot-scope="scope">
                        <el-button
                          type="text"
                          size="small"
                          @click.native.prevent="
                            cutOutRow(scope.$index, tableData)
                          "
                        >删除
                        </el-button>
                        <el-button
                          type="text"
                          size="small"
                          @click="addRow(scope.row)"
                        >追加
                        </el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                  <!--                  <el-checkbox-->
                  <!--                    v-model="isShowPagination"-->
                  <!--                    @change="changePagination"-->
                  <!--                    >加入分页参数-->
                  <!--                  </el-checkbox>-->
                </el-tab-pane>
                <el-tab-pane label="数据转换" name="second">
                  <template>
                    <div class="filterWrapper">
                      <div class="filterTable">
                        <div
                          v-for="(item, index) in itemFilterList"
                          :key="index"
                          class="filterBox"
                        >
                          <div class="box">
                            <Dictionary
                              v-model="item.transformType"
                              :updata-dict="item.transformType"
                              :dict-key="'TRANSFORM_TYPE'"
                            />
                            <el-button
                              type="text"
                              class="reduceFilter"
                              icon="el-icon-close"
                              @click="reduceFilter(item)"
                            />
                            <el-button
                              v-if="
                                item.transformType == 'js' ||
                                  item.transformType == 'dict'
                              "
                              type="text"
                              class="editor"
                              icon="el-icon-edit"
                              @click="filterScriptBtn(item)"
                            />
                          </div>
                          <el-button
                            type="text"
                            class="font-icon-right"
                            icon="el-icon-right"
                          />
                        </div>
                        <el-dialog
                          :title="dialogTitle"
                          :visible.sync="dialogSwitchVisible"
                          :close-on-click-modal="false"
                          width="60%"
                          min-height="400px"
                          append-to-body
                        >
                          <div v-if="isItemFilterType.transformType == 'js'">
                            <div class="codemirror">
                              <!-- //自定义高级规则？ -->
                              <monaco-editor
                                v-model.trim="transformScript"
                                language="javascript"
                                style="height: 500px"
                              />
                            </div>
                          </div>
                          <div v-else>
                            <div
                              v-for="(item, index) in tableData2"
                              :key="index"
                            >
                              <label>字典项名称：</label>
                              <el-input
                                v-model.trim="item.name"
                                size="small"
                                style="width: 25%"
                              />
                              <el-button
                                type="danger"
                                size="small"
                                plain
                                icon="el-icon-delete"
                                @click="delAllDict(index, tableData2)"
                              />
                              <el-table
                                :data="item.children"
                                border
                                style="width: 80%"
                              >
                                <el-table-column label="key" width="180">
                                  <template slot-scope="scope">
                                    <el-input
                                      v-model.trim="
                                        item.children[scope.$index].name
                                      "
                                    />
                                  </template>
                                </el-table-column>
                                <el-table-column label="值">
                                  <template slot-scope="scope">
                                    <el-input
                                      v-model.trim="
                                        item.children[scope.$index].value
                                      "
                                    />
                                  </template>
                                </el-table-column>
                                <el-table-column
                                  label="操作"
                                  width="150px"
                                  align="center"
                                >
                                  <template slot-scope="scope">
                                    <el-button
                                      type="success"
                                      circle
                                      plain
                                      icon="el-icon-plus"
                                      @click="
                                        addDict(scope.$index, item.children)
                                      "
                                    />

                                    <el-button
                                      type="danger"
                                      circle
                                      plain
                                      icon="el-icon-delete"
                                      @click="
                                        delDict(scope.$index, item.children)
                                      "
                                    />
                                  </template>
                                </el-table-column>
                              </el-table>
                            </div>
                            <el-button
                              type="success"
                              @click="addAllDict()"
                            >新增
                            </el-button>
                          </div>
                          <span slot="footer" class="dialog-footer">
                            <el-button
                              @click="dialogSwitchVisible = false"
                            >取消</el-button
                            >
                            <el-button
                              type="primary"
                              @click="filterScriptConfirm"
                            >保存</el-button
                            >
                          </span>
                        </el-dialog>
                        <!-- //添加数据转换 -->
                        <div
                          class="addFilter"
                          disabled="true"
                          @click="addFilter"
                        >
                          <i class="el-icon-plus" /><span>新增</span>
                        </div>
                      </div>
                    </div>
                  </template>
                </el-tab-pane>
                <el-tab-pane label="测试预览" name="third">
                  <div style="max-height: 400px; overflow: auto">
                    <vue-json-editor
                      v-model="cols"
                      :show-btns="false"
                      :mode="'code'"
                      lang="zh"
                      @json-change="onJsonChange"
                      @json-save="onJsonSave"
                    />
                  </div>
                </el-tab-pane>
              </el-tabs>
            </el-form>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <!-- <el-button type="warning" @click="test">测试</el-button> -->
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="submit('form')">保存</el-button>
      </div>
    </el-dialog>
    <el-dialog
      :title="title"
      :visible.sync="dialogPermissionVisible"
      :close-on-click-modal="false"
      width="60%"
    >
      <div class="codemirror">
        <!-- //自定义高级规则？ -->
        <monaco-editor
          v-model.trim="validationRules"
          language="javascript"
          style="height: 500px"
        />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="warning" @click="testResultset">测试</el-button>

        <el-button
          type="primary"
          @click="dialogValidationRules"
        >保存</el-button
        >
        <el-button @click="dialogPermissionVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import {
  queryAllDataSourceSet,
  verificationSet,
  testTransformSet,
  dataSetPreview,
  addDataSet,
  editDataSet
} from '@/api/report/report'
import Dictionary from '@/components/Dictionary/index'
import { codemirror } from 'vue-codemirror' // 引入codeMirror全局实例
import 'codemirror/mode/sql/sql.js'
import 'codemirror/mode/javascript/javascript.js'
import 'codemirror/lib/codemirror.css' // 核心样式
import 'codemirror/theme/cobalt.css' // 引入主题后还需要在 options 中指定主题才会生效
import vueJsonEditor from 'vue-json-editor'
import MonacoEditor from './MonacoEditor.vue'
import {validateEngOrNum} from '@/utils/validate'
export default {
  name: 'Support',
  components: { Dictionary, codemirror, vueJsonEditor, MonacoEditor },
  props: {
    visib: {
      required: true,
      type: Boolean,
      default: false
    },
    dataSet: {
      required: false,
      type: Object,
      default: () => {
        return ''
      }
    }
  },
  data() {
    return {
      data: [],
      listLoading: true,
      permission: {
        add: ['admin', 'alipayConfig:add'],
        edit: ['admin', 'alipayConfig:edit'],
        del: ['admin', 'datasource:del']
      },
      // 内部真实的内容
      code: '',
      // 默认配置
      optionsSql: {
        mode: 'text/x-sql',
        tabSize: 2, // 缩进格式
        // theme: 'cobalt', // monokai主题，对应主题库 JS 需要提前引入
        lineNumbers: true, // 显示行号
        line: true,
        styleActiveLine: true, // 高亮选中行
        hintOptions: {
          completeSingle: true // 当匹配只有一项的时候是否自动补全
        }
      },
      optionsJavascript: {
        mode: 'text/javascript',
        tabSize: 2, // 缩进格式
        // theme: 'cobalt', // monokai主题，对应主题库 JS 需要提前引入
        lineNumbers: true, // 显示行号
        line: true,
        styleActiveLine: true, // 高亮选中行
        hintOptions: {
          completeSingle: true // 当匹配只有一项的时候是否自动补全
        }
      },
      selectedList: [],
      clickType: '',
      formData: {},
      // 弹框默认隐藏
      dialogFormVisible: false,
      dialogFormVisibleTitle: '',
      dialogPermissionVisible: false,
      dialogSwitchVisible: false,
      permissionTextarea: '',
      isItemFilterType: '', // 选中的转换类型id
      itemFilterList: [
        {
          transformType: 'js',
          transformScript: ``
        }
      ],
      transformScript: `function dataTransform(data){\n\t//自定义脚本内容\n\treturn data;\n}`,
      itemFilterScriptId: '',
      title: '自定义高级规则',
      totalCount: 0,
      totalPage: 0,
      params: {
        pageNumber: 1,
        pageSize: 10,
        helpTitle: '',
        enabled: null,
        helpCategory: '',
        order: 'DESC',
        sort: 'update_time'
      },
      formRules: {
        setName: [
          { required: true, message: '数据集名称必填', trigger: 'blur' }
        ],
        setCode: [
          { required: true, message: '数据集编码必填', trigger: 'blur' },
          { validator: validateEngOrNum, trigger: 'blur' },
        ],
        sourceCode: [
          { required: true, message: '数据源必选', trigger: 'change' }
        ]
      },
      sourceList: [],
      validationRules: '',
      tabsActiveName: 'first',
      cols: [],
      tableData: [
        {
          paramName: '',
          paramDesc: '',
          paramType: '',
          sampleItem: '',
          mandatory: true,
          requiredFlag: 1,
          validationRules: `function verification(data){\n\t//自定义脚本内容\n\t//可返回true/false单纯校验键入的data正确性\n\t//可返回文本，实时替换,比如当前时间等\n\t//return "2099-01-01 00:00:00";\n\treturn true;\n}`
        }
      ],
      isRowData: {},
      tableData2: [],
      dialogTitle: 'js脚本填写',
      isShowPagination: false,
      updataDisabled: false,
      dialogCaseResult: false,
      caseResultTitle: '',
      caseResultContent: null,
      testMassageCode: null,
      query: {
        setName: '',
        setCode: ''
      },
      setType: '',  //数据集类型，主要用于区分http   addSql  addHttp  edit
      httpForm: {   //http数据源相关数据
        apiUrl: '',
        method: 'GET',
        header: '{"Content-Type":"application/json;charset=UTF-8"}',
        body: '',
      },
      //待删除
      dictionaryOptions: [], // 数据源类型
      list: null,
      basicDialog: false,
      dialogForm: {
        sourceName: '',
        sourceCode: '',
        sourceType: '',
        sourceDesc: '',
        sourceConfig: ''
      },
      dataLink: [],
      rules: {
        sourceType: [
          { required: true, message: '数据集名称必选', trigger: 'change' }
        ],
        sourceCode: [
          { required: true, message: '数据集编码必填', trigger: 'blur' }
        ],
        sourceName: [
          { required: true, message: '数据源名称必选', trigger: 'blur' }
        ]
      },
      value: '',
      testReplyCode: null
    }
  },
  // 在生命周期 beforeCreate里面改变this指向
  beforeCreate: function() {},
  mounted() {},
  methods: {
    // 编辑数据集,获取单条数据详情
    async addOrEditDataSet(row, type) {
      this.setType = type
      if (type == 'http' && row.dynSentence) {
        this.httpForm = JSON.parse(row.dynSentence)
      }
      //获取数据源下拉
      const { code, data } = await queryAllDataSourceSet()
      if (code != '200') return
      this.sourceList = data

      this.dialogFormVisible = true
      this.tabsActiveName = 'first'
      this.testMassageCode = null
      if (row.id) {
        this.updataDisabled = true
        this.dialogFormVisibleTitle = '编辑数据集'
        dataSetPreview(row).then(data => {
          this.formData = data.data
          if (
            data.data.dataSetParamDtoList != null ||
            data.data.dataSetParamDtoList.length > 0
          ) {
            this.tableData = data.data.dataSetParamDtoList
            var count = 0
            this.tableData.find((value, i) => {
              if (
                value.paramName === 'pageNumber' ||
                value.paramName === 'pageSize'
              ) {
                count++
              }
              if (value.requiredFlag == 1) {
                this.tableData[i].mandatory = true
              } else {
                this.tableData[i].mandatory = false
              }
            })
            if (count === 2) {
              this.isShowPagination = true
            } else {
              this.isShowPagination = false
            }
          } else {
            this.tableData = []
            this.isShowPagination = false
          }
          if (data.data.dataSetTransformDtoList !== null) {
            this.itemFilterList = data.data.dataSetTransformDtoList
          } else {
            this.itemFilterList = []
          }
          this.itemFilterList.forEach((item, i) => {
            if (item.transformType == 'dict') {
              const extendObj = JSON.parse(item.transformScript)
              const extendArry = []
              for (const i in extendObj) {
                const children = []
                for (const y in extendObj[i]) {
                  children.push({ name: y, value: extendObj[i][y] })
                }
                extendArry.push({ name: i, children: children })
              }
              this.tableData2 = extendArry
            }
          })
        })
      } else {
        this.dialogFormVisibleTitle = '新增数据集'
        this.updataDisabled = false
        this.formData = {
          setName: '',
          setCode: '',
          setDesc: '',
          sourceCode: '',
          dynSentence: ''
        }
        this.tableData = []
        this.itemFilterList = []
        this.isShowPagination = false
      }
    },
    // 关闭模态框
    closeDialog() {
      this.$emit('handleClose')
    },

    onJsonChange(value) {},
    onJsonSave(value) {},
    handleClose(done) {
      this.dialogFormVisible = false
    },
    isShowCaseResult(item) {
      this.dialogCaseResult = true
      this.caseResultTitle = item.setName
      this.caseResultContent = JSON.parse(item.caseResult)
    },

    // 测试预览
    async handleClickTabs(tab, event) {
      if (this.setType == 'http') {
        //针对http数据源
        console.log('http数据集' + this.httpForm)
        this.formData.dynSentence = JSON.stringify(this.httpForm)
      }
      if (tab.paneName == 'third') {
        const params = {
          sourceCode: this.formData.sourceCode,
          dynSentence: this.formData.dynSentence,
          dataSetParamDtoList: this.tableData,
          dataSetTransformDtoList: this.itemFilterList,
          setType: this.setType
        }
        const { code, data } = await testTransformSet(params)
        if (code != '200') return
        this.cols = data.data
        this.testMassageCode = code
      }
    },
    // 必选
    Mandatory(val) {
      if (!this.tableData[val].mandatory) {
        this.tableData[val].requiredFlag = 0
      } else {
        this.tableData[val].requiredFlag = 1
      }
    },
    // 分页参数增加列
    changePagination() {
      if (this.isShowPagination) {
        this.tableData.push(
          {
            paramName: 'pageNumber',
            paramDesc: '当前页',
            paramType: 'int',
            sampleItem: '1',
            mandatory: true,
            requiredFlag: 1,
            validationRules: `function verification(data){\n\t//自定义脚本内容\n\treturn true;\n}`
          },
          {
            paramName: 'pageSize',
            paramDesc: '条数',
            paramType: 'int',
            sampleItem: '10',
            mandatory: true,
            requiredFlag: 1,
            validationRules: `function verification(data){\n\t//自定义脚本内容\n\treturn true;\n}`
          }
        )
      } else {
        this.tableData.forEach((item, i) => {
          if (item.paramName == 'pageSize' || item.paramName == 'pageNumber') {
            this.tableData.splice(i, 2)
          }
        })
      }
    },
    // js 脚本编辑
    async filterScriptBtn(item) {
      console.log(item)
      this.isItemFilterType = item
      this.dialogSwitchVisible = true
      if (item.transformType == 'js') {
        this.itemFilterScriptId = item.itemFilterSort
        const fnCont = `function dataTransform(data){\n\t//自定义脚本内容\n\treturn data;\n}`
        this.transformScript = item.transformScript
          ? item.transformScript
          : fnCont
      } else if (item.transformType == 'dict') {
        // this.dialogTitle = '字典翻译'
        // this.itemFilterScriptId = item.itemFilterSort
        // const { code, data } = await getDictList('TRANSFORM_TYPE')
        // if (code != '200') return
        // const extend = data.find(function (y) {
        //   if (y.id == item.transformType) {
        //     return y
        //   }
        // })
        // const extendArry = []
        // const extendObj = JSON.parse(extend.extend)
        // for (const i in extendObj) {
        //   const children = []
        //   for (const y in extendObj[i]) {
        //     children.push({ name: y, value: extendObj[i][y] })
        //   }
        //   extendArry.push({ name: i, children: children })
        // }
        // this.tableData2 = extendArry
      }
    },
    // js 脚本编辑确定
    filterScriptConfirm() {
      const arr = this.toObject(this.tableData2)
      this.itemFilterList.forEach((el, index) => {
        if (el.transformType == 'dict') {
          el.transformScript = JSON.stringify(arr)
        } else {
          el.transformScript = this.transformScript
        }
      })
      this.dialogSwitchVisible = false
    },
    addFilter() {
      let obj = {}
      this.tableData2 = []
      if (this.itemFilterList.length == 0) {
        obj = {
          transformType: '',
          transformScript: '',
          itemFilterSort: 1
        }
      } else {
        obj = {
          transformType: '',
          transformScript: '',
          itemFilterSort: this.itemFilterList.length + 1
        }
      }
      this.itemFilterList.push(obj)
    },
    // 删除filter
    reduceFilter(item) {
      if (this.itemFilterList.length > 0) {
        var index = this.itemFilterList.indexOf(item)
        if (index > -1) {
          this.itemFilterList.splice(index, 1)
        }
      }
    },
    // --查询参数-----------------///////////////////////////////////////////////////////////////////////
    permissionClick(row, index) {
      this.title = '自定义高级规则'
      if (this.isRowData.sampleItem != '') {
        this.isRowData = row
        const fnCont = `function verification(data){\n\t//自定义脚本内容\n\t//可返回true/false单纯校验键入的data正确性\n\t//可返回文本，实时替换,比如当前时间等\n\t//return "2099-01-01 00:00:00";\n\treturn true;\n}`
        this.validationRules = row.validationRules
          ? row.validationRules
          : fnCont
      }
      this.dialogPermissionVisible = true
    },
    dialogValidationRules() {
      this.isRowData.validationRules = this.validationRules
      this.dialogPermissionVisible = false
    },
    // 字典项 增删改
    addAllDict() {
      this.tableData2.push({
        name: '',
        children: [{ name: '', value: '' }]
      })
    },
    addDict(index, item) {
      item.push({ name: '', value: '' })
    },
    delAllDict(index, rows) {
      rows.splice(index, 1)
    },
    delDict(index, rows) {
      if (index == 0) {
        this.$message.error('至少保留一条')
        return
      }
      rows.splice(index, 1)
    },
    // -------------------------------------------------------------------------------
    // 数据源下拉切换
    changeSource() {},
    // 自定义高级规则
    async testResultset() {
      this.isRowData.validationRules = this.validationRules
      console.log(this.isRowData,'12345678')
      const { code, message, data } = await verificationSet(this.isRowData)
      if (code == '200') {
        if (data) {
          this.$message.success('返回结果：'+ data)
        } else {
          this.$message.warning('当前示例值校验不通过')
        }
      } else {
        this.$message.error(message)
      }
    },
    // 删除
    cutOutRow(index, rows) {
      rows.splice(index, 1)
    },
    // 追加
    addRow(index, row) {
      this.tableData.push({
        paramName: '',
        paramDesc: '',
        paramType: '',
        sampleItem: '',
        mandatory: true,
        requiredFlag: 1,
        validationRules: `function verification(data){\n\t//自定义脚本内容\n\t//可返回true/false单纯校验键入的data正确性\n\t//可返回文本，实时替换,比如当前时间等\n\t//return "2099-01-01 00:00:00";\n\treturn true;\n}`
      })
    },
    async submit(formName) {
      if (this.setType == 'http') {
        //针对http数据源
        console.log('http数据集' + this.httpForm)
        this.formData.dynSentence = JSON.stringify(this.httpForm)
      }
      this.formData.setType = this.setType
      this.$refs[formName].validate(async (valid, obj) => {
        if (valid) {
          if (this.testMassageCode == 200) {
            this.formData.dataSetParamDtoList = this.tableData
            this.formData.dataSetTransformDtoList = this.itemFilterList
            this.formData.caseResult = JSON.stringify(this.cols)
            if (this.dialogFormVisibleTitle === '新增数据集') {
              const { code } = await addDataSet(this.formData)
              if (code != '200') return
              this.$emit('refreshList')
              this.closeDialog()
            } else {
              const { code } = await editDataSet(this.formData)
              if (code != '200') return
              this.$emit('refreshList')
              this.closeDialog()
            }
          } else {
            this.$message.error('请先测试预览，操作成功后便可保存！')
            return
          }
        } else {
          return
        }
      })
    },
    toObject(val) {
      const objfirst = {}
      const objSecond = {}
      val.forEach(el => {
        el.name ? (objfirst[el.name] = el.children) : ''
      })
      for (const key in objfirst) {
        const newObj = {}
        objfirst[key].map(ev => {
          Object.assign(newObj, { [ev.name]: ev.value })
        })
        objSecond[key] = newObj
      }
      return objSecond
    }
  }
}
</script>
<style lang="scss" scoped>
.code-mirror-form {
  /deep/ .el-form-item__content {
    height: 200px;
    overflow: hidden;
  }
}

.my-editor {
  height: 400px;

  div.jsoneditor {
    height: 400px !important;
  }
}

.title {
  width: 100%;
  display: inline-block;
  text-align: left;
}

.addSwitch {
  ul {
    list-style: none;
    margin: 0px;

    li {
      width: 200px;
      height: 60px;
      padding: 10px;
      border: 1px solid #aaa;
      float: left;
      margin-right: 10px;
      text-align: center;
      line-height: 40px;
      cursor: pointer;

      &.add,
      &:hover {
        border: 1px solid #3385ff;
        color: #3385ff;
      }
    }
  }
}

.filterWrapper {
  position: relative;
}

.filterTable {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 5px 15px 25px 15px;
}

.filterWrapper .addFilter {
  cursor: pointer;
  width: 150px;
  height: 50px;
  border-radius: 3px;
  border: 1px solid #67c23a;
  margin-top: 20px;
  background: #e4f7da;
  padding-left: 24px;
}

.filterWrapper .addFilter i {
  font-size: 20px;
  color: #67c23a;
  margin-top: 16px;
  margin-left: 16px;
}

.filterWrapper .addFilter span {
  color: #67c23a;
  margin-left: 5px;
  font-size: 18px;
}

.filterBox {
  width: 182px;
  position: relative;
  margin-top: 20px;
  display: flex;
  flex-direction: row;
}

.filterBox .box {
  width: 150px;
  height: 50px;
  text-align: center;
  position: relative;
  background: #e0e8ff;
  border-radius: 3px;
  border: 1px solid #b8caff;
  float: left;
  padding: 5px 20px;
}

.filterBox .box .reduceFilter {
  width: 10px;
  height: 10px;
  position: absolute;
  right: 4px;
  top: 4px;
  cursor: pointer;
}

.filterBox .box .editor {
  width: 12px;
  height: 13px;
  position: absolute;
  left: -5px;
  top: 5px;
  cursor: pointer;
}

.filterBox .rightIcon {
  width: 45px;
  height: 19px;
  position: absolute;
  right: 10px;
  top: 15px;
}

.filterTextarea /deep/ .el-textarea__inner {
  min-height: 300px !important;
}

.filterBox .code-selected {
  width: 70%;
  margin-top: 4px;
}

.filterBox /deep/ .el-input--suffix .el-input__inner {
  background: #f4f7ff;
  border-radius: 3px;
  border: 1px solid #b8caff;
  color: #89a3ee;
}

.font-icon-right {
  line-height: 35px;
  font-size: 30px;
}

.popover-box {
  width: 100%;
  height: 220px;
  overflow-y: auto;
  padding: 10px;
}
</style>
