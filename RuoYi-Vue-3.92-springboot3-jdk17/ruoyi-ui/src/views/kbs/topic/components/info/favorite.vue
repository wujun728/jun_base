<template>
  <div>
    <el-dialog title="选择收藏组" :visible.sync="favoriteOpen" width="500px" append-to-body>
      <el-divider />
      <el-form ref="favoriteForm" :model="favoriteForm" :rules="rules" label-width="80px">
        <el-form-item label="收藏组" prop="groupId">
          <el-select v-model="favoriteForm.groupId" placeholder="请选择收藏组" clearable>
            <el-option v-for="item in groups" :key="item.groupId" :label="item.groupName" :value="item.groupId" />
          </el-select>
          <span style="margin-left: 24px">
            <el-button type="text" size="medium" icon="el-icon-setting" @click="openGroup = true">管理分组</el-button>
          </span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <favorite-group :openGroup="openGroup" @close-group="closeGroup" />
  </div>
</template>

<script>
import { getGroupSelectList } from "@/api/kbs/favorite/group";
import { addFavorite } from "@/api/kbs/favorite/favorite";
import FavoriteGroup from "./favorite-group.vue";

export default {
  components: { FavoriteGroup },
  data() {
    return {
      // 管理分组
      openGroup: false,
      // 收藏组
      groups: [],
      // 收藏表单
      favoriteForm: {
        objectId: "",
        objectName: "",
        objectType: "2",
        rootObjectId: "",
        parentObjectId: "",
        groupId: "",
      },
      rules: {
        groupId: [{ required: true, message: "请选择收藏组", trigger: "blur" }],
      },
    };
  },
  props: {
    topicId: null,
    favoriteOpen: {
      type: Boolean,
      default: false,
    },
    // 收藏文档对象
    favoriteObject: {
      type: Object,
      default: null,
    },
  },
  mounted() {
    this.getGroupSelectList();
  },
  methods: {
    // 查询知识库收藏组下拉列表
    async getGroupSelectList() {
      getGroupSelectList().then((res) => {
        if (res.data) {
          this.groups = res.data;
        }
      });
    },
    // 取消
    cancel() {
      this.$emit("close-favorite");
    },
    // 提交收藏
    submit() {
      this.$refs["favoriteForm"].validate((valid) => {
        if (valid) {
          this.favoriteForm.objectId = this.favoriteObject.id;
          this.favoriteForm.objectName = this.favoriteObject.name;
          this.favoriteForm.rootObjectId = this.favoriteObject.rootObjectId;
          this.favoriteForm.objectType = this.favoriteObject.type;
          this.favoriteForm.parentObjectId = this.favoriteObject.parentId ? this.favoriteObject.parentId : this.topicId;
          addFavorite(this.favoriteForm).then((res) => {
            if (res.code == 200) {
              this.$message.success("已收藏!");
              this.reset();
              this.$emit("success-favorite");
            }
          });
        }
      });
    },
    // 重置收藏表单
    reset() {
      this.favoriteForm = {
        objectId: "",
        objectName: "",
        objectType: "2",
        rootObjectId: "",
        parentObjectId: "",
        groupId: "",
      };
    },
    closeGroup() {
      this.openGroup = false;
      this.getGroupSelectList();
    },
  },
};
</script>