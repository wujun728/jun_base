export default (listeners = {}) => {
  return {
    rules: {
      jobName: [
        { required: true, message: '任务名称不能为空', trigger: 'blur' },
      ],
      invokeTarget: [
        { required: true, message: '调用目标字符串不能为空', trigger: 'blur' },
      ],
      cronExpression: [
        {
          required: true,
          message: 'cron执行表达式不能为空',
          trigger: 'change',
        },
      ],
    },
    formItems: [
      {
        field: 'jobName',
        type: 'input',
        label: '任务名称',
        config: {
          maxlength: 30,
        },
      },
      {
        field: 'jobGroup',
        type: 'select',
        label: '任务分组',
        options: [],
        config: {
          clearable: false,
        },
      },
      {
        field: 'invokeTarget',
        type: 'input',
        label: '调用目标字符串',
        config: {
          maxlength: 30,
        },
      },
      {
        field: 'cronExpression',
        type: 'input',
        label: 'cron表达式',
        config: {
          maxlength: 200,
        },
        slotNames: ['append'],
      },
      {
        field: 'status',
        type: 'radio',
        label: '状态',
        isGroup: true,
        options: [],
        optionConfig: {
          border: true,
        },
      },
      {
        field: 'misfirePolicy',
        type: 'radio',
        label: '执行策略',
        isGroup: true,
        options: [
          {
            label: '立即执行',
            value: '1',
          },
          {
            label: '执行一次',
            value: '2',
          },
          {
            label: '放弃执行',
            value: '3',
          },
        ],
        optionConfig: {
          border: true,
        },
      },
      {
        field: 'concurrent',
        type: 'radio',
        label: '是否并发',
        isGroup: true,
        options: [
          {
            label: '允许',
            value: '0',
          },
          {
            label: '禁止',
            value: '1',
          },
        ],
        optionConfig: {
          border: true,
        },
      },
    ],
    colLayout: {
      xl: 24,
      lg: 24,
      md: 24,
      sm: 24,
      xs: 24,
    },
    itemStyle: {
      padding: '0px 8px 0px 8px',
    },
    elFormConfig: {
      labelWidth: '130px',
    },
    hideItems: ref([]),
  }
}
