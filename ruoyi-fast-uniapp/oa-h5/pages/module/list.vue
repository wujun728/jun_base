<template>
  <view class="content">
    <view class="cr-position-header">
      <default-nav-bar
        jump-url="/pages/workbench/index"
        :defaultTitle="data.title"
        :backgroundColor="data.backgroundColor"
        :color="`#fff`"
        :is-right="true"
        :right-data="data.rightIcon"
        @handleNarItem="handleNarItem"
      ></default-nav-bar>
    </view>
    <view class="examine-content">
      <view class="search">
        <uni-easyinput
          prefixIcon="search"
          v-model="data.where.keyword_default"
          @confirm="getTableList(1)"
          @focus="getTableList(1)"
          @clear="clearSearch"
          placeholder="关键字搜索"
        ></uni-easyinput>
      </view>
      <view class="view-search">
        <formBox :info="data.info" @confirmData="confirmData"></formBox>
      </view>

      <item :info="data.info" :table-data="data.tableData" :keyName="data.keyName" @selectFn="selectFn"></item>
    </view>
    <view class="tab" v-if="data.list && data.list.length > 0">
      <view v-for="(item, index) in data.menu_list" :key="index" class="tab-item" @click="switchTab(item, index)">
        <view :style="{ color: data.currentIndex == index ? selectedColor : color }">
          <text :class="getIconCss(item.icon)"></text>
        </view>
        <view class="tab_text" :style="{ color: data.currentIndex == index ? selectedColor : color }">{{ item.menu_name }}</view>
      </view>
      <dean-popover
        v-if="data.list.length > 5"
        ref="deanPopoverRef"
        model-direction="right"
        :btnList="data.hide_list"
        @select="switchTab($event, index)"
      >
        <template #icon>
          <view class="tab-item">
            <view :style="{ color: data.currentIndex == index ? selectedColor : color }">
              <text class="iconfont icon-yunwenjian-liebiaomoshi"></text>
            </view>
            <view class="tab_text" :style="{ color: data.currentIndex == index ? selectedColor : color }">更多</view>
          </view>
        </template>
      </dean-popover>
    </view>
  </view>
</template>

<script setup>
import { crudModuleInfoApi, crudModuleListApi, crudModuleDelApi, crudModuleMenusApi } from '@/api/crud'
import defaultNavBar from '@/components/defaultNavBar/index.vue'
import message from '@/utils/message'
import formBox from './components/formBox.vue'
import item from './components/item.vue'
import deanPopover from '@/components/deanPopover/index.vue'

const data = reactive({
  backgroundColor: 'rgba(0,0,0,0)',
  info: {},
  rightIcon: [{ type: 1, icon: 'icon-xuanfuanniu-jia' }],
  title: '列表名称',
  keyName: 'kehuguanli',
  tableData: [],
  main_name: false,
  currentIndex: 0,
  where: {
    limit: 10,
    page: 1,
    keyword_default: '',
  },
  menu_list: [],
  hide_list: [],
})
const selectedColor = ref('#308BF8')
const color = ref('#687383')

onLoad((options) => {
  if (options.tablename) {
    data.keyName = options.tablename
    getInfo()
    getMenus()
  }
})

const getIconCss = (iconName) => {
  if (!iconName) {
    return ['iconfont', 'icon-renshi-huibaoguanli-cebian']
  } else if (iconName.includes('el-icon')) {
    return iconName
  } else {
    return ['iconfont', `icon-${item.icon.slice(4)}`]
  }
}

// 获取底部菜单
const getMenus = () => {
  crudModuleMenusApi(data.keyName).then((res) => {
    data.list = res.data
    if (res.data.length > 5) {
      data.menu_list = res.data.slice(0, 4)
      data.hide_list = res.data.slice(4, res.data.length)
    } else {
      data.menu_list = res.data
    }
  })
}

