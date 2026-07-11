<template>
  <!-- 添加或修改知识库主题对话框 -->
  <div>
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body :before-close="cancel">
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="主题名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入主题名称" />
        </el-form-item>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="主题分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择主题类别" clearable>
                <el-option v-for="item in categorys" :key="item.categoryId" :label="item.categoryName" :value="item.categoryId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="可见范围" prop="visualScope">
          <el-select v-model="form.visualScope" placeholder="请选择可见范围" clearable @change="changeVisualScope">
            <el-option v-for="dict in dict.type.t_kbs_topic_visual_scope" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
          <div v-if="['2'].includes(form.visualScope)">
            <topic-auth-user
              :itemName="visualScopeItemName"
              :authUserType="visualScope"
              :authUsers="form.visualScopeAuthUsers"
              :sysUsers="form.visualScopeSysUsers"
              :selectType="selectType"
              @auth-user-change="authUserChange"
            />
          </div>
        </el-form-item>
        <el-form-item v-if="['1','2'].includes(form.visualScope)" label="操作权限" prop="operateType">
          <el-select v-model="form.operateType" placeholder="请选择操作类型" clearable @change="changeOperateType">
            <el-option v-for="dict in dict.type.t_kbs_topic_operate_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
          <span v-if="['2'].includes(form.visualScope) && ['1'].includes(form.operateType)">
            <i class="el-icon-info ml5" />
            <span class="authTip">可见范围为：部分可见时，设置此权限的用户须包含在可见范围的用户内，否则会忽略。</span>
          </span>
          <div v-if="['1'].includes(form.operateType)">
            <topic-auth-user
              :itemName="operateTypeItemName"
              :authUserType="operateType"
              :authUsers="form.operateTypeAuthUsers"
              :sysUsers="form.operateTypeSysUsers"
              :selectType="selectType"
              @auth-user-change="authUserChange"
            />
          </div>
        </el-form-item>
        <el-form-item label="卡片背景" prop="imgUrl">
          <file-upload :value="form.coverPicId" :fileSize="50" :fileType="['jpeg', 'jpg', 'png','webp']" :limit="1" @input="uploadSuccess">
            <i class="el-icon-picture-outline"></i>
          </file-upload>
        </el-form-item>
        <el-form-item label="主题简介" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="4" placeholder="请输入内容" />
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
import { getInfo, addInfo, updateInfo } from "@/api/kbs/topic/info";
import { getCategorySelectList } from "@/api/kbs/topic/category";
import TopicAuthUser from "./auth/topic-auth-user.vue";

export default {
  name: "AddTopic",
  dicts: ["t_kbs_topic_visual_scope", "t_kbs_topic_operate_type"],
  components: { TopicAuthUser },
  data() {
    return {
      // 主题类别
      categorys: [],
      // 多选
      selectType: "multiple",
      // 可见范围用户
      visualScopeItemName: "可查看主题的用户",
      // 操作类型用户
      operateTypeItemName: "可编辑主题的用户",
      // 可见范围
      visualScope: "1",
      // 操作类型
      operateType: "2",
      // 表单参数
      form: {
        name: "",
        visualScope: "1",
        operateType: "2",
        categoryId: "",
        coverPicId: "",
        remark: "",
        visualScopeAuthUsers: [],
        visualScopeSysUsers: [],
        operateTypeAuthUsers: [],
        operateTypeSysUsers: [],
      },
      // 表单校验
      rules: {
        name: [{ required: true, message: "主题名称不能为空", trigger: "blur" }],
        visualScope: [{ required: true, message: "可见范围不能为空", trigger: "change" }],
        categoryId: [{ required: true, message: "主题类别不能为空", trigger: "blur" }],
      },
    };
  },
  watch: {
    formData: {
      handler(obj) {
        if (obj && "id" in obj) {
          this.form = JSON.parse(JSON.stringify(obj));
        }
      },
    },
    open: {
      handler(val) {
        if (!val) return;
        this.getCategoryOpts();
      },
    },
    deep: true,
    immediate: true,
  },
  props: {
    open: false,
    title: "",
    formData: {
      type: Object,
      default: () => {},
    },
  },
  methods: {
    // 改变可见范围
    changeVisualScope(val) {},
    // 删除用户
    removeAuthUser(index) {
      this.form.authUsers.splice(index, 1);
    },
    uploadSuccess(obj) {
      this.form.coverPicId = obj;
    },
    // 查询知识库主题类别下拉列表
    getCategoryOpts() {
      getCategorySelectList().then((res) => {
        if (res.code === 200) {
          this.categorys = res.data || [];
        }
      });
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        visualScope: "1",
        operateType: "2",
        categoryId: null,
        coverPicId: null,
        remark: null,
        visualScopeAuthUsers: [],
        visualScopeSysUsers: [],
        operateTypeAuthUsers: [],
        operateTypeSysUsers: [],
      };
      this.resetForm("form");
    },
    /** 加载基本信息 */
    loadInfo(id) {
      this.reset();
      getInfo(id).then((response) => {
        this.form = response.data;
        this.title = "修改知识库主题";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (!["1", "2"].includes(this.form.visualScope)) {
          this.form.authUsers = [];
        } else if (!this.form.operateType) {
          this.$modal.msgError("请选择操作类型");
          return;
        }
        if (valid) {
          if (this.form.id != null) {
            updateInfo(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.$emit("topic-submit");
            });
          } else {
            addInfo(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.$emit("topic-submit");
            });
          }
        }
      });
    },
    // 取消按钮
    cancel() {
      this.reset();
      this.$emit("topic-close");
    },
    /** 修改操作类型 */
    changeOperateType() {},
    /** 修改用户 */
    authUserChange({ type, authUsers, sysUsers }) {
      if (type === "1") {
        this.form.visualScopeAuthUsers = authUsers;
        this.form.visualScopeSysUsers = sysUsers;
      } else if (type === "2") {
        this.form.operateTypeAuthUsers = authUsers;
        this.form.operateTypeSysUsers = sysUsers;
      }
    },
  },
};
</script>

<style scoped>
.box-card {
  margin: 0 12px 12px 12px;
}
.user-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding-top: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}
.user-add {
  float: right;
}
.user-remove {
  margin-left: 12px;
  margin-bottom: 22px;
}
.authTip {
  color: #909399;
  font-size: 12px;
  margin-left: 5px;
}
</style>
