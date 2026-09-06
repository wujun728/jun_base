<template>
  <view class="forget">
    <uni-forms :border="false" label-width="80px">
      <template v-for="(item, index) in configData" :key="'form' + index">
        <!-- input类型 -->
        <template v-if="item.type === 'input' && !item.hidden">
          <uni-forms-item v-if="item.props && item.props.type === 'textarea'" class="is-direction-top">
            <template v-slot:label>
              <view class="uni-forms-item__label">
                <text v-if="item.effect && item.effect.required" class="iconfont"> * </text>
                <text>{{ item.title }}</text>
              </view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="item.value" type="textarea" :clearable="false" :styles="styles"
              :placeholder-style="placeholderStyle" :maxlength="256" :autoHeight="true" placeholder="请输入"
              @input="changeForms()"></uni-easyinput>
          </uni-forms-item>

          <uni-forms-item v-else class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">
                <text v-if="item.effect && item.effect.required" class="iconfont"> * </text>
                <text class="label-item">{{ item.title }}</text>
              </view>
            </template>
            <uni-easyinput :disabled="item.props ? item.props.readonly : false" :inputBorder="false" type="input"
              v-model="item.value" :clearable="false" :styles="styles" :placeholder-style="placeholderStyle"
              :maxlength="256" :autoHeight="true" placeholder="请输入" @input="changeForms"></uni-easyinput>
          </uni-forms-item>
        </template>
        <!-- 文字类型 -->
        <uni-forms-item v-if="item.type === 'span'" class="input-label">
          <template v-slot:label>
            <view class="uni-forms-item__label">
              <text v-if="item.effect && item.effect.required" class="iconfont"> * </text>
              <text class="label-item">{{ item.title }}</text>
            </view>
          </template>
          <uni-easyinput :disabled="true" :inputBorder="false" type="input" v-model="item.children[0]"
            :clearable="false" :styles="styles" :placeholder-style="placeholderStyle" :autoHeight="true"
            placeholder="请输入" @input="changeForms"></uni-easyinput>

          <!-- <view>{{item.children[0]}}</view> -->
        </uni-forms-item>
        <!-- 数字类型 -->
        <uni-forms-item v-if="item.type === 'inputNumber'" class="input-label">
          <template v-slot:label>
            <view class="uni-forms-item__label">
              <text v-if="item.effect && item.effect.required" class="iconfont"> * </text>
              <text class="label-item">{{ item.title }}</text>
            </view>
          </template>
          <uni-easyinput :inputBorder="false" v-model="item.value" :clearable="false" :styles="styles"
            :placeholder-style="placeholderStyle" :maxlength="256" :autoHeight="true" placeholder="请输入"
            @input="changeForms" @change="numToFixed(item)"></uni-easyinput>
        </uni-forms-item>
        <!-- 金额类型 -->
        <view v-if="item.type === 'moneyFrom'" class="money-from">
          <uni-forms-item class="input-label" :style="{ marginBottom: config.numFrom ? 0 : '20rpx' }">
            <template v-slot:label>
              <view class="uni-forms-item__label">
                <text v-if="item.effect && item.effect.required" class="iconfont"> * </text>
                <text class="label-item">{{ item.title }}</text>
              </view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="item.value" @change="moneyFromChange(item)" :clearable="false"
              :styles="styles" :placeholder-style="placeholderStyle" :maxlength="256" :autoHeight="true"
              placeholder="请输入" @input="changeForms"></uni-easyinput>
          </uni-forms-item>
          <view class="money-from-daxie" v-if="config.numFrom">
            <uni-row class="display-align">
              <uni-col :span="4">大写</uni-col>
              <uni-col :span="20" class="text-right">
                {{ config.numFrom }}
              </uni-col>
            </uni-row>
          </view>
        </view>
        <template v-if="item.type === 'datePicker'">
          <uni-forms-item v-if="item.props && item.props.type === 'datetime'" class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">
                <text v-if="item.effect && item.effect.required" class="iconfont"> * </text>
                <text class="label-item">{{ item.title }}</text>
              </view>
            </template>
            <view class="xp-picker-content">
              <xp-picker v-model="item.value" mode="ymdhi" actionPosition="top" :yearRange="[2014, 2050]"
                @confirm="changeForms" />
              <view class="iconfont icon-fanhui"></view>
            </view>
          </uni-forms-item>
          <!-- 普通时间类型 -->
          <uni-forms-item v-else class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">
                <text v-if="item.effect && item.effect.required" class="iconfont"> * </text>
                <text class="label-item">{{ item.title }}</text>
              </view>
            </template>
            <view class="xp-picker-content">
              <xp-picker v-model="item.value" mode="ymd" actionPosition="top" :yearRange="[2014, 2050]"
                @confirm="changeForms" />
              <view class="iconfont icon-fanhui"></view>
            </view>
          </uni-forms-item>
        </template>
        <!-- 省市区 -->
        <uni-forms-item v-if="item.type === 'city'" class="input-label">
          <template v-slot:label>
            <view class="uni-forms-item__label">
              <text v-if="item.effect && item.effect.required" class="iconfont"> * </text>
              <text class="label-item">省市区</text>
            </view>
          </template>
          <uni-data-picker v-model="item.value" :localdata="data.cityData" popup-title="请选择省市区" @change="cityChange">
            <view v-if="!item.value" class="picker-input picker-input-placeholder">
              请选择
              <view class="iconfont icon-fanhui"></view>
            </view>
            <view class="picker-input" v-else>
              {{ data.cityText }}
              <view class="iconfont icon-fanhui"></view>
            </view>
          </uni-data-picker>
        </uni-forms-item>
        <!-- {{!item.hidden}} -->
        <!-- select || radio -->
        <uni-forms-item v-show="(item.type === 'select' || item.type === 'radio') && !item.hidden" class="input-label">
          <template v-slot:label>
            <view class="uni-forms-item__label">
              <text v-if="item.effect && item.effect.required" class="iconfont"> * </text>
              <text class="label-item">{{ item.title }}</text>
            </view>
          </template>
          <picker mode="selector" :value="getIndex(item.options,item.value)" :range="item.options" range-key="label"
            @change="pickerChange($event, item, index)">
            <view v-if="!item.text" class="picker-input picker-input-placeholder">
              请选择
              <view class="iconfont icon-fanhui"></view>
            </view>
            <view v-else class="picker-input">
              {{ item.text }}
              <view class="iconfont icon-fanhui"></view>
            </view>
          </picker>


        </uni-forms-item>
        <!-- checkbox -->
        <uni-forms-item v-if="item.type === 'checkbox'" class="input-label">
          <template v-slot:label>
            <view class="uni-forms-item__label">
              <text v-if="item.effect && item.effect.required" class="iconfont"> * </text>
              <text class="label-item min-width">{{ item.title }}</text>
            </view>
            <custom-checkbox :config-data="item" @change="pickerMultiChange($event, item)"></custom-checkbox>
          </template>
        </uni-forms-item>
        <!-- cascader -->
        <uni-forms-item v-if="item.type === 'cascader'" class="input-label">
          <template v-slot:label>
            <view class="uni-forms-item__label">
              <text v-if="item.effect && item.effect.required" class="iconfont"> * </text>
              <text class="label-item">{{ item.title }}</text>
            </view>
          </template>

          <uni-data-picker class="selected-list" popup-title="请选择" :localdata="item.props.options"
            :clear-icon="item.effect.required ? false : true" v-model="item.value" @change="cascaderChange(item)"
            @popupclosed="chageClosed(item)" @nodeclick="onnodeclick($event, item)"></uni-data-picker>
        </uni-forms-item>
        <!-- 上传附件类型 -->
        <template v-if="item.type === 'uploadFrom'">
          <upload-from :config-data="item" @change="changeUploadFrom($event, item)"></upload-from>
        </template>
        <!-- 明细类型 -->
        <template v-if="item.type === 'approvalBill'">
          <approval-bill :config-data="item" @change="changeApprovalBill($event, item)"></approval-bill>
        </template>
        <!-- 时间类型 -->

        <template v-if="item.type == 'timeFrom'">
          <time-from :config-data="item" @change="changeTimeFrom($event, item)"></time-from>
        </template>
        <!-- 选择部门或成员 -->
        <template v-if="item.type === 'departmentTree'">
          <select-department v-if="item._fc_drag_tag === 'departmentTree'" :index="index" :config-data="item"
            @change="changeDepartment"></select-department>
          <select-member v-if="item._fc_drag_tag === 'memberTree'" :index="index" :config-data="item"
            @change="changeMember"></select-member>
        </template>
      </template>
    </uni-forms>
  </view>
