<template>
  <div class="container">
    <div class="left-board">
      <div class="logo-wrapper">
        <div class="logo">
          <img :src="logo" alt="logo"> Form Generator
        </div>
      </div>
      <el-scrollbar class="left-scrollbar">
        <div class="components-list">
          <div v-for="(item, listIndex) in leftComponents" :key="listIndex">
            <div class="components-title">
              <svg-icon icon-class="component" />
              {{ item.title }}
            </div>
            <draggable
              class="components-draggable"
              :list="item.list"
              :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
              :clone="cloneComponent"
              draggable=".components-item"
              :sort="false"
              @end="onEnd"
            >
              <div
                v-for="(element, index) in item.list"
                :key="index"
                class="components-item"
                @click="addComponent(element)"
              >
                <div class="components-body">
                  <svg-icon :icon-class="element.__config__.tagIcon" />
                  {{ element.__config__.label }}
                </div>
              </div>
            </draggable>
          </div>
        </div>
      </el-scrollbar>
    </div>

    <div class="center-board">
      <div class="action-bar">
        <el-button icon="el-icon-plus" type="text" @click="handleForm">
          保存
        </el-button>
        <el-button icon="el-icon-video-play" type="text" @click="run">
          运行
        </el-button>
        <el-button icon="el-icon-view" type="text" @click="showJson">
          查看json
        </el-button>
        <el-button icon="el-icon-download" type="text" @click="download">
          导出vue文件
        </el-button>
        <el-button class="copy-btn-main" icon="el-icon-document-copy" type="text" @click="copy">
          复制代码
        </el-button>
        <el-button class="delete-btn" icon="el-icon-delete" type="text" @click="empty">
          清空
        </el-button>
      </div>
      <el-scrollbar class="center-scrollbar">
        <el-row class="center-board-row" :gutter="formConf.gutter">
          <el-form
            :size="formConf.size"
            :label-position="formConf.labelPosition"
            :disabled="formConf.disabled"
            :label-width="formConf.labelWidth + 'px'"
          >
            <draggable class="drawing-board" :list="drawingList" :animation="340" group="componentsGroup">
              <draggable-item
                v-for="(item, index) in drawingList"
                :key="item.renderKey"
                :drawing-list="drawingList"
                :current-item="item"
                :index="index"
                :active-id="activeId"
                :form-conf="formConf"
                @activeItem="activeFormItem"
                @copyItem="drawingItemCopy"
                @deleteItem="drawingItemDelete"
              />
            </draggable>
            <div v-show="!drawingList.length" class="empty-info">
              从左侧拖入或点选组件进行表单设计
            </div>
          </el-form>
        </el-row>
      </el-scrollbar>
    </div>

    <right-panel
      :active-data="activeData"
      :form-conf="formConf"
      :show-field="!!drawingList.length"
      @tag-change="tagChange"
      @fetch-data="fetchData"
    />

    <form-drawer
      :visible.sync="drawerVisible"
      :form-data="formData"
      size="100%"
      :generate-conf="generateConf"
    />
    <json-drawer
      size="60%"
      :visible.sync="jsonDrawerVisible"
      :json-str="JSON.stringify(formData)"
      @refresh="refreshJson"
    />
    <code-type-dialog
      :visible.sync="dialogVisible"
      title="选择生成类型"
      :show-file-name="showFileName"
      @confirm="generate"
    />
    <input id="copyNode" type="hidden">
    <!--表单配置详情-->
    <el-dialog :title="formTitle" :visible.sync="formOpen" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="表单名称" prop="formName">
          <el-input v-model="form.formName" placeholder="请输入表单名称" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import { debounce } from 'throttle-debounce'
import { saveAs } from 'file-saver'
import ClipboardJS from 'clipboard'
import render from '@/components/render/render'
import FormDrawer from './FormDrawer'
import JsonDrawer from './JsonDrawer'
import RightPanel from './RightPanel'
import {
  inputComponents, selectComponents, layoutComponents, formConf
} from '@/utils/generator/config'
import {
  exportDefault, beautifierConf, isNumberStr, titleCase, deepClone, isObjectObject
} from '@/utils/index'
import {
  makeUpHtml, vueTemplate, vueScript, cssStyle
} from '@/utils/generator/html'
import { makeUpJs } from '@/utils/generator/js'
import { makeUpCss } from '@/utils/generator/css'
import drawingDefalut from '@/utils/generator/drawingDefalut'
import logo from '@/assets/logo/logo.png'
import CodeTypeDialog from './CodeTypeDialog'
import DraggableItem from './DraggableItem'
import {
  getDrawingList, saveDrawingList, getIdGlobal, saveIdGlobal, getFormConf
} from '@/utils/db'
import loadBeautifier from '@/utils/loadBeautifier'
import {getForm, addForm, updateForm} from '@/api/flowable/form'

