<template>
  <div>
    <oaFromBox
      :isAddBtn="false"
      :isTotal="false"
      :isViewSearch="false"
      :search="search"
      :sortSearch="false"
      :treeData="treeData"
      @confirmData="confirmData"
      @treeChange="handType"
    ></oaFromBox>
  </div>
</template>

<script>
import { billCateApi } from '@/api/enterprise'
import oaFromBox from '@/components/common/oaFromBox'
export default {
  name: 'FormBox',
  components: {
    oaFromBox
  },
  data() {
    return {
      pickerOptions: this.$pickerOptionsTimeEle,
      timeVal: [
        this.$moment().startOf('month').format('YYYY/MM/DD'),
        this.$moment().endOf('month').format('YYYY/MM/DD')
      ],
      tableFrom: {
        time: '',
        type: '',
        cate_id: []
      },
      statusOptions: [{ label: this.$t('toptable.all'), value: '' }],
      sexOptions: [
        { label: this.$t('finance.all'), value: '' },
        { label: this.$t('finance.income'), value: '1' },
        { label: this.$t('finance.pay'), value: '0' }
      ],
      search: [
        {
          field_name: '开始时间',
          field_name_end: '结束时间',
          field_name_en: 'time',
          form_value: 'date_picker',
          data_dict: [
            this.$moment().startOf('month').format('YYYY/MM/DD'),
            this.$moment().endOf('month').format('YYYY/MM/DD')
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
  mounted() {
    this.tableFrom.time = this.timeVal[0] + '-' + this.timeVal[1]
    this.getBillCate()
    this.confirmData(this.tableFrom)
  },
  methods: {
    // 搜索记账类型
    handType(e) {
      this.tableFrom.type = e.value
      this.tableFrom.cate_id = []
      if (e.value === '') {
        this.statusOptions = []
        this.tableFrom.cate_id = []
        this.getBillCate(e.value)
      } else {
        this.statusOptions = this.getBillCate(e.value)
      }
      this.confirmData(this.tableFrom)
    },
    unique(arr) {
      return Array.from(new Set(arr))
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
    confirmData(where) {
      if (where === 'reset') {
        this.timeVal = [
          this.$moment().startOf('month').format('YYYY/MM/DD'),
          this.$moment().endOf('month').format('YYYY/MM/DD')
        ]
        this.search[0].data_dict = this.timeVal
        where = {
          time: (this.tableFrom.time = this.timeVal[0] + '-' + this.timeVal[1]),
          type: '',
          cate_id: []
        }
      }
      this.tableFrom = { ...this.tableFrom, ...where }
      // Object.keys(this.tableFrom).forEach((key) => {
      //   if (key == 'cate_id') {
      //     let arr2 = [].concat.apply([], this.tableFrom.status)
      //     if (arr2.length > 0) {
      //       this.tableFrom.cate_id = this.unique(arr2)
      //     } else {
      //       this.tableFrom.cate_id = []
      //     }
      //   }
      // })
      this.$emit('confirmData', this.tableFrom)
    },
    setDataDict(field_name_en, data_dict) {
      for (let i = 0; i < this.search.length; i++) {
        if (this.search[i].field_name_en == field_name_en) {
          this.search[i].data_dict = data_dict
          break
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped></style>
