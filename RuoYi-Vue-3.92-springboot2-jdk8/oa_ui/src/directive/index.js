import permission from './permission/permission';
import hasPermi from './permission/hasPermi';
import hasRole from './permission/hasRole';

const install = function (Vue) {
  Vue.directive('permission', permission);
  Vue.directive('hasPermi', hasPermi);
  Vue.directive('hasRole', hasRole);
};

if (window.Vue) {
  window['permission'] = permission;
  window['hasPermi'] = hasPermi;
  window['hasRole'] = hasRole;
  Vue.use(install); // eslint-disable-line
}

// permission.install = install;
// export default permission;
export default install;
