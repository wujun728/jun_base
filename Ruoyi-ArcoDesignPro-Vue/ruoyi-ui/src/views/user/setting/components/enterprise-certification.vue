<template>
  <a-card
      class="general-card"
      :title="'企业实名认证'"
      :header-style="{ padding: '0px 20px 16px 20px' }"
  >
    <template #extra>
      <a-link>{{ '修改认证主体' }}</a-link>
    </template>
    <a-descriptions
        class="card-content"
        :data="renderData"
        :column="3"
        align="right"
        layout="inline-horizontal"
        :label-style="{ fontWeight: 'normal' }"
        :value-style="{
        width: '200px',
        paddingLeft: '8px',
        textAlign: 'left',
      }"
    >
      <template #label="{ label }">{{ label }} :</template>
      <template #value="{ value, data }">
        <a-tag
            v-if="data.label === '认证状态'"
            color="green"
            size="small"
        >
          已认证
        </a-tag>
        <span v-else>{{ value }}</span>
      </template>
    </a-descriptions>
  </a-card>
</template>

<script lang="ts" setup>
import {PropType, computed} from 'vue';
import {EnterpriseCertificationModel} from '@/api/user-center';
import type {DescData} from '@arco-design/web-vue/es/descriptions/interface';

const props = defineProps({
  enterpriseInfo: {
    type: Object as PropType<EnterpriseCertificationModel>,
    required: true,
  },
});
const renderData = computed(() => {
  const {
    accountType,
    status,
    time,
    legalPerson,
    certificateType,
    authenticationNumber,
    enterpriseName,
    enterpriseCertificateType,
    organizationCode,
  } = props.enterpriseInfo;
  return [
    {
      label: '账号类型',
      value: accountType,
    },
    {
      label: '认证状态',
      value: status,
    },
    {
      label: '认证时间',
      value: time,
    },
    {
      label: '法人姓名',
      value: legalPerson,
    },
    {
      label: '法人证件类型',
      value: certificateType,
    },
    {
      label: '法人认证号码',
      value: authenticationNumber,
    },
    {
      label: '企业名称',
      value: enterpriseName,
    },
    {
      label: '企业证件类型',
      value: enterpriseCertificateType,
    },
    {
      label: '组织机构代码',
      value: organizationCode,
    },
  ] as DescData[];
});
</script>

<style scoped lang="less">
.card-content {
  width: 100%;
  padding: 20px;
  background-color: rgb(var(--gray-1));
}

.item-label {
  min-width: 98px;
  text-align: right;
  color: var(--color-text-8);

  &:after {
    content: ':';
  }
}
</style>
