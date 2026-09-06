<template>
  <a-modal :visible="open" :title="state.title" width="50vw" @cancel="handleCancel" @ok="handleConfirm">
    <a-form ref="formRef" :model="state.form" :rules="state.rules" auto-label-width>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="公告标题" field="noticeTitle">
            <a-input v-model="state.form.noticeTitle" placeholder="请输入公告标题"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="公告类型" field="noticeType">
            <a-select v-model="state.form.noticeType" placeholder="请选择">
              <a-option
                  v-for="item in state.noticeTypeOptions"
                  :key="item.dictValue"
                  :value="item.dictValue"
                  :label="item.dictLabel"
              >{{ item.dictLabel }}
              </a-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="状态">
            <a-radio-group v-model="state.form.status">
              <a-radio
                  v-for="dict in state.noticeStatusOptions"
                  :key="dict.dictValue"
                  :value="dict.dictValue"
                  :label="dict.dictLabel"
              >{{ dict.dictLabel }}
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="内容" field="noticeContent">
            <editor v-if="open" v-model:value="state.form.noticeContent" :mode="'simple'" :min-height="400"/>
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
import {addNotice, getNotice, updateNotice} from "@/api/system/notice";
import Editor from '@/components/editor/index.vue';

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
  title: '新增通知公告',
  form: {
    noticeId: undefined,
    noticeTitle: undefined,
    noticeType: undefined,
    noticeContent: undefined,
    status: "0"
  },
  // 表单校验
  rules: {
    noticeTitle: [
      {required: true, message: "公告标题不能为空", trigger: "blur"}
    ],
    noticeType: [
      {required: true, message: "公告类型不能为空", trigger: "change"}
    ]
  },
  noticeStatusOptions: [] as DictDataRecord[],
  noticeTypeOptions: [] as DictDataRecord[],
});

// 初始化字典
const initData = async () => {
  const {data: noticeStatus} = await getDicts('sys_notice_status');
  state.noticeStatusOptions = noticeStatus;
  const {data: noticeType} = await getDicts('sys_notice_type');
  state.noticeTypeOptions = noticeType;
};
initData();

const reset = () => {
  state.title = '新增通知公告';
  state.form = {
    noticeId: undefined,
    noticeTitle: undefined,
    noticeType: undefined,
    noticeContent: undefined,
    status: "0"
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
      if (state.form.noticeId !== undefined) {
        updateNotice(state.form).then(() => {
          Message.success("修改成功");
          emits('update:visible', false);
          emits('refreshDataList');
        });
      } else {
        addNotice(state.form).then(() => {
          Message.success("新增成功");
          emits('update:visible', false);
          emits('refreshDataList');
        });
      }
    }
  });
};

const getConfigForm = async (id: number) => {
  const {data, code, msg} = await getNotice(id);
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
        state.title = '修改通知公告';
        if (props.id) {
          getConfigForm(props.id);
        } else {
          reset();
        }
      } else {
        reset();
      }
    }, {immediate: true}
);
watch(
    () => props.id,
    (value) => {
    },
    {immediate: true}
)

</script>

<style scoped>

</style>
