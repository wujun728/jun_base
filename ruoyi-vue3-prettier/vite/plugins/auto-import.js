import AutoImport from 'unplugin-auto-import/vite'
// import Components from 'unplugin-vue-components/vite'
// import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
export default function createAutoImport() {
  return AutoImport({
    // resolvers: [ElementPlusResolver()],
    imports: ['vue', 'vue-router', 'pinia'],
    dts: false,
  })
}
// export function createComponents() {
//   return Components({
//     resolvers: [ElementPlusResolver()],
//   })
// }
