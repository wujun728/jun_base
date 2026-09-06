import { defineConfig } from 'oxfmt'

export default defineConfig({
  printWidth: 80,
  singleQuote: true,
  semi: false,
  trailingComma: 'none',
  bracketSameLine: true,
  htmlWhitespaceSensitivity: 'ignore',
  sortTailwindcss: {
    stylesheet: './src/assets/styles/core/tailwind.css'
  },
  sortImports: {
    groups: [
      'type',
      'builtin',
      'external',
      'internal',
      ['parent', 'sibling', 'index'],
      'style',
      'unknown'
    ],
    newlinesBetween: true,
    order: 'asc'
  },
  ignorePatterns: [
    'node_modules',
    'dist',
    'public',
    '.**/**',
    'src/iconfont/**',
    'src/uni_modules/**',
    'unpackage/**'
  ]
})
