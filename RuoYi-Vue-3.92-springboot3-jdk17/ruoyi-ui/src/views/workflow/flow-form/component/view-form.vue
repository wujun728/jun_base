<template>
  <el-row :gutter="24">
    <el-form ref="form" label-position="right" :label-width="labelWidth">
      <el-col v-for="item in formViewList" :key="item.key" :span="item.span">
        <el-form-item :label="item.label" :prop="item.prop">
          <template slot="label">
            <span v-if="item.required" class="required">*</span>
            {{ item.label }}
          </template>
          <span v-if="item.tag === 'el-radio-group'">
            <el-radio-group v-model="item.value">
              <el-radio :label="r.value" v-for="r in item.radioList" :key="r.value" :disabled="item.value === r.value ? false : true">{{ r.label }}</el-radio>
            </el-radio-group>
          </span>
          <span v-else-if="item.tag === 'el-checkbox-group'" class="disabCheck">
            <el-checkbox-group v-model="item.value">
              <el-checkbox :label="c.value" v-for="c in item.checkList" :key="c.value" disabled>{{ c.label }}</el-checkbox>
            </el-checkbox-group>
          </span>
          <span v-else-if="item.tag === 'el-switch'">
            <el-switch v-model="item.value" disabled></el-switch>
          </span>
          <span v-else-if="item.tag === 'el-slider'">
            <el-slider v-model="item.value" disabled></el-slider>
          </span>
          <span v-else-if="item.tag === 'el-rate'">
            <el-rate v-model="item.value" disabled show-score text-color="#ff9900"></el-rate>
          </span>
          <span v-else-if="item.tag === 'el-color-picker'">
            <el-color-picker v-model="item.value" disabled></el-color-picker>
          </span>
          <span v-else-if="item.tag === 'design-user-select'">
            <div class="text-content" v-if="item.value && item.value.length >0">
              <span v-for="(line, index) in item.value" :key="line.userId" class="org-select">
                <el-avatar :size="24" :src="avatar(line.avatar)" />
                <span class="ml2">{{ line.nickName }}</span>
                <span v-if="index < item.value.length-1">、</span>
              </span>
            </div>
          </span>
          <span v-else-if="item.tag === 'design-dept-select'">
            <div class="text-content" v-if="item.value && item.value.length >0">
              <span v-for="(line, index) in item.value" :key="line.id" class="org-select">
                <span class="ml2">{{ line.label }}</span>
                <span v-if="index < item.value.length-1">、</span>
              </span>
            </div>
          </span>
          <div v-else class="text-content">{{ item.value }}</div>
        </el-form-item>
      </el-col>
    </el-form>
  </el-row>
</template>

<script>
import { deepClone } from "@/utils/index";
import { StrUtil } from "@/utils/StrUtil";

