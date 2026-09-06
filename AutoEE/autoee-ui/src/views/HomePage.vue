<template>
  <!-- 主页布局 -->
  <a-layout theme="light" style="height: 100%; overflow: hidden">
    <!-- 左侧边栏 -->
    <a-layout-sider theme="light" v-model:collapsed="collapsed" :trigger="null" collapsible>
      <!-- 左侧边栏的顶部header -->
      <a-layout-header style="background: #2a4cd4; padding: 0; height: 50px; line-height: 50px; text-align: center" mode="inline">
        <img v-if="collapsed == false" src="/src/assets/styles/loginPage/autoee-logo-white-noBg.png" alt="logo" style="height: 40px" />
        <img v-if="collapsed == true" src="/src/assets/styles/loginPage/autoee-logo-white-noBg-little.png" alt="logo" style="height: 40px" />
      </a-layout-header>
      <!-- 左侧边栏的菜单栏 -->
      <a-menu v-model:selectedKeys="menuState.selectedMenuKeys" :open-keys="menuState.openedFaterMenuKeys" theme="light" mode="inline" style="height: 100%; overflow: scroll; padding-top: 4px" @openChange="expandMenu">
        <a-menu-item key="workbench" @click="clickMenuItem('HomePage', '/home', '主页', 'Workbench', 'workbench', '工作台', 'workbench')">
          <template #icon>
            <AutoeeIcon icon="HomeOutlined" />
          </template>
          工作台
        </a-menu-item>
        <!-- 循环生成菜单：菜单的key使用sidebarRouters中的path，父菜单的key带“/”，子菜单的key不带“/” -->
        <a-sub-menu v-for="router in sidebarRouters" :key="router.path">
          <template #icon>
            <AutoeeIcon :icon="router.meta.icon" />
          </template>
          <template #title>{{ router.meta.title }}</template>
          <div v-for="children in router.children" :key="children.path">
            <!-- 二级父菜单 -->
            <a-sub-menu v-if="children.children" :key="children.path">
              <template #icon>
                <AutoeeIcon :icon="children.meta.icon" />
              </template>
              <template #title>{{ children.meta.title }}</template>
              <!-- 三级菜单 -->
              <a-menu-item
                v-for="threeLevelChildren in children.children"
                :key="threeLevelChildren.path"
                @click="
                  clickThreeLevelMenuItem(router.path, children.name, children.path, children.meta.title, threeLevelChildren.name, threeLevelChildren.path, threeLevelChildren.meta.title, threeLevelChildren.component)
                "
              >
                <template #icon>
                  <AutoeeIcon :icon="threeLevelChildren.meta.icon" />
                </template>
                {{ threeLevelChildren.meta.title }}
              </a-menu-item>
            </a-sub-menu>
            <!-- 二级菜单 -->
            <a-menu-item v-else :key="children.path" @click="clickMenuItem(router.name, router.path, router.meta.title, children.name, children.path, children.meta.title, children.component)">
              <template #icon>
                <AutoeeIcon :icon="children.meta.icon" />
              </template>
              {{ children.meta.title }}
            </a-menu-item>
          </div>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <!-- 右侧布局 -->
    <a-layout>
      <!-- 右侧布局的顶部header -->
      <a-layout-header style="background: #2f54eb; padding: 0; height: 50px; line-height: 50px" mode="inline">
        <a-row>
          <a-col :span="12">
            <!-- 展开、关闭菜单列表图标 -->
            <MenuUnfoldOutlined v-if="collapsed" @click="() => (collapsed = !collapsed)" style="color: rgb(253, 253, 253); padding-left: 20px" />
            <MenuFoldOutlined v-else @click="() => (collapsed = !collapsed)" style="color: rgb(253, 253, 253); padding-left: 20px" />
            <span style="color: rgb(253, 253, 253); padding-left: 20px">{{ headerInfo.currentFatherMenuTitle }}</span>
            <span style="color: rgb(253, 253, 253); padding-left: 10px">/</span>
            <span style="color: rgb(253, 253, 253); padding-left: 10px">{{ headerInfo.currentMenuTitle }}</span></a-col
          >
          <a-col :span="12" style="text-align: right; padding-right: 19px">
            <!-- 用户名称 -->
            <a-dropdown placement="bottomRight">
              <a style="color: rgb(253, 253, 253)" @click.prevent>
                {{ vueStore.getters.nickName }}&nbsp;
                <!-- <DownOutlined /> -->
                <AutoeeIcon icon="DownOutlined" />
              </a>
              <template #overlay>
                <a-menu>
                  <a-menu-item>
                    <AutoeeIcon icon="SettingOutlined" style="padding-right: 10px" />
                    <a @click="enterPersonalCenter" style="padding-right: 20px">个人中心</a>
                  </a-menu-item>
                  <a-menu-item>
                    <AutoeeIcon icon="LogoutOutlined" style="padding-right: 10px" />
                    <a @click="logout" style="padding-right: 20px">退出登录</a>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </a-col>
        </a-row>
      </a-layout-header>
      <!-- 右侧布局的主显示区 -->
      <a-layout-content style="margin: 8px 0px 8px 8px; padding: 0px; minheight: 280px; height: 100%; overflow: scroll; background: white">
        <div class="card-container" style="padding: 20px">
          <a-config-provider :locale="zhCN">
            <router-view></router-view>
          </a-config-provider>
        </div>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>
<script setup>
import { ref, computed, reactive } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useStore, mapState, mapGetters } from 'vuex';
import { UserOutlined, MenuFoldOutlined, MenuUnfoldOutlined, HomeOutlined, DownOutlined } from '@ant-design/icons-vue';
import { AutoeeIcon } from '@/utils/AutoeeIcon.js';
import AutoeeUtil from '@/utils/AutoeeUtil.js';
// 国际化：显示中文
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
dayjs.locale('zh-cn');

