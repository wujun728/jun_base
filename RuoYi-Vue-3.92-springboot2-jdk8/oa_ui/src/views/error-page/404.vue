<template>
  <div class="wscn-http404-container">
    <div class="wscn-http404">
      <div class="bullshit">
        <img class="pic-404__parent" src="@/assets/404_images/404-01.png" alt="404" />
        <div class="bullshit__headline text-center">{{ message }}</div>
        <div class="bullshit__info text-center">您可能输错了网址，或该网页已被删除、不存在等</div>

        <div class="text-center mt14">
          <!-- <router-link :to="{ path: roterPre + '/user/work' }">
            <span class="bullshit__return-home">返回首页</span>
          </router-link> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { roterPre } from '@/settings'
export default {
  name: 'Page404',
  data() {
    return {
      roterPre: roterPre
    }
  },
  computed: {
    message() {
      return '抱歉，您访问的页面出错了'
    }
  }
}
</script>

<style lang="scss" scoped>
.wscn-http404-container {
  transform: translate(-50%, -50%);
  position: absolute;
  top: 40%;
  left: 50%;
  .wscn-http404 {
    position: relative;
    width: 1200px;
    padding: 0 50px;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    .bullshit {
      position: relative;
      float: left;
      width: 300px;
      padding: 30px 0;
      overflow: hidden;
      .bullshit__headline {
        font-size: 20px;
        font-weight: 600;
        margin-top: 20px;
        color: #333333;
      }
      &__info {
        margin-top: 15px;
        color: #999999;
        font-size: 13px;
      }
      &__return-home {
        display: inline-block;
        white-space: nowrap;
        cursor: pointer;
        border: 1px solid #dcdfe6;
        color: #ffffff;
        -webkit-appearance: none;
        text-align: center;
        box-sizing: border-box;
        outline: none;
        margin: 0;
        transition: 0.1s;
        font-weight: 500;
        -moz-user-select: none;
        -webkit-user-select: none;
        -ms-user-select: none;
        padding: 12px 20px 8px;
        background-color: #1890ff;
        font-size: 13px;
        border-radius: 4px;
      }
      @keyframes slideUp {
        0% {
          transform: translateY(60px);
          opacity: 0;
        }
        100% {
          transform: translateY(0);
          opacity: 1;
        }
      }
    }
  }
}
</style>
