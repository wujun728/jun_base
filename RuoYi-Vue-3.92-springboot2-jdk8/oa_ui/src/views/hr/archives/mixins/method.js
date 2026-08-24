import { enterpriseCardApi, enterpriseBatchApi } from '@/api/enterprise'
let myMixins = {
  methods: {
    // 导出功能
    getExportData(val) {
      this.$refs.formBox.saveName = '导出员工档案_' + this.$moment(new Date()).format('MM_DD_HH_mm_ss') + '.xlsx';
      enterpriseCardApi(val)
        .then((res) => {
          const newdata = res.data.list;
          if (newdata.length === 0) return;

          const aoaData = [
            [
              '姓名',
              '员工性别',
              '学历',
              '职位',
              '部门',
              '手机号码',
              '员工类型',
              '员工状态',
              '入职时间',
              '身份证',
              '婚姻状况',
              '现居住地',
              '毕业院校',
              '毕业时间',
              '银行卡号',
              '社保账号',
              '公积金号'
            ]
          ];

          const sexMap = { 0: '未知', 1: '男', 2: '女' };
          const partMap = { 0: '全职', 1: '兼职', 2: '实习', 3: '劳务派遣', 4: '退休返聘', 5: '劳务外包' };
          const typeMap = { 0: '未入职', 1: '正式', 2: '试用', 3: '实习', 4: '离职' };
          const educationMap = { 2: '高中及以下', 4: '专科', 5: '本科', 6: '研究生' };
          const statusMap = { 1: '正常', 0: '未激活' };
          const marriageMap = { 0: '未婚', 1: '已婚' };

          newdata.forEach((value) => {
            value.sex = sexMap[value.sex] || '未知';
            value.is_part = partMap[value.is_part] || '其他';
            value.type = typeMap[value.type] || '未入职';
            value.education = educationMap[value.education] || '';
            value.status = statusMap[value.status] || '未激活';

            if (value.frames.length > 0) {
              value.frames = value.frames.map(item => item.name).join(',');
            } else {
              value.frames = '未知';
            }

            value.marriage = marriageMap[value.marriage] || '已婚';

            aoaData.push([
              value.name,
              value.sex,
              value.education,
              value.job ? value.job.name : '',
              value.frames,
              value.phone,
              value.is_part,
              value.type,
              value.work_time,
              value.card_id,
              value.marriage,
              value.address,
              value.graduate_name,
              value.graduate_date,
              value.bank_num,
              value.social_num,
              value.fund_num
            ]);
          });

          this.$refs.formBox.exportData.data = aoaData;
          this.$refs.formBox.getExportExcel();
        })
        .catch((error) => {
          // this.$message.error(error.message);
        });
    },

    // 处理回显数据
    formatData(res) {
      const isPart = {
        0: '全职',
        1: '兼职',
        2: '实习',
        3: '劳务派遣',
        4: '退休返聘',
        5: '劳务外包'
      }
      this.is_partText = isPart[res.data.is_part] || '其他'

      const isType = {
        0: '未入职',
        1: '正式',
        2: '试用',
        3: '实习',
        4: '离职'
      }
      this.typeText = isType[res.data.type] || ''

      const isSex = {
        1: '男',
        2: '女'
      }
      this.sexText = isSex[res.data.sex] || '未知'
      const isMarriage = {
        0: '未婚',
        1: '已婚'
      }
      this.marriageText = isMarriage[res.data.marriage] || '已婚已育'
      const isEducation = {
        2: '高中及以下',
        4: '专科',
        5: '本科',
        6: '研究生'
      }
      this.educationText = isEducation[res.data.education] || ''
    },
    // 数组转对象
    arrToObj() {
      const obj = {}
      if (this.isEdit === 1) {
        if (this.superiorFrame && this.superiorFrame.length) {
          this.superiorFrame.forEach((el, index) => {
            obj[el.id] = el
          })
          this.activeDepartment = obj
        } else {
          this.activeDepartment = {}
        }
      } else if (this.isEdit === 0) {
        if (this.frames && this.frames.length) {
          this.frames.forEach((el, index) => {
            obj[el.id] = el
          })
          this.activeDepartment = obj
        } else {
          this.activeDepartment = {}
        }
      } else if (this.isEdit === 3) {
        if (this.rangeUser && this.rangeUser.length) {
          this.rangeUser.forEach((el, index) => {
            obj[el.id] = el
          })
          this.activeMastart = obj
        } else {
          this.activeMastart = {}
        }
      }
    },

    // 获取当天的日期
    getDate() {
      let nowDay = this.$moment().format('YYYY-MM-DD')
      if (this.tabtypes == 1) {
        this.ruleForm.work_time = nowDay
      } else if (this.tabtypes == 2) {
        this.ruleForm.quit_time = nowDay
      } else {
        this.ruleForm.interview_date = nowDay
      }
    },
    // 重置数据
    resetForm() {
      // 使用Object.fromEntries和Object.entries将对象的所有属性值设为true
      this.FORMITEMISEDIT = Object.fromEntries(
        Object.entries(this.FORMITEMISEDIT).map(([key]) => [key, true])
      );

      // 重置数组
      this.frames = [];
      this.rangeUser = [];
      this.superiorUser = [];
      this.workData = [];
      this.educationsData = [];

      // 重置表单字段
      this.ruleForm = {
        ...this.ruleForm,
        is_admin: '0',
        manage_frame: []
      };

      // 重置表单验证
      this.$refs.form?.resetFields();
    },

    // 批量删除
    handleDelete(type) {
      if (type === 1) {
        // 批量邀请
        this.$emit('invitationFn', type)
      }
      if (type === 2) {
        if (this.selection.length <= 0) {
          this.$message.error('至少选择一项内容')
        } else {
          let ids = []
          this.$modalSure('删除后不可恢复,您确认要删除吗').then(() => {
            this.selection.map((item) => {
              ids.push(item.id)
            })
            let data = {
              id: ids
            }
            enterpriseBatchApi(data)
              .then((res) => {
                // this.$message.success(res.message);
                this.getList()
              })
              .catch((err) => {
                // this.$message.error(err.message);
              })
          })
        }
      }
    }
  }
}
export default myMixins
