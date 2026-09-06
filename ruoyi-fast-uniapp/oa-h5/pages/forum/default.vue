<template>
  <view>
    <!-- #ifdef APP-PLUS -->
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :index="1" :is-right="false"></default-nav-bar>
    </view>
    <!-- #endif -->
    <view class="default plr10">
      <view class="title"><text v-if="data.isLoading && data.hotData.includes(data.default.id)"
          class="iconfont icon-rebang default-error"></text>{{data.default.title}}
      </view>
      <uni-row class="caption display-align">
        <uni-col :span="15" class="caption-left">
          <view class="bottom display-align">
            <view class="bottom-left line1">
              {{data.default.author}}
            </view>
            <view class="bottom-right line1">
              {{data.default.types === 0 ? '原创 ': '转载 '}}
              {{data.default.visit}}浏览
            </view>
          </view>

        </uni-col>
        <uni-col :span="9" class="text-right">
          <uni-dateformat format="yyyy/MM/dd hh:mm" :date="data.default.created_at"></uni-dateformat>
        </uni-col>
      </uni-row>

      <view class="content">
        <view v-html="data.content"></view>
        <!-- <mp-html :content="data.content" :lazy-load="true" :selectable="true" /> -->
      </view>

      <view class="forum-button plr10" v-if="data.isLoading">
        <view class="forum-button-con">
          <view class="forum-button-item">
            <view :class="data.default.is_support === 1 ? 'default-error': ''" @click="getArticleSupport(data.default)">
              <text class="iconfont "
                :class="data.default.is_support === 1 ? 'icon-shequ-dianzan-yidian': 'icon-shequ-dianzan'"></text>
              <text>{{data.default.support}}</text>
            </view>
          </view>
          <view class="forum-button-item">
            <view :class="data.default.is_collect === 1 ? 'default-warning': ''"
              @click="getArticleCollect(data.default)">
              <text class="iconfont"
                :class="data.default.is_collect === 1 ? 'icon-shequ-shoucang-yishoucang' : 'icon-shequ-shoucang1'"></text>
              <text>{{data.default.collect}}</text>
            </view>
          </view>
          <view class="forum-button-item" @click="getArticleShare(data.default)">
            <view>
              <text class="iconfont icon-shequ-fenxiang1"></text>
              <!-- <text>0</text> -->
            </view>
          </view>
        </view>
      </view>
    </view>
    <global-index />
    <loginPop ref="loginRef" @loginOk='loginOk'></loginPop>
  </view>
</template>

<script setup lang="ts">
  import loginPop from './components/loginPop'
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import globalIndex from '@/components/globalIndex/index.vue'
  import message from '@/utils/message'
  import { articleInfoApi, articleCollectApi, articleSupportApi, knowUserInfoApi } from '@/api/forum'
  import type { Res, GetType, Detail } from '@/utils/typeHelper'
  import { useStore } from 'vuex'
  const store = useStore()
  const loginRef = ref(null)
  onLoad((e : GetType) => {
    if (e.id) {
      getArticleInfo(e.id)
    }
  })

  const data = reactive({
    default: <any>{},
    newIsShowMax: 3,
    hotData: [],
    content: '',
    isLoading: false
  })

  // 获取企业动态详情
  const getArticleInfo = (id : number) => {
    articleInfoApi(id).then((res : Res) => {
      data.default = res.data || {}
      if (res.data.content) {
        data.content = res.data.content.replace(/<img/gi, '<img style="max-width:100%;height:auto" ')
      }
      data.hotData = res.data.hot
      data.isLoading = true
    }).catch((error : Res) => {
      message.error(error.message)
    })
  }


  // 文章收藏与取消收藏
  const getArticleCollect = (row : Detail) : void => {
    if (!store.state.app.forumToken) {
      loginRef.value.inputDialogToggle()
    } else {
      const datas = {
        id: row.id,
        status: row.is_collect === 0 ? 1 : 0
      }
      articleCollectApi(datas).then(() => {
        if (row.is_collect === 0) {
          message.success('收藏成功')
        } else {
          message.success('取消收藏')
        }
        getArticleInfo(row.id)
      }).catch((error : Res) => {
        message.error(error.message)
      })
    }
  }

  // 文章点赞与取消点赞
  const getArticleSupport = (row : Detail) : void => {
    if (!store.state.app.forumToken) {
      loginRef.value.inputDialogToggle()
    } else {
      const datas = {
        id: row.id,
        status: row.is_support === 0 ? 1 : 0
      }
      articleSupportApi(datas).then(() => {
        if (row.is_support === 0) {
          message.success('点赞成功')
        } else {
          message.success('取消点赞')
        }
        getArticleInfo(row.id)
      }).catch((error : Res) => {
        message.error(error.message)
      })
    }

  }

  const getArticleShare = (e) => {
    message.error('分享制作中')
    const oInput = document.createElement('input')
    let value = ''
    value = `https://tuoluojiang.com/mobile/detail/${e.id}`
    oInput.value = value
    document.body.appendChild(oInput)
    oInput.select()
    document.execCommand('Copy')
    oInput.style.display = 'none'
    document.body.removeChild(oInput)
    message.success('复制分享链接成功，请前去粘贴使用')

  }
