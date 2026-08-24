<template>
  <div class="countContainer">
    <div class="item">
      <span class="name">字数</span>
      <span class="value">{{ words }}</span>
    </div>
    <div class="item">
      <span class="name">节点</span>
      <span class="value">{{ num }}</span>
    </div>
  </div>
</template>

<script>
import { DATA_CHANGE } from '../event-constant';


let countEl = document.createElement('div')

export default {
  name: 'Count',
  props: {
    mindMap: {
      type: Object
    }
  },
  data() {
    return {
      textStr: '',
      words: 0,
      num: 0
    }
  },
  created() {
    this.$bus.$on(DATA_CHANGE, this.onDataChange)
    if (this.mindMap) {
      this.onDataChange(this.mindMap.getData())
    }
  },
  beforeDestroy() {
    this.$bus.$off(DATA_CHANGE, this.onDataChange)
  },
  methods: {
    onDataChange(data) {
      this.textStr = ''
      this.words = 0
      this.num = 0
      this.walk(data)
      countEl.innerHTML = this.textStr
      this.words = countEl.textContent.length
    },

    walk(data) {
      if (!data) return
      this.num++
      this.textStr += String(data.data.text) || ''
      if (data.children && data.children.length > 0) {
        data.children.forEach(item => {
          this.walk(item)
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.countContainer {
  padding: 0 12px;
  position: fixed;
  left: 20px;
  bottom: 20px;
  background: hsla(0, 0%, 100%, 0.8);
  border-radius: 2px;
  opacity: 0.8;
  height: 22px;
  line-height: 22px;
  font-size: 12px;
  display: flex;

  .item {
    color: #555;
    margin-right: 15px;

    &:last-of-type {
      margin-right: 0;
    }

    .name {
      margin-right: 5px;
    }
  }
}

@media screen and (max-width: 740px) {
  .countContainer {
    display: none;
  }
}
</style>
