<template>
  <div>
    <el-popover placement="bottom" width="80" trigger="manual" v-model="showPopover" popper-class="time-popover">
      <div v-for="(item, index) in list.length > 0 ? list : crudList" :key="index" class="item-box"
        @click="rowFn(item)">
        {{ item.label }}
      </div>
      <template #reference>
        <div @click="showPopover = true">
          <slot>
            <el-button type="text" size="small" v-if="!icon">批量设置</el-button>
            <span v-else class="iconfont iconxitong-xitongshezhi-cebian"></span>
          </slot>
        </div>
      </template>
    </el-popover>
  </div>
</template>
<script>
export default {
  name: '',
  props: {
    icon: {
      type: Boolean,
      default: false
    },
    list: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      showPopover: false,
      crudList: [
        {
          value: 0,
          label: '不允许'
        },
        {
          value: 1,
          label: '仅本人'
        },
        {
          value: 5,
          label: '直属下级'
        },
        {
          value: 2,
          label: '本部门'
        },

        {
          value: 4,
          label: '全部数据'
        }
      ]
    }
  },
  mounted() {
    window.addEventListener('click', this.handleClosePopover);
  },
  destroyed() {
    window.removeEventListener('click', this.handleClosePopover);
  },
  methods: {
    handleClosePopover(e) {
      if (this.$el.contains(e.target) || !this.showPopover) return;
      this.showPopover = false
    },
    rowFn(item) {
      this.$emit('handClick', item)
      this.showPopover = false
    }
  }
}
</script>
<style scoped lang="scss">
.iconxitong-xitongshezhi-cebian {
  color: #c8c8c8;
  line-height: 23px;
  font-size: 12px;
  cursor: pointer;
  margin-left: 5px;
}

.item-box {
  cursor: pointer;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  height: 32px;
  line-height: 32px;
  padding: 0 14px;
}

.item-box:hover {
  background: #f2f3f5;
}
</style>
<style>
.time-popover {
  padding: 0;
}
</style>
