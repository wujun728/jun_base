<template>
  <view class="content">
    <view class="cr-position-header">
      <default-nav-bar :defaultTitle="data.title" :backgroundColor="data.backgroundColor" :color="`#fff`"
        :is-right="true" :right-data="data.rightIcon" @handleNarItem="handleNarItem"></default-nav-bar>
      <!-- 选项菜单 -->
    </view>
    <view class="examine-content">
      <view class="nav">
        <!-- 选项卡水平方向滑动，scroll-with-animation是滑动到下一个选项时，有一个延时效果 -->
        <scroll-view class="tab-scroll" scroll-x="true" scroll-with-animation :scroll-left="scrollLeft">
          <view class="tab-scroll_box">
            <!-- 选项卡类别列表 -->
            <view class="tab-scroll_item" v-for="(item, index) in data.category" :key="index"
              :class="{ active: data.isActive == index }" @click="chenked(index, item)">
              {{ item.table_name }}
            </view>
          </view>
        </scroll-view>
        <!-- 内容 -->

        <view v-if="data.isActive == 0" class="p10"
          :class="data.info.crudInfo&&data.info.crudInfo.show_comment?'pb60':''">
          <basicInfo :list="data.infoList"></basicInfo>
          <!-- 评论 -->
          <comment v-if="data.info.crudInfo&&data.info.crudInfo.show_comment" :list="data.commentList"
            :info="data.info.crudInfo" @saveReplay="saveReplay" @deleteReplyFn="deleteReplyFn"></comment>
        </view>
        <view v-if="data.isActive != 0">
          <Item :info="data.tableInfo" :type="`detail`" :table-data="data.tableData" :keyName="data.keyName"
            :tableName="data.tablename" @selectFn="selectFn"></Item>
        </view>
      </view>
    </view>
    <drop-down ref="dropDownRef" :fixRight="data.fixRight" :list-data="data.forumMeus"
      @btn-click="dropDownItem"></drop-down>
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index'
  import dropDown from '@/pages/forum/components/dropDown.vue'
  import basicInfo from './components/basicInfo.vue'
  import comment from './components/comment.vue'
  import message from '@/utils/message'
  import Item from './components/item.vue'
  import {
    crudModuleInfoApi,
    crudModuleListApi,
    crudModuleDelApi,
    crudModuleFindApi,
    crudModuleCommentListDataApi,
    crudModuleCommentSaveDataApi,
    crudModuleCommentDeleteDataApi
  } from '@/api/crud'
  import { reactive } from 'vue'
  const data = reactive({
    title: '详情',
    backgroundColor: 'rgba(0,0,0,0)',
    info: {},
    rightIcon: [
      // { type: 1, icon: 'icon-xuanfuanniu-jia' },
      {
        type: 2,
        icon: 'icon-gengduo2',
      },
    ],
    where: {
      limit: 10,
      page: 1,
      crud_value: 0,
      crud_id: 0,
    },
    commentList: [],
    count: 0,
    commentWhere: {
      limit: 10,
      page: 1,
    },
    id: 0,
    fixRight: '36rpx',
    forumMeus: [],
    isActive: 0,
    isActiveData: {},
    tableData: [],
    tableInfo: {},
    tablename: '',
    keyName: '',
    currentindex: 0,
    main_name: false,
    scrollLeft: 0, // 横向滚动条位
    category: [{
      id: 0,
      table_name: '基本信息',
    }, ],
  })

  onLoad((options) => {
    data.keyName = options.key
    data.where.crud_value = options.id
    data.id = options.id
    data.title = options.name
    getInfo()
    getFindInfo()
    getComment()
  })

  const dropDownRef = ref(null)
  // 获取实体信息
  const getInfo = () => {
    crudModuleInfoApi(data.keyName, 0).then((res) => {
      data.info = res.data
      if (data.info.userOptions.options && data.info.userOptions.options.create && data.info.userOptions.options
        .create.length == 0) {
        data.rightIcon = data.rightIcon.splice(1, 1)
      }
      data.where.crud_id = res.data.crudInfo.id
      data.category = [...data.category, ...data.info.userOptions.options.tab]
    })
  }
  // 获取当前实体详情
  const getFindInfo = () => {
    uni.showLoading({
      title: '加载中',
    })
    crudModuleFindApi(data.keyName, data.id).then((res) => {
      data.infoList = res.data.values
      uni.hideLoading()
    })
  }

  // 获取实体评论内容
  const getComment = () => {
    crudModuleCommentListDataApi(data.keyName, data.id, data.commentWhere).then(res => {
      data.commentList = [...data.commentList, ...res.data.list]
      data.count = res.data.count
    })
  }

  // 提交评论
  const saveReplay = (item, val) => {
    if (data.commentList.length == 0) {
      item.pid = 0
      item.id = 0
    }
    let obj = {
      pid: item.pid || item.id || 0,
      comment: val
    }
    crudModuleCommentSaveDataApi(data.keyName, data.id, obj).then(res => {
      message.success(res.message)
      data.commentList = []
      setTimeout(() => {
        getComment()
      }, 300)
    })
  }


  // 删除评论
  const deleteReplyFn = (item) => {
    showModal('您确定要删除吗')
      .then((res) => {
        crudModuleCommentDeleteDataApi(data.keyName, item.id)
          .then((res) => {
            message.success(res.message)
            data.commentList = []
            getComment()
          })
          .catch((error) => {
            message.error(error.message)
          })
      })
      .catch((error) => {
        console.log('取消了')
      })
  }

  // 编辑删除
  const selectFn = (e, row) => {
    if (e.type == 2) {
      showModal('您确定要删除吗')
        .then((res) => {
          crudModuleDelApi(data.tablename, row)
            .then((res) => {
              message.success(res.message)
              getTableList(1)
            })
            .catch((error) => {
              message.error(error.message)
            })
        })
        .catch((error) => {
          console.log('取消了')
        })
    } else {
      // 编辑
      let routeData = {
        key: data.tablename,
        crud_id: data.where.crud_id,
        crud_value: data.where.crud_value,
        name: data.title,
        keyName: data.keyName,
        id: data.id,
      }
      clickNavigateTo(
        `/pages/module/addForm?key=${data.tablename}&&id=${row}&&route=${encodeURIComponent(JSON.stringify(routeData))}`
      )
    }
  }

  const getTableInfo = () => {
    crudModuleInfoApi(data.tablename, 0).then((res) => {
      if (res.data.crudInfo.main_field_name == '') {
        data.main_name = true
        res.data.crudInfo.main_field_name = 'main_field_name'
        let obj = {
          field_name_en: 'main_field_name',
        }
        res.data.showField.unshift(obj)
      }
      data.tableInfo = res.data
      getTableList(1)
    })
  }
  // 获取实体列表
  const getTableList = (val) => {
    if (val) {
      data.where.page = val
    }
    crudModuleListApi(data.tablename, data.where).then((res) => {
      if (data.where.page == 1) {
        data.tableData = []
      }
      data.tableData.push(...res.data.list)
      data.tableData.forEach((item) => {
        if (data.main_name) {
          item.main_field_name = data.isActiveData.field_name
        }
      })

      const allPage = Math.ceil(res.data.count / data.where.limit)
      if (data.tableData.length <= 0 || data.where.page >= allPage) {
        listLoading.value = false
      } else {
        listLoading.value = true
      }
      uni.stopPullDownRefresh() //停止刷新
    })
  }
  import { clickNavigateTo, showModal } from '@/utils/helper'

  // 新增编辑
  const dropDownItem = (e) => {
    if (e.icon) {
      if (e.id == 1) {
        clickNavigateTo(`/pages/module/addForm?key=${data.keyName}&&id=${data.id}&&name=${data.title}`)
      } else if (e.id == 3) {
        showModal('您确定要删除吗')
          .then((res) => {
            crudModuleDelApi(data.keyName, data.id)
              .then((res) => {
                message.success(res.message)
                clickNavigateTo(`/pages/module/list?tablename=${data.keyName}`)
              })
              .catch((error) => {
                message.error(error.message)
              })
          })
          .catch((error) => {
            console.log('取消了')
          })
      }
    } else {
      let routeData = {
        key: e.table_name_en,
        crud_id: data.where.crud_id,
        crud_value: data.where.crud_value,
        name: data.title,
        keyName: data.keyName,
        id: data.id,
      }

      clickNavigateTo(`/pages/module/addForm?route=${encodeURIComponent(JSON.stringify(routeData))}`)
    }
  }
  // 点击tab
  const chenked = (index, item) => {
    data.tablename = item.table_name_en
    data.isActiveData = item
    data.isActive = index
    getTableInfo()
  }
  const handleNarItem = (e) => {
    if (e.type === 2) {
      data.fixRight = '36rpx'
      data.forumMeus = [
        { name: '编辑', id: 1, icon: 'icon-gongzuohuibao-bianji' },
        { name: '删除', id: 3, icon: 'icon-shanchu1' },
      ]
      dropDownRef.value.openDropdown()
    } else {
      data.fixRight = '100rpx'
      console.log(data.info.userOptions.options.create)
      data.forumMeus = [...data.info.userOptions.options.create]
      dropDownRef.value.openDropdown()
    }
  }

  import { onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app'
  const listLoading = ref(false)
  // 下拉加载
  onReachBottom(() => {
    if (data.isActive == 0 && data.info.crudInfo.show_comment && data.count / 10 > data.commentWhere.page) {
      data.commentWhere.page++
      getComment()
    } else {
      if (listLoading.value) {
        data.where.page++
        getTableList()
      }
    }

  })
  //上拉加载
  onPullDownRefresh(() => {
    data.where.page = 1
    getTableList()
  })
</script>
<style scoped lang="scss">
  .cr-position-header {
    position: fixed;
    padding-top: var(--status-bar-height);
    height: calc($uni-default-bar-height + var(--status-bar-height));
    background: linear-gradient(90deg, #459fff 0%, #388aef 100%, #3384e7 100%);
  }

  .content {
    display: flex;
    flex-direction: column;
    width: 100%;
    flex: 1;

    .examine-content {
      padding-top: calc($uni-default-bar-height + var(--status-bar-height));
      padding-bottom: 50rpx;
    }

    .nav {
      border-top: 1rpx solid #f2f2f2;
      background-color: #ffffff;
      // position: fixed;
      z-index: 99;
      width: 100%;
      align-items: center;
      height: 80rpx;

      .tab-scroll {
        flex: 1;
        overflow: hidden;
        box-sizing: border-box;
        padding-left: 30rpx;
        padding-right: 30rpx;

        .tab-scroll_box {
          display: flex;
          align-items: center;
          flex-wrap: nowrap;
          box-sizing: border-box;

          .tab-scroll_item {
            height: 42px;
            margin-right: 48rpx;
            flex-shrink: 0;
            padding-bottom: 26rpx;
            display: flex;
            justify-content: center;
            font-family: PingFang SC, PingFang SC;
            font-weight: 400;
            font-size: 26rpx;
            color: #909399;
            padding-top: 10px;
            border-bottom: 3rpx solid rgba(0, 0, 0, 0);
          }
        }
      }
    }
  }

  .active {
    position: relative;
    font-family: PingFang SC, PingFang SC;
    font-weight: 500 !important;
    font-size: 26px;
    color: #303133 !important;
    border-bottom: 3rpx solid #1890ff !important;
  }

  ::v-deep.uni-scroll-view::-webkit-scrollbar {
    display: none;
  }

  .pb60 {
    padding-bottom: 60px;
  }
</style>