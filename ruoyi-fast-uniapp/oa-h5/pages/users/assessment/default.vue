<template>
  <view class="content">
    <view class="cr-position-header assess-header">
      <!-- #ifndef APP-PLUS -->
      <image class="image" :src="assessbgH5" mode=""></image>
      <!-- #endif -->

      <!-- #ifdef APP-PLUS -->
      <image class="image" :src="assessbg" mode=""></image>
      <view class="bar">
        <default-nav-bar :index="1" background-color="rgba(0,0,0,0)" color="#fff" :default-title="data.navTitle">
        </default-nav-bar>
      </view>
      <!-- #endif -->

      <view class="assess-bottom">
        <uni-row class="display-align">
          <uni-col :span="data.rowSpan">
            <view class="text-center">{{data.assessInfo.total}}</view>
            <view class="text-center capitin" @click="explainClick">总分 <text v-if="data.explain"
                class="iconfont icon-bangzhu"></text></view>
          </uni-col>
          <uni-col :span="1" class="text-center">
            <view class="bottom-line"></view>
          </uni-col>
          <uni-col :span="data.rowSpan">
            <view class="text-center">{{finishRatioSum()}}</view>
            <view class="text-center capitin">自评分</view>
          </uni-col>
          <uni-col v-if="data.assessInfo.status > 1" :span="1" class="text-center">
            <view class="bottom-line"></view>
          </uni-col>
          <uni-col :span="data.rowSpan" v-if="data.assessInfo.status > 1">
            <view class="text-center">{{data.assessInfo.score}}</view>
            <view class="text-center capitin"> {{ data.assessInfo.status > 3 ? '绩效得分' : '当前得分'}}</view>
          </uni-col>
        </uni-row>
      </view>
    </view>
    <view class="assessment">
      <swiper class="swiper" :circular="false" :duration="800" :indicator-dots="false" :current="currentIndex"
        :autoplay="false" @change="handleChange">
        <swiper-item v-for="(item,index) in data.info" :key="'swiper'+index">
          <scroll-view style="height: 100%;" scroll-y="true">
            <view class="assessment-header">
              <uni-row>
                <uni-col :span="12">
                  <view class="line1">{{item.name}} ({{ current +'/' +data.info.length}})</view>
                </uni-col>
                <uni-col :span="12" class="assessment-header-right line1">
                  {{ data.assessInfo.types === 0 ? '权重' : '分数'}}：{{item.ratio}}
                  {{ data.assessInfo.types === 0 ? '%' : ''}}
                  得分：{{currentSum(item, index)}}
                </uni-col>
              </uni-row>
            </view>
            <view class="assessment-list m10">
              <uni-list :border="false">
                <uni-list-item v-for="(items,indexs) in item.target" :key="items.id">
                  <template v-slot:body>
                    <view class="assessment-list-item">
                      <view class="target-name">{{items.name}}</view>
                      <uni-forms :border="false" label-position="top" label-width="80px">
                        <uni-forms-item label="指标说明">
                          <uni-easyinput :inputBorder="false" :disabled="true" type="textarea" :styles="styles"
                            :maxlength="512" :autoHeight="true" v-model="items.content" placeholder="请输入指标说明">
                          </uni-easyinput>
                        </uni-forms-item>
                        <uni-forms-item label="评分等级">
                          <uni-easyinput :inputBorder="false" :disabled="data.assessInfo.status === 5" type="textarea"
                            :styles="styles" :maxlength="255" :autoHeight="true" v-model="items.info"
                            placeholder="请输入评分等级">
                          </uni-easyinput>
                        </uni-forms-item>
                        <uni-forms-item label="完成情况">
                          <uni-easyinput :inputBorder="false" :disabled="data.assessInfo.status === 5" type="textarea"
                            :styles="styles" :maxlength="512" :autoHeight="true" v-model="items.finish_info"
                            placeholder="请输入完成情况">
                          </uni-easyinput>
                        </uni-forms-item>
                        <uni-forms-item>
                          <template v-slot:label>
                            <uni-row class="list-item-label display-align">
                              <uni-col :span="12">
                                <view>{{ data.assessInfo.types === 0 ? '完成度(自评)' : '自我评分'}}</view>
                              </uni-col>
                              <uni-col :span="12" class="text-right">
                                <view>{{ data.assessInfo.types === 0 ? '权重' : '指标满分'}}：{{items.ratio}}
                                  {{ data.assessInfo.types === 0 ? '%' : ''}}
                                </view>
                              </uni-col>
                            </uni-row>
                          </template>
                          <template v-if="data.assessInfo.types === 0">
                            <!-- 加权评分 -->
                            <uni-number-box :min="0" :disabled="data.assessInfo.status === 5"
                              @blur="numberInput($event,items)" :max="100" :step="1" background="#fff"
                              v-model="items.finish_ratio">
                            </uni-number-box>
                          </template>
                          <!-- 加和评分 -->
                          <template v-if="data.assessInfo.types === 1">
                            <uni-number-box :disabled="data.assessInfo.status === 5" :min="0"
                              @blur="numberInput($event,items)" :max="items.ratio" :step="1" background="#fff"
                              v-model="items.finish_ratio">
                            </uni-number-box>
                          </template>
                        </uni-forms-item>
                        <!-- 查看上级评价 -->
                        <uni-forms-item v-if="data.assessInfo.status > 2" class="check-items">
                          <template v-slot:label>
                            <uni-row class="list-item-label display-align">
                              <uni-col :span="12">
                                <view>上级评价</view>
                              </uni-col>
                              <uni-col :span="12" class="text-right">
                                <view>上级评分：{{items.score}} {{ data.assessInfo.types === 0 ? '%' : ''}}</view>
                              </uni-col>
                            </uni-row>
                          </template>
                          <uni-easyinput :clearable="false" :inputBorder="false" :disabled="true" :styles="styles"
                            v-model="items.check_info" type="textarea" :maxlength="512" :autoHeight="true"
                            placeholder="暂无评价">
                          </uni-easyinput>
                        </uni-forms-item>
                      </uni-forms>
                    </view>
                  </template>
                </uni-list-item>
              </uni-list>
            </view>
          </scroll-view>
        </swiper-item>
      </swiper>
    </view>
    <view class="assessment-button display-align">
      <template v-if="current > 1 && current !== data.info.length">
        <button class="assess-btn upper mr10" plain="true" @click="clickUpper">上一项</button>
        <button class="assess-btn ml10" type="primary" @click="clickDown">下一项</button>
      </template>
      <template v-else-if="current == 1 && data.info.length === 1">
        <button :disabled="data.assessInfo.status === 5" class="assess-btn" type="primary" :loading="loading"
          @click="clickSubmit">完成</button>
      </template>
      <template v-else-if="current > 1 && current === data.info.length">
        <button class="assess-btn upper mr10" plain="true" @click="clickUpper">上一项</button>
        <button :disabled="data.assessInfo.status === 5" class="assess-btn ml10" type="primary" :loading="loading"
          @click="clickSubmit">完成</button>
      </template>
      <template v-else>
        <button class="assess-btn" type="primary" @click="clickDown">下一项</button>
      </template>
    </view>

    <explain ref="explainRef" :explain="data.explain"></explain>
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import { ref, reactive, nextTick, computed } from 'vue'
  import { assessInfoApi, assessUpdateApi } from '@/api/user'
  import message from '@/utils/message'
  import explain from './components/explain.vue'
  import { getStatusText, getPeriodText } from '@/utils/assessment'
  // #ifndef APP-PLUS
  import assessbgH5 from '../static/image/assess-bg-h5.png'
  // #endif
  // #ifdef APP-PLUS
  import assessbg from '../static/image/assess-bg.png'
  // #endif

  import { useStore } from 'vuex'
  const store = useStore()
  const uid = computed(() => store.state.app.uid)

  const styles = reactive({
    color: '#303133',
    disableColor: '#ffffff'
  })

  const indicatorDots = ref(true)
  const loading = ref(false)
  const assessId = ref(0)
  const current = ref(1)
  const currentIndex = ref(0)
  const data = reactive({
    assessInfo: {},
    info: [],
    userInfo: {},
    navTitle: '考核详情',
    rowSpan: 12,
    totalScore: 0,
    spaceIds: [],
    targetIds: [],
    explain: ''
  })

  // 下一项
  const clickDown = () => {
    currentIndex.value++
    current.value++
  }
  // 下一项
  const clickUpper = () => {
    currentIndex.value--
    current.value--
  }

  // 完成
  const clickSubmit = () => {
    const res = subJudge()
    let type = 0

    if (data.assessInfo.status <= 3 && uid.value === data.assessInfo.test.uid) {
      type = 0
    } else if (uid.value === data.assessInfo.check.uid) {
      type = 1
    } else {
      type = 2
    }
    const data_s = {
      types: type,
      is_draft: 0,
      space: data.spaceIds,
      target: data.targetIds,
      data: res
    }
    if (!loading.value) {
      assessUpdate(assessId.value, data_s)
    }
  }

  import { onLoad } from '@dcloudio/uni-app'
  onLoad(options => {
    if (options.id) {
      assessId.value = options.id
      getAssessInfo(assessId.value)
    }
  })

  const handleChange = e => {
    current.value = e.detail.current + 1
  }

  const explainRef = ref(null)

  const explainClick = () => {
    if (!data.explain) return false
    explainRef.value.popupOpen()
  }


  // 获取考核详情
  const getAssessInfo = id => {
    assessInfoApi(id).then(res => {
      data.assessInfo = res.data.assessInfo
      data.info = res.data.info
      data.explain = res.data.explain
      data.navTitle = res.data.assessInfo.name
      data.rowSpan = res.data.assessInfo.status > 1 ? 8 : 12
    }).catch(error => {
      message.error(error.message)
    })
  }


  import { delayedReLaunch } from '@/utils/helper'
  const assessUpdate = (id, data) => { //async
    loading.value = true
    assessUpdateApi(id, data).then(res => {
      loading.value = true
      message.success(res.message)
      delayedReLaunch('/pages/users/assessment/index')
    }).catch(error => {
      loading.value = false
      message.error(error.message)
    })
  }

  //提交时格式验证与数据整合
  const subJudge = () => {
    let res = []
    for (let i = 0; i < data.info.length; i++) {
      const target = []
      for (let j = 0; j < data.info[i].target.length; j++) {
        target.push({
          id: data.info[i].target[j].id,
          name: data.info[i].target[j].name,
          content: data.info[i].target[j].content,
          finish_info: data.info[i].target[j].finish_info,
          info: data.info[i].target[j].info, // 评分等级
          finish_ratio: data.info[i].target[j].finish_ratio ? data.info[i].target[j].finish_ratio : 0,
          check_info: data.info[i].target[j].check_info,
          score: data.info[i].target[j].score === undefined ? 0 : data.info[i].target[j].score,
          ratio: data.info[i].target[j].ratio,
        })
      }
      if (target.length <= 0) {
        break
      }
      res.push({
        id: data.info[i].id,
        name: data.info[i].name,
        ratio: data.info[i].ratio,
        target: target,
      })
    }

    return res

  }

  const numberInput = (e, item, index, items) => {
    let value = 0
    if (data.assessInfo.types === 1) {
      value = e.detail.value > item.ratio ? item.ratio : e.detail.value
    } else {
      value = e.detail.value > 100 ? 100 : e.detail.value
    }
    item.finish_ratio = value
  }

  // 计算个人得分
  const finishRatioSum = () => {
    let number = 0
    let itemNumber = 0

    if (data.info.length > 0) {
      data.info.forEach(value => {
        if (value.target.length > 0) {
          if (data.assessInfo.types === 0) {
            value.target.forEach(val => {
              itemNumber += Number(
                (value.ratio * val.ratio * (val.finish_ratio === undefined ? 0 : val.finish_ratio)) / 10000,
              )
            })
          } else {
            value.target.forEach(val => {
              itemNumber += Number(val.finish_ratio === undefined ? 0 : val.finish_ratio)
            })
          }
        } else {
          itemNumber = 0
        }
      })
    }
    if (data.assessInfo.types === 0) {
      number = ((Number(data.assessInfo.total) * itemNumber) / 100).toFixed(2)
    } else {
      number = itemNumber.toFixed(2)
    }
    return number
  }

  // 当前得分计算
  const currentSum = (row, index) => {
    let number = 0
    if (row.target.length > 0) {
      if (data.assessInfo.types === 0) {
        row.target.forEach(val => {
          number += Number((row.ratio * val.ratio * (val.finish_ratio === undefined ? 0 : val.finish_ratio)) /
            10000)
        })
        number = ((Number(data.assessInfo.total) * number) / 100).toFixed(2)
      } else {
        row.target.forEach(val => {
          number += Number(val.finish_ratio === undefined ? 0 : val.finish_ratio)
        })
      }
    }
    data.info[index].score = number
    totalSum()
    return number
  }

  // 获取当前总分
  const totalSum = () => {
    let number = 0
    nextTick(() => {
      data.info[0].target.forEach(val => {
        number += Number(val.score ? val.score : 0)
      })
      number = ((number * 100) / 100).toFixed(2)
      data.totalScore = number
    })
  }
