<template>
  <view class="notice-list">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :default-title="data.defaultTitle" :is-right="true" :right-data="data.rightIcon"
        @handleNarItem="handleNarItem"></default-nav-bar>
    </view>
    <view class="notice-content pl10">
      <scroll-view v-if="data.listData.length > 0" :scroll-top="data.scrollTop" class="notice-content-view"
        @scrolltoupper="upper" scroll-y="true">
        <view class="notice-content-list pr10">
          <uni-load-more iconType="circle" v-if="data.listLoading" :status="data.status" :show-text="false"
            :content-text="data.contentText" />
          <uni-list :border="false">
            <uni-list-item v-for="(item,index) in data.listData" :id="'items'+index" :key="'items'+index"
              :class="index < data.where.limit ? 'item-list-click' : ''">
              <template v-slot:body>
                <view class="note-list-time" @longpress.prevent="onLongItem(index)" @touchend="touchEnd"
                  @touchmove="touchMove">
                  <text>{{formatDate(item.created_at, true)}}</text>
                </view>
                <view class="notice-list" @click="noticeDetails(item,item.buttons[0],index)"
                  @longpress.prevent="onLongItem(index)" @touchend="touchEnd" @touchmove="touchMove">
                  <view class="notice-list-item">
                    <view class="notice-list-item-left">
                      <image v-if="item.image" class="image" :src="item.image" mode=""></image>
                      <view v-if="item.is_read === 0" class="unread" :class="item.image ? 'unread-1' : 'unread-2'">
                      </view>
                      <view class="notice-list-item-right-top" :class="getIsClassName(item)">{{item.title}}</view>
                    </view>
                    <view class="notice-list-item-right">
                      <view class="notice-list-item-right-bottom">{{item.message}}</view>
                    </view>
                  </view>
                  <uni-row class="item-list-button" v-if="item.buttons.length > 0">
                    <uni-col :span="20">{{item.buttons[0].title}}</uni-col>
                    <uni-col :span="4" class="text-right">
                      <text v-if="!data.selectedType.includes(item.buttons[0].action) && item.buttons[0].uni_url"
                        class="iconfont icon-jinru-copy"></text>
                    </uni-col>
                  </uni-row>
                </view>
              </template>
            </uni-list-item>
          </uni-list>
        </view>
      </scroll-view>
      <empty v-else :index="5" :title="data.emptyTitle"></empty>
    </view>
    <long-press-box ref="longPressBoxRef" :meus="forumMeus" :position="data.positionObj"
      @change="changeLong"></long-press-box>
    <global-index />
    <drop-down ref="dropDownRef" :list-data="forumMeus" @btn-click="dropDownItem"></drop-down>
  </view>
</template>

