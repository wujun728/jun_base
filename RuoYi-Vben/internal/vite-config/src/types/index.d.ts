/**
 * 解决vite-plugin-purge-icons报错没有类型定义
 */
declare module 'vite-plugin-purge-icons' {
  const type = any;
  export default type;
}