let beautifier
const emptyActiveData = { style: {}, autosize: {} }
let oldActiveId
let tempActiveData
const drawingListInDB = getDrawingList()
const formConfInDB = getFormConf()
const idGlobal = getIdGlobal()

export default {
  props: {
    formId: {type: String,default: null},
  },
  components: {
    draggable,
    render,
    FormDrawer,
    JsonDrawer,
    RightPanel,
    CodeTypeDialog,
    DraggableItem
  },
  data() {
    return {
      logo,
      idGlobal,
      formConf,
      inputComponents,
      selectComponents,
      layoutComponents,
      labelWidth: 100,
      drawingList: drawingDefalut,
      drawingData: {},
      activeId: drawingDefalut[0].formId,
      drawerVisible: false,
      formData: {},
      dialogVisible: false,
      jsonDrawerVisible: false,
      generateConf: null,
      showFileName: false,
      activeData: drawingDefalut[0],
      saveDrawingListDebounce: debounce(340, saveDrawingList),
      saveIdGlobalDebounce: debounce(340, saveIdGlobal),
      leftComponents: [
        {
          title: '输入型组件',
          list: inputComponents
        },
        {
          title: '选择型组件',
          list: selectComponents
        },
        {
          title: '布局型组件',
          list: layoutComponents
        }
      ],
      formOpen: false,
      formTitle: '',
      // 表单参数
      form: {
        formId: null,
        formName: null,
        formContent: null,
        remark: null
      },
      // 表单校验
      rules: {}
    }
  },
  computed: {
  },
  watch: {
    // eslint-disable-next-line func-names
    'activeData.__config__.label': function (val, oldVal) {
      if (
        this.activeData.placeholder === undefined
        || !this.activeData.__config__.tag
        || oldActiveId !== this.activeId
      ) {
        return
      }
      this.activeData.placeholder = this.activeData.placeholder.replace(oldVal, '') + val
    },
    activeId: {
      handler(val) {
        oldActiveId = val
      },
      immediate: true
    },
    drawingList: {
      handler(val) {
        this.saveDrawingListDebounce(val)
        if (val.length === 0) this.idGlobal = 100
      },
      deep: true
    },
    idGlobal: {
      handler(val) {
        this.saveIdGlobalDebounce(val)
      },
      immediate: true
    }
  },

  mounted() {
    const that = this
    if (Array.isArray(drawingListInDB) && drawingListInDB.length > 0) {
      that.drawingList = drawingListInDB
    } else {
      that.drawingList = drawingDefalut
    }
    this.activeFormItem(that.drawingList[0])
    // // if (formConfInDB) {
    // //   this.formConf = formConfInDB
    // // }
    that.drawingList = []
    // const formId =  that.$route.query && that.$route.query.formId
    const formId =  this.formId
    if (formId) {
      getForm(formId).then(res =>{
        that.formConf = JSON.parse(res.data.formContent)
        that.drawingList = that.formConf.fields
        that.form = res.data
      })
    }else {
      if (formConfInDB) {
        that.formConf = formConfInDB
      }
    }
    loadBeautifier(btf => {
      beautifier = btf
    })
    const clipboard = new ClipboardJS('#copyNode', {
      text: trigger => {
        const codeStr = this.generateCode()
        this.$notify({
          title: '成功',
          message: '代码已复制到剪切板，可粘贴。',
          type: 'success'
        })
        return codeStr
      }
    })
    clipboard.on('error', e => {
      this.$message.error('代码复制失败')
    })
  },
  methods: {
    setObjectValueReduce(obj, strKeys, data) {
      const arr = strKeys.split('.')
      arr.reduce((pre, item, i) => {
        if (arr.length === i + 1) {
          pre[item] = data
        } else if (!isObjectObject(pre[item])) {
          pre[item] = {}
        }
        return pre[item]
      }, obj)
    },
    setRespData(component, resp) {
      const { dataPath, renderKey, dataConsumer } = component.__config__
      if (!dataPath || !dataConsumer) return
      const respData = dataPath.split('.').reduce((pre, item) => pre[item], resp)

      // 将请求回来的数据，赋值到指定属性。
      // 以el-tabel为例，根据Element文档，应该将数据赋值给el-tabel的data属性，所以dataConsumer的值应为'data';
      // 此时赋值代码可写成 component[dataConsumer] = respData；
      // 但为支持更深层级的赋值（如：dataConsumer的值为'options.data'）,使用setObjectValueReduce
      this.setObjectValueReduce(component, dataConsumer, respData)
      const i = this.drawingList.findIndex(item => item.__config__.renderKey === renderKey)
      if (i > -1) this.$set(this.drawingList, i, component)
    },
    fetchData(component) {
      const { dataType, method, url } = component.__config__
      if (dataType === 'dynamic' && method && url) {
        this.setLoading(component, true)
        this.$axios({
          method,
          url
        }).then(resp => {
          this.setLoading(component, false)
          this.setRespData(component, resp.data)
        })
      }
    },
    setLoading(component, val) {
      const { directives } = component
      if (Array.isArray(directives)) {
        const t = directives.find(d => d.name === 'loading')
        if (t) t.value = val
      }
    },
    activeFormItem(currentItem) {
      this.activeData = currentItem
      this.activeId = currentItem.__config__.formId
    },
    onEnd(obj) {
      if (obj.from !== obj.to) {
        this.fetchData(tempActiveData)
        this.activeData = tempActiveData
        this.activeId = this.idGlobal
      }
    },
    addComponent(item) {
      const clone = this.cloneComponent(item)
      this.fetchData(clone)
      this.drawingList.push(clone)
      this.activeFormItem(clone)
    },
    cloneComponent(origin) {
      const clone = deepClone(origin)
      const config = clone.__config__
      config.span = this.formConf.span // 生成代码时，会根据span做精简判断
      this.createIdAndKey(clone)
      clone.placeholder !== undefined && (clone.placeholder += config.label)
      tempActiveData = clone
      return tempActiveData
    },
    createIdAndKey(item) {
      const config = item.__config__
      config.formId = ++this.idGlobal
      config.renderKey = `${config.formId}${+new Date()}` // 改变renderKey后可以实现强制更新组件
      if (config.layout === 'colFormItem') {
        item.__vModel__ = `field${this.idGlobal}`
      } else if (config.layout === 'rowFormItem') {
        config.componentName = `row${this.idGlobal}`
        !Array.isArray(config.children) && (config.children = [])
        delete config.label // rowFormItem无需配置label属性
      }
      if (Array.isArray(config.children)) {
        config.children = config.children.map(childItem => this.createIdAndKey(childItem))
      }
      return item
    },
    AssembleFormData() {
      this.formData = {
        fields: deepClone(this.drawingList),
        ...this.formConf
      }
    },
    generate(data) {
      const func = this[`exec${titleCase(this.operationType)}`]
      this.generateConf = data
      func && func(data)
    },
    execRun(data) {
      this.AssembleFormData()
      this.drawerVisible = true
    },
    execDownload(data) {
      const codeStr = this.generateCode()
      const blob = new Blob([codeStr], { type: 'text/plain;charset=utf-8' })
      saveAs(blob, data.fileName)
    },
    execCopy(data) {
      document.getElementById('copyNode').click()
    },
    empty() {
      this.$confirm('确定要清空所有组件吗？', '提示', { type: 'warning' }).then(
        () => {
          this.drawingList = []
          this.idGlobal = 100
        }
      )
    },
    drawingItemCopy(item, list) {
      let clone = deepClone(item)
      clone = this.createIdAndKey(clone)
      list.push(clone)
      this.activeFormItem(clone)
    },
    drawingItemDelete(index, list) {
      list.splice(index, 1)
      this.$nextTick(() => {
        const len = this.drawingList.length
        if (len) {
          this.activeFormItem(this.drawingList[len - 1])
        }
      })
    },
    generateCode() {
      const { type } = this.generateConf
      this.AssembleFormData()
      const script = vueScript(makeUpJs(this.formData, type))
      const html = vueTemplate(makeUpHtml(this.formData, type))
      const css = cssStyle(makeUpCss(this.formData))
      return beautifier.html(html + script + css, beautifierConf.html)
    },
    showJson() {
      this.AssembleFormData()
      this.jsonDrawerVisible = true
    },
    download() {
      this.dialogVisible = true
      this.showFileName = true
      this.operationType = 'download'
    },
    run() {
      this.dialogVisible = true
      this.showFileName = false
      this.operationType = 'run'
    },
    copy() {
      this.dialogVisible = true
      this.showFileName = false
      this.operationType = 'copy'
    },
    tagChange(newTag) {
      newTag = this.cloneComponent(newTag)
      const config = newTag.__config__
      newTag.__vModel__ = this.activeData.__vModel__
      config.formId = this.activeId
      config.span = this.activeData.__config__.span
      this.activeData.__config__.tag = config.tag
      this.activeData.__config__.tagIcon = config.tagIcon
      this.activeData.__config__.document = config.document
      if (typeof this.activeData.__config__.defaultValue === typeof config.defaultValue) {
        config.defaultValue = this.activeData.__config__.defaultValue
      }
      Object.keys(newTag).forEach(key => {
        if (this.activeData[key] !== undefined) {
          newTag[key] = this.activeData[key]
        }
      })
      this.activeData = newTag
      this.updateDrawingList(newTag, this.drawingList)
    },
    updateDrawingList(newTag, list) {
      const index = list.findIndex(item => item.__config__.formId === this.activeId)
      if (index > -1) {
        list.splice(index, 1, newTag)
      } else {
        list.forEach(item => {
          if (Array.isArray(item.__config__.children)) this.updateDrawingList(newTag, item.__config__.children)
        })
      }
    },
    refreshJson(data) {
      this.drawingList = deepClone(data.fields)
      delete data.fields
      this.formConf = data
    },
    /** 表单基本信息 */
    handleForm(){
      this.formData = {
        fields: deepClone(this.drawingList),
        ...this.formConf
      }
     this.form.formContent = JSON.stringify(this.formData)
     this.formOpen = true
     this.formTitle = '添加表单'
    },
    // 表单重置
    reset() {
      this.form = {
        formId: null,
        formName: null,
        formContent: null,
        remark: null
      }
      this.resetForm('form')
    },
    // 取消按钮
    cancel() {
      this.formOpen = false
      this.reset()
    },
    /** 保存表单信息 */
    submitForm(){
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.formId != null) {
            updateForm(this.form).then(response => {
              this.elMsgSuccess('修改成功')
            })
          } else {
            addForm(this.form).then(response => {
              this.elMsgSuccess('新增成功')
            })
          }
          this.drawingList = []
          this.idGlobal = 100
          this.open = false
          // 关闭当前标签页并返回上个页面
          this.$emit('ok')
          // this.$store.dispatch('tagsView/delView', this.$route)
          // this.$router.go(-1)
        }
      })
    }
  }
}
</script>

