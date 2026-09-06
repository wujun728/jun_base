/*
 * @Descripttion: 柱状对比图 json
 * @version:
 * @Author: qianlishi
 * @Date: 2021-08-29 07:39:35
 * @LastEditors: qianlishi
 * @LastEditTime: 2021-09-28 14:09:58
 */
export const widgetBarCompare = {
    code: 'widgetBarCompareChart',
    type: 'chart',
    label: '柱状对比图',
    icon: 'iconduibitupu',
    options: {
      // 配置
      setup: [
        {
          type: 'el-input-text',
          label: '图层名称',
          name: 'layerName',
          required: false,
          placeholder: '',
          value: '柱状对比图',
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
            name: '柱体设置',
            list: [
              {
                type: 'el-slider',
                label: '最大宽度',
                name: 'maxWidth',
                required: false,
                placeholder: '',
                value: 15,
              },
              {
                type: 'el-slider',
                label: '圆角',
                name: 'radius',
                require: false,
                placeholder: '',
                value: 5,
              },
            ],
          },
          {
            name: '标题设置',
            list: [
              {
                type: 'el-switch',
                label: '标题',
                name: 'isNoTitle',
                required: false,
                placeholder: '',
                value: true,
              },
              {
                type: 'el-input-text',
                label: '标题',
                name: 'titleText',
                required: false,
                placeholder: '',
                value: '',
              },
              {
                type: 'vue-color',
                label: '字体颜色',
                name: 'textColor',
                required: false,
                placeholder: '',
                value: '#FFD700'
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
                value: 'center'
              },
            ],
          },
          {
            name: '左X轴设置',
            list: [
              {
                type: 'el-switch',
                label: '显示',
                name: 'hideXLeft',
                required: false,
                placeholder: '',
                value: true,
              },
              {
                type: 'el-input-number',
                label: '数值间隔',
                name: 'splitNumberLeft',
                required: false,
                placeholder: '',
                value: ''
              },
              {
                type: 'vue-color',
                label: '数值颜色',
                name: 'XcolorLeft',
                required: false,
                placeholder: '',
                value: '#fff',
              },
              {
                type: 'el-input-number',
                label: '数值字号',
                name: 'fontSizeXLeft',
                required: false,
                placeholder: '',
                value: 14,
              },
              {
                type: 'el-switch',
                label: '刻度线',
                name: 'tickLineLeft',
                require: false,
                placeholder: '',
                value: false,
              },
              {
                type: 'el-switch',
                label: 'X轴线',
                name: 'xLineLeft',
                require: false,
                placeholder: '',
                value: false,
              },
              {
                type: 'vue-color',
                label: '轴颜色',
                name: 'lineColorXLeft',
                required: false,
                placeholder: '',
                value: '#fff',
              },
              {
                type: 'el-switch',
                label: '竖分割线',
                name: 'SplitLineLeft',
                require: false,
                placeholder: '',
                value: false,
              },
              {
                type: 'vue-color',
                label: '分割线颜色',
                name: 'SplitLineColorLeft',
                required: false,
                placeholder: '',
                value: '#fff',
              },
              {
                type: 'el-input-number',
                label: '分割线宽度',
                name: 'SplitLinefontSizeLeft',
                required: false,
                placeholder: '',
                value: 1,
              },
              {
                type: 'el-switch',
                label: '边框线',
                name: 'frameLineLeft',
                require: false,
                placeholder: '',
                value: false,
              },
            ],
          },
          {
            name: '右X轴设置',
            list: [
              {
                type: 'el-switch',
                label: '显示',
                name: 'hideXRight',
                required: false,
                placeholder: '',
                value: true,
              },
              {
                type: 'el-input-number',
                label: '数值间隔',
                name: 'splitNumberRight',
                required: false,
                placeholder: '',
                value: ''
              },
              {
                type: 'vue-color',
                label: '数值颜色',
                name: 'XcolorRight',
                required: false,
                placeholder: '',
                value: '#fff',
              },
              {
                type: 'el-input-number',
                label: '数值字号',
                name: 'fontSizeXRight',
                required: false,
                placeholder: '',
                value: 14,
              },
              {
                type: 'el-switch',
                label: '刻度线',
                name: 'tickLineRight',
                require: false,
                placeholder: '',
                value: false,
              },
              {
                type: 'el-switch',
                label: 'X轴线',
                name: 'xLineRight',
                require: false,
                placeholder: '',
                value: false,
              },
              {
                type: 'vue-color',
                label: '轴颜色',
                name: 'lineColorXRight',
                required: false,
                placeholder: '',
                value: '#fff',
              },
              {
                type: 'el-switch',
                label: '竖分割线',
                name: 'SplitLineRight',
                require: false,
                placeholder: '',
                value: false,
              },
              {
                type: 'vue-color',
                label: '分割线颜色',
                name: 'SplitLineColorRight',
                required: false,
                placeholder: '',
                value: '#fff',
              },
              {
                type: 'el-input-number',
                label: '分割线宽度',
                name: 'SplitLinefontSizeRight',
                required: false,
                placeholder: '',
                value: 1,
              },
              {
                type: 'el-switch',
                label: '边框线',
                name: 'frameLineRight',
                require: false,
                placeholder: '',
                value: false,
              },
            ],
          },
          {
            name: 'Y轴设置',
            list: [
              {
                type: 'el-switch',
                label: '显示',
                name: 'hideY',
                required: false,
                placeholder: '',
                value: true,
              },
              {
                type: 'vue-color',
                label: '数值颜色',
                name: 'colorY',
                required: false,
                placeholder: '',
                value: '#fff',
              },
              {
                type: 'el-input-number',
                label: '数值字号',
                name: 'fontSizeY',
                required: false,
                placeholder: '',
                value: 14,
              },
              {
                type: 'el-select',
                label: '数值对齐',
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
              {
                type: 'el-switch',
                label: '刻度线',
                name: 'tickLineY',
                require: false,
                placeholder: '',
                value: false,
              },
              {
                type: 'el-switch',
                label: 'Y轴线',
                name: 'lineY',
                require: false,
                placeholder: '',
                value: false,
              },
              {
                type: 'vue-color',
                label: '轴颜色',
                name: 'lineColorY',
                required: false,
                placeholder: '',
                value: '#fff',
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
                value: true
              },
              {
                type: 'el-input-number',
                label: '字体大小',
                name: 'fontSize',
                required: false,
                placeholder: '',
                value: 14
              },
              {
                type: 'vue-color',
                label: '字体颜色',
                name: 'subTextColor',
                required: false,
                placeholder: '',
                value: '#fff'
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
                name: 'tipsFontSize',
                required: false,
                placeholder: '',
                value: 16
              },
              {
                type: 'vue-color',
                label: '字体颜色',
                name: 'lineColor',
                required: false,
                placeholder: '',
              },
            ],
          },
          {
            name: '坐标轴边距设置',
            list: [
              {
                type: 'el-slider',
                label: '左右边距(像素)',
                name: 'marginLeftRight',
                required: false,
                placeholder: '',
                value: 10,
              },
              {
                type: 'el-slider',
                label: '顶边距(像素)',
                name: 'marginTop',
                required: false,
                placeholder: '',
                value: 40,
              },
              {
                type: 'el-slider',
                label: '底边距(像素)',
                name: 'marginBottom',
                required: false,
                placeholder: '',
                value: 10,
              },
            ],
          },
          {
            name: '图例操作',
            list: [
              {
                type: 'el-switch',
                label: '显示',
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
                  {code: 'center', name: '居中'},
                  {code: 'left', name: '左对齐'},
                  {code: 'right', name: '右对齐'},
                ],
                value: 'center'
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
                value: 'top'
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
                value: 'horizontal'
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
                value: [{color: '#36c5e7'}, {color: '#e68b55'}],
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
          value: [
            {'axis':'07-25','name':'success','data':'2'},
            {'axis':'07-25','name':'fail','data':'10'},
            {'axis':'07-26','name':'success','data':'5'},
            {'axis':'07-26','name':'fail','data':'20'},
            {'axis':'07-27','name':'success','data':'15'},
            {'axis':'07-27','name':'fail','data':'30'},
            {'axis':'07-28','name':'success','data':'10'},
            {'axis':'07-28','name':'fail','data':'12'},
            {'axis':'07-29','name':'success','data':'9'},
            {'axis':'07-29','name':'fail','data':'16'},
          ],
        },
        {
          type: 'dycustComponents',
          label: '',
          name: 'dynamicData',
          required: false,
          placeholder: '',
          relactiveDom: 'dataType',
          relactiveDomValue: 'dynamicData',
          chartType: 'widget-stackchart',
          dictKey: 'STACK_PROPERTIES',
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