const switchTab = (item, index) => {
  data.currentIndex = index
  let url = item.uni_path
  clicKReLaunch(url)
}
const clearSearch = () => {
  data.where.keyword_default = ''
  data.where.page = 1
  getTableList()
}

// 编辑删除
const selectFn = (e, row) => {
  if (e.type == 2) {
    showModal('您确定要删除吗')
      .then((res) => {
        crudModuleDelApi(data.keyName, row)
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
    clickNavigateTo(`/pages/module/addForm?key=${data.keyName}&&id=${row}`)
  }
}
import { clickNavigateTo, showModal, clicKReLaunch } from '@/utils/helper'
const handleNarItem = (e) => {
  clickNavigateTo(`/pages/module/addForm?key=${data.keyName}`)
}

// 获取实体信息
const getInfo = () => {
  crudModuleInfoApi(data.keyName, 0).then((res) => {
    res.data.seniorSearch = res.data.seniorSearch.filter((item) => {
      return !['input_number', 'input_float', 'input_percentage', 'input_price', 'input'].includes(item.form_value)
    })

    if (res.data.crudInfo.main_field_name == '') {
      data.main_name = true
      res.data.crudInfo.main_field_name = 'main_field_name'
      let obj = {
        field_name_en: 'main_field_name',
      }
      res.data.showField.unshift(obj)
    }
    data.info = res.data
    data.title = res.data.crudInfo.table_name + '列表'
  })
}

// 获取实体列表
const getTableList = (val) => {
  if (val) {
    data.where.page = val
  }
  crudModuleListApi(data.keyName, data.where).then((res) => {
    if (data.where.page == 1) {
      data.tableData = []
    }
    data.tableData.push(...res.data.list)
    data.tableData.forEach((item) => {
      if (data.main_name) {
        item.main_field_name = data.info.crudInfo.table_name
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

const confirmData = (e) => {
  data.where = { page: 1, limit: data.where.limit, ...e }
  getTableList()
}

import { onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app'
const listLoading = ref(false)
// 下拉加载
onReachBottom(() => {
  if (listLoading.value) {
    data.where.page++
    getTableList()
  }
})
//上拉加载
onPullDownRefresh(() => {
  data.where.page = 1

  getTableList()
})
</script>

<style scoped lang="scss">
@import "@/static/iconfont/elementui-icon.scss";

.cr-position-header {
  position: fixed;
  padding-top: var(--status-bar-height);
  height: calc($uni-default-bar-height + var(--status-bar-height));
  background: linear-gradient(90deg, #459fff 0%, #388aef 100%, #3384e7 100%);
}

.examine-content {
  padding-top: calc($uni-default-bar-height + var(--status-bar-height));
  padding-bottom: 113rpx;
  ::v-deep .is-input-border {
    border: none;
  }

  .search {
    width: 100%;
    height: 82rpx;
    background-color: #ffffff;
    padding: 18rpx 30rpx 0 30rpx;
  }

  .view-search {
    width: 100%;
    height: 72rpx;
    padding: 0 30rpx;
    background-color: #ffffff;
  }
}

::v-deep .uni-easyinput__content {
  background-color: #f5f5f5 !important;
}

::v-deep .uni-easyinput__content-input {
  height: 32px !important;
  font-size: 13px !important;
  .uni-easyinput__placeholder-class {
    font-size: 13px !important;
  }
}
::v-deep .content-clear-icon {
  font-size: 16px !important;
}
.tab {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 113rpx;
  background: white;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0px 0px 16px 0px rgba(215, 215, 215, 0.5);
  padding-bottom: env(safe-area-inset-bottom); // 适配iphoneX的底部
  padding: 12rpx 26rpx;
  display: flex;
  justify-content: space-between;
  .tab-item {
    // flex: 1;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;

    [class^='el-icon'],
    .iconfont {
      font-size: 44rpx;
    }

    .tab_text {
      margin-top: 12rpx;
      font-size: 22rpx;
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      color: #f3f3f3;
    }
  }
}
</style>
