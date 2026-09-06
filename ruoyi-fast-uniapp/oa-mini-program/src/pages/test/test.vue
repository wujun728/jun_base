<script lang="ts" setup>
import { getCommonParams, setCommonParams } from '@/config/commonParams';
import { useInit } from '@/hooks/useInit';
import { useTitle } from '@/hooks/useTitle';
import { onUrlPage } from '@/utils/router';
import uniAsync from '@/utils/uniAsync';
import { getUser } from '@/service/api/user';
import { useUserStore } from '@/store';

onLoad(() => {
  const { pageName, pagePath, pageQuery } = useInit();
  console.log(pageName, pagePath, pageQuery, 'pageName,pagePath, pageQuery');
});
const { title, changeTitle } = useTitle();

const userStore = useUserStore();

async function getTest() {
  const getTest = await getUser({ id: 1 });
  if (!getTest) {
    uni.showToast({
      title: '自定义异常处理'
    });
    return;
  }
  // getTest.data?.age
  // getTest.data?.name
  console.log(getTest, 'getTest');
}
async function postTest() {
  const postTest = await getUser({ id: 1 });
  if (!postTest) return;
  // postTest.data?.val
  console.log(postTest, 'postTest');
}

function getCommonParam() {
  console.log(getCommonParams());
}
function setCommonParam() {
  setCommonParams({ channel: 'test' });
  getCommonParam();
}

async function uniAsyncTest() {
  const systemInfo = await uniAsync.getSystemInfo();
  console.log(systemInfo, 'systemInfo');
}

function onScrollToLower() {
  console.log('自定义 onScrollToLower');
}
function handleClickLeft() {
  uni.navigateBack()
}
</script>

<template>
  <wd-navbar title="标题" left-text="返回" left-arrow @click-left="handleClickLeft"></wd-navbar>
  <div class="h-screen">
    <pullList :on-scroll-to-lower="onScrollToLower">
      <template #list>
        <view>name:{{ userStore.user.id }}</view>
        <view>fullName:{{ userStore.user.id }}</view>
        <view>title:{{ title }}</view>
        <view @click="changeTitle">changeTitle</view>
        <view @click="getTest">getTest</view>
        <view @click="postTest">postTest</view>
        <view @click="getCommonParam">getCommonParams</view>
        <view @click="setCommonParam">setCommonParams</view>
        <view @click="uniAsyncTest">uniAsyncTest</view>
        <view data-url="index?a=1" @click="onUrlPage">onUrlPage</view>
        <view class="p-10">unocss-test</view>
        <view v-for="i in 30" :key="i" class="p10">scroll Test</view>
      </template>
    </pullList>
  </div>
</template>