</template>

<script setup>
  import { commonCityApi } from '@/api/customer'
  import { ref, reactive, toRefs, onMounted } from 'vue'
  import message from '@/utils/message'
  import { intToChinese } from '@/utils/helper'
  import uploadFrom from './components/uploadFrom'
  import timeFrom from './components/timeFrom'
  import approvalBill from './components/approvalBill'
  import selectDepartment from './components/selectDepartment.vue'
  import selectMember from './components/selectMember.vue'
  import customCheckbox from './components/customCheckbox.vue'
  import { useStore } from 'vuex'
  const store = useStore()
  const data = reactive({
    cityData: [],
    cityText: '',
    holiday_type: '',
    item: '',
  })
  let tryDateOptions = ref([])
  let tryYiChangOptions = ref([])

  const props = defineProps({
    configData: {
      type: Object,
      default: () => {
        return {
          cityData: [],
        }
      },
    },
    type: {
      type: String,
      default: '',
    },
  })

  const { configData, type } = toRefs(props)
  const placeholderStyle = ref('color: #C0C4CC;font-size: 30rpx')
  const styles = reactive({
    color: '#303133',
    disableColor: '#ffffff',
  })

  const config = reactive({
    numFrom: '',
    inputEle: ['input', 'moneyFrom', 'inputNumber'],
  })

  onMounted(() => {
    getCitylist()
  })
  // 深度监听审批内容
  watch(
    () => [configData.value],
    (newvalue) => {
      newvalue[0].forEach(item => {

        if ((item.type === 'select' || item.type === 'radio') && item.value) {
          let text = findNamesByIds(item.options, item.value)
          item.text = text
        }
      })
    }, { deep: true }
  )

  // 获取下拉回显默认值

  const emit = defineEmits(['change', 'searchData'])
  // 获取省市区数据
  const getCitylist = () => {
    commonCityApi().then((res) => {
      data.cityData = res.data
      changeId2(data.cityData, 'label', 'text')
    })
  }
  const changeId2 = (objAry, key, newkey) => {
    if (objAry != null) {
      objAry.forEach((item) => {
        Object.assign(item, {
          [newkey]: item[key],
        })
        delete item[key]
        changeId2(item.children, key, newkey)
      })
    }
  }

  // 根据选中数据获取数组的index
  const getIndex = (list, id) => {
    if (list.length > 0) {
      list.findIndex(function(item) {
        return item.value == id;
      })
    } else {
      return 0
    }

  }

  // 获取级联数据
  const findNamesByIds = (tree, id) => {
    if (id.constructor !== Array) {
      id = [id]
    }

    let result = []

    function traverse(node) {
      if (id.includes(node.value)) {
        result.push(node.label)
      }
      if (node.children) {
        for (const child of node.children) {
          traverse(child)
        }
      }
    }

    if (tree) {
      for (const node of tree) {
        if (node) {
          traverse(node)
        }
      }
    }

    let str = ''
    if (result.length > 1) {
      str = result.join(' - ')
    } else {
      str = result[0]
    }
    return str
  }
  const cityChange = (e) => {
    data.cityData = []
    let len = e.detail.value
    let text = []
    len.map((item) => {
      text.push(item.text)
    })
    data.cityText = text.join('-')
  }

  const cascaderChange = (item) => {}
  // 级联选择父子不联动
  const onnodeclick = (e, item) => {
    if (item.symbol == 'incomeCategories') {
      data.item = e
    }
  }

  // 关闭级联选择弹窗
  const chageClosed = (item) => {
    item.value = data.item.value
  }

  // 精确小数点
  const numToFixed = (item) => {
    let valData = Number(item.value)
    item.value = Number.isNaN(valData) ? "" : valData.toFixed(item.props.precision)
  }
  const moneyFromChange = (item) => {
    numToFixed(item)
    config.numFrom = intToChinese(item.value)
  }
  // select
  const pickerChange = (e, item, index) => {


    let timeType
    let expressList = ['liaisonMan', 'telephone', 'mailingAddress']
    // 补卡异常记录
    if (item.symbol == 'attendanceExceptionDate') {
      configData.value[index + 1].options = item.options[e.detail.value].record
    }
    item.text = item.options[e.detail.value].label
    item.value = item.options[e.detail.value].value
    if (item.symbol == 'holidayType') {
      item.options.map((el) => {
        if (item.value == el.value) {
          timeType = el.duration_type
        }
      })
      configData.value[index + 1].props.timeType = timeType == 0 ? 'day' : 'time'


    }

    // V1.4动态显示隐藏申请开票的邮寄字段
    if (item.field == 'Form1oggw64kzs') {
      configData.value.map((val) => {
        if (item.options[e.detail.value].value == 'express') {
          if (expressList.includes(val.symbol)) {
            val.hidden = false
          }
          if (val.symbol == 'invoicingEmail') {
            val.hidden = true
          }
        } else {
          if (expressList.includes(val.symbol)) {
            val.hidden = true
          }
          if (val.symbol == 'invoicingEmail') {
            val.hidden = false
          }
        }
      })
    }
    changeForms()
  }

  // select 多选
  const pickerMultiChange = (e, item) => {
    item.value = e
  }

  // 表单时间选择
  const changeTimeFrom = (e, item) => {
    item.value = e
    changeForms()
  }

  // 上传附件回调
  const changeUploadFrom = (e, item) => {
    item.value = e
    changeForms()
  }
  // 添加明细
  const changeApprovalBill = (e, item) => {
    item.value = e
  }
  // 选择部门
  const changeDepartment = (e) => {
    const len = store.state.app.depSelectIndex
    if (len > -1) {
      configData.value[len].value = e
    }
  }
  // 选择成员
  const changeMember = (e) => {
    const len = store.state.app.depSelectIndex
    if (len > -1) {
      configData.value[len].value = e
    }
  }

  // 表单change
  const changeForms = () => {
    emit('change', configData.value)
  }

  // 表单数据提交
  const submit = (callback) => {
    if (configData.value.length <= 0) return false
    let fromData = {}
    for (let i = 0; i < configData.value.length; i++) {
      let datas = configData.value[i]
      if (
        datas.effect &&
        datas.effect.required &&
        datas.type === 'timeFrom' &&
        (!datas.value || !datas.value.dateEnd || !datas.value.dateStart || !datas.value.timeEnd || !datas.value
          .timeStart)
      ) {
        message.error('请选择时间')
        return false
        break
      }
      if (datas.effect && datas.effect.required && !datas.value) {
        let str = ''
        if (config.inputEle.includes(datas.type)) {
          str = '请输入'
        } else if (datas.type === 'uploadFrom') {
          str = '请上传'
        } else {
          str = '请选择'
        }
        message.error(str + datas.title)
        return false
        break
      }

      if (datas.type === 'timeFrom') {
        datas.value['type'] = type.value
      }
      fromData[datas.field] = datas.value || ''
    }

    callback(fromData)
  }
  defineExpose({
    submit,
  })
