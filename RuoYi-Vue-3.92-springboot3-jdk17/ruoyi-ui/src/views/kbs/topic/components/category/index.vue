<template>
  <!-- 添加或修改知识库主题类别对话框 -->
  <div>
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :before-close="cancel">
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入主题类别" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序" />
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
import { getCategory, addCategory, updateCategory } from "@/api/kbs/topic/category";

export default {
  name: "Category",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 知识库主题类别表格数据
      categoryList: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sort: [{ required: true, message: "排序不能为空", trigger: "blur" }],
        name: [{ required: true, message: "主题类别名称不能为空", trigger: "blur" }],
      },
    };
  },
  props: {
    open: false,
    title: "",
    formData: {
      type: Object,
      default: () => {},
    },
  },
  watch: {
    formData: {
      handler(obj) {
        if (obj && "id" in obj) {
          this.form = JSON.parse(JSON.stringify(obj));
        }
      },
      deep: true,
      immediate: true,
    },
  },
  created() {},
  methods: {
    // 表单重置
    reset() {
      this.form = {
        sort: 1,
        name: null,
      };
      this.resetForm("form");
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getCategory(id).then((response) => {
        this.form = response.data;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateCategory(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.$emit("category-submit");
            });
          } else {
            addCategory(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.$emit("category-submit");
            });
          }
        }
      });
    },
    // 取消按钮
    cancel() {
      this.reset();
      this.$emit("category-close");
    },
  },
};
</script>
