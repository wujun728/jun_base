<!--element下拉列表封装
  使用：
   <hyb-select ref="hybSelect"
   url="/v1/orgs"
   v-model="search.orgId" option="orgId" label="orgName"
   placeholder multiple localOptions></hyb-select>

    1.url:要显示的下拉列表数据来源
    规范：返回的数据要符合(JSON中对应的字段必须：label和value,不能其他)
      [
        {label:'显示1',value:'值1'},
        {label:'显示2',value:'值2'},
        {label:'显示3',value:'值3'},
        ...
      ]
    2.v-model数据的双向绑定,绑定父组件中的字段
    3.option自定义select的value对应的字段
    4.label自定义select的label对应的字段
    5.placeholder下拉列表提示
    6.multiple是否是多选
    7.localOptions使用本地的数据,不用请求远程数据,注意：使用该属性时Url属性不能要,不然无效
-->
<template>
  <div>
    <el-select
      v-model="selectValue"
      :clearable="clearable"
      :collapse-tags="collapseTags"
      filterable
      class="filter-item"
      :placeholder="placeholder"
      :disabled="disabled"
      :multiple="multiple == null ? false : true"
      :remote="remoteFilter"
      :remote-method="remoteQuery"
      @change="change"
    >
      <el-option
        v-for="(item, index) in options"
        :key="index"
        :label="getItemLabel(item, label)"
        :value="item[option]"
        :disabled="isDisabledOption(item)"
      >
        <template v-if="mergeLabel">
          <span style="float: left">{{ getItemLabel(item, label) }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{
            item[option]
          }}</span>
        </template>
      </el-option>
      <el-option v-if="totalPage > 1" label="搜索更多" value="" disabled />
    </el-select>
  </div>
</template>

