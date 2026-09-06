/*
 * @Descripttion: 中国地图 json
 * @version: 
 * @Author: qianlishi
 * @Date: 2021-08-29 07:31:21
 * @LastEditors: qianlishi
 * @LastEditTime: 2021-09-28 14:17:20
 */
export const widgetMap = {
    code: 'widget-map',
    type: 'chart',
    label: '中国地图',
    icon: 'iconzhongguoditu',
    options: {
      setup: [
        {
          type: 'el-input-text',
          label: '图层名称',
          name: 'layerName',
          required: false,
          placeholder: '',
          value: '迁徙图',
        },
      ],
      data: [],
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
          value: 600,
        },
        {
          type: 'el-input-number',
          label: '高度',
          name: 'height',
          required: false,
          placeholder: '该容器在1080px大屏中的高度',
          value: 400,
        },
      ]
    }
  }
