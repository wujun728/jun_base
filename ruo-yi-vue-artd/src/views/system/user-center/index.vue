<!-- 个人中心页面 -->
<template>
  <div class="h-full w-full border-none bg-transparent p-0 shadow-none">
    <div class="flex-b relative mt-2.5 max-md:mt-1 max-md:block">
      <div class="mr-5 w-112 max-md:mr-0 max-md:w-full">
        <div class="art-card-sm relative overflow-hidden p-9 pb-6 text-center">
          <img
            class="absolute top-0 left-0 h-50 w-full object-cover"
            src="@imgs/user/bg.webp" />

          <div class="relative z-10 mx-auto mt-30 border-white object-cover">
            <UserAvatar />
          </div>

          <h2 class="mt-5 text-xl font-normal">{{ userInfo.data.userName }}</h2>
          <!-- <p class="mt-5 text-sm">专注于用户体验跟视觉设计</p> -->

          <div class="mx-auto mt-7.5 w-75 text-left">
            <div class="mt-2.5">
              <ArtSvgIcon icon="ri:mail-line" class="text-g-700" />
              <span class="ml-2 text-sm">{{ userInfo.data.email }}</span>
            </div>
            <div class="mt-2.5">
              <ArtSvgIcon icon="ri:phone-fill" class="text-g-700" />
              <span class="ml-2 text-sm">{{ userInfo.data.phonenumber }}</span>
            </div>
            <div class="mt-2.5">
              <ArtSvgIcon icon="ri:user-3-line" class="text-g-700" />
              <span class="ml-2 text-sm">{{ userInfo.roleGroup }}</span>
            </div>
            <div class="mt-2.5">
              <ArtSvgIcon icon="ri:dribbble-fill" class="text-g-700" />
              <span class="ml-2 text-sm" v-if="userInfo.data.dept">
                {{ userInfo.data.dept.deptName }} / {{ userInfo.postGroup }}
              </span>
            </div>
            <div class="mt-2.5">
              <ArtSvgIcon icon="ri:time-line" class="text-g-700" />
              <span class="ml-2 text-sm">{{ userInfo.data.createTime }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="flex-1 overflow-hidden max-md:mt-3.5 max-md:w-full">
        <div class="art-card-sm">
          <h1 class="border-b border-g-300 p-4 text-xl font-normal">
            基本设置
          </h1>

          <ElForm
            :model="form"
            class="box-border p-5 [&>.el-row_.el-form-item]:w-[calc(50%-10px)] [&>.el-row_.el-input]:w-full [&>.el-row_.el-select]:w-full"
            ref="ruleFormRef"
            :rules="rules"
            label-width="86px"
            label-position="top">
            <ElRow>
              <ElFormItem label="姓名" prop="nickName">
                <ElInput v-model="form.nickName" :disabled="!isEdit" />
              </ElFormItem>
              <ElFormItem label="性别" prop="sex" class="ml-5">
                <ElSelect
                  v-model="form.sex"
                  placeholder="Select"
                  :disabled="!isEdit">
                  <ElOption
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value" />
                </ElSelect>
              </ElFormItem>
            </ElRow>

            <ElRow>
              <ElFormItem label="邮箱" prop="email">
                <ElInput v-model="form.email" :disabled="!isEdit" />
              </ElFormItem>
              <ElFormItem label="手机" prop="phonenumber" class="ml-5">
                <ElInput v-model="form.phonenumber" :disabled="!isEdit" />
              </ElFormItem>
            </ElRow>
            <div class="flex-c justify-end [&_.el-button]:!w-27.5">
              <ElButton type="primary" class="w-22.5" v-ripple @click="edit">
                {{ isEdit ? '保存' : '编辑' }}
              </ElButton>
            </div>
          </ElForm>
        </div>

        <div class="art-card-sm my-5">
          <h1 class="border-b border-g-300 p-4 text-xl font-normal">
            更改密码
          </h1>

          <ElForm
            :model="pwdForm"
            ref="pwdFormRef"
            :rules="pwdRules"
            class="box-border p-5"
            label-width="86px"
            label-position="top">
            <ElFormItem label="当前密码" prop="oldPassword">
              <ElInput
                v-model="pwdForm.oldPassword"
                type="oldPassword"
                :disabled="!isEditPwd"
                show-password />
            </ElFormItem>

            <ElFormItem
              label="新密码"
              prop="newPassword"
              :rules="infoPwdValidator">
              <ElInput
                v-model="pwdForm.newPassword"
                type="password"
                :disabled="!isEditPwd"
                show-password />
            </ElFormItem>

            <ElFormItem label="确认新密码" prop="confirmPassword">
              <ElInput
                v-model="pwdForm.confirmPassword"
                type="password"
                :disabled="!isEditPwd"
                show-password />
            </ElFormItem>

            <div class="flex-c justify-end [&_.el-button]:!w-27.5">
              <ElButton type="primary" class="w-22.5" v-ripple @click="editPwd">
                {{ isEditPwd ? '保存' : '编辑' }}
              </ElButton>
            </div>
          </ElForm>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { FormInstance, FormRules } from 'element-plus'

import { usePasswordRule } from '@/hooks/core/usePassword'
import { SysUser, UserProfileResult } from '@/types/api/system/user'

import { getUserProfile, updateUserProfile, updateUserPwd } from './api'
import UserAvatar from './components/userAvatar.vue'

defineOptions({ name: 'UserCenter' })

const { infoPwdValidator } = usePasswordRule()

const isEdit = ref(false)
const isEditPwd = ref(false)
const date = ref('')
const ruleFormRef = ref<FormInstance>()
const pwdFormRef = ref<FormInstance>()

/**
 * 用户信息
 */
const userInfo = ref<UserProfileResult>({
  data: {},
  roleGroup: '',
  postGroup: ''
})

/**
 * 用户信息表单
 */
const form = reactive({
  nickName: '',
  email: '',
  phonenumber: '',
  sex: ''
})

/**
 * 密码修改表单
 */
const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const equalToPassword = (
  rule: any,
  value: string,
  callback: (error?: Error) => void
): void => {
  if (pwdForm.newPassword !== value) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}
/**
 * 密码修改表单验证规则
 */
const pwdRules: FormRules = {
  oldPassword: [{ required: true, message: '旧密码不能为空', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '确认密码不能为空', trigger: 'blur' },
    { required: true, validator: equalToPassword, trigger: 'blur' }
  ]
}
/**
 * 表单验证规则
 */
const rules = reactive<FormRules>({
  nickName: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
  phonenumber: [{ required: true, message: '请输入手机号码', trigger: 'blur' }],
  sex: [{ required: true, message: '请选择性别', trigger: 'blur' }]
})

/**
 * 性别选项
 */
const options = [
  { value: '0', label: '男' },
  { value: '1', label: '女' }
]

onMounted(() => {
  getDate()
})

/**
 * 根据当前时间获取问候语
 */
const getDate = () => {
  const h = new Date().getHours()

  if (h >= 6 && h < 9) date.value = '早上好'
  else if (h >= 9 && h < 11) date.value = '上午好'
  else if (h >= 11 && h < 13) date.value = '中午好'
  else if (h >= 13 && h < 18) date.value = '下午好'
  else if (h >= 18 && h < 24) date.value = '晚上好'
  else date.value = '很晚了，早点睡'
}

/**
 * 切换用户信息编辑状态
 */
const edit = async () => {
  if (isEdit.value) {
    await ruleFormRef.value?.validate()
    await updateUserProfile(form as SysUser)
    ElMessage.success('修改成功')
    getUser()
  }
  isEdit.value = !isEdit.value
}

/**
 * 切换密码编辑状态
 */
const editPwd = async () => {
  if (isEditPwd.value) {
    await pwdFormRef.value?.validate()
    await updateUserPwd(pwdForm.oldPassword!, pwdForm.newPassword!)
    ElMessage.success('修改成功')
    getUser()
  }
  isEditPwd.value = !isEditPwd.value
}

async function getUser() {
  const response = await getUserProfile()
  userInfo.value = response
  form.nickName = userInfo.value.data.nickName || ''
  form.email = userInfo.value.data.email || ''
  form.phonenumber = userInfo.value.data.phonenumber || ''
  form.sex = userInfo.value.data.sex || ''
}

onMounted(() => {
  getUser()
})
</script>
