import { compression } from 'vite-plugin-compression2'

export default function createCompression(env) {
  const { VITE_BUILD_COMPRESS } = env
  const plugin = []
  if (VITE_BUILD_COMPRESS) {
    const compressList = VITE_BUILD_COMPRESS.split(',')
    if (compressList.includes('gzip')) {
      plugin.push(
        compression({
          algorithm: 'gzip',
          // 体积大于threshold则进行压缩，单位为bytes
          threshold: 1024 * 50,
          // 压缩后是否删除源文件
          deleteOriginalAssets: false,
        })
      )
    }
    if (compressList.includes('brotli')) {
      plugin.push(
        compression({
          // 压缩算法
          algorithm: 'brotliCompress',
          // 体积大于threshold则进行压缩，单位为bytes
          threshold: 1024 * 50,
          // 压缩后是否删除源文件
          deleteOriginalAssets: false,
        })
      )
    }
  }
  return plugin
}
