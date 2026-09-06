<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar jump-url="/pages/workbench/index"></default-nav-bar>
    </view>
    <view class="assessment m10">
      <uni-list :border="false" v-if="data.listData.length > 0">
        <uni-list-item v-for="item in data.listData" :key="'list'+item.id"
          :to="`/pages/users/assessment/default?id=${item.id}`">
          <template v-slot:body>
            <view class="item-list">
              <uni-row class="display-align item-list-top">
                <uni-col :span="16" class="right-title">{{item.name}}</uni-col>
                <uni-col :span="8" class="text-right">
                  <uni-tag v-if="item.status === 1" :inverted="true" :text="getStatusText(item.status)"
                    type="primary" />
                  <uni-tag v-if="item.status === 2" :inverted="true" :text="getStatusText(item.status)"
                    type="warning" />
                  <uni-tag v-if="item.status === 3" :inverted="true" :text="getStatusText(item.status)" type="error" />
                  <uni-tag v-if="item.status === 4" :inverted="true" :text="getStatusText(item.status)" />
                  <uni-tag v-if="item.status === 5" :inverted="true" :text="getStatusText(item.status)" />
                </uni-col>
              </uni-row>
              <view class="item-list-content">
                <view class="items display-align">被考核人 <text class="default-color">{{item.test.name}}</text></view>
                <view class="items display-align">
                  开始时间
                  <uni-dateformat format="yyyy/MM/dd" :date="item.start_time"></uni-dateformat>
                </view>
                <view class="items display-align">
                  结束时间
                  <uni-dateformat format="yyyy/MM/dd" :date="item.end_time"></uni-dateformat>
                </view>
                <view class="items display-align">考核周期 <text>{{ getPeriodText(item.period) }}</text></view>
                <!--<view class="items display-align">
									<image :src="item.check.avatar" mode=""></image>
									<text class="items-name">{{ item.check.name}}创建</text>
								</view>-->
              </view>
              <uni-row class="item-list-content item-list-button">
                <button class="default-color" v-if="item.status === 1">进行{{getStatusText(item.status)}}</button>
              </uni-row>
            </view>
          </template>
        </uni-list-item>
      </uni-list>
      <empty v-else :index="2" title="暂无绩效考核内容～"></empty>
    </view>
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index'
  import empty from '@/components/empty/index'
  import { ref, reactive, onMounted } from "vue"
  import { assessMineApi } from '@/api/user'
  import message from '@/utils/message'
  import { getStatusText, getPeriodText } from '@/utils/assessment'

  onMounted(() => {
    getAssessMine()
  })

  const data = reactive({
    listData: [],
    where: {
      page: 1,
      limit: 10,
      type: 0
    }
  })

  const listLoading = ref(false)

  const getAssessMine = (tab = false) => {
    assessMineApi(data.where).then(res => {
      data.listData.push(...res.data.list)
      const allPage = Math.ceil(res.data.count / data.where.limit)
      if (data.listData.length <= 0 || data.where.page >= allPage) {
        listLoading.value = false
      } else {
        listLoading.value = true
      }
      uni.stopPullDownRefresh() //停止刷新
    }).catch((error) => {
      uni.hideLoading()
      message.error(error.message)
    })
  }

  import { onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app'
  // 下拉加载
  onReachBottom(() => {
    if (listLoading.value) {
      data.where.page++
      getAssessMine()
    }
  })
  //上拉加载
  onPullDownRefresh(() => {
    data.where.page = 1
    listLoading.value = false
    data.listData = []
    getAssessMine()
  })
</script>

<style scoped lang="scss">
  @import '@/static/css/assessment.scss';

  .content {
    width: 100%;

    .assessment {

      padding-top: calc($uni-default-bar-height + var(--status-bar-height));

    }
  }
</style>