module.exports = {
  root: true,
  env: {
    browser: true,
    es2021: true,
    node: true
  },
  extends: [
    'eslint:recommended',
    'plugin:vue/vue3-recommended',
    'plugin:prettier/recommended',
    './config/eslint/.eslintrc-auto-import.json'
  ],
  parserOptions: {
    ecmaVersion: 2021,
    sourceType: 'module',
    ecmaFeatures: {
      jsx: true
    }
  },
  plugins: ['vue', 'prettier'],
  rules: {
    // Vue规则
    'vue/multi-word-component-names': 'off',
    'vue/no-v-html': 'off',
    'vue/require-default-prop': 'off',
    'vue/require-explicit-emits': 'off',
    'vue/no-setup-props-destructure': 'off',
    
    // 基础规则
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-unused-vars': ['error', { 
      argsIgnorePattern: '^_',
      varsIgnorePattern: '^_'
    }],
    'no-empty': ['error', { allowEmptyCatch: true }],
    
    // 代码风格
    'indent': 'off',
    'quotes': ['error', 'single', { avoidEscape: true }],
    'semi': ['error', 'always'],
    'comma-dangle': ['error', 'only-multiline'],
    'object-curly-spacing': ['error', 'always'],
    'array-bracket-spacing': ['error', 'never'],
    'arrow-parens': ['error', 'always'],
    'arrow-spacing': ['error', { before: true, after: true }],
    'space-before-blocks': ['error', 'always'],
    'keyword-spacing': ['error', { before: true, after: true }],
    
    // Prettier集成
    'prettier/prettier': ['error', {
      singleQuote: true,
      semi: true,
      useTabs: false,
      tabWidth: 2,
      trailingComma: 'es5',
      printWidth: 100,
      bracketSpacing: true,
      arrowParens: 'always',
      endOfLine: 'lf'
    }]
  },
  globals: {
    defineProps: 'readonly',
    defineEmits: 'readonly',
    defineExpose: 'readonly',
    withDefaults: 'readonly'
  },
  // 忽略的文件和目录（从 .eslintignore 合并而来）
  ignorePatterns: [
    // 依赖和构建输出
    'node_modules/',
    'dist/',
    'dist-*',
    'build/',
    'coverage/',
    
    // 编辑器相关
    '.idea/',
    '.vscode/',
    '*.suo',
    '*.ntvs*',
    '*.njsproj',
    '*.sln',
    '*.sw?',
    
    // 日志文件
    '*-debug.log*',
    '*-error.log*',
    
    // 环境配置
    '.env.local',
    '.env.*.local',
    
    // 编译输出
    '*.min.js',
    '*.min.css',
    
    // 其他
    '.DS_Store',
    'public/',
    '*.md',
    '*.json',
    '*.yaml',
    '*.yml',
    '*.lock',
    
    // 静态资源
    'src/assets/',
    '*.svg',
    '*.png',
    '*.jpg',
    '*.jpeg',
    '*.gif',
    '*.ico',
    
    // 打包文件
    '*.zip',
    '*.tar.gz'
  ]
};
