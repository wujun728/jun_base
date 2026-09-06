<template>
  <div class="top-nav">
    <el-menu :default-active="activeMenu" :background-color="settings.sideTheme === 'theme-dark'
        ? variables.menuBackground
        : variables.menuLightBackground
      " :text-color="settings.sideTheme === 'theme-dark'
          ? variables.menuColor
          : variables.menuLightColor
        " :unique-opened="true" :active-text-color="settings.theme" mode="horizontal">
      <div>
        <img style="width: 100%; height: 50px" src="@/assets/logo/logo.png" alt="" />
      </div>
      <menu-item v-for="(route, index) in sidebarRouters" :key="route.path + index" :item="route"
        :base-path="route.path" />
    </el-menu>
    <div class="right-menu">
      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar" />
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>
<script>
import { mapGetters, mapState } from "vuex";

import Logo from "./Logo";
import MenuItem from "./MenuItem";
import variables from "@/assets/styles/variables.scss";
import Screenfull from "@/components/Screenfull";
export default {
  components: { MenuItem, Logo, Screenfull },

  data() {
    return {

    };
  },
  created() {
  },
  computed: {
    ...mapState(["settings"]),
    ...mapGetters(["sidebarRouters", "sidebar", "avatar", "device"]),
    activeMenu() {
      const route = this.$route;
      const { meta, path } = route;
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {

        return meta.activeMenu;
      }
      return path;
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo;
    },
    variables() {
      return variables;
    },
  },
  methods: {
    search() { },

    handleChange(val) {
      console.log(val);
    },
    expandSearch() {
      this.isExpanded = true;
    },
    collapseSearch() {
      if (this.searchText === "") {
        this.isExpanded = false;
        this.searchText = "";
      }
    },

    async logout() {
      this.$confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$store.dispatch("LogOut").then(() => {
            location.href = "/index";
          });
        })
        .catch(() => { });
    },
  },
};
</script>
<style lang="scss" scoped>
.top-nav {
  --nav-bg: rgba(48, 65, 86, 0.96);
  --avatar-hover-bg: rgba(255, 255, 255, 0.08);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
  background: var(--nav-bg);
  backdrop-filter: blur(8px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0 150px;
}

::v-deep.el-menu--horizontal {
  display: flex;
  height: 100%;
  border: none;
  background: transparent !important;
}

.right-menu {
  display: flex;
  justify-content: flex-end;

  .avatar-container {
    .avatar-wrapper {
      display: flex;
      align-items: center;
      padding: 4px 12px;
      border-radius: 24px;
      transition: background 0.3s;

      &:hover {
        background: var(--avatar-hover-bg);
      }

      .user-avatar {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        margin-right: 8px;
      }

      .el-icon-caret-bottom {
        margin-left: 6px;
        font-size: 12px;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
}
</style>