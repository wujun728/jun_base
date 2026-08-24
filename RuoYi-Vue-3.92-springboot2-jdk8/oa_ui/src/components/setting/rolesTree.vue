<!-- @FileDescription: 系统-角色权限/用户权限-菜单权限树形组件 -->
<template>
  <div class="container">
    <div class="tree-wrapper">
      <div class="tree-box">
        <div class="tree-con">
          <span class="text">菜单权限</span>
          <el-tree
            ref="tree"
            class="blue-theme"
            :data="treeData"
            show-checkbox
            node-key="value"
            :default-expand-all="true"
            :default-checked-keys="defaultCheckedKeys"
            :highlight-current="true"
            :check-on-click-node="false"
            :props="defaultProps"
            :expand-on-click-node="false"
            @check="handelCheck"
            @node-click="handelClick"
          />
        </div>
        <div class="check-box">
          <div class="check_list">
            <span class="text"> 按钮/接口权限</span>

            <div class="list">
              <el-checkbox-group v-model="checkValue">
                <el-row :gutter="20">
                  <el-col v-for="(item, index) in checkList" :key="index" :lg="24" :xl="8">
                    <el-checkbox
                      :label="item.value"
                      class="check-item"
                      :disabled="isAdmin"
                      :checked="item.is_default"
                      @change="(checked) => handelCheckChange(checked, item)"
                    >
                      {{ item.label }}
                    </el-checkbox>
                  </el-col>
                </el-row>
              </el-checkbox-group>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RolesTree',
  props: {
    treeData: {
      type: Array,
      default: () => {
        return []
      }
    },
    defaultCheckedKeys: {
      type: Array,
      default: () => {
        return []
      }
    },
    rolesList: {
      type: [Array, Object],
      default: () => {
        return []
      }
    },
    isAdmin: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    defaultCheckedKeys: {
      handler(nVal, oVal) {
        if (this.isAdmin) {
          this.$refs.tree.setCheckedKeys(nVal)
        }
      },
      deep: true
    }
  },
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'label',
        activeId: 0,
        disabled: function (data, node) {
          return data.disabled
        }
      },
      checkList: [],
      checkValue: [],
      listOne: []
    }
  },
  mounted() {
    if (this.rolesList.length > 0) {
      this.rolesList.map((item) => {
        this.checkValue.push(Number(item))
        this.listOne.push(Number(item))
      })
    }
  },
  methods: {
    handelCheck(checkedNode, checkedKeys) {
      if (checkedKeys.checkedKeys.indexOf(checkedNode.value) != -1) {
        // 节点选中
        this.checkedNodeFn(checkedNode, 2)
        this.listOne = this.checkValue
      } else {
        // 节点未选中
        this.checkedNodeFn(checkedNode, 1)
        this.listOne = this.checkValue
      }
    },
    // tree节点点击
    handelClick(data, node, element) {
      let that = this
      if (data.apis) {
        this.checkList = data.apis.map((item) => {
          return {
            ...item,
            is_default: this.listOne.some((id) => id === item.value)
          }
        })
      } else {
        that.checkList = []
      }
      setTimeout(() => {
        that.checkValue = that.listOne
      }, 300)
    },

    checkedNodeFn(data, val) {
      if (data.apis) {
        let len = data.apis.length
        if (len > 0) {
          for (let i = 0; i < len; i++) {
            if (val !== 1) {
              this.$nextTick(() => {
                data.apis[i].is_default = true
                this.checkValue.push(data.apis[i].value)
              })
            } else {
              data.apis[i].is_default = false
              const filteredArray = this.checkValue.filter((value) => value !== data.apis[i].value)
              this.checkValue = filteredArray
            }
          }
        }
      }

      if (data.children) {
        data.children.forEach((item) => {
          this.checkedNodeFn(item, val)
        })
      }
    },

    handelCheckChange(val, item) {
      if (val) {
        this.checkValue.push(item.value)
      } else {
        this.checkValue = this.checkValue.filter((val) => {
          return val != item.value
        })
      }
      this.listOne = []

      if (this.checkValue.length) {
        this.checkValue.map((item) => {
          this.listOne.push(item)
        })
      }
    },
    getNodeValue() {
      return new Promise((resolve, reject) => {
        const obj = {}
        obj.rules = this.$refs.tree.getCheckedKeys(true)
        obj.apis = this.listOne
        resolve(obj)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  padding: 0 20px;
  height: 100%;
}

.tree-wrapper {
  display: flex;
  height: 100%;
  .tree-box {
    flex: 1;
    display: flex;

    .tree-con {
      height: calc(100vh - 300px);
      // width: 260px;
      overflow-y: auto;
      border-right: 1px solid #eeeeee;
      padding-bottom: 20px;
      // padding-left: 20px;
      &::-webkit-scrollbar-thumb {
        -webkit-box-shadow: inset 0 0 6px #ccc;
      }
      &::-webkit-scrollbar {
        width: 4px !important; /*对垂直流动条有效*/
      }
    }
  }
}
.check-box {
  height: 100%;
  display: flex;
  flex: 1;
  .check_list {
    padding-left: 26px;
    overflow-y: auto;
    height: 100%;
    width: 290px;
    &::-webkit-scrollbar-thumb {
      -webkit-box-shadow: inset 0 0 6px #ccc;
    }
    &::-webkit-scrollbar {
      width: 4px !important; /*对垂直流动条有效*/
    }
  }
  .check-item {
    margin-bottom: 20px;
  }
}
.label-txt {
  font-size: 14px;
  line-height: 26px;
}
.list {
  width: 1px;
}
::v-deep .el-scrollbar__wrap {
  overflow-y: scroll;
  overflow-x: hidden !important;
}
::v-deep .el-scrollbar__thumb {
  height: 100px !important;
}
.text {
  display: inline-block;
  font-size: 14px;
  color: #333;
  font-weight: 600;
  margin: 20px 0;
}
::v-deep.el-tree-node__content {
  padding-right: 0;
}
</style>
