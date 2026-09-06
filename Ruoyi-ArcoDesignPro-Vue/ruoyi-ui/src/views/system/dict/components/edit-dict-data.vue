<template>
  <a-modal :visible="open" :title="state.title" width="50vw" @cancel="handleCancel" @ok="handleConfirm">
    <a-form ref="formRef" :model="state.form" :rules="state.rules" auto-label-width>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="字典类型" field="dictType">
            <a-input v-model="state.form.dictType" placeholder="请输入字典名称" :disabled="true"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="数据标签" field="dictLabel">
            <a-input v-model="state.form.dictLabel" placeholder="请输入数据标签"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="数据键值" field="dictValue">
            <a-input v-model="state.form.dictValue" placeholder="请输入数据键值"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="样式属性" field="cssClass">
            <a-input v-model="state.form.cssClass" placeholder="请输入样式属性"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="显示排序" field="listClass">
            <a-input-number v-model="state.form.dictSort" :step="1" :min="0"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="回显样式" field="cssClass">
            <a-select v-model="state.form.listClass" placeholder="请选择">
              <a-option
                  v-for="item in state.listClassOptions"
                  :key="item.value"
                  :value="item.value"
                  :label="item.label + '(' + item.value + ')'"
              >{{ item.label + '(' + item.value + ')' }}
              </a-option>
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
import {addData, DictDataRecord, getData, getDicts, updateData} from "@/api/system/dict-data";
import {Form, Message} from "@arco-design/web-vue";

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  dictCode: {
    type: [String, Number],
    default: '',
  },
  dictType: {
    type: Object,
  },
});

const open = computed(() => {
  return !!props.visible;
});

const state = reactive({
  title: '新增字典数据',
  form: {
    dictCode: undefined,
    dictType: '',
    dictLabel: undefined,
    dictValue: undefined,
    cssClass: undefined,
    listClass: 'default',
    dictSort: 0,
    status: "0",
    remark: undefined
  },
  // 表单校验
  rules: {
    dictLabel: [
      {required: true, message: "数据标签不能为空", trigger: "blur"}
    ],
    dictValue: [
      {required: true, message: "数据键值不能为空", trigger: "blur"}
    ],
    dictSort: [
      {required: true, message: "数据顺序不能为空", trigger: "blur"}
    ]
  },
  // 数据标签回显样式
  listClassOptions: [
    {
      value: "default",
      label: "默认"
    },
    {
      value: "primary",
      label: "主要"
    },
    {
      value: "success",
      label: "成功"
    },
    {
      value: "info",
      label: "信息"
    },
    {
      value: "warning",
      label: "警告"
    },
    {
      value: "danger",
      label: "危险"
    }
  ],
  statusOptions: [] as DictDataRecord[],
});

// 初始化字典
const initData = async () => {
  const {data: statusData} = await getDicts('sys_normal_disable');
  state.statusOptions = statusData;
};
initData();

const reset = () => {
  state.title = '新增字典数据';
  state.form = {
    dictCode: undefined,
    dictType: '',
    dictLabel: undefined,
    dictValue: undefined,
    cssClass: undefined,
    listClass: 'default',
    dictSort: 0,
    status: "0",
    remark: undefined
  };
};

// 关闭
const emits = defineEmits(['update:visible', 'refreshDataList']);
const handleCancel = () => {
  emits('update:visible', false);
  reset();
};

// 提交表单
const formRef = ref<typeof Form>();
const handleConfirm = () => {
  formRef.value?.validate().then((error: any) => {
    if (!error) {
      if (state.form.dictCode !== undefined) {
        updateData(state.form).then(() => {
          Message.success("修改成功");
          emits('update:visible', false);
          emits('refreshDataList');
        });
      } else {
        addData(state.form).then(() => {
          Message.success("新增成功");
          emits('update:visible', false);
          emits('refreshDataList');
        });
      }
    }
  });
};

// 查询表单详情
const getDictDataForm = async (dictCode: string) => {
  const {data, code, msg} = await getData(dictCode);
  if (code && code === 200) {
    state.form = data;
  } else {
    Message.error(msg)
  }
};

watch(
    () => props.dictCode,
    (value) => {
      if (value) {
        state.title = '修改字典数据';
        getDictDataForm(value as string);
      } else {
        reset();
      }
    }
);

watch(
    () => props.visible,
    (value) => {
      if (value) {
        reset();
        const dictType = props.dictType as any;
        state.form.dictType = dictType.dictType;
      }
    }
);

</script>

<style scoped>

</style>
