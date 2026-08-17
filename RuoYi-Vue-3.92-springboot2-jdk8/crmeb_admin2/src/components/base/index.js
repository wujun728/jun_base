
const components = require.context('./', false, /\.vue$/u);
export default (Vue) => {
  components.keys().map((item) => {
    Vue.component(components(item).default.name, components(item).default);
  });
};
