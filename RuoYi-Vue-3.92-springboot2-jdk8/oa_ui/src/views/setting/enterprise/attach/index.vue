<template>
  <div class="divBox">
    <div class="v-height-flag">
      <el-card>
        <div class="v-height-flag">
          <div v-height>
            <uploadPicture :btnShow="false" />
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import uploadPicture from '@/components/uploadPicture/index'
export default {
  name: 'Attach',
  components: {
    uploadPicture
  },
  data() {
    return {}
  },
  computed: {},
  mounted() {},
  methods: {}
}
</script>

<style lang="scss" scoped></style>
