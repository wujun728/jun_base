<template>
  <el-form ref="userRef" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="用户昵称" prop="nickName">
      <el-input v-model="user.nickName" maxlength="30" />
    </el-form-item>
    <el-form-item label="手机号码" prop="phonenumber">
      <span style="padding-right: 20px;">{{ user.phonenumber || '未绑定' }}</span>
      <el-link v-if="user.phonenumber" type="danger" underline="never"
        @click="openVerify('phone', 'reset')">解绑</el-link>
      <el-link v-else type="primary" underline="never" @click="openVerify('phone', 'bind')">绑定</el-link>
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <span style="padding-right: 20px;">{{ user.email || '未绑定' }}</span>
      <el-link v-if="user.email" type="danger" underline="never" @click="openVerify('email', 'reset')">解绑</el-link>
      <el-link v-else type="primary" underline="never" @click="openVerify('email', 'bind')">绑定</el-link>
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="user.sex">
        <el-radio value="0">男</el-radio>
        <el-radio value="1">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">保存</el-button>
      <el-button type="danger" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>

  <!-- 绑定/解绑 验证弹窗 -->
  <el-dialog v-model="verify.visible" :title="verifyTitle" width="420px" :close-on-click-modal="false">
    <el-form :model="verify" label-width="90px">
      <el-form-item :label="verify.type === 'phone' ? '手机' : '邮箱'">
        <!-- 解绑时展示原值，绑定时可填写新值 -->
        <template v-if="verify.mode === 'reset'">
          <el-input :model-value="verify.value" disabled />
        </template>
        <template v-else>
          <el-input v-model="verify.value" :placeholder="verify.type === 'phone' ? '请输入手机号' : '请输入邮箱'" />
        </template>
      </el-form-item>
      <el-form-item label="验证码">
        <div style="display:flex; gap:8px; width:100%">
          <el-input v-model="verify.code" placeholder="请输入验证码" maxlength="6" />
          <el-button :disabled="verify.countdown > 0 || verify.sending" @click="sendCode">
            {{ verify.countdown > 0 ? `${verify.countdown}s` : '发送验证码' }}
          </el-button>
        </div>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="verify.visible = false">取消</el-button>
      <el-button type="primary" :loading="verify.submitting" @click="confirmVerify">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { updateUserProfile } from "@/api/system/user";
import {
  sendEmailCode,
  verifyEmailCode,
  sendPhoneCode,
  verifyPhoneCode,
  getInfo
} from '@/api/login'
import { ref, reactive, computed, onUnmounted, getCurrentInstance } from 'vue'

const props = defineProps({
  user: {
    type: Object
  }
});

const { proxy } = getCurrentInstance();

const rules = ref({
  nickName: [{ required: true, message: "用户昵称不能为空", trigger: "blur" }],
});

/** 提交按钮 */
function submit() {
  proxy.$refs.userRef.validate(valid => {
    if (valid) {
      updateUserProfile(props.user).then(response => {
        proxy.$modal.msgSuccess("修改成功");
      });
    }
  });
};
/** 关闭按钮 */
function close() {
  proxy.$tab.closePage();
};

const verify = reactive({
  visible: false,
  type: 'phone', // 'phone' | 'email'
  mode: 'bind', // 'bind' | 'reset'
  value: '',
  code: '',
  countdown: 0,
  timer: 0,
  sending: false,
  submitting: false,
})

const verifyTitle = computed(() => {
  const t = verify.type === 'phone' ? '手机号' : '邮箱'
  const m = verify.mode === 'bind' ? '绑定' : '解绑'
  return `${m}${t}`
})

function openVerify(type, mode) {
  verify.type = type
  verify.mode = mode
  verify.visible = true
  verify.code = ''
  if (mode === 'reset') {
    verify.value = type === 'phone' ? (props.user?.phonenumber || '') : (props.user?.email || '')
  } else {
    verify.value = ''
  }
}

function startCountdown(sec = 60) {
  clearCountdown()
  verify.countdown = sec
  verify.timer = window.setInterval(() => {
    verify.countdown--
    if (verify.countdown <= 0) {
      clearCountdown()
    }
  }, 1000)
}

function clearCountdown() {
  if (verify.timer) {
    clearInterval(verify.timer)
    verify.timer = 0
  }
}

onUnmounted(() => clearCountdown())

function isValidPhone(p) {
  return /^1[3-9]\d{9}$/.test(p)
}
function isValidEmail(e) {
  return /.+@.+\..+/.test(e)
}

const getTarget = () => verify.value || (verify.mode === 'reset'
  ? (verify.type === 'phone' ? props.user?.phonenumber : props.user?.email)
  : '')

const validateTarget = (target) => {
  if (verify.type === 'phone') {
    if (!target || !isValidPhone(target)) {
      proxy.$modal.msgError('请输入有效的手机号')
      return false
    }
  } else {
    if (!target || !isValidEmail(target)) {
      proxy.$modal.msgError('请输入有效的邮箱')
      return false
    }
  }
  return true
}

const sendCodeApi = async (target) => {
  if (verify.type === 'phone') {
    return sendPhoneCode({ phonenumber: target }, verify.mode)
  } else {
    return sendEmailCode({ email: target }, verify.mode)
  }
}

const verifyCodeApi = async (target) => {
  if (verify.type === 'phone') {
    return verifyPhoneCode({ phonenumber: target, code: verify.code }, verify.mode)
  } else {
    return verifyEmailCode({ email: target, code: verify.code }, verify.mode)
  }
}

async function sendCode() {
  const target = getTarget()
  if (!validateTarget(target)) return
  try {
    verify.sending = true
    await sendCodeApi(target)
    proxy.$modal.msgSuccess('验证码已发送')
    startCountdown(60)
  } finally {
    verify.sending = false
  }
}

async function confirmVerify() {
  const target = getTarget()
  if (!verify.code || verify.code.length < 4) return proxy.$modal.msgError('请输入正确的验证码')
  if (!validateTarget(target)) return
  try {
    verify.submitting = true
    await verifyCodeApi(target)
    // 刷新用户数据
    try {
      const res = await getInfo()
      const u = res?.user || res?.data?.user || {}
      if (u) {
        if ('phonenumber' in u) props.user.phonenumber = u.phonenumber
        if ('email' in u) props.user.email = u.email
      }
    } catch (e) { /* ignore */ }
    proxy.$modal.msgSuccess(`${verify.mode === 'bind' ? '绑定' : '解绑'}成功`)
    verify.visible = false
  } finally {
    verify.submitting = false
  }
}
</script>