</script>
<style>
  page {
    background-color: #fff;
  }
</style>

<style scoped lang="scss">
  .default {
    // #ifdef APP-PLUS
    padding-top: calc($uni-default-bar-height + var(--status-bar-height));
    // #endif
    padding-bottom: 86rpx;
    padding-bottom: calc(86px + constant(safe-area-inset-bottom)); ///兼容 IOS<11.2/
    padding-bottom: calc(86rpx + env(safe-area-inset-bottom)); ///兼容 IOS>11.2/

    .title {
      padding-top: 28rpx;
      font-weight: $uni-default-font-weight;
      font-size: 40rpx;
      color: $uni-text-color;
      line-height: 60rpx;

      .iconfont {
        font-size: 40rpx;
        margin-right: 10rpx;
      }
    }

    .caption {
      padding-top: 16rpx;
      color: $nui-text-color-four;
      font-size: 26rpx;
      line-height: 1.5;
      font-weight: 400;

      .caption-left {
        .top {
          padding-bottom: 10rpx;

          .iconfont {
            font-size: 32rpx;
            padding-right: 16rpx;

            &:last-of-type {
              padding-right: 0;
            }
          }
        }

        .bottom {
          padding-top: 10rpx;

          .bottom-left {
            max-width: 50%;
          }

          .bottom-right {
            max-width: 50%;
            padding-left: 8rpx;
          }
        }
      }

      .item-icon {
        color: $nui-text-color-four;

        .iconfont {
          font-size: 32rpx;
          padding-bottom: 18rpx;
        }
      }
    }

    .content {
      width: 100%;
      margin: 40rpx 0;
      font-size: 30rpx;
      line-height: 60rpx;
      color: $uni-article-detail-color;
    }

    .forum-button {
      border-top: solid 1px #ebeef5;
      width: 100%;
      height: 86rpx;
      height: calc(86px + constant(safe-area-inset-bottom)); ///兼容 IOS<11.2/
      height: calc(86rpx + env(safe-area-inset-bottom)); ///兼容 IOS>11.2/
      padding-bottom: constant(safe-area-inset-bottom); ///兼容 IOS<11.2/
      padding-bottom: env(safe-area-inset-bottom); ///兼容 IOS>11.2/
      background-color: #fff;
      position: fixed;
      left: 0;
      bottom: 0;
      right: 0;

      .forum-button-con {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .forum-button-item {
          width: 33.33%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 28rpx;

          .iconfont {
            font-size: 36rpx;
            margin-right: 8rpx;
            margin-top: -2rpx;
          }
        }
      }
    }
  }
</style>