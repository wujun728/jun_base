import { Tag } from 'ant-design-vue';
import { VNode, h } from 'vue';

interface TagType {
  [key: string]: { label: string; color: string };
}

export const tagTypes: TagType = {
  /** 由于和elementUI不同 用于替换颜色 */
  default: { label: '默认(default)', color: 'default' },
  primary: { label: '主要(primary)', color: 'processing' },
  success: { label: '成功(success)', color: 'success' },
  info: { label: '信息(info)', color: 'default' },
  warning: { label: '警告(warning)', color: 'warning' },
  danger: { label: '危险(danger)', color: 'error' },
  /** 自定义预设 color可以为16进制颜色 */
  pink: { label: 'pink', color: 'pink' },
  red: { label: 'red', color: 'red' },
  orange: { label: 'orange', color: 'orange' },
  green: { label: 'green', color: 'green' },
  cyan: { label: 'cyan', color: 'cyan' },
  purple: { label: 'purple', color: 'purple' },
};

// 字典选择使用 { label: string; value: string }[]
interface Options {
  label: string | VNode;
  value: string;
}

export const tagSelectOptions = () => {
  const selectArray: Options[] = [];
  Object.keys(tagTypes).forEach((key) => {
    const label = tagTypes[key].label;
    const color = tagTypes[key].color;
    selectArray.push({
      label: h(Tag, { color }, () => label),
      value: key,
    });
  });
  return selectArray;
};
