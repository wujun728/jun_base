<template>
  <div class="image-gallery">
    <image class="image" :src="imageUrl" @click="showImage" />
    <!-- 使用 v-if 控制是否显示放大图片的模态框 -->
    <div class="modal" v-if="isModalVisible" @click="closeImage">
      <div class="modal-content">
        <image class="img" :src="imageUrl" :style="modalImageStyle"/>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isModalVisible: false,
	  modalImageStyle: {}, // 控制放大图片的样式
    };
  },
  props: {
    imageUrl: String, // 传递图片 URL 作为 prop
  },
  methods: {
	showImage() {
	      // 点击图片时显示模态框
	      this.isModalVisible = true;
	      // 获取原始图片的宽度和高度
	      const img = new Image();
	      img.src = this.imageUrl;
	      img.onload = () => {
	        const width = img.width;
	        const height = img.height;
	        // 计算放大比例，保持原始宽高比
	        const maxWidth = window.innerWidth * 0.9;
	        const maxHeight = window.innerHeight * 0.9;
	        let newWidth = width;
	        let newHeight = height;
	        if (width > maxWidth) {
	          newWidth = maxWidth;
	          newHeight = (maxWidth / width) * height;
	        }
	        if (newHeight > maxHeight) {
	          newHeight = maxHeight;
	          newWidth = (maxHeight / height) * width;
	        }
	        this.modalImageStyle = {
	          width: `${newWidth}px`,
	          height: `${newHeight}px`,
	        };
	      };
	    },
    closeImage() {
      // 点击模态框内的图片时关闭模态框
      this.isModalVisible = false;
    },
  },
};
</script>

<style scoped>
/* 样式可以根据您的需求进行自定义 */
.image-gallery {
  position: relative;
  width: 100%;
  height: 100%;
}
uni-image {
	width: 500rpx;
	height: 500rpx;
}
.image {
	width: 100%;
	height: 100%;
}

.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  z-index: 999;
}

.modal-content {
  max-width: 90%;
  max-height: 90%;
}

.modal img {
  width: 100%;
  height: auto;
}
</style>