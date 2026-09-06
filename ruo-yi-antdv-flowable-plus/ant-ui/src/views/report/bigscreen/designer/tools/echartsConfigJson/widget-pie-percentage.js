/*
 * @Descripttion: 百分比图 json
 * @version:
 * @Author: qianlishi
 * @Date: 2021-08-29 07:34:01
 * @LastEditors: qianlishi
 * @LastEditTime: 2021-09-28 14:18:45
 */
export const widgetPiePercentage = {
    code: 'widgetPiePercentageChart',
    type: 'chart',
    label: '百分比图',
    icon: 'iconbaifenbi',
    options: {
      // 配置
      setup: [
        {
          type: 'el-input-text',
          label: '图层名称',
          name: 'layerName',
          required: false,
          placeholder: '',
          value: '百分比图',
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
            name: '数值设置',
            list: [
              {
                type: 'vue-color',
                label: '数值颜色',
                name: 'textNumColor',
                required: false,
                placeholder: '',
                value: '#fff'
              },
              {
                type: 'el-input-number',
                label: '数值字体',
                name: 'textNumFontSize',
                required: false,
                placeholder: '',
                value: 40
              },
              {
                type: 'el-select',
                label: '字体粗细',
                name: 'textNumFontWeight',
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
                type: 'vue-color',
                label: '%号颜色',
                name: 'textPerColor',
                required: false,
                placeholder: '',
                value: '#fff'
              },
              {
                type: 'el-input-number',
                label: '%号字体',
                name: 'textPerFontSize',
                required: false,
                placeholder: '',
                value: 20
              },
              {
                type: 'el-select',
                label: '字体粗细',
                name: 'textPerFontWeight',
                required: false,
                placeholder: '',
                selectOptions: [
                  {code: 'normal', name: '正常'},
                  {code: 'bold', name: '粗体'},
                  {code: 'bolder', name: '特粗体'},
                  {code: 'lighter', name: '细体'}
                ],
                value: 'normal'
              }
            ],
          },
          {
            name: '圆环设置',
            list: [
              {
                type: 'el-input-number',
                label: '刻度数量',
                name: 'lineNumber',
                required: false,
                placeholder: '',
                value: 8
              },
              {
                type: 'el-input-number',
                label: '刻度长度',
                name: 'lineLength',
                required: false,
                placeholder: '',
                value: 19
              },
              {
                type: 'el-input-number',
                label: '刻度宽度',
                name: 'lineWidth',
                required: false,
                placeholder: '',
                value: 2
              },
              {
                type: 'vue-color',
                label: '刻度颜色',
                name: 'lineColor',
                required: false,
                placeholder: '',
                value: '#061740'
              },
            ]
          },
          {
            name: '渐变色',
            list: [
              {
                type: 'vue-color',
                label: '0%处颜色',
                name: 'color0Start',
                required: false,
                placeholder: '',
                value: '#4FADFD'
              },
              {
                type: 'vue-color',
                label: '100%颜色',
                name: 'color100End',
                required: false,
                placeholder: '',
                value: '#28E8FA'
              },
              {
                type: 'vue-color',
                label: '余处颜色',
                name: 'colorsurplus',
                required: false,
                placeholder: '',
                value: '#173164'
              },
            ]
          }
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
            {'num':60}
          ],
        },
        {
          type: 'dycustComponents',
          label: '',
          name: 'dynamicData',
          required: false,
          placeholder: '',
          relactiveDom: 'dataType',
          chartType: 'widget-piechart',
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
          value: 300,
        },
      ],
    }
  }
