/**
 * v-hasPermi 操作权限处理
 * Copyright (c) 2019 ruoyi
 */

import { useUserStore } from '@/stores/user';

export default {
  mounted(el, binding) {
    const { value } = binding;
    const all_permission = '*:*:*';
    const permissions = useUserStore().permissions;

    // 如果没有传入权限值或为 null/undefined，则不做权限控制，直接显示
    if (!value || value === null || value === undefined) {
      return;
    }

    // 如果传入的不是数组，抛出错误
    if (!(value instanceof Array)) {
      throw new Error('请设置操作权限标签值为数组格式');
    }

    // 如果传入空数组，则不做权限控制，直接显示
    if (value.length === 0) {
      return;
    }

    const permissionFlag = value;

    const hasPermissions = permissions.some((permission) => {
      return all_permission === permission || permissionFlag.includes(permission);
    });

    if (!hasPermissions) {
      el.parentNode && el.parentNode.removeChild(el);
    }
  },
};
