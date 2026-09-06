<template>
  <div>
    <a-modal v-model:open="pageData.visible" :title="pageData.title" width="60%">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form ref="menuDetailFormRef" :labelCol="{ span: 8 }" :model="menuDetailForm.data" :wrapperCol="{ span: 14 }"
              class="bearjia-detail-page" name="menuDetailForm">
        <a-row :gutter="24">
          <!-- <a-col span="12">
            <a-form-item name="menuName" label="用户账号">
              <span>{{ menuDetailForm.data.menuName }}</span>
            </a-form-item>
          </a-col> -->
          <a-col span="12">
            <a-form-item label="菜单名称" name="menuName">
              <span>{{ menuDetailForm.data.menuName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="上级菜单" name="parentId">
              <span> {{ BearJiaUtil.getMenuNameByMenuId(menuDetailForm.data.parentId, pageData.menusDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="显示顺序" name="orderNum">
              <span>{{ menuDetailForm.data.orderNum }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="路由地址" name="path">
              <span>{{ menuDetailForm.data.path }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="组件路径" name="component">
              <span>{{ menuDetailForm.data.component }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="路由参数" name="query">
              <span>{{ menuDetailForm.data.query }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="是否为外链" name="isFrame">
              <span>{{ BearJiaUtil.getDictLabelByKey(menuDetailForm.data.isFrame, fatherPageData.sysYesNoDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="菜单类型" name="menuType">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(menuDetailForm.data.menuType, fatherPageData.menuTypeDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="是否显示" name="visible">
              <span>{{ BearJiaUtil.getDictLabelByKey(menuDetailForm.data.visible, fatherPageData.sysYesNoDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="菜单状态" name="status">
              <span>{{
                  BearJiaUtil.getDictLabelByKey(menuDetailForm.data.status, fatherPageData.sysShowHideDict)
                }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="权限标识" name="perms">
              <span>{{ menuDetailForm.data.perms }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="菜单图标" name="icon">
              <span> <BearJiaIcon :icon="menuDetailForm.data.icon"
                                  style="color: blue; padding-right: 10px"/>{{ menuDetailForm.data.icon }} </span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="备注" name="remark">
              <span>{{ menuDetailForm.data.remark }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {getMenu, listMenu as getMenuTreeselect} from '@/api/system/menu';
import {reactive, ref} from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';
import {handleTree} from '@/utils/bearjia.js';

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
const menuDetailForm = reactive({data: {}});
// 打开详细窗口
const openModal = (record) => {
  getMenu(record.menuId).then((response) => {
    menuDetailForm.data = response.data;
    // 查询菜单下拉列表
    getMenuTreeselect().then((response) => {
      pageData.menusDict = handleTree(response.data, 'menuId');
      pageData.menusDict.push({menuId: 0, menuName: '无'});
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
