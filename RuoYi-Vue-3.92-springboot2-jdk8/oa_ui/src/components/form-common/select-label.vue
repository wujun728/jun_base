<!-- @FileDescription: 下拉选择标签组件 -->
<template>
  <div class="">
    <el-popover
      placement="bottom-start"
      trigger="manual"
      v-model="showPopover"
      popper-class="popover"
      ref="treePopover"
    >
      <div class="tree-box" id="treePopover">
        <div class="input">
          <el-input size="small" prefix-icon="el-icon-search" placeholder="请输入标签搜索" v-model="filterText">
          </el-input>
        </div>
        <el-tree
          highlight-current
          :props="props"
          :indent="4"
          :data="treeData"
          ref="tree"
          node-key="id"
          :filter-node-method="filterNode"
        >
          <div class="custom-tree-node" slot-scope="{ node, data }" @click.stop="selectFn(node, data)">
            <div class="flex-box">
              <span
                class="over-text"
                :class="{
                  isChecked: labelIds.includes(data[valType])
                }"
                :default-expanded-keys="treeExpandData"
                >{{ node.label }}</span
              >

              <span class="all-text" v-if="data.pid == 0" @click.stop="selectAllFn(node, data)">{{
                allIds.includes(data[valType]) ? '取消全选' : '全选'
              }}</span>
              <span v-if="data.pid != 0 && labelIds.includes(data[valType])" class="el-icon-check"></span>
            </div>
          </div>
        </el-tree>
      </div>
      <!-- 标签数据 -->
      <template slot="reference">
        <slot name="custom"></slot>
        <div
          class="select plan-footer-one mr10"
          ref="select"
          v-if="!isSlots && !slotType"
          @click.stop="handlePopoverShow"
        >
          <div v-if="selectList && selectList.length == 0" class="placeholder flex-between">
            <span>{{ placeholder }}</span>
          </div>

          <div v-if="selectList.length > 0 && !isSearch">
            <span
              v-for="(item, index) in selectList"
              :key="index"
              class="el-tag el-tag--small el-tag--info el-tag--light mr10"
              @click.stop="getTreeData()"
            >
              {{ item.name }}
              <i class="el-tag__close el-icon-close" @click.stop="cardTag(index, item[valType])" />
            </span>
          </div>
          <div class="flex-box" v-if="selectList.length > 0 && isSearch">
            <div
              style="max-width: 100px"
              class="el-tag el-tag--small el-tag--info el-tag--light mr10 lh-center"
              @click.stop=""
            >
              <span class="line1"> {{ selectList[0].name }}</span>
              <span class="el-tag__close el-icon-close" @click.stop="cardTag(0, selectList[0][valType])" />
            </div>
            <div
              v-if="selectList.length > 1"
              class="el-tag el-tag--small el-tag--info el-tag--light mr10"
              @click.stop=""
            >
              {{ selectList.length - 1 }}
            </div>
          </div>
          <span class="el-icon-arrow-down"></span>
        </div>
        <div v-if="slotType == 'customer'" @click.stop="handlePopoverShow">设置标签</div>
      </template>
    </el-popover>
  </div>
