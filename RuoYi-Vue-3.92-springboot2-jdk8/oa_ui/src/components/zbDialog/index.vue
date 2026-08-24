<template>
  <el-dialog
    class="mldialog"
    :append-to-body="appendToBody"
    :width="isFullSceen ? '100%' : width"
    v-model="isShow"
    :show-close="false"
    @close="close"
    :close-on-click-modal="closeOnClickModal"
    :class="{ 'not-header': notHeader, isFullSceen: isFullSceen }"
    :top="isFullSceen ? '0' : top"
    v-if="isShow"
    :draggable="draggable"
  >
    <template #header>
      <span class="my-title">{{ title }}</span>
      <span class="fr close-icon" @click="close" v-if="showClose">
        <el-icon size="20">
          <ElIconClose />
        </el-icon>
      </span>

      <span class="fr full-screen-icon" @click="onFullSceen" v-if="showFullSceen">
        <el-icon size="20">
          <ElIconFullScreen />
        </el-icon>
      </span>
    </template>
    <slot></slot>
    <template #footer v-if="isShowFooter">
      <slot name="footer"></slot>
    </template>
  </el-dialog>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'MyDialog',
  props: {
    modelValue: null,
    title: { type: String, default: '' },
    appendToBody: { type: Boolean, default: false },
    width: { type: String, default: '50%' },
    closeOnClickModal: { type: Boolean, default: false },
    notHeader: { type: Boolean, default: false },
    top: { type: String, default: '15vh' },
    draggable: { type: Boolean, default: false },
    showClose: { type: Boolean, default: true },
    showFullSceen: { type: Boolean, default: false },
    autoFullScreen: { type: Boolean, default: false }
  },
  data() {
    return {
      isShow: null,
      isShowFooter: false,
      contentSlots: {},
      isFullSceen: false
    }
  },
  watch: {
    modelValue: {
      handler(newVal) {
        this.isShow = newVal
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
    this.isShow = this.modelValue
    if (this.autoFullScreen) {
      this.isFullSceen = true
    }
    this.contentSlots = this.$slots
    if (this.contentSlots.footer) {
      this.isShowFooter = true
    }
  },
  methods: {
    close() {
      this.isShow = false
      this.$emit('update:modelValue', this.isShow)
    },
    onFullSceen() {
      this.isFullSceen = !this.isFullSceen
    }
  }
}
</script>

<style></style>
