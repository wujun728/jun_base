<template>
  <!-- 添加或修改消息管理对话框 -->
  <el-dialog v-model="dialogVisible" :title="title" width="600px" append-to-body>
    <el-form ref="messageSystemFormRef" :model="form" :rules="rules" label-width="80px" v-loading="loading">
      <el-row>
        <el-col :span="24">
          <el-form-item label="消息标题" prop="messageTitle">
            <el-input v-model="form.messageTitle" placeholder="请输入消息标题" maxlength="50" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="发送方式" prop="sendMode">
            <el-select v-model="form.sendMode" placeholder="请选择发送方式" @change="handleSendModeChange" style="width: 100%">
              <el-option v-for="dict in send_mode" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="消息类型" prop="messageType">
            <el-select v-model="form.messageType" placeholder="请选择消息类型" style="width: 100%">
              <el-option v-for="dict in message_type" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 消息内容组件 -->
      <div class="message-content">
        <el-form-item label="内容类型" prop="contentType">
          <el-radio-group v-model="contentType" @change="handleContentTypeChange">
            <el-radio label="template">模板签名</el-radio>
            <el-radio label="content">消息内容</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 模板选择 -->
        <el-form-item v-if="contentType === 'template'" label="模板选择" prop="messageContent">
          <el-select v-model="form.messageContent" placeholder="请选择模板签名" style="width: 100%"
            @change="handleTemplateChange" :loading="templateLoading">
            <el-option v-for="temp in templates" :key="temp.templateId" :label="temp.templateCode"
              :value="temp.templateCode" />
          </el-select>
        </el-form-item>

        <!-- 内容输入 -->
        <el-form-item v-else label="消息内容" prop="messageContent">
          <el-input v-model="form.messageContent" type="textarea" :rows="3" placeholder="短信格式须为: 模板名?模板参数=值"
            maxlength="100" show-word-limit @input="handleContentInput" />
        </el-form-item>
      </div>

      <!-- 收件人选择组件 -->
      <RecipientSelector :send-mode="form.sendMode" @update:recipients="handleRecipientsUpdate"
        @update:contactInfo="form.code = $event" ref="recipientSelectorRef" />

      <!-- 备注区域 -->
      <div class="form-section">
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="可选：添加备注信息" />
            </el-form-item>
          </el-col>
        </el-row>
      </div>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
        <el-button @click="toggleDialog">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, getCurrentInstance, watch, nextTick } from 'vue';
import { batchAddMessage } from '@modules/message/api/messageSystem';
import { selecTemplates } from '@modules/message/api/template';
import { ElMessage } from 'element-plus';
import RecipientSelector from './recipientSelector.vue';

const props = defineProps({ title: { type: String, default: "发送消息" } });
const { proxy } = getCurrentInstance();
const { send_mode, message_type } = proxy.useDict("send_mode", "message_type");
const emit = defineEmits(['success', 'close']);
const messageSystemFormRef = ref(null);
const recipientSelectorRef = ref(null);
const dialogVisible = ref(false);
const loading = ref(false);

const form = ref({
  messageTitle: null,
  messageContent: null,
  messageRecipient: [],
  remark: null,
  sendMode: null,
  code: null,
  contentType: "template",
  messageType: null
});
const contentType = ref('template');
const templates = ref([]);
const templateLoading = ref(false);

// 表单验证规则
const rules = ref({
  messageTitle: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  sendMode: [{ required: true, message: '请选择发送方式', trigger: 'change' }],
  messageRecipient: [
    {
      required: true,
      message: '请选择收件人',
      trigger: 'change',
      validator: (_, value, callback) => {
        if (!value || (Array.isArray(value) && value.length === 0)) {
          callback(new Error('请选择收件人'));
        } else {
          callback();
        }
      }
    }
  ],
  messageType: [{ required: true, message: '请选择消息类型', trigger: 'change' }],
  messageContent: [{ required: true, message: '模版签名或消息内容不能为空', trigger: 'blur' }]
});

// 处理发送方式改变
function handleSendModeChange() {
  form.value.code = '';
}

// 处理收件人更新
function handleRecipientsUpdate(recipients) {
  form.value.messageRecipient = recipients;
  // 手动触发表单验证
  nextTick(() => {
    if (messageSystemFormRef.value) {
      messageSystemFormRef.value.validateField('messageRecipient');
    }
  });
}

// 获取模板列表
async function getTemplates() {
  try {
    templateLoading.value = true;
    const response = await selecTemplates();
    templates.value = response.data || [];
  } catch (error) {
    console.error('获取模板信息失败:', error);
    ElMessage.error('获取模板信息失败');
  } finally {
    templateLoading.value = false;
  }
}

// 处理内容类型变化
function handleContentTypeChange() {
  form.value.messageContent = '';
}

// 处理模板选择变化
function handleTemplateChange(value) {
  form.value.messageContent = value;
}

// 处理内容输入变化
function handleContentInput(value) {
  form.value.messageContent = value;
}

// 验证消息内容格式
function validateMessageContent(value) {
  if (form.value.sendMode === '1' && contentType.value === 'content') {
    const pattern = /^[^?]+?\?[^=]+?=.*$/;
    if (!pattern.test(value)) {
      return '短信输入内容格式必须为：模板名?模板参数=值';
    }
  }
  return true;
}

// 监听发送方式变化
watch(() => form.value.sendMode, () => {
  // 发送方式变化时可能需要重新验证内容格式
  if (form.value.messageContent) {
    const validation = validateMessageContent(form.value.messageContent);
    if (validation !== true) {
      ElMessage.warning(validation);
    }
  }
});

// 发送消息表单提交
async function submitForm() {
  loading.value = true;
  try {
    await messageSystemFormRef.value.validate();
    // 验证消息内容格式
    const contentValidation = validateMessageContent(form.value.messageContent);
    if (contentValidation !== true) {
      throw new Error(contentValidation);
    }

    // 从子组件获取收件人信息
    const recipients = await recipientSelectorRef.value.getRecipients();
    if (!recipients || recipients.length === 0) {
      ElMessage.warning('请选择收件人');
      return;
    }

    // 构建消息数据
    const messages = recipients.map(recipient => ({
      ...form.value,
      messageRecipient: recipient.userName || recipient.name,
      code: form.value.sendMode === '1' ? recipient.phonenumber :
        form.value.sendMode === '2' ? recipient.email :
          recipient.name,
      sendMode: form.value.sendMode
    }));
    await batchAddMessage(messages);
    ElMessage.success("消息发送成功！");
    emit('success');
    toggleDialog();
  } catch (error) {
    console.error('Submit form error:', error);
  } finally {
    loading.value = false;
  }
}

// 重置表单
function reset() {
  form.value = {
    messageTitle: null,
    messageContent: null,
    messageRecipient: [],
    remark: null,
    sendMode: null,
    code: null,
    contentType: "template",
    messageType: null
  };
  contentType.value = 'template';
  if (recipientSelectorRef.value) {
    recipientSelectorRef.value.reset();
  }
}

// 切换对话框显示状态
function toggleDialog() {
  dialogVisible.value = !dialogVisible.value;
  if (!dialogVisible.value) {
    reset();
    emit('close');
    loading.value = false;
  }
}

defineExpose({
  open: () => {
    dialogVisible.value = true;
  }
});
getTemplates();
</script>