<template>
  <el-dialog v-model="drawerVisible" width="600px" append-to-body :title="`${drawerProps?.title}用户`">
    <el-form
      ref="ruleFormRef"
      label-width="100px"
      label-suffix=" :"
      :rules="rules"
      :disabled="drawerProps?.isView"
      :model="drawerProps.row"
      :hide-required-asterisk="drawerProps?.isView"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="用户昵称" prop="nickName">
            <el-input v-model="drawerProps.row!.nickName" placeholder="请输入用户昵称" maxlength="30" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="归属部门" prop="deptId">
            <el-tree-select
              v-model="drawerProps.row!.deptId"
              :data="deptOptions"
              :props="{ value: 'id', label: 'label', children: 'children' }"
              value-key="id"
              placeholder="请选择归属部门"
              check-strictly
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="手机号码" prop="phonenumber">
            <el-input v-model="drawerProps.row!.phonenumber" placeholder="请输入手机号码" maxlength="11" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="drawerProps.row!.email" placeholder="请输入邮箱" maxlength="50" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item v-if="drawerProps.row!.userId == undefined" label="用户名称" prop="userName">
            <el-input v-model="drawerProps.row!.userName" placeholder="请输入用户名称" maxlength="30" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="drawerProps.row!.userId == undefined" label="用户密码" prop="password">
            <el-input
              v-model="drawerProps.row!.password"
              placeholder="请输入用户密码"
              type="password"
              maxlength="20"
              show-password
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="用户性别">
            <el-select v-model="drawerProps.row!.sex" placeholder="请选择">
              <el-option v-for="dict in sys_user_sex" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态">
            <el-radio-group v-model="drawerProps.row!.status">
              <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="岗位">
            <el-select v-model="drawerProps.row!.postIds" multiple placeholder="请选择">
              <el-option
                v-for="item in postOptions"
                :key="item.postId"
                :label="item.postName"
                :value="item.postId"
                :disabled="item.status == 1"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="角色">
            <el-select v-model="drawerProps.row!.roleIds" multiple placeholder="请选择">
              <el-option
                v-for="item in roleOptions"
                :key="item.roleId"
                :label="item.roleName"
                :value="item.roleId"
                :disabled="item.status == 1"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input v-model="drawerProps.row!.remark" type="textarea" placeholder="请输入内容"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button v-show="!drawerProps?.isView" type="primary" @click="handleSubmit">确定</el-button>
      <el-button @click="drawerVisible = false">取消</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts" name="UserDrawer">
import { ref, reactive } from "vue";
import { ElMessage, FormInstance } from "element-plus";
import { Dept, User } from "@/api/interface";
import { deptTreeSelect, getUser } from "@/api/ruoyimodule/system/user";
import { useDict } from "@/hooks/useDict.js";

const { sys_normal_disable, sys_user_sex } = useDict("sys_normal_disable", "sys_user_sex");

const rules = reactive({
  userName: [
    { required: true, message: "用户名称不能为空", trigger: "blur" },
    { min: 2, max: 20, message: "用户名称长度必须介于 2 和 20 之间", trigger: "blur" }
  ],
  nickName: [{ required: true, message: "用户昵称不能为空", trigger: "blur" }],
  password: [
    { required: true, message: "用户密码不能为空", trigger: "blur" },
    { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" }
  ],
  email: [{ type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
  phonenumber: [{ pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }]
});

interface DrawerProps {
  title: string;
  isView: boolean;
  row: Partial<User.ResUserInfo>;
  api?: (params: any) => Promise<any>;
  getTableList?: () => void;
}

const drawerVisible = ref(false);
const drawerProps = ref<DrawerProps>({
  isView: false,
  title: "",
  row: {}
});

// 接收父组件传过来的参数
const roleOptions = ref();
const postOptions = ref();
const acceptParams = (params: DrawerProps) => {
  drawerProps.value = params;
  drawerVisible.value = true;
  const userId = drawerProps.value.row.userId;
  drawerProps.value.row.status = drawerProps.value.row.status || "0";
  getUser(userId).then((response: any) => {
    postOptions.value = response.posts;
    roleOptions.value = response.roles;
    drawerProps.value.row.postIds = response.postIds;
    drawerProps.value.row.roleIds = response.roleIds;
  });
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
/** 查询部门下拉树结构 */
const deptOptions = ref<Array<Dept.ResDeptInfo>>();
function getDeptTree() {
  deptTreeSelect().then(res => {
    deptOptions.value = res.data;
  });
}
getDeptTree();
defineExpose({
  acceptParams
});
</script>
