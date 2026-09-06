import { fileURLToPath } from 'node:url'
import path from 'path'

import { defineConfig } from 'oxlint'

// import apiDst from './src/types/import/auto-imports.d.ts'

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

export default defineConfig({
  plugins: ['vue', 'typescript'],
  env: {
    browser: true,
    node: true
  },
  ignorePatterns: [
    'node_modules',
    'dist',
    'public',
    '.vscode/**',
    'src/iconfont/**',
    'src/uni_modules/**'
  ]
})
