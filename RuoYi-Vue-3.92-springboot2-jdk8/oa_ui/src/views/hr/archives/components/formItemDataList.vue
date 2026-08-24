<template>
  <div class="app-body">
    <!------------- 编辑: 个人经历页面 ------------->
    <template v-if="componentStatus && componentStatus.personalExperience">
      <!-- 工作经历table -->
      <div class="from-item-title mb15">
        <span class="title">工作经历</span>
        <el-link :underline="false" icon="el-icon-plus" @click="editWorks('work')">添加经历</el-link>
      </div>
      <div>
        <tableBox
          :id="id"
          :table-data="workData"
          :table-label="workLabel"
          :type="edit"
          style="margin-bottom: 20px"
          table-name="works"
        ></tableBox>
      </div>

      <!-- 教育经历table -->
      <div class="from-item-title mb15">
        <span class="title">教育经历</span>
        <el-link :underline="false" icon="el-icon-plus" @click="editEducation('education')">添加经历</el-link>
      </div>
      <div>
        <tableBox
          :id="id"
          :table-data="educationsData"
          :table-label="educationLabel"
          :type="edit"
          style="margin-bottom: 20px"
          table-name="education"
        ></tableBox>
      </div>
    </template>

    <!-------------- 新增:新增页面 ------------>
    <el-form v-else ref="form" :model="ruleForm" :rules="fromRules" label-width="90px">
      <div v-for="(formItem, itemIndex) in newFrom" :key="itemIndex" class="p20">
        <div
          v-if="componentStatus.type === 'add' || !['工作经历', '教育经历'].includes(formItem.title)"
          :id="itemIndex"
          :class="FORMITEMISEDIT[itemIndex] && componentStatus.type === 'edit' ? 'm10' : 'mb15'"
          class="from-item-title"
        >
          <!-- 表单头标题 -->
          <span class="title">
            {{ formItem.title }}
            <!-- 个人简历需要展示的文字 -->
            <span v-if="formItem.title === '银行卡信息' && user === '个人简历'" class="text-title">
              <i class="el-icon-warning"></i>
              银行卡信息为敏感信息，右侧简历不会发送给其他企业</span
            >
            <span v-if="formItem.title === '社保信息' && user === '个人简历'" class="text-title">
              <i class="el-icon-warning"></i>
              社保信息为敏感信息，右侧简历不会发送给其他企业</span
            >
            <span v-if="formItem.title === '个人材料' && user === '个人简历'" class="text-title">
              <i class="el-icon-warning"></i>
              个人材料为敏感信息，右侧简历不会发送给其他企业
            </span>
          </span>

          <!-- 修改按钮 -->
          <span v-if="FORMITEMISEDIT[itemIndex] && componentStatus.type === 'edit'" class="editBtn">
            <el-link
              :underline="false"
              icon="el-icon-edit-outline"
              @click="FORMITEMISEDIT[itemIndex] = !FORMITEMISEDIT[itemIndex]"
              >编辑</el-link
            >
          </span>
          <div v-if="!FORMITEMISEDIT[itemIndex]">
            <el-button size="small" @click="restFn(itemIndex)">取消</el-button>
            <el-button size="small" type="primary" @click="formItemEdit(formItem.edit_type, itemIndex)">确定</el-button>
          </div>

          <!-- 新增:添加经历 -->
          <span v-if="['工作经历', '教育经历'].includes(formItem.title)">
            <div style="padding: 10px">
              <el-link :underline="false" icon="el-icon-plus" @click="addWorks(formItem.title)">添加经历</el-link>
            </div>
          </span>
        </div>

        <!-- 工作经历table -->
        <tableBox
          v-if="formItem.slot === 'workExperience' && componentStatus.type === 'add'"
          :person-id="personId"
          :table-data="workData"
          :table-label="workLabel"
          :type="(edit = 'add')"
          :user="user"
          style="margin-bottom: 20px"
          table-name="works"
          @deleteWork="deleteWork"
          @putWork="putWork"
        ></tableBox>

        <!-- 教育经历table -->
        <tableBox
          v-if="formItem.slot === 'educationalExperience' && componentStatus.type === 'add'"
          :person-id="personId"
          :table-data="educationsData"
          :table-label="educationLabel"
          :type="(edit = 'add')"
          :user="user"
          style="margin-bottom: 20px"
          table-name="education"
          @deleteWork="deleteWork"
          @putWork="putWork"
        ></tableBox>

        <!-- 表单数据内容 -->
        <div class="form-box">
          <div v-for="(item, index) in formItem.data" :key="index" class="form-item">
            <el-form-item
              :class="
                FORMITEMISEDIT[itemIndex] && componentStatus.type === 'edit' && item.type !== 'uploadImg'
                  ? 'activeItem'
                  : ''
              "
              :prop="item.value"
            >
              <!-- 表单label -->

              <span
                slot="label"
                :class="FORMITEMISEDIT[itemIndex] && componentStatus.type === 'edit' ? 'label' : 'labelfont'"
                >{{ item.label }}</span
              >

              <!-- 编辑状态用span来替换input来显示-->
              <div
                v-if="FORMITEMISEDIT[itemIndex] && componentStatus.type === 'edit' && item.type !== 'uploadImg'"
                class="label-item"
              >
                <span
                  v-if="
                    !['cascader', 'frame_id', 'manage_frame'].includes(item.slot) &&
                    !['radio', 'select'].includes(item.type)
                  "
                  >{{ ruleForm[item.value] }}</span
                >
                <div v-if="item.slot === 'frames'">
                  <span v-for="(item, index) in rangeUserText" :key="index">{{ item }} </span>
                </div>

                <span v-if="item.slot === 'cascader'">{{ positionText }}</span>
                <span v-if="item.slot === 'manage_frame'">{{ manage_frameText }}</span>
                <span v-if="item.value === 'is_part'">{{ is_partText }}</span>
                <span v-if="item.value === 'type'">{{ typeText }}</span>
                <span v-if="item.value === 'sex'">{{ sexText }}</span>
                <span v-if="item.value === 'marriage'">{{ marriageText }}</span>
                <span v-if="item.value === 'education'">{{ educationText }}</span>
                <div v-if="item.type === 'radio'">
                  {{ is_adminText }}
                </div>
                <template v-if="item.slot === 'superior_uid' && is_adminText === '是'">
                  <span>{{ superiorText }}</span>
                </template>

                <div v-if="item.slot === 'frame_id'">
                  <span v-for="(item, index) in frameText" :key="index" class="mr5"
                    >{{ item.name }} <span v-if="item.is_mastart" style="color: #1890ff; font-size: 13px"> (主) </span>
                  </span>
                </div>
              </div>

              <!-- 新增状态 -->
              <div v-else>
                <el-input
                  v-if="item.type === 'input'"
                  v-model="ruleForm[item.value]"
                  :placeholder="item.placeholder"
                  size="small"
                  clearable
                  @blur="getBirthday(item.value)"
                />

                <el-input-number
                  v-if="item.type == 'num'"
                  v-model="ruleForm[item.value]"
                  :max="999999"
                  :min="1"
                  size="small"
                  controls-position="right"
                ></el-input-number>

                <!-- 时间选择 -->
                <el-date-picker
                  v-if="item.type === 'date'"
                  v-model="ruleForm[item.value]"
                  :placeholder="item.placeholder"
                  type="date"
                  size="small"
                  value-format="yyyy-MM-dd"
                >
                </el-date-picker>

                <!-- 职位等级 -->
                <template v-if="item.slot === 'cascader'">
                  <div class="flx">
                    <el-cascader
                      v-model="ruleForm.position"
                      :options="treeData"
                      :placeholder="item.placeholder"
                      :props="propsPos"
                      filterable
                      size="small"
                    ></el-cascader>
                    <i class="el-icon-circle-plus" @click="addQuick"></i>
                  </div>
                </template>

                <!-- 部门 -->

                <select-department
                  v-if="item.slot === 'frame_id'"
                  :value="frames || []"
                  :is-site="true"
                  :placeholder="`选择所属部门`"
                  @changeMastart="changeMastart"
                  style="width: 100%"
                ></select-department>

                <!-- 负责人 -->
                <template v-if="item.type === 'radio'">
                  <el-radio v-model="ruleForm[item.value]" label="1">是</el-radio>
                  <el-radio v-model="ruleForm[item.value]" label="0">否</el-radio>
                </template>

                <!-- 直属上级 -->
                <div v-if="item.slot == 'superior_uid'">
                  <select-member
                    :only-one="true"
                    ref="selectMember"
                    :value="superiorUser || []"
                    @getSelectList="getSelectList"
                  >
                    <template v-slot:custom>
                      <span
                        class="pointer"
                        v-if="superiorUser.length == 0"
                        style="color: #1890ff"
                        @click="handlesuperiorOpen"
                        >添加</span
                      >
                      <span v-else @click="handlesuperiorOpen">
                        {{ superiorUser[0].name }}
                      </span>
                      <span
                        class="pointer ml10"
                        v-if="superiorUser.length > 0"
                        style="color: #1890ff"
                        @click="handlesuperiorOpen"
                        >修改</span
                      >
                    </template>
                  </select-member>
                </div>

                <div v-if="item.slot == 'manage_frame'">
                  <el-select
                    v-model="ruleForm.manage_frame"
                    placeholder="请选择"
                    size="small"
                    :disabled="ruleForm.is_admin != 1"
                    multiple
                    style="width: 100%"
                  >
                    <el-option v-for="item in frames" :key="item.id" :label="item.name" :value="item.id"> </el-option>
                  </el-select>
                </div>

                <!-- select 下拉框 -->
                <el-select
                  v-if="item.type === 'select'"
                  v-model="ruleForm[item.value]"
                  :disabled="tabtypes !== 1 && item.value == 'type'"
                  :placeholder="item.placeholder"
                  size="small"
                  clearable
                >
                  <el-option
                    v-for="(item, index) in item.optionsList"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>

                <!-- 上传图片 -->
                <upload-image
                  v-if="item.type === 'uploadImg'"
                  :append-to-body="true"
                  :imageUrl.sync="ruleForm[item.value]"
                  :isUploadStatus="componentStatus.type === 'edit' && FORMITEMISEDIT[itemIndex]"
                  :user="user"
                ></upload-image>

                <!-- 系统设置 radio -->
                <template v-if="item.slot === 'radio'">
                  <el-radio
                    v-model="ruleForm.radio"
                    :disabled="componentStatus.type === 'edit' && FORMITEMISEDIT[itemIndex]"
                    label="1"
                    >直接加入</el-radio
                  >
                  <el-radio
                    v-model="ruleForm.radio"
                    :disabled="componentStatus.type === 'edit' && FORMITEMISEDIT[itemIndex]"
                    label="2"
                    >后续邀请</el-radio
                  >
                </template>
              </div>
            </el-form-item>
          </div>
          <!-- <div v-if="formItem.type !== 1" class="line"></div> -->
        </div>
      </div>
    </el-form>

    <!-- 新增时的工作经历 -->
    <Addwork ref="refwork" :add="add" :append-to-body="true" :edit="editTable" @addWorkFn="addWorkFn"></Addwork>
    <!-- 新增职位等级 -->
    <position-dialog ref="positionDialog" :config="positionConfig" @handleConfig="handleConfig"></position-dialog>
  </div>
