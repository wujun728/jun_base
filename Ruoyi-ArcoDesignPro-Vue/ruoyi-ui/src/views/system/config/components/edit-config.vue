<template>
  <a-modal :visible="open" :title="state.title" width="50vw" @cancel="handleCancel" @ok="handleConfirm">
    <a-form ref="formRef" :model="state.form" :rules="state.rules" auto-label-width>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="参数名称" field="configName">
            <a-input v-model="state.form.configName" placeholder="请输入参数名称"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="参数键名" field="configKey">
            <a-input v-model="state.form.configKey" placeholder="请输入参数键名"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="参数键值" field="configValue">
            <a-input v-model="state.form.configValue" placeholder="请输入参数键值"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="系统内置">
            <a-radio-group v-model="state.form.configType">
              <a-radio
                  v-for="dict in state.configTypeOptions"
                  :key="dict.dictValue"
                  :value="dict.dictValue"
                  :label="dict.dictLabel"
              >{{ dict.dictLabel }}
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="备注" field="remark">
            <a-textarea v-model="state.form.remark" type="textarea" placeholder="请输入内容"></a-textarea>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script lang="ts" setup>
import {computed, reactive, ref, watch} from "vue";
import {DictDataRecord, getDicts} from "@/api/system/dict-data";
import {Form, Message} from "@arco-design/web-vue";
import {addConfig, getConfig, updateConfig} from "@/api/system/config";

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
  return !!props.visible;
});

const state = reactive({
  title: '新增参数',
  form: {
    configId: undefined,
    configName: undefined,
    configKey: undefined,
    configValue: undefined,
    configType: "Y",
    remark: undefined
  },
  // 表单校验
  rules: {
    configName: [
      {required: true, message: "参数名称不能为空", trigger: "blur"}
    ],
    configKey: [
      {required: true, message: "参数键名不能为空", trigger: "blur"}
    ],
    configValue: [
      {required: true, message: "参数键值不能为空", trigger: "blur"}
    ]
  },
  configTypeOptions: [] as DictDataRecord[],
});

// 初始化字典
const initData = async () => {
  const {data: yesNoData} = await getDicts('sys_yes_no');
  state.configTypeOptions = yesNoData;
};
initData();

const reset = () => {
  state.title = '新增参数';
  state.form = {
    configId: undefined,
    configName: undefined,
    configKey: undefined,
    configValue: undefined,
    configType: "Y",
    remark: undefined
  };
};

const emits = defineEmits(['update:visible', 'refreshDataList']);
const handleCancel = () => {
  emits('update:visible', false);
  reset();
};

const formRef = ref<typeof Form>();
const handleConfirm = () => {
  formRef.value?.validate().then((error: any) => {
    if (!error) {
      if (state.form.configId !== undefined) {
        updateConfig(state.form).then(() => {
          Message.success("修改成功");
          emits('update:visible', false);
          emits('refreshDataList');
        });
      } else {
        addConfig(state.form).then(() => {
          Message.success("新增成功");
          emits('update:visible', false);
          emits('refreshDataList');
        });
      }
    }
  });
};

const getConfigForm = async (id: number) => {
  const {data, code, msg} = await getConfig(id);
  if (code && code === 200) {
    state.form = data;
  } else {
    Message.error(msg)
  }
};

watch(
    () => props.visible,
    (value) => {
      if (value) {
        state.title = '修改参数';
        if (props.id) {
          getConfigForm(props.id);
        } else {
          reset();
        }
      } else {
        reset();
      }
    }
    , {immediate: true}
);

</script>

<style scoped>

</style>
