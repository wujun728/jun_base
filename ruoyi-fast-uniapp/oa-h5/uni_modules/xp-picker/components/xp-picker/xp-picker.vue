<template>
  <view class="xp-h-full">
    <view @tap="show" class="xp-h-full">
      <slot>
        <view class="picker-label xp-h-full" :class="{'is-placeholder':label===placeholder}">{{label}}
        </view>
      </slot>
    </view>
    <view class="xp-picker" :style="{'visibility':pickerVisible?'visible':'hidden'}">
      <view class="xp-picker-mask" :class="{'xp-picker-animation':animation}" :style="{'opacity':pickerVisible?0.6:0}"
        @tap="_cancel"></view>
      <view class="xp-picker-container"
        :class="{'xp-picker-container--show':pickerVisible,'xp-picker-animation':animation}">
        <view v-if="actionPosition==='top'" class="xp-picker-action">
          <view class="xp-picker-action--cancel" @tap="_cancel">取消</view>
          <view class="xp-picker-action--confirm" @tap="_confirm">确定</view>
        </view>
        <view v-if="isError" class="xp-picker-error" :style="{'height':height+'vh'}">
          <text>（请检查你的配置 或 查看控制台错误信息）</text>
        </view>
        <!-- #ifdef VUE3 -->
        <picker-view v-else style="margin-top: 40rpx;" :style="{'height':height+'vh'}" indicator-style="height:50px;"
          :value="selected" @change="_change">
          <picker-view-column v-for="(k,i) in modeArr" :key="cols[i].length" class="xp-picker-column">
            <view class="xp-picker-list-item" v-for="(item,index) in cols[i]" :key="index">
              {{item+units[i]}}
            </view>
          </picker-view-column>
        </picker-view>
        <!-- #endif -->
        <!-- #ifdef VUE2 -->
        <picker-view v-else style="margin-top: 40rpx;" :style="{'height':height+'vh'}" indicator-style="height:50px;"
          :value="selected" @change="_change">
          <picker-view-column v-for="(k,i) in modeArr" :key="k" class="xp-picker-column">
            <view class="xp-picker-list-item" v-for="(item,index) in cols[i]" :key="index">
              {{item+units[i]}}
            </view>
          </picker-view-column>
        </picker-view>
        <!-- #endif -->
        <view v-if="actionPosition==='bottom'" class="xp-picker-btns">
          <button class="xp-button xp-button--cancel" @tap="_cancel">取消</button>
          <button class="xp-button xp-button--confirm" @tap="_confirm">确定</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
  import {
    templateFactory,
    getLocalTime,
    fmtNumber,
    time2Timestamp,
    getDate,
    getForm
  } from "./util.js"
  import assert from './assert.js'
  export default {
    name: 'XpPicker',
    data() {
      return {
        isError: false,
        pickerVisible: false,
        cols: [],
        selected: []
      }
    },
    props: {
      mode: {
        type: String,
        default: "ymd"
      },
      animation: {
        type: Boolean,
        default: true
      },
      height: {
        type: [Number, String],
        default: 35
      },
      actionPosition: {
        type: String,
        default: "bottom"
      },
      yearRange: {
        type: Array,
        default: () => [2010, null]
      },
      value: String,
      // #ifdef VUE3
      modelValue: String,
      // #endif
      history: Boolean,
      placeholder: {
        type: String,
        default: '请选择'
      }
    },
    watch: {
      mode() {
        this.render()
      }
    },
    // #ifdef VUE3
    emits: ['confirm', 'update:modelValue'],
    // #endif
    computed: {
      modeArr() {
        return this.mode.split("")
      },
      units() {
        const arr = []
        for (const k in this.template) {
          if (this.mode.indexOf(k) !== -1) arr.push(this.template[k].text)
        }
        return arr
      },
      label() {
        const val = this.value || this.modelValue
        if (val) return val
        return this.placeholder
      }
    },
    created() {
      this.bindForm()

      this.isConfirm = false
      this.template = {}
      this.render()
      // #ifdef VUE2
      if (this.value) {
        this.setSelected(this.value)
      }
      // #endif
      // #ifdef VUE3
      if (this.modelValue) {
        this.setSelected(this.modelValue)
      }
      // #endif
    },
    methods: {
      bindForm() {
        this.form = getForm.call(this, 'uniForms')
        this.formItem = getForm.call(this, 'uniFormsItem')
        if (this.form && this.formItem) {
          if (this.formItem.name) {
            if (!this.is_reset) {
              this.is_reset = false
              // #ifdef VUE2
              this.formItem.setValue(this.value)
              // #endif
              // #ifdef VUE3
              this.formItem.setValue(this.modelValue)
              // #endif
            }
            this.form.inputChildrens.push(this)
          }
        }
      },
      render() {
        try {
          assert(this)
          this.template = templateFactory(this) //生成所需列 默认模板
          this.initCols() //根据模板 初始化列
          this.initSelected() //设置默认值
        } catch (e) {
          console.error(e)
          this.isError = true
        }
      },
      initCols() {
        for (const k of this.mode) {
          const range = this.template[k].range
          this.fillCol(k, ...range)
        }
      },
      initSelected() {
        let dt
        // #ifdef VUE2
        dt = this.value
        // #endif
        // #ifdef VUE3
        dt = this.modelValue
        // #endif
        if (!dt) dt = getLocalTime(this.mode)
        if (!dt) return
        this.setSelected(dt)
      },
      fillCol(k, s, e) {
        const index = this.mode.indexOf(k)
        let arr = []
        for (let i = s; i <= e; i++)
          arr.push(fmtNumber(i))
        // #ifdef VUE2
        this.$set(this.cols, index, arr)
        // #endif
        // #ifdef VUE3
        this.cols[index] = arr
        // #endif
      },
      //dt 时间字符串 如 '2020-02-16'
      setSelected(dt) {
        const arr = dt.split(/-|:|\s/)
        const a = this.cols
        for (let i = 0; i < a.length; i++)
          this.$set(this.selected, i, a[i].indexOf(arr[i]))
      },
      show() {
        if ((!this.value && !this.modelValue) || (!this.history) || (this.history && !this.isConfirm)) {
          this.initSelected()
        }
        this.pickerVisible = true
      },
      _resolveCurrentDt() {
        let str = ""
        for (let i = 0; i < this.selected.length; i++)
          str += this.cols[i][this.selected[i]] + this.units[i]
        let dt = str
          .replace('年', '-')
          .replace('月', '-')
          .replace('日', ' ')
          .replace('时', ':')
          .replace('分', ':')
          .replace('秒', '')
        if (!this.mode.endsWith('s'))
          dt = dt.substring(0, dt.length - 1)
        return dt
      },
      _confirm() {
        const result = this._getResult()
        const val = result.value
        if (!this.isError) {
          // #ifdef VUE2
          this.$emit('input', val)
          // #endif
          // #ifdef VUE3
          this.$emit('update:modelValue', val)
          // #endif
          this.$emit('confirm', result)
          this.isConfirm = true
          if (this.formItem) this.formItem.setValue(val)
        }
        this.pickerVisible = false
      },
      _getResult() {
        const value = this._resolveCurrentDt()
        const detail = {
          value
        }
        const tp = time2Timestamp(value)
        if (!isNaN(tp)) detail.timestamp = tp
        return detail
      },
      _cancel() {
        this.pickerVisible = false
      },
      _change(e) {
        let col;
        const newValue = e.detail.value
        for (let i = 0; i < newValue.length; i++) {
          if (newValue[i] !== this.selected[i]) {
            col = this.modeArr[i]
            break
          }
        }
        this.selected = newValue
        const index = this.mode.indexOf("d")
        if (index !== -1 && (col === 'y' || col === 'm')) {
          const currentDt = this._resolveCurrentDt()
          this.fillCol("d", 1, getDate(currentDt))
        }
      }
    }
  }
