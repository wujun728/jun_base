import AutoImport from 'unplugin-auto-import/vite'

export default function createAutoImport() {
  return AutoImport({
    // 存放类型的文件地址
    dts: 'src/auto-imports.d.ts', // 这里是生成的global函数文件
    imports: ['vue', 'vue-router', 'pinia'], // 需要自动导入的插件
    include: [
      /\.[tj]sx?$/, // .ts, .tsx, .js, .jsx
      /\.vue$/,
      /\.vue\?vue/, // .vue
      /\.md$/, // .md
    ],
  })
}
