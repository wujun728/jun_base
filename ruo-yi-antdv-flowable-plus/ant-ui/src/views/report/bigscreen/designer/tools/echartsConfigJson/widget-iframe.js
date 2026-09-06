/*
 * @Descripttion: iframe json
 * @version: 
 * @Author: qianlishi
 * @Date: 2021-08-29 07:17:55
 * @LastEditors: qianlishi
 * @LastEditTime: 2021-09-28 14:14:39
 */
export const widgetIframe =  {
    code: 'widget-iframe',
    type: 'html',
    label: '内联框架',
    icon: 'iconkuangjia',
    options: {
      // 配置
      setup: [
        {
          type: 'el-input-text',
          label: '图层名称',
          name: 'layerName',
          required: false,
          placeholder: '',
          value: 'iframe',
        },
        {
          type: 'el-input-text',
          label: '地址',
          name: 'iframeAdress',
          required: false,
          placeholder: '',
          value: 'https://report.anji-plus.com/index.html',
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
          value: 200,
        },
      ],
    }
  }