const vueStore = useStore();
const vueRouter = useRouter();
const vueRoute = useRoute();
const userRouters = vueRouter.getRoutes();
// console.log("vueRouter.getRoutes()=" + JSON.stringify(vueRouter.getRoutes()));
// console.log("vueRoute=" + JSON.stringify(vueRoute));

// 左侧边栏是否折叠：默认不折叠
const collapsed = ref(false);

// 菜单相关状态数据
const menuState = reactive({
  // 所有一级菜单节点
  rootSubmenuKeys: [],
  // 展开的父菜单节点
  openedFaterMenuKeys: [],
  // 当前选中的菜单项
  selectedMenuKeys: [],
});

// 通过从后端获取的用户可以访问的菜单信息生产前端菜单列表
const sidebarRouters = computed(mapGetters(['sidebarRouters']).sidebarRouters.bind({ $store: vueStore }));
// console.log('sidebarRouters=', JSON.stringify(sidebarRouters.value));
// 设置一级父菜单
sidebarRouters.value.forEach((element) => {
  // console.log(element.path);
  menuState.rootSubmenuKeys.push(element.path);
});
// 将工作台菜单添加到一级菜单节点数组中
menuState.rootSubmenuKeys.push('workbench');

const headerInfo = reactive({ currentFatherMenuTitle: '主页', currentMenuTitle: '工作台' });
// alert(JSON.stringify(vueRoute));
// 刷新页面后重新选中对应的菜单
if (vueRoute.path) {
  let arr = vueRoute.path.split('/');
  // 如果有父菜单，获取父菜单key
  if (arr.length > 2) {
    menuState.openedFaterMenuKeys = [];
    menuState.openedFaterMenuKeys.push('/' + arr[1]);
    menuState.selectedMenuKeys = [];
    menuState.selectedMenuKeys.push(arr[2]);
  } else {
    // 如果没有父菜单
    if ('/home' === vueRoute.path) {
      // 如果是主页路由，则选中工作台菜单
      menuState.selectedMenuKeys.push('workbench');
    } else {
      menuState.openedFaterMenuKeys = [];
      menuState.selectedMenuKeys = [];
      menuState.selectedMenuKeys.push(arr[1]);
    }
  }

  // 刷新页面后，重新设置顶部的菜单名称
  sidebarRouters.value.forEach((element) => {
    if (element.path === '/' + arr[1]) {
      headerInfo.currentFatherMenuTitle = element.meta.title;
      if (element.children) {
        element.children.forEach((child) => {
          if (child.path === arr[2]) {
            headerInfo.currentMenuTitle = child.meta.title;
          }
        });
      }
    }
  });
}
// console.log('vueRoute.path=' + vueRoute.path);
// console.log('menuState.openedFaterMenuKeys=' + menuState.openedFaterMenuKeys);
// console.log('menuState.selectedMenuKeys=' + menuState.selectedMenuKeys);

// 展开菜单时，收起其他已展开的菜单
const expandMenu = (openedFaterMenuKeys) => {
  const latestOpenKey = openedFaterMenuKeys.find((key) => menuState.openedFaterMenuKeys.indexOf(key) === -1);
  if (menuState.rootSubmenuKeys.indexOf(latestOpenKey) === -1) {
    menuState.openedFaterMenuKeys = openedFaterMenuKeys;
  } else {
    menuState.openedFaterMenuKeys = latestOpenKey ? [latestOpenKey] : [];
  }
};

// 点击菜单项：跳转到对应的路由
const clickMenuItem = (fatherMenuName, fatherMenuPath, fatherTitle, menuName, menuPath, menuTitle, menuComponent) => {
  if (menuPath === 'workbench') {
    //如果点击的是工作台菜单，则关闭其他已展开的父菜单
    menuState.openedFaterMenuKeys = [];
  }
  headerInfo.currentFatherMenuTitle = fatherTitle;
  headerInfo.currentMenuTitle = menuTitle;

  let routePathStr = fatherMenuPath + '/' + menuPath;
  console.log('点击菜单后请求路由=' + routePathStr);
  vueRouter.push({ path: routePathStr });
};
// 点击菜单项：跳转到对应的路由
const clickThreeLevelMenuItem = (greatFatherMenuPath, fatherMenuName, fatherMenuPath, fatherTitle, menuName, menuPath, menuTitle, menuComponent) => {
  if (menuPath === 'workbench') {
    //如果点击的是工作台菜单，则关闭其他已展开的父菜单
    menuState.openedFaterMenuKeys = [];
  }
  headerInfo.currentFatherMenuTitle = fatherTitle;
  headerInfo.currentMenuTitle = menuTitle;

  let routePathStr = greatFatherMenuPath + '/' + fatherMenuPath + '/' + menuPath;
  console.log('点击菜单后请求路由=' + routePathStr);
  vueRouter.push({ path: routePathStr });
};

// 退出登录
const logout = () => {
  vueStore
    .dispatch('LogOut_Action')
    .then(() => {
      // 登陆成功，跳转到主页
      vueRouter.push({ path: '/' }).catch(() => {});
    })
    .catch(() => {
      // this.loading = false;
      // if (this.captchaOnOff) {
      //   this.getCode();
      // }
    });
};

// 进入个人中心
const enterPersonalCenter = () => {
  AutoeeUtil.messageInfo('个人中心功能尚未实现！');
};
</script>

<style lang="less">
// 对antd的tabs和menu部分样式进行了重写
@import '/src/assets/styles/homePage/homePageStyles.less';
// 引入默认的全局样式
@import '/src/assets/styles/autoeeGlobalStyles.less';
</style>
