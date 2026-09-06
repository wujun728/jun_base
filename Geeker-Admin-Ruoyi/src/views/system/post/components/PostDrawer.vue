<template>
  <el-dialog v-model="drawerVisible" width="500px" append-to-body :title="`${drawerProps?.title}用户`">
    <el-form
      ref="ruleFormRef"
      label-width="100px"
      label-suffix=" :"
      :rules="rules"
      :disabled="drawerProps?.isView"
      :model="drawerProps.row"
      :hide-required-asterisk="drawerProps?.isView"
    >
      <el-form-item label="岗位名称" prop="postName">
        <el-input v-model="drawerProps.row!.postName" placeholder="请输入岗位名称" />
      </el-form-item>
      <el-form-item label="岗位编码" prop="postCode">
        <el-input v-model="drawerProps.row!.postCode" placeholder="请输入编码名称" />
      </el-form-item>
      <el-form-item label="岗位顺序" prop="postSort">
        <el-input-number v-model="drawerProps.row!.postSort" controls-position="right" :min="0" />
      </el-form-item>
      <el-form-item label="岗位状态" prop="status">
        <el-radio-group v-model="drawerProps.row!.status">
          <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="drawerProps.row!.remark" type="textarea" placeholder="请输入内容" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button v-show="!drawerProps?.isView" type="primary" @click="handleSubmit">确定</el-button>
      <el-button @click="drawerVisible = false">取消</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts" name="PostDrawer">
import { ref, reactive } from "vue";
import { ElMessage, FormInstance } from "element-plus";
import { Post } from "@/api/interface";
import { useDict } from "@/hooks/useDict.js";
import { getPost } from "@/api/ruoyimodule/system/post";

const { sys_normal_disable } = useDict("sys_normal_disable");

const rules = reactive({
  postName: [{ required: true, message: "岗位名称不能为空", trigger: "blur" }],
  postCode: [{ required: true, message: "岗位编码不能为空", trigger: "blur" }],
  postSort: [{ required: true, message: "岗位顺序不能为空", trigger: "blur" }]
});

interface DrawerProps {
  title: string;
  isView: boolean;
  row: Partial<Post.ResPostInfo>;
  api?: (params: any) => Promise<any>;
  getTableList?: () => void;
}

const drawerVisible = ref(false);
const drawerProps = ref<DrawerProps>({
  isView: false,
  title: "",
  row: {}
});

const acceptParams = (params: any) => {
  drawerProps.value = params;
  drawerVisible.value = true;
  const postId = params.postId;
  if (postId) {
    getPost(postId).then((response: any) => {
      drawerProps.value.row = response.data;
      drawerVisible.value = true;
    });
  } else {
    drawerProps.value.row.status = drawerProps.value.row.status || "0";
  }
};

// 提交数据（新增/编辑）
const ruleFormRef = ref<FormInstance>();
const handleSubmit = () => {
  ruleFormRef.value!.validate(async valid => {
    if (!valid) return;
    try {
      await drawerProps.value.api!(drawerProps.value.row);
      ElMessage.success({ message: `${drawerProps.value.title}用户成功！` });
      drawerProps.value.getTableList!();
      drawerVisible.value = false;
    } catch (error) {
      console.log(error);
    }
  });
};

defineExpose({
  acceptParams
});
</script>
