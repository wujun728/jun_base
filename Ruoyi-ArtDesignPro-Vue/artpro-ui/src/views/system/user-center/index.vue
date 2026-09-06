<template>
  <div class="profile-page art-full-height">
    <div class="profile-layout">
      <ElCard class="profile-card identity-card" shadow="never">
        <div class="profile-cover">
          <img :src="profileBanner" alt="" />
        </div>
        <div class="identity-content">
          <ElUpload
            class="avatar-uploader"
            :show-file-list="false"
            accept="image/*"
            :http-request="uploadAvatar"
          >
            <div class="avatar-wrap">
              <ElAvatar :size="116" :src="avatarUrl" />
              <div class="avatar-mask">
                <ArtSvgIcon icon="ri:camera-line" />
                <span>更新头像</span>
              </div>
            </div>
          </ElUpload>

          <h2 class="profile-name">{{ displayValue(profile.nickName || profile.userName) }}</h2>
          <p class="profile-account">@{{ displayValue(profile.userName) }}</p>

          <div class="profile-details">
            <div v-for="item in profileDetails" :key="item.label" class="profile-detail">
              <ArtSvgIcon :icon="item.icon" />
              <div>
                <span>{{ item.label }}</span>
                <strong>{{ displayValue(item.value) }}</strong>
              </div>
            </div>
          </div>

          <div class="role-section">
            <div class="section-divider"></div>
            <h3>角色标签</h3>
            <div class="role-tags">
              <ElTag v-for="role in roles" :key="role" effect="plain">{{ role }}</ElTag>
              <ElTag v-if="!roles.length" effect="plain" type="info">未设置</ElTag>
            </div>
          </div>
        </div>
      </ElCard>

      <ElCard class="profile-card settings-card" shadow="never">
        <template #header>
          <div class="settings-header">
            <div>
              <h2>基本设置</h2>
              <p>维护你的基础资料、联系信息与对外展示内容。</p>
            </div>
            <ElButton @click="openPasswordDrawer">
              <ArtSvgIcon icon="ri:lock-password-line" class="mr-1" />
              修改密码
            </ElButton>
          </div>
        </template>

        <ArtForm
          ref="profileFormRef"
          v-model="profile"
          :items="profileItems"
          :rules="profileRules"
          :span="12"
          label-position="top"
          :show-submit="false"
          :show-reset="false"
        />
        <div class="settings-actions">
          <ElButton type="primary" :loading="savingProfile" @click="saveProfile">保存资料</ElButton>
        </div>
      </ElCard>
    </div>

    <ElDrawer
      v-model="passwordVisible"
      title="修改密码"
      :size="passwordDrawerSize"
      @closed="resetPasswordForm"
    >
      <div class="password-tip">
        <ArtSvgIcon icon="ri:shield-keyhole-line" />
        <div>
          <strong>账号安全</strong>
          <p>建议定期更换密码，密码长度为 5 到 20 个字符。</p>
        </div>
      </div>
      <ArtForm
        ref="passwordFormRef"
        v-model="password"
        :items="passwordItems"
        :rules="passwordRules"
        :span="24"
        label-position="top"
        :show-submit="false"
        :show-reset="false"
      />
      <template #footer>
        <ElButton @click="passwordVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="savingPassword" @click="savePassword">
          确认修改
        </ElButton>
      </template>
    </ElDrawer>
  </div>
</template>

