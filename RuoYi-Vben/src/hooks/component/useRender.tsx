import { Tag, Button, Tooltip, Switch } from 'ant-design-vue';
import { JsonPreview } from '@/components/CodeEditor';
import Icon from '@/components/Icon/Icon.vue';
import { DictTag } from '@/components/Dict';
import { DictData } from '@/api/system/dict/dictData.model';
import { formatToDate, formatToDateTime } from '@/utils/dateUtil';
import { useGo } from '@/hooks/web/usePage';
import { TooltipPlacement } from 'ant-design-vue/lib/tooltip';
import { getDict } from '@/utils/dict';
import { useMessage } from '@/hooks/web/useMessage';

function renderTag(text: string, color?: string) {
  return <Tag color={color}>{text}</Tag>;
  // return h(Tag, { color }, () => text);
}

/**
 *
 * @param tags 标签list
 * @param wrap 是否换行显示
 * @param [gap=1] 间隔
 * @returns
 */
function renderTags(tags: string[], wrap = false, gap = 1) {
  return (
    <div class={['flex', `gap-${gap}`, wrap ? 'flex-col' : 'flex-row']}>
      {tags.map((tag, index) => {
        return <div key={index}>{renderTag(tag)}</div>;
      })}
    </div>
  );
  // const tagList: VNode[] = [];
  // tags.forEach((item) => {
  //   tagList.push(renderTag(item));
  //   if (wrap) {
  //     tagList.push(h('br'));
  //   }
  // });
  // return h('span', tagList);
}

/**
 *
 * @param json json对象 接受object/string类型
 * @returns json预览
 */
function renderJsonPreview(json: any) {
  if (typeof json !== 'object' && typeof json !== 'string') {
    return <span>{json}</span>;
  }
  if (typeof json === 'object') {
    return <JsonPreview data={json} />;
    // return h(JsonPreview, { data: json });
  }
  try {
    const obj = JSON.parse(json);
    // 基本数据类型可以被转为json
    if (typeof obj !== 'object') {
      return <span>{obj}</span>;
    }
    return <JsonPreview data={obj} />;
  } catch (e) {
    return <span>{json}</span>;
  }
}

// 图标
function renderIcon(icon: string) {
  return <Icon icon={icon}></Icon>;
  // return h(Icon, { icon });
}

// httpMethod
function renderHttpMethodTag(type: string) {
  const method = type.toUpperCase();
  let color = 'default';
  const title = method + '请求';
  switch (method) {
    case 'GET':
      color = 'green';
      break;
    case 'POST':
      color = 'blue';
      break;
    case 'PUT':
      color = 'orange';
      break;
    case 'DELETE':
      color = 'red';
      break;
  }
  return <Tag color={color}>{title}</Tag>;
  // return h(Tag, { color }, () => title);
}

function renderDictTag(value: string, dicts: DictData[]) {
  return <DictTag dicts={dicts} value={value}></DictTag>;
  // return h(DictTag, { dicts, value });
}

/**
 * render多个dictTag
 * @param value key数组 string[]类型
 * @param dicts 字典数组
 * @param wrap 是否需要换行显示
 * @param [gap=1] 间隔
 * @returns render
 */
function renderDictTags(value: string[], dicts: DictData[], wrap = true, gap = 1) {
  if (!Array.isArray(value)) {
    return <div>{value}</div>;
  }
  return (
    <div class={['flex', `gap-${gap}`, wrap ? 'flex-col' : 'flex-row']}>
      {value.map((item, index) => {
        return <div key={index}>{renderDictTag(item, dicts)}</div>;
      })}
    </div>
  );
  // const tagList: VNode[] = [];
  // if (Array.isArray(value)) {
  //   value.forEach((item) => {
  //     tagList.push(renderDictTag(item, dicts));
  //     if (wrap) {
  //       tagList.push(h('br'));
  //     }
  //   });
  // }
  // return h('span', tagList);
}

/**
 * @param value 值
 * @param dictName dictName
 * @returns tag
 */
function renderDict(value: string, dictName: string) {
  const dictInfo = getDict(dictName);
  return renderDictTag(value, dictInfo);
}

/**
 * 加粗文字
 * @param value 文字
 * @param colorCss 颜色 使用windicss类名 如text-red-500
 * @returns
 */
function renderBoldText(value: string, colorCss?: string) {
  return <span class={['font-bold', colorCss]}>{value}</span>;
  // return h('span', { class: ['font-bold', colorCss] }, value);
}

function renderIconSpan(icon: string, value: string, center = false, marginLeft = '2px') {
  const justifyCenter = center ? 'justify-center' : '';
  return (
    <span class={['flex', 'items-center', justifyCenter]}>
      {renderIcon(icon)}
      <span style={{ marginLeft }}>{value}</span>
    </span>
  );
  // return h(
  //   'span',
  //   {
  //     style: {
  //       display: 'flex',
  //       alignItems: 'center',
  //       justifyContent: 'center',
  //     },
  //   },
  //   [renderIcon(icon), ' ', h('span', { style: { marginLeft } }, value)],
  // );
}

function renderDate(date: string) {
  return formatToDate(date);
}

function renderDateTime(date: string) {
  return formatToDateTime(date);
}

