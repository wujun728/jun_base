<script>
import { deepClone } from "../../utils/index";
import render from "../render/render.js";

const ruleTrigger = {
  "el-input": "blur",
  "el-input-number": "blur",
  "el-select": "change",
  "el-radio-group": "change",
  "el-checkbox-group": "change",
  "el-cascader": "change",
  "el-time-picker": "change",
  "el-date-picker": "change",
  "el-rate": "change",
};

const layouts = {
  colFormItem(h, scheme) {
    const config = scheme.__config__;
    const listeners = buildListeners.call(this, scheme);

    let labelWidth = config.labelWidth ? `${config.labelWidth}px` : null;
    if (config.showLabel === false) labelWidth = "0";
    if (scheme.tooltip !== undefined && scheme.tooltip !== "") {
      let tooltipStyle = " white-space: normal !important; word-break: break-word; max-width: 500px;line-height: 24px; ";
      return (
        <el-col span={config.span}>
          <el-form-item label-width={labelWidth} prop={scheme.__vModel__}>
            <span slot="label">
              {config.label}
              <el-tooltip placement="bottom">
                <div slot="content" style={tooltipStyle}>
                  {scheme.tooltip
                    .replace(/(\\r\\n|\\n|\\r)/g, "\n") // 处理所有常见转义形式
                    .split("\n")
                    .filter((line) => line.trim())
                    .map((line, index) => (
                      <div key={index}>{line}</div>
                    ))}
                </div>
                <i class="el-icon-info" style="margin-left:2px;color:#409EFF"></i>
              </el-tooltip>
            </span>
            <render conf={scheme} on={listeners} />
          </el-form-item>
        </el-col>
      );
    } else {
      return (
        <el-col span={config.span}>
          <el-form-item label-width={labelWidth} prop={scheme.__vModel__} label={config.showLabel ? config.label : ""}>
            <render conf={scheme} on={listeners} />
          </el-form-item>
        </el-col>
      );
    }
  },
  rowFormItem(h, scheme) {
    let child = renderChildren.apply(this, arguments);
    if (scheme.type === "flex") {
      child = (
        <el-row type={scheme.type} justify={scheme.justify} align={scheme.align}>
          {child}
        </el-row>
      );
    }
    return (
      <el-col span={scheme.span}>
        <el-row gutter={scheme.gutter}>{child}</el-row>
      </el-col>
    );
  },
};

function renderFrom(h) {
  const { formConfCopy } = this;

  return (
    <el-row gutter={formConfCopy.gutter}>
      <el-form
        size={formConfCopy.size}
        label-position={formConfCopy.labelPosition}
        disabled={formConfCopy.disabled}
        label-width={`${formConfCopy.labelWidth}px`}
        ref={formConfCopy.formRef}
        // model不能直接赋值 https://github.com/vuejs/jsx/issues/49#issuecomment-472013664
        props={{ model: this[formConfCopy.formModel] }}
        rules={this[formConfCopy.formRules]}
      >
        {renderFormItem.call(this, h, formConfCopy.fields)}
      </el-form>
    </el-row>
  );
}
/* 
function formBtns(h) {
  return (
    <el-col>
      <el-form-item size="small" style="float:right">
        <el-button type="primary" onClick={this.submitForm}>
          保存
        </el-button>
        <el-button onClick={this.resetForm}>重置</el-button>
      </el-form-item>
    </el-col>
  )
} */

function renderFormItem(h, elementList) {
  if (elementList) {
    return elementList.map((scheme) => {
      const config = scheme.__config__;
      const layout = layouts[config.layout];

      if (layout) {
        return layout.call(this, h, scheme);
      }
      throw new Error(`没有与${config.layout}匹配的layout`);
    });
  }
}

function renderChildren(h, scheme) {
  const config = scheme.__config__;
  if (!Array.isArray(config.children)) return null;
  return renderFormItem.call(this, h, config.children);
}

function setValue(event, config, scheme) {
  this.$set(config, "defaultValue", event);
  this.$set(this[this.formConf.formModel], scheme.__vModel__, event);
}

function buildListeners(scheme) {
  const config = scheme.__config__;
  const methods = this.formConf.__methods__ || {};
  const listeners = {};

  // 给__methods__中的方法绑定this和event
  Object.keys(methods).forEach((key) => {
    listeners[key] = (event) => methods[key].call(this, event);
  });

  // 响应 render.js 中的 vModel $emit('input', val)
  listeners.input = (event) => setValue.call(this, event, config, scheme);

  // 上传表单元素组件的成功、移除和预览事件
  if (config.tag === "el-upload") {
    listeners.upload = (response, file, fileList) => setUpload.call(this, config, scheme, response, file, fileList);

    listeners.deleteUpload = (file, fileList) => deleteUpload.call(this, config, scheme, file, fileList);

    listeners.previewUpload = (file, fileList) => window.open(file.url, "_blank");
  }

  return listeners;
}