</script>

<style lang="scss" scoped>
  .forget {
    width: 100%;

    ::v-deep .uni-forms-item {
      background-color: #fff;
      border-radius: 12rpx;
      padding: 36rpx 24rpx 18rpx 24rpx;
      margin-bottom: 20rpx;

      .uni-forms-item__label {
        height: auto;
        padding: 0;
        font-size: 30rpx;
        color: $uni-text-color;
        line-height: 1;

        .iconfont {
          color: #ff2529;
        }
      }

      .is-disabled {
        color: $uni-text-color;
      }

      .uni-easyinput__content-textarea {
        min-height: 252rpx;
      }

      .uni-date-x {
        padding: 0;

        .uniui-calendar {
          display: none;
        }
      }
    }

    .input-label {
      padding: 18rpx 24rpx;
      align-items: center;

      ::v-deep .uni-easyinput__content-input {
        text-align: right;
        padding-right: 0 !important;
      }

      ::v-deep .uni-forms-item__label {
        max-width: 148rpx;
        display: flex;
        line-height: 1.2;

        .iconfont {
          width: 16rpx;
        }
      }

      .selected-list {
        display: flex;
        justify-content: flex-end;
        // ::v-deep .input-arrow {
        //   margin-left: 20rpx;
        //   width: 15rpx;
        //   height: 15rpx;
        //   margin-top: 30rpx;
        // }
      }

      ::v-deep .input-value {
        font-size: 30rpx;
      }

      :deep(.uni-data-tree) {
        .input-value-border {
          border: none;
        }

        .placeholder {
          font-size: 30rpx;
          color: #c0c4cc;
          justify-content: flex-end;
        }

        .arrow-area {
          transform: rotate(225deg);
          margin-bottom: 0;
        }
      }

      .picker-input {
        text-align: right;
        height: 35px;
        color: $uni-text-color;
        font-size: 30rpx;
        align-items: center;
        display: flex;
        justify-content: flex-end;

        .iconfont {
          padding-right: 16rpx;
          margin-top: 7rpx;
          transform: rotate(180deg);
          font-size: 24rpx;
          color: #c0c4cc;
        }
      }

      .picker-input-placeholder {
        color: #c0c4cc;
      }

      .input-right-conment {
        text-align: right;
        height: 35px;
        line-height: 35px;
        color: $uni-text-color;
      }
    }

    .money-from {
      ::v-deep .uni-forms-item {
        // border-radius: 12rpx 12rpx 0 0;
      }

      .money-from-daxie {
        color: #c0c4cc;
        margin-bottom: 20rpx;
        padding: 0 24rpx 36rpx 24rpx;
        // border-radius: 0 0 12rpx 12rpx;
        background-color: #fff;
      }
    }
  }

  .min-width {
    width: 100rpx;
  }
</style>