<template>
  <div>
    <a-modal v-model:visible="pageData.visible" width="60%" :title="pageData.title">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form class="autoee-detail-page" ref="menuDetailFormRef" name="menuDetailForm" :model="menuDetailForm.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
        <a-row :gutter="24">
          <!-- <a-col span="12">
            <a-form-item name="menuName" label="用户账号">
              <span>{{ menuDetailForm.data.menuName }}</span>
            </a-form-item>
          </a-col> -->
          <a-col span="12">
            <a-form-item name="menuName" label="菜单名称">
              <span>{{ menuDetailForm.data.menuName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="parentId" label="上级菜单">
              <span> {{ AutoeeUtil.getMenuNameByMenuId(menuDetailForm.data.parentId, pageData.menusDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="orderNum" label="显示顺序">
              <span>{{ menuDetailForm.data.orderNum }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="path" label="路由地址">
              <span>{{ menuDetailForm.data.path }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="component" label="组件路径">
              <span>{{ menuDetailForm.data.component }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="query" label="路由参数">
              <span>{{ menuDetailForm.data.query }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="isFrame" label="是否为外链">
              <span>{{ AutoeeUtil.getDictLabelByKey(menuDetailForm.data.isFrame, fatherPageData.sysYesNoDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="menuType" label="菜单类型">
              <span>{{ AutoeeUtil.getDictLabelByKey(menuDetailForm.data.menuType, fatherPageData.menuTypeDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="visible" label="是否显示">
              <span>{{ AutoeeUtil.getDictLabelByKey(menuDetailForm.data.visible, fatherPageData.sysYesNoDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="status" label="菜单状态">
              <span>{{ AutoeeUtil.getDictLabelByKey(menuDetailForm.data.status, fatherPageData.sysShowHideDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="perms" label="权限标识">
              <span>{{ menuDetailForm.data.perms }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="icon" label="菜单图标">
              <span> <AutoeeIcon :icon="menuDetailForm.data.icon" style="color: blue; padding-right: 10px" />{{ menuDetailForm.data.icon }} </span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="remark" label="备注">
              <span>{{ menuDetailForm.data.remark }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { listMenu as getMenuTreeselect, getMenu } from '@/api/system/menu';
import { reactive, ref } from 'vue';
import AutoeeUtil from '@/utils/AutoeeUtil.js';
import { AutoeeIcon } from '@/utils/AutoeeIcon.js';
import { handleTree } from '@/utils/ruoyi.js';

// 父页面公用数据
const fatherPageData = defineProps({
  sysYesNoDict: Array,
  sysShowHideDict: Array,
  menuTypeDict: Array,
});

// 当前页面使用的数据
const pageData = reactive({
  title: '详细页面',
  visible: false,
  operateType: '',
  menusDict: [],
});

//详细Form
const menuDetailFormRef = ref();
const menuDetailForm = reactive({ data: {} });
// 打开详细窗口
const openModal = (record) => {
  getMenu(record.menuId).then((response) => {
    menuDetailForm.data = response.data;
    // 查询菜单下拉列表
    getMenuTreeselect().then((response) => {
      pageData.menusDict = handleTree(response.data, 'menuId');
      pageData.menusDict.push({ menuId: 0, menuName: '无' });
      pageData.visible = true;
    });
  });
};

// 对外暴露出去
defineExpose({
  openModal,
});
</script>

<style lang="less"></style>
