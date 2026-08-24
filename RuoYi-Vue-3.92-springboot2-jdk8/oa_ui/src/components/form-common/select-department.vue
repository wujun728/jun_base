<!-- @FileDescription: 下拉选择部门组件 -->
<template>
  <el-popover
    ref="treePopover"
    v-model="showPopover"
    placement="bottom-start"
    popper-class="popover"
    :width="isSite ? 306 : 266"
    trigger="manual"
  >
    <isFullScreen @call-parent-method="handlePopoverHide">
      <div class="tree-box">
        <div class="input">
          <el-input
            v-model="filterText"
            style="width: 218px"
            placeholder="请输入部门搜索"
            prefix-icon="el-icon-search"
            size="small"
          >
          </el-input>
        </div>

        <el-tree
          ref="tree"
          :check-strictly="true"
          :data="treeData"
          :default-checked-keys="selectIds"
          :default-expanded-keys="treeExpandData"
          :filter-node-method="filterNode"
          :indent="8"
          :props="props"
          :show-checkbox="!onlyOne"
          highlight-current
          node-key="id"
          @check-change="depCheck"
        >
          <span slot-scope="{ node, data }" class="custom-tree-node">
            <div class="flex flex-between">
              <div
                :class="{
                  isChecked: departmentIds.includes(data.id)
                }"
                class="flex"
                @click.stop="append(node, data)"
              >
                <i class="tree-icon iconfont iconwenjianjia" />
                <span class="label-text over-text">{{ data.label }}</span>
              </div>
              <div
                v-if="isSite && departmentIds.includes(data.id)"
                :class="{
                  checkedColor: data.id == is_mastartId
                }"
                class="main-department"
                @click.stop="isMastart(data)"
              >
                {{ data.id == is_mastartId ? '主部门' : '设为主部门' }}
              </div>
            </div>
          </span>
        </el-tree>
      </div>
    </isFullScreen>

    <!-- 部门数据 -->
    <template slot="reference">
      <slot name="custom"></slot>
      <div v-if="!isSlots" ref="select" class="select plan-footer-one mr10" @click="handlePopoverShow">
        <span class="el-icon-arrow-down"></span>
        <span v-if="selectList && selectList.length == 0" class="placeholder">{{ placeholder }}</span>
        <div v-if="selectList.length > 0 && !isSearch" class="flex-box">
          <span
            v-for="(item, index) in selectList"
            :key="index"
            class="el-tag el-tag--small el-tag--info el-tag--light mr10"
            @click.stop=""
          >
            {{ item.label || item.name }}
            <span v-if="isSite && item.id == is_mastartId">(主) </span>
            <i class="el-tag__close el-icon-close" @click.stop="cardTag(index)" />
          </span>
        </div>

        <!-- 以下是筛选条件展示样式 -->
        <div v-if="selectList.length > 0 && isSearch" class="flex">
          <div
            v-for="(item, index) in selectList.slice(0, 1)"
            :key="index"
            class="el-tag el-tag--small el-tag--info el-tag--light mr10 flex-search"
            @click.stop=""
          >
            <span class="left over-text"> {{ item.label || item.name }}</span>

            <i class="right el-tag__close el-icon-close" @click.stop="cardTag(0)" />
          </div>
          <el-tag v-if="selectList.length > 1" class="el-tag el-tag--small el-tag--info el-tag--light mt3">
            {{ selectList.length - 1 }}</el-tag
          >
        </div>
      </div>
    </template>
  </el-popover>
</template>
<script>
import { extractArrayIds, isInArray, removeDuplicateObjects } from '@/libs/public'
import isFullScreen from '@/components/isFullScreen/index'

