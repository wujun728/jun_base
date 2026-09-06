<template>
  <a-form
      ref="genInfoForm"
      :model="form"
      :rules="state.rules"
      :label-col-props="{ span: 6 }"
      :wrapper-col-props="{ span: 18 }"
      label-align="left"
      auto-label-width
  >
    <a-row :gutter="16">
      <a-col :span="8" :offset="2">
        <a-form-item field="tplCategory" :label="'生成模板'">
          <a-select v-model="form.tplCategory" @change="handleTplSelectChange">
            <a-option label="单表（增删改查）" value="crud"/>
            <a-option label="树表（增删改查）" value="tree"/>
            <a-option label="主子表（增删改查）" value="sub"/>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="packageName" :label="'生成包路径'" tooltip="生成在哪个java包下，例如 com.ruoyi.system">
          <a-input v-model="form.packageName" placeholder="请输入生成包路径"/>
        </a-form-item>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="moduleName" :label="'生成模块名'" tooltip="可理解为子系统名，例如 system">
          <a-input v-model="form.moduleName" placeholder="请输入生成模块名"/>
        </a-form-item>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="businessName" :label="'生成业务名'" tooltip="可理解为功能英文名，例如 user">
          <a-input v-model="form.businessName" placeholder="请输入生成业务名"/>
        </a-form-item>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="functionName" :label="'生成功能名'" tooltip="用作类描述，例如 用户">
          <a-input v-model="form.functionName" placeholder="请输入生成功能名"/>
        </a-form-item>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="parentMenuId" :label="'上级菜单'" tooltip="分配到指定菜单下，例如 系统管理">
          <a-tree-select
              v-model="form.parentMenuId"
              :field-names="{ key: 'menuId', title: 'menuName', children: 'children' }"
              :data="state.menuOptions"
              :show-count="false"
              placeholder="选择上级菜单"
          ></a-tree-select>
        </a-form-item>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="genType" :label="'生成代码方式'" tooltip="默认为zip压缩包下载，也可以自定义生成路径">
          <a-radio-group
              v-model="form.genType" :options="[{label:'zip压缩包', value:'0'},{label:'自定义路径', value:'1'}]"/>
        </a-form-item>
      </a-col>
      <a-col v-if="form.genType == '1'" :span="18" :offset="2">
        <a-form-item field="genPath" :label="'自定义路径'" tooltip="填写磁盘绝对路径，若不填写，则生成到当前Web项目下">
          <a-input v-model="form.genPath">
            <template #append>
              <a-dropdown trigger="hover">
                <a-button type="primary">
                  最近路径快速选择
                </a-button>
                <template #content>
                  <a-doption>
                    <a-button type="primary" @click="form.genPath = '/'">
                      恢复默认的生成基础路径
                    </a-button>
                  </a-doption>
                </template>
              </a-dropdown>
            </template>
          </a-input>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row v-if="form.tplCategory === 'tree'" :gutter="16">
      <a-col :span="19" :offset="1">
        <h4 class="form-header">其他信息</h4>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="treeCode" :label="'树编码字段'" tooltip="树显示的编码字段名">
          <a-select v-model="form.treeCode">
            <a-option
                v-for="(column, index) in form.columns"
                :key="index"
                :label="column.columnName + '：' + column.columnComment"
                :value="column.columnName"
            />
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="treeParentCode" :label="'树父编码字段'" tooltip="树显示的父编码字段名， 如：parent_Id">
          <a-select v-model="form.treeParentCode">
            <a-option
                v-for="(column, index) in form.columns"
                :key="index"
                :label="column.columnName + '：' + column.columnComment"
                :value="column.columnName"
            />
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="treeName" :label="'树名称字段'" tooltip="树节点的显示名称字段名， 如：dept_name">
          <a-select v-model="form.treeName">
            <a-option
                v-for="(column, index) in form.columns"
                :key="index"
                :label="column.columnName + '：' + column.columnComment"
                :value="column.columnName"
            />
          </a-select>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row v-if="form.tplCategory === 'sub'" :gutter="16">
      <a-col :span="19" :offset="1">
        <h4 class="form-header">关联信息</h4>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="subTableName" :label="'关联子表的表名'" tooltip="关联子表的表名， 如：sys_user">
          <a-select v-model="form.subTableName">
            <a-option
                v-for="(table, index) in tables"
                :key="index"
                :label="table.tableName + '：' + table.tableComment"
                :value="table.tableName"
            />
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="subTableFkName" :label="'关联子表的表名'" tooltip="子表关联的外键名， 如：user_id">
          <a-select v-model="form.subTableFkName">
            <a-option
                v-for="(column, index) in state.subColumns"
                :key="index"
                :label="column.columnName + '：' + column.columnComment"
                :value="column.columnName"
            />
          </a-select>
        </a-form-item>
      </a-col>
    </a-row>
  </a-form>
</template>

<script setup lang="ts">

import {computed, reactive, ref, watch} from "vue";
import {ColumnRow, GenTableDetailInfo} from "@/api/tool/gen";
import {listMenu} from "@/api/system/menu";

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => {
      return {}
    }
  },
  tables: {
    type: Object,
    default: () => {
      return [] as GenTableDetailInfo[]
    }
  },
})

const emits = defineEmits(['update:modelValue']);

const form = computed<GenTableDetailInfo>({
  get() { return props.modelValue as GenTableDetailInfo },
  set(val) { emits('update:modelValue', val) },
})

const state = reactive({
  subColumns: [] as ColumnRow[],
  menuOptions: [] as Array<any>,
  // 表单校验
  rules: {
    tplCategory: [
      {required: true, message: "请选择生成模板", trigger: "blur"}
    ],
    packageName: [
      {required: true, message: "请输入生成包路径", trigger: "blur"}
    ],
    moduleName: [
      {required: true, message: "请输入生成模块名", trigger: "blur"}
    ],
    businessName: [
      {required: true, message: "请输入生成业务名", trigger: "blur"}
    ],
    functionName: [
      {required: true, message: "请输入生成功能名", trigger: "blur"}
    ],
  }
})

const handleTplSelectChange = (value: string) => {
  if (value !== 'sub') {
    form.value.subTableName = '';
    form.value.subTableFkName = '';
  }
}

// 数组转树结构集合
const listToTree: any = (list: Array<any>) => {
  const map: any = {};
  const treeData: Array<any> = [];
  // 过滤掉权限字符
  list = list.filter(item => {
    return item.menuType !== 'F'
  })
  list = list.map(item => {
    return {menuId: `${item.menuId}`, parentId: `${item.parentId}`, menuName: item.menuName,}
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
  state.menuOptions = [...listToTree(data)];
};
fetchTreeSelectOption();

/** 设置关联外键 */
const setSubTableColumns = (value: string) => {
  props.tables?.forEach((item: GenTableDetailInfo) => {
    const {tableName} = item;
    if (value === tableName) {
      const {columns} = item
      state.subColumns = columns;
    }
  })
}

const genInfoForm = ref();
// 验证表单
const validateForm = () => {
  return genInfoForm.value.validate()
}

defineExpose({
  validateForm,
})

watch(() => form.value.subTableName, (value) => {
      if (value) {
        form.value.subTableFkName = ''
        setSubTableColumns(value);
      }
    },
    { immediate: true }
)

</script>

<style scoped lang="less">

.form-header {
  font-size: 15px;
  color: #6379bb;
  border-bottom: 1px solid #ddd;
  margin: 8px 10px 25px 10px;
  padding-bottom: 5px
}

</style>