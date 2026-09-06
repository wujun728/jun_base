<template>
  <a-modal :visible="open" :title="state.title" width="50vw" @cancel="handleCancel" @ok="handleConfirm">
    <a-form ref="formRef" :model="state.form" :rules="state.rules" auto-label-width>
      <a-row :gutter="16">
        <a-col v-if="state.form.parentId !== 0" :span="24">
          <a-form-item label="上级部门" field="parentId">
            <a-tree-select
                v-model="state.form.parentId"
                :field-names="{ key: 'deptId', title: 'deptName', children: 'children' }"
                :data="state.deptOptions"
                :show-count="false"
                placeholder="选择上级部门"
            ></a-tree-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="部门名称" field="deptName">
            <a-input v-model="state.form.deptName" placeholder="请输入部门名称"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="显示排序" field="orderNum">
            <a-input-number v-model="state.form.orderNum" :step="1" :min="0"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="负责人" field="leader">
            <a-input v-model="state.form.leader" placeholder="请输入负责人" maxlength="20"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="联系电话" field="phone">
            <a-input v-model="state.form.phone" placeholder="请输入联系电话" maxlength="11"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="邮箱" field="email">
            <a-input v-model="state.form.email" placeholder="请输入邮箱" maxlength="50"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="部门状态">
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
    </a-form>
  </a-modal>
</template>

<script lang="ts" setup>
import {computed, reactive, ref, watch} from "vue";
import {Form, Message} from "@arco-design/web-vue";
import {addDept, getDept, listDept, listDeptExcludeChild, updateDept} from "@/api/system/detp";
import {DictDataRecord, getDicts} from "@/api/system/dict-data";

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  id: {
    type: Number,
    default: undefined,
  },
  parentId: {
    type: Number,
    default: 0,
  },
});

const open = computed(() => {
  return !!props.visible;
})

const state = reactive({
  title: '新增部门',
  form: {
    deptId: undefined,
    parentId: 0,
    deptName: undefined,
    orderNum: undefined,
    leader: undefined,
    phone: undefined,
    email: undefined,
    status: "0"
  },
  // 表单校验
  rules: {
    parentId: [
      {required: true, message: "上级部门不能为空", trigger: "blur"}
    ],
    deptName: [
      {required: true, message: "部门名称不能为空", trigger: "blur"}
    ],
    orderNum: [
      {required: true, message: "显示排序不能为空", trigger: "blur"}
    ],
    email: [
      {
        type: "email",
        message: "请输入正确的邮箱地址",
        trigger: ["blur", "change"]
      }
    ],
    phone: [
      {
        pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
        message: "请输入正确的手机号码",
        trigger: "blur"
      }
    ]
  } as unknown,
  deptOptions: [] as Array<any>,
  statusOptions: [] as DictDataRecord[],
});

// 初始化字典
const initData = async () => {
  const {data: statusData} = await getDicts('sys_normal_disable');
  state.statusOptions = statusData;
};
initData();

// 数组转树结构集合
const listToTree: any = (list: Array<any>) => {
  const map: any = {};
  const treeData: Array<any> = [];
  list = list.map(item => {
    return {deptId: item.deptId, parentId: item.parentId, deptName: item.deptName,}
  })
  for (let i = 0; i < list.length; i += 1) {
    map[list[i].deptId] = i;
    list[i].children = [];
  }
  for (let i = 0; i < list.length; i += 1) {
    const node = list[i];
    if (node.parentId && list[map[node.parentId]]) {
      list[map[node.parentId]].children.push(node);
    } else {
      treeData.push(node);
    }
  }
  return treeData;
};

// 查询部门下拉树结构
const fetchTreeSelectOption = async () => {
  const {data} = await listDept({});
  state.deptOptions = listToTree(data);
};
fetchTreeSelectOption();

const reset = () => {
  state.title = '新增部门';
  state.form = {
    deptId: undefined,
    parentId: 0,
    deptName: undefined,
    orderNum: undefined,
    leader: undefined,
    phone: undefined,
    email: undefined,
    status: "0"
  };
};

const emits = defineEmits(['update:visible', 'update:parentId', 'refreshDataList']);
const handleCancel = () => {
  emits('update:visible', false);
  emits('update:parentId', 0);
  reset();
};

const formRef = ref<typeof Form>();
const handleConfirm = () => {
  formRef.value?.validate().then((error: any) => {
    if (!error) {
      if (state.form.deptId !== undefined) {
        updateDept(state.form).then(() => {
          Message.success("修改成功");
          emits('update:visible', false);
          emits('update:parentId', 0);
          emits('refreshDataList');
        });
      } else {
        addDept(state.form).then(() => {
          Message.success("新增成功");
          emits('update:visible', false);
          emits('update:parentId', 0);
          emits('refreshDataList');
        });
      }
    }
  });
};

const getDeptForm = async (id: number) => {
  const {data, code, msg} = await getDept(id);
  const {data: deptList} = await listDeptExcludeChild(props.id as number);
  state.deptOptions = listToTree(deptList);
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
          state.title = '修改部门';
          getDeptForm(props.id);
        } else {
          reset();
        }
      } else {
        reset();
        state.form.parentId = props.parentId;
      }
    }
)
watch(
    () => props.parentId,
    (value) => {
      state.form.parentId = value;
    }, {immediate: true},
)
</script>

<style scoped>

</style>
