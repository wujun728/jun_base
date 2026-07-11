<template>
  <div class="today-task">
    <div class="task-header">
      <span>我的分类</span>
      <el-button type="text" icon="el-icon-plus" @click="handleAdd">新增</el-button>
    </div>

    <div class="today-task-scroll">
      <custom-radio ref="customRadio" :type-list="typeList" @checked="typeSelect" @handle-update="handleUpdate" @handle-delete="handleDelete" />
    </div>

    <!-- 新增分类 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="92px">
        <el-form-item label="分类名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="主题颜色" prop="color">
          <el-color-picker v-model="form.color" @change="colorChange" />
          <div class="item-remark">说明：此颜色用于右侧日历中的日程标题颜色，默认黑色字体</div>
        </el-form-item>
        <el-form-item label="标签名" prop="tagName">
          <el-input v-model="form.tagName" placeholder="显示在日程标题前面，不填则不显示" style="width: 300px" />
        </el-form-item>
        <el-form-item label="标签样式" prop="tagEffect">
          <el-select v-model="form.tagEffect">
            <el-option v-for="item in tagEffectList" :key="item.value" :value="item.value" :label="item.label" />
          </el-select>
        </el-form-item>
        <el-form-item label="标签颜色" prop="tagType">
          <el-select v-model="form.tagType">
            <el-option v-for="item in tagStyleList" :key="item.value" :value="item.value" :label="item.label" />
          </el-select>
          <div class="ml10">
            示例：
            <el-tag v-if="form.tagName" size="mini" effect="light" :type="form.tagType" :effect="form.tagEffect">{{ form.tagName }}</el-tag>
            <span class="ml5" :style="topStyle">这是一条测试日程！</span>
          </div>
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
import CustomRadio from "../custom-radio.vue";
import { listAll, getType, delType, addType, updateType } from "@/api/schedule/type";

export default {
  components: { CustomRadio },
  data() {
    return {
      activeName: "",
      // 日程分类表格数据
      typeList: [],
      // 是否显示弹出层
      open: false,
      // 弹出层标题
      title: "",
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [{ required: true, message: "名称不能为空", trigger: "blur" }],
        color: [{ required: true, message: "主题颜色不能为空", trigger: "blur" }],
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 5,
        typeId: "",
      },
      topStyle: {},
      // 标题主题
      tagEffectList: [
        { label: "朴素", value: "plain" },
        { label: "浅色", value: "light" },
        { label: "深色", value: "dark" },
      ],
      // 标签颜色
      tagStyleList: [
        { label: "蓝色", value: "" },
        { label: "绿色", value: "success" },
        { label: "灰色", value: "info" },
        { label: "黄色", value: "warning" },
        { label: "红色", value: "danger" },
      ],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询日程分类列表 */
    getList() {
      this.loading = true;
      listAll(this.queryParams).then((response) => {
        this.typeList = response.data || [];
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        title: null,
        tagName: null,
        tagType: "",
        tagEffect: "plain",
        color: "#303133",
      };
      this.resetForm("form");
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加日程分类";
    },
    /** 修改按钮操作 */
    handleUpdate(id) {
      this.reset();
      getType(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改日程分类";
      });
    },
    /** 删除按钮操作 */
    handleDelete(id) {
      this.$modal
        .confirm("确定删除此分类吗？")
        .then(function () {
          return delType(id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 分类提交 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateType(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
              this.$emit("type-change", this.queryParams.typeId);
            });
          } else {
            addType(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
              this.$emit("type-change", null);
            });
          }
        }
      });
    },
    colorChange(val) {
      this.topStyle.color = val;
    },
    /** 类型切换 */
    typeSelect(val) {
      this.queryParams.typeId = val;
      this.getList();
      this.$emit("type-change", val);
    },
    /** 日历月份变化时，重置参数 */
    resetType() {
      this.$refs.customRadio.reset();
    },
  },
};
</script>

<style scoped>
.today-task {
  margin-top: 10px;
  height: calc(88vh - 436px);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}
.task-header {
  padding: 5px 12px 3px 0px;
  font-size: 14px;
  font-weight: bold;
  color: #303133;
  border-bottom: 1px solid #ebeef5;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.today-task-scroll {
  margin-top: 5px;
  flex: 1;
  overflow-y: auto;
}

.item-remark {
  line-height: 0;
  font-size: 12px;
  color: #909399;
  margin-bottom: 5px;
}
</style>