<template>
  <PageWrapper>
    <Row :gutter="[20, 20]">
      <Col v-bind="span">
        <BasicTable @register="registerFirstTable" @row-click="handleCacheNameClick" />
      </Col>
      <Col v-bind="span">
        <BasicTable @register="registerSecondTable" @row-click="handleCacheKeyClick" />
      </Col>
      <Col v-bind="span">
        <Description @register="registerDescription">
          <template #action>
            <a-button danger size="small" @click="handleClearAll">清空全部</a-button>
          </template>
        </Description>
      </Col>
    </Row>
  </PageWrapper>
</template>

<script setup lang="tsx">
  import { PageWrapper } from '@/components/Page';
  import { useTable, BasicTable } from '@/components/Table';
  import { Row, Col } from 'ant-design-vue';
  import { reactive, ref } from 'vue';
  import {
    cacheGetNames,
    cacheGetKeys,
    cacheGetValue,
    cacheClearName,
    cacheClearKey,
    cacheClearAll,
  } from '@/api/monitor/cache';
  import { useDescription, Description } from '@/components/Description';
  import { useRender } from '@/hooks/component/useRender';

  defineOptions({ name: 'CacheList' });

  const span = reactive({ xs: 24, sm: 24, md: 12, lg: 8 });

  const [registerFirstTable, { reload }] = useTable({
    api: cacheGetNames,
    rowKey: 'cacheName',
    pagination: false,
    title: '缓存列表',
    showIndexColumn: true,
    columns: [
      {
        title: '缓存名称',
        dataIndex: 'cacheName',
      },
      {
        title: '备注',
        dataIndex: 'remark',
      },
      {
        title: '操作',
        customRender({ record }) {
          async function handleDelete() {
            await cacheClearName(record.cacheName);
            await reload();
          }

          return (
            <a-button size="small" danger onClick={handleDelete}>
              删除
            </a-button>
          );
        },
      },
    ],
  });

  const currentCacheName = ref('');
  async function handleCacheNameClick(record: Recordable) {
    const { cacheName } = record;
    currentCacheName.value = cacheName;
    setSecondTableProps({
      api: () => cacheGetKeys(cacheName),
    });
    await reloadSecondTable({ searchInfo: cacheName });
  }

  const [registerSecondTable, { reload: reloadSecondTable, setProps: setSecondTableProps }] =
    useTable({
      rowKey: 'key',
      pagination: false,
      title: 'key列表',
      immediate: false,
      showIndexColumn: true,
      afterFetch(data) {
        return data.map((item: string) => ({ key: item }));
      },
      columns: [
        {
          title: '缓存key',
          dataIndex: 'key',
        },
        {
          title: '操作',
          width: 100,
          customRender({ record }) {
            async function handleDelete() {
              await cacheClearKey(record.key);
              await reload();
            }

            return (
              <a-button size="small" danger onClick={handleDelete}>
                删除
              </a-button>
            );
          },
        },
      ],
    });

  async function handleCacheKeyClick(record: Recordable) {
    const { key } = record;
    const data = await cacheGetValue(currentCacheName.value, key);
    setDescProps({ data });
  }

  const { renderJsonPreview } = useRender();
  const [registerDescription, { setDescProps }] = useDescription({
    column: 1,
    layout: 'vertical',
    title: '缓存详情',
    schema: [
      {
        label: '名称',
        field: 'cacheName',
      },
      {
        label: '缓存key',
        field: 'cacheKey',
      },
      {
        label: '缓存内容',
        field: 'cacheValue',
        render: (value) => {
          return renderJsonPreview(value);
        },
      },
      {
        label: '备注',
        field: 'remark',
      },
    ],
  });

  async function handleClearAll() {
    await cacheClearAll();
    // 会被踢下线
    await reload();
  }
</script>
