let frameFn = (rule, value, callback) => {
  if (value && value.length > 0 ) {
    callback();
  } else{
    callback(new Error('请选择部门'));
  }
};
let positionFn = (rule, value, callback) => {

  if (value) {
    callback();
  } else {
    callback(new Error('职位不能为空'));
  }
};
let formOptions = {

  data() {
    this.FORMOPTIONS = [
      // 基本信息数据集合
      {
        title: '基本信息',
        edit_type:'basic',
        data: [{
            type: 'input',
            label: '人员姓名：',
            value: 'name',
            placeholder: '请输入人员姓名',
          },
          {
            type: 'input',
            label: '手机号码：',
            value: 'phone',
            placeholder: '手机号为员工登录账号，默认密码：888888',
          },
          {
            slot: 'cascader',
            label: '职位：',
            value: 'position',
            placeholder: '请选择职位',
          },
          {
            slot: 'frame_id',
            label: '部门：',
            value: 'frame_id',
            placeholder: '请选择所属部门',
          },
          {
            type:'radio',
            label: '负责人：',
            value: 'is_admin',
            placeholder: '请选择负责人',
          },
          {
            label: '负责部门：',
            slot:'manage_frame',
          },
      
          {
            label: '直属上级：',
            slot:'superior_uid',

          },
        

        ],
      },
      // 职工信息数据集合
      {
        title: '职工信息',
        edit_type:'staff',
        data: [{
            type: 'select',

            label: '员工类型：',
            value: 'is_part',
            placeholder: '请选择员工类型',
            optionsList: [{
              label: '全职',
              value: 0
            },
            {
              label: '兼职',
              value: 1
            },
            {
              label: '实习 ',
              value: 2
            },
            {
              label: '劳务派遣',
              value: 3
            },
            {
              label: '退休返聘',
              value: 4
            },
            {
              label: '劳务外包',
              value: 5
            },
            {
              label: '其他',
              value: 6
            }
          ]
          },
          {
            type: 'select',
            label: '员工状态：',
            value: 'type',
            placeholder: '请选择员工状态',
            optionsList: [{
                label: '正式',
                value: 1
              },
              {
                label: '试用',
                value: 2
              },
              {
                label: '实习',
                value: 3
              },
              {
                label: '离职',
                value: 4
              },
            ]
          },
          {
            type: 'date',
            label: '入职时间：',
            value: 'work_time',
            placeholder: '请选择入职时间',
          },
          {
            type: 'date',
            label: '试用到期：',
            value: 'trial_time',
            placeholder: '请选择试用到期时间',
          },
          {
            type: 'date',
            label: '转正时间：',
            value: 'formal_time',
            placeholder: '请选择转正时间',
          },
          {
            type: 'date',
            label: '合同到期：',
            value: 'treaty_time',
            placeholder: '请选择合同到期时间',
          },
        ]
      },
      // 个人信息数据集合
      {
        title: '个人信息',
        edit_type:'user',
        data: [{
            type: 'input',
            label: '身份证：',
            value: 'card_id',
            placeholder: '请输入身份证号码',
          },
          {
            type: 'select',
            label: '性别：',
            value: 'sex',
            placeholder: '请选择性别',
            optionsList: [{
                label: '男',
                value: 1
              },
              {
                label: '女',
                value: 2
              },
              {
                label: '未知',
                value: 0
              },
            ]
          },
          {
            type: 'date',
            label: '出生日期：',
            value: 'birthday',
            placeholder: '请选择出生日期',
          },
          {
            type: 'input',
            label: '年龄：',
            value: 'age',
            placeholder: '请输入年龄',
          },
          {
            type: 'input',
            label: '民族：',
            value: 'nation',
            placeholder: '请输入民族',

          },
          {
            type: 'input',
            label: '政治面貌：',
            value: 'politic',
            placeholder: '请输入政治面貌',

          },
          {
            type: 'input',
            label: '工作年限：',
            value: 'work_years',
            placeholder: '请输入相关岗位工作年限',
          },
          {
            type: 'input',
            label: '籍贯：',
            value: 'native',
            placeholder: '请输入籍贯',
          },
          {
            type: 'input',
            label: '现居住地：',
            value: 'address',
            placeholder: '请输入现居住地,详细地址',
          },
          {
            type: 'select',
            label: '婚姻状况：',
            value: 'marriage',
            placeholder: '请选择婚姻状况',
            optionsList: [{
                label: '未婚',
                value: 0
              },
              {
              label:'已婚',
              value:1
              },
              {
              label:'已婚已育',
              value:2
              },
            ]
          },
          {
            type: 'input',
            label: '邮箱地址： ',
            value: 'email',
            placeholder: '请输入邮箱地址',
          },
        ],
      },
      // 学历信息数据集合
      {
        title: '学历信息',
        edit_type:'education',
        data: [{
            type: 'select',
            label: '最高学历：',
            value: 'education',
            placeholder: '请输入最高学历',
            optionsList: [

              {
                label: '研究生',
                value: 6
              },
              {
                label: '本科',
                value: 5
              },
              {
                label: '专科',
                value: 4
              },

              {
                label: '高中及以下',
                value: 2
              },

            ]
          },
          {
            type: 'input',
            label: '最高学位：',
            value: 'acad',
            placeholder: '请输入最高学位',
          },
          {
            type: 'date',
            label: '毕业时间：',
            value: 'graduate_date',
            placeholder: '请选择毕业时间',
          },
          {
            type: 'input',
            label: '毕业院校：',
            value: 'graduate_name',
            placeholder: '请输入毕业院校',
          },
        ],
      },
       // 个人材料
       {
        title: '个人材料',
        edit_type:'card',

        slot: 'personalMaterials',
        data: [{
            type: 'uploadImg',
            label: '身份证正面：',
            value: 'card_front',
          },
          {
            type: 'uploadImg',
            label: '身份证反面：',
            value: 'card_both',
          },
          {
            type: 'uploadImg',
            label: '学历证书：',
            value: 'education_image',
          },
          {
            type: 'uploadImg',
            label: '学位证书：',
            value: 'acad_image',
          },

        ],
      },
      // 银行卡信息
      {
        title: '银行卡信息',
        edit_type:'bank',
        data: [{
            type: 'input',
            label: '银行卡号：',
            value: 'bank_num',
            placeholder: '请输入银行卡',
          },

          {
            type: 'input',
            label: '开户行：',
            value: 'bank_name',
            placeholder: '请输入该银行卡开户行',
          },
        ],
      },
      // 社保信息
      {
        title: '社保信息',
        edit_type:'social',
        data: [{
            type: 'input',
            label: '社保账号：',
            value: 'social_num',
            placeholder: '请输入个人社保账号',
          },
          {
            type: 'input',
            label: '公积金号：',
            value: 'fund_num',
            placeholder: '请输入该个人公积金账号',
          },
        ],
      },
      // 紧急联系人
      {
        title: '紧急联系人',
        edit_type:'spare',
        data: [{
            type: 'input',
            label: '联系人姓名：',
            value: 'spare_name',
            placeholder: '请输入紧急联系人姓名',
          },
          {
            type: 'input',
            label: '联系人电话：',
            value: 'spare_tel',
            placeholder: '请输入紧急联系人电话',
          },
        ],
      },

      // 工作经历
      {
        title: '工作经历',
        type:1,
        slot: 'workExperience',
        data: []
      },
      // 教育经历
      {
        title: '教育经历',
        type:1,
        slot: 'educationalExperience',
        data: []
      },
      {
        title: '系统信息',
        type:1,
        edit_type:'sort',
        slot: 'systemInformation',
        data: [
       

          {
            type: 'num',
            label: '排序：',
            value: 'sort',
            placeholder: '请输入排序数值',
          }
        ]
      }
    ]
    // 个人简历
    this.userForm =[
      {
        title: '职工信息',
        data: [
         {
          type: 'input',
          label: '职位：',
          value: 'position',
          placeholder: '请输入职位',
        },
        {
          type: 'select',
          label: '所属类型：',
          value: 'is_part',
          placeholder: '请选择所属类型',
          optionsList: [{
              label: '全职',
              value: 0
            },
            {
              label: '兼职',
              value: 1
            }
          ]
        }

        ]
      },
      {
        title: '个人信息',
        data: [
          {
            type: 'input',
            label: '人员姓名：',
            value: 'name',
            placeholder: '请输入姓名',
          },
          {
            type: 'input',
            label: '联系电话：',
            value: 'phone',
            placeholder: '请输入手机号码',
          },


          {
            type: 'input',
            label: '身份证：',
            value: 'card_id',
            placeholder: '请输入身份证号码',
          },
          {
            type: 'select',
            label: '性别：',
            value: 'sex',
            placeholder: '请选择性别',
            optionsList: [{
                label: '男',
                value: 1
              },
              {
                label: '女',
                value: 2
              },
              {
                label: '未知',
                value: 0
              },
            ]
          },
          {
            type: 'date',
            label: '出生日期：',
            value: 'birthday',
            placeholder: '请选择出生日期',
          },
          {
            type: 'input',
            label: '年龄：',
            value: 'age',
            placeholder: '请输入年龄',
          },
          {
            type: 'input',
            label: '民族：',
            value: 'nation',
            placeholder: '请输入民族',
          },
          {
            type: 'input',
            label: '政治面貌：',
            value: 'politic',
            placeholder: '请输入政治面貌',
          },
          {
            type: 'input',
            label: '工作年限：',
            value: 'work_years',
            placeholder: '请输入相关岗位工作年限',
          },
          {
            type: 'input',
            label: '籍贯：',
            value: 'native',
            placeholder: '请输入籍贯',
          },
          {
            type: 'input',
            label: '现居住地：',
            value: 'address',
            placeholder: '请输入现居住地,详细地址',
          },
          {
            type: 'select',
            label: '婚姻状况：',
            value: 'marriage',
            placeholder: '请选择婚姻状况',
            optionsList: [{
                label: '未婚',
                value: 0
              },
              {
              label:'已婚',
              value:1
              },
              {
                label:'已婚已育',
                value:2
                },


            ]
          },
          {
            type: 'input',
            label: '邮箱地址： ',
            value: 'email',
            placeholder: '请输入邮箱地址',
          },
        ],
      },

      {
        title: '学历信息',
        data: [{
            type: 'select',
            label: '最高学历：',
            value: 'education',
            placeholder: '请输入最高学历',
            optionsList: [
            
              {
                label: '研究生',
                value: 6
              },
              {
                label: '本科',
                value: 5
              },
              {
                label: '专科',
                value: 4
              },
            
              {
                label: '高中及以下',
                value: 2
              },
            
            ]
          },
          {
            type: 'input',
            label: '最高学位：',
            value: 'acad',
            placeholder: '请输入最高学位',
          },
          {
            type: 'date',
            label: '毕业时间：',
            value: 'graduate_date',
            placeholder: '请选择毕业时间',
          },
          {
            type: 'input',
            label: '毕业院校：',
            value: 'graduate_name',
            placeholder: '请输入毕业院校',
          },
        ],
      },
      {
        title: '个人材料',

        slot: 'personalMaterials',
        data: [{
            type: 'uploadImg',
            label: '身份证正面：',
            value: 'card_front',
          },
          {
            type: 'uploadImg',
            label: '身份证反面：',
            value: 'card_both',
          },
          {
            type: 'uploadImg',
            label: '学历证书：',
            value: 'education_image',
          },
          {
            type: 'uploadImg',
            label: '学位证书：',
            value: 'acad_image',
          },

        ],
      },
      {
        title: '银行卡信息',
        data: [{
            type: 'input',
            label: '银行卡号：',
            value: 'bank_num',
            placeholder: '请输入银行卡',
          },

          {
            type: 'input',
            label: '开户行：',
            value: 'bank_name',
            placeholder: '请输入该银行卡开户行',
          },
        ],
      },
      {
        title: '社保信息',
        data: [{
            type: 'input',
            label: '社保账号：',
            value: 'social_num',
            placeholder: '请输入个人社保账号',
          },
          {
            type: 'input',
            label: '公积金号：',
            value: 'fund_num',
            placeholder: '请输入该个人公积金账号',
          },
        ],
      },
      {
        title: '紧急联系人',
        data: [{
            type: 'input',
            label: '联系人姓名：',
            value: 'spare_name',
            placeholder: '请输入紧急联系人姓名',
          },
          {
            type: 'input',
            label: '联系人电话：',
            value: 'spare_tel',
            placeholder: '请输入紧急联系人电话',
          },
        ],
      },

        // 工作经历
        {
          title: '工作经历',
          type:1,
          slot: 'workExperience',
          data: []
        },
        // 教育经历
        {
          title: '教育经历',
          type:1,
          slot: 'educationalExperience',
          data: []
        },
    ]

    // 未入职员工
    this.notEntry=[
      {
        title: '职工信息',
        edit_type:'staff',
        data: [
         {
          type: 'date',
          label: '面试时间：',
          value: 'interview_date',
          placeholder: '请选择面试时间',
        },
        {
          type: 'select',
          label: '员工类型：',
          value: 'is_part',
          placeholder: '请选择员工类型',
          optionsList: [{
              label: '全职',
              value: 0
            },
            {
              label: '兼职',
              value: 1
            },
            {
              label: '实习 ',
              value: 2
            },
            {
              label: '劳务派遣',
              value: 3
            },
            {
              label: '退休返聘',
              value: 4
            },
            {
              label: '劳务外包',
              value: 5
            },
            {
              label: '其他',
              value: 6
            }
          ]
        },
        {
          type: 'input',
          label: '面试职位：',
          value: 'interview_position',
          placeholder: '请输入面试职位',
        },
        {
          type: 'select',
          label: '员工状态：',
          value: 'type',
          placeholder: '请选择员工状态',
          optionsList: [{
              label: '正式',
              value: 1
            },
            {
              label: '试用',
              value: 2
            },
            {
              label: '未入职',
              value: 3
            },
            {
              label: '离职',
              value: 4
            },
          ]
        },
    ],
  },
  {
    title: '个人信息',
    edit_type:'user',
    data: [
      {
        type: 'input',
        label: '人员姓名：',
        value: 'name',
        placeholder: '请输入姓名',
      },
      {
        type: 'input',
        label: '手机号码：',
        value: 'phone',
        placeholder: '手机账号为员工登录账号，默认密码：888888',
      },


      {
        type: 'input',
        label: '身份证：',
        value: 'card_id',
        placeholder: '请输入身份证号码',
      },
      {
        type: 'select',
        label: '性别：',
        value: 'sex',
        placeholder: '请选择性别',
        optionsList: [{
            label: '男',
            value: 1
          },
          {
            label: '女',
            value: 2
          },
          {
            label: '未知',
            value: 0
          },
        ]
      },
      {
        type: 'date',
        label: '出生日期：',
        value: 'birthday',
        placeholder: '请选择出生日期',
      },
      {
        type: 'input',
        label: '年龄：',
        value: 'age',
        placeholder: '请输入年龄',
      },
      {
        type: 'input',
        label: '民族：',
        value: 'nation',
        placeholder: '请输入民族',
      },
      {
        type: 'input',
        label: '政治面貌：',
        value: 'politic',
        placeholder: '请输入政治面貌',
      },
      {
        type: 'input',
        label: '工作年限：',
        value: 'work_years',
        placeholder: '请输入相关岗位工作年限',
      },
      {
        type: 'input',
        label: '籍贯：',
        value: 'native',
        placeholder: '请输入籍贯',
      },
      {
        type: 'input',
        label: '现居住地：',
        value: 'address',
        placeholder: '请输入现居住地,详细地址',
      },
      {
        type: 'select',
        label: '婚姻状况：',
        value: 'marriage',
        placeholder: '请选择婚姻状况',
        optionsList: [{
            label: '未婚',
            value: 0
          },
          {
          label:'已婚',
          value:1
          },
          {
            label:'已婚已育',
            value:2
            },


        ]
      },
      {
        type: 'input',
        label: '邮箱地址： ',
        value: 'email',
        placeholder: '请输入邮箱地址',
      },

    ],
  },
  {
    title: '学历信息',
    edit_type:'education',
    data: [{
        type: 'select',
        label: '最高学历：',
        value: 'education',
        placeholder: '请输入最高学历',
        optionsList: [
          
              {
                label: '研究生',
                value: 6
              },
              {
                label: '本科',
                value: 5
              },
              {
                label: '专科',
                value: 4
              },
            
              {
                label: '高中及以下',
                value: 2
              },
            
        ]
      },
      {
        type: 'input',
        label: '最高学位：',
        value: 'acad',
        placeholder: '请输入最高学位',
      },
      {
        type: 'date',
        label: '毕业时间：',
        value: 'graduate_date',
        placeholder: '请选择毕业时间',
      },
      {
        type: 'input',
        label: '毕业院校：',
        value: 'graduate_name',
        placeholder: '请输入毕业院校',
      },
    ],
  },
  {
    title: '紧急联系人',
    edit_type:'spare',
    data: [{
        type: 'input',
        label: '联系人姓名：',
        value: 'spare_name',
        placeholder: '请输入紧急联系人姓名',
      },
      {
        type: 'input',
        label: '联系人电话：',
        value: 'spare_tel',
        placeholder: '请输入紧急联系人电话',
      },
    ],
  },
    // 工作经历
    {
      title: '工作经历',
      type:1,
      slot: 'workExperience',
      data: []
    },
    // 教育经历
    {
      title: '教育经历',
      type:1,
      slot: 'educationalExperience',
      data: []
    },

  ],


    // 表单校验
    this.fromRules = {
      name: [
        { required: true, message: '请输入人员姓名', trigger: 'blur' },
      ],
     
      position:[
        { required: true,validator: positionFn, trigger:  'blur' }
      ],

      is_admin:[
        { required: true, message: '负责人不能为空', trigger: 'blur' },
      ],
      frame_id:[
        { required: true,validator: frameFn, trigger: 'blur' }
      ],
      phone:[
        { required: true, message: '请输入手机号码', trigger: 'blur' },
        {
          pattern: /^[1][3,4,5,6,7,8,9][0-9]{9}$/,
           message: '请输入正确的手机号码',
         },

      ],
      is_part:[
        { required: true, message: '请选择员工类型', trigger: 'blur' },
      ],
      email:[
        { required: false, message: '请选择员工类型', trigger: 'blur' },
        {
          type: 'email',
          message: '请输入正确的邮箱地址',
          trigger: ['blur'],
        },
      ],
      type:[
        { required: true, message: '员工状态不能为空', trigger: 'blur' },
      ],
      card_id:[
        {
          pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
          message: "请输入正确的身份证号码",
          trigger: 'blur'
        },
      ],
      work_time:[
        { required: true, message: '请选择入职时间', trigger: 'blur' },
      ],
      interview_date:[
        { required: true, message: '请选择面试时间', trigger: 'blur' },
      ],
      quit_time:[
        { required: true, message: '请选择离职时间', trigger: 'blur' },
      ],
      interview_position:[
        { required: true, message: '请输入面试职位', trigger: 'blur' },
      ],

    }
    return {
      // 控制每个表单的是否修改状态
      FORMITEMISEDIT: {
        0: true,
        1: true,
        2: true,
        3: true,
        4: true,
        5: true,
        6: true,
        7: true,
        8: true,
        9: true,
        10: true
      }
    }
  }
}
export default formOptions;
