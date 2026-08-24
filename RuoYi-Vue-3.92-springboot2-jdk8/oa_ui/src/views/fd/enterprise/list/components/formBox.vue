<!-- 收支记账列表条件筛选组件 -->
<template>
  <div>
    <oaFromBox
      v-if="search.length > 0"
      :treeData="treeData"
      :search="search"
      :dropdownList="dropdownList"
      :isViewSearch="false"
      :sortSearch="false"
      :total="total"
      :title="$route.meta.title"
      btnText="新增账目"
      :isAddBtn="true"
      @addDataFn="addFinance"
      @dropdownFn="dropdownFn"
      @treeChange="treeChange"
      @confirmData="confirmData"
    ></oaFromBox>

    <export-excel :template="false" :save-name="saveName" :export-data="exportData" ref="exportExcel" />
  </div>
</template>

<script>
import file from '@/utils/file'
import Vue from 'vue'
Vue.use(file)
import { billCateApi, billImportApi, enterprisePayTypeApi } from '@/api/enterprise'

export default {
  name: 'FormBox',
  props: ['total'],
  components: {
    importExcel: () => import('@/components/common/importExcel'),
    exportExcel: () => import('@/components/common/exportExcel'),
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  data() {
    return {
      timeVal: [
        this.$moment().startOf('months').format('YYYY/MM/DD'),
        this.$moment().endOf('months').format('YYYY/MM/DD')
      ],
      gettime: '',
      tableFrom: {
        name: '',
        time: '',
        types: '',
        type_id: '',
        search: 0,
        cate_id: [],
        exportType: 0
      },
      payData: [],
      exportFileUrl: '',
      sexOptions: [
        { label: this.$t('finance.all'), value: '' },
        { label: this.$t('finance.income'), value: '1' },
        { label: this.$t('finance.pay'), value: '0' }
      ],
      statusOptions: [{ label: this.$t('toptable.all'), value: '' }],
      saveName: '',
      exportData: {
        data: [],
        cols: [{ wpx: 70 }, { wpx: 70 }, { wpx: 120 }, { wpx: 140 }, { wpx: 120 }]
      },
      columnNumber: 2,
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
      search: [
        {
          field_name: '金额/备注',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '开始时间',
          field_name_end: '结束时间',
          field_name_en: 'time',
          form_value: 'date_picker',
          data_dict: [
            this.$moment().startOf('months').format('YYYY/MM/DD'),
            this.$moment().endOf('months').format('YYYY/MM/DD')
          ]
        },
        {
          field_name: '账目分类',
          field_name_en: 'cate_id',
          form_value: 'cascader',
          data_dict: [],
          props: {
            collapseTags: true
          }
        },
        {
          field_name: '支付方式',
          field_name_en: 'type_id',
          form_value: 'select',
          // multiple: true,
          data_dict: []
        }
      ],
      treeData: [
        {
          options: [
            {
              value: '',
              label: '全部'
            },
            {
              value: '1',
              label: '收入'
            },
            {
              value: '0',
              label: '支出'
            }
          ]
        }
      ]
    }
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang
    }
  },
  watch: {
    lang() {
      this.setOptions()
    }
  },
  created() {
    this.tableFrom.time = `${this.timeVal[0]}-${this.timeVal[1]}`
    this.getBillCate()
    this.getPayType()
  },
  methods: {
    dropdownFn(data) {
      switch (data.value) {
        case 1:
          // 导出

          this.$emit('getExportData')
          break
        case 2:
          // 导入

          this.$emit('importExcelDFn')
          break
        case 3:
          // 导出模板
          this.$emit('exportTemplate')

          break
      }
    },
    setOptions() {
      this.sexOptions = [
        {
          label: this.$t('finance.all'),
          value: ''
        },
        {
          label: this.$t('finance.income'),
          value: '0'
        },
        {
          label: this.$t('finance.pay'),
          value: '1'
        }
      ]
      this.statusOptions = [
        {
          label: this.$t('toptable.all'),
          value: ''
        }
      ]
    },
    addFinance() {
      this.$emit('addFinance')
    },

    // 获取收入类型
    getBillCate(type) {
      const data = {
        types: type
      }
      const list = [{ label: this.$t('toptable.all'), value: '' }]
      billCateApi(data).then((res) => {
        res.data === undefined ? (res.data = []) : res.data
        this.statusOptions = res.data
        this.setDataDict('cate_id', res.data)
      })
      return list
    },

    getPayType() {
      var data = {
        page: 1,
        limit: 0
      }
      enterprisePayTypeApi(data).then((res) => {
        const data = res.data.list ? res.data.list : []
        this.exportFileUrl = res.data.import_temp
        this.payData = data
        this.payData.unshift({ name: this.$t('toptable.all'), id: '' })
        this.setDataDict('type_id', this.payData)
      })
    },
    // 确认
    handleConfirm() {
      this.tableFrom.exportType = 0
      this.tableFrom.search = 1
      this.confirmData()
    },
    // 搜索记账类型
    handType(e) {
      this.tableFrom.types = e
      this.handleConfirm()
    },
    confirmData(data) {
      if (data == 'reset') {
        this.search[1].data_dict = this.timeVal
        this.tableFrom = {
          name: '',
          time: this.timeVal[0] + '-' + this.timeVal[1],
          types: '',
          type_id: '',
          search: 0,
          cate_id: [],
          limit: 15,
          exportType: 0
        }
      } else {
        // 处理账目分类的数据结构
        // let arr = []
        // data.cate_id = eval(data.cate_id)
        // if (data.cate_id && data.cate_id.length > 0) {
        //   data.cate_id.map((item) => {
        //     item = eval(item)
        //     arr.push(item[item.length - 1])
        //   })
        // }

        // data.cate_id = arr
        this.tableFrom = { ...this.tableFrom, ...data }
      }
      this.tableFrom.search = 1
      this.$emit('confirmData', this.tableFrom)
    },
    unique(arr) {
      return Array.from(new Set(arr))
    },
    importExcelData(data) {
      if (data[0][0] == '示例：收入') {
        data.splice(0, 1)
      }
      var res = []
      if (data.length <= 0) {
        this.$message.error('批量导出内容为空')
      } else {
        for (let i = 0; i <= data.length - 1; i++) {
          if (data[i][0] === '') {
            continue
          } else {
            res.push({
              types: data[i][0],
              cate_id: data[i][1],
              num: data[i][2],
              pay_type: data[i][3],
              edit_time: this.$moment(data[i][4], 'YYYY-MM-DD HH:mm:ss').format('YYYY-MM-DD HH:mm:ss'),
              mark: data[i][5]
            })
          }
        }
        var data_s = {
          data: res
        }
        billImportApi(data_s)
          .then((res) => {
            this.tableFrom.search = 1
            this.confirmData()
          })
          .catch((error) => {})
      }
    },
    exportTemplate() {
      this.gettime = this.$moment().format('YYYY-MM-DD HH:mm:ss')
      if (!this.exportFileUrl) {
        this.$message.error('暂无导入模板,请联系管理员')
      } else {
        this.fileLinkDownLoad(this.exportFileUrl, '收支记账导入模板' + this.gettime + '.xlsx')
      }
    },
    exportExcelData() {
      this.tableFrom.exportType = 1
      this.confirmData()
    },
    getExportExcel() {
      this.$refs.exportExcel.exportExcel()
    },
    setDataDict(field_name_en, data_dict) {
      for (let i = 0; i < this.search.length; i++) {
        if (this.search[i].field_name_en == field_name_en) {
          this.search[i].data_dict = data_dict
          break
        }
      }
    },
    treeChange(data) {
      this.handType(data.value)
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep.el-cascader__tags {
  flex-wrap: nowrap;
}
::v-deep .el-cascader__search-input {
  cursor: pointer;
}
</style>
