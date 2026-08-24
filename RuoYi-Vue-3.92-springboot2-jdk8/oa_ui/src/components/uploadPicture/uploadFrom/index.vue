<template>
  <div>
    <el-dialog title="提示" :visible.sync="visible" width="896px" :before-close="handleClose" :modal="false">
      <upload-index v-if="visible" @getImage="getImage" />
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'UploadFroms',
  components: { UploadIndex: () => import('@/components/uploadPicture/index.vue') },
  data() {
    return {
      visible: false,
      callback: function () {}
    }
  },

  methods: {
    handleClose() {
      this.visible = false
    },
    getImage(img) {
      this.callback(img)
      this.visible = false
    }
  }
}
</script>

<style scoped></style>
