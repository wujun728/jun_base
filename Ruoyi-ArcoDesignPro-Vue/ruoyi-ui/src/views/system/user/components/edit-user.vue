<template>
  <a-modal :visible="open" :title="state.title" width="50vw" @cancel="handleCancel" @ok="handleConfirm">
    <a-form ref="formRef" :model="state.form" :rules="state.rules" auto-label-width>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item
              label="用户昵称" field="nickName"
              :rules="[ { required: true, message: '用户昵称不能为空' } ]"
              :validate-trigger="['change', 'input']">
            <a-input v-model="state.form.nickName" placeholder="请输入用户昵称" maxlength="30"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="归属部门" field="deptId">
            <a-tree-select
                v-model="state.form.deptId" :field-names="{
              key: 'id',
              title: 'label',
              children: 'children',
            }" :data="state.deptOptions" :show-count="true" placeholder="请选择归属部门"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item
              label="手机号码" field="phonenumber"
              :rules="[ { match: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: '请输入正确的手机号码' } ]"
              :validate-trigger="['change', 'input']"
          >
            <a-input v-model="state.form.phonenumber" placeholder="请输入手机号码" maxlength="11"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
              label="邮箱" field="email" :rules="[ { type: 'email', message: '请输入正确的邮箱地址', } ]"
              :validate-trigger="['change', 'input']">
            <a-input v-model="state.form.email" placeholder="请输入邮箱" maxlength="50"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item
              v-if="state.form.userId === undefined"
              label="用户名称" field="userName" :rules="[
              { required: true, message: '用户名称不能为空' },
              { min: 2, max: 20, message: '用户名称长度必须介于 2 和 20 之间' }
            ]" :validate-trigger="['change', 'input']">
            <a-input v-model="state.form.userName" placeholder="请输入用户名称" maxlength="30"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
              v-if="state.form.userId === undefined"
              label="用户密码" field="password" :rules="[
            { required: true, message: '用户密码不能为空' },
            { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间' }
          ]" :validate-trigger="['change', 'input']">
            <a-input v-model="state.form.password" placeholder="请输入用户密码" type="password" maxlength="20" show-password/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="用户性别">
            <a-select v-model="state.form.sex" placeholder="请选择性别">
              <a-option
                  v-for="dict in state.sexOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
              ></a-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="状态">
            <a-radio-group v-model="state.form.status">
              <a-radio
                  v-for="dict in state.statusOptions"
                  :key="dict.dictValue"
                  :value="dict.dictValue"
                  :label="dict.dictValue"
              >{{ dict.dictLabel }}
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="岗位">
            <a-select v-model="state.form.postIds" multiple placeholder="请选择岗位">
              <a-option
                  v-for="item in state.postOptions"
                  :key="item.postId"
                  :label="item.postName"
                  :value="item.postId"
                  :disabled="item.status === '1'"
              ></a-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="角色">
            <a-select v-model="state.form.roleIds" multiple placeholder="请选择角色">
              <a-option
                  v-for="item in state.roleOptions"
                  :key="item.roleId"
                  :label="item.roleName"
                  :value="item.roleId"
                  :disabled="item.status === '1'"
              ></a-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item label="备注" label-col-flex="70px">
            <a-textarea v-model="state.form.remark" placeholder="请输入内容"></a-textarea>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script lang="ts" setup>
import {computed, reactive, ref, watch,} from 'vue';
import {addUser, getDeptTreeSelect, getUser, updateUser} from '@/api/system/user';
import {Form, Message} from '@arco-design/web-vue';
import {DictDataRecord, getDicts} from '@/api/system/dict-data';

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  id: {
    type: [Number, String],
    default: undefined,
  },
});

const open = computed(() => {
  return !!props.visible;
})

const state = reactive({
  title: '新增用户',
  deptOptions: [],
  postOptions: [],
  roleOptions: [],
  sexOptions: [],
  statusOptions: [] as DictDataRecord[],
  form: {
    postIds: [],
    roleIds: [],
    userId: undefined,
    nickName: undefined,
    deptId: undefined,
    phonenumber: undefined,
    email: undefined,
    userName: undefined,
    password: undefined,
    sex: undefined,
    status: undefined,
    dictLabel: undefined,
    remark: undefined,
  },
  rules: {}
})

const reset = () => {
  state.title = '新增用户';
  state.form = {
    postIds: [],
    roleIds: [],
    userId: undefined,
    nickName: undefined,
    deptId: undefined,
    phonenumber: undefined,
    email: undefined,
    userName: undefined,
    password: undefined,
    sex: undefined,
    status: undefined,
    dictLabel: undefined,
    remark: undefined,
  };
};

// 获取部门树
const fetchDeptData = async () => {
    const {data} = await getDeptTreeSelect();
    state.deptOptions = data;
};

fetchDeptData();

// 初始化字典
const initData = async () => {
  const {data: statusData} = await getDicts('sys_normal_disable');
  state.statusOptions = statusData;
  const {data: sexData} = await getDicts('sys_user_sex');
  state.sexOptions = sexData;
  const {posts, roles} = await getUser('');
  state.postOptions = posts;
  state.roleOptions = roles;
};

initData();

const getUserForm = async () => {
    state.title = '修改用户';
  const {data, postIds, roleIds, code, msg} = await getUser(props.id as number | string);
    if (code && code === 200) {
        state.form = data;
        state.form.postIds = postIds;
        state.form.roleIds = roleIds;
    } else {
        Message.error(msg)
    }
}

const emits = defineEmits(['update:visible', 'refreshDataList']);
const handleCancel = () => {
  emits('update:visible', false)
}

const formRef = ref<typeof Form>()

function handleConfirm() {
  formRef.value?.validate().then((error: any) => {
    if (!error) {
      if (state.form.userId !== undefined) {
        updateUser(state.form).then(() => {
          Message.success("修改成功");
          emits('update:visible', false)
          emits('refreshDataList')
          reset();
        });
      } else {
        addUser(state.form).then(() => {
          Message.success("新增成功");
          emits('update:visible', false)
          emits('refreshDataList')
          reset();
        })
      }
    }
  })
}

watch(
    () => props.id,
    (value) => {
      if (value) {
        getUserForm();
      } else {
        reset();
      }
    }
)
</script>

<script lang="ts">
export default {
  name: "EditUser"
};
</script>

<style scoped>

</style>
