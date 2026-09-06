<template>
  <a-modal :visible="open" :title="state.title" width="50vw" @cancel="handleCancel" @ok="handleConfirm">
    <a-form ref="formRef" :model="state.form" :rules="state.rules" auto-label-width>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item label="上级菜单" field="parentId">
            <a-tree-select
                v-model="state.form.parentId"
                :field-names="{ key: 'menuId', title: 'menuName', children: 'children' }"
                :data="state.menuOptions"
                :show-count="false"
                placeholder="选择上级菜单"
            ></a-tree-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="菜单类型" field="menuType">
            <a-radio-group v-model="state.form.menuType" default-value="M">
              <a-radio key="M" value="M">目录</a-radio>
              <a-radio key="C" value="C">菜单</a-radio>
              <a-radio key="F" value="F">按钮</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col v-if="state.form.menuType !== 'F'" :span="24">
          <a-form-item label="菜单图标" field="icon">
            <icon-picker v-model:icon="state.form.icon">
              <template #iconSelect>
                <a-input v-model="state.form.icon">
                  <template #prefix>
                    <functional-icons :icon="state.form.icon"></functional-icons>
                  </template>
                </a-input>
              </template>
            </icon-picker>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="菜单名称" field="menuName">
            <a-input v-model="state.form.menuName" placeholder="请输入菜单名称"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="显示排序" field="orderNum">
            <a-input-number v-model="state.form.orderNum" :step="1" :min="0"/>
          </a-form-item>
        </a-col>
        <a-col v-if="state.form.menuType !== 'F'" :span="12">
          <a-form-item label="是否外链" field="isFrame" tooltip="选择是外链则路由地址需要以`http(s)://`开头">
            <a-radio-group v-model="state.form.isFrame" default-value="0">
              <a-radio key="0" value="0">是</a-radio>
              <a-radio key="1" value="1">否</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col v-if="state.form.menuType !== 'F'" :span="12">
          <a-form-item label="路由地址" field="path" tooltip="访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头">
            <a-input v-model="state.form.path" placeholder="请输入路由地址"/>
          </a-form-item>
        </a-col>
        <a-col v-if="state.form.menuType === 'C'" :span="12">
          <a-form-item label="组件路径" field="component" tooltip="访问的组件路径，如：`system/user/index`，默认在`views`目录下">
            <a-input v-model="state.form.component" placeholder="请输入组件路径"/>
          </a-form-item>
        </a-col>
        <a-col v-if="state.form.menuType !== 'M'" :span="12">
          <a-form-item
              label="权限字符" field="perms" tooltip="控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasPermi('system:user:list')`)">
            <a-input v-model="state.form.perms" placeholder="请输入权限标识" maxlength="100"/>
          </a-form-item>
        </a-col>
        <a-col v-if="state.form.menuType === 'C'" :span="12">
          <a-form-item label="路由参数" field="query" tooltip='访问路由的默认传递参数，如：`{"id": 1, "name": "ry"}`'>
            <a-input v-model="state.form.query" placeholder="请输入路由参数" maxlength="255"/>
          </a-form-item>
        </a-col>
        <a-col v-if="state.form.menuType === 'C'" :span="12">
          <a-form-item label="是否缓存" field="isCache" tooltip="选择是则会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致">
            <a-radio-group v-model="state.form.isCache" default-value="1">
              <a-radio key="0" value="0">缓存</a-radio>
              <a-radio key="1" value="1">不缓存</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col v-if="state.form.menuType !== 'F'" :span="12">
          <a-form-item label="显示状态" tooltip="选择隐藏则路由将不会出现在侧边栏，但仍然可以访问">
            <a-radio-group v-model="state.form.visible">
              <a-radio
                  v-for="dict in state.visibleOptions"
                  :key="dict.dictValue"
                  :value="dict.dictValue"
                  :label="dict.dictValue"
              >{{ dict.dictLabel }}
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col v-if="state.form.menuType !== 'F'" :span="12">
          <a-form-item label="菜单状态" tooltip="选择停用则路由将不会出现在侧边栏，也不能被访问">
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
import {addMenu, getMenu, listMenu, updateMenu} from "@/api/system/menu";
import {getDicts} from "@/api/system/dict-data";
import FunctionalIcons from '@/components/functional-icons/index.vue';
import IconPicker from '@sanqi377/arco-vue-icon-picker';

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  id: {
    type: [Number, String],
    default: undefined,
  },
  parentId: {
    type: Number,
    default: 0,
  },
});

const open = computed(() => {
  return props.visible;
})

const state = reactive({
  title: '新增菜单',
  form: {
    menuId: undefined,
    parentId: 0,
    menuName: undefined,
    icon: undefined,
    menuType: 'M',
    orderNum: undefined,
    isFrame: '1',
    isCache: '0',
    visible: '0',
    status: '0',
    path: undefined,
  },
  menuOptions: [] as Array<any>,
  visibleOptions: [],
  statusOptions: [],
})

// 初始化字典
const initData = async () => {
  const {data: visibleData} = await getDicts('sys_show_hide');
  state.visibleOptions = visibleData;
  const {data: statusData} = await getDicts('sys_normal_disable');
  state.statusOptions = statusData;
};
initData();

// 数组转树结构集合
const listToTree: any = (list: Array<any>) => {
  const map: any = {};
  const treeData: Array<any> = [];
  list = list.map(item => {
    return {menuId: item.menuId, parentId: item.parentId, menuName: item.menuName,}
  })
  for (let i = 0; i < list.length; i += 1) {
    map[list[i].menuId] = i;
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

// 查询菜单下拉树结构
const fetchTreeSelectOption = async () => {
  const {data} = await listMenu({});
  const menu = {menuId: 0, menuName: '主类目', children: [...listToTree(data)]};
  state.menuOptions.push(menu);
};
fetchTreeSelectOption();

const reset = () => {
  state.title = '新增菜单';
  state.form = {
    menuId: undefined,
    parentId: 0,
    menuName: undefined,
    icon: undefined,
    menuType: 'M',
    orderNum: undefined,
    isFrame: '1',
    isCache: '0',
    visible: '0',
    status: '0',
    path: undefined,
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
      if (state.form.menuId !== undefined) {
        updateMenu(state.form).then(() => {
          Message.success("修改成功");
          emits('update:visible', false);
          emits('update:parentId', 0);
          emits('refreshDataList');
          reset();
        });
      } else {
        addMenu(state.form).then(() => {
          Message.success("新增成功");
          emits('update:visible', false);
          emits('update:parentId', 0);
          emits('refreshDataList');
          reset();
        });
      }
    }
  });
};

const getMenuForm = async (id: number | string) => {
  const {data, code, msg} = await getMenu(id);
  if (code && code === 200) {
    state.form = data;
  } else {
    Message.error(msg)
  }
};

watch(
    () => props.id,
    (value) => {
      if (value) {
        state.title = '修改菜单';
        getMenuForm(value);
      } else {
        reset();
        state.form.parentId = props.parentId;
      }
    },
    {immediate: true}
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