export default {
  name: '',
  components: {
    isFullScreen
  },
  props: {
    // 选中人员数据
    value: {
      type: Array,
      default: () => {
        return []
      }
    },
    // 开启设为主部门
    isSite: {
      type: Boolean,
      default: false
    },
    placeholder: {
      type: String,
      default: '请选择部门'
    },
    // 只能单选一个人员
    onlyOne: {
      type: Boolean,
      default: false
    },
    isSearch: {
      type: Boolean,
      default: false
    },
    // 开启权限
    role: {
      type: Number,
      default: 0
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
        label: 'label'
      },
      showPopover: false,
      isSlots: false,
      treeHeight: '',
      filterText: '',
      selectIds: [],
      is_mastartId: 0,
      treeExpandData: [],
      departmentIds: [], // 选中部门id
      selectList: [], // 选中部门
      treeData: []
    }
  },
  computed: {},
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    },
    value(newVal, oldValue) {
      this.selectList = newVal
      this.departmentIds = extractArrayIds(this.selectList)
      this.selectIds = this.findNamesByIds(this.treeData, this.departmentIds)
      this.$refs.tree.setCheckedKeys(this.selectIds)
      if (this.isSite) {
        this.selectList.map((item) => {
          if (item.is_mastart) {
            this.is_mastartId = item.id
          }
        })
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
      this.selectList = this.value
      this.departmentIds = extractArrayIds(this.selectList)
      this.selectIds = extractArrayIds(this.selectList)

      if (this.isSite) {
        this.selectList.map((item) => {
          if (item.is_mastart) {
            this.is_mastartId = item.id
          }
        })
      }
    }
  },
  methods: {
    handleGlobalClick(event) {
      if (!this.$refs.treePopover.$el.contains(event.target)) {
        this.showPopover = false
      }
    },
    handlePopoverShow() {
      this.getTreeData()
      this.showPopover = true
    },
    handlePopoverHide() {
      this.showPopover = false
    },
    // 获取树形结构默认展开节点
    getRoleTreeRootNode(res) {
      this.treeExpandData.push(res[0]?.id)
    },

    cardTag(index) {
      this.selectList.splice(index, 1)
      this.$emit('changeMastart', this.selectList)
    },

    // 设置部门
    isMastart(key) {
      if (key.is_mastart) {
      } else {
        this.is_mastartId = key.id
      }
      this.selectList.forEach((item) => {
        if (item.id == this.is_mastartId) {
          item.is_mastart = true
        } else {
          item.is_mastart = false
        }
      })
      this.$emit('changeMastart', this.selectList)
    },

    //  选择部门多选
    depCheck(data, checked) {
      if (this.disabledList.includes(data.id)) {
        return this.$message.warning('此部门已加入其他考勤组')
      }

      //选择部门

      if (checked) {
        this.selectList.push({
          id: data.id,
          value: data.value,
          label: data.label,
          name: data.label,
          is_mastart: false,
          pid: data.pid,
          path: data.path
        })
        this.selectList = removeDuplicateObjects(this.selectList)
        if (this.isSite && this.selectList.length == 1) {
          this.isMastart(data)
        }
      } else {
        this.selectList = this.selectList.filter((item) => {
          return item.id !== data.id
        })
      }
      // 部门选中
      this.departmentIds = extractArrayIds(this.selectList)
      this.$emit('changeMastart', this.selectList)
    },

    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    //选择部门单选
    append(node, data) {
      if (this.disabledList.includes(data.id)) {
        return this.$message.warning('此部门已加入其他考勤组')
      }
      if (!this.onlyOne) {
        return false
      }
      if (isInArray(this.departmentIds, data.id)) {
        return this.$message.warning('已选中该部门')
      }

      data.is_mastart = false
      this.selectList = [
        {
          id: data.id,
          value: data.value,
          label: data.label,
          name: data.label,
          is_mastart: false,
          pid: data.pid,
          path: data.path
        }
      ]
      this.departmentIds = extractArrayIds(this.selectList)
      this.$emit('changeMastart', this.selectList)
      this.$refs.treePopover.doClose()
    },

    getTreeData() {
      this.treeData = this.$store.state.user.departmentList
      this.getRoleTreeRootNode(this.treeData)
      this.selectIds = this.findNamesByIds(this.treeData, this.departmentIds)
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
            } else {
              members.push(child)
            }
          })
        }
        traverseChildren([department])
        return members
      }

      return []
    },
    // 获取部门
    getSubDepartmentsById(departmentsData, departmentId) {
      let subDepartments = []
      function traverseDepartments(departments) {
        for (const department of departments) {
          subDepartments.push({
            id: department.id,
            value: department.value,
            label: department.label,
            name: department.label,
            is_mastart: false,
            pid: department.pid,
            path: department.path
          })
          if (department.children && department.children.length > 0) {
            traverseDepartments(department.children)
          }
        }
      }
      traverseDepartments(departmentsData)

      return subDepartments
    }
  },
  beforeDestroy() {
    document.removeEventListener('click', this.handleGlobalClick)
  }
}
</script>
<style lang="scss" scoped>
.tree-box {
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
    width: 100%;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 13px;
    color: #303133;

    .iconwenjianjia {
      color: #1890ff;
      margin-right: 6px;
    }
    .avatar {
      display: inline-block;
      width: 20px;
      height: 20px;
      border-radius: 50%;
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
.mt3 {
  margin-top: 3px;
}
.main-department {
  // width: 80px;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 14px;
  color: #1890ff;
  text-align: right;
}
.checkedColor {
  color: #909399 !important;
}
.flex-search {
  margin-top: 3px;
  display: flex;
  align-items: center;
  max-width: 80%;

  .left {
    max-width: 90%;
  }
  .right {
    margin-top: 3px;
    width: 10%;
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
  top: 10px;
}

.label-text {
  display: inline-block;
  max-width: 150px;
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
