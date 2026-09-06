<template>
  <a-card
      class="general-card"
      :title="'帮助文档'"
      :header-style="{ paddingBottom: 0 }"
      :body-style="{ paddingTop: 0 }"
      style="height: 166px"
  >
    <template #extra>
      <a-link>{{ '查看更多' }}</a-link>
    </template>
    <a-row>
      <a-col :span="12">
        <a-link>
          {{ '产品概要' }}
        </a-link>
      </a-col>
      <a-col :span="12">
        <a-link>
          {{ '使用指南' }}
        </a-link>
      </a-col>
      <a-col :span="12">
        <a-link>
          {{ '接入流程' }}
        </a-link>
      </a-col>
      <a-col :span="12">
        <a-link>
          {{ '接口文档' }}
        </a-link>
      </a-col>
    </a-row>
  </a-card>
</template>

<style lang="less" scoped>
.arco-card-body .arco-link {
  margin: 10px 0;
  color: rgb(var(--gray-8));
}
</style>
