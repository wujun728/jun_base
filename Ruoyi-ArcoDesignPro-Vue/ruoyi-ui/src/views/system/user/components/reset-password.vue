<template>
  <a-modal v-model:visible="open" title="重置密码" @cancel="handleCancel" @ok="handleOk">
    <a-form :model="state.form">
      <a-form-item
          label="新密码"
          field="password"
          :rules="[ { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间' } ]"
          :validate-trigger="['change', 'input']"
      >
        <a-input
            v-model="state.form.password"
            placeholder="请输入用户密码"
            type="password"
            maxlength="20"
            show-password/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts" setup>
import {computed, reactive} from 'vue';
import {resetUserPwd} from '@/api/system/user';
import {Message} from '@arco-design/web-vue';

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  id: {
    type: Number,
    default: undefined,
  },
});

const open = computed(() => {
  reset();
  return !!props.visible;
})

const emits = defineEmits(['update:visible']);

const state = reactive({
  form: {
    password: '',
  },
});

const handleOk = async () => {
  const userId = props.id as number;
  const pwd = state.form.password;
  if (userId && pwd) {
    const {code} = await resetUserPwd(userId, pwd);
    if (code && code === 200) {
      Message.success(`修改成功，新密码是：${pwd}`)
      emits('update:visible', false)
    }
  }
};

const handleCancel = () => {
  emits('update:visible', false)
}

const reset = () => {
  state.form.password = '';
}

</script>

<script lang="ts">
export default {
  name: "ResetPassword"
};
</script>
