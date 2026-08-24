<!-- @FileDescription: 人事-审批设置-明细预览组件 -->
<template>
  <div class="ab-container">
    <div v-for="(item, idx) in cacheRule" :key="idx">
      <el-alert
        v-if="idx !== Object.keys(cacheRule)[0] && !formCreateInject.preview"
        :key="'a' + idx"
        :title="'明细' + (Object.keys(cacheRule).indexOf(idx) + 1)"
        type="info"
        close-text="删除"
        @close="del(idx)"
      />
      <div v-if="formCreateInject.preview" class="num">{{ '明细' + (Object.keys(cacheRule).indexOf(idx) + 1) }}</div>
      <component
        :is="type"
        :key="'b' + idx"
        :rule="item.rule"
        :extend-option="true"
        :option="item.options"
        @update:value="(val) => formData(idx, val)"
        @input="($f) => add$f(idx, $f)"
      />
      <el-divider v-if="Object.keys(cacheRule).indexOf(idx) - 1"></el-divider>
    </div>
    <ElButton v-if="!formCreateInject.preview" type="text" @click="add">添加明细</ElButton>
  </div>
</template>

<script>
export default {
  name: 'ApprovalBill',
  formCreateParser: {
    loadChildren: false,
    renderChildren(ctx) {
      return []
    }
  },
  props: {
    formCreateInject: {
      type: Object,
      default: () => ({})
    },
    value: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      rule: this.formCreateInject.rule.children,
      len: 0,
      cacheRule: {},
      cacheValue: {},
      type: undefined
    }
  },
  watch: {
    value(n) {
      n = n || []
      const keys = Object.keys(this.cacheRule)
      const total = keys.length
      const len = total - n.length
      if (len < 0) {
        for (let i = len; i < 0; i++) {
          this.addRule(n.length + i)
        }
        for (let i = 0; i < total; i++) {
          this.setValue(keys[i], n[i])
        }
      } else {
        if (len > 0) {
          for (let i = 0; i < len; i++) {
            this.removeRule(keys[total - i - 1])
          }
          this.subForm()
        }
        n.forEach((val, i) => {
          this.setValue(keys[i], n[i])
        })
      }
    }
  },
  created() {
    this.type = this.formCreateInject.form.$form()
    if (!this.value.length) this.expandRule(1)
    for (let i = 0; i < this.value.length; i++) {
      this.addRule(i)
    }
  },
  methods: {
    cache(k, val) {
      this.cacheValue[k] = JSON.stringify(val)
    },
    input(value) {
      this.$emit('input', value)
      this.$emit('change', value)
    },
    formData(key, formData) {
      const cacheRule = this.cacheRule
      const keys = Object.keys(cacheRule)
      if (keys.filter((k) => cacheRule[k].$f).length !== keys.length) {
        return
      }
      const value = keys.map((k) => {
        const data = key === k ? formData : { ...this.cacheRule[k].$f.form }
        this.cache(k, data)
        return data
      })
      this.input(value)
    },
    setValue(key, value) {
      if (this.cacheValue[key] === JSON.stringify(value)) {
        return
      }
      this.cache(key, value)
      this.cacheRule[key].$f && this.cacheRule[key].$f.coverValue(value || {})
    },
    addRule(i, emit) {
      const rule = this.formCreateInject.form.copyRules(this.rule)
      const options = this.options
        ? { ...this.options }
        : {
            submitBtn: false,
            resetBtn: false
          }
      options.formData = this.value[i]
      this.$set(this.cacheRule, ++this.len, { rule, options })
      if (emit) {
        this.$nextTick(() => this.$emit('add', rule, Object.keys(this.cacheRule).length - 1))
      }
    },
    add$f(key, $f) {
      this.cacheRule[key].$f = $f
      this.subForm()
    },
    subForm() {
      this.formCreateInject.subForm(Object.keys(this.cacheRule).map((k) => this.cacheRule[k].$f))
    },
    removeRule(key, emit) {
      const index = Object.keys(this.cacheRule).indexOf(key)
      this.$delete(this.cacheRule, key)
      this.$delete(this.cacheValue, key)
      if (emit) {
        this.$nextTick(() => this.$emit('remove', index))
      }
    },
    add(i) {
      this.addRule(i, true)
    },
    del(key) {
      const i = Object.keys(this.cacheRule).indexOf(key)
      this.removeRule(key, true)
      this.subForm()
      this.value.splice(i, 1)
      this.input(this.value)
    },
    expandRule(n) {
      for (let i = 0; i < n; i++) {
        this.value.push(this.field ? null : {})
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.ab-container {
  .num {
    font-size: 14px;
    color: #000000;
  }
  position: relative;
  min-height: 177px;
  ::v-deep .el-alert {
    height: 32px;
    margin-bottom: 17px;
    background-color: #f2f9fe;
  }

  ::v-deep .el-alert__title {
    color: #000;
  }

  ::v-deep .el-alert.is-light .el-alert__closebtn {
    top: 0;
    color: #f5222d;
    line-height: 32px;
  }

  ::v-deep .el-form-item--medium .el-form-item__label {
    text-align: right;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: #606266;
    line-height: 40px;
    padding: 0 12px 0 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
  }
}
</style>