<script setup lang="ts">
  import { useWindowSize } from '@vueuse/core'
  import type { UploadRequestOptions } from 'element-plus'
  import profileBanner from '@/assets/images/user/bg.webp'
  import defaultAvatar from '@/assets/images/user/avatar.webp'
  import ArtSvgIcon from '@/components/core/base/art-svg-icon/index.vue'
  import {
    fetchProfile,
    resolveAvatarUrl,
    updatePassword,
    updateProfile,
    uploadProfileAvatar,
    type Profile
  } from '@/api/system/profile'
  import { useUserStore } from '@/store/modules/user'

  defineOptions({ name: 'SystemUserCenter' })
  const userStore = useUserStore()
  const { width } = useWindowSize()
  const profileFormRef = ref<{ validate: () => Promise<boolean> }>()
  const passwordFormRef = ref<{ validate: () => Promise<boolean>; ref?: { clearValidate: () => void } }>()
  const profile = reactive<Profile>({})
  const password = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })
  const savingProfile = ref(false)
  const savingPassword = ref(false)
  const passwordVisible = ref(false)

  const avatarUrl = computed(() => resolveAvatarUrl(profile.avatar) || defaultAvatar)
  const passwordDrawerSize = computed(() => (width.value < 640 ? '100%' : '520px'))
  const roles = computed(() =>
    (profile.roleGroup || '')
      .split(',')
      .map((role) => role.trim())
      .filter(Boolean)
  )
  const profileDetails = computed(() => [
    { label: '登录账号', value: profile.userName, icon: 'ri:user-3-line' },
    { label: '邮箱', value: profile.email, icon: 'ri:mail-line' },
    { label: '手机号码', value: profile.phonenumber, icon: 'ri:phone-line' },
    { label: '所属部门', value: profile.dept?.deptName, icon: 'ri:building-2-line' },
    { label: '岗位', value: profile.postGroup, icon: 'ri:briefcase-4-line' }
  ])
  const profileItems = [
    {
      key: 'userName',
      label: '登录账号',
      type: 'input',
      span: 12,
      props: { disabled: true, placeholder: '未设置登录账号' }
    },
    {
      key: 'nickName',
      label: '用户昵称',
      type: 'input',
      span: 12,
      props: { placeholder: '请输入用户昵称' }
    },
    {
      key: 'phonenumber',
      label: '手机号码',
      type: 'input',
      span: 12,
      props: { placeholder: '请输入手机号码' }
    },
    {
      key: 'email',
      label: '邮箱',
      type: 'input',
      span: 12,
      props: { placeholder: '请输入邮箱' }
    },
    {
      key: 'sex',
      label: '性别',
      type: 'select',
      span: 12,
      props: {
        placeholder: '请选择性别',
        options: [
          { label: '男', value: '0' },
          { label: '女', value: '1' },
          { label: '未知', value: '2' }
        ]
      }
    }
  ]
  const passwordItems = [
    {
      key: 'oldPassword',
      label: '旧密码',
      type: 'input',
      props: { type: 'password', showPassword: true, placeholder: '请输入旧密码' }
    },
    {
      key: 'newPassword',
      label: '新密码',
      type: 'input',
      props: { type: 'password', showPassword: true, placeholder: '请输入新密码' }
    },
    {
      key: 'confirmPassword',
      label: '确认密码',
      type: 'input',
      props: { type: 'password', showPassword: true, placeholder: '请再次输入新密码' }
    }
  ]
  const profileRules = {
    nickName: [{ required: true, message: '请输入用户昵称' }],
    email: [{ type: 'email', message: '请输入正确的邮箱地址' }]
  }
  const passwordRules = {
    oldPassword: [{ required: true, message: '请输入旧密码' }],
    newPassword: [{ required: true, min: 5, max: 20, message: '新密码长度为 5 到 20 个字符' }],
    confirmPassword: [
      { required: true, message: '请再次输入新密码' },
      {
        validator: (_rule: unknown, value: string, callback: (error?: Error) => void) =>
          value === password.newPassword ? callback() : callback(new Error('两次输入的密码不一致'))
      }
    ]
  }

  onMounted(async () => Object.assign(profile, await fetchProfile()))

  function displayValue(value?: string) {
    return value || '未设置'
  }
  function openPasswordDrawer() {
    passwordVisible.value = true
  }
  function resetPasswordForm() {
    Object.assign(password, { oldPassword: '', newPassword: '', confirmPassword: '' })
    passwordFormRef.value?.ref?.clearValidate()
  }
  async function saveProfile() {
    await profileFormRef.value?.validate()
    savingProfile.value = true
    try {
      await updateProfile(profile)
      userStore.setUserInfo({ ...userStore.info, ...profile } as Api.Auth.UserInfo)
    } finally {
      savingProfile.value = false
    }
  }
  async function savePassword() {
    await passwordFormRef.value?.validate()
    savingPassword.value = true
    try {
      await updatePassword(password.oldPassword, password.newPassword)
      passwordVisible.value = false
      resetPasswordForm()
    } finally {
      savingPassword.value = false
    }
  }
  async function uploadAvatar(options: UploadRequestOptions) {
    const result = await uploadProfileAvatar(options.file)
    profile.avatar = result.imgUrl
    userStore.setUserInfo({ ...userStore.info, avatar: result.imgUrl } as Api.Auth.UserInfo)
  }