export default {
  name: "ViewForm",
  data() {
    return {
      // label宽度默认128
      labelWidth: "138px",
      // 表单数据集
      formViewList: [],
      baseApi: process.env.VUE_APP_BASE_API,
    };
  },
  props: {
    // 表单配置
    formConf: {
      type: Object,
      default: () => {},
    },
    // 表单值
    formData: {
      type: Object,
      default: () => {},
    },
  },
  watch: {
    formConf: {
      handler(val) {
        if (val) {
          this.init();
        }
      },
      immediate: true,
    },
  },
  mounted() {
    this.init();
  },
  methods: {
    /** 初始化 */
    init() {
      // 深拷贝
      const formConfCopy = deepClone(this.formConf);
      // 固定138px，如有需要，可使用下面这行代码
      // this.labelWidth = formConfCopy.labelWidth + "px";

      const fields = formConfCopy.fields;
      if (!fields || fields.length === 0) return;
      const formDataCopy = deepClone(this.formData);
      let fieldList = [];
      fields.map((item) => {
        const _config = item.__config__;
        let field = {
          prop: item.__vModel__,
          label: _config.label + "：",
          tag: _config.tag,
          key: _config.renderKey,
          span: _config.span || 24,
          value: this.getValue(item, formDataCopy),
          required: _config.required || false,
          radioList: this.getCheckGroup(item),
          checkList: this.getCheckGroup(item),
        };
        fieldList.push(field);
      });
      this.formViewList = fieldList;
    },
    /** 获取值 */
    getValue(conf, formDataCopy) {
      let value;
      const _config = conf.__config__;
      const prop = conf.__vModel__;
      switch (_config.tag) {
        case "el-select": // 下拉选项
          const slot = conf.__slot__;
          const selectVal = StrUtil.isBlank(_config.defaultValue) ? formDataCopy[prop] : _config.defaultValue;
          if (slot && slot.options) {
            slot.options.map((o) => {
              if (o.value == selectVal) {
                value = o.label;
              }
            });
          }
          break;
        case "el-cascader": // 级联选择
          const separator = StrUtil.isBlank(conf.separator) ? " / " : " " + conf.separator + " ";
          const cascaderVal = _config.defaultValue && _config.defaultValue.length ? _config.defaultValue : formDataCopy[prop];
          const options = conf.options;
          value = this.getCascadeLabel(cascaderVal, options, separator);
          break;
        case "el-date-picker": // 日期选择
          value = this.getDatePickerValue(conf, formDataCopy, prop);
          break;
        case "el-time-picker": // 时间选择
          value = this.getTimePickerValue(conf, formDataCopy, prop);
          break;
        // case "design-user-select":
        //   value = this.getUserSelectValue(conf, formDataCopy, prop);
        //   break;
        default:
          value = _config.defaultValue ? _config.defaultValue : formDataCopy[prop];
      }
      return value;
    },
    /**
     * 级联标签路径生成方法
     * @param {Array} ids 选中的ID路径，如 [1, 2]
     * @param {Array} options 级联数据源
     * @returns {String} 拼接后的中文路径
     */
    getCascadeLabel(ids, options, separator) {
      const labels = [];
      let currentLevel = options || [];
      for (const id of ids) {
        const foundItem = currentLevel.find((item) => item.id === id);
        if (!foundItem) break;
        labels.push(foundItem.label);
        currentLevel = foundItem.children || [];
      }
      return labels.join(separator);
    },
    /** 获取选择项（单选、多选） */
    getCheckGroup(conf) {
      const slot = conf.__slot__;
      return slot && slot.options ? slot.options : [];
    },
    /** 日期选择值 */
    getDatePickerValue(conf, formDataCopy, prop) {
      const _config = conf.__config__;
      if (conf.type && conf.type === "daterange") {
        const dateScopeList = _config.defaultValue && _config.defaultValue.length ? _config.defaultValue : formDataCopy[prop];
        return dateScopeList[0] + " 至 " + dateScopeList[1];
      }
      return StrUtil.isBlank(_config.defaultValue) ? formDataCopy[prop] : _config.defaultValue;
    },
    /** 获取时间选择值 */
    getTimePickerValue(conf, formDataCopy, prop) {
      const _config = conf.__config__;
      if (conf["is-range"]) {
        const timeScopeList = _config.defaultValue && _config.defaultValue.length ? _config.defaultValue : formDataCopy[prop];
        return timeScopeList[0] + " 至 " + timeScopeList[1];
      }
      return StrUtil.isBlank(_config.defaultValue) ? formDataCopy[prop] : _config.defaultValue;
    },
    /** 获取选人组件值 */
    getUserSelectValue(conf, formDataCopy, prop) {
      const dataArr = formDataCopy[prop]; // 获取表单中的值
      if (Array.isArray(dataArr) && dataArr.length > 0) {
        return dataArr.map((user) => user.nickName || user.name || "未知用户").join("、");
      }
      return "";
    },
    /** 头像处理 */
    avatar(val) {
      if (StrUtil.isBlank(val)) return;
      return this.baseApi + val;
    },
  },
};
</script>

<style scoped type="scss">
::v-deep .el-form-item__content {
  font-size: 14px;
  color: #3f3f3f;
}

.text-content {
  display: block;
  word-wrap: anywhere;
  hyphens: auto; /* 英文单词智能断字 */
  text-align: justify; /* 两端对齐优化 */
}

::v-deep .el-form-item__label {
  font-size: 14px;
  color: #1890ff;
  font-weight: 500;
}

.required {
  font-size: 15px;
  color: red;
}

.org-select {
  display: inline-flex;
  align-items: center;
  margin-right: 3px;
  /* background: #e9f2ff; */
  position: relative;
}

/* 多选框样式调整 */
.disabCheck >>> .el-checkbox__input.is-disabled.is-checked .el-checkbox__inner::after {
  border-color: #fff;
}
.disabCheck >>> .el-checkbox__input.is-disabled.is-checked .el-checkbox__inner {
  background-color: #409eff;
  border-color: #409eff;
}
.disabCheck >>> .el-checkbox__input.is-disabled + span.el-checkbox__label {
  color: #606266;
}
.disabCheck >>> .el-checkbox__label {
  font-size: 12px;
}
</style>