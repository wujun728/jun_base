<!-- @FileDescription: 公共-缺省页 -->
<template>
  <div class="default-page">
    <div
      v-if="showContent"
      class="content"
      :style="contentStyle"
    >
      <div class="content-list">
        <img :src="currentContent.url" alt="" class="img" />
        <p>{{ displayText }}</p>
      </div>
    </div>
  </div>
</template>

<script>
// 静态内容配置
const STATIC_CONTENTS = [
  { url: require('@/assets/images/newdef1.png'), text: '暂无表单数据' },
  { url: require('@/assets/images/none-008.png'), text: '暂无评论' },
  { url: require('@/assets/images/newdef2.png'), text: '暂无列表数据' },
  { url: require('@/assets/images/view.png'), text: '暂无视图' },
  { url: require('@/assets/images/empty/dk-success.png'), text: '您的问卷已经提交，感谢您的参与！' }
];

export default {
  name: 'DefaultPage',
  props: {
    index: {
      type: Number,
      default: -1,
      validator: value => value >= -1
    },
    minHeight: {
      type: [Number, String],
      default: 520
    },
    height: {
      type: String,
      default: ''
    },
    imgWidth: {
      type: String,
      default: '200px'
    }
  },
  computed: {
    lang() {
      return this.$store.getters.lang;
    },
    showContent() {
      return this.index > -1 && this.currentContent;
    },
    currentContent() {
      if (this.index < 0) return null;
      return this.contentList[this.index] || null;
    },
    displayText() {
      return this.currentContent?.text ? `${this.currentContent.text}~` : '';
    },
    contentStyle() {
      return {
        minHeight: this.height || `${this.minHeight}px`,
        '--imgWidth': this.imgWidth
      };
    },
    // 动态生成国际化内容
    i18nContents() {
      return Array.from({length: 16}, (_, i) => ({
        url: require(`@/assets/images/none-${this.getImageType(i)}.png`),
        text: this.$t(`public.message${i.toString().padStart(2, '0')}`)
      }));
    },
    contentList() {
      return [...this.i18nContents, ...STATIC_CONTENTS];
    }
  },
  methods: {
    getImageType(index) {
      const types = ['assess', 'statistics', '001', '002', '003', '004', '005', '006', '007', '007', 
                     'statistics', 'statistics', '003', '003', 'assess', '003'];
      return types[index] || '003';
    }
  }
};
</script>

<style lang="scss" scoped>
.default-page {
  display: flex;
  align-content: center;
  justify-content: center;
  
  .content {
    height: auto;
    min-height: 400px;
    width: 100%;
    text-align: center;
    position: relative;
    
    .content-list {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      
      .img {
        width: var(--imgWidth);
      }
      
      p {
        margin: 0;
        padding: 6px 0 0 0;
        font-size: 13px;
        color: #999999;
      }
    }
  }
}
</style>
