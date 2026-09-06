<template>
  <div class="recipient-selector">
    <el-form-item label="收件人" prop="recipientType">
      <el-radio-group v-model="recipientType" @change="handleTypeChange">
        <el-radio v-for="type in recipientTypes" :key="type.value" :label="type.value">
          {{ type.label }}
        </el-radio>
      </el-radio-group>
    </el-form-item>

    <el-form-item v-if="recipientType && options.length > 0" :label="selectedLabel" prop="messageRecipient">
      <!-- 部门树形选择 -->
      <el-tree-select v-if="recipientType === 'dept'" v-model="selectedRecipients" :data="options" placeholder="请选择部门"
        clearable :props="{ value: 'deptId', label: 'deptName', children: 'children' }" @change="handleSelectionChange"
        multiple style="width: 100%" />
      <!-- 用户/角色选择 -->
      <el-select v-else v-model="selectedRecipients" placeholder="请选择收件人" @change="handleSelectionChange" multiple
        style="width: 100%">
        <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
    </el-form-item>

    <el-form-item v-if="contactInfo" label="接收号码">
      <el-input v-model="contactInfo" placeholder="接收人联系方式" readonly />
    </el-form-item>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { getSystemResource } from '@modules/message/api/messageSystem';
import { ElMessage } from 'element-plus';

const props = defineProps({
  sendMode: {
    type: String,
    default: ''
  }
});
const emit = defineEmits(['update:recipients', 'update:contactInfo']);
const recipientType = ref('');
const selectedRecipients = ref([]);
const users = ref([]);
const roles = ref([]);
const depts = ref([]);
const contactInfo = ref('');
const recipientTypes = computed(() => [
  { value: 'user', label: '用户' },
  { value: 'role', label: '角色' },
  { value: 'dept', label: '部门' }
]);

const selectedLabel = computed(() => {
  const type = recipientTypes.value.find(t => t.value === recipientType.value);
  return type ? type.label : '';
});

const options = computed(() => {
  if (!recipientType.value) return [];

  switch (recipientType.value) {
    case 'user':
      return users.value.map(user => ({
        id: user.userId || user.id,
        name: user.userName || user.name,
        phonenumber: user.phonenumber,
        email: user.email
      }));
    case 'role':
      return roles.value.map(role => ({
        id: role.roleId || role.id,
        name: role.roleName || role.name
      }));
    case 'dept':
      return depts.value;
    default:
      return [];
  }
});

// 统一资源获取函数
async function fetchSystemResource(type, param = null, sendMode = null) {
  try {
    const response = await getSystemResource(type, param, sendMode);
    return response.data || [];
  } catch (error) {
    console.error(`获取${type}失败:`, error);
    ElMessage.error(`获取${type}失败`);
    return [];
  }
}

// 获取用户列表
async function getUsers() {
  let userData;
  if (props.sendMode === '0' || !props.sendMode) {
    userData = await fetchSystemResource('user');
  } else {
    userData = await fetchSystemResource('usersbysendmode', null, props.sendMode);
  }
  users.value = userData;

  if (userData.length === 0 && props.sendMode && props.sendMode !== '0') {
    const modeText = props.sendMode === '1' ? '手机号' : '邮箱';
    ElMessage.warning(`当前没有填写${modeText}的用户`);
  }
}

// 获取角色列表
async function getRoles() {
  roles.value = await fetchSystemResource('role');
}

// 获取部门列表
async function getDepts() {
  const deptData = await fetchSystemResource('dept');
  depts.value = buildTree(deptData);
}

// 构建部门树
function buildTree(depts) {
  const map = {}, root = [];
  depts.forEach(dept => {
    map[dept.deptId] = dept;
    if (dept.parentId === 0) {
      root.push(dept);
    } else {
      map[dept.parentId] = map[dept.parentId] || {};
      map[dept.parentId].children = map[dept.parentId].children || [];
      map[dept.parentId].children.push(dept);
    }
  });
  return root;
}

const handlers = { user: getUsers, role: getRoles, dept: getDepts };

// 处理收件人选择
async function handleTypeChange() {
  selectedRecipients.value = [];
  contactInfo.value = '';
  const handler = handlers[recipientType.value];
  if (handler) await handler();
}

// 处理选择变化
async function handleSelectionChange() {
  if (!selectedRecipients.value || selectedRecipients.value.length === 0) {
    contactInfo.value = '';
    emit('update:contactInfo', '');
    emit('update:recipients', []);
    return;
  }

  try {
    const users = await getUsersByRecipientType();
    updateContactInfo(users);
    emit('update:recipients', selectedRecipients.value);
  } catch (error) {
    console.error('获取用户信息失败:', error);
    ElMessage.error('获取用户信息失败');
  }
}

// 根据收件人类型获取用户
async function getUsersByRecipientType() {
  let users = [];

  switch (recipientType.value) {
    case 'role':
      for (const roleId of selectedRecipients.value) {
        const roleUsers = await fetchSystemResource('usersbyrole', roleId);
        users.push(...roleUsers);
      }
      break;
    case 'dept':
      for (const deptId of selectedRecipients.value) {
        const deptUsers = await fetchSystemResource('usersbydept', deptId);
        users.push(...deptUsers);
      }
      break;
    case 'user':
      users = options.value.filter(option =>
        selectedRecipients.value.includes(option.id)
      );
      break;
  }

  return users;
}

// 更新联系方式
function updateContactInfo(users) {
  const validContacts = users.map(user => {
    let contact = '';
    if (props.sendMode === '0') {
      contact = user.userName || user.name || user.nickName || '';
    } else if (props.sendMode === '1') {
      contact = user.phonenumber || '';
    } else if (props.sendMode === '2') {
      contact = user.email || '';
    }
    return contact;
  }).filter(contact => contact && contact.trim() !== '');

  const uniqueContacts = [...new Set(validContacts)];
  contactInfo.value = uniqueContacts.join(', ');
  emit('update:contactInfo', contactInfo.value);

  if (!contactInfo.value && props.sendMode && props.sendMode !== '0') {
    const contactType = props.sendMode === '1' ? '手机号' : '邮箱地址';
    ElMessage.warning(`所选收件人中没有有效的${contactType}信息！`);
  }
}

// 监听发送方式变化
watch(() => props.sendMode, () => {
  if (recipientType.value === 'user') {
    getUsers();
  }
  if (selectedRecipients.value.length > 0) {
    handleSelectionChange();
  }
});

// 暴露方法给父组件
defineExpose({
  getRecipients: getUsersByRecipientType,
  reset: () => {
    recipientType.value = '';
    selectedRecipients.value = [];
    contactInfo.value = '';
    users.value = [];
    roles.value = [];
    depts.value = [];
  }
});
</script>
