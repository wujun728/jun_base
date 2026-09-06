<template>
  <wd-tabbar :modelValue="props.selected" fixed bordered safeAreaInsetBottom placeholder @change="tabChange">
    <wd-tabbar-item v-for="item in list" :key="item.icon" :title="item.title">
      <template #icon>
        <wd-img round height="40rpx" width="40rpx"
          :src="props.selected === item.index ? item.icon : item.iconM"></wd-img>
      </template>
    </wd-tabbar-item>
  </wd-tabbar>
</template>

<script lang="ts" setup>
import indexSrc from '../static/icon/index.png'
import indexSrcM from '../static/icon/index-m.png'
import office from '../static/icon/office.png'
import officeM from '../static/icon/office-m.png'
import message from '../static/icon/message.png'
import messageM from '../static/icon/message-m.png'
import mine from '../static/icon/mine.png'
import mineM from '../static/icon/mine-m.png'
const props = withDefaults(
  defineProps<{
    selected?: number;
  }>(),
  {
    selected: 0
  }
);

interface ListItem {
  index: number,
  pagePath: string;
  title: string;
  icon: string;
  iconM: string;
}

const list = ref<ListItem[]>([
  {
    pagePath: '/pages/index/index',
    title: '首页',
    iconM: indexSrcM,
    icon: indexSrc,
    index: 0
  },
  {
    pagePath: '/pages/office/office',
    title: '办公',
    iconM: officeM,
    icon: office,
    index: 1
  },
  {
    pagePath: '/pages/message/message',
    title: '消息',
    iconM: messageM,
    icon: message,
    index: 2
  },
  {
    pagePath: '/pages/mine/mine',
    title: '我的',
    iconM: mineM,
    icon: mine,
    index: 3
  }
]);

const tabChange = ({ value }: { value: number }) => {
  const url = list.value[value].pagePath;
  uni.switchTab({
    url
  });
};
</script>