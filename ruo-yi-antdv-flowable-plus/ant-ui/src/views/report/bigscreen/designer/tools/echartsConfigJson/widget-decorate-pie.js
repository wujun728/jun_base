/*
 * @Descripttion: 装饰饼图
 * @version:
 * @Author: foming
 * @Date:
 * @LastEditors:
 * @LastEditTime:
 */
export const widgetDecoratePie = {
  code: 'widgetDecoratePieChart',
  type: 'chart',
  label: '装饰饼图',
  icon: 'iconicon_tubiao_bingtu',
  options: {
    // 配置
    setup: [
      {
        type: 'el-input-text',
        label: '图层名称',
        name: 'layerName',
        required: false,
        placeholder: '',
        value: '静态装饰饼图',
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
          name: '最外环设置',
          list: [
            {
              type: 'el-switch',
              label: '显示',
              name: 'isLastRingShow',
              required: false,
              placeholder: '',
              value: true
            },
            {
              type: 'vue-color',
              label: '0%颜色',
              name: 'lastRing0Color',
              required: false,
              placeholder: '',
              value: '#4FADFD'
            },
            {
              type: 'vue-color',
              label: '100%颜色',
              name: 'lastRing100Color',
              required: false,
              placeholder: '',
              value: '#28E8FA'
            },
          ]
        },
        {
          name: '八分环设置',
          list: [
            {
              type: 'vue-color',
              label: '颜色',
              name: 'eightColor',
              required: false,
              placeholder: '',
              value: '#4FADFD'
            },
          ]
        },
        {
          name: '虚线环设置',
          list: [
            {
              type: 'el-input-number',
              label: '虚线数量',
              name: 'dottedNum',
              required: false,
              placeholder: '',
              value: 40
            },
            {
              type: 'vue-color',
              label: '颜色',
              name: 'dottedColor',
              required: false,
              placeholder: '',
              value: '#28E8FA'
            },
          ]
        },
        {
          name: '三分环设置',
          list: [
            {
              type: 'vue-color',
              label: '一段颜色',
              name: 'three1Color',
              required: false,
              placeholder: '',
              value: '#fc8d89'
            },
            {
              type: 'vue-color',
              label: '二段颜色',
              name: 'three2Color',
              required: false,
              placeholder: '',
              value: '#46d3f3'
            },
            {
              type: 'vue-color',
              label: '三段颜色',
              name: 'three3Color',
              required: false,
              placeholder: '',
              value: 'rgba(203,203,203,.2)'
            },
          ]
        },
        {
          name: '外指标环设置',
          list: [
            {
              type: 'el-switch',
              label: '环显示',
              name: 'isOutRingShow',
              required: false,
              placeholder: '',
              value: false
            },
            {
              type: 'vue-color',
              label: '环颜色',
              name: 'outRingColor',
              required: false,
              placeholder: '',
              value: '#0dc2fe'
            },
            {
              type: 'el-input-number',
              label: '环宽度',
              name: 'outRingWidth',
              required: false,
              placeholder: '',
              value: 1
            },
            {
              type: 'el-switch',
              label: '指标线显示',
              name: 'isOutSplitShow',
              required: false,
              placeholder: '',
              value: true
            },
            {
              type: 'el-input-number',
              label: '指标线数量',
              name: 'outSplitNum',
              required: false,
              placeholder: '',
              value: 30
            },
            {
              type: 'el-input-number',
              label: '指标线长度',
              name: 'outSplitLength',
              required: false,
              placeholder: '',
              value: 32
            },
            {
              type: 'el-input-number',
              label: '指标线宽度',
              name: 'outSplitWidth',
              required: false,
              placeholder: '',
              value: 2
            },
            {
              type: 'vue-color',
              label: '指标线颜色',
              name: 'outSplitColor',
              required: false,
              placeholder: '',
              value: '#0dc2fe'
            },
            {
              type: 'el-switch',
              label: '刻度线显示',
              name: 'isOutTickShow',
              required: false,
              placeholder: '',
              value: false
            },
            {
              type: 'el-input-number',
              label: '刻度线数量',
              name: 'outTickNum',
              required: false,
              placeholder: '',
              value: 5
            },
            {
              type: 'el-input-number',
              label: '刻度线长度',
              name: 'outTickLength',
              required: false,
              placeholder: '',
              value: 20
            },
            {
              type: 'el-input-number',
              label: '刻度线宽度',
              name: 'outTickWidth',
              required: false,
              placeholder: '',
              value: 2
            },
            {
              type: 'vue-color',
              label: '刻度线颜色',
              name: 'outTickColor',
              required: false,
              placeholder: '',
              value: '#0dc2fe'
            },
          ]
        },
        {
          name: '里指标环设置',
          list: [
            {
              type: 'el-switch',
              label: '环显示',
              name: 'isInRingShow',
              required: false,
              placeholder: '',
              value: false
            },
            {
              type: 'vue-color',
              label: '环颜色',
              name: 'inRingColor',
              required: false,
              placeholder: '',
              value: '#0dc2fe'
            },
            {
              type: 'el-input-number',
              label: '环宽度',
              name: 'inRingWidth',
              required: false,
              placeholder: '',
              value: 1
            },
            {
              type: 'el-switch',
              label: '指标线显示',
              name: 'isInSplitShow',
              required: false,
              placeholder: '',
              value: false
            },
            {
              type: 'el-input-number',
              label: '指标线数量',
              name: 'inSplitNum',
              required: false,
              placeholder: '',
              value: 30
            },
            {
              type: 'el-input-number',
              label: '指标线长度',
              name: 'inSplitLength',
              required: false,
              placeholder: '',
              value: 20
            },
            {
              type: 'el-input-number',
              label: '指标线宽度',
              name: 'inSplitWidth',
              required: false,
              placeholder: '',
              value: 2
            },
            {
              type: 'vue-color',
              label: '指标线颜色',
              name: 'inSplitColor',
              required: false,
              placeholder: '',
              value: '#0dc2fe'
            },
            {
              type: 'el-switch',
              label: '刻度线显示',
              name: 'isInTickShow',
              required: false,
              placeholder: '',
              value: true
            },
            {
              type: 'el-input-number',
              label: '刻度线数量',
              name: 'inTickNum',
              required: false,
              placeholder: '',
              value: 5
            },
            {
              type: 'el-input-number',
              label: '刻度线长度',
              name: 'inTickLength',
              required: false,
              placeholder: '',
              value: 20
            },
            {
              type: 'el-input-number',
              label: '刻度线宽度',
              name: 'inTickWidth',
              required: false,
              placeholder: '',
              value: 2
            },
            {
              type: 'vue-color',
              label: '刻度线颜色',
              name: 'inTickColor',
              required: false,
              placeholder: '',
              value: '#0dc2fe'
            },
          ]
        },
        {
          name: '环外环设置',
          list: [
            {
              type: 'el-switch',
              label: '显示',
              name: 'isRingOnRingShow',
              required: false,
              placeholder: '',
              value: true
            },
            {
              type: 'vue-color',
              label: '颜色',
              name: 'ringOnRingColor',
              required: false,
              placeholder: '',
              value: '#4FADFD'
            },
          ]
        },
        {
          name: '中饼图设置',
          list: [
            {
              type: 'el-input-number',
              label: '半径',
              name: 'pieWidth',
              required: false,
              placeholder: '',
              value: 40
            },
            {
              type: 'vue-color',
              label: '颜色',
              name: 'pieColor',
              required: false,
              placeholder: '',
              value: '#0dc2fe'
            },
            {
              type: 'vue-color',
              label: '边框颜色',
              name: 'pieBorderColor',
              required: false,
              placeholder: '',
              value: '#3D4268'
            },
            {
              type: 'el-select',
              label: '分块',
              name: 'pieBlocks',
              required: false,
              placeholder: '',
              selectOptions: [
                {code: 'four', name: '十字星'},
                {code: 'five', name: '五角星'},
                {code: 'six', name: '六芒星'},
              ],
              value: 'six'
            },
          ]
        },
      ],
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
