<template>
  <XmindEditor v-if="xmindJsonData" ref="editor" :xmindData="xmindJsonData" :fileName="file.file_name" :size="xmindSize"
    @save="save" />
</template>

<script>
import { fileUpload } from "@/api/public";
import XmindEditor from "@/components/xmind-editor/index.vue";
import XmindParse from 'simple-mind-map/src/parse/xmind';

export default {
  name: "editXmind",
  props: {
    url: {
      type: String,
      default: ''
    },
    fid: {
      type: String,
      default: ''
    },
    file: {
      type: Object,
      default: () => ({})
    },
    editBtn: {
      type: Boolean,
      default: false
    }
  },
  components: {
    XmindEditor
  },
  data() {
    return {
      xmindJsonData: null,
      isSaveProcessing: false,
      xmindSize: 0
    }
  },
  mounted() {
    this.getXmindFileBlob();
  },
  methods: {
    async getXmindFileBlob() {
      const response = await fetch(this.$processResourceUrl(this.url));
      const xmindBlob = await response.blob();
      this.xmindSize = xmindBlob.size / 1024;
      this.xmindJsonData = await XmindParse.parseXmindFile(xmindBlob);
    },
    async save() {
      if (this.isSaveProcessing) return
      this.isSaveProcessing = true;
      this.$emit('startLoading')
      try {
        const xmindBlob = await this.$refs.editor.getData();
        const xmindFile = new File([xmindBlob], this.file.file_name, {
          type: 'application/x-xmind'
        })
        
        const formData = new FormData()
        formData.append('content', xmindFile);
        const uploadResponse = await fileUpload(this.fid, this.file.id, formData)
        this.$emit('closeLoading')

      } catch (err) {
        this.$emit('closeLoading')
        console.error(err)
        this.$message.error("保存失败")
      }

      this.isSaveProcessing = false;
    }
  }
}
</script>

<style scoped></style>
