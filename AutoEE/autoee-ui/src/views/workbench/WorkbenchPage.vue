<template>
  <a-button type="primary" block style="text-align: left">AutoEE-快速开发平台：前端框架</a-button>
  <p />
  <br />
  <a-card title="平台简介">
    <template #extra> </template>
    <p>基于 vue3.x + CompositionAPI + vite + Ant Design of Vue 实现的管理平台前端框架。</p>
    <p>使用各组件的最新版本，融合新技术，代码简单易懂，组件拆分清晰，不做过度封装，符合常规开发模式，自动代码生成，修改简单高效，运行速度快。</p>
    <p>界面简洁美观，使用Ant Design of Vue最新3.x版本，原汁原味，清晰明了，操作方便。</p>
    <p>后端直接对接RuoYi-Vue最新版本。访问RuoYi-Vue：https://gitee.com/y_project/RuoYi-Vue</p>
  </a-card>
  <br />
  <a-card title="已完成功能">
    <template #extra> </template>
    <a-descriptions bordered style="width: 300px">
      <a-descriptions-item label="系统管理">
        用户管理
        <br />
        角色管理
        <br />
        菜单管理
        <br />
        部门管理
        <br />
        岗位管理
        <br />
        字典管理
        <br />
        参数设置
        <br />
        日志管理
        <br />
        - 操作日志
        <br />
        - 登录日志
      </a-descriptions-item>
    </a-descriptions>
    <a-descriptions bordered style="width: 300px">
      <a-descriptions-item label="系统工具">
        代码生成
        <br />
      </a-descriptions-item>
    </a-descriptions>
  </a-card>
  <br />
  <a-card title="如果AutoEE对您有帮助，请我喝杯咖啡吧！">
    <p><img src="/src/assets/styles/loginPage/admireQRcode.jpg" class="logo" alt="logo" style="width: 200px" /></p>
  </a-card>

  <br />
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router';
const vueRouter = useRouter();
const data = vueRouter.getRoutes();
// const data = JSON.stringify(vueRouter.getRoutes());
</script>

<style lang="less"></style>
