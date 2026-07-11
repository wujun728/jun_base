<template>
  <div class="app-container">
    <el-form ref="businessForm" :model="info" :rules="rules" label-width="128px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="表单类型" prop="formType">
            <template>
              <el-select v-model="info.formType" placeholder="请选择" clearable filterable style="width: 240px" @change="changeFormType">
                <el-option v-for="dict in dict.type.workflow_template_form_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </template>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="info.formType !== '1'" label="表单编码" prop="formCode">
            <template>
              <el-input placeholder="请输入表单编码" v-model="info.formCode" style="width: 240px;" />
            </template>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="关联表单" prop="formId">
            <template>
              <el-select v-model="info.formId" clearable filterable placeholder="请选择">
                <el-option v-for="form in formOptions" :key="form.formId" :label="form.formName" :value="form.formId">
                  <span style="float: left">{{ form.formName }}</span>
                </el-option>
              </el-select>
            </template>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import { getDynaFormOptionSelect } from "@/api/workflow/dynamicForm";

export default {
  props: {
    info: {
      type: Object,
      default: null,
    },
    formType: {
      type: String,
      default: "",
    },
    dynamicForm: {
      type: Object,
      default: null,
    },
  },
  dicts: ["workflow_template_form_type"],
  data() {
    return {
      rules: {
        formType: [{ required: true, message: "请选择表单类型", trigger: "blur" }],
        formCode: [{ required: true, message: "请选择表单编码", trigger: "blur" }],
        formId: [{ required: true, message: "请选择关联表单", trigger: "blur" }],
      },
      formOptions: [],
      tempFormType: "",
    };
  },
  watch: {
    dynamicForm: {
      handler(newValue) {
        if (Object.keys(newValue).length > 0) {
          this.handleOldDynamicOption();
        }
      },
    },
  },
  created() {
    this.getOptionSelect();
  },
  methods: {
    //动态表单下拉列表
    async getOptionSelect() {
      await getDynaFormOptionSelect().then((response) => {
        this.formOptions = response.data;
      });
    },
    /** 处理已选动态表单回显 */
    async handleOldDynamicOption() {
      if (this.formOptions.length === 0) {
        await this.getOptionSelect();
      }
      let oldList = this.formOptions.filter((item) => item.formId === this.dynamicForm.id);
      if (!oldList || oldList.length === 0) {
        let oldForm = this.formOptions.filter((item) => item.formKey === this.dynamicForm.formKey)[0];
        let formName = this.dynamicForm.name;
        if (oldForm) {
          formName = oldForm.formName + "(旧版本)";
        }
        let form = { formId: this.dynamicForm.id, formName: formName, formKey: this.dynamicForm.formKey };
        this.formOptions.push(form);
      }
    },
    /** 改变表单类型 */
    changeFormType(val) {
      if (val === "1") {
        this.$set(this.info, "formCode", "dynamic");
      } else {
        this.$set(this.info, "formCode", "");
      }
    },
  },
};
</script>
