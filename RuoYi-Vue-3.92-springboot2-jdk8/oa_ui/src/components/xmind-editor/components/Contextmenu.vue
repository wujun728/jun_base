<template>
  <div class="contextmenuContainer listBox" v-if="isShow" ref="contextmenuRef"
    :style="{ left: left + 'px', top: top + 'px' }">
    <template v-if="type === 'node'">
      <div class="item" @click="handleExecCommand('INSERT_NODE', insertNodeBtnDisabled)"
        :class="{ disabled: insertNodeBtnDisabled }">
        <span class="name">插入同级节点</span>
        <span class="desc">Enter</span>
      </div>
      <div class="item" @click="handleExecCommand('INSERT_CHILD_NODE')" :class="{ disabled: isGeneralization }">
        <span class="name">插入子级节点</span>
        <span class="desc">Tab</span>
      </div>
      <div class="item" @click="handleExecCommand('INSERT_PARENT_NODE')" :class="{ disabled: insertNodeBtnDisabled }">
        <span class="name">插入父节点</span>
        <span class="desc">Shift + Tab</span>
      </div>
      <div class="item" @click="handleExecCommand('ADD_GENERALIZATION')" :class="{ disabled: insertNodeBtnDisabled }">
        <span class="name">插入概要</span>
        <span class="desc">Ctrl + G</span>
      </div>
      <div class="splitLine"></div>
      <div class="item" @click="handleExecCommand('UP_NODE')" :class="{ disabled: upNodeBtnDisabled }">
        <span class="name">上移节点</span>
        <span class="desc">Ctrl + ↑</span>
      </div>
      <div class="item" @click="handleExecCommand('DOWN_NODE')" :class="{ disabled: downNodeBtnDisabled }">
        <span class="name">下移节点</span>
        <span class="desc">Ctrl + ↓</span>
      </div>
      <div class="item" @click="handleExecCommand('EXPAND_ALL')">
        <span class="name">展开所有下级节点</span>
      </div>
      <div class="splitLine"></div>
      <div class="item danger" @click="handleExecCommand('REMOVE_NODE')">
        <span class="name">删除节点</span>
        <span class="desc">Delete</span>
      </div>
      <div class="item danger" @click="handleExecCommand('REMOVE_CURRENT_NODE')">
        <span class="name">仅删除当前节点</span>
        <span class="desc">Shift + Backspace</span>
      </div>
      <div class="splitLine"></div>
      <div class="item" @click="handleExecCommand('COPY_NODE')" :class="{ disabled: isGeneralization }">
        <span class="name">复制节点</span>
        <span class="desc">Ctrl + C</span>
      </div>
      <div class="item" @click="handleExecCommand('CUT_NODE')" :class="{ disabled: isGeneralization }">
        <span class="name">剪切节点</span>
        <span class="desc">Ctrl + X</span>
      </div>
      <div class="item" @click="handleExecCommand('PASTE_NODE')">
        <span class="name">粘贴节点</span>
        <span class="desc">Ctrl + V</span>
      </div>
      <div class="splitLine"></div>
      <div class="item" @click="handleExecCommand('REMOVE_HYPERLINK')" v-if="hasHyperlink">
        <span class="name">移除超链接</span>
      </div>
      <div class="item" @click="handleExecCommand('REMOVE_NOTE')" v-if="hasNote">
        <span class="name">移除备注</span>
      </div>
      <!-- 样式无法保留，暂时隐藏 -->
      <!-- <div class="item" @click="handleExecCommand('REMOVE_CUSTOM_STYLES')">
        <span class="name">一键去除自定义样式</span>
      </div> -->
      <div class="item" @click="handleExecCommand('EXPORT_CUR_NODE_TO_PNG')">
        <span class="name">导出该节点为图片</span>
      </div>
    </template>
    <template v-if="type === 'svg'">
      <div class="item" @click="handleExecCommand('RETURN_CENTER')">
        <span class="name">回到根节点</span>
        <span class="desc">Ctrl + Enter</span>
      </div>
      <div class="splitLine"></div>
      <div class="item" @click="handleExecCommand('EXPAND_ALL')">
        <span class="name">展开所有</span>
      </div>
      <div class="item" @click="handleExecCommand('UNEXPAND_ALL')">
        <span class="name">收起所有</span>
      </div>
      <div class="item">
        <span class="name">展开到</span>
        <span class="el-icon-arrow-right"></span>
        <div class="subItems listBox" :class="{ showLeft: subItemsShowLeft }" style="top: -10px">
          <div class="item" v-for="(item, index) in expandList" :key="item"
            @click="handleExecCommand('UNEXPAND_TO_LEVEL', false, index + 1)">
            {{ item }}
          </div>
        </div>
      </div>
      <div class="splitLine"></div>
      <div class="item" @click="handleExecCommand('RESET_LAYOUT')">
        <span class="name">一键整理布局</span>
        <span class="desc">Ctrl + L</span>
      </div>
      <div class="item" @click="handleExecCommand('FIT_CANVAS')">
        <span class="name">适应画布</span>
        <span class="desc">Ctrl + i</span>
      </div>
      <div class="item" @click="handleExecCommand(TOGGLE_ZEN_MODE)">
        <span class="name">禅模式</span>
        {{ isZenMode ? '√' : '' }}
      </div>
      <div class="splitLine"></div>
      <!-- 样式无法保存，暂时注释 -->
      <!-- <div class="item" @click="handleExecCommand('REMOVE_ALL_NODE_CUSTOM_STYLES')">
        <span class="name">一键去除所有节点自定义样式</span>
      </div> -->
      <div class="item">
        <span class="name">复制到剪贴板</span>
        <span class="el-icon-arrow-right"></span>
        <div class="subItems listBox" :class="{ showLeft: subItemsShowLeft }" style="top: -100px">
          <div class="item" v-for="item in copyTypeList" :key="item.value" @click="copyToClipboard(item.value)">
            {{ item.name }}
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import { getTextFromHtml, imgToDataUrl } from 'simple-mind-map/src/utils'
import { transformToMarkdown } from 'simple-mind-map/src/parse/toMarkdown'
import { transformToTxt } from 'simple-mind-map/src/parse/toTxt'

