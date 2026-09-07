<template>
  <PageWrapper title="缓存监控">
    <Row :gutter="[15, 15]">
      <Col :span="24">
        <CollapseContainer :canExpan="false">
          <template #title>
            <div class="flex gap-1.5">
              <Icon icon="logos:redis" :size="14" />
              <span>redis信息</span>
            </div>
          </template>
          <template #action>
            <a-button size="small" class="mr-2" :pre-icon="IconEnum.REFRESH" @click="load"
              >刷新</a-button
            >
          </template>
          <Description @register="registerDescription" />
        </CollapseContainer>
      </Col>
      <Col :sm="24" :xs="24" :md="24" :lg="12">
        <CollapseContainer title="命令统计" :canExpan="false">
          <template #title>
            <div class="flex gap-1.5">
              <Icon icon="flat-color-icons:command-line" />
              <span>命令统计</span>
            </div>
          </template>
          <CommandChart :data="chartData.command" />
        </CollapseContainer>
      </Col>
      <Col :sm="24" :xs="24" :md="24" :lg="12">
        <CollapseContainer :canExpan="false">
          <template #title>
            <div class="flex gap-1.5">
              <Icon icon="la:memory" />
              <span>内存占用</span>
            </div>
          </template>
          <MemoryChart :data="chartData.memory" />
        </CollapseContainer>
      </Col>
    </Row>
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { Description, useDescription } from '@/components/Description';
  import { onMounted, reactive } from 'vue';
  import { redisCacheInfo } from '@/api/monitor/cache';
  import { CollapseContainer } from '@/components/Container';
  import { descSchemas } from './cache.data';
  import { Row, Col } from 'ant-design-vue';
  import { IconEnum } from '@/enums/appEnum';
  import MemoryChart from './components/MemoryChart.vue';
  import CommandChart from './components/CommandChart.vue';
  import { useLoading } from '@/components/Loading';
  import Icon from '@/components/Icon/Icon.vue';

  defineOptions({ name: 'Cache' });

  const chartData = reactive<{ memory: string; command: any[] }>({
    memory: '0',
    command: [],
  });

  onMounted(async () => {
    await load();
  });

  const [openFullLoading, closeFullLoading] = useLoading({
    tip: '加载中...',
  });

  async function load() {
    try {
      openFullLoading();
      const ret = await redisCacheInfo();

      // 单位MB 保留两位小数
      const usedMemory = (parseInt(ret.info['used_memory']) / 1024 / 1024).toFixed(2);
      chartData.memory = usedMemory;
      // 命令统计
      chartData.command = ret.commandStats;

      setDescProps({ data: { ...ret.info, dbSize: ret.dbSize } });
    } catch (e) {
      console.warn(e);
    } finally {
      closeFullLoading();
    }
  }

  const [registerDescription, { setDescProps }] = useDescription({
    column: { xs: 1, sm: 1, md: 3, lg: 4, xl: 4 },
    schema: descSchemas,
  });
</script>

<style scoped></style>