</script>
<style>
  page {
    background-color: #fff;
  }
</style>
<style scoped lang="scss">
  .content {
    width: 100%;

    .assess-header {
      // #ifndef APP-PLUS
      height: 144rpx;
      // #endif
      // #ifdef APP-PLUS
      height: 316rpx;
      // #endif

      .image {
        width: 100%;
        height: 100%;
      }

      .bar {
        width: 100%;
        position: absolute;
        left: 0;
        top: 44px;
      }

      .assess-bottom {
        width: 100%;
        position: absolute;
        left: 0;
        // #ifndef APP-PLUS
        top: 50%;
        transform: translate(0, -50%);
        // #endif
        // #ifdef APP-PLUS
        top: calc(88px + 20rpx);
        // #endif
        font-size: 40rpx;
        font-weight: 500;
        color: #fff;

        .bottom-line {
          width: 1px;
          height: 28rpx;
          display: inline-block;
          background-color: #7BB3F7;
        }

        .capitin {
          font-weight: normal;
          font-size: 26rpx;

          .iconfont {
            font-size: 26rpx;
          }
        }
      }
    }

    .assessment {
      // #ifndef APP-PLUS
      height: calc(100vh - 144rpx - 98rpx);
      margin-top: 144rpx;
      // #endif
      // #ifdef APP-PLUS
      height: calc(100vh - 316rpx - 98rpx);
      margin-top: 316rpx;
      // #endif

      .swiper {
        height: 100%;
      }

      .assessment-header {
        width: 100%;
        height: 90rpx;
        line-height: 90rpx;
        font-size: 26rpx;
        color: $uni-text-color;
        background-color: #F0F1F5;
        padding: 0 20rpx;

        .assessment-header-right {
          text-align: right;
        }
      }

      .assessment-list {
        ::v-deep .uni-list {
          .uni-list--border {
            left: auto;
            top: auto;
          }

          .uni-list-item {
            margin-bottom: 40rpx;
            border-radius: 8rpx;
            background-color: #F0F1F5;

            .uni-list-item__container {
              padding: 36rpx 24rpx;
            }
          }
        }

        .assessment-list-item {
          width: 100%;

          .target-name {
            font-size: 30rpx;
            color: $uni-text-color;
            padding-bottom: 36rpx;
          }

          ::v-deep .uni-forms {
            width: 100%;

            .uni-forms-item {
              background-color: #fff;
              border-radius: 12rpx;
              padding: 36rpx 24rpx 18rpx 24rpx;
              margin-bottom: 30rpx;

              &:last-of-type {
                margin-bottom: 0;
              }

              .uni-easyinput__content-input {
                padding-left: 0 !important;
              }

              .uni-forms-item__label {
                height: auto;
                padding: 0;
                font-size: 26rpx;
                color: $nui-text-color-four;
                line-height: 1;
              }

              .list-item-label {
                font-size: 26rpx;
                color: $nui-text-color-four;

                .text-right {
                  font-size: 22rpx;
                }
              }

              .uni-numbox {
                .uni-numbox-btns {
                  display: none;
                }

                .uni-numbox__value {
                  width: 100%;
                  text-align: left;
                  height: 70rpx;
                }
              }

              .is-disabled {
                color: $uni-text-color;
              }

              .uni-easyinput__content-textarea {
                min-height: 160rpx;
              }

              .uni-easyinput__placeholder-class {
                font-size: 28rpx;
                color: #C0C4CC;
              }
            }

            .check-items {
              .uni-easyinput__content-textarea {
                min-height: 70rpx;
              }
            }
          }
        }
      }
    }

    .assessment-button {
      position: fixed;
      left: 0;
      bottom: 0;
      right: 0;
      width: 100%;
      background-color: #fff;
      height: 98rpx;
      box-shadow: 0px 0px 8px 0px rgba(215, 215, 215, 0.5);
      padding: 0 20rpx;

      .assess-btn {
        width: 100%;
        height: 70rpx;
        line-height: 70rpx;
        font-size: 30rpx;
        border-radius: 6rpx
      }

      .ml10 {
        margin-left: 10rpx;
      }

      .mr10 {
        margin-right: 10rpx;
      }

      .upper {
        background-color: rgba(24, 144, 255, 0.1);
        color: $uni-color-primary;
        border: none;
      }
    }
  }
</style>