<template>
  <a-card
      class="general-card"
      :title="'认证记录'"
      :header-style="{ border: 'none' }"
  >
    <a-table v-if="renderData.length" :data="renderData">
      <template #columns>
        <a-table-column
            :title="'认证类型'"
        >
          <template #cell>
            {{ '企业证件认证' }}
          </template>
        </a-table-column>
        <a-table-column
            :title="'认证内容'"
            data-index="certificationContent"
        />
        <a-table-column :title="'认证内容'">
          <template #cell="{ record }">
            <p v-if="record.status === 0">
              <span class="circle"></span>
              <span>{{ '审核中' }}</span>
            </p>
            <p v-if="record.status === 1">
              <span class="circle pass"></span>
              <span>{{ '已通过' }}</span>
            </p>
          </template>
        </a-table-column>
        <a-table-column
            :title="'创建时间'"
            data-index="time"
        />
        <a-table-column
            :title="'操作'"
        >
          <template #cell="{ record }">
            <a-space>
              <a-button type="text">
                {{ '查看' }}
              </a-button>
              <a-button v-if="record.status === 0" type="text">
                {{ '撤回' }}
              </a-button>
            </a-space>
          </template>
        </a-table-column>
      </template>
    </a-table>
  </a-card>
</template>

<script lang="ts" setup>
import {PropType} from 'vue';
import {CertificationRecord} from '@/api/user-center';

defineProps({
  renderData: {
    type: Array as PropType<CertificationRecord>,
    default() {
      return [];
    },
  },
});
</script>

<style scoped lang="less">
:deep(.arco-table-th) {
  &:last-child {
    .arco-table-th-item-title {
      margin-left: 16px;
    }
  }
}
</style>
