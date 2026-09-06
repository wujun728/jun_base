<template>
  <a-modal :visible="open" :title="state.title" width="50vw" @cancel="handleCancel" @ok="handleConfirm">
    <a-form ref="formRef" :model="state.form" :rules="state.rules" auto-label-width>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="岗位名称" field="postName">
            <a-input v-model="state.form.postName" placeholder="请输入岗位名称"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="岗位编码" field="postCode">
            <a-input v-model="state.form.postCode" placeholder="请输入岗位编码"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="岗位顺序" field="postSort">
            <a-input-number v-model="state.form.postSort" :step="1" :min="0"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="岗位状态">
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
import {addPost, getPost, updatePost} from "@/api/system/post";

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
  title: '新增岗位',
  form: {
    postId: undefined,
    postCode: undefined,
    postName: undefined,
    postSort: 0,
    status: "0",
    remark: undefined
  },
  // 表单校验
  rules: {
    postName: [
      {required: true, message: "岗位名称不能为空", trigger: "blur"}
    ],
    postCode: [
      {required: true, message: "岗位编码不能为空", trigger: "blur"}
    ],
    postSort: [
      {required: true, message: "岗位顺序不能为空", trigger: "blur"}
    ]
  },
  statusOptions: [] as DictDataRecord[],
});

// 初始化字典
const initData = async () => {
  const {data: statusData} = await getDicts('sys_normal_disable');
  state.statusOptions = statusData;
};
initData();

const reset = () => {
  state.title = '新增岗位';
  state.form = {
    postId: undefined,
    postCode: undefined,
    postName: undefined,
    postSort: 0,
    status: "0",
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
      if (state.form.postId !== undefined) {
        updatePost(state.form).then(() => {
          Message.success("修改成功");
          emits('update:visible', false);
          emits('refreshDataList');
          reset();
        });
      } else {
        addPost(state.form).then(() => {
          Message.success("新增成功");
          emits('update:visible', false);
          emits('refreshDataList');
          reset();
        });
      }
    }
  });
};

const getPostForm = async (id: number) => {
  const {data, code, msg} = await getPost(id);
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
        if (props.id) {
          state.title = '修改岗位';
          getPostForm(props.id);
        } else {
          reset();
        }
      } else {
        reset();
      }
    }, {immediate: true},
);

</script>

<style scoped>

</style>
