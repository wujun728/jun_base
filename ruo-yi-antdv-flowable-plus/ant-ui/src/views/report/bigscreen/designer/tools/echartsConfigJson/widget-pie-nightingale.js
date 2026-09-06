/*
 * @Descripttion: 南丁格尔玫瑰图 json
 * @version:
 * @Author: qianlishi
 * @Date: 2021-08-29 07:32:40
 * @LastEditors: qianlishi
 * @LastEditTime: 2021-09-28 14:18:05
 */
export const WidgetPieNightingale = {
    code: 'WidgetPieNightingaleRoseArea',
    type: 'chart',
    label: '南丁格尔玫瑰图',
    icon: 'iconnandinggeermeiguitu',
    options: {
      // 配置
      setup: [
        {
          type: 'el-input-text',
          label: '图层名称',
          name: 'layerName',
          required: false,
          placeholder: '',
          value: '南丁格尔玫瑰图',
        },
        {
          type: 'vue-color',
          label: '背景颜色',
          name: 'background',
          required: false,
          placeholder: '',
          value: ''
        },
        {
          type: 'el-select',
          label: '饼图模式',
          name: 'nightingleRosetype',
          required: false,
          placeholder: '',
          selectOptions: [
            {code: 'area', name: '面积模式'},
            {code: 'radius', name: '半径模式'},
          ],
          value: 'area'
        },
        [
          {
            name: '标题设置',
            list: [
              {
                type: 'el-switch',
                label: '标题',
                name: 'isNoTitle',
                required: false,
                placeholder: '',
                value: true
              },
              {
                type: 'el-input-text',
                label: '标题',
                name: 'titleText',
                required: false,
                placeholder: '',
                value: ''
              },
              {
                type: 'vue-color',
                label: '字体颜色',
                name: 'textColor',
                required: false,
                placeholder: '',
                value: '#fff'
              },
              {
                type: 'el-select',
                label: '字体粗细',
                name: 'textFontWeight',
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
                type: 'el-input-number',
                label: '字体大小',
                name: 'textFontSize',
                required: false,
                placeholder: '',
                value: 20
              },
              {
                type: 'el-select',
                label: '字体位置',
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
              {
                type: 'el-input-text',
                label: '副标题',
                name: 'subText',
                required: false,
                placeholder: '',
                value: ''
              },
              {
                type: 'vue-color',
                label: '字体颜色',
                name: 'subTextColor',
                required: false,
                placeholder: '',
                value: ''
              },
              {
                type: 'el-select',
                label: '字体粗细',
                name: 'subTextFontWeight',
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
                type: 'el-input-number',
                label: '字体大小',
                name: 'subTextFontSize',
                required: false,
                placeholder: '',
                value: 12
              },
            ],
          },
          {
            name: '数值设定',
            list: [
              {
                type: 'el-switch',
                label: '显示',
                name: 'isShow',
                required: false,
                placeholder: '',
                value: true,
              },
              {
                type: 'el-switch',
                label: '数值',
                name: 'numberValue',
                require: false,
                placeholder: '',
                value: true,
              },
              {
                type: 'el-switch',
                label: '百分比',
                name: 'percentage',
                require: false,
                placeholder: '',
                value: false,
              },
              {
                type: 'el-input-number',
                label: '字体大小',
                name: 'fontSize',
                required: false,
                placeholder: '',
                value: 14,
              },
              {
                type: 'vue-color',
                label: '字体颜色',
                name: 'subTextColor',
                required: false,
                placeholder: '',
                value: ''
              },
              {
                type: 'el-select',
                label: '字体粗细',
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
            ],
          },
          {
            name: '提示语设置',
            list: [
              {
                type: 'el-input-number',
                label: '字体大小',
                name: 'fontSize',
                required: false,
                placeholder: '',
                value: 12
              },
              {
                type: 'vue-color',
                label: '网格线颜色',
                name: 'lineColor',
                required: false,
                placeholder: '',
                value: ''
              },
            ],
          },
          {
            name: '图例操作',
            list: [
              {
                type: 'el-switch',
                label: '图例',
                name: 'isShowLegend',
                required: false,
                placeholder: '',
                value: true,
              },
              {
                type: 'vue-color',
                label: '字体颜色',
                name: 'lengedColor',
                required: false,
                placeholder: '',
                value: '#fff',
              },
              {
                type: 'el-input-number',
                label: '字体大小',
                name: 'lengedFontSize',
                required: false,
                placeholder: '',
                value: 16,
              },
              {
                type: 'el-input-number',
                label: '图例宽度',
                name: 'lengedWidth',
                required: false,
                placeholder: '',
                value: 15,
              },
              {
                type: 'el-select',
                label: '横向位置',
                name: 'lateralPosition',
                required: false,
                placeholder: '',
                selectOptions: [
                  {code: 'left', name: '左对齐'},
                  {code: 'right', name: '右对齐'},
                ],
                value: ''
              },
              {
                type: 'el-select',
                label: '纵向位置',
                name: 'longitudinalPosition',
                required: false,
                placeholder: '',
                selectOptions: [
                  {code: 'top', name: '顶部'},
                  {code: 'bottom', name: '底部'},
                ],
                value: ''
              },
              {
                type: 'el-select',
                label: '布局前置',
                name: 'layoutFront',
                required: false,
                placeholder: '',
                selectOptions: [
                  {code: 'vertical', name: '竖排'},
                  {code: 'horizontal', name: '横排'},
                ],
                value: ''
              },
            ],
          },
          {
            name: '自定义配色',
            list: [
              {
                type: 'customColor',
                label: '',
                name: 'customColor',
                required: false,
                value: [{color: '#FF801C'}, {color: '#F5FF46'}, {color: '#00FE65'}, {color: '#00FEFF'}, {color: '#ffa800'}]
              },
            ],
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
          value: [{'value': 1048,'name': '搜索引擎'},{'value': 735, 'name': '直接访问'},{'value': 580, 'name': '邮件营销'},{'value': 484,'name':'联盟广告'},{'value':300,'name':'视频广告'}],
        },
        {
          type: 'dycustComponents',
          label: '',
          name: 'dynamicData',
          required: false,
          placeholder: '',
          relactiveDom: 'dataType',
          chartType: 'widget-piechart',
          dictKey: 'PIE_PROPERTIES',
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