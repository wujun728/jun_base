<template>
  <el-dialog
    :title="caseResultTitle"
    :close-on-click-modal="false"
    :visible.sync="visib"
    width="70%"
    :before-close="closeDialog"
  >
    <vue-json-editor
      v-model="caseResultContent"
      :show-btns="false"
      :mode="'code'"
      lang="zh"
      class="my-editor"
      @json-change="onJsonChange"
      @json-save="onJsonSave"
    />
    <span slot="footer" class="dialog-footer">
      <el-button
        type="primary"
        @click="closeDialog"
      >关闭</el-button
      >
    </span>
  </el-dialog>
</template>
<script>
import vueJsonEditor from 'vue-json-editor'
export default {
  name: 'Support',
  components: { vueJsonEditor },
  props: {
    visib: {
      required: true,
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      dialogCaseResult: false,
      caseResultTitle: '',
      caseResultContent: null,
    }
  },
  // 在生命周期 beforeCreate里面改变this指向
  beforeCreate: function () {},
  mounted() {},
  methods: {

    dataViewPreview(caseResultTitle, caseResultContent){
      this.caseResultTitle = caseResultTitle
      this.caseResultContent = caseResultContent
    },

    // 关闭模态框
    closeDialog() {
      this.$emit('handleClose')
    },
    onJsonChange(value) {},
    onJsonSave(value) {},

  },
}
</script>
