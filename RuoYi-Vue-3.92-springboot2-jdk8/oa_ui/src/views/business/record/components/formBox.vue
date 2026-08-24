<template>
  <div class="box">
    <oaFromBox
      v-if="search.length > 0"
      :btnText="'导出'"
      :isAddBtn="true"
      :search="search"
      :title="`审批记录`"
      :btnIcon="false"
      :total="total"
      :viewSearch="viewSearch"
      @addDataFn="getExportData"
      @confirmData="confirmData"
    ></oaFromBox>
  </div>
</template>

<script>
import oaFromBox from '@/components/common/oaFromBox'
import { approveConfigSearchApi } from '@/api/business'
import 'animate.css'

export default {
  name: 'FormBox',
  components: {
    oaFromBox
  },
  props: ['total'],
  data() {
    return {
      tableFrom: {},
      sexOptions: [],
      search: [
        { field_name: '申请人姓名', field_name_en: 'name', form_value: 'input', data_dict: [] },

        {
          field_name: '审批类型',
          field_name_en: 'approve_id',
          form_value: 'select',
          data_dict: []
        },
        {
          field_name: '提交时间',
          field_name_en: 'time',
          form_value: 'date_time_picker'
        }
      ],
      viewSearch: [
        {
          field: 'status',
          title: '审批状态',
          type: 'select',
          options: [
            { name: this.$t('toptable.all'), value: '' },
            { name: '待审核', value: 0 },
            { name: '已通过', value: 1 },
            { name: '已拒绝', value: 2 },
            { name: '已撤销', value: -1 }
          ]
        },
        {
          field: 'frame_id',
          title: '部门',
          type: 'frame_id',
          options: []
        }
      ],
      identityOptions: [],
      departmentList: []
    }
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang
    }
  },
  mounted() {
    this.getEntListData()
  },
  methods: {
    // 获取创建分类
    async getEntListData() {
      const result = await approveConfigSearchApi(2)
      const data = result.data ? result.data : []
      this.identityOptions = data
      this.identityOptions.unshift({ name: '全部', id: '' })
      this.search[1].data_dict = this.identityOptions
    },

    getExportData() {
      this.$emit('getExportData')
    },

    // 重置
    reset() {
      this.timeVal = []
      this.tableFrom = {
        time: '',
        approve_id: '',
        name: '',
        status: '',
        frame_id: '',
        types: 2
      }
      this.departmentList = []
      this.handleEmit()
    },

    handleEmit() {
      this.$emit('confirmData', this.tableFrom)
    },
    // 确认
    handleConfirm() {
      this.confirmData()
    },

    confirmData(data) {
      if (data == 'reset') {
        this.reset()
      } else {
        this.tableFrom = { ...this.tableFrom, ...data }
        this.tableFrom.types = 2
      }

      this.handleEmit()
    }
  }
}
</script>

<style lang="scss" scoped>
.plan-footer-one {
  line-height: 28px;
  height: 28px;
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
</style>
