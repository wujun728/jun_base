<template>
  <el-dialog class="nodeTagDialog" title="标签" :visible.sync="dialogVisible" :width="isMobile ? '90%' : '50%'"
    :top="isMobile ? '20px' : '15vh'">
    <el-input v-model="tag" @keyup.native.enter="add" @keyup.native.stop @keydown.native.stop
      :disabled="tagArr.length >= max" placeholder="请按回车键添加">
    </el-input>
    <div class="tagList">
      <div class="tagItem" v-for="(item, index) in tagArr" :key="index" :style="{
        backgroundColor: generateColorByContent(item)
      }">
        {{ typeof item === 'string' ? item : item.text }}
        <div class="delBtn" @click="del(index)">
          <span class="xmind-iconfont iconshanchu"></span>
        </div>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import {
  generateColorByContent,
  isMobile
} from 'simple-mind-map/src/utils/index'
import { END_TEXT_EDIT, NODE_ACTIVE, SHOW_NODE_TAG, START_TEXT_EDIT } from '../event-constant';


export default {
  name: 'NodeTag',
  data() {
    return {
      dialogVisible: false,
      tagArr: [],
      tag: '',
      activeNodes: [],
      max: 5,
      isMobile: isMobile()
    }
  },
  watch: {
    dialogVisible(val, oldVal) {
      if (!val && oldVal) {
        this.$bus.$emit(END_TEXT_EDIT)
      }
    }
  },
  created() {
    this.$bus.$on(NODE_ACTIVE, this.handleNodeActive)
    this.$bus.$on(SHOW_NODE_TAG, this.handleShowNodeTag)
  },
  beforeDestroy() {
    this.$bus.$off(NODE_ACTIVE, this.handleNodeActive)
    this.$bus.$off(SHOW_NODE_TAG, this.handleShowNodeTag)
  },
  methods: {
    generateColorByContent,

    handleNodeActive(...args) {
      this.activeNodes = [...args[1]]
      if (this.activeNodes.length > 0) {
        let firstNode = this.activeNodes[0]
        this.tagArr = firstNode.getData('tag') || []
      } else {
        this.tagArr = []
        this.tag = ''
      }
    },

    handleShowNodeTag() {
      this.$bus.$emit(START_TEXT_EDIT)
      this.dialogVisible = true
    },

    add() {
      const text = this.tag.trim()
      if (!text) return
      this.tagArr.push(text)
      this.tag = ''
    },


    del(index) {
      this.tagArr.splice(index, 1)
    },


    cancel() {
      this.dialogVisible = false
    },


    confirm() {
      this.activeNodes.forEach(node => {
        node.setTag(this.tagArr)
      })
      this.cancel()
    }
  }
}
</script>

<style lang="scss" scoped>
.nodeTagDialog {
  .tagList {
    display: flex;
    flex-wrap: wrap;
    margin-top: 5px;

    .tagItem {
      position: relative;
      padding: 3px 5px;
      margin-right: 5px;
      margin-bottom: 5px;
      color: #fff;

      .delBtn {
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.4);
        color: #fff;
        display: flex;
        justify-content: center;
        align-items: center;
        cursor: pointer;
        visibility: hidden;
      }

      &:hover {
        .delBtn {
          visibility: visible;
        }
      }
    }
  }
}
</style>
