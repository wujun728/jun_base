<!--
 * @Author: From-wh from-wh@hotmail.com
 * @Date: 2023-03-09 15:45:51
 * @FilePath: /admin/src/layout/navMenu/subItem.vue
 * @Description:
-->
<template>
  <div>
    <template v-for="val in chil">
      <el-submenu :index="val.path" :key="val.path" v-if="val.children && val.children.length > 0">
        <template slot="title">
          <svg-icon :icon-class="val.icon" class-name="menu-icon" />
          <span>{{ val.title }}</span>
        </template>
        <sub-item :chil="val.children" />
      </el-submenu>
      <template v-else>
        <el-menu-item :index="val.path" :key="val.path" v-if="!val.isLink || (val.isLink && val.isIframe)">
          <svg-icon :icon-class="val.icon" class-name="menu-icon" />
          <span>{{ val.title }}</span>
        </el-menu-item>
        <el-menu-item :key="val.path" v-else>
          <a :href="val.isLink" target="_blank" style="display:block;padding:0 20px;">
            <svg-icon :icon-class="val.icon" class-name="menu-icon" />
            <span>{{ val.title }}</span>
          </a>
        </el-menu-item>
      </template>
    </template>
  </div>
</template>

<script>
export default {
  name: 'subItem',
  props: {
    chil: {
      type: Array,
      default() {
        return [];
      },
    },
  },
};
</script>