<script setup lang="ts">
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import longPressBox from './components/longPressBox.vue'
  import empty from '@/components/empty/index'
  import globalIndex from '@/components/globalIndex/index.vue'
  import message from '@/utils/message'
  import dropDown from '@/pages/forum/components/dropDown.vue'
  import { clickNavigateTo } from '@/utils/helper'
  import { messageListApi, userMessageBatchApi, userMessageDeleteApi, userMessageHandleApi } from '@/api/user'
  import type { Res, Drop, Detail } from '@/utils/typeHelper'
  import { showModal, clickSwitchTab } from '@/utils/helper'
  import { formatDate } from '@/utils/schedule'
  import { useStore } from 'vuex'
  const store = useStore()

  onLoad((e) => {
    if (e.id) {
      data.where.cate_id = e.id
      // #ifdef H5
      uni.setNavigationBarTitle({
        title: e.title
      })
      // #endif
      // #ifdef APP-PLUS
      data.defaultTitle = e.title
      // #endif
      getMessageList()
    }
  })

  const forumMeus = reactive([
    { name: '全部已读', id: 2, icon: 'icon-danchuang-quanbuyidu' },
    { name: '全部删除', id: 1, icon: 'icon-shanchu' }
  ])

  const data = reactive({
    listData: [],
    where: {
      cate_id: '',
      page: 1,
      limit: 10,
      is_read: ''
    },
    emptyTitle: '暂无消息内容～',
    // #ifdef APP-PLUS
    defaultTitle: '待办详情',
    // #endif
    scrollTop: 0,
    status: 'more',
    contentText: { contentdown: null, contentrefresh: "", contentnomore: "没有更多数据了" },
    firstLoading: false,
    loading: false,
    listLoading: false,
    positionObj: {},
    longIndex: -1,
    ifLongtap: true,
    templateType: ['business_approval'],
    urlTemplateType: ['daily_remind'],
    selectedType: ['delete', 'recall'],
    rightIcon: [
      { type: 1, icon: 'icon-gengduo1', types: 'icon' }
    ],
  })

  // 滚动到顶部触发
  const upper = () => {
    if (data.loading || !data.listLoading) return false
    data.status = 'loading'
    data.where.page++
    getMessageList()
  }

  const getIsClassName = (item : Detail) => {
    if (item.image) {
      return ''
    } else {
      if (item.is_read === 0) {
        return 'p1'
      } else {
        return 'p0'
      }
    }
  }

  const dropDownRef = ref(null)
  const handleNarItem = () : void => {
    dropDownRef.value.openDropdown()
  }
  const dropDownItem = (item : Drop) => {
    if (item.id === 1) {
      showModal('你确定要删除全部消息吗', '删除提醒').then(() => {
        messageDelete({ cate_id: data.where.cate_id }, true)
      }).catch(() => {

      })
    } else {
      showModal('你确定要全部标记已读吗',).then(() => {
        messageBatch(1, { cate_id: data.where.cate_id }, -1, true)
      }).catch(() => {

      })
    }
  }

  const getMessageList = (tab : boolean = false) : void => {
    data.loading = true
    messageListApi(data.where).then((res : Res) => {
      if (tab) data.listData = []
      data.listData.unshift(...res.data.list)
      const allPage : number = Math.ceil(res.data.messageNum / data.where.limit)
      data.listLoading = !(data.listData.length <= 0 || data.where.page >= allPage)
      if (data.listData.length > 0) {
        scrollToPage()
      }
      data.status = 'more'
      data.loading = false
    }).catch((error : Res) => {
      message.error(error.message)
      data.loading = false
      data.status = 'more'
    })
  }

  // 查看详情
  const noticeDetails = (row : Detail, item : Detail, index : number) : boolean => {
    console.log(9999, row)
    if (row.template_type === 'system_crud_type') {
      return clickNavigateTo(`/pages/module/details?id=${row.other.id}&key=${row.other.table_name_en}&&name=${row.title}`)
    }
    userMessageHandleApi(row.id, 1).then(() => {
      row.is_handle = 1
    }).catch((error : Res) => {
      message.error(error.message)
    });
    if (row.is_read === 0) {
      messageBatch(1, { ids: [row.id] }, index)
    }
    if (!item.uni_url || data.selectedType.includes(item.action)) return false
    // 设置消息跳转按钮
    store.commit('setiINoticeJumpPage', true)
    clickNavigateTo(item.uni_url)
  }

  //标记已读
  const messageBatch = (status : number, ids : object, index : number, isAll : boolean = false) : void => {
    userMessageBatchApi(status, ids).then(() => {
      if (isAll) {
        message.success('批量标记成功')
        data.where.page = 1
        getMessageList(true)
      } else {
        data.listData[index].is_read = 1
      }
    }).catch(() => {
      // message.error( error.message )
    })
  }

  const instance : ComponentInternalInstance = getCurrentInstance()
  const scrollToPage = () : void => {
    nextTick(() => {
      let height = 0
      const query = uni.createSelectorQuery().in(instance)
      if (data.firstLoading) {
        query.selectAll('.item-list-click').fields({ size: true, rect: true }, () => { })
        query.exec((datas) => {
          const res = datas[0]
          if (res.length > 0) {
            res.forEach((value : any) => {
              height += value.height
            })
          }
          data.scrollTop = height
        })
      } else {
        query.select('.notice-content-list').fields({ size: true, rect: true }, () => { })
        query.exec((datas) => {
          data.scrollTop = datas[0].height
        })
      }
      data.firstLoading = true
    })
  }


  const longPressBoxRef = ref(null)
  const onLongItem = (index : number) : void => {
    if (data.ifLongtap) {
      data.longIndex = index
      const query = uni.createSelectorQuery().in(instance)
      query.select(`#items${index}`).fields({ size: true, rect: true }, () => { })
      query.exec((datas) => {
        data.positionObj = datas[0]
        longPressBoxRef.value.openLong()
      })
    }
  }
  const touchEnd = () : void => {
    data.ifLongtap = true;
  }
  const touchMove = () : void => {
    // 手指触摸后的移动事件
    data.ifLongtap = false;
  }
  const changeLong = () : void => {
    const ids = [data.listData[data.longIndex].id]
    messageDelete(ids)
  }

  const messageDelete = (datas : object, isAll : boolean = false) : void => {
    userMessageDeleteApi(datas).then((res : Res) => {
      message.success(res.message)
      if (isAll) {
        clickSwitchTab('/pages/notice/index')
      } else {
        data.listData.splice(data.longIndex, 1)
      }
    }).catch((error : Res) => {
      message.error(error.message)
    })
  }
