/*
 * @Descripttion: 滚动文件json
 * @version: 
 * @Author: qianlishi
 * @Date: 2021-08-29 07:00:00
 * @LastEditors: qianlishi
 * @LastEditTime: 2021-09-28 14:17:39
 */
export const widgetMarquee = {
    code: 'widget-marquee',
    type: 'html',
    label: '滚动文本',
    icon: 'iconhengxiangwenzi',
    options: {
      // 配置
      setup: [
        {
          type: 'el-input-text',
          label: '图层名称',
          name: 'layerName',
          required: false,
          placeholder: '',
          value: '跑马灯',
        },
        {
          type: 'el-input-text',
          label: '文本内容',
          name: 'text',
          required: false,
          placeholder: '',
          value: '滚动文本',
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
          value: 'rgba(115,170,229,.5)',
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
/*        {
          type: 'el-input-number',
          label: '滚动速度',
          name: 'jScrollPane',
          //required: false,
          placeholder: '',
          value: '50',
        }*/
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
          value: 40,
        },
      ],
    }
  }