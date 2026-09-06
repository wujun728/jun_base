<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable
        ref="proTable"
        row-key="menuId"
        :columns="columns"
        :request-api="listMenu"
        :request-auto="true"
        :pagination="false"
        :default-expand-all="isExpandAll"
      >
        <!-- 表格 header 按钮 -->
        <template #tableHeader>
          <el-button type="primary" :icon="CirclePlus" v-hasPermi="['system:menu:add']" @click="handleAdd">新增菜单</el-button>
          <el-button type="info" plain icon="Sort" @click="toggleExpandAll">展开/折叠</el-button>
        </template>
        <!-- 表格操作 -->
        <template #operation="scope">
          <el-button type="primary" link :icon="EditPen" v-hasPermi="['system:menu:edit']" @click="handleUpdate(scope.row)">
            编辑
          </el-button>
          <el-button type="primary" link :icon="CirclePlus" v-hasPermi="['system:menu:add']" @click="handleAdd(scope.row)">
            新增
          </el-button>
          <el-button type="primary" link :icon="Delete" v-hasPermi="['system:menu:remove']" @click="deleteAccount(scope.row)">
            删除
          </el-button>
        </template>
      </ProTable>
    </div>
    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="menuRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <el-tree-select
                v-model="form.parentId"
                :data="menuOptions"
                :props="{ value: 'menuId', label: 'menuName', children: 'children' }"
                value-key="menuId"
                placeholder="选择上级菜单"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio label="M">目录</el-radio>
                <el-radio label="C">菜单</el-radio>
                <el-radio label="F">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="form.menuType != 'F'">
            <el-form-item label="菜单图标" prop="icon">
              <SelectIcon v-model="form.icon" v-model:icon-value="form.icon"></SelectIcon>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'F'">
            <el-form-item>
              <template #label>
                <span>
                  <el-tooltip content="选择是外链则路由地址需要以`http(s)://`开头" placement="top">
                    <el-icon><question-filled /></el-icon>
                  </el-tooltip>
                  是否外链
                </span>
              </template>
              <el-radio-group v-model="form.isFrame">
                <el-radio label="0">是</el-radio>
                <el-radio label="1">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'F'">
            <el-form-item prop="path">
              <template #label>
                <span>
                  <el-tooltip content="访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头" placement="top">
                    <el-icon><question-filled /></el-icon>
                  </el-tooltip>
                  路由地址
                </span>
              </template>
              <el-input v-model="form.path" placeholder="请输入路由地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType == 'C'">
            <el-form-item prop="component">
              <template #label>
                <span>
                  <el-tooltip content="访问的组件路径，如：`system/user/index`，默认在`views`目录下" placement="top">
                    <el-icon><question-filled /></el-icon>
                  </el-tooltip>
                  组件路径
                </span>
              </template>
              <el-input v-model="form.component" placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'M'">
            <el-form-item>
              <el-input v-model="form.perms" placeholder="请输入权限标识" maxlength="100" />
              <template #label>
                <span>
                  <el-tooltip
                    content="控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasPermi('system:user:list')`)"
                    placement="top"
                  >
                    <el-icon><question-filled /></el-icon>
                  </el-tooltip>
                  权限字符
                </span>
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType == 'C'">
            <el-form-item>
              <el-input v-model="form.query" placeholder="请输入路由参数" maxlength="255" />
              <template #label>
                <span>
                  <el-tooltip content='访问路由的默认传递参数，如：`{"id": 1, "name": "ry"}`' placement="top">
                    <el-icon><question-filled /></el-icon>
                  </el-tooltip>
                  路由参数
                </span>
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType == 'C'">
            <el-form-item>
              <template #label>
                <span>
                  <el-tooltip content="选择是则会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致" placement="top">
                    <el-icon><question-filled /></el-icon>
                  </el-tooltip>
                  是否缓存
                </span>
              </template>
              <el-radio-group v-model="form.isCache">
                <el-radio label="0">缓存</el-radio>
                <el-radio label="1">不缓存</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'F'">
            <el-form-item>
              <template #label>
                <span>
                  <el-tooltip content="选择隐藏则路由将不会出现在侧边栏，但仍然可以访问" placement="top">
                    <el-icon><question-filled /></el-icon>
                  </el-tooltip>
                  显示状态
                </span>
              </template>
              <el-radio-group v-model="form.visible">
                <el-radio v-for="dict in sys_show_hide" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'F'">
            <el-form-item>
              <template #label>
                <span>
                  <el-tooltip content="选择停用则路由将不会出现在侧边栏，也不能被访问" placement="top">
                    <el-icon><question-filled /></el-icon>
                  </el-tooltip>
                  菜单状态
                </span>
              </template>
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="tsx" name="Menu">
import { CirclePlus, Delete, EditPen } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps } from "@/components/ProTable/interface";
import SvgIcon from "@/components/SvgIcon/index.vue";
import { nextTick, reactive, ref } from "vue";
import { getEnum } from "@/api/ruoyimodule";
import { addMenu, delMenu, getMenu, listMenu, updateMenu } from "@/api/ruoyimodule/system/menu";
import { useDict } from "@/hooks/useDict.js";
import SelectIcon from "@/components/SelectIcon/index.vue";
import { ElMessage } from "element-plus";
import { useHandleData } from "@/hooks/useHandleData";