import { DRAW_CLICK, EXEC_COMMAND, EXPAND_BTN_CLICK, MOUSEUP, NODE_CLICK, NODE_CONTEXTMENU, SVG_MOUSEDOWN, TOGGLE_ZEN_MODE, TRANSLATE } from '../event-constant';

import { copy, setDataToClipboard, setImgToClipboard } from "../utils";

export default {
  name: 'Contextmenu',
  props: {
    mindMap: Object,
    isZenMode: Boolean
  },
  data() {
    return {
      isShow: false,
      left: 0,
      top: 0,
      node: null,
      type: '',
      isMousedown: false,
      mosuedownX: 0,
      mosuedownY: 0,
      enableCopyToClipboardApi: navigator.clipboard,
      numberType: '',
      numberLevel: '',
      subItemsShowLeft: false,

      // 子组件要监听的父组件事件，定义在这里，方便之后统一开始和取消监听
      listenEventMap: {
        [NODE_CONTEXTMENU]: this.handleSetNodeContextMenuVisible,
        [NODE_CLICK]: this.handleSetContextMenuHidden,
        [DRAW_CLICK]: this.handleSetContextMenuHidden,
        [EXPAND_BTN_CLICK]: this.handleSetContextMenuHidden,
        [SVG_MOUSEDOWN]: this.onMousedown,
        [MOUSEUP]: this.onMouseup,
        [TRANSLATE]: this.handleSetContextMenuHidden
      },

      TOGGLE_ZEN_MODE,
    }
  },
  computed: {
    expandList() {
      return [
        "一级主题",
        "二级主题",
        "三级主题",
        "四级主题",
        "五级主题",
        "六级主题"
      ]
    },
    copyTypeList() {
      const list = [
        {
          name: "JSON",
          value: 'json'
        },
        {
          name: "Markdown",
          value: 'md'
        },
        {
          name: "TXT",
          value: 'txt'
        }
      ]
      if (this.enableCopyToClipboardApi) {
        list.push({
          name: "图片",
          value: 'png'
        })
      }
      return list
    },
    insertNodeBtnDisabled() {
      return !this.node || this.node.isRoot || this.node.isGeneralization
    },
    upNodeBtnDisabled() {
      if (!this.node || this.node.isRoot || this.node.isGeneralization) {
        return true
      }
      let isFirst =
        this.node.parent.children.findIndex(item => {
          return item === this.node
        }) === 0
      return isFirst
    },
    downNodeBtnDisabled() {
      if (!this.node || this.node.isRoot || this.node.isGeneralization) {
        return true
      }
      let children = this.node.parent.children
      let isLast =
        children.findIndex(item => {
          return item === this.node
        }) ===
        children.length - 1
      return isLast
    },
    isGeneralization() {
      return this.node.isGeneralization
    },
    hasHyperlink() {
      return !!this.node.getData('hyperlink')
    },
    hasNote() {
      return !!this.node.getData('note')
    }
  },
  created() {
    Object.entries(this.listenEventMap)
      .forEach(([eventName, handler]) => {
        this.$bus.$on(eventName, handler);
      });
  },
  beforeDestroy() {
    Object.entries(this.listenEventMap)
      .forEach(([eventName, handler]) => {
        this.$bus.$off(eventName, handler);
      });
  },
  methods: {

    // 计算右键菜单元素的显示位置
    getShowPosition(x, y) {
      const rect = this.$refs.contextmenuRef.getBoundingClientRect()
      if (x + rect.width > window.innerWidth) {
        x = x - rect.width - 20
      }
      this.subItemsShowLeft = x + rect.width + 150 > window.innerWidth
      if (y + rect.height > window.innerHeight) {
        y = window.innerHeight - rect.height - 10
      }
      return { x, y }
    },

    // 节点右键显示
    handleSetNodeContextMenuVisible(e, node) {
      this.type = 'node'
      this.isShow = true
      this.node = node
      const number = this.node.getData('number')
      if (number) {
        this.numberType = number.type || 1
        this.numberLevel = number.level === '' ? 1 : number.level
      }
      this.$nextTick(() => {
        const { x, y } = this.getShowPosition(e.clientX + 10, e.clientY + 10)
        this.left = x
        this.top = y
      })
    },

    // 鼠标按下事件
    onMousedown(e) {
      if (e.which !== 3) {
        return
      }
      this.mosuedownX = e.clientX
      this.mosuedownY = e.clientY
      this.isMousedown = true
    },

    // 鼠标松开事件
    onMouseup(e) {
      if (!this.isMousedown) {
        return
      }
      this.isMousedown = false
      if (
        Math.abs(this.mosuedownX - e.clientX) > 3 ||
        Math.abs(this.mosuedownY - e.clientY) > 3
      ) {
        this.handleSetContextMenuHidden()
        return
      }
      this.show2(e)
    },

    // 画布右键显示
    show2(e) {
      this.type = 'svg'
      this.isShow = true
      this.$nextTick(() => {
        const { x, y } = this.getShowPosition(e.clientX + 10, e.clientY + 10)
        this.left = x
        this.top = y
      })
    },

    // 隐藏
    handleSetContextMenuHidden() {
      this.isShow = false
      this.left = -9999
      this.top = -9999
      this.type = ''
      this.node = ''
      this.numberType = ''
      this.numberLevel = ''
    },

    // 执行命令
    handleExecCommand(key, disabled, ...args) {
      if (disabled) {
        return
      }
      switch (key) {
        case 'COPY_NODE':
          this.mindMap.renderer.copy()
          break
        case 'CUT_NODE':
          this.mindMap.renderer.cut()
          break
        case 'PASTE_NODE':
          this.mindMap.renderer.paste()
          break
        case 'RETURN_CENTER':
          this.mindMap.renderer.setRootNodeCenter()
          break
        case TOGGLE_ZEN_MODE:
          this.$bus.$emit(TOGGLE_ZEN_MODE);
          break
        case 'FIT_CANVAS':
          this.mindMap.view.fit()
          break
        case 'REMOVE_HYPERLINK':
          this.node.setHyperlink('', '')
          break
        case 'REMOVE_NOTE':
          this.node.setNote('')
          break
        case 'EXPORT_CUR_NODE_TO_PNG':
          this.mindMap.export(
            'png',
            true,
            getTextFromHtml(this.node.getData('text')),
            false,
            this.node
          )
          break
        case 'EXPAND_ALL':
          this.$bus.$emit(EXEC_COMMAND, key, this.node.uid)
          break
        default:
          this.$bus.$emit(EXEC_COMMAND, key, ...args)
          break
      }
      this.handleSetContextMenuHidden()
    },

    // 复制到剪贴板
    async copyToClipboard(type) {
      try {
        this.handleSetContextMenuHidden()
        let data
        let str
        switch (type) {
          case 'json':
            data = this.mindMap.getData(true)
            str = JSON.stringify(data)
            break
          case 'md':
            data = this.mindMap.getData()
            str = transformToMarkdown(data)
            break
          case 'txt':
            data = this.mindMap.getData()
            str = transformToTxt(data)
            break
          case 'png':
            const png = await this.mindMap.export('png', false)
            const blob = await imgToDataUrl(png, true)
            setImgToClipboard(blob)
            break
          default:
            break
        }
        if (str) {
          if (this.enableCopyToClipboardApi) {
            setDataToClipboard(str)
          } else {
            copy(str)
          }
        }
        this.$message.success("复制成功")
      } catch (error) {
        console.log(error)
        this.$message.error("复制失败")
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.listBox {
  width: 250px;
  background: #fff;
  box-shadow: 0 4px 12px 0 hsla(0, 0%, 69%, 0.5);
  border-radius: 4px;
  padding-top: 16px;
  padding-bottom: 16px;


}

.contextmenuContainer {
  position: fixed;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #1a1a1a;

  .splitLine {
    width: 95%;
    height: 1px;
    background-color: #e9edf2;
    margin: 2px auto;
  }

  .item {
    position: relative;
    height: 28px;
    padding: 0 16px;
    cursor: pointer;
    display: flex;
    justify-content: space-between;
    align-items: center;

    &.danger {
      color: #f56c6c;
    }

    &:hover {
      background: #f5f5f5;

      .subItems {
        visibility: visible;
      }
    }

    &.disabled {
      color: grey;
      cursor: not-allowed;
      pointer-events: none;

      &:hover {
        background: #fff;
      }
    }

    .name {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .desc {
      color: #999;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .subItems {
      position: absolute;
      left: 100%;
      visibility: hidden;
      width: 150px;
      cursor: auto;

      &.showLeft {
        left: -150px;
      }
    }
  }
}
</style>
