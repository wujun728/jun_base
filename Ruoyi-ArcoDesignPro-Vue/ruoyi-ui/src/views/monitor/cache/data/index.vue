<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统监控', '缓存列表']"></Breadcrumb>
    <a-card
        class="general-card r-nav-card"
        :body-style="{ padding: '15px 0' }"
        :header-style="{ padding: '15px 0' }"
    >
      <a-row style="padding-left: 15px;">
        <a-col :span="8" class="card-box">
          <a-card style="height: calc(100vh - 125px)">
            <template #title>
              <span><icon-computer/>缓存列表</span>
            </template>
            <template #extra>
              <a-link @click="handleRefreshCacheNames()">
                <icon-refresh/>
              </a-link>
            </template>
            <a-table
                row-key="cacheName"
                :columns="state.cacheNameColumns"
                :data="state.cacheNames"
                :loading="loading"
                :bordered="false"
                :pagination="false"
                :size="'medium'"
                @row-click="fetchCacheKeys"
            >
              <template #index="{ record }">
                {{ state.cacheNames.indexOf(record) + 1 }}
              </template>
              <template #cacheName="{ record }">
                {{ record.cacheName.replace(":", "") }}
              </template>
              <template #optional="{ record }">
                <a-link @click="handleClearCacheName(record)">
                  <icon-delete/>
                </a-link>
              </template>
            </a-table>
          </a-card>
        </a-col>
        <a-col :span="8" class="card-box">
          <a-card style="height: calc(100vh - 125px)">
            <template #title>
              <span><icon-computer/>键名列表</span>
            </template>
            <template #extra>
              <a-link @click="handleRefreshCacheKeys()">
                <icon-refresh/>
              </a-link>
            </template>
            <a-table
                :columns="state.cacheKeyColumns"
                :data="state.cacheKeys"
                :loading="subLoading"
                :bordered="false"
                :pagination="false"
                :size="'medium'"
                @row-click="handleCacheValue"
            >
              <template #index="{ record }">
                {{ state.cacheKeys.indexOf(record) + 1 }}
              </template>
              <template #cacheKey="{ record }">
                {{ record.replace(state.nowCacheName, "") }}
              </template>
              <template #optional="{ record }">
                <a-link @click="handleClearCacheKey(record)">
                  <icon-delete/>
                </a-link>
              </template>
            </a-table>
          </a-card>
        </a-col>
        <a-col :span="8" class="card-box">
          <a-card style="height: calc(100vh - 125px)">
            <template #title>
              <span><icon-computer/>缓存内容</span>
            </template>
            <template #extra>
              <a-link @click="handleClearCacheAll()">
                <icon-refresh/>
                清理全部
              </a-link>
            </template>
            <a-form :model="state.cacheForm">
              <a-row :gutter="16">
                <a-col :offset="1" :span="22">
                  <a-form-item label="缓存名称" field="cacheName">
                    <a-input v-model="state.cacheForm.cacheName" readonly/>
                  </a-form-item>
                </a-col>
                <a-col :offset="1" :span="22">
                  <a-form-item label="缓存键名" field="cacheKey">
                    <a-input v-model="state.cacheForm.cacheKey" readonly/>
                  </a-form-item>
                </a-col>
                <a-col :offset="1" :span="22">
                  <a-form-item label="缓存内容" field="cacheValue">
                    <a-textarea v-model="state.cacheForm.cacheValue" :auto-size="true" readonly/>
                  </a-form-item>
                </a-col>
              </a-row>
            </a-form>
          </a-card>
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script lang="ts" setup>

import {reactive} from "vue";
import useLoading from "@/hooks/loading";
import {
  clearCacheAll,
  clearCacheKey,
  clearCacheName,
  getCacheValue,
  listCacheKey,
  listCacheName
} from "@/api/monitor/cache";
import {Message} from "@arco-design/web-vue";
import {TableData} from "@arco-design/web-vue/es/table/interface";

const state = reactive({
  cacheNameColumns: [
    {
      title: "序号",
      slotName: 'index',
      width: 60,
    },
    {
      title: '缓存名称',
      dataIndex: 'cacheName',
      slotName: 'cacheName',
      width: 150,
    },
    {
      title: '备注',
      dataIndex: 'remark',
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'optional',
      slotName: 'optional',
      width: 60,
    },
  ],
  cacheKeyColumns: [
    {
      title: "序号",
      slotName: 'index',
      width: 60,
    },
    {
      title: '缓存键名',
      dataIndex: 'cacheKey',
      slotName: 'cacheKey',
      width: 300,
    },
    {
      title: '操作',
      dataIndex: 'optional',
      slotName: 'optional',
      width: 60,
    },
  ],
  nowCacheName: "",
  cacheNames: [],
  cacheKeys: [],
  cacheForm: {
    cacheForm: '',
    cacheKey: '',
    cacheValue: '',
  },
})

const {loading, setLoading} = useLoading(true);
// 查询缓存名称列表
const fetchCacheNames = async () => {
  setLoading(true);
  const {data} = await listCacheName();
  state.cacheNames = data;
  setLoading(false);
};
fetchCacheNames();

// 刷新缓存名称列表
const handleRefreshCacheNames = () => {
  fetchCacheNames();
  Message.success("刷新缓存列表成功");
};

// 查询缓存键名列表
const {loading: subLoading, setLoading: setSubLoading} = useLoading(false);
const fetchCacheKeys = async (record: TableData | undefined) => {
  const cacheName = record !== undefined ? record.cacheName : state.nowCacheName;
  if (cacheName === "") {
    return;
  }
  setSubLoading(true);
  const {data} = await listCacheKey(cacheName);
  state.cacheKeys = data;
  state.nowCacheName = cacheName;
  setSubLoading(false);
};

// 清理指定名称缓存
const handleClearCacheName = async (record: any) => {
  await clearCacheName(record.cacheName);
  Message.success(`清理缓存名称[${state.nowCacheName}]成功`);
  await fetchCacheKeys(undefined);
};

// 刷新缓存键名列表
const handleRefreshCacheKeys = () => {
  fetchCacheKeys(undefined);
  Message.success("刷新键名列表成功");
};

// 清理指定键名缓存
const handleClearCacheKey = async (cacheKey: any) => {
  await clearCacheKey(cacheKey);
  Message.success(`清理缓存键名[${cacheKey}]成功`);
  await fetchCacheKeys(undefined);
};

// 查询缓存内容详细
const handleCacheValue = async (record: string | undefined) => {
  const cacheKey = record !== undefined ? record : "";
  const {data} = await getCacheValue(state.nowCacheName, cacheKey);
  state.cacheForm = data;
};

const handleClearCacheAll = async () => {
  await clearCacheAll();
  Message.success("清理全部缓存成功");
};

</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}

.card-box {
  padding-right: 15px;
  margin-bottom: 20px;
}

</style>
