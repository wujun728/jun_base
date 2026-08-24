import defaultSettings from '@/settings';
import i18n from '@/lang';

const title = defaultSettings.title || 'OA系统企业端';

export default function getPageTitle(key) {
  const hasKey = i18n.te(`route.${key}`);
  if (hasKey) {
    const pageName = i18n.t(`route.${key}`);
    return `${pageName} - ${defaultSettings.title}`;
  }
  return `${defaultSettings.title}`;
}
