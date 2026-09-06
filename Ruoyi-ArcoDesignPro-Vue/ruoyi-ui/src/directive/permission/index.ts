import {DirectiveBinding} from 'vue';
import {useUserStore} from '@/store';

function checkPermission(el: HTMLElement, binding: DirectiveBinding) {
  const {value} = binding;
  const permissionAll = "*:*:*";
  const userStore = useUserStore();
  const {permissions} = userStore;

  if (Array.isArray(value)) {
    if (value.length > 0) {
      const permissionValues = value;

      const hasPermissions = permissions.some(permission => {
        return permissionAll === permission || permissionValues.includes(permission)
      })

      if (!hasPermissions && el.parentNode) {
        el.parentNode.removeChild(el);
      }
    }
  } else {
    throw new Error(`need roles! Like v-permission="['*:*:*','*:*:*']"`);
  }
}

export default {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    checkPermission(el, binding);
  },
  updated(el: HTMLElement, binding: DirectiveBinding) {
    checkPermission(el, binding);
  },
};