<style>
.container {
  position: relative;
  width: 100%;
  height: 100%;
}

.components-list {
  padding: 8px;
  box-sizing: border-box;
  height: 100%;
}
.components-list .components-item {
  display: inline-block;
  width: 48%;
  margin: 1%;
  transition: transform 0ms !important;
}

.components-draggable {
  padding-bottom: 20px;
}

.components-title {
  font-size: 14px;
  color: #222;
  margin: 6px 2px;
}
.components-title .svg-icon {
  color: #666;
  font-size: 18px;
}

.components-body {
  padding: 8px 10px;
  background: #f6f7ff;
  font-size: 12px;
  cursor: move;
  border: 1px dashed #f6f7ff;
  border-radius: 3px;
}
.components-body .svg-icon {
  color: #777;
  font-size: 15px;
}
.components-body:hover {
  border: 1px dashed #787be8;
  color: #787be8;
}
.components-body:hover .svg-icon {
  color: #787be8;
}

.left-board {
  width: 260px;
  position: absolute;
  left: 0;
  top: 0;
  height: 100vh;
}

.left-scrollbar {
  height: calc(100vh - 42px);
  overflow: hidden;
}

.center-scrollbar {
  height: calc(100vh - 42px);
  overflow: hidden;
  border-left: 1px solid #f1e8e8;
  border-right: 1px solid #f1e8e8;
  box-sizing: border-box;
}