//获取上传表单元素组件 上传的文件
function setUpload(config, scheme, response, file, fileList) {
  //response: 上传接口返回的数据
  let fileObj = {
    name: response.fileName,
    url: response.url,
    fileType: response.contentType,
  };
  let oldValue = "";
  try {
    oldValue = JSON.parse(this[this.formConf.formModel][scheme.__vModel__]);
  } catch (err) {
    console.warn(err);
  }

  if (oldValue) {
    oldValue.push(fileObj);
  } else {
    oldValue = [fileObj];
  }

  this.$set(config, "defaultValue", JSON.stringify(oldValue));
  this.$set(this[this.formConf.formModel], scheme.__vModel__, JSON.stringify(oldValue));
}

//获取上传表单元素组件 删除文件后的文件列表
function deleteUpload(config, scheme, file, fileList) {
  let oldValue = JSON.parse(this[this.formConf.formModel][scheme.__vModel__]);

  //file 删除的文件
  //过滤掉删除的文件
  let newValue = oldValue.filter((item) => item.name !== file.name);
  this.$set(config, "defaultValue", JSON.stringify(newValue));
  this.$set(this[this.formConf.formModel], scheme.__vModel__, JSON.stringify(newValue));
}

export default {
  components: {
    render,
  },
  props: {
    formConf: {
      type: Object,
      required: true,
    },
    valData: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    const data = {
      formConfCopy: deepClone(this.formConf),
      [this.formConf.formModel]: this.valData ? deepClone(this.valData) : {},
      [this.formConf.formRules]: {},
    };
    if (data.formConfCopy.fields) {
      this.initFormData(data.formConfCopy.fields, data[this.formConf.formModel]);
      this.buildRules(data.formConfCopy.fields, data[this.formConf.formRules]);
    }
    return data;
  },
  methods: {
    /** 初始化表单 */
    initFormData(componentList, formData) {
      componentList.forEach((item) => {
        const config = item.__config__;
        if (item.__vModel__ && formData && formData.hasOwnProperty(item.__vModel__)) config.defaultValue = formData[item.__vModel__];
        if (config.children) this.initFormData(config.children, formData);
      });
    },
    /** 创建校验规则 */
    buildRules(componentList, rules) {
      componentList.forEach((cur) => {
        const config = cur.__config__;
        if (Array.isArray(config.regList)) {
          if (config.required) {
            const required = { required: config.required, message: cur.placeholder };
            if (Array.isArray(config.defaultValue)) {
              required.type = "array";
              required.message = `请至少选择一个${config.label}`;
            }
            required.message === undefined && (required.message = `${config.label}不能为空`);
            config.regList.push(required);
          }
          rules[cur.__vModel__] = config.regList.map((item) => {
            item.pattern && (item.pattern = eval(item.pattern));
            item.trigger = ruleTrigger && ruleTrigger[config.tag];
            return item;
          });
        }
        if (config.children) this.buildRules(config.children, rules);
      });
    },
    /** 表单校验 */
    validateForm() {
      this.$refs[this.formConf.formRef].validate((valid) => {
        if (!valid) {
          return false;
        }
        return true;
      });
    },
    /** 父组件调用的校验表单方法 */
    refValidateForm() {
      return new Promise((resolve) => {
        this.$refs[this.formConf.formRef].validate((valid) => {
          resolve(valid);
        });
      });
    },
    /** 重置表单 */
    resetForm() {
      this.formConfCopy = deepClone(this.formConf);
      this.$refs[this.formConf.formRef].resetFields();
    },
    /**
     * 提交
     */
    submitForm() {
      this.$refs[this.formConf.formRef].validate((valid) => {
        if (!valid) return false;
        // 触发sumit事件
        // this.$emit('submit', this[this.formConf.formModel])
        const params = {
          formData: this.formConfCopy,
          valData: this[this.formConf.formModel],
        };
        this.$emit("submit", params);
        return true;
      });
    },
    // 传值给父组件
    getData() {
      this.$emit("getData", this[this.formConf.formModel]);
      // this.$emit('getData',this.formConfCopy)
    },
    // 获取表单数据
    getFormConf() {
      this.$emit("getFormConf", this.formConfCopy);
    },
  },
  render(h) {
    return renderFrom.call(this, h);
  },
};
</script>