</template>

<script>
import formOptions from '../mixins/index.js'
import myMixins from '../mixins/method.js'
import {
  saveCard,
  jobsCreate,
  enterpriseCardIdApi,
  putCardApi,
  enterpriseWorkCreateApi,
  enterpriseEducationCreateApi,
  getWorkList,
  getEducationList
} from '@/api/enterprise'
import {
  getResume,
  saveResume,
  userCreateWork,
  userCreateEducation,
  getUserWorkList,
  getUserEducationList
} from '@/api/user'

export default {
  name: 'formOptions',
  mixins: [formOptions, myMixins],
  props: {
    componentStatus: {
      type: Object,
      default: () => {}
    },
    user: {
      type: String,
      default: () => ''
    },
    id: {
      type: [String, Number, Boolean],
      default: () => false
    },
    tabtypes: {
      type: [String, Number, Boolean],
      default: 1
    },
    photo: {
      type: String,
      default: () => ''
    }
  },

  data() {
    return {
      departmentShow: false,
      memberShow: false,
      userWorkId: '',
      add: 'work',
      choiceEdit: 0,
      isEdit: 0,
      edit: 'edit',
      editTable: '新增',
      superiorUser: [],
      frames: [],
      treeData: [],
      setIndex: 0,
      superiorFrame: [], // 上级部门
      propsPos: { value: 'id', label: 'name', multiple: false, checkStrictly: true },
      mastart_id: '', // 主部门,
      ruleForm: {
        name: '', // 员工姓名
        phone: '', // 手机号码
        photo: '',
        position: '', // 职位
        card_id: '', // 身份证号
        birthday: '', // 生日,
        age: '',
        sex: 0, // 性别: 0、未知；1、男；2、女；3、其他
        nation: '', // 民族
        education: '', // 学历
        acad: '', // 学位
        politic: '', // 政治面貌
        address: '', // 居住地
        native: '', // 籍贯
        marriage: '', // 婚姻状况:0、未婚；1、已婚；
        work_start: '', // 开始工作时间
        spare_name: '', // 紧急联系人
        spare_tel: '', // 紧急联系电话
        email: '', // 邮箱
        sort: 0, // 排序
        invite: '',
        quit_time: '', // 离职时间
        status: '', // 状态：1、正常；0、锁定；
        is_admin: '0', // 负责人1=主管,0=普通成员
        work_years: '', // 工作年限
        work_time: '', // 入职时间
        trial_time: '', // 试用时间
        formal_time: '', // 转正时间
        treaty_time: '', // 合同到期时间
        frame_id: [], // 部门
        frames: [],
        manage_frame: '',
        graduate_name: '', // 毕业院校
        graduate_date: '', // 毕业时间
        card_front: '', // 身份证正面
        card_both: '', // 身份证反面
        education_image: '', // 学历证书
        acad_image: '', // 学位证书
        social_num: '', // 社保账号
        fund_num: '', // 公积金账号
        bank_num: '', // 银行卡
        bank_name: '', // 开户行
        main_id: '', // 组织架构主部门ID
        edit_type: '',
        type: 1, // 员工状态
        interview_date: '', // 面试时间
        interview_position: '', // 面试职位
        superior_uid: '', // 直属上级
        superior: {},
        // 身份证照片测试
        radio: '1'
        // 测试字段
      },
      rangeUser: [],
      tabsIndex: 'personalFile',
      direction: 'rtl',
      activeName: 'first',
      positionData: [],
      positionLabel: [],
      workData: [],
      rangeUserText: [],
      positionText: '',
      manage_frameText: '',
      is_adminText: '',
      superiorText: '',
      is_partText: '',
      typeText: '',
      marriageText: '',
      sexText: '',
      educationText: '',
      frameText: [],
      personId: 1,
      positionConfig: {},

      // 工作经历表格头部
      workLabel: [
        {
          name: this.$t('toptable.startdate'),
          prop: 'start_time',
          width: '100'
        },
        {
          name: this.$t('toptable.endingdate'),
          prop: 'end_time',
          width: '100'
        },
        {
          name: this.$t('hr.company'),
          prop: 'company',
          width: '110'
        },
        {
          name: this.$t('hr.jobdescription'),
          prop: 'describe',
          width: '120'
        },
        {
          name: this.$t('toptable.post'),
          prop: 'position',
          width: '100'
        },
        {
          name: this.$t('hr.reasonsleaving'),
          prop: 'quit_reason',
          width: '120'
        }
      ],
      // 教育经历
      educationsData: [],
      loading: false,
      // 教育经历表格头部
      educationLabel: [
        {
          name: this.$t('hr.admissiontime'),
          prop: 'start_time',
          width: '100'
        },
        {
          name: this.$t('hr.graduationtime'),
          prop: 'end_time',
          width: '100'
        },
        {
          name: this.$t('hr.schoolname'),
          prop: 'school_name',
          width: '90'
        },
        {
          name: this.$t('hr.major'),
          prop: 'major',
          width: '90'
        },
        {
          name: this.$t('hr.education'),
          prop: 'education',
          width: '80'
        },
        {
          name: this.$t('hr.degree'),
          prop: 'academic',
          width: '80'
        },
        {
          name: this.$t('public.remarks'),
          prop: 'remark',
          width: '100'
        }
      ]
    }
  },

  mounted() {
    this.getTreeData()
  },
  computed: {
    newFrom() {
      // 数据在minxins
      if (this.user == '个人简历') {
        return this.userForm
      } else if (this.tabtypes === 0) {
        return this.notEntry
      } else if (this.tabtypes === 2) {
        this.FORMOPTIONS.forEach((item) => {
          if (item.title == '职工信息') {
            let obj = {
              type: 'date',
              label: '离职时间：',
              value: 'quit_time',
              placeholder: '请选择离职时间'
            }

            item.data.push(obj)
          }
        })
        return this.FORMOPTIONS
      } else {
        this.FORMOPTIONS.forEach((item) => {
          if (item.title == '职工信息') {
            if (item.data.length == 7) {
              item.data.splice(item.data.length - 1, 1)
            }

            item.data[1].optionsList.splice(2, 2)
          }
        })
        return this.FORMOPTIONS
      }
    }
  },

  methods: {
    // 保存个人简历信息
    saveResume(val) {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.ruleForm.works = this.workData
          this.ruleForm.photo = val
          this.ruleForm.frames = this.rangeUser
          this.ruleForm.frame_id = this.frames
          saveResume(this.ruleForm)
            .then((res) => {
              this.$emit('hiddenSave')
            })
            .catch((err) => {
              this.$emit('hiddenSave')
            })
        }
      })
    },

    // 身份证获取出生日期
    getBirthday(val) {
      // 若当前字段不是身份证号，直接返回
      if (val !== 'card_id') return;

      const cardId = this.ruleForm.card_id;
      // 验证身份证号是否有效
      if (!this.isValidCardId(cardId)) {
        this.resetBirthInfo();
        return;
      }

      const { birthday, sex } = this.extractInfoFromCardId(cardId);
      this.ruleForm.birthday = this.formatBirthday(birthday);
      this.ruleForm.sex = this.calculateSex(sex);
      this.ruleForm.age = this.calculateAge(this.ruleForm.birthday);
    },

    // 验证身份证号是否有效
    isValidCardId(cardId) {
      return cardId && (cardId.length === 15 || cardId.length === 18);
    },

    // 从身份证号中提取出生日期和性别信息
    extractInfoFromCardId(cardId) {
      let birthday = '';
      let sex = '';

      if (cardId.length === 15) {
        birthday = '19' + cardId.substr(6, 6);
        sex = cardId.substring(14, 15);
      } else if (cardId.length === 18) {
        birthday = cardId.substr(6, 8);
        sex = cardId.substring(16, 17);
      }

      return { birthday, sex };
    },

    // 格式化出生日期
    formatBirthday(birthday) {
      return birthday.replace(/(.{4})(.{2})/, '$1-$2-');
    },

    // 计算性别
    calculateSex(sexNumber) {
      return sexNumber % 2 === 0 ? 2 : 1;
    },

    // 计算年龄
    calculateAge(birthday) {
      const birthDate = new Date(birthday);
      const currentDate = new Date();
      const age =
        currentDate.getFullYear() -
        birthDate.getFullYear() -
        (currentDate.getMonth() < birthDate.getMonth() ||
        (currentDate.getMonth() === birthDate.getMonth() && currentDate.getDate() < birthDate.getDate())
          ? 1
          : 0);

      return isNaN(age) ? '' : age;
    },

    // 重置出生日期、性别和年龄信息
    resetBirthInfo() {
      this.ruleForm.birthday = '';
      this.ruleForm.sex = 0;
      this.ruleForm.age = '';
    },

    restFn(data) {
      this.$refs.form.resetFields()
      this.FORMITEMISEDIT[data] = !this.FORMITEMISEDIT[data]
      this.enterpriseCardId()
    },
   
    // defaultFn() {
    //   this.getDate()
    //   if (this.tabtypes == 0) {
    //     this.$set(this.ruleForm, 'type', '未入职')
    //   } else if (this.tabtypes == 1) {
    //     this.$set(this.ruleForm, 'type', 2)
    //   } else if (this.tabtypes == 2) {
    //     this.$set(this.ruleForm, 'type', 4)
    //     this.typeText = '离职'
    //   }
    // },

    // 获取个人简历信息
    getResume() {
      getResume().then((res) => {
        this.workData = res.data.works
        this.educationsData = res.data.educations
        res.data.education = Number(res.data.education)
        this.ruleForm = res.data
        if (this.ruleForm.education == 0) {
          this.ruleForm.education = ''
        }
        this.userWorkId = res.data.id
        if (this.ruleForm.card_id) {
          this.getBirthday()
        }
        this.$emit('getPhoto', res.data.photo)
      })
    },

    // 新增:保存个人档案信息
    saveCard() {
      this.ruleForm.works = this.workData
      this.ruleForm.educations = this.educationsData
      this.ruleForm.photo = this.photo
      let position = ''
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.ruleForm.position.length > 0) {
            position = this.ruleForm.position[0]
          }
          saveCard(this.tabtypes, {
            ...this.ruleForm,
            position
          }).then((res) => {
            if (res.status == 200) {
              this.$emit('submitOk')
              this.resetForm()
              this.$store.dispatch('user/getMember')
            }
          })
        }
      })
    },

    // 编辑: 保存个人档案信息
    async formItemEdit(type, itemIndex) {
      // 定义不同类型的验证字段数组
      const validationFields = {
        staff: ['interview_position', 'interview_date', 'is_part', 'type'],
        basic: ['name', 'phone', 'position', 'frame_id'],
        user: ['card_id']
      };

      // 获取当前类型对应的验证字段
      const fields = validationFields[type] || [];

      // 如果有需要验证的字段
      if (fields.length > 0) {
        try {
          // 并行验证所有字段
          await Promise.all(fields.map(field => {
            return new Promise((resolve, reject) => {
              this.$refs.form.validateField(field, (error) => {
                if (error && error.length > 0) {
                  reject(error);
                } else {
                  resolve();
                }
              });
            });
          }));
          // 所有字段验证通过，执行编辑操作
          await this.editFn(type, itemIndex);
        } catch (error) {
          // 验证失败，不执行编辑操作
          return;
        }
      } else {
        // 没有需要验证的字段，直接执行编辑操作
        await this.editFn(type, itemIndex);
      }
    },
    async editFn(type, itemIndex) {
      this.FORMITEMISEDIT[itemIndex] = !this.FORMITEMISEDIT[itemIndex]
      this.ruleForm.edit_type = type
      if (this.ruleForm.position.length > 0) {
        this.ruleForm.position = this.ruleForm.position[this.ruleForm.position.length - 1]
      }
      this.ruleForm.frames = []
      this.ruleForm.photo = this.photo
      this.rangeUser.forEach((item) => {
        this.ruleForm.frames.push(item.id)
      })

      await putCardApi(this.id, this.ruleForm)
      await this.enterpriseCardId()
      await this.$store.dispatch('user/getMember')
    },

    // 新增：职位等级
    addQuick() {
      this.positionConfig = {
        title: '添加职位',
        width: '520px',
        data: this.treeData
      }
      this.$refs.positionDialog.handleOpen()
    },

    handleConfig() {
      this.getTreeData()
    },

    // 新增: 获取工作/教育经历列表
    addWorkFn(data, val) {
      // 根据 val 确定目标数组
      const targetArray = val === 'work' ? this.workData : this.educationsData;
      if (this.editTable === '编辑') {
        targetArray.splice(this.setIndex, 1, data);
      } else {
        targetArray.push(data);
      }
    },

    // 刷新个人简历工作经历列表
    async getUserWorkList() {
      let data = {
        resume_id: this.userWorkId
      }
      const result = await getUserWorkList(data)
      this.workData = result.data.list
    },

    // 刷新个人简历教育经历列表
    async getUserEducationList() {
      const result = await getUserEducationList()
      this.educationsData = result.data.list
    },

    // 新增: 编辑工作/教育经历
    putWork(row, index, type) {
      this.setIndex = index
      this.editTable = '编辑'
      if (type === 'works') {
        this.$refs.refwork.title = '编辑工作经历'
        this.add = 'work'
        this.$nextTick(() => {
          this.$refs.refwork.workForm = row
        })
      } else {
        this.$refs.refwork.title = '编辑教育经历'
        this.add = 'education'
        this.$nextTick(() => {
          this.$refs.refwork.educationForm = row
        })
      }

      this.$refs.refwork.dialogFormVisible = true
    },

    // 新增：删除工作/教育经历
    deleteWork(row, index, type) {
      if (type === 'works') {
        this.workData.splice(index, 1)
      }
      if (type === 'education') {
        this.educationsData.splice(index, 1)
      }
    },

    // 编辑: 获取工作经历列表
    async getWorkList() {
      let data = {
        user_id: this.id
      }
      const result = await getWorkList(data)
      this.workData = result.data.list
    },

    //  编辑: 新增工作经历
    addWorks(val) {
      const isPersonalResume = this.user === '个人简历';
      const handlePersonalResume = (api, refreshFn) => {
        this.$modalForm(api({})).then((res) => {
          refreshFn();
        });
      };
      const handleOtherUser = (type, title, formProp) => {
        this.add = type;
        this.editTable = '新增';
        this.$refs.refwork.title = title;
        this.$refs.refwork[formProp] = {};
        this.$refs.refwork.dialogFormVisible = true;
      };

      switch (val) {
        case '工作经历':
          if (isPersonalResume) {
            handlePersonalResume(userCreateWork, this.getUserWorkList.bind(this));
          } else {
            handleOtherUser('work', '新增工作经历', 'workForm');
          }
          break;
        case '教育经历':
          if (isPersonalResume) {
            handlePersonalResume(userCreateEducation, this.getUserEducationList.bind(this));
          } else {
            handleOtherUser('education', '新增教育经历', 'educationForm');
          }
          break;
        default:
          break;
      }
    },

    // 编辑: 编辑工作经历
    editWorks() {
      this.$modalForm(
        enterpriseWorkCreateApi({
          user_id: this.id
        })
      ).then((res) => {
        this.getWorkList()
      })
    },

    // 编辑: 获取教育经历列表
    async getEducationList() {
      let data = {
        user_id: this.id
      }
      const result = await getEducationList(data)
      this.educationsData = result.data.list
    },

    // 编辑 新增教育经历
    editEducation() {
      this.$modalForm(
        enterpriseEducationCreateApi({
          user_id: this.id
        })
      ).then((res) => {
        this.getEducationList()
      })
    },

    // 获取职位tree数据
    async getTreeData() {
      const result = await jobsCreate()
      this.treeData = result.data.tree
    },

    // 选择部门回调
    changeMastart(data) {
      // 更新部门列表
      this.frames = data;
      // 提取部门ID数组
      const frameIdArr = this.frames.map(item => item.id);
      this.ruleForm.frame_id = frameIdArr;

      // 记录之前的主部门ID
      const preMastartId = this.mastart_id;
      // 查找当前主部门
      const currentMastart = this.frames.find(el => el.is_mastart);

      if (currentMastart) {
        // 更新主部门ID
        this.ruleForm.main_id = currentMastart.id;
        this.mastart_id = currentMastart.id;

        if (preMastartId) {
          // 替换上级部门列表中的旧主部门
          this.superiorFrame = this.superiorFrame.map(i => 
            i.id === preMastartId ? currentMastart : i
          );
        } else {
          // 添加新的主部门到上级部门列表
          this.superiorFrame.push(currentMastart);
        }
      }
    },

    // 添加负责人
    handlesuperiorOpen() {
      setTimeout(() => {
        this.$refs.selectMember[0].handlePopoverShow()
      }, 200)
    },
    
    // 选择人员回调
    getSelectList(data) {
      this.superiorUser = data
      this.ruleForm.superior = this.superiorUser[0]
      this.ruleForm.superior_uid = this.superiorUser[0].value
    },

    // 删除管理范围
    cardTag(index, val) {
      this.rangeUser = this.rangeUser.filter((item) => {
        return item.id != val.id
      })
    },

    // 删除所在部门
    handleDeleteDep(index) {
      this.$refs[`popover-${index}`][0].doClose()
      this.frames.splice(index, 1)
    },

    // 回显数据
    async enterpriseCardId() {
      // 初始化数据
      this.superiorUser = [];
      this.rangeUser = [];
      this.positionText = [];
      this.manage_frameText = '';
      this.frameText = [];
      this.rangeUserText = [];

      try {
        const res = await enterpriseCardIdApi(this.id);
        this.formatData(res);

        // 处理主表单数据
        this.ruleForm = {
          ...res.data,
          age: res.data.age || '',
          work_years: res.data.work_years || '',
          education: res.data.education === 0 ? '' : Number(res.data.education),
          frame_id: res.data.frames.map((item) => item.id),
          position: res.data.job ? res.data.job.id : '',
          is_admin: (res.data.is_admin || 0).toString(),
          superior_uid: res.data.superior?.id || ''
        };

        if (!this.tabtypes) {
          this.typeText = '未入职';
          this.ruleForm.type = 3;
        }

        // 处理工作和教育经历数据
        this.workData = res.data.works;
        this.educationsData = res.data.educations;

        // 处理部门数据
        const newFrame = [];
        const newId = [];
        this.frames = res.data.frames;
        this.frames.forEach((el) => {
          if (el.is_mastart) {
            this.ruleForm.main_id = el.id;
            this.mastart_id = el.id;
          }
          newFrame.push({ name: el.name, is_mastart: el.is_mastart });
          newId.push(el.id);
        });
        this.frameText = newFrame;

        // 处理管理范围数据
        this.rangeUser = res.data.scope;
        this.rangeUserText = this.rangeUser.map((item) => item.name);

        // 处理职位和管理范围文本
        this.positionText = res.data.job ? res.data.job.name : '';
        this.manage_frameText = res.data.manage_frames.length > 0
          ? res.data.manage_frames.map((item) => item.name).join(',')
          : '--';

        // 处理负责人和直属上级信息
        this.is_adminText = this.ruleForm.is_admin === '1' ? '是' : '否';
        if (res.data.superior) {
          this.superiorUser.push(res.data.superior);
          this.superiorText = res.data.superior.name;
        } else {
          this.superiorText = '';
        }

        // 处理用户类型文本
        const typeNew = !this.tabtypes ? '未入职' : this.tabtypes === 1 ? '在职' : '离职';

        // 触发事件
        this.$emit('userInfoFn', {
          name: res.data.name,
          id: res.data.id,
          type: typeNew,
          uid: res.data.uid,
          position: res.data.job ? res.data.job.name : '暂无职位'
        });
        this.$emit('getPhoto', res.data.photo);
      } catch (error) {
        console.error('获取企业卡片信息失败:', error);
      }
    }
  },

  components: {
    // 新增: 添加工作/教育经历
    Addwork: () => import('./addWork'),
    // 编辑: 工作/教育经历
    tableBox: () => import('../components/tableBox'),
    // 上传图片
    UploadImage: () => import('../components/UploadImage.vue'),
    ElImageViewer: () => import('element-ui/packages/image/src/image-viewer'),

    selectMember: () => import('@/components/form-common/select-member'),
    selectDepartment: () => import('@/components/form-common/select-department'),

    // 职位等级
    positionDialog: () => import('@/views/hr/archives/components/positionDialog')
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-drawer__body {
  overflow-y: hidden;
}

.line {
  width: 100%;
  height: 3px;
  margin-top: 10px;
  margin-bottom: 30px;
  border-bottom: 1px dashed #ebeef5;
}
// .m10 {
//   margin: 10px 0 30px 0 !important;
// }

.label {
  color: #909399;
  font-weight: 400;
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
}
.activeItem {
  margin: -7px -10px !important;
}
.item-superior {
  margin-left: -88px;

  span {
    cursor: pointer;
    font-size: 13px;
  }
}
::v-deep .el-form-item__label,
.el-form-item__content {
  // font-size: 12px !important;
  // font-weight: 400;
  // color: #303133;
}
.text-title {
  font-size: 12px !important;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #909399;
  .format {
    font-size: 10px;
  }
}
.label-item {
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
}
::v-deep .el-cascader {
  width: 100%;
}
::v-deep .el-input--small {
  font-size: 13px;
}
::v-deep .el-input--medium {
  font-size: 13px;
}
::v-deep .el-button--small {
  width: 44px;
  height: 28px;
  padding: 0;
  font-size: 13px;
}
.labelfont {
  font-size: 13px;
  font-weight: 400;
  color: #303133;
}
.el-icon-warning {
  font-size: 10px;
  color: #1890ff;
  opacity: 0.5;
}
::v-deep .form-box .form-item .el-input {
  width: 100% !important;
}
::v-deep .el-drawer__close-btn {
  margin-bottom: 0;
}
.m10 {
  margin: 5px 0 20px 10px;
  color: #666;
}
.add {
  color: #1890ff;
  margin-left: 20px;
  cursor: pointer;
}
.main {
  margin: 20px;
  overflow-y: hidden;
}
::v-deep .el-tabs__item {
  font-weight: 700;
  z-index: 9999;
}
::v-deep .el-form-item__label {
  padding-right: 4px !important;
}
.table-box {
  height: 600px;
  overflow-y: auto;
  padding: 0 13px;
  .table-item {
    padding-bottom: 29px;
    .title {
      margin: 20px 0 14px;
      padding-left: 10px;
      border-left: 2px solid #1890ff;
      font-size: 13px;
    }
  }
}
.flx {
  display: flex;
}

.el-icon-circle-plus {
  margin-top: 8px;
  margin-left: 10px;
  font-size: 14px;
  color: #ccc;
}
.form-item-section {
  width: 100%;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  padding: 0px 15px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;

  span {
    margin: 5px 5px 5px 5px;
    &:last-of-type {
      margin-right: 0;
    }
  }
}
.header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}
.add-text {
  font-size: 14px;
  font-weight: 500;
  color: #1890ff;
  margin: 20px 20px 0 0;
}