function renderHref(value: string, path: string) {
  const go = useGo();
  return (
    <Button type="link" onClick={() => go(path)}>
      {value}
    </Button>
  );
  // return h(
  //   Button,
  //   {
  //     type: 'link',
  //     onClick() {
  //       go(path);
  //     },
  //   },
  //   () => value,
  // );
}

function renderTooltip(value: string, placement: TooltipPlacement = 'top') {
  return (
    <Tooltip placement={placement} title={value}>
      {value}
    </Tooltip>
  );
  // return h(Tooltip, { placement, title: value }, () => value);
}

const { createConfirm } = useMessage();
/**
 * feat 目前无法处理reload
 * 封装switch 用于表格内状态的切换
 * @param record record(reactive)
 * @param api 切换状态的api  默认传参是record
 * @param customContent 自定义内容 回调参数为新状态的str
 * @returns
 */
function renderSwitch(
  record: Recordable,
  api: (...data: any) => Promise<any>,
  customContent?: (newStatusStr: string) => string,
  reload?: () => Promise<any>,
) {
  return (
    <Switch
      checked={record.status}
      checked-children="启用"
      un-checked-children="禁用"
      checkedValue="0"
      unCheckedValue="1"
      onChange={(newStatus) => {
        const lastStatus = record.status;
        const newStatusStr = newStatus === '0' ? '启用' : '禁用';
        // 切换状态
        record.status = newStatus;
        // 自定义内容
        const defaultContent = `确认${newStatusStr}吗?`;
        const content = customContent ? customContent(newStatusStr) : defaultContent;
        createConfirm({
          title: '提示',
          iconType: 'warning',
          content,
          async onOk() {
            try {
              await api(record);
              await reload?.();
            } catch (e) {
              record.status = lastStatus;
            }
          },
          onCancel() {
            record.status = lastStatus;
          },
        });
      }}
    ></Switch>
  );
  // return h(Switch, {
  //   checked: record.status,
  //   'checked-children': '启用',
  //   'un-checked-children': '禁用',
  //   checkedValue: '0',
  //   unCheckedValue: '1',
  //   onChange: (newStatus) => {
  //     const lastStatus = record.status;
  //     const newStatusStr = newStatus === '0' ? '启用' : '禁用';
  //     // 切换状态
  //     record.status = newStatus;
  //     // 自定义内容
  //     const defaultContent = `确认${newStatusStr}吗?`;
  //     const content = customContent ? customContent(newStatusStr) : defaultContent;
  //     createConfirm({
  //       title: '提示',
  //       iconType: 'warning',
  //       content,
  //       async onOk() {
  //         try {
  //           await api(record);
  //         } catch (e) {
  //           record.status = lastStatus;
  //         }
  //       },
  //       onCancel() {
  //         record.status = lastStatus;
  //       },
  //     });
  //   },
  // });
}

const osOptions = [
  { icon: 'devicon:windows8', value: 'windows' },
  { icon: 'devicon:linux', value: 'linux' },
  { icon: 'wpf:macos', value: 'osx' },
  { icon: 'flat-color-icons:android-os', value: 'android' },
  { icon: 'majesticons:iphone-x-apps-line', value: 'iphone' },
];

/**
 * 浏览器图标
 * cn.hutool.http.useragent -> browers
 */
const browserOptions = [
  { icon: 'logos:chrome', value: 'chrome' },
  { icon: 'logos:microsoft-edge', value: 'edge' },
  { icon: 'logos:firefox', value: 'firefox' },
  { icon: 'logos:opera', value: 'opera' },
  { icon: 'logos:safari', value: 'safari' },
  { icon: 'mdi:wechat', value: 'micromessenger' },
  { icon: 'logos:quarkus-icon', value: 'quark' },
  { icon: 'mdi:wechat', value: 'wxwork' },
  { icon: 'simple-icons:tencentqq', value: 'qq' },
  { icon: 'ri:dingding-line', value: 'dingtalk' },
  { icon: 'arcticons:uc-browser', value: 'uc' },
  { icon: 'ri:baidu-fill', value: 'baidu' },
];

function renderOsIcon(os: string, center = false) {
  if (!os) {
    return;
  }
  let current = osOptions.find((item) => os.toLocaleLowerCase().includes(item.value));
  // windows要特殊处理
  if (os.toLocaleLowerCase().includes('windows')) {
    current = osOptions[0];
  }
  if (current) {
    return renderIconSpan(current.icon, os, center, '5px');
  }
  // 返回默认
  const defaultIcon = 'ic:outline-computer';
  return renderIconSpan(defaultIcon, os, center, '5px');
}

function renderBrowserIcon(browser: string, center = false) {
  if (!browser) {
    return;
  }
  const current = browserOptions.find((item) => browser.toLocaleLowerCase().includes(item.value));
  if (current) {
    return renderIconSpan(current.icon, browser, center, '5px');
  }
  // 返回默认
  const defaultIcon = 'ph:browser-duotone';
  return renderIconSpan(defaultIcon, browser, center, '5px');
}

export function useRender() {
  return {
    renderTag,
    renderTags,
    renderJsonPreview,
    renderIcon,
    renderHttpMethodTag,
    renderDictTag,
    renderDictTags,
    renderDict,
    renderBoldText,
    renderIconSpan,
    renderDate,
    renderDateTime,
    renderHref,
    renderTooltip,
    renderSwitch,
    renderOsIcon,
    renderBrowserIcon,
  };
}
