<!--
 * @Author: lxy
 * @Date: 2021-03-17 14:10:40
 * @Last Modified by:   lxy
 * @Last Modified time: 2021-03-17 14:10:40
 */
!-->
<template>
  <el-select
    v-model="dictionary"
    placeholder="请选择"
    clearable
    @change="selectChange"
    size="mini"
  >
    <el-option
      v-for="item in dictionaryOptions"
      :key="item.id"
      :label="item.text"
      :value="item.id"
    />
  </el-select>
</template>

<script>
import { getDictList } from '@/api/report/dict-data' // 获取数据字典
export default {
  name: 'GetDictionary',
  props: {
    dictKey: String, // 字典code
    updataDict: String, // 回显绑定的值
    value: String
  },
  data() {
    return {
      dictionary: '', // 绑定选中的值
      dictionaryOptions: [] // 拉下字典选项
    }
  },
  watch: {
    dictKey: {
      immediate: true,
      handler() {
        this.getSystem()
      }
    },
    value: {
      handler(val) {
        this.dictionary = val
      },
      deep: true
    }
  },
  created() {
    this.getSystem()
  },
  mounted() {
    this.dictionary = this.value
  },
  methods: {
    // 获取数据字典
    async getSystem() {
      const { code, data } = await getDictList(this.dictKey)
      if (code != '200') return
      this.dictionaryOptions = data
    },
    selectChange(val) {
      this.$emit('input', val)
    }
  }
}
</script>

<style lang="scss" scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}
</style>
