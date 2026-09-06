<template>
  <div>
    <a-modal v-model:open="pageData.visible" width="60%" :title="pageData.title" :destroyOnClose="true" @ok="clickModalOk" @cancel="handleModalCancel">
      <a-form ref="roleAddUpdateFormRef" name="roleAddUpdateForm" :model="roleAddUpdateForm.data" :labelCol="{ span: 6 }" :wrapperCol="{ span: 18 }">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item name="roleName" label="角色名称" :rules="[{ required: true, message: '角色名称不能为空！' }]">
              <a-input v-model:value="roleAddUpdateForm.data.roleName" :maxlength="30" placeholder="请填写角色名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="roleKey" label="角色编码" :rules="[{ required: true, message: '角色编码不能为空！' }]">
              <a-input v-model:value="roleAddUpdateForm.data.roleKey" :maxlength="100" placeholder="请填写角色编码"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="roleSort" label="显示顺序" :rules="[{ required: true, message: '显示顺序不能为空！' }]">
              <a-input-number v-model:value="roleAddUpdateForm.data.roleSort" :min="1" :max="9999" />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="dataScope" label="数据权限" :rules="[{ required: true, message: '数据权限不能为空！' }]">
              <a-select v-model:value="roleAddUpdateForm.data.dataScope" :options="fatherPageData.dataScopeDict" placeholder="请选择数据权限" @change="onDataScopeChange"> </a-select>
            </a-form-item>
          </a-col>
          <!-- <a-col span="12">
            <a-form-item name="deptCheckStrictly" label="部门树父子关联" :rules="[{}]">
              <a-checkbox v-model:checked="roleAddUpdateForm.data.deptCheckStrictly"></a-checkbox>
            </a-form-item>
          </a-col> -->
          <a-col span="12">
            <a-form-item name="status" label="角色状态" :rules="[{ required: true, message: '角色状态不能为空！' }]">
              <a-select v-model:value="roleAddUpdateForm.data.status" :options="fatherPageData.sysNormalDisableDict" placeholder="请选择角色状态"> </a-select>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="remark" label="备注" :rules="[{}]">
              <a-input v-model:value="roleAddUpdateForm.data.remark" :maxlength="500" placeholder="请填写备注" />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="menuIds" label="菜单权限" :rules="[{ required: true, message: '菜单权限不能为空！' }]">
              <a-tree
                v-model:checkedKeys="roleAddUpdateForm.data.checkedMenuIds"
                checkable
                :checkStrictly="!roleAddUpdateForm.data.menuCheckStrictly"
                :tree-data="pageData.menusDict"
                :fieldNames="{ children: 'children', title: 'menuName', key: 'menuId' }"
                @check="onCheck"
              >
                <template #title="{ menuName, menuType }">
                  <span>
                    {{ menuName }}
                    <a-tag v-if="menuType === 'M'" color="blue" size="small" style="margin-left: 8px">目录</a-tag>
                    <a-tag v-else-if="menuType === 'C'" color="green" size="small" style="margin-left: 8px">菜单</a-tag>
                    <a-tag v-else-if="menuType === 'F'" color="orange" size="small" style="margin-left: 8px">按钮</a-tag>
                  </span>
                </template>
              </a-tree>
            </a-form-item>
          </a-col>
          
          <!-- 数据权限选择区域 - 显示在菜单权限右边 -->
          <a-col v-if="roleAddUpdateForm.data.dataScope === '2'" span="12">
            <a-form-item label="数据权限">
              <div class="data-scope-controls">
                <a-space size="small" wrap>
                  <a-button 
                    size="small" 
                    :type="pageData.expandAll ? 'primary' : 'default'"
                    @click="toggleExpandAll"
                  >
                    展开/折叠
                  </a-button>
                  <a-button 
                    size="small" 
                    :type="pageData.selectAll ? 'primary' : 'default'"
                    @click="toggleSelectAll"
                  >
                    全选/全不选
                  </a-button>
                  <a-button 
                    size="small" 
                    :type="pageData.checkStrictly ? 'primary' : 'default'"
                    @click="toggleCheckStrictly"
                  >
                    父子联动
                  </a-button>
                </a-space>
              </div>
              <a-tree
                v-model:checkedKeys="roleAddUpdateForm.data.checkedDeptIds"
                v-model:expandedKeys="pageData.expandedKeys"
                :checkStrictly="!pageData.checkStrictly"
                :tree-data="pageData.deptTreeData"
                :fieldNames="{ children: 'children', title: 'deptName', key: 'deptId' }"
                checkable
                @check="onDeptCheck"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup>
