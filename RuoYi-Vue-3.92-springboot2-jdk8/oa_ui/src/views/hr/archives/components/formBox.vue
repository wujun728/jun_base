<template>
  <div>
    <oaFromBox
      v-if="search.length > 0"
      :search="search"
      :dropdownList="dropdownList"
      :viewSearch="viewSearch"
      :timeSearch="timeSearch"
      :isViewSearch="isViewSearch"
      :total="total"
      :title="`员工列表`"
      @addDataFn="openDrawer"
      @dropdownFn="dropdownFn"
      @confirmData="confirmData"
    ></oaFromBox>

    <export-excel :template="false" :save-name="saveName" :export-data="exportData" ref="exportExcel" />
    <!-- 导入 -->
    <importExcel
      ref="importExcel"
      :distinguish="distinguish"
      :column-number="columnNumber"
      @importExcelData="importExcelData"
    />
  </div>
</template>

<script>
import 'element-ui/lib/theme-chalk/display.css'
import { getTemp, importCardApi } from '@/api/enterprise'
import exportExcel from '@/components/common/exportExcel'
import importExcel from '@/components/common/importExcel'
import oaFromBox from '@/components/common/oaFromBox'
import 'animate.css'
import file from '@/utils/file'
import Vue from 'vue'
Vue.use(file)
export default {
  name: 'FormBox',
  components: {
    importExcel,
    exportExcel,
    oaFromBox
  },
  props: {
    notEmployees: {
      type: String
    },
    total: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      departmentShow: false,
      pickerOptions: this.$pickerOptionsTimeEle,
      showForm: false,
      showText: '展开',
      distinguish: 2,
      columnNumber: 1,
      exportData: {
        data: [],
        cols: [{ wpx: 70 }, { wpx: 70 }, { wpx: 120 }, { wpx: 140 }, { wpx: 120 }]
      },
      saveName: '',
      timeVal: [],
      isViewSearch: true,
      dropdownList: [
        {
          label: '导出',
          value: 1
        },
        {
          label: '导入',
          value: 2
        },
        {
          label: '下载模板',
          value: 3
        }
      ],
      search: [],
      sexOptions: [
        {
          name: this.$t('toptable.all'),
          value: ''
        },
        {
          name: this.$t('toptable.unknown'),
          value: '0'
        },
        {
          name: this.$t('toptable.male'),
          value: '1'
        },
        {
          name: this.$t('toptable.female'),
          value: '2'
        }
      ],
      educationOptions: [
        {
          name: '研究生',
          value: 6
        },
        {
          name: '本科',
          value: 5
        },
        {
          name: '专科',
          value: 4
        },

        {
          name: '高中及以下',
          value: 2
        }
      ],
      timeSearch: [
        {
          name: '创建时间',
          value: 'createTime'
        },
        {
          name: '修改时间',
          value: 'editTime'
        }
      ],
      viewSearch: [
        {
          field: 'sex',
          title: '员工性别',
          type: 'select',
          options: []
        },
        {
          field: 'education',
          title: '学历',
          type: 'select',
          options: this.educationOptions
        },
        {
          field: 'status',
          title: '账号状态',
          type: 'select',
          options: [
            {
              name: this.$t('toptable.all'),
              value: ''
            },
            {
              name: this.$t('toptable.normal'),
              value: 1
            },
            {
              name: '停用',
              value: 2
            },
            {
              name: '未激活',
              value: '0'
            }
          ]
        },
        {
          field: 'type',
          title: '员工状态',
          type: 'select',
          options: [
            {
              name: '全部',
              value: ''
            },
            {
              name: '正式',
              value: 1
            },
            {
              name: '试用',
              value: 2
            }
          ]
        }
      ],

      tableFrom: {
        time: '',
        sex: '',
        field: '',
        search: '',
        status: '',
        frame_id: '',
        types: [],
        education: '',
        exportType: 0
      },

      // 员工类型
      identityOptions: [
        {
          label: '全部',
          value: ''
        },
        {
          label: '正式',
          value: 1
        },
        {
          label: '试用',
          value: 2
        }
      ],
      activeDepartment: {},
      openStatus: false,
      departmentList: []
    }
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang
    }
  },

  mounted() {
    if (this.notEmployees == '未入职') {
      this.isViewSearch = false
      this.search = [
        {
          field_name: '姓名/手机号',
          field_name_en: 'search',
          form_value: 'input'
        },
        {
          field_name: '员工性别',
          field_name_en: 'sex',
          form_value: 'select',
          data_dict: this.sexOptions
        },
        {
          field_name: '学历',
          field_name_en: 'education',
          form_value: 'select',
          data_dict: this.educationOptions
        }
      ]
    } else if (this.notEmployees == '在职') {
      this.isViewSearch = true
      this.search = [
        {
          field_name: '姓名/手机号',
          field_name_en: 'search',
          form_value: 'input'
        },
        {
          field_name: '选择部门',
          field_name_en: 'frame_id',
          form_value: 'frame_id',
          data_dict: []
        },
        {
          field_name: '入职时间',
          field_name_en: 'time',
          form_value: 'date_picker'
        }
      ]
      this.viewSearch[0].options = this.sexOptions
      this.viewSearch[1].options = this.educationOptions
    } else {
      this.isViewSearch = true
      this.search = [
        {
          field_name: '姓名/手机号',
          field_name_en: 'search',
          form_value: 'input'
        },
        {
          field_name: '选择部门',
          field_name_en: 'frame_id',
          form_value: 'frame_id',
          data_dict: []
        },
        {
          field_name: '离职时间',
          field_name_en: 'time',
          form_value: 'date_picker'
        }
      ]
      this.viewSearch[0].options = this.sexOptions
      this.viewSearch[1].options = this.educationOptions
      this.viewSearch.splice(2, 2)
    }
  },
  methods: {
    async dropdownFn(data) {
      if (data.value == 1) {
        this.tableFrom.exportType = 1
        this.confirmData()
      } else if (data.value == 2) {
        this.$refs.importExcel.btnClick()
      } else if (data.value == 3) {
        const result = await getTemp()
        this.fileLinkDownLoad(result.data.url, '员工档案导入模板.xlsx')
      }
      this.tableFrom.exportType = 0
    },
    // 导入
    async importExcelData(value) {
      let tabtypes = localStorage.getItem('tabTypes')
      var res = []
      if (value.length <= 0) {
        this.$message.error('批量导入内容为空')
      } else {
        for (let i = 0; i <= value.length - 1; i++) {
        
          if (value[i][0] === '') {
            continue
          } else {
            const isType = {
              正式: 1,
              试用: 2,
              实习: 3,
              离职: 4
            }
            value[i][7] = isType[value[i][7]] || 0
            const isPart = {
              兼职: 1,
              全职: 0,
              实习: 2,
              劳务派遣: 3,
              退休返聘: 4,
              劳务外包: 5,
              其他: 6
            }
            value[i][6] = isPart[value[i][6]] || 0

            const isGender = {
              男: 1,
              女: 2
            }
            value[i][1] = isGender[value[i][1]] || 0

            const isEducation = {
              研究生: 6,
              本科: 5,
              专科: 4,
              高中及以下: 2
            }
            value[i][2] = isEducation[value[i][2]] || 2

            res.push({
              name: value[i][0],
              sex: value[i][1],
              education: value[i][2],
              phone: value[i][5],
              is_part: value[i][6],
              type: value[i][7],
              position: '',
              frames:'' ,
              card_id: value[i][8]
            })
          }
        }
      }
      let obj = {
          type: tabtypes,
          data: res
        }
          await importCardApi(obj)
    },

    // 导出上传模板
    async exportTemplate() {
      const result = await getTemp()
      this.fileLinkDownLoad(result.data.url, '员工档案导入模板.xlsx')
    },
    // 新增
    openDrawer() {
      this.$emit('opendrawer', true)
    },
    getExportExcel() {
      this.$refs.exportExcel.exportExcel()
    },

    confirmData(data) {
      if (data == 'reset') {
        this.$emit('confirmData', {})
      } else {
        this.$emit('confirmData', data)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .animate__animated {
  animation-duration: 0.4s;
}
.noWrap {
  display: flex;
  justify-content: space-between;
}
::v-deep .el-button {
  font-size: 13px;
}

.open {
  width: 62px;
  height: 32px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;

  text-align: center;
  font-size: 13px;
  color: #303133;
  line-height: 32px;
  cursor: pointer;
}
.iconzhankai1 {
  font-size: 13px;
  color: #909399;
}
.right-text {
  display: inline-block;
  transform: rotate(180deg);
}
.box {
  display: flex;
  flex-wrap: wrap;
}
.department ::v-deep .el-input__inner {
  color: #c0c4cc;
}
::v-deep .from-s .flex-row .el-form-item {
  margin-left: 0px;
  margin-right: 10px;
}
.form-top-line._show {
  width: 100%;
}

.from-s .select-bar {
  height: 33px;
}
</style>
