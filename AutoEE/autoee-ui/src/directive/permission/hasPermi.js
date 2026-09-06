/**
 * v-hasPermi 操作权限处理
 * Copyright (c) 2019 ruoyi
 */

import store from '@/store';

export default {
  mounted(el, binding, vnode) {
    const { value } = binding;
    const all_permission = '*:*:*';
    const permissions = store.getters && store.getters.permissions;
    // console.log('permissions=' + JSON.stringify(permissions));
    // console.log('binding=' + JSON.stringify(binding));

    if (value && value instanceof Array && value.length > 0) {
      const permissionFlag = value;

      const hasPermissions = permissions.some((permission) => {
        return all_permission === permission || permissionFlag.includes(permission);
      });
      // console.log('hasPermissions=' + JSON.stringify(hasPermissions));

      if (!hasPermissions) {
        el.parentNode && el.parentNode.removeChild(el);
      }
    } else {
      throw new Error(`请设置操作权限标签值`);
    }
  },
};
