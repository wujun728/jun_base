<template>
  <div>
    <ArtCrud
      ref="crudRef"
      :config="crudConfig"
      v-model:search="queryParams"
      v-model:edit-form="form"
      @add-row="handleAdd"
      @edit-row="handleEdit">
      <template #toolbar>
        <el-button
          type="warning"
          plain
          :icon="Check"
          @click="handleSaveSort"
          v-auth="'system:menu:edit'">
          保存排序
        </el-button>
        <el-button type="info" plain :icon="Sort" @click="toggleExpandAll">
          展开/折叠
        </el-button>
      </template>
      <!-- -------------列表插槽-------------- -->
      <!-- 菜单名称 -->
      <template #col-menuName="{ row }">
        <ArtSvgIcon
          v-if="row.icon"
          :icon="`ri:${row.icon}`"
          class="align-middle text-base" />
        <span class="ml5 align-middle">{{ row.menuName }}</span>
      </template>

      <!-- 菜单类型 -->
      <template #col-menuType="{ row }">
        <el-tag
          v-if="row.menuType === 'M' && row.isFrame === '0'"
          type="danger"
          size="small">
          外链
        </el-tag>
        <el-tag v-else-if="row.menuType === 'M'" type="primary" size="small">
          目录
        </el-tag>
        <el-tag
          v-else-if="row.menuType === 'C' && row.isFrame === '0'"
          type="danger"
          size="small">
          外链
        </el-tag>
        <el-tag v-else-if="row.menuType === 'C'" type="success" size="small">
          菜单
        </el-tag>
        <el-tag v-else-if="row.menuType === 'F'" type="warning" size="small">
          按钮
        </el-tag>
      </template>
      <!-- 排序 -->
      <template #col-orderNum="{ row }">
        <el-input-number
          v-model="row.orderNum"
          controls-position="right"
          :min="0"
          style="width: 88px" />
      </template>
      <!-- 状态 -->
      <template #col-status="{ row }">
        <dict-tag :options="sys_normal_disable" :value="row.status" />
      </template>
      <!-- 操作 -->
      <template #col-operationTemplate="{ row }">
        <ArtButtonTable
          v-if="hasAuth('system:menu:add')"
          type="add"
          @click="handleAdd(row)"
          tooltip="新增">
          新增
        </ArtButtonTable>
      </template>

      <!-- -------------表单插槽-------------- -->
      <template #form-icon="{ modelValue }">
        <ArtIconPicker v-model="modelValue.icon" />
      </template>
    </ArtCrud>
  </div>
</template>

<script setup lang="ts" name="Menu">
import type { ArtCrudConfig } from '@/components/business/art-crud/index.vue'
import type { TreeSelect } from '@/types/api/system/common'
import type { MenuQueryParams, SysMenu } from '@/types/api/system/menu'

import { Check, Sort } from '@element-plus/icons-vue'

import ArtCrud from '@/components/business/art-crud/index.vue'
import { useAuth } from '@/hooks'
import { useDict } from '@/hooks/core/useDict'
import { handleTree } from '@/utils/sys/ruoyi'

import {
  addMenu,
  delMenu,
  getMenu,
  listMenu,
  updateMenu,
  updateMenuSort
} from './api'

const { hasAuth } = useAuth()
const { sys_show_hide, sys_normal_disable } = useDict(
  'sys_show_hide',
  'sys_normal_disable'
)

const crudRef = useTemplateRef<InstanceType<typeof ArtCrud>>('crudRef')

const expandRowKeys = ref<string[]>([])
const originalOrders = ref<Record<number, number>>({})
const menuList = ref<SysMenu[]>([])
const menuOptions = ref<TreeSelect[]>([])

const menuTypeOptions = [
  { value: 'M', label: '目录' },
  { value: 'C', label: '菜单' },
  { value: 'F', label: '按钮' }
]

const queryParams = ref<MenuQueryParams>({
  menuName: '',
  status: ''
})

const form = ref<SysMenu>({
  menuId: undefined,
  parentId: 0,
  menuName: undefined,
  icon: undefined,
  menuType: 'M',
  orderNum: undefined,
  isFrame: '1',
  isCache: '0',
  visible: '0',
  status: '0'
})

/** 新增按钮操作 */
function handleAdd(row?: SysMenu) {
  // reset()
  getTreeselect()
  if (row != null && row.menuId) {
    form.value.parentId = row.menuId
    crudRef.value?.openAdd()
  } else {
    form.value.parentId = 0
  }
}

