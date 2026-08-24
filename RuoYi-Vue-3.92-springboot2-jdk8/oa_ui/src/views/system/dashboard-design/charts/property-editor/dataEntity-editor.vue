<template>
  <el-form-item label="图表数据实体">
    <el-cascader
      v-model="myOption.dataEntity"
      placeholder="请选择实体"
      :options="unSystemEntityList"
      :show-all-levels="false"
      filterable
      :disabled="!designer.selectedId"
      size="small"
      :props="props"
      @change="changeEntity"
      clearable
    >
    </el-cascader>
    <!-- <el-select
      v-model="myOption.dataEntity"
      filterable
      placeholder="选择实体"
      @change="changeEntity"
      :disabled="!designer.selectedId"
      filter
    >
      <el-option :label="op.table_name" :value="op.id" v-for="(op, inx) in unSystemEntityList" :key="inx" />
    </el-select> -->
  </el-form-item>
</template>
<script>
import { databaseListApi, getDatabaseApi } from '@/api/develop'
export default {
  name: 'dataEntity-editor',
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  },
  data() {
    return {
      myOption: JSON.parse(JSON.stringify({ ...this.optionModel })),
      unSystemEntityList: [],
      props: {
        multiple: false,
        label: 'label',
        value: 'value',
        children: 'children',
        emitPath: false //绑定的内容只获取最后一级的value值。
      }
    }
  },
  computed: {
    // ...mapState({
    //   unSystemEntityList: (state) => state.common.unSystemEntityList
    // })
  },
  watch: {
    optionModel: {
      handler() {
        this.initchartStyle()
      },
      deep: true
    }
  },
  mounted() {
    this.initchartStyle()
    this.getdatabaseList()
  },
  methods: {
    getdatabaseList() {
      getDatabaseApi().then((res) => {
        // 解决一级value和二级value重复，导致选中不了问题
        res.data.forEach((item) => {
          item.value = item.value + 'res'
        })
        this.unSystemEntityList = res.data
        let allEntityName = {}
        res.data.forEach((el) => {
          el.children.forEach((item) => {
            allEntityName[item.value] = item.table_name_en
          })
        })
        localStorage.setItem('allEntityName', JSON.stringify(allEntityName))
      })
    },
    initchartStyle() {
      this.myOption = { ...this.optionModel }
    },
    changeEntity(e) {
      // this.myOption.setDimensional = {
      //   dimension: [],
      //   metrics: [],
      //   targetValue: 1,
      //   showFields: [],
      //   dimensionRow: [],
      //   dimensionCol: []
      // }
      // this.myOption.setChartFilter = {
      //   equation: '',
      //   list: []
      // }
      this.myOption.dataEntity = e
      // this.$emit('optionModelChange', this.myOption)
      this.$nextTick((e) => {
        // this.$emit('update:optionModel', this.myOption)
        this.$emit('clearSearch', this.myOption)
      })
    }
  }
}
</script>
<style lang="scss" scoped></style>
