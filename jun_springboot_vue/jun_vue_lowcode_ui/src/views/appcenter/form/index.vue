<template>
  <div class="app-container">
    <fm-generate-form
      :data="jsonData"
      ref="generateForm"
      style="width: 800px; display: flex"
    >
    </fm-generate-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitMyForm">提 交</el-button>
      <el-button @click="close">重 置</el-button>
    </div>
  </div>
</template>

<script>
import { listDef, getDef } from "@/api/form/form";
import { start } from "@/api/flow/flow";

export default {
  name: "AppList",
  data() {
    return {
      jsonData: {},
      formId: "",
      procKey: "",
      formName: "",
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 单定义表格数据
      defList: [],
      // 弹出层标题
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        defination: null,
        refProcKey: null,
        createName: null,
      },
      // 表单参数
      form: { procKey: null, formId: null, formName: null, data: null },
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.formId = this.$route.params.formId;
    getDef(this.formId).then((response) => {
      this.jsonData = JSON.parse(response.data.defination);
      this.procKey = response.data.refProcKey;
      this.formName = response.data.name;
    });
  },
  methods: {
    // 取消按钮
    close() {
      this.$refs.generateForm.reset();
      this.form = {};
    },
    submitMyForm() {
      this.$refs.generateForm
        .getData()
        .then((data) => {
          this.form.data = JSON.stringify(data);
          this.form.procKey = this.procKey;
          this.form.formId = this.formId;
          this.form.formName = this.formName;
          start(this.form).then((response) => {
            this.$modal.msgSuccess("提交成功");
            this.close();
          });
        })
        .catch((e) => {});
    },
  },
};
</script>