.center-board {
  height: 100vh;
  width: auto;
  margin: 0 350px 0 260px;
  box-sizing: border-box;
}

.empty-info {
  position: absolute;
  top: 46%;
  left: 0;
  right: 0;
  text-align: center;
  font-size: 18px;
  color: #ccb1ea;
  letter-spacing: 4px;
}

.action-bar {
  position: relative;
  height: 42px;
  text-align: right;
  padding: 0 15px;
  box-sizing: border-box;
  border: 1px solid #f1e8e8;
  border-top: none;
  border-left: none;
}
.action-bar .delete-btn {
  color: #F56C6C;
}

.logo-wrapper {
  position: relative;
  height: 42px;
  background: #fff;
  border-bottom: 1px solid #f1e8e8;
  box-sizing: border-box;
}

.logo {
  position: absolute;
  left: 12px;
  top: 6px;
  line-height: 30px;
  color: #00afff;
  font-weight: 600;
  font-size: 17px;
  white-space: nowrap;
}
.logo > img {
  width: 30px;
  height: 30px;
  vertical-align: top;
}
.logo .github {
  display: inline-block;
  vertical-align: sub;
  margin-left: 15px;
}
.logo .github > img {
  height: 22px;
}

.center-board-row {
  padding: 12px 12px 15px 12px;
  box-sizing: border-box;
}
.center-board-row > .el-form {
  height: calc(100vh - 69px);
}

