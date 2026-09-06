import { createVNode } from 'vue';
import * as $Icon from '@ant-design/icons-vue';

import { saveAs } from 'file-saver';
import { getToken } from '@/utils/auth';
import errorCode from '@/utils/errorCode';
import { blobValidate } from '@/utils/ruoyi';
import { download as commDownload } from '@/utils/request';

// 实现动态加载antd的图标
export const AutoeeIcon = (props) => {
  let iconName = props.icon;
  if (iconName) {
    // console.log('iconName=' + iconName);
    // 转换plus后台配置的图标名称：临时方案，可以直接在菜单管理里修改对应菜单的图标
    if (iconName === 'system') {
      iconName = 'setting-outlined';
    }
    if (iconName === 'user') {
      iconName = 'user-outlined';
    }
    if (iconName === 'peoples') {
      iconName = 'team-outlined';
    }
    if (iconName === 'tree-table') {
      iconName = 'bars-outlined';
    }
    if (iconName === 'tree') {
      iconName = 'apartment-outlined';
    }
    if (iconName === 'post') {
      iconName = 'flag-outlined';
    }
    if (iconName === 'dict') {
      iconName = 'read-outlined';
    }
    if (iconName === 'edit') {
      iconName = 'profile-outlined';
    }
    if (iconName === 'monitor') {
      iconName = 'monitor-outlined';
    }
    if (iconName === 'tool') {
      iconName = 'tool-outlined';
    }
    if (iconName === 'code') {
      iconName = 'code-outlined';
    }
    if (iconName === 'guide') {
      iconName = 'ie-outlined';
    }
    if (iconName === 'star') {
      iconName = 'filter-outlined';
    }
    if (iconName === 'form') {
      iconName = 'diff-outlined';
    }
    if (iconName === 'log') {
      iconName = 'snippets-outlined';
    }
    if (iconName === 'logininfor') {
      iconName = 'contacts-outlined';
    }
    if (iconName === 'job') {
      iconName = 'field-time-outlined';
    }
    if (iconName === 'online') {
      iconName = 'user-switch-outlined';
    }
    if (iconName === 'message') {
      iconName = 'bell-outlined';
    }

    // 兼容带横线的icon名称：去掉横线并将每个单词的首字母大写
    if (iconName.indexOf('-') != -1) {
      let array = iconName.split('-');
      iconName = '';
      for (let index = 0; index < array.length; index++) {
        let str = array[index];
        // 首字母大写
        str = str.replace(str[0], str[0].toUpperCase());
        iconName = iconName + str;
      }
    }
    // console.log('$Icon[iconName]=' + $Icon[iconName]);
    if ($Icon[iconName]) {
      return createVNode($Icon[iconName]);
    } else {
      // 获取不到对应图标时，使用默认图标
      // return createVNode($Icon['BarsOutlined']);
    }
  }
};
