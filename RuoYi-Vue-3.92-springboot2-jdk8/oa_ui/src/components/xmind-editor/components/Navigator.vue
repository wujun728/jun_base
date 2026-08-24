<template>
  <div v-if="showMiniMap" class="navigatorBox" ref="navigatorBox" :style="{ width: width + 'px' }"
    @mousedown="onMousedown" @mousemove="onMousemove">
    <div class="svgBox" ref="svgBox" :style="{
      transform: `scale(${svgBoxScale})`,
      left: svgBoxLeft + 'px',
      top: svgBoxTop + 'px'
    }">
      <img :src="mindMapImg" @mousedown.prevent />
    </div>
    <div class="windowBox" :style="viewBoxStyle" :class="{ withTransition: withTransition }"
      @mousedown.stop="onViewBoxMousedown" @mousemove="onViewBoxMousemove"></div>
  </div>
</template>

<script>
import { DATA_CHANGE, MINI_MAP_VIEW_BOX_POSITION_CHANGE, NODE_TREE_RENDER_END, TOGGLE_MINI_MAP, VIEW_DATA_CHANGE } from '../event-constant';

export default {
  props: {
    mindMap: {
      type: Object
    }
  },
  data() {
    return {
      showMiniMap: false,
      timer: null,
      boxWidth: 0,
      boxHeight: 0,
      svgBoxScale: 1,
      svgBoxLeft: 0,
      svgBoxTop: 0,
      viewBoxStyle: {
        left: 0,
        top: 0,
        bottom: 0,
        right: 0
      },
      mindMapImg: '',
      width: 0,
      setSizeTimer: null,
      withTransition: true,

      listenEventMap: {
        [TOGGLE_MINI_MAP]: this.toggle_mini_map,
        [DATA_CHANGE]: this.data_change,
        [VIEW_DATA_CHANGE]: this.data_change,
        [NODE_TREE_RENDER_END]: this.data_change,
        [MINI_MAP_VIEW_BOX_POSITION_CHANGE]: this.onViewBoxPositionChange
      }
    }
  },
  mounted() {
    this.setSize()
    window.addEventListener('resize', this.setSize)

    Object.entries(this.listenEventMap)
      .forEach(([eventName, handler]) => {
        this.$bus.$on(eventName, handler);
      });

    window.addEventListener('mouseup', this.onMouseup)
  },
  destroyed() {
    window.removeEventListener('resize', this.setSize)

    Object.entries(this.listenEventMap)
      .forEach(([eventName, handler]) => {
        this.$bus.$off(eventName, handler);
      });

    window.removeEventListener('mouseup', this.onMouseup)
  },
  methods: {
    // 切换显示小地图
    toggle_mini_map(show) {
      this.showMiniMap = show
      this.$nextTick(() => {
        if (this.$refs.navigatorBox) {
          this.init()
        }
        if (this.$refs.svgBox) {
          this.drawMiniMap()
        }
      })
    },

    // 思维导图数据改变，更新小地图
    data_change() {
      if (!this.showMiniMap) {
        return
      }
      clearTimeout(this.timer)
      this.timer = setTimeout(() => {
        this.drawMiniMap()
      }, 500)
    },

    // 计算容器宽度
    setSize() {
      clearTimeout(this.setSizeTimer)
      this.setSizeTimer = setTimeout(() => {
        this.width = Math.min(window.innerWidth - 80, 370)
        this.$nextTick(() => {
          if (this.showMiniMap) {
            this.init()
            this.drawMiniMap()
          }
        })
      }, 300)
    },

    // 获取宽高
    init() {
      let { width, height } = this.$refs.navigatorBox.getBoundingClientRect()
      this.boxWidth = width
      this.boxHeight = height
    },

    // 渲染小地图
    drawMiniMap() {
      let {
        getImgUrl,
        viewBoxStyle,
        miniMapBoxScale,
        miniMapBoxLeft,
        miniMapBoxTop
      } = this.mindMap.miniMap.calculationMiniMap(this.boxWidth, this.boxHeight)
      // 渲染到小地图
      getImgUrl(img => {
        this.mindMapImg = img
      })
      this.viewBoxStyle = viewBoxStyle
      this.svgBoxScale = miniMapBoxScale
      this.svgBoxLeft = miniMapBoxLeft
      this.svgBoxTop = miniMapBoxTop
    },

    // 小地图鼠标按下事件
    onMousedown(e) {
      this.mindMap.miniMap.onMousedown(e)
    },

    // 小地图鼠标移动事件
    onMousemove(e) {
      this.mindMap.miniMap.onMousemove(e)
    },

    // 鼠标松开事件，最好绑定要window
    onMouseup(e) {
      if (!this.withTransition) {
        this.withTransition = true
      }
      if (this.mindMap.miniMap) this.mindMap.miniMap.onMouseup(e)
    },

    // 视口框的鼠标按下事件
    onViewBoxMousedown(e) {
      this.mindMap.miniMap.onViewBoxMousedown(e)
    },

    // 视口框的鼠标移动事件
    onViewBoxMousemove(e) {
      this.mindMap.miniMap.onViewBoxMousemove(e)
    },

    // 视口框的位置大小改变了，需要更新
    onViewBoxPositionChange({ left, right, top, bottom }) {
      this.withTransition = false
      this.viewBoxStyle.left = left
      this.viewBoxStyle.right = right
      this.viewBoxStyle.top = top
      this.viewBoxStyle.bottom = bottom
    }
  }
}
</script>

<style lang="scss" scoped>
.navigatorBox {
  position: absolute;
  height: 220px;
  background-color: #fff;
  bottom: 80px;
  right: 70px;
  box-shadow: 0 0 16px #989898;
  border-radius: 4px;
  border: 1px solid #eee;
  cursor: pointer;
  user-select: none;


  .svgBox {
    position: absolute;
    left: 0;
    transform-origin: left top;
  }

  .windowBox {
    position: absolute;
    border: 2px solid rgb(238, 69, 69);
    background-color: rgba(238, 69, 69, 0.2);

    &.withTransition {
      transition: all 0.3s;
    }
  }
}
</style>
