/*
 * @Descripttion: 图片json
 * @version: 
 * @Author: qianlishi
 * @Date: 2021-08-29 07:07:23
 * @LastEditors: qianlishi
 * @LastEditTime: 2021-09-28 14:14:51
 */
export const widgetImage = {
    code: 'widget-image',
    type: 'html',
    label: '图片',
    icon: 'icontupian',
    options: {
      // 配置
      setup: [
        {
          type: 'el-input-text',
          label: '图层名称',
          name: 'layerName',
          required: false,
          placeholder: '',
          value: '图片',
        },
        {
          type: 'el-switch',
          label: '开启旋转',
          name: 'startRotate',
          required: false,
          placeholder: '',
          value: false,
        },
        {
          type: 'el-slider',
          label: '透明度',
          name: 'transparency',
          required: false,
          placeholder: '',
          value: 100
        },
        {
          type: 'el-input-number',
          label: '圆角',
          name: 'borderRadius',
          required: false,
          placeholder: '',
          value: '0'
        },
        {
          type: 'custom-upload',
          label: '图片地址',
          name: 'imageAdress',
          required: false,
          placeholder: '',
          value: 'http://10.108.26.197:9095/file/download/fd20d563-00aa-45e2-b5db-aff951f814ec',
        },
        {
          type: 'vue-color',
          label: '背景颜色',
          name: 'background',
          required: false,
          placeholder: '',
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