<script>
import request from '@/utils/request'
import { getStorageItem } from '@/utils/storage'
export default {
  props: {
    dictCode: null, // 当传入dictCode时，可以不用传递url
    url: null,
    method: null,
    queryParam: {
      type: Object,
      default: () => {
        return {}
      }
    },
    value: null,
    placeholder: null,
    label: {
      type: String,
      default: 'text'
    },
    option: {
      type: String,
      default: 'id'
    },
    multiple: null,
    localOptions: null,
    disabled: null,
    clearable: {
      type: Boolean,
      default: true
    },
    collapseTags: {
      type: Boolean,
      default: false
    },
    mergeLabel: {
      type: Boolean,
      default: false
    },
    // 禁用的下拉选项
    disabledOptions: {
      type: String,
      default: () => {
        return ''
      }
    },
    // 使用远程搜索
    remoteFilter: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      options: null,
      selectValue: null,
      // 如果是分页的，
      totalPage: 0
    }
  },
  computed: {
    // 根据dictCode和url拼出最终的请求url
    requestUrl() {
      if (this.url != null && this.url.trim() != '') {
        if (this.url.indexOf('?') > 0) {
          if (this.option == null) {
            console.log('url-' + this.url.substring(this.url.indexOf('?')))
          }
          if (this.label == null) {
          }
        }
        return this.url
      }
      if (this.dictCode != null && this.dictCode.trim() != '') {
        return `/meta/gaeaDict/select/${this.dictCode}`
      }
      return null
    }
  },
  watch: {
    value: function(val, oldVal) {
      if (this.multiple != null) {
        if (!this.value) {
          this.selectValue = []
        } else {
          this.selectValue = this.value
        }
      } else {
        if (this.value != null && this.value != undefined) {
          this.selectValue = this.value
        } else {
          this.selectValue = ''
        }
      }
    },
    url() {
      setTimeout(() => {
        this.queryData()
      }, 500)
    }
  },
  created() {
    if (this.multiple != null) {
      this.selectValue = this.value
    } else {
      if (this.value != null) {
        this.selectValue = this.value
      }
    }
  },
  mounted() {
    if (this.requestUrl == null) {
      this.options = this.localOptions
      return
    }
    this.queryData()
  },
  methods: {
    // 判断选择是否已经禁用
    isDisabledOption(option) {
      if (
        option == null ||
        this.disabledOptions == null ||
        this.disabledOptions.length == 0
      ) {
        return false
      }
      var currentOptionVal = option[this.option]
      return this.disabledOptions.indexOf(currentOptionVal) >= 0
    },
    change(value) {
      if (value === '') {
        value = null
      }
      this.$emit('input', value)

      // 根据当前值，找出对应的选项
      var optionItem = this.options.find(item => item[this.option] == value)
      this.$emit('change', value, optionItem)
    },
    // 根据用户配置的label，生成对应的标签
    getItemLabel(item, label) {
      if (label.indexOf('${') < 0 && label.indexOf('}' < 0)) {
        return item[label]
      }
      var reg = /\$\{[a-zA-Z0-9]*\}/g
      var list = label.match(reg)
      // ["${id}", "${text}"]
      var result = label
      for (var i = 0; i < list.length; i++) {
        var sub = list[i]
        var key = sub.replace('${', '').replace('}', '')
        result = result.replace(sub, item[key])
      }
      return result
    },
    // 从本地localStorage取 gaeaDict
    getOptionsFromLocalStorage() {
      var dicts = getStorageItem('gaeaDict')
      var options = []
      if (!dicts.hasOwnProperty(this.dictCode)) {
        return []
      }
      var dictItems = dicts[this.dictCode]
      for (var i = 0; i < dictItems.length; i++) {
        var dictItem = dictItems[i]
        options.push({ id: dictItem.id, text: dictItem.text })
      }
      return options
    },
    queryData() {
      // 所有从本地localStorage取，因为在App.vue中已经请求远程保存到本地了
      var options = this.getOptionsFromLocalStorage()
      if (this.isNotBlank(options)) {
        this.options = options
        return
      }
      // 本地localStorage取不到，再从远程接口取
      if (this.requestUrl == null) {
        return
      }
      if (
        this.method != null &&
        this.method.toLocaleLowerCase().trim() == 'post'
      ) {
        this.queryDataByPost()
      } else {
        this.queryDataByGet()
      }
    },
    queryDataByGet(keyword) {
      var param = this.deepClone(this.queryParam)
      if (this.isNotBlank(keyword)) {
        param['keyword'] = keyword
      }
      param['multiple'] = this.multiple == null ? null : 1
      request({
        url: this.requestUrl,
        headers: { noPrompt: true },
        params: param
      }).then(response => {
        this.setOptions(response.data)
      })
    },
    queryDataByPost(keyword) {
      var param = this.deepClone(this.queryParam)
      if (this.isNotBlank(keyword)) {
        param['keyword'] = keyword
      }
      request({
        url: this.requestUrl,
        method: 'post',
        headers: { noPrompt: true },
        data: param
      }).then(response => {
        this.setOptions(response.data)
      })
    },
    setOptions(resData) {
      if (resData == null || resData.length == 0) {
        this.options = []
        this.totalPage = 0
        return
      }
      if (this.isArray(resData)) {
        this.options = resData
        this.totalPage = 1
        return
      }
      if (
        resData.records == null ||
        resData.total == null ||
        resData.pages == null
      ) {
        this.options = []
        return
      }
      this.totalPage = resData.pages
      // resData.records
      // resData.total
      // resData.size
      // resData.current
      this.options = resData.records
    },
    remoteQuery(keyword) {
      if (this.isBlank(keyword)) {
        return
      }
      setTimeout(() => {
        if (
          this.method != null &&
          this.method.toLocaleLowerCase().trim() == 'post'
        ) {
          this.queryDataByPost(keyword)
        } else {
          this.queryDataByGet(keyword)
        }
      }, 200)
    }
  }
}
</script>
<style scoped>
.filter-item {
  width: 100%;
}
</style>
