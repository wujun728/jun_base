<template>
  <div>
    <el-form label-width="100px" label-position="left">
      <el-form-item label="数据集">
        <el-select
          size="mini"
          v-model="dataSetValue"
          filterable
          placeholder="请选择"
          @change="selectDataSet"
        >
          <el-option
            v-for="item in dataSet"
            :key="item.code"
            :label="item.setName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        v-for="(item, index) in userNameList"
        :key="index"
        :label="item.paramName"
      >
        <el-input v-model.trim="item.sampleItem" size="mini" />
      </el-form-item>
      <el-form-item v-for="item in setParamList" :key="item" :label="item">
        <Dictionary
          v-model="params[item]"
          :dict-key="getDictKey()"
          @input="selectParams($event, item)"
        />
      </el-form-item>
      <el-button
        style="width: 100%"
        type="primary"
        plain
        size="mini"
        @click="saveDataBtn"
      >刷新</el-button
      >
    </el-form>
  </div>
</template>
<script>
import { queryAllDataSet, detailBysetId } from '@/api/report/bigscreen'
import Dictionary from '@/components/Dictionary/index'

export default {
  name: 'DynamicComponents',
  components: {
    Dictionary
  },
  model: {
    prop: 'formData',
    event: 'input'
  },
  props: {
    chartType: String,
    dictKey: String,
    formData: Object
  },
  data() {
    return {
      dataSetValue: '',
      dataSet: [], // 数据集
      userNameList: [], // 用户
      setParamList: [], // 对应的不同的图形数据参数
      params: {},
      chartProperties: {}
    }
  },
  watch: {
    formData: {
      handler(val) {
        this.echoDataSet(val)
      },
      deep: true
    }
  },
  computed: {
    setCode() {
      let code = ''
      this.dataSet.forEach(el => {
        if (el.id == this.dataSetValue) {
          code = el.setCode
        }
      })
      return code
    }
  },
  mounted() {
    this.loadDataSet()
    this.echoDataSet(this.formData)
  },
  methods: {
    async loadDataSet() {
      const { code, data } = await queryAllDataSet()
      this.dataSet = data
      if (code != '200') return
    },
    async selectDataSet() {
      const { code, data } = await detailBysetId(this.dataSetValue)
      this.userNameList = data.dataSetParamDtoList
      this.setParamList = data.setParamList
      if (code != '200') return
    },
    async saveDataBtn() {
      const contextData = {}
      for (let i = 0; i < this.userNameList.length; i++) {
        contextData[this.userNameList[i].paramName] = this.userNameList[
          i
        ].sampleItem
      }
      const params = {
        chartType: this.chartType,
        setCode: this.setCode,
        chartProperties: this.chartProperties,
        contextData
      }
      this.$emit('input', params)
      this.$emit('change', params)
    },
    selectParams(val, key) {
      this.chartProperties[key] = val
    },
    getDictKey() {
      return this.dictKey == null ? 'CHART_PROPERTIES' : this.dictKey
    },
    // 数据集回显
    async echoDataSet(val) {
      if (!val) return
      const setCode = val.setCode

      await this.loadDataSet()

      this.dataSetValue = this.dataSet.filter(
        el => setCode == el.setCode
      )[0].id

      await this.selectDataSet()
      this.echoDynamicData(val)
    },
    echoDynamicData(val) {
      const chartProperties = this.deepClone(val.chartProperties)
      this.chartProperties = chartProperties
      if (this.userNameList.length > 0) {
      }
      if (this.setParamList.length > 0) {
        for (let i = 0; i < this.setParamList.length; i++) {
          const item = this.setParamList[i]
          if (chartProperties.hasOwnProperty(item)) {
            this.params[item] = chartProperties[item]
          }
        }
      }
    }
  }
}
</script>
<style lang="sass" scoped></style>
