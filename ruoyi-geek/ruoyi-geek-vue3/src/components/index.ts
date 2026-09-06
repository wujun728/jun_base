import type { App } from 'vue'

// 分页组件
import Pagination from '@/components/Pagination/index.vue'
// 自定义表格工具组件
import RightToolbar from '@/components/RightToolbar/index.vue'
// 文件上传组件
import FileUpload from "@/components/UploadComponents/FileUpload/index.vue"
// 图片上传组件
import ImageUpload from "@/components/UploadComponents/ImageUpload/index.vue"
// 分片上传组件
import ChunkUpload from "@/components/UploadComponents/ChunkUpload/index.vue"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview/index.vue"
// 自定义树选择组件
import TreeSelect from '@/components/TreeSelect/index.vue'
// 字典标签组件
import DictTag from '@/components/DictTag/index.vue'
// 富文本编辑器组件
import Editor from "@/components/Editor/index.vue"
// Qrcode 二维码组件
import QRCodeView from "@/components/QRCodeView/index.vue"
// JsBarcode 条形码组件
import JsBarcodeView from "@/components/JsBarcodeView/index.vue"


// svg图标
// @ts-ignore
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon/index.vue'


import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import ElementPlus from 'element-plus'
import locale from 'element-plus/dist/locale/zh-cn.mjs'// 中文语言

// 初始化路由
export default function initComponents(app: App<Element>): void {
  app.component('DictTag', DictTag)
  app.component('Pagination', Pagination)
  app.component('TreeSelect', TreeSelect)
  app.component('FileUpload', FileUpload)
  app.component('ImageUpload', ImageUpload)
  app.component('ChunkUpload', ChunkUpload)
  app.component('ImagePreview', ImagePreview)
  app.component('RightToolbar', RightToolbar)
  app.component('Editor', Editor)
  app.component('QRCodeView', QRCodeView)
  app.component('JsBarcodeView', JsBarcodeView)
  app.component('svg-icon', SvgIcon)



  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }

  app.use(ElementPlus, {
    locale,
    size: (localStorage.getItem('size') as 'default' | 'small' | 'large' | '') ?? 'default',
  })
}