.drawing-board {
  height: 100%;
  position: relative;
}
.drawing-board .components-body {
  padding: 0;
  margin: 0;
  font-size: 0;
}
.drawing-board .sortable-ghost {
  position: relative;
  display: block;
  overflow: hidden;
}
.drawing-board .sortable-ghost::before {
  content: " ";
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  height: 3px;
  background: #5959df;
  z-index: 2;
}
.drawing-board .components-item.sortable-ghost {
  width: 100%;
  height: 60px;
  background-color: #f6f7ff;
}
.drawing-board .active-from-item > .el-form-item {
  background: #f6f7ff;
  border-radius: 6px;
}
.drawing-board .active-from-item > .drawing-item-copy, .drawing-board .active-from-item > .drawing-item-delete {
  display: initial;
}
.drawing-board .active-from-item > .component-name {
  color: #409EFF;
}
.drawing-board .el-form-item {
  margin-bottom: 15px;
}

.drawing-item {
  position: relative;
  cursor: move;
}
.drawing-item.unfocus-bordered:not(.active-from-item) > div:first-child {
  border: 1px dashed #ccc;
}
.drawing-item .el-form-item {
  padding: 12px 10px;
}

.drawing-row-item {
  position: relative;
  cursor: move;
  box-sizing: border-box;
  border: 1px dashed #ccc;
  border-radius: 3px;
  padding: 0 2px;
  margin-bottom: 15px;
}
.drawing-row-item .drawing-row-item {
  margin-bottom: 2px;
}
.drawing-row-item .el-col {
  margin-top: 22px;
}
.drawing-row-item .el-form-item {
  margin-bottom: 0;
}
.drawing-row-item .drag-wrapper {
  min-height: 80px;
}
.drawing-row-item.active-from-item {
  border: 1px dashed #409EFF;
}
.drawing-row-item .component-name {
  position: absolute;
  top: 0;
  left: 0;
  font-size: 12px;
  color: #bbb;
  display: inline-block;
  padding: 0 6px;
}

.drawing-item:hover > .el-form-item, .drawing-row-item:hover > .el-form-item {
  background: #f6f7ff;
  border-radius: 6px;
}
.drawing-item:hover > .drawing-item-copy, .drawing-item:hover > .drawing-item-delete, .drawing-row-item:hover > .drawing-item-copy, .drawing-row-item:hover > .drawing-item-delete {
  display: initial;
}
.drawing-item > .drawing-item-copy, .drawing-item > .drawing-item-delete, .drawing-row-item > .drawing-item-copy, .drawing-row-item > .drawing-item-delete {
  display: none;
  position: absolute;
  top: -10px;
  width: 22px;
  height: 22px;
  line-height: 22px;
  text-align: center;
  border-radius: 50%;
  font-size: 12px;
  border: 1px solid;
  cursor: pointer;
  z-index: 1;
}
.drawing-item > .drawing-item-copy, .drawing-row-item > .drawing-item-copy {
  right: 56px;
  border-color: #409EFF;
  color: #409EFF;
  background: #fff;
}
.drawing-item > .drawing-item-copy:hover, .drawing-row-item > .drawing-item-copy:hover {
  background: #409EFF;
  color: #fff;
}
.drawing-item > .drawing-item-delete, .drawing-row-item > .drawing-item-delete {
  right: 24px;
  border-color: #F56C6C;
  color: #F56C6C;
  background: #fff;
}
.drawing-item > .drawing-item-delete:hover, .drawing-row-item > .drawing-item-delete:hover {
  background: #F56C6C;
  color: #fff;
}
</style>