import { reactive, ref, watch } from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';
import { listMenu as getMenuTreeselect } from '@/api/system/menu';
import { handleTree } from '@/utils/bearjia.js';
import { getRole, addRole, updateRole, dataScope } from '@/api/system/role';
import { roleMenuTreeselect } from '@/api/system/menu';
import { listDept } from '@/api/system/dept';

// 父页面公用数据
const fatherPageData = defineProps({
  dataScopeDict: Array,
  menuCheckStrictlyDict: Array,
  sysNormalDisableDict: Array,
});

// 引入父页面方法
const pageEmit = defineEmits(['refreshFatherPageTable']);

// 当前页面使用的数据
const pageData = reactive({
  title: '新增页面',
  visible: false,
  operateType: '',
  menusDict: [],
  deptTreeData: [],
  expandAll: false,
  selectAll: false,
  checkStrictly: true,
  expandedKeys: [],
});

// 新增修改Form
const roleAddUpdateFormRef = ref();
const roleAddUpdateForm = reactive({ 
  data: { 
    checkedMenuIds: [], 
    menuIds: [], 
    deptIds: [], 
    checkedDeptIds: [],
    menuCheckStrictly: true 
  } 
});

// 监听选中菜单的变化
watch(
  () => roleAddUpdateForm.data.checkedMenuIds,
  (newCheckedIds) => {
    if (Array.isArray(newCheckedIds)) {
      roleAddUpdateForm.data.menuIds = newCheckedIds.map((id) => parseInt(id));
      console.log('Watch监听到菜单选中变化:', roleAddUpdateForm.data.menuIds);
    }
  },
  { deep: true },
);

// 监听数据权限范围变化
watch(
  () => roleAddUpdateForm.data.dataScope,
  (newScope) => {
    if (newScope !== '2') {
      // 如果不是自定义数据权限，清空选中的部门
      roleAddUpdateForm.data.checkedDeptIds = [];
      roleAddUpdateForm.data.deptIds = [];
    }
  }
);

// 监听选中部门的变化
watch(
  () => roleAddUpdateForm.data.checkedDeptIds,
  (newCheckedIds) => {
    if (Array.isArray(newCheckedIds)) {
      roleAddUpdateForm.data.deptIds = newCheckedIds.map((id) => parseInt(id));
      console.log('监听到部门选中变化:', roleAddUpdateForm.data.deptIds);
    }
  },
  { deep: true }
);
// 重置Form
const resetRoleAddUpdateForm = () => {
  BearJiaUtil.resetFormFieldsToEmpty(roleAddUpdateForm.data);
};

// 打开新增窗口
const openAddModal = () => {
  pageData.operateType = 'add';
  pageData.title = '新增角色';
  // 查询菜单下拉列表
  getMenuTreeselect().then((response) => {
    pageData.menusDict = handleTree(response.data, 'menuId');
    // 查询部门树数据
    listDept().then((deptResponse) => {
      pageData.deptTreeData = handleTree(deptResponse.data, 'deptId');
      // 默认展开第一级节点
      const getFirstLevelKeys = (depts) => {
        return depts.map(dept => dept.deptId);
      };
      pageData.expandedKeys = getFirstLevelKeys(pageData.deptTreeData);
      pageData.visible = true;
    });
  });
};

