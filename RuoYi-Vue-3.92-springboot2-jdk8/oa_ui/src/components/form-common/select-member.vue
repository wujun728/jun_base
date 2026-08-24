<!-- @FileDescription: 下拉选择人员组件 -->
<template>
  <el-popover
    placement="bottom-start"
    trigger="manual"
    v-model="showPopover"
    width="266"
    popper-class="popover"
    ref="treePopover"
  >
    <isFullScreen @call-parent-method="handlePopoverHide">
      <!-- 人员数据 -->
      <div class="tree-box">
        <div class="input">
          <el-input size="small" prefix-icon="el-icon-search" placeholder="请输入人员搜索" v-model="filterText">
          </el-input>
        </div>
        <el-tree
          highlight-current
          :props="props"
          :data="treeData"
          :show-checkbox="!onlyOne"
          :default-checked-keys="selectIds"
          :filter-node-method="filterNode"
          @check="depCheck"
          @node-click="checkUser"
          ref="tree"
          node-key="id"
        >
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span
              :class="{
                isChecked: userIds.includes(data.value)
              }"
            >
              <i v-if="data.type == 0" class="tree-icon iconfont iconwenjianjia" />
              <img v-if="data.type == 1" :src="data.avatar" alt="" class="avatar" />
              {{ node.label }}</span
            >
          </span>
        </el-tree>
      </div>
    </isFullScreen>

    <!-- 人员数据 -->
    <template slot="reference">
      <slot name="custom"></slot>
      <div class="select plan-footer-one mr10" ref="select" v-if="!isSlots" @click="handlePopoverShow">
        <span class="el-icon-arrow-down"></span>
        <span v-if="userList && userList.length == 0" class="placeholder">{{ placeholder }}</span>
        <div class="flex-box" v-if="userList.length > 0 && !isSearch">
          <span
            v-for="(item, index) in userList"
            :key="index"
            class="el-tag el-tag--small el-tag--info el-tag--light mr10"
            @click.stop=""
          >
            <img v-if="isAvatar" :src="item.avatar" alt="" class="avatar" /> {{ item.name }}
            <i class="el-tag__close el-icon-close" @click.stop="cardTag(index)" />
          </span>
        </div>

        <!-- 以下是筛选条件展示样式 -->
        <div class="flex-box" v-if="userList.length > 0 && isSearch">
          <span
            v-for="(item, index) in userList.slice(0, 1)"
            :key="index"
            class="el-tag el-tag--small el-tag--info el-tag--light mr10"
            @click.stop=""
          >
            <img v-if="isAvatar" :src="item.avatar" alt="" class="avatar" /> {{ item.name }}
            <i class="el-tag__close el-icon-close" @click.stop="cardTag(0)" />
          </span>
          <el-tag class="el-tag el-tag--small el-tag--info el-tag--light" v-if="userList.length > 1">
            {{ userList.length - 1 }}</el-tag
          >
        </div>
      </div>
    </template>
  </el-popover>
</template>
<script>
import { extractArrayIds, getArrayDifference, isInArray, removeDuplicateObjects } from '@/libs/public'
import isFullScreen from '@/components/isFullScreen/index'

