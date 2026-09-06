<template>
  <a-list :bordered="false">
    <a-list-item>
      <a-list-item-meta>
        <template #avatar>
          <a-typography-paragraph>
            {{ '登录密码' }}
          </a-typography-paragraph>
        </template>
        <template #description>
          <div class="content">
            <a-typography-paragraph>
              {{ '已设置。密码至少6位字符，支持数字、字母和除空格外的特殊字符，且必须同时包含数字和大小写字母。' }}
            </a-typography-paragraph>
          </div>
          <div class="operation">
            <a-link>
              {{ '修改' }}
            </a-link>
          </div>
        </template>
      </a-list-item-meta>
    </a-list-item>
    <a-list-item>
      <a-list-item-meta>
        <template #avatar>
          <a-typography-paragraph>
            {{ '密保问题' }}
          </a-typography-paragraph>
        </template>
        <template #description>
          <div class="content">
            <a-typography-paragraph class="tip">
              {{
                '您暂未设置密保问题，密保问题可以有效的保护账号的安全。'
              }}
            </a-typography-paragraph>
          </div>
          <div class="operation">
            <a-link>
              {{ '设置' }}
            </a-link>
          </div>
        </template>
      </a-list-item-meta>
    </a-list-item>
    <a-list-item>
      <a-list-item-meta>
        <template #avatar>
          <a-typography-paragraph>
            {{ '安全手机' }}
          </a-typography-paragraph>
        </template>
        <template #description>
          <div class="content">
            <a-typography-paragraph>
              已绑定：150******50
            </a-typography-paragraph>
          </div>
          <div class="operation">
            <a-link>
              {{ '修改' }}
            </a-link>
          </div>
        </template>
      </a-list-item-meta>
    </a-list-item>
    <a-list-item>
      <a-list-item-meta>
        <template #avatar>
          <a-typography-paragraph>
            {{ '安全邮箱' }}
          </a-typography-paragraph>
        </template>
        <template #description>
          <div class="content">
            <a-typography-paragraph class="tip">
              {{ '您暂未设置邮箱，绑定邮箱可以用来找回密码、接收通知等。' }}
            </a-typography-paragraph>
          </div>
          <div class="operation">
            <a-link>
              {{ '修改' }}
            </a-link>
          </div>
        </template>
      </a-list-item-meta>
    </a-list-item>
  </a-list>
</template>

<script lang="ts" setup></script>

<style scoped lang="less">
:deep(.arco-list-item) {
  border-bottom: none !important;

  .arco-typography {
    margin-bottom: 20px;
  }

  .arco-list-item-meta-avatar {
    margin-bottom: 1px;
  }

  .arco-list-item-meta {
    padding: 0;
  }
}

:deep(.arco-list-item-meta-content) {
  flex: 1;
  border-bottom: 1px solid var(--color-neutral-3);

  .arco-list-item-meta-description {
    display: flex;
    flex-flow: row;
    justify-content: space-between;

    .tip {
      color: rgb(var(--gray-6));
    }

    .operation {
      margin-right: 6px;
    }
  }
}
</style>
