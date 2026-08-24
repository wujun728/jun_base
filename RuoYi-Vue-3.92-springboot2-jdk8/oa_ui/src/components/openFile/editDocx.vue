<template>
  <div>
    <div class="toolbar">
      <Toolbar :defaultConfig="toolbarConfig" :editor="editor" :mode="mode" />
    </div>
    <div class="main">
      <Editor
        v-model="vHtml"
        :defaultConfig="editorConfig"
        :mode="mode"
        style="overflow-y: hidden; margin-top: 20px"
        @onCreated="onCreated"
      />
    </div>
  </div>
</template>

<script>
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { uploader } from '@/utils/uploadCloud'
import mammoth from 'mammoth'
import { asBlob } from 'html-docx-js-typescript'
import { saveAs } from 'file-saver'
import { fileUpload } from '@/api/public'
export default {
  name: 'fileEdit',
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
    }
  },
  components: {
    Editor,
    Toolbar
  },
  data() {
    return {
      vHtml: '',
      editor: null,
      toolbarConfig: {},
      editorConfig: { MENU_CONF: {}, placeholder: '请输入内容...' },
      mode: 'default' // or 'simple'
    }
  },
  created() {
    let that = this
    that.editorConfig.MENU_CONF['uploadImage'] = {
      customUpload(file, insertFn) {
        let options = {
          way: 2,
          relation_type: '',
          relation_id: 0,
          eid: 0
        }
        uploader(file, 1, options).then((res) => {
          insertFn(res.data.url, '图片上传', res.data.url)
        })
      }
    }

    that.toolbarConfig.excludeKeys = ['fullScreen', 'insertVideo', 'uploadVideo', 'group-video']

    // that.toolbarConfig.insertKeys = {
    //   index: 2, // 插入的位置，基于当前的 toolbarKeys
    //   keys: ['menu-key1', 'menu-key2']
    // }
  },
  mounted() {
    const xhr = new XMLHttpRequest()
    xhr.open('get', this.$processResourceUrl(this.url), true)
    xhr.responseType = 'arraybuffer'
    xhr.onload = () => {
      if (xhr.status == 200) {
        mammoth.convertToHtml({ arrayBuffer: new Uint8Array(xhr.response) }).then((resultObject) => {
          this.$nextTick(() => {
            this.vHtml = resultObject.value
          })
        })
      }
    }
    xhr.send()
  },
  methods: {
    onCreated(editor) {
      this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
    },
    //保存word
    wordOption(type) {
      const innerHtml = this.vHtml
        .replace(/<strong>/g, '<b>')
        .replace(/<\/strong>/g, '</b>')
        // 背景色不生效问题
        .replace(/<mark/g, '<span')
        .replace(/<\/mark>/g, '</span>')

      asBlob(innerHtml).then((data) => {
        if (type === 'export') {
          saveAs(data, this.file.name + '.' + this.file.file_ext) // save as docx file
        } else {
          let data = {
            content: innerHtml,
            is_file: 1
          }
          fileUpload(this.fid, this.file.id, data).then((res) => {
            if (res.status === 200) {
              // let path = this.$router.history.current.path
              // this.$router.push({ path, query: { ...this.$route.query, word_url: res.data.file_url } })
            }
            this.$emit('closeLoading')
          })
        }
      })
    },
    beforeDestroy() {
      const editor = this.editor
      if (editor == null) return
      editor.destroy() // 组件销毁时，及时销毁编辑器
    }
  }
}
</script>
<style src="@wangeditor/editor/dist/css/style.css"></style>
<style lang="scss" scoped>
.toolbar {
  position: fixed;
  width: 100%;
  z-index: 99;
}
::v-deep.w-e-bar-show {
  display: flex;
  justify-content: center;
}

.main {
  margin-top: 60px;
  width: 70%;
  margin: 0 auto;
  padding-top: 20px;
}
::v-deep .w-e-text-container {
  margin-top: 30px;
  height: calc(100vh - 150px) !important;
  overflow-y: auto;
  border: none !important;
  padding: 40px 60px;
}
::v-deep .w-e-text-placeholder {
  padding: 40px 60px;
}
</style>
