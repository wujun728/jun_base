<!-- @FileDescription: 人事-审批设置-省市区组 -->
<template>
  <div class="timeFrom">
    <div class="el-form-item el-form-item--mini">
      <label class="el-form-item__label" style="width: 120px">
        <span
          v-if="
            formCreateInject && formCreateInject.rule
              ? formCreateInject.rule.effect.required
              : formCreateInject.effect.required
          "
          class="color-tab"
          >*</span
        >
        <span>省市区</span>
      </label>
      <div class="el-form-item__content" style="margin-left: 120px">
        <template v-if="!check">
          <el-cascader
            v-model="timeData.city"
            size="small"
            :options="cityData"
            style="width: 100%"
            clearable
            @change="changeDuration"
            :props="{ value: 'label', label: 'label' }"
          />
        </template>
        <span v-if="check">{{ timeData.city[0] }}-{{ timeData.city[1] }}-{{ timeData.city[2] }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { getCityListApi } from '@/api/public'
export default {
  name: 'Index',
  props: {
    formCreateInject: Object,
    value: {
      type: Object,
      default: () => ({})
    },
    disabled: Boolean,
    timeType: {
      type: String,
      default: () => 'day'
    },
    time: {
      type: String,
      default: () => ''
    },
    titleIpt: {
      type: String,
      default: () => '省市区'
    },
    check: {
      type: Boolean,
      default: false
    }
  },
  mounted() {
    if (!localStorage.getItem('city')) {
      this.getCityList()
    }
  },
  data() {
    return {
      cityData: JSON.parse(localStorage.getItem('city')),
      title: this.titleIpt,
      timeData: {
        city: this.value.city
      }
    }
  },
  watch: {
    value(n) {
      this.timeData = n
    }
  },

  methods: {
    getCityList() {
      getCityListApi().then((res) => {
        localStorage.setItem('city', JSON.stringify(res.data))
      })
    },
    changeDuration() {
      this.$emit('input', this.timeData)
    }
  }
}
</script>

<style scoped lang="scss">
.el-form--label-top .el-form-item__label {
  float: none;
  display: inline-block;
  text-align: left;
  padding: 0 0 10px 0;
}
.timeFrom {
  padding-top: 10px;
}
.timeFrom {
  ::v-deep .el-date-editor,
  ::v-deep .el-date-editor--date,
  ::v-deep .el-select {
    width: 220px;
  }
}
.timeFrom ::v-deep .el-form-item__error {
  margin-left: 100px;
}
</style>
