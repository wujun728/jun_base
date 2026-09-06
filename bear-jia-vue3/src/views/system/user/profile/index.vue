<template>
  <div class="account-settings-page">
    <!-- 统一的白色面板容器 -->
    <div class="unified-panel">
      <div class="panel-layout">
        <!-- 左侧：个人信息侧边栏 -->
        <div class="sidebar-section">
          <!-- 头像区域 -->
          <div class="avatar-wrapper">
            <UserAvatar
              :avatar="user.avatar"
              :avatar-size="120"
              mask-text="更换头像"
              @success="handleAvatarSuccess"
            />
            <a-typography-title :level="4" class="username">{{ user.nickName }}</a-typography-title>
            <a-tag color="blue" class="role-tag">{{ user.roleGroup || '暂无角色' }}</a-tag>
          </div>

          <!-- 详细信息列表 -->
          <div class="info-list">
            <div class="info-item">
              <div class="info-label">
                <UserOutlined class="info-icon" />
                <span>用户账号</span>
              </div>
              <div class="info-value">{{ user.userName || '暂无' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <TeamOutlined class="info-icon" />
                <span>所属部门</span>
              </div>
              <div class="info-value">{{ user.deptName || '暂无' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <IdcardOutlined class="info-icon" />
                <span>岗位</span>
              </div>
              <div class="info-value">{{ user.postGroup || '暂无' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <PhoneOutlined class="info-icon" />
                <span>手机号码</span>
              </div>
              <div class="info-value">{{ user.phonenumber || '暂无' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <MailOutlined class="info-icon" />
                <span>邮箱</span>
              </div>
              <div class="info-value">{{ user.email || '暂无' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <ClockCircleOutlined class="info-icon" />
                <span>创建时间</span>
              </div>
              <div class="info-value">{{ formatCreateTime(user.createTime) }}</div>
            </div>
          </div>
        </div>

        <!-- 垂直分割线 -->
        <div class="vertical-divider"></div>

        <!-- 右侧：设置内容区域 -->
        <div class="content-section">
          <a-tabs v-model:activeKey="activeTabKey" class="settings-tabs">
            <!-- 基本资料 Tab -->
            <a-tab-pane key="basic" tab="基本资料">
              <div class="form-content">
                <a-form :model="userForm" layout="vertical" @finish="onFinish" ref="basicFormRef">
                  <a-form-item label="用户昵称" name="nickName" :rules="[{ required: true, message: '请输入用户昵称!' }]">
                    <a-input v-model:value="userForm.nickName" size="large" placeholder="请输入用户昵称" class="form-input" />
                  </a-form-item>
                  <a-form-item label="手机号码" name="phonenumber">
                    <a-input v-model:value="userForm.phonenumber" size="large" placeholder="请输入手机号码" class="form-input" />
                  </a-form-item>
                  <a-form-item label="邮箱" name="email">
                    <a-input v-model:value="userForm.email" size="large" placeholder="请输入邮箱" class="form-input" />
                  </a-form-item>
                  <a-form-item label="性别" name="sex" :rules="[{ required: true, message: '请选择性别!' }]">
                    <a-select v-model:value="userForm.sex" size="large" placeholder="请选择性别" class="form-input">
                      <a-select-option value="0">男</a-select-option>
                      <a-select-option value="1">女</a-select-option>
                    </a-select>
                  </a-form-item>
                  <a-form-item>
                    <a-button
                      type="primary"
                      size="large"
                      html-type="submit"
                      class="submit-button"
                      :loading="basicFormLoading"
                    >
                      保存更改
                    </a-button>
                  </a-form-item>
                </a-form>
              </div>
            </a-tab-pane>

            <!-- 修改密码 Tab -->
            <a-tab-pane key="password" tab="修改密码">
              <div class="form-content">
                <a-form layout="vertical" @finish="onChangePassword" ref="passwordFormRef">
                  <a-form-item label="旧密码" name="oldPassword" :rules="[{ required: true, message: '请输入旧密码!' }]">
                    <a-input-password v-model:value="passwordForm.oldPassword" size="large" placeholder="请输入旧密码" class="form-input" />
                  </a-form-item>
                  <a-form-item label="新密码" name="newPassword" :rules="[
                    { required: true, message: '请输入新密码!' },
                    { min: 6, message: '密码长度不能少于6位' }
                  ]">
                    <a-input-password
                      v-model:value="passwordForm.newPassword"
                      size="large"
                      placeholder="请输入新密码"
                      class="form-input"
                      @input="checkPasswordStrength"
                    />
                    <!-- 密码强度指示器 -->
                    <div v-if="passwordForm.newPassword" class="password-strength">
                      <div class="strength-bar">
                        <div
                          class="strength-fill"
                          :class="passwordStrengthClass"
                          :style="{ width: passwordStrengthWidth }"
                        ></div>
                      </div>
                      <span class="strength-text" :class="passwordStrengthClass">
                        {{ passwordStrengthText }}
                      </span>
                    </div>
                  </a-form-item>
                  <a-form-item label="确认新密码" name="confirmPassword" :rules="[
                    { required: true, message: '请再次输入新密码!' },
                    { validator: validateConfirmPassword }
                  ]">
                    <a-input-password v-model:value="passwordForm.confirmPassword" size="large" placeholder="请再次输入新密码" class="form-input" />
                  </a-form-item>
                  <a-form-item>
                    <a-button
                      type="primary"
                      size="large"
                      html-type="submit"
                      class="submit-button"
                      :loading="passwordFormLoading"
                    >
                      更新密码
                    </a-button>
                  </a-form-item>
                </a-form>
              </div>
            </a-tab-pane>
          </a-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from 'vue';
import { message } from 'ant-design-vue';
import { TeamOutlined, ClockCircleOutlined, UserOutlined, IdcardOutlined, PhoneOutlined, MailOutlined } from '@ant-design/icons-vue';
import { getUserProfile, updateUserProfile, updateUserPwd } from '@/api/system/user';
import { useUserStore } from '@/stores/user';
import UserAvatar from './userAvatar.vue';

// 获取 store
const userStore = useUserStore();

// 激活的 Tab
const activeTabKey = ref('basic');
const basicFormRef = ref();
const passwordFormRef = ref();

// 加载状态
const basicFormLoading = ref(false);
const passwordFormLoading = ref(false);

// 密码强度状态
const passwordStrength = ref(0);

// 用户信息
const user = reactive({
  userName: '',
  nickName: '',
  avatar: '',
  deptName: '',
  roleGroup: '',
  postGroup: '',
  phonenumber: '',
  email: '',
  createTime: ''
});

// 基本资料表单状态
const userForm = reactive({
  nickName: '',
  phonenumber: '',
  email: '',
  sex: undefined
});

// 密码表单状态
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

// 格式化创建时间（只显示日期）
const formatCreateTime = (time) => {
  if (!time) return '暂无';
  return time.split(' ')[0];
};

// 密码强度计算
const checkPasswordStrength = () => {
  const pwd = passwordForm.newPassword;
  let strength = 0;

  if (pwd.length >= 6) strength += 1;
  if (pwd.length >= 10) strength += 1;
  if (/[a-z]/.test(pwd) && /[A-Z]/.test(pwd)) strength += 1;
  if (/\d/.test(pwd)) strength += 1;
  if (/[^a-zA-Z0-9]/.test(pwd)) strength += 1;

  passwordStrength.value = Math.min(strength, 3);
};

// 密码强度相关计算属性
const passwordStrengthClass = computed(() => {
  const classes = ['weak', 'medium', 'strong'];
  return classes[passwordStrength.value - 1] || 'weak';
});

const passwordStrengthText = computed(() => {
  const texts = ['弱', '中', '强'];
  return texts[passwordStrength.value - 1] || '弱';
});

const passwordStrengthWidth = computed(() => {
  const widths = ['33%', '66%', '100%'];
  return widths[passwordStrength.value - 1] || '33%';
});

// 确认密码验证器
const validateConfirmPassword = (_, value) => {
  if (value && value !== passwordForm.newPassword) {
    return Promise.reject('两次输入的密码不一致');
  }
  return Promise.resolve();
};

// 获取用户信息
const fetchUserProfile = async () => {
  try {
    const res = await getUserProfile();
    console.log('用户信息响应:', res);

    // 更新基本用户信息
    const userData = res.data || {};
    user.userName = userData.userName || '';
    user.nickName = userData.nickName || '';
    user.createTime = userData.createTime || '';
    user.phonenumber = userData.phonenumber || '';
    user.email = userData.email || '';

    // 处理头像 - 直接使用原始值，让 computed 属性处理URL拼接
    user.avatar = userData.avatar || '';

    // 处理部门信息 - 若依后端返回的部门信息在 dept 对象中
    // 也可能直接在 data 中有 deptName 字段
    if (userData.dept && userData.dept.deptName) {
      user.deptName = userData.dept.deptName;
    } else if (userData.deptName) {
      user.deptName = userData.deptName;
    } else {
      user.deptName = '暂无';
    }

    // 处理角色信息 - 若依后端可能在顶层返回 roleGroup
    user.roleGroup = res.roleGroup || userData.roleGroup || '暂无';

    // 处理岗位信息 - 若依后端可能在顶层返回 postGroup
    user.postGroup = res.postGroup || userData.postGroup || '暂无';

    // 更新表单数据
    Object.assign(userForm, {
      nickName: userData.nickName || '',
      phonenumber: userData.phonenumber || '',
      email: userData.email || '',
      sex: userData.sex
    });
  } catch (err) {
    message.error('获取用户信息失败');
    console.error('获取用户信息失败:', err);
  }
};

// 头像上传成功处理
const handleAvatarSuccess = ({ url, fullUrl }) => {
  // 更新用户头像
  user.avatar = url;
  // 更新 store 中的头像
  userStore.avatar = fullUrl;
};

// 基本资料表单提交
const onFinish = async (values) => {
  basicFormLoading.value = true;
  try {
    const response = await updateUserProfile(userForm);
    message.success(response.msg || '基本资料更新成功！');
    // 更新用户信息
    Object.assign(user, userForm);
    // 直接更新 store 中的用户信息
    userStore.nickName = userForm.nickName;
  } catch (err) {
    message.error('保存失败');
    console.error('保存失败:', err);
  } finally {
    basicFormLoading.value = false;
  }
};

// 修改密码表单提交
const onChangePassword = async (values) => {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    message.error('两次输入的密码不一致');
    return;
  }

  passwordFormLoading.value = true;
  try {
    const response = await updateUserPwd(passwordForm.oldPassword, passwordForm.newPassword);
    message.success(response.msg || '密码修改成功！');
    // 重置表单
    passwordForm.oldPassword = '';
    passwordForm.newPassword = '';
    passwordForm.confirmPassword = '';
    passwordStrength.value = 0;
    // 重置表单验证状态
    passwordFormRef.value.resetFields();
  } catch (err) {
    message.error('密码修改失败');
    console.error('密码修改失败:', err);
  } finally {
    passwordFormLoading.value = false;
  }
};

// 组件挂载时获取用户信息
onMounted(() => {
  fetchUserProfile();
});
</script>

<style scoped>
/* 页面全局样式 */
.account-settings-page {
  padding: 12px;
  min-height: calc(100vh - 48px);
}

/* 统一的白色面板 */
.unified-panel {
  background: var(--component-background);
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  overflow: hidden;
  transition: background-color 0.3s ease;
}

/* 面板布局 - 左右分栏 */
.panel-layout {
  display: flex;
  min-height: 500px;
}

/* 左侧侧边栏 */
.sidebar-section {
  width: 300px;
  flex-shrink: 0;
  padding: 40px 28px 32px;
  background: var(--table-header-bg);
  text-align: center;
  display: flex;
  flex-direction: column;
  transition: background-color 0.3s ease;
}

/* 垂直分割线 */
.vertical-divider {
  width: 1px;
  background: linear-gradient(to bottom, transparent, var(--border-color) 20%, var(--border-color) 80%, transparent);
  flex-shrink: 0;
}

/* 右侧内容区域 */
.content-section {
  flex: 1;
  padding: 32px 48px;
  overflow-y: auto;
}

/* 头像区域 */
.avatar-wrapper {
  margin-bottom: 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.username {
  margin-top: 16px !important;
  margin-bottom: 8px !important;
  font-weight: 600;
  font-size: 18px;
  color: var(--text-primary);
}

.role-tag {
  border-radius: 6px;
  padding: 4px 12px;
  font-size: 13px;
}

/* 信息列表 */
.info-list {
  text-align: left;
  margin-top: 28px;
  flex: 1;
  padding-bottom: 8px;
}

.info-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid var(--border-color);
  min-height: 40px;
  transition: border-color 0.3s ease;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  display: flex;
  align-items: center;
  color: var(--text-secondary);
  font-size: 13px;
  flex-shrink: 0;
  width: 85px;
}

.info-icon {
  margin-right: 6px;
  font-size: 14px;
  color: var(--text-secondary);
}

.info-value {
  color: var(--text-primary);
  font-weight: 500;
  font-size: 13px;
  text-align: right;
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.5;
}

/* Tabs 样式 */
.settings-tabs {
  margin: -8px 0;
}

:deep(.ant-tabs-nav) {
  margin-bottom: 32px !important;
}

:deep(.ant-tabs-tab) {
  font-size: 15px;
  padding: 12px 16px;
}

:deep(.ant-tabs-ink-bar) {
  height: 3px;
  border-radius: 3px;
}

/* 表单内容 */
.form-content {
  max-width: 480px;
}

/* 表单输入框 - 限制宽度 */
.form-input {
  max-width: 450px;
}

:deep(.ant-form-item-label > label) {
  font-weight: 500;
  color: var(--text-primary);
  font-size: 14px;
}

:deep(.ant-input-lg),
:deep(.ant-input-password-lg),
:deep(.ant-select-lg .ant-select-selector) {
  height: 44px;
  border-radius: 8px !important;
  font-size: 14px;
}

:deep(.ant-select-lg .ant-select-selector) {
  padding: 0 16px;
}

/* 密码强度指示器 */
.password-strength {
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.strength-bar {
  flex: 1;
  height: 4px;
  background: var(--border-color);
  border-radius: 2px;
  overflow: hidden;
  transition: background-color 0.3s ease;
}

.strength-fill {
  height: 100%;
  transition: width 0.3s ease, background-color 0.3s ease;
  border-radius: 2px;
}

.strength-fill.weak {
  background: #ff4d4f;
}

.strength-fill.medium {
  background: #faad14;
}

.strength-fill.strong {
  background: #52c41a;
}

.strength-text {
  font-size: 12px;
  font-weight: 500;
  min-width: 24px;
}

.strength-text.weak {
  color: #ff4d4f;
}

.strength-text.medium {
  color: #faad14;
}

.strength-text.strong {
  color: #52c41a;
}

/* 提交按钮 */
.submit-button {
  min-width: 120px;
  height: 44px;
  border-radius: 8px;
  font-weight: 500;
  font-size: 15px;
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.2);
  transition: all 0.3s ease;
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
}

/* 响应式设计 */
@media (max-width: 992px) {
  .panel-layout {
    flex-direction: column;
  }

  .sidebar-section {
    width: 100%;
    border-right: none;
  }

  .vertical-divider {
    display: none;
  }

  .content-section {
    padding: 24px;
  }
}
</style>
