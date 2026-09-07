<template>
  <div :class="[prefixCls, { fullscreen }]">
    <Upload
      name="file"
      multiple
      @change="handleChange"
      :action="uploadUrl"
      :headers="headers"
      :showUploadList="false"
      accept=".jpg,.jpeg,.gif,.png,.webp"
    >
      <a-button type="primary" v-bind="{ ...getButtonProps }">
        {{ t('component.upload.imgUpload') }}
      </a-button>
    </Upload>
  </div>
</template>
<script lang="ts" setup>
  import { computed } from 'vue';

  import { Upload } from 'ant-design-vue';
  import { useDesign } from '@/hooks/web/useDesign';
  import { useGlobSetting } from '@/hooks/setting';
  import { useI18n } from '@/hooks/web/useI18n';
  import { getToken } from '@/utils/auth';
  import { useMessage } from '@/hooks/web/useMessage';

  defineOptions({ name: 'TinymceImageUpload' });

  const props = defineProps({
    fullscreen: {
      type: Boolean,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  });

  const emit = defineEmits(['uploading', 'done', 'error']);

  let uploading = false;

  const { apiUrl } = useGlobSetting();
  const uploadUrl = `${apiUrl}/common/upload`;
  // 使用upload组件只能这样上传
  const headers = {
    Authorization: 'Bearer ' + getToken(),
  };
  const { t } = useI18n();
  const { prefixCls } = useDesign('tinymce-img-upload');

  const getButtonProps = computed(() => {
    const { disabled } = props;
    return {
      disabled,
    };
  });

  const { createMessage } = useMessage();
  function handleChange(info: Record<string, any>) {
    const file = info.file;
    const status = file?.status;
    // const url = file?.response?.data.url;
    const name = file?.name;

    if (status === 'uploading') {
      if (!uploading) {
        emit('uploading', name);
        uploading = true;
      }
    } else if (status === 'done') {
      // http 200会走到这里  需要再次判断
      const { response } = file;
      const { code, msg = '服务器错误', url } = response;
      if (code === 200) {
        emit('done', name, url);
      } else {
        createMessage.error(msg);
      }
      // emit('done', name, url);
      uploading = false;
    } else if (status === 'error') {
      emit('error');
      uploading = false;
    }
  }
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-tinymce-img-upload';

  .@{prefix-cls} {
    position: absolute;
    z-index: 20;
    top: 4px;
    right: 10px;

    &.fullscreen {
      position: fixed;
      z-index: 10000;
    }
  }
</style>
