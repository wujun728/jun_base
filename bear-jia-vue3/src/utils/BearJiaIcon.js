import { createVNode } from 'vue';
import * as AntIcons from '@ant-design/icons-vue';
import SvgIcon from '@/components/SvgIcon/index.vue';

const ANT_ICON_THEME_SUFFIX_REG = /(Outlined|Filled|TwoTone)$/;

// 保留历史映射：兼容旧数据中的 icon 名称
const LEGACY_ICON_ALIAS = {
  system: 'setting-outlined',
  user: 'user-outlined',
  peoples: 'team-outlined',
  tree: 'apartment-outlined',
  post: 'flag-outlined',
  dict: 'read-outlined',
  edit: 'profile-outlined',
  monitor: 'monitor-outlined',
  tool: 'tool-outlined',
  guide: 'ie-outlined',
  star: 'filter-outlined',
  form: 'diff-outlined',
  log: 'snippets-outlined',
  job: 'field-time-outlined',
  online: 'user-switch-outlined',
  message: 'bell-outlined',
};

// 兼容旧 custom 命名与当前文件名不一致的场景
const CUSTOM_ICON_ALIAS = {
  'tree-table': 'treeTable',
  'redis-list': 'redis',
  code: 'codeNew',
  logininfor: 'loginLog',
};

const customIconModules = import.meta.glob('@/assets/icons/*.svg');
const customIconNameSet = new Set(
  Object.keys(customIconModules).map((path) => path.split('/').pop().replace('.svg', ''))
);

const toPascalCase = (value) =>
  value
    .split('-')
    .filter(Boolean)
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join('');

const toCamelCase = (value) =>
  value
    .split('-')
    .filter(Boolean)
    .map((word, index) => (index === 0 ? word : word.charAt(0).toUpperCase() + word.slice(1)))
    .join('');

const isExplicitAntIconName = (iconName) => {
  return (
    ANT_ICON_THEME_SUFFIX_REG.test(iconName) ||
    iconName.includes('-outlined') ||
    iconName.includes('-filled') ||
    iconName.includes('-two-tone')
  );
};

const resolveCustomIconName = (iconName) => {
  if (!iconName || iconName === '#') return null;
  const raw = String(iconName).trim();
  if (!raw) return null;

  const candidates = [raw];
  if (raw.includes('-')) {
    candidates.push(toCamelCase(raw));
  }
  if (raw.startsWith('#icon-')) {
    const spriteName = raw.slice('#icon-'.length);
    candidates.push(spriteName);
    if (spriteName.includes('-')) {
      candidates.push(toCamelCase(spriteName));
    }
  }

  const alias = CUSTOM_ICON_ALIAS[raw];
  if (alias) {
    candidates.push(alias);
  }

  for (const candidate of candidates) {
    if (customIconNameSet.has(candidate)) {
      return candidate;
    }
  }
  return null;
};

const resolveAntIconName = (iconName) => {
  if (!iconName || iconName === '#') return null;
  const raw = String(iconName).trim();
  if (!raw) return null;

  const candidates = [];
  const addCandidate = (name) => {
    if (name && !candidates.includes(name)) {
      candidates.push(name);
    }
  };

  addCandidate(raw);

  if (raw.includes('-')) {
    const pascal = toPascalCase(raw);
    addCandidate(pascal);
    if (!ANT_ICON_THEME_SUFFIX_REG.test(pascal)) {
      addCandidate(`${pascal}Outlined`);
    }
  } else {
    const pascal = raw.charAt(0).toUpperCase() + raw.slice(1);
    addCandidate(pascal);
    if (!ANT_ICON_THEME_SUFFIX_REG.test(raw)) {
      addCandidate(`${pascal}Outlined`);
    }

    if (/outlined$/i.test(raw) || /filled$/i.test(raw) || /twotone$/i.test(raw)) {
      addCandidate(
        pascal
          .replace(/outlined$/i, 'Outlined')
          .replace(/filled$/i, 'Filled')
          .replace(/twotone$/i, 'TwoTone')
      );
    }
  }

  for (const candidate of candidates) {
    if (AntIcons[candidate]) {
      return candidate;
    }
  }
  return null;
};

const resolveIcon = (iconName) => {
  if (!iconName || iconName === '#') {
    return { type: 'ant', name: 'BarsOutlined' };
  }

  const raw = String(iconName).trim();
  if (!raw) {
    return { type: 'ant', name: 'BarsOutlined' };
  }

  // 显式 ant 图标优先（例如 SearchOutlined / search-outlined）
  if (isExplicitAntIconName(raw)) {
    const explicitAntIcon = resolveAntIconName(raw);
    if (explicitAntIcon) {
      return { type: 'ant', name: explicitAntIcon };
    }
  }

  // 自定义 SVG 图标（assets/icons/*.svg）
  const customIcon = resolveCustomIconName(raw);
  if (customIcon) {
    return { type: 'custom', name: customIcon };
  }

  // 直接尝试解析 ant 图标
  const antIcon = resolveAntIconName(raw);
  if (antIcon) {
    return { type: 'ant', name: antIcon };
  }

  // 历史映射兜底
  const legacyAlias = LEGACY_ICON_ALIAS[raw];
  if (legacyAlias) {
    const legacyCustom = resolveCustomIconName(legacyAlias);
    if (legacyCustom) {
      return { type: 'custom', name: legacyCustom };
    }
    const legacyAnt = resolveAntIconName(legacyAlias);
    if (legacyAnt) {
      return { type: 'ant', name: legacyAnt };
    }
  }

  return { type: 'ant', name: 'BarsOutlined' };
};

// 实现统一加载 icon：支持 Ant Design 图标 + 自定义 SVG
export const BearJiaIcon = (props = {}) => {
  const { icon, size = '1em', color = 'currentColor', className = '', ...restProps } = props;

  const resolved = resolveIcon(icon);

  if (resolved.type === 'custom') {
    return createVNode(SvgIcon, {
      ...restProps,
      iconClass: resolved.name,
      size,
      color,
      className,
    });
  }

  const IconComponent = AntIcons[resolved.name] || AntIcons.BarsOutlined;
  return createVNode(IconComponent, restProps);
};