</template>
<script>
import { clientConfigLabelApi } from '@/api/enterprise'
import { extractArrayIds, isInArray, removeDuplicateObjects, getArrayDifference } from '@/libs/public'
export default {
  name: '',
  props: {
    // 选中的标签数据
    value: {
      type: Array,
      default: () => {
        return []
      }
    },
    labelList: {
      type: Array,
      default: () => {
        return []
      }
    },
    ids: {
      // 客户管理—批量设置标签表格id集合
      type: Array,
      default: () => {
        return null
      }
    },
    list: {
      type: Array,
      default: () => {
        return []
      }
    },
    placeholder: {
      type: String,
      default: '请选择标签'
    },

    isSearch: {
      type: Boolean,
      default: false
    },
    slotType: {
      type: String,
      default: ''
    },
    props: {
      type: Object,
      default: () => {
        return {
          children: 'children',
          label: 'name'
        }
      }
    }
  },
  data() {
    return {
      valType: 'id',
      filterText: '',
      allIds: [],
      expandData: [],
      isSlots: false,
      showPopover: false,
      treeExpandData: [],
      labelIds: [], // 选中标签id
      selectList: [], // 选中标签数据
      treeData: []
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    },
    value(newVal, oldValue) {
      this.labelIds = newVal.map(Number)
      if (this.labelIds.length > 0) {
        this.selectList = this.findNamesByIds(this.treeData, this.labelIds)
      }
    },
    labelList(newVal, oldValue) {
      if (this.labelList.length > 0) {
        this.selectList = newVal
        this.labelIds = extractArrayIds(this.selectList, this.valType)
      } else {
        this.selectList = []
        this.labelIds = []
      }
    },
    labelIds(val) {
      this.allIdsChange()
    }
  },
  created() {
    document.addEventListener('click', this.handleGlobalClick)
  },
  mounted() {
    if (this.$slots.custom) {
      this.isSlots = true
    }
    if (this.list.length > 0) {
      this.valType = 'id'
      this.treeData = this.list
    } else {
      this.valType = 'id'
    }
    if (this.labelList.length > 0) {
      this.selectList = this.labelList
      this.labelIds = extractArrayIds(this.selectList, this.valType)
    }
    if (this.value.length > 0 && this.value instanceof Array) {
      this.labelIds = this.value.map(Number)
      this.selectList = this.findNamesByIds(this.treeData, this.labelIds)
    }
  },
  methods: {
    // 获取树形结构默认展开节点
    getRoleTreeRootNode(res) {
      this.treeExpandData.push(res[0][this.valType])
    },
    checkUser(node) {
      console.log('点击')
    },

    findNamesByIds(tree, id) {
      let ids = id.map((str) => parseInt(str))
      let result = []
      function traverse(node) {
        if (ids.includes(node.id)) {
          result.push(node)
        }
        if (node.children) {
          for (const child of node.children) {
            traverse(child)
          }
        }
      }
      for (const node of tree) {
        traverse(node)
      }
      return result
    },

    cardTag(index, id) {
      this.selectList.splice(index, 1)
      this.labelIds = this.labelIds.filter((item) => item != id)
      this.confirmData()
    },
    allIdsChange() {
      this.treeData.map((item) => {
        let arr = extractArrayIds(item[this.props.children], this.valType)
        if (this.isContained(this.labelIds, arr)) {
          this.allIds.push(item[this.valType])
        } else {
          this.allIds = this.allIds.filter((item) => item != item[this.valType])
        }
      })
    },

    filterNode(value, data) {
      if (!value) return true
      return data.name.indexOf(value) !== -1
    },

    handleGlobalClick(e) {
      let treePopover = document.getElementById('treePopover')
      if (treePopover) {
        if (!treePopover.contains(e.target)) {
          let data = {
            ids: this.labelIds,
            list: this.selectList
          }
          this.$emit('submit', data)
          this.showPopover = false
        }
      }
    },
    handlePopoverShow() {
      if (this.ids && this.ids.length == 0) {
        this.$message.error('至少选一项')
        return false
      }
      if (this.list.length == 0) {
        this.getTreeData()
      }
      this.showPopover = true
    },
    handlePopoverHide() {
      this.showPopover = false
    },

    // 判断一个数组里面是否完全包含另一个数组
    // 定义函数
    isContained(a, b) {
      // a和b其中一个不是数组，直接返回false
      if (!(a instanceof Array) || !(b instanceof Array)) return false
      const len = b.length
      // a的长度小于b的长度，直接返回false

      if (a.length < len) return false
      for (let i = 0; i < len; i++) {
        // 遍历b中的元素，遇到a没有包含某个元素的，直接返回false
        if (!a.includes(b[i])) return false
      }
      // 遍历结束，返回true
      return true
    },
    toggleExpand(node) {
      node.expanded = !node.expanded
      this.$emit('expand-change', node)
    },

    // 选择标签单选
    selectFn(node, data) {
      if (node.parent && !node.isLeaf) {
        this.toggleExpand(node)
      }
      if (data.pid == 0) return false
      if (isInArray(this.labelIds, data[this.valType])) {
        this.labelIds = this.labelIds.filter((item) => item != data[this.valType])
        this.selectList = this.selectList.filter((item) => item[this.valType] != data[this.valType])
        this.confirmData()
        return false
      } else {
        this.selectList.push(data)
        this.labelIds = extractArrayIds(this.selectList, this.valType)
      }
      this.confirmData()
    },

    //  选择标签多选
    selectAllFn(node, data) {
      if (isInArray(this.allIds, data[this.valType])) {
        // 取消全选
        this.allIds = this.allIds.filter((item) => item != data[this.valType])
        this.selectList = getArrayDifference(this.selectList, data[this.props.children])
        this.labelIds = extractArrayIds(this.selectList, this.valType)
        this.confirmData()
        return false
      }

      // 全选
      this.allIds.push(data.id)
      if (data[this.props.children].length > 0) {
        this.labelIds = []
        data[this.props.children].map((item) => {
          this.selectList.push(item)
        })
        this.selectList = removeDuplicateObjects(this.selectList, this.valType)
        this.labelIds = extractArrayIds(this.selectList, this.valType)
      }
      this.confirmData()
    },

    confirmData() {
      let data = {
        ids: this.labelIds,
        list: this.selectList
      }
      this.$emit('handleLabelConf', data)
    },

    getTreeData() {
      if (this.treeData.length > 0) return false
      let data = {
        page: 0,
        limit: 0
      }
      clientConfigLabelApi(data).then((res) => {
        this.treeData = res.data.list
        this.getRoleTreeRootNode(res.data.list)
        if (this.value.length > 0) {
          this.selectList = this.findNamesByIds(this.treeData, this.labelIds)
        }
      })
    }
  }
}
</script>
<style scoped lang="scss">
.input {
  margin-top: 12px;
  margin-bottom: 12px;
}
.tree-box {
  padding: 0 20px 20px 20px;
  width: 242px;
  min-height: 150px;
  .custom-tree-node {
    position: relative;
    width: calc(100% - 40px);
    .flex-box {
      width: 100%;
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 14px;
      color: #303133;
      display: flex;
      align-items: center;
      span:first-of-type {
        display: inline-block;
        max-width: 80%;
      }
      .all-text {
        font-size: 13px;
        color: #1890ff;
        cursor: pointer;
        margin-left: 10px;
      }
      .el-icon-check {
        font-size: 18px;
        position: absolute;
        right: 0px;
        color: #1890ff;
      }
    }
  }

  ::v-deep.el-tree {
    max-height: 350px;
    overflow-y: auto;
    scrollbar-width: none; /* firefox */
    -ms-overflow-style: none; /* IE 10+ */
    .is-checked {
      color: #1890ff !important;
      cursor: pointer;
    }

    .el-tree-node__content {
      height: 32px;
      line-height: 32px;
    }
  }
}
::v-deep.el-tree-node__content > .el-tree-node__expand-icon {
  padding-left: 0px;
}

.plan-footer-one {
  position: relative;
  cursor: pointer;
  -webkit-appearance: none;
  background-color: #fff;
  background-image: none;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  color: #c0c4cc;
  display: inline-block;
  font-size: inherit;
  min-height: 32px;
  display: flex;
  align-items: center;
  outline: none;
  font-size: 13px;
  padding: 0 15px;
  -webkit-transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
  transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
  width: 100%;
  .el-tag.el-tag--info {
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 13px;
    color: #303133;
  }
}
.isChecked {
  color: #1890ff !important;
}
.el-icon-arrow-down {
  font-weight: 400;
  position: absolute;
  right: 10px;
}
::v-deep .el-popper {
  margin-top: 5px;
}
.flex-box {
  display: flex;
  align-items: center;
}
.line1 {
  display: inline-block;
  max-width: 80%;
  overflow: hidden;
  text-overflow: ellipsis; //文本溢出显示省略号
  white-space: nowrap; //文本不会换行
}
</style>
<style>
.popover {
  padding: 0px 12px 18px 12px;
}
</style>