const menuRef = ref();
const proTable = ref();

const { sys_show_hide, sys_normal_disable } = useDict("sys_show_hide", "sys_normal_disable");
const form = ref<any>({});
const menuOptions = ref<Menu.MenuOptions[]>([]);
const title = ref("");
const open = ref(false);
const rules = {
  menuName: [{ required: true, message: "菜单名称不能为空", trigger: "blur" }],
  orderNum: [{ required: true, message: "菜单顺序不能为空", trigger: "blur" }],
  path: [{ required: true, message: "路由地址不能为空", trigger: "blur" }]
};
// 表格配置项
const columns = reactive<ColumnProps<Menu.MenuOptions>[]>([
  { prop: "menuName", label: "菜单名称", search: { el: "input" } },
  {
    prop: "icon",
    label: "图标",
    render: scope => {
      return <SvgIcon name={scope.row.icon}></SvgIcon>;
    }
  },
  { prop: "perms", label: "权限标识" },
  { prop: "component", label: "组件路径" },
  {
    prop: "status",
    label: "状态",
    tag: true,
    enum: getEnum("sys_normal_disable"),
    search: { el: "tree-select" },
    fieldNames: { label: "dictLabel", value: "dictValue" },
    render: scope => {
      return <el-tag type={scope.row.status == 0 ? "success" : "danger"}>{scope.row.status == 0 ? "正常" : "禁用"}</el-tag>;
    }
  },
  { prop: "createTime", label: "创建时间", width: 180 },
  { prop: "operation", label: "操作", width: 300, fixed: "right" }
]);

/** 查询菜单下拉树结构 */
function getTreeselect() {
  menuOptions.value = [];
  listMenu({}).then((response: any) => {
    const menu: any = { menuId: 0, menuName: "主类目", children: [] };
    menu.children = response.rows;
    menuOptions.value.push(menu);
  });
}

// 菜单新增
const handleAdd = (row?) => {
  open.value = true;
  title.value = "添加菜单";
  getTreeselect();
  reset();
  if (row != null && row.menuId) {
    form.value.parentId = row.menuId;
  } else {
    form.value.parentId = 0;
  }
};

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 提交按钮 */
function submitForm() {
  menuRef.value.validate(valid => {
    if (valid) {
      if (form.value.menuId != undefined) {
        updateMenu(form.value).then(() => {
          ElMessage.success("修改成功");
          open.value = false;
          proTable.value?.getTableList();
        });
      } else {
        addMenu(form.value).then(() => {
          ElMessage.success("新增成功");
          open.value = false;
          proTable.value?.getTableList();
        });
      }
    }
  });
}
/** 表单重置 */
function reset() {
  form.value = {
    menuId: undefined,
    parentId: 0,
    menuName: undefined,
    icon: undefined,
    menuType: "M",
    orderNum: undefined,
    isFrame: "1",
    isCache: "0",
    visible: "0",
    status: "0"
  };
  nextTick(() => {
    menuRef.value.resetFields();
  });
}
/** 修改按钮操作 */
async function handleUpdate(row) {
  console.log(row);
  reset();
  await getTreeselect();
  getMenu(row.menuId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改菜单";
  });
}

const deleteAccount = async (params: Menu.MenuOptions) => {
  await useHandleData(delMenu, params.menuId, `删除菜单【${params.menuName}】`);
  proTable.value?.getTableList();
};

/** 展开/折叠操作 */
const isExpandAll = ref(true);

function toggleExpandAll() {
  isExpandAll.value = !isExpandAll.value;
}
</script>

<style scoped lang="scss"></style>