// 打开修改窗口
const openUpdateModal = (record, tablePage) => {
  pageData.operateType = 'update';
  pageData.title = '修改角色';
  getRole(record.roleId).then((response) => {
    roleAddUpdateForm.data = response.data;
    // 查询菜单下拉列表
    getMenuTreeselect().then((response) => {
      pageData.menusDict = handleTree(response.data, 'menuId');
      // 查询部门树数据
      listDept().then((deptResponse) => {
        pageData.deptTreeData = handleTree(deptResponse.data, 'deptId');
        // 默认展开第一级节点
        const getFirstLevelKeys = (depts) => {
          return depts.map(dept => dept.deptId);
        };
        pageData.expandedKeys = getFirstLevelKeys(pageData.deptTreeData);
        
        roleMenuTreeselect(record.roleId).then((response) => {
          // 处理菜单数据，将id和label转换为menuId和menuName
          const convertMenuData = (menus) => {
            return menus.map((menu) => ({
              menuId: menu.id,
              menuName: menu.label,
              children: menu.children ? convertMenuData(menu.children) : [],
            }));
          };

          // 如果response.data包含menus，使用它；否则使用之前获取的菜单数据
          if (response.data && response.data.menus) {
            pageData.menusDict = convertMenuData(response.data.menus);
          }

          // 确保menuIds是数字数组
          const checkedKeys = response.data ? response.data.checkedKeys : response.checkedKeys;
          roleAddUpdateForm.data.menuIds = (checkedKeys || []).map((id) => parseInt(id));

          // 直接设置选中的菜单ID，不需要复杂的叶子节点处理
          roleAddUpdateForm.data.checkedMenuIds = (checkedKeys || []).map((id) => parseInt(id));

          // 如果数据权限是自定义，设置已分配的部门
          if (roleAddUpdateForm.data.dataScope === '2' && roleAddUpdateForm.data.deptIds) {
            roleAddUpdateForm.data.checkedDeptIds = roleAddUpdateForm.data.deptIds.map(id => parseInt(id));
          }

          pageData.visible = true;
        });
      });
    });
  });
};

// 点击窗口确认
const clickModalOk = (e) => {
  roleAddUpdateFormRef.value
    .validateFields()
    .then((values) => {
      // 确保menuIds是正确的数组格式
      const submitData = {
        ...roleAddUpdateForm.data,
        menuIds: (roleAddUpdateForm.data.menuIds || []).map((id) => parseInt(id)),
      };

      console.log('提交的数据:', submitData);

      if (pageData.operateType === 'add') {
        addRole(submitData).then((res) => {
          submitData.deptIds = [];
          dataScope(submitData).then((response) => {
            pageData.visible = false;
            resetRoleAddUpdateForm();
            // 调用父页面刷新方法
            pageEmit('refreshFatherPageTable');
            BearJiaUtil.messageSuccess('新增操作成功。');
          });
        });
      } else if (pageData.operateType === 'update') {
        updateRole(submitData).then((res) => {
          submitData.deptIds = [];
          dataScope(submitData).then((response) => {
            pageData.visible = false;
            resetRoleAddUpdateForm();
            // 调用父页面刷新方法
            pageEmit('refreshFatherPageTable');
            BearJiaUtil.messageSuccess('修改操作成功。');
          });
        });
      }
    })
    .catch((info) => {
      console.log('Validate Failed:', info);
    });
};

// 数据权限范围变化处理
const onDataScopeChange = (value) => {
  console.log('数据权限范围变化:', value);
  if (value !== '2') {
    // 如果不是自定义数据权限，清空选中的部门
    roleAddUpdateForm.data.checkedDeptIds = [];
    roleAddUpdateForm.data.deptIds = [];
  }
};

// 展开/折叠控制
const toggleExpandAll = () => {
  pageData.expandAll = !pageData.expandAll;
  console.log('展开/折叠:', pageData.expandAll);
  
  if (pageData.expandAll) {
    // 展开所有节点
    const getAllKeys = (depts) => {
      let keys = [];
      depts.forEach(dept => {
        keys.push(dept.deptId);
        if (dept.children && dept.children.length > 0) {
          keys = keys.concat(getAllKeys(dept.children));
        }
      });
      return keys;
    };
    pageData.expandedKeys = getAllKeys(pageData.deptTreeData);
  } else {
    // 折叠所有节点
    pageData.expandedKeys = [];
  }
};

// 全选/全不选控制
const toggleSelectAll = () => {
  pageData.selectAll = !pageData.selectAll;
  if (pageData.selectAll) {
    // 获取所有部门ID
    const getAllDeptIds = (depts) => {
      let ids = [];
      depts.forEach(dept => {
        ids.push(dept.deptId);
        if (dept.children && dept.children.length > 0) {
          ids = ids.concat(getAllDeptIds(dept.children));
        }
      });
      return ids;
    };
    const allIds = getAllDeptIds(pageData.deptTreeData);
    roleAddUpdateForm.data.checkedDeptIds = allIds;
    roleAddUpdateForm.data.deptIds = allIds;
    console.log('全选部门:', allIds);
  } else {
    roleAddUpdateForm.data.checkedDeptIds = [];
    roleAddUpdateForm.data.deptIds = [];
    console.log('取消全选');
  }
};