/** 编辑按钮操作 */
async function handleEdit(row: SysMenu) {
  getTreeselect()
  const response = await getMenu(row.menuId!)
  form.value = response!
}

/** 查询菜单下拉树结构 */
async function getTreeselect() {
  menuOptions.value = []
  const res = await listMenu()
  const menu = {
    menuId: 0,
    menuName: '主类目',
    children: [] as any[],
    disabled: false
  }
  menu.children = handleTree(res, 'menuId')
  menuOptions.value.push(menu)
}

/** 展开/折叠操作 */
function toggleExpandAll() {
  expandRowKeys.value = expandRowKeys.value.length ? [] : expandAll()
}
function expandAll(): string[] {
  const keys: string[] = []

  const dfs = (list: SysMenu[]) => {
    list.forEach((item) => {
      keys.push(String(item.menuId!))
      if (item.children?.length) {
        dfs(item.children)
      }
    })
  }
  dfs(menuList.value)
  return keys
}

/** 保存排序 */
function handleSaveSort() {
  const changedMenuIds: number[] = []
  const changedOrderNums: number[] = []
  const menuList = crudRef.value?.data || []
  const collectChanged = (list: SysMenu[]) => {
    list.forEach((item) => {
      if (
        String(originalOrders.value[item.menuId!]) !== String(item.orderNum)
      ) {
        changedMenuIds.push(item.menuId!)
        changedOrderNums.push(item.orderNum!)
      }
      if (item.children && item.children.length) {
        collectChanged(item.children)
      }
    })
  }
  collectChanged(menuList)
  if (changedMenuIds.length === 0) {
    ElMessage.warning('未检测到排序修改')
    return
  }
  updateMenuSort({
    menuIds: changedMenuIds.join(','),
    orderNums: changedOrderNums.join(',')
  }).then(() => {
    ElMessage.success('排序保存成功')
    recordOriginalOrders(menuList)
    refreshTable()
  })
}

/** 递归记录原始排序 */
function recordOriginalOrders(list: SysMenu[]) {
  list.forEach((item) => {
    originalOrders.value[item.menuId!] = item.orderNum!
    if (item.children && item.children.length) {
      recordOriginalOrders(item.children)
    }
  })
}

/** 刷新表格 */
function refreshTable() {
  if (crudRef.value) {
    crudRef.value.handleRefresh()
  }
}

