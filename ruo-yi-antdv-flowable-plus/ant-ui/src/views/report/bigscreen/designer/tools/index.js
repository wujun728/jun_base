/*
 * @Descripttion: 主文件
 * @version:
 * @Author: qianlishi
 * @Date: 2021-08-29 06:43:07
 * @LastEditors: qianlishi
 * @LastEditTime: 2021-08-29 08:00:59
 */
import { widgetTool } from './main'
const screenConfig = {
  code: 'screen',
  type: 'screen',
  label: '大屏设置',
  icon: '',
  options: {
    setup: [
      {
        type: 'el-input-number',
        label: '大屏宽度',
        name: 'width',
        required: false,
        placeholder: 'px',
        value: '1920',
      },
      {
        type: 'el-input-number',
        label: '大屏高度',
        name: 'height',
        required: false,
        placeholder: 'px',
        value: '1080',
      },
      {
        type: 'el-input-text',
        label: '标题',
        name: 'title',
        require: false,
        placeholder: '',
        value: '大屏',
      },
      {
        type: 'el-input-textarea',
        label: '大屏简介',
        name: 'description',
        required: false,
        placeholder: '',
      },
      {
        type: 'vue-color',
        label: '背景颜色',
        name: 'backgroundColor',
        required: false,
        placeholder: '',
        value: '#000',
      },
      {
        type: 'custom-upload',
        label: '图片地址',
        name: 'backgroundImage',
        required: false,
        placeholder: '',
        value: 'https://report.anji-plus.com/file/download/bf566e48-ccad-40e1-8ee9-228427e5466b',
      },
    ],
    data: [],
    position: [],
  }
}
const widgetTools = [
  ...widgetTool
]

const getToolByCode = function (code) {
  // 获取大屏底层设置属性
  if (code == 'screen') {
    return screenConfig
  }
  // 获取组件
  var item = widgetTools.find(function (item, index, arrs) {
    return item.code === code
  })
  return item
}

export {widgetTools, getToolByCode}
