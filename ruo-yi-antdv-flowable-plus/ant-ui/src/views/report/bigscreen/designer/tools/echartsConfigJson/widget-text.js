/*
 * @Descripttion: 文本json文件
 * @version: 
 * @Author: qianlishi
 * @Date: 2021-08-29 06:52:13
 * @LastEditors: qianlishi
 * @LastEditTime: 2021-09-28 14:20:12
 */
export const widgetText =  {
    code: 'widget-text',
    type: 'html',
    label: '文本',
    icon: 'iconziyuan',
    options: {
      // 配置
      setup: [
        {
          type: 'el-input-text',
          label: '图层名称',
          name: 'layerName',
          required: false,
          placeholder: '',
          value: '文本框',
        },
        {
          type: 'el-input-text',
          label: '文本内容',
          name: 'text',
          required: false,
          placeholder: '',
          value: '文本框',
        },
        {
          type: 'el-input-number',
          label: '字体大小',
          name: 'fontSize',
          required: false,
          placeholder: '',
          value: '26',
        },
        {
          type: 'vue-color',
          label: '字体颜色',
          name: 'color',
          required: false,
          placeholder: '',
          value: '#FAD400',
        },
        {
          type: 'el-input-number',
          label: '字体间距',
          name: 'letterSpacing',
          required: false,
          placeholder: '',
          value: '0',
        },
        {
          type: 'vue-color',
          label: '字体背景',
          name: 'background',
          required: false,
          placeholder: '',
          value: 'rgba(115,170,229,.0)',
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
          value: 'center'
        },
      ],
      // 数据
      data: [
        {
          type: 'el-radio-group',
          label: '数据类型',
          name: 'dataType',
          require: false,
          placeholder: '',
          selectValue: true,
          selectOptions: [
            {
              code: 'staticData',
              name: '静态数据',
            },
            {
              code: 'dynamicData',
              name: '动态数据',
            },
          ],
          value: 'staticData',
        },
        {
          type: 'el-input-number',
          label: '刷新时间(毫秒)',
          name: 'refreshTime',
          relactiveDom: 'dataType',
          relactiveDomValue: 'dynamicData',
          value: 5000
        },
        {
          type: 'el-button',
          label: '静态数据',
          name: 'staticData',
          required: false,
          placeholder: '',
          relactiveDom: 'dataType',
          relactiveDomValue: 'staticData',
          value: '文本框',
        },
        {
          type: 'dycustComponents',
          label: '',
          name: 'dynamicData',
          required: false,
          placeholder: '',
          relactiveDom: 'dataType',
          relactiveDomValue: 'dynamicData',
          chartType: 'widget-text',
          dictKey: 'TEXT_PROPERTIES',
          value: '',
        }
      ],

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
          value: 100,
        },
        {
          type: 'el-input-number',
          label: '高度',
          name: 'height',
          required: false,
          placeholder: '该容器在1080px大屏中的高度',
          value: 40
        },
      ],
    }
  }