<template>
  <div v-if="!appStore.navbar" class="fixed-settings" @click="setVisible">
    <a-button type="primary">
      <template #icon>
        <icon-settings/>
      </template>
    </a-button>
  </div>
  <a-drawer
      :width="300"
      unmount-on-close
      :visible="visible"
      :cancel-text="'关闭'"
      :ok-text="'复制配置'"
      @ok="copySettings"
      @cancel="cancel"
  >
    <template #title> {{ '页面配置' }}</template>
    <Block :options="contentOpts" :title="'内容区域'"/>
    <Block :options="othersOpts" :title="'其他设置'"/>
    <a-alert>{{ '配置之后仅是临时生效，要想真正作用于项目，点击下方的 "复制配置" 按钮，将配置替换到 settings.json 中即可。' }}</a-alert>
  </a-drawer>
</template>

<script lang="ts" setup>
import {computed} from 'vue';
import {Message} from '@arco-design/web-vue';
import {useClipboard} from '@vueuse/core';
import {useAppStore} from '@/store';
import Block from './block.vue';

const emit = defineEmits(['cancel']);

const appStore = useAppStore();
const {copy} = useClipboard();
const visible = computed(() => appStore.globalSettings);
const contentOpts = computed(() => [
  {name: '导航栏', key: 'navbar', defaultVal: appStore.navbar},
  {
    name: '菜单栏',
    key: 'menu',
    defaultVal: appStore.menu,
  },
  {name: '底部', key: 'footer', defaultVal: appStore.footer},
  {name: '多页签', key: 'tabBar', defaultVal: appStore.tabBar},
  {
    name: '菜单来源于后台',
    key: 'menuFromServer',
    defaultVal: appStore.menuFromServer,
  },
  {
    name: '菜单宽度 (px)',
    key: 'menuWidth',
    defaultVal: appStore.menuWidth,
    type: 'number',
  },
]);
const othersOpts = computed(() => [
  {
    name: '色弱模式',
    key: 'colorWeak',
    defaultVal: appStore.colorWeak,
  },
]);

const cancel = () => {
  appStore.updateSettings({globalSettings: false});
  emit('cancel');
};
const copySettings = async () => {
  const text = JSON.stringify(appStore.$state, null, 2);
  await copy(text);
  Message.success('复制成功，请粘贴到 src/settings.json 文件中');
};
const setVisible = () => {
  appStore.updateSettings({globalSettings: true});
};
</script>

<style scoped lang="less">
.fixed-settings {
  position: fixed;
  top: 280px;
  right: 0;

  svg {
    font-size: 18px;
    vertical-align: -4px;
  }
}
</style>
