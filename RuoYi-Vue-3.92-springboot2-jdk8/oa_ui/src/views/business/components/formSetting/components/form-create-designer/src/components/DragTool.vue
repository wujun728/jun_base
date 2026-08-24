<template>
  <div class="drag-tool" @click.stop="active" :class="{ active: state.active === id }">
    <!-- 编辑字段页面 -->
    <div class="drag-l">
      <el-tooltip effect="dark" content="拖拽" placement="top">
        <div class="drag-btn _fc-drag-btn" v-if="state.active === id && dragBtn !== false" style="cursor: move">
          <i class="iconfont icontuozhuai"></i>
        </div>
      </el-tooltip>
    </div>
    <div class="drag-r">
      <el-tooltip effect="dark" content="添加" placement="top">
        <div class="drag-btn" @click="$emit('add')" v-if="typeList.indexOf(type) == -1">
          <i class="iconfont icontianjia1"></i>
        </div>
      </el-tooltip>
      <el-tooltip effect="dark" content="复制" placement="top">
        <div class="drag-btn" @click="$emit('copy')" v-if="typeList.indexOf(type) == -1">
          <i class="iconfont iconfuzhi1"></i>
        </div>
      </el-tooltip>
      <div class="drag-btn" v-if="children" @click="$emit('addChild')">
        <i class="fc-icon icon-add-child"></i>
      </div>
      <el-tooltip effect="dark" content="删除" placement="top">
        <div class="drag-btn drag-btn-danger" @click="$emit('delete')">
          <i class="iconfont iconshanchu1"></i>
        </div>
      </el-tooltip>
    </div>
    <div class="drag-mask"></div>
    <slot name="default"></slot>
  </div>
</template>

<script>
let id = 1
export default {
  name: 'DragTool',
  inject: ['fcx'],
  props: ['dragBtn', 'children', 'unique', 'type'],
  data() {
    return {
      typeList: [
        'leaveFrom',
        'overtimeFrom',
        'outFrom',
        'refillFrom',
        'tripFrom',
        'contractPayment',
        'contractRenewal',
        'contractExpenditure',
        'issueInvoice',
        'voidedInvoice'
      ],
      id: this.unique || id++,
      state: this.fcx
    }
  },
  methods: {
    active() {
      if (this.state.active === this.id) return
      this.state.active = this.id
      this.$emit('active')
    }
  },
  beforeDestroy() {
    this.state = {}
  }
}
</script>

<style>
.drag-tool {
  position: relative;
  min-height: 20px;
  box-sizing: border-box;
  padding: 0 20px;
  /*outline: 1px dashed #2E73FF;*/
  overflow: hidden;
  word-wrap: break-word;
  word-break: break-all;
}

.drag-tool .drag-tool {
  margin: 5px;
}

.drag-tool + .drag-tool {
  margin-top: 5px;
}

.drag-tool.active {
  background: #f3f9ff;
  /*outline: 2px solid #2E73FF;*/
}

.drag-tool.active > div > .drag-btn {
  display: flex;
}

.drag-tool .drag-btn {
  display: none;
}

.drag-r {
  position: absolute;
  right: 2px;
  bottom: 2px;

  z-index: 100;
}

.drag-l {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 100;
}

.drag-btn {
  height: 18px;
  width: 18px;
  color: #fff;

  background: rgba(0, 0, 0, 0.5);
  text-align: center;
  line-height: 20px;
  padding-bottom: 1px;
  float: left;
  cursor: pointer;
  justify-content: center;
  z-index: 100;
}
._fc-drag-btn {
  background-color: #2e73ff;
}

.drag-btn + .drag-btn {
  margin-left: 2px;
}

/* .drag-btn-danger {
  background-color: #ff2e2e;
} */

.drag-btn i {
  font-size: 13px;
}

.drag-mask {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  z-index: 3;
}
</style>