// 父子联动控制
const toggleCheckStrictly = () => {
  pageData.checkStrictly = !pageData.checkStrictly;
};

// 部门选择变化
const onDeptCheck = (checkedKeys, e) => {
  console.log('部门选择变化:', checkedKeys);
  roleAddUpdateForm.data.checkedDeptIds = checkedKeys;
};

// 树形控件，点击子节点时，将父节点已放入选中节点数组中
const onCheck = (checkedKeys, e) => {
  console.log('onCheck 接收到的 checkedKeys:', checkedKeys, '类型:', typeof checkedKeys);

  // 处理不同类型的 checkedKeys
  let selectedKeys = [];

  if (Array.isArray(checkedKeys)) {
    // 如果是数组，直接使用
    selectedKeys = checkedKeys;
  } else if (checkedKeys && typeof checkedKeys === 'object') {
    // 如果是对象，可能包含 checked 属性
    if (checkedKeys.checked && Array.isArray(checkedKeys.checked)) {
      selectedKeys = checkedKeys.checked;
    } else if (checkedKeys.checkedNodes && Array.isArray(checkedKeys.checkedNodes)) {
      selectedKeys = checkedKeys.checkedNodes.map((node) => node.key || node.menuId);
    } else {
      // 尝试获取对象的值
      selectedKeys = Object.values(checkedKeys).find((val) => Array.isArray(val)) || [];
    }
  } else {
    // 其他情况，设为空数组
    selectedKeys = [];
  }

  // 确保menuIds是数字数组
  roleAddUpdateForm.data.menuIds = selectedKeys.map((id) => parseInt(id));
  console.log('处理后的菜单IDs:', roleAddUpdateForm.data.menuIds);
};

// 菜单树反显时，父节点下的子节点没有全部选中时，保证父节点为半选中状态，而不能是全选中状态，否则导致父节点下的子节点全部被选中
let allMenuLeafNodes = [];
const initMenuCheckedKeys = (menusDict, resCheckedKeys) => {
  // 1.循环遍历出最深层子节点，存放在一个数组中
  allMenuLeafNodes = [];
  getAllMenuLeafNodes(menusDict);
  let allResCheckedMenuLeafNodes = getAllResCheckedMenuLeafNodes(allMenuLeafNodes, resCheckedKeys);
  roleAddUpdateForm.data.checkedMenuIds = allResCheckedMenuLeafNodes;
};

//  循环遍历出最深层叶子节点，存放在一个数组中
const getAllMenuLeafNodes = (data) => {
  data &&
    data.map((item) => {
      if (item.children && item.children.length > 0) {
        getAllMenuLeafNodes(item.children);
      } else {
        allMenuLeafNodes.push(item.menuId);
      }
      return null;
    });
  return allMenuLeafNodes;
};

//  将后台返回的含有父节点的数组和第一步骤遍历的数组做比较,如果有相同值，将相同值取出来，push到allResCheckedMenuLeafNodes数组中
const getAllResCheckedMenuLeafNodes = (allMenuLeafNodes, resCheckedKeys) => {
  let allResCheckedMenuLeafNodes = [];
  for (var i in resCheckedKeys) {
    for (var k in allMenuLeafNodes) {
      if (allMenuLeafNodes[k] === resCheckedKeys[i]) {
        allResCheckedMenuLeafNodes.push(allMenuLeafNodes[k]);
      }
    }
  }
  return allResCheckedMenuLeafNodes;
};

// 点击窗口取消
const handleModalCancel = () => {
  resetRoleAddUpdateForm();
};

// 对外暴露出去
defineExpose({
  openAddModal,
  openUpdateModal,
});
</script>

<style lang="less">
.data-scope-controls {
  margin-bottom: 12px;
}

.data-scope-controls .ant-space {
  width: 100%;
}

.data-scope-controls .ant-btn {
  font-size: 12px;
  height: 24px;
  padding: 0 8px;
  border-radius: 4px;
}

.data-scope-controls .ant-btn-sm {
  font-size: 11px;
  height: 22px;
  padding: 0 6px;
}
</style>