const crudConfig: any = computed<ArtCrudConfig<SysMenu>>(() => ({
  crudConfig: {
    showPagination: false,
    rowKey: 'menuId',
    expandRowKeys: expandRowKeys.value,
    treeProps: { children: 'children', hasChildren: 'hasChildren' }
  },
  api: {
    list: async (params) => {
      const res = await listMenu(queryParams.value)
      console.log(res, 's')
      menuList.value = handleTree(res, 'menuId')
      recordOriginalOrders(menuList.value)
      return menuList.value
    },
    update: updateMenu,
    add: addMenu,
    delete: delMenu
  },
  searchConfig: {
    items: [
      {
        label: '菜单名称',
        key: 'menuName',
        type: 'input',
        placeholder: '请输入菜单名称'
      },
      {
        label: '状态',
        key: 'status',
        type: 'select',
        placeholder: '请选择状态',
        options: sys_normal_disable
      }
    ]
  },
  columns: [
    {
      label: '菜单名称',
      prop: 'menuName',
      width: 200,
      useSlot: true,
      slotName: 'menuName'
    },
    {
      label: '菜单类型',
      prop: 'menuType',
      width: 100,
      useSlot: true,
      slotName: 'menuType'
    },
    {
      label: '排序',
      prop: 'orderNum',
      width: 120,
      useSlot: true,
      slotName: 'orderNum'
    },
    {
      label: '权限标识',
      width: 200,
      prop: 'perms'
    },
    {
      label: '组件路径',
      width: 200,
      prop: 'component'
    },
    {
      label: '状态',
      prop: 'status',
      useSlot: true,
      slotName: 'status'
    },
    {
      label: '操作',
      prop: 'operation',
      width: 180,
      useSlot: true,
      slotName: 'operationTemplate'
    }
  ],
  editConfig: {
    labelWidth: 100,
    items: [
      {
        label: '上级菜单',
        key: 'parentId',
        type: 'treeselect',
        span: 24,
        props: {
          data: menuOptions.value,
          props: { value: 'menuId', label: 'menuName', children: 'children' },
          valueKey: 'menuId',
          placeholder: '选择上级菜单',
          checkStrictly: true
        }
      },
      {
        label: '菜单类型',
        key: 'menuType',
        type: 'radiogroup',
        span: 24,
        props: {
          options: menuTypeOptions
        }
      },
      {
        label: '菜单图标',
        key: 'icon',
        type: 'input',
        span: 12,
        hidden: form.value.menuType === 'F',
        useSlot: true,
        slotName: 'icon'
      },
      {
        label: '显示排序',
        key: 'orderNum',
        type: 'number',
        span: 12,
        props: { controlsPosition: 'right', min: 0 }
      },
      {
        label: '菜单名称',
        key: 'menuName',
        type: 'input',
        span: 12,
        props: { placeholder: '请输入菜单名称' }
      },
      {
        label: '路由名称',
        tooltip:
          '默认不填则和路由地址相同：如地址为：`user`，则名称为`User`（注意：因为router会删除名称相同路由，为避免名字的冲突，特殊情况下请自定义，保证唯一性）',
        key: 'routeName',
        type: 'input',
        span: 12,
        hidden: form.value.menuType !== 'C',
        props: { placeholder: '请输入路由名称' }
      },
      {
        label: '是否外链',
        tooltip: '选择是外链则路由地址需要以`http(s)://`开头',
        key: 'isFrame',
        type: 'radiogroup',
        span: 12,
        hidden: form.value.menuType === 'F',
        props: {
          options: [
            { value: '0', label: '是' },
            { value: '1', label: '否' }
          ]
        }
      },
      {
        label: '路由地址',
        tooltip:
          '访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头',
        key: 'path',
        type: 'input',
        span: 12,
        hidden: form.value.menuType === 'F',
        props: { placeholder: '请输入路由地址' }
      },
      {
        label: '组件路径',
        tooltip: '访问的组件路径，如：`system/user/index`，默认在`views`目录下',
        key: 'component',
        type: 'input',
        span: 12,
        hidden: form.value.menuType !== 'C',
        props: { placeholder: '请输入组件路径' }
      },
      {
        label: '权限字符',
        tooltip:
          "控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasPermi('system:user:list')`)",
        key: 'perms',
        type: 'input',
        span: 12,
        hidden: form.value.menuType === 'M',
        props: { placeholder: '请输入权限标识', maxlength: 100 }
      },
      {
        label: '路由参数',
        tooltip: '访问路由的默认传递参数，如：`{"id": 1, "name": "ry"}`',
        key: 'query',
        type: 'input',
        span: 12,
        hidden: form.value.menuType !== 'C',
        props: { placeholder: '请输入路由参数', maxlength: 255 }
      },
      {
        label: '是否缓存',
        tooltip:
          '选择是则会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致',
        key: 'isCache',
        type: 'radiogroup',
        span: 12,
        hidden: form.value.menuType !== 'C',
        props: {
          options: [
            { value: '0', label: '缓存' },
            { value: '1', label: '不缓存' }
          ]
        }
      },
      {
        label: '显示状态',
        tooltip: '选择隐藏则路由将不会出现在侧边栏，但仍然可以访问',
        key: 'visible',
        type: 'radiogroup',
        span: 12,
        hidden: form.value.menuType === 'F',
        props: {
          options: sys_show_hide.value
        }
      },
      {
        label: '菜单状态',
        tooltip: '选择停用则路由将不会出现在侧边栏，也不能被访问',
        key: 'status',
        type: 'radiogroup',
        span: 12,
        props: {
          options: sys_normal_disable.value
        }
      }
    ],
    rules: {
      menuName: [
        { required: true, message: '菜单名称不能为空', trigger: 'blur' }
      ],
      orderNum: [
        { required: true, message: '菜单顺序不能为空', trigger: 'blur' }
      ],
      path: [{ required: true, message: '路由地址不能为空', trigger: 'blur' }]
    }
  },

  btnConfig: {
    showAddOperation: () => hasAuth('system:menu:add'),
    showEditOperation: () => hasAuth('system:menu:edit'),
    showDeleteOperation: () => hasAuth('system:menu:delete')
  }
}))
</script>
