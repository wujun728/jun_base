<!--
 * @Author: lide1202@hotmail.com
 * @Date: 2021-5-4 11:04:24
 * @Last Modified by:   lide1202@hotmail.com
 * @Last Modified time: 2021-5-5 11:04:24
 !-->
<template>
  <div>
    <el-input class="filterInput" placeholder="搜索" v-model="filterText" v-if="enableFilter" />
    <div class="title">{{ labelName }}</div>
    <el-tree
      ref="table_tree"
      :data="treeData"
      node-key="id"
      :default-expand-all="isOpen"
      :expand-on-click-node="false"
      :filter-node-method="filterNode"
      @node-click="nodeClick"
      @check="checkedEvent" />
  </div>
</template>

<script>
import request from '@/utils/request'
export default {
  components: {},
  props: {
    url: {
      type: [String],
      default: () => {
        return ''
      },
    },
    id: {
      type: [String],
      default: () => {
        return 'id'
      },
    },
    label: {
      type: [String],
      default: () => {
        return ''
      },
    },
    value: {
      type: [String],
      default: () => {
        return ''
      },
    },
    labelName: String,
    enableFilter: Boolean,
    isOpen: Boolean,
  },
  data() {
    return {
      filterText: '',
      treeData: [],
    }
  },
  computed: {},
  watch: {
    filterText(val) {
      this.$refs.table_tree.filter(val)
    },
  },
  mounted() {
    this.queryData()
  },
  methods: {
    filterNode(val, data) {
      if (!val) return true
      return data.label.indexOf(val) !== -1
    },
    queryData() {
      if (this.isBlank(this.url)) {
        return
      }
      request({
        url: this.url,
        method: 'GET',
      }).then((response) => {
        if (response.code != '200') {
          return
        }
        this.treeData = Object.prototype.toString.call(response.data) == '[object Array]' ? response.data : response.data.tree|| response.data.menuTree
      })
    },
    // 点击tree节点时 将tree的id作为上级机构代码 查询列表
    nodeClick(node) {
      this.$emit('input', node['id'])
      this.$emit('node-click', node['id'])
    },
    checkedEvent(item, evt) {
      var ids = evt.checkedKeys.toString()
      this.$emit('input', ids)
    },
  },
}
</script>

<style scoped lang="scss">
.filterInput {
  margin-bottom: 20px;
}
</style>