</script>

<style scoped lang="scss">
  .cr-position-header {
    position: sticky;
    top: 0;
    background-color: #fff;
  }

  .notice-content {
    .notice-content-view {
      height: calc(100vh - $uni-default-bar-height - var(--status-bar-height));
    }

    .notice-content-list {
      height: auto;
      padding-bottom: 20rpx;
    }

    ::v-deep .uni-list {
      background-color: $uni-default-bg;

      .uni-list-item {
        margin-bottom: 12rpx;

        &:last-of-type {
          margin-bottom: 0;
        }

        .uni-list-item__container {
          display: block;
          background-color: $uni-default-bg;
          padding: 0;

        }
      }

      .uni-list--border {
        left: auto;
        top: auto;
      }
    }

    .note-list-time {
      padding: 36rpx 0 24rpx 0;
      text-align: center;
      font-size: 24rpx;
      color: $nui-text-color-four;
    }

    .notice-list {
      width: 100%;
      padding: 30rpx 24rpx 24rpx 24rpx;
      background-color: #fff;
      border-radius: 16rpx;

      .notice-list-item {
        width: 100%;

        .notice-list-item-left {
          width: 100%;
          display: flex;
          align-items: center;
          position: relative;

          .notice-list-item-right-top {
            padding-left: 24rpx;
            font-size: $uni-font-size-default;
            font-weight: $uni-default-font-weight;
            color: $uni-text-color;

            &.p0 {
              padding-left: 0;
            }

            &.p1 {
              padding-left: 16rpx;
            }
          }

          .image {
            width: 60rpx;
            height: 60rpx;
            border-radius: 10rpx;
          }

          .unread {
            width: 12rpx;
            height: 12rpx;
            background-color: $uni-color-one;
            border-radius: 50%;
            position: absolute;

            &-1 {
              top: -6rpx;
              left: 54rpx;
            }

            &-2 {
              left: 0;
            }
          }
        }

        .notice-list-item-right {
          width: 100%;
          padding-top: 22rpx;

          .notice-list-item-right-bottom {
            margin-top: 8rpx;
            font-size: 26rpx;
            color: $nui-text-color-four;
            line-height: 1.5;
            white-space: pre-line
          }
        }
      }

      .item-list-button {
        margin-top: 30rpx;
        border-top: 1px solid #eee;
        padding-top: 24rpx;
        display: flex;
        justify-content: space-between;
        font-size: 24rpx;
        color: $uni-text-color;
        font-weight: 600;

        .iconfont {
          font-size: 24rpx;
          color: $nui-text-color-four;
          font-weight: normal;
        }
      }
    }

    .box-operation {
      position: absolute;
      right: 0;
      bottom: -30rpx;
    }
  }
</style>