</script>

<style scoped lang="scss">
  .xp-h-full {
    height: 100%;
  }

  .picker-label {
    height: 100%;
    display: flex;
    align-items: center;
    padding-left: 10px;
    font-size: 13px;
  }

  .is-placeholder {
    color: #999;
  }

  .xp-picker {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 999;
    font-size: 30rpx;
  }

  .xp-picker-container {
    position: fixed;
    bottom: 0;
    transform: translateY(100%);
    z-index: 999;
    width: 100%;
    background-color: #fff;
    visibility: hidden;
  }

  .xp-picker-container--show {
    transform: translateY(0);
    visibility: visible;
  }

  .xp-picker-mask {
    z-index: 998;
    width: 100%;
    height: 100%;
    background-color: rgb(0, 0, 0);
  }

  .xp-picker-animation {
    transition: all 0.25s;
  }

  .xp-picker-error {
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: #ff0000
  }

  .xp-picker-action {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 90rpx;
    padding: 0 28rpx;
    box-sizing: border-box;
    position: relative;
    font-size: 30rpx;
    border-bottom: 0.5px solid #e5e5e5
  }

  .xp-picker-btns {
    width: 100%;
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding: 40rpx 30rpx;
    box-sizing: border-box;
    position: relative;
  }

  .xp-button {
    line-height: 2.3;
    font-size: 32rpx;
    margin: 0;
    padding: 0 80rpx;
    transform: translate(0upx, 0upx);
  }

  .xp-button:active:not([disabled]) {
    transform: translate(1upx, 1upx);
  }

  .xp-button:after {
    border: none;
  }

  .xp-button--cancel {
    background-color: $uni-default-bg;
    color: #47a16e;
  }

  .xp-button--confirm {
    background-color: #47a16e;
    color: #fff;
  }

  .xp-picker-action--cancel {
    opacity: .7;
  }

  .xp-picker-action--confirm {
    color: #007aff;
  }

  .xp-picker-column {
    text-align: center;
    border: none;
    font-size: 30rpx;
  }

  .xp-picker-list-item {
    display: flex;
    justify-content: center;
    align-items: center;
    // height: 40px;
  }
</style>