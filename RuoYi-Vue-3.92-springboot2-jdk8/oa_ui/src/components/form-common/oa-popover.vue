<!-- @FileDescription: 公共-全局-弹出框组件 应用举例：低代码新建字段、新建触发器 -->
<template>
  <div class="popover-box">
    <el-popover placement="bottom" width="230" trigger="click">
      <el-input
        v-if="searchShow"
        v-model="search"
        prefix-icon="el-icon-search"
        size="small"
        placeholder="请输入关键字"
        clearable
        style="width: 100%"
        class="input"
      ></el-input>

      <el-scrollbar :style="{ height: height }" wrap-class="scrollbar-wrapper">
        <div
          class="line"
          :class="searchList.length > 1 ? 'border-bottom' : ''"
          v-for="(item, index) in searchList"
          :key="index"
        >
          <div class="field-text" v-for="(i, index) in item.options" :key="index" @click="handleClick(i)">
            {{ i.label }}
            <span v-if="isValueShow"> / {{ i.value }}</span>
            <span v-if="i.message" class="field-message">({{ i.message }})</span>
          </div>
        </div>
      </el-scrollbar>
      <el-button size="small" type="primary" slot="reference">{{ title }}<i class="el-icon-arrow-down"></i></el-button>
    </el-popover>
  </div>
</template>
<script>
export default {
  name: '',
  components: {},
  props: {
    list: {
      // 数组结构：[{options:[{label: 'xxx', value: 'xxx'}] }]
      type: Array,
      default: () => {}
    },
    title: {
      // 弹出框标题
      type: String,
      default: ''
    },
    height: {
      type: String,
      default: '500px'
    },
    // value值显示隐藏
    isValueShow: {
      // 弹出框标题
      type: Boolean,
      default: true
    },
    searchShow: {
      // 搜索框显示隐藏
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      search: ''
    }
  },
  computed: {
    searchList: function () {
      let list = [
        {
          options: []
        }
      ]
      if (this.search !== '') {
        this.list.map((item) => {
          item.options.map((key) => {
            if (key.label.includes(this.search)) {
              list[0].options.push(key)
            }
          })
        })
      } else {
        list = this.list
      }
      return list
    }
  },

  methods: {
    handleClick(val) {
      this.$emit('handleClick', val)
    }
  }
}
</script>
<style scoped lang="scss">
.h32 {
  height: 32px;
}
.border-bottom {
  border-bottom: 1px solid #e8ebf2;
}
.line {
  margin-top: 10px;
  // padding-bottom: 10px;
}

.field-text {
  cursor: pointer;
  height: 32px;
  line-height: 32px;
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
}
.field-text:hover {
  background: #f7fbff;
  color: #1890ff;
}
.field-message {
  color: #909399;
  margin-left: 4px;
  font-size: 13px;
}

::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
</style>