.drawer-body {
  display: flex;
}

.right-item {
  line-height: 25px;
  cursor: pointer;
  margin-top: 10px;

  &:first-child {
    margin-top: 0;
  }
}

.right {
  margin: 0 20px;
  width: calc(100% - 180px);
  height: 700px;
  overflow-y: auto;
  padding-bottom: 80px;
  padding-top: 20px;
}

.right::-webkit-scrollbar {
  height: 0;
  width: 0;
}

.from-item-title {
  display: flex;
  justify-content: space-between;
  height: 14px;
  line-height: 14px;
  margin-bottom: 10px;
  align-items: center;
  margin-top: 10px;
  border-left: 2px solid #1890ff;
  .title {
    display: inline-block;
    font-size: 14px;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #333333;
  }

  span {
    padding-left: 10px;
    font-weight: 500;
    font-size: 13px;
  }
}

.form-box {
  display: flex;
  flex-wrap: wrap;
  // margin: 0 20px;
  justify-content: space-between;

  .form-item {
    width: 48%;

    ::v-deep .el-form-item__content {
      width: calc(100% - 90px);
    }

    ::v-deep .el-input {
      width: 95%;
    }

    ::v-deep .el-select {
      width: 100%;
    }

    ::v-deep .el-form-item {
      margin-bottom: 20px;
    }

    ::v-deep .el-textarea__inner {
      resize: none;
    }
  }
}

.plan-footer-one {
  height: 36px;
  line-height: 36px;

  .placeholder {
    font-size: 14px;
    color: #ccc;
  }

  span {
    margin-right: 6px;
  }
}

.from-text {
  margin: 20px 0;
  font-size: 11px;
  color: gray;
}

::v-deep .avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

::v-deep .avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  text-align: center;
}

::v-deep .el-upload--picture-card {
  width: 160px;
  height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  line-height: 100px;
  position: relative;
  overflow: hidden;
}

::v-deep .avatar {
  width: 160px;
  height: 100px;
  display: block;
}
.tabsEdit {
  height: 56px;
  display: flex;
  justify-content: space-between;
  .invitationUrl {
    margin-top: 10px;
    margin-right: 20px;
  }
}

.placeholder-text {
  font-size: 13px;
  color: #ccc;
  line-height: 20px;
}
</style>
