<script setup>
import { updateUserProfile } from '@/api/system/user'

const props = defineProps({
  user: {
    type: Object,
  },
})

const proxy = inject('proxy')
const userRef = useTemplateRef('userRef')
const labelCol = { style: { width: '80px' } }

const rules = {
  nickName: [{ required: true, message: '用户昵称不能为空', trigger: 'blur' }],
  email: [
    { required: true, message: '邮箱地址不能为空', trigger: 'blur' },
    {
      type: 'email',
      message: '请输入正确的邮箱地址',
      trigger: ['blur', 'change'],
    },
  ],
  phonenumber: [
    { required: true, message: '手机号码不能为空', trigger: 'blur' },
    {
      pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
      message: '请输入正确的手机号码',
      trigger: 'blur',
    },
  ],
}

async function submit() {
  try {
    await userRef.value.validate()
    await updateUserProfile(props.user)
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
    ref="userRef"
    :model="user"
    :rules="rules"
    label-align="right"
    :label-col="labelCol"
  >
    <a-form-item label="用户昵称" name="nickName">
      <a-input v-model:value="user.nickName" :maxlength="30" />
    </a-form-item>
    <a-form-item label="手机号码" name="phonenumber">
      <a-input v-model:value="user.phonenumber" :maxlength="11" />
    </a-form-item>
    <a-form-item label="邮箱" name="email">
      <a-input v-model:value="user.email" :maxlength="50" />
    </a-form-item>
    <a-form-item label="性别" name="sex">
      <a-radio-group v-model:value="user.sex">
        <a-radio value="0">男</a-radio>
        <a-radio value="1">女</a-radio>
      </a-radio-group>
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
