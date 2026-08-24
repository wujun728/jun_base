<!-- @FileDescription: 选择图标组件 例：菜单管理选择图标 -->
<template>
  <div class="iconBox">
    <div class="iconBox">
      <el-input
        ref="search"
        v-model="iconVal"
        placeholder="输入关键字搜索"
        clearable
        style="width: 300px"
        @input="upIcon(iconVal)"
      />
      <div class="icons-container">
        <div class="grid">
          <div v-for="(item, index) of list" :key="index" @click="handleClipboard(item.icon)">
            <div class="icon-item">
              <i :class="[iconfont, item.icon]"></i>
              <span>{{ item.name }}</span>
            </div>
          </div>
          <div v-for="(css, index) of elementIconCss" :key="css" @click="handleClipboard(css)">
            <div class="icon-item">
              <i :class="css"></i>
              <span>{{ css.slice(8) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import iconfontIcons from '../../libs/iconfont-icons'

export default {
  name: 'Index',
  data() {
    return {
      iconVal: '',
      modals2: false,
      list: iconfontIcons,
      iconfont: 'iconfont',
      elementIconCssInit: [],
      elementIconCss: []
    }
  },
  created() {
    this.generateElementIconCode()
  },
  methods: {
    async generateElementIconCode() {
      const cssString = (await import('!!raw-loader!element-ui/packages/theme-chalk/src/icon.scss')).default;
      const regex = /(el-icon-[a-zA-Z0-9-]+):before\s*{/g;
      let match;
      const classNames = [];

      while ((match = regex.exec(cssString)) !== null) {
        // match[1] 是我们的整个类名
        classNames.push(match[1]);
      }
      this.elementIconCss = this.elementIconCssInit = classNames;
    },
    handleClipboard(n) {
      this.iconChange(n)
    },
    // 搜索
    upIcon(n) {
      this.list = iconfontIcons.filter(i => i.name.includes(n));
      this.elementIconCss = this.elementIconCssInit.filter(i => i.slice(8).includes(n));
    },
    iconChange(n) {
      /* eslint-disable */
      form_create_helper.set(this.$route.query.field, n)
      form_create_helper.close('icon')
    }
  }
}
</script>

<style lang="scss" scoped>
.iconBox {
  width: 100%;
  height: 100%;
}
.icons-container {
  width: 100%;
  margin: 10px 20px 0;
  overflow: hidden;

  .grid {
    width: 100%;
    position: relative;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  }

  .icon-item {
    margin: 10px 20px;
    text-align: center;
    width: 100px;
    float: left;
    font-size: 20px;
    color: #24292e;
    cursor: pointer;

    [class^='el-icon-'],
    .iconfont {
      font-size: 30px;
    }
  }

  span {
    display: block;
    font-size: 14px;
    margin-top: 10px;
  }

  .disabled {
    pointer-events: none;
  }
}
</style>
