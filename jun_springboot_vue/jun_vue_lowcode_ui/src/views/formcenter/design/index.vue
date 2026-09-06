<template>
  <div>
    <fm-making-form
      ref="makingform"
      style="height: 900px; display: flex"
      preview
      generate-code
      generate-json
    >
      <template slot="action">
        <el-button type="text" icon="el-icon-upload" @click="saveForm"
          >保存</el-button
        >
      </template>
    </fm-making-form>
    <!-- 添加或修改单定义对话框 -->
    <el-dialog :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="表单名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入表单名称" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addDef } from "@/api/form/form";

export default {
  name: "form-make",
  components: {},
  data() {
    return {
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
    };
  },
  created() {},
  methods: {
    saveForm() {
      this.open = true;
    },
    submitForm() {
      const json = this.$refs.makingform.getJSON();
      console.log(JSON.stringify(json));

      let data = {
        name: this.form.name,
        defination: JSON.stringify(json),
      };

      addDef(data).then((response) => {
        this.$modal.msgSuccess("新增成功");
        this.open = false;
      });
    },
    cancel() {
      this.open = false;
    },
  },
};
</script>
