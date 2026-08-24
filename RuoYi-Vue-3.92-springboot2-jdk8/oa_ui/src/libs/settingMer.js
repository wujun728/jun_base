import Cookies from 'js-cookie';
// 请求接口地址 如果没有配置自动获取当前网址路径
const VUE_APP_API_URL = process.env.VUE_APP_BASE_API || `${location.origin}`;
const VUE_APP_WS_URL =
  process.env.VUE_APP_WS_URL || (location.protocol === 'https:' ? 'wss' : 'ws') + '://' + location.host;
const login_title = Cookies.get('MerInfo') ? JSON.parse(Cookies.get('MerInfo')).login_title : '';
const SettingMer = {
  // 服务器地址
  httpUrl: VUE_APP_API_URL,
  // 接口请求地址（若依后端，去掉 /api/ent）
  https: VUE_APP_API_URL,
  // https: 'http://123.56.28.2:20200/api/ent',
  // socket连接
  wsSocketUrl: VUE_APP_WS_URL,
  // 路由标题
  title: login_title || 'OA系统-总后台',
};

export default SettingMer;
