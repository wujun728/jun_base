<template>
  <ErrorPage
    status-code="404"
    label="页面未找到"
    title="抱歉，访问的页面不存在"
    description="页面地址可能已变更、被移除，或者当前账号尚未加载对应路由"
    variant="missing"
  >
    <template #actions>
      <el-button type="primary" :icon="House" @click="goHome">返回首页</el-button>
      <el-button :icon="Refresh" @click="reload">重新加载</el-button>
    </template>
  </ErrorPage>
</template>

<script setup>
import { House, Refresh } from "@element-plus/icons-vue";
import ErrorPage from "./components/ErrorPage.vue";

defineOptions({ name: "Page404" });

const router = useRouter();

function goHome() {
  router.push("/");
}

function reload() {
  window.location.reload();
}
</script>
