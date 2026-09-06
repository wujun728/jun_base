module.exports = {
  // 可选类型
  types: [
    { value: 'feat', name: 'feat:        新功能' },
    { value: 'fix', name: 'fix:         修复' },
    { value: 'style', name: 'style:       样式' },
    { value: 'refactor', name: 'refactor:    重构' },
    { value: 'pref', name: 'pref:        性能优化' },
    { value: 'docs', name: 'docs:        文档变更' },
    { value: 'test', name: 'test:        增加测试' },
    { value: 'chore', name: 'chore:       构建过程或辅助工具变更' },
    { value: 'revert', name: 'revert:      回退' },
    { value: 'dependence', name: 'dependence:  依赖更新或迁移' },
    { value: 'WIP', name: 'WIP:         工作进行中' }
  ],
  scopes: [{ name: '组件' }, { name: '依赖' }],
  allowCustomScopes: true,
  allowBreakingChanges: ['feat', 'fix'],

  // 步骤
  messages: {
    type: '请选出提交类型',
    scope: '请输入修改的范围scope',
    customScope: '请输入修改的范围customScope',
    subject: '简要描述',
    body: '详细描述',
    breaking: '破坏性更改',
    footer: '关联issue',
    confirmCommit: '请确认要使用以上信息提交? (y/n)'
  },

  // 跳过步骤
  // skipQuestions: ['body', 'footer'],

  // 默认长度
  subjectLimit: 72
};