export default {
  name: '',
  components: { isFullScreen },
  props: {
    // 选中人员数据
    value: {
      type: Array,
      default: () => {
        return []
      }
    },

    // 角色权限禁用特殊处理
    disabled: {
      type: Boolean,
      default: false
    },
    placeholder: {
      type: String,
      default: '请选择人员'
    },
    // 只能单选一个人员
    onlyOne: {
      type: Boolean,
      default: false
    },
    // 开启权限
    role: {
      type: Number,
      default: 0
    },
    // 是否展示人员头像
    isAvatar: {
      type: Boolean,
      default: false
    },
    isSearch: {
      type: Boolean,
      default: false
    },
    // 禁用人员名单
    disabledList: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      props: {
        children: 'children',
        label: 'label',
        disabled: this.disabledFn
      },

      treeHeight: '',
      filterText: '',
      selectIds: [],
      showPopover: false,
      userList: [], //选中的人员数据
      userIds: [], //选中的人员的id
      selectList: [],
      leave: 0,
      isSlots: false,
      treeData: []
    }
  },
  computed: {
    // checkDisabled() {
    //   return (node) => {
    //     console.log(node, this.userIds)
    //     return this.userIds.includes(node.value)
    //   }
    // }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    },

    value(newVal, oldValue) {
      if (this.value.length > 0) {
        this.userList = this.value
        this.userIds = extractArrayIds(this.userList, this.userList[0].id ? 'id' : 'value')
        this.selectIds = this.findNamesByIds(this.treeData, this.userIds)
        this.$refs.tree.setCheckedKeys(this.selectIds)
      } else {
        this.userList = []
        this.userIds = []
        this.selectIds = []
        this.$refs.tree.setCheckedKeys([])
      }
    }
  },
  mounted() {
    document.addEventListener('click', this.handleGlobalClick)
    // 判断是否有插槽内容
    if (this.$slots.custom) {
      this.isSlots = true
    }
    this.getTreeData()
    if (this.value.length > 0) {
      this.userList = this.value
      this.userIds = extractArrayIds(this.userList, this.userList[0].id ? 'id' : 'value')
      this.selectIds = this.findNamesByIds(this.treeData, this.userIds)
    }
  },
  methods: {
    // 禁用某些节点
    disabledFn(data, node) {
      if (this.disabled) {
        if (this.disabledList.length > 0) {
          return this.disabledList.includes(data.value)
        } else {
          return this.userIds.includes(data.value)
        }
      } else {
        return false
      }
    },
    cardTag(index) {
      this.userList.splice(index, 1)

      this.$emit('getSelectList', this.userList)
    },
    // 多选
    depCheck(data, status) {
      if (data.user_count === 0) {
        return false
      }
      if (this.disabledList.includes(data.value)) {
        return this.$message.warning('不能选择此人员')
      }
      // 选择成员
      const arr = this.getDepartmentDataById(data, data.value)
      // 点击选中
      if (isInArray(status.checkedKeys, data.id)) {
        this.userList.push(...arr)
        // 根据id过滤重复成员
        this.userList = removeDuplicateObjects(this.userList, 'value')
      } else {
        // 数组去差集
        this.userList = getArrayDifference(this.userList, arr, 'value')
      }
      this.userIds = extractArrayIds(this.userList, 'value')
      this.selectIds = this.findNamesByIds(this.treeData, this.userIds)
      this.$refs.tree.setCheckedKeys(this.selectIds)
      this.$emit('getSelectList', this.userList)
    },

    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },

    // 选中人员单选
    checkUser(user) {
      if (user.type == 0) return false
      if (this.disabledList.includes(user.value)) {
        return this.$message.warning('不能选择此人员')
      }
      if (!this.onlyOne) {
        return
      }
      if (isInArray(this.userIds, user.value)) {
        return this.$message.warning('已选中该成员')
      }

      this.userList = []
      this.userList.push(user)
      this.userIds = extractArrayIds(this.userList, 'value')
      this.userList = removeDuplicateObjects(this.userList)
      this.$emit('getSelectList', this.userList)
      this.$refs.treePopover.doClose()
    },
    handleGlobalClick(event) {
      if (!this.$refs.treePopover.$el.contains(event.target)) {
        this.showPopover = false
        this.filterText = ''
      }
    },
    handlePopoverShow() {
      this.getTreeData()
      this.showPopover = true
    },
    handlePopoverHide() {
      this.showPopover = false
      this.filterText = ''
    },

    getTreeData() {
      this.treeData = this.$store.state.user.memberList
      this.selectIds = this.findNamesByIds(this.treeData, this.userIds)
    },
    /**
     * 递归获取某部门所有成员
     * @param {Object} data  初始数据
     * @param {Number} deptId 部门id
     * @returns {*[]}
     */
    getDepartmentDataById(data, deptId) {
      let department = data
      if (department) {
        let members = []
        // 递归函数，用于遍历子部门和用户
        function traverseChildren(children) {
          children.forEach((child) => {
            if (child.children) {
              traverseChildren(child.children)
            } else if (child.type != 0) {
              members.push(child)
            }
          })
        }
        traverseChildren([department])
        return members
      }

      return []
    },

    findNamesByIds(tree, id) {
      let ids = id.map((str) => parseInt(str))
      let result = []
      function traverse(node) {
        if (ids.includes(node.value) && node.type != 0) {
          result.push(node.id)
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
    }
  },
  beforeDestroy() {
    document.removeEventListener('click', this.handleGlobalClick)
  }
}
</script>
<style scoped lang="scss">
.avatar {
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 50%;
}
.tree-box {
  // width: 242px;
  min-height: 150px;
  position: sticky;
  padding: 24px 12px;
  z-index: 9999;
  background: #fff;
  min-width: 150px;
  border-radius: 4px;
  border: 1px solid #e6ebf5;
  .input {
    padding: 0 12px;
  }
  .custom-tree-node {
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 14px;
    color: #303133;

    .iconwenjianjia {
      color: #1890ff;
      margin-right: 6px;
    }
  }

  ::v-deep.el-tree {
    margin-top: 12px;
    max-height: 350px;
    overflow-y: auto;
    scrollbar-width: none; /* firefox */
    -ms-overflow-style: none; /* IE 10+ */
    .is-checked {
      color: #1890ff !important;
    }
    .el-tree-node__content {
      height: 32px;
      line-height: 32px;
    }
    .el-tree-node__content:hover {
      background: rgba(24, 144, 255, 0.05);
    }
  }
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
  line-height: 30px;
  outline: none;
  font-size: 13px;
  padding: 0 10px;
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
.el-icon-arrow-down {
  font-weight: 400;
  position: absolute;
  right: 10px;
  top: 8px;
}

.isChecked {
  color: #1890ff !important;
}
::v-deep .el-popper {
  padding: 0;
  margin-top: 5px;
}
</style>
<style>
.popover {
  padding: 0px !important;
}
</style>
