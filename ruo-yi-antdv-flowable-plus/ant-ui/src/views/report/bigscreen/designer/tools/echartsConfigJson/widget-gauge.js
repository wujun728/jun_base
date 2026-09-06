/*
 * @Descripttion: 仪表盘 json
 * @version:
 * @Author: qianlishi
 * @Date: 2021-08-29 07:30:25
 * @LastEditors: qianlishi
 * @LastEditTime: 2021-09-28 14:13:17
 */
export const widgetGauge = {
  code: 'widget-gauge',
  type: 'chart',
  label: '仪表盘',
  icon: 'iconyibiaopan',
  options: {
    // 配置
    setup: [
      {
        type: 'el-input-text',
        label: '图层名称',
        name: 'layerName',
        required: false,
        placeholder: '',
        value: '仪表盘',
      },
      {
        type: 'vue-color',
        label: '背景颜色',
        name: 'background',
        required: false,
        placeholder: '',
        value: ''
      },
      [
        {
          name: '圆环设置',
          list: [
            {
              type: 'el-switch',
              label: '显示',
              name: 'ringShow',
              required: false,
              placeholder: '',
              value: true,
            },
            {
              type: 'el-input-number',
              label: '圆环宽度',
              name: 'pieWeight',
              require: false,
              placeholder: '',
              value: 10,
            },
          ]
        },
        {
          name: '0%~30%渐变色',
          list: [
            {
              type: 'vue-color',
              label: '0处颜色',
              name: 'color30p0',
              required: false,
              placeholder: '',
              value: 'rgba(0, 237, 3,0.1)'
            },
            {
              type: 'vue-color',
              label: '0.5处颜色',
              name: 'color30p5',
              required: false,
              placeholder: '',
              value: 'rgba(0, 237, 3,0.6)'
            },
            {
              type: 'vue-color',
              label: '1处颜色',
              name: 'color30p10',
              required: false,
              placeholder: '',
              value: 'rgba(0, 237, 3,1)'
            },
          ]
        },
        {
          name: '30%~70%渐变色',
          list: [
            {
              type: 'vue-color',
              label: '0处颜色',
              name: 'color70p0',
              required: false,
              placeholder: '',
              value: 'rgba(255, 184, 0,0.1)'
            },
            {
              type: 'vue-color',
              label: '0.5处颜色',
              name: 'color70p5',
              required: false,
              placeholder: '',
              value: 'rgba(255, 184, 0,0.6)'
            },
            {
              type: 'vue-color',
              label: '1处颜色',
              name: 'color70p10',
              required: false,
              placeholder: '',
              value: 'rgba(255, 184, 0,1)'
            },
          ]
        },
        {
          name: '70%~100%渐变色',
          list: [
            {
              type: 'vue-color',
              label: '0处颜色',
              name: 'color100p0',
              required: false,
              placeholder: '',
              value: 'rgba(175, 36, 74,0.1)'
            },
            {
              type: 'vue-color',
              label: '0.5处颜色',
              name: 'color100p5',
              required: false,
              placeholder: '',
              value: 'rgba(255, 36, 74,0.6)'
            },
            {
              type: 'vue-color',
              label: '1处颜色',
              name: 'color100p10',
              required: false,
              placeholder: '',
              value: 'rgba(255, 36, 74,1)'
            },
          ]
        },
        {
          name: '刻度线',
          list: [
            {
              type: 'el-switch',
              label: '显示',
              name: 'tickShow',
              required: false,
              placeholder: '',
              value: true,
            },
            {
              type: 'el-input-number',
              label: '距离',
              name: 'tickDistance',
              require: false,
              placeholder: '',
              value: 0,
            },
            {
              type: 'el-input-number',
              label: '长度',
              name: 'tickLength',
              require: false,
              placeholder: '',
              value: 10,
            },
            {
              type: 'el-input-number',
              label: '宽度',
              name: 'tickWidth',
              require: false,
              placeholder: '',
              value: 2,
            },
          ]
        },
        {
          name: '指标线',
          list: [
            {
              type: 'el-switch',
              label: '显示',
              name: 'splitShow',
              required: false,
              placeholder: '',
              value: true,
            },
            {
              type: 'el-input-number',
              label: '距离',
              name: 'splitDistance',
              require: false,
              placeholder: '',
              value: 0,
            },
            {
              type: 'el-input-number',
              label: '长度',
              name: 'splitLength',
              require: false,
              placeholder: '',
              value: 14,
            },
            {
              type: 'el-input-number',
              label: '宽度',
              name: 'splitWidth',
              require: false,
              placeholder: '',
              value: 4,
            },
          ]
        },
        {
          name: '指标',
          list: [
            {
              type: 'el-switch',
              label: '显示',
              name: 'labelShow',
              required: false,
              placeholder: '',
              value: true,
            },
            {
              type: 'el-input-number',
              label: '距离',
              name: 'labelDistance',
              require: false,
              placeholder: '',
              value: 0,
            },
            {
              type: 'el-input-number',
              label: '字号',
              name: 'labelFontSize',
              require: false,
              placeholder: '',
              value: 10,
            },
            {
              type: 'vue-color',
              label: '颜色',
              name: 'labelColor',
              required: false,
              placeholder: '',
              value: '#fff',
            },
          ]
        },
        {
          name: '数值',
          list: [
            {
              type: 'vue-color',
              label: '颜色',
              name: 'labelColor',
              required: false,
              placeholder: '',
              value: '#fff',
            },
            {
              type: 'el-input-number',
              label: '字号',
              name: 'labelFontSize',
              require: false,
              placeholder: '',
              value: 14,
            },
            {
              type: 'el-select',
              label: '字体粗细',
              name: 'labelFontWeight',
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
          ]
        },
      ],
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
        value: [
          {'num':50}
        ],
      },
      {
        type: 'dycustComponents',
        label: '',
        name: 'dynamicData',
        required: false,
        placeholder: '',
        relactiveDom: 'dataType',
        chartType: 'widget-text',
        dictKey: 'TEXT_PROPERTIES',
        relactiveDomValue: 'dynamicData',
        value: '',
      },
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
        value: 400,
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
