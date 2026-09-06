<template>
  <div class="background-animation">
    <div v-for="i in 10" :key="i" class="floating-shape"></div>
  </div>
</template>
<style lang="scss" scoped>
@use 'sass:list';
@use 'sass:map';

/* 背景动画元素 */
.background-animation {
  position: absolute;
  inset: 0;
  z-index: 0;
  pointer-events: none;

  .floating-shape {
    position: absolute;
    width: 80px;
    height: 80px;
    border-radius: 50%;
    background: var(--el-color-primary-light-8);
    animation: float 15s infinite linear;

    @keyframes float {
      0% {
        transform: translateY(0) scale(1);
        opacity: 0.6;
      }

      50% {
        transform: translateY(-40px) scale(1.2);
        opacity: 0.3;
      }

      100% {
        transform: translateY(0) scale(1);
        opacity: 0.6;
      }
    }

    // 使用Sass循环简化浮动形状配置
    $shapes: (
      (top: 10%, left: 10%, w: 60px, h: 60px, delay: 0s, duration: 20s),
      (top: 20%, right: 20%, w: 100px, h: 100px, delay: 2s, duration: 22s),
      (bottom: 15%, left: 15%, w: 70px, h: 70px, delay: 4s, duration: 18s),
      (bottom: 25%, right: 10%, w: 50px, h: 50px, delay: 6s, duration: 25s),
      (top: 50%, left: 5%, w: 40px, h: 40px, delay: 8s, duration: 30s),
      (top: 40%, right: 5%, w: 90px, h: 90px, delay: 10s, duration: 28s),
      (top: 70%, left: 30%, w: 55px, h: 55px, delay: 12s, duration: 24s),
      (bottom: 40%, right: 25%, w: 65px, h: 65px, delay: 14s, duration: 26s),
      (top: 30%, left: 40%, w: 75px, h: 75px, delay: 16s, duration: 29s),
      (bottom: 10%, right: 40%, w: 45px, h: 45px, delay: 18s, duration: 27s)
    );

  @for $i from 1 through list.length($shapes) {
    $shape: list.nth($shapes, $i);

    &:nth-child(#{$i}) {
      @each $key, $value in $shape {
        @if $key ==top or $key ==bottom or $key ==left or $key ==right {
          #{$key}: #{$value};
        }
      }

      width: map.get($shape, w);
      height: map.get($shape, h);
      animation-delay: map.get($shape, delay);
      animation-duration: map.get($shape, duration);
    }
  }
}
}
</style>