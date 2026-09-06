<template>
  <div class="image-preview-container">
    <div class="image-preview-wrapper" v-if="imageList.length > 0">
      <div
          class="image-item"
          v-for="(image, index) in imageList"
          :key="index"
          @click="handlePreview(image, index)"
      >
        <img :src="getImageUrl(image)" :alt="`图片${index + 1}`" />
      </div>
    </div>
    <div class="empty-placeholder" v-else>
      <a-empty description="暂无图片" />
    </div>

    <!-- 预览弹窗 -->
    <a-modal
        v-model:open="previewVisible"
        :title="previewTitle"
        :footer="null"
        @cancel="previewVisible = false"
        :width="800"
    >
      <div class="preview-container">
        <img :src="previewImage" :alt="previewTitle" />
      </div>

      <!-- 缩略图导航 -->
      <div class="thumbnail-nav" v-if="imageList.length > 1">
        <div
            class="thumbnail-item"
            v-for="(image, index) in imageList"
            :key="index"
            :class="{ active: currentIndex === index }"
            @click="handlePreview(image, index)"
        >
          <img :src="getImageUrl(image)" :alt="`缩略图${index + 1}`" />
        </div>
      </div>

      <!-- 导航按钮 -->
      <div class="preview-actions">
        <a-button
            type="primary"
            shape="circle"
            icon="left"
            @click="prevImage"
            :disabled="currentIndex <= 0"
            v-if="imageList.length > 1"
        >
          <template #icon><left-outlined /></template>
        </a-button>
        <span class="preview-counter" v-if="imageList.length > 1">
          {{ currentIndex + 1 }} / {{ imageList.length }}
        </span>
        <a-button
            type="primary"
            shape="circle"
            icon="right"
            @click="nextImage"
            :disabled="currentIndex >= imageList.length - 1"
            v-if="imageList.length > 1"
        >
          <template #icon><right-outlined /></template>
        </a-button>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { defineComponent, ref, computed, watch } from 'vue';
import { LeftOutlined, RightOutlined } from '@ant-design/icons-vue';
import { isExternal } from "@/utils/bearjia";

export default defineComponent({
  name: "ImagePreview",
  components: {
    LeftOutlined,
    RightOutlined
  },
  props: {
    // 图片列表，可以是字符串(逗号分隔)、数组或对象
    images: {
      type: [String, Array, Object],
      default: ''
    },
    // 基础URL，用于拼接相对路径
    baseUrl: {
      type: String,
      default: import.meta.env.VITE_APP_BASE_API || ''
    }
  },
  setup(props) {
    // 预览状态
    const previewVisible = ref(false);
    const previewImage = ref('');
    const previewTitle = ref('');
    const currentIndex = ref(0);

    // 处理图片列表
    const imageList = computed(() => {
      if (!props.images) return [];

      if (typeof props.images === 'string') {
        return props.images.split(',').filter(item => item);
      } else if (Array.isArray(props.images)) {
        return props.images.filter(item => item);
      } else if (typeof props.images === 'object') {
        return [props.images];
      }

      return [];
    });

    // 获取图片完整URL
    const getImageUrl = (image) => {
      if (typeof image === 'string') {
        if (isExternal(image) || image.startsWith('data:')) {
          return image;
        }
        return props.baseUrl + image;
      } else if (typeof image === 'object') {
        return image.url || '';
      }
      return '';
    };

    // 预览图片
    const handlePreview = (image, index) => {
      currentIndex.value = index;
      previewImage.value = getImageUrl(image);
      previewVisible.value = true;

      // 设置标题
      if (typeof image === 'string') {
        const fileName = image.split('/').pop();
        previewTitle.value = fileName || `图片${index + 1}`;
      } else if (typeof image === 'object') {
        previewTitle.value = image.name || `图片${index + 1}`;
      } else {
        previewTitle.value = `图片${index + 1}`;
      }
    };

    // 上一张图片
    const prevImage = () => {
      if (currentIndex.value > 0) {
        currentIndex.value--;
        const image = imageList.value[currentIndex.value];
        previewImage.value = getImageUrl(image);

        // 更新标题
        if (typeof image === 'string') {
          const fileName = image.split('/').pop();
          previewTitle.value = fileName || `图片${currentIndex.value + 1}`;
        } else if (typeof image === 'object') {
          previewTitle.value = image.name || `图片${currentIndex.value + 1}`;
        } else {
          previewTitle.value = `图片${currentIndex.value + 1}`;
        }
      }
    };

    // 下一张图片
    const nextImage = () => {
      if (currentIndex.value < imageList.value.length - 1) {
        currentIndex.value++;
        const image = imageList.value[currentIndex.value];
        previewImage.value = getImageUrl(image);

        // 更新标题
        if (typeof image === 'string') {
          const fileName = image.split('/').pop();
          previewTitle.value = fileName || `图片${currentIndex.value + 1}`;
        } else if (typeof image === 'object') {
          previewTitle.value = image.name || `图片${currentIndex.value + 1}`;
        } else {
          previewTitle.value = `图片${currentIndex.value + 1}`;
        }
      }
    };

    return {
      imageList,
      previewVisible,
      previewImage,
      previewTitle,
      currentIndex,
      getImageUrl,
      handlePreview,
      prevImage,
      nextImage
    };
  }
});
</script>

<style scoped lang="less">
.image-preview-container {
  .image-preview-wrapper {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;

    .image-item {
      width: 100px;
      height: 100px;
      border-radius: 4px;
      overflow: hidden;
      cursor: pointer;
      border: 1px solid #e8e8e8;
      transition: all 0.3s;

      &:hover {
        border-color: #1890ff;
        box-shadow: 0 0 8px rgba(24, 144, 255, 0.2);
      }

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
  }

  .preview-container {
    display: flex;
    justify-content: center;
    align-items: center;

    img {
      max-width: 100%;
      max-height: 60vh;
      object-fit: contain;
    }
  }

  .thumbnail-nav {
    display: flex;
    justify-content: center;
    margin-top: 16px;
    gap: 8px;
    overflow-x: auto;
    padding-bottom: 8px;

    .thumbnail-item {
      width: 60px;
      height: 60px;
      border-radius: 4px;
      overflow: hidden;
      cursor: pointer;
      border: 2px solid transparent;
      transition: all 0.3s;

      &:hover {
        border-color: #1890ff;
      }

      &.active {
        border-color: #1890ff;
      }

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
  }

  .preview-actions {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 16px;
    gap: 16px;

    .preview-counter {
      font-size: 14px;
      color: #606266;
    }
  }
}
</style>