</script>

<style scoped lang="scss">
  .profile-page {
    overflow: auto;
  }

  .profile-layout {
    display: grid;
    grid-template-columns: minmax(300px, 360px) minmax(0, 1fr);
    gap: 16px;
  }

  .profile-card {
    border-color: var(--default-border);
    background: var(--default-box-color);
  }

  .identity-card {
    :deep(.el-card__body) {
      padding: 0;
    }
  }

  .profile-cover {
    height: 176px;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  .identity-content {
    padding: 0 28px 28px;
  }

  .avatar-uploader {
    display: flex;
    justify-content: center;
    height: 64px;
  }

  .avatar-wrap {
    position: relative;
    width: 124px;
    height: 124px;
    overflow: hidden;
    cursor: pointer;
    border: 4px solid var(--default-box-color);
    border-radius: 50%;
    transform: translateY(-60px);

    &:hover .avatar-mask {
      opacity: 1;
    }
  }

  .avatar-mask {
    position: absolute;
    inset: 0;
    display: flex;
    flex-direction: column;
    gap: 2px;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 13px;
    background: rgb(0 0 0 / 52%);
    opacity: 0;
    transition: opacity 0.2s ease;

    :deep(svg) {
      font-size: 20px;
    }
  }

  .profile-name {
    margin-top: 16px;
    color: var(--art-gray-900);
    font-size: 24px;
    font-weight: 600;
    text-align: center;
  }

  .profile-account {
    margin-top: 6px;
    color: var(--art-gray-500);
    text-align: center;
  }

  .profile-details {
    display: grid;
    gap: 18px;
    margin-top: 34px;
  }

  .profile-detail {
    display: flex;
    gap: 12px;
    align-items: center;

    > :deep(svg) {
      flex: none;
      color: var(--art-gray-500);
      font-size: 19px;
    }

    div {
      display: grid;
      gap: 3px;
      min-width: 0;
    }

    span {
      color: var(--art-gray-500);
      font-size: 12px;
    }

    strong {
      overflow: hidden;
      color: var(--art-gray-800);
      font-size: 14px;
      font-weight: 500;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  .role-section {
    margin-top: 28px;

    h3 {
      margin: 22px 0 14px;
      color: var(--art-gray-800);
      font-size: 15px;
      font-weight: 600;
      text-align: center;
    }
  }

  .section-divider {
    height: 1px;
    background: var(--default-border);
  }

  .role-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    justify-content: center;
  }

  .settings-card {
    :deep(.el-card__header) {
      padding: 24px 28px;
    }

    :deep(.el-card__body) {
      padding: 14px 16px 24px;
    }
  }

  .settings-header {
    display: flex;
    gap: 16px;
    align-items: center;
    justify-content: space-between;

    h2 {
      color: var(--art-gray-900);
      font-size: 22px;
      font-weight: 600;
    }

    p {
      margin-top: 8px;
      color: var(--art-gray-500);
      font-size: 14px;
    }
  }

  .settings-actions {
    display: flex;
    justify-content: flex-end;
    padding: 8px 16px 0;
  }

  .password-tip {
    display: flex;
    gap: 12px;
    align-items: center;
    padding: 14px 16px;
    margin: 0 16px 4px;
    color: var(--art-gray-700);
    background: color-mix(in srgb, var(--theme-color) 8%, var(--default-box-color));
    border: 1px solid color-mix(in srgb, var(--theme-color) 20%, var(--default-border));
    border-radius: 8px;

    > :deep(svg) {
      flex: none;
      color: var(--theme-color);
      font-size: 24px;
    }

    strong {
      font-size: 14px;
    }

    p {
      margin-top: 4px;
      color: var(--art-gray-500);
      font-size: 12px;
    }
  }

  @media screen and (max-width: 960px) {
    .profile-layout {
      grid-template-columns: 1fr;
    }
  }

  @media screen and (max-width: 560px) {
    .settings-header {
      align-items: flex-start;
      flex-direction: column;
    }

    .identity-content {
      padding-right: 20px;
      padding-left: 20px;
    }
  }
</style>
