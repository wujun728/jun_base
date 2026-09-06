<template>
  <!-- 添加或修改模版管理对话框 -->
  <el-dialog :title="title" :modelValue="visible" @update:modelValue="emit('update:visible', $event)" width="600px" append-to-body>
    <el-form ref="templateRef" :model="form" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="模版名称" prop="templateName">
            <el-input v-model="form.templateName" placeholder="请输入模版名称" maxlength="50" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="模版签名" prop="templateCode">
            <el-input v-model="form.templateCode" placeholder="请输入模版签名" maxlength="50" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模版类型" prop="templateType">
            <el-select v-model="form.templateType" placeholder="请选择模版类型" style="width: 100%">
              <el-option v-for="dict in template_type" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="选择变量" prop="templateVariable">
            <el-select v-model="form.templateVariable" placeholder="请选择变量" multiple filterable
              @change="handleVariableChange" style="width: 100%">
              <el-option v-for="item in variable" :key="item.variableId" :label="item.variableName"
                :value="item.variableName" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="模版内容" prop="templateContent">
            <el-input v-model="form.templateContent" type="textarea" :rows="6" placeholder="请输入模版内容"
              maxlength="1000" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="场景说明">
            <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入场景说明" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
        <el-button @click="handleCancel">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="TemplateFormDialog">
import { ref, reactive, toRefs, getCurrentInstance, watch } from 'vue';

const { proxy } = getCurrentInstance();
const { template_type } = proxy.useDict("template_type");

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: ''
  },
  formData: {
    type: Object,
    default: () => ({})
  },
  variable: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['update:visible', 'submit', 'cancel']);

const templateRef = ref(null);
const data = reactive({
  form: { templateContent: '' },
  rules: {
    templateName: [{ required: true, message: '请输入模版名称', trigger: 'blur' }],
    templateCode: [{ required: true, message: '请输入模版CODE', trigger: 'blur' }, { validator: validateTemplateCode, trigger: 'blur' }],
    templateType: [{ required: true, message: '请选择模版类型', trigger: 'change' }],
    templateVariable: [{ required: true, message: '请选择变量', trigger: 'change' }],
    templateContent: [{ required: true, message: '请输入模版内容', trigger: 'blur' }]
  }
});

const { form, rules } = toRefs(data);

watch(() => props.formData, (newVal) => {
  if (newVal) {
    form.value = { ...newVal };
  }
}, { deep: true, immediate: true });

function validateTemplateCode(_, value, callback) {
  if (!value) {
    callback(new Error('请输入模版CODE'));
  } else if (!value.startsWith('SMS_')) {
    callback(new Error('模版CODE必须以SMS_开头'));
  } else {
    callback();
  }
}

// 当变量选项改变时触发
function handleVariableChange(value) {
  form.value.templateVariable = value;
  if (!form.value.templateContent) {
    form.value.templateContent = '';
  }

  // 获取当前模板内容中的所有 ${变量名} 格式的占位符
  const currentPlaceholders = form.value.templateContent.match(/\$\{[^}]+\}/g) || [];
  const selectedVariableNames = value.map(v => `\${${v}}`);

  // 移除未选中的变量占位符
  currentPlaceholders.forEach(placeholder => {
    if (!selectedVariableNames.includes(placeholder)) {
      // 使用全局替换，移除所有匹配的占位符及其前后空格
      const escapedPlaceholder = placeholder.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
      const regex = new RegExp('\\s*' + escapedPlaceholder + '\\s*', 'g');
      form.value.templateContent = form.value.templateContent.replace(regex, ' ');
    }
  });

  // 添加新选中的变量占位符（只添加最后一个新选择的变量）
  const lastSelectedVariableName = value[value.length - 1];
  if (lastSelectedVariableName) {
    const variablePlaceholder = `\${${lastSelectedVariableName}}`;
    if (!form.value.templateContent.includes(variablePlaceholder)) {
      // 在模板内容末尾添加新变量
      if (form.value.templateContent.trim()) {
        form.value.templateContent = form.value.templateContent.trim() + ` ${variablePlaceholder}`;
      } else {
        form.value.templateContent = variablePlaceholder;
      }
    }
  }

  // 清理多余的空格
  form.value.templateContent = form.value.templateContent.replace(/\s+/g, ' ').trim();
}

function handleSubmit() {
  templateRef.value.validate(valid => {
    if (valid) {
      emit('submit', form.value);
    }
  });
}

function handleCancel() {
  emit('cancel');
}

function resetForm() {
  form.value = {
    templateId: null, templateName: null, templateCode: null, templateType: null, templateContent: '',
    templateVariable: [], createBy: null, createTime: null, updateBy: null, updateTime: null, remark: null
  };
  if (templateRef.value) {
    templateRef.value.resetFields();
  }
}

defineExpose({
  resetForm
});
</script>
