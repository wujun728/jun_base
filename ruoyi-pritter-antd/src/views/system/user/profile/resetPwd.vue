<script setup>
import { updateUserPwd } from '@/api/system/user'

const proxy = inject('proxy')
const pwdRef = useTemplateRef('pwdRef')
const labelCol = { style: { width: '80px' } }

const user = reactive({
  oldPassword: undefined,
  newPassword: undefined,
  confirmPassword: undefined,
})

const equalToPassword = async (_rule, value) => {
  if (user.newPassword !== value) {
    return Promise.reject('两次输入的密码不一致')
  }
}

const rules = {
  oldPassword: [{ required: true, message: '旧密码不能为空', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '用户密码不能为空', trigger: 'blur' },
    {
      min: 5,
      max: 20,
      message: '用户密码长度必须介于 5 和 20 之间',
      trigger: 'blur',
    },
    {
      pattern: /^[^<>"'|\\]+$/,
      message: '不能包含非法字符：< > " \' \\ |',
      trigger: 'blur',
    },
  ],
  confirmPassword: [
    { required: true, message: '确认密码不能为空', trigger: 'blur' },
    { validator: equalToPassword, trigger: 'blur' },
  ],
}

async function submit() {
  try {
    await pwdRef.value.validate()
    await updateUserPwd(user.oldPassword, user.newPassword)
    proxy.$modal.notifySuccess('修改成功')
  } catch {
    // 表单校验未通过
  }
}

function close() {
  proxy.$tab.closePage()
}
</script>

<template>
  <a-form
    ref="pwdRef"
    :model="user"
    :rules="rules"
    label-align="right"
    :label-col="labelCol"
  >
    <a-form-item label="旧密码" name="oldPassword">
      <a-input-password
        v-model:value="user.oldPassword"
        placeholder="请输入旧密码"
      />
    </a-form-item>
    <a-form-item label="新密码" name="newPassword">
      <a-input-password
        v-model:value="user.newPassword"
        placeholder="请输入新密码"
      />
    </a-form-item>
    <a-form-item label="确认密码" name="confirmPassword">
      <a-input-password
        v-model:value="user.confirmPassword"
        placeholder="请确认新密码"
      />
    </a-form-item>
    <a-form-item class="profile-form-actions">
      <a-button type="primary" @click="submit">保存</a-button>
      <a-button danger class="ml12" @click="close">关闭</a-button>
    </a-form-item>
  </a-form>
</template>

<style lang="scss" scoped>
.profile-form-actions :deep(.ant-form-item-control) {
  margin-left: 80px;
}
</style>
