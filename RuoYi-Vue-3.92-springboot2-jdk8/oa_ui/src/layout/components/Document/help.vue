<!-- 右侧帮助文档模块 -->
<template>
    <div class="box">
      <div class="title">
        <span class="text">帮助中心 </span>
        <span class="iconfont iconguanbi1" @click="closeHelp" />
      </div>
      <iframe :src="url" class="iframe"></iframe>

      <!-- 二维码组件 -->
      <payment ref="payment" :from-data="payData"></payment>
      <file-temp ref="fileTemp" :from-data="fileTemp" />
    </div>
</template>

<script>
import file from '@/utils/file'
import Vue from 'vue'
Vue.use(file)
import { getAccount } from '@/utils/format'
export default {
  name: 'CrmebOaEntHelp',
  components: {
    payment: () => import('@/components/form-common/dialog-payment'),
    fileTemp: () => import('@/views/user/cloudfile/components/fileTemp')
  },
  props: ['helpShow'],

  data() {
    return {
      url: '',
      word: '',
      Loading: false,
      payData: {},
      fileTemp: {
        type: 1,
        title: '查看更多文档',
        width: '820px'
      },
      wordList: [],
      info: {}
    }
  },
  watch: {
    $route(val, from) {
      if (val.path !== from.path) {
        this.wordList = JSON.parse(localStorage.getItem('navTitle'))
        let keyWord = this.wordList.join('/')
        this.word = getAccount(keyWord)
        let uniquedId = JSON.parse(localStorage.getItem('enterprise')).uniqued
        let accountNum = getAccount(JSON.parse(localStorage.getItem('userInfo')).phone, uniquedId)
        this.url = `https://doc.tuoluojiang.com/files/search?account=${accountNum}&uniqued=${uniquedId}&word=${this.word}`
      }
    },
    helpShow(val, oldVal) {
      if (val) {
        this.wordList = JSON.parse(localStorage.getItem('navTitle'))
        let keyWord = this.wordList.join('/')
        this.word = getAccount(keyWord)
        let uniquedId = JSON.parse(localStorage.getItem('enterprise')).uniqued
        let accountNum = getAccount(JSON.parse(localStorage.getItem('userInfo')).phone, uniquedId)
        this.url = `https://doc.tuoluojiang.com/files/search?account=${accountNum}&uniqued=${uniquedId}&word=${this.word}`
      }
    }
  },

  methods: {
    closeHelp() {
      this.$emit('closeHelp')
    },
    
  }
}
</script>

<style lang="scss" scoped>
.pointer {
  cursor: pointer;
}
.iframe {
  width: 100%;
  height: 100vh;
  border: none;

  ::v-deep .help-box {
    min-width: none;
  }
}
.mt4 {
  margin-top: 4px;
}

.template-box {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}
.mt0 {
  margin-top: 0;
}
.yuan {
  display: inline-block;
  flex-shrink: 0;
  width: 4px;
  height: 4px;
  background: #1890ff;
  border-radius: 50%;
  margin-right: 10px;
  margin-top: 8px;
}
@keyframes fadenum {
  0% {
    width: 0px;
  }
  100% {
    width: 360px;
  }
}

.box {
  z-index: 70;
  position: fixed;
  right: 0;
  top: 0;
  width: 0px;
  padding: 18px 0;
  background-color: #fff;
  box-shadow: 4px 4px 4px 4px rgba(0, 0, 0, 0.06);
  animation: fadenum 0.5s;
  animation-fill-mode: forwards;

  .title {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    padding: 0 30px;
    .text {
      font-size: 18px;
      font-family: PingFang SC-Semibold, PingFang SC;
      font-weight: 600;
      color: #333333;
    }
    .iconguanbi1 {
      color: #999;
      font-size: 12px;
    }
  }
  .enter {
    cursor: pointer;
    font-size: 14px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: rgba(24, 144, 255, 0.851);
    margin-top: 25px;
    display: flex;
    align-items: flex-end;
    .iconjinru {
      margin-top: 4px;
      font-size: 12px;
    }
  }

  .help-center {
    font-size: 13px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: #606266;
    margin-top: 22px;
    margin-bottom: 20px;
  }
  .template {
    cursor: pointer;
    position: relative;
    border-radius: 6px;
    width: 140px;
    height: 158px;
    border: 1px solid #eeeeee;
    margin-bottom: 20px;
    padding-bottom: 0;

    .img {
      display: block;
      width: 100%;
      height: 75px;
      object-fit: cover;
      border-radius: 6px 6px 0px 0px;
    }
    .name {
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      font-size: 13px;
      font-family: PingFang SC-Regular, PingFang SC;
      font-weight: 400;
      color: #606266;
      line-height: 18px;
      margin: 8px 9px 9px 9px;
    }
  }
  .template .box-y {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.4);
    display: none;

    .look {
      margin-top: 104px;
      margin-left: 100px;
      width: 100px;
      height: 32px;
      background: #ffffff;
      border-radius: 2px;
      line-height: 32px;
      text-align: center;
      font-size: 16px;
      font-family: PingFang SC-Regular, PingFang SC;
      font-weight: 400;
      color: #1890ff;
      display: none;
    }
  }

  .template:hover .box-y {
    cursor: pointer;
    display: block;
  }
  .template:hover .look {
    display: block;
  }

  .footer {
    width: 100%;
    position: absolute;
    bottom: 0px;
    // margin: 0 4px;
    .left {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }
    .flex-use {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 6px;
    }
    .icon {
      // margin-left: 4px;
      font-size: 12px;
      font-weight: 600;
      color: #f5222d;
    }
    .num {
      font-size: 14px;
      font-family: PingFang SC-Semibold, PingFang SC;
      font-weight: 600;
      color: #f5222d;
    }
    .use {
      font-size: 13px;
      font-family: PingFang SC-Regular, PingFang SC;
      font-weight: 400;
      color: #909399;
      // margin-left: 6px;
    }
    .iconxiazai {
      font-size: 14px;
      color: rgba(24, 144, 255, 0.851);
    }
  }
}

.normal-page {
  overflow: scroll;
  padding-bottom: 120px;
}
.default {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 200px;

  .img {
    display: block;
    width: 200px;
    height: 150px;
  }
  .text {
    font-size: 13px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: #999999;
  }
}

.s-box {
  position: absolute;
  left: 0;
  width: 40px;
  height: 20px;
  background: #ff9900;
  border-radius: 6px 0px 6px 0px;
  font-size: 13px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #ffffff;
  line-height: 20px;
  text-align: center;
}
.f-box {
  background: #1890ff !important;
}
.lh21 {
  text-align: left;
  cursor: pointer;
  line-height: 21px;
}

.lh20::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 4px;
  background: #ff9900;
  border-radius: 50%;
  margin-right: 10px;
  margin-bottom: 2px;
}
// .lh21::before {
//   content: '';
//   display: inline-block;
//   width: 4px;
//   height: 4px;
//   background: #1890ff;
//   border-radius: 50%;
//   margin-right: 10px;
//   margin-bottom: 2px;
// }
.top-icon {
  position: absolute;
  bottom: 0;
  margin-top: 5px;
  width: 100%;
  height: 2px;
  border-radius: 0 0 6px 6px;
}
.normal-page::-webkit-scrollbar {
  height: 0;
  width: 0;
}
.articles-flex {
  margin-bottom: 22px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 15px;
  font-family: PingFang SC-Medium, PingFang SC;
  font-weight: 500;
  color: #303133;
}
.articles-right {
  cursor: pointer;
  font-size: 14px !important;
  font-weight: 400;
  color: rgba(24, 144, 255, 0.851);
}
</style>
