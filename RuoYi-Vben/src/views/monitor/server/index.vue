<template>
  <PageWrapper title="服务器信息查看" contentFullHeight>
    <Row type="flex" :gutter="[16, 16]">
      <Col :xs="24" :sm="24" :md="12" :lg="12">
        <InfoContainer
          title="CPU"
          icon="bi:cpu"
          :loading="componentsLoading"
          :register="registerCPUDescription"
          :data="serverInfo?.cpu"
          :canExpan="canExpan"
        />
      </Col>
      <Col :xs="24" :sm="24" :md="12" :lg="12">
        <InfoContainer
          title="内存信息"
          icon="la:memory"
          :loading="componentsLoading"
          :register="registerMemoryDescription"
          :data="serverInfo?.mem"
          :canExpan="canExpan"
        />
      </Col>
      <Col :xs="24" :sm="24" :md="12" :lg="12">
        <InfoContainer
          title="JVM内存"
          icon="la:memory"
          :loading="componentsLoading"
          :register="registerJvmMemoryDescription"
          :data="serverInfo?.jvm"
          :canExpan="canExpan"
        />
      </Col>
      <Col :xs="24" :sm="24" :md="12" :lg="12">
        <InfoContainer
          title="服务器信息"
          icon="mdi-light:memory"
          :loading="componentsLoading"
          :register="registerServerDescription"
          :data="serverInfo?.sys"
          :canExpan="canExpan"
        />
      </Col>
      <!-- jvm信息 -->
      <Col :span="24">
        <InfoContainer
          title="Java信息"
          icon="devicon:java-wordmark"
          :loading="componentsLoading"
          :register="registerJvmDescription"
          :data="serverInfo?.jvm"
          :canExpan="canExpan"
        />
      </Col>
      <Col :span="24">
        <CollapseContainer>
          <template #title>
            <span class="title">
              <Icon icon="icon-park-outline:solid-state-disk" />&nbsp;磁盘信息</span
            >
          </template>
          <BasicTable
            @register="registerTable"
            :loading="componentsLoading"
            :data-source="serverInfo?.sysFiles"
            :canExpan="canExpan"
          />
        </CollapseContainer>
      </Col>
    </Row>
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { getServerInfo, ServerInfo } from '@/api/monitor/server';
  import { onMounted, ref } from 'vue';
  import { Row, Col } from 'ant-design-vue';
  import { CollapseContainer } from '@/components/Container/index';
  import Icon from '@/components/Icon/Icon.vue';
  import { DescItem, useDescription } from '@/components/Description/index';
  import { BasicTable, useTable } from '@/components/Table';
  import InfoContainer from './InfoContainer.vue';

  defineOptions({ name: 'Server' });

  // 是否可折叠
  const canExpan = ref<boolean>(false);

  const serverInfo = ref<ServerInfo>();
  const componentsLoading = ref<boolean>(true);

  onMounted(async () => {
    const ret = await getServerInfo();
    // 在java信息里显示 对齐
    ret.jvm['userDir'] = ret.sys.userDir;
    serverInfo.value = ret;
    // 加载完成后loading false
    componentsLoading.value = false;
  });

  const [registerCPUDescription] = useDescription({
    column: 1,
    schema: [
      {
        field: 'cpuNum',
        label: 'CPU核心数',
      },
      {
        field: 'used',
        label: '用户使用率',
        render(value) {
          return `${value}%`;
        },
      },
      {
        field: 'sys',
        label: '系统使用率',
        render(value) {
          return `${value}%`;
        },
      },
      {
        field: 'free',
        label: '当前空闲率',
        render(value) {
          return `${value}%`;
        },
      },
    ],
  });

  /**
   * 提取公共
   * @param unit 内存单位
   */
  function getMemorySchema(unit: string): DescItem[] {
    return [
      {
        field: 'total',
        label: '总内存',
        render(value) {
          return `${value}${unit}`;
        },
      },
      {
        field: 'used',
        label: '已使用',
        render(value) {
          return `${value}${unit}`;
        },
      },
      {
        field: 'free',
        label: '剩余',
        render(value) {
          return `${value}${unit}`;
        },
      },
      {
        field: 'usage',
        label: '使用率',
        render(value) {
          return `${value}%`;
        },
      },
    ];
  }

  const [registerMemoryDescription] = useDescription({
    column: 1,
    schema: getMemorySchema('G'),
  });

  const [registerJvmMemoryDescription] = useDescription({
    column: 1,
    schema: getMemorySchema('M'),
  });

  const [registerJvmDescription] = useDescription({
    column: 2,
    schema: [
      {
        field: 'name',
        labelMinWidth: 130,
        label: '类型',
      },
      {
        field: 'version',
        label: '版本',
      },
      {
        field: 'startTime',
        label: '启动时间',
      },
      {
        field: 'runTime',
        label: '运行时长',
      },
      {
        field: 'home',
        label: '路径',
      },
      {
        field: 'userDir',
        label: '项目路径',
      },
      {
        field: 'inputArgs',
        label: '运行参数',
      },
    ],
  });

  const [registerServerDescription] = useDescription({
    column: 1,
    schema: [
      {
        field: 'computerIp',
        label: '服务器IP',
      },
      {
        field: 'computerName',
        label: '服务器名称',
      },
      {
        field: 'osArch',
        label: '系统架构',
      },
      {
        field: 'osName',
        label: '操作系统',
      },
    ],
  });

  const [registerTable] = useTable({
    pagination: false,
    showTableSetting: false,
    columns: [
      {
        dataIndex: 'dirName',
        title: '盘符路径',
      },
      {
        dataIndex: 'sysTypeName',
        title: '文件系统',
      },
      {
        dataIndex: 'typeName',
        title: '盘符类型',
      },
      {
        dataIndex: 'used',
        title: '已用空间',
      },
      {
        dataIndex: 'usage',
        title: '已用空间占比',
        customRender({ value }) {
          return `${value}%`;
        },
      },
      {
        dataIndex: 'free',
        title: '可用大小',
      },
      {
        dataIndex: 'total',
        title: '总大小',
      },
    ],
  });
</script>

<style lang="less" scoped></style>
