/*
 * @Descripttion: 时间控件json
 * @version: 
 * @Author: qianlishi
 * @Date: 2021-08-29 07:05:52
 * @LastEditors: qianlishi
 * @LastEditTime: 2021-09-28 14:20:30
 */
export const widgetTime =  {
    code: 'widget-time',
    type: 'html',
    label: '当前时间',
    icon: 'iconshijian',
    options: {
      // 配置
      setup: [
        {
          type: 'el-input-text',
          label: '图层名称',
          name: 'layerName',
          required: false,
          placeholder: '',
          value: '当前时间',
        },
        {
          type: 'el-select',
          label: '时间格式',
          name: 'timeFormat',
          required: false,
          placeholder: '',
          selectOptions: [
            {code: 'yyyy-MM-dd', name: '日期'},
            {code: 'yyyy-MM-dd hh:mm', name: '日期+时分'},
            {code: 'yyyy-MM-dd hh:mm:ss', name: '日期+时分秒'},
            {code: 'MM-dd', name: '日期无年'},
            {code: 'hh:mm', name: '时分'},
            {code: 'hh:mm:ss', name: '时分秒'},
            {code: 'year-week', name: '日期+星期'},
            {code: 'year-h-m-week', name: '日期+时分+星期'},
            {code: 'year-h-m-s-week', name: '日期+时分秒+星期'},
            {code: 'week', name: '星期'}
          ],
          value: 'yyyy-MM-dd hh:mm:ss'
        },
        {
          type: 'el-input-number',
          label: '字体间距',
          name: 'letterSpacing',
          required: false,
          placeholder: '',
          value: '0'
        },
        {
          type: 'el-input-number',
          label: '字体大小',
          name: 'fontSize',
          required: false,
          placeholder: '',
          value: '26'
        },
        {
          type: 'vue-color',
          label: '字体颜色',
          name: 'color',
          required: false,
          placeholder: '',
          value: '#FAD400'
        },
        {
          type: 'vue-color',
          label: '字体背景',
          name: 'background',
          required: false,
          placeholder: '',
          value: 'rgba(115,170,229,.5)'
        },
        {
          type: 'el-select',
          label: '文字粗细',
          name: 'fontWeight',
          required: false,
          placeholder: '',
          selectOptions: [
            {code: 'normal', name: '正常'},
            {code: 'bold', name: '粗体'},
            {code: 'bolder', name: '特粗体'},
            {code: 'lighter', name: '细体'}
          ],
          value: 'normal'
        },
        {
          type: 'el-select',
          label: '对齐方式',
          name: 'textAlign',
          required: false,
          placeholder: '',
          selectOptions: [
            {code: 'center', name: '居中'},
            {code: 'left', name: '左对齐'},
            {code: 'right', name: '右对齐'},
          ],
          value: 'left'
        },
      ],
      // 数据
      data: [],
      // 坐标
      position: [
        {
          type: 'el-input-number',
          label: '左边距',
          name: 'left',
          required: false,
          placeholder: '',
          value: 0,
        },
        {
          type: 'el-input-number',
          label: '上边距',
          name: 'top',
          required: false,
          placeholder: '',
          value: 0,
        },
        {
          type: 'el-input-number',
          label: '宽度',
          name: 'width',
          required: false,
          placeholder: '该容器在1920px大屏中的宽度',
          value: 300,
        },
        {
          type: 'el-input-number',
          label: '高度',
          name: 'height',
          required: false,
          placeholder: '该容器在1080px大屏中的高度',
          value: 100,
        },
      ],
    }
